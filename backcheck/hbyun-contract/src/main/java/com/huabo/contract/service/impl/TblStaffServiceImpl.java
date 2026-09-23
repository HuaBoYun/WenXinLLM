package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollectionUtil;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblFormElements;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.entity.TblStaff;
import com.huabo.contract.mapper.TblOrganizationMapper;
import com.huabo.contract.mapper.TblStaffMapper;
import com.huabo.contract.service.TblStaffService;
import com.huabo.contract.util.HttpClient;
import com.huabo.contract.vo.StaffResult;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;


@Service()
@Slf4j
public class TblStaffServiceImpl implements TblStaffService {

	@Resource
	public TblStaffMapper tblStaffMapper;

	@Resource
	public TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
	private UserProvider userProvider;
	
	private static final String formurl= ResourceBundle.getBundle("setting/process").getString("formurl").toString();

	@Override
    public Map<String, Object> findAllPageBeanPid(Integer pageNumber, Integer pageSize, BigDecimal orgid,TblOrganization organization) {
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
			/*PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			pageInfo.setTlist(tblStaffMapper.selectListByPageInfo(pageInfo, orgid, organization));
			pageInfo.setTotalRecord(tblStaffMapper.selectListByPageInfoCount(orgid, organization));
			resultMap.put("code", "1");
			resultMap.put("msg", "访问接口成功");
			resultMap.put("pageInfo", pageInfo);*/
			return resultMap;
    }

	@Override
	public TblStaff findById(String staffid) throws Exception {
		return tblStaffMapper.findByStaffid(staffid);
	}

	@Override
	public void updateTs(TblStaff ts) throws Exception {
		tblStaffMapper.updateTs(ts);
	}

	@Override
	public StaffResult getUserInfoExam(Integer staffId, Integer orgid, String type) throws Exception {
		return tblStaffMapper.findUserInfoExam(staffId, tblStaffMapper.findLikeUser(orgid, type));
	}

	@Override
	public Map<String, Object> findAllPageBeanPid(String pid, Integer pageNumber, Integer pageSize, String token,
			String staffId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			Map<String, Object> dataMap = new HashMap<String, Object>(0);
			TblStaffUtil user = userProvider.get();
			if (user == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			TblOrganization attribute = new TblOrganization();
			if (pid != null && !pid.equals("")) {
				attribute = tblOrganizationMapper.findById(new BigDecimal(pid));
			} else {
				attribute.setOrgid(user.getCurrentOrg().getOrgid());
				attribute.setOrgtype(user.getCurrentOrg().getOrgtype());
			}
			PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
			pageInfo.setCurrentPage(pageNumber);
			pageInfo.setPageSize(pageSize);
			
			IPage<TblStaff> page = new Page<TblStaff>(pageNumber,pageSize);
			IPage<TblStaff> pageList = tblStaffMapper.findAllPageBeanPid(page, attribute);
			
			pageInfo.setTlist(pageList.getRecords());
			pageInfo.setTotalRecord((int)pageList.getTotal());
			dataMap.put("pageInfo", pageInfo);
			dataMap.put("attribute", attribute);
			resultMap.put("code", "1");
			resultMap.put("msg", "访问接口成功");
			resultMap.put("pageInfo", dataMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> findHttpClient(BigDecimal formId, String jsonArry, String paramArry, String eleName,
			String valueId, Integer pageNumber, Integer pageSize, Integer queryType, BigDecimal oldFormId,
			String textname) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
		try {
			HashMap<String, Object> fields = new HashMap<String, Object>(0);
			fields.put("formId", formId);
			fields.put("pageNumber", pageNumber);
			fields.put("pageSize", pageSize);
			fields.put("queryType", queryType);
			fields.put("oldFormId", oldFormId);
			fields.put("eleName", textname);
			if (jsonArry != null && !"".equals(jsonArry) && jsonArry.length() > 2) {
				jsonArry = jsonArry.substring(0, jsonArry.length() - 1) + ",[\"VALUESTATUS\",\"等于\",\"6\"]]";
			} else {
				jsonArry = "[[\"VALUESTATUS\",\"等于\",\"6\"]]";
			}
			fields.put("jsonArry", jsonArry);
			String result = HttpClient.request(formurl + "/formReport/findFormReportList", fields, null);
			JSONObject resultjs = JSONObject.parseObject(result);

			PageInfo<TblFormElements> pageInfo = new PageInfo<TblFormElements>();
			if ("true".equals(resultjs.get("result").toString())) {
				pageInfo.setTotalRecord(Integer.parseInt(resultjs.get("count").toString()));
				com.alibaba.fastjson.JSONArray array = resultjs.getJSONArray("mapList");
				dataMap.put("array", array);
				String eleListstr = resultjs.get("eleList").toString();
				List<TblFormElements> eleList = JSONObject.parseArray(eleListstr, TblFormElements.class);
				dataMap.put("eleList", eleListstr);
				dataMap.put("eleLists", eleList);
				dataMap.put("formName", resultjs.get("formName").toString());
			} else {
				pageInfo.setTotalRecord(0);
			}
			pageInfo.setCurrentPage(pageNumber);
			dataMap.put("formId", formId);
			dataMap.put("pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataMap.put("eleName", eleName);
		dataMap.put("valueId", valueId);
		dataMap.put("arrayMap", printJsonArray(jsonArry, paramArry));
		resultMap.put("code", "1");
		resultMap.put("msg", "访问接口成功");
		resultMap.put("data", dataMap);
		return resultMap;
	}

	/**
	 * 获取用户名称
	 * @param staffId
	 * @return
	 */
	@Override
	public String getStaffName(Long staffId) {
		return tblStaffMapper.getStaffName(staffId);
	}

	/**
	 * 获取用户名称
	 * @param staffIds
	 * @return
	 */
	@Override
	public String getStaffNames(String staffIds) {
		if (StringUtil.isEmpty(staffIds)){
			return null;
		}
		List<String> staffNames = tblStaffMapper.getStaffNames(staffIds);
		if (CollectionUtil.isEmpty(staffNames)){
			return null;
		}
		return staffNames.stream().distinct().collect(Collectors.joining(","));
	}

	private Object printJsonArray(String jsonArry, String paramArry) {
		JSONObject object = new JSONObject();
		if (jsonArry != null) {
			JSONArray jsonArray = JSONArray.fromObject(jsonArry);
			Object[] objs = null;
			for (int i = 0; i < jsonArray.size(); i++) {
				objs = jsonArray.getJSONArray(i).toArray();
				object.put(objs[0].toString(), objs[2].toString());
			}
		}
		if (paramArry != null) {
			JSONArray paramArray = JSONArray.fromObject(paramArry);
			Object[] objs = null;
			for (int i = 0; i < paramArray.size(); i++) {
				objs = paramArray.getJSONArray(i).toArray();
				object.put(objs[0].toString(), objs[2].toString());
			}
		}
		return object;
	}
}
