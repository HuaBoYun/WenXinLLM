package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_RISKCATEGORY")
@Schema(name="TblRiskcategoryMySql对象")
public class TblRiskcategoryMySql implements java.io.Serializable {

    /**
     * @author tyb
     * 2016-7-12下午12:00:42
     * @Des:标识行业风险数据库
     */
    public final static String HYFXSJK = "hyfxsjk";

    /**
     * @author tyb
     * 2016年9月4日下午4:53:51
     * @Des:标识风险数据库
     */
    public final static String FXSJK = "FXSJK";
    /**
     * @author tyb
     * 2016-7-15下午10:31:12
     * @Des:标识行业风险事件库
     */
    public final static String HYFXSHIJIANK = "hyfx-shijian-k";

    /**
     * @author tyb
     * 2016年9月4日下午4:54:36
     * @Des:标识风险事件库
     */
    public final static String FXSHIJIANK = "FXSHIJIANK";

    @TableId("RISKCATID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal riskcatid;
    @TableField("RISKCATNUMBER")
    private String riskcatnumber;
    @TableField("RISKCATNAME")
    private String riskcatname;
    @TableField("RISKCATDES")
    private String riskcatdes;
    @TableField("RISKSTATUS")
    private String riskstatus;
    @TableField("FATHERRISKCATID")
    private BigDecimal fatherriskcatid;
    @TableField("FULLPATH")
    private String fullpath;
    @TableField("MEMO")
    private String memo;
    @TableField("ISLEAF")
    private BigDecimal isleaf;
    @TableField("UNIT")
    private String unit;
    @TableField("MODULETYPE")
    private String moduletype;
    @Transient
    private Set tblRisks = new HashSet(0);

}