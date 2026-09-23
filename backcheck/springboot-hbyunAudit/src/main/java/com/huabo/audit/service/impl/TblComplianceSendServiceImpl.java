package com.huabo.audit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblComplianceWeekly;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblComplianceSendMapper;
import com.huabo.audit.oracle.mapper.TblComplianceWeeklyMapper;
import com.huabo.audit.service.TblComplianceSendService;
import com.huabo.audit.util.FiexibleNameAssignment;
import com.huabo.audit.vo.param.fieldOrgStaffId;
import com.huabo.audit.vo.param.fieldOrgStaffName;

@Service
public class TblComplianceSendServiceImpl implements TblComplianceSendService {

    @Autowired
    private TblComplianceSendMapper tblComplianceSendMapper;

    @Autowired
    private TblComplianceWeeklyMapper tblComplianceWeeklyMapper; 
    
    @Resource
    public TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public JsonBean complianceWeenlyList(String token, Integer pageNumber, Integer pageSize,TblComplianceWeekly tblComplianceWeekly) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        PageInfo<TblComplianceWeekly> pageInfo = new PageInfo<TblComplianceWeekly>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        List<TblComplianceWeekly>  list= this.tblComplianceSendMapper.selectWeeklyListByPageInfo(pageInfo, tblComplianceWeekly, loginStaff);
        		FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(list)){
			list.forEach(entity->{
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
        pageInfo.setTlist(list);
        pageInfo.setTotalRecord(this.tblComplianceSendMapper.selectWeeklyCountByPageInfo(pageInfo, tblComplianceWeekly, loginStaff));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1, 200, resultMap);
    }
    
    @Override
    public JsonBean complianceWeenlySave(TblComplianceWeekly tblComplianceWeekly, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
//            Integer count = this.tblComplianceSendMapper.selectPlanCodeByOrgid(tblComplianceSend);
//            if (count > 0) {
//                return ResponseFormat.retParam(0, 202, null);
//            }
        try {
			//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
			if (tblComplianceWeekly.getId() != null) {
			    //修改；
			    this.tblComplianceWeeklyMapper.updateById(tblComplianceWeekly);
			} else {
			    //新增；
				tblComplianceWeekly.setId(RandomUtil.uuBigDecimalId());
				tblComplianceWeekly.setCreator(loginStaff.getStaffid());
				tblComplianceWeekly.setWorkUnit(loginStaff.getLinkDetp().getOrgid());
				tblComplianceWeekly.setBelongGroup(loginStaff.getCurrentOrg().getOrgid());
				tblComplianceWeekly.setCreatedTime(new Date());
			    this.tblComplianceWeeklyMapper.insert(tblComplianceWeekly);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        TblComplianceWeekly tblComplianceWeeklys=tblComplianceWeeklyMapper.selectById(tblComplianceWeekly.getId());
         System.out.println(tblComplianceWeeklys.getMemo()+"*************************************************");
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("tblComplianceWeekly", tblComplianceWeeklys);
        return ResponseFormat.retParam(1, 200, resultMap);
    }
    
    @Override
    public JsonBean complianceWeenlyDel(String id, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblComplianceWeekly tblComplianceWeekly = this.tblComplianceSendMapper.selectWeeklyById(id);

        if (tblComplianceWeekly == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }

        this.tblComplianceSendMapper.deleteWeeklyById(id);
        return ResponseFormat.retParam(1, 200, null);
    }
    
    @Override
    public JsonBean complianceWeenlyDetail(String token, String id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        TblComplianceWeekly tblComplianceWeekly = this.tblComplianceSendMapper.selectWeeklyById(id);
		try {
			FiexibleNameAssignment ment = new FiexibleNameAssignment();
			// 对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item = new fieldOrgStaffId();
			BeanUtils.copyProperties(tblComplianceWeekly, item);
			fieldOrgStaffName nameEntity = ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity, tblComplianceWeekly);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        resultMap.put("tblComplianceWeekly", tblComplianceWeekly);
        
        //查询附件
        List<TblAttachment> files=null;
        if(tblComplianceWeekly.getFileIds()!=null&&StringUtils.isNotBlank(tblComplianceWeekly.getFileIds())){
         files = this.tblAttachmentMapper.findAttachmentListInIds(tblComplianceWeekly.getFileIds());
        }
        resultMap.put("files", files);
        
        return ResponseFormat.retParam(1, 200, resultMap);
    }

}
