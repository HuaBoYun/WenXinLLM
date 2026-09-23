package com.huabo.fxgl.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Data
@ToString
@TableName("TBL_RISKCATEGORY")
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class Riskcategory implements Serializable {

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

    private static final long serialVersionUID = 1L;

    @Schema(name="子集list集合")
    @TableField(exist = false)
    private List<Riskcategory> children;

    /**
     * 风险编号
     */
	@Schema(name="风险类型编号")
    private String riskcatnumber;

    /**
     * 风险名称
     */
	@Schema(name="风险类型名称")
    private String riskcatname;

    /**
     * ID
     */
	@Schema(name="风险类型主键")
    @TableId(type = IdType.INPUT)
    private BigDecimal riskcatid;

    /**
     * 风险描述
     */
	@Schema(name="风险描述")
    private String riskcatdes;

    /**
     * 状态
     */
	@Schema(name="状态  0-正常，1-禁用")
    private String riskstatus;

    /**
     * 父ID
     */
	@Schema(name="父ID")
    private BigDecimal fatherriskcatid;

	@Schema
    private String fullpath;

    /**
     * 备注
     */
	@Schema(name="备注")
    private String memo;

	@Schema(name="是否是子节点  1-是  0-否")
    private BigDecimal isleaf;

	@Schema(name="所属公司")
    private String unit;

    /**
     * 类别
     */
	@Schema(name="类别")
    private String moduletype;

	@Schema
    @TableField(exist = false)
    private int isdel;

}
