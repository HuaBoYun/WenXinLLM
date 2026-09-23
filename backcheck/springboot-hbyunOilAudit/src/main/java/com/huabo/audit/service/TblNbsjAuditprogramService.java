package com.huabo.audit.service;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjAuditprogramEntity;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-20
*/
public interface TblNbsjAuditprogramService extends IService<TblNbsjAuditprogramEntity> {

    /**
    * 条件查询 封装QueryWrapper
    * @param model
    * @return
    */
    LambdaQueryWrapper<TblNbsjAuditprogramEntity> onSelectWhere(TblNbsjAuditprogramEntity model);

    /**
    *  封装保存方法
    * @param model
    * @return
    */
    boolean saveTblNbsjAuditprogram(TblNbsjAuditprogramEntity model);

    /**
    *  封装更新方法
    * @param model
    * @return
    */
    boolean updateTblNbsjAuditprogram(TblNbsjAuditprogramEntity model);

    List<TblNbsjAuditprogramEntity> selectByTempId(Integer tempId);

    List<TblNbsjAuditprogramEntity> selectByTargetId(Integer targetId);
    
	JsonBean mergeTblNbsjAuditprogramInfo(TblNbsjAuditprogramEntity program, String token)throws Exception;

	JsonBean selectInfo(String programId);

	JsonBean deleteInfo(String programId);

	JsonBean selectTblNbsjAuditprogramListByPageInfo(String token, Integer pageNumber, Integer pageSize,
			String templeteId, String targetId) throws Exception ;

}
