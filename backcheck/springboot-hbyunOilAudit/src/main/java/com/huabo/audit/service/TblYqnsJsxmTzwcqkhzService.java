package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkhz;

public interface TblYqnsJsxmTzwcqkhzService extends IService<TblYqnsJsxmTzwcqkhz> {

    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsJsxmTzwcqkhz vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsJsxmTzwcqkhz vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsJsxmTzwcqkhz vo) throws Exception;

    
    /**
     * 汇总查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean hzlist(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmTzwcqkhz vo) throws Exception;
    
    JsonBean getJsxmtzList(String token, Integer pageNumber, Integer pageSize) throws Exception;

    /**
     * 工程建设项目竣工决算-明细
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    JsonBean getJsxmjgjsList(String token, Integer pageNumber, Integer pageSize,String year) throws Exception;


}
