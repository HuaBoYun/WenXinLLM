package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.entity
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:17:58
 */
@TableName("TBL_WGZZ_WGHC")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name="违规核查实体")
@Accessors(chain = true)
public class TblWgzzWghc {

   @TableField(value = "ID")
   @Schema(name = "主键id")
   @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
   private BigDecimal id;

    @TableField("STATUS")
    @Schema(name = "状态核查")
    private Integer status;

    @TableField("YSSTUTS")
    @Schema(name = "状态移送")
    private Integer ysstuts;

   @TableField("CREATOR")
   @Schema(name = "创建人")
   private BigDecimal creator;

   @TableField("IMPCREATEUSERNAME")
   @Schema(name = "创建时间")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date impcreateusername;

    @TableField("CLUENABER")
    @Schema(name = "线索编号")
    private Integer cluenaber;

    @TableField("HSCONTENT")
    @Schema(name = "核实内容")
    private String hscontent;

}
