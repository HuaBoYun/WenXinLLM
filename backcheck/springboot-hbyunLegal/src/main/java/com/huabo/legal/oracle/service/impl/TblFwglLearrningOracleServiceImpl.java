package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.oracle.entity.TblFwglLearrningOracle;
import com.huabo.legal.oracle.mapper.TblAttachmentMapper;
import com.huabo.legal.oracle.mapper.TblFwglLearrningOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLearrningOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblFwglLearrningOracleServiceImpl implements TblFwglLearrningOracleService {


	@Resource
	private TblFwglLearrningOracleMapper tblFwglLearrningOracleMapper;

	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private UserProvider userProvider;

	@Override
	public JsonBean findAll(String code, String name, Integer pageNumber, Integer pageSize, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

		Example example = new Example(TblFwglLearrningOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(code)) {
			criteria.andLike("lingcode", "%" + code + "%");
		}
		if (StringUtils.isNotBlank(name)) {
			criteria.andLike("lingdname", "%" + name + "%");
		}
		example.setOrderByClause(" lingid DESC ");
		com.github.pagehelper.PageInfo<TblFwglLearrningOracle> pageInfo1 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblFwglLearrningOracleMapper.selectByExample(example));

		PageInfo<TblFwglLearrningOracle> pageInfo = new PageInfo<TblFwglLearrningOracle>();

		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		//		pageInfo.setTlist(tblFwglLearrningOracleMapper.findbyorgidall(pageInfo, loginStaff.getCurrentOrg().getOrgid(), code, name));
		//		pageInfo.setTotalRecord(tblFwglLearrningOracleMapper.findbyorgidallCpount(loginStaff.getCurrentOrg().getOrgid(), code, name));
		pageInfo.setTlist(pageInfo1.getList());
		pageInfo.setTotalRecord((int) pageInfo1.getTotal());
		pageInfo.getTotalPage();
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean findbyid(Long lingid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblFwglLearrningOracle learrningOracle = tblFwglLearrningOracleMapper.selectAllInfoById(lingid);
		if (learrningOracle == null) {
			return ResponseFormat.retParam(0, "查询失败", null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("data", learrningOracle);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean delete(Long lingid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		tblFwglLearrningOracleMapper.deletePlannode(lingid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean saveEnity(String token, TblFwglLearrningOracle ling, String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (ling.getLingid() != null) {
			tblFwglLearrningOracleMapper.updateByPrimaryKeySelective(ling);
			//			tblFwglLearrningOracleMapper.updateEnity(ling);
			if (attids != null && !"".equals(attids)) {
				String[] attId = attids.split(",");
				for (String aid : attId) {
					tblAttachmentMapper.insertAttmentling(ling.getLingid(), aid);
				}
			}
		} else {
			ling.setLingid(RandomUtil.uuLongId());
			ling.setCreatetime(new Date());
			ling.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
			tblFwglLearrningOracleMapper.insertSelective(ling);
			//			tblFwglLearrningOracleMapper.saveEnity(ling);
			if (attids != null && !"".equals(attids)) {
				String[] attId = attids.split(",");
				for (String aid : attId) {
					tblAttachmentMapper.insertAttmentling(ling.getLingid(), aid);
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("data", ling);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean findattlist(String token, Long lingid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		List<TblAttachment> list = tblAttachmentMapper.selectAttListBylingid(lingid);
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("data", list);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean deletefj(Long attid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		tblAttachmentMapper.deleteAttmentattid(attid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean findbycode(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		Date dt = new Date();
		int yeara = dt.getYear();
		int year = yeara + 1900;
		String code = tblFwglLearrningOracleMapper.findbycode(year + "");
		Integer integer = tblFwglLearrningOracleMapper.findbycodecount(year + "");
		Integer num = 1;
		if (code != null && code.length() > 0) {
			code = code.substring((code.length() - 2), (code.length()));
			System.out.println("----------" + code);
			System.out.println(Integer.parseInt(code));
			integer = Integer.parseInt(code);
			num = Integer.parseInt(code) + 1;

		}

		if (integer < 10) {
			code = year + "0" + num;
		} else {
			code = year + "" + num;
		}

		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("data", code);
		return ResponseFormat.retParam(1, 200, resultMap);

	}

	public static void main(String[] args) {
		String str = "202303";
		System.out.println(str.substring((str.length() - 1), (str.length())));
	}


}
