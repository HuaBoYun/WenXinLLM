package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

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
@Schema(name="TblLogintypeMySql对象")
public class TblLoginTypeMySql implements Serializable {
    // extends TblOrganization

    private static final long serialVersionUID = 1L;

    @TableId("LOGINID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
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
