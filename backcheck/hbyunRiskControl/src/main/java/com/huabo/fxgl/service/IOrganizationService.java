package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hbfk.util.JsonBean;

import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.util.Tree;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import com.huabo.fxgl.entity.Staff;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
public interface IOrganizationService extends IService<Organization> {
     /**
      * 模型下组织机构列表
      * @param orgid 级别机构编码
      * @return
      */
     List<Organization> findOrganizationByOrgid(String orgid);

     /**
      * 行业模型库---》左侧菜单
      * @return
      */
     List<Organization> findOrganizationAll();


     /**
      * 初始化新组织类型
      */
     void initOrgByOrgtype(int orgtype, String orgname, String orgid);


     /**
      * 通过组织类型和组织编号返回组织树
      * @param orgid    编号
      * @param orgtype  类型
      * @param hyzskType 标识
      * @return
      */
     String findOrgTreeByOrgtypeAndOrgid(String orgid, int orgtype, String hyzskType);

    String getOrgTree(String kri_info_seach);
    Organization findById(String id);

     Organization getHYFirst();
     String findOrgByorgId(String orgid);


    public List<Tree> getNodeAllbm(BigDecimal nodeId);

    JsonBean getOrganizationTree(String str, String orgid, String hbOrgEntity) throws Exception;
    JsonBean userLeft(String nodeId, String type, String orgId, String token) throws Exception;

    boolean isAuditByOrgId(String toString);

    List<BigDecimal> getIdsByFatherId(BigDecimal orgid);
    
    List<BigDecimal> getIdsByFathersId(BigDecimal orgid);

    //根据公司名称获取所有的子集部门id
    List<BigDecimal> getAllDeptIds(BigDecimal orgid);


    JsonBean getAllDepartmentWithTree(String type, String nodeId, String token) throws Exception;
//    JsonBean getAllDepartmentWithTree(String type, BigDecimal nodeId, Organization hbOrgEntity);

     /**
      * 显示详情
      * @param userName
      * @return
      */
     List<Organization> findOrgByUser(String userName);


     Boolean isSJByOrgId(String userOrgid);
//    List<Tree> getTree(BigDecimal nodeId);
    List<Tree> getNodeAllJT(BigDecimal nodeId);

    List<Tree> getTreeHy(BigDecimal nodeId);
    List<Tree> getNodeAllHy(BigDecimal nodeId);
    
    
    List<Tree> getTrees(BigDecimal nodeId);
    
    List<Tree> getNodeAlls(BigDecimal nodeId);
    
    String selectNamesByids(String ids) throws Exception;
    
    String selectIdsByNames(String ids,BigDecimal orgid) throws Exception;
    
    String selectNamesByNames(String ids,BigDecimal orgid) throws Exception;


	  String selectNameByids(BigDecimal id) throws Exception;

	  
	    List<Organization> getOrgsByOrgids(String ids,List<BigDecimal> orgids) throws Exception;


}
