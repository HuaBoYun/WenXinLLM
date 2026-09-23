package com.huabo.audit.service;

import com.huabo.audit.oracle.entity.TblProcessAnalusisUser;

public interface TblProcessAnalusisUserService {
    TblProcessAnalusisUser findOnd(String anaId, String formId);

    void updateSetting(TblProcessAnalusisUser analysisUser);

    void insertSetting(TblProcessAnalusisUser analysisUser) throws Exception;

}
