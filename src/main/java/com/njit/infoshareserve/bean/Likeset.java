package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName likeset
 */
@Data
@TableName("likeset")
public class Likeset implements Serializable {
    /**
     * 联合主键1
     */
    private Integer userid;

    /**
     * 联合主键2
     */
    private Integer setid;

    /**
     * 
     */
    private Date createtime;
    private Integer status;

    private static final long serialVersionUID = 1L;
}