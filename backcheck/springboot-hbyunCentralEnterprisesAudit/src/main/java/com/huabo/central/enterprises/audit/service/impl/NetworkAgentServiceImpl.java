package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpInventory;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaNetworkAgentOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaIpInventoryService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaNetworkAgentOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaIpInventoryService;
import com.huabo.central.enterprises.audit.service.NetworkAgentService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaNetworkAgentQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NetworkAgentServiceImpl implements NetworkAgentService {

	@Resource
	private TblCeaNetworkAgentOracleService tblCeaNetworkAgentOracleService;
	@Resource
	private CeaIpInventoryService ceaIpInventoryService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private TblCeaIpInventoryService tblCeaIpInventoryService;


	/**
	 * 外网代理服务 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaNetworkAgentOracle> getTblCeaNetworkAgentList(TblCeaNetworkAgentQueryParam param) {
		PageInfo<TblCeaNetworkAgentOracle> pageInfo = tblCeaNetworkAgentOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaNetworkAgentOracle::getCreator).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				if (Objects.nonNull(item.getIpInventoryId())) {
					try {
						TblCeaIpInventory model = tblCeaIpInventoryService.findById(item.getIpInventoryId());
						item.setNetworkInterface(model.getIpAddress());
					} catch (Exception e) {
						log.warn("外网代理服务-ip清单id不存在",e);
					}
				}
			}); PageResult<TblCeaNetworkAgentOracle> build = new PageResult<TblCeaNetworkAgentOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		} return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 外网代理服务 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaNetworkAgentOracle> saveOrUpdateTblCeaNetworkAgent(TblCeaNetworkAgentOracle param) {
		TblCeaNetworkAgentOracle model = tblCeaNetworkAgentOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 外网代理服务 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaNetworkAgent(Long id) {
		tblCeaNetworkAgentOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 外网代理服务 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaNetworkAgentOracle> getTblCeaNetworkAgent(Long id) {
		TblCeaNetworkAgentOracle result = tblCeaNetworkAgentOracleService.findById(id);
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getIpInventoryId())) {
			MyJsonBean<FileVo<TblCeaIpInventory>> tblCeaIpInventory = ceaIpInventoryService.getTblCeaIpInventory(result.getIpInventoryId());
			result.setNetworkInterface(tblCeaIpInventory.getData().getData().getIpAddress());
		}
		return MyResponseFormat.retParam(200, 200, result);
	}
}
