package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblSupplier;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblSupplierQueryVO;

public interface ITblSupplierService extends IService<TblSupplier> {
    PageResult<TblSupplier> selectByPage(TblSupplierQueryVO queryVO);
    boolean addRecord(TblSupplier record);
    boolean updateRecord(TblSupplier record);
    boolean deleteRecord(String id);
}

