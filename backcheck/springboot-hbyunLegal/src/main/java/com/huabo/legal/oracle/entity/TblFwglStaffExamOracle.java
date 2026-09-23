package com.huabo.legal.oracle.entity;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试人员关联表，下发考试人员
 *
 */
@Schema(name="TblFwglStaffExam")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_staff_exam")
public class TblFwglStaffExamOracle {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "考试人员关联表")
	private Long id;
	
	@Id
	@Column(name = "EXAMID")
	@Schema(name = "考试id")
	private String examId;
	
	@Id
	@Column(name = "STAFFID")
	@Schema(name = "人员id")
	private Long staffId;

}
