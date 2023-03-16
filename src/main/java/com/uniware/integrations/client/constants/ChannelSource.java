package com.uniware.integrations.client.constants;

/**
 * Created by admin on 20/05/22.
 */
public enum ChannelSource {

    FLIPKART_DROPSHIP("FLIPKART_DROPSHIP"),
    FLIPKART_OMNI("FLIPKART_OMNI"),
    FLIPKART_WHOLESALE("FLIPKART_WHOLESALE"),
    FLIPKART_LITE("FLIPKART_LITE"),
    FLIPKART_B2B("FLIPKART_B2B"),
    FLIPKART_FA("FLIPKART_FA"),
    FLIPKART_SMART("FLIPKART_SMART");

    private String channelSourceCode;

    ChannelSource(String channelSourceCode) {
        this.channelSourceCode = channelSourceCode;
    }

    public String getChannelSourceCode() {
        return channelSourceCode;
    }

    public static ChannelSource findByChannelSourceCode(String value) {
        ChannelSource result = null;
        for (ChannelSource channelSource : values()) {
            if (channelSource.getChannelSourceCode().equalsIgnoreCase(value)) {
                result = channelSource;
                break;
            }
        }
        return result;
    }

}
