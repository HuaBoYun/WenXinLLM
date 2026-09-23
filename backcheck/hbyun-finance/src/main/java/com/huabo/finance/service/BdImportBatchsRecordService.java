package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.BdImportBatchsRecord;
import com.huabo.finance.vo.BdImportBatchsRecordVo;

public interface BdImportBatchsRecordService extends IService<BdImportBatchsRecord> {

	JsonBean getImportRecordList(BdImportBatchsRecordVo vo) throws Exception;

	JsonBean getImportRecordDetail(String importId) throws Exception;

	JsonBean removeImportData(String importId, String tableId) throws Exception;


}
