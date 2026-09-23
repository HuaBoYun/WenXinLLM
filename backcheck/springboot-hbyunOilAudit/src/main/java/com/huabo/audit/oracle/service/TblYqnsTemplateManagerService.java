package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsTemplateManagerEntity;
import com.huabo.audit.oracle.entity.TblYqnsTemplateManagerEntity;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsTemplateManagerService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-基础配置-模板管理
 */
public interface TblYqnsTemplateManagerService extends IService<TblYqnsTemplateManagerEntity> {


    /**
     * 查询模板管理
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     */
    JsonBean getTemplateList(String token, Integer pageNumber, Integer pageSize, TblYqnsTemplateManagerEntity vo) throws Exception;


    /**
     * 根据模板管理id查询模板管理
     * @param id
     * @return
     */
    JsonBean getTemplateById(String token, Long id)  throws Exception;

    /**
     * 保存或更新模板管理
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsTemplateManagerEntity vo)  throws Exception;

    /**
     * 删除模板管理(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;



    /**
     * 删除模板管理 附件
     * @param token
     * @param attId
     * @return
     * @throws Exception
     */
    JsonBean deleteFileAttach(String token,String attId) throws Exception;

}
