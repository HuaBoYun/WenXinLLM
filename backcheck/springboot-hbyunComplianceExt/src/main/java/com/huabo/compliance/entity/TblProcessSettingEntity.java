package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName TBL_PROCESS_SETTING
 */
@TableName(value ="TBL_PROCESS_SETTING")
@Data
public class TblProcessSettingEntity implements Serializable {

    public  final static String OFF="OFF";//关闭
    public final static String ON="ON";//开启
    /**
     * 
     */
    @TableId(value = "SETTINGID")
    private Long settingid;

    /**
     * 
     */
    @TableField(value = "STATUS")
    private String status;

    /**
     * 
     */
    @TableField(value = "MODULE")
    private String module;

    /**
     * 
     */
    @TableField(value = "REMARK")
    private String remark;

    /**
     * 
     */
    @TableField(value = "ORGID")
    private Long orgid;

    /**
     * 
     */
    @TableField(value = "COMPANYID")
    private Long companyid;

}