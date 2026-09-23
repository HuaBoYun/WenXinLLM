package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ProjectSuggestionNoticeEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.entity.TblYqnsOperate;
import com.huabo.audit.oracle.mapper.ProjectSuggestionNoticeMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOperateMapper;
import com.huabo.audit.service.ProjectSuggestionNoticeService;
import com.huabo.audit.util.PageInfoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;


@Service
public class ProjectSuggestionNoticeServiceImpl extends ServiceImpl<ProjectSuggestionNoticeMapper, ProjectSuggestionNoticeEntity> implements ProjectSuggestionNoticeService {
    @Autowired
    private ProjectSuggestionNoticeMapper projectSuggestionNoticeMapper;
    
    @Autowired
    private TblStaffMapper tblStaffMapper;
    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
	private TblYqnsOperateMapper tblYqnsOperateMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String id, String name) throws Exception {
        ProjectSuggestionNoticeEntity entity = new ProjectSuggestionNoticeEntity();
        TblStaffUtil user = userProvider.get();
        if (user == null ){
            return  ResponseFormat.retParam(0,20006,null);
        }

        if(StringUtils.isNotBlank(user.getDeptIds())){
            entity.setQueryDeptIds(user.getDeptIds());
        }
        if(StringUtil.isNotEmpty(name)){
            entity.setName(name);
        }
        if(StringUtil.isNotEmpty(id)){
            entity.setId(new BigDecimal(id));
        }

        TblStaff loginuser = new TblStaff();
        loginuser.setStaffid(user.getStaffid());
        loginuser.setOrgid(user.getLinkDetp().getOrgid());

        Page<ProjectSuggestionNoticeEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> projectSuggestionNoticeMapper.selectByEntity(entity,loginuser));
        PageInfo<ProjectSuggestionNoticeEntity> pageInfo = new PageInfoUtil<ProjectSuggestionNoticeEntity>().parsePageInfo(page);
        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) {
        ProjectSuggestionNoticeEntity entity = projectSuggestionNoticeMapper.findById(id);
        if(StringUtil.isNotEmpty(entity.getStaffids1())) {
        	 List<String> realnames1 = tblStaffMapper.getByIds(entity.getStaffids1());
        	 String result = realnames1.stream().collect(Collectors.joining(", "));
        	 entity.setRealname1(result);
        }
        
        if(StringUtil.isNotEmpty(entity.getStaffids2())) {
       	 List<String> realnames1 = tblStaffMapper.getByIds(entity.getStaffids2());
       	 String result = realnames1.stream().collect(Collectors.joining(", "));
       	 entity.setRealname1(result);
       }
        
        
        if(StringUtil.isNotEmpty(entity.getOrgids1())) {
          	 List<String> realnames1 = tblOrganizationMapper.getByIds(entity.getOrgids1());
          	 String result = realnames1.stream().collect(Collectors.joining(", "));
          	 entity.setOrgname1(result);
          }
        
        
        if(StringUtil.isNotEmpty(entity.getOrgids2())) {
         	 List<String> realnames1 = tblOrganizationMapper.getByIds(entity.getOrgids2());
         	 String result = realnames1.stream().collect(Collectors.joining(", "));
         	 entity.setOrgids2(result);
         }
        
       
        
        
        entity.setAttachments(projectSuggestionNoticeMapper.selectAttachmentById(Long.parseLong(entity.getId().toString())));
        return  ResponseFormat.retParam(1,200,entity);
    }

    @Override
    public JsonBean updateEntity(ProjectSuggestionNoticeEntity projectSuggestionNoticeEntity) {
        projectSuggestionNoticeMapper.updateById(projectSuggestionNoticeEntity);
        projectSuggestionNoticeMapper.deleteAttachmentByIds(projectSuggestionNoticeEntity.getId()+"");
        insertAttachment(projectSuggestionNoticeEntity);
        return ResponseFormat.retParam(1,200,projectSuggestionNoticeEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveEntity(String token, ProjectSuggestionNoticeEntity projectSuggestionNoticeEntity) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            projectSuggestionNoticeEntity.setCreateUserId(user.getStaffid().toString());
            projectSuggestionNoticeEntity.setCreateTime(new Date());
        }
        projectSuggestionNoticeEntity.setId(RandomUtil.uuBigDecimalId());
        projectSuggestionNoticeMapper.insertEntity(projectSuggestionNoticeEntity);
//        projectSuggestionNoticeMapper.insert(projectSuggestionNoticeEntity);
        insertAttachment(projectSuggestionNoticeEntity);
        return ResponseFormat.retParam(1,200,projectSuggestionNoticeEntity);
    }

    @Override
    public void deleteByIds(String ids) { 
        if(StringUtil.isNotEmpty(ids))
            projectSuggestionNoticeMapper.deleteBatchIds(Arrays.stream(ids.split(",")).collect(Collectors.toList()));
    }


    public void insertAttachment(ProjectSuggestionNoticeEntity projectSuggestionNoticeEntity){
        if(StringUtil.isNotEmpty(projectSuggestionNoticeEntity.getAttids())){
            String[] ids = projectSuggestionNoticeEntity.getAttids().split(",");
            for (String attId : ids){
                projectSuggestionNoticeMapper.insertAttachmentsWidthId(projectSuggestionNoticeEntity.getId(),attId);
            }
        }
    }

    @Override 
    public void distribute(String token,String ids, String personIds) throws Exception {
        String[] idArr = ids.split(",");
        TblStaffUtil user = userProvider.get();
        for (String id : idArr){
        	if(personIds!=null && personIds.length()>0) {
        		String[] userids = personIds.split(",");
        		tblYqnsOperateMapper.deleteoneByUserId(personIds, id);
        		for (String userid : userids) {
        			ProjectSuggestionNoticeEntity entity = projectSuggestionNoticeMapper.selectById(id);
        			 TblYqnsOperate newoper=new TblYqnsOperate();
        				newoper.setSsmkid("1511");
        				newoper.setSsmk("立项建议表");
        				newoper.setRwmc("立项建议表上报");
        				newoper.setFormid(new BigDecimal(id));
        				newoper.setFormname(entity.getName());
        				newoper.setOperid(RandomUtil.uuBigDecimalId());
//        			    newoper.setParentid(tb.getOperid());
        			    newoper.setStatus(0);
        			    newoper.setCreatestaffid(user.getStaffid()); 
        			    newoper.setCreatename(user.getRealname());
        			    newoper.setCreatedate(new Date());
        			    newoper.setRwuserid(userid);
        			    newoper.setOrgid(user.getCurrentOrg().getOrgid());
					    newoper.setOrgname(user.getCurrentOrg().getOrgname());
        			    tblYqnsOperateMapper.insert(newoper);
        			
				}
        		projectSuggestionNoticeMapper.xfxmzry(id, personIds);
        	}
        	
        	
        	
        	
        }

    }
}
