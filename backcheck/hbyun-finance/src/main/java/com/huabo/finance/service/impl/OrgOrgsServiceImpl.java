package com.huabo.finance.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.entity.caiji.OrgOrgs;
import com.huabo.finance.mapper.OrgOrgsMapper;
import com.huabo.finance.service.OrgOrgsService;
import com.huabo.finance.vo.OrgOrgsVo;
import com.huabo.finance.vr.OrgOrgVr;

/**
 * <p>
 * 财务组织服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-19
 */
@Service
public class OrgOrgsServiceImpl extends ServiceImpl<OrgOrgsMapper, OrgOrgs> implements OrgOrgsService {
	
	@Resource
	private OrgOrgsMapper orgOrgsMapper;

	@Override
	public JsonBean getList(TblStaffUtil staff, OrgOrgsVo vo) throws Exception {
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
		
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		QueryWrapper<OrgOrgs> wrapper = new QueryWrapper<OrgOrgs>();
		
		if(StringUtils.isNotBlank(bookInfo.getPkFinanplanid())) {
			wrapper.eq("FPLANID", bookInfo.getPkFinanplanid());
			wrapper.eq("DATAORIGINFLAG", -2);
		}
		
		if(StringUtils.isNotBlank(vo.getCode())) {
			wrapper.like("CODE", vo.getCode());
		}
		
		if(StringUtils.isNotBlank(vo.getInnercode())) {
			wrapper.like("INNERCODE", vo.getInnercode());
		}
		if(StringUtils.isNotBlank(vo.getMnecode())) {
			wrapper.like("MNECODE", vo.getMnecode());
		}
		if(StringUtils.isNotBlank(vo.getName())) {
			wrapper.like("NAME", vo.getName());
		}
		if(StringUtils.isNotBlank(vo.getShortname())) {
			wrapper.like("SHORTNAME", vo.getShortname());
		}
		Page<OrgOrgs> page = new Page<OrgOrgs>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<OrgOrgs> pageList = this.orgOrgsMapper.selectPage(page, wrapper);
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean setCompanyInfo(TblStaffUtil staff, String pkOrg, BigDecimal orgId) throws Exception {
		this.orgOrgsMapper.removeOtherCompanyInfo(orgId);
		this.orgOrgsMapper.updateCompanyInfo(pkOrg,orgId);
		return ResponseFormat.retParam(1, 200);
	}

	@Override
	public JsonBean getTreeList(TblStaffUtil staff, OrgOrgsVo vo) throws Exception {
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
		
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		QueryWrapper<OrgOrgs> wrapper = new QueryWrapper<OrgOrgs>();
		
		if(StringUtils.isNotBlank(bookInfo.getPkFinanplanid())) {
			wrapper.eq("FPLANID", bookInfo.getPkFinanplanid());
			wrapper.eq("DATAORIGINFLAG", -2);
		}
		
		List<OrgOrgVr> orgList = this.orgOrgsMapper.selectAllList(vo,bookInfo);
		
		List<OrgOrgVr> rootList = orgList.stream().filter(item -> StringUtils.isBlank(item.getPkFatherorg()) || "-1".equals(item.getPkFatherorg())).collect(Collectors.toList());
		
		List<OrgOrgVr> chilList = orgList.stream().filter(item -> StringUtils.isNotBlank(item.getPkFatherorg()) && !"-1".equals(item.getPkFatherorg())).collect(Collectors.toList());
		
		if(rootList == null || rootList.size() == 0) {
			return ResponseFormat.retParam(0, "未采集到根节点公司信息！", null);
		}
		
		//通过父级主键生成Map<Sting,List<OrgOrgVr>>
		Map<String, List<OrgOrgVr>> chilMap = chilList.stream().collect(Collectors.groupingBy(OrgOrgVr::getPkFatherorg));
		
		//根公司放入子集
		this.genOrgTreeList(rootList,chilMap);
		return ResponseFormat.retParam(1, 200, rootList);
	}

	private void genOrgTreeList(List<OrgOrgVr> rootList, Map<String, List<OrgOrgVr>> chilMap ) throws Exception {
		List<OrgOrgVr> childrenList = new ArrayList<OrgOrgVr>(0);
		for (OrgOrgVr root : rootList) {
			childrenList = chilMap.get(root.getPkOrg());
			this.genChildrenTreeList(childrenList,chilMap);
			root.setChildrenList(childrenList);
		}
	}

	private void genChildrenTreeList(List<OrgOrgVr> forList, Map<String, List<OrgOrgVr>> chilMap) throws Exception {
		if(forList == null || forList.size() == 0) {
			return;
		}
		List<OrgOrgVr> childrenList = new ArrayList<OrgOrgVr>(0);
		for (OrgOrgVr org : forList) {
			childrenList = chilMap.get(org.getPkOrg());
			this.genChildrenTreeList(childrenList,chilMap);
			org.setChildrenList(childrenList);
		}
		
	}

}
