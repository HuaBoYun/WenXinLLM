package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_PERSONAL_TRAIN")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="个人培训TBL_PERSONAL_TRAIN对象", description="")
public class TblPersonalTrain implements Serializable {
	
	
	@TableId(value="TRAINID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal trainid;//主键ID,自动增长
	
	@TableField("TRAINTIME")
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Schema(name="培训时间")
	private Date traintime;
	
	@TableField("TRAINLOCATION")
	@Column(name = "TRAINLOCATION")
	@Schema(name="培训地点")
	private String trainlocation;
	
	@TableField("TRAINEVIDENCE")
	@Column(name = "TRAINEVIDENCE")
	@Schema(name="培训证明材料")
	private String trainevidence;
	
	@TableField("TRAINWITENEE")
	@Column(name = "TRAINWITNESS")
	@Schema(name="培训证明人")
	private String trainwitness;
	 
	@TableField("STAFFID")
	@Column(name = "STAFFID")
	@Schema(name="关联用户 ")
	private String staffid;
	
	@Transient
	private String attid;
	
	@TableField(exist = false)
	@Schema(name="是否加密存储，1表示加密，0表示不加密，默认值为0")
	private Boolean isEncrypted = true;

	@Schema(name="加密地址，用于预览")
	@TableField(exist = false)
	private String jmurl;
	    
	@Schema(name = "附件路径")
	@TableField(exist = false)
    private String attpath;
	 
}
