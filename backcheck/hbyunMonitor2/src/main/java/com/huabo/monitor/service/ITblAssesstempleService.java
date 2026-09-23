package com.huabo.monitor.service;

import com.huabo.monitor.entity.TblAssesstemple;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface ITblAssesstempleService extends IService<TblAssesstemple> {


	IPage<TblAssesstemple> findAll(BigDecimal orgid, Integer pageNumber, TblAssesstemple assesstemple);
	
	List<TblAssesstemple> getTmplByNumber(String number, BigDecimal orgid);
	
	Serializable add(TblAssesstemple tblAssesstemple);
	
	void modify (TblAssesstemple tblAssesstemple);
	
	TblAssesstemple findByid(BigDecimal id);
	
	void delete(TblAssesstemple tblAssesstemple);
	
	void saveTempleOrg(BigDecimal orgid,BigDecimal tempid);
	
	void removeTempleOrg(BigDecimal tempid);
 
	void insertTemples (TblAssesstemple tblAssesstemple)throws Exception;
	
	void updateTemples (TblAssesstemple tblAssesstemple)throws Exception;

    PageInfo<TblAssesstemple>  findAllNewPage(String orgid,Integer pageNumber,TblAssesstemple assesstemple,Integer pageSize,TblStaffUtil user)throws Exception;

}
