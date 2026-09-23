package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.GlDetail;
import com.huabo.finance.vo.GlDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 凭证明细 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-26
 */
public interface IGlDetailService extends IService<GlDetail> {

	JsonBean getFinanceDataList(TblStaffUtil staff, GlDetailVo vo) throws Exception;

	JsonBean getFinanceDataDetailBookList(TblStaffUtil staff, GlDetailVo vo) throws Exception;

	JsonBean getFinanceDataDiaryBookList(TblStaffUtil staff, GlDetailVo vo) throws Exception;

}
