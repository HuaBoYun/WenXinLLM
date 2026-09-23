package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_NBSJ_SHEET_REPORT")
@Schema(name="TblNbsjSheetReport对象", description="")
public class TblNbsjSheetReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="REPORTID",type = IdType.INPUT)
    private BigDecimal reportid;

    @TableField("REPORTCONTENT")
    private String reportcontent;

    @TableField("REPORTORGIDS")
    private String reportorgids;

    @TableField("SHEETID")
    private BigDecimal sheetid;
    
    @TableField("tblNbsjSheet")
    private TblNbsjSheet tblNbsjSheet;

}
