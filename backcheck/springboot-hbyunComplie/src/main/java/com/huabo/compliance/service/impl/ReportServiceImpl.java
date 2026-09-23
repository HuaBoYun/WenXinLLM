package com.huabo.compliance.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.compliance.config.DateBaseConfig;
import com.huabo.compliance.oracle.entity.TblComplianceFileOracle;
import com.huabo.compliance.oracle.entity.TblComplianceReportOracle;
import com.huabo.compliance.oracle.service.TblComplianceFileOracleService;
import com.huabo.compliance.oracle.service.TblComplianceReportOracleService;
import com.huabo.compliance.oracle.service.TblStaffOracleService;
import com.huabo.compliance.service.FileUploadService;
import com.huabo.compliance.service.ReportService;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.vo.param.TblComplianceReportQueryParam;
import com.huabo.compliance.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

	@Resource
	private TblComplianceReportOracleService tblComplianceReportOracleService;


	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblComplianceFileOracleService tblComplianceFileOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 合规报告 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblComplianceReportList(TblComplianceReportQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblComplianceReportOracle> pageInfo = tblComplianceReportOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				Map<Integer, String> userInfosMap = new HashMap<>();
				//提取 部门负责人 与 拟草人  创建人ID
				List<Integer> departmentHeads = pageInfo.getList().stream().map(TblComplianceReportOracle::getDepartmentHead).distinct()
						.collect(Collectors.toList());
				List<Integer> draftsmans = pageInfo.getList().stream().map(TblComplianceReportOracle::getDraftsman).distinct()
						.collect(Collectors.toList());
				List<Integer> creator = pageInfo.getList().stream().map(TblComplianceReportOracle::getCreator).distinct()
						.collect(Collectors.toList());
				departmentHeads.addAll(draftsmans);
				departmentHeads.addAll(creator);
				List<Integer> collect = departmentHeads.stream().distinct().collect(Collectors.toList());

				List<Integer> collectNew = new ArrayList<Integer>();
				if(null != collect){
					//去null
					for (int i = 0;i < collect.size(); i++) {
				      if (collect.get(i) != null) {
				    	  collectNew.add(collect.get(i));
				      }
				    }
				}

				//获取用户信息map
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(StringUtils.join(collectNew, ","));
				if (CollectionUtil.isNotEmpty(userInfos)) {
					userInfosMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
				Map<Integer, String> finalUserInfosMapMap = userInfosMap;
				//参数组装
				pageInfo.getList().forEach(x -> {
					x.setDepartmentHeadName(finalUserInfosMapMap.getOrDefault(x.getDepartmentHead(), ""));
					x.setDraftsmanName(finalUserInfosMapMap.getOrDefault(x.getDraftsman(), ""));
					x.setCreatorName(finalUserInfosMapMap.getOrDefault(x.getCreator(), ""));
				});
				PageResult<TblComplianceReportOracle> build = new PageResult<TblComplianceReportOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 合规报告 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblComplianceReport(TblComplianceReportOracle param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceReportOracle report = tblComplianceReportOracleService.saveOrUpdate(param);
			return ResponseFormat.retParam(200, 200, report);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规报告 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblComplianceReport(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceReportOracle report = tblComplianceReportOracleService.findById(id);
			tblComplianceReportOracleService.delete(id);
			//附件-删除
			if (StringUtils.isNotBlank(report.getFileIds())) {
				List<String> fileIds = Arrays.asList(report.getFileIds().split(","));
				fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规报告 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblComplianceReport(Integer id) {
		Map<String, Object> map = new HashMap<>();
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceReportOracle report = tblComplianceReportOracleService.findById(id);
			if (report.getDraftsman() != null) {
				report.setDraftsmanName(tblStaffOracleService.getCreatorUserInfo(report.getDraftsman()));
			}
			if (report.getDepartmentHead() != null) {
				report.setDepartmentHeadName(tblStaffOracleService.getCreatorUserInfo(report.getDepartmentHead()));
			}
			if (StringUtils.isNotEmpty(report.getFileIds())) {
				List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(report.getFileIds());
				map.put("file", file);
			}
			map.put("report", report);
			return ResponseFormat.retParam(200, 200, map);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}
}
