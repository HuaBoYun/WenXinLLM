package com.huabo.audit.oracle.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "TBL_CURRENT_POSITION")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="任职履历表", description="TBL_CURRENT_POSITION")
@KeySequence(value="HIBERNATE_SEQUENCE") //value为数据库中生成的序列名，class指主键属性类型
public class TblCurrentPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "任职履历ID")
    @TableId(type= IdType.INPUT)  //注意主键类型要指定为Input
    private Integer currentpositionid;

    @Schema(name = "内审机构名称")
    @TableField("CURRENTPOSITIONORGNAME")
    private String currentPositionOrgName;

    @Schema(name = "任职时间")
    @TableField("CURRENTPOSITIONSTARTTIME")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date currentPositionStartTime;

    @Schema(name = "离任时间")
    @TableField("CURRENTPOSITIONENDTIME")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date currentPositionEndTime;

    @Schema(name = "关联用户ID")
    @TableField("CPSTAFFID")
    private Integer cpStaffId;
}
