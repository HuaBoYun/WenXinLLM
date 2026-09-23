package com.huabo.system.service;

import com.huabo.system.entity.TblOrganization;

import java.util.ArrayList;
import java.util.HashMap;

public interface TblOrgExcelService {
	
    Integer importFinancData(HashMap<String, ArrayList<String[]>> var1, String var2, Integer var3, TblOrganization var4) throws Exception;
}
