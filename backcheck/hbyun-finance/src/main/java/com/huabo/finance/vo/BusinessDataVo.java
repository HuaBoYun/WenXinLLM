package com.huabo.finance.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.entity.TblConfigColumnInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 凭证库表
 * </p>
 *
 * @author L
 * @since 2025-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description="业务数据筛选条件")
public class BusinessDataVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

	@Schema(name = "筛选条件")
	private List<TblConfigColumnInfo> filterColList;
	  
	@Schema(name = "业务表主键")  
	private String tableId;
	

}
