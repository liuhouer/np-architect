package com.test;

import com.imooc.Application;
import com.imooc.es.pojo.GEO;
import com.imooc.es.pojo.Stu;
import com.imooc.es.pojo.User;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.GeoDistanceAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalGeoDistance;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange.Bucket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESGEOTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    /**
     * 添加坐标点
     */
    @Test
    public void createGEOPoint() {
        User user = new User();
        user.setPlace("棂星门");
        user.setUserId(2001l);
        user.setUserName("Jason Statham");

        GEO geo = new GEO();
        geo.setLon(118.79578);
        geo.setLat(32.026506);

        user.setGeo(geo);

        IndexQuery iq = new IndexQueryBuilder().withObject(user).build();
        esTemplate.index(iq);
    }

        /**
     * 两点构成的矩阵查询
     */
    @Test
    public void geo_bounding_box() {

        PageRequest pageRequest = PageRequest.of(0, 10);
//        PageRequest pageRequest = PageRequest.of(0, 2);

        GeoPoint topLeft = new GeoPoint(32.030249,  118.789703);
        GeoPoint bottomRight = new GeoPoint(32.024341,  118.802171);

        GeoBoundingBoxQueryBuilder geoQueryBuilder = QueryBuilders
                                                        .geoBoundingBoxQuery("geo")
                                                        .setCorners(topLeft, bottomRight);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                            .withQuery(geoQueryBuilder)
                            .withPageable(pageRequest)
                            .build();

        AggregatedPage<User> pagedUser = esTemplate.queryForPage(searchQuery, User.class);
        List<User> userList = pagedUser.getContent();
        for (User u : userList) {
            System.out.println(u);
        }
    }

    /**
     * 自定义形状查询
     */
    @Test
    public void geo_polygon() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        GeoPoint point1 = new GeoPoint(32.029269,  118.798533);
        GeoPoint point2 = new GeoPoint(32.028427,  118.797221);
        GeoPoint point3 = new GeoPoint(32.02555,  118.792748);
        GeoPoint point4 = new GeoPoint(32.025634,  118.799449);

        List<GeoPoint> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);

        GeoPolygonQueryBuilder geoQueryBuilder = QueryBuilders.geoPolygonQuery("geo", points);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(geoQueryBuilder)
                .withPageable(pageRequest)
                .build();

        AggregatedPage<User> pagedUser = esTemplate.queryForPage(searchQuery, User.class);
        List<User> userList = pagedUser.getContent();
        for (User u : userList) {
            System.out.println(u);
        }
    }

    /**
     * 根据当前点的位置查询距离范围内的坐标
     */
    @Test
    public void geo_distance() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        GeoPoint position = new GeoPoint(32.026973,  118.795739);

        GeoDistanceQueryBuilder geoQueryBuilder = QueryBuilders.geoDistanceQuery("geo")
                        .distance("1", DistanceUnit.KILOMETERS)
                        .point(position);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(geoQueryBuilder)
                .withPageable(pageRequest)
                .build();

        AggregatedPage<User> pagedUser = esTemplate.queryForPage(searchQuery, User.class);
        List<User> userList = pagedUser.getContent();
        for (User u : userList) {
            System.out.println(u);
        }
    }

    /**
     * 统计一定范围内坐标点的个数
     */
    @Test
    public void geo_distance_aggs() {

        GeoPoint position = new GeoPoint(32.027042,  118.79549);

        GeoDistanceAggregationBuilder tongjiAggs = AggregationBuilders
                            .geoDistance("tongji", position)
                            .field("geo")
                            .unit(DistanceUnit.KILOMETERS)
                            .distanceType(GeoDistance.PLANE)
                            .addRange(0, 1)
                            .addRange(1, 5)
                            .addRange(5, 100);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .addAggregation(tongjiAggs)
                .build();

        Aggregations aggregations =
                esTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
                    @Override
                    public Aggregations extract(SearchResponse response) {
                        return response.getAggregations();
                    }
                });

        Map aggMap = aggregations.asMap();
        InternalGeoDistance teamAgg = (InternalGeoDistance) aggMap.get("tongji");
        List bucketList = teamAgg.getBuckets();

        for (int i = 0 ; i < bucketList.size() ; i ++) {
            Bucket bucket = (Bucket) bucketList.get(i);
            long docCount = bucket.getDocCount();
            String key = bucket.getKey();

            System.out.println("key: " + key);
            System.out.println("docCount: " + docCount);
        }

    }
}
