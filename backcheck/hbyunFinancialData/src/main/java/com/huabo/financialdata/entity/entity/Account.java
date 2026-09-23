package com.huabo.financialdata.entity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 财务科目表 - 实体
 *
 * @author lee
 * @version 1.0.0
 **/
@Data
@TableName("TBL_ACCOUNT")
@Schema(name=" 财务科目表 TBL_ACCOUNT实体类")
public class Account {


    /**
     * 科目编码
     */
    @TableId("ACCID")
    @Schema(name = "科目编码")
    private String ACCID;

    /**
     * 科目名称
     */
    @Schema(name = "科目名称")
    private String ACCNAME1;

    /**
     * 科目方向
     */
    @Schema(name = "科目方向")
    private String DC;

    /**
     * 科目类别名称
     */
    @Schema(name = "科目类别名称")
    private String TYPENAME;

    /**
     * 上级科目编码
     */
    @Schema(name = "上级科目编码")
    private String HIGHACCID;

    /**
     * 科目类别编号
     */
    @Schema(name = "科目类别编号")
    private String TYPEID;

    /**
     * 科目全称
     */
    @Schema(name = "科目全称")
    private String ACCNAME2;

    /**
     * 科目级别
     */
    @Schema(name = "科目级别")
    private Integer IGRADE;

    /**
     * 是否为现金或现金等价物（
     */
    @Schema(name = "是否为现金或现金等价物")
    private Integer SICASH;

    /**
     * 是否为标准科目
     */
    @Schema(name = "是否为标准科目")
    private Integer ISBZKM;

    /**
     * 是否为最底层科目
     */
    @Schema(name = "是否为最底层科目")
    private Integer ISDCACC;

    /**
     * 备注信息
     */
    @Schema(name = "备注信息")
    private String DES;

    /**
     * 年份信息
     */
    @Schema(name = "年份信息")
    private Integer AYEAR;


}
