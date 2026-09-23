package com.huabo.finance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.sql.Clob;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 财务采方案sql初始化表
 * </p>
 *
 * @author L
 * @since 2025-03-13
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_INIT_SQLCONFIG")
@Schema(name="BdInitSqlconfig对象", description="财务采方案sql初始化表")
public class BdInitSqlconfig implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("FID")
      private String fid;

      @Schema(name = "sql标题")
      @TableField("FNAME")
    private String fname;

      @Schema(name = "所属版本")
      @TableField("FVERSIONID")
    private String fversionid;

      @Schema(name = "存入数据表名")
      @TableField("FTABLE")
    private String ftable;  
      
      
      @Schema(name = "sql采集语句")
      @TableField("FINITSQL")
    private String finitsql;
      
      @Schema(name = "特定where条件，不创建在原生sql")
      @TableField("FINSPECIFICITYCOL")
    private String finSpecificityCol;
      
      @Schema(name = "采集配置列名")
      @TableField("FINITCOL")
    private String finitcol;
      
      @Schema(name = "增量标识列")
      @TableField("INCREMENTCOL")
    private String incrementcol;

      @Schema(name = "数据目标端主键列.")
      @TableField("PRIMARYCOL")
    private String primarycol;
}
