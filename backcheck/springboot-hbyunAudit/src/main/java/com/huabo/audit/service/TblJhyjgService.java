package com.huabo.audit.service;

import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.vo.TblReportVo;
import com.huabo.audit.util.R;

public interface TblJhyjgService {

    JsonBean zdyPageList(String token, Integer pageNumber, Integer pageSize, TblReportVo tblReportVo, Integer projectId) throws Exception;
}
