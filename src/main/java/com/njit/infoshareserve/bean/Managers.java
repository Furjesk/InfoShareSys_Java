package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName managers
 */
@TableName("managers")
@Data
public class Managers implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer managerid;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String userpwd;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private String imgurl;

    /**
     * 
     */
    private Integer authority;

    private static final long serialVersionUID = 1L;
}