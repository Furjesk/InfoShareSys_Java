package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.Joinchannel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.njit.infoshareserve.bean.VideoSet;

import java.util.List;

/**
* @author 86187
* @description 针对表【joinchannel】的数据库操作Service
* @createDate 2023-05-28 10:24:19
*/
public interface JoinchannelService extends IService<Joinchannel> {

    //获取用户加入频道列表
    public List<Joinchannel> getJoinListByUserid(Integer userId);

    //获取频道top8视频
    public List<VideoSet> getChannelTop8(Integer category2Id);
}
