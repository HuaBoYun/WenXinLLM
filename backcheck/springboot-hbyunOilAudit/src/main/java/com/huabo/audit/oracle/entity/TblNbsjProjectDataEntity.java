package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TBL_PROJECT_DATAPRE")
@Schema(name="项目资料")
public class TblNbsjProjectDataEntity {
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "ID")
	@Schema
	 @Id
     @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
	private Integer id;
	
	@TableField(value = "PROJECT_DATAPRE_ID")
	@Schema
	private String projectDatapreId;
	
	
	@TableField(value = "PROJECTID")
	@Schema
	private Integer projectid;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Integer oldProjectId;
	
	@TableField(value = "DATA_NAME")
	@Schema
	private String dataName;
	
	@TableField(value = "DATA_CAPACITY")
	@Schema
	private String dataCapacity;
	
	@TableField(value = "DATA_DATE")
	@Schema(hidden=true)
	private Date dataDate;
	
	@TableField(value = "PROJECTNAME")
	@Schema
	private String projectname;
	
	@TableField(value = "USERNAME")
	@Schema(hidden=true)
	private String username;
	
	@TableField(value = "ORGID")
	@Schema(hidden=true)
	private String orgid;
	
	@TableField(value = "FRISTUSERID")
	@Schema(name="存储下发人员id",hidden=true)
	private String fristuserid;
	
}
