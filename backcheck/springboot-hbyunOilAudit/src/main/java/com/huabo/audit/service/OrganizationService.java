package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblOrganization;


public interface OrganizationService {
	public void add(TblOrganization org);
    public void modify(TblOrganization org);
    public List<TblOrganization> findAllOrg();
    public List<TblOrganization> findAllHYOrg();
    public List<TblOrganization> findByPid(String pid);
    public List<TblOrganization> findByHYPid(String pid);
    public String getOrgTree();
    public String getHyOrgTree();
    public TblOrganization getHY();
    /**
     * 行业下第一个行业
     * @return
     */
    public TblOrganization getHYFirst();
    public void delete(String id);
    /**
     * 查询部门
     * @param orgid
     * @return
     */
    public List<TblOrganization> findAllOrg(String orgid);
	public List<TblOrganization> findByOrgids(String orgids);
	public List<TblOrganization> findAllOrgBM(String orgid);
    public String getOrgTreeqx();
    public TblOrganization findById(String id);
    public List<TblOrganization> findByIdFatharid(String id);
	public String getOrgTree(int type);
	public Set<BigDecimal> getSubOrgs(String pid);
	public String getOrgTree(String pageurl);
	public String getOrgTreeLefts(BigDecimal orgid);
	public String getOrgTree(BigDecimal orgid);
	public List<TblOrganization> isXj(String orgid);
	public void deleteOrg(TblOrganization org);
	/**
	 * 根据当前切换的机构查询下级机构及部门
	 * @param orgid
	 * @return
	 */
	public String getOrgTreeLeft(BigDecimal orgid);
	 public List<TblOrganization> gsisXj(String orgid);

	
	/**
	 * @author tyb
	 * 2016-8-29下午5:06:01
	 * @Des:是公司则默认添加外聘专家库
	 */
	public TblOrganization isCompanyAddWPZJ(TblOrganization org);
	
	public TblOrganization getSJZYK(TblOrganization org,String iszy);
	/**
	 * @Author：TYB
	 * @Date:2016-10-19 上午 9:54
	 * @Des:返回公司部门总个数 （排除行业的）
	 */
	public Integer countOrg();
	
	public Integer countOrgGS(String orgid);
	/**
	 * @Author：TYB
	 * @Date:2016-10-19 上午 10:29
	 * @Des:组织架构上下移动
	 */
	public void orgSorts(List<TblOrganization> orgs,String orgid,String move);
	
	public TblOrganization findByname(String orgname);
	
	public List<TblOrganization> findByPlanAllPlanid(String planid);
	
	 public TblOrganization findByFatharidAndCommpy(String id);
	 
	public List<TblOrganization> findByIdAlls(String id);
	public BigDecimal addReturnId(TblOrganization org);
	
	public TblOrganization belongToCompanyzc(String orgid,String bmid);
	
	public Object findBynameGZ(String orgname);
	
	public TblOrganization findByCompanyzc(String orgid,String bmid);
	public String findIniStatus(BigDecimal orgid) throws Exception;
	
	/**
	 * 查询用友融合机构
	 * @param orgid
	 * @return
	 */
	public TblOrganization getOrgid(String orgid);
	
	
	/**
	 * 1.查询下级公司(公司名称+公司编号)company
	 * 2.查询下级组织(组织名称+组织编号)organ
	 * 3.查询行业(行业名称+行业编号)industry
	 * @param pid
	 * @return
	 */
 	public List<TblOrganization> findOrgByType(String pid,String type);
 	/**
 	 * 获取启用的公司和部门（派可报表组织架构同步专用）
 	 * @return
 	 */
 	public List<TblOrganization> selectAllCompanyAndDept();
 	
 	

 	//==
 	JsonBean findAllListPage(String token, Integer pageNumber, Integer pageSize) throws Exception;
 	
}
