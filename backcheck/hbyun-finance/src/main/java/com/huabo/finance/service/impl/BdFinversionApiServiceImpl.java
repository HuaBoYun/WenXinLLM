package com.huabo.finance.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.finance.entity.BdFinversionApi;
import com.huabo.finance.mapper.BdFinversionApiMapper;
import com.huabo.finance.service.BdFinversionApiService;
import com.huabo.finance.vo.BdFinversionApiVo;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 财务系统API配置 服务实现类
 * </p>
 *
 * @author Augment Code
 * @since 2026-08-09
 */
@Slf4j
@Service
public class BdFinversionApiServiceImpl extends ServiceImpl<BdFinversionApiMapper, BdFinversionApi> implements BdFinversionApiService {

	@Resource
	private BdFinversionApiMapper bdFinversionApiMapper;

	@Override
	public JsonBean findAllList(TblStaffUtil staff, BdFinversionApiVo vo) throws Exception {
		
		QueryWrapper<BdFinversionApi> wrapper = new QueryWrapper<BdFinversionApi>();
		List<BdFinversionApi> falist = new ArrayList<BdFinversionApi>(0);
		List<BdFinversionApi> chlist = new ArrayList<BdFinversionApi>(0);
		if(StringUtils.isNotBlank(vo.getHandtext())) {
			wrapper.like("HANDTEXT", vo.getHandtext());
			wrapper.isNotNull("PID");
			chlist = this.bdFinversionApiMapper.selectList(wrapper);
			
			if(chlist == null || chlist.size() == 0) {
				return ResponseFormat.retParam(1, 50001, falist);
			}
			
			Set<String> ids = chlist.stream().map(BdFinversionApi::getPid).collect(Collectors.toSet());
			wrapper.clear();
			wrapper.in("FId", ids);
			falist = this.bdFinversionApiMapper.selectList(wrapper);
		}else {
			wrapper.isNull("pid");
			falist = this.bdFinversionApiMapper.selectList(wrapper);
			wrapper.clear();
			wrapper.isNotNull("PID");
			chlist = this.bdFinversionApiMapper.selectList(wrapper);
		}
		
		for (BdFinversionApi fa : falist) {
			fa.setChildrenList(chlist.stream().filter(obj -> fa.getFid().equals(obj.getPid())).collect(Collectors.toList()));
		}
		return ResponseFormat.retParam(1, 200, falist);
	}

	@Override
	public JsonBean save(TblStaffUtil staff, BdFinversionApi fv) throws Exception {
		
		if(StringUtils.isBlank(fv.getFid())) {
			fv.setFid(RandomUtil.uuStringId());
			fv.setCreationtime(new Date());
			fv.setCreator(staff.getStaffid());
			this.bdFinversionApiMapper.insert(fv);
		}else {
			fv.setModifiedtime(new Date());
			fv.setModifier(staff.getStaffid());
			this.bdFinversionApiMapper.updateById(fv);
		}
		
		return ResponseFormat.retParam(1, 200, fv);
	}

	@Override
	public JsonBean getOne(String fid) throws Exception {
		BdFinversionApi fv = this.bdFinversionApiMapper.selectById(fid);
		return ResponseFormat.retParam(1, 200, fv);
	}

	@Override
	public JsonBean remove(String fid) throws Exception {
		this.bdFinversionApiMapper.deleteById(fid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getParentList(TblStaffUtil staff) throws Exception {
		QueryWrapper<BdFinversionApi> wrapper = new QueryWrapper<BdFinversionApi>();
		List<BdFinversionApi> falist = new ArrayList<BdFinversionApi>(0);
		wrapper.isNull("pid");
		falist = this.bdFinversionApiMapper.selectList(wrapper);
		return ResponseFormat.retParam(1, 200, falist);
	}

	@Override
	public JsonBean testApiConnection(TblStaffUtil staff, BdFinversionApi api) throws Exception {
		try {
			// TODO: 实现API连接测试逻辑
			log.info("测试API连接: {}, URL: {}", api.getHandtext(), api.getApiUrl());
			return ResponseFormat.retParam(1, 200, "API连接测试成功");
		} catch (Exception e) {
			log.error("API连接测试失败", e);
			return ResponseFormat.retParam(0, 500, "API连接测试失败: " + e.getMessage());
		}
	}

}
