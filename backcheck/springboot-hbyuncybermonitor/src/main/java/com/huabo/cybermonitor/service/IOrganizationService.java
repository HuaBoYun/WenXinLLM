package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.Tree;
import org.apache.ibatis.annotations.Param;

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


     List<Tree> getNodeAll(BigDecimal nodeId);

    /**
     * 指标操作
     * @param orgid
     * @return
     */
    List<Organization> findOrgTreeObjByHY(String orgid);

    /**
     * 指标管理
     * @param orgid
     * @return
     */
    Organization belongToCompany(@Param("orgid")String orgid);

}
