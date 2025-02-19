package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Momcomment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86187
* @description 针对表【momcomment】的数据库操作Mapper
* @createDate 2023-04-24 11:05:52
* @Entity com.njit.infoshareserve.bean.Momcomment
*/
@Mapper
public interface MomcommentMapper extends BaseMapper<Momcomment> {

    @Select("select username,imgurl,momcomment.* from users,momcomment where users.userid = momcomment.userid and momentId = #{momentId}")
    public List<Momcomment> selectCommentListByMomId(Integer momentId);
}




