package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.entity.BiDatasource;

public interface IBiDatasourceService extends IService<BiDatasource> {
    public String getTree(String orgid);
}
