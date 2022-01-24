package com.imooc.user.service;

import com.imooc.user.pojo.UserAddress;
import com.imooc.user.pojo.bo.AddressBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("foodie-user-service")
@RequestMapping("address-api")
public interface AddressService {

    /**
     * 根据用户id查询用户的收货地址列表
     * @param userId
     * @return
     */
    @GetMapping("addressList")
    public List<UserAddress> queryAll(@RequestParam("userId") String userId);

    /**
     * 用户新增地址
     * @param addressBO
     */
    @PostMapping("address")
    public void addNewUserAddress(@RequestBody AddressBO addressBO);

    /**
     * 用户修改地址
     * @param addressBO
     */
    @PutMapping("address")
    public void updateUserAddress(@RequestBody AddressBO addressBO);

    /**
     * 根据用户id和地址id，删除对应的用户地址信息
     * @param userId
     * @param addressId
     */
    @DeleteMapping("address")
    public void deleteUserAddress(@RequestParam("userId") String userId,
                                  @RequestParam("addressId") String addressId);

    /**
     * 修改默认地址
     * @param userId
     * @param addressId
     */
    @PostMapping("setDefaultAddress")
    public void updateUserAddressToBeDefault(@RequestParam("userId") String userId,
                                             @RequestParam("addressId") String addressId);

    /**
     * 根据用户id和地址id，查询具体的用户地址对象信息
     * @param userId
     * @param addressId
     * @return
     */
    @GetMapping("queryAddress")
    public UserAddress queryUserAddres(@RequestParam("userId") String userId,
                                       @RequestParam(value = "addressId", required = false) String addressId);
}
