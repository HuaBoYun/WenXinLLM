package com.huabo.audit.oracle.entity;

import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_QUESTIONAFFIRM")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjQuestionaffirmEntity {
	
	@TableId(value = "affirmid", type= IdType.AUTO)
    @Schema
    private Integer affirmid;

    @TableField(value = "tblNbsjQuestion")
    @Schema
    private TblNbsjQuestionEntity tblNbsjQuestion;
    
    @TableField(value = "tblNbsjFactbook")
    @Schema
    private TblNbsjFactbookEntity tblNbsjFactbook;
    
    @TableField(value = "describe")
    @Schema
    private String describe;
    
    @TableField(value = "affirmtime")
    @Schema
    private Date affirmtime;
    
    @TableField(value = "tblNbsjQuestionaffirmatts")
    @Schema
    private Set tblNbsjQuestionaffirmatts;
    
    @TableField(value = "FACTID")
    @Schema
    private Integer factid;
    
    @TableField(value = "QUESTIONID")
    @Schema
    private Integer questionid;
}
