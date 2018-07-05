package com.smartalgorithms.getit.Models.Local;

import com.google.gson.Gson;
import com.smartalgorithms.getit.Helpers.GeneralHelper;

import java.util.List;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/06/25.
 * Updated by Ndivhuwo Nthambeleni on 2018/06/25.
 */

public class GoogleSearchResponse extends NetworkResponse {
    private List<GooglePlace> results;

    public List<GooglePlace> getResults() {
        return results;
    }

    public GoogleSearchResponse fromJson(String s) {
        return (GoogleSearchResponse) GeneralHelper.getObjectFromJson(s, GoogleSearchResponse.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public class GooglePlace {
        private Geometry geometry;
        private String icon;
        private String name;
        private List<GooglePlacePhoto> photos;
        private List<String> types;
        private String vicinity;
        private String website;

        public Geometry getGeometry() {
            return geometry;
        }

        public String getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }

        public List<GooglePlacePhoto> getPhotos() {
            return photos;
        }

        public List<String> getTypes() {
            return types;
        }

        public String getVicinity() {
            return vicinity;
        }

        public String getWebsite() {
            return website;
        }

        public class Geometry {
            private Loc location;

            public Loc getLocation() {
                return location;
            }

            public class Loc {
                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }
        }

        public class GooglePlacePhoto {
            private int height;
            private int width;
            private String photo_reference;

            public int getHeight() {
                return height;
            }

            public int getWidth() {
                return width;
            }

            public String getPhoto_reference() {
                return photo_reference;
            }
        }
    }
}
