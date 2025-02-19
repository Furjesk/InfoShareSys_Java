package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.SetComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86187
* @description 针对表【setcomment】的数据库操作Service
* @createDate 2023-04-14 15:26:38
*/
public interface SetCommentService extends IService<SetComment> {

    public List<SetComment> selectCommentListBySetId(Integer setId);

}
