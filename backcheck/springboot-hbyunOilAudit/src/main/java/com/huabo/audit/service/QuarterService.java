package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.QuarterEntity;

public interface QuarterService extends IService<QuarterEntity> {

    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, QuarterEntity entity) throws Exception;

    JsonBean findById(String id) throws Exception;;

    void updateEntity(QuarterEntity quarterEntity) throws Exception;;

    void saveEntity(String token, QuarterEntity quarterEntity) throws Exception;;

    void deleteByIds(String ids) throws Exception;;
}
