package com.huabo.compliance.service;

import com.huabo.compliance.entity.TblRiskAttWord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-09-13
 */
public interface ITblRiskAttWordService extends IService<TblRiskAttWord> {


    TblRiskAttWord getFile(String reportType, String toString, String id);
}
