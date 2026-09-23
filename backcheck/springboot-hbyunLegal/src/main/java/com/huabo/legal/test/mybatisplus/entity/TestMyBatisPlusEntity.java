package com.huabo.legal.test.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * mybatis-plus 实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TEST_MYBATIS_PLUS_TABLE")
public class TestMyBatisPlusEntity {

	@Schema(name="主键ID")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private BigDecimal id;

	@TableField("NAME")
	@Schema(name="名称")
	private String name;

	@TableField("BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;

	@Schema(name="创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField("CREATETIME")
	private Date createTime;

	@Schema(name="mybatis实体主键ID 链表用的")
	@TableField("TESTMYBATISID")
	private Integer testMybatisId;
}