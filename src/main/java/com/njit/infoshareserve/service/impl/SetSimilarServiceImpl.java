package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.SetSimilar;
import com.njit.infoshareserve.service.SetSimilarService;
import com.njit.infoshareserve.mapper.SetSimilarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 86187
* @description 针对表【set_similar】的数据库操作Service实现
* @createDate 2023-05-12 19:53:50
*/
@Service
public class SetSimilarServiceImpl extends ServiceImpl<SetSimilarMapper, SetSimilar>
    implements SetSimilarService{

    @Autowired
    SetSimilarMapper setSimilarMapper;

    @Override
    public void truncateTable() {
        setSimilarMapper.truncateTable();
    }
}




