package com.smartalgorithms.getit.Models.Local;

import com.google.gson.Gson;
import com.smartalgorithms.getit.Helpers.GeneralHelper;

import java.util.List;

/**
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2017/12/06.
 * Updated by Ndivhuwo Nthambeleni on 2017/12/06.
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
        String name = "";
        int checkins = 0;
        String about = "";
        String description = "";
        FBlocation location;
        String phone = "";
        String link = "";
        FacebookPictureResponse picture;
        FBPhoto photos;

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

        public FacebookPictureResponse getPicture() {
            return picture;
        }

        public FBPhoto getPhotos() {
            return photos;
        }

        public class FBlocation{
            float latitude = 0;
            float longitude = 0;
            String name = "";

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

        //TODO store Photo Data list in placeInfo object.
        public class FBPhoto {
            List<PhotoData> data;



            public List<PhotoData> getData() {
                return data;
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


}
