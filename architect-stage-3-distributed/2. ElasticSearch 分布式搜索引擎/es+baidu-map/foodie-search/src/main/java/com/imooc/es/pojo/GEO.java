package com.imooc.es.pojo;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "geo", type = "_doc", createIndex = false)
public class GEO {

    private double lon;
    private double lat;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "GEO{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
