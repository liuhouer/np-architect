package com.imooc.controller;

import com.imooc.es.pojo.User;
import com.imooc.es.pojo.search.GeoBoundingBoxEntity;
import com.imooc.utils.IMOOCJSONResult;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("esmap")
public class ESMapController {

	@Autowired
	private ElasticsearchTemplate esTemplate;

	@PostMapping("/hello")
	@ResponseBody
	public Object hello(HttpServletRequest request, HttpServletResponse response) {
		return "Hello SpringBoot~~";
	}

	/**
	 * 矩阵搜索
	 * @param geoBoundingBoxEntity
	 * @return
	 */
	@PostMapping("/geoBoundingBox")
	@ResponseBody
	public IMOOCJSONResult geoBoundingBox(
			@RequestBody GeoBoundingBoxEntity geoBoundingBoxEntity) {

		GeoBoundingBoxQueryBuilder geoQueryBuilder = QueryBuilders
				.geoBoundingBoxQuery("geo")
				.setCorners(geoBoundingBoxEntity.getTopLeft(),
						geoBoundingBoxEntity.getBottomRight());
		List<User> userList = doSearchQuery(geoQueryBuilder);
		return IMOOCJSONResult.ok(userList);
	}

	/**
	 * 自定义区域搜索
	 * @param geoPointList
	 * @return
	 */
	@PostMapping("/geoPolygon")
	@ResponseBody
	public IMOOCJSONResult geoPolygon(
			@RequestBody List<GeoPoint> geoPointList) {

		GeoPolygonQueryBuilder geoQueryBuilder =
				QueryBuilders.geoPolygonQuery("geo",
						geoPointList);
		List<User> userList = doSearchQuery(geoQueryBuilder);
		return IMOOCJSONResult.ok(userList);
	}

	/**
	 * 从当前位置搜索一定范围内的坐标点
	 * @param centerPoint
	 * @param km
	 * @return
	 */
	@PostMapping("/geoDistance")
	@ResponseBody
	public IMOOCJSONResult geoDistance(
			@RequestBody  GeoPoint centerPoint, String km) {

		GeoDistanceQueryBuilder geoQueryBuilder =
				QueryBuilders.geoDistanceQuery("geo")
				.distance(km, DistanceUnit.KILOMETERS)
				.point(centerPoint);
		List<User> userList = doSearchQuery(geoQueryBuilder);
		return IMOOCJSONResult.ok(userList);
	}

	private List<User> doSearchQuery(QueryBuilder builder) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(builder)
				.build();
		AggregatedPage<User> pagedUser = esTemplate.queryForPage(searchQuery, User.class);
		List<User> userList = pagedUser.getContent();
		return userList;
	}

	/**
	 * 添加坐标点
	 * @param user
	 * @return
	 */
	@PostMapping("/addESPoint")
	@ResponseBody
	public IMOOCJSONResult addESPoint(@RequestBody User user) {

		Integer randomId = new Random().nextInt(1000000);
		user.setUserId(Long.valueOf(randomId));

		IndexQuery iq = new IndexQueryBuilder().withObject(user).build();
		esTemplate.index(iq);

		return IMOOCJSONResult.ok();
	}

}
