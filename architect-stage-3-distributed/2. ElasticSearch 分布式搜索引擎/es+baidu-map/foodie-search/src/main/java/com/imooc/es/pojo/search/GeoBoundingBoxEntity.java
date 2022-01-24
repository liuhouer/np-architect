package com.imooc.es.pojo.search;

import org.elasticsearch.common.geo.GeoPoint;

public class GeoBoundingBoxEntity {

    private GeoPoint topLeft;
    private GeoPoint bottomRight;

    public GeoPoint getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(GeoPoint topLeft) {
        this.topLeft = topLeft;
    }

    public GeoPoint getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(GeoPoint bottomRight) {
        this.bottomRight = bottomRight;
    }

    @Override
    public String toString() {
        return "GeoBoundingBoxEntity{" +
                "topLeft=" + topLeft +
                ", bottomRight=" + bottomRight +
                '}';
    }
}
