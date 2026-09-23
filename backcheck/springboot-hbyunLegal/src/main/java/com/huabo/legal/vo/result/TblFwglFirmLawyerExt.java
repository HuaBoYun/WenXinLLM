package com.huabo.legal.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 公司律师扩展表
 */
@Schema(name="TblFwglFirmLawyerExt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_firm_lawyer_ext")
public class TblFwglFirmLawyerExt implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "LAWYEREXTID")
	@GeneratedValue(generator = "JDBC")
	@Schema(name = "公司律师扩展ID")
	private Long lawyerExtId;
	@Column(name = "STARETIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "开始时间")
	private Date stareTime;
	@Column(name = "ENDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "结束时间")
	private Date endTime;
	@Column(name = "OLDWORKUNIT")
	@Schema(name = "在何地何部门(学习)工作")
	private String oldWorkUnit;
	@Column(name = "POSITION")
	@Schema(name = "职务")
	private String position;
	@Column(name = "REMARK")
	@Schema(name = "备注")
	private String remark;
	@Column(name = "`STATE`")
	@Schema(name = "状态")
	private Integer state;
	@Column(name = "CREATOR")
	@Schema(name="创建人",hidden=true)
	private String creator;
	@Column(name = "WORKUNIT")
	@Schema(name="工作单位",hidden=true)
	private String workUnit;
	@Column(name = "BELONGGROUP")
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;
	@Column(name = "CREATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="创建时间",hidden=true)
	private Date createdTime;
	@Column(name = "UPDATEDTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name="更新时间",hidden=true)
	private Date updatedTime;

	public static TblFwglFirmLawyerExt ofId(Long id) {
		TblFwglFirmLawyerExt tblFwglFirmLawyerExtMySql = new TblFwglFirmLawyerExt();
		tblFwglFirmLawyerExtMySql.setLawyerExtId(id);
		return tblFwglFirmLawyerExtMySql;
	}
}
