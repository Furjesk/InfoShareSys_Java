package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Watch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86187
* @description 针对表【watch】的数据库操作Mapper
* @createDate 2023-05-26 13:39:13
* @Entity com.njit.infoshareserve.bean.Watch
*/
@Mapper
public interface WatchMapper extends BaseMapper<Watch> {

    //获取用户视频的浏览历史
    @Select("select videoset.userid as UPId,username,imgurl,setimg,setname,setlong,category1name,watch.* from watch,videoset,users,category1 " +
            "where watch.setid = videoset.setid and videoset.category1id = category1.category1id and users.userid = videoset.userid and  watch.userid = #{userId} " +
            "order by watch.watchtime desc")
    public List<Watch> getSetHistory(Integer userId);

}




