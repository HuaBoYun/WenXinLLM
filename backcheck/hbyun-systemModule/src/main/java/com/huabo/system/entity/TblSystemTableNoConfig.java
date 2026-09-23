package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="系统编号表设置", description="")
@TableName(value = "TBL_SYSTEM_TABLENO_CONFIG")
public class TblSystemTableNoConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@TableId(value="CONFIGID",type = IdType.INPUT)
	@Schema(name="主键ID")
	private String configId;
	
	@TableField("RIGHTID")
	@Schema(name="权限表主键")
	private BigDecimal rightId;
	
	@TableField("TABLENAME")
	@Schema(name="表名")
	private String tableName;
	
	@TableField("NUMBERCOLUMN")
	@Schema(name="编号列名")
	private String numberColumn;
	
	@TableField("FIRSTNO")
	@Schema(name="一级菜单缩写")
	private String firstNo;
	
	@TableField("SECONDNO")
	@Schema(name="二级菜单缩写")
	private String secondNo;
	
	@TableField("THIRDNO")
	@Schema(name="三级菜单缩写")
	private String thirdNo;
	
	@TableField("URGEFINDSQL")
	@Schema(name="查询催办人sql语句配置")
	private String urgeFindSql;
	
}
