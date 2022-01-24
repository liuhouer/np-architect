package com.bfxy.rabbit.producer.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.bfxy.rabbit.producer.entity.BrokerMessage;

@Mapper
public interface BrokerMessageMapper {
	
    int deleteByPrimaryKey(String messageId);
    
    int insert(BrokerMessage record);

    int insertSelective(BrokerMessage record);

    BrokerMessage selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessage record);

    int updateByPrimaryKeyWithBLOBs(BrokerMessage record);

    int updateByPrimaryKey(BrokerMessage record);
	
	void changeBrokerMessageStatus(@Param("brokerMessageId")String brokerMessageId, @Param("brokerMessageStatus")String brokerMessageStatus, @Param("updateTime")Date updateTime);

	List<BrokerMessage> queryBrokerMessageStatus4Timeout(@Param("brokerMessageStatus")String brokerMessageStatus);
	
	List<BrokerMessage> queryBrokerMessageStatus(@Param("brokerMessageStatus")String brokerMessageStatus);
	
	int update4TryCount(@Param("brokerMessageId")String brokerMessageId, @Param("updateTime")Date updateTime);
	
}