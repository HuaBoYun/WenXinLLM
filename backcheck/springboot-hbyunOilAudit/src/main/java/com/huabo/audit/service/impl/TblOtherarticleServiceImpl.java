package com.huabo.audit.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblOtherarticle;
import com.huabo.audit.oracle.mapper.TblOtherarticleMapper;
import com.huabo.audit.oracle.vo.TblOtherarticleVo;
import com.huabo.audit.service.TblOtherarticleService;
import com.huabo.audit.util.PageInfo;

import cn.hutool.core.util.StrUtil;
@Service
@Transactional(rollbackFor = Exception.class)
public class TblOtherarticleServiceImpl extends ServiceImpl<TblOtherarticleMapper, TblOtherarticle>  implements TblOtherarticleService {
    @Autowired
    private TblOtherarticleMapper tblOtherarticleMapper;
    
    @Resource
    private UserProvider userProvider;
    
	@Override
	public JsonBean selectOtherarticlePageInfo(String token, Integer pageNumber, Integer pageSize,
			TblOtherarticleVo tblOtherarticleVo) throws Exception {
		
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblOtherarticle> pageInfo = new PageInfo<TblOtherarticle>();
		com.github.pagehelper.PageInfo<TblOtherarticle> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblOtherarticleMapper.selectOtherarticlePageInfo(pageInfo,tblOtherarticleVo);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean mergeOtherarticleInfo(TblOtherarticle tblOtherarticle, String token, String attIds)  throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(tblOtherarticle.getOthartid()!= null) {
			//修改；
			//tblOtherarticleMapper.deleteTemOrg(tblOtherarticle.gettblOtherarticleId().toString());
			tblOtherarticleMapper.updateEntity(tblOtherarticle);
		}else {
			//新增；
			tblOtherarticleMapper.insertEntity(tblOtherarticle);
		}
		if(StrUtil.isNotBlank(attIds)) {
			String[] split = attIds.split(",");
			for (String string : split) {
				System.out.println(string);
				//tblOtherarticleMapper.insertTemOrg(tblOtherarticle.gettblOtherarticleId().toString(), string);
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("tblOtherarticle",tblOtherarticle);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean selectInfo(String othartid) {
		//查询
		TblOtherarticle tblOtherarticle = tblOtherarticleMapper.findInfobyid(othartid);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("tblOtherarticle",tblOtherarticle);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteInfo(String othartid) {
		//查询
		tblOtherarticleMapper.deleteInfoById(othartid);
		return ResponseFormat.retParam(1,70003,null);
	}


}
