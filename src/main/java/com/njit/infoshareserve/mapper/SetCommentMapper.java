package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.SetComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86187
* @description 针对表【setcomment】的数据库操作Mapper
* @createDate 2023-04-14 15:26:38
* @Entity com.njit.infoshareserve.bean.SetComment
*/
@Mapper
public interface SetCommentMapper extends BaseMapper<SetComment> {

    @Select("select username,imgurl,setcomment.* from users,setcomment where users.userid = setcomment.userid and setid = #{setId} order by setcomment.createtime asc")
    public List<SetComment> selectCommentListBySetId(Integer setId);
}




