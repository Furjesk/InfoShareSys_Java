package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Follow;
import com.njit.infoshareserve.bean.Users;
import com.njit.infoshareserve.service.FollowService;
import com.njit.infoshareserve.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【follow】的数据库操作Service实现
* @createDate 2023-05-19 15:47:17
*/
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
    implements FollowService{

    @Autowired
    FollowMapper followMapper;

    @Override
    public List<Users> getFollowList(Integer userId) {
        return followMapper.getFollowList(userId);
    }

    @Override
    public List<Users> getFollowerList(Integer userId) {
        return followMapper.getFollowerList(userId);
    }
}




