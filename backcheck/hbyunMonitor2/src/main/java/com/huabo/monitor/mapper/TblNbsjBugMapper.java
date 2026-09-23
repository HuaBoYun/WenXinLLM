package com.huabo.monitor.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.entity.TblNbkzRisk;
import com.huabo.monitor.entity.TblNbsjBugEntity;


public interface TblNbsjBugMapper extends BaseMapper<TblNbsjBugEntity> {
	 
	@Select(" SELECT TNA.*,cc.bugcrilevel,ORG.ORGNAME"
      +" FROM TBL_NBSJ_BUG TNA "
         +" LEFT JOIN TBL_NBSJ_BUG_CRITERION cir on TNA.BUGID=CIR.BUGID "
          +" LEFT JOIN TBL_NBSJ_BUGCRITERION cc on cc.BUGCRIID=CIR.BUGCRIID "
         +" LEFT JOIN TBL_ORGANIZATION ORG on ORG.ORGID=TNA.bugdepartment"
         + "  where  TNA.BUGID in (select bugid from TBL_REPORT_BUG where reportid=#{id} )")
	@Results({
    	@Result(column="BUGCRILEVEL",property="bugcrilevel"),
    	@Result(column="ORGNAME",property="orgname"),
    })
	List<TblNbsjBugEntity> selectNbsjBugList(BigDecimal id);
 
}
