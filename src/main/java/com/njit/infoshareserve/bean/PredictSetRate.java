package com.njit.infoshareserve.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName predict_set_rate
 */
@Data
@TableName("predict_set_rate")
public class PredictSetRate implements Serializable {
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
    private Double rate;

    private static final long serialVersionUID = 1L;
}