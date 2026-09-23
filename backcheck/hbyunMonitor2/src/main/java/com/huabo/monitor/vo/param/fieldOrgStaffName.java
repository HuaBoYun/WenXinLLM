package com.huabo.monitor.vo.param;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.impl.TblStaffServiceImpl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 赋值Name
 * */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class fieldOrgStaffName {
   	@Schema(name="预留人员单选name1")
   	private String staffidname1;

   	@Schema(name="预留人员单选name2")
   	private String staffidname2;

   	@Schema(name="预留人员单选name3")
   	private String staffidname3;

   	@Schema(name="预留人员单选name4")
   	private String staffidname4;

   	@Schema(name="预留人员单选name5")
   	private String staffidname5;

   	@Schema(name="预留人员多选name1")
   	private String staffidsname1;

   	@Schema(name="预留人员多选name2")
   	private String staffidsname2;

   	@Schema(name="预留人员多选name3")
   	private String staffidsname3;

   	@Schema(name="预留人员多选name4")
   	private String staffidsname4;

   	@Schema(name="预留人员多选name5")
   	private String staffidsname5;

   	@Schema(name="预留组织单选name1")
   	private String orgidname1;

   	@Schema(name="预留组织单选name2")
   	private String orgidname2;

   	@Schema(name="预留组织单选name3")
   	private String orgidname3;

   	@Schema(name="预留组织单选name4")
   	private String orgidname4;

   	@Schema(name="预留组织单选name5")
   	private String orgidname5;

   	@Schema(name="预留组织多选name1")
   	private String orgidsname1;

   	@Schema(name="预留组织多选name2")
   	private String orgidsname2;

   	@Schema(name="预留组织多选name3")
   	private String orgidsname3;

   	@Schema(name="预留组织多选name4")
   	private String orgidsname4;

   	@Schema(name="预留组织多选name5")
   	private String orgidsname5;
	
}
