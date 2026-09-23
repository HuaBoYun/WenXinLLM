package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.Tree;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.ArchiveMenuList;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.oracle.entity.TblNbsjArchiveEntity;
import com.huabo.audit.oracle.entity.TblNbsjAuthorizationEntity;
import com.huabo.audit.oracle.entity.TblNbsjOperateEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.entity.TblTargetTypeEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjdd;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjOperateMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.mapper.TblTargetTypeMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjddMapper;
import com.huabo.audit.oracle.vo.TblNbsjOperateVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbsjOperateService;
import com.huabo.audit.service.TblNbsjProjectArchiveService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjStaffSelectService;
import com.huabo.audit.service.TblReportService;
import com.huabo.audit.service.TblTargetTypeService;
import com.huabo.audit.util.DateUtils;
import com.huabo.audit.util.PageInfo;
@Service
public class TblNbsjOperateServiceImpl extends ServiceImpl<TblNbsjOperateMapper, TblNbsjOperateEntity> implements TblNbsjOperateService {

	@Autowired
	TblNbsjOperateMapper tblNbsjOperateMapper;
	
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Resource
	private TblTargetTypeMapper tblTargetTypeMapper;
	
	@Autowired
	private TblTargetTypeService tblTargetTypeService;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;
	
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
    private TblNbsjProjectArchiveService tblNbsjProjectArchiveService;
    
    @Resource
    private TblReportService tblReportService;
    
    @Resource
    private TblNbsjStaffSelectService tblNbsjStaffSelectService;
    
    
    @Autowired
    private  ImplementPlanServiceImpl implementPlanService;
    
    @Autowired
    private  ImplementPlanMapper implementPlanMapper;
    
    @Resource
    private TblYqnsSjddMapper tblYqnsSjddMapper;

    @Resource
    private UserProvider userProvider;
    
    
    
	@Override
	public boolean saveBatch(Collection<TblNbsjOperateEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblNbsjOperateEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblNbsjOperateEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblNbsjOperateEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblNbsjOperateEntity getOne(Wrapper<TblNbsjOperateEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblNbsjOperateEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblNbsjOperateEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblNbsjOperateMapper getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblNbsjOperateEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finsh(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount(Integer projectId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TblNbsjOperateEntity findByAuthId(Integer authId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjOperateEntity> findByProjectId(Integer projectid, Integer finishStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(TblNbsjOperateEntity oper) {
		// TODO Auto-generated method stub

	}

	
	//==
	@Override
	public JsonBean checkListMyPageList(String token, Integer pageNumber, Integer pageSize,
			TblNbsjOperateVo tblNbsjOperateVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	
    	PageInfo<TblNbsjOperateEntity> pageInfo = new PageInfo<TblNbsjOperateEntity>();
//    	TblNbsjOperateEntity.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjEntermeeting);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	if(null == tblNbsjOperateVo.getProjectId()){
    		//==查询当前实施的项目！
    		 ImplementPlanEntity tnp = implementPlanService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		
    		tblNbsjOperateVo.setStatus(1);
    		BigDecimal projectid = tnp.getId();
        	tblNbsjOperateVo.setProjectId(projectid);
    	}
    	tblNbsjOperateVo.setStaffid(loginStaff.getStaffid());
    	
    	pageInfo.setTlist(this.tblNbsjOperateMapper.selectListByPageInfo(pageInfo,tblNbsjOperateVo));
    	pageInfo.setTotalRecord(this.tblNbsjOperateMapper.selectCountByPageInfo(pageInfo,tblNbsjOperateVo));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean checkListMyDetail(String token, Integer programid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblAduitProGramEntity plan = this.tblNbsjOperateMapper.selectById(programid);
		resultMap.put("entermeeting", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean checkListFinal(Integer operateid, Integer programId, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblAduitProGramEntity plan = this.tblNbsjOperateMapper.selectById(programId);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
		this.tblNbsjOperateMapper.finalById(operateid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean checkAllListMyPageList(String token, Integer pageNumber, Integer pageSize,String businessType,Integer targetId)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		
		Integer projectid = tnp.getProjectId();
    	
    	PageInfo<TblNbsjOperateEntity> pageInfo = new PageInfo<TblNbsjOperateEntity>();
//    	TblNbsjOperateEntity.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjEntermeeting);

		com.github.pagehelper.PageInfo<TblNbsjOperateEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblNbsjOperateMapper.selectAllListByPageInfo(pageInfo,projectid,businessType,targetId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	

	@Override
	public JsonBean pjArchiveMyPageList(String token)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Integer projectid = tnp.getProjectId();
		TblNbsjProject pj = this.tblNbsjProjectService.getProjectById(projectid);
		
		List<ArchiveMenuList> list = tblNbsjProjectArchiveService.getArchiveMenuList();
        List<TblNbsjArchiveEntity> tblNbsjArchiveList = tblNbsjProjectArchiveService.getTblNbsjArchiveList(pj);
        resultMap.put("archiveMenuList", list);
        if(null!=tblNbsjArchiveList&&tblNbsjArchiveList.size()>0) {
        	resultMap.put("archiveMoney", tblNbsjArchiveList.get(0).getPrice());
        }
		
		
		
    	resultMap.put("project", pj);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean projRwList(String token, Integer pageNumber, Integer pageSize, Integer projectid,Integer targetId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjOperateEntity> pageInfo = new PageInfo<TblNbsjOperateEntity>();
//    	TblNbsjOperateEntity.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjEntermeeting);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectid);
//    	Integer tempId = null;
//    	if(null !=project) {
//    		tempId = project.getTbltemplete().getTempleteId();
//    	}
    	
//    	//==
//    	List<TblAduitProGramEntity> list = this.tblNbsjOperateMapper.selectRWFPListCon(pageInfo,projectid,targetId);//this.tblNbsjOperateMapper.selectRWFPListByPageInfo(pageInfo,projectid,targetId);
//		 for (TblAduitProGramEntity tblAduitProGram : list) {
//		     List<TblNbsjAuthorizationEntity> authorization = this.tblNbsjOperateMapper.getNbsjAuthorByPG(projectid, tblAduitProGram.getProgramId());
//		     if (null != authorization) {
//		    	 if(authorization.size()>0) {
//		    		 TblNbsjAuthorizationEntity auth = authorization.get(0);
//		    		 tblAduitProGram.setRenyuan(auth.getAuthStaff().getRealname());
//		    	 }
//		     }
//		 }    	
    	
    	
//    	pageInfo.setTlist(this.tblNbsjOperateMapper.selectRWFPListByPageInfo(pageInfo,tempId,targetId));//this.tblNbsjOperateMapper.selectRWFPListByPageInfo(pageInfo,projectid,targetId);
//    	pageInfo.setTotalRecord(this.tblNbsjOperateMapper.selectRWFPCountByPageInfo(pageInfo,tempId,targetId));//this.tblNbsjOperateMapper.selectRWFPCountByPageInfo(pageInfo,projectid,targetId)
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean getRwfpTree(String token, BigDecimal projectId, BigDecimal nodeId,String type) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		if(null==projectId || "".equals(projectId)) {
			//==查询当前实施的项目！
			  //当前实施项目
	        ImplementPlanEntity tnp = implementPlanService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			if(tnp == null) {
				return ResponseFormat.retParam(0,30003,resultMap);
			}
			projectId = tnp.getId();
			
			if(null == projectId) {
				return ResponseFormat.retParam(0,30003,resultMap);
			}
		}
		
		 ImplementPlanEntity plan = implementPlanMapper.selectById(projectId.toString());
		 BigDecimal tempId =plan.getTempId();
		
		String url = "";
      boolean all = false;
      String mainUrl = "";
      if (StringUtils.isNotBlank(type)) {
          switch (type) {
              // 我的清单
              case "my":
                  url = "/nbsj/sjss/check_list_my?1=1";
                  mainUrl = "/nbsj/sjss/check_list_all_my?1=1";
                  break;
              // 清单管理
              case "all":
                  url = "/nbsj/sjss/check_list_my_all?1=1";
                  mainUrl = "/nbsj/sjss/check_list_all?1=1";
                  all = true;
                  break;
              default:
                  break;
          }
      }
		
		if (null == nodeId) {
            List<Tree> tree = this.tblTargetTypeService.getRoot(tempId,mainUrl);
            resultMap.put("tree", tree);
        	return ResponseFormat.retParam(1,200,resultMap);
        } else {
            List<Tree> list = null;
            TblTargetTypeEntity tblTargetType = tblTargetTypeMapper.getById(nodeId);
//            if (all) {
                list = this.tblTargetTypeService.getTree(nodeId, tempId, url);
//            } else {
//                if (tblTargetType.getParentId() == null) {
//                    list = this.tblTargetTypeService.getTreeByUser(tempId, projectId, loginStaff.getStaffid(), url);
//                } else {
//                    list = this.tblTargetTypeService.getTreeByUser(nodeId, tempId, projectId, loginStaff.getStaffid(), url);
//                }
//            }
            
            resultMap.put("tree", list);
        	return ResponseFormat.retParam(1,200,resultMap);
            
        }
	}
	
	@Override
	public JsonBean checkAllListMyrwPageList(String token, Integer pageNumber, Integer pageSize,String businessType,BigDecimal targetId,BigDecimal projectId)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null); 
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	//==查询当前实施的项目！
    	 ImplementPlanEntity tnp = implementPlanService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    	 if(projectId!=null) {
    		 tnp = implementPlanMapper.selectById(projectId.toString());
    	 }
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		BigDecimal tempId = tnp.getTempId();
//		Integer projectid = tnp.getId().intValue();
    	
    	PageInfo<TblNbsjOperateEntity> pageInfo = new PageInfo<TblNbsjOperateEntity>();
//    	TblNbsjOperateEntity.setAuditorgid(loginStaff.getCurrentOrg().getOrgid()); 
//    	pageInfo.setCondition(tblNbsjEntermeeting);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
		com.github.pagehelper.PageInfo<TblNbsjOperateEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						List<TblNbsjOperateEntity> list = this.tblNbsjOperateMapper.selectRWFPListByPageInfo(pageInfo,tempId, targetId);
						 // 根据模板查询督导任务
						if(list!=null && list.size()>0 && projectId!=null) {
							for (TblNbsjOperateEntity op : list) {
								if(op.getAuthId()!=null) {
									List<TblYqnsSjdd> list1 = tblYqnsSjddMapper.selectByrwId(Long.valueOf(op.getAuthId()),projectId);
									if(list1!=null && list1.size()>0) {
										op.setSjddrw(list1.get(0));			                	}
									}
								}
								
						}
						
					} catch (Exception e) {;
						throw new RuntimeException(e);
					}
				});  

    	
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	

	@Override
	public JsonBean getTree(String token, Integer projectId, Integer nodeId,String type) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		if(null==projectId || "".equals(projectId)) {
			//==查询当前实施的项目！
			TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			if(tnp == null) {
				return ResponseFormat.retParam(0,30003,resultMap);
			}
			projectId = tnp.getProjectId();
			
			if(null == projectId) {
				return ResponseFormat.retParam(0,30003,resultMap);
			}
		}
		
		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
		BigDecimal tempId = project.getTbltemplete().getTempleteId();
		
		String url = "";
      boolean all = false;
      String mainUrl = "";
      if (StringUtils.isNotBlank(type)) {
          switch (type) {
              // 我的清单
              case "my":
                  url = "/nbsj/sjss/check_list_my?1=1";
                  mainUrl = "/nbsj/sjss/check_list_all_my?1=1";
                  break;
              // 清单管理
              case "all":
                  url = "/nbsj/sjss/check_list_my_all?1=1";
                  mainUrl = "/nbsj/sjss/check_list_all?1=1";
                  all = true;
                  break;
              default:
                  break;
          }
      }
		
		if (null == nodeId) {
            List<Tree> tree = this.tblTargetTypeService.getRoot(tempId,mainUrl);
            resultMap.put("tree", tree);
        	return ResponseFormat.retParam(1,200,resultMap);
        } else {
            List<Tree> list = null;
//            TblTargetTypeEntity tblTargetType = tblTargetTypeMapper.getById(nodeId);
//            if (all) {
                list = this.tblTargetTypeService.getTree(new BigDecimal(nodeId), tempId, url);
//            } else {
//                if (tblTargetType.getParentId() == null) {
//                    list = this.tblTargetTypeService.getTreeByUser(tempId, projectId, loginStaff.getStaffid(), url);
//                } else {
//                    list = this.tblTargetTypeService.getTreeByUser(nodeId, tempId, projectId, loginStaff.getStaffid(), url);
//                }
//            }
            
            resultMap.put("tree", list);
        	return ResponseFormat.retParam(1,200,resultMap);
            
        }
	}

	@Override
	public JsonBean getMyWorkTree(String token,BigDecimal projectId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//==查询当前实施的项目！
		ImplementPlanEntity tnp = implementPlanService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null && projectId==null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		if(projectId!=null) {
			tnp = implementPlanMapper.selectById(projectId.toString());
		}
		BigDecimal tempId =tnp.getTempId();
		
		String url = "/nbsj/sjss/check_list_my?1=1";
		
		List<TblTargetTypeEntity> rootList = this.tblTargetTypeMapper.getRoot(tempId);
		List<Tree> list = new ArrayList<Tree>(0);
		Tree tree = null;
		BigDecimal targetId = null;
		List<TblTargetTypeEntity> childrenList = null;
		for (TblTargetTypeEntity tar : rootList) {
			tree = new Tree();
			targetId = tar.getTargetId();
			tree.setTarget("mainFramex");
			tree.setId(tar.getTargetId());
			tree.setName(tar.getTargetName());
			tree.setType("0");
			childrenList = this.tblTargetTypeMapper.selectMyyqTarget(tnp.getId(), targetId,loginStaff.getStaffid());
			if(childrenList .size() >0 ){
				tree.setIsParent(true);
				tree.setOpen(true);
				this.setMyWorkTargetChildren(targetId,tree,childrenList,loginStaff.getStaffid(),tnp.getId(),url,tempId);
				
				
			}else{
				tree.setIsParent(false);
			}
			tree.setpId(tar.getParentId());
			if(null!=tar.getParentId()){
				tree.setUrl(url+"&targetId="+targetId+"&tempId="+tempId);
			}else{
				tree.setUrl(url+"&tempId="+tempId);
			}
			list.add(tree);
		}
		
		resultMap.put("tree", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	private void setMyWorkTargetChildren(BigDecimal parentId, Tree parentTree, List<TblTargetTypeEntity> parentList,
			BigDecimal staffid, BigDecimal projectId, String url, BigDecimal tempId) throws Exception {
		List<Tree> list = new ArrayList<Tree>(0);
		Tree tree = null;
		BigDecimal targetId = null;
		List<TblTargetTypeEntity> childrenList = null;
		
		for (TblTargetTypeEntity tar : parentList) {
			tree = new Tree();
			targetId = tar.getTargetId();
			tree.setTarget("mainFramex");
			tree.setId(tar.getTargetId());
			tree.setName(tar.getTargetName());
			tree.setType("0");
			tree.setpId(tar.getParentId());
			if(null!=tar.getParentId()){
				tree.setUrl(url+"&targetId="+targetId+"&tempId="+tempId);
			}else{
				tree.setUrl(url+"&tempId="+tempId);
			}
			childrenList = this.tblTargetTypeMapper.selectMyyqTarget(projectId, targetId,staffid);
			if(childrenList .size() > 0 ){
				tree.setIsParent(true);
				tree.setOpen(true);
				this.setMyWorkTargetChildren(targetId,tree,childrenList,staffid,projectId,url,tempId);
			}else{
				tree.setIsParent(false);
			}
			list.add(tree);
		}
		parentTree.setChildren(list);
		
		
	}

	@Override
	public JsonBean xmgdSave(String token, String selectedData, Integer numPrice, String dateEndTime, String filCode,
			String filName,Integer worktime) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Integer projectid = tnp.getProjectId();
		TblNbsjProject selectProject = this.tblNbsjProjectService.getProjectById(projectid);
		
		if (null != selectProject) {
        	//归档前，需要审批完成报告  
//        	List<TblReportEntity> reportList = tblReportService.findNoReportByProjectId(selectProject.getProjectId());
//        	if(reportList!=null) {
//        		return ResponseFormat.retParam(0,30001,resultMap);
////        		return JsonBean.error("审计报告未审批完成，无法归档");
//        	}
            // 根据项目查看当前项目是否归档
            List<TblNbsjArchiveEntity> arcList = tblNbsjProjectArchiveService.getTblNbsjArchiveList(selectProject);
            if (arcList.size() == 0) {
                try {
                    String[] selectedDatalist = selectedData.split(",");
                    for (String str : selectedDatalist) {
                        String[] strlist = str.split("-");
                        String id = strlist[0];
                        if (id != null && id != "") {
                            Integer level = Integer.parseInt(strlist[1]);
                            TblNbsjArchiveEntity objTblNbsjArchive = new TblNbsjArchiveEntity();
                            objTblNbsjArchive.setEndtime(DateUtils.parse(dateEndTime));
                            objTblNbsjArchive.setLevel(level);
                            objTblNbsjArchive.setMenucode(id);
                            objTblNbsjArchive.setObjTblnbsjProject(selectProject);
                            TblStaff user = new TblStaff();
                            user.setStaffid(loginStaff.getStaffid());
							objTblNbsjArchive.setObjTblStaff(user );
                            objTblNbsjArchive.setPrice(numPrice);
                            objTblNbsjArchive.setAcrtime(new Date());
                            objTblNbsjArchive.setWorktime(worktime);
                            tblNbsjProjectArchiveService.save(objTblNbsjArchive);//归档信息
                            selectProject.setStatus(TblNbsjProject.GD_STATUS);
//                            selectProject.setCyrrentStatre(TblNbsjProject.NO_SELECT);
                            selectProject.setFinishTime(DateUtils.parse(dateEndTime));
                            selectProject.setFilCode(filCode);//档案编号
                            selectProject.setFilName(filName);//档案名称
                            this.tblNbsjProjectMapper.updateEntity(selectProject);
                            //删除当前实施的项目,确保所有用户左侧没有已归档的项目
                            this.tblNbsjStaffSelectService.deleteByprojectId( selectProject.getProjectId());
                        } else {
                        	return ResponseFormat.retParam(0,30001,resultMap);
//                            return JsonBean.error("项目归档失败");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseFormat.retParam(0,30001,resultMap);
                }
                return ResponseFormat.retParam(1,200,resultMap);
            } else {
            	return ResponseFormat.retParam(0,30001,resultMap);
//                logger.info("项目名称为：" + selectProject.getProjectName() + "已经归档");
//                return JsonBean.error("项目已经归档");
            }
        }
		return ResponseFormat.retParam(0,30001,resultMap);
	}

	@Override
	public JsonBean getTreeZy(String token, Integer tempId, Integer nodeId, String type) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		Integer projectId = null;
		if(null==tempId || "".equals(tempId)) {
			//==查询当前实施的项目！
			TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			if(tnp == null) {
				return ResponseFormat.retParam(0,30003,resultMap);
			}
			projectId = tnp.getProjectId();
			if(null == projectId) {
				return ResponseFormat.retParam(0,30003,resultMap);
			}
			TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
//			tempId = project.getTbltemplete().getTempleteId();
			//tempId = project.getTbltempletezy().getTempleteId();
		}
		
		String url = "";
		if (StringUtils.isNotBlank(type)) {
    		switch (type) {
    		// 模板
    		case "mb":
    			url = "/nbsj/sjyj/def_cat_list_zy?1=1";//&cz="+cz+"&flview="+flview+"&isuse="+isuse+"&choiceSearch="+choiceSearch;
    			break;
    			// 分配
    		case "fp":
    			url = "/nbsj/jsfp/def_cat_list_zy?1=1";//&cz="+cz+"&choiceSearch="+choiceSearch;
    			break;
    		default:
    			break;
    		}
    	}
		
		if (null == nodeId) {
//    		List<Tree> tree = this.tblTargetTypeService.getRoot(tempId, url);//"/nbsj/sjyj/def_cat_list?1=1&cz="+cz
    		
//    		resultMap.put("tree", tree);
        	return ResponseFormat.retParam(1,200,resultMap);
//    		return JSONObject.toJSONString(tree);
    	} else {
//    		List<Tree> list = this.tblTargetTypeService.getTree(new BigDecimal(nodeId), tempId, url);
//    		
//    		resultMap.put("tree", list);
        	return ResponseFormat.retParam(1,200,resultMap);
//    		return JSONObject.toJSONString(list);
    	}
		
	}

	@Override
	public JsonBean findPorgramByUser(String token,Integer targetId, Integer pageNumber, Integer pageSize, String bsunitname)
			throws Exception {
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Integer projectid = tnp.getProjectId();
		TblNbsjProject selectProject = this.tblNbsjProjectService.getProjectById(projectid);
		 
		PageInfo<TblNbsjOperateEntity> pageInfo = new PageInfo<TblNbsjOperateEntity>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(tblNbsjOperateMapper.findPorgramByUser(pageInfo,targetId,projectid,loginStaff.getStaffid(),bsunitname));
    	pageInfo.setTotalRecord(tblNbsjOperateMapper.findCountPorgramByUser(pageInfo,targetId,projectid,loginStaff.getStaffid(),bsunitname));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean MyTaskFinish(String token, Integer operateid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		this.tblNbsjOperateMapper.finalById(operateid);
    	return ResponseFormat.retParam(1,200,null);
	}

}
