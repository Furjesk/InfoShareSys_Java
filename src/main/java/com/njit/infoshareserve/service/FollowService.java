package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.njit.infoshareserve.bean.Users;

import java.util.List;

/**
* @author 86187
* @description 针对表【follow】的数据库操作Service
* @createDate 2023-05-19 15:47:17
*/
public interface FollowService extends IService<Follow> {

    // 获取UP主页的关注列表
    public List<Users> getFollowList(Integer userId);
    // 获取UP主页的粉丝列表
    public List<Users> getFollowerList(Integer userId);

}
