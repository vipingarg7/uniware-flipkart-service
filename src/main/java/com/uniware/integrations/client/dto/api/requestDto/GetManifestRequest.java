package com.uniware.integrations.client.dto.api.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.joda.time.DateTime;

public class GetManifestRequest {
    @JsonProperty("params")
    private Params params = null;

    private GetManifestRequest(Builder builder) {
        setParams(builder.params);
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GetManifestRequest {\n");

        sb.append("    params: ").append(toIndentedString(params)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    public static class Params {
        @JsonProperty("pickupDate")
        private PickUpDate pickupDate = null;

        @JsonProperty("locationId")
        private String locationId = null;

        @JsonProperty("is_mps")
        private Boolean isMps = null;

        @JsonProperty("vendorGroupCode")
        private String vendorGroupCode = null;

        private Params(Builder builder) {
            setPickupDate(builder.pickupDate);
            setLocationId(builder.locationId);
            setIsMps(builder.isMps);
            setVendorGroupCode(builder.vendorGroupCode);
        }

        public PickUpDate getPickupDate() {
            return pickupDate;
        }

        public void setPickupDate(PickUpDate pickupDate) {
            this.pickupDate = pickupDate;
        }

        public Params locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public Params isMps(Boolean isMps) {
            this.isMps = isMps;
            return this;
        }

        public Boolean isIsMps() {
            return isMps;
        }

        public void setIsMps(Boolean isMps) {
            this.isMps = isMps;
        }

        public Params vendorGroupCode(String vendorGroupCode) {
            this.vendorGroupCode = vendorGroupCode;
            return this;
        }

        public String getVendorGroupCode() {
            return vendorGroupCode;
        }

        public void setVendorGroupCode(String vendorGroupCode) {
            this.vendorGroupCode = vendorGroupCode;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class Params {\n");

            sb.append("    pickupDate: ").append(toIndentedString(pickupDate)).append("\n");
            sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
            sb.append("    isMps: ").append(toIndentedString(isMps)).append("\n");
            sb.append("    vendorGroupCode: ").append(toIndentedString(vendorGroupCode)).append("\n");
            sb.append("}");
            return sb.toString();
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private String toIndentedString(Object o) {
            if (o == null) {
                return "null";
            }
            return o.toString().replace("\n", "\n    ");
        }

        public static final class Builder {
            private PickUpDate pickupDate;
            private String locationId;
            private Boolean isMps;
            private String vendorGroupCode;

            public Builder() {
            }

            public Builder setPickupDate(PickUpDate val) {
                pickupDate = val;
                return this;
            }

            public Builder setLocationId(String val) {
                locationId = val;
                return this;
            }

            public Builder setIsMps(Boolean val) {
                isMps = val;
                return this;
            }

            public Builder setVendorGroupCode(String val) {
                vendorGroupCode = val;
                return this;
            }

            public Params build() {
                return new Params(this);
            }
        }
    }

    public static class PickUpDate {
        @JsonProperty("to")
        private DateTime to = null;

        @JsonProperty("from")
        private DateTime from = null;

        private PickUpDate(Builder builder) {
            setTo(builder.to);
            setFrom(builder.from);
        }

        public PickUpDate to(DateTime to) {
            this.to = to;
            return this;
        }

        public DateTime getTo() {
            return to;
        }

        public void setTo(DateTime to) {
            this.to = to;
        }

        public PickUpDate from(DateTime from) {
            this.from = from;
            return this;
        }

        public DateTime getFrom() {
            return from;
        }

        public void setFrom(DateTime from) {
            this.from = from;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PickUpDate pickUpDate = (PickUpDate) o;
            return Objects.equals(this.to, pickUpDate.to) &&
                    Objects.equals(this.from, pickUpDate.from);
        }

        @Override
        public int hashCode() {
            return Objects.hash(to, from);
        }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class PickUpDate {\n");

            sb.append("    to: ").append(toIndentedString(to)).append("\n");
            sb.append("    from: ").append(toIndentedString(from)).append("\n");
            sb.append("}");
            return sb.toString();
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private String toIndentedString(Object o) {
            if (o == null) {
                return "null";
            }
            return o.toString().replace("\n", "\n    ");
        }

        public static final class Builder {
            private DateTime to;
            private DateTime from;

            public Builder() {
            }

            public Builder setTo(DateTime val) {
                to = val;
                return this;
            }

            public Builder setFrom(DateTime val) {
                from = val;
                return this;
            }

            public PickUpDate build() {
                return new PickUpDate(this);
            }
        }
    }

    public static final class Builder {
        private Params params;

        public Builder() {
        }

        public Builder setParams(Params val) {
            params = val;
            return this;
        }

        public GetManifestRequest build() {
            return new GetManifestRequest(this);
        }
    }
}
