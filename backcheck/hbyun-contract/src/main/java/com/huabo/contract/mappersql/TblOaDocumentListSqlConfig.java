package com.huabo.contract.mappersql;

import com.huabo.contract.entity.TblOaDocumentList;

public class TblOaDocumentListSqlConfig {
	 
    public String insertDocument(TblOaDocumentList doc) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_OA_DOCUMENTLIST (DOCUMENTID");
        StringBuffer value = new StringBuffer(" VALUES ("+doc.getDocumentId());

        if(doc.getId() != null) {
            column.append(",ID");
            value.append(",'"+doc.getId()+"'");
        }
        if(doc.getStateName() != null) {
            column.append(",STATENAME");
            value.append(",'"+doc.getStateName()+"'");
        }
        if(doc.getSubject() != null) {
            column.append(",SUBJECT");
            value.append(",'"+doc.getSubject()+"'");
        }
        if(doc.getUrl() != null) {
            column.append(",URL");
            value.append(",'"+doc.getUrl()+"'");
        }
        if(doc.getH5url() != null) {
            column.append(",H5URL");
            value.append(",'"+doc.getH5url()+"'");
        }
        if(doc.getEdocmark() != null) {
            column.append(",EDOCMARK");
            value.append(",'"+doc.getEdocmark()+"'");
        }
        if(doc.getIsFolder() != null) {
            column.append(",ISFOLDER");
            value.append(",'"+doc.getIsFolder()+"'");
        }
        if(doc.getSendName() != null) {
            column.append(",SENDNAME");
            value.append(",'"+doc.getSendName()+"'");
        }
        if(doc.getFrtypeName() != null) {
            column.append(",FRTYPENAME");
            value.append(",'"+doc.getFrtypeName()+"'");
        }
        if(doc.getContractId() != null) {
            column.append(",CONTRACTID");
            value.append(",'"+doc.getContractId()+"'");
        }
        if(doc.getCateGoryName()!= null) {
            column.append(",CATEGORYNAME");
            value.append(",'"+doc.getCateGoryName()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
	
}
 


