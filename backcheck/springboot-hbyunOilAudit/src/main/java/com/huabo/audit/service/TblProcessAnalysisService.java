package com.huabo.audit.service;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblProcessAnalysis;

import java.math.BigDecimal;
import java.util.List;

public interface TblProcessAnalysisService {
    List<TblProcessAnalysis> getByModuel(String settingid);

    TblProcessAnalysis findOndBytakdidstart(String blande, String settingid);

    boolean findByModuel(String flownumber, TblStaffUtil user);

    TblProcessAnalysis findOnd(String analid);

    TblProcessAnalysis findOndBytakdid(String usertaskid);

    TblProcessAnalysis findOndBytakdidAnId(String usertaskid,String anid);

}
