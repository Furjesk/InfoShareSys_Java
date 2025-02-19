package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName rateset
 */
@Data
@TableName("rateset")
public class Rateset implements Serializable {
    /**
     * 
     */
    private Integer userid;

    /**
     * 
     */
    private Integer setid;

    /**
     * 
     */
    private Integer rate;

    /**
     *
     */
    private Date createtime;

    private static final long serialVersionUID = 1L;
}