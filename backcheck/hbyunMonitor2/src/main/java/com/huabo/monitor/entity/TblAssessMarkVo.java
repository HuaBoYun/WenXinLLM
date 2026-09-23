package com.huabo.monitor.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("TBL_ASSESS_MARK")
public class TblAssessMarkVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ASSMARKID")
    @TableId(type= IdType.INPUT)
    private BigDecimal assmarkid;

    @TableField("SCORE")
    private BigDecimal score;

    @TableField("MEMO")
    private String memo;
    
    @Schema(name="适用性(1适用 0不适用)")
    @TableField("SUITABLE")
    private String suitable;

    //TBL_ASSESSELEMENT 外键
    @TableField("ASSELEID")
    private BigDecimal asseleid;

    //TBL_STAFF 外键
    @Schema(name="主评人id")
    @TableField("STAFFID")
    private BigDecimal staffid;

    @Schema(name="主评人名字")
    @TableField("REALNAME")
    private String realname;


    @Schema(name="负责人登录名")
    @TableField("USERNAME")
    private String username;


    @Schema(name="评价要素")
    @TableField("ELEMENTNAME")
    private String elementname;

    @Schema(name="状态")
    private String state = "1";
    //TBL_ASSESS 外键
    @TableField("ASSID")
    private BigDecimal assid;

    //TBL_ORGANIZATION 外键
    @TableField("ASSORGID")
    private BigDecimal assorgid;

    //TBL_ASSESS_TARGET 外键
    @TableField("ASSESSTARGETID")
    private BigDecimal assesstargetid;

    @Schema(name="评价对象名称")
    @TableField("ORGNAME")
    private String  orgname;
    
    @TableField(exist=false)
    @Schema(name="参评人集合")
    private List<TblAssessStaffVo> canpingrens;

}
