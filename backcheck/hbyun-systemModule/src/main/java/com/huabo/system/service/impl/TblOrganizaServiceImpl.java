package com.huabo.system.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JwtUtils;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.Node;
import com.huabo.system.entity.TblAuthorizationRecord;
import com.huabo.system.entity.TblBiReportMenu;
import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblOrganizationInfo;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSynchronizationRecord;
import com.huabo.system.mapper.TblAuthorizationRecordMapper;
import com.huabo.system.mapper.TblBiReportMenuMapper;
import com.huabo.system.mapper.TblLoginTypeMapper;
import com.huabo.system.mapper.TblManageRightDAO;
import com.huabo.system.mapper.TblOrganizationInfoMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblOrganizationResultMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSynchronizationRecordMapper;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.YMBusinessService;
import com.huabo.system.utils.PageResult;
import com.huabo.system.utils.RedisFinalUtis;
import com.huabo.system.utils.Tree;
import com.huabo.system.utils.WebServiceClient;
import com.huabo.system.utils.XmlManager;
import com.huabo.system.vo.param.TblOrganizationQueryParam;
import com.huabo.system.vo.result.TblOrganizationResult;
import com.huabo.system.mapper.TblDateRightInfoMapper;
import com.huabo.system.entity.TblDateRightInfo;

import cn.hutool.extra.pinyin.PinyinUtil;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Service
public class TblOrganizaServiceImpl implements TblOrganizaService {
	@Resource
    private TblOrganizationMapper tblOrganizationMapper;

	@Resource
	public TblOrganizaService tblOrganizaService;

    @Resource
    private TblOrganizationInfoMapper tblOrganizationInfoMapper;

    @Resource
    private TblLoginTypeMapper tblLoginTypeMapper;

    @Resource
    private TblBiReportMenuMapper tblBiReportMenuMapper;
    
    @Resource
    private TblOrganizationResultMapper tblOrganizationResultMapper;

    @Resource
    private TblStaffMapper tblStaffMapper;

    @Resource
    private TblManageRightDAO tblManageRightDAO;

    @Resource
    private YMBusinessService ymBusinessService;
    
    @Resource
    private TblAuthorizationRecordMapper tblAuthorizationRecordMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Resource
    private TblSynchronizationRecordMapper tblSynchronizationRecordMapper;

	@Autowired
	private TblDateRightInfoMapper tblDateRightInfoMapper;
    
    
    private static final String zhOrgNumberStrs = ",6004,600401010300,600402010300,600403010300,600404010300,600405010300,600406010300,600407010600,600410010400,"
    		+ "10120899000219,600411010600,600504010600,12345678,AQ0000048,10120899000258,90085,90084";
    
    
    @Override
	public void changeOrgStatus(TblOrganization organization) throws Exception {
		//判断启用弃用的是公司还是部门
    	/*if(organization.getOrgtype() == null || organization.getOrgtype() > 0 ) {
    		//获取当前组织下的所有公司主键
    		String orgIdStrs = this.getAllCompanyIdStrsByfahterOrgids(organization.getOrgid().toString(),null,null);
    		if(orgIdStrs != null ){
    			this.dealChangeChilDeptStatus(orgIdStrs.split(","),organization.getStatus());
    			//处理公司；
        		this.tblOrganizationMapper.updateOrgInfoStatusByIds(orgIdStrs,organization.getStatus());
    		}
    	}else {
    		//部门
    		//获取当前组织下的所有部门主键
    		String allOrgIdStrs = this.getAllDepartmentIdStrsByfahterOrgids(organization.getOrgid().toString(),null,null);
    		if(allOrgIdStrs != null){
    			this.tblOrganizationMapper.updateOrgInfoStatusByIds(allOrgIdStrs,organization.getStatus());
    		}
    	}*/
    	 this.tblOrganizationMapper.updateOrgInfoStatusByIds(organization.getOrgid().toString(),organization.getStatus());
	}
    
    private void dealChangeChilDeptStatus(String[] companyIds, Integer status) throws Exception {
    	String allOrgIdStrs = null;
    	for (String id : companyIds) {
    		allOrgIdStrs = this.getAllDepartmentIdStrsByfahterOrgids(id,null,null);
    		if(allOrgIdStrs != null){
    			this.tblOrganizationMapper.updateOrgInfoStatusByIds(allOrgIdStrs,status);
    		}
		}
	}

	@Override
	public List<TblOrganization> findCurrentOrgHyListInfo(BigDecimal orgid) throws Exception {
    	List<TblOrganization> orgList = this.tblOrganizationMapper.selectCurrentOrgHyList(orgid, BigDecimal.valueOf(-1));
    	List<TblOrganization> allList = new ArrayList<TblOrganization>(0);
    	if(orgList != null && orgList.size() > 0 ) {
    		allList.addAll(orgList);
    		this.setchildrenHyList(orgList,orgid,allList);
    	}
		return allList;
	}
    
    private void setchildrenHyList(List<TblOrganization> orgList, BigDecimal orgid, List<TblOrganization> allList) {
    	List<TblOrganization> childrenList = null;
		for (TblOrganization org : orgList) {
			childrenList = this.tblOrganizationMapper.selectCurrentOrgHyList(orgid, org.getOrgid());
			if(childrenList != null && childrenList.size() != 0) {
				allList.addAll(childrenList);
				this.setchildrenHyList(childrenList, orgid, allList);
			}else {
				continue;
			}
		}
	}

	@Override
	public Map<String, Object> selectDeptListByroleid(String roleid, Integer pageNumber, Integer pageSize,
			String orgname) throws Exception {
		PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		
		Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblOrganization> pageList = tblOrganizationMapper.selectDeptListByroleid(roleid, page, orgname);
		
		List<TblOrganization> orgList = pageList.getRecords();
		for (TblOrganization org : orgList) {
			org.setOrgTreeNames(String.join(",",this.tblOrganizationMapper.selectTreeNamesByOrgTreeId(org.getOrganizationTrees())));
		}
		pageInfo.setTlist(orgList);
		pageInfo.setTotalRecord((int) pageList.getTotal());
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("pageInfo", pageInfo);
		return resultMap;
	}
    
    @Override
	public List<TblOrganization> findAllCompany(String orgid, Integer audittype) throws Exception {
    	TblOrganizationQueryParam query = new TblOrganizationQueryParam();
    	query.setOrgname("");
    	query.setOrgtype(-2);
    	List<String> orgIdStrs = this.tblOrganizationResultMapper.selectAllOrgIdByOrgName(query);
		if(orgIdStrs == null || orgIdStrs.size() == 0){
			return new ArrayList<TblOrganization>(0);
		}
		//String allOrgIdStrs = String.join(",", orgIdStrs);
		// 获取所有符合条件的orgIds 包括上级公司的 id
		List<String> allOrgIdStrs = this.selectAllOrgIdsByOrgIdsTrees(orgIdStrs,orgIdStrs);
        return tblOrganizationMapper.findBysqlOrgid(String.join(",", allOrgIdStrs));
	}
    
    @Override
    public List<TblOrganization> parentHy(String orgids) throws Exception {
    	TblOrganizationQueryParam query = new TblOrganizationQueryParam();
    	query.setOrgname("");
    	query.setOrgtype(-3);
    	List<String> orgIdStrs = this.tblOrganizationResultMapper.selectAllOrgIdByOrgName(query);
		if(orgIdStrs == null || orgIdStrs.size() == 0){
			return new ArrayList<TblOrganization>(0);
		}
		//String allOrgIdStrs = String.join(",", orgIdStrs);
		// 获取所有符合条件的orgIds 包括上级公司的 id
		List<String> allOrgIdStrs = this.selectAllOrgIdsByOrgIdsTrees(orgIdStrs,orgIdStrs);
        List<TblOrganization> list = this.tblOrganizationMapper.findByorgids(String.join(",", allOrgIdStrs));
        return list;
    }
    
    @Override
    public void delete(BigDecimal orgid) {
    	tblOrganizationMapper.deleteOrg(orgid);
    }
    
    @Override
    public List<TblOrganization> findHYAuthorize(String hyid, String pid) throws Exception {
    	TblOrganizationQueryParam query = new TblOrganizationQueryParam();
    	query.setOrgname("");
    	query.setOrgtype(-3);
    	List<String> orgIdStrs = this.tblOrganizationResultMapper.selectAllOrgIdByOrgName(query);
		if(orgIdStrs == null || orgIdStrs.size() == 0){
			return new ArrayList<TblOrganization>(0);
		}
		//String allOrgIdStrs = String.join(",", orgIdStrs);
		// 获取所有符合条件的orgIds 包括上级公司的 id
		List<String> allOrgIdStrs = this.selectAllOrgIdsByOrgIdsTrees(orgIdStrs,orgIdStrs);
        return tblOrganizationMapper.findBy(hyid, String.join(",", allOrgIdStrs));
    }
    
    @Override
    public void saveModify(TblOrganization org) {
        this.tblOrganizationMapper.saveOrg(org);
    }

    @Override
    public List<TblOrganization> isParent(String id) {
        List<TblOrganization> list = this.tblOrganizationMapper.findSql(id);
        return list;
    }
    
    @Override
    public TblOrganization findByIdOrgid(String pid) {
        TblOrganization org = tblOrganizationMapper.findIdOrgid(pid);
        return org;
    }
    
    @Override
    public List<TblOrganization> findPid(String pid) {
        TblOrganization tbl = tblOrganizationMapper.findPid(pid);
        List<TblOrganization> tblOrganizationList = new ArrayList<>();
        tblOrganizationList.add(tbl);
        return tblOrganizationList;
    }
    
    @Override
    public Map<String, Object> findAllHYOrgStaffid(Integer pageNumber, Integer pageSize, String staffId, String token, String pid) {
    	 PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
         pageInfo.setCurrentPage(pageNumber);
         pageInfo.setPageSize(pageSize);
         if (pageSize != null) {
             pageInfo.setPageSize(pageSize);
         }
         pageInfo.setCurrentPage(pageNumber);
         pageInfo.setPageSize(pageSize);
         
         Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
         page.setOptimizeCountSql(false); // 禁用自动优化
     	 IPage<TblOrganization> pageList = tblOrganizationMapper.selectListByStaffid(page, pid);
           
         pageInfo.setTlist(pageList.getRecords());
         pageInfo.setTotalRecord((int) pageList.getTotal());
         Map<String, Object> resultMap = new HashMap<String, Object>(0);
         resultMap.put("code", "1");
         resultMap.put("msg", "数据访问成功");
         resultMap.put("pageInfo", pageInfo);
         return resultMap;
    }
    
    @Override
    public Map<String, Object> findAllOrgBM(Integer pageNumber, Integer pageSize, Find find, BigDecimal pid) throws Exception {
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
//    	PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
//    	pageInfo.setCurrentPage(pageNumber);
//		pageInfo.setPageSize(pageSize);
//    	//获取所有公司ID拼接的字符串
//       /* String orgIdStrs = this.getAllDepartmentIdStrsByfahterOrgids(pid.toString(),null,"");
//       	orgIdStrs = orgIdStrs.substring(1, orgIdStrs.length());*/
//       	 	IPage<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
//       	 	IPage<TblOrganization> pageList = tblOrganizationMapper.selectListByPageInfoOrgid(page, find, pid,pid.toString());
//            pageInfo.setTlist(pageList.getRecords());
//            pageInfo.setTotalRecord((int)pageList.getTotal());
            
          //链表分页  xml 写法
    		com.github.pagehelper.PageInfo<TblOrganization> pageInfo = PageMethod.startPage(pageNumber, pageSize)
    				.doSelectPageInfo(() -> tblOrganizationMapper.selectListByPageInfoOrgid(find, pid,pid.toString()));
    		
    		PageResult<TblOrganization> build = new PageResult<TblOrganization>().build(pageInfo);
            
		TblOrganization company = this.tblOrganizationMapper.selectByOrgId(pid);
		resultMap.put("pageInfo", build);
		resultMap.put("company", company);
		return resultMap;
    }
    
    @Override
	public String deleteOrg(TblOrganization organ) {
    	this.tblOrganizationMapper.deleteById(organ.getOrgid());
		return "sucess";
	}
    
	@Override
	public Integer selectChildrenCount(String orgid) throws Exception {
		return this.tblOrganizationMapper.selectChildrenCount(new BigDecimal(orgid));
	}
    
    @Override
    public TblOrganization findByOrgid(String orgid) {
        TblOrganization togz = tblOrganizationMapper.selectOrgid(orgid);
        return togz;
    }
    
    @Override
    public void updateAtionHangYe(TblOrganization organization) {
        tblOrganizationMapper.updateAtionHangYe(organization);
    }
    
    @Override
    public TblOrganization isCompanyAddWPZ(TblOrganization org) {
        TblOrganization o = new TblOrganization();
        o.setOrgname("外聘专家库");
        o.setFatherorgid(org.getOrgid());
        o.setOrgnumber(org.getOrgnumber() + "wpzj");
        o.setMemo("外聘专家库");
        o.setOrgmeno("外聘专家库");
        o.setOrgtype(0);
        o.setIszy("wpzjk");
        return this.tblOrganizationMapper.saveModiOrgan(o);
    }
    
    @Override
    public Integer addReturnOrg(TblOrganization organization) {
        return this.tblOrganizationMapper.insert(organization);
    }
    
    @Override
    public void updateZuZhi(TblOrganization organization) {
        tblOrganizationMapper.updateZuZhi(organization);
    }
    
    @Override
    public TblOrganization findByOrg(BigDecimal orgid) {
        return tblOrganizationMapper.selectByOrgid(orgid);
    }
    
    @Override
    public void saveAtionHangYe(TblOrganization ation) {
    	tblOrganizationMapper.saveAtionHangYe(ation);
    }
    
    @Override
    public Integer addReturnId(TblOrganization organization) {
        return this.tblOrganizationMapper.insert(organization);
    }
    
    @Override
    public void modiOrganization(TblOrganization organization) {
        tblOrganizationMapper.saveModiOrganization(organization);
    }
    
    @Override
    public TblOrganization findById(String orgid) {
    	//根据公司主键获取公司信息
        TblOrganization organization = tblOrganizationMapper.selectByOrgId(new BigDecimal(orgid));
        return organization;
    }
    
	@Override
	public Integer selectRepeatNumber(String orgnumber,String uniqueNumber, BigDecimal orgId) throws Exception {
		return this.tblOrganizationMapper.selectRepeatNumber(orgnumber,uniqueNumber,orgId);
	}
    
    @Override
	public Integer selectRepeatName(String orgname, String uniqueNumber, BigDecimal orgId) throws Exception {
		return this.tblOrganizationMapper.selectRepeatName(orgname,uniqueNumber,orgId);
	}
    
	@Override
    public Map<String, Object> findOrganizationInfoByModuleId(Integer pageNumber, Integer pageSize, TblOrganization tblOrganization, BigDecimal moduleId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setCondition(tblOrganization);
        
        Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblOrganization> pageList = tblOrganizationMapper.selectStaffInfoByModuleIdList(page, moduleId,tblOrganization);
        
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }
	
	 @Override
	    public List<Tree> getJTTreeNodeId(BigDecimal nodeId) {
	            List<Tree> trees = new ArrayList();
	            List<TblOrganization> list = this.tblOrganizationMapper.getNodesaGSNodeId(nodeId);
	            Iterator var4 = list.iterator();

	            while (true) {
	                TblOrganization tblOrganization;
	                do {
	                    if (!var4.hasNext()) {
	                        return trees;
	                    }

	                    tblOrganization = (TblOrganization) var4.next();
	                } while (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0);

	                Tree tree = new Tree();
	                tree.setName(tblOrganization.getOrgname());
	                tree.setId(tblOrganization.getOrgid());
	                tree.setpId(tblOrganization.getFatherorgid());
	                tree.setOpen(true);
	                tree.setIsParent(this.getChildrenByCommpany(tblOrganization.getChildren()));
	                trees.add(tree);
	            }
	    }

	 private boolean getChildrenByCommpany(Set<TblOrganization> chil) {
	        Iterator var2 = chil.iterator();

	        TblOrganization org;
	        do {
	            if (!var2.hasNext()) {
	                return false;
	            }

	            org = (TblOrganization) var2.next();
	        } while (org.getOrgtype() == null || org.getOrgtype() == 0);

	        return true;
	    }
	 
	@Override
	public String findCompanyTreeListByOrgName(String orgName) throws Exception {
		Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>();
		List<Tree> children = new ArrayList<Tree>();
		List<TblOrganization> childrenList = null;
		//查找所有副歌少选条件的公司主键
		List<String> allOrgIdList = this.tblOrganizationMapper.selectAllCompanyListByOrgName(orgName);
		
		if(allOrgIdList == null) {
			return "";
		}
		
		String allOrgIdStrs = String.join(",", allOrgIdList);
		// 获取所有符合条件的orgIds 包括上级公司的 id
		allOrgIdStrs = this.selectAllOrgIdsByOrgIds(allOrgIdStrs,allOrgIdStrs);
		// 根据orgtype 递归获取公司数据 并保持子父级关系
		int i = 1;
		List<TblOrganization> fatherOrgList = this.tblOrganizationMapper.selectAllListByOrgtypeAndOrgIds(1,allOrgIdStrs);
		Tree childrentree = null;
		//循环生成 tree 结构
		for (TblOrganization father : fatherOrgList) {
			tree = new Tree();
			tree.setName(father.getOrgname());
			tree.setId(father.getOrgid());
			tree.setpId(father.getFatherorgid());
			tree.setOpen(true);
			tree.setIsParent(true);
			tree.setChildren(this.setAllOrgInfoTreeChildren(father.getOrgid(),allOrgIdStrs));
			trees.add(tree);
		}
		return com.alibaba.fastjson.JSONObject.toJSONString(trees);
	}
	 
	private List<Tree> setAllOrgInfoTreeChildren(BigDecimal orgid, String allOrgIdStrs) throws Exception {
		List<TblOrganization> childrenList = this.tblOrganizationMapper.selectChilrenListByOrgIdScope(orgid,allOrgIdStrs);
		Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>(0);
		for (TblOrganization org : childrenList) {
			tree = new Tree();
			tree.setName(org.getOrgname());
			tree.setId(org.getOrgid());
			tree.setpId(org.getFatherorgid());
			tree.setChildren(this.setAllOrgInfoTreeChildren(org.getOrgid(),allOrgIdStrs));
			tree.setOpen(tree.getChildren().size()>0?true:false);
			tree.setIsParent(tree.getChildren().size()>0?true:false);
			trees.add(tree);
		}
		return trees;
	}
	
	private String selectAllOrgIdsByOrgIds(String orgStrIds,String allOrgIdStrs) throws Exception {
		List<String> orgIdsList = this.tblOrganizationMapper.selectAllListByChildrenOrgName(orgStrIds,allOrgIdStrs);
		if(orgIdsList == null) {
			return allOrgIdStrs;
		}
		String orgIds = String.join(",", orgIdsList);
		
		return this.selectAllOrgIdsByOrgIds(orgIds,allOrgIdStrs+","+orgIds);
	}
	
	
    @Override
    public void modify(TblOrganization org) {
        this.tblOrganizationMapper.saveModiOrganization(org);
    }

    @Override
    public Serializable countOrg() {
    	TblOrganization tblOrganization = tblLoginTypeMapper.selectcOunt();
        return tblOrganization;
    }


    @Override
    public List<TblOrganization> gsisXj(String id) {
        List<TblOrganization> to = tblOrganizationMapper.selectId(id);
        return to;
    }

    @Override
    public void add(TblOrganization org) {
        this.tblOrganizationMapper.addReturnId(org);

    }

    @Override
    public List<TblOrganization> getHyOrgTree() {
        String tree = "";
        List<TblOrganization> tbl = tblOrganizationMapper.selectHyzong(BigDecimal.valueOf(-1));
        if (tbl == null || tbl.size() == 0) {
            TblOrganization org = new TblOrganization();
            org.setOrgname("行业");
            org.setFatherorgid(new BigDecimal("-1"));
            org.setOrgnumber("00");
            org.setOrgmeno("行业");
            org.setMemo("行业");
            org.setOrgtype(100);
            this.tblOrganizationMapper.addReturnId(org);
            tbl.add(org);
        }else {
        	this.setHyOrgListInfo(tbl);
        }

        return tbl;
    }

    private void setHyOrgListInfo(List<TblOrganization> tbl) {
    	List<TblOrganization> hyList = null;
		for (TblOrganization org : tbl) {
			hyList = this.tblOrganizationMapper.selectHyzong(org.getOrgid());
			if (hyList == null || hyList.size() == 0) {
				break;
			}else {
				org.setChildrenList(hyList);
				this.setHyOrgListInfo(hyList);
			}
		}
	}

	@Override
    public TblOrganization getHY() {
        List<TblOrganization> listSql = tblOrganizationMapper.selectGetHY();
        return listSql != null && listSql.size() > 0 ? (TblOrganization) listSql.get(0) : null;
    }

    @Override
    public TblOrganization findByname(String orgname) {
        List<TblOrganization> list = this.tblOrganizationMapper.findBysql(orgname);
        return list != null && list.size() > 0 ? (TblOrganization) list.get(0) : null;
    }

    @Override
    public Map<String, Object> findAllHYOrg(Integer pageNumber, Integer pageSize) {
    	Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
    	page.setOptimizeCountSql(false); // 禁用自动优化
    	IPage<TblOrganization> pageList = tblOrganizationMapper.selectListByPageInfo(page);
           
    	PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("pageInfo", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllHYOrgs(Integer pageNumber, Integer pageSize, String pid) {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblOrganization> pageList = tblOrganizationMapper.selectListByPid(page, pid);
            
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", pageInfo);
            return resultMap;
    }

    @Override
    public Map<String, Object> getSJZYK(TblOrganization organization, String wpzjk, Integer currentPage, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
        if (pageSize != null) {
            pageInfo.setPageSize(pageSize);
        }
        pageInfo.setCurrentPage(currentPage);
        try {
            pageInfo.setTlist(tblOrganizationMapper.selectListByPageInfoo(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }


    //组织架构标记
    @Override
    public List<Tree> getNodeAllbm(BigDecimal nodeId) {
    	
    	
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGTREEDEPTLIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGTREEDEPTLIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganization> list = tblOrganizationMapper.findByNodeId(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        // Set<TblOrganization> chil = tblOrganization.getChildren();
                        Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                        children = getNoteTreesbm(chil);
                        Tree tree = new Tree();
                        tree.setChildren(children);
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(chil.size() > 0 ? true : false);
                        trees.add(tree);
                    }
                    return trees;
                }
            } finally {
            	jedis.close();
            }
    }


    private List<Tree> getNoteTreesbm(Set<TblOrganization> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (TblOrganization tblOrganization2 : chil) {
            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0) {
                continue;
            }
            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
                Tree tree = new Tree();
                Set<TblOrganization> chil2 = tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid());
                if (chil2.size() > 0) {
                    List<Tree> children1 = new ArrayList<Tree>();
                    children1 = getNoteTrees(chil2);
                    tree.setChildren(children1);
                }
                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(chil2.size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }

    @Override
    public String findOrgByAllJT(String orgid) {
    	return String.valueOf(tblOrganizationMapper.selectById(orgid));
    }

    @Override
    public String findOrgByAll(String orgid) {
    	
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGROOTSTR + orgid)) {
                    String str = jedis.get(RedisFinalUtis.ORGROOTSTR + orgid);
                    return str;
                } else {
                    List<TblOrganization> organizations = tblOrganizationMapper.findByOrg(orgid);
                    List<TblOrganization> list = new ArrayList<TblOrganization>();
                    walkDepartmentList(organizations, list);
                    Map nodeList = new LinkedHashMap();
                    Node root = null;
                    Integer str = null;
                    String pc = ",";
                    for (int i = 0; i < list.size(); i++) {
                        TblOrganization tbl = list.get(i);
                        //Map dataRecord = (Map) it.next();
                        if (tbl.getStatus() != null && tbl.getStatus() == 1) {
                            continue;
                        }
                        if (i > 0 && tbl.getOrgtype() != null && tbl.getOrgtype() != 0 && !tbl.getOrgid().toString().equals(orgid)) {
                            pc += tbl.getOrgid().toString() + ",";
                            continue;
                        }
                        if (pc.contains("," + tbl.getFatherorgid().toString() + ",")) {
                            pc += tbl.getOrgid().toString() + ",";
                            continue;
                        }
                        Node node = new Node();
                        node.id = tbl.getOrgid().toString();
                        node.text = tbl.getOrgname().toString();
                        node.parentId = null;
                        if (null != tbl.getFatherorgid()) {
                            node.parentId = tbl.getFatherorgid().toString();
                        }
                        for (int k = 0; k < list.size(); k++) {
                            TblOrganization copyMap = list.get(k);
                            if (copyMap.getStatus() == null || copyMap.getStatus() == 1) {
                                continue;
                            }
                            if (copyMap.getFatherorgid() != null) {
                                if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() == 0) {
                                    node.state = "open";//closed
                                    break;
                                }
                                if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() > 0 && copyMap.getOrgtype() < 100) {
                                    node.state = "open";
                                    break;
                                }
                            }
                        }
                        if (tbl.getFatherorgid().toString().equals("-1")) {
                            node.state = "open";
                        }
                        nodeList.put(node.id, node);
                    }
                    Set entrySet = nodeList.entrySet();
                    for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                        Node node = (Node) ((Map.Entry) it.next()).getValue();
                        if (node.parentId == null || node.parentId.equals("") || node.parentId.equals("0") || nodeList.get(node.parentId) == null) {
                            root = node;
                        } else {
                            ((Node) nodeList.get(node.parentId)).addChild(node);
                        }
                    }

                    return root.toString();
                }
            } finally {
            	jedis.close();
            }
    }

    private static void walkDepartmentList(Collection<TblOrganization> toList, List<TblOrganization> list) {
        for (TblOrganization top : toList) {
            if (top.getStatus() != null && top.getStatus() == 0) {
                //顶级部门
                TblOrganization copy = new TblOrganization();//使用副本，因为原对象在Session中
                copy.setOrgid(top.getOrgid());
                copy.setOrgname(top.getOrgname());
                copy.setFatherorgid(top.getFatherorgid());
                copy.setOrgtype(top.getOrgtype());
                copy.setStatus(top.getStatus());
                list.add(copy);
                //子树
                walkDepartmentList(top.getChildren(), list);
            }
        }

    }

    @Override
    public Map<String, Object> findAllCommpanyPageBeanGS(Integer pageNumber, Integer pageSize, String staffId, String token, Find find, BigDecimal orgId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Jedis jedis = null;
        try {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            TblOrganizationQueryParam query = new TblOrganizationQueryParam();
            query.setFatherorgid(orgId);
    		
            //获取所有公司ID拼接的字符串
            //String orgIdStrs = this.getAllCompanyIdStrsByfahterOrgids(query.getFatherorgid().toString(),null,"");
            	 //orgIdStrs = orgIdStrs.substring(1, orgIdStrs.length());
                 Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
                 page.setOptimizeCountSql(false); // 禁用自动优化
                 IPage<TblOrganization> pageList = tblOrganizationMapper.findByPageBean(page, orgId.toString(), find);
                 pageInfo.setTlist(pageList.getRecords());
                 pageInfo.setTotalRecord((int) pageList.getTotal());
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
            	jedis.close();
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllCommpanyPageBeanGSXj(Integer startIndex, Integer pageSize, String staffId, Find find, String token, String orgId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Jedis jedis = null;

        try {
            if (token == null && staffId == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            if (token == null && staffId != null) {
                jedis = JedisUtil.getJedis();
                token = jedis.get(staffId);
            }
            Claims claims = JwtUtils.parseJwt(token);
            Object object = claims.get("staffInfo");
            JSONObject objJson = JSONObject.fromObject(object);
            TblStaff staff = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);

            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            if (pageSize != null) {
                pageInfo.setPageSize(pageSize);
            }
            TblOrganization organization = new TblOrganization();
            organization.setOrderid(staff.getCurrentOrg().getOrgid().intValue());
            pageInfo.setTlist(tblOrganizationMapper.findAllCommpanyPageBeanGSXj(pageInfo, find, orgId));
            pageInfo.setTotalRecord(tblOrganizationMapper.findAllCommpanyPageBeanGS(pageInfo, find, orgId));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } finally {
            if (jedis != null) {
            	jedis.close();
            }
        }
        return resultMap;
    }

    @Override
    public List<Tree> getJTTreeGS(BigDecimal nodeId) {
            List<Tree> trees = new ArrayList();
            List<TblOrganization> list = this.tblOrganizationMapper.getNodesaGS(nodeId);
            Iterator var4 = list.iterator();

            while (true) {
                TblOrganization tblOrganization;
                do {
                    if (!var4.hasNext()) {
                        return trees;
                    }

                    tblOrganization = (TblOrganization) var4.next();
                } while (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0);

                Tree tree = new Tree();
                tree.setName(tblOrganization.getOrgname());
                tree.setId(tblOrganization.getOrgid());
                tree.setpId(tblOrganization.getFatherorgid());
                tree.setOpen(true);
                // tree.setIsParent(this.getChildrenByCommpany(tblOrganization.getChildren()));
                trees.add(tree);
            }
    }

//公司管理标记
    public List<Tree> getJTNodeAllGS(BigDecimal nodeId) {
		List<Tree> trees = new ArrayList<Tree>();
		List<Tree> children = new ArrayList<Tree>();
		List<TblOrganization> list = tblOrganizationMapper.findByNodeId(nodeId);
		for (TblOrganization tblOrganization : list) {
		    // Set<TblOrganization> chil = tblOrganization.getChildren();
		    Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
		    for (TblOrganization tblOrganization2 : chil) {
		        if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() == 0) continue;
		        Tree tree = new Tree();
		        tree.setName(tblOrganization2.getOrgname());
		        tree.setId(tblOrganization2.getOrgid());
		        tree.setpId(tblOrganization2.getFatherorgid());
		        tree.setOpen(true);
		        tree.setIsParent(getChildrenByCommpany(tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid())));
		        children.add(tree);
		    }
		    Tree tree = new Tree();
		    tree.setChildren(children);
		    tree.setName(tblOrganization.getOrgname());
		    tree.setId(tblOrganization.getOrgid());
		    tree.setpId(tblOrganization.getFatherorgid());
		    tree.setOpen(true);
		    tree.setIsParent(getChildrenByCommpany(chil));
		    trees.add(tree);
		}
		return trees;
    }


    @Override
    public Map<String, Object> findAllCommpanyPageBean(Integer pageNumber, Integer pageSize, BigDecimal pid, String orgname, String orgnumber) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                TblOrganizationQueryParam query = new TblOrganizationQueryParam();
                query.setFatherorgid(pid);
        		
                //获取所有公司ID拼接的字符串
                //String orgIdStrs = this.getAllCompanyIdStrsByfahterOrgids(query.getFatherorgid().toString(),null,"");
                //orgIdStrs = orgIdStrs.substring(1, orgIdStrs.length());
                     Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
                     page.setOptimizeCountSql(false); // 禁用自动优化
                     IPage<TblOrganization> pageList = tblOrganizationMapper.findAllCommpanyPageBeanStaffid(page, pid.toString(), orgname, orgnumber);
                     pageInfo.setTlist(pageList.getRecords());
                     pageInfo.setTotalRecord((int)pageList.getTotal());
               
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public TblOrganization findByid(String id) {
        TblOrganization org = tblOrganizationMapper.findByid(id);
        return org;
    }

    @Override
    public TblOrganization findId(String id) {
        TblOrganization tbl = tblOrganizationMapper.findByid(id);
        return tbl;
    }

    @Override
    public List<TblOrganization> findOrgByType(String pid) {
        TblOrganization tbl = tblOrganizationMapper.findByid(pid);
        return null;
    }

    @Override
    public TblOrganization findByStringId(String id) {
        TblOrganization tbl = tblOrganizationMapper.findByid(id);
        return null;
    }

    @Override
    public Map<String, Object> finreportMenuList(Integer pageNumber, Integer pageSize, String type, String token, String orgid) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                PageInfo<TblBiReportMenu> pageInfo = new PageInfo<TblBiReportMenu>();
                TblStaffUtil user = userProvider.get();
                if (StringUtil.isNullOrEmpty(orgid)) {
                    orgid = user.getCurrentOrg().getOrgid().toString();
                }
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                Page<TblBiReportMenu> page = new Page<TblBiReportMenu>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblBiReportMenu> pageList = tblBiReportMenuMapper.selectType(page, type, new BigDecimal(orgid));
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("pageInfo", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public Map<String, Object> getJTTree(Integer pageNumber, Integer pageSize, String token, String staffId) {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblOrganization> pageList = tblOrganizationMapper.getNodesa(page);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
            return resultMap;
    }

    @Override
    public Map<String, Object> getJTNodeAll(Integer pageNumber, Integer pageSize, BigDecimal nodeId) {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblOrganization> pageList = tblOrganizationMapper.getNodesaNodeId(page, nodeId);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
            return resultMap;
    }


    @Override
    public void saveAtion(TblOrganization organization) {
        tblOrganizationMapper.saveModiOrganization(organization);
    }

    @Override
    public TblOrganization findOrgid(String pid) {
        return tblOrganizationMapper.selectByPid(pid);
    }

    @Override
    public List<Tree> getTree(BigDecimal nodeId) {
            Jedis jedis = JedisUtil.getJedis();
            List var5;
            try {
                List trees;
                if (!jedis.exists("orgdept_list_" + nodeId)) {
                    List<Tree> tres = new ArrayList();
                    tres = this.tblOrganizationMapper.getNodes(nodeId);
                    Iterator var12 = tres.iterator();
                    while (var12.hasNext()) {
                        TblOrganization tblOrganization = (TblOrganization) var12.next();
                        Tree tree = new Tree();
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0);
                        if ((tblOrganization.getOrgtype() == null || tblOrganization.getOrgtype() == 0) && (tblOrganization.getStatus() == null || tblOrganization.getStatus() != 1) && tblOrganization.getStatus() != null && tblOrganization.getStatus() == 0) {
                            tres.add(tree);
                        }
                    }

                    ArrayList var13 = (ArrayList) tres;
                    return var13;
                }
                String str = jedis.get("orgdept_list_" + nodeId);
                trees = JSONArray.parseArray(str, Tree.class);
                var5 = trees;
            } finally {
            	jedis.close();
            }
            return var5;
    }

    @Override
    public List<Tree> getNodeAll(BigDecimal nodeId) {
            Jedis jedis = JedisUtil.getJedis();
            try {
                List children;
                if (jedis.exists("orgdept_list_" + nodeId)) {
                    String str = jedis.get("orgdept_list_" + nodeId);
                    List<Tree> trees = JSONArray.parseArray(str, Tree.class);
                    children = trees;
                    return children;
                } else {
                    List<Tree> trees = new ArrayList();
                    new ArrayList();
                    List<TblOrganization> list = this.tblOrganizationMapper.findByNode(nodeId);
                    Iterator var7 = list.iterator();

                    while (var7.hasNext()) {
                        TblOrganization tblOrganization = (TblOrganization) var7.next();
                        if (tblOrganization.getStatus() == null || tblOrganization.getStatus() != 1) {
                            Set<TblOrganization> chil = tblOrganization.getChildren();
                            children = this.getNoteTrees(chil);
                            Tree tree = new Tree();
                            tree.setChildren(children);
                            tree.setName(tblOrganization.getOrgname());
                            tree.setId(tblOrganization.getOrgid());
                            tree.setpId(tblOrganization.getFatherorgid());
                            tree.setOpen(true);
                            tree.setIsParent(tblOrganization.getChildren().size() > 0);
                            trees.add(tree);
                        }
                    }

                    ArrayList var16 = (ArrayList) trees;
                    return var16;
                }
            } finally {
            	jedis.close();
            }
    }

    @Override
    public String GetTreeOrg(BigDecimal id, Map<BigDecimal, Object> map, String orgid) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            List<Tree> list = new ArrayList();
            List<TblManageRight> root = this.tblManageRightDAO.getTreeRoots(id.toString());
            List<TblManageRight> list1 = this.tblManageRightDAO.getOrgRight(new BigDecimal(orgid));

            for (TblManageRight tblManageRight : root) {
                for (TblManageRight right : list1) {
                    if (tblManageRight.getRightid().toString().equals(right.getRightid().toString())) {
                        Tree tree = new Tree();
                        tree.setName(tblManageRight.getRightname());
                        tree.setId(tblManageRight.getRightid());
                        tree.setTarget("mainFramex");
                        tree.setChecked(map.get(tblManageRight.getRightid()) == null ? false : true);
                        tree.setpId(tblManageRight.getFatherrightid());
                        tree.setUrl("");
                        tree.setOpen(true);
                        List<Tree> childre = this.addChildren(id, tblManageRight.getRightid(), map, list1);
                        if (childre.size() > 0) {
                            tree.setChildren(childre);
                        }
                        list.add(tree);
                    }
                }
            }
            return com.alibaba.fastjson.JSONObject.toJSONString(list);

    }


    @Override
    public TblOrganization findByoId(BigDecimal orgid) {
        return tblOrganizationMapper.findByoId(orgid);
    }


    @Override
    public TblOrganizationUtil selectFatherOrgIdByID(String orgId) {
            List<TblOrganizationUtil> organization = tblOrganizationMapper.findByOrgid(orgId);
            if (organization != null && organization.size() > 0) {
                return organization.get(0);
            }
            return null;
    }

    @Override
    public String findIniStatus(BigDecimal orgid) {
    	return tblOrganizationMapper.findIniStatus(orgid);
    }

    @Override
    public TblOrganization getHYFirst() {
        List<TblOrganization> list = tblOrganizationMapper.findAllorganization();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Tree> getTreeHy(BigDecimal nodeId) {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGTYPELIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGTYPELIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<TblOrganization> list = this.tblOrganizationMapper.getNodeId(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                            continue;
                        }
                        Tree tree = new Tree();
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                        if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() != 100) {
                            continue;
                        }
                        if (tblOrganization.getStatus() == null || tblOrganization.getStatus() == 0) {
                            trees.add(tree);
                        }
                    }
                    return trees;
                }
            } finally {
            	jedis.close();
            }
    }

    @Override
    public List<Tree> getNodeAllHy(BigDecimal nodeId) {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGNODEHYTREE + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGNODEHYTREE + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganization> list = tblOrganizationMapper.findByNodeIdNew(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                        // Set<TblOrganization> chil = tblOrganization.getChildren();
                        for (TblOrganization tblOrganization2 : chil) {
                            Tree tree = new Tree();
                            tree.setName(tblOrganization2.getOrgname());
                            tree.setId(tblOrganization2.getOrgid());
                            tree.setpId(tblOrganization2.getFatherorgid());
                            //tree.setOpen(true);
                            tree.setIsParent(tblOrganization2.getChildren().size() > 0 ? true : false);
                            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 100) {
                                continue;
                            }
                            children.add(tree);
                        }
                        Tree tree = new Tree();
                        tree.setChildren(children);
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                        trees.add(tree);
                    }
                    return trees;
                }
            } finally {
            	jedis.close();
            }
    }

    @Override
    public void updateorgn(TblOrganization orgn) {
        tblOrganizationMapper.updateById(orgn);
    }

    public List<Tree> addChildren(Serializable tmplId, BigDecimal pId, Map<BigDecimal, Object> map, List<TblManageRight> lists) {
        List<Tree> list = new ArrayList();
        List<TblManageRight> root = this.tblManageRightDAO.getTreeByNodeId(pId);
        for (TblManageRight tblManageRight : root) {
            for (TblManageRight right : lists) {
                if (tblManageRight.getRightid().toString().equals(right.getRightid().toString())) {
                    Tree tree = new Tree();
                    tree.setName(tblManageRight.getRightname());
                    tree.setId(tblManageRight.getRightid());
                    tree.setTarget("mainFramex");
                    tree.setChecked(map.get(tblManageRight.getRightid()) == null ? false : true);
                    tree.setpId(tblManageRight.getFatherrightid());
                    tree.setUrl("");
                    tree.setIsParent(false);
                    tree.setUrl("");
                    List<Tree> trees = addChildren(tmplId, tblManageRight.getRightid(), map, lists);
                    if (trees.size() > 0) {
                        tree.setChildren(trees);
                    }
                    list.add(tree);
                }
            }
        }
        return list;
    }

    private List<Tree> getNoteTrees(Set<TblOrganization> chil) {
        List<Tree> children = new ArrayList();
        Iterator var3 = chil.iterator();

        while (true) {
            TblOrganization tblOrganization2;
            do {
                do {
                    if (!var3.hasNext()) {
                        return children;
                    }

                    tblOrganization2 = (TblOrganization) var3.next();
                } while (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 1);
            } while (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0);

            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
                Tree tree = new Tree();
                Set<TblOrganization> chil2 = tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid());
                if (chil2.size() > 0) {
                    new ArrayList();
                    List<Tree> children1 = this.getNoteTrees(chil2);
                    tree.setChildren(children1);
                }

                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(chil2.size() > 0);
                children.add(tree);
            }
        }

    }

    private boolean getChildrenByCommpany1(Set<TblOrganization> chil) {
        Iterator var2 = chil.iterator();

        TblOrganization org;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            org = (TblOrganization) var2.next();
        } while (org.getOrgtype() == null || org.getOrgtype() != 0);

        return true;
    }

    @Override
    public Tree getTreeRoot(BigDecimal nodeId) {
            return null;
    }

    @Override
    public TblOrganization get(BigDecimal bigDecimal) {
            return null;
    }

    /*
	 * 1.第一遍全部插入
	 * 2.第二遍直接修改
	 * hierarchyId字段解析规则：机构 1 部门 2岗位 4人员 8 
	 * */  
	@Override
	public Map<String, Object> syncAllOrg(Integer operaType, String data) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			net.sf.json.JSONArray arr = net.sf.json.JSONArray.fromObject(data);
			for (Object o : arr) {
				JSONObject dept = (JSONObject) o;
				List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(dept.getString("orgid"));
				if (list.size() == 0) {
					TblOrganization org = new TblOrganization();
					org.setOrgname(dept.getString("orgname"));
					org.setStatus(Integer.valueOf(dept.getString("status")));
					org.setDatasource("zz");
					org.setOrgtype(Integer.valueOf(dept.getString("orgtype")));
					org.setHistorycode(dept.getString("orgid")); // 存储历史ID
					org.setHistorydepartmentid(dept.getString("fatherorgid"));
					org.setOrgnumber(dept.getString("orgnumber"));
					tblOrganizationMapper.saveAtionHangYe(org);
				} else if (list.size() == 1) {
					TblOrganization org = list.get(0);
					org.setOrgname(dept.getString("orgname"));
					org.setStatus(Integer.valueOf(dept.getString("status")));
					org.setDatasource("zz");
					org.setOrgtype(Integer.valueOf(dept.getString("orgtype")));
					org.setHistorycode(dept.getString("orgid")); // 存储历史ID
					org.setHistorydepartmentid(dept.getString("fatherorgid"));
					org.setOrgnumber(dept.getString("orgnumber"));
					tblOrganizationMapper.updateAtionHangYe(org);
				}
			}
			//第二遍循环调整
			for (Object o : arr) {
				JSONObject dept = (JSONObject) o;
				List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(dept.getString("orgid"));
				if (list.size() == 1) {
						TblOrganization org = list.get(0);
						if(dept.has("orgtype")&&dept.getString("orgtype").equals("1")){ //公司
							List<TblOrganization> list2 = tblOrganizationMapper.getOrgByHistoryid(dept.getString("fatherorgid"));
							if (list2 != null) {
								TblOrganization organ = tblOrganizationMapper.findPid(list2.get(0).getOrgid().toString());
								org.setFatherorgid(organ.getOrgid());
								org.setOrgtype(organ.getOrgtype()+1);
								tblOrganizationMapper.updateAtionHangYe(org);
							}
						}else{ //部门
							List<TblOrganization> list2 = tblOrganizationMapper.getOrgByHistoryid(dept.getString("fatherorgid"));
							if (list2 != null) {
								TblOrganization organ = tblOrganizationMapper.findPid(list2.get(0).getOrgid().toString());
								org.setFatherorgid(organ.getOrgid());
								tblOrganizationMapper.updateAtionHangYe(org);
							}
						}
				}
			}
			  resultMap.put("code", "0");
              resultMap.put("msg", "部门同步成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

    //调用人资接口	
	@Override
	public void syncOrg(String data) throws Exception {
		Map<String, Object> resultMap=new HashMap<String, Object>();
		try {
			JSONObject json=JSONObject.fromObject(data);
			JSONObject msg=JSONObject.fromObject(json.get("msg"));
			net.sf.json.JSONArray arr = net.sf.json.JSONArray.fromObject(msg.get("depts"));
			for (Object o : arr) {
				JSONObject dept = (JSONObject) o;
				List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(dept.getString("id"));
				if (list.size() == 0) {
					TblOrganization org = new TblOrganization();
					if(!StringUtils.isEmpty(dept.getString("id"))){
					org.setOrgname(dept.getString("name"));
					org.setStatus(dept.getString("isDelete")=="true"?1:0);
					org.setDatasource("zz");
					if(dept.getString("name").indexOf("有限公司")!=-1){
						org.setOrgtype(1);
					}else{
						org.setOrgtype(0);
					}
					org.setHistorycode(dept.getString("id")); // 存储历史ID
					org.setHistorydepartmentid(dept.getString("parentId"));
					org.setOrgnumber(dept.getString("orderNum"));
					tblOrganizationMapper.saveAtionHangYe(org);
					}
				} else if (list.size() == 1) {
					TblOrganization org = list.get(0);
					org.setOrgname(dept.getString("name"));
					org.setStatus(dept.getString("isDelete")=="true"?1:0);
					org.setDatasource("zz");
					org.setHistorycode(dept.getString("id")); // 存储历史ID
					org.setHistorydepartmentid(dept.getString("parentId"));
					org.setOrgnumber(dept.getString("orderNum"));
					tblOrganizationMapper.updateAtionHangYe(org);
				}
			}
			//第二遍循环调整
			for (Object o : arr) {
				JSONObject dept = (JSONObject) o;
				List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(dept.getString("id"));
				if (list.size() == 1) {
						TblOrganization org = list.get(0);
						if(dept.has("orgtype")&&!dept.getString("orgtype").equals("0")){ //公司
							List<TblOrganization> list2 = tblOrganizationMapper.getOrgByHistoryid(dept.getString("parentId"));
							if (list2 != null) {
								TblOrganization organ = tblOrganizationMapper.findPid(list2.get(0).getOrgid().toString());
								org.setFatherorgid(organ.getOrgid());
								org.setOrgtype(organ.getOrgtype()+1);
								tblOrganizationMapper.updateAtionHangYe(org);
							}
						}else{ //部门
							List<TblOrganization> list2 = tblOrganizationMapper.getOrgByHistoryid(dept.getString("parentId"));
							if (list2.size()>0) {
								TblOrganization organ = tblOrganizationMapper.findPid(list2.get(0).getOrgid().toString());
								org.setFatherorgid(organ.getOrgid());
								tblOrganizationMapper.updateAtionHangYe(org);
							}
						}
						
				}
			}
			  resultMap.put("code", "0");
              resultMap.put("msg", "部门同步成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	public JsonBean enableCompanyInfo(String orgid, String status, TblStaffUtil staff) throws Exception {
		List<TblOrganization> childrenList = new ArrayList<TblOrganization>(0);
		
		TblOrganization organization = this.tblOrganizationMapper.selectOrgid(orgid);
		String targetIdString = orgid;
		String memo = "";
		int operaType = 0;
		String targetType ;
		
		
		 //查询所有公司 过滤出所有子集公司集合
		QueryWrapper<TblOrganization> wrapper = new QueryWrapper<TblOrganization>();
		if(organization.getOrgtype() == null || organization.getOrgtype() > 0) {
			wrapper.between("ORGTYPE", 1, 100);
			memo="公司";
			targetType = TblAuthorizationRecord.TARGETTYPECOMPANY;
		}else {
			wrapper.eq("ORGTYPE", 0);
			memo="部门";
			targetType = TblAuthorizationRecord.TARGETTYPEDEPT;
		}
		wrapper.select("ORGID,ORGNAME,FATHERORGID,ORGANIZATIONTREES");
		wrapper.orderByAsc("ORGTYPE,ORDERID");
		List<TblOrganization> orgList = this.tblOrganizationMapper.selectList(wrapper);
		if(orgList != null && orgList.size() > 0) {
			childrenList = orgList.stream()
					.filter(item -> !targetIdString.equals(item.getOrgid().toString()))
					.filter(item -> org.apache.commons.lang.StringUtils.isNotBlank(item.getOrganizationTrees()) && (item.getOrganizationTrees().equals(targetIdString) || 
							item.getOrganizationTrees().startsWith(targetIdString + ",") ||
							item.getOrganizationTrees().contains("," + targetIdString + ",") ||
							item.getOrganizationTrees().endsWith("," + targetIdString)))
					.collect(Collectors.toList());
		}
		if (status != null && status.equals("1")) {
            organization.setStatus(1);
            memo = "弃用"+memo;
            operaType = TblAuthorizationRecord.OPERATIONDEPRECATED;
		}else {
			organization.setStatus(0);
			memo = "启用"+memo;
			operaType = TblAuthorizationRecord.OPERATIONENABLE;
        }
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			//需要插入确认表
			memo = memo + organization.getOrgname();
			TblAuthorizationRecord confirm = new TblAuthorizationRecord();
	       	confirm.setRecordId(RandomUtil.uuStringId());
	       	confirm.setRecordText(memo);
	       	confirm.setCreationTime(new Date());
	       	confirm.setCreator(staff.getStaffid());
	       	confirm.setCreatorName(staff.getRealname());
	       	confirm.setOperationData(com.alibaba.fastjson.JSONObject.toJSONString(organization));
	       	
	       	if(childrenList != null && childrenList.size() > 0) {
	       		List<String> orgNameList = childrenList.stream().map(TblOrganization::getOrgname).collect(Collectors.toList());
	       		memo = memo+"，其所有子集数据一并修改，包含以下数据："+String.join(",", orgNameList);
	       	}
	       	
	       	confirm.setOperationMemo(memo);
	       	confirm.setOperationType(operaType);
	       	confirm.setStatus(0);
	       	confirm.setTargetId(organization.getOrgid().toString());
	       	confirm.setTargetType(targetType);
	       	this.tblAuthorizationRecordMapper.insert(confirm);
	       	return ResponseFormat.retParam(1, 200, confirm);
		}else {
            this.tblOrganizationMapper.updateOrgInfoStatusByIds(organization.getOrgid().toString(),organization.getStatus());
            for (TblOrganization chilOrg : childrenList) {
            	this.tblOrganizationMapper.updateOrgInfoStatusByIds(chilOrg.getOrgid().toString(),organization.getStatus());
			}
            return ResponseFormat.retParam(1, 200, null);
		}
		
	}
	

	@Override
	public JsonBean findOrganInfoDetail(String token, String orgid) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> resultMap = new HashMap<String, Object>(0); 
		 TblStaffUtil user = userProvider.get();
          if (user==null) {
        	  resultMap.put("code", "0");
              resultMap.put("msg", "用户已失效");
        	  return ResponseFormat.retParam(0,20006,resultMap);
          }
          try {
		    TblOrganizationInfo info = tblOrganizationMapper.findOrganInfoDetail(orgid);
        	  resultMap.put("code", "1");
              resultMap.put("msg", "数据访问成功");
              resultMap.put("info",  info);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
          return ResponseFormat.retParam(1,200,resultMap);
        
	}

	@Override
	public JsonBean updateOrganInfo(String token, TblOrganizationInfo info) throws Exception {
		// TODO Auto-generated method stub
				 Map<String, Object> resultMap = new HashMap<String, Object>(0); 
				TblStaffUtil user = userProvider.get();
		          if (user==null) {
		        	  resultMap.put("code", "0");
		              resultMap.put("msg", "用户已失效");
		        	  return ResponseFormat.retParam(0,20006,resultMap);
		          }
		          try {
		        	  if(info.getOrgid()!=null){
		        		  //查询中间表是否已经有此单位
		        		 TblOrganizationInfo in= tblOrganizationMapper.findOrganInfoDetail(info.getOrgid().toString());
		        		  if(in ==null){
		        			  info.setInfoid(RandomUtil.uuBigDecimalId());
                              tblOrganizationInfoMapper.insert(info);
		        	      }else{
                              tblOrganizationInfoMapper.updateById(info);
		        	     }
		        	  }
		        	  resultMap.put("code", "1");
		              resultMap.put("msg", "数据访问成功");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		          return ResponseFormat.retParam(1,200,resultMap);
	}

	
	@Override
	public BigDecimal findRootCompanyId(TblOrganizationUtil org) throws Exception {
		BigDecimal orgId = org.getOrgid();
			//如果父级公司的ID不是-1 则递归查询获取父ID为-1的公司数据
			if(org.getFatherorgid().intValue() != -1) {
				orgId = this.findRootCompanyIdByChildren(org.getFatherorgid());
			}
		return orgId;
	}

	private BigDecimal findRootCompanyIdByChildren(BigDecimal fatherorgid) throws Exception {
		TblOrganization org = this.tblOrganizationMapper.selectByOrgid(fatherorgid);
		BigDecimal orgId = org.getOrgid();
		if(org.getFatherorgid().intValue() != -1) {
			orgId = this.findRootCompanyIdByChildren(org.getFatherorgid());
		}
		return orgId;
		
	}
	
	
	@Override
	public TblOrganization findRootCompanyInfoByOrgId(BigDecimal orgid) throws Exception {
		TblOrganization org = this.tblOrganizationMapper.selectByOrgid(orgid);
		if(org.getFatherorgid().compareTo(BigDecimal.valueOf(-1)) !=0 ) {
			org = this.findRootCompanyInfoByOrgId(org.getFatherorgid());
		}
		return org;
	}
	
	@Override
	public String getDeptLinkCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		TblOrganization fatherOrg = this.tblOrganizationMapper.selectFatherOrgIdInfoByOrgId(deptId);
		if(null==fatherOrg) {
			return null;
		}
		if(fatherOrg!=null && fatherOrg.getOrgtype() == 0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgname();
	}
	
	public String getCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		TblOrganization fatherOrg = this.tblOrganizationMapper.selectFatherOrgIdInfoByOrgId(deptId);
		if(null==fatherOrg) {
			return null;
		}
		if(fatherOrg!=null && fatherOrg.getOrgtype() == 0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgname();
	}
	
	@Override
	public JsonBean getallCompanyInfoTree(String orgId, String orgName) throws Exception {
		//if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
		Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>();
		List<TblOrganization> childrenList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		if(org.apache.commons.lang.StringUtils.isBlank(orgName)) {
			//无筛选条件直接查询所有的公司信息
			List<TblOrganization> list = tblOrganizationMapper.selectAllListByParentId(new BigDecimal(orgId));
			//根节点公司默认打开，其他公司默认折叠
			boolean open = false;
			if("-1".equals(orgId)) {
				open = true;
			}
			for (TblOrganization org : list) {
				tree = new Tree();
				tree.setName(org.getOrgname());
				tree.setId(org.getOrgid());
				tree.setpId(org.getFatherorgid());
				tree.setOpen(open);
				childrenList = tblOrganizationMapper.selectAllListByParentId(org.getOrgid());
				if(open) {
					tree.setChildren(this.setAllCompanyInfoTree(childrenList));
				}
				tree.setIsParent(childrenList.size() > 0 ? true : false);
				trees.add(tree);
			}
		}else {
			//有筛选条件，筛选符合筛选条件的公司数据
			String allOrgIdStrs = this.tblOrganizationMapper.selectAllListByOrgName(orgName);
			
			if(StringUtils.isEmpty(allOrgIdStrs)) {
				return ResponseFormat.retParam(1, 200, resultMap);
			}
			// 获取所有符合条件的orgIds 包括上级公司的 id
			allOrgIdStrs = this.selectAllOrgIdsByOrgIds(allOrgIdStrs,allOrgIdStrs);
			// 根据orgtype 递归获取公司数据 并保持子父级关系
			int i = 1;
			List<TblOrganization> fatherOrgList = this.tblOrganizationMapper.selectAllListByOrgtypeAndOrgIds(1,allOrgIdStrs);
			Tree childrentree = null;
			for (TblOrganization father : fatherOrgList) {
				tree = new Tree();
				tree.setName(father.getOrgname());
				tree.setId(father.getOrgid());
				tree.setpId(father.getFatherorgid());
				tree.setOpen(true);
				tree.setIsParent(true);
				tree.setChildren(this.setAllOrgInfoTreeChildren(father.getOrgid(),allOrgIdStrs));
				trees.add(tree);
			}
		}
		resultMap.put("data", trees);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	private List<Tree> setAllCompanyInfoTree(List<TblOrganization> list) throws Exception {
		Tree tree = null;
		List<TblOrganization> childrenList = null;
		List<Tree> trees = new ArrayList<Tree>();
		for (TblOrganization org : list) {
			tree = new Tree();
			tree.setName(org.getOrgname());
			tree.setId(org.getOrgid());
			tree.setpId(org.getFatherorgid());
			tree.setOpen(false);
			childrenList = tblOrganizationMapper.selectAllListByParentId(org.getOrgid());
			//tree.setChildren(this.setAllCompanyInfoTree(childrenList));
			tree.setIsParent(childrenList.size() > 0 ? true : false);
			trees.add(tree);
		}
		return trees;
	}
	
	@Override
	public JsonBean getAllOrgTree(String token,TblOrganizationQueryParam query) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>();
		List<TblOrganizationResult> childrenList = null;
		boolean open = false;
		List<TblOrganizationResult> list = new ArrayList<TblOrganizationResult>(0);
		
		if(query.getFatherorgid() == null) {
			/*BigDecimal fatherOrgid = this.getRootCompanyByOrgId(loginStaff.getCurrentOrg().getOrgid());
			query.setFatherorgid(fatherOrgid);*/
			String orgIdTree = this.tblOrganizationResultMapper.selectOrgTreeByorgId(loginStaff.getCurrentOrg().getOrgid());
			if(orgIdTree.indexOf(",") == -1){
				query.setFatherorgid(new BigDecimal(orgIdTree));
			}else {
				query.setFatherorgid(new BigDecimal(orgIdTree.substring(0, orgIdTree.indexOf(","))));
			}

		}
		
		if(org.apache.commons.lang.StringUtils.isNotBlank(query.getOrgname())) {
			//有筛选条件，筛选符合筛选条件的公司树结构数据
			List<String> orgIdTreeList =  this.tblOrganizationResultMapper.selectAllOrgIdTreeByOrgName(query);
			if(orgIdTreeList == null || orgIdTreeList.size() == 0){
				return ResponseFormat.retParam(1, 200, trees);
			}
			
			if(query.getFatherorgid() != null) {
				orgIdTreeList.removeIf(s -> !s.contains(query.getFatherorgid().toString()));
			}
			
			//将组织机构树list 横向封装为 二维数组 ，从父到子的结构
			List<List<String>> orgIdListLevel = getOrgListofLists(orgIdTreeList);
			
			//生成根集合组织父子结构数据
			List<TblOrganizationResult> orgList = this.getOrgTreeListByListofList(orgIdListLevel);
			
			//根据公司集合循环生成tree树
			trees = this.getTreesOrgByList(orgList);
			
		}else {
	        query.setOrgid(query.getFatherorgid());
	        TblOrganizationResult root = this.tblOrganizationResultMapper.selectRootTreeInfoById(query);
	        list.add(root);
	        
			for (TblOrganizationResult org : list) {
				tree = new Tree();
				tree.setName(org.getOrgname());
				tree.setId(org.getOrgid());
				tree.setpId(org.getFatherorgid());
				tree.setOpen(open);
				tree.setType(org.getOrgtype().toString());
				query.setOrgid(org.getOrgid());
				childrenList = this.tblOrganizationResultMapper.selectTreeListInfoByOrgId(query);
				tree.setChildren(this.orgTreeSetChildrenInfo(childrenList,query));
				tree.setIsParent(childrenList.size() > 0 ? true : false);
				trees.add(tree);
			}
		}
		return ResponseFormat.retParam(1, 200, trees);
	}
	
	@Override
	public BigDecimal getRootCompanyByOrgId(BigDecimal orgid) throws Exception {
		TblOrganizationQueryParam query = new TblOrganizationQueryParam();
		query.setFatherorgid(orgid);
		TblOrganizationResult result = this.tblOrganizationResultMapper.selectRootTreeInfoById(query);
		if(new BigDecimal("-1").compareTo(result.getFatherorgid()) == 0) {
			return result.getOrgid();
		}
		return this.getRootCompanyByOrgId(result.getFatherorgid());
	}
	
	
	
	
	
	@Override
	public JsonBean getCompanyInfoTree(String token, TblOrganizationQueryParam query) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>();
		List<TblOrganizationResult> childrenList = null;
		boolean open = true;
		List<TblOrganizationResult> list = new ArrayList<TblOrganizationResult>(0);
        query.setOrgtype(-2);
        
        if(query.getFatherorgid() == null) {
			/*BigDecimal fatherOrgid = this.getRootCompanyByOrgId(loginStaff.getCurrentOrg().getOrgid());
			query.setFatherorgid(fatherOrgid);*/
			String orgIdTree = this.tblOrganizationResultMapper.selectOrgTreeByorgId(loginStaff.getCurrentOrg().getOrgid());
			if(orgIdTree.indexOf(",") == -1){
				query.setFatherorgid(new BigDecimal(orgIdTree));
			}else {
				query.setFatherorgid(new BigDecimal(orgIdTree.substring(0, orgIdTree.indexOf(","))));
			}
		}
        
        if(org.apache.commons.lang.StringUtils.isNotBlank(query.getOrgname())) {
        	//有筛选条件，筛选符合筛选条件的公司树结构数据
			List<String> orgIdTreeList =  this.tblOrganizationResultMapper.selectAllOrgIdTreeByOrgName(query);
			if(orgIdTreeList == null || orgIdTreeList.size() == 0){
				return ResponseFormat.retParam(1, 200, trees);
			}
			if(query.getFatherorgid() != null) {
				orgIdTreeList.removeIf(s -> !s.contains(query.getFatherorgid().toString()));
			}
			//将组织机构树list 横向封装为 二维数组 ，从父到子的结构
			List<List<String>> orgIdListLevel = getOrgListofLists(orgIdTreeList);
			
			//生成根集合组织父子结构数据
			List<TblOrganizationResult> orgList = this.getOrgTreeListByListofList(orgIdListLevel);
			
			//根据公司集合循环生成tree树
			trees = this.getTreesOrgByList(orgList);
        	
        	/*//有筛选条件，筛选符合筛选条件的公司数据
			List<String> orgIdStrs = this.tblOrganizationResultMapper.selectAllOrgIdByOrgName(query);
			if(orgIdStrs == null || orgIdStrs.size() == 0){
				return ResponseFormat.retParam(1, 200, trees);
			}
			//String allOrgIdStrs = String.join(",", orgIdStrs);
			// 获取所有符合条件的orgIds 包括上级公司的 id
			List<String> allOrgIdStrs = this.selectAllOrgIdsByOrgIdsTrees(orgIdStrs,orgIdStrs);
			TblOrganizationResult root = this.tblOrganizationResultMapper.selectRootTreeInfoById(query);
			list.add(root);
			//list = this.tblOrganizationResultMapper.selectChilrenListByOrgIdScope(query.getFatherorgid(),allOrgIdStrs,query);
			for (TblOrganizationResult father : list) {
				tree = new Tree();
				tree.setName(father.getOrgname());
				tree.setId(father.getOrgid());
				tree.setpId(father.getFatherorgid());
				tree.setOpen(true);
				tree.setIsParent(true);
				tree.setChildren(this.setAllOrgInfoTreeChildrenByChoice(father.getOrgid(),allOrgIdStrs,query));
				trees.add(tree);
			}*/
        }else {
            query.setOrgid(query.getFatherorgid());
            TblOrganizationResult root = this.tblOrganizationResultMapper.selectRootTreeInfoById(query);
            list.add(root);
            
            for (TblOrganizationResult org : list) {
    			tree = new Tree();
    			tree.setName(org.getOrgname());
    			tree.setId(org.getOrgid());
    			tree.setpId(org.getFatherorgid());
    			tree.setOpen(open);
    			tree.setBgimage(org.getBgimage());
    			tree.setBgname(org.getBgname());
    			tree.setCtztimage(org.getCtztimage());
    			tree.setCtztname(org.getCtztname());
    			tree.setJdztimage(org.getJdztimage());
    			tree.setJdztname(org.getJdztname());
    			tree.setLogoimage(org.getLogoimage());
    			tree.setLogoname(org.getLogoname());
    			tree.setBaname(org.getBaname());
    			query.setOrgid(org.getOrgid());
    			childrenList = this.tblOrganizationResultMapper.selectTreeListInfoByOrgId(query);
    			tree.setChildren(this.orgTreeSetChildrenInfo(childrenList,query));
    			tree.setIsParent(childrenList.size() > 0 ? true : false);
    			trees.add(tree);
    		}
        }
        return ResponseFormat.retParam(1, 200, trees);
	}
	
	@Override
	public JsonBean getDepartmentInfoTree(String token, TblOrganizationQueryParam query) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>();
		List<TblOrganizationResult> childrenList = null;
		boolean open = true;
		List<TblOrganizationResult> list = new ArrayList<TblOrganizationResult>(0);
        query.setOrgtype(-1);
        
        if(query.getFatherorgid() == null) {
        	query.setFatherorgid(loginStaff.getCurrentOrg().getOrgid());
        }
        
        if(org.apache.commons.lang.StringUtils.isNotBlank(query.getOrgname())) {
        	//有筛选条件，筛选符合筛选条件的公司树结构数据
			/*List<String> orgIdTreeList =  this.tblOrganizationResultMapper.selectAllOrgIdTreeByOrgName(query);
			if(orgIdTreeList == null || orgIdTreeList.size() == 0){
				return ResponseFormat.retParam(1, 200, trees);
			}
			
			//将组织机构树list 横向封装为 二维数组 ，从父到子的结构
			List<List<String>> orgIdListLevel = getOrgListofLists(orgIdTreeList);
			
			//生成根集合组织父子结构数据
			List<TblOrganizationResult> orgList = this.getOrgTreeListByListofList(orgIdListLevel);
			
			//根据公司集合循环生成tree树
			trees = this.getTreesOrgByList(orgList);*/
        	
        	//有筛选条件，筛选符合筛选条件的公司数据
			List<String> orgIdStrs = this.tblOrganizationResultMapper.selectAllOrgIdByOrgName(query);
			if(orgIdStrs == null || orgIdStrs.size() == 0){
				return ResponseFormat.retParam(1, 200, trees);
			}
			//String allOrgIdStrs = String.join(",", orgIdStrs);
			// 获取所有符合条件的orgIds 包括上级公司的 id
			List<String> allOrgIdStrs = this.selectAllOrgIdsByOrgIdsTrees(orgIdStrs,orgIdStrs);
			
	        TblOrganizationResult root = this.tblOrganizationResultMapper.selectRootTreeInfoById(query);
	        list.add(root);
			for (TblOrganizationResult father : list) {
				tree = new Tree();
				tree.setName(father.getOrgname());
				tree.setId(father.getOrgid());
				tree.setpId(father.getFatherorgid());
				tree.setOpen(true);
				tree.setIsParent(true);
				tree.setChildren(this.setAllOrgInfoTreeChildrenByChoice(father.getOrgid(),allOrgIdStrs,query));
				trees.add(tree);
			}
        }else {
            query.setOrgid(query.getFatherorgid());
            TblOrganizationResult root = this.tblOrganizationResultMapper.selectRootTreeInfoById(query);
            list.add(root);
            for (TblOrganizationResult org : list) {
    			tree = new Tree();
    			tree.setName(org.getOrgname());
    			tree.setId(org.getOrgid());
    			tree.setpId(org.getFatherorgid());
    			tree.setOpen(open);
    			query.setOrgid(org.getOrgid());
    			childrenList = this.tblOrganizationResultMapper.selectTreeListInfoByOrgId(query);
    			tree.setChildren(this.orgTreeSetChildrenInfo(childrenList,query));
    			tree.setIsParent(childrenList.size() > 0 ? true : false);
    			trees.add(tree);
    		}
        }
        return ResponseFormat.retParam(1, 200, trees);
	}
	
	
	@Override
	public JsonBean getCompanyListInfo(String token, TblOrganizationQueryParam query, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        boolean flag = false;
        if(query.getFatherorgid() == null) {
        	String orgTrees= this.tblOrganizationResultMapper.selectOrgTreeByorgId(loginStaff.getCurrentOrg().getOrgid());
        	String rootOrgId = "";
			if(orgTrees.indexOf(",") == -1){
				rootOrgId = orgTrees;
			}else {
				rootOrgId = orgTrees.substring(0, orgTrees.indexOf(","));
			}

			//BigDecimal rootOrgId = this.getRootCompanyByOrgId(loginStaff.getCurrentOrg().getOrgid());
			query.setFatherorgid(new BigDecimal(rootOrgId));
			flag = true;
		}
		
        /*//获取所有公司ID拼接的字符串
        String orgIdStrs = this.getAllCompanyIdStrsByfahterOrgids(query.getFatherorgid().toString(),query.getStatus(),"");
        orgIdStrs = orgIdStrs.substring(1, orgIdStrs.length());*/
        
        //通过父级公司主键获取所有的下级公司主键的集合
        List<String> childrenIdList = this.tblOrganizationResultMapper.selectAllCompanyChildrenIdsByTrees(query.getFatherorgid().toString(), query.getStatus());
        query.setOrgIdList(childrenIdList);
        if(flag) {
        	query.setFatherorgid(null);
        }

        //分页查询
        PageHelper.startPage(pageNumber, pageSize);
        List<TblOrganizationResult> list = this.tblOrganizationResultMapper.selectListByPageInfo(query);
		
        return ResponseFormat.retParam(1, 200, new com.github.pagehelper.PageInfo<>(list));
	}
	
	private String getAllCompanyIdStrsByfahterOrgids(String choiceIds,Integer status,String orgIdStrs) throws Exception {
		List<String> orgIdList = this.tblOrganizationResultMapper.selectChildrenCompanyIds(choiceIds,status);
		
		if(orgIdList == null || orgIdList.size() == 0) {
			return orgIdStrs;
		}
		choiceIds = String.join(",", orgIdList);
		orgIdStrs += ","+choiceIds;
		return this.getAllCompanyIdStrsByfahterOrgids(choiceIds, status, orgIdStrs);
	}
	
	@Override
	public JsonBean getDepartmentListInfo(String token, TblOrganizationQueryParam query, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        boolean flag = false;
        if(query.getFatherorgid() == null) {
			query.setFatherorgid(loginStaff.getCurrentOrg().getOrgid());
			flag = true;
		}
		
        //获取所有公司ID拼接的字符串
        String orgIdStrs = this.getAllDepartmentIdStrsByfahterOrgids(query.getFatherorgid().toString(),query.getStatus(),"");
        orgIdStrs = orgIdStrs.substring(1, orgIdStrs.length());
        query.setOrgIdStrs(orgIdStrs);
        if(flag) {
        	query.setFatherorgid(null);
        }
        
        //分页查询
        PageHelper.startPage(pageNumber, pageSize);
       
        List<TblOrganizationResult> list = this.tblOrganizationResultMapper.selectListByPageInfo(query);
        return ResponseFormat.retParam(1, 200, new com.github.pagehelper.PageInfo<>(list));
	}
	
	@Override
	public String getAllChildrenOrgIdStrs(String orgId, Integer orgtype) throws Exception {
		List<String> orgIdList = this.tblOrganizationResultMapper.selectAllChildrenIds(orgId,orgtype);
		String orgIdStrs = orgId;
		if(orgIdList == null || orgIdList.size() == 0) {
			return orgIdStrs;
		}
		String childrenStrs = String.join(",", orgIdList);
		return this.getAllChildrenOrgIdStrsByFaterStrs(childrenStrs,orgIdStrs+","+childrenStrs,orgtype);
	}
	
	
	private String getAllChildrenOrgIdStrsByFaterStrs(String childrenStrs, String orgIdStrs, Integer orgtype) throws Exception {
		List<String> orgIdList = this.tblOrganizationResultMapper.selectAllChildrenIds(childrenStrs,orgtype);
		if(orgIdList == null || orgIdList.size() == 0) {
			return orgIdStrs;
		}
		childrenStrs = String.join(",", orgIdList);
		return this.getAllChildrenOrgIdStrsByFaterStrs(childrenStrs,orgIdStrs+","+childrenStrs,orgtype);
	}

	private String getAllDepartmentIdStrsByfahterOrgids(String choiceIds,Integer status,String orgIdStrs) throws Exception {
		List<String> orgIdList = this.tblOrganizationResultMapper.selectChildrenDepartmentIds(choiceIds,status);
		
		if(orgIdList == null || orgIdList.size() == 0) {
			return orgIdStrs;
		}
		choiceIds = String.join(",", orgIdList);
		orgIdStrs += ","+choiceIds;
		return this.getAllDepartmentIdStrsByfahterOrgids(choiceIds, status, orgIdStrs);
	}
	
	

	private List<String> selectAllOrgIdsByOrgIdsTrees(List<String> orgStrIds,List<String> allOrgIdStrs) throws Exception {
		List<String> orgIdList = this.tblOrganizationResultMapper.selectOrgIdsByallOrgIdStrs(orgStrIds,allOrgIdStrs);
		if(orgIdList == null || orgIdList.size() == 0) {
			return allOrgIdStrs;
		}
		allOrgIdStrs.addAll(orgIdList);
		return this.selectAllOrgIdsByOrgIdsTrees(orgIdList,allOrgIdStrs);
	}

	private List<Tree> setAllOrgInfoTreeChildrenByChoice(BigDecimal orgid, List<String> allOrgIdList,TblOrganizationQueryParam query) throws Exception {
		String orgIdsSql = DataBaseSqlConfig.getInOrNotSql("ORGID", "IN", "OR", allOrgIdList);
		List<TblOrganizationResult> childrenList = this.tblOrganizationResultMapper.selectChilrenListByOrgIdScope(orgid,orgIdsSql,query);
		Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>(0);
		for (TblOrganizationResult org : childrenList) {
			tree = new Tree();
			tree.setName(org.getOrgname());
			tree.setId(org.getOrgid());
			tree.setpId(org.getFatherorgid());
			tree.setChildren(this.setAllOrgInfoTreeChildrenByChoice(org.getOrgid(),allOrgIdList,query));
			tree.setOpen(tree.getChildren().size()>0?true:false);
			tree.setIsParent(tree.getChildren().size()>0?true:false);
			tree.setType(org.getOrgtype().toString());
			trees.add(tree);
		}
		return trees;
	}
	
	
	private List<Tree> orgTreeSetChildrenInfo(List<TblOrganizationResult> list,TblOrganizationQueryParam query) throws Exception {
		Tree tree = null;
		List<TblOrganizationResult> childrenList = null;
		List<Tree> trees = new ArrayList<Tree>();
		for (TblOrganizationResult org : list) {
			tree = new Tree();
			tree.setName(org.getOrgname());
			tree.setId(org.getOrgid());
			tree.setpId(org.getFatherorgid());
			tree.setOpen(false);
			tree.setType(org.getOrgtype().toString());
			tree.setBgimage(org.getBgimage());
			tree.setBgname(org.getBgname());
			tree.setCtztimage(org.getCtztimage());
			tree.setCtztname(org.getCtztname());
			tree.setJdztimage(org.getJdztimage());
			tree.setJdztname(org.getJdztname());
			tree.setLogoimage(org.getLogoimage());
			tree.setLogoname(org.getLogoname());
			tree.setBaname(org.getBaname());
			query.setOrgid(org.getOrgid());
			childrenList = this.tblOrganizationResultMapper.selectTreeListInfoByOrgId(query);
			tree.setIsParent(childrenList.size() > 0 ? true : false);
			trees.add(tree);
		}
		return trees;
	}
	
	@Override
	public String selectAllDeptIdsByOrgName(String companyName) throws Exception {
		List<String> orgId = this.tblOrganizationMapper.selectOrgIdsByOrgName(companyName);
		if(orgId != null && orgId.size() != 0) {
			String orgIdStrs = String.join(",", orgId);
			return this.selectChildrenDeptIdsByFatherOrgIds(orgIdStrs,orgIdStrs);
		}
		return null;
	}

	private String selectChildrenDeptIdsByFatherOrgIds(String orgId,String result) throws Exception {
		List<String> chilredDeptIds = this.tblOrganizationMapper.selectChildrenDeptIdsByFatherOrgIds(orgId);
		if(chilredDeptIds != null && chilredDeptIds.size() != 0) {
			String chilreDept = String.join(",", chilredDeptIds);
			result += ","+chilreDept;
			return this.selectChildrenDeptIdsByFatherOrgIds(chilreDept,result);
		}
		return result;
	}
	
	@Override
	public JsonBean getallCompanyInfoTreeToGrantRole(String orgId, String orgName, BigDecimal roleId) throws Exception {
		Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>();
		List<TblOrganization> childrenList = null;
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		if(org.apache.commons.lang.StringUtils.isBlank(orgName)) {
			//没有筛选条件，查询所有公司，分配给当前角色roleId的公司设置为选中状态
			List<TblOrganization> list = tblOrganizationMapper.selectAllListByParentIdToRoleId(new BigDecimal(orgId),roleId);
			boolean open = false;
			if("-1".equals(orgId)) {
				open = true;
			}
			//循环查询数据生成tree结构，如果有子集记录，则递归查询一并生成
			for (TblOrganization org : list) {
				tree = new Tree();
				tree.setName(org.getOrgname());
				tree.setId(org.getOrgid());
				tree.setpId(org.getFatherorgid());
				tree.setOpen(open);
				tree.setDisabled(org.getIsChecked()==0?false:true);
				childrenList = tblOrganizationMapper.selectAllListByParentIdToRoleId(org.getOrgid(),roleId);
				if(open) {
					tree.setChildren(this.setAllCompanyInfoTreeByRole(childrenList,roleId));
				}
				tree.setIsParent(childrenList.size() > 0 ? true : false);
				trees.add(tree);
			}
		}else {
			//有筛选条件，查询出所有符合条件的公司主键
			List<String> allOrgIdList = this.tblOrganizationMapper.selectAllCompanyListByOrgName(orgName);
			
			if(allOrgIdList == null || allOrgIdList.size() == 0) {
				TblOrganization org = this.tblOrganizationMapper.selectByOrgId(new BigDecimal(orgId));
				tree = new Tree();
				tree.setName(org.getOrgname());
				tree.setId(org.getOrgid());
				tree.setpId(org.getFatherorgid());
				tree.setOpen(false);
				tree.setIsParent(false);
				tree.setDisabled(false);
				trees.add(tree);
				resultMap.put("data", trees);
				return ResponseFormat.retParam(1, 200, resultMap);
			}
			// 获取所有符合条件的orgIds 包括上级公司的 id
			String allOrgIdStrs = String.join(",", allOrgIdList);
			allOrgIdStrs = this.selectAllOrgIdsByOrgIds(allOrgIdStrs,allOrgIdStrs);
			// 根据orgtype 递归获取公司数据 并保持子父级关系
			int i = 1;
			List<TblOrganization> fatherOrgList = this.tblOrganizationMapper.selectAllListByOrgtypeAndOrgIdsByRole(1,allOrgIdStrs,roleId);
			Tree childrentree = null;
			//循环递归生成tree树结构
			for (TblOrganization father : fatherOrgList) {
				tree = new Tree();
				tree.setName(father.getOrgname());
				tree.setId(father.getOrgid());
				tree.setpId(father.getFatherorgid());
				tree.setOpen(true);
				tree.setIsParent(true);
				tree.setDisabled(father.getIsChecked()==0?false:true);
				tree.setChildren(this.setAllOrgInfoTreeChildrenToRole(father.getOrgid(),allOrgIdStrs,roleId));
				trees.add(tree);
			}
		}
		resultMap.put("data", trees);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	private List<Tree> setAllCompanyInfoTreeByRole(List<TblOrganization> list, BigDecimal roleId) throws Exception {
		Tree tree = null;
		List<TblOrganization> childrenList = null;
		List<Tree> trees = new ArrayList<Tree>();
		for (TblOrganization org : list) {
			tree = new Tree();
			tree.setName(org.getOrgname());
			tree.setId(org.getOrgid());
			tree.setpId(org.getFatherorgid());
			tree.setOpen(false);
			childrenList = tblOrganizationMapper.selectAllListByParentIdToRoleId(org.getOrgid(),roleId);
			//tree.setChildren(this.setAllCompanyInfoTree(childrenList));
			tree.setIsParent(childrenList.size() > 0 ? true : false);
			tree.setDisabled(org.getIsChecked()==0?false:true);
			trees.add(tree);
		}
		return trees;
	}
	
	private List<Tree> setAllOrgInfoTreeChildrenToRole(BigDecimal orgid, String allOrgIdStrs, BigDecimal roleId) throws Exception {
		List<TblOrganization> childrenList = this.tblOrganizationMapper.selectChilrenListByOrgIdScopeToRole(orgid,allOrgIdStrs,roleId);
		Tree tree = null;
		List<Tree> trees = new ArrayList<Tree>(0);
		for (TblOrganization org : childrenList) {
			tree = new Tree();
			tree.setName(org.getOrgname());
			tree.setId(org.getOrgid());
			tree.setpId(org.getFatherorgid());
			tree.setDisabled(org.getIsChecked()==0?false:true);
			tree.setChildren(this.setAllOrgInfoTreeChildrenToRole(org.getOrgid(),allOrgIdStrs,roleId));
			tree.setOpen(tree.getChildren().size()>0?true:false);
			tree.setIsParent(tree.getChildren().size()>0?true:false);
			trees.add(tree);
		}
		return trees;
	}

	@Override
	public Integer findCompanyIdByDeptId(String orgId) throws Exception {
		Integer orgid = 0;
			orgid = tblOrganizationMapper.findCompanyIdByDeptId(orgId);
		return orgid;
	}

	@Override
	public Integer selectCountByName(String companyName) throws Exception {
		return this.tblOrganizationMapper.selectCountByName(companyName);
	}
	
	@Override
	public BigDecimal findDeptInfoByorgName(String companyName) throws Exception {
		return this.tblOrganizationMapper.selectOrgIdByCompanyName(companyName);
	}

	@Override
	public JsonBean checkCompanyName(String companyName) throws Exception {
		Integer count = tblOrganizationMapper.selectCountByRootName(companyName);
		if(count == 0 ) {
			return ResponseFormat.retParam(200, 200, null);
		}
		return ResponseFormat.retParam(0, "公司名称重复", null);
	}

	@Override
	public TblOrganization findCompanyInfoByDeptId(BigDecimal orgid) throws Exception {
		return getCompanyInfoByChildren(orgid);
	}
	
	
	private TblOrganization getCompanyInfoByChildren(BigDecimal orgid) {
		TblOrganization fatherOrg = this.tblOrganizationMapper.selectByOrgid(orgid);
		if(fatherOrg!=null && fatherOrg.getOrgtype().compareTo(0) == 0) {
			return getCompanyInfoByChildren(fatherOrg.getFatherorgid());
		}
		return fatherOrg;
	}
	
	@Override
	public void selectSetDeptIdsByOrgId(BigDecimal orgid, Set<String> deptSet) throws Exception {
		List<String> deptIdList = this.tblOrganizationMapper.selectSetDeptIdListByOrgId(orgid);
		for (String deptId : deptIdList) {
			deptSet.add(deptId);
			this.setChildrenIdsByOrgId(deptId,deptSet);
		}
		
	}
	
	private void setChildrenIdsByOrgId(String deptId, Set<String> deptSet) throws Exception {
		List<String> deptIdList = this.tblOrganizationMapper.selectSetDeptIdListByOrgId(new BigDecimal(deptId));
		for (String id : deptIdList) {
			deptSet.add(id);
			this.setChildrenIdsByOrgId(id,deptSet);
		}
	}
	
	@Override
	public String selectAllDeptIdsByOrgId(BigDecimal pid) throws Exception {
		List<String> deptIds = this.tblOrganizationMapper.selectDeptIdsByCompanyId(pid.toString());
		if(deptIds == null || deptIds.size() == 0){
			return pid.toString();
		}
		String deptId = String.join(",", deptIds);
		return this.setAllDeptIdsByCompanyIds(deptId,pid+","+deptId);
	}
	
	public String setAllDeptIdsByCompanyIds(String deptIds,String result) throws Exception {
		List<String> deptIdList = this.tblOrganizationMapper.selectDeptIdsByCompanyId(deptIds);
		if(deptIdList == null || deptIdList.size() == 0){
			return result;
		}
		String deptId = String.join(",", deptIdList);
		return this.setAllDeptIdsByCompanyIds(deptId,result+","+deptId);
	}

	@Override
	public TblOrganization findByOrgNumber(String orgnumber) throws Exception {
		return this.tblOrganizationMapper.selectInfoByOrgNumber(orgnumber);
	}

	@Override
	public void importCompanyExcelData(XSSFSheet sheet, String token, Integer importType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ;
        }
        
        XSSFRow row = null;
        XSSFCell cell = null;
        TblOrganization org = null;
        TblOrganization faOrg = null;
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
            	org = new TblOrganization();

            	org.setOrgid(RandomUtil.uuBigDecimalId());
            	org.setStatus(0);
            	org.setOrderid(i);
            	
                cell = row.getCell(0);
                if(cell != null){
                    cell.setCellType(CellType.STRING);
                    org.setOrgname(StringUtils.isEmpty(cell.getStringCellValue())?null:cell.getStringCellValue());
                }

                cell = row.getCell(1);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    org.setOrgmeno(cell.getStringCellValue());
                }
                
                org.setOrgnumber(PinyinUtil.getFirstLetter(org.getOrgname(), "").toUpperCase());
                
                cell = row.getCell(2);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    org.setFatherorgid(new BigDecimal(cell.getStringCellValue()));
                }
                
                org.setOrgtype(importType);
                faOrg = tblOrganizationMapper.findByid(org.getFatherorgid().toString());
                org.setOrganizationTrees(faOrg.getOrganizationTrees()+","+org.getOrgid());
                this.tblOrganizationMapper.insert(org);
                
                if(importType == 0) {
                	this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.DEPTINSERT);
                }else {
                	this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.ORGINSERT);
                }
            }
        }
	}

	@Override
	public String checkImportCompany(XSSFSheet sheet, String token) throws Exception {
        XSSFRow row = null;
        XSSFCell cell = null;
        String allName = "";
        String orgName = "";
        Integer count = 0;
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row != null) {
                cell = row.getCell(0);
                if(cell != null){
                    cell.setCellType(CellType.STRING);
                    orgName = StringUtils.isEmpty(cell.getStringCellValue())?null:cell.getStringCellValue();
                }
                count = this.tblOrganizationMapper.selectCountByName(orgName);
                if(count == 0) {
                	allName = orgName + ",";
                }
                
            }
        }
        return allName;
	}
	
	// 命名空间常量
    private static final String SOAP_NS = "http://schemas.xmlsoap.org/soap/envelope/";
    private static final String SYSWARE_NS = "http://server.adapter.sysware.com";
    
	//中核
	//更新组织信息(全量)
	@Override
	public void syncZhOrg(String xmlData, TblSynchronizationRecord record) throws Exception {
		if(null==xmlData) {
			return;
		}
		
		try {
			
			Document initDoc = XmlManager.string2Dom(xmlData);
			if(initDoc == null) {
				return;
			}
			
			String initdocStr = XmlManager.dom2String(initDoc);
			
			//特殊字符转义
			initdocStr = initdocStr.replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			record.setResulttext(record.getResulttext()+"1.转义特殊字符。。。。。。"+initdocStr);
			// 解析XML
			Document doc = XmlManager.string2Dom(initdocStr);
			record.setResulttext(record.getResulttext()+"\n2.解析XML。。。。。。");
			
			String errorCode = WebServiceClient.getLeafVal(doc,"//returnData/errorCode");
			
			if(errorCode == null || !"0".equals(errorCode)) {
				record.setResulttext(record.getResulttext()+"\n获取数据失败!");
				return;
			}
			
			List<Map<String,String>> orgList = WebServiceClient.getLeafs(doc,"//returnData/batch/orgs/org");
			
			//查询已存在的公司信息list ,生成key = orgnumber,value = org实体 的map ,用于判断当前组织信息是新增还是修改；
			QueryWrapper<TblOrganization> wrapper = new QueryWrapper<TblOrganization>();
			wrapper.between("ORGTYPE", 1, 100).select("ORGID,ORGNUMBER");
			List<TblOrganization> existCompanyList = this.tblOrganizationMapper.selectList(wrapper);
			Map<String, TblOrganization> existCompanyInfoMap = existCompanyList.stream().collect(Collectors.toMap(TblOrganization::getOrgnumber,Function.identity(),(existing, replacement) -> existing ));
			record.setResulttext(record.getResulttext()+"\n3.获取已存在的公司组织信息。。。。。。");
			TblOrganization com = parseOrgs(orgList,record);
			
			com.setUniqueNumber(com.getOrgnumber());
			com.setOrgcreate(new Date());
			if(existCompanyInfoMap != null && existCompanyInfoMap.containsKey(com.getOrgnumber())) {
				com.setOrgid(existCompanyInfoMap.get(com.getOrgnumber()).getOrgid());
				com.setOrganizationTrees(com.getOrgid().toString());
				com.setOrgtype(1);
				this.tblOrganizationMapper.updateById(com);
				if(YMUrlStatic.status == 0) {
					this.ymBusinessService.dealUniqueOrgInfo(com,YMUrlStatic.ORGUPDATE);
				}
			}else {
				com.setOrgid(RandomUtil.uuBigDecimalId());
				com.setOrganizationTrees(com.getOrgid().toString());
				com.setOrgtype(1);
				this.tblOrganizationMapper.insert(com);
				if(YMUrlStatic.status == 0) {
					this.ymBusinessService.dealUniqueOrgInfo(com,YMUrlStatic.ORGINSERT);
				}
			}
			record.setResulttext(record.getResulttext()+"\n处理组织数据："+com.toString());
			existCompanyInfoMap.put(com.getOrgnumber(), com);
			this.dealSaveSynchronizationOrgChildrenInfo(com,com.getChildrenList(),existCompanyInfoMap,com.getUniqueNumber(),record);
			
			// 解析depts
			QueryWrapper<TblOrganization> deptwrap = new QueryWrapper<TblOrganization>();
			deptwrap.select("ORGID,ORGNUMBER,ORGTYPE,ORGANIZATIONTREES,UNIQUENUMBER");
			List<TblOrganization> existDeptList = this.tblOrganizationMapper.selectList(deptwrap);
			Map<String, TblOrganization> existDeptMap = existDeptList.stream().collect(Collectors.toMap(TblOrganization::getOrgnumber,Function.identity(),(existing, replacement) -> existing ));
			
			orgList = WebServiceClient.getLeafs(doc,"//returnData/batch/depts/dept");
			List<TblOrganization> rootDepts = parseDepts(orgList,existCompanyInfoMap,record);
			TblOrganization facomInfo = null;
			boolean orgflag = false;
			for (TblOrganization rootDept : rootDepts) {
				record.setResulttext(record.getResulttext()+"\n处理组织数据："+rootDept.toString());
				facomInfo = existCompanyInfoMap.get(rootDept.getFahterOrgName()); 
				record.setResulttext(record.getResulttext()+"获取父级节点信息："+facomInfo.toString());
				rootDept.setFatherorgid(facomInfo.getOrgid());
				rootDept.setUniqueNumber(facomInfo.getUniqueNumber()+facomInfo.getOrgnumber());
				rootDept.setOrgcreate(new Date());
				orgflag = false;
				
				if(zhOrgNumberStrs.indexOf(","+rootDept.getOrgnumber()+",") != -1 ) {
					orgflag = true;
					rootDept.setOrgtype(facomInfo.getOrgtype() + 1);
					rootDept.setUniqueNumber(facomInfo.getUniqueNumber());
				}
				
				if(existDeptMap != null && existDeptMap.containsKey(rootDept.getOrgnumber())) {
					rootDept.setOrgid(existDeptMap.get(rootDept.getOrgnumber()).getOrgid());
					rootDept.setOrganizationTrees(facomInfo.getOrganizationTrees()+","+rootDept.getOrgid());
					this.tblOrganizationMapper.updateById(rootDept);
					if(YMUrlStatic.status == 0) {
						if(orgflag) {
							this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.ORGUPDATE);
						}else {
							this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.DEPTUPDATE);
						}
					}
				}else {
					rootDept.setOrgid(RandomUtil.uuBigDecimalId());
					rootDept.setOrganizationTrees(facomInfo.getOrganizationTrees()+","+rootDept.getOrgid());
					this.tblOrganizationMapper.insert(rootDept);
					if(YMUrlStatic.status == 0) {
						if(orgflag) {
							this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.ORGINSERT);
						}else {
							this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.DEPTINSERT);
						}
					}
				}
				record.setResulttext(record.getResulttext()+"\n处理组织数据主键信息："+rootDept.getOrgid());
				existDeptMap.put(rootDept.getOrgnumber(), rootDept);
				this.dealSaveSynchronizationDeptChildrenInfo(rootDept,rootDept.getChildrenList(),existDeptMap,rootDept.getUniqueNumber(),record);
			}
			record.setResulttext(record.getResulttext()+"\n同步成功！");
		}catch (Exception e) {
			record.setResulttext(record.getResulttext()+"\n同步失败："+e.getMessage()+e.getCause()+e.getClass()+e.getLocalizedMessage());
		}finally{
			this.tblSynchronizationRecordMapper.insert(record);
		}
	}
	
	//更新组织信息(增量)
	@Override
	public void syncZhNewOrg(String xmlData, TblSynchronizationRecord record) throws Exception {
		try {
			if(null==xmlData) {
				return;
			}
			Document initDoc = XmlManager.string2Dom(xmlData);
			if(initDoc == null) {
				return;
			}
			
			String initdocStr = XmlManager.dom2String(initDoc);
			
			//特殊字符转义
			initdocStr = initdocStr.replaceAll("<!\\[CDATA\\[", "").replaceAll("]]>", "").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			record.setResulttext(record.getResulttext()+"1.转义特殊字符。。。。。。"+initdocStr);
			// 解析XML
			Document doc = XmlManager.string2Dom(initdocStr);
			record.setResulttext(record.getResulttext()+"\n2.解析XML。。。。。。");
			
			String errorCode = WebServiceClient.getLeafVal(doc,"//returnData/errorCode");
			
			if(errorCode == null || !"0".equals(errorCode)) {
				if("4".equals(errorCode)) {
					record.setResulttext(record.getResulttext()+"\n无同步记录!");
					return;
				}
				record.setResulttext(record.getResulttext()+"\n获取数据失败!");
				return;
			}
			
			
			//查询已存在的公司信息list ,生成key = orgnumber,value = org实体 的map ,用于判断当前组织信息是新增还是修改；
			QueryWrapper<TblOrganization> wrapper = new QueryWrapper<TblOrganization>();
			wrapper.between("ORGTYPE", 1, 100).select("ORGID,ORGNUMBER,ORGTYPE,ORGANIZATIONTREES");
			List<TblOrganization> existCompanyList = this.tblOrganizationMapper.selectList(wrapper);
			Map<String, TblOrganization> existCompanyInfoMap = existCompanyList.stream().collect(Collectors.toMap(TblOrganization::getOrgnumber,Function.identity(),(existing, replacement) -> existing ));
			
			List<Map<String,String>> list = WebServiceClient.getLeafs(doc,"//returnData/orgs/org");
			List<TblOrganization> comList =  paseOrgsForIncrement(list,existCompanyInfoMap,record);
			
			if(comList != null && comList.size() != 0) {
				String comUniqueNumber = "1000";
				
				for (TblOrganization com : comList) {
					if(existCompanyInfoMap != null && existCompanyInfoMap.containsKey(com.getOrgnumber())) {
						com.setOrgid(existCompanyInfoMap.get(com.getOrgnumber()).getOrgid());
						com.setOrganizationTrees(com.getOrganizationTrees()+","+com.getOrgid().toString());
						com.setUniqueNumber(comUniqueNumber);
						this.tblOrganizationMapper.updateById(com);
						if(YMUrlStatic.status == 0) {
							this.ymBusinessService.dealUniqueOrgInfo(com,YMUrlStatic.ORGUPDATE);
						}
					}else {
						com.setOrgid(RandomUtil.uuBigDecimalId());
						com.setOrganizationTrees(com.getOrganizationTrees()+","+com.getOrgid().toString());
						com.setOrgtype(1);
						com.setUniqueNumber(comUniqueNumber);
						this.tblOrganizationMapper.insert(com);
						if(YMUrlStatic.status == 0) {
							this.ymBusinessService.dealUniqueOrgInfo(com,YMUrlStatic.ORGINSERT);
						}
					}
					record.setResulttext(record.getResulttext()+"\n处理组织数据："+com.toString());
					this.dealSaveSynchronizationOrgChildrenInfo(com,com.getChildrenList(),existCompanyInfoMap,comUniqueNumber,record);
				}
			}
			
			// 解析depts
			QueryWrapper<TblOrganization> deptwrap = new QueryWrapper<TblOrganization>();
			deptwrap.select("ORGID,ORGNUMBER,ORGTYPE,ORGANIZATIONTREES,UNIQUENUMBER");
			List<TblOrganization> existOrgList = this.tblOrganizationMapper.selectList(deptwrap);
			Map<String, TblOrganization> existOrgMap = existOrgList.stream().collect(Collectors.toMap(TblOrganization::getOrgnumber,Function.identity(),(existing, replacement) -> existing ));
			
			list = WebServiceClient.getLeafs(doc,"//returnData/depts/dept");
			List<TblOrganization> rootDepts = parseDeptsForIncrement(list,existOrgMap,record);
			
			TblOrganization pcom = null;
			
			boolean orgflag = false;
			for (TblOrganization rootDept : rootDepts) {
				orgflag = false;
				pcom = existOrgMap.get(rootDept.getFahterOrgName());
				if(zhOrgNumberStrs.indexOf(","+rootDept.getOrgnumber()+",") != -1 ) {
					orgflag = true;
					rootDept.setOrgtype(pcom.getOrgtype() + 1);
					rootDept.setUniqueNumber(pcom.getUniqueNumber());
				}
				
				if(existOrgMap != null && existOrgMap.containsKey(rootDept.getOrgnumber())) {
					rootDept.setOrgid(existOrgMap.get(rootDept.getOrgnumber()).getOrgid());
					rootDept.setOrganizationTrees(rootDept.getOrganizationTrees()+","+rootDept.getOrgid());
					this.tblOrganizationMapper.updateById(rootDept);
					if(YMUrlStatic.status == 0) {
						if(orgflag) {
							this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.ORGUPDATE);
						}else {
							this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.DEPTUPDATE);
						}
					}
				}else {
					rootDept.setOrgid(RandomUtil.uuBigDecimalId());
					rootDept.setOrganizationTrees(rootDept.getOrganizationTrees()+","+rootDept.getOrgid());
					this.tblOrganizationMapper.insert(rootDept);
					if(YMUrlStatic.status == 0) {
						if(orgflag) {
							this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.ORGINSERT);
						}else {
							this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.DEPTINSERT);
						}
					}
				}
				existOrgMap.put(rootDept.getOrgnumber(), rootDept);
				this.dealSaveSynchronizationDeptChildrenInfo(rootDept,rootDept.getChildrenList(),existOrgMap,rootDept.getUniqueNumber(),record);
			}
			record.setResulttext(record.getResulttext()+"\n同步成功！");
		} catch (Exception e) {
			e.printStackTrace();
			record.setResulttext(record.getResulttext()+"\n同步失败："+e.getMessage());
		}finally{
			this.tblSynchronizationRecordMapper.insert(record);
		}
	}
	

	private void dealSaveSynchronizationDeptChildrenInfo(TblOrganization rootDept, List<TblOrganization> childrenList,
			Map<String, TblOrganization> existDeptMap, String uniqueNumber, TblSynchronizationRecord record) throws Exception {
		boolean orgflag = false;
    	for (TblOrganization chil : childrenList) {
    		orgflag = false;
    		chil.setFatherorgid(rootDept.getOrgid());
    		chil.setUniqueNumber(uniqueNumber);
    		chil.setOrgcreate(new Date());
    		record.setResulttext(record.getResulttext()+"\n处理组织数据："+chil.toString());
    		if(zhOrgNumberStrs.indexOf(","+rootDept.getOrgnumber()+",") != -1 ) {
				orgflag = true;
				chil.setOrgtype(rootDept.getOrgtype() + 1);
				chil.setUniqueNumber(rootDept.getUniqueNumber());
			}
    		
    		if(existDeptMap != null && existDeptMap.containsKey(chil.getOrgnumber())) {
    			chil.setOrgid(existDeptMap.get(chil.getOrgnumber()).getOrgid());
    			chil.setOrganizationTrees(rootDept.getOrganizationTrees()+","+chil.getOrgid());
				this.tblOrganizationMapper.updateById(chil);
				if(YMUrlStatic.status == 0) {
					if(orgflag) {
						this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.ORGUPDATE);
					}else {
						this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.DEPTUPDATE);
					}
				}
			}else {
				chil.setOrgid(RandomUtil.uuBigDecimalId());
				chil.setOrganizationTrees(rootDept.getOrganizationTrees()+","+chil.getOrgid());
				this.tblOrganizationMapper.insert(chil);
				if(YMUrlStatic.status == 0) {
					if(orgflag) {
						this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.ORGINSERT);
					}else {
						this.ymBusinessService.dealUniqueOrgInfo(rootDept,YMUrlStatic.DEPTINSERT);
					}
				}
			}
    		record.setResulttext(record.getResulttext()+"\n处理组织数据主键信息："+chil.getOrgid());
			existDeptMap.put(chil.getOrgnumber(), chil);
			this.dealSaveSynchronizationDeptChildrenInfo(chil,chil.getChildrenList(),existDeptMap,uniqueNumber,record);
		}
	}

	private void dealSaveSynchronizationOrgChildrenInfo(TblOrganization com, List<TblOrganization> childrenList,
			Map<String, TblOrganization> existCompanyInfoMap, String uniqueNumber, TblSynchronizationRecord record) throws Exception {
		for (TblOrganization chil : childrenList) {
			chil.setOrgtype(com.getOrgtype()+1);
			chil.setFatherorgid(com.getOrgid());
			chil.setUniqueNumber(uniqueNumber);
			chil.setOrgcreate(new Date());
			if(existCompanyInfoMap != null && existCompanyInfoMap.containsKey(chil.getOrgnumber())) {
				chil.setOrgid(existCompanyInfoMap.get(chil.getOrgnumber()).getOrgid());
				chil.setOrganizationTrees(com.getOrgid()+","+chil.getOrgid());
				this.tblOrganizationMapper.updateById(chil);
				if(YMUrlStatic.status == 0) {
					this.ymBusinessService.dealUniqueOrgInfo(chil,YMUrlStatic.ORGUPDATE);
				}
			}else {
				chil.setOrgid(RandomUtil.uuBigDecimalId());
				chil.setOrganizationTrees(com.getOrgid()+","+chil.getOrgid());
				this.tblOrganizationMapper.insert(chil);
				if(YMUrlStatic.status == 0) {
					this.ymBusinessService.dealUniqueOrgInfo(chil,YMUrlStatic.ORGINSERT);
				}
			}
			record.setResulttext(record.getResulttext()+"\n处理组织数据"+chil.toString());
			existCompanyInfoMap.put(chil.getOrgnumber(), chil);
			this.dealSaveSynchronizationOrgChildrenInfo(chil,chil.getChildrenList(),existCompanyInfoMap,uniqueNumber,record);
		}
	}


    private TblOrganization parseOrgs(List<Map<String, String>> list,TblSynchronizationRecord record) throws Exception {
    	List<TblOrganization> companyList = new ArrayList<TblOrganization>();
        TblOrganization company = null;
        TblOrganization rootCompany = null;
        String statustext = "";
        
        String orgNumber = "";
        String fatherOrgNumber = "";
        
        Map<String, String> ortMap = null;
        //"ORG_SERIAL_NUMBER" 排序
        for (int i = 0; i < list.size(); i++) {
        	ortMap = list.get(i);
            orgNumber = ortMap.get("ORG_CODE");
            fatherOrgNumber =  ortMap.get("PARENT_ORG_CODE");
            
            company = new TblOrganization();
            company.setOrgname(ortMap.get("ORG_NAME"));
            company.setOrgmeno(company.getOrgname());
            statustext =  ortMap.get("ORG_STATUS");
            company.setStatus("Y".equals(statustext)?0:1);
            company.setDatasource("zhzsj");
            company.setUseSecrect(1);
            company.setWrittenByDept("核");
            
            if(org.apache.commons.lang.StringUtils.isBlank(orgNumber)) {
            	record.setResulttext(record.getResulttext()+"\n 公司编码为空："+company.toString());
            	continue;
            }
            if(org.apache.commons.lang.StringUtils.isBlank(fatherOrgNumber)) {
            	fatherOrgNumber = "-1";
            	record.setResulttext(record.getResulttext()+"\n 公司父级编码为空："+company.toString());
            }
            
            company.setOrgnumber(orgNumber.trim());
            company.setFahterOrgName(fatherOrgNumber.trim());
            
            if("1000".equals(company.getOrgnumber())) {
            	rootCompany = company;
            	rootCompany.setOrgtype(1);
            	rootCompany.setFatherorgid(BigDecimal.valueOf(-1));
            	continue;
            }
            companyList.add(company);
        }
        String rootCompanyNumber = rootCompany.getOrgnumber();
        String rootParent = rootCompany.getFahterOrgName();
        rootCompany.setChildrenList(companyList.stream().filter(com -> rootCompanyNumber.equals(com.getFahterOrgName()) || StringUtils.isEmpty(com.getFahterOrgName()) || rootParent.equals(com.getFahterOrgName())).collect(Collectors.toList()));
        mainChildrenFatherRelation(rootCompany.getChildrenList(),companyList);
        return rootCompany;
    }
    
    private List<TblOrganization> paseOrgsForIncrement(List<Map<String, String>> list,
			Map<String, TblOrganization> existCompanyInfoMap,TblSynchronizationRecord record) {
       
    	List<TblOrganization> rootOrgList = new ArrayList<TblOrganization>(0);
        List<TblOrganization> chilList = new ArrayList<TblOrganization>(0);
        TblOrganization company = null;
        TblOrganization pcom = null;
        String statustext = "";
        
        List<TblOrganization> companyList = new ArrayList<TblOrganization>(0);
        Map<String,TblOrganization> syncOrgMap = new HashMap<String,TblOrganization>(0);
        String orgNumber = "";
        String fatherOrgNumber = "";
        
        Map<String, String> orgMap = null;
        for (int i = 0; i < list.size(); i++) {
        	orgMap = list.get(i);
            
            orgNumber = orgMap.get("ORG_CODE");
            fatherOrgNumber =  orgMap.get("PARENT_ORG_CODE");
            company = new TblOrganization();
            company.setOrgname( orgMap.get("ORG_NAME"));
            company.setOrgmeno(company.getOrgname());
            statustext =  orgMap.get("ORG_STATUS");
            company.setStatus("Y".equals(statustext)?0:1);
            company.setDatasource("zhzsj");
            company.setUseSecrect(1);
            company.setWrittenByDept("核");


			if(org.apache.commons.lang.StringUtils.isBlank(orgNumber)) {
            	record.setResulttext(record.getResulttext()+"\n 公司编码为空："+company.toString());
            	continue;
            }
            if(org.apache.commons.lang.StringUtils.isBlank(fatherOrgNumber)) {
            	record.setResulttext(record.getResulttext()+"\n 公司父级编码为空："+company.toString());
            	continue;
            }
            
            company.setOrgnumber(orgNumber.trim());
            company.setFahterOrgName(fatherOrgNumber.trim());
			companyList.add(company);
            syncOrgMap.put(company.getOrgnumber(), company);
        }
        
        
        //判断生成子级和父级
        for (TblOrganization com : companyList) {
			if(syncOrgMap.containsKey(com.getFahterOrgName())) {
				chilList.add(com);
			}else {
				pcom = existCompanyInfoMap.get(com.getFahterOrgName());
				if(pcom == null) {
					record.setResulttext(record.getResulttext() + "父级公司不存在，跳过：" + com.toString());
					System.out.println("父级公司不存在，跳过：" + com.toString());
					continue;   // ← 跳过该条，不影响其他数据继续处理
				}
				com.setOrgtype(pcom.getOrgtype()+1);
				com.setOrgTreeNames(pcom.getOrganizationTrees());
				com.setFatherorgid(pcom.getOrgid());
            	rootOrgList.add(com);
			}
		}
        
        
        if(rootOrgList != null && rootOrgList.size() != 0) {
        	for (TblOrganization root : rootOrgList) {
        		root.setChildrenList(chilList.stream().filter(com->root.getOrgnumber().equals(com.getFahterOrgName())).collect(Collectors.toList()));
        		mainChildrenFatherRelation(root.getChildrenList(),chilList);
    		}
    	}
        
		return rootOrgList;
	}
    

	private List<TblOrganization> parseDepts(List<Map<String, String>> list, Map<String, TblOrganization> existCompanyInfoMap,TblSynchronizationRecord record) throws Exception {
		List<TblOrganization> rootDepts = new ArrayList<TblOrganization>();
       
		
        List<TblOrganization> chilDepts = new ArrayList<TblOrganization>();
        TblOrganization dept = null;
        
        String orgNumber = "";
        String fatherOrgNumber = "";
        
        List<String> rootDeptsNumber = new ArrayList<String>(0);
        
        Map<String, String> deptMap = null;
        for (int i = 0; i < list.size(); i++) {
        	deptMap = list.get(i);
            orgNumber = deptMap.get("DEPT_CODE");
        	fatherOrgNumber = deptMap.get("PARENT_DEPT_CODE");
            dept = new TblOrganization();
            dept.setOrgname(deptMap.get("DEPT_NAME"));
            dept.setStatus("Y".equals(deptMap.get("DEPT_STATUS"))?0:1);
            dept.setDatasource("zhzsj");
            dept.setWrittenByDept("核");
            dept.setOrgtype(0);
            
            if(org.apache.commons.lang.StringUtils.isBlank(orgNumber)) {
            	record.setResulttext(record.getResulttext()+"\n 公司编码为空："+dept.toString());
            	continue;
            }
            if(org.apache.commons.lang.StringUtils.isBlank(fatherOrgNumber)) {
            	record.setResulttext(record.getResulttext()+"\n 公司父级编码为空："+dept.toString());
            	continue;
            }
            
            dept.setOrgnumber(orgNumber.trim());
            dept.setFahterOrgName(fatherOrgNumber.trim());
            
            
            if(existCompanyInfoMap.containsKey(dept.getFahterOrgName()) && !rootDeptsNumber.contains(dept.getFahterOrgName()) ) {
            	
            	rootDeptsNumber.add(dept.getOrgnumber());
            	rootDepts.add(dept);
            }else {
            	chilDepts.add(dept);
            }
        }
        mainChildrenFatherRelation(rootDepts,chilDepts);
        return rootDepts;
    }
	
	private List<TblOrganization> parseDeptsForIncrement(List<Map<String, String>> list, Map<String, TblOrganization> existOrgMap, TblSynchronizationRecord record) {
		List<TblOrganization> rootDepts = new ArrayList<TblOrganization>(0);
        
        List<TblOrganization> chilList = new ArrayList<TblOrganization>(0);
        TblOrganization pcom = null;
        
        List<TblOrganization> syncDeptList = new ArrayList<TblOrganization>(0);
        Map<String,TblOrganization> syncDeptMap = new HashMap<String,TblOrganization>(0);
        
        
        TblOrganization dept = null;
        String orgNumber = "";
        String fatherOrgNumber = "";
        Map<String, String> deptMap = null;
        for (int i = 0; i < list.size(); i++) {
        	deptMap = list.get(i);
            orgNumber = deptMap.get("DEPT_CODE");
        	fatherOrgNumber = deptMap.get("PARENT_DEPT_CODE");
            dept = new TblOrganization();
            dept.setOrgname(deptMap.get("DEPT_NAME"));
            dept.setStatus("Y".equals(deptMap.get("DEPT_STATUS"))?0:1);
            dept.setDatasource("zhzsj");
            dept.setWrittenByDept("核");
            dept.setOrgtype(0);
            
            if(org.apache.commons.lang.StringUtils.isBlank(orgNumber)) {
            	record.setResulttext(record.getResulttext()+"\n 公司编码为空："+dept.toString());
            	continue;
            }
            if(org.apache.commons.lang.StringUtils.isBlank(fatherOrgNumber)) {
            	record.setResulttext(record.getResulttext()+"\n 公司父级编码为空："+dept.toString());
            	continue;
            }
            
            dept.setOrgnumber(orgNumber.trim());
            dept.setFahterOrgName(fatherOrgNumber.trim());
            
            syncDeptList.add(dept);
            syncDeptMap.put(dept.getOrgnumber(), dept);
        }
        
        for (TblOrganization de : syncDeptList) {
			if(syncDeptMap.containsKey(de.getFahterOrgName())) {
				chilList.add(de);
			}else {
				pcom = existOrgMap.get(de.getFahterOrgName());
				if(pcom == null) {
					continue;
				}
				de.setOrgTreeNames(pcom.getOrganizationTrees());
            	de.setUniqueNumber(pcom.getUniqueNumber());
            	de.setFatherorgid(pcom.getOrgid());
            	rootDepts.add(de);
			}
		}
        mainChildrenFatherRelation(rootDepts,chilList);
        return rootDepts;
	}

	 private static void mainChildrenFatherRelation(List<TblOrganization> childrenList,
				List<TblOrganization> companyList) {
	    	if(childrenList == null || childrenList.size() == 0) {
	    		return;
	    	}
	    	for (TblOrganization chil : childrenList) {
	    		chil.setChildrenList(companyList.stream().filter(com->chil.getOrgnumber().equals(com.getFahterOrgName())).collect(Collectors.toList()));
	    		mainChildrenFatherRelation(chil.getChildrenList(),companyList);
			}
		}
	
		private static Document parseXml(String xml) throws Exception {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setNamespaceAware(true); // 启用命名空间支持
	        factory.setIgnoringElementContentWhitespace(true); // 忽略空白
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        return builder.parse(new InputSource(new StringReader(xml)));
	    }

	    private static Element getBatchElement(Document doc) {
	        // 使用命名空间获取batch元素
	        NodeList returnList = doc.getElementsByTagNameNS(SYSWARE_NS, "return");
	        if (returnList.getLength() == 0) return null;
	        
	        Element returnElement = (Element) returnList.item(0);
	        NodeList returnDataList = returnElement.getElementsByTagName("returnData");
	        if (returnDataList.getLength() == 0) return null;
	        
	        Element returnData = (Element) returnDataList.item(0);
	        NodeList batchList = returnData.getElementsByTagName("batch");
	        return batchList.getLength() > 0 ? (Element) batchList.item(0) : null;
	    }
	    
	    
    private static String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            org.w3c.dom.Node node = nodeList.item(0);
            return node.getTextContent() != null ? node.getTextContent().trim() : null;
        }
        return null;
    }

	//更新部门信息
	@Override
	public void syncZhDept(String xmlData) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = builder.parse(new InputSource(new StringReader(xmlData)));
	        document.getDocumentElement().normalize();
	 
	        // 获取根元素
            Element root = document.getDocumentElement();
            
            String errorCode = root.getElementsByTagName("errorCode").item(0).getTextContent();
            String errorMsg = root.getElementsByTagName("errorMsg").item(0).getTextContent();
            
            if(errorCode!=null && "0".equals(errorCode)) {
            	//成功
            	//获取所有data元素
                NodeList dataList = root.getElementsByTagName("data");
                for (int i = 0; i < dataList.getLength(); i++) {
                    Element data = (Element) dataList.item(i);
//                    String category = data.getAttribute("category");
                    //上级部门主键
                    String PARENT_DEPT_PK = data.getElementsByTagName("PARENT_DEPT_PK").item(0).getTextContent();
                    //上级部门编号
                    String PARENT_DEPT_CODE = data.getElementsByTagName("PARENT_DEPT_CODE").item(0).getTextContent();
                    //所属组织编号
                    String BELONG_ORG_CODE = data.getElementsByTagName("BELONG_ORG_CODE").item(0).getTextContent();
                    //所属组织主键
                    String BELONG_ORG_PK = data.getElementsByTagName("BELONG_ORG_PK").item(0).getTextContent();
                    //编号
                    String DEPT_CODE = data.getElementsByTagName("DEPT_CODE").item(0).getTextContent();
                    //部门电话
                    String DEPT_TEL = data.getElementsByTagName("DEPT_TEL").item(0).getTextContent();
                    //负责人
                    String DEPT_PRINCIPAL = data.getElementsByTagName("DEPT_PRINCIPAL").item(0).getTextContent();
                    //部门简称
                    String DEPT_SHOTNAME = data.getElementsByTagName("DEPT_SHOTNAME").item(0).getTextContent();
                    //最后修改时间
                    String DEPT_MODIFY_TIME_NC = data.getElementsByTagName("DEPT_MODIFY_TIME_NC").item(0).getTextContent();
                    //地址
                    String DEPT_ADDR = data.getElementsByTagName("DEPT_ADDR").item(0).getTextContent();
                    //备注
                    String DEPT_MEMO = data.getElementsByTagName("DEPT_MEMO").item(0).getTextContent();
                    //名称
                    String DEPT_NAME = data.getElementsByTagName("DEPT_NAME").item(0).getTextContent();
                    //部门状态 1=未启用;2=已启用;3=已停用
                    String DEPT_STATUS = data.getElementsByTagName("DEPT_STATUS").item(0).getTextContent();
					//新增字段
					String DEPT_TYPE = data.getElementsByTagName("DEPT_TYPE").item(0).getTextContent();

					//保存组织信息，通过主键确定是新增or修改
                    List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(DEPT_CODE);
                    if (list.size() == 0) {
    					TblOrganization org = new TblOrganization();
    					if(!StringUtils.isEmpty(DEPT_CODE)){
    					org.setOrgname(DEPT_NAME);
    					org.setOrgmeno(DEPT_NAME);
    					org.setMemo(DEPT_SHOTNAME);
//    					//通过上级部门编号查询Fatherorgid
//    					List<TblOrganization> listParentDept = tblOrganizationMapper.getOrgByHistoryid(PARENT_DEPT_CODE);
//    					org.setFatherorgid();
    					if(null!=DEPT_STATUS && "2".equals(DEPT_STATUS)) {
    						org.setStatus(0);
    					}else {
    						org.setStatus(1);
    					}
    					org.setDatasource("zhzsj");//中核主数据
    					org.setOrgtype(0);
    					org.setHistorycode(DEPT_CODE); //存储历史ID
    					org.setHistorydepartmentid(PARENT_DEPT_CODE);
    					org.setOrgnumber(DEPT_CODE);

    					tblOrganizationMapper.saveAtionHangYe(org);
    					}
    				} else if (list.size() == 1) {
    					TblOrganization org = list.get(0);
    					org.setOrgname(DEPT_NAME);
    					org.setOrgmeno(DEPT_NAME);
    					org.setMemo(DEPT_SHOTNAME);
    					if(null!=DEPT_STATUS && "2".equals(DEPT_STATUS)) {
    						org.setStatus(0);
    					}else {
    						org.setStatus(1);
    					}
    					org.setDatasource("zhzsj");//中核主数据
    					org.setHistorycode(DEPT_CODE); //存储历史ID
    					org.setHistorydepartmentid(PARENT_DEPT_CODE);
    					org.setOrgnumber(DEPT_CODE);
    					tblOrganizationMapper.updateAtionHangYe(org);
    				}
                }
            }else {
            	//同步数据异常
            	System.out.println("部门同步异常: "+errorMsg);
            }
			System.out.println("部门同步成功: ");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	
	//获取excel内容
  	private List<Map<String,Object>> getExcel(String excelPath) throws Exception {
          File file = new File(excelPath);//("C:\\Users\\Lenovo\\Desktop\\数据导入模板及填写格式 (2).xlsx");
          if (!file.exists()){
              throw new Exception("文件不存在!");
          }
          InputStream in = new FileInputStream(file);
          
          List<Map<String,Object>> listExcel = new ArrayList<Map<String,Object>>();
   
          // 读取整个Excel
          XSSFWorkbook sheets = new XSSFWorkbook(in);
          // 获取第一个表单Sheet
          XSSFSheet sheetAt = sheets.getSheetAt(0);
   
          //默认第一行为标题行，i = 0
          XSSFRow titleRow = sheetAt.getRow(0);
          // 循环获取每一行数据
          for (int i = 1; i < sheetAt.getPhysicalNumberOfRows(); i++) {
              XSSFRow row = sheetAt.getRow(i);
              // 读取每一格内容
              if(null != row) {
              	Map<String,Object> mapExcel = new HashMap<String, Object>();
                  for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                      XSSFCell titleCell = titleRow.getCell(index);
                      XSSFCell cell = row.getCell(index);
                      if(null == cell) {
                      	mapExcel.put(index+"", null);
                      }else {
                    	  cell.setCellType(CellType.STRING);
                    	  mapExcel.put(index+"", cell.getStringCellValue());
                      }
                  }
                  listExcel.add(mapExcel);
              }
          }
          return listExcel;
      }
	
	@Override
	public Map<String,Object> importOrg(String token,String excelPath) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            return resultMap;
        }
		try {
			//获取excel内容
			List<Map<String, Object>> listExcel = getExcel(excelPath);
			//保存公司基本信息
			saveOrgByExcel(listExcel,user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
  	
  	//保存公司基本信息（EXCEL导入）
  	private void saveOrgByExcel(List<Map<String, Object>> listExcel,TblStaffUtil user){
  		try {
  			Map<String, Object> mapExcel = null;
  			for (int i = 0; i < listExcel.size(); i++) {
  				mapExcel = listExcel.get(i);
  				
  				//没有编号则无法保存
  				String orgnumber = mapExcel.get("2").toString();
  				if(null==orgnumber || "".equals(orgnumber)){
  					continue;
  				}
  				
  				//组装公司的基本信息
  				String orgname = mapExcel.get("1").toString().trim();//公司名称
  				String fatherOrgnumber = mapExcel.get("3").toString().trim();//上级编号
  				String orgtype = mapExcel.get("4").toString().trim();//层级
  				
  				//查询编号是否已存在
  				Integer cnt = tblOrganizationMapper.selectCntByOrgNumber(orgnumber);
  				if(cnt>0) {
  					continue;
  				}
  				
  				TblOrganization org = new TblOrganization();
  				org.setOrgid(RandomUtil.uuBigDecimalId());
  				org.setOrgname(orgname);
  				org.setOrgnumber(orgnumber);
  				TblOrganization fatherOrg = tblOrganizationMapper.selectInfoByOrgNumber(fatherOrgnumber);
  				if("-1".equals(fatherOrgnumber)) {
  					org.setFatherorgid(new BigDecimal(-1));
  					org.setOrganizationTrees(org.getOrgid().toString());
  				}else {
  					org.setFatherorgid(fatherOrg.getOrgid());
  					if(null!=fatherOrg) {
  	  					org.setOrganizationTrees(fatherOrg.getOrganizationTrees()+","+org.getOrgid());
  	  				}
  				}
  				org.setOrgmeno(orgname);
  				org.setMemo(orgname);
  				org.setOrgtype(Integer.valueOf(orgtype));
  				org.setStatus(0);
  				org.setIsautonumber(1);
  				org.setDatasource("zh");
  				org.setWrittenByDept("核");
  				
                
                //保存公司信息
                this.addReturnId(org);;
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
  	
  	@Override
	public Map<String,Object> importDept(String token,String excelPath) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            return resultMap;
        }
		try {
			//获取excel内容
			List<Map<String, Object>> listExcel = getExcel(excelPath);
			//保存部门基本信息
			saveDeptByExcel(listExcel,user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
  	
  	//保存部门基本信息（EXCEL导入）
  	private void saveDeptByExcel(List<Map<String, Object>> listExcel,TblStaffUtil user){
  		try {
  			Map<String, Object> mapExcel = null;
  			for (int i = 0; i < listExcel.size(); i++) {
  				mapExcel = listExcel.get(i);
  				
  				//没有编号则无法保存
  				String orgnumber = mapExcel.get("2").toString();
  				if(null==orgnumber || "".equals(orgnumber)){
  					continue;
  				}
  				
  				//组装部门的基本信息
  				String orgname = mapExcel.get("1").toString().trim();//名称
  				String fatherOrgnumber = mapExcel.get("3").toString().trim();//所属公司名称
  				
  				//查询编号是否已存在
  				Integer cnt = tblOrganizationMapper.selectCntByOrgNumber(orgnumber);
  				if(cnt>0) {
  					continue;
  				}
  				
  				TblOrganization org = new TblOrganization();
  				org.setOrgname(orgname);
  				org.setOrgnumber(orgnumber);
  				TblOrganization fatherOrg = tblOrganizationMapper.selectInfoByOrgNumber(fatherOrgnumber);
  				org.setFatherorgid(fatherOrg.getOrgid());
  				org.setOrgmeno(orgname);
  				org.setMemo(orgname);
  				org.setOrgtype(0);
  				org.setStatus(0);
  				org.setIsautonumber(1);
  				org.setDatasource("zh");
  				org.setWrittenByDept("核");
  				org.setOrgid(RandomUtil.uuBigDecimalId());
                org.setOrganizationTrees(fatherOrg.getOrganizationTrees()+","+org.getOrgid());
                
                //保存部门信息
                this.addReturnId(org);
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}

  	
  	private static List<List<String>> getOrgListofLists(List<String> orgIdTreeList) {
		List<List<String>> listOfLists = new ArrayList<List<String>>(0);
		Integer level = 0;
		List<String> middleList = null;
		for (String str : orgIdTreeList) {
			middleList = Arrays.asList(str.split(","));
			if(middleList.size() > level) {
				level = middleList.size();
			}
			listOfLists.add(middleList);
		}
		
		List<List<String>> orgIdListLevel = new ArrayList<List<String>>(0);
		List<String> orgIdList = new ArrayList<String>(0);
		for (int i = 0 ; i < level ; i++) {
			orgIdList = new ArrayList<String>(0);
			for (int j = 0 ; j < listOfLists.size() ; j++) {
				if(!isIndexOutOfBounds(listOfLists.get(j),i) && !orgIdList.contains(listOfLists.get(j).get(i))) {
					orgIdList.add(listOfLists.get(j).get(i));
				}
			}
			if(orgIdList.size() > 0) {
				orgIdListLevel.add(orgIdList);
			}
		}
		return orgIdListLevel;
	}
  	
	private List<TblOrganizationResult> getOrgTreeListByListofList(List<List<String>> orgIdListLevel) throws Exception {
		//按照从父到子的逻辑顺序查询出公司列表集合
		List<List<TblOrganizationResult>> orgListorgLists = new ArrayList<List<TblOrganizationResult>>(0);
		List<TblOrganizationResult> orgList = null;
		for (List<String> enList : orgIdListLevel) {
			orgList = this.tblOrganizationResultMapper.selectOrgListByIds(enList);
			orgListorgLists.add(orgList);
		}
		
		//实现子父级关系映射
		orgList = new ArrayList<TblOrganizationResult>(0);
		if(orgListorgLists.size() > 1) {
			for (int i = orgListorgLists.size() - 2; i >= 0; i--) {
				for(TblOrganizationResult enOrg : orgListorgLists.get(i)) {
					enOrg.setChildren(orgListorgLists.get(i+1).stream().filter(obj -> enOrg.getOrgid().toString().equals(obj.getFatherorgid().toString())).collect(Collectors.toList()));
					if(i == 0) {
						orgList.add(enOrg);
					}
				}
			}
		}else {
			for (int i = 0; i < orgListorgLists.size(); i++) {
				for(TblOrganizationResult enOrg : orgListorgLists.get(i)) {
					orgList.add(enOrg);
				}
			}
		}
		return orgList;
	}
	
	private List<Tree> getTreesOrgByList(List<TblOrganizationResult> orgList) {
		List<Tree> trees = new ArrayList<Tree>();
		Tree tree = null;
		for (TblOrganizationResult org : orgList) {
			tree = new Tree();
			tree.setName(org.getOrgname());
			tree.setId(org.getOrgid());
			tree.setpId(org.getFatherorgid());
			tree.setOpen(org.getChildren()!=null&&org.getChildren().size()>0);
			tree.setIsParent(org.getChildren()!=null&&org.getChildren().size()>0);
			tree.setType(org.getOrgtype().toString());
			tree.setBgimage(org.getBgimage());
			tree.setBgname(org.getBgname());
			tree.setCtztimage(org.getCtztimage());
			tree.setCtztname(org.getCtztname());
			tree.setJdztimage(org.getJdztimage());
			tree.setJdztname(org.getJdztname());
			tree.setLogoimage(org.getLogoimage());
			tree.setLogoname(org.getLogoname());
			tree.setBaname(org.getBaname());
			tree.setChildren(org.getChildren()!=null&&org.getChildren().size()>0?getTreesOrgByList(org.getChildren()):null);
			trees.add(tree);
		}
		return trees;
	}

  	/**
  	 * 校验数组是否越界
  	 * @param list
  	 * @param index
  	 * @return
  	 */
	public static boolean isIndexOutOfBounds(List<?> list, int index) {
	    return index < 0 || index >= list.size();
	}

	@Override
	public String getRootOrganizationTree(BigDecimal nodeId, String type) throws Exception {
		String targetIdString = nodeId.toString();
		//查询所有公司信息
		QueryWrapper<TblOrganization> wrapper = new QueryWrapper<TblOrganization>();
		wrapper.between("ORGTYPE", 1, 100);
		wrapper.eq("STATUS", 0);
		wrapper.select("ORGID,ORGNAME,FATHERORGID,ORGTYPE,STATUS,ORDERID,ORGANIZATIONTREES");
		wrapper.orderByAsc("ORGTYPE,ORDERID");
		List<TblOrganization> orgList = this.tblOrganizationMapper.selectList(wrapper);
		
		if(orgList == null || orgList.size() == 0) {
			return null;
		}
		
		//过滤出当前用户所属的根公司rootOrg  和 所有的子集集合childrenList；
		
		List<TblOrganization> rootOrgList = orgList.stream().filter(item -> targetIdString.equals(item.getOrgid().toString())).collect(Collectors.toList());
		if(rootOrgList == null || rootOrgList.size() == 0) {
			return null;
		}
		
		TblOrganization rootOrg = rootOrgList.get(0);
		
		List<TblOrganization> childrenList = orgList.stream()
				.filter(item -> !targetIdString.equals(item.getOrgid().toString()))
				.filter(item -> org.apache.commons.lang.StringUtils.isNotBlank(item.getOrganizationTrees()) && (item.getOrganizationTrees().equals(targetIdString) || 
						item.getOrganizationTrees().startsWith(targetIdString + ",") ||
						item.getOrganizationTrees().contains("," + targetIdString + ",") ||
						item.getOrganizationTrees().endsWith("," + targetIdString)))
				.collect(Collectors.toList());
		
		Map<BigDecimal, TblOrganization> orgMap = childrenList.stream()
			        .collect(Collectors.toMap(TblOrganization::getOrgid, Function.identity()));
		orgMap.put(rootOrg.getOrgid(), rootOrg);
			    
		 //初始化子集集合
		orgMap.values().forEach(org -> {
			if (org.getChildrenList() == null) {
				org.setChildrenList(new ArrayList<TblOrganization>(0));
		    }
		});
		
		childrenList.forEach(org -> {
			TblOrganization parent = orgMap.get(org.getFatherorgid());
		    if (parent != null) {
		    	parent.getChildrenList().add(org);
		    }
		});
		 
		rootOrg.setChildrenList(childrenList.stream()
				.filter(org -> org.getFatherorgid().toString().equals(rootOrg.getOrgid().toString()))
			    .collect(Collectors.toList()));

		
		//生成tree数结构
		List<Tree> rootTreeList = new ArrayList<Tree>(0);
		Tree rootTree = new Tree();
		
		rootTree.setName(rootOrg.getOrgname());
		rootTree.setId(rootOrg.getOrgid());
		rootTree.setpId(rootOrg.getFatherorgid());
		this.setTreeChildrenList(rootTree,rootOrg);
		
		rootTreeList.add(rootTree);
		return com.alibaba.fastjson.JSONObject.toJSONString(rootTreeList);
	}

	@Override
	public String getRootOrganizationTreeA(BigDecimal nodeId, String type,TblStaffUtil staff) throws Exception {
		String targetIdString = nodeId.toString();
		//获取角色ID
		String roleIdStrs = staff.getRoleIdStrs();
		List<BigDecimal> roleIdList = new ArrayList<>();
		if (roleIdStrs != null && !roleIdStrs.trim().isEmpty()) {
    		String[] idArr = roleIdStrs.split(",");
    		for (String idStr : idArr) {
        		String trimId = idStr.trim();
        		if (!trimId.isEmpty()) {
            		roleIdList.add(new BigDecimal(trimId));
        		}
    		}
		}
		
		//根据角色id查询有权限的公司和部门
		QueryWrapper<TblDateRightInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("ROLEID",roleIdList);
		List<TblDateRightInfo> tblDateRightList = tblDateRightInfoMapper.selectList(queryWrapper);
		List<BigDecimal> rightOrgIdList = tblDateRightList.stream()
			.map(TblDateRightInfo::getOrgId)
        	.filter(Objects::nonNull)
        	.collect(Collectors.toList());
		QueryWrapper<TblOrganization> qWrapper = new QueryWrapper<>();
		qWrapper.in("ORGID",rightOrgIdList);
		List<TblOrganization> orgList = tblOrganizationMapper.selectList(qWrapper);
		//查询所有公司信息
		// List<TblOrganization> orgList = this.tblOrganizationMapper.selectOrgTreeBySystemRight();

		if(orgList == null || orgList.size() == 0) {
			return null;
		}
		//过滤出当前用户所属的根公司rootOrg  和 所有的子集集合childrenList；

		List<TblOrganization> rootOrgList = orgList.stream().filter(item -> targetIdString.equals(item.getOrgid().toString())).collect(Collectors.toList());
		if(rootOrgList == null || rootOrgList.size() == 0) {
			return null;
		}

		TblOrganization rootOrg = rootOrgList.get(0);

		List<TblOrganization> childrenList = orgList.stream()
				.filter(item -> !targetIdString.equals(item.getOrgid().toString()))
				.filter(item -> org.apache.commons.lang.StringUtils.isNotBlank(item.getOrganizationTrees()) && (item.getOrganizationTrees().equals(targetIdString) ||
						item.getOrganizationTrees().startsWith(targetIdString + ",") ||
						item.getOrganizationTrees().contains("," + targetIdString + ",") ||
						item.getOrganizationTrees().endsWith("," + targetIdString)))
				.collect(Collectors.toList());

		Map<BigDecimal, TblOrganization> orgMap = childrenList.stream()
				.collect(Collectors.toMap(TblOrganization::getOrgid, Function.identity()));
		orgMap.put(rootOrg.getOrgid(), rootOrg);

		//初始化子集集合
		orgMap.values().forEach(org -> {
			if (org.getChildrenList() == null) {
				org.setChildrenList(new ArrayList<TblOrganization>(0));
			}
		});

		childrenList.forEach(org -> {
			TblOrganization parent = orgMap.get(org.getFatherorgid());
			if (parent != null) {
				parent.getChildrenList().add(org);
			}
		});

		rootOrg.setChildrenList(childrenList.stream()
				.filter(org -> org.getFatherorgid().toString().equals(rootOrg.getOrgid().toString()))
				.collect(Collectors.toList()));


		//生成tree数结构
		List<Tree> rootTreeList = new ArrayList<Tree>(0);
		Tree rootTree = new Tree();

		rootTree.setName(rootOrg.getOrgname());
		rootTree.setId(rootOrg.getOrgid());
		rootTree.setpId(rootOrg.getFatherorgid());
		this.setTreeChildrenList(rootTree,rootOrg);

		rootTreeList.add(rootTree);
		return com.alibaba.fastjson.JSONObject.toJSONString(rootTreeList);
	}

	private void setTreeChildrenList(Tree tree, TblOrganization org) {
		if(org.getChildrenList() == null || org.getChildrenList().size() == 0) {
			tree.setOpen(false);
			tree.setIsParent(false);
			tree.setChildren(null);
			return;
		}
		List<Tree> chilTreeList = new ArrayList<Tree>(0);
		Tree chilTree = null;
		for (TblOrganization chilOrg : org.getChildrenList()) {
			chilTree = new Tree();
			chilTree.setName(chilOrg.getOrgname());
			chilTree.setId(chilOrg.getOrgid());
			chilTree.setpId(chilOrg.getFatherorgid());
			this.setTreeChildrenList(chilTree,chilOrg);
			chilTreeList.add(chilTree);
		}
		tree.setOpen(true);
		tree.setIsParent(true);
		tree.setChildren(chilTreeList);
	}

	@Override
	public List<String> findNameByOrgIds(String orgids) throws Exception {
		List<String> nameList = this.tblOrganizationMapper.selectNameListByOrgIds(orgids);
		return nameList;
	}

}
