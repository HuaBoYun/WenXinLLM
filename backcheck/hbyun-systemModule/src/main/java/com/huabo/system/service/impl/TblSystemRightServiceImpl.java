package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblAuthorizationRecord;
import com.huabo.system.entity.TblBiReportMenu;
import com.huabo.system.entity.TblDateRightInfo;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemOrgRight;
import com.huabo.system.entity.TblSystemRight;
import com.huabo.system.mapper.TblAuthorizationRecordMapper;
import com.huabo.system.mapper.TblBiReportMenuMapper;
import com.huabo.system.mapper.TblDateRightInfoMapper;
import com.huabo.system.mapper.TblManageRightDAO;
import com.huabo.system.mapper.TblManageUserRightMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblRoleMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSystemOrgRightMapper;
import com.huabo.system.mapper.TblSystemRightMapper;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblSystemRightService;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

@Service("TblSystemRightService")
public class TblSystemRightServiceImpl implements TblSystemRightService {
    @Resource
    private TblManageRightDAO tblManageRightDAO;

    @Resource
    private TblManageUserRightMapper tblManageUserRightMapper;

    @Resource
    private TblSystemRightMapper tblSystemRightMapper;

    @Resource
    private TblSystemOrgRightMapper tblSystemOrgRightMapper;

    @Resource
    private TblStaffMapper tblStaffMapper;

    @Resource
    private TblDateRightInfoMapper tblDateRightInfoMapper;
    
    @Resource
    private TblRoleMapper tblRoleMapper;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private TblOrganizaService tblOrganizaService;
    
    @Resource
    private TblBiReportMenuMapper tblBiReportMenuMapper;
    
    @Resource
    private TblAuthorizationRecordMapper tblAuthorizationRecordMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public JsonBean saveManageRight(TblSystemRight right, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //权限主键为空 新增，不为空 修改
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if(SystemStaticValue.REQUIREMENTVALIDATE) {
        	TblAuthorizationRecord confirm = null;
        	String memo = "";
        	TblSystemRight parentRight = this.tblSystemRightMapper.selectById(right.getParent());
        	
        	if(right.getType() == 0) {
        		memo += "菜单类型：目录；";
        	}else if(right.getType() == 1) {
        		memo += "菜单类型：页面；";
        	}else {
        		memo += "菜单类型：按钮；";
        	}
        	
        	if (right.getId() != null) {
        		TblSystemRight preRight = this.tblSystemRightMapper.selectById(right.getId());
        		
        		//判断是否有正在审批的数据
            	confirm = this.tblAuthorizationRecordMapper.selectSpzRecordInfoByTargetId(right.getId().toString());
            	if(!preRight.getName().equals(right.getName())) {
            		memo = "修改菜单名称："+preRight.getName()+"——>"+right.getName()+"；"+memo;
            	}
            	if(preRight.getParent().compareTo(right.getParent()) != 0) {
            		memo += "上级菜单修改："+parentRight.getName()+"；"+memo;
            	}
            	if(confirm != null) {
            		//修改审批中的确认数据
            		confirm.setModifiedTime(new Date());
            		confirm.setModifier(loginStaff.getStaffid());
            		confirm.setModifyerName(loginStaff.getRealname());
            		confirm.setOperationData(JSONObject.toJSONString(right));
            		confirm.setOperationMemo("修改菜单信息，"+memo);
            		confirm.setRecordText("修改菜单"+right.getName());
            		this.tblAuthorizationRecordMapper.updateById(confirm);
            	}else {
            		//新增确认记录需要发起流程
            		confirm = new TblAuthorizationRecord();
            		confirm.setRecordId(RandomUtil.uuStringId());
            		confirm.setCreationTime(new Date());
            		confirm.setCreator(loginStaff.getStaffid());
            		confirm.setCreatorName(loginStaff.getRealname());
               	 	confirm.setOperationData(JSONObject.toJSONString(right));
               	 	confirm.setOperationMemo("修改菜单信息，"+memo);
               	 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONMODIFY);
               	 	confirm.setStatus(0);
               	 	confirm.setTargetId(right.getId().toString());
               	 	confirm.setRecordText("修改菜单"+right.getName());
               	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPERIGHT);
               	 	this.tblAuthorizationRecordMapper.insert(confirm);
            	}
            } else {
            	right.setId(RandomUtil.uuBigDecimalId());
            	//新增确认记录需要发起流程
            	confirm = new TblAuthorizationRecord();
				confirm.setRecordId(RandomUtil.uuStringId());
				confirm.setCreationTime(new Date());
				confirm.setCreator(loginStaff.getStaffid());
				confirm.setCreatorName(loginStaff.getRealname());
				confirm.setOperationData(JSONObject.toJSONString(right));
				if(parentRight!=null) {
					confirm.setOperationMemo("新增菜单信息，菜单名称："+right.getName()+"、上级菜单："+parentRight.getName()+"、"+memo);
				}else {
					confirm.setOperationMemo("新增菜单信息，菜单名称："+right.getName()+"、"+memo);
				}
				
				confirm.setOperationType(TblAuthorizationRecord.OPERATIONINSERT);
				confirm.setStatus(0);
				confirm.setTargetId(right.getId().toString());
				confirm.setRecordText("新增菜单"+right.getName());
				confirm.setTargetType(TblAuthorizationRecord.TARGETTYPERIGHT);
				this.tblAuthorizationRecordMapper.insert(confirm);
            }
        	resultMap.put("data", confirm);
        }else {
        	if (right.getId() != null) {
                this.tblSystemRightMapper.updateTblSystemRight(right);
            } else {
            	right.setId(RandomUtil.uuBigDecimalId());
                this.tblSystemRightMapper.insertTblManageRight(right);
            }
        }
       
        resultMap.put("rightId", right.getId());
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean findRightEntityById(BigDecimal id) throws Exception {
		TblSystemRight right = this.tblSystemRightMapper.findById(id);
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("right", right);
		return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean modifyManageRight(TblSystemRight right, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblSystemRightMapper.updateTblSystemRight(right);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("rightId", right.getId());
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean removeManageRight(BigDecimal rightId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	Integer count = this.tblSystemRightMapper.selectChildrenCount(rightId);
		if (count > 0) {
		    return ResponseFormat.retParam(1, 20007, null);
		}
		 
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			TblAuthorizationRecord confirm = null;
			TblSystemRight right = this.tblSystemRightMapper.selectById(rightId);
			
			confirm = new TblAuthorizationRecord();
			confirm.setRecordId(RandomUtil.uuStringId());
			confirm.setCreationTime(new Date());
			confirm.setCreator(loginStaff.getStaffid());
			confirm.setCreatorName(loginStaff.getRealname());
			confirm.setOperationData(JSONObject.toJSONString(right));
			confirm.setOperationMemo("删除菜单信息，菜单名称："+right.getName());
			confirm.setOperationType(TblAuthorizationRecord.OPERATIONREMOVE);
			confirm.setStatus(0);
			confirm.setTargetId(right.getId().toString());
			confirm.setRecordText("删除菜单"+right.getName());
			confirm.setTargetType(TblAuthorizationRecord.TARGETTYPERIGHT);
			this.tblAuthorizationRecordMapper.insert(confirm);
			return ResponseFormat.retParam(1, 200, confirm);
		}else {
			this.tblSystemRightMapper.deleteOrgRIght(rightId);
			this.tblSystemRightMapper.deleteRoleRIght(rightId);
			this.tblSystemRightMapper.deleteSystemRIght(rightId);
			return ResponseFormat.retParam(1, 200, null);
		}
		
    }

    @Override
    public JsonBean findRightLIstByUser(String token, BigDecimal rightId, String moduleType) throws Exception {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            if (rightId == null) {
                rightId = BigDecimal.valueOf(0);
            }

            List<TblSystemRight> rightList = this.tblSystemRightMapper.selectChildrenRightListByUser(rightId, loginStaff.getStaffid(), loginStaff.getLinkOrg().getOrgid(), moduleType);
            this.setChidrenRightList(rightList, loginStaff.getStaffid(), loginStaff.getLinkOrg().getOrgid(), moduleType);
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("rightList", rightList);
            return ResponseFormat.retParam(1, 200, resultMap);
    }

    private void setChidrenRightList(List<TblSystemRight> rightList, BigDecimal staffid, BigDecimal orgid, String moduleType) throws Exception {
        List<TblSystemRight> childrenList = null;
        for (TblSystemRight right : rightList) {
            childrenList = this.tblSystemRightMapper.selectChildrenRightListByUser(right.getId(), staffid, orgid, moduleType);
            this.setChidrenRightList(childrenList, staffid, orgid, moduleType);
            right.setChildren(childrenList);
        }
    }

    @Override
    public JsonBean findChildrenRightListByUser(String token, BigDecimal rightId, String moduleType) throws Exception {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            if (rightId == null) {
                rightId = BigDecimal.valueOf(0);
            }
            List<TblSystemRight> rightList = this.tblSystemRightMapper.selectChildrenRightListByUser(rightId, loginStaff.getStaffid(), loginStaff.getLinkOrg().getOrgid(), moduleType);
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("rightList", rightList);
            return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean findAllRightListByCompany(String token, BigDecimal rightId, BigDecimal orgId, String moduleType) throws Exception {
		 TblStaffUtil loginStaff = userProvider.get();
		 if (loginStaff == null) {
		     return ResponseFormat.retParam(0, 20006, null);
		 }
		 //根据用户所属的上级公司权限  获取当前公司能拥有的所有权限
		 List<TblSystemRight> rightList = this.tblSystemRightMapper.selectAllRightListByFatherCompany(TblSystemRight.MENUTYPE,loginStaff.getLinkOrg().getOrgid(), moduleType,orgId);
		 List<TblSystemRight> pageList = this.tblSystemRightMapper.selectAllRightListByFatherCompany(TblSystemRight.PAGETYPE,loginStaff.getLinkOrg().getOrgid(), moduleType,orgId);
		 List<TblSystemRight> btnList = this.tblSystemRightMapper.selectAllRightListByFatherCompany(TblSystemRight.BTNTYPE,loginStaff.getLinkOrg().getOrgid(), moduleType,orgId);
		 
		   	//放入页面的按钮操作权限
     	for (TblSystemRight page : pageList) {
				page.setChildren(btnList.stream().filter(obj -> page.getId().toString().equals(obj.getParent().toString())).collect(Collectors.toList()));
			}
     	
     	for (TblSystemRight r : rightList) {
				r.setChildren(pageList.stream().filter(obj -> r.getId().toString().equals(obj.getParent().toString())).collect(Collectors.toList()));;
			}
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 resultMap.put("rightList", rightList);
		 return ResponseFormat.retParam(1, 200, resultMap);
    }


    @Override
    public JsonBean grantUserRight(String rightIds, String token, BigDecimal staffId) throws Exception {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            //移除之前的权限
            this.tblSystemRightMapper.removeUserRigthAll(staffId);
            String[] rightId = rightIds.split(",");
            for (String id : rightId) {
                this.tblSystemRightMapper.saveUserRight(id, staffId);
            }
            return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean grantCompanyRight(String rightIds, String token, String orgId,String moduleType) throws Exception {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return ResponseFormat.retParam(0, 20006, null);
            }
            //移除之前的权限
            this.tblSystemRightMapper.removeCompanyRigthAll(orgId,moduleType);
            String[] rightId = rightIds.split(",");
            for (String id : rightId) {
                this.tblSystemRightMapper.saveCompanyIdRight(id, orgId);
            }
            return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean getCompanySettingRightList(String token, BigDecimal rightId, String moduleType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		//根据用户主键和公司主键 获取当前所有的权限；
		List<TblSystemRight> rightList = this.tblSystemRightMapper.selectChildrenRightListByUser(rightId, loginStaff.getStaffid(), loginStaff.getCurrentOrg().getOrgid(), moduleType);
		this.setChidrenRightList(rightList, loginStaff.getStaffid(), loginStaff.getCurrentOrg().getOrgid(), moduleType);
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("rightList", rightList);
		return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean getCompanySettingRightListInfo(String token, BigDecimal rightId, String moduleType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		//根据用户主键和公司主键 获取当前所有的权限；
		List<TblSystemRight> rightList = this.tblSystemRightMapper.selectChildrenRightListByUser(rightId, loginStaff.getStaffid(), loginStaff.getCurrentOrg().getOrgid(), moduleType);
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("rightList", rightList);
		return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean findSystemRightSettingById(BigDecimal id) throws Exception {
            TblSystemRight right = this.tblSystemRightMapper.findSettingRightById(id);
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("right", right);
            return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean modifySystemSettingRight(TblSystemRight right, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblSystemOrgRight orgRight = new TblSystemOrgRight();
        orgRight.setOrgid(loginStaff.getCurrentOrg().getOrgid());
        orgRight.setRightid(right.getId());
        orgRight.setRightname(right.getName());
        orgRight.setRightstatus(right.getVisible());
        this.tblSystemOrgRightMapper.updateSystemSettingRight(orgRight);
        right.setName(null);
        right.setVisible(null);
        this.tblSystemRightMapper.updateTblSystemRight(right);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("rightId", right.getId());
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean findAllRightListcf(String token, TblSystemRight right, Integer judge) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblSystemRight> rightList = new ArrayList<TblSystemRight>(0);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        //judge 不为空 带入筛选条件进行查询，为空则不带入
        if (judge != null && 1 == judge) {
        	if(StringUtils.isNotBlank(right.getName())) {
        		String rightIdStr = "0";
                String allRightIds = "";
                String praentIds = "";
                List<String> rightId = new ArrayList<String>(0);
                List<String> parentId = new ArrayList<String>(0);
                //筛选出所有符合筛选条件的 权限主键
                rightId = this.tblSystemRightMapper.selectRightIdStrByRight(right);
                if (rightId != null) {
                    rightIdStr = String.join(",", rightId);
                    allRightIds =  String.join(",", rightId);
                    //循环拼接所有符合筛选条件的权限信息的上级权限
                    do {
                        parentId = this.tblSystemRightMapper.selectFatherRightIdStrByRight(rightIdStr,allRightIds);
                        if (parentId == null) {
                            break;
                        }
                        praentIds = String.join(",", parentId);
                        allRightIds += "," + praentIds;
                        rightIdStr = praentIds;
                    } while (praentIds.indexOf(",") != -1);
                    //根绝拼接得到的权限主键，查询所有符合条件的权限记录并保持字符集关系
                    allRightIds = allRightIds.substring(0, allRightIds.length()-1);
                    rightList = this.tblSystemRightMapper.selectAllRightListByChoice(new BigDecimal(0), allRightIds);
                    //递归筛选到末级权限
                    this.setAllChildrenRightListByChoice(rightList, allRightIds);
                }else {
                	rightList = null;
                }
            } else {
            	//1.查找所有目录
            	rightList = this.tblSystemRightMapper.selectAllRightListByModuleType(TblSystemRight.MENUTYPE, right.getModuletype());
            	List<TblSystemRight> pageList = this.tblSystemRightMapper.selectAllRightListByModuleType(TblSystemRight.PAGETYPE, right.getModuletype());
            	List<TblSystemRight> btnList = this.tblSystemRightMapper.selectAllRightListByModuleType(TblSystemRight.BTNTYPE, right.getModuletype());
            	
            	//放入页面的按钮操作权限
            	for (TblSystemRight page : pageList) {
					page.setChildren(btnList.stream().filter(obj -> page.getId().toString().equals(obj.getParent().toString())).collect(Collectors.toList()));
				}
            	
            	for (TblSystemRight r : rightList) {
					r.setChildren(pageList.stream().filter(obj -> r.getId().toString().equals(obj.getParent().toString())).collect(Collectors.toList()));;
				}
            }
        	resultMap.put("rightList", rightList);
        } else {
        	//没有筛选条件 获取所有的权限信息
            rightList = this.tblSystemRightMapper.selectAllRightList(BigDecimal.valueOf(0));
            this.setAllChildrenRightList(rightList);
            resultMap.put("rightList", rightList);
        }
        return ResponseFormat.retParam(1, 200, resultMap);
    }

	private void setAllChildrenRightListByChoice(List<TblSystemRight> rightList, String rightIdStr) throws Exception {
        List<TblSystemRight> childrenList = null;
        for (TblSystemRight tblSystemRight : rightList) {
            childrenList = this.tblSystemRightMapper.selectAllRightListByChoice(tblSystemRight.getId(), rightIdStr);
            this.setAllChildrenRightList(childrenList);
            tblSystemRight.setChildren(childrenList);
        }
    }

    private void setAllChildrenRightList(List<TblSystemRight> rightList) throws Exception {
        List<TblSystemRight> childrenList = null;
        for (TblSystemRight tblSystemRight : rightList) {
            childrenList = this.tblSystemRightMapper.selectAllRightList(tblSystemRight.getId());
            this.setAllChildrenRightList(childrenList);
            tblSystemRight.setChildren(childrenList);
        }
    }

    @Override
    public JsonBean findRightListByRole(String token, BigDecimal rightId, String moduleType) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (rightId == null) {
            rightId = new BigDecimal(0);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if (loginStaff.getRoleIdStrs() == null || "".equals(loginStaff.getRoleIdStrs())) {
            resultMap.put("rightList", null);
            return ResponseFormat.retParam(1, 200, resultMap);
        }
		//根据用户所在当前公司的角色和模块类型， 获取传入权限主键的子级权限数据
		List<TblSystemRight> rightList = this.tblSystemRightMapper.selectChildrenRightListByRole(rightId, loginStaff.getRoleIdStrs(), loginStaff.getLinkOrg().getOrgid(), moduleType);
		this.setChidrenRightListByRole(rightList, loginStaff.getRoleIdStrs(), loginStaff.getLinkOrg().getOrgid(), moduleType);
		resultMap.put("rightList", rightList);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    private void setChidrenRightListByRole(List<TblSystemRight> rightList, String roleId, BigDecimal orgid, String moduleType) throws Exception {
        List<TblSystemRight> childrenList = null;
        for (TblSystemRight right : rightList) {
            childrenList = this.tblSystemRightMapper.selectChildrenRightListByRole(right.getId(), roleId, orgid, moduleType);
            this.setChidrenRightListByRole(childrenList, roleId, orgid, moduleType);
            right.setChildren(childrenList);
        }
    }

    @Override
    public JsonBean grantRoleRight(String rightIds, BigDecimal roleId, String moduleType) throws Exception {
	    //移除之前的权限
	    this.tblSystemRightMapper.removeRoleRigthByModuleType(roleId, moduleType);
	    String[] rightId = rightIds.split(",");
	    for (String id : rightId) {
	    	//保存新的权限
	        this.tblSystemRightMapper.saveRoleRight(id, roleId);
	    }
	    return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean findRoleRigetListByType(String token, Integer type, String switchType,String moduleType) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	
		 List<TblSystemRight> rightList = new ArrayList<TblSystemRight>(0);
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 if (loginStaff.getRoleIdStrs() == null || "".equals(loginStaff.getRoleIdStrs())) {
		 	//登录用户未绑定角色
		     resultMap.put("rightList", null);
		     return ResponseFormat.retParam(1, 200, resultMap);
		 }
		
		 if (type == null) {
		 	//查询用户所在的当前公司 的角色 拥有当前菜单的一级模块
		     rightList = this.tblSystemRightMapper.selectRoleRigetListByType(loginStaff.getUsername(),loginStaff.getRoleIdStrs(), loginStaff.getLinkOrg().getOrgid(), moduleType, TblSystemRight.MENUTYPE);
		     List<TblSystemRight> pageList = this.tblSystemRightMapper.selectRoleRigetListByType(loginStaff.getUsername(),loginStaff.getRoleIdStrs(), loginStaff.getCurrentOrg().getOrgid(), moduleType, TblSystemRight.PAGETYPE);
		     
        	for (TblSystemRight r : rightList) {
				r.setChildren(pageList.stream().filter(obj -> r.getId().toString().equals(obj.getParent().toString())).collect(Collectors.toList()));
        	}
		     int i = rightList.size(); //一级菜单排序
		     int k = 0;  //一级菜单下的二级菜单排序
		     int j = 1;  //一级菜单生成唯一标识
		     int l = 1;  //一级菜单下的二级菜单生成唯一标识
		     //通过公司 模块类型 查询出所有的一级主题报表信息
		     List<TblBiReportMenu> oneList = this.tblBiReportMenuMapper.selectFirstRpeortListByModuleType(moduleType,loginStaff.getCurrentOrg().getOrgid(),null);
		     TblSystemRight right = null;
		     List<TblBiReportMenu> chilList = null;
		     List<TblSystemRight> rpeortList = null;
		     TblSystemRight chri = null;
		     for (TblBiReportMenu menu : oneList) {
		     	i++;
		     	k = 0;
		     	l = 1;
		 		right = new TblSystemRight();
		 		right.setId(menu.getPageid());
		 		right.setIslink(1);
		 		right.setName(menu.getPagename());
		 		right.setPath("reportLink_"+j);
		 		right.setPerms("reportLink_"+j);
		 		right.setType(0);
		 		right.setVisible(1);
		 		right.setModuletype(moduleType);
		 		right.setChecked(true);
		 		right.setSort(i);
		 		right.setParent(new BigDecimal(0));
		 		chilList = this.tblBiReportMenuMapper.selectFirstRpeortListByModuleType(moduleType,loginStaff.getCurrentOrg().getOrgid(),menu.getPageid());
		 		rpeortList = new ArrayList<TblSystemRight>(0);
		 		for (TblBiReportMenu chil : chilList) {
		 			k++;
		 			chri = new TblSystemRight();
		 			chri.setId(chil.getPageid());
		 			chri.setIslink(1);
		 			chri.setName(chil.getPagename());
		 			chri.setPath("reportLink_"+j+"_"+l);
		 			chri.setPerms("reportLink_"+j+"_"+l);
		 			chri.setType(1);
		 			chri.setVisible(1);
		 			chri.setModuletype(moduleType);
		 			chri.setChecked(true);
		 			chri.setSort(k);
		 			chri.setParent(menu.getPageid());
		 			chri.setComponent("@/views/setting/middle/index");
		 			chri.setReportType(chil.getType());
		 			chri.setReportLinkUrl(chil.getRqurl());
		 			rpeortList.add(chri);
		 			l++;
		 		}
		 		right.setChildren(rpeortList);
		 		rightList.add(right);
		 		j++;
		 	}
		     
		     if("znfx".equals(moduleType)) {
		     	//智能分析模块式查询有没有下发到个人的报表信息
		     	oneList = this.tblBiReportMenuMapper.selectFirstRpeortListByPerson(loginStaff.getStaffid(),null);
		     	for (TblBiReportMenu menu : oneList) {
		         	i++;
		         	k = 0;
		         	l = 1;
		 			right = new TblSystemRight();
		 			right.setId(menu.getPageid());
		 			right.setIslink(1);
		 			right.setName(menu.getPagename());
		 			right.setPath("reportLink_"+j);
		 			right.setPerms("reportLink_"+j);
		 			right.setType(0);
		 			right.setVisible(1);
		 			right.setModuletype(moduleType);
		 			right.setChecked(true);
		 			right.setSort(i);
		 			right.setParent(new BigDecimal(0));
		 			chilList = this.tblBiReportMenuMapper.selectFirstRpeortListByPerson(loginStaff.getStaffid(),menu.getPageid());
		 			rpeortList = new ArrayList<TblSystemRight>(0);
		 			for (TblBiReportMenu chil : chilList) {
		 				k++;
		 				chri = new TblSystemRight();
		 				chri.setId(chil.getPageid());
		 				chri.setIslink(1);
		 				chri.setName(chil.getPagename());
		 				chri.setPath("reportLink_"+j+"_"+l);
		 				chri.setPerms("reportLink_"+j+"_"+l);
		 				chri.setType(1);
		 				chri.setVisible(1);
		 				chri.setModuletype(moduleType);
		 				chri.setChecked(true);
		 				chri.setSort(k);
		 				chri.setParent(menu.getPageid());
		 				chri.setComponent("@/views/setting/middle/index");
		 				chri.setReportType(chil.getType());
		 				chri.setReportLinkUrl(chil.getRqurl());
		 				rpeortList.add(chri);
		 				l++;
		 			}
		 			right.setChildren(rpeortList);
		 			rightList.add(right);
		 			j++;
		 		}
		     }
		 } else {
			if (switchType != null && switchType .equals("") && switchType.equals("company")) {
                rightList = null;
                resultMap.put("rightList", rightList);
                return ResponseFormat.retParam(1, 200, resultMap);
            }else {
                rightList = this.tblSystemRightMapper.selectRoleRigetListByType(loginStaff.getUsername(),loginStaff.getRoleIdStrs(), loginStaff.getCurrentOrg().getOrgid(), moduleType, type);
            }
		    //  rightList = this.tblSystemRightMapper.selectRoleRigetListByType(loginStaff.getRoleIdStrs(), loginStaff.getCurrentOrg().getOrgid(), moduleType, type);
		 }
		 resultMap.put("rightList", rightList);
		 return ResponseFormat.retParam(1, 200, resultMap);
    }
    
    private void setChildrenRoleRigetListByType(List<TblSystemRight> rightList, String roleId, BigDecimal orgid, String moduleType) throws Exception {
        List<TblSystemRight> childrenList = null;
        for (TblSystemRight right : rightList) {
            childrenList = this.tblSystemRightMapper.selectChildrenRightListByRoleType(right.getId(), roleId, orgid, moduleType);
            //this.setChildrenRoleRigetListByType(childrenList, roleId, orgid, moduleType);
            right.setChildren(childrenList);
        }
    }


    @Override
    public JsonBean modifySystemRightVisible(String token, BigDecimal rightId, Integer visible) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		//修改当前权限菜单是否可见
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			TblAuthorizationRecord confirm = null;
        	String memo = "";
			TblSystemRight right = this.tblSystemRightMapper.selectById(rightId);
    		Integer operationType = 0;
    		//判断是否有正在审批的数据
        	confirm = this.tblAuthorizationRecordMapper.selectSpzRecordInfoByTargetId(rightId.toString());
        	
        	if(visible == 1) {
        		memo += "启用菜单"+right.getName();
        		operationType = TblAuthorizationRecord.OPERATIONENABLE;
        	}else {
        		memo += "弃用菜单"+right.getName();
        		operationType = TblAuthorizationRecord.OPERATIONDEPRECATED;
        	}
        	right.setVisible(visible);
        	
        	if(confirm != null) {
        		//修改审批中的确认数据
        		confirm.setModifiedTime(new Date());
        		confirm.setModifier(loginStaff.getStaffid());
        		confirm.setModifyerName(loginStaff.getRealname());
        		confirm.setOperationData(JSONObject.toJSONString(right));
        		confirm.setOperationMemo(memo);
        		confirm.setRecordText(memo);
        		confirm.setOperationType(operationType);
        		this.tblAuthorizationRecordMapper.updateById(confirm);
        	}else {
        		//新增确认记录需要发起流程
        		confirm = new TblAuthorizationRecord();
        		confirm.setRecordId(RandomUtil.uuStringId());
        		confirm.setCreationTime(new Date());
        		confirm.setCreator(loginStaff.getStaffid());
        		confirm.setCreatorName(loginStaff.getRealname());
           	 	confirm.setOperationData(JSONObject.toJSONString(right));
           	 	confirm.setOperationMemo(memo);
           	 	confirm.setOperationType(operationType);
           	 	confirm.setStatus(0);
           	 	confirm.setTargetId(right.getId().toString());
           	 	confirm.setRecordText(memo);
           	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPERIGHT);
           	 	this.tblAuthorizationRecordMapper.insert(confirm);
        	}
        	resultMap.put("data", confirm);
		}else {
			this.tblSystemRightMapper.updateSystemRightVisible(rightId, visible);
		}
		
		resultMap.put("rightId", rightId);
		return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean findAllRightListByCompanyToGrant(String token, BigDecimal rightId, BigDecimal roleId, String moduleType) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
		//根据当前公司主键获取传的权限主键下级全选数据
		List<TblSystemRight> rightList = this.tblSystemRightMapper.selectAllRightListByCompanye(TblSystemRight.MENUTYPE, loginStaff.getLinkOrg().getOrgid(), moduleType);
		List<TblSystemRight> pageList = this.tblSystemRightMapper.selectAllRightListByCompanye(TblSystemRight.PAGETYPE, loginStaff.getLinkOrg().getOrgid(), moduleType);
		List<TblSystemRight> btnList = this.tblSystemRightMapper.selectAllRightListByCompanye(TblSystemRight.BTNTYPE, loginStaff.getLinkOrg().getOrgid(), moduleType);
		
		
		//查询当前角色拥有当前模块下的权限主键；
		List<BigDecimal> rightIdList = this.tblSystemRightMapper.selectAllRightIdListByRoleId(TblSystemRight.MENUTYPE, roleId, moduleType);
		List<BigDecimal> pageIdList = this.tblSystemRightMapper.selectAllRightIdListByRoleId(TblSystemRight.PAGETYPE, roleId, moduleType);
		List<BigDecimal> btnIdList = this.tblSystemRightMapper.selectAllRightIdListByRoleId(TblSystemRight.BTNTYPE, roleId, moduleType);
		
		
		for (TblSystemRight btn : btnList) {
			btn.setChecked(btnIdList.contains(btn.getId()));
		}
		
		
		//放入页面的按钮操作权限
    	for (TblSystemRight page : pageList) {
			page.setChildren(btnList.stream().filter(obj -> page.getId().toString().equals(obj.getParent().toString())).collect(Collectors.toList()));
			page.setChecked(pageIdList.contains(page.getId()));
		}
    	
    	for (TblSystemRight r : rightList) {
			r.setChildren(pageList.stream().filter(obj -> r.getId().toString().equals(obj.getParent().toString())).collect(Collectors.toList()));
			r.setChecked(rightIdList.contains(r.getId()));
		}
		resultMap.put("rightList", rightList);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

	/**
	 * 批量获取树名称
	 * @param rightIds
	 * @return
	 */
	@Override
	public Map<BigDecimal, TblSystemRight> getRightNamesMap(List<BigDecimal> rightIds) {
		List<TblSystemRight> tblSystemRights = tblSystemRightMapper.getRightNamesList(StringUtils.join(rightIds,","));
		if (CollectionUtil.isEmpty(tblSystemRights)) {
			return Collections.emptyMap();
		}
		return tblSystemRights.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
	}

    @Override
	public JsonBean grantRoleDataRight(BigDecimal roleId, String companyIds) throws Exception {
        String[] companyStrs = companyIds.split("-");//分割传的授权公司部门信息
        String[] orgIds = null;
        String deptIds = "";
		//this.tblRoleMapper.deleteDateRightInfoByRoleId(roleId);
		for (String companys : companyStrs) {
			orgIds = companys.split("~");//分割公司和部门的信息
			//判断当前公司是否授权给这个角色，如果deptIds不为空 则已授权，拼接新授权的部门信息，如果为空，则之前未授权，直接保存最新的授权信息
			deptIds = this.tblRoleMapper.selectDateRightDeptIDByOrgId(roleId,orgIds[0]);
			if(StringUtils.isNotBlank(deptIds)){
				this.tblRoleMapper.updateDataRightInfo(roleId,orgIds[0],deptIds+","+orgIds[1]);
			}else {
				this.tblRoleMapper.insertDataRightInfo(roleId,orgIds[0],orgIds[1]);
			}
			
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getRoleDataDeptInfo(BigDecimal roleId, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		List<TblDateRightInfo> dataList = this.tblDateRightInfoMapper.selectDateRightInfoByRoleId(roleId);
			
		for (TblDateRightInfo data : dataList) {
			data.setDeptList(this.tblOrganizationMapper.selectDeptListByDeptIds(data.getDeptIdStrs()));
		}
        return ResponseFormat.retParam(1, 200, dataList);
	}
	
	@Override
	public JsonBean getGrantRoleDataDeptInfo(BigDecimal roleId, String token, Integer pageNumber, Integer pageSize,
			String deptName, String companyName) throws Exception {
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
			
			Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
			IPage<TblOrganization> pageList = tblOrganizationMapper.selectDeptInfoByGranDataList(page,deptName,companyName,roleId);
			
			PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			
			pageInfo.setTlist(pageList.getRecords());
			pageInfo.setTotalRecord((int) pageList.getTotal());
			resultMap.put("pageInfo", pageInfo);
			return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	
	@Override
	public JsonBean removeDataRight(BigDecimal roleId, String dataJson) throws Exception {
        JSONArray dataArray = JSONArray.parseArray(dataJson);
        JSONObject dataObj = null;
        String[] deptIds = null;
        String deptIdStrs = null;
        for (int i = 0 ; i < dataArray.size() ; i++) {
			dataObj = dataArray.getJSONObject(i);
			deptIds = dataObj.getString("deptId").split(",");
			deptIdStrs = this.tblRoleMapper.selectDateRightDeptIDByOrgId(roleId, dataObj.getString("orgId"));
			deptIdStrs = ","+deptIdStrs+",";
			for (String deptId : deptIds) {
				if(deptIdStrs.indexOf(","+deptId+",") != -1) {
					deptIdStrs = deptIdStrs.replace(","+deptId+",", ",");
				}
			}
			if(deptIdStrs.length() > 1) {
				deptIdStrs = deptIdStrs.substring(1, deptIdStrs.length()-1);
				this.tblRoleMapper.updateDataRightInfo(roleId, dataObj.getString("orgId"), deptIdStrs);
			}else {
				this.tblRoleMapper.removeDateRightInfo(roleId, dataObj.getString("orgId"));
			}
			
		}
        return ResponseFormat.retParam(1, 200, null);
	}
	
	
	@Override
	public JsonBean getGrantDataRightDeptList(Integer pageNumber, String token, Integer pageSize, BigDecimal pid,
		String deptNumber, String deptName, BigDecimal roleId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
			
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		if (pid == null) {
			pid = loginStaff.getLinkOrg().getOrgid();
		    if(loginStaff.getLinkOrg().getFatherorgid().intValue() != -1) {
		    	TblOrganization org = this.findRootCompanyIdByChildren(loginStaff.getLinkOrg().getFatherorgid());
		    	pid = org.getOrgid();
				resultMap.put("company", org);
		    }else {
		    	resultMap.put("company", loginStaff.getLinkOrg());
		    }
		}else {
			TblOrganization org = this.tblOrganizationMapper.selectByOrgid(pid);
			resultMap.put("company", org);
		}
		
		String deptIds = this.tblOrganizaService.selectAllDeptIdsByOrgId(pid);
		
		
		Page<TblOrganization> page = new Page<TblOrganization>(pageNumber,pageSize);
        page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblOrganization> pageList =  this.tblOrganizationMapper.selectGrantDataRightDeptListByCompanyId(page,deptIds,deptName,deptNumber,roleId,pid);
		
		PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int) pageList.getTotal());
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	private TblOrganization findRootCompanyIdByChildren(BigDecimal fatherorgid) throws Exception {
		TblOrganization org = this.tblOrganizationMapper.selectByOrgid(fatherorgid);
		if(org.getFatherorgid().intValue() != -1) {
			org = this.findRootCompanyIdByChildren(org.getFatherorgid());
		}
		return org;
		
	}
	
	@Override
	public JsonBean getGrantSystemRightStaffList(Integer pageNumber, String token, Integer pageSize, String userName,
			String realName, BigDecimal roleId, String companyName, String deptName) throws Exception {
			TblStaffUtil loginStaff = userProvider.get();
		   	if (loginStaff == null) {
		   		return ResponseFormat.retParam(0, 20006, null);
		    }
	
		   	Map<String, Object> resultMap = new HashMap<String, Object>(0);
			
			
			String deptIds = null;
			
			if(StringUtils.isNotBlank(companyName)) {
				deptIds = this.tblOrganizaService.selectAllDeptIdsByOrgName(companyName);
			}
			
			Page<TblStaff> page = new Page<TblStaff>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
			IPage<TblStaff> pageList = this.tblStaffMapper.selectStaffListByRoleId(page,userName,realName,roleId,deptName,deptIds);
			
			List<TblStaff> staffList = pageList.getRecords();
			String currentdeptId = null;
			String midcomName = null;
			for (TblStaff tblStaff : staffList) {
				if(StringUtils.isBlank(tblStaff.getDataSource())) {
					continue;
				}
				if(StringUtils.isNotBlank(currentdeptId) && currentdeptId.equals(tblStaff.getDataSource())) {
					tblStaff.setCompanyname(midcomName);
					continue;
				}
				midcomName = this.tblOrganizaService.getDeptLinkCompanyNameByDeptId(new BigDecimal(tblStaff.getDataSource()));
				currentdeptId = tblStaff.getDataSource();
				tblStaff.setCompanyname(midcomName);
			}
			PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
			pageInfo.setPageSize(pageSize);
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setTlist(staffList);
			pageInfo.setTotalRecord((int)pageList.getTotal());
			resultMap.put("pageInfo", pageInfo);
		    return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean getSystemRightStaffList(Integer pageNumber, String token, Integer pageSize, String userName,
			String realName, BigDecimal roleId, String companyName, String deptName, Integer isAll, BigDecimal orgId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
	   	if (loginStaff == null) {
	   		return ResponseFormat.retParam(0, 20006, null);
	    }

	   	Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		String deptIds = null;
		
		
		if(isAll == 0) {
			//不开启全集团查询
			if (orgId == null) {
				//未传入公司主键 默认查询用户所属的公司信息
				orgId = loginStaff.getLinkOrg().getOrgid();
		        if(loginStaff.getLinkOrg().getFatherorgid().intValue() != -1) {
		        	TblOrganization org = this.findRootCompanyIdByChildren(loginStaff.getLinkOrg().getFatherorgid());
		        	orgId = org.getOrgid();
		    		resultMap.put("company", org);
		        }else {
		        	resultMap.put("company", loginStaff.getLinkOrg());
		        }
		    }else {
		    	//传入公司主键 已传入公司的主键昨晚查询条件
		    	TblOrganization org = this.tblOrganizationMapper.selectByOrgid(orgId);
		    	resultMap.put("company", org);
		    }
			//根据得到的公司主键获取所有下级部门的主键
			deptIds = this.tblOrganizaService.selectAllDeptIdsByOrgId(orgId);
			if(StringUtils.isBlank(deptIds)) {
				resultMap.put("pageInfo", null);
				return ResponseFormat.retParam(1, 200, resultMap);
			}
		}
		
		
		if(StringUtils.isNotBlank(companyName)) {
			//公司名称不为空 查询所有符合查询条件的公司主键
			deptIds = this.tblOrganizaService.selectAllDeptIdsByOrgName(companyName);
			if(StringUtils.isBlank(deptIds)) {
				resultMap.put("pageInfo", null);
				return ResponseFormat.retParam(1, 200, resultMap);
			}
		}
		
		Page<TblStaff> page = new Page<TblStaff>(pageNumber,pageSize);
        page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblStaff> pageList = this.tblStaffMapper.selectStaffListToGrantRole(page,userName,realName,roleId,deptName,deptIds,isAll);
		//获取符合条件的用户分页数据
		List<TblStaff> staffList = pageList.getRecords();
		String currentdeptId = null;//当前循环判断的部门主键
		String midcomName = null;//当前循环的公司名称
		for (TblStaff tblStaff : staffList) {
			if(StringUtils.isBlank(tblStaff.getDataSource())) {
				continue;
			}
			if(StringUtils.isNotBlank(currentdeptId) && currentdeptId.equals(tblStaff.getDataSource())) {
					tblStaff.setCompanyname(midcomName);
					continue;
			}
			//根据查询的部门主键获取公司名称
			midcomName = this.tblOrganizaService.getDeptLinkCompanyNameByDeptId(new BigDecimal(tblStaff.getDataSource()));
			currentdeptId = tblStaff.getDataSource();
			tblStaff.setCompanyname(midcomName);
		}
		
		PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(staffList);
		pageInfo.setTotalRecord((int)pageList.getTotal());
		resultMap.put("pageInfo", pageInfo);
	    return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean zdyInsertRightList() {
		try {
			List<TblOrganization> orgList = this.tblOrganizationMapper.selectAllListByParentId(BigDecimal.valueOf(-1));
			for (TblOrganization org : orgList) {
				org.setOrganizationTrees(org.getOrgid().toString());
				this.tblOrganizationMapper.updateById(org);
			}
			this.dealAllOrgTrees(orgList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	private void dealAllOrgTrees(List<TblOrganization> orgList) throws Exception {
		for (TblOrganization father : orgList) {
			List<TblOrganization> chilList = this.tblOrganizationMapper.selectAllListByParentId(father.getOrgid());
			if(chilList == null || chilList.size() == 0) {
				continue;
			}
			for (TblOrganization chil : chilList) {
				chil.setOrganizationTrees(father.getOrganizationTrees()+","+chil.getOrgid());
				this.tblOrganizationMapper.updateById(chil);
			}
			this.dealAllOrgTrees(chilList);
		}
	}

	@Override
	public List<String> findNameByRithIds(String rightIds) throws Exception {
		List<String> nameList = this.tblSystemRightMapper.selectNameListByIds(rightIds);
		return nameList;
	}

}
