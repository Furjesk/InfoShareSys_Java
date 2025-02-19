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
 * 
 * @TableName momcomment
 */
@Data
@TableName("momcomment")
public class Momcomment implements Serializable {

    //评论者昵称
    @TableField(exist = false)
    private String username;
    //父级评论的用户名
    @TableField(exist = false)
    private String fatherusername;
    //父级评论的用户ID
    @TableField(exist = false)
    private Integer fatheruserid;
    //评论者头像
    @TableField(exist = false)
    private String imgurl;
    //子评论列表
    @TableField(exist = false)
    private List<Momcomment> subcommentlist;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer commentid;

    /**
     * 
     */
    private Integer userid;

    /**
     * 
     */
    private Integer momentid;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Integer likenum;

    /**
     * 
     */
    private Integer fatherid;

    /**
     * 
     */
    private Integer commentlevel;

    /**
     * 
     */
    private Integer topcommentid;

    private static final long serialVersionUID = 1L;
}