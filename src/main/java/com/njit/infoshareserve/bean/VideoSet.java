package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName videoset
 */
@Data
@TableName("videoset")
public class VideoSet implements Serializable {

//    @TableField(exist = false)
//    private List<Label> labelList; //这个好像没用到

    //up主昵称
    @TableField(exist = false)
    private String username;
    //up主头像
    @TableField(exist = false)
    private String imgurl;
    /**
     * 统计推荐模块字段
     */
    @TableField(exist = false)
    private Date yearmonth;
    @TableField(exist = false)
    //每个月点赞数
    private Integer like_count;
    @TableField(exist = false)
    //每个月评论数
    private Integer comment_count;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer setid;

    /**
     *
     */
    private String setimg;

    /**
     *
     */
    private String setlong;

    /**
     *
     */
    private String setname;

    /**
     *
     */
    private String setbrief;

    private Integer category1id;

    /**
     *
     */
    private Integer category2id;
    private String videotype;
    private Integer cannotforward;


    /**
     *
     */
    private Integer likenum;

    /**
     *
     */
    private Integer favoritenum;

    /**
     *
     */
    private Integer commentnum;

    /**
     *
     */
    private Integer transmitnum;

    /**
     *
     */
    private Integer coinnum;

    /**
     *
     */
    private Integer watchnum;

    /**
     *
     */
    private Date createtime;

    /**
     *
     */
    private Integer userid;
    /**
     * 0表示待审核；1表示通过审核；2表示退回；3表示用户删除/撤销
     */
    private Integer status;
    /**
     * 0表示不允许评论；1表示允许评论
     */
    private Integer cancomment;

    private static final long serialVersionUID = 1L;
}