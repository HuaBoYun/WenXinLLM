package com.huabo.compliance.service;

import com.huabo.compliance.oracle.entity.TblhgglAret;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.vo.param.TblhgglAretQueryParam;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:18:23
 */

public interface TblhgglaretService {

	JsonBean<PageResult<TblhgglAret>> getlistByselect(TblhgglAretQueryParam param);

	JsonBean getlistByselectID(Integer id, String token) throws Exception;

	JsonBean addandupdateListaret(TblhgglAret tblhgglAret, String token) throws Exception;

	JsonBean removeList(String token, Integer id) throws Exception;

	/**
	 * 汇总列表查询详情
	 * @param id
	 * @param token
	 * @return
	 */
	JsonBean getTblhgglAret(Integer id, String token) throws Exception;

	/* JsonBean updaterectification (String token,Integer id,TblhgglAret tblhgglAret)throws Exception;
	 */

}
