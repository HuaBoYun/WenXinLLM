package com.huabo.legal.vo.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.excel.DictMapUtil;
import com.huabo.legal.util.excel.annotation.ExcelField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 法务机构及负责人表
 */
@Schema(name="TblFwglLegalOrganization")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_fwgl_legal_organization")
public class TblFwglLegalOrganization implements Serializable {

	private static final long serialVersionUID = 1L;

	@ExcelField(title = "机构名称", sort = 1, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "名称")
	private String organizationName;

	@ExcelField(title = "机构性质", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "性质")
	private String nature;

	@ExcelField(title = "负责人", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "负责人")
	private String responsiblePerson;

	@ExcelField(title = "负责人电话", sort = 4, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "负责人电话")
	private String responsiblePersonPhone;

	@ExcelField(title = "联络人", sort = 5, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "联络人")
	private String contact;

	@ExcelField(title = "联络人电话", sort = 6, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "联络人电话")
	private String contactPhone;

	@ExcelField(title = "状态", sort = 7, column = 0, align = ExcelField.Align.CENTER, width = 3000, dictType = DictMapUtil.SHSTATE_TYPE)
	@Schema(name="状态",hidden=true)
	private Integer state;

	//@ExcelField(title = "机构ID", sort = 0, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "机构ID")
	private Long organizationId;

	@Schema(name = "所属集团(列表)ID")
	private Long belongGroupId;

	//@ExcelField(title = "所属集团(列表)名称", sort = 2, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="所属集团(列表)名称",hidden=true)
	private String belongGroupName;

	//@ExcelField(title = "工作单位(列表)名称", sort = 3, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="工作单位(列表)名称",hidden=true)
	private String workUnitName;

	@Schema(name = "工作单位(列表)ID")
	private Long workUnitId;

	//@ExcelField(title = "联络人手机", sort = 9, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "联络人手机")
	private String contactMobilePhone;

	//@ExcelField(title = "联络邮箱", sort = 10, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "联络邮箱")
	private String contactEmail;

	//@ExcelField(title = "制单人", sort = 11, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "制单人")
	private String makingPeople;

	//@ExcelField(title = "制单日期", sort = 12, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name = "制单日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date makingTime;

	//	@ExcelField(title = "审核人", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核人ID")
	private String auditPerson;

	//@ExcelField(title = "审核人", sort = 13, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核人名称")
	private String auditPersonName;

	//@ExcelField(title = "审核时间", sort = 14, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "审核时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date auditTime;

	//@ExcelField(title = "年度", sort = 15, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name = "年度")
	private String annual;

	@Column(name = "ORGANIZATIONEXTID")
	@Schema(name = "关联id-法务机构及负责人扩展ID,多个逗号隔开")
	private String organizationExtId;

	//@ExcelField(title = "创建人", sort = 17, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="创建人",hidden=true)
	private String creator;

	//@ExcelField(title = "工作单位", sort = 18, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	//@ExcelField(title = "所属集团", sort = 19, column = 0, align = ExcelField.Align.CENTER, width = 3000)
	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

	//	@ExcelField(title = "创建时间", sort = 20, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="创建时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	//	@ExcelField(title = "更新时间", sort = 21, column = 0, align = ExcelField.Align.CENTER, width = 5000)
	@Schema(name="更新时间",hidden=true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	public static TblFwglLegalOrganization ofId(Long id) {
		TblFwglLegalOrganization tblFwglLegalOrganizationMySql = new TblFwglLegalOrganization();
		tblFwglLegalOrganizationMySql.setOrganizationId(id);
		return tblFwglLegalOrganizationMySql;
	}
}
