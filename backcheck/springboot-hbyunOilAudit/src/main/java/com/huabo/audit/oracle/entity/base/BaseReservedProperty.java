package com.huabo.audit.oracle.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program:
 * @description:
 * @author:
 * @create: 预留字段
 **/
@Data
public class BaseReservedProperty implements Serializable {
    private static final long serialVersionUID = 1L;

    // 预留字符串（输入框）10个
    @Column(name = "RESERVEDSTRING1")
    @TableField(value = "RESERVEDSTRING1")
    @Schema(name = "预留字符串1")
    private String reservedString1;

    @Column(name = "RESERVEDSTRING2")
    @TableField(value = "RESERVEDSTRING2")
    @Schema(name = "预留字符串2")
    private String reservedString2;

    @Column(name = "RESERVEDSTRING3")
    @TableField(value = "RESERVEDSTRING3")
    @Schema(name = "预留字符串3")
    private String reservedString3;

    @Column(name = "RESERVEDSTRING4")
    @TableField(value = "RESERVEDSTRING4")
    @Schema(name = "预留字符串4")
    private String reservedString4;

    @Column(name = "RESERVEDSTRING5")
    @TableField(value = "RESERVEDSTRING5")
    @Schema(name = "预留字符串5")
    private String reservedString5;

    @Column(name = "RESERVEDSTRING6")
    @TableField(value = "RESERVEDSTRING6")
    @Schema(name = "预留字符串6")
    private String reservedString6;

    @Column(name = "RESERVEDSTRING7")
    @TableField(value = "RESERVEDSTRING7")
    @Schema(name = "预留字符串7")
    private String reservedString7;

    @Column(name = "RESERVEDSTRING8")
    @TableField(value = "RESERVEDSTRING8")
    @Schema(name = "预留字符串8")
    private String reservedString8;

    @Column(name = "RESERVEDSTRING9")
    @TableField(value = "RESERVEDSTRING9")
    @Schema(name = "预留字符串9")
    private String reservedString9;

    @Column(name = "RESERVEDSTRING10")
    @TableField(value = "RESERVEDSTRING10")
    @Schema(name = "预留字符串10")
    private String reservedString10;

    // 预留大文本（文本域）10个
    @Column(name = "RESERVEDCONTENT1")
    @TableField(value = "RESERVEDCONTENT1")
    @Schema(name = "预留大文本1")
    private String reservedContent1;

    @Column(name = "RESERVEDCONTENT2")
    @TableField(value = "RESERVEDCONTENT2")
    @Schema(name = "预留大文本2")
    private String reservedContent2;

    @Column(name = "RESERVEDCONTENT3")
    @TableField(value = "RESERVEDCONTENT3")
    @Schema(name = "预留大文本3")
    private String reservedContent3;

    @Column(name = "RESERVEDCONTENT4")
    @TableField(value = "RESERVEDCONTENT4")
    @Schema(name = "预留大文本4")
    private String reservedContent4;

    @Column(name = "RESERVEDCONTENT5")
    @TableField(value = "RESERVEDCONTENT5")
    @Schema(name = "预留大文本5")
    private String reservedContent5;

    @Column(name = "RESERVEDCONTENT6")
    @TableField(value = "RESERVEDCONTENT6")
    @Schema(name = "预留大文本6")
    private String reservedContent6;

    @Column(name = "RESERVEDCONTENT7")
    @TableField(value = "RESERVEDCONTENT7")
    @Schema(name = "预留大文本7")
    private String reservedContent7;

    @Column(name = "RESERVEDCONTENT8")
    @TableField(value = "RESERVEDCONTENT8")
    @Schema(name = "预留大文本8")
    private String reservedContent8;

    @Column(name = "RESERVEDCONTENT9")
    @TableField(value = "RESERVEDCONTENT9")
    @Schema(name = "预留大文本9")
    private String reservedContent9;

    @Column(name = "RESERVEDCONTENT10")
    @TableField(value = "RESERVEDCONTENT10")
    @Schema(name = "预留大文本10")
    private String reservedContent10;

    // 预留下拉多选字符串（多选下拉）5个
    @Column(name = "RESERVEDDROPDOWNMULTIPLE1")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE1")
    @Schema(name = "预留下拉多选字符串1")
    private String reservedDropdownMultiple1;

    @Column(name = "RESERVEDDROPDOWNMULTIPLE2")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE2")
    @Schema(name = "预留下拉多选字符串2")
    private String reservedDropdownMultiple2;

    @Column(name = "RESERVEDDROPDOWNMULTIPLE3")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE3")
    @Schema(name = "预留下拉多选字符串3")
    private String reservedDropdownMultiple3;

    @Column(name = "RESERVEDDROPDOWNMULTIPLE4")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE4")
    @Schema(name = "预留下拉多选字符串4")
    private String reservedDropdownMultiple4;

    @Column(name = "RESERVEDDROPDOWNMULTIPLE5")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE5")
    @Schema(name = "预留下拉多选字符串5")
    private String reservedDropdownMultiple5;

    // 预留多选字符串（多选框）5个
    @Column(name = "RESERVEDMULTIPLECHOICE1")
    @TableField(value = "RESERVEDMULTIPLECHOICE1")
    @Schema(name = "预留多选字符串1")
    private String reservedMultipleChoice1;

    @Column(name = "RESERVEDMULTIPLECHOICE2")
    @TableField(value = "RESERVEDMULTIPLECHOICE2")
    @Schema(name = "预留多选字符串2")
    private String reservedMultipleChoice2;

    @Column(name = "RESERVEDMULTIPLECHOICE3")
    @TableField(value = "RESERVEDMULTIPLECHOICE3")
    @Schema(name = "预留多选字符串3")
    private String reservedMultipleChoice3;

    @Column(name = "RESERVEDMULTIPLECHOICE4")
    @TableField(value = "RESERVEDMULTIPLECHOICE4")
    @Schema(name = "预留多选字符串4")
    private String reservedMultipleChoice4;

    @Column(name = "RESERVEDMULTIPLECHOICE5")
    @TableField(value = "RESERVEDMULTIPLECHOICE5")
    @Schema(name = "预留多选字符串5")
    private String reservedMultipleChoice5;

    // 预留年份（年份）5个
    @Column(name = "RESERVEDYEARTIME1")
    @TableField(value = "RESERVEDYEARTIME1")
    @Schema(name = "预留年份1")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    private Date reservedYearTime1;

    @Column(name = "RESERVEDYEARTIME2")
    @TableField(value = "RESERVEDYEARTIME2")
    @Schema(name = "预留年份2")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    private Date reservedYearTime2;

    @Column(name = "RESERVEDYEARTIME3")
    @TableField(value = "RESERVEDYEARTIME3")
    @Schema(name = "预留年份3")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    private Date reservedYearTime3;

    @Column(name = "RESERVEDYEARTIME4")
    @TableField(value = "RESERVEDYEARTIME4")
    @Schema(name = "预留年份4")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    private Date reservedYearTime4;

    @Column(name = "RESERVEDYEARTIME5")
    @TableField(value = "RESERVEDYEARTIME5")
    @Schema(name = "预留年份5")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    private Date reservedYearTime5;

    // 预留时间（日期（年月日时分秒））5个
    @Column(name = "RESERVEDYEARACCURATETIME1")
    @TableField(value = "RESERVEDYEARACCURATETIME1")
    @Schema(name = "预留时间1")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reservedYearAccurateTime1;

    @Column(name = "RESERVEDYEARACCURATETIME2")
    @TableField(value = "RESERVEDYEARACCURATETIME2")
    @Schema(name = "预留时间2")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reservedYearAccurateTime2;

    @Column(name = "RESERVEDYEARACCURATETIME3")
    @TableField(value = "RESERVEDYEARACCURATETIME3")
    @Schema(name = "预留时间3")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reservedYearAccurateTime3;

    @Column(name = "RESERVEDYEARACCURATETIME4")
    @TableField(value = "RESERVEDYEARACCURATETIME4")
    @Schema(name = "预留时间4")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reservedYearAccurateTime4;

    @Column(name = "RESERVEDYEARACCURATETIME5")
    @TableField(value = "RESERVEDYEARACCURATETIME5")
    @Schema(name = "预留时间5")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reservedYearAccurateTime5;

    // 预留年月日（日期（年月日））5个
    @Column(name = "RESERVEDTIME1")
    @TableField(value = "RESERVEDTIME1")
    @Schema(name = "预留年月日1")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reservedTime1;

    @Column(name = "RESERVEDTIME2")
    @TableField(value = "RESERVEDTIME2")
    @Schema(name = "预留年月日2")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reservedTime2;

    @Column(name = "RESERVEDTIME3")
    @TableField(value = "RESERVEDTIME3")
    @Schema(name = "预留年月日3")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reservedTime3;

    @Column(name = "RESERVEDTIME4")
    @TableField(value = "RESERVEDTIME4")
    @Schema(name = "预留年月日4")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reservedTime4;

    @Column(name = "RESERVEDTIME5")
    @TableField(value = "RESERVEDTIME5")
    @Schema(name = "预留年月日5")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reservedTime5;

    // 预留单选字符串（单选框）5个
    @Column(name = "RESERVEDSINGLECHOICE1")
    @TableField(value = "RESERVEDSINGLECHOICE1")
    @Schema(name = "预留单选字符串1")
    private String reservedSingleChoice1;

    @Column(name = "RESERVEDSINGLECHOICE2")
    @TableField(value = "RESERVEDSINGLECHOICE2")
    @Schema(name = "预留单选字符串2")
    private String reservedSingleChoice2;

    @Column(name = "RESERVEDSINGLECHOICE3")
    @TableField(value = "RESERVEDSINGLECHOICE3")
    @Schema(name = "预留单选字符串3")
    private String reservedSingleChoice3;

    @Column(name = "RESERVEDSINGLECHOICE4")
    @TableField(value = "RESERVEDSINGLECHOICE4")
    @Schema(name = "预留单选字符串4")
    private String reservedSingleChoice4;

    @Column(name = "RESERVEDSINGLECHOICE5")
    @TableField(value = "RESERVEDSINGLECHOICE5")
    @Schema(name = "预留单选字符串5")
    private String reservedSingleChoice5;

    // 预留下拉单选字符串（单选下拉框）5个
    @Column(name = "RESERVEDDROPDOWNSINGLECHOICE1")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE1")
    @Schema(name = "预留下拉单选字符串1")
    private String reservedDropdownSingleChoice1;

    @Column(name = "RESERVEDDROPDOWNSINGLECHOICE2")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE2")
    @Schema(name = "预留下拉单选字符串2")
    private String reservedDropdownSingleChoice2;

    @Column(name = "RESERVEDDROPDOWNSINGLECHOICE3")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE3")
    @Schema(name = "预留下拉单选字符串3")
    private String reservedDropdownSingleChoice3;

    @Column(name = "RESERVEDDROPDOWNSINGLECHOICE4")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE4")
    @Schema(name = "预留下拉单选字符串4")
    private String reservedDropdownSingleChoice4;

    @Column(name = "RESERVEDDROPDOWNSINGLECHOICE5")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE5")
    @Schema(name = "预留下拉单选字符串5")
    private String reservedDropdownSingleChoice5;

    // 预留数字（数字输入框）5个
    @Column(name = "RESERVEDNUM1")
    @TableField(value = "RESERVEDNUM1")
    @Schema(name = "预留数字1")
    private Integer reservedNum1;

    @Column(name = "RESERVEDNUM2")
    @TableField(value = "RESERVEDNUM2")
    @Schema(name = "预留数字2")
    private Integer reservedNum2;

    @Column(name = "RESERVEDNUM3")
    @TableField(value = "RESERVEDNUM3")
    @Schema(name = "预留数字3")
    private Integer reservedNum3;

    @Column(name = "RESERVEDNUM4")
    @TableField(value = "RESERVEDNUM4")
    @Schema(name = "预留数字4")
    private Integer reservedNum4;

    @Column(name = "RESERVEDNUM5")
    @TableField(value = "RESERVEDNUM5")
    @Schema(name = "预留数字5")
    private Integer reservedNum5;

    // 预留人员单选 5个
    @Column(name = "STAFFID1")
    @TableField(value = "STAFFID1")
    @Schema(name = "预留人员单选1")
    private BigDecimal staffid1;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名1")
    private String realname1;

    @Column(name = "STAFFID2")
    @TableField(value = "STAFFID2")
    @Schema(name = "预留人员单选2")
    private BigDecimal staffid2;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名2")
    private String realname2;

    @Column(name = "STAFFID3")
    @TableField(value = "STAFFID3")
    @Schema(name = "预留人员单选3")
    private BigDecimal staffid3;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名3")
    private String realname3;

    @Column(name = "STAFFID4")
    @TableField(value = "STAFFID4")
    @Schema(name = "预留人员单选4")
    private BigDecimal staffid4;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名4")
    private String realname4;

    @Column(name = "STAFFID5")
    @TableField(value = "STAFFID5")
    @Schema(name = "预留人员单选5")
    private BigDecimal staffid5;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名5")
    private String realname5;

    // 预留人员多选 5个
    @Column(name = "STAFFIDS1")
    @TableField(value = "STAFFIDS1")
    @Schema(name = "预留人员多选1")
    private String staffids1;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表1")
    private String realnames1;

    @Column(name = "STAFFIDS2")
    @TableField(value = "STAFFIDS2")
    @Schema(name = "预留人员多选2")
    private String staffids2;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表2")
    private String realnames2;

    @Column(name = "STAFFIDS3")
    @TableField(value = "STAFFIDS3")
    @Schema(name = "预留人员多选3")
    private String staffids3;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表3")
    private String realnames3;

    @Column(name = "STAFFIDS4")
    @TableField(value = "STAFFIDS4")
    @Schema(name = "预留人员多选4")
    private String staffids4;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表4")
    private String realnames4;

    @Column(name = "STAFFIDS5")
    @TableField(value = "STAFFIDS5")
    @Schema(name = "预留人员多选5")
    private String staffids5;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表5")
    private String realnames5;

    // 预留组织单选 5个
    @Column(name = "ORGID1")
    @TableField(value = "ORGID1")
    @Schema(name = "预留组织单选1")
    private BigDecimal orgid1;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称1")
    private String orgname1;

    @Column(name = "ORGID2")
    @TableField(value = "ORGID2")
    @Schema(name = "预留组织单选2")
    private BigDecimal orgid2;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称2")
    private String orgname2;

    @Column(name = "ORGID3")
    @TableField(value = "ORGID3")
    @Schema(name = "预留组织单选3")
    private BigDecimal orgid3;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称3")
    private String orgname3;

    @Column(name = "ORGID4")
    @TableField(value = "ORGID4")
    @Schema(name = "预留组织单选4")
    private BigDecimal orgid4;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称4")
    private String orgname4;

    @Column(name = "ORGID5")
    @TableField(value = "ORGID5")
    @Schema(name = "预留组织单选5")
    private BigDecimal orgid5;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称5")
    private String orgname5;

    // 预留组织多选 5个
    @Column(name = "ORGIDS1")
    @TableField(value = "ORGIDS1")
    @Schema(name = "预留组织多选1")
    private String orgids1;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表1")
    private String orgnames1;

    @Column(name = "ORGIDS2")
    @TableField(value = "ORGIDS2")
    @Schema(name = "预留组织多选2")
    private String orgids2;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表2")
    private String orgnames2;

    @Column(name = "ORGIDS3")
    @TableField(value = "ORGIDS3")
    @Schema(name = "预留组织多选3")
    private String orgids3;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表3")
    private String orgnames3;

    @Column(name = "ORGIDS4")
    @TableField(value = "ORGIDS4")
    @Schema(name = "预留组织多选4")
    private String orgids4;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表4")
    private String orgnames4;

    @Column(name = "ORGIDS5")
    @TableField(value = "ORGIDS5")
    @Schema(name = "预留组织多选5")
    private String orgids5;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表5")
    private String orgnames5;

}
