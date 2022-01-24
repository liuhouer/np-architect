package com.imooc.order.fallback.itemservice;

import com.google.common.collect.Lists;
import com.imooc.item.pojo.vo.MyCommentVO;
import com.imooc.pojo.PagedGridResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by 半仙.
 */
@Component
public class ItemCommentsFallbackFactory implements FallbackFactory<ItemCommentsFeignClient> {

    @Override
    public ItemCommentsFeignClient create(Throwable throwable) {
        return new ItemCommentsFeignClient() {

            @Override
            public PagedGridResult queryMyComments(String userId, Integer page,  Integer pageSize) {
                MyCommentVO commentVO = new MyCommentVO();
                commentVO.setContent("正在加载中");

                PagedGridResult result = new PagedGridResult();
                result.setRows(Lists.newArrayList(commentVO));
                result.setTotal(1);
                result.setRecords(1);
                return result;
            }

            @Override
            public void saveComments(@RequestBody Map<String, Object> map) {

            }
        };
    }
}
