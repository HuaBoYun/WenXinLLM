package com.huabo.cybermonitor.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblBiDatasource;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-09-07
 */
public interface ITblBiDatasourceService extends IService<TblBiDatasource> {


    String getTree(String orgid);
    public List<TblBiDatasource> getTreeList(String orgid);
}
