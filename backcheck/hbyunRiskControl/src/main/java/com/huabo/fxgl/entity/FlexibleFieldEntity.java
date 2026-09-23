package com.huabo.fxgl.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.fxgl.vo.fieldActivationVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlexibleFieldEntity {

    //预留字段
   	@TableField("RESERVEDSTRING1")
   	@Schema(name="预留字符串1")
   	private String reservedstring1;

   	@TableField("RESERVEDSTRING2")
   	@Schema(name="预留字符串2")
   	private String reservedstring2;

   	@TableField("RESERVEDSTRING3")
   	@Schema(name="预留字符串3")
   	private String reservedstring3;

   	@TableField("RESERVEDSTRING4")
   	@Schema(name="预留字符串4")
   	private String reservedstring4;

   	@TableField("RESERVEDSTRING5")
   	@Schema(name="预留字符串5")
   	private String reservedstring5;

   	@TableField("RESERVEDSTRING6")
   	@Schema(name="预留字符串6")
   	private String reservedstring6;

   	@TableField("RESERVEDSTRING7")
   	@Schema(name="预留字符串7")
   	private String reservedstring7;

   	@TableField("RESERVEDSTRING8")
   	@Schema(name="预留字符串8")
   	private String reservedstring8;

   	@TableField("RESERVEDSTRING9")
   	@Schema(name="预留字符串9")
   	private String reservedstring9;

   	@TableField("RESERVEDSTRING10")
   	@Schema(name="预留字符串10")
   	private String reservedstring10;

   	@TableField("RESERVEDCONTENT1")
   	@Schema(name="预留大文本1")
   	private String reservedcontent1;

   	@TableField("RESERVEDCONTENT2")
   	@Schema(name="预留大文本2")
   	private String reservedcontent2;

   	@TableField("RESERVEDCONTENT3")
   	@Schema(name="预留大文本3")
   	private String reservedcontent3;

   	@TableField("RESERVEDCONTENT4")
   	@Schema(name="预留大文本4")
   	private String reservedcontent4;

   	@TableField("RESERVEDCONTENT5")
   	@Schema(name="预留大文本5")
   	private String reservedcontent5;

   	@TableField("RESERVEDCONTENT6")
   	@Schema(name="预留大文本6")
   	private String reservedcontent6;

   	@TableField("RESERVEDCONTENT7")
   	@Schema(name="预留大文本7")
   	private String reservedcontent7;

   	@TableField("RESERVEDCONTENT8")
   	@Schema(name="预留大文本8")
   	private String reservedcontent8;

   	@TableField("RESERVEDCONTENT9")
   	@Schema(name="预留大文本9")
   	private String reservedcontent9;

   	@TableField("RESERVEDCONTENT10")
   	@Schema(name="预留大文本10")
   	private String reservedcontent10;

   	@TableField("RESERVEDDROPDOWNMULTIPLE1")
   	@Schema(name="预留下拉多选字符串1")
   	private String reserveddropdownmultiple1;

   	@TableField("RESERVEDDROPDOWNMULTIPLE2")
   	@Schema(name="预留下拉多选字符串2")
   	private String reserveddropdownmultiple2;

   	@TableField("RESERVEDDROPDOWNMULTIPLE3")
   	@Schema(name="预留下拉多选字符串3")
   	private String reserveddropdownmultiple3;

   	@TableField("RESERVEDDROPDOWNMULTIPLE4")
   	@Schema(name="预留下拉多选字符串4")
   	private String reserveddropdownmultiple4;

   	@TableField("RESERVEDDROPDOWNMULTIPLE5")
   	@Schema(name="预留下拉多选字符串5")
   	private String reserveddropdownmultiple5;

   	@TableField("RESERVEDMULTIPLECHOICE1")
   	@Schema(name="预留多选字符串1")
   	private String reservedmultiplechoice1;

   	@TableField("RESERVEDMULTIPLECHOICE2")
   	@Schema(name="预留多选字符串2")
   	private String reservedmultiplechoice2;

   	@TableField("RESERVEDMULTIPLECHOICE3")
   	@Schema(name="预留多选字符串3")
   	private String reservedmultiplechoice3;

   	@TableField("RESERVEDMULTIPLECHOICE4")
   	@Schema(name="预留多选字符串4")
   	private String reservedmultiplechoice4;

   	@TableField("RESERVEDMULTIPLECHOICE5")
   	@Schema(name="预留多选字符串5")
   	private String reservedmultiplechoice5;

   	@TableField("RESERVEDYEARTIME1")
   	@Schema(name="预留年份1")
   	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
   	@DateTimeFormat(pattern = "yyyy")
   	private Date reservedyeartime1;

   	@TableField("RESERVEDYEARTIME2")
   	@Schema(name="预留年份2")
   	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
   	@DateTimeFormat(pattern = "yyyy")
   	private Date reservedyeartime2;

   	@TableField("RESERVEDYEARTIME3")
   	@Schema(name="预留年份3")
   	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
   	@DateTimeFormat(pattern = "yyyy")
   	private Date reservedyeartime3;

   	@TableField("RESERVEDYEARTIME4")
   	@Schema(name="预留年份4")
   	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
   	@DateTimeFormat(pattern = "yyyy")
   	private Date reservedyeartime4;

   	@TableField("RESERVEDYEARTIME5")
   	@Schema(name="预留年份5")
   	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
   	@DateTimeFormat(pattern = "yyyy")
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

   	@TableField("RESERVEDSINGLECHOICE1")
   	@Schema(name="预留单选字符串1")
   	private String reservedsinglechoice1;

   	@TableField("RESERVEDSINGLECHOICE2")
   	@Schema(name="预留单选字符串2")
   	private String reservedsinglechoice2;

   	@TableField("RESERVEDSINGLECHOICE3")
   	@Schema(name="预留单选字符串3")
   	private String reservedsinglechoice3;

   	@TableField("RESERVEDSINGLECHOICE4")
   	@Schema(name="预留单选字符串4")
   	private String reservedsinglechoice4;

   	@TableField("RESERVEDSINGLECHOICE5")
   	@Schema(name="预留单选字符串5")
   	private String reservedsinglechoice5;

   	@TableField("RESERVEDDROPDOWNSINGLECHOICE1")
   	@Schema(name="预留下拉单选字符串1")
   	private String reserveddropdownsinglechoice1;

   	@TableField("RESERVEDDROPDOWNSINGLECHOICE2")
   	@Schema(name="预留下拉单选字符串2")
   	private String reserveddropdownsinglechoice2;

   	@TableField("RESERVEDDROPDOWNSINGLECHOICE3")
   	@Schema(name="预留下拉单选字符串3")
   	private String reserveddropdownsinglechoice3;

   	@TableField("RESERVEDDROPDOWNSINGLECHOICE4")
   	@Schema(name="预留下拉单选字符串4")
   	private String reserveddropdownsinglechoice4;

   	@TableField("RESERVEDDROPDOWNSINGLECHOICE5")
   	@Schema(name="预留下拉单选字符串5")
   	private String reserveddropdownsinglechoice5;

   	@TableField("RESERVEDNUM1")
   	@Schema(name="预留数字1")
   	private BigDecimal reservednum1;

   	@TableField("RESERVEDNUM2")
   	@Schema(name="预留数字2")
   	private BigDecimal reservednum2;

   	@TableField("RESERVEDNUM3")
   	@Schema(name="预留数字3")
   	private BigDecimal reservednum3;

   	@TableField("RESERVEDNUM4")
   	@Schema(name="预留数字4")
   	private BigDecimal reservednum4;

   	@TableField("RESERVEDNUM5")
   	@Schema(name="预留数字5")
   	private BigDecimal reservednum5;

   	@TableField("STAFFID1")
   	@Schema(name="预留人员单选1")
   	private BigDecimal staffid1;

   	@TableField("STAFFID2")
   	@Schema(name="预留人员单选2")
   	private BigDecimal staffid2;

   	@TableField("STAFFID3")
   	@Schema(name="预留人员单选3")
   	private BigDecimal staffid3;

   	@TableField("STAFFID4")
   	@Schema(name="预留人员单选4")
   	private BigDecimal staffid4;

   	@TableField("STAFFID5")
   	@Schema(name="预留人员单选5")
   	private BigDecimal staffid5;

   	@TableField("STAFFIDS1")
   	@Schema(name="预留人员多选1")
   	private String staffids1;

   	@TableField("STAFFIDS2")
   	@Schema(name="预留人员多选2")
   	private String staffids2;

   	@TableField("STAFFIDS3")
   	@Schema(name="预留人员多选3")
   	private String staffids3;

   	@TableField("STAFFIDS4")
   	@Schema(name="预留人员多选4")
   	private String staffids4;

   	@TableField("STAFFIDS5")
   	@Schema(name="预留人员多选5")
   	private String staffids5;

   	@TableField("ORGID1")
   	@Schema(name="预留组织单选1")
   	private BigDecimal orgid1;

   	@TableField("ORGID2")
   	@Schema(name="预留组织单选2")
   	private BigDecimal orgid2;

   	@TableField("ORGID3")
   	@Schema(name="预留组织单选3")
   	private BigDecimal orgid3;

   	@TableField("ORGID4")
   	@Schema(name="预留组织单选4")
   	private BigDecimal orgid4;

   	@TableField("ORGID5")
   	@Schema(name="预留组织单选5")
   	private BigDecimal orgid5;

   	@TableField("ORGIDS1")
   	@Schema(name="预留组织多选1")
   	private String orgids1;

   	@TableField("ORGIDS2")
   	@Schema(name="预留组织多选2")
   	private String orgids2;

   	@TableField("ORGIDS3")
   	@Schema(name="预留组织多选3")
   	private String orgids3;

   	@TableField("ORGIDS4")
   	@Schema(name="预留组织多选4")
   	private String orgids4;

   	@TableField("ORGIDS5")
   	@Schema(name="预留组织多选5")
   	private String orgids5;
   	    
   	//人员及组织查询赋值
   	@TableField(exist = false)
   	@Schema(name="预留人员单选name1")
   	private String staffidname1;

   	@TableField(exist = false)
   	@Schema(name="预留人员单选name2")
   	private String staffidname2;

   	@TableField(exist = false)
   	@Schema(name="预留人员单选name3")
   	private String staffidname3;

   	@TableField(exist = false)
   	@Schema(name="预留人员单选name4")
   	private String staffidname4;

   	@TableField(exist = false)
   	@Schema(name="预留人员单选name5")
   	private String staffidname5;

   	@TableField(exist = false)
   	@Schema(name="预留人员多选name1")
   	private String staffidsname1;

   	@TableField(exist = false)
   	@Schema(name="预留人员多选name2")
   	private String staffidsname2;

   	@TableField(exist = false)
   	@Schema(name="预留人员多选name3")
   	private String staffidsname3;

   	@TableField(exist = false)
   	@Schema(name="预留人员多选name4")
   	private String staffidsname4;

   	@TableField(exist = false)
   	@Schema(name="预留人员多选name5")
   	private String staffidsname5;

   	@TableField(exist = false)
   	@Schema(name="预留组织单选name1")
   	private String orgidname1;

   	@TableField(exist = false)
   	@Schema(name="预留组织单选name2")
   	private String orgidname2;

   	@TableField(exist = false)
   	@Schema(name="预留组织单选name3")
   	private String orgidname3;

   	@TableField(exist = false)
   	@Schema(name="预留组织单选name4")
   	private String orgidname4;

   	@TableField(exist = false)
   	@Schema(name="预留组织单选name5")
   	private String orgidname5;

   	@TableField(exist = false)
   	@Schema(name="预留组织多选name1")
   	private String orgidsname1;

   	@TableField(exist = false)
   	@Schema(name="预留组织多选name2")
   	private String orgidsname2;

   	@TableField(exist = false)
   	@Schema(name="预留组织多选name3")
   	private String orgidsname3;

   	@TableField(exist = false)
   	@Schema(name="预留组织多选name4")
   	private String orgidsname4;

   	@TableField(exist = false)
   	@Schema(name="预留组织多选name5")
   	private String orgidsname5;
   	
   	
	//查询条件赋值到主表
   	public void setFieldActivationCopy(fieldActivationVo file){
   		this.reservedstring1=file.getReservedstring1();
   		this.reservedstring2=file.getReservedstring2();
   		this.reservedstring3=file.getReservedstring3();
   		this.reservedstring4=file.getReservedstring4();
   		this.reservedstring5=file.getReservedstring5();
   		this.reservedstring6=file.getReservedstring6();
   		this.reservedstring7=file.getReservedstring7();
   		this.reservedstring8=file.getReservedstring8();
   		this.reservedstring9=file.getReservedstring9();
   		this.reservedstring10=file.getReservedstring10();
   		this.reservedcontent1=file.getReservedcontent1();
   		this.reservedcontent2=file.getReservedcontent2();
   		this.reservedcontent3=file.getReservedcontent3();
   		this.reservedcontent4=file.getReservedcontent4();
   		this.reservedcontent5=file.getReservedcontent5();
   		this.reservedcontent6=file.getReservedcontent6();
   		this.reservedcontent7=file.getReservedcontent7();
   		this.reservedcontent8=file.getReservedcontent8();
   		this.reservedcontent9=file.getReservedcontent9();
   		this.reservedcontent10=file.getReservedcontent10();
           this.reserveddropdownmultiple1=file.getReserveddropdownmultiple1();
           this.reserveddropdownmultiple2=file.getReserveddropdownmultiple2();
           this.reserveddropdownmultiple3=file.getReserveddropdownmultiple3();
           this.reserveddropdownmultiple4=file.getReserveddropdownmultiple4();
           this.reserveddropdownmultiple5=file.getReserveddropdownmultiple5();
           this.reservedmultiplechoice1=file.getReservedmultiplechoice1();
           this.reservedmultiplechoice2=file.getReservedmultiplechoice2();
           this.reservedmultiplechoice3=file.getReservedmultiplechoice3();
           this.reservedmultiplechoice4=file.getReservedmultiplechoice4();
           this.reservedmultiplechoice5=file.getReservedmultiplechoice5();
   		this.reservedyeartime1=file.getReservedyeartime1();
   		this.reservedyeartime2 = file.getReservedyeartime2();
   		this.reservedyeartime3 = file.getReservedyeartime3();
   		this.reservedyeartime4 = file.getReservedyeartime4();
   		this.reservedyeartime5 = file.getReservedyeartime5();
   		this.reservedyearaccuratetime1 = file.getReservedyearaccuratetime1();
   		this.reservedyearaccuratetime2 = file.getReservedyearaccuratetime2();
   		this.reservedyearaccuratetime3 = file.getReservedyearaccuratetime3();
   		this.reservedyearaccuratetime4 = file.getReservedyearaccuratetime4();
   		this.reservedyearaccuratetime5 = file.getReservedyearaccuratetime5();
   		this.reservedtime1 = file.getReservedtime1();
   		this.reservedtime2 = file.getReservedtime2();
   		this.reservedtime3 = file.getReservedtime3();
   		this.reservedtime4 = file.getReservedtime4();
   		this.reservedtime5 = file.getReservedtime5();
   		this.reservedsinglechoice1 = file.getReservedsinglechoice1();
   		this.reservedsinglechoice2 = file.getReservedsinglechoice2();
   		this.reservedsinglechoice3 = file.getReservedsinglechoice3();
   		this.reservedsinglechoice4 = file.getReservedsinglechoice4();
   		this.reservedsinglechoice5 = file.getReservedsinglechoice5();
           this.reserveddropdownsinglechoice1=file.getReserveddropdownsinglechoice1();
           this.reserveddropdownsinglechoice2=file.getReserveddropdownsinglechoice2();
           this.reserveddropdownsinglechoice3=file.getReserveddropdownsinglechoice3();
           this.reserveddropdownsinglechoice4=file.getReserveddropdownsinglechoice4();
           this.reserveddropdownsinglechoice5=file.getReserveddropdownsinglechoice5();
           this.reservednum1=file.getReservednum1();
           this.reservednum2=file.getReservednum2();
           this.reservednum3=file.getReservednum3();
           this.reservednum4=file.getReservednum4();
           this.reservednum5=file.getReservednum5();
           this.staffid1=file.getStaffid1();
           this.staffid2=file.getStaffid2();
           this.staffid3=file.getStaffid3();
           this.staffid4=file.getStaffid4();
           this.staffid5=file.getStaffid5();
           this.staffids1=file.getStaffids1();
           this.staffids2=file.getStaffids2();
           this.staffids3=file.getStaffids3();
           this.staffids4=file.getStaffids4();
           this.staffids5=file.getStaffids5();
           this.orgid1=file.getOrgid1();
           this.orgid2=file.getOrgid2();
           this.orgid3=file.getOrgid3();
           this.orgid4=file.getOrgid4();
           this.orgid5=file.getOrgid5();
           this.orgids1=file.getOrgids1();
           this.orgids2=file.getOrgids2();
           this.orgids3=file.getOrgids3();
           this.orgids4=file.getOrgids4();
           this.orgids5=file.getOrgids5();
   	}
   	
}
