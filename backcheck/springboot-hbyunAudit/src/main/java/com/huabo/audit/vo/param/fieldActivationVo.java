package com.huabo.audit.vo.param;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class fieldActivationVo {
	
    private String content;
    private String desc;
	
	@Schema(name="预留字符串1")
	private String reservedstring1;

	@Schema(name="预留字符串2")
	private String reservedstring2;

	@Schema(name="预留字符串3")
	private String reservedstring3;

	@Schema(name="预留字符串4")
	private String reservedstring4;

	@Schema(name="预留字符串5")
	private String reservedstring5;

	@Schema(name="预留字符串6")
	private String reservedstring6;

	@Schema(name="预留字符串7")
	private String reservedstring7;

	@Schema(name="预留字符串8")
	private String reservedstring8;

	@Schema(name="预留字符串9")
	private String reservedstring9;

	@Schema(name="预留字符串10")
	private String reservedstring10;

	@Schema(name="预留大文本1")
	private String reservedcontent1;

	@Schema(name="预留大文本2")
	private String reservedcontent2;

	@Schema(name="预留大文本3")
	private String reservedcontent3;

	@Schema(name="预留大文本4")
	private String reservedcontent4;

	@Schema(name="预留大文本5")
	private String reservedcontent5;

	@Schema(name="预留大文本6")
	private String reservedcontent6;

	@Schema(name="预留大文本7")
	private String reservedcontent7;

	@Schema(name="预留大文本8")
	private String reservedcontent8;

	@Schema(name="预留大文本9")
	private String reservedcontent9;

	@Schema(name="预留大文本10")
	private String reservedcontent10;

	@Schema(name="预留下拉多选字符串1")
	private String reserveddropdownmultiple1;

	@Schema(name="预留下拉多选字符串2")
	private String reserveddropdownmultiple2;

	@Schema(name="预留下拉多选字符串3")
	private String reserveddropdownmultiple3;

	@Schema(name="预留下拉多选字符串4")
	private String reserveddropdownmultiple4;

	@Schema(name="预留下拉多选字符串5")
	private String reserveddropdownmultiple5;

	@Schema(name="预留多选字符串1")
	private String reservedmultiplechoice1;

	@Schema(name="预留多选字符串2")
	private String reservedmultiplechoice2;

	@Schema(name="预留多选字符串3")
	private String reservedmultiplechoice3;

	@Schema(name="预留多选字符串4")
	private String reservedmultiplechoice4;

	@Schema(name="预留多选字符串5")
	private String reservedmultiplechoice5;

	@Schema(name="预留年份1")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date reservedyeartime1;

	@Schema(name="预留年份2")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date reservedyeartime2;

	@Schema(name="预留年份3")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date reservedyeartime3;

	@Schema(name="预留年份4")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date reservedyeartime4;

	@Schema(name="预留年份5")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	private Date reservedyeartime5;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间1(年月日时分秒)")
	private Date reservedyearaccuratetime1;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间2(年月日时分秒)")
	private Date reservedyearaccuratetime2;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间3(年月日时分秒)")
	private Date reservedyearaccuratetime3;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间4(年月日时分秒)")
	private Date reservedyearaccuratetime4;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间5(年月日时分秒)")
	private Date reservedyearaccuratetime5;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间1(年月日)")
	private Date reservedtime1;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间2(年月日)")
	private Date reservedtime2;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间3(年月日)")
	private Date reservedtime3;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间4(年月日)")
	private Date reservedtime4;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间5(年月日)")
	private Date reservedtime5;

	@Schema(name="预留单选字符串1")
	private String reservedsinglechoice1;

	@Schema(name="预留单选字符串2")
	private String reservedsinglechoice2;

	@Schema(name="预留单选字符串3")
	private String reservedsinglechoice3;

	@Schema(name="预留单选字符串4")
	private String reservedsinglechoice4;

	@Schema(name="预留单选字符串5")
	private String reservedsinglechoice5;

	@Schema(name="预留下拉单选字符串1")
	private String reserveddropdownsinglechoice1;

	@Schema(name="预留下拉单选字符串2")
	private String reserveddropdownsinglechoice2;

	@Schema(name="预留下拉单选字符串3")
	private String reserveddropdownsinglechoice3;

	@Schema(name="预留下拉单选字符串4")
	private String reserveddropdownsinglechoice4;

	@Schema(name="预留下拉单选字符串5")
	private String reserveddropdownsinglechoice5;

	@Schema(name="预留数字1")
	private BigDecimal reservednum1;

	@Schema(name="预留数字2")
	private BigDecimal reservednum2;

	@Schema(name="预留数字3")
	private BigDecimal reservednum3;

	@Schema(name="预留数字4")
	private BigDecimal reservednum4;

	@Schema(name="预留数字5")
	private BigDecimal reservednum5;
	
	@Schema(name="预留人员单选1")
	private BigDecimal staffid1;
	
	@Schema(name="预留人员单选2")
	private BigDecimal staffid2;
	
	@Schema(name="预留人员单选3")
	private BigDecimal staffid3;
	
	@Schema(name="预留人员单选4")
	private BigDecimal staffid4;
	
	@Schema(name="预留人员单选5")
	private BigDecimal staffid5;

	@Schema(name="预留人员多选1")
	private String staffids1;
	
	@Schema(name="预留人员多选2")
	private String staffids2;
	
	@Schema(name="预留人员多选3")
	private String staffids3;
	
	@Schema(name="预留人员多选4")
	private String staffids4;
	
	@Schema(name="预留人员多选5")
	private String staffids5;
	
	@Schema(name="预留组织单选1")
	private BigDecimal orgid1;
	
	@Schema(name="预留组织单选2")
	private BigDecimal orgid2;

	@Schema(name="预留组织单选3")
	private BigDecimal orgid3;
	
	@Schema(name="预留组织单选4")
	private BigDecimal orgid4;
	
	@Schema(name="预留组织单选5")
	private BigDecimal orgid5;
	
	@Schema(name="预留组织多选1")
	private String orgids1;
	
	@Schema(name="预留组织多选2")
	private String orgids2;
	
	@Schema(name="预留组织多选3")
	private String orgids3;
	
	@Schema(name="预留组织多选4")
	private String orgids4;

	@Schema(name="预留组织多选5")
	private String orgids5;
	
}
