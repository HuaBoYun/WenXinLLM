package com.huabo.monitor.service.impl;

import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblEvaluateDefects;
import com.huabo.monitor.entity.TblRepAtt;
import com.huabo.monitor.entity.TblReport;
import com.huabo.monitor.mapper.TblAssesslevelMapper;
import com.huabo.monitor.mapper.TblEvaluateDefectsMapper;
import com.huabo.monitor.service.ITblAssesslevelService;
import com.huabo.monitor.service.TblEvaluateDefectsService;
import com.huabo.monitor.util.ConstClass;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cj
 * @since 2024-12-16
 */
@Service
public class TblEvaluateDefectsServiceImpl extends ServiceImpl<TblEvaluateDefectsMapper, TblEvaluateDefects> implements TblEvaluateDefectsService {

	@Resource 
	TblEvaluateDefectsMapper tblEvaluateDefectsMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public PageInfo<TblEvaluateDefects> getHomepage_List(Integer pageNumber,
			Integer pageSize, Integer authorityType, TblEvaluateDefects def) throws Exception {
		// TODO Auto-generated method stub
		PageInfo<TblEvaluateDefects>  pageInfo=null;
		try {
	        pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblEvaluateDefectsMapper.getHomepage_List(def,authorityType));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return pageInfo;
	}

	@Override
	public Map<String, Object> saveOrUpdate(String token, TblEvaluateDefects entity, String attids) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map=new HashMap<String, Object>();
		try {
			TblStaffUtil staff = userProvider.get();
			if(entity.getId()!=null&&entity.getId().compareTo(new BigDecimal("0"))!=0){
				tblEvaluateDefectsMapper.updateById(entity);
			}else{
				entity.setCreatestaffid(staff.getStaffid());
				entity.setUnit(staff.getLinkOrg().getOrgid());
				entity.setCreatetime(new Date());
				tblEvaluateDefectsMapper.insert(entity);
			    if (StringUtils.isNotBlank(attids)) {
		            String[] ids = attids.split(",");
		            TblRepAtt  repAtt=new TblRepAtt();
		            for (int i = 0; i < ids.length; i++) {
		                tblEvaluateDefectsMapper.insertAtt(new BigDecimal(ids[i]), entity.getId());
		            }
		         }
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TblEvaluateDefects getDetatilsById(BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		TblEvaluateDefects entity=null;
		try {
			entity=tblEvaluateDefectsMapper.getDetatilsById(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public String deleteById(BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		try {
			tblEvaluateDefectsMapper.deleteById(id);
			
	      this.tblEvaluateDefectsMapper.removeAttById(id);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return JsonBean.error();
		}
		return JsonBean.success();
	}

	@Override
	public List<TblEvaluateDefects> exportPjqx(Integer authorityType, TblEvaluateDefects def) throws Exception {
		List<TblEvaluateDefects> list=null;
        try {
        	list=tblEvaluateDefectsMapper.getHomepage_List(def,authorityType);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<TblAttachment> getAttList(BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		List<TblAttachment> attlist=null;
		try {
			attlist=tblEvaluateDefectsMapper.getAttList(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return attlist;
	}


}
