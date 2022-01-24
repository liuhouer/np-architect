package com.bfxy.collector.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.extern.slf4j.Slf4j;

/**
 * $FastJsonConvertUtil
 * @author hezhuo.bai
 * @since 2019年1月15日 下午4:53:28
 */
@Slf4j
public class FastJsonConvertUtil {

	private static final SerializerFeature[] featuresWithNullValue = { SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse,
	        SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty };

	/**
	 * <B>方法名称：</B>将JSON字符串转换为实体对象<BR>
	 * <B>概要说明：</B>将JSON字符串转换为实体对象<BR>
	 * @author hezhuo.bai
	 * @since 2019年1月15日 下午4:53:49 
	 * @param data JSON字符串
	 * @param clzss 转换对象
	 * @return T
	 */
	public static <T> T convertJSONToObject(String data, Class<T> clzss) {
		try {
			T t = JSON.parseObject(data, clzss);
			return t;
		} catch (Exception e) {
			log.error("convertJSONToObject Exception", e);
			return null;
		}
	}
	
	/**
	 * <B>方法名称：</B>将JSONObject对象转换为实体对象<BR>
	 * <B>概要说明：</B>将JSONObject对象转换为实体对象<BR>
	 * @author hezhuo.bai
	 * @since 2019年1月15日 下午4:54:32
	 * @param data JSONObject对象
	 * @param clzss 转换对象
	 * @return T
	 */
	public static <T> T convertJSONToObject(JSONObject data, Class<T> clzss) {
		try {
			T t = JSONObject.toJavaObject(data, clzss);
			return t;
		} catch (Exception e) {
			log.error("convertJSONToObject Exception", e);
			return null;
		}
	}

	/**
	 * <B>方法名称：</B>将JSON字符串数组转为List集合对象<BR>
	 * <B>概要说明：</B>将JSON字符串数组转为List集合对象<BR>
	 * @author hezhuo.bai
	 * @since 2019年1月15日 下午4:54:50
	 * @param data JSON字符串数组
	 * @param clzss 转换对象
	 * @return List<T>集合对象
	 */
	public static <T> List<T> convertJSONToArray(String data, Class<T> clzss) {
		try {
			List<T> t = JSON.parseArray(data, clzss);
			return t;
		} catch (Exception e) {
			log.error("convertJSONToArray Exception", e);
			return null;
		}
	}

	/**
	 * <B>方法名称：</B>将List<JSONObject>转为List集合对象<BR>
	 * <B>概要说明：</B>将List<JSONObject>转为List集合对象<BR>
	 * @author hezhuo.bai
	 * @since 2019年1月15日 下午4:55:11
	 * @param data List<JSONObject>
	 * @param clzss 转换对象
	 * @return List<T>集合对象
	 */
	public static <T> List<T> convertJSONToArray(List<JSONObject> data, Class<T> clzss) {
		try {
			List<T> t = new ArrayList<T>();
			for (JSONObject jsonObject : data) {
				t.add(convertJSONToObject(jsonObject, clzss));
			}
			return t;
		} catch (Exception e) {
			log.error("convertJSONToArray Exception", e);
			return null;
		}
	}

	/**
	 * <B>方法名称：</B>将对象转为JSON字符串<BR>
	 * <B>概要说明：</B>将对象转为JSON字符串<BR>
	 * @author hezhuo.bai
	 * @since 2019年1月15日 下午4:55:41
	 * @param obj 任意对象
	 * @return JSON字符串
	 */
	public static String convertObjectToJSON(Object obj) {
		try {
			String text = JSON.toJSONString(obj);
			return text;
		} catch (Exception e) {
			log.error("convertObjectToJSON Exception", e);
			return null;
		}
	}
	
	/**
	 * <B>方法名称：</B>将对象转为JSONObject对象<BR>
	 * <B>概要说明：</B>将对象转为JSONObject对象<BR>
	 * @author hezhuo.bai
	 * @since 2019年1月15日 下午4:55:55
	 * @param obj 任意对象
	 * @return JSONObject对象
	 */
	public static JSONObject convertObjectToJSONObject(Object obj){
		try {
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
			return jsonObject;
		} catch (Exception e) {
			log.error("convertObjectToJSONObject Exception", e);
			return null;
		}		
	}

	public static String convertObjectToJSONWithNullValue(Object obj) {
		try {
			String text = JSON.toJSONString(obj, featuresWithNullValue);
			return text;
		} catch (Exception e) {
			log.error("convertObjectToJSONWithNullValue Exception", e);
			return null;
		}
	}
}
