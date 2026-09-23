package com.huabo.finance.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.finance.entity.BdFinancePlanDataConfig;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.mapper.BdFinancePlanDataConfigMapper;
import com.huabo.finance.mapper.BdFinanceplanMapper;
import com.huabo.finance.mapper.BdPlanSqlconfigMapper;
import com.huabo.finance.service.BdFinanceplanService;
import com.huabo.finance.vo.BdFinanceplanVo;
import com.huabo.finance.vr.BdFinancePlanDataConfigVr;
import com.huabo.finance.vr.BdFinanceplanVr;
import com.huabo.finance.vr.FaAccbookinfoVr;

/**
 * <p>
 * 公司采集配置方案信息表 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
@Service
public class BdFinanceplanServiceImpl extends ServiceImpl<BdFinanceplanMapper, BdFinanceplan> implements BdFinanceplanService {

	@Resource
	private BdFinanceplanMapper bdFinanceplanMapper;
	
	@Resource
	private BdFinancePlanDataConfigMapper bdFinancePlanDataConfigMapper;
	
	@Resource
	private BdPlanSqlconfigMapper bdPlanSqlconfigMapper;
	
	@Override
	public JsonBean save(TblStaffUtil staff, BdFinanceplan fd) throws Exception {
		List<BdFinancePlanDataConfigVr> existsConfigList = new ArrayList<BdFinancePlanDataConfigVr>();
		if(StringUtils.isNotBlank(fd.getFid())) {
			fd.setModifiedtime(new Date());
			fd.setModifier(staff.getStaffid());
			this.bdFinanceplanMapper.updateById(fd);
			existsConfigList = this.bdFinancePlanDataConfigMapper.selectListByPlanId(fd.getFid());
		}else {
			fd.setFid(RandomUtil.uuStringId());
			fd.setCreationtime(new Date());
			fd.setCreator(staff.getStaffid());
			fd.setLinkdetpid(staff.getLinkDetp().getOrgid());
			fd.setLinkorgid(staff.getCurrentOrg().getOrgid());
			fd.setFstatus(0);
			this.bdFinanceplanMapper.insert(fd);
		}
		
		
		if(fd.getDataConfigList() != null && fd.getDataConfigList().size() > 0) {
			//清除掉不存在的数据源信息
			List<BdFinancePlanDataConfig> modifyDcList = fd.getDataConfigList().stream().filter(t -> StringUtils.isNotBlank(t.getFid())).collect(Collectors.toList());
			List<BdFinancePlanDataConfig> insertDcList = fd.getDataConfigList().stream().filter(t -> StringUtils.isBlank(t.getFid())).collect(Collectors.toList());
			
			if(existsConfigList != null && existsConfigList.size() > 0) {
				boolean flag = true;
				for (BdFinancePlanDataConfigVr bpdcv : existsConfigList) {
					flag = true;
					for (BdFinancePlanDataConfig modc : modifyDcList) {
						if(modc.getFid().equals(bpdcv.getFid())) {
							flag = false;
							break;
						}
					}
					if(flag) {
						this.bdFinancePlanDataConfigMapper.deleteById(bpdcv.getFid());
					}
				}
			}
			for (BdFinancePlanDataConfig modc : modifyDcList) {
				this.bdFinancePlanDataConfigMapper.updateById(modc);
			}
			for (BdFinancePlanDataConfig indec : insertDcList) {
				indec.setFid(RandomUtil.uuStringId());
				indec.setPlanid(fd.getFid());
				this.bdFinancePlanDataConfigMapper.insert(indec);
			}
		}else {
			if(existsConfigList != null && existsConfigList.size() > 0) {
				for (BdFinancePlanDataConfigVr bpdcv : existsConfigList) {
					this.bdFinancePlanDataConfigMapper.deleteById(bpdcv.getFid());
				}
			}
		}
		
		return ResponseFormat.retParam(1, 200, fd);
	}

	@Override
	public JsonBean detail(String fid) throws Exception {
		BdFinanceplanVr bp = this.bdFinanceplanMapper.selectUniqueById(fid);
		bp.setDataConfigList(this.bdFinancePlanDataConfigMapper.selectListByPlanId(fid));
		return ResponseFormat.retParam(1, 200, bp);
	}

	@Override
	public JsonBean remove(String fid) throws Exception {
		this.bdFinanceplanMapper.deleteById(fid);
		this.bdPlanSqlconfigMapper.deleteByPlanId(fid);
		this.bdFinancePlanDataConfigMapper.deleteByPlanId(fid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean findAllList(TblStaffUtil staff, BdFinanceplanVo vo) throws Exception {
        Page<BdFinanceplanVr> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		vo.setLinkOrgId(staff.getCurrentOrg().getOrgid());
        IPage<BdFinanceplanVr> pageList  = this.bdFinanceplanMapper.selectPageInfo(page, vo);
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean getFianacePlanList(TblStaffUtil staff, String fname) throws Exception {
		QueryWrapper<BdFinanceplan> wrapper = new QueryWrapper<BdFinanceplan>();
		if(StringUtils.isNotBlank(fname)) {
			wrapper.like("FNAME",fname);
		}
		List<BdFinanceplan> planList = this.bdFinanceplanMapper.selectList(wrapper);
		return ResponseFormat.retParam(1, 200, planList);
	}

	@Override
	public JsonBean modifyStatus(String fid, Integer fstatus) throws Exception {
		this.bdFinanceplanMapper.updateStatus(fid,fstatus);
		return ResponseFormat.retParam(1, 200, null);
	}

}
