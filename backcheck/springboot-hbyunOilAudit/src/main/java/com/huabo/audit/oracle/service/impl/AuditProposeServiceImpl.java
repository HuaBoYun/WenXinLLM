package com.huabo.audit.oracle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsProposeAdopt;
import com.huabo.audit.oracle.entity.TblYqnsProposeEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSjbgdg;
import com.huabo.audit.oracle.mapper.AuditProposeMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsProposeAdoptMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjbgSjbgdgMapper;
import com.huabo.audit.oracle.service.AuditProposeService;
import com.huabo.audit.service.impl.ReservePropertyService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @ Author: Striker dev@example.com
 * @ Date: 2023-09-11 16:15
 * @ TODO:
 **/
@Service
public class AuditProposeServiceImpl implements AuditProposeService {
    @Autowired
    AuditProposeMapper auditProposeMapper;

    @Resource
    private TblYqnsProposeAdoptMapper tblYqnsProposeAdoptMapper;
    
    @Resource
    private TblYqnsSjbgSjbgdgMapper tblYqnsSjbgSjbgdgMapper;
    
    @Resource
    private ImplementPlanMapper implementPlanMapper;

    @Resource
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;


    @Override
    public JsonBean saveOrUpdate(TblYqnsProposeEntity param, String token) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Date date = new Date();
        JsonBean jsonBean = null;
        if (param.getId() == null) {
            // ID为空，代表新增操作
            param.setId(RandomUtil.uuBigDecimalId());
            param.setAddTime(date);
            param.setOperator(loginStaff.getRealname());
            param.setOperatorId(loginStaff.getStaffid());
            param.setStatus(0);
            Integer flag = auditProposeMapper.insert(param);
            if (flag != null) {
                jsonBean = ResponseFormat.retParam(1,200, "添加成功");
            }
        } else {
            // Id不为空，更新操作
            param.setUpdateTime(date);
            Integer flag = auditProposeMapper.updateById(param);
            if (flag != null) {
                jsonBean = ResponseFormat.retParam(1,200, "成功");
            }
        }
        return jsonBean;
    }

    @Override
    public JsonBean delete(BigDecimal id) {
        Integer flag = auditProposeMapper.deleteById(id);
        return ResponseFormat.retParam(1, "删除成功", "");
    }

    @Override
    public JsonBean getProposeList(Integer pageNumber, Integer pageSize, String title) {
        HashMap<String, Object> result = new HashMap<>();
        TblStaffUtil user=null;
		try {
			user = userProvider.get();
			if(user == null) {
	              return ResponseFormat.retParam(0,20006,null);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
        
        com.huabo.audit.util.PageInfo<TblYqnsProposeEntity> info = new com.huabo.audit.util.PageInfo<>();
        PageInfo<TblYqnsProposeEntity> pageInfo;
        if (title == null) {
        	
        	QueryWrapper<TblYqnsProposeEntity> wrapper = new QueryWrapper<>();
            if(user.getRoleNames()!=null && !user.getRoleNames().contains("审理")) {
            	wrapper.eq("OPERATORID", user.getStaffid());
            	 pageInfo = PageMethod.startPage(pageNumber, pageSize, "ADDTIME desc ")
                         .doSelectPageInfo(() -> auditProposeMapper.selectList(wrapper));
	        }else {
	        	 pageInfo = PageMethod.startPage(pageNumber, pageSize, "ADDTIME desc ")
	                     .doSelectPageInfo(() -> auditProposeMapper.selectList(null));
	        }
           
        } else {
            QueryWrapper<TblYqnsProposeEntity> wrapper = new QueryWrapper<>();
            wrapper.like("TITLE", title);
            if(user.getRoleNames()!=null && !user.getRoleNames().contains("审理")) {
            	wrapper.eq("OPERATORID", user.getStaffid());
	        }
            pageInfo = PageMethod.startPage(pageNumber, pageSize, "ADDTIME desc ")
                    .doSelectPageInfo(() -> auditProposeMapper.selectList(wrapper));
            
        }
        if( pageInfo.getList()!=null &&  pageInfo.getList().size()>0) {
     	   for (TblYqnsProposeEntity pose : pageInfo.getList()) {
				if(pose.getSjbgdgid()!=null) {
					TblYqnsSjbgSjbgdg bean = this.tblYqnsSjbgSjbgdgMapper.selectById(pose.getSjbgdgid());
		        	if(bean != null && bean.getProjectId() != null) {
		        		String projectName = implementPlanMapper.selectProjectNameById(bean.getProjectId());
		        		pose.setProjectName(projectName);
		        	}

                    //构建预留字段返回
                    reservePropertyService.buildReserveProperty(bean);
				}
               //构建预留字段返回
               reservePropertyService.buildReserveProperty(pose);
			}
        }
        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());
        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        result.put("pageInfo", info);

        return ResponseFormat.retParam(1, "查询成功", result);
    }

    @Override
    public JsonBean getProposeById(BigDecimal proposeId, BigDecimal wtzgid) {
        TblYqnsProposeEntity tblYqnsProposeEntity = auditProposeMapper.selectById(proposeId);
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        if (tblYqnsProposeEntity != null) {
        	if(wtzgid != null) {
        		TblYqnsProposeAdopt adopt = this.tblYqnsProposeAdoptMapper.selectByWtzgPropoose(wtzgid,tblYqnsProposeEntity.getId());
        		resultMap.put("adopt", adopt);
        	}

            //构建预留字段返回
            reservePropertyService.buildReserveProperty(tblYqnsProposeEntity);

        	resultMap.put("tblYqnsProposeEntity", tblYqnsProposeEntity);
            return ResponseFormat.retParam(1, "查询成功", resultMap);
        } else {
            return ResponseFormat.retParam(0, "暂无此数据", resultMap);
        }
    }

	@Override
	public JsonBean saveAuditProposeAdopt(TblYqnsProposeAdopt adopt, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
		if(StringUtils.isNotBlank(adopt.getAdoptId())) {
			this.tblYqnsProposeAdoptMapper.updateById(adopt);
		}else {
			adopt.setAdoptId(RandomUtil.uuStringId());
			this.tblYqnsProposeAdoptMapper.insert(adopt);
		}
        
		return ResponseFormat.retParam(1, 200, adopt);
	}

	@Override
	public JsonBean getAuditProposeAdopt(String adoptId, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        TblYqnsProposeAdopt adopt = this.tblYqnsProposeAdoptMapper.selectById(adoptId);
        
		return ResponseFormat.retParam(1, 200, adopt);
	}
}
