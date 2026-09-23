package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.RiskAssessmentstd;
import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskInfludegree;
import com.huabo.fxgl.entity.RiskLevelmapping;
import com.huabo.fxgl.mapper.RiskAssessmentstdMapper;
import com.huabo.fxgl.mapper.RiskAssplanMapper;
import com.huabo.fxgl.mapper.RiskInfludegreeMapper;
import com.huabo.fxgl.mapper.RiskLevelmappingMapper;
import com.huabo.fxgl.mapper.RiskPossibilityMapper;
import com.huabo.fxgl.service.IRiskInfludegreeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
@Service
public class RiskInfludegreeServiceImpl extends ServiceImpl<RiskInfludegreeMapper, RiskInfludegree>
		implements IRiskInfludegreeService {

	@Autowired
	private RiskPossibilityMapper riskPossibilityMapper;
	@Autowired
	private RiskLevelmappingMapper riskLevelmappingMapper;
	@Autowired
	private RiskAssplanMapper riskAssplanMapper;
	@Autowired
	private RiskInfludegreeMapper riskInfludegreeMapper;
	@Autowired
	private RiskAssessmentstdMapper riskAssessmentstdMapper;

	@Override
	public BigDecimal getInflu(BigDecimal assId, String level) {
		BigDecimal degressid=new BigDecimal(0);
		try {
		Set<RiskInfludegree> infludegrees = null;
		Map<BigDecimal, RiskInfludegree> map = new HashMap<>();
		if (null == infludegrees) {
			infludegrees = baseMapper.selectListByAssstdid(assId);
			for (RiskInfludegree riskInfludegree : infludegrees) {
				map.put(riskInfludegree.getRilevel(), riskInfludegree);
			}
		}
		if(map.get(BigDecimal.valueOf(Long.parseLong(level)))!=null){
			  degressid=map.get(BigDecimal.valueOf(Long.parseLong(level))).getDegreeid();
		} 
		} catch (Exception e) {
			// TODO: handle exceptione
			e.printStackTrace();
		}
		return degressid;
	}

	@Override
	public Map<String, Object> v_list_jb(BigDecimal assstdid) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>(0);
		if (null != assstdid) {
			List<RiskAssplan> assplans = riskAssplanMapper.getRiskAssplanByAssessMentId(assstdid);
			RiskAssessmentstd assessMentsTd = riskAssessmentstdMapper.select(assstdid);
			 Set<RiskLevelmapping> mappingList=riskLevelmappingMapper.selectListByAssstdid(assstdid);
			 Set<RiskInfludegree> riskInfludegreeList=riskInfludegreeMapper.selectListByAssstdid(assstdid);
			 if (null != assessMentsTd) {
				boolean isaddMapping = false;
				if (mappingList.size() == 0) {
					isaddMapping = true;
					for (RiskInfludegree infludegree : riskInfludegreeList) {
						RiskLevelmapping levelMapping = new RiskLevelmapping();
						levelMapping.setAssstdid(assstdid);
						levelMapping.setInfludegree(infludegree.getDegreeid() + "");// setInfluDegree(infludegree.getDegreeid()+"");
						levelMapping.setPoss1(1 + "");
						levelMapping.setPoss2(2 + "");
						levelMapping.setPoss3(3 + "");
						levelMapping.setPoss4(4 + "");
						levelMapping.setPoss5(5 + "");
						riskLevelmappingMapper.insert(levelMapping);
					}
					 if(isaddMapping){
						 mappingList=riskLevelmappingMapper.selectListByAssstdid(assstdid);
					 }
				}
				List<RiskInfludegree> infludegrees = new ArrayList<RiskInfludegree>();
				infludegrees.addAll(riskInfludegreeList);
				Collections.sort(infludegrees, new Comparator<RiskInfludegree>() {
					@Override
					public int compare(RiskInfludegree o1, RiskInfludegree o2) {
						return o1.getRilevel().compareTo(o2.getRilevel());
					}
				});
				for (RiskInfludegree riskInfludegree : infludegrees) {
					riskInfludegree.setRiskLevelMapping(
							riskLevelmappingMapper.getByInfluId(riskInfludegree.getDegreeid().toString()));
				}
				result.put("assId", assstdid);
				result.put("levelMapping", infludegrees);
				if (assplans.size() > 0) {
					result.put("update", "update");
				} else {
					result.put("update", "save");
				}
			}
		}
		return result;
	}
}
