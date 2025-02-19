package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Joinchannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njit.infoshareserve.bean.VideoSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86187
* @description 针对表【joinchannel】的数据库操作Mapper
* @createDate 2023-05-28 10:24:19
* @Entity com.njit.infoshareserve.bean.Joinchannel
*/
@Mapper
public interface JoinchannelMapper extends BaseMapper<Joinchannel> {

    //获取用户加入频道列表
    @Select("select category2name,joinchannel.* from joinchannel,category2 " +
            "where joinchannel.category2Id = category2.category2Id and joinchannel.userId = #{userId} ")
    public List<Joinchannel> getJoinListByUserid(Integer userId);

    //获取频道top8视频
    @Select("select username, videoset.* from users,videoset " +
            "where users.userId = videoset.userId and category2Id = #{category2Id} " +
            "order by likenum desc limit 8")
    public List<VideoSet> getChannelTop8(Integer category2Id);

}




