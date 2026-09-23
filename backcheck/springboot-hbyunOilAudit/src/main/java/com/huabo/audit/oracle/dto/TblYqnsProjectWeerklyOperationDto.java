package com.huabo.audit.oracle.dto;

import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblNbsjAdvicenoteEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectWeerklyEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Schema(name="审计实施-审计项目运行情况-审计项目运行情况汇总")
public class TblYqnsProjectWeerklyOperationDto {



	private ImplementPlanEntity implementPlanEntity;

	private List<TblYqnsProjectWeerklyEntity> weerklyList;

	
}
