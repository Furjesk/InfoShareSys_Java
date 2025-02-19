package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Watch;
import com.njit.infoshareserve.service.WatchService;
import com.njit.infoshareserve.mapper.WatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【watch】的数据库操作Service实现
* @createDate 2023-05-26 13:39:13
*/
@Service
public class WatchServiceImpl extends ServiceImpl<WatchMapper, Watch>
    implements WatchService{

    @Autowired
    WatchMapper watchMapper;

    @Override
    public List<Watch> getSetHistory(Integer userId) {
        return watchMapper.getSetHistory(userId);
    }
}




