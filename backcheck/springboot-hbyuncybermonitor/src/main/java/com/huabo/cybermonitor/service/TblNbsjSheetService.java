package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.cybermonitor.entity.TblNbsjSheetEntity;
import com.huabo.cybermonitor.vo.TBlNbsjSheetVo;

import java.util.List;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-20
*/
public interface TblNbsjSheetService extends IService<TblNbsjSheetEntity> {


    JsonBean dgAllPageList(String token, Integer pageNumber, Integer pageSize, TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;
    JsonBean dgglPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;

    JsonBean findNbsjSheetDetail(String token, Integer sheetid) throws Exception;




}
