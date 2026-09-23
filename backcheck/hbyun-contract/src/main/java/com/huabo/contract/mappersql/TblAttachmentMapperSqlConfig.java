package com.huabo.contract.mappersql;

import java.util.Date;

import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblAttachment;

public class TblAttachmentMapperSqlConfig {
	
	public String insertEntity(TblAttachment att) throws Exception {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_ATTACHMENT(ATTID,UPLOADER,UPLOADTIME");
		StringBuffer valSb = new StringBuffer(" VALUES ("+att.getAttid()+",'"+att.getUploader()+"',"+DataBaseSqlConfig.getDateStrFormat(new Date()));
		
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
	
    public String add(TblAttachment att) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ATTACHMENT (ATTID");
        StringBuffer value = new StringBuffer(" VALUES ("+att.getAttid());

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
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(att.getUploadtime()));
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
    }
}
