package com.imooc.item.mapper;

import com.imooc.item.pojo.ItemsComments;
import com.imooc.item.pojo.vo.MyCommentVO;
import com.imooc.my.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    public void saveComments(Map<String, Object> map);

    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}