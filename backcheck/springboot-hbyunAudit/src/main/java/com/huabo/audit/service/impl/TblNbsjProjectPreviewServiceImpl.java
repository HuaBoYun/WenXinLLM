package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.Tree;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblTargetTypeEntity;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.service.TblNbsjProjectPreviewService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblTargetTypeService;

@Service
public class TblNbsjProjectPreviewServiceImpl implements TblNbsjProjectPreviewService {
	
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Autowired
	public TblTargetTypeService tblTargetTypeService;
	
	@Resource
    private TblNbsjProjectService tblNbsjProjectService;
	
	@Resource
    private UserProvider userProvider;
    

	@Override
	public JsonBean getLiftMenu(String token,BigDecimal projectId) throws Exception {
		
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
		
		resultMap.put("tree", getTree(project, null));
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	private Tree getTree(TblNbsjProject project,String type){
		String[][] Menu =  new String[][]{
			{"审计指引","/nbsj/sjgl/project_look"},
			{"项目方案","/nbsj/sjzb/project_proposal_edit"},
			{"审计任务清单","#"},
			{"项目资料","/nbsj/xmzl/project_data_list?fication="+type},
			{"审计通知书","/nbsj/sjzb/notice_list"},
//			{"进场纪要","/nbsj/sjss/in_meet_record_list"},
			{"工作小结","/nbsj/xmgl/workReportList"},
			{"任务查看","#"},
			{"疑点管理","/nbsj/xmgl/dp_list"},
			{"审计取证单","/nbsj/xmzl/certificate_list?fication="+type},
			{"底稿管理","/nbsj/sjss/project_standard_listall"},
			{"审计发现","/nbsj/xmzl/question_store_list"},
//			{"事实确认书","/nbsj/sjss/confirmation_list"},
//			{"离场纪要","/nbsj/sjss/out_meet_record_list"},
			{"审计报告","/nbkz/nkbg/list?type=nbsj"},
			//{"审计建议书","/nbkz/nkbg/list?type=sj_zdy"},
//			{"审计建议书","/nbsj/sjzj/audit_suggest_list"},
			//{"整改分派","/nbsj/sjzg/question_allocate_list"},
			//{"整改跟踪","/nbsj/sjzg/index"},
			//{"整改落实","/nbsj/sjzg/index_question_rectify_impl"},
			//{"整改查询","/nbsj/sjzg/question_rectify_distory_list"},
			};
		if(type!=null && type.length()>0){
			Menu =  new String[][]{
					{"审计指引","/nbsj/sjgl/project_look?fication="+type},
					{"项目方案","/nbsj/sjzb/project_proposal_edit?fication="+type},
					{"项目资料","/nbsj/xmzl/project_data_list?fication="+type},
					{"审计通知书","/nbsj/sjzb/notice_list?fication="+type},
//					{"进场纪要","/nbsj/sjss/in_meet_record_list?fication="+type},
					{"任务查看","#"},
					//{"工作底稿","/nbsj/sjss/project_standard_listall?fication="+type},
					{"审计取证单","/nbsj/xmzl/certificate_list?fication="+type},
					{"底稿管理","/nbsj/sjss/dg_listall?fication="+type},
					{"审计发现","/nbsj/xmzl/question_store_list?fication="+type},
//					{"事实确认书","/nbsj/sjss/confirmation_list?fication="+type},
//					{"离场纪要","/nbsj/sjss/out_meet_record_list?fication="+type},
					{"审计报告","/nbkz/nkbg/list?fication="+type+"&type=nbsj"},
					//{"审计建议书","/nbkz/nkbg/list?type=sj_zdy"},
					{"审计建议书","/nbsj/sjzj/audit_suggest_list?fication="+type},
					};
		}
		Tree tree = new Tree();
		tree.setId(new BigDecimal("-1"));
		tree.setIsParent(true);
		tree.setName("项目情况一览");
		tree.setOpen(true);
		tree.setUrl("/nbsj/sjgl/project_look");
		tree.setTarget("mainFramex");
		List<Tree> children = new ArrayList<Tree>();
		for (String[] str : Menu) {
			Tree childTree = new Tree();
			if(str[0].equals("审计指引")&&project.getTbltempletezy()!=null){
				childTree.setOpen(true);
				childTree.setType("1");
				List<Tree> childtree = getTypeChildreZy(project, "/nbsj/sjyj/def_cat_list_zy?fication="+type+"&projectId="+project.getProjectId()+"&type=view","1");
				if(childtree!=null && childtree.size()>0){
				childTree.getChildren().addAll(childtree);
				childTree.setIsParent(true);
				}else{
					continue;
					//childTree.setIsParent(false);
				}
			}else if(str[0].equals("任务查看") && project.getTbltemplete()!=null){
				childTree.setOpen(true);
				childTree.setType("0");
				List<Tree> typeChildre = getTypeChildre(project, "/nbsj/sjss/check_list_my_all?fication="+type+"&projectId="+project.getProjectId()+"&type=view",null);
				if(typeChildre!=null && typeChildre.size()>0){
					childTree.getChildren().addAll(typeChildre);
					childTree.setIsParent(true);
				}else{
					//childTree.setIsParent(false);
					continue;
				}
			}else{
				childTree.setIsParent(false);
			}
			childTree.setName(str[0]);
			if(str[0].equals("任务查看") && project.getTbltemplete()==null){//没有选择审计模板
				childTree.setUrl("/nbsj/xmzl/view?url=/nbsj/sjgl/project_look");
			}else{
				childTree.setUrl("/nbsj/xmzl/view?url="+str[1]+"&projectId="+project.getProjectId());
			}
			childTree.setpId(new BigDecimal("-1"));
			childTree.setTarget("mainFramex");
			childTree.setId(new BigDecimal("0"));
			children.add(childTree);
		}
		tree.getChildren().addAll(children);
		return tree;
	}
	
	private List<Tree> getTypeChildreZy(TblNbsjProject project,String url,String type){
		BigDecimal templeteId = project.getTbltempletezy().getTempleteId();
		
		if(null == templeteId) {
			return null;
		}
		
		List<TblTargetTypeEntity> root = tblTargetTypeService.getRoot(templeteId);
		List<Tree> tree = null;
		if(root!=null && root.size()>0){
			 tree = tblTargetTypeService.getTreezy(root.get(0).getTargetId(), templeteId,url,"审计指引");
			 if(null!=type){
				 for (Tree tree2 : tree) {
					 tree2.setType(type);
					 tree2.setTopName("审计指引");
				}
			 }
		}
		return tree;
	}
	
	private List<Tree> getTypeChildre(TblNbsjProject project,String url,String type){
		BigDecimal templeteId = project.getTbltemplete().getTempleteId();
		
		if(null == templeteId) {
			return null;
		}
		
		List<TblTargetTypeEntity> root = tblTargetTypeService.getRoot(templeteId);
		List<Tree> tree = null;
		if(root!=null && root.size()>0){
			 tree = tblTargetTypeService.getTreezy(root.get(0).getTargetId(), templeteId,url,"任务查看");
			 if(null!=type){
				 for (Tree tree2 : tree) {
					 tree2.setType(type);
				}
			 }
			 for (Tree tree2 : tree) {
				 tree2.setTopName("任务查看");
			}
		}
		return tree;
	}

}
