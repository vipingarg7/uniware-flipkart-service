package com.uniware.integrations.client.dto.api.responseDto;

import com.google.gson.annotations.SerializedName;
import com.uniware.integrations.client.dto.BaseResponse;
import java.util.List;

public class LocationDetailsResponse extends BaseResponse {

    @SerializedName("locations")
    private List<Location> locations;

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Override public String toString() {
        return "LocationDetailsResponse{" + "locations=" + locations + '}';
    }

    public static class Location {
        @SerializedName("locationId")
        private String locationId;
        @SerializedName("locationName")
        private String locationName;

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        @Override public String toString() {
            return "Location{" + "locationId='" + locationId + '\'' + ", locationName='" + locationName + '\'' + '}';
        }
    }

}
