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
import com.huabo.legal.oracle.entity.TblFwglNoticeOracle;
import com.huabo.legal.oracle.mapper.TblAttachmentMapper;
import com.huabo.legal.oracle.mapper.TblFwglNoticeOracleMapper;
import com.huabo.legal.oracle.service.TblFwglNoticeOracleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblFwglNoticeOracleServiceImpl implements TblFwglNoticeOracleService {


	@Resource
	private TblFwglNoticeOracleMapper tblFwglNoticeOracleMapper;

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

		Example example = new Example(TblFwglNoticeOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(code)) {
			criteria.andLike("noticecode", "%" + code + "%");
		}
		if (StringUtils.isNotBlank(name)) {
			criteria.andLike("noticename", "%" + name + "%");
		}
		example.setOrderByClause(" noticeid DESC ");
		com.github.pagehelper.PageInfo<TblFwglNoticeOracle> pageInfo1 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblFwglNoticeOracleMapper.selectByExample(example));

		PageInfo<TblFwglNoticeOracle> pageInfo = new PageInfo<TblFwglNoticeOracle>();

		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		//		pageInfo.setTlist(tblFwglNoticeOracleMapper.findbyorgidall(pageInfo, loginStaff.getCurrentOrg().getOrgid(), code, name));
		//		pageInfo.setTotalRecord(tblFwglNoticeOracleMapper.findbyorgidallCpount(loginStaff.getCurrentOrg().getOrgid(), code, name));
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
		TblFwglNoticeOracle learrningOracle = tblFwglNoticeOracleMapper.selectAllInfoById(lingid);
		if (learrningOracle == null) {
			return ResponseFormat.retParam(0, "查询失败", null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("data", learrningOracle);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean delete(Long noticeid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		tblFwglNoticeOracleMapper.deletePlannode(noticeid);
		tblAttachmentMapper.deleteAttmennotice(noticeid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean saveEnity(String token, TblFwglNoticeOracle ling, String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (ling.getNoticeid() != null) {
			tblFwglNoticeOracleMapper.updateByPrimaryKeySelective(ling);
			if (attids != null && !"".equals(attids)) {
				String[] attId = attids.split(",");
				for (String aid : attId) {
					tblAttachmentMapper.insertAttmentnotice(ling.getNoticeid(), aid);
				}
			}
		} else {
			ling.setNoticeid(RandomUtil.uuLongId());
			ling.setCreatetime(new Date());
			ling.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
			tblFwglNoticeOracleMapper.insertSelective(ling);
			if (attids != null && !"".equals(attids)) {
				String[] attId = attids.split(",");
				for (String aid : attId) {
					tblAttachmentMapper.insertAttmentnotice(ling.getNoticeid(), aid);
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("data", ling);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean findattlist(String token, Long nociteid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		List<TblAttachment> list = tblAttachmentMapper.selectAttListBynoticeid(nociteid);
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
		String code = tblFwglNoticeOracleMapper.findbycode(year + "");
		Integer integer = tblFwglNoticeOracleMapper.findbycodecount(year + "");
		Integer num = 1;
		if (code != null && code.length() > 0) {
			code = code.substring((code.length() - 2), (code.length()));
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


}
