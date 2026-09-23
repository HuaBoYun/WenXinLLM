package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcg;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgGL;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划草稿) 关联】1`
 */
public interface TblYqnsJhglJhcgGLService extends IService<TblYqnsJhglJhcgGL> {


    /**
     * 通过jhcgid查询明细
     * @param token
     * @param jhcgid
     * @return
     * @throws Exception
     */
    List<TblYqnsJhglJhcgGL> findListByJHCGID(String token, String jhcgid) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsJhglJhcgGL vo) throws Exception;

    /**
     * 批量新增修改
     * @param token
     * @param voList
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdateList(String token, long jhcgid,   List<TblYqnsJhglJhcgGL> voList)throws Exception;

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


	JsonBean getGcxmjshzList(String token, Integer pageNumber, Integer pageSize) throws Exception;


	JsonBean getJsxmtzList(String token, Integer pageNumber, Integer pageSize) throws Exception;


	JsonBean getSjdwlrsjSbList(String token, Integer pageNumber, Integer pageSize) throws Exception;


	JsonBean getGcxmjsListByhz(String token, BigDecimal id, String jsdw, Integer pageNumber, Integer pageSize) throws Exception;


	JsonBean getJsxmtzListByhz(String token, BigDecimal id, String tbdwName, Integer pageNumber, Integer pageSize) throws Exception;


	JsonBean getSjdwlrsjSbList(String token, BigDecimal id, BigDecimal orgId, Integer pageNumber, Integer pageSize) throws Exception;
	
	JsonBean selectListByRwall(String token, BigDecimal id) throws Exception;
	
	
	JsonBean selectListByIdall(String token, BigDecimal id) throws Exception;
	
	 JsonBean rwfpry(String token, String ids, String ryids,String rynames,String xmtype) throws Exception;
	 
	 
	 JsonBean selectListBymtRw(String token, BigDecimal id,BigDecimal projectId) throws Exception;
	 
	 
	 JsonBean selectListByIdmyrw(String token, BigDecimal id) throws Exception ;


	List<TblYqnsJhglJhcgGL> findListByJHCGIDByCaogao(String token, String jhcgid) throws Exception;

	JsonBean getJhChuGHuizongList(String token, String jhcgid, String relaid,String glType) throws Exception;
	
	List<TblYqnsGcxmzjZjb>  selectListByRwalllist(String token, BigDecimal id) throws Exception;
	
	
	List<TblYqnsJsxmTzwcqk> selectListByIdalllist(String token, BigDecimal id) throws Exception;
	
	
	JsonBean selectListByRwcfll(String token, BigDecimal id) throws Exception;
	
	
	JsonBean selectListByIdallcf(String token, BigDecimal id) throws Exception ;
	
	
	JsonBean rwfpddry(String token, String ids, String ryids,String rynames,String xmtype) throws Exception;
}
