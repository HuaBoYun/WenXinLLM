package com.huabo.compliance.service;


import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.mysql.entity.TblOrganizationMySql;
import com.huabo.compliance.util.Tree;

import java.math.BigDecimal;
import java.util.List;

public interface TreeService {

    TblOrganization belongToCompany(String orgid);

    TblOrganizationMySql belongToMySqlCompany(String orgid);

    List<Tree> findAllCompanyByTree(BigDecimal orgid);

    List<TblOrganization> findAllCompany(String orgid, Integer audittype);

    List<TblOrganizationMySql> findAllMySqlCompany(String orgid, Integer audittype);

    List<TblOrganization> findOrgTreeObjByHY(String orgid);

    List<TblOrganizationMySql> findMySqlOrgTreeObjByHY(String orgid);

    List<Tree> findAllCompanyALL(String orgid, Integer audittype);

//    List<TblOrganization> findAllCompany(String orgid, Integer audittype);

    // List<TblOrganization> findAllCompany(String var1, Integer var2);
}
