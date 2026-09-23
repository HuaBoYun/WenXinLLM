package com.huabo.fxgl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.RedisFinalUtis;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.config.DateBaseConfig;
import com.huabo.fxgl.dto.TblRiskMonitoringCreationDto;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.Riskevent;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.TblFillIssued;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblMajorRiskbranchCreate;
import com.huabo.fxgl.entity.TblMonModelVersion;
import com.huabo.fxgl.entity.TblOrganization;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskImprovementEntiry;
import com.huabo.fxgl.entity.TblRiskMonDictonary;
import com.huabo.fxgl.entity.TblRiskMonModel;
import com.huabo.fxgl.entity.TblRiskMonitoringCreation;
import com.huabo.fxgl.entity.TblRiskMonitoringFill;
import com.huabo.fxgl.entity.TblRiskReportingEntity;
import com.huabo.fxgl.entity.Tree;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.mapper.TblFillIssuedMapper;
import com.huabo.fxgl.mapper.TblMajorRiskCreationMapper;
import com.huabo.fxgl.mapper.TblMajorRiskbranchCreateMapper;
import com.huabo.fxgl.mapper.TblMonModelVersionMapper;
import com.huabo.fxgl.mapper.TblRiskImplementGroupMapper;
import com.huabo.fxgl.mapper.TblRiskImplementMapper;
import com.huabo.fxgl.mapper.TblRiskMonDictonaryMapper;
import com.huabo.fxgl.mapper.TblRiskMonModelMapper;
import com.huabo.fxgl.mapper.TblRiskMonitoringCreationMapper;
import com.huabo.fxgl.mapper.TblRiskMonitoringFillMapper;
import com.huabo.fxgl.mapper.TblRiskReportingMapper;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.TblMajorRiskCreationService;
import com.huabo.fxgl.service.TblRiskImplementService;
import com.huabo.fxgl.service.TblRiskImprovementService;
import com.huabo.fxgl.service.TblRiskMonitoringService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.util.PageResult;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import redis.clients.jedis.Jedis;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.ls.LSInput;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
public class TblRiskMonitoringServiceImpl extends ServiceImpl<TblRiskMonitoringCreationMapper, TblRiskMonitoringCreation> implements TblRiskMonitoringService {

    @Resource
    private TblRiskMonitoringCreationMapper tblRiskMonitoringCreationMapper;
    @Resource
    private TblRiskMonitoringFillMapper  tblRiskMonitoringFillMapper;
    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private UserProvider userProvider;
    @Resource
    private IOrganizationService organizationService;
    @Resource
    private IStaffService staffService;
    @Resource
    private StaffMapper staffMapper;
    
    
    @Resource
    private TblRiskMonDictonaryMapper tblRiskMonDictonaryMapper;
    
    @Resource
    private TblFillIssuedMapper tblFillIssuedMapper;
    
    @Resource
    private TblRiskMonModelMapper tblRiskMonModelMapper;
    
    @Resource
    private  TblMonModelVersionMapper tblMonModelVersionMapper;
 
	@Override
	public JsonBean getList(String token, Integer pageNumber, Integer pageSize ,TblRiskMonitoringCreationDto dto)
			throws Exception {
		 Map<String, Object> hashMap = new HashMap<>();
       try {
           //得到了当前登录的用户信息
           TblStaffUtil staffUtil = userProvider.get();
           if (staffUtil == null){
               return  ResponseFormat.retParam(0, 20006, hashMap);
           }
           String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "c.LINKORGID", "c.LINKDEPTID", "c.CREATESTAFFID", "c.SECRECTLEVELID", "c.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
  		 dto.setCreatestaffid(staffUtil.getStaffid());
            PageInfo<TblRiskMonitoringCreation> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                   .doSelectPageInfo(() -> tblRiskMonitoringCreationMapper.getList(dto,sql));
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
           hashMap.put("pageInfo",pageInfo);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
       return ResponseFormat.retParam(1, 200, hashMap);
	}
    
    
@Override
public JsonBean saveOrUpdate(TblRiskMonitoringCreation tblRiskMonitoringCreation, String attIds, String token) throws Exception {
	  Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            if(tblRiskMonitoringCreation.getId()!=null&&tblRiskMonitoringCreation.getId().compareTo(new BigDecimal(0))==1){
            	tblRiskMonitoringCreationMapper.updateById(tblRiskMonitoringCreation);
            }else{
            	tblRiskMonitoringCreation.setCreatestaffid(staffUtil.getStaffid());//创建人名称
 	            tblRiskMonitoringCreation.setCreatetime(new Date());//创建时间
 	           tblRiskMonitoringCreation.setLinkOrgId(staffUtil.getLinkOrg().getOrgid());//创建单位
 	          tblRiskMonitoringCreation.setLinkDeptId(staffUtil.getLinkDetp().getOrgid());
 	            tblRiskMonitoringCreation.setId(RandomUtil.uuBigDecimalId());
 	           tblRiskMonitoringCreationMapper.insert(tblRiskMonitoringCreation);
            }
            hashMap.put("pageInfo",tblRiskMonitoringCreation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
}
    
@Override
public JsonBean delete(String id) throws Exception {
	// TODO Auto-generated method stub
	  Map<String, Object> hashMap = new HashMap<>();
	  try {
		  TblRiskMonitoringCreation tbl=tblRiskMonitoringCreationMapper.selectById(id);
		  //if(tbl!=null&&tbl.getStatus()!=null&&tbl.getStatus()==0){
		  tblRiskMonitoringCreationMapper.deleteById(id);
//		  }else{
//			  return ResponseFormat.retParam(0, 201, null);
//		  }
		  
//            //删除所有附件
//            List<BigDecimal> attIdList = this.tblRiskReportingMapper.findAttIdListByReporting(id);
//            for (BigDecimal attId : attIdList) {
//                this.deleteRealtionAttInfo(attId);
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	  return ResponseFormat.retParam(1, 200, hashMap);
}


@Override
public JsonBean detail(String id) throws Exception {
	// TODO Auto-generated method stub
	 Map<String, Object> hashMap = new HashMap<>();
	 FiexibleNameAssignment ment=new FiexibleNameAssignment();
	 TblRiskMonitoringCreation tbl=null;
	try {
		 tbl=tblRiskMonitoringCreationMapper.selectById(id);
		  if(tbl!=null){
	        	//对灵活字段中的姓名名称及机构名称赋值
				fieldOrgStaffId item=new fieldOrgStaffId();
				BeanUtils.copyProperties(tbl,item); 
				fieldOrgStaffName nameEntity=ment.setOpenName(item);
				BeanUtils.copyProperties(nameEntity,tbl); 
		  }
		 
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	 return ResponseFormat.retParam(1, 200, tbl);

}
 
	@Override
	public JsonBean monitoringIssued(String ids,String staffids,String issuedStaffName) throws Exception {
		// TODO Auto-generated method stub
		try {
			//ids循环监测创建表单
			for(String s:ids.split(",")){
			TblRiskMonitoringCreation entity=tblRiskMonitoringCreationMapper.selectById(s);
			 entity.setLssuedUserId(staffids);
        	  entity.setLssuedUserName(issuedStaffName);
        	  Map<String, String> map=setUnitName(staffids);
        	  entity.setLssuedUnitId(map.get("id"));
        	  entity.setLssuedUnitName(map.get("name"));
        	  entity.setLssuedstatus(1);
        	  entity.setLssuedDate(new Date());
        	 tblRiskMonitoringCreationMapper.updateById(entity);
        	 //内层下发填报单
        	 String[] staffs=staffids.split(",");
        	 String[] unit=map.get("id").split(",");
        	 for(int i=0;i<staffs.length;i++){
        		 //验证是否是重复下发
        		 if(tblRiskMonitoringFillMapper.checkDouble(entity.getId(),staffs[i])==0){
        		 BigDecimal linkDeptId=staffMapper.selectById(staffs[i]).getOrgid();
        		 TblRiskMonitoringFill fill=new TblRiskMonitoringFill(Integer.valueOf(entity.getRiskyear().toString()),entity.getQuartername(),entity.getNotes(),new BigDecimal(staffs[i]),
        				 entity.getSecrectLevelId(), new BigDecimal(unit[i]), linkDeptId,null,null);
        		fill.setId(RandomUtil.uuBigDecimalId());
        		fill.setMonitorId(entity.getId());
        		fill.setCreatetime(new Date());
        		 tblRiskMonitoringFillMapper.insert(fill);
        	 }
        	 }
			}
         	 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, null);
	}
	public BigDecimal getDeptLinkCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		Organization fatherOrg = this.organizationMapper.selectById(deptId);
		if(fatherOrg.getOrgtype().compareTo(new BigDecimal(0))==0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgid();
	}
	
	public BigDecimal getCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		TblOrganization fatherOrg = this.organizationMapper.selectFatherOrgIdInfoByOrgId(deptId);
		if(fatherOrg.getOrgtype() == 0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgid();
	}
	
	public Map<String, String> setUnitName(String staffids)throws Exception{
		Map<String, String> map=new HashMap<String, String>();
		try {
		StringBuffer buffId=new StringBuffer();
		StringBuffer buffName=new StringBuffer();
		String[] stList=staffids.split(",");
		for(String s :stList){
			Staff staff=staffMapper.selectById(s);
			BigDecimal id=getDeptLinkCompanyNameByDeptId(staff.getOrgid());
			String name=organizationMapper.selectById(id).getOrgname();
			buffId.append(id+",");
			buffName.append(name+",");
		}
		int index = buffId.lastIndexOf(",");
		int index2 = buffName.lastIndexOf(",");
		 if (index != -1) {
			 buffId.deleteCharAt(index);
	        }
		 if (index2 != -1) {
			 buffName.deleteCharAt(index2);
	        }
		 map.put("id", buffId.toString());
		 map.put("name", buffName.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	//404客户提出，他们的下发是往404公司的，不涉及子公司的
	@Override
	public JsonBean monitoringIssuedNew(List<TblFillIssued> issueds,String ids,String token) throws Exception {
		// TODO Auto-generated method stub
		try {
		TblStaffUtil loginStaff=userProvider.get();
		if (loginStaff == null){
            return  ResponseFormat.retParam(0, 20006, null);
        }
			for(String s:ids.split(",")){
				TblRiskMonitoringCreation creation = tblRiskMonitoringCreationMapper.selectById(s);
				creation.setLssuedstatus(1);
				creation.setLssuedDate(new Date()); //修改下发状态
				tblRiskMonitoringCreationMapper.updateById(creation);
				for(TblFillIssued entity:issueds){
					if (entity.getDeptStaff()!=null&&tblRiskMonitoringFillMapper.checkDouble(creation.getId(),entity.getDeptStaff().toString()) == 0) {
						TblRiskMonitoringFill fill = new TblRiskMonitoringFill(Integer.valueOf(creation.getRiskyear().toString()),
								creation.getQuartername(), creation.getNotes(),entity.getDeptStaff(),
								creation.getSecrectLevelId(), loginStaff.getLinkOrg().getOrgid(),
								entity.getDeptId(),new Date(),entity.getVersionId());  //这个地方的部门，对应的是下发里面匹配的部门，不一定是人员所对应的部门，因为存在组织子集选人的情况
						fill.setId(RandomUtil.uuBigDecimalId());
						fill.setMonitorId(creation.getId());
						fill.setCreatetime(new Date());
						tblRiskMonitoringFillMapper.insert(fill);
						//entity
						entity.setCreatId(new BigDecimal(s));//关联风险创建的表单id
						entity.setId(RandomUtil.uuBigDecimalId());
						entity.setCreateTime(new Date());
						entity.setFillId(fill.getId());
						tblFillIssuedMapper.insert(entity); //保存下发信息
	        	 }
				}
			 
			}
         	 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean getMonDictonaryInfo(String token) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap = new HashMap<>();
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			List<TblRiskMonDictonary> dicList = tblRiskMonDictonaryMapper.getList(loginStaff.getLinkOrg().getOrgid());
			hashMap.put("data", dicList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, hashMap);

	}


	@Override
	public JsonBean saveMonDictonaryDept(String token, JSONArray arr) throws Exception {
		// TODO Auto-generated method stub
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			//先查询版本表，看该公司下是否有版本信息
			Integer version=tblMonModelVersionMapper.getMaxVersion(loginStaff.getLinkOrg().getOrgid());
			if(version==null){
				version=1;
			} else{
				version=(version+1);
			}
			//字典部门版本表
			TblMonModelVersion vEntity=new TblMonModelVersion();
			vEntity.setId(RandomUtil.uuBigDecimalId());
			vEntity.setCreateid(loginStaff.getStaffid());
			vEntity.setCreatetime(new Date());
			vEntity.setOrgid(loginStaff.getLinkOrg().getOrgid());
			vEntity.setVersion(version);
			tblMonModelVersionMapper.insert(vEntity);
			BigDecimal orgid=loginStaff.getLinkOrg().getOrgid();
			//先删除该公司下的关联数据，再重新新建
			//tblRiskMonModelMapper.removeByOrgid(orgid);
			for(Object obj:arr){
				Map<String, Object> map=(Map<String, Object>) obj;
				//JSONObject o=(JSONObject)obj;
				TblRiskMonModel model=new TblRiskMonModel();
				model.setDictonaryId(new BigDecimal(map.get("dictonaryId").toString()));
				model.setDeptId(new BigDecimal(map.get("deptId").toString()));
				model.setId(RandomUtil.uuBigDecimalId());
				model.setCreatetime(new Date());
				model.setLinkOrgID(orgid);
				model.setVersionId(vEntity.getId());
				tblRiskMonModelMapper.insert(model);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, null);
	}

  
	//获取当前公司下配置的下发部门的信息
	@Override
	public Map<String, Object> getMonDictonaryDeptList(String token) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap = new HashMap<>();
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return hashMap;
			}
			BigDecimal orgid=loginStaff.getLinkOrg().getOrgid();
			//有了版本信息，只获取最大的版本的关联
			List<TblRiskMonModel> getList=tblRiskMonModelMapper.getOrgList(orgid);
			hashMap.put("data", getList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return hashMap;
	}


	@Override
	public JsonBean getIssuedList(String token, String id) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				Map<String, Object> hashMap = new HashMap<>();
				try {
					TblStaffUtil loginStaff = userProvider.get();
					if (loginStaff == null) {
						return ResponseFormat.retParam(0, 20006, null);
					}
					List<TblFillIssued> list=tblFillIssuedMapper.selectByCreatid(id); 
					for(TblFillIssued f:list){
						 f.setDeptName(f.getDeptId()!=null?organizationService.selectNameByids(f.getDeptId()):"");
	                     f.setDeptStaffName(f.getDeptStaff()!=null?staffService.selectNameByids(f.getDeptStaff()):"");	
					}
					hashMap.put("data", list);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return ResponseFormat.retParam(1, 200, hashMap);
	}


	@Override
	public JsonBean getRiskMonDeptList(String token) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap = new HashMap<>();
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			//先根据填报ID获取对应的部门设置，这是为了查询到字典及部门匹配之前的关联
//			TblRiskMonitoringFill fill=tblRiskMonitoringFillMapper.selectById(id);
//			if(fill!=null){
			List<TblRiskMonDictonary> list=tblRiskMonDictonaryMapper.getMonDicByDept(loginStaff.getLinkOrg().getOrgid());
			hashMap.put("data", list);
			//}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, hashMap);
	}

	
	  //组织架构标记
    @Override
    public List<Tree> getNodeAllbm(BigDecimal nodeId) {
    	  List<Tree> trees=null;
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            //return (List<Tree>) tblOrganizationMapper.selectByPrimaryKey(nodeId);
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGTREEDEPTLIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGTREEDEPTLIST + nodeId);
                    trees = (List<Tree>) com.alibaba.fastjson.JSONArray.parseArray(str, Tree.class);
                } else {
                	trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganization> list = organizationMapper.findBysql(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        // Set<TblOrganization> chil = tblOrganization.getChildren();
                        Set<TblOrganization> chil = organizationMapper.findByfatherorgId(tblOrganization.getOrgid());
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
                }
            } finally {
                //JedisUtil.returnResource(jedis);
            }
        } 
        return trees;
    }
    
    private List<Tree> getNoteTreesbm(Set<TblOrganization> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (TblOrganization tblOrganization2 : chil) {
            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0) {
                continue;
            }
            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
                Tree tree = new Tree();
                Set<TblOrganization> chil2 = organizationMapper.findByfatherorgId(tblOrganization2.getOrgid());
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
                Set<TblOrganization> chil2 = organizationMapper.findByfatherorgId(tblOrganization2.getOrgid());
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

}
