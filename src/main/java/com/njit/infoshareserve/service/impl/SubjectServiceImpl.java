package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Subject;
import com.njit.infoshareserve.service.SubjectService;
import com.njit.infoshareserve.mapper.SubjectMapper;
import org.springframework.stereotype.Service;

/**
* @author 86187
* @description 针对表【subject】的数据库操作Service实现
* @createDate 2023-04-23 21:26:51
*/
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject>
    implements SubjectService{

}




