package com.huabo.audit.oracle.entity;



import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统业务单据下发通知表
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SYSTEM_DISTRIBUTION")
@Schema(name="TblSystemDistribution对象", description="系统业务单据下发通知表")
public class TblSystemDistribution implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String ZGFATYPE = "ZGFA";//整改方案下发类型
    public static final String ZGFAURL = "95";//整改方案详情页面前端路由，由前端提供

    public static final String ZGFPTYPE = "ZCGZLS";//整改分派下发类型
    public static final String ZGFPURL = "97";//整改分派详情页面前端路由，由前端提供
    
    public static final String SJDWLRSJMXTYPE = "SJDWLRSJMX";//三级单位离任审计
    public static final String SJDWLRSJMXURL = "663";
    
    public static final String SJLXJYTZTYPE = "SJLXJYTZ";//审计立项建议通知
    public static final String SJLXJYTZURL = "666";
    
    public static final String LXJYBTYPE = "LXJYB";//立项建议表
    public static final String LXJYBURL = "1511";
    

    public static final String XQJYBTYPE = "XQJYB";//需求建议表
    public static final String XQJYBURL = "1386";//
    
    public static final String FGLDHZTYPE = "FGLDHZ";//分管领导汇总
    public static final String FGLDHZURL = "1388";//
    
    
    public static final String FWXQBTYPE = "FWXQB";//服务需求表
    public static final String FWXQBURL = "1387";//
    
    public static final String EJDWJCYLRSJTYPE = "EJDWJCYLRSJ";//二级单位及成员单位离任审计
    public static final String EJDWJCYLRSJURL = "1396";//
    
    public static final String WWTJYJLRTYPE = "WWTJYJLR";//未委托及预计离任
    public static final String WWTJYJLRURL = "1495";//
    
    public static final String SJDWLRSJTYPE = "SJDWLRSJ";//三级单位离任审计
    public static final String SJDWLRSJURL = "1395";//
    
    
    public static final String GZFATYPE = "GZFA";//工作方案
    public static final String GZFAJURL = "1497";//
    
    
    public static final String SJQKBTYPE = "SJQKB";//审计项目表
    public static final String SJQKBURL = "1524";//
    
    
    public static final String SJXMZDTYPE = "SJXMZD";//审计项目制度
    public static final String SJXMZDURL = "627";//
    
    public static final String IPQDTYPE = "IPQD";//IP清单
    public static final String IPQDURL = "569272297635909";//

	public static final String YXXMPYXTYPE = "YXXMPX";//优秀项目评选
	public static final String YXXMPYXURL = "80010";//
	
	
	public static final String GCXMAPBTYPE = "GCSJXMAP";//工程审计项目安排
	public static final String GCXMAPBURL = "1465";//
	
	public static final String CWXMAPBTYPE = "CWSJXMAP";//财务审计项目安排
	public static final String CWXMAPBURL = "1466";//
    
	
	public static final String CWDDFGTYPE = "CWDDFG";//财务督导分工
	public static final String CWDDFGURL = "579594840944709";// 
	
	public static final String GCDDFGTYPE = "GCDDFG";//工程督导分工
	public static final String GCDDFGURL = "579594969821253";//
	
	
	public static final String LLYTTZTYPE = "LLYTTZ";//理论研讨通知
	public static final String LLYTTZURL = "573766053322821";//
	
	
	public static final String WTDZTYPE = "WTDZ";//问题定责
	public static final String WTDZURL = "800022";//
	
	public static final String HJTZTYPE = "HJTZ";//获奖通知
	public static final String HJTZURL = "80051";//
	
	public static final String SJTZSTYPE = "SJTZS";//审计通知书
	public static final String SJTZSURL = "1356";//
	
	public static final String XMHJTZTYPE = "XMPYHJTZ";//项目评优-获奖通知
	public static final String XMHJTZURL = "617366174068805";//
	
	public static final String ZNSJSJTZSTYPE = "ZNSJSJTZS";//智能审计-审计通知
	public static final String ZNSJSJTZSURL = "219";//
	
	
	public static final String SJJGWSTYPE = "SJJGWS";//智能审计-审计结果文书
	public static final String SJJGWSURL = "231";//
	
	
	public static final String ZGTZWTZGTYPE = "ZGLS";//整改通知按问题整改下发类型
	public static final String ZGTZWTZGURL = "1604";//整改通知按问题整改详情页面前端路由，由前端提供
	
	
      @Schema(name = "下发事项主键")
      @TableId("DISTRIBUTIONID")
      @Id
      @Column(name = "DISTRIBUTIONID")
      private String distributionId;

    @TableField("DISTRIBUTIONTITLE")
    @Schema(name = "下发事项标题")
    @Column(name = "DISTRIBUTIONTITLE")
    private String distributionTitle;

      @Schema(name = "接收人")
      @TableField("RECIVER")
      @Column(name = "RECIVER")
    private BigDecimal reciver;

      @Schema(name = "创建时间")
      @TableField("CREATETIME")
      @Column(name = "CREATETIME")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

      @Schema(name = "接受确认时间")
      @TableField("RECIVEDATE")
      @Column(name = "RECIVEDATE")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date reciveDate;

      @Schema(name = "业务单据类型")
      @TableField("DISTRIBUTIONTYPE")
      @Column(name = "DISTRIBUTIONTYPE")
    private String distributionType;

      @Schema(name = "业务单据主键")
      @TableField("FORMID")
      @Column(name = "FORMID")
    private String formId;

      @Schema(name = "所属模块,   智能监控 - znjk 合同管理-htgl 内控合规 -nkhg 系统设置-xtsz 智能审计-znsj 智能分析-znfx 整改追责-zgzz 业务管控财经实训等后续新增的一级权限 为该权限的Id")
      @TableField("MODULETYPE")
      @Column(name = "MODULETYPE")
    private String moduleType;

      @Schema(name = "创建人 下发人")
      @TableField("CREATESTAFF")
      @Column(name = "CREATESTAFF")
    private BigDecimal createStaff;

      @Schema(name = "是否接受 0-否  1-是")
      @TableField("ISREAD")
      @Column(name = "ISREAD")
    private Integer isread;

      @Schema(name = "路由 ,来源 TBl_system_sheettable 中的 tableId")
      @TableField("PAGEURL")
      @Column(name = "PAGEURL")
    private String pageUrl;
      
      
}
