package com.huabo.legal.test.mybatis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * mybatis 实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TEST_MYBATIS_TABLE")
public class TestMyBatisEntity {

	@Id
	@GeneratedValue(generator = "JDBC")
	@Column(name = "id")
	@Schema(name="主键ID")
	private Integer id;

	@Column(name = "NAME")
	@Schema(name="名称")
	private String name;

	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private Integer belongGroup;

	@Schema(name="创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CREATETIME")
	private Date createTime;
}