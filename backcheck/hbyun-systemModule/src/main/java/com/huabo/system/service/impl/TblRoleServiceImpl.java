package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblRoleDao;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblRoleService;
import com.huabo.system.service.YMBusinessService;

import lombok.extern.slf4j.Slf4j;

@Service("TblRoleService")
@Slf4j
public class TblRoleServiceImpl implements TblRoleService {
    @Resource
    private TblRoleDao tblRoleDao;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private TblStaffMapper tblStaffMapper;
    
    @Resource
    private YMBusinessService ymBusinessService;
    
    @Resource
    private TblOrganizaService tblOrganizaService;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public Map<String, Object> findTblRoleAll(TblRole role, Integer pageNumber, Integer pageSize, String token,String orgIds, String roleName, String orgName) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            PageInfo<TblRole> pageInfo = new PageInfo<TblRole>();
            TblStaffUtil staff = userProvider.get();
            BigDecimal companyid = staff.getCurrentOrg().getOrgid();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<TblRole> page = new Page<TblRole>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
//            IPage<TblRole> pageList = tblRoleDao.selectListByPageInfo(page, companyid, role,orgIds,roleName,orgName);
//            List<TblRole> roleList = pageList.getRecords();
            com.github.pagehelper.Page<TblRole> page1 = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()->tblRoleDao.selectListByPageInfo(page, companyid, role,orgIds,roleName,orgName));
          
            
         //查询当前角色在哪些部门下面，为提升效率先注释，后续需要在进行放开
           /* List<String> orgTreeIds = null;
            List<String> orgNames = null;
            List<String> orgTreeNames = new ArrayList<String>(0);
            for (TblRole rr : roleList) {
            	orgTreeIds = this.tblRoleDao.selectOrgNamesByRoleId(rr.getRoleId());
            	for (String ids : orgTreeIds) {
            		orgNames = this.tblOrganizationMapper.selectTreeNamesByOrgTreeId(ids);
            		if(orgNames != null && orgNames.size() > 0) {
            			orgTreeNames.add(String.join(",", orgNames));
            		}
				}
            	rr.setOrgTreeName(orgTreeNames);
			}*/
            
             
            pageInfo.setTlist(page1.getResult());
            pageInfo.setTotalRecord((int) page1.getTotal());
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    
    
    
    @Override
    public List<TblRole> findAll(BigDecimal companyid) {
        return this.tblRoleDao.findBysql(companyid);
    }

    @Override
    public void save(TblRole tr) {
        this.tblRoleDao.saveTblRole(tr);
    }

    @Override
    public TblRole findByid(String rid) {
        List<TblRole> list = this.tblRoleDao.findByRid(rid);
        return list != null && list.size() > 0 ? (TblRole) list.get(0) : null;
    }

    @Override
    public List<TblStaff> isSY(String rid) {
        return this.tblRoleDao.findBysqlobj(rid);
    }

    @Override
    public void update(TblRole role) {
        tblRoleDao.updateTblRole(role);
    }

    @Override
    public void delete(TblRole role) {
        tblRoleDao.deleteRole(role.getRid());
    }


    @Override
    public void deleteByRid(Integer rid) {
    	tblRoleDao.deleteByRid(rid);
    }

    @Override
    public Boolean findIsByJs(String jsmc, TblStaff staff) {
        // TODO Auto-generated method stub
        return null;

    }

	@Override
	public void inertOrgandRole(String orgid, String roleid, BigDecimal rootId) throws Exception {
		tblRoleDao.inertOrgandRole(orgid, roleid,rootId);
	}

	@Override
    public void delOrgandRole(String orgid, String roleid) {
    	tblRoleDao.delOrgandRole(orgid, roleid);
    }
	
	@Override
	public Integer selectReaprtRoleName(String rname, BigDecimal rid, BigDecimal pid) throws Exception {
		return this.tblRoleDao.selectReaprtRoleName(rname,rid,pid);
	}

	@Override
	public Map<String, Object> roleDetail(String token, BigDecimal roleId) throws Exception {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
	        try {
	            TblStaffUtil staff = userProvider.get();
	            if(staff == null) {
	            	resultMap.put("code", "0");
		            resultMap.put("msg", "用户登录失效");
	            	return resultMap;
	            }

	            TblRole role = this.tblRoleDao.selectById(roleId);
	            if(role == null) {
	            	resultMap.put("code", "0");
	            	resultMap.put("msg", "角色不存在");
	            	return resultMap;
	            }

	            List<String> orgTreeIds = null;
	            List<String> orgNames = null;
	            List<String> orgTreeNames = new ArrayList<String>(0);


	            orgTreeIds = this.tblRoleDao.selectOrgNamesByRoleId(role.getRoleId());
	            for (String ids : orgTreeIds) {
	            	orgNames = this.tblOrganizationMapper.selectTreeNamesByOrgTreeId(ids);
	            	if(orgNames != null && orgNames.size() > 0) {
	            		orgTreeNames.add(String.join(",", orgNames));
	            	}
				}
	            role.setOrgTreeName(orgTreeNames);
	            List<String> orgIds = this.tblRoleDao.selectOrgRoleIds(role.getRoleId());
	            if(orgIds != null && orgIds.size() > 0) {
	            	role.setOrgIdStrs(String.join(",", orgIds));
	            }

	            resultMap.put("code", "1");
	            resultMap.put("msg", "访问接口成功");
	            resultMap.put("data", role);
	        } catch (Exception e) {
	            e.printStackTrace();
	            resultMap.put("code", "0");
	            resultMap.put("msg", "获取角色详情失败: " + e.getMessage());
	        }
	        return resultMap;
	}

	@Override
	public void grantToUser(String staffids, String roleid) throws Exception {
		String[] orgidlist=staffids.split(",");
		/*String[] roleids = roleid.split(",");
		String[] iscanpres = iscanpre.split(",");
		TblUserRolerelation rela = null;*/
		//存储用户角色关系表用于查看系统数据
		/*for (int i = 0; i < roleids.length; i++) {
			rela = new TblUserRolerelation();
			rela.setRelaid(RandomUtil.uuBigDecimalId());
			rela.setCreatestaffid(staff.getStaffid());
			rela.setCreatestaffname(staff.getRealname());
			rela.setCreatetime(new Date());
			rela.setIscanpre(Integer.parseInt(iscanpres[i]));
			rela.setRoleid(new BigDecimal(roleids[i]));
			rela.setStaffid(new BigDecimal(id));
			rela.setStatus(1);
			this.tblUserRolerelationService.saveEntity(rela);
		}*/
		for (String id : orgidlist) {
			TblStaff user = tblStaffMapper.selectPid(id);
			String roles=user.getRoleIdStrs();
			//判断用户是否拥有当前角色 如果有则跳过本次循环
			if(user.getRoleIdStrs()!=null && user.getRoleIdStrs().length()>0 && (","+roles+",").contains(","+roleid+",")) {
				continue;
			}
			if(user.getRoleIdStrs()!=null && user.getRoleIdStrs().length()>0) {
				roles=user.getRoleIdStrs()+","+roleid;
			}else {
				roles=roleid;
			}
			tblStaffMapper.updateuserRole(id,roles);
		}
		if(YMUrlStatic.status == 0){
			//修改信息同步至业务中台
        	this.ymBusinessService.dealUserRoleRelationUniqueRight(roleid,staffids);
        }
	}

	@Override
	public void unGrantFromUser(String staffids, String roleid) throws Exception {
		String[] orgidlist=staffids.split(",");
		String roleidStr = ","+roleid+",";
		TblStaff user = null;
		String roles = null;
		for (String id : orgidlist) {
			user = tblStaffMapper.selectPid(id);
			roles = user.getRoleIdStrs();
			if(user.getRoleIdStrs()!=null) {
				roles=","+user.getRoleIdStrs()+",";
				roles=roles.replaceAll(roleidStr, ",");
				if(roles.length() == 1) {
					roles = "";
				}else {
					roles=roles.substring(1, roles.lastIndexOf(","));
				}
				tblStaffMapper.updateuserRole(id,roles);
			}
		}
		//this.tblUserRolerelationService.setEndRoleDateByStaffIds(roleid,staffids);
		if(YMUrlStatic.status == 0){
        	this.ymBusinessService.dealUserRoleRelationUniqueRight(roleid,staffids);
        }
	}

	@Override
	public void grantToOrg(String orgids, String roleid) throws Exception {
		String[] orgidlist=orgids.split(",");
		TblOrganization orgInfo = null;
		BigDecimal rootId = null;
		for (String orgid : orgidlist) {
			//获取部门的公司节点
			orgInfo = this.tblOrganizaService.findByid(orgid);
			if(orgInfo.getOrgtype() == 0) {
				orgInfo = this.tblOrganizaService.findCompanyInfoByDeptId(orgInfo.getOrgid());
			}
			rootId = orgInfo.getOrgid();
			tblRoleDao.inertOrgandRole(orgid, roleid,rootId);
		}
	}

	@Override
	public void unGrantFromOrg(String orgids, String roleid) throws Exception {
		String[] orgidlist=orgids.split(",");
		for (String orgid : orgidlist) {
			tblRoleDao.delOrgandRole(orgid, roleid);
		}
	}
	
	
	
}
