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
 * @TableName review
 */
@Data
@TableName("review")
public class Review implements Serializable {

    @TableField(exist = false)
    private String setname;
    @TableField(exist = false)
    private String setimg;
    @TableField(exist = false)
    private String setlong;

    @TableId(type = IdType.AUTO)
    private Integer reviewid;
    /**
     * 
     */
    private Integer managerid;

    /**
     * 
     */
    private Integer setid;

    /**
     * 
     */
    private Integer momentid;

    /**
     * 1表示setid；2表示momentid
     */
    private Integer targettype;

    /**
     * 
     */
    private Integer status;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Date dealtime;
    private String feedback;

    private static final long serialVersionUID = 1L;
}