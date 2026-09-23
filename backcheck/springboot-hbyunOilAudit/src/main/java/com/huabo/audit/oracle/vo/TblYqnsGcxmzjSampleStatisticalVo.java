package com.huabo.audit.oracle.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.audit.oracle.dto.TblYqnsGcxmzjSampleStatisticalDto;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(name="工程项目造价表-抽审表(按施工单位及额度)-统计表 -展示")
public class TblYqnsGcxmzjSampleStatisticalVo {
	


	/**
	 * 建设单位
	 */
	@Schema(name = "建设单位")
	@Transient
	private String jsdw;

	/**
	 * 明细
	 */
	@Schema(name = "明细")
	@Transient
	private List<TblYqnsGcxmzjSampleStatisticalDto> list;


	public TblYqnsGcxmzjSampleStatisticalVo(String jsdw, List<TblYqnsGcxmzjSampleStatisticalDto> list) {
		this.jsdw = jsdw;
		this.list = list;
	}

}
