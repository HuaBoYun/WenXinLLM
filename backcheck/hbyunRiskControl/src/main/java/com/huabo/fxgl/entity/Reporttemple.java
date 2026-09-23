package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-18
 */
@Data
@TableName("TBL_REPORTTEMPLE")
public class Reporttemple implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal reporttempid;

    private String templenumber;

    private String templename;

    private String createdtime;

    private String creater;

    private String createddepartment;

    private String templeinfo;

    private String templetype;

    private String memo;

}
