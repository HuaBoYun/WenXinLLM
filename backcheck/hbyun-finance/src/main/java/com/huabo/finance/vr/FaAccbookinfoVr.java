package com.huabo.finance.vr;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 账簿信息
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="FaAccbookinfo对象", description="账簿信息")
public class FaAccbookinfoVr implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      private String pkAccbookinfo;

      @Schema(name = "组织_集团 ")
    private String pkGroup;

      @Schema(name = "组织_业务单元_财务组织 ")
    private String pkOrg;

      @Schema(name = "账簿_账簿类型 ")
    private String pkSetofbook;

      @Schema(name = "外币折算日期   	0=卡片建卡日期;1=业务发生日期;")
    private Integer convertDate;

      @Schema(name = "本币原值来源")
    private String localoriginvalue;

      @Schema(name = "明细 ")
    private String bodyvos;
      
      @Schema(name = "账簿名称 ")
    private String bookName;

      @Schema(name = "数据采集方案")
    private String pkFinanplanid;

      @Schema(name = "账簿类别编码 ")
    private String accbooktypecode;

      @Schema(name = "账簿类别名称 ")
    private String accbooktypename;

      @Schema(name = "创建人")
    private String creator;

      @Schema(name = "创建时间 ")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

      @Schema(name = "最后修改人")
    private String modifier;

      @Schema(name = "最后修改时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

      
    @Schema(name = "组织_集团名称")
    private String groupName;
    
    @Schema(name = "财务组织名称")
    private String orgName;
    
    @Schema(name = "账簿类型名称")
    private String setOfName;
    
    @Schema(name = "采集方案名称")
    private String planName;
    
    @Schema(name = "创建人姓名")
    private String creatorName;
    
    @Schema(name = "修改人姓名")
    private String modifierName;
    
    @Schema(name = "是否选中")
    private Integer isChecked;

}
