package com.njit.infoshareserve.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.njit.infoshareserve.bean.*;
import com.njit.infoshareserve.service.*;
import com.njit.infoshareserve.utils.CommentDealUtil;
import com.njit.infoshareserve.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    MailUtil mailUtil;

    @PostMapping("/login")
    public ResultData<Users> userLogin(@RequestBody Users user){

        // 数据库中的user
//        Users dbUser = userService.selectUserByUserName(user.getUsername());
        Users dbUser;
        QueryWrapper queryWrapper = new QueryWrapper();

        //这里的username其实是登录账号
        if(user.getUsername().indexOf('@') != -1){
            //邮箱登录
            queryWrapper.eq("userEmail",user.getUsername());
        }else {
            //手机号登录
            queryWrapper.eq("userPhone",user.getUsername());
        }
        dbUser = userService.getOne(queryWrapper);

        if(dbUser != null && SecureUtil.md5(user.getUserpwd()).equals(dbUser.getUserpwd())){
            dbUser.setUserpwd("");
            dbUser.setToken("this_is_a_token");
            return ResultData.success(dbUser);
        }
        return ResultData.fail(401,"后端：用户名或密码错误！");
    }

    /**
     * 注册验证码
     * @param session 【这个需要改进一下，用redis还是什么的存储code，过期删除】！！！
     * @return
     */
    @GetMapping("/getCode/{phone}")
    public ResultData<String> getVerifyCode(@PathVariable(value = "phone") String phone,
                                            HttpSession session) throws Exception {

        //【默认邮箱注册】
        String code = RandomStringUtils.randomNumeric(6);
        mailUtil.sendSimpleMail(phone,"宁共享获取验证码",code);
        session.setAttribute("register-"+phone,code);
        session.setMaxInactiveInterval(60);//以秒为单位
        return ResultData.success("验证码发送成功");
//        if(phone.indexOf('@') != -1){
//            //邮箱登录
//            String code = RandomStringUtils.randomNumeric(6);
//            mailUtil.sendSimpleMail(phone,"宁共享获取验证码",code);
//            session.setAttribute(phone,code);
//            session.setMaxInactiveInterval(60);//以秒为单位
//            return ResultData.success("验证码发送成功");
//        }else {
//            //手机号登录
//        }
//        return ResultData.fail(400,"后端：请输入手机号或邮箱！");

    }
    /**
     * @param session 【这个需要改进一下，用redis还是什么的存储code，过期删除】！！！
     * @return
     */
    @PostMapping("/register")
    public ResultData<String> userRegister(@RequestBody Users user,
                                           HttpSession session){
        if(user!=null){
            // 验证码正确
            if(session.getAttribute("register-"+user.getUsername()).equals(user.getCode())){

//                Users tableUser = userService.selectUserByUserName(user.getUsername());
                QueryWrapper queryWrapper = new QueryWrapper();
                if(user.getUsername().indexOf('@') != -1){
                    queryWrapper.eq("userEmail",user.getUsername());
                }else {
                    queryWrapper.eq("userPhone",user.getUsername());
                }
                Users tableUser = userService.getOne(queryWrapper);
                if(tableUser!=null){
                    //用户名已存在
                    return ResultData.fail(403,"后端：用户名已被注册！");
                }
                // 用户名未被注册
                Date date = new Date();
                user.setCreatetime(date);
                //MD5信息摘要算法，不可逆
                user.setUserpwd(SecureUtil.md5(user.getUserpwd()));
                //默认昵称就是注册账号，用户可以在个人中心修改
                if(user.getUsername().indexOf('@') != -1){
                    user.setUseremail(user.getUsername());
                }else {
                    user.setUserphone(user.getUsername());
                }
                userService.save(user);
                // 删除session中的验证码，需要改进
                session.removeAttribute(user.getUsername());

                return ResultData.success("注册成功");
            }
        }

        return ResultData.fail(400,"后端：请正确输入信息！");
    }

    /**
     * 获取忘记密码的验证码【未实现邮件发送】
     */
    @GetMapping("/getForgetCode/{account}")
    public ResultData<String> getForgetCode(@PathVariable(value = "account") String account,
                                            HttpSession session) throws Exception {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(account.indexOf('@') != -1){
            //邮箱找回
            queryWrapper.eq("userEmail",account);
        }else {
            //手机号找回
            queryWrapper.eq("userPhone",account);
        }
        Users user = userService.getOne(queryWrapper);
        if(user == null)
            return ResultData.fail(412,"账号不存在");
        //随机生成验证码
        String code = RandomStringUtils.randomNumeric(6);

        //code发给用户【默认邮箱找回】
        mailUtil.sendSimpleMail(account,"宁共享获取验证码",code);

        //code存入session，保持60s
        session.setAttribute("forget-"+account,code);
        session.setMaxInactiveInterval(60);//以秒为单位

        return ResultData.success("验证码发送成功");
    }
    @PostMapping("/changePwd")
    public ResultData<String> changePwd(@RequestBody Users user,HttpSession session) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.set("userPwd",SecureUtil.md5(user.getUserpwd()));
        if(user.getUsername().indexOf('@') != -1){
            //邮箱登录
            updateWrapper.eq("userEmail",user.getUsername());
        }else {
            //手机号登录
            updateWrapper.eq("userPhone",user.getUsername());
        }
        Boolean success = userService.update(updateWrapper);
        if(success) {
            //移除session
            session.removeAttribute("forget-"+user.getUsername());
            return ResultData.success("修改密码成功");
        }else {
            return ResultData.fail(500,"修改密码失败");
        }
    }

    /** 关注or取关后更新vuex仓库中的userInfo */
    @GetMapping("/getUserInfo/{userId}")
    public ResultData<Users> getUserInfo(@PathVariable(value = "userId") Integer userId) {
        Users user = userService.getById(userId);
        return ResultData.success(user);
    }

}