package com.huabo.system.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblLanguageBasicConfig;
import com.huabo.system.entity.TblLanguageConversionInfo;
import com.huabo.system.entity.TblLanguageTranslate;
import com.huabo.system.mapper.TblLanguageBasicConfigMapper;
import com.huabo.system.mapper.TblLanguageConversionInfoMapper;
import com.huabo.system.mapper.TblLanguageTranslateMapper;
import com.huabo.system.service.TblSystemLanguageService;

@Service
public class TblSystemLanguageServiceImpl implements TblSystemLanguageService {

	@Resource
	private TblLanguageConversionInfoMapper tblLanguageConversionInfoMapper;
	
	@Resource
	private TblLanguageBasicConfigMapper tblLanguageBasicConfigMapper;
	
	@Resource
	private TblLanguageTranslateMapper tblLanguageTranslateMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean conversionGetList(String token, Integer pageNumber, Integer pageSize, String infoname,
			String targetlanguage) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<TblLanguageConversionInfo> wrapper = new QueryWrapper<TblLanguageConversionInfo>();
		if(StringUtils.isNotBlank(infoname)) {
			wrapper.like("INFONAME", infoname);
		}
		if(StringUtils.isNotBlank(targetlanguage)) {
			wrapper.like("TARGETLANGUAGE", targetlanguage);
		}
		
		wrapper.orderByAsc("CREATEDATE");
		
		Page<TblLanguageConversionInfo> page = new Page<TblLanguageConversionInfo>(pageNumber, pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblLanguageConversionInfo> pageList = this.tblLanguageConversionInfoMapper.selectPage(page, wrapper);
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean conversionmodify(String token, String infoname, String targetlanguage, String infoid)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblLanguageConversionInfo info = new TblLanguageConversionInfo();
		info.setInfoname(infoname);
		info.setTargetlanguage(targetlanguage);
		if(StringUtils.isNotBlank(infoid)) {
			info.setModifydate(new Date());
			info.setModifystaff(staff.getStaffid());
			info.setInfoid(infoid);
			this.tblLanguageConversionInfoMapper.updateById(info);
		}else {
			info.setInfoid(RandomUtil.uuStringId());
			info.setCreatedate(new Date());
			info.setCreatestaff(staff.getStaffid());
			this.tblLanguageConversionInfoMapper.insert(info);
		}
		return ResponseFormat.retParam(1, 200, info);
	}

	@Override
	public JsonBean getTranslateList(String token, String infoid, Integer pageNumber, Integer pageSize, String trantext,
			String menuname) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblLanguageBasicConfig config = new TblLanguageBasicConfig();
		config.setTrantext(trantext);
		config.setMenuname(menuname);
		config.setInfoid(infoid);
		
		Page<TblLanguageBasicConfig> page = new Page<TblLanguageBasicConfig>(pageNumber, pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblLanguageBasicConfig> pageList = this.tblLanguageBasicConfigMapper.selectPageList(page, config);
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean translateMenger(String token, String infoid, String configid, String trantext, String menuname)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		//1.基础语言配置信息表
		TblLanguageBasicConfig config = new TblLanguageBasicConfig();
		Integer configCount = 0 ;
		QueryWrapper<TblLanguageBasicConfig> wrapper = new QueryWrapper<TblLanguageBasicConfig>();
		if(StringUtils.isBlank(configid)) {
			configid = RandomUtil.uuStringId();
			
			//校验菜单名称是否重复
			wrapper.eq("MENUNAME", menuname);
			configCount = this.tblLanguageBasicConfigMapper.selectCount(wrapper).intValue();
			if(configCount > 0) {
				return ResponseFormat.retParam(0, "菜单名称重复", null);
			}
			config.setConfigid(configid);
			config.setConfigtype(1);
			config.setCreatestaff(staff.getStaffid());
			config.setCreatetime(new Date());
			config.setMenuname(menuname);
			this.tblLanguageBasicConfigMapper.insert(config);
		}else {
			config = this.tblLanguageBasicConfigMapper.selectById(configid);
			if(config.getConfigtype() != -2) {
				wrapper.eq("MENUNAME", menuname);
				wrapper.ne("CONFIGID", configid);
				configCount = this.tblLanguageBasicConfigMapper.selectCount(wrapper).intValue();
				
				if(configCount > 0) {
					return ResponseFormat.retParam(0, "菜单名称重复", null);
				}
				
				config.setModifystaff(staff.getStaffid());
				config.setModifytime(new Date());
				config.setMenuname(menuname);
				this.tblLanguageBasicConfigMapper.updateById(config);
			}
		}
		
		//2.语言翻译表
		QueryWrapper<TblLanguageTranslate> twp = new QueryWrapper<TblLanguageTranslate>();
		twp.eq("INFOID", infoid);
		twp.eq("CONFIGID", configid);
		Integer tranCount = this.tblLanguageTranslateMapper.selectCount(twp).intValue();
		TblLanguageTranslate tran = new TblLanguageTranslate();
		if(tranCount == 0) {
			tran.setInfoid(infoid);
			tran.setConfigid(configid);
			tran.setTrantext(trantext);
			tran.setCreatedate(new Date());
			tran.setCreatestaff(staff.getStaffid());
			this.tblLanguageTranslateMapper.insert(tran);
		}else {
			tran.setTrantext(trantext);
			tran.setModifydate(new Date());
			tran.setModifystaff(staff.getStaffid());
			this.tblLanguageTranslateMapper.update(tran, twp);
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean translateClearUp(String token, String infoid, String configid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<TblLanguageTranslate> twp = new QueryWrapper<TblLanguageTranslate>();
		twp.eq("INFOID", infoid);
		twp.eq("CONFIGID", configid);
		this.tblLanguageTranslateMapper.delete(twp);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean translateRemove(String token, String infoid, String configid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		boolean flag = true;
		TblLanguageBasicConfig config = this.tblLanguageBasicConfigMapper.selectById(configid);
		if(config.getConfigtype() != -2) {
			this.tblLanguageBasicConfigMapper.deleteById(configid);
			flag = false;
		}
		
		QueryWrapper<TblLanguageTranslate> twp = new QueryWrapper<TblLanguageTranslate>();
		twp.eq("INFOID", infoid);
		twp.eq("CONFIGID", configid);
		this.tblLanguageTranslateMapper.delete(twp);
		
		if(flag) {
			return ResponseFormat.retParam(1, "系统预制菜单无法删除，已清除翻译信息", null);
		}else {
			return ResponseFormat.retParam(1, 200, null);
		}
		
	}

	@Override
	public JsonBean translateGetUserConfig(String token, String infoid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		List<TblLanguageBasicConfig> configList = this.tblLanguageBasicConfigMapper.selectConfigList(infoid);
		
		JSONObject jsonObject = new JSONObject();

		configList.forEach(item -> jsonObject.put(item.getMenuname(), item.getTrantext()));
		
		return ResponseFormat.retParam(1, 200, jsonObject);
	}

	@Override
	public JsonBean conversiondetial(String token, String infoid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblLanguageConversionInfo info = this.tblLanguageConversionInfoMapper.selectById(infoid);
		return ResponseFormat.retParam(1, 200, info);
	}

	@Override
	public JsonBean conversionremove(String token, String infoid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		QueryWrapper<TblLanguageTranslate> twp = new QueryWrapper<TblLanguageTranslate>();
		twp.eq("INFOID", infoid);
		this.tblLanguageTranslateMapper.delete(twp);
		
		
		this.tblLanguageConversionInfoMapper.deleteById(infoid);
		
		return ResponseFormat.retParam(1, 200, null);
	}
	
	

}
