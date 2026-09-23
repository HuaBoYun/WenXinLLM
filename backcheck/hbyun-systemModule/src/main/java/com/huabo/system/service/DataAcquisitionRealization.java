package com.huabo.system.service;

import com.huabo.system.entity.TblOrgExcel;
import com.huabo.system.entity.TblOrganization;

import java.util.ArrayList;
import java.util.HashMap;

public interface DataAcquisitionRealization {
    void acquisitionData(HashMap<String, ArrayList<String[]>> hashMap, String year, Integer type, TblOrganization organization, TblOrgExcel sqlOrg, String dataName) throws Exception;

}
