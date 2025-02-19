package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Moment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njit.infoshareserve.bean.Subject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 86187
* @description 针对表【moment】的数据库操作Mapper
* @createDate 2023-04-22 21:07:50
* @Entity com.njit.infoshareserve.bean.Moment
*/
@Mapper
public interface MomentMapper extends BaseMapper<Moment> {

    //通过momentIdList和status更新视频集状态
    public Boolean updateMomStatusByMomIds(Integer status, List<Integer> momentIdList);

    //根据keyword查找动态，涉及内容和话题。status = 1表示已通过审核的
    @Select("select username,subjectname,moment.* from moment,users,subject " +
            "where moment.userId = users.userId and moment.subjectId = subject.subjectId and status = 1 " +
            "and (subjectname like #{keyword} or content like #{keyword})")
    public List<Moment> searchMomentListByKey(String keyword);
}




