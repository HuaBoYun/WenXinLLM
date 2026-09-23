package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblGroupTemplateDetail;
import com.huabo.monitor.entity.TblGroupTestplan;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.entity.TblTestTemplate;
import com.huabo.monitor.entity.TblTestelement;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTesttask;
import com.huabo.monitor.mapper.TblAttachmentMapper;
import com.huabo.monitor.mapper.TblGroupTemplateDetailMapper;
import com.huabo.monitor.mapper.TblGroupTestplanMapper;
import com.huabo.monitor.mapper.TblOrganizationMapper;
import com.huabo.monitor.mapper.TblStaffMapper;
import com.huabo.monitor.mapper.TblTestelementMapper;
import com.huabo.monitor.mapper.TblTestplanMapper;
import com.huabo.monitor.mapper.TblTesttaskMapper;
import com.huabo.monitor.mapper.TblGroupTestplanMapper;
import com.huabo.monitor.entity.TblGroupTestplan;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblGroupTestplanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.mime.Attachment;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TblGroupTestplanServiceImpl extends ServiceImpl<TblGroupTestplanMapper, TblGroupTestplan> implements ITblGroupTestplanService{ 

    @Resource
    private TblGroupTestplanMapper tblGroupTestplanMapper;
    
    @Resource
    private TblTestplanMapper tblTestplanMapper;
    
    @Resource
    private TblStaffMapper tblStaffMapper;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private  TblAttachmentMapper  attachmentMapper;
    
    @Resource
    private  TblGroupTemplateDetailMapper  tblGroupTemplateDetailMapper;

    @Resource
    private  TblTestelementMapper TblTestelementMapper;
    
	@Resource
	private TblTesttaskMapper tblTesttaskMapper;
	
	@Resource
	private TblAssessServiceImpl assImpl;
	
	@Resource
	private ITblStaffService tblStaffService;
    
    @Override
    public void savePlan(TblGroupTestplan TblGroupTestplan) throws Exception{
    	TblGroupTestplan.setId(RandomUtil.uuBigDecimalId());
    	tblGroupTestplanMapper.insert(TblGroupTestplan);  //insertEntity
    	   if(TblGroupTestplan.getDetailsList().size()>0){
    		tblGroupTemplateDetailMapper.delGroupTemplateDetail(TblGroupTestplan.getId());
           	for(TblGroupTemplateDetail t:TblGroupTestplan.getDetailsList()){
           		t.setGroupid(TblGroupTestplan.getId());
           		t.setId(RandomUtil.uuBigDecimalId());
           		tblGroupTemplateDetailMapper.insert(t);
           	}
           }
    }
    
    @Override
    public void update(TblGroupTestplan TblGroupTestplan) throws Exception{
        tblGroupTestplanMapper.updateById(TblGroupTestplan); //Entity
        if(TblGroupTestplan.getDetailsList().size()>0){
 		   tblGroupTemplateDetailMapper.delGroupTemplateDetail(TblGroupTestplan.getId());
           	for(TblGroupTemplateDetail t:TblGroupTestplan.getDetailsList()){
           		if(t.getId()==null||t.getId().compareTo(new BigDecimal("0"))==0){
           		    t.setGroupid(TblGroupTestplan.getId());
           		    t.setId(RandomUtil.uuBigDecimalId());
           		    tblGroupTemplateDetailMapper.insert(t);
               }else{
            	    t.setGroupid(TblGroupTestplan.getId());
              	    tblGroupTemplateDetailMapper.updateById(t);
           		}
           	}
           }
    }

    @Override
    public void add(TblGroupTestplan TblGroupTestplan) throws Exception{
    	TblGroupTestplan.setId(RandomUtil.uuBigDecimalId());
        tblGroupTestplanMapper.insert(TblGroupTestplan);
    }


    @Override
    public TblGroupTestplan getById(BigDecimal testplanid) {

        return tblGroupTestplanMapper.selectById(testplanid);
    }

 
 

    @Override
    public void deleteById(BigDecimal testplanid) {
        tblGroupTestplanMapper.deleteById(testplanid);
    }

 
 

    @Override
	public JsonBean toIssued(String id, String staffids, String staffnames) throws Exception {
		try {
            	  //修改集团测试计划下发状态、时间、下发人员
			TblGroupTestplan entity=tblGroupTestplanMapper.selectById(id);
            	  entity.setIssuedStaffid(staffids);
            	  entity.setIssuedStaffName(staffnames);
            	  Map<String, String> map=setUnitName(staffids);
            	  entity.setLssuedUnit(map.get("id"));
            	  entity.setIssuedUnitName(map.get("name"));
            	  entity.setToIssued(new BigDecimal(1));//确认下发状态
            	  entity.setIssueddate(new Date()); //下发时间
            	  tblGroupTestplanMapper.updateById(entity);
            /*	//将下发人员信息填充到评估计划中，通过人员信息循环新建子公司评估计划信息
            	  if(StringUtils.isNotBlank(staffids)){
            		  for(String s:staffids.split(",")){
            			  TblStaff  staff=tblStaffMapper.selectById(s);
            			  BigDecimal orgid=getDeptLinkCompanyNameByDeptId(staff.getOrgid());
            			  String orgname=tblOrganizationMapper.selectById(orgid).getOrgname();
            			  //已下发过的数据不能再新建  子公司季度数据
            			  System.out.println("****"+tblTestplanMapper.getCountById(entity.getId(), s)+"%%%");
                          if(tblTestplanMapper.getCountById(entity.getId(), s)==0){
                        	  TblTestplan plan=new TblTestplan(entity);
                              //当前用户选择的组织
                        	  plan.setCreatetime(new Date());
                        	  plan.setIsgroup(new BigDecimal("1"));//集团下发数据的标记
                        	  plan.setLinkorgid(orgid);
                        	  plan.setLinkdeptid(staff.getOrgid());
                        	  plan.setStaffid(staff.getStaffid());
                        	  plan.setCreatid(staff.getStaffid());
                        	  String deptname=tblOrganizationMapper.selectById(staff.getOrgid()).getOrgname();
                        	  plan.setPlanmadedep(deptname);
                          	  plan.setOrgid(orgid);
                          	  plan.setTestedorgs(orgname);
                          	  plan.setPlanstatus("未启动");
                        	  String number=  assImpl.findFlowNextIdNk("TBL_TESTPLAN", "PLANNUMBER", "ORGID", orgid,282,  null, null, null);
                        	  plan.setPlannumber(number);
                        	  plan.setPlanleader(staff.getRealname());
                        	  plan.setSecrectLevelId(entity.getSecrectLevelId());
                        	  tblTestplanMapper.insert(plan);
                        		List<TblTestelement> list = TblTestelementMapper.findByPlanidAll(plan.getTestplanid().toString());
                        		if (list != null && list.size() > 0) {
                        			for (TblTestelement tblTestElement : list) {
                        				TblTesttask task = new TblTesttask();
                        				task.setElementid(tblTestElement.getElementid());
                        				task.setPlanid(plan.getTestplanid());
                        				task.setCompletestaus(new BigDecimal(0));
                        				task.setTesttaskid(RandomUtil.uuBigDecimalId());
                        				tblTesttaskMapper.insert(task);
                        			}
                        		}
                              //附件处理-1.查询集团附件信息，新增附件信息到子公司
                        	  List<TblAttachment> tblAttachments =attachmentMapper.getAttList("TBL_GROUPTESTPLAN_ATT", "id", entity.getId());
                              for(TblAttachment a:tblAttachments){
                            	  TblAttachment att=new TblAttachment(a);
                            	  att.setUploadtime(LocalDateTime.now());
                            	  att.setUploader(staff.getRealname());
                            	 attachmentMapper.insert(att); //新增附件信息
                            	 //插入到评估计划附件表中
                            	// RiskAssplanAtt riskAssplanAtt = new RiskAssplanAtt(tblRiskAssplan.getAssplanid(), att.getAttid());
                            	//  riskAssplanAttMapper.insert(riskAssplanAtt);
                              }
                          }
                    	 
            		  }
            	  }*/
             // }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, null);
	}
    public Map<String, String> setUnitName(String staffids)throws Exception{
		Map<String, String> map=new HashMap<String, String>();
		try {
		StringBuffer buffId=new StringBuffer();
		StringBuffer buffName=new StringBuffer();
		String[] stList=staffids.split(",");
		for(String s :stList){
	TblStaff staff=tblStaffMapper.selectById(s);
			BigDecimal id=getDeptLinkCompanyNameByDeptId(staff.getOrgid());
			String name=tblOrganizationMapper.selectById(id).getOrgname();
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
	
    public BigDecimal getDeptLinkCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		TblOrganization fatherOrg = this.tblOrganizationMapper.selectById(deptId);
		if(fatherOrg.getOrgtype()==0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgid();
	}
	
	public BigDecimal getCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		TblOrganization fatherOrg = this.tblOrganizationMapper.selectFatherOrgIdInfoByOrgIdIss(deptId);
		if(fatherOrg.getOrgtype() == 0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgid();
	}

	@Override
	public JsonBean startIssued(String id, String token) throws Exception {
		// TODO Auto-generated method stub
		 TblGroupTestplan entity=null;
		try {
      	  //修改集团测试计划下发状态、时间、下发人员
		  entity=tblGroupTestplanMapper.selectById(id);
		  List<TblGroupTemplateDetail>  details=tblGroupTemplateDetailMapper.getListByGroupid(entity.getId());
		  String staffids = details.stream()
	                .map(TblGroupTemplateDetail::getStaffid)
	                .filter(amount -> amount != null)
	                .distinct()
	                .map(BigDecimal::toString)
	                .collect(Collectors.joining(","));
		  if(StringUtils.isBlank(staffids)){
			  return ResponseFormat.retParam(0, 10002, null);
		  }
      	  entity.setIssuedStaffid(staffids);
      	  entity.setIssuedStaffName(  tblStaffService.selectNamesByids(staffids));
      	  Map<String, String> map=setUnitName(staffids);
      	  entity.setLssuedUnit(map.get("id"));
      	  entity.setIssuedUnitName(map.get("name"));
      	  entity.setToIssued(new BigDecimal(1));//确认下发状态
      	  entity.setIssueddate(new Date()); //下发时间
      	  tblGroupTestplanMapper.updateById(entity);
      	  StringBuffer buffer=new StringBuffer();
		  buffer.append("[");
          for(String str:staffids.split(",")){
        	buffer.append("{\"formId\":"+id+",\"distributionTitle\":\""+entity.getPlanname()+"\",\"reciver\":"+str+",\"isread\":0,\"moduleType\":\"nkhg\"}");
          }
          entity.setJsonString(buffer.append("]").toString());
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		return null;
	}
	 return ResponseFormat.retParam(1, 200, entity);
}
}
