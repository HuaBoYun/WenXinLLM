package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_HOME_PAGE_MODEL")
@Schema(name="TblHomePageModelMySql对象")
public class TblHomePageModelMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private BigDecimal id;

    @TableField("NAME")
    private String name;

    @TableField("URL")
    private String url;

    @TableField("TYPE")
    private String type;

    @TableField("RIGHTID")
    private BigDecimal rightid;


}
