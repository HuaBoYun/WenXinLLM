package com.huabo.compliance.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.oracle.entity.TblComplianceFileOracle;
import com.huabo.compliance.oracle.entity.TblhgglAret;
import com.huabo.compliance.oracle.mapper.TblhgglAretMapper;
import com.huabo.compliance.oracle.service.TblComplianceFileOracleService;
import com.huabo.compliance.service.FileUploadService;
import com.huabo.compliance.service.TblhgglaretService;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.util.ResponseFormat;
import com.huabo.compliance.vo.param.TblhgglAretQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:18:25
 */
@Service
public class TblhgglaretServiceimpl implements TblhgglaretService {

	@Resource
	private TblhgglAretMapper tblhgglArets;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblComplianceFileOracleService tblComplianceFileOracleService;
	
	@Resource
	private UserProvider userProvider;

	//查询列表
	@Override
	public JsonBean<PageResult<TblhgglAret>> getlistByselect(TblhgglAretQueryParam param) {
		Example example = new Example(TblhgglAret.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getBusiness())) {
			criteria.andLike("business", "%" + param.getBusiness() + "%");
		}
		if (StringUtils.isNotBlank(param.getType())) {
			criteria.andLike("type", "%" + param.getType() + "%");
		}
		example.setOrderByClause("id desc ");
		PageInfo<TblhgglAret> pageInfo = PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblhgglArets.selectByExample(example));
		PageResult<TblhgglAret> build = new PageResult<TblhgglAret>().build(pageInfo);
		return ResponseFormat.retParam(200, 200, build);
	}

	//详情
	@Override
	public JsonBean getlistByselectID(Integer id, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		List<TblhgglAret> list = tblhgglArets.getListByselectID(id);
		return ResponseFormat.retParam(1, 0, list);
	}

	//新增/修改
	@Override
	public JsonBean addandupdateListaret(TblhgglAret tblhgglAret, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		if (tblhgglAret.getId() != null) {
			//修改
			tblhgglArets.updateByPrimaryKeySelective(tblhgglAret);
		} else {
			//新增
			tblhgglArets.insertSelective(tblhgglAret);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("tblhgglAret", tblhgglAret);

		return ResponseFormat.retParam(1, 200, resultMap);
	}

	//删除
	@Override
	public JsonBean removeList(String token, Integer id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		TblhgglAret tblhgglAret = tblhgglArets.findById(id);
		tblhgglArets.deleteListByaret(id);
		//附件-删除
		if (StringUtils.isNotBlank(tblhgglAret.getFileids())) {
			List<String> fileIds = Arrays.asList(tblhgglAret.getFileids().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	/**
	 * 汇总列表查询详情
	 * @param id
	 * @param token
	 * @return
	 */
	@Override
	public JsonBean getTblhgglAret(Integer id, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		HashMap<String, Object> map = new HashMap<>();
		TblhgglAret tblhgglAret = tblhgglArets.findById(id);
		if (StringUtils.isNotBlank(tblhgglAret.getFileids())) {

		}
		if (StringUtils.isNotBlank(tblhgglAret.getFileids())) {
			List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(tblhgglAret.getFileids());
			map.put("file", file);
		}
		map.put("tblhgglAret", tblhgglAret);
		return ResponseFormat.retParam(1, 200, map);
	}
}
