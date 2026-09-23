package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskPossibility;
import com.huabo.fxgl.mapper.RiskAssplanMapper;
import com.huabo.fxgl.mapper.RiskInfludegreeMapper;
import com.huabo.fxgl.mapper.RiskPossibilityMapper;
import com.huabo.fxgl.service.IRiskPossibilityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Service
public class RiskPossibilityServiceImpl extends ServiceImpl<RiskPossibilityMapper, RiskPossibility> implements IRiskPossibilityService {

    @Autowired
    private RiskPossibilityMapper riskPossibilityMapper;
    
    @Autowired
    private RiskAssplanMapper riskAssplanMapper;
    @Autowired
    private RiskInfludegreeMapper riskInfludegreeMapper;
	
	
	@Override
	public Map<String, Object> v_list_knx(BigDecimal assstdid) throws Exception {
		// TODO Auto-generated method stub
 	 Map<String,Object> result = new HashMap<String,Object>(0);
		if(null!=assstdid){
			List<RiskAssplan> assplans = riskAssplanMapper.getRiskAssplanByAssessMentId(assstdid);
			List<RiskPossibility> list = riskPossibilityMapper.findAllByAssId(assstdid);
			Collections.sort(list,new Comparator<RiskPossibility>() {
				@Override
				public int compare(RiskPossibility o1, RiskPossibility o2) {
					 return o1.getRplevel().compareTo(o2.getRplevel());
				}
			}); 
			result.put("list", list);
			result.put("assId",assstdid);
			if(assplans.size()>0){
				result.put("update", "update");
			}else{
				result.put("update", "save");
			}
			
		}
		return result;
	}
}
