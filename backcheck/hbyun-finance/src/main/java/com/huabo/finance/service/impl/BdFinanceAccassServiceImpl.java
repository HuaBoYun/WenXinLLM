package com.huabo.finance.service.impl;

import com.huabo.finance.entity.caiji.BdFinanceAccass;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.mapper.BdFinanceAccassMapper;
import com.huabo.finance.service.BdFinanceAccassService;
import com.huabo.finance.vo.BdFinanceAccassVo;
import com.huabo.finance.vo.GlAssBalaneVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会计辅助信息 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Service
public class BdFinanceAccassServiceImpl extends ServiceImpl<BdFinanceAccassMapper, BdFinanceAccass> implements BdFinanceAccassService {
	
	@Resource
	private BdFinanceAccassMapper bdFinanceAccassMapper;

	@Override
	public JsonBean getFinanceAccAssInfoList(TblStaffUtil staff, BdFinanceAccassVo vo) throws Exception {
		//获取用户所选择的账簿
		FaAccbookinfoUtil bookInfo = staff.getAccbook();
						
		if(bookInfo == null) {
			return ResponseFormat.retParam(0, 204, null);
		}
		
		if(StringUtils.isBlank(vo.getPkAccassitem())) {
			return ResponseFormat.retParam(0, "请选择辅助类型！", null);
		}
		
		Page<BdFinanceAccass> page = new Page<BdFinanceAccass>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<BdFinanceAccass> pageList = this.bdFinanceAccassMapper.selectPageList(page, vo,bookInfo);
				
		return ResponseFormat.retParam(1, 200, pageList);
	}

}
