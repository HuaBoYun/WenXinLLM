package com.huabo.system.mapper;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.Tblzsgxlibrary;

public class TblzsgxlibraryMapperSqlConfig {
	
    public String selectType(IPage<Tblzsgxlibrary> page, Tblzsgxlibrary library,String type,BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select * from TBL_ZSGX_LIBRARY   WHERE 1=1 and CREATEORGANID= "+orgid);
       if(library!=null && library.getXllevel()!=null) {
    	   sbSql.append(" and XLLEVEL='"+library.getXllevel()+"' ");
       }
       if(library!=null && library.getToplicclass()!=null) {
    	   sbSql.append(" and TOPLICCLASS='"+library.getToplicclass()+"' ");
       }
       if(library!=null && library.getZdorgan()!=null) {
    	   sbSql.append(" and ZDORGAN='"+library.getZdorgan()+"' ");
       }
       if(library!=null && library.getTimeliness()!=null) {
    	   sbSql.append(" and TIMELINESS='"+library.getTimeliness()+"' ");
       }
       if(library!=null && library.getFgcategory()!=null) {
    	   sbSql.append(" and FGCATEGORY='"+library.getFgcategory()+"' ");
       }
       if(library!=null && library.getGbyear()!=null) {
    	   sbSql.append(" and GBYEAR='"+library.getGbyear()+"' ");
       }
       if(type!=null && type.length()>0) {
    	   sbSql.append(" and LRTYPE='"+type+"' ");
       }
       if(library!=null && library.getWknane()!=null) {
       	   sbSql.append(" and WKNANE like'%"+library.getWknane()+"%' ");
        }
       if(library!=null && library.getWkcode()!=null) {
        	sbSql.append(" and WKCODE like '%"+library.getWkcode()+"%' ");
        }
        
        sbSql.append(" order by LIBRARYID");
        return sbSql.toString();
    }
}
