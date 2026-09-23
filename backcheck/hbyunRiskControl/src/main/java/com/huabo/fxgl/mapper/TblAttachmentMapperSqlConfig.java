package com.huabo.fxgl.mapper;

import java.util.Date;

import com.hbfk.util.DateUtil;
import com.huabo.fxgl.entity.TblAttachment;

public class TblAttachmentMapperSqlConfig {

	public String insertEntity(TblAttachment att) {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_ATTACHMENT(ATTID,UPLOADER,UPLOADTIME");
		StringBuffer valSb = new StringBuffer(" VALUES ( HIBERNATE_SEQUENCE.nextval,'"+att.getUploader()+"',TO_DATE('"+DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		
		if(att.getAttname() != null && !"".equals(att.getAttname())) {
			colSb.append(",ATTNAME");
			valSb.append(",'"+att.getAttname()+"'");
		}
		
		if(att.getAttpath() != null && !"".equals(att.getAttpath())) {
			colSb.append(",ATTPATH");
			valSb.append(",'"+att.getAttpath()+"'");
		}
		
		if(!"".equals(att.getAttsize())) {
			colSb.append(",ATTSIZE");
			valSb.append(",'"+att.getAttsize()+"'");
		}
		
		if(att.getMemo() != null && !"".equals(att.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+att.getMemo()+"'");
		}
		
		if(att.getMemo() != null && !"".equals(att.getMemo())) {
			colSb.append(",UPLOADTIME");
			valSb.append(",'"+att.getMemo()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
  /*  public String add(TblAttachment att) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ATTACHMENT (ATTID");
        StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(att.getAttname() != null && !"".equals(att.getAttname())) {
            column.append(",ATTNAME");
            value.append(",'"+att.getAttname()+"'");
        }
        if(att.getAttpath() != null && !"".equals(att.getAttpath())) {
            column.append(",ATTPATH");
            value.append(",'"+att.getAttpath()+"'");
        }
        if(!"".equals(att.getAttsize())) {
            column.append(",ATTSIZE");
            value.append(",'"+att.getAttsize()+"'");
        }
        if(att.getMemo() != null && !"".equals(att.getMemo())) {
            column.append(",MEMO");
            value.append(",'"+att.getMemo()+"'");
        }
        if(att.getUploadtime() != null && !"".equals(att.getUploadtime())) {
            column.append(",UPLOADTIME");
            value.append(",TO_DATE('"+ DateUtil.parseDate(att.getUploadtime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(att.getUploader() != null && !"".equals(att.getUploader())) {
            column.append(",UPLOADER");
            value.append(",'"+att.getUploader()+"'");
        }
        if(att.getIspythonflag() != null && !"".equals(att.getIspythonflag())) {
            column.append(",ISPYTHONFLAG");
            value.append(",'"+att.getIspythonflag()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }*/
}
