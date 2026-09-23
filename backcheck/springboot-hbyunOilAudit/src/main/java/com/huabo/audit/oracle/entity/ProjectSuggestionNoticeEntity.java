package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

/**
 * @author Rui
 * @ClassName ProjectSuggestionNoticeEntity
 * @Description
 * @DATE 2024/03/21
 */
@Data
@TableName("TBL_YQNS_PS_NOTICE")
@Schema(name="审计立项建议通知实体")
@Accessors(chain = true)
public class ProjectSuggestionNoticeEntity implements Serializable {

    @TableId(value="ID",  type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @TableField(value="NOTICENO")
    @Schema(name="编号")
    private String noticeNo;
    
    @TableField(value="NAME")
    @Schema(name="通知名称")
    private String name;

    @TableField(value="ORG_IDS")
    @Schema(name="通知单位IDS，多个用逗号分割")
    private String orgIds;
    
    @TableField(exist = false)
    @Schema(name="通知单位名称")
    private String orgname;

    @TableField(exist = false)
    private List<TblOrganization> organizations;

    @TableField(value="PS_IDS")
    @Schema(name="立项建议表IDS，多个用逗号分割")
    private String psIds;

    @TableField(exist = false)
    private List<ProjectSuggestionEntity> projectSuggestions;

    @TableField(value="CONTENT") 
    @Schema(name="通知内容")
    private String content;

    @TableField(value="REMARK")
    @Schema(name="备注")
    private String remark;

    
    @TableField(exist = false)
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人Id")
    @Column(name = "CREATE_USER")
    private String createUserId;

    
    @TableField(exist = false)
    @Schema(name="创建人名称")
    private String createname;
    
    
    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createTime;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private String attids;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;

    @TableField(value="PERSON_IDS")
    @Schema(name="下发的人员ID")
    private String personIds;

    @TableField(exist = false)
    @Schema(name="用户查看数据权限部门")
    private String queryDeptIds;
    
    @TableField(value="STATUS")
    @Schema(name="审批状态")
    private Integer status;
    
    

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
	
	@TableField(value = "RESERVEDCONTENT6")
	@Schema(name = "预留富文本6")
	private String reservedcontent6;
	
	@TableField(value = "RESERVEDCONTENT7")
	@Schema(name = "预留富文本7")
	private String reservedcontent7;
	
	
	@TableField(value = "RESERVEDCONTENT8")
	@Schema(name = "预留富文本8")
	private String reservedcontent8;
	
	@TableField(value = "RESERVEDCONTENT9")
	@Schema(name = "预留富文本9")
	private String reservedcontent9;
	
	@TableField(value = "RESERVEDCONTENT10")
	@Schema(name = "预留富文本10")
	private String reservedcontent10;
	

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
	private Date reservedyearaccuratetime45;



	// 预留数字（数字输入框）5个
	@TableField(value = "RESERVEDNUM1")
	@Schema(name = "预留数字1")
	private Integer reservedNum1;

	@TableField(value = "RESERVEDNUM2")
	@Schema(name = "预留数字2")
	private Integer reservedNum2;

	@TableField(value = "RESERVEDNUM3")
	@Schema(name = "预留数字3")
	private Integer reservedNum3;

	@TableField(value = "RESERVEDNUM4")
	@Schema(name = "预留数字4")
	private Integer reservedNum4;

	@TableField(value = "RESERVEDNUM5")
	@Schema(name = "预留数字5")
	private Integer reservedNum5;

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
	
	
	@TableField(value = "STAFFID1")
	@Schema(name="预留选择人员1")
	private BigDecimal staffid1;
	
	@TableField(exist = false)
	@Schema(name="预留选择人员1")
	private String realname1;
	
	
	@TableField(value = "STAFFID2")
	@Schema(name="预留选择人员2")
	private BigDecimal staffid2;
	
	@TableField(exist = false)
	@Schema(name="预留选择人员2")
	private String realname2;
	
	@TableField(value = "STAFFIDS1")
	@Schema(name="预留多选择人员1")
	private String staffids1;
	
	@TableField(exist = false)
	@Schema(name="预留多选择人员1")
	private String realnames1;
	
	
	@TableField(value = "STAFFID2")
	@Schema(name="预留多选择人员2")
	private String staffids2;
	
	@TableField(exist = false)
	@Schema(name="预留多选择人员2")
	private String realnames2;
	
    
	
	@TableField(value = "ORGID1")
	@Schema(name="预留选择组织1")
	private BigDecimal orgid1;
	
	@TableField(exist = false)
	@Schema(name="预留选择组织1")
	private String orgname1;
	
	
	@TableField(value = "ORGID2")
	@Schema(name="预留选择组织1")
	private BigDecimal orgid2;
	
	@TableField(exist = false)
	@Schema(name="预留选择组织1")
	private String orgname2;
	
	@TableField(value = "ORGIDS1")
	@Schema(name="预留多选择组织1")
	private String orgids1;
	
	@TableField(exist = false)
	@Schema(name="预留多选择组织1")
	private String orgnames1;
	
	
	@TableField(value = "ORGIDS2")
	@Schema(name="预留多选择组织1")
	private String orgids2;
	
	@TableField(exist = false)
	@Schema(name="预留多选择组织1")
	private String orgnames2;
    
}
