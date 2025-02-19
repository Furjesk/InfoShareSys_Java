package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njit.infoshareserve.bean.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86187
* @description 针对表【follow】的数据库操作Mapper
* @createDate 2023-05-19 15:47:17
* @Entity com.njit.infoshareserve.bean.Follow
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    // 获取UP主页的关注列表
    @Select("select follow.status as isFollow,users.* from users,follow " +
            "where users.userId = follow.followId and status = 1 and followerId = #{userId} " +
            "order by followtime desc")
    public List<Users> getFollowList(Integer userId);

    // 获取UP主页的粉丝列表
    @Select("select users.* from users,follow " +
            "where users.userId = follow.followerId and status = 1 and followId = #{userId} " +
            "order by followtime desc")
    public List<Users> getFollowerList(Integer userId);

}




