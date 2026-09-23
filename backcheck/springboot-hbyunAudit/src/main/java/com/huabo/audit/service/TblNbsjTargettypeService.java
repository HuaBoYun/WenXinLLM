package com.huabo.audit.service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjTargettypeEntity;

import java.util.List;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-21
*/
public interface TblNbsjTargettypeService extends IService<TblNbsjTargettypeEntity> {

    /**
    * 条件查询 封装QueryWrapper
    * @param model
    * @return
    */
    LambdaQueryWrapper<TblNbsjTargettypeEntity> onSelectWhere(TblNbsjTargettypeEntity model);

    /**
    *  封装保存方法
    * @param model
    * @return
    */
    boolean saveTblNbsjTargettype(TblNbsjTargettypeEntity model);

    /**
    *  封装更新方法
    * @param model
    * @return
    */
    boolean updateTblNbsjTargettype(TblNbsjTargettypeEntity model);


    /**
     * 根据模板id查询
     * @param teamId
     * @return
     */
    List<TblNbsjTargettypeEntity> selectByTeamId(Integer teamId);

    /**
     * 根据父id查询
     * @param parentId
     * @return
     */
    List<TblNbsjTargettypeEntity> selectByParentId(Integer parentId);

    /**
     * 根据模板id和父节点id获取目标模板信息
     * @param tempId
     * @param parentId
     * @return Integer
     */
    Integer selectCountByTempIdAndPid(Integer tempId,Integer parentId);
    
	JsonBean mergeNbsjTargettypInfo(TblNbsjTargettypeEntity target, String token)throws Exception;
	public JsonBean selectInfo(String targetId) throws Exception;
	
	public JsonBean deleteInfo(String targetId) throws Exception;
	
	public JsonBean findNbsjTargetTree(String templeteId,String nodeId) throws Exception;
	
	public String getTargetTree(String templeteId) throws Exception;
	
}
