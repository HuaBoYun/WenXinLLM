package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblOrganization;
import com.huabo.system.utils.Tree;

public interface TreeService {

    TblOrganization belongToCompany(String orgid);

    List<TblOrganization> findAllCompany(String orgid, Integer audittype);

    List<Tree> findAllCompanyALL(String orgid, Integer audittype);

	List<Tree> findAllCompanyByTree(BigDecimal orgid);

}
