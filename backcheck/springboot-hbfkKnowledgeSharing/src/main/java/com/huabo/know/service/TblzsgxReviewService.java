package com.huabo.know.service;

import com.hbfk.util.JsonBean;
import com.huabo.know.page.BasePageParam;
import com.huabo.know.vo.param.CreateTermsParam;
import com.huabo.know.vo.param.TermsListParam;

import java.util.List;

public interface TblzsgxReviewService {

	/**
	 * 查询风险清单
	 * @return
	 * @throws Exception
	 */
	JsonBean getReviewCheckList(BasePageParam pageParam)  throws Exception;

	/**
	 * 查询风险清单信息详情
	 * @return
	 */
	JsonBean getReviewItemTree(String reviewId) throws Exception;


	JsonBean getReviewItemInfo(String reviewItemId) throws Exception;
	}
