package com.huabo.system.service.impl;

import com.huabo.system.entity.TblOrgExcel;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.mapper.TblAccountMapper;
import com.huabo.system.service.DataAcquisitionRealization;
import com.huabo.system.service.TblOrgExcelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class TblOrgExcelServiceImpl implements TblOrgExcelService {

    @Resource
    private TblAccountMapper tblAccountMapper;


    @Override
    public Integer importFinancData(HashMap<String, ArrayList<String[]>> hashMap, String year, Integer type, TblOrganization organization) throws Exception {
        String dataName = SysConfig.get("cwsjBaseName");
        TblOrgExcel sqlOrg = this.tblAccountMapper.findTblSqlOrg(dataName, organization.getOrgid(), year, year);
        if (sqlOrg == null) {
            return 0;
        } else {
            if (type == 3) {
                Integer count = this.tblAccountMapper.listBySqlPageCount(dataName, organization.getOrgid(), year);
                if (count == 0) {
                    return -5;
                }
            }

            DataAcquisitionRealization datareal = (new DataAcquisitionFactoryImpl()).dataAcquisitionRealization(sqlOrg);
            datareal.acquisitionData(hashMap, year, type, organization, sqlOrg, dataName);
            return 1;
        }
    }

}
