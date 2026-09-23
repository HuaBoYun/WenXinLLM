package com.huabo.system.service;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblBiReportMenu;
import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblManageUserRight;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.utils.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblManageRightService {
    TblManageRight findById(String pri);

    List<Tree> getOrgFatherRightforRedis(TblOrganization var1) throws Exception;

    /**
     * A 一级主题仓库下发业务逻辑
     * @param moduleTypes - 报表下发所属模块数组
     * @param pageids     - 一级主题仓库下发数组
     * @param token       - 当前用户登录的token令牌
     * @return JsonBean
     * @throws Exception
     * --数据库新增字段
     * alter table TBL_SYSTEM_BIMODULE add MODULETYPE VARCHAR2(50);
     */
    JsonBean distributionPageIdByModuleType(String[] moduleTypes, String[] pageids, String token) throws Exception;

    Map<String,Object>  findRightListbyModule(String token, String staffId);

    String getTreeListSettingMenu(BigDecimal tmplId, Map<BigDecimal, Object> map, String token, String staffId);

    List<TblManageRight> findByManageParentId(BigDecimal fatherrightid, String token, String staffId) throws Exception;

    TblManageRight findByRightname(String rightname);

    String getRightForUser(String token, String staffId);

    void delright(TblManageRight vmr);

    JsonBean distributionPageIdByRightIdTwo(String[] moduleTypes, String[] pageids, String token, String pid) throws Exception;

    String GetTree(String staffid, Map<BigDecimal, Object> map, String orgid);

    JsonBean findChildrenRightListByUser(String token, BigDecimal rightId) throws Exception;

	JsonBean saveManageRight(TblManageRight right, String token) throws Exception;

	JsonBean findRightEntityById(BigDecimal rightId) throws Exception;

	JsonBean modifyManageRight(TblManageRight right, String token) throws Exception;

	JsonBean removeManageRight(BigDecimal rightId) throws Exception;

    void grantScreenRight(String userid, String priid);

    void save(TblManageRight viewTblManageRight);

    List<TblManageRight> findByUserAll(String userid);

    void updateright(TblManageRight tblManageRight);

    void inserUserRight(TblManageUserRight userRight);

    List<TblManageRight> findByorgid(BigDecimal id);

    public List<TblBiReportMenu> findBiReportList(BigDecimal orgid, BigDecimal rightid) throws Exception;

    /**
     * 主题仓库取消模块下发 获取模块集合
     * @param pageids
     * @param token
     * @return
     * @throws Exception
     */
	JsonBean cancelModuleList(String[] pageids, String token) throws Exception;

	/**
	 * 取消主题模块下发
	 * @param pageids
	 * @param moduleTypes
	 * @param type 
	 * @param token 
	 * @return
	 * @throws Exception
	 */
	JsonBean cancelBiModule(String[] pageids, String[] moduleTypes, String token, Integer type) throws Exception;

	/**
	 * 取消主题用户下发数据
	 * @param pageids
	 * @param staffids
	 * @param token
	 * @param type
	 * @return
	 * @throws Exception
	 */
	JsonBean cancelBiStaff(String[] pageids, String[] staffids, String token, Integer type) throws Exception;
	
}
