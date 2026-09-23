package com.huabo.cybermonitor.entity;

import cn.hutool.db.meta.Table;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@TableName("TBL_FORM_CONTROELE")
@KeySequence(value="HIBERNATE_SEQUENCE",dbType = DbType.ORACLE)
public class FormControele implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.INPUT)
    private BigDecimal reid;

    private BigDecimal eleid;

    private BigDecimal ruleid;

    private String replacekvalue;

    private String replacekey;

    private String replacetype;

    public BigDecimal getReid() {
        return reid;
    }

    public void setReid(BigDecimal reid) {
        this.reid = reid;
    }
    public BigDecimal getEleid() {
        return eleid;
    }

    public void setEleid(BigDecimal eleid) {
        this.eleid = eleid;
    }
    public BigDecimal getRuleid() {
        return ruleid;
    }

    public void setRuleid(BigDecimal ruleid) {
        this.ruleid = ruleid;
    }
    public String getReplacekvalue() {
        return replacekvalue;
    }

    public void setReplacekvalue(String replacekvalue) {
        this.replacekvalue = replacekvalue;
    }
    public String getReplacekey() {
        return replacekey;
    }

    public void setReplacekey(String replacekey) {
        this.replacekey = replacekey;
    }
    public String getReplacetype() {
        return replacetype;
    }

    public void setReplacetype(String replacetype) {
        this.replacetype = replacetype;
    }

    @Override
    public String toString() {
        return "FormControele{" +
            "reid=" + reid +
            ", eleid=" + eleid +
            ", ruleid=" + ruleid +
            ", replacekvalue=" + replacekvalue +
            ", replacekey=" + replacekey +
            ", replacetype=" + replacetype +
        "}";
    }
}
