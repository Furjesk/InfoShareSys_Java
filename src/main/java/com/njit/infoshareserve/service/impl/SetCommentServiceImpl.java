package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.SetComment;
import com.njit.infoshareserve.service.SetCommentService;
import com.njit.infoshareserve.mapper.SetCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【setcomment】的数据库操作Service实现
* @createDate 2023-04-14 15:26:38
*/
@Service
public class SetCommentServiceImpl extends ServiceImpl<SetCommentMapper, SetComment>
    implements SetCommentService {

    @Autowired
    SetCommentMapper setCommentMapper;

    @Override
    public List<SetComment> selectCommentListBySetId(Integer setId) {
        return setCommentMapper.selectCommentListBySetId(setId);
    }
}




