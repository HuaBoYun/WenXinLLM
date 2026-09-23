package com.huabo.system.service;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblOrganizationInfo;
import com.huabo.system.entity.TblSynchronizationRecord;
import com.huabo.system.utils.Tree;
import com.huabo.system.vo.param.TblOrganizationQueryParam;

/**
 * @author tyb
 * 2016-8-25上午11:34:20
 * @Des:
 */

/**
 * @author tyb
 * 2016-8-25上午11:34:22
 * @Des:
 */
public interface TblOrganizaService {


    Map<String, Object> findOrganizationInfoByModuleId(Integer pageNumber, Integer pageSize, TblOrganization tblOrganization, BigDecimal moduleId);

    void modify(TblOrganization var1);

    Serializable countOrg();

    List<TblOrganization> gsisXj(String id);

    //void deleteOrg(TblOrganization organ);

    void add(TblOrganization ation);

    List<TblOrganization> getHyOrgTree();

    TblOrganization getHY();

    TblOrganization findByname(String var1);

    //List<TblOrganization> findOrgByType(String var1, String var2);

    List<TblOrganization> isParent(String var1);

    //void delete(String var1);

    List<TblOrganization> findHYAuthorize(String var1, String pid) throws Exception;

    Map<String, Object> findAllHYOrg(Integer pageNumber, Integer pageSize);

    Map<String, Object> findAllHYOrgs(Integer pageNumber, Integer pageSize, String pid);

    /**
     * 根据公司主键 分页获取该公司下的所有部门数据
     * @param pageNumber	起始页
     * @param pageSize		每页数量
     * @param find			查询条件
     * @param pid			公司主键
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllOrgBM(Integer pageNumber, Integer pageSize, Find find, BigDecimal pid ) throws Exception;

//	Map<String, Object> findAllHYOrgPid(Integer pageNumber, Integer pageSize, String staffId,String token);

    Map<String, Object> getSJZYK(TblOrganization organization, String wpzjk, Integer currentPage, Integer pageSize);

    List<Tree> getNodeAllbm(BigDecimal nodeId);

    String findOrgByAllJT(String orgid);

    String findOrgByAll(String orgid);

    Map<String, Object> findAllCommpanyPageBeanGS(Integer var1, Integer var2, String var3, String token, Find find, BigDecimal orgId);

    Map<String, Object> findAllCommpanyPageBeanGSXj(Integer var1, Integer var2, String var3, Find var4, String token, String orgId);

    List<Tree> getJTTreeGS(BigDecimal var1);

    List<Tree> getJTNodeAllGS(BigDecimal nodeId);

    Map<String, Object> findAllCommpanyPageBean(Integer pageNumber, Integer pageSize, BigDecimal pid, String orgname, String orgnumber);

//	Map<String, Object> findAllCommpanyPageBean(Integer pageNumber, Integer pageSize, BigDecimal staffId, String token);

    void saveModify(TblOrganization org);

    TblOrganization findByOrgid(String orgid);

    String deleteOrg(TblOrganization organ);

    TblOrganization findByIdOrgid(String pid);

    TblOrganization findByid(String id);

    TblOrganization findId(String id);

    List<TblOrganization> findOrgByType(String pid);

    List<TblOrganization> findPid(String pid);

    TblOrganization findByStringId(String id);

    void delete(BigDecimal id);

    Map<String, Object> findAllHYOrgStaffid(Integer pageNumber, Integer pageSize, String staffId, String token, String pid);

    TblOrganization findByOrg(BigDecimal orgid);

    Map<String, Object> finreportMenuList(Integer pageNumber, Integer pageSize, String type, String token, String orgid);

    Map<String, Object> getJTTree(Integer pageNumber, Integer pageSize, String token, String staffId);

    Map<String, Object> getJTNodeAll(Integer pageNumber, Integer pageSize, BigDecimal nodeId);

    void modiOrganization(TblOrganization organization);

    Integer addReturnId(TblOrganization organization);

    Integer addReturnOrg(TblOrganization organization);

    TblOrganization isCompanyAddWPZ(TblOrganization org);

    void saveAtion(TblOrganization organization);

    void updateZuZhi(TblOrganization organization);

    void saveAtionHangYe(TblOrganization ation);

    TblOrganization findOrgid(String pid);

    void updateAtionHangYe(TblOrganization organization);

    TblOrganization findById(String orgid);

    List<Tree> getTree(BigDecimal nodeId);

    List<Tree> getNodeAll(BigDecimal nodeId);

    String GetTreeOrg(BigDecimal id, Map<BigDecimal, Object> map, String orgid);

    List<Tree> getJTTreeNodeId(BigDecimal nodeId);

    List<TblOrganization> parentHy(String orgids) throws Exception;

    TblOrganization findByoId(BigDecimal orgid);

    TblOrganizationUtil selectFatherOrgIdByID(String orgId);

    String findIniStatus(BigDecimal orgid);

    TblOrganization getHYFirst();

    List<Tree> getTreeHy(BigDecimal nodeId);

    List<Tree> getNodeAllHy(BigDecimal nodeId);

    void updateorgn(TblOrganization orgn);

    Tree getTreeRoot(BigDecimal nodeId);

    TblOrganization get(BigDecimal bigDecimal);

    Map<String, Object> syncAllOrg(Integer operaType, String data) throws Exception;

    void syncOrg(String data) throws Exception;

    JsonBean findOrganInfoDetail(String token, String orgid) throws Exception;

    JsonBean updateOrganInfo(String token, TblOrganizationInfo info) throws Exception;

    Integer selectRepeatName(String orgname, String uniqueNumber, BigDecimal orgId) throws Exception ;

    Integer selectRepeatNumber(String orgnumber,String uniqueNumber, BigDecimal orgId) throws Exception;

	BigDecimal findRootCompanyId(TblOrganizationUtil linkOrg ) throws Exception;

	Map<String, Object> selectDeptListByroleid(String roleid, Integer pageNumber, Integer pageSize, String orgname) throws Exception;

	String getDeptLinkCompanyNameByDeptId(BigDecimal orgid) throws Exception;

	String findCompanyTreeListByOrgName(String orgName) throws Exception;

	JsonBean getallCompanyInfoTree(String orgId, String orgName) throws Exception;

	String selectAllDeptIdsByOrgName(String companyName) throws Exception;

	JsonBean getallCompanyInfoTreeToGrantRole(String orgId, String orgName, BigDecimal roleId) throws Exception;

	Integer findCompanyIdByDeptId(String orgId) throws Exception;

	Integer selectCountByName(String companyName) throws Exception;

	BigDecimal findDeptInfoByorgName(String companyName) throws Exception;

	JsonBean checkCompanyName(String companyName) throws Exception;

	/**
	 * 获取公司和部门所有数据 的 树结构信息
	 * @param token  用户登录令牌
	 * @param query	fatherOrgid- 为空默认查询所有，不为空只查询传入公司主键下的所有公司和部门的数据
	 * @return		status  	-为空默认查询所有 状态 1弃用 ，0-启用
	 * 				orgname		-根据名称筛选查询查询所有组织下的公司和部门数据
	 * @throws Exception
	 */
	JsonBean getAllOrgTree(String token, TblOrganizationQueryParam query) throws Exception;

	/**
	 * 获取公司所有数据 的 树结构信息
	 * @param token  用户登录令牌
	 * @param query	fatherOrgid- 为空默认查询所有，不为空只查询传入公司主键下的所有公司和部门的数据
	 * @return		status  	-为空默认查询所有 状态 1弃用 ，0-启用
	 * 				orgname		-根据名称筛选查询查询所有组织下的公司和部门数据
	 * @throws Exception
	 */
	JsonBean getCompanyInfoTree(String token, TblOrganizationQueryParam query) throws Exception;

	/**
	 * 获取部门所有数据 的 树结构信息
	 * @param token  用户登录令牌
	 * @param query	fatherOrgid- 为空默认查询所有，不为空只查询传入公司主键下的所有公司和部门的数据
	 * @return		status  	-为空默认查询所有 状态 1弃用 ，0-启用
	 * 				orgname		-根据名称筛选查询查询所有组织下的公司和部门数据
	 * @throws Exception
	 */
	JsonBean getDepartmentInfoTree(String token,TblOrganizationQueryParam query) throws Exception;

	JsonBean getCompanyListInfo(String token, TblOrganizationQueryParam query, Integer pageNumber, Integer pageSize) throws Exception;

	JsonBean getDepartmentListInfo(String token, TblOrganizationQueryParam query, Integer pageNumber, Integer pageSize) throws Exception;

	BigDecimal getRootCompanyByOrgId(BigDecimal orgid) throws Exception;

	/**
	 * 递归返回传入公司的主键下的所有子级主键 包含公司和部门
	 * @param orgId
	 * @param orgtype
	 * @return
	 * @throws Exception
	 */
	String getAllChildrenOrgIdStrs(String orgId, Integer orgtype) throws Exception;

	//新方法
	TblOrganization findCompanyInfoByDeptId(BigDecimal orgid) throws Exception;

	void selectSetDeptIdsByOrgId(BigDecimal orgid, Set<String> deptSet) throws Exception;

	/**
	 * 通过传入的Id 获取所有下级的部门Id 包含自己
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	String selectAllDeptIdsByOrgId(BigDecimal pid) throws Exception;

	/**
	 * 查找传入 公司主键下的子集数量
	 * @param orgid --公司主键
	 * @return
	 * @throws Exception
	 */
	Integer selectChildrenCount(String orgid) throws Exception;

	List<TblOrganization> findAllCompany(String orgid, Integer audittype) throws Exception;

	/**
	 * 查找当前公司所有的行业公司数据
	 * @param orgid
	 * @return
	 * @throws Exception
	 */
	List<TblOrganization> findCurrentOrgHyListInfo(BigDecimal orgid) throws Exception;

	/**
	 * 公司或部门启用弃用 修改
	 * @param organization
	 * @throws Exception
	 */
	void changeOrgStatus(TblOrganization organization) throws Exception;

	TblOrganization findByOrgNumber(String orgnumber) throws Exception;

	void importCompanyExcelData(XSSFSheet sheet, String token, Integer importType) throws Exception;

	String checkImportCompany(XSSFSheet sheet, String token) throws Exception;

	String getRootOrganizationTree(BigDecimal nodeId, String type) throws Exception;
	String getRootOrganizationTreeA(BigDecimal nodeId, String type,TblStaffUtil staff) throws Exception;


	//中核
	void syncZhOrg(String data, TblSynchronizationRecord record) throws Exception;

	void syncZhNewOrg(String data, TblSynchronizationRecord record) throws Exception;

	void syncZhDept(String data) throws Exception;

	Map<String,Object> importOrg(String token,String excelPath) throws Exception;

	Map<String,Object> importDept(String token,String excelPath) throws Exception;

	JsonBean enableCompanyInfo(String orgid, String status, TblStaffUtil staff) throws Exception;

	TblOrganization findRootCompanyInfoByOrgId(BigDecimal orgid) throws Exception;

	List<String> findNameByOrgIds(String orgids) throws Exception;
}
