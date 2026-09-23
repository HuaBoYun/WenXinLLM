package com.huabo.monitor.service;


import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.Find;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblOrganizationInfo;
import com.huabo.monitor.mysql.entity.FindMySql;
import com.huabo.monitor.mysql.entity.TblOrganizationMySql;
import com.huabo.monitor.util.Tree;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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


	Map<String, Object> findOrganizationInfoByModuleId(Integer pageNumber, Integer pageSize, TblOrganization tblOrganization, Integer moduleId);

	Map<String, Object> findMySqlOrganizationInfoByModuleId(Integer pageNumber, Integer pageSize, TblOrganizationMySql tblOrganization, Integer moduleId);

	void modify(TblOrganization var1);

	void modifyMySql(TblOrganizationMySql var1);

	Serializable countOrg();

	List<TblOrganization> gsisXj(String id);

	List<TblOrganizationMySql> gsisXjMySql(String id);

	List<TblOrganization> findChildrenByOrgid(String var1);

	List<TblOrganizationMySql> findChildrenByMySqlOrgid(String var1);

	//void deleteOrg(TblOrganization organ);

	void add(TblOrganization ation);

	void addMySql(TblOrganizationMySql ation);

	List<TblOrganization> getHyOrgTree();

	List<TblOrganizationMySql> getMySqlHyOrgTree();

	TblOrganization getHY();

	TblOrganizationMySql getMySqlHY();

	TblOrganization findByname(String var1);

	TblOrganizationMySql findByMySqlname(String var1);

	//List<TblOrganization> findOrgByType(String var1, String var2);

	List<TblOrganization> isParent(String var1);

	List<TblOrganizationMySql> isParentMySql(String var1);

	//void delete(String var1);

	List<TblOrganization> findHYAuthorize(String var1, String pid);

	List<TblOrganizationMySql> findMySqlHYAuthorize(String var1, String pid);

	Map<String, Object> findAllHYOrg(Integer pageNumber,Integer pageSize);

	Map<String, Object> findAllHYOrgs(Integer pageNumber, Integer pageSize, String pid);

	Map<String, Object> findAllOrgBM(Integer pageNumber, Integer pageSize, Find find, BigDecimal pid) throws Exception;

	Map<String, Object> findAllMySqlOrgBM(Integer pageNumber, Integer pageSize, Find find, BigDecimal pid) throws Exception;

//	Map<String, Object> findAllHYOrgPid(Integer pageNumber, Integer pageSize, String staffId,String token);

	Map<String, Object> getSJZYK(TblOrganization organization, String wpzjk, Integer currentPage, Integer pageSize);

	Map<String, Object> getMySqlSJZYK(TblOrganizationMySql organization, String wpzjk, Integer currentPage, Integer pageSize);

	List<Tree> getNodeAllbm(BigDecimal nodeId);

    String findOrgByAllJT(String orgid);

	String findOrgByAll(String orgid);

    Map<String, Object> findAllCommpanyPageBeanGS(Integer var1, Integer var2, String var3,String token,Find find,BigDecimal orgId);

	Map<String, Object> findAllMySqlCommpanyPageBeanGS(Integer var1, Integer var2, String var3,String token,Find find,BigDecimal orgId);

	Map<String, Object> findAllCommpanyPageBeanGSXj(Integer var1, Integer var2, String var3, Find var4,String token,String orgId);

	Map<String, Object> findAllMySqlCommpanyPageBeanGSXj(Integer var1, Integer var2, String var3, FindMySql var4, String token, String orgId);

	List<Tree> getJTTreeGS(BigDecimal var1);

	List<Tree> getJTNodeAllGS(BigDecimal nodeId);

	Map<String, Object> findAllCommpanyPageBean(Integer pageNumber, Integer pageSize, BigDecimal pid);

//	Map<String, Object> findAllCommpanyPageBean(Integer pageNumber, Integer pageSize, BigDecimal staffId, String token);

	void saveModify(TblOrganization org);

	void saveMySqlModify(TblOrganizationMySql org);

	TblOrganization findByOrgid(String orgid);

	TblOrganizationMySql findByMySqlOrgid(String orgid);

    String deleteOrg(TblOrganization organ);

	String deleteMySqlOrg(TblOrganizationMySql organ);

	TblOrganization findByIdOrgid(String pid);

	TblOrganizationMySql findByMySqlIdOrgid(String pid);

	TblOrganization findByid(String id);

	TblOrganizationMySql findByMySqlid(String id);

	TblOrganization findId(String id);

	TblOrganizationMySql findMySqlId(String id);

	List<TblOrganization> findOrgByType(String pid);

	List<TblOrganizationMySql> findOrgByMySqlType(String pid);

	List<TblOrganization> findPid(String pid);

	List<TblOrganizationMySql> findMySqlPid(String pid);

	TblOrganization findByStringId(String id);

	TblOrganizationMySql findByMySqlStringId(String id);

	void delete(BigDecimal id);

	Map<String, Object> findAllHYOrgStaffid(Integer pageNumber, Integer pageSize, String staffId,String token,String pid);

	TblOrganization findByOrg(BigDecimal orgid);

	TblOrganizationMySql findByMySqlOrg(BigDecimal orgid);

    Map<String, Object> finreportMenuList(Integer pageNumber, Integer pageSize, String type,String token,String orgid);

	Map<String, Object> getJTTree(Integer pageNumber, Integer pageSize, String token, String staffId);

	Map<String, Object> getJTNodeAll(Integer pageNumber, Integer pageSize, BigDecimal nodeId);

	void modiOrganization(TblOrganization organization);

	void modiOrganizationMySql(TblOrganizationMySql organizationMySql);

	Integer addReturnId(TblOrganization organization);

	Integer addMySqlReturnId(TblOrganizationMySql organization);

	void isCompanyAddWPZJ(TblOrganization org);

	void isCompanyAddMySqlWPZJ(TblOrganizationMySql org);

	Integer addReturnOrg(TblOrganization organization);

	Integer addMySqlReturnOrg(TblOrganizationMySql organization);

	TblOrganization isCompanyAddWPZ(TblOrganization org);

	TblOrganizationMySql isCompanyAddMySqlWPZ(TblOrganizationMySql org);

	void saveAtion(TblOrganization organization);

	void saveMySqlAtion(TblOrganizationMySql organization);

    void updateZuZhi(TblOrganization organization);

	void updateMySqlZuZhi(TblOrganizationMySql organization);

	void saveAtionHangYe(TblOrganization ation);

	TblOrganization findOrgid(String pid);

	TblOrganizationMySql findMySqlOrgid(String pid);

	void updateAtionHangYe(TblOrganization organization);

	void updateMySqlAtionHangYe(TblOrganizationMySql organization);

	TblOrganization findById(String orgid);

	TblOrganizationMySql findByMySqlId(String orgid);

    List<Tree> getTree(BigDecimal nodeId);

	List<Tree> getNodeAll(BigDecimal nodeId);

	String GetTreeOrg(BigDecimal id, Map<BigDecimal, Object> map, String orgid);

	List<Tree> getJTTreeNodeId(BigDecimal nodeId);

    List<TblOrganization> parentHy(String orgids);

	List<TblOrganizationMySql> parentMySqlHy(String orgids);

	List getOrgTreeLefts(BigDecimal orgid);

	List getOrgTreeLeftsOneLevel(BigDecimal orgid);

	TblOrganization findByoId(BigDecimal orgid);

	TblOrganizationMySql findByMySqloId(BigDecimal orgid);

	TblOrganizationUtil selectFatherOrgIdByID(String orgId);

	String findIniStatus(BigDecimal orgid);

    TblOrganization getHYFirst();

	TblOrganizationMySql getMySqlHYFirst();

	List<Tree> getTreeHy(BigDecimal nodeId);

	List<Tree> getNodeAllHy(BigDecimal nodeId);

	void updateorgn(TblOrganization orgn);

	void updateMySqlorgn(TblOrganizationMySql orgn);

	Tree getTreeRoot(BigDecimal nodeId);

	TblOrganization get(BigDecimal bigDecimal);

	TblOrganizationMySql getMySql(BigDecimal bigDecimal);

    Map<String, Object> syncAllOrg(Integer operaType,String data) throws Exception;
   
    void syncOrg(String data)throws Exception;

    JsonBean findOrganInfoDetail(String token,String orgid)throws Exception;
    
    JsonBean updateOrganInfo(String token, TblOrganizationInfo info)throws Exception;

    
	  List<BigDecimal> getTblOrganizationAll(BigDecimal orgid) throws Exception;

	  List<BigDecimal> getOrgIdListAutoNumber() throws Exception;
    
	  List<BigDecimal> getOrgIdLis(BigDecimal orgid) throws Exception;
	  
	  List<BigDecimal> getOrgIdLisBySy(BigDecimal orgid) throws Exception;


//	TblOrganization findSelectedUser(BigDecimal orgid);


//	TblOrganization getWPZJK(String orgid, String iszy);
	  
	  String selectNamesByids(String ids) throws Exception;
	     
	  String selectNameByids(BigDecimal id) throws Exception;

}
