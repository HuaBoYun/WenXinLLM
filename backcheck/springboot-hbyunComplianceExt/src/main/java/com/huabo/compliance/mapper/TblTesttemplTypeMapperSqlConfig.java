package com.huabo.compliance.mapper;



import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.huabo.compliance.entity.TblTesttemplType;

public class TblTesttemplTypeMapperSqlConfig {
	
	public String selectTreeListInfo(BigDecimal parentId, BigDecimal testtempletaid) {
		String sql = "SELECT * FROM TBL_COM_EXT_TESTTEMPL_TYPE WHERE TESTTEMPLETAID = "+testtempletaid;
		if(parentId != null) {
			sql += " AND PARENTID = "+parentId;
		}else {
			sql += " AND PARENTID IS NULL";
		}
		return sql;
	}
	

	public String insertEntity(TblTesttemplType newType) {
		String colSql = "INSERT INTO TBL_COM_EXT_TESTTEMPL_TYPE(TYPEID, TYPENAME, TESTTEMPLETAID, CREATETIME ";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval,'"+newType.getTypename()+"','"+newType.getTesttempletaid()+"',SYSDATE";
		
		if (newType.getParentid() != null) {
			colSql += ",PARENTID ";
			valueSql += ","+newType.getParentid();
        }
		
		if (StringUtils.isNotBlank(newType.getTypedesc())) {
			colSql += ",TYPEDESC ";
			valueSql += ",'"+newType.getTypedesc()+"'";
        }
		
		if (StringUtils.isNotBlank(newType.getTypecode())) {
			colSql += ",TYPECODE ";
			valueSql += ",'"+newType.getTypecode()+"'";
        }
		
		return colSql + ")" + valueSql + ")";
	}
	
	public String updateEntity(TblTesttemplType type) {
		String sql = "UPDATE TBL_COM_EXT_TESTTEMPL_TYPE SET TYPENAME = '"+type.getTypename()+"',TESTTEMPLETAID = "+type.getTesttempletaid();
		
		if (type.getParentid() != null) {
			sql += ",PARENTID ="+type.getParentid();
        }
		
		if (StringUtils.isNotBlank(type.getTypedesc())) {
			sql += ",TYPEDESC = '"+type.getTypedesc()+"'";
        }
		
		if (StringUtils.isNotBlank(type.getTypecode())) {
			sql += ",TYPECODE = '"+type.getTypecode()+"'";
        }
		
		sql += " WHERE TYPEID = "+type.getTypeid();
		return sql;
	}
}
