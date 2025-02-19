package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Joinchannel;
import com.njit.infoshareserve.bean.VideoSet;
import com.njit.infoshareserve.service.JoinchannelService;
import com.njit.infoshareserve.mapper.JoinchannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【joinchannel】的数据库操作Service实现
* @createDate 2023-05-28 10:24:19
*/
@Service
public class JoinchannelServiceImpl extends ServiceImpl<JoinchannelMapper, Joinchannel>
    implements JoinchannelService{

    @Autowired
    JoinchannelMapper joinchannelMapper;

    @Override
    public List<Joinchannel> getJoinListByUserid(Integer userId) {
        return joinchannelMapper.getJoinListByUserid(userId);
    }

    @Override
    public List<VideoSet> getChannelTop8(Integer category2Id) {
        return joinchannelMapper.getChannelTop8(category2Id);
    }
}




