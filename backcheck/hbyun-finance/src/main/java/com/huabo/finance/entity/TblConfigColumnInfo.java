package com.huabo.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 业务数据库列信息
 * </p>
 *
 * @author L
 * @since 2025-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONFIG_COLUMNINFO")
@Schema(name="TblConfigColumnInfo对象", description="业务数据库列信息")
public class TblConfigColumnInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "列注释")
      @TableField("FNAME")
    private String fname;

      @Schema(name = "目标端列名")
      @TableField("OURSCOLNAME")
    private String oursColname;

      @Schema(name = "源端列名")
      @TableField("OUTSCOLNAME")
    private String outsColname;

      @Schema(name = "列类型")
      @TableField("COLTYPE")
    private String colType;

      @Schema(name = "长度")
      @TableField("COLLENGTH")
    private String colLength;

      @Schema(name = "是否是自增标识0是 1否")
      @TableField("ISAUTOINCREMENT")
    private Integer isAutoIncrement;

      @Schema(name = "是否展现在列表页 0是 1否")
      @TableField("ISSHOWLIST")
    private Integer isShowList;

      @Schema(name = "是否是筛选条件 0 是 1否")
      @TableField("ISFILTER")
    private Integer isFilter;

      @Schema(name = "是否是主键标识  0是 1否")
      @TableField("ISPRIMARYKEY")
    private Integer isPrimaryKey;
      
      @Schema(name = "所属表主键")
      @TableField("FTBLID")
    private String ftableId;
      
      
      @Schema(name = "采集数据")
      @TableField(exist = false)
    private Object queryData;
      

}