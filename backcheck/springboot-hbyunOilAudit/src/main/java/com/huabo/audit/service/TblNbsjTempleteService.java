package com.huabo.audit.service;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjTempleteEntity;
import com.huabo.audit.oracle.vo.TblNbsjTempleteVo;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-20
*/
public interface TblNbsjTempleteService {

    /**
    * 条件查询 封装QueryWrapper
    * @param model
    * @return
    */
    LambdaQueryWrapper<TblNbsjTempleteEntity> onSelectWhere(TblNbsjTempleteEntity model);

    /**
    *  封装保存方法
    * @param model
    * @return
    */
    boolean saveTblNbsjTemplete(TblNbsjTempleteEntity model);

    /**
    *  封装更新方法
    * @param model
    * @return
    */
    boolean updateTblNbsjTemplete(TblNbsjTempleteEntity model);

	void delete(BigDecimal bigDecimal);
	
	/**
	 * 根据审计类型查询审计模板
	 * @param templeteName
	 * @param templeteCode
	 * @param status
	 * @param tempType
	 * @param pageNumber
	 * @param pageSize
	 * @param orgId
	 * @return
	 */
    JsonBean selectNbsjTempleteListByPageInfo(String token, Integer pageNumber, Integer pageSize,TblNbsjTempleteVo tblNbsjTempleteVo) throws Exception;
	JsonBean mergeNbsjTempleteInfo(TblNbsjTempleteEntity templete, String token,String orgids)throws Exception;
//	public PageBean findTempleteByType(String auditType,Integer tempType,Integer pageNumber,Integer pageSize, String orgId);
//
//	PageBean findTemplete(String templeteName, String templeteCode, Integer status, Integer zyType, Integer pageNumber,
//			int pageSize);
	JsonBean updateStatus(String templeteId) throws Exception;
	
	JsonBean copyTemplete(String templeteId,Integer copytype,String token) throws Exception;
	
	public JsonBean selectInfo(String templeteId) throws Exception;
	
	public JsonBean deleteInfo(String templeteId) throws Exception;

	

}
