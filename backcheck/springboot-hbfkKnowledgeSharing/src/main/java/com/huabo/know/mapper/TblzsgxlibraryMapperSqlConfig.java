package com.huabo.know.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.know.entity.Tblzsgxlibrary;

import java.math.BigDecimal;

public class TblzsgxlibraryMapperSqlConfig {
	
 
	
    public String selectType(PageInfo<Tblzsgxlibrary> pageInfo, Tblzsgxlibrary library,String type,BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select * from (SELECT BUDGET.*,ROWNUM RNUM FROM  ("
        		+ "select * from TBL_ZSGX_LIBRARY   WHERE 1=1 and CREATEORGANID= "+orgid);
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
       if(library!=null && library.getWkname()!=null) {
       	   sbSql.append(" and WKNANE like'%"+library.getWkname()+"%' ");
        }
       if(library!=null && library.getWkcode()!=null) {
        	sbSql.append(" and WKCODE like '%"+library.getWkcode()+"%' ");
        }
        
        sbSql.append(" order by LIBRARYID) BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
        return sbSql.toString();
    }

    public String selectTypeCount( String type,Tblzsgxlibrary library,BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select count(*) from TBL_ZSGX_LIBRARY  WHERE 1=1 AND CREATEORGANID=" + orgid );
        if(library!=null && library.getXllevel()!=null) {
     	   sbSql.append(" and XLLEVEL='"+library.getXllevel()+"' ");
        }
        if(library!=null && library.getWkname()!=null) {
       	   sbSql.append(" and WKNANE like'%"+library.getWkname()+"%' ");
        }
        if(library!=null && library.getWkcode()!=null) {
        	sbSql.append(" and WKCODE like '%"+library.getWkcode()+"%' ");
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
        sbSql.append(" order by LIBRARYID");
        return sbSql.toString();
    }
   
}
