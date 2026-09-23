package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_IMP_LOG")
@Schema(name="TblImplogMySql")
public class TblImplogMySql implements Serializable {

    /**
     * 序列化Id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 测试模板导入代码
     */
    public static final String IMPSTATE_CSMB = "CSMB";
    /**
     * 法律法规程序导入代码
     */
    public static final String IMPSTATE_WG = "FLFG";
    /**
     * 规章制度程序导入代码
     */
    public static final String IMPSTATE_NG = "GZZD";
    /**
     * 控制矩阵导入程序导入代码
     */
    public static final String IMPSTATE_KZJZ = "KZJZ";
    /**
     * 内部控制程序导入代码
     */
    public static final String IMPSTATE_NBKZ = "NBKZ";
    /**
     * 流程管理的内外规定导入代码
     */
    public static final String IMPSTATE_NWGD = "NWGD";
    /**
     * 审计-项目资料导入代码
     */
    public static final String IMPSTATE_XMZL = "XMZL";

    @TableId("IMPID")
    private BigDecimal impid;//注解ID
    @TableField("IMPDESC")
    private String impdesc;  //描述
    @TableField("IMPURL")
    private String impurl;   //文件Url
    @TableField("IMPFILENAME")
    private String impfilename;//文件名称
    @TableField("IMPCREATETIME")
    private Date impcreatetime; //创建时间
    @TableField("IMPCREATEUSERNAME")
    private String impcreateusername; //创建人，当前用户
    @TableField("IMPTYPE")
    private String imptype;  //所属类型
    @TableField("IMPSTATE")
    private String impstate;//导入是否成功


}
