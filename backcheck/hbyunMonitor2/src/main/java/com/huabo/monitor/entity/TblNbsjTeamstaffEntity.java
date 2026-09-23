package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 描述: 
 * author: ziyao
 * date: 2022-04-19
 */
@TableName("TBL_NBSJ_TEAMSTAFF")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjTeamstaffEntity {
	
	public static final Integer LEADER=0;
	public static final Integer CREW=1;
	
    @TableId(value = "id", type= IdType.AUTO)
    @Schema(name = "id")
    private Integer id;

    @TableField(value = "teamid")
    @Schema
    private Integer teamid;

    @TableField(value = "staffid")
    @Schema
    private Integer staffid;

    @TableField(value = "stafftype")
    @Schema//0=组长 1=组员
    private Integer stafftype;
    
    @TableField(value = "staff")
    @Schema
    private TblStaff staff;
    
    @TableField(value = "")
    @Schema
    private String createUserName;
    
    @TableField(value = "")
    @Schema
    private Date createTime;
    
    @TableField(value = "")
    @Schema
    private String teamName;
}
