package com.example.tccdemo.controller;

import com.example.tccdemo.db131.model.User;
import com.example.tccdemo.service.UserService;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("user")
public class UserController {

    private Set<String> tokenSet = new HashSet<>();

    @Autowired
    private UserService userService;

    @RequestMapping("userList")
    public String userList(ModelMap map){
        List<User> users = userService.getAllUsers();
        map.addAttribute("users",users);

        return "user/user-list";
    }

    @RequestMapping("delUser")
    @ResponseBody
    public Map<String,Object> delUser(@RequestParam Integer userId){

        int result = userService.delUser(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("status",result);
        return map;
    }

    @RequestMapping("userDetail")
    public String userDetail(@RequestParam  Integer userId,ModelMap map){
        User user = userService.selectById(userId);
        map.addAttribute("user",user);
        return "user/user-detail";
    }

    @RequestMapping("updateUser")
    public String updateUser(User user,String token) throws Exception {

        Thread.sleep(5000);

        if (user.getId() !=null){
            System.out.println("更新用户");
            userService.updateUser(user);
        }else {
            if (tokenSet.contains(token)){
                System.out.println("添加用户");
                userService.insertUser(user,token);
            }else {
                throw new Exception("token 不存在");
            }
        }
        return "redirect:/user/userList";
    }

    @RequestMapping("register")
    public String register(ModelMap map){
        String token = UUID.randomUUID().toString();
        tokenSet.add(token);
        map.addAttribute("user",new User());
        map.addAttribute("token",token);

        return "/user/user-detail";
    }

}
