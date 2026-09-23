package com.huabo.audit.oracle.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.service.impl.TblOrganizaServiceImpl;
import com.huabo.audit.service.impl.TblStaffServiceImpl;
import com.huabo.audit.util.SpringContextHolder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program:
 * @description:
 * @author:
 * @create: 预留字段
 **/
@Getter
@Setter
public class ReservedEntity {


    // 预留字符串（输入框）10个
    @Column(name="RESERVEDSTRING1")
    @TableField(value = "RESERVEDSTRING1")
    @Schema(name = "预留字符串1")
    private String reservedString1;

    @Column(name="RESERVEDSTRING2")
    @TableField(value = "RESERVEDSTRING2")
    @Schema(name = "预留字符串2")
    private String reservedString2;

    @Column(name="RESERVEDSTRING3")
    @TableField(value = "RESERVEDSTRING3")
    @Schema(name = "预留字符串3")
    private String reservedString3;

    @Column(name="RESERVEDSTRING4")
    @TableField(value = "RESERVEDSTRING4")
    @Schema(name = "预留字符串4")
    private String reservedString4;

    @Column(name="RESERVEDSTRING5")
    @TableField(value = "RESERVEDSTRING5")
    @Schema(name = "预留字符串5")
    private String reservedString5;

    @Column(name="RESERVEDSTRING6")
    @TableField(value = "RESERVEDSTRING6")
    @Schema(name = "预留字符串6")
    private String reservedString6;

    @Column(name="RESERVEDSTRING7")
    @TableField(value = "RESERVEDSTRING7")
    @Schema(name = "预留字符串7")
    private String reservedString7;

    @Column(name="RESERVEDSTRING8")
    @TableField(value = "RESERVEDSTRING8")
    @Schema(name = "预留字符串8")
    private String reservedString8;

    @Column(name="RESERVEDSTRING9")
    @TableField(value = "RESERVEDSTRING9")
    @Schema(name = "预留字符串9")
    private String reservedString9;

    @Column(name="RESERVEDSTRING10")
    @TableField(value = "RESERVEDSTRING10")
    @Schema(name = "预留字符串10")
    private String reservedString10;

    // 预留大文本（文本域）10个
    @Column(name="RESERVEDCONTENT1")
    @TableField(value = "RESERVEDCONTENT1")
    @Schema(name = "预留大文本1")
    private String reservedContent1;

    @Column(name="RESERVEDCONTENT2")
    @TableField(value = "RESERVEDCONTENT2")
    @Schema(name = "预留大文本2")
    private String reservedContent2;

    @Column(name="RESERVEDCONTENT3")
    @TableField(value = "RESERVEDCONTENT3")
    @Schema(name = "预留大文本3")
    private String reservedContent3;

    @Column(name="RESERVEDCONTENT4")
    @TableField(value = "RESERVEDCONTENT4")
    @Schema(name = "预留大文本4")
    private String reservedContent4;

    @Column(name="RESERVEDCONTENT5")
    @TableField(value = "RESERVEDCONTENT5")
    @Schema(name = "预留大文本5")
    private String reservedContent5;

    @Column(name="RESERVEDCONTENT6")
    @TableField(value = "RESERVEDCONTENT6")
    @Schema(name = "预留大文本6")
    private String reservedContent6;

    @Column(name="RESERVEDCONTENT7")
    @TableField(value = "RESERVEDCONTENT7")
    @Schema(name = "预留大文本7")
    private String reservedContent7;

    @Column(name="RESERVEDCONTENT8")
    @TableField(value = "RESERVEDCONTENT8")
    @Schema(name = "预留大文本8")
    private String reservedContent8;

    @Column(name="RESERVEDCONTENT9")
    @TableField(value = "RESERVEDCONTENT9")
    @Schema(name = "预留大文本9")
    private String reservedContent9;

    @Column(name="RESERVEDCONTENT10")
    @TableField(value = "RESERVEDCONTENT10")
    @Schema(name = "预留大文本10")
    private String reservedContent10;

    // 预留下拉多选字符串（多选下拉）5个
    @Column(name="RESERVEDDROPDOWNMULTIPLE1")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE1")
    @Schema(name = "预留下拉多选字符串1")
    private String reservedDropdownMultiple1;

    @Column(name="RESERVEDDROPDOWNMULTIPLE2")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE2")
    @Schema(name = "预留下拉多选字符串2")
    private String reservedDropdownMultiple2;

    @Column(name="RESERVEDDROPDOWNMULTIPLE3")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE3")
    @Schema(name = "预留下拉多选字符串3")
    private String reservedDropdownMultiple3;

    @Column(name="RESERVEDDROPDOWNMULTIPLE4")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE4")
    @Schema(name = "预留下拉多选字符串4")
    private String reservedDropdownMultiple4;

    @Column(name="RESERVEDDROPDOWNMULTIPLE5")
    @TableField(value = "RESERVEDDROPDOWNMULTIPLE5")
    @Schema(name = "预留下拉多选字符串5")
    private String reservedDropdownMultiple5;

    // 预留多选字符串（多选框）5个
    @Column(name="RESERVEDMULTIPLECHOICE1")
    @TableField(value = "RESERVEDMULTIPLECHOICE1")
    @Schema(name = "预留多选字符串1")
    private String reservedMultipleChoice1;

    @Column(name="RESERVEDMULTIPLECHOICE2")
    @TableField(value = "RESERVEDMULTIPLECHOICE2")
    @Schema(name = "预留多选字符串2")
    private String reservedMultipleChoice2;

    @Column(name="RESERVEDMULTIPLECHOICE3")
    @TableField(value = "RESERVEDMULTIPLECHOICE3")
    @Schema(name = "预留多选字符串3")
    private String reservedMultipleChoice3;

    @Column(name="RESERVEDMULTIPLECHOICE4")
    @TableField(value = "RESERVEDMULTIPLECHOICE4")
    @Schema(name = "预留多选字符串4")
    private String reservedMultipleChoice4;

    @Column(name="RESERVEDMULTIPLECHOICE5")
    @TableField(value = "RESERVEDMULTIPLECHOICE5")
    @Schema(name = "预留多选字符串5")
    private String reservedMultipleChoice5;

    // 预留年份（年份）5个
    @Column(name="RESERVEDYEARTIME1")
    @TableField(value = "RESERVEDYEARTIME1")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    @Schema(name = "预留年份1")
    private Date reservedYearTime1;

    @Column(name="RESERVEDYEARTIME2")
    @TableField(value = "RESERVEDYEARTIME2")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    @Schema(name = "预留年份2")
    private Date reservedYearTime2;

    @Column(name="RESERVEDYEARTIME3")
    @TableField(value = "RESERVEDYEARTIME3")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    @Schema(name = "预留年份3")
    private Date reservedYearTime3;

    @Column(name="RESERVEDYEARTIME4")
    @TableField(value = "RESERVEDYEARTIME4")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    @Schema(name = "预留年份4")
    private Date reservedYearTime4;

    @Column(name="RESERVEDYEARTIME5")
    @TableField(value = "RESERVEDYEARTIME5")
    @JsonFormat(pattern = "yyyy", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy")
    @Schema(name = "预留年份5")
    private Date reservedYearTime5;

    // 预留时间（日期（年月日时分秒））5个
    @Column(name="RESERVEDYEARACCURATETIME1")
    @TableField(value = "RESERVEDYEARACCURATETIME1")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "预留时间1")
    private Date reservedYearAccurateTime1;

    @Column(name="RESERVEDYEARACCURATETIME2")
    @TableField(value = "RESERVEDYEARACCURATETIME2")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "预留时间2")
    private Date reservedYearAccurateTime2;

    @Column(name="RESERVEDYEARACCURATETIME3")
    @TableField(value = "RESERVEDYEARACCURATETIME3")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "预留时间3")
    private Date reservedYearAccurateTime3;

    @Column(name="RESERVEDYEARACCURATETIME4")
    @TableField(value = "RESERVEDYEARACCURATETIME4")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "预留时间4")
    private Date reservedYearAccurateTime4;

    @Column(name="RESERVEDYEARACCURATETIME5")
    @TableField(value = "RESERVEDYEARACCURATETIME5")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "预留时间5")
    private Date reservedYearAccurateTime5;

    // 预留年月日（日期（年月日））5个
    @Column(name="RESERVEDTIME1")
    @TableField(value = "RESERVEDTIME1")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "预留年月日1")
    private Date reservedTime1;

    @Column(name="RESERVEDTIME2")
    @TableField(value = "RESERVEDTIME2")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "预留年月日2")
    private Date reservedTime2;

    @Column(name="RESERVEDTIME3")
    @TableField(value = "RESERVEDTIME3")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "预留年月日3")
    private Date reservedTime3;

    @Column(name="RESERVEDTIME4")
    @TableField(value = "RESERVEDTIME4")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "预留年月日4")
    private Date reservedTime4;

    @Column(name="RESERVEDTIME5")
    @TableField(value = "RESERVEDTIME5")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "预留年月日5")
    private Date reservedTime5;

    // 预留单选字符串（单选框）5个
    @Column(name="RESERVEDSINGLECHOICE1")
    @TableField(value = "RESERVEDSINGLECHOICE1")
    @Schema(name = "预留单选字符串1")
    private String reservedSingleChoice1;

    @Column(name="RESERVEDSINGLECHOICE2")
    @TableField(value = "RESERVEDSINGLECHOICE2")
    @Schema(name = "预留单选字符串2")
    private String reservedSingleChoice2;

    @Column(name="RESERVEDSINGLECHOICE3")
    @TableField(value = "RESERVEDSINGLECHOICE3")
    @Schema(name = "预留单选字符串3")
    private String reservedSingleChoice3;

    @Column(name="RESERVEDSINGLECHOICE4")
    @TableField(value = "RESERVEDSINGLECHOICE4")
    @Schema(name = "预留单选字符串4")
    private String reservedSingleChoice4;

    @Column(name="RESERVEDSINGLECHOICE5")
    @TableField(value = "RESERVEDSINGLECHOICE5")
    @Schema(name = "预留单选字符串5")
    private String reservedSingleChoice5;

    // 预留下拉单选字符串（单选下拉框）5个
    @Column(name="RESERVEDDROPDOWNSINGLECHOICE1")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE1")
    @Schema(name = "预留下拉单选字符串1")
    private String reservedDropdownSingleChoice1;

    @Column(name="RESERVEDDROPDOWNSINGLECHOICE2")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE2")
    @Schema(name = "预留下拉单选字符串2")
    private String reservedDropdownSingleChoice2;

    @Column(name="RESERVEDDROPDOWNSINGLECHOICE3")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE3")
    @Schema(name = "预留下拉单选字符串3")
    private String reservedDropdownSingleChoice3;

    @Column(name="RESERVEDDROPDOWNSINGLECHOICE4")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE4")
    @Schema(name = "预留下拉单选字符串4")
    private String reservedDropdownSingleChoice4;

    @Column(name="RESERVEDDROPDOWNSINGLECHOICE5")
    @TableField(value = "RESERVEDDROPDOWNSINGLECHOICE5")
    @Schema(name = "预留下拉单选字符串5")
    private String reservedDropdownSingleChoice5;

    // 预留数字（数字输入框）5个
    @Column(name="RESERVEDNUM1")
    @TableField(value = "RESERVEDNUM1")
    @Schema(name = "预留数字1")
    private Integer reservedNum1;

    @Column(name="RESERVEDNUM2")
    @TableField(value = "RESERVEDNUM2")
    @Schema(name = "预留数字2")
    private Integer reservedNum2;

    @Column(name="RESERVEDNUM3")
    @TableField(value = "RESERVEDNUM3")
    @Schema(name = "预留数字3")
    private Integer reservedNum3;

    @Column(name="RESERVEDNUM4")
    @TableField(value = "RESERVEDNUM4")
    @Schema(name = "预留数字4")
    private Integer reservedNum4;

    @Column(name="RESERVEDNUM5")
    @TableField(value = "RESERVEDNUM5")
    @Schema(name = "预留数字5")
    private Integer reservedNum5;

    // 预留人员单选 5个
    @Column(name="STAFFID1")
    @TableField(value = "STAFFID1")
    @Schema(name = "预留人员单选1")
    private Long staffId1;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名1")
    private String staffRealName1;

    @Column(name="STAFFID2")
    @TableField(value = "STAFFID2")
    @Schema(name = "预留人员单选2")
    private Long staffId2;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名2")
    private String staffRealName2;

    @Column(name="STAFFID3")
    @TableField(value = "STAFFID3")
    @Schema(name = "预留人员单选3")
    private Long staffId3;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名3")
    private String staffRealName3;

    @Column(name="STAFFID4")
    @TableField(value = "STAFFID4")
    @Schema(name = "预留人员单选4")
    private Long staffId4;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名4")
    private String staffRealName4;

    @Column(name="STAFFID5")
    @TableField(value = "STAFFID5")
    @Schema(name = "预留人员单选5")
    private Long staffId5;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名5")
    private String staffRealName5;

    // 预留人员多选 5个
    @Column(name="STAFFIDS1")
    @TableField(value = "STAFFIDS1")
    @Schema(name = "预留人员多选1")
    private String staffIds1;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表1")
    private String staffRealNames1;

    @Column(name="STAFFIDS2")
    @TableField(value = "STAFFIDS2")
    @Schema(name = "预留人员多选2")
    private String staffIds2;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表2")
    private String staffRealNames2;

    @Column(name="STAFFIDS3")
    @TableField(value = "STAFFIDS3")
    @Schema(name = "预留人员多选3")
    private String staffIds3;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表3")
    private String staffRealNames3;

    @Column(name="STAFFIDS4")
    @TableField(value = "STAFFIDS4")
    @Schema(name = "预留人员多选4")
    private String staffIds4;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表4")
    private String staffRealNames4;

    @Column(name="STAFFIDS5")
    @TableField(value = "STAFFIDS5")
    @Schema(name = "预留人员多选5")
    private String staffIds5;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留人员姓名列表5")
    private String staffRealNames5;

    // 预留组织单选 5个
    @Column(name="ORGID1")
    @TableField(value = "ORGID1")
    @Schema(name = "预留组织单选1")
    private Long orgId1;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称1")
    private String orgName1;

    @Column(name="ORGID2")
    @TableField(value = "ORGID2")
    @Schema(name = "预留组织单选2")
    private Long orgId2;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称2")
    private String orgName2;

    @Column(name="ORGID3")
    @TableField(value = "ORGID3")
    @Schema(name = "预留组织单选3")
    private Long orgId3;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称3")
    private String orgName3;

    @Column(name="ORGID4")
    @TableField(value = "ORGID4")
    @Schema(name = "预留组织单选4")
    private Long orgId4;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称4")
    private String orgName4;

    @Column(name="ORGID5")
    @TableField(value = "ORGID5")
    @Schema(name = "预留组织单选5")
    private Long orgId5;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称5")
    private String orgName5;

    // 预留组织多选 5个
    @Column(name="ORGIDS1")
    @TableField(value = "ORGIDS1")
    @Schema(name = "预留组织多选1")
    private String orgIds1;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表1")
    private String orgNames1;

    @Column(name="ORGIDS2")
    @TableField(value = "ORGIDS2")
    @Schema(name = "预留组织多选2")
    private String orgIds2;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表2")
    private String orgNames2;

    @Column(name="ORGIDS3")
    @TableField(value = "ORGIDS3")
    @Schema(name = "预留组织多选3")
    private String orgIds3;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表3")
    private String orgNames3;

    @Column(name="ORGIDS4")
    @TableField(value = "ORGIDS4")
    @Schema(name = "预留组织多选4")
    private String orgIds4;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表4")
    private String orgNames4;

    @Column(name="ORGIDS5")
    @TableField(value = "ORGIDS5")
    @Schema(name = "预留组织多选5")
    private String orgIds5;

    @Transient
    @TableField(exist = false)
    @Schema(name = "预留组织名称列表5")
    private String orgNames5;


    /**
     * 预留人员姓名列表Map
     */
    @TableField(exist = false)
    private Map<Long, String> realNamesMap;

    /**
     * 预留组织名称列表Map
     */
    @TableField(exist = false)
    private Map<Long, String> orgNamesMap;


    public String getStaffRealName1() {
        return getStaffRealNameByStaffId(getStaffId1());
    }

    public String getStaffRealName2() {
        return getStaffRealNameByStaffId(getStaffId2());
    }

    public String getStaffRealName3() {
        return getStaffRealNameByStaffId(getStaffId3());
    }

    public String getStaffRealName4() {
        return getStaffRealNameByStaffId(getStaffId4());
    }

    public String getStaffRealName5() {
        return getStaffRealNameByStaffId(getStaffId5());
    }

    public String getStaffRealNames1() {
        return getStaffRealNameByStaffIds(getStaffIds1());
    }

    public String getStaffRealNames2() {
        return getStaffRealNameByStaffIds(getStaffIds2());
    }

    public String getStaffRealNames3() {
        return getStaffRealNameByStaffIds(getStaffIds3());
    }

    public String getStaffRealNames4() {
        return getStaffRealNameByStaffIds(getStaffIds4());
    }

    public String getStaffRealNames5() {
        return getStaffRealNameByStaffIds(getStaffIds5());
    }


    public String getOrgName1() {
        return getOrgNameByOrgId(getOrgId1());
    }

    public String getOrgName2() {
        return getOrgNameByOrgId(getOrgId2());
    }

    public String getOrgName3() {
        return getOrgNameByOrgId(getOrgId3());
    }

    public String getOrgName4() {
        return getOrgNameByOrgId(getOrgId4());
    }

    public String getOrgName5() {
        return getOrgNameByOrgId(getOrgId5());
    }

    public String getOrgNames1() {
        return getOrgNamesByOrgIds(getOrgIds1());
    }

    public String getOrgNames2() {
        return getOrgNamesByOrgIds(getOrgIds2());
    }

    public String getOrgNames3() {
        return getOrgNamesByOrgIds(getOrgIds3());
    }

    public String getOrgNames4() {
        return getOrgNamesByOrgIds(getOrgIds4());
    }

    public String getOrgNames5() {
        return getOrgNamesByOrgIds(getOrgIds5());
    }

    /**
     * 获取预留人员姓名Map
     */
    private void getRealNamesMap() {
        TblStaffServiceImpl tblStaffService = SpringContextHolder.getBean("tblStaffServiceImpl");
        // 获取预留人员id集合
        Object[] mixedFields = {staffId1, staffId2, staffId3, staffId4, staffId5, staffIds1, staffIds2, staffIds3, staffIds4, staffIds5};
        String ids = getMergeIds(mixedFields);
        if (StringUtil.isEmpty(ids)) {
            realNamesMap = Collections.emptyMap();
            return;
        }
        List<TblStaff> tablStaffList = tblStaffService.selectTblStaffByIds(ids);
        realNamesMap = tablStaffList.stream()
                .collect(Collectors.toMap(
                        staff -> staff.getStaffid().longValue(),
                        TblStaff::getRealname,
                        (existing, replacement) -> existing
                ));
    }

    /**
     * 获取预留组织姓名Map
     */
    private void getOrgNamesMap() {

        TblOrganizaServiceImpl tblOrganizationMapper = SpringContextHolder.getBean("tblOrganizaServiceImpl");
        // 获取预留人员id集合
        Object[] mixedFields = {orgId1, orgId2, orgId3, orgId4, orgId5, orgIds1, orgIds2, orgIds3, orgIds4, orgIds5};
        String ids = getMergeIds(mixedFields);
        if (StringUtil.isEmpty(ids)) {
            orgNamesMap = Collections.emptyMap();
            return;
        }
        List<TblOrganization> tablStaffList = tblOrganizationMapper.selectByIds(ids);
        orgNamesMap = tablStaffList.stream()
                .collect(Collectors.toMap(
                        org -> org.getOrgid().longValue(), // 将 BigDecimal 转为 Long
                        TblOrganization::getOrgname,
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 根据预留人员id获取预留人员姓名
     *
     * @param staffId
     * @return
     */
    private String getStaffRealNameByStaffId(Long staffId) {
        if (staffId == null) {
            return ""; // 处理空值或空字符串
        }
        if (realNamesMap == null) {
            getRealNamesMap();
        }
        String staffRealName = realNamesMap.get(staffId);
        return staffRealName;
    }

    /**
     * 根据预留人员id集合获取预留人员姓名
     *
     * @param staffIds
     * @return
     */
    private String getStaffRealNameByStaffIds(String staffIds) {
        if (staffIds == null || staffIds.isEmpty()) {
            return ""; // 处理空值或空字符串
        }

        // 拆分ID并转换为Long集合
        // 获取真实姓名并过滤null值
        return Arrays.stream(staffIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong) // 解析为 Long
                .map(id -> realNamesMap.getOrDefault(id, ""))
                .filter(name -> !name.isEmpty())
                .collect(Collectors.joining(","));
    }



    /**
     * 根据预留人员id获取预留人员姓名
     *
     * @param orgId
     * @return
     */
    private String getOrgNameByOrgId(Long orgId) {
        if (orgNamesMap == null) {
            getOrgNamesMap();
        }
        if (orgId == null) return "";
        // 修正键类型转换（假设 TblOrganization.orgid 是 Long 类型）
        return orgNamesMap.getOrDefault(orgId, "");
    }

    /**
     * 根据预留人员id集合获取预留人员姓名
     *
     * @param orgIds
     * @return
     */
    private String getOrgNamesByOrgIds(String orgIds) {
        if (orgIds == null || orgIds.isEmpty()) {
            return ""; // 处理空值或空字符串
        }

        // 拆分ID并转换为Long集合
        return Arrays.stream(orgIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong) // 解析为 Long
                .map(id -> orgNamesMap.getOrDefault(id, ""))
                .filter(name -> !name.isEmpty())
                .collect(Collectors.joining(","));
    }
    /**
     * 合并ids
     *
     * @param mixedFields
     * @return
     */
    private String getMergeIds(Object[] mixedFields) {
        List<Long> idList = Arrays.stream(mixedFields)
                .filter(Objects::nonNull)
                .flatMap(field -> {
                    if (field instanceof Long) {
                        return Stream.of((Long) field);
                    } else if (field instanceof String) {
                        return Arrays.stream(((String) field).split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .map(Long::parseLong);
                    }
                    return Stream.empty();
                })
                .distinct()
                .collect(Collectors.toList());

        if (idList.isEmpty()) {
            return null;
        }
        String ids = idList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        return ids;
    }

}
