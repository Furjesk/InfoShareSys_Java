package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName users
 */
@Data
@TableName("users")
public class Users implements Serializable {
    /**
     * 验证码
     */
    @TableField(exist = false)
    private String code;

    /**
     * token
     */
    @TableField(exist = false)
    private String token;
    //别人是否关注了他，用于主页关注和粉丝列表显示关注状态,2表示相互关注
    @TableField(exist = false)
    private Integer isFollow;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer userid;

    /**
     * 
     */
    private String username;
    private String useremail;
    private String userphone;

    /**
     * 
     */
    private String userpwd;

    /**
     * 
     */
    private Date createtime;
    private String userbrief;

    /**
     * 
     */
    private String imgurl;

    /**
     * 
     */
    @TableField("`rank`")
    private String rank;

    /**
     * 
     */
    private Integer credit;

    /**
     * 
     */
    private String turename;

    /**
     * 
     */
    private Integer sex;

    /**
     * 
     */
    private Date birthday;

    /**
     * 
     */
    private String location;

    /**
     * 
     */
    private String school;

    /**
     * 
     */
    private String department;

    /**
     * 
     */
    private String specialty;

    /**
     * 
     */
    private String classname;

    /**
     * 
     */
    private Integer followernum;

    /**
     * 
     */
    private Integer follownum;

    /**
     * 
     */
    private Integer state;

    private static final long serialVersionUID = 1L;
}