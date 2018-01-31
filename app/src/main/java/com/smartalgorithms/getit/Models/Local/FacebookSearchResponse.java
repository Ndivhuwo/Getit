package com.smartalgorithms.getit.Models.Local;

import com.google.gson.Gson;
import com.smartalgorithms.getit.Helpers.GeneralHelper;

import java.util.List;

/**
 * Copyright (c) 2017 Smart Algorithms (PTY) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/01/10.
 * Updated by Ndivhuwo Nthambeleni on 2018/01/10.
 */

public class FacebookSearchResponse extends NetworkResponse{
    private List<FacebookData> data;
    private Pager paging;

    public List<FacebookData> getData() {
        return data;
    }

    public Pager getPaging() {
        return paging;
    }

    public FacebookSearchResponse fromJson(String s) {
        return (FacebookSearchResponse) GeneralHelper.objectFromJson(s, FacebookSearchResponse.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public class FacebookData{
        String name;
        int checkins;
        String about;
        String description;
        FBlocation location;
        String phone;
        String link;
        FBPicture picture;

        public String getName() {
            return name;
        }

        public int getCheckins() {
            return checkins;
        }

        public String getAbout() {
            return about;
        }

        public String getDescription() {
            return description;
        }

        public FBlocation getLocation() {
            return location;
        }

        public String getPhone() {
            return phone;
        }

        public String getLink() {
            return link;
        }

        public FBPicture getPicture() {
            return picture;
        }

        public class FBlocation{
            float latitude;
            float longitude;
            String name;

            public float getLatitude() {
                return latitude;
            }

            public float getLongitude() {
                return longitude;
            }

            public String getName() {
                return name;
            }
        }
    }

    public class Pager {
        FBCursor cursors;
        String previous;
        String next;

        public class FBCursor{
            String after;
            String before;

            public String getAfter() {
                return after;
            }

            public String getBefore() {
                return before;
            }
        }

        public FBCursor getCursors() {
            return cursors;
        }

        public String getPrevious() {
            return previous;
        }

        public String getNext() {
            return next;
        }
    }

    public class FBPicture {
        PictureData data;

        public PictureData getData() {
            return data;
        }

        public class PictureData {
            boolean is_silhouette;
            String url;

            public boolean isIs_silhouette() {
                return is_silhouette;
            }

            public String getUrl() {
                return url;
            }
        }
    }
}
