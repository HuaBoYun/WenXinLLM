package com.huabo.contract.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.contract.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_CONTRACT_PROJECT")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblContractProject {
	
	@TableId(value = "projectid", type= IdType.INPUT)
	@Schema
	private BigDecimal projectid;
	
	@TableField(value = "projectname")
	@Schema
	private String projectname;
	
	@TableField(value = "projectcode")
	@Schema
	private String projectcode;
	
	@TableField(value = "memo")
	@Schema
	private String memo;
	
	@TableField(value = "createtime")
	@Schema
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	
	@TableField(value = "STATE")
	@Schema
	private String STATE;
	
	@TableField(value = "undertakestaffid")
	@Schema
	private String undertakestaffid;

	@TableField(exist = false)
	@Schema(name = "undertakestaffname")
	private String undertakestaffname;
	
	@TableField(value = "undertakeorgid")
	@Schema
	private String undertakeorgid;

	@TableField(exist = false)
	@Schema(name = "undertakeorgname")
	private String undertakeorgname;
	
	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff undertakestaff;
	
	@TableField(exist = false)
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblOrganization undertakeorg;
	
	@TableField(value = "CREATESTAFFID")
	@Schema
	private String createstaffid;
	

	@TableField(value = "UNIQUEID")
	@Schema(name="关联资产项目id",hidden=true)
	private String uniuqeid;
	
	
	@Schema(name="关联资产项目公司名称",hidden=true)
	private String xmorgname;
	
	
	@TableField(value = "STATENAME")
	@Schema(name = "项目状态名称")
	private String statename;
	
	@TableField(value = "STATECODE")
	@Schema(name="项目状态编号",hidden=true)
	private String statecode;

	@TableField(value = "RESERVEDSTRING1")
	@Schema(name = "预留字符串1")
	private String reservedstring1;

	@TableField(value = "RESERVEDSTRING2")
	@Schema(name = "预留字符串2")
	private String reservedstring2;

	@TableField(value = "RESERVEDSTRING3")
	@Schema(name = "预留字符串3")
	private String reservedstring3;

	@TableField(value = "RESERVEDSTRING4")
	@Schema(name = "预留字符串4")
	private String reservedstring4;

	@TableField(value = "RESERVEDSTRING5")
	@Schema(name = "预留字符串5")
	private String reservedstring5;

	@TableField(value = "RESERVEDSTRING6")
	@Schema(name = "预留字符串6")
	private String reservedstring6;

	@TableField(value = "RESERVEDSTRING7")
	@Schema(name = "预留字符串7")
	private String reservedstring7;

	@TableField(value = "RESERVEDSTRING8")
	@Schema(name = "预留字符串8")
	private String reservedstring8;

	@TableField(value = "RESERVEDSTRING9")
	@Schema(name = "预留字符串9")
	private String reservedstring9;

	@TableField(value = "RESERVEDSTRING10")
	@Schema(name = "预留字符串10")
	private String reservedstring10;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE1")
	@Schema(name = "预留下拉单选字符串1")
	private String reserveddropdownsinglechoice1;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE2")
	@Schema(name = "预留下拉单选字符串2")
	private String reserveddropdownsinglechoice2;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE3")
	@Schema(name = "预留下拉单选字符串3")
	private String reserveddropdownsinglechoice3;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE4")
	@Schema(name = "预留下拉单选字符串4")
	private String reserveddropdownsinglechoice4;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE5")
	@Schema(name = "预留下拉单选字符串5")
	private String reserveddropdownsinglechoice5;

	@TableField(value = "RESERVEDNUM1")
	@Schema(name = "预留数字1")
	private Integer reservednum1;

	@TableField(value = "RESERVEDNUM2")
	@Schema(name = "预留数字2")
	private Integer reservednum2;

	@TableField(value = "RESERVEDNUM3")
	@Schema(name = "预留数字3")
	private Integer reservednum3;

	@TableField(value = "RESERVEDNUM4")
	@Schema(name = "预留数字4")
	private Integer reservednum4;

	@TableField(value = "RESERVEDNUM5")
	@Schema(name = "预留数字5")
	private Integer reservednum5;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE1")
	@Schema(name = "预留下拉多选字符串1")
	private String reserveddropdownmultiple1;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE2")
	@Schema(name = "预留下拉多选字符串2")
	private String reserveddropdownmultiple2;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE3")
	@Schema(name = "预留下拉多选字符串3")
	private String reserveddropdownmultiple3;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE4")
	@Schema(name = "预留下拉多选字符串4")
	private String reserveddropdownmultiple4;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE5")
	@Schema(name = "预留下拉多选字符串5")
	private String reserveddropdownmultiple5;


	@TableField(value = "RESERVEDSINGLECHOICE1")
	@Schema(name = "预留单选字符串1")
	private String reservedsinglechoice1;

	@TableField(value = "RESERVEDSINGLECHOICE2")
	@Schema(name = "预留单选字符串2")
	private String reservedsinglechoice2;

	@TableField(value = "RESERVEDSINGLECHOICE3")
	@Schema(name = "预留单选字符串3")
	private String reservedsinglechoice3;

	@TableField(value = "RESERVEDSINGLECHOICE4")
	@Schema(name = "预留单选字符串4")
	private String reservedsinglechoice4;

	@TableField(value = "RESERVEDSINGLECHOICE5")
	@Schema(name = "预留单选字符串5")
	private String reservedsinglechoice5;


	@TableField(value = "RESERVEDMULTIPLECHOICE1")
	@Schema(name = "预留多选字符串1")
	private String reservedmultiplechoice1;

	@TableField(value = "RESERVEDMULTIPLECHOICE2")
	@Schema(name = "预留多选字符串2")
	private String reservedmultiplechoice2;

	@TableField(value = "RESERVEDMULTIPLECHOICE3")
	@Schema(name = "预留多选字符串3")
	private String reservedmultiplechoice3;

	@TableField(value = "RESERVEDMULTIPLECHOICE4")
	@Schema(name = "预留多选字符串4")
	private String reservedmultiplechoice4;

	@TableField(value = "RESERVEDMULTIPLECHOICE5")
	@Schema(name = "预留多选字符串5")
	private String reservedmultiplechoice5;


	@TableField(value = "RESERVEDTIME1")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间1(年月日)")
	private Date reservedtime1;

	@TableField(value = "RESERVEDTIME2")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间2(年月日)")
	private Date reservedtime2;

	@TableField(value = "RESERVEDTIME3")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间3(年月日)")
	private Date reservedtime3;

	@TableField(value = "RESERVEDTIME4")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间4(年月日)")
	private Date reservedtime4;

	@TableField(value = "RESERVEDTIME5")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间5(年月日)")
	private Date reservedtime5;


	@TableField(value = "RESERVEDYEARTIME1")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间1(年)")
	private Date reservedyeartime1;

	@TableField(value = "RESERVEDYEARTIME2")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间2(年)")
	private Date reservedyeartime2;

	@TableField(value = "RESERVEDYEARTIME3")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间3(年)")
	private Date reservedyeartime3;

	@TableField(value = "RESERVEDYEARTIME4")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间4(年)")
	private Date reservedyeartime4;

	@TableField(value = "RESERVEDYEARTIME5")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间5(年)")
	private Date reservedyeartime5;


	@TableField(value = "RESERVEDYEARACCURATETIME1")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间1(年月日时分秒)")
	private Date reservedyearaccuratetime1;

	@TableField(value = "RESERVEDYEARACCURATETIME2")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间2(年月日时分秒)")
	private Date reservedyearaccuratetime2;

	@TableField(value = "RESERVEDYEARACCURATETIME3")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间3(年月日时分秒)")
	private Date reservedyearaccuratetime3;

	@TableField(value = "RESERVEDYEARACCURATETIME4")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间4(年月日时分秒)")
	private Date reservedyearaccuratetime4;

	@TableField(value = "RESERVEDYEARACCURATETIME5")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间5(年月日时分秒)")
	private Date reservedyearaccuratetime5;


	@TableField(value = "RESERVEDCONTENT1")
	@Schema(name = "预留富文本1")
	private String reservedcontent1;

	@TableField(value = "RESERVEDCONTENT2")
	@Schema(name = "预留富文本2")
	private String reservedcontent2;

	@TableField(value = "RESERVEDCONTENT3")
	@Schema(name = "预留富文本3")
	private String reservedcontent3;

	@TableField(value = "RESERVEDCONTENT4")
	@Schema(name = "预留富文本4")
	private String reservedcontent4;

	@TableField(value = "RESERVEDCONTENT5")
	@Schema(name = "预留富文本5")
	private String reservedcontent5;

	@TableField(value = "STAFFID1")
	@Schema(name = "预留人员单选1")
	private Long staffid1;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称1")
	private String realname1;

	@TableField(value = "STAFFID2")
	@Schema(name = "预留人员单选2")
	private Long staffid2;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称2")
	private String realname2;

	@TableField(value = "STAFFID3")
	@Schema(name = "预留人员单选3")
	private Long staffid3;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称3")
	private String realname3;

	@TableField(value = "STAFFID4")
	@Schema(name = "预留人员单选4")
	private Long staffid4;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称4")
	private String realname4;

	@TableField(value = "STAFFID5")
	@Schema(name = "预留人员单选5")
	private Long staffid5;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称5")
	private String realname5;

	@TableField(value = "STAFFIDS1")
	@Schema(name = "预留人员多选1")
	private String staffids1;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称1")
	private String realnames1;

	@TableField(value = "STAFFIDS2")
	@Schema(name = "预留人员多选2")
	private String staffids2;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称2")
	private String realnames2;

	@TableField(value = "STAFFIDS3")
	@Schema(name = "预留人员多选3")
	private String staffids3;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称3")
	private String realnames3;

	@TableField(value = "STAFFIDS4")
	@Schema(name = "预留人员多选4")
	private String staffids4;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称4")
	private String realnames4;

	@TableField(value = "STAFFIDS5")
	@Schema(name = "预留人员多选5")
	private String staffids5;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称5")
	private String realnames5;

	@TableField(value = "ORGID1")
	@Schema(name = "预留组织单选1")
	private Long orgid1;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称1")
	private String orgname1;

	@TableField(value = "ORGID2")
	@Schema(name = "预留组织单选2")
	private Long orgid2;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称2")
	private String orgname2;

	@TableField(value = "ORGID3")
	@Schema(name = "预留组织单选3")
	private Long orgid3;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称3")
	private String orgname3;

	@TableField(value = "ORGID4")
	@Schema(name = "预留组织单选4")
	private Long orgid4;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称4")
	private String orgname4;

	@TableField(value = "ORGID5")
	@Schema(name = "预留组织单选5")
	private Long orgid5;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称5")
	private String orgname5;

	@TableField(value = "ORGIDS1")
	@Schema(name = "预留组织多选1")
	private String orgids1;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称1")
	private String orgnames1;

	@TableField(value = "ORGIDS2")
	@Schema(name = "预留组织多选2")
	private String orgids2;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称2")
	private String orgnames2;

	@TableField(value = "ORGIDS3")
	@Schema(name = "预留组织多选3")
	private String orgids3;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称3")
	private String orgnames3;

	@TableField(value = "ORGIDS4")
	@Schema(name = "预留组织多选4")
	private String orgids4;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称4")
	private String orgnames4;

	@TableField(value = "ORGIDS5")
	@Schema(name = "预留组织多选5")
	private String orgids5;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称5")
	private String orgnames5;
}
