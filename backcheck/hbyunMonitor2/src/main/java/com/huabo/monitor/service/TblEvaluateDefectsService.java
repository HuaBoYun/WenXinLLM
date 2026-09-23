package com.huabo.monitor.service;

import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblEvaluateDefects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.w3c.dom.ls.LSInput;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface TblEvaluateDefectsService extends IService<TblEvaluateDefects> {

/*
 * 列表页面查询
 * param:
 * */
	PageInfo<TblEvaluateDefects> getHomepage_List(Integer pageNumber,Integer pageSize,Integer authorityType,TblEvaluateDefects def) throws Exception;

	Map<String, Object> saveOrUpdate(String token,TblEvaluateDefects entity,String attids)throws Exception;
	
	TblEvaluateDefects getDetatilsById(BigDecimal id)throws Exception;

	String deleteById(BigDecimal id)throws Exception;

	List<TblEvaluateDefects> exportPjqx(Integer authorityType,TblEvaluateDefects def) throws Exception;

	List<com.huabo.monitor.entity.TblAttachment> getAttList(BigDecimal id) throws Exception;
}
