package com.njit.infoshareserve.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName momentimg
 */
@Data
@TableName("momentimg")
public class Momentimg implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer momimgid;

    /**
     * 
     */
    private String momimgurl;

    /**
     * 
     */
    private Integer momentid;

    private static final long serialVersionUID = 1L;
}