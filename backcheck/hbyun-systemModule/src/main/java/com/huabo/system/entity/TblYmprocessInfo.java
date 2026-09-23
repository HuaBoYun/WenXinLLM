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
@Schema(name="TBL_YMPROCESS_INFO对象", description="")
@TableName(value = "TBL_YMPROCESS_INFO")
public class TblYmprocessInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	public final static Integer USER_DISABLE=0;//禁用
	public final static Integer USER_ENBLE=1;//启用
	
	public final static String REGISTERUSERPASSWORD = "REDACTED";
	
	@TableId(value="INFOID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal infoId;
	
	@TableField("PRONAME")
	@Schema(name="流程接口名称")
	private String proName;
	
	@TableField("PROURL")
	@Schema(name="流程接口路径")
	private String proUrl;
	
	@TableField("PROMEMO")
	@Schema(name="流程接口描述")
	private String proMemo;

	
}
