package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_LOGINTYPE")
@Schema(name="TblLogintype对象", description="")
public class TblLoginType  implements Serializable {
   // extends TblOrganization

    private static final long serialVersionUID = 1L;

    @TableId(value="LOGINID",type = IdType.INPUT)
    private BigDecimal loginid;

    @TableField("LOGINURL")
    private String loginurl;

    @TableField("SALEHOTLINE")
    private String salehotline;

    @TableField("ECHNICALSUPPORTPHONE")
    private String echnicalsupportphone;

    @TableField("LOGINPAGE")
    private String loginpage;

    @TableField("LOGINPAGETWO")
    private String loginpagetwo;

    @TableField("LOGINPAGETHREE")
    private String loginpagethree;

    @TableField("HOMEPAGE")
    private String homepage;

    @TableField("HOMEPAGEPIC")
    private String homepagepic;

    @TableField("LOGINPIC")
    private String loginpic;

    @TableField("LOGINNAME")
    private String loginname;

    @TableField("ORGID")
    private String orgid;

//
//    @JSONField(serialize = false)
//    public void setOrgid(String toString) {
//
//    }
}
