package com.huabo.financialdata.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookRequestVo;
import com.huabo.financialdata.entity.vo.auxiliaryBook.AuxiliaryBookResponsetVo;

/**
 * <p>
 * 辅助账  接口类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-21
 */
public interface AuxiliaryBookService {

    //查询辅助账数据
    ApiResponse<PageInfo<AuxiliaryBookResponsetVo>> getList(String token, AuxiliaryBookRequestVo auxiliaryBookRequestVo) throws Exception;

    // 获取辅助账类型数据接口
	ApiResponse<List<String>> getAssTypeList(String token) throws Exception;

}
