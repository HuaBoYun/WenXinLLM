package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.BdAccount;
import com.huabo.finance.entity.caiji.OrgOrgs;
import com.huabo.finance.mapper.BdAccountMapper;
import com.huabo.finance.mapper.OrgOrgsMapper;
import com.huabo.finance.service.IBdAccountService;
import com.huabo.finance.vo.BdAccountVo;
import com.huabo.finance.vr.BdAccountVr;
import com.huabo.finance.vr.OrgOrgVr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会计科目基本信息 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-24
 */
@Service
public class BdAccountServiceImpl extends ServiceImpl<BdAccountMapper, BdAccount> implements IBdAccountService {

	@Resource
	private BdAccountMapper bdAccountMapper;
	
	@Resource
	private OrgOrgsMapper orgOrgsMapper;
	
	@Override
	public JsonBean getFinanceDataList(TblStaffUtil staff, BdAccountVo vo) throws Exception {
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
		
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		if(StringUtils.isBlank(vo.getPkorg())) {
			String org = this.orgOrgsMapper.selectFinanceOrgIdByOrgId(staff.getCurrentOrg().getOrgid());
			if(StringUtils.isBlank(org)) {
				return ResponseFormat.retParam(0, "请选择财务组织！", null);
			}
			vo.setPkorg(org);
		}
		List<String> fatherList = new ArrayList<String>(0);
		fatherList.add("-1");
		
//		if(StringUtils.isNotBlank(vo.getPid())) {
//			String rootid = this.bdAccountMapper.selectFatherPkAccount(vo.getPid(),vo,bookInfo);
//			vo.setFpkaccount(rootid);
//			fatherList.add(rootid);
//		}
		
		//生成父级ID存在的主键集合
		List<BdAccountVr> vrList = this.bdAccountMapper.selectAllList(vo,bookInfo);
		List<String> pkList = vrList.stream().map(BdAccountVr::getPkAccount).distinct().collect(Collectors.toList());
		for (BdAccountVr vr : vrList) {
			if(!pkList.contains(vr.getPid())) {
				fatherList.add(vr.getPid());
			}
		}
		
		//通过父级主键生成Map<Sting,List<OrgOrgVr>>
		Map<String, List<BdAccountVr>> chilMap = vrList.stream().collect(Collectors.groupingBy(BdAccountVr::getPid));
		List<BdAccountVr> rootList = new ArrayList<BdAccountVr>(0);
		for (BdAccountVr vr : vrList) {
			if(StringUtils.isBlank(vr.getPid()) || fatherList.contains(vr.getPid()) || !chilMap.containsKey(vr.getPid())) {
				rootList.add(vr);
			}
		}
		
		//生成子父级关系
		this.genPraentAccountList(rootList,chilMap);
		return ResponseFormat.retParam(1, 200, rootList);
	}

	private void genPraentAccountList(List<BdAccountVr> rootList, Map<String, List<BdAccountVr>> chilMap) throws Exception {
		List<BdAccountVr> childrenList = new ArrayList<BdAccountVr>(0);
		for (BdAccountVr acc : rootList) {
			childrenList = chilMap.get(acc.getPkAccount());
			this.genChildrenList(childrenList,chilMap);
			acc.setChildrenList(childrenList);
		}
	}

	private void genChildrenList(List<BdAccountVr> forList, Map<String, List<BdAccountVr>> chilMap) throws Exception {
		if(forList == null || forList.size() == 0) {
			return;
		}
		List<BdAccountVr> childrenList = new ArrayList<BdAccountVr>(0);
		for (BdAccountVr acc : forList) {
			childrenList = chilMap.get(acc.getPkAccount());
			this.genChildrenList(childrenList,chilMap);
			acc.setChildrenList(childrenList);
		}
	}

}
