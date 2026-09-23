package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhchugGL;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JHGL_JHCHUG_G(计划初稿管理计划) 关联】
 */
public interface TblYqnsJhglJhchugGLService extends IService<TblYqnsJhglJhchugGL> {


    /**
     * 通过jhcgid查询明细
     * @param token
     * @param jhchugid
     * @return
     * @throws Exception
     */
    List<TblYqnsJhglJhchugGL> findListByJHCHUGID(String token, String jhchugid) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsJhglJhchugGL vo) throws Exception;

    /**
     * 批量新增修改
     * @param token
     * @param voList
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdateList(String token, long jhchugid,   List<TblYqnsJhglJhchugGL> voList)throws Exception;

    /**
     * 一个删除
     *
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    JsonBean deleteGL(String token, String id) throws Exception;


	JsonBean deleteGLByIds(String token, BigDecimal jhcgid, BigDecimal formid) throws Exception;


	JsonBean getSjdwlrsjSbList(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber, Integer pageSize) throws Exception;


	JsonBean getGcxmjsListByhz(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber, Integer pageSize) throws Exception;


	JsonBean getJsxmtzListByhz(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber, Integer pageSize) throws Exception;


	List<TblYqnsJhglJhchugGL> findListByJHCGIDByChugao(String token, String string) throws Exception;


	JsonBean getJhChuGHuizongList(String token, String jhchugid, String relaid, String glType) throws Exception;



}
