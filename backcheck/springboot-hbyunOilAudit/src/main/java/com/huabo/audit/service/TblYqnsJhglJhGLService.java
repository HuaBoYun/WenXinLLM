package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhGL;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划) 关联】
 */
public interface TblYqnsJhglJhGLService extends IService<TblYqnsJhglJhGL> {


    /**
     * 通过jhcgid查询明细
     * @param token
     * @param jhid
     * @return
     * @throws Exception
     */
    List<TblYqnsJhglJhGL> findListByJHID(String token, String jhid) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsJhglJhGL vo) throws Exception;

    /**
     * 批量新增修改
     * @param token
     * @param voList
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdateList(String token, long jhcgid,   List<TblYqnsJhglJhGL> voList)throws Exception;

    /**
     * 一个删除
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    JsonBean deleteGL(String token, String id) throws Exception;


	JsonBean deleteGLByIds(String token, BigDecimal jhid, BigDecimal formid) throws Exception;


	JsonBean getSjdwlrsjSbList(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber, Integer pageSize,String type) throws Exception;


	JsonBean getGcxmjsListByhz(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber, Integer pageSize) throws Exception;


	JsonBean getJsxmtzListByhz(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber, Integer pageSize) throws Exception;
	
	
	JsonBean getcwanbList(String token, BigDecimal glid, BigDecimal id, Integer pageNumber,Integer pageSize) throws Exception;



}
