package com.bfxy.rabbit.common.serializer.impl;

import com.bfxy.rabbit.api.Message;
import com.bfxy.rabbit.common.serializer.Serializer;
import com.bfxy.rabbit.common.serializer.SerializerFactory;

public class JacksonSerializerFactory implements SerializerFactory{

	public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();
	
	@Override
	public Serializer create() {
		return JacksonSerializer.createParametricType(Message.class);
	}

}
