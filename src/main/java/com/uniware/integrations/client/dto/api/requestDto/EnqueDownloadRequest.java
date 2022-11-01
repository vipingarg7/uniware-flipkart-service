package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseRequest;
import com.uniware.integrations.client.dto.PackRequest;
import java.util.ArrayList;
import java.util.List;

public class EnqueDownloadRequest extends BaseRequest {

    @SerializedName("state")
    private String state;

    @SerializedName("refiners")
    private Refiner refiner;

    @SerializedName("verticalGroups")
    private VerticalGroup verticalGroup;

    private EnqueDownloadRequest(Builder builder) {
        setState(builder.state);
        setRefiner(builder.refiner);
        setVerticalGroup(builder.verticalGroup);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Refiner getRefiner() {
        return refiner;
    }

    public void setRefiner(Refiner refiner) {
        this.refiner = refiner;
    }

    public VerticalGroup getVerticalGroup() {
        return verticalGroup;
    }

    public void setVerticalGroup(VerticalGroup verticalGroup)
    {
        this.verticalGroup = verticalGroup;
    }

    public static class Refiner {

        @SerializedName("internal_state")
        List<InternalState> internalStateList;

        public Refiner() {}
        private Refiner(Builder builder) {
            setInternalStateList(builder.internalStateList);
        }

        public Refiner addInternalState(InternalState internalState) {
            if (this.internalStateList == null) {
                this.internalStateList = new ArrayList<>();
            }
            this.internalStateList.add(internalState);
            return this;
        }

        public List<InternalState> getInternalStateList() {
            return internalStateList;
        }

        public void setInternalStateList(List<InternalState> internalStateList)
        {
            this.internalStateList = internalStateList;
        }

        public static final class Builder {
            private List<InternalState> internalStateList;

            public Builder() {
            }

            public Builder setInternalStateList(List<InternalState> val) {
                internalStateList = val;
                return this;
            }

            public Refiner build() {
                return new Refiner(this);
            }
        }
    }

    public static class InternalState {

        @SerializedName("exactValue")
        private ExactValue exactValue;

        @SerializedName("valueType")
        private String valueType;

        private InternalState(Builder builder) {
            setExactValue(builder.exactValue);
            setValueType(builder.valueType);
        }

        public ExactValue getExactValue() {
            return exactValue;
        }

        public void setExactValue(ExactValue exactValue) {
            this.exactValue = exactValue;
        }

        public String getValueType() {
            return valueType;
        }

        public void setValueType(String valueType) {
            this.valueType = valueType;
        }

        public static final class Builder {
            private ExactValue exactValue;
            private String valueType;

            public Builder() {
            }

            public Builder setExactValue(ExactValue val) {
                exactValue = val;
                return this;
            }

            public Builder setValueType(String val) {
                valueType = val;
                return this;
            }

            public InternalState build() {
                return new InternalState(this);
            }
        }
    }

    public static class ExactValue {

        @SerializedName("value")
        private String value;

        private ExactValue(Builder builder) {
            setValue(builder.value);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static final class Builder {
            private String value;

            public Builder() {
            }

            public Builder setValue(String val) {
                value = val;
                return this;
            }

            public ExactValue build() {
                return new ExactValue(this);
            }
        }
    }

    public static class VerticalGroup {

    }

    public static final class Builder {
        private String state;
        private Refiner refiner;
        private VerticalGroup verticalGroup;

        public Builder() {
        }

        public Builder setState(String val) {
            state = val;
            return this;
        }

        public Builder setRefiner(Refiner val) {
            refiner = val;
            return this;
        }

        public Builder setVerticalGroup(VerticalGroup val) {
            verticalGroup = val;
            return this;
        }

        public EnqueDownloadRequest build() {
            return new EnqueDownloadRequest(this);
        }
    }
}
