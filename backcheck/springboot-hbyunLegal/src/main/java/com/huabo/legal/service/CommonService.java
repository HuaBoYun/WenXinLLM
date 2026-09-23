package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.vo.param.HeadquartersLegalQueryParam;

public interface CommonService {

	/**
	 * 查询该用户是否是总部法务人员
	 * @param param
	 * @return
	 */
	JsonBean isHeadquartersLegal(HeadquartersLegalQueryParam param);
}
