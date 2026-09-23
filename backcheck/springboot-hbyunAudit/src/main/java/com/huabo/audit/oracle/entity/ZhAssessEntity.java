package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_ZH_ASSESS")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class ZhAssessEntity {
	
	@TableId(value = "assessid", type= IdType.INPUT)
	@Schema
	private BigDecimal assessid;
	
	@TableField(value = "numbercode")
	@Schema
	private int numbercode;
	
	@TableField(value = "assesscontent")
	@Schema
	private String assesscontent;
	
	@TableField(value = "selfscore")
	@Schema
	private Integer selfscore;
	
	@TableField(value = "otherscore")
	@Schema
	private Integer otherscore;
	
	@TableField(value = "otherscore")
	@Schema
	private String note;
	
	@TableField(value = "otherscore")
	@Schema
	private String remark;

	@TableField(exist = false)
	@Schema
	private ZhFormEntity zhform;
	
	public ZhAssessEntity(BigDecimal assessid, int numbercode, String assesscontent, Integer selfscore, Integer otherscore,
			String note, String remark, ZhFormEntity zhform) {
		super();
		this.assessid = assessid;
		this.numbercode = numbercode;
		this.assesscontent = assesscontent;
		this.selfscore = selfscore;
		this.otherscore = otherscore;
		this.note = note;
		this.remark = remark;
		this.zhform = zhform;
	}
}
