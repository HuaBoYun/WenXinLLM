package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_EXISTING_STANDARD_ATT")
@Schema(name="现行标准文件关联实体类")
public class TblExistingStandardAtt {
    private static final long serialVersionUID = 1L;
    @Schema(name = "现行标准ID")
    @TableField("TESID")
    private BigDecimal tesId;

    @Schema(name = "文件ID")
    @TableField("ATTID")
    private BigDecimal attId;
}
