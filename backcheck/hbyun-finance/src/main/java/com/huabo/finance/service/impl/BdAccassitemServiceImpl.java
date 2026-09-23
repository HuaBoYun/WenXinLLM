package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.BdAccassitem;
import com.huabo.finance.mapper.BdAccassitemMapper;
import com.huabo.finance.service.IBdAccassitemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会计辅助核算项目 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Service
public class BdAccassitemServiceImpl extends ServiceImpl<BdAccassitemMapper, BdAccassitem> implements IBdAccassitemService {
	
	@Resource
	private BdAccassitemMapper bdAccassitemMapper;

	@Override
	public JsonBean getAccAssTreeList(TblStaffUtil staff) throws Exception {
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
						
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
			
		QueryWrapper<BdAccassitem> wrapper = new QueryWrapper<BdAccassitem>();
		wrapper.eq("FPLANID", bookInfo.getPkFinanplanid());
		wrapper.eq("DATAORIGINFLAG", -2);
		wrapper.orderByAsc("CODE");
		List<BdAccassitem> itemList = this.bdAccassitemMapper.selectList(wrapper);
		
		return ResponseFormat.retParam(1, 200, itemList);
	}

}
