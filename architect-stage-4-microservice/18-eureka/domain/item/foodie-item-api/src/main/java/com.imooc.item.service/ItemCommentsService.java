package com.imooc.item.service;

import com.imooc.pojo.PagedGridResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by 半仙.
 */
@RequestMapping("item-comments-api")
public interface ItemCommentsService {

    /**
     * 我的评价查询 分页
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("myComments")
    public PagedGridResult queryMyComments(@RequestParam("userId") String userId,
                                           @RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "pageSize", required = false)Integer pageSize);

    @PostMapping("saveComments")
    public void saveComments(@RequestBody Map<String, Object> map);

}
