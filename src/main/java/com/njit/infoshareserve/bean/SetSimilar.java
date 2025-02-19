package com.njit.infoshareserve.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName set_similar
 */
@Data
@TableName("set_similar")
public class SetSimilar implements Serializable {
    /**
     * 
     */
    private Integer setid;

    /**
     * 
     */
    private String similarSets;

    private static final long serialVersionUID = 1L;
}