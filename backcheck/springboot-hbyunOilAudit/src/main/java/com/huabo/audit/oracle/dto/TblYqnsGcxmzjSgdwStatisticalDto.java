package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@Schema(name="工程项目造价表-施工单位 统计表")
public class TblYqnsGcxmzjSgdwStatisticalDto {

    /**
     * 建设单位
     */
    @Schema(name = "建设单位 - 施工单位")
    @Transient
    @TableField(value = "SGDW")
    private String sgdw;

    @Schema(name = "合同编号统计-合计项目个数")
    @Transient
    @TableField(value = "HTBHCOUNT")
    private String htbhCount;

    @Schema(name = "额度金额-结算金额合计")
    @Transient
    @TableField(value = "EDJESUM")
    private String edjeSum;

    @Schema(name = "大于900万个数")
    @Transient
    @TableField(value = "COUNTOVERNINEHUND")
    private String countOverNineHund;

    @Schema(name = "大于900万金额")
    @Transient
    @TableField(value = "SUMOVERNINEHUND")
    private String sumOverNineHund;

    @Schema(name = "100-900万个数")
    @Transient
    @TableField(value = "COUNTHUNDTONINEHUND")
    private String countHundToNineHund;

    @Schema(name = "100-900万金额")
    @Transient
    @TableField(value = "SUMHUNDTONINEHUND")
    private String sumHundToNineHund;

    @Schema(name = "50-100万 个数")
    @Transient
    @TableField(value = "COUNTFIFTYTOHUND")
    private String countFiftyToHund;

    @Schema(name = "50-100万金额")
    @Transient
    @TableField(value = "SUMFIFTYTOHUND")
    private String sumFiftyToHund;

    @Schema(name = "20-100万个数")
    @Transient
    @TableField(value = "COUNTTWENTYTOFIFTY")
    private String countTwentyToFifty;

    @Schema(name = "20-50万金额")
    @Transient
    @TableField(value = "SUMTWENTYTOFIFTY")
    private String sumTwentyToFifty;

    @Schema(name = "小于20万个数")
    @Transient
    @TableField(value = "COUNTUNDERTWENTY")
    private String countUnderTwenty;

    @Schema(name = "小于20万金额")
    @Transient
    @TableField(value = "SUMUNDERTWENTY")
    private String sumUnderTwenty;
    
    @Schema(name = "筛选年度")
	@Transient
	@TableField(exist = false)
	private Integer queryYear;

}
