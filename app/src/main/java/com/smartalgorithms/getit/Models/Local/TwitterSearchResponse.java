package com.smartalgorithms.getit.Models.Local;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.smartalgorithms.getit.Helpers.GeneralHelper;

import java.util.List;

/**
 * Copyright (c) 2017 Smart Algorithms (PTY) Ltd. All rights reserved
 * Contact info@smartalg.co.za
 * Created by Ndivhuwo Nthambeleni on 2018/01/21.
 * Updated by Ndivhuwo Nthambeleni on 2018/01/21.
 */

public class TwitterSearchResponse extends NetworkResponse{
    List<Tweet> statuses;
    TWMetaData search_metadata;

    public List<Tweet> getStatuses() {
        return statuses;
    }

    public TwitterSearchResponse fromJson(String s) {
        return (TwitterSearchResponse) GeneralHelper.objectFromJson(s, TwitterSearchResponse.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public class Tweet {
        Geo geo;
        String created_at;
        TWEntity entities;
        String text;

        public Geo getGeo() {
            return geo;
        }

        public String getCreated_at() {
            return created_at;
        }

        public TWEntity getEntities() {
            return entities;
        }

        public String getText() {
            return text;
        }

        public class Geo{
            String type;
            double [] coordinates = new double[2];

            public String getType() {
                return type;
            }

            public double[] getCoordinates() {
                return coordinates;
            }
        }
        public class TWEntity{
            List<HashTag> hashtags;

            public List<HashTag> getHashtags() {
                return hashtags;
            }

            public class HashTag{
                String text;

                public String getText() {
                    return text;
                }
            }
        }

    }

    public class TWMetaData{
        String refresh_url;
        String next_results;
        int count;
        double completed_in;
    }
}
