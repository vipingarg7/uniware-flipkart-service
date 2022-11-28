package com.uniware.integrations.client.utils;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import com.unifier.core.fileparser.Row;
import com.unifier.core.fileparser.XlsxExcelSheetParser;
import com.unifier.core.utils.CollectionUtils;
import com.unifier.core.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class FKXlsxExcelSheetParser {

    private static final Logger LOG = LoggerFactory.getLogger(FKXlsxExcelSheetParser.class);

    private final File file;

    public FKXlsxExcelSheetParser(File file) {
        this.file = file;
    }

    public FKXlsxExcelSheetParser(String filePath) {
        if(StringUtils.isBlank(filePath)) {
            throw new IllegalArgumentException("FilePath is blank");
        } else if (!filePath.endsWith(".xlsx")) {
            throw new RuntimeException("Invalid file type, .xlsx is expected: " + filePath);
        }
        this.file = new File(filePath);
        if (!this.file.exists()) {
            throw new IllegalArgumentException("No such file exists at path: " + filePath);
        }
    }

    /**
     *
     * @param sheetName(sheet to parse in excel workbook)
     * @param skipInitialLines(no of line to skip while parsing)
     * @return SheetIterator, which helps to iterate over each and every row and cell.
     */
    public Iterator<Row> processXLSXFile(String sheetName, int skipInitialLines) {
        try {
            long startTimeInMillis = System.currentTimeMillis();
            OPCPackage pkg = OPCPackage.open(file, PackageAccess.READ);
            XSSFReader xssfReader = new XSSFReader(pkg);

            FKXlsxExcelSheetParser.SheetDataHandler handler = new FKXlsxExcelSheetParser.SheetDataHandler(xssfReader.getSharedStringsTable(), xssfReader.getStylesTable());
            XMLReader xmlParser = fetchSheetParser(handler);
            Iterator<InputStream> sheetIterator = xssfReader.getSheetsData();

            InputStream sheetInputStream = findSheetInputStream(sheetName, sheetIterator);
            if (sheetInputStream != null) {
                xmlParser.parse(new InputSource(sheetInputStream));
                sheetInputStream.close();
                FKXlsxExcelSheetParser.SheetIterator xlsxSheetIterator = new FKXlsxExcelSheetParser.SheetIterator(true, skipInitialLines, handler.getRowCache());
                LOG.info("Xlsx file: " + file.getName() + " parsed successfully with new framework, of size(bytes): " + file
                        .length() + ", contains: " + xlsxSheetIterator.getNoOfLine() + " rows, taken(ms): " + (System.currentTimeMillis() - startTimeInMillis));
                return xlsxSheetIterator;
            } else {
                throw new RuntimeException("No matching excel sheet found by name: " + sheetName + " in file: " + file.getName());
            }
        } catch (OpenXML4JException | IOException | SAXException e) {
            LOG.error("Error while parsing xlsx file: " + file.getName() + ", size: " + file.length() + ", exception: " + e);
            throw new RuntimeException("Error while parsing xlsx file, exception:" + e.getMessage());
        }
    }

    /**
     *
     * @param sheetName(sheet name that need to parse from workbook. Currently, parsing only single sheet.
     * If input sheet not present parse very first sheet, otherwise sheet with same name)
     * @param sheetIterator(XSSFReader's default excel sheet iterator)
     * @return InputStream(Matching sheet inputstream with input sheetName)
     */
    private InputStream findSheetInputStream(String sheetName, Iterator<InputStream> sheetIterator) {
        if (StringUtils.isBlank(sheetName)) {
            return sheetIterator.next();
        }
        if (sheetIterator instanceof XSSFReader.SheetIterator) {
            InputStream sheetInputStream;
            while (sheetIterator.hasNext()) {
                sheetInputStream = sheetIterator.next();
                if (sheetName.equalsIgnoreCase(((XSSFReader.SheetIterator) sheetIterator).getSheetName())) {
                    return sheetInputStream;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param handler(SheetDataHandler as input, where XML events action defined)
     * @return XMLReader(XML parser)
     * @throws SAXException
     */
    private XMLReader fetchSheetParser(ContentHandler handler) throws SAXException {
        XMLReader parser = new SAXParser();
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * This class contains logic to parse XML data, where for every start and end of an element(tag)
     * methods receive notifications and take suitable action on it.
     * Member Variables -
     * sst - SharedString table contains in OPCPackage class(contains underlying structure of xlsx file), which is read by XSSFReader.
     * lastContents - contains data inside XML tag.
     * nextIsString - identifier used to map string index with actual string defined in shared string table.
     * inlineStr - identifier if string is expressed directly in the cell definition instead of implementing the shared string table.
     * cellCache - contains cell value list for current rows.
     * rowCache - contains row value for each and every excel rows.
     */
    private static class SheetDataHandler extends DefaultHandler {

        private SharedStringsTable sst;
        private StringBuffer 		lastContents;
        private boolean 			nextIsString;
        private boolean 			inlineStr;
        private List<String> cellCache = new LinkedList<>();
        private List<String[]>		rowCache = new LinkedList<>();
        private String 				styleSequenceNo;
        private StylesTable stylesTable;

        private SheetDataHandler(SharedStringsTable sst, StylesTable stylesTable) {
            this.sst = sst;
            this.stylesTable = stylesTable;
        }

        /**
         * Events defined for notifications received of the start of an element.
         * ROW_EVENT - for every excel row start element
         * CELL_EVENT - for every excel cell start element of all the rows:
         * attribute(t) value=="s" <==> (nextIsString = true) - denotes cell contains a reference to shared string
         * attribute(t) value=="inlineStr" <==> (inlineStr = true) - denotes cell contains actual string
         * attribute(s) value=An integer value denotes style sequence no in StyleTable.xfs
         */
        public void startElement(String uri, String localName, String name, Attributes attributes) {
            FKXlsxExcelSheetParser.SheetDataHandler.Event event = FKXlsxExcelSheetParser.SheetDataHandler.Event.getInstance(name);
            lastContents = new StringBuffer();
            if (event == null) {
                return;
            }
            switch (event) {
            case ROW_EVENT: {
                cellCache.clear();
            }
            break;
            case CELL_EVENT: {
                String cellDataType = attributes.getValue(FKXlsxExcelSheetParser.SheetDataHandler.CellAttribute.CELL_DATA_TYPE.getAttr());
                styleSequenceNo = attributes.getValue(FKXlsxExcelSheetParser.SheetDataHandler.CellAttribute.CELL_STYLE.getAttr());
                nextIsString = FKXlsxExcelSheetParser.SheetDataHandler.CellDataType.SHARED_STRING.getType().equalsIgnoreCase(cellDataType);
                inlineStr = FKXlsxExcelSheetParser.SheetDataHandler.CellDataType.INLINE_STRING.getType().equalsIgnoreCase(cellDataType);
            }
            break;
            default:
            }
        }

        /**
         * Events defined for notifications received of the end of an element.
         *
         * It collects value defined in every cell, if it is not an string(nextIsString = false)
         * lastContents contains actual value of cell,
         * otherwise either actual string is directly defined in cell(inlineStr = true) or
         * it needs to resolve through defined shared string table.
         *
         * With every cell there is defined style number(styleSequenceNo), which is a reference
         * to a cellstyle set defined in StyleTables.xfs. CellStyle contains numberformatId,
         * which is again an reference to StyleTables.numberFormats.
         * Finally StyleTables.numberFormats contains numberFormatCode(expected representation of cell value)
         * It adds collected cell values into row value list.
         */
        public void endElement(String uri, String localName, String name) {
            if (nextIsString) {
                lastContents = new StringBuffer(new XSSFRichTextString(sst.getEntryAt(Integer.parseInt(lastContents.toString()))).toString());
                nextIsString = false;
            } else {
                //Need to format cell value only for numeric cell value(nextIsString = false || inlineStr = false)
                if(StringUtils.isNotBlank(lastContents.toString()) && StringUtils.isNotBlank(styleSequenceNo) && !inlineStr) {
                    long numFormatId = stylesTable.getCellXfAt(Integer.parseInt(styleSequenceNo)).getNumFmtId();
                    int intNumFmtId = (int)numFormatId;
                    String numberFormatCode = stylesTable.getNumberFormatAt(intNumFmtId);
                    lastContents = getActualCellValue(intNumFmtId, numberFormatCode);
                }
            }

            if (FKXlsxExcelSheetParser.SheetDataHandler.Event.CELL_VALUE_EVENT.getEvent().equalsIgnoreCase(name) ||
                    (FKXlsxExcelSheetParser.SheetDataHandler.Event.CELL_EVENT.getEvent().equalsIgnoreCase(name) && (StringUtils.isBlank(lastContents.toString()) || inlineStr))) {
                cellCache.add(lastContents.toString());
                // clearing styleSequenceNo for next cell's style no
                styleSequenceNo = null;
            }
            if (FKXlsxExcelSheetParser.SheetDataHandler.Event.ROW_EVENT.getEvent().equalsIgnoreCase(name)) {
                if (!cellCache.isEmpty()) {
                    rowCache.add(CollectionUtils.convertListIntoArray(cellCache, String.class));
                }
                cellCache.clear();
            }
        }

        /**
         *
         * @param numberFormatId (StyleTables.numberFormats contains formatId as key)
         * @param numberFormatCode (StyleTables.numberFormats contains formatCode as value)(expected representation of cell value)
         * numberFormatCode applicable only for numeric cell value, not for String.
         *
         * @class BuiltinFormats - contains number format predefined by Apache POI library.
         * If numberFormatCode is blank for a formatId(not defined in StyleTables.numberFormats),
         * then it must be defined in BuiltinFormats._formats.
         * @return Formatted string with @numberFormatCode
         */
        private StringBuffer getActualCellValue(int numberFormatId, String numberFormatCode) {
            if(StringUtils.isBlank(numberFormatCode)) {
                numberFormatCode = BuiltinFormats.getBuiltinFormat(numberFormatId);
            }
            if(numberFormatId > 0 && StringUtils.isNotBlank(numberFormatCode)) {
                double doubleVal = Double.parseDouble(lastContents.toString());
                if (DateUtil.isValidExcelDate(doubleVal)) {
                    boolean isDate = DateUtil.isADateFormat(numberFormatId, numberFormatCode);
                    if(isDate) {
                        DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                        return new StringBuffer(sdf.format(DateUtil.getJavaDate(doubleVal, false)));
                    }
                }
            }
            return lastContents;
        }

        /**
         * It read character defined in XML sheet data for specified range and store it in lastContents.
         */
        public void characters(char[] ch, int start, int length) throws SAXException {
            lastContents.append(new String(ch, start, length));
        }

        public List<String[]> getRowCache() {
            return rowCache;
        }

        /**
         * Enum for XML tags defined in xml sheet data.
         * row => complete excel row data defined in this tag
         * c => define excel cell value including it's attribute and content.
         * v => contains defined value inside cell tag.
         */
        enum Event {
            ROW_EVENT("row"), CELL_EVENT("c"), CELL_VALUE_EVENT("v");

            public String event;

            Event(String event) {
                this.event = event;
            }

            public static FKXlsxExcelSheetParser.SheetDataHandler.Event getInstance(String event) {
                for (FKXlsxExcelSheetParser.SheetDataHandler.Event e : FKXlsxExcelSheetParser.SheetDataHandler.Event.values()) {
                    if (e.getEvent().equalsIgnoreCase(event)) {
                        return e;
                    }
                }
                return null;
            }

            public String getEvent() {
                return event;
            }
        }

        /**
         * Enum for attributes defined for every start xml tags.
         * t => defines cell data type, possibles values defined in CellDataType enum
         * s => defines cell style(including font, color, cell format etc)
         * r => defines excel cell number
         */
        enum CellAttribute {
            CELL_DATA_TYPE("t"), CELL_STYLE("s"), CELL_NUMBER("r");

            public String attr;

            CellAttribute(String attr) {
                this.attr = attr;
            }

            public static FKXlsxExcelSheetParser.SheetDataHandler.CellAttribute getInstance(String attr) {
                for (FKXlsxExcelSheetParser.SheetDataHandler.CellAttribute cellAttr : FKXlsxExcelSheetParser.SheetDataHandler.CellAttribute.values()) {
                    if (cellAttr.getAttr().equalsIgnoreCase(attr)) {
                        return cellAttr;
                    }
                }
                return null;
            }

            public String getAttr() {
                return attr;
            }
        }

        /**
         * Enum for possible cell data type.
         * s => String that defined in shared string table.
         * inlineStr => String that doesn't use the shared string table(actual string defined)
         * n => Number
         * b => Boolean
         * d => Date
         */
        enum CellDataType {
            SHARED_STRING("s"),
            INLINE_STRING("inlineStr"),
            NUMBER("n"),
            BOOLEAN("b"),
            DATE("d");

            public String valueType;

            CellDataType(String valueType) {
                this.valueType = valueType;
            }

            public static FKXlsxExcelSheetParser.SheetDataHandler.CellDataType getInstance(String value) {
                for (FKXlsxExcelSheetParser.SheetDataHandler.CellDataType type : FKXlsxExcelSheetParser.SheetDataHandler.CellDataType.values()) {
                    if (type.getType().equalsIgnoreCase(value)) {
                        return type;
                    }
                }
                return null;
            }

            public String getType() {
                return valueType;
            }
        }
    }

    /**
     * Defined custom iterator to iterate over excel rows.
     */
    public static class SheetIterator implements Iterator<Row> {

        private int 					lineNo;
        private Map<String, Integer> columnNamesToIndex;
        private String[] 				columnNames;
        public List<String[]> 			rows;

        public SheetIterator(boolean containsHeader, int skipInitialLines, List<String[]> rows) {
            this.rows = rows;
            if (containsHeader) {
                columnNames = getColumnValues(rows.get(lineNo++));
                columnNamesToIndex = new HashMap<>(columnNames.length);
                for (int i = 0; i < columnNames.length; i++) {
                    columnNamesToIndex.put(StringUtils.removeNonWordChars(columnNames[i]).toLowerCase(), i);
                }
            }
            if (skipInitialLines > 0) {
                skip(skipInitialLines);
            }
        }

        private String[] getColumnValues(String[] colValues) {
            if (colValues == null || colValues.length == 0) {
                return new String[0];
            } else {
                return colValues;
            }
        }

        @Override
        public boolean hasNext() {
            return lineNo < rows.size();
        }

        public String getColumnNames() {
            StringBuilder builder = new StringBuilder();
            for (String s : columnNames) {
                builder.append(StringEscapeUtils.escapeCsv(s)).append(',');
            }
            return builder.deleteCharAt(builder.length() - 1).toString();
        }

        @Override
        public Row next() {
            if (hasNext()) {
                String[] columnValues = getColumnValues(rows.get(lineNo++));
                if (columnNamesToIndex != null) {
                    return new Row(lineNo, columnValues, columnNamesToIndex);
                } else {
                    return new Row(lineNo, columnValues);
                }
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void skip(int noOfLines) {
            while (noOfLines-- > 0 && hasNext()) {
                lineNo++;
            }
        }

        public int getNoOfLine() {
            return rows.size();
        }
    }
}
