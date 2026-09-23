package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopic;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicFirst;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicSecond;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExamineTopicFirstService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExamineTopicSecondService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExamineTopicService;
import com.huabo.central.enterprises.audit.service.CeaExamineTopicService;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicFirstQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicSecondQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CeaExamineTopicServiceImpl implements CeaExamineTopicService {

	@Resource
	private TblCeaExamineTopicFirstService tblCeaExamineTopicFirstService;
	@Resource
	private TblCeaExamineTopicSecondService tblCeaExamineTopicSecondService;
	@Resource
	private TblCeaExamineTopicService tblCeaExamineTopicService;


	/**
	 * 考核题目首部分列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblCeaExamineTopicFirstList(TblCeaExamineTopicFirstQueryParam param) {
		param.setCreator(null);
		param.setWorkUnit(null);
		PageInfo<TblCeaExamineTopicFirst> pageInfo = tblCeaExamineTopicFirstService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblCeaExamineTopicFirst> build = new PageResult<TblCeaExamineTopicFirst>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 考核题目首部分 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblCeaExamineTopicFirst(TblCeaExamineTopicFirst param) {
		TblCeaExamineTopicFirst examineTopicFirst = tblCeaExamineTopicFirstService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, examineTopicFirst);
	}

	/**
	 * 考核题目首部分 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblCeaExamineTopicFirst(Long id) {
		tblCeaExamineTopicFirstService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目首部分详情
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblCeaExamineTopicFirst(Long id) {
		TblCeaExamineTopicFirst examineTopicFirst = tblCeaExamineTopicFirstService.findById(id);
		return ResponseFormat.retParam(200, 200, examineTopicFirst);
	}

	/**
	 * 考核题目第二部分列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblCeaExamineTopicSecondList(TblCeaExamineTopicSecondQueryParam param) {
		PageInfo<TblCeaExamineTopicSecond> pageInfo = tblCeaExamineTopicSecondService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			Map<Long, TblCeaExamineTopicFirst> map = tblCeaExamineTopicFirstService.getMap();
			pageInfo.getList().forEach(x -> {
				if (map.containsKey(x.getFirstId())) {
					x.setEmploymentObjective(map.get(x.getFirstId()).getEmploymentObjective());
					x.setSerialNumber(map.get(x.getFirstId()).getSerialNumber());
					x.setExamineEmphasis(map.get(x.getFirstId()).getExamineEmphasis());
				}
			});
			PageResult<TblCeaExamineTopicSecond> build = new PageResult<TblCeaExamineTopicSecond>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 考核题目第二部分列表 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblCeaExamineTopicSecond(List<TblCeaExamineTopicSecond> param) {
		tblCeaExamineTopicSecondService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目第二部分 刪除
	 * @param transactionId
	 * @return
	 */
	@Override
	public JsonBean deleteTblCeaExamineTopicSecond(String transactionId) {
		tblCeaExamineTopicSecondService.delete(transactionId);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目第二部分详情 查询
	 * @param transactionId
	 * @return
	 */
	@Override
	public JsonBean getTblCeaExamineTopicSecond(String transactionId) {
		List<TblCeaExamineTopicSecond> examineTopicSecond = tblCeaExamineTopicSecondService.findById(transactionId);
		Map<Long, TblCeaExamineTopicFirst> map = tblCeaExamineTopicFirstService.getMap();
		examineTopicSecond.forEach(x -> {
			if (map.containsKey(x.getFirstId())) {
				x.setEmploymentObjective(map.get(x.getFirstId()).getEmploymentObjective());
				x.setSerialNumber(map.get(x.getFirstId()).getSerialNumber());
				x.setExamineEmphasis(map.get(x.getFirstId()).getExamineEmphasis());
			}
		});
		return ResponseFormat.retParam(200, 200, examineTopicSecond);
	}

	/**
	 * 考核题目列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblCeaExamineTopicList(TblCeaExamineTopicQueryParam param) {
		PageInfo<TblCeaExamineTopic> pageInfo = tblCeaExamineTopicService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblCeaExamineTopic> build = new PageResult<TblCeaExamineTopic>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 考核题目 新增/更新
	 * @param param
	 * @param scoreTransaction
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblCeaExamineTopic(String scoreTransaction, List<TblCeaExamineTopic> param) {
		tblCeaExamineTopicService.saveOrUpdate(scoreTransaction, param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目 刪除
	 * @param scoreTransaction
	 * @return
	 */
	@Override
	public JsonBean deleteTblCeaExamineTopic(String scoreTransaction) {
		tblCeaExamineTopicService.delete(scoreTransaction);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目详情 查询
	 * @param scoreTransaction
	 * @return
	 */
	@Override
	public JsonBean getTblCeaExamineTopic(String scoreTransaction) {
		List<TblCeaExamineTopic> examineTopic = tblCeaExamineTopicService.findById(scoreTransaction);
		return ResponseFormat.retParam(200, 200, examineTopic);
	}

	/**
	 * 评分项明细列表 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblCeaExamineTopicSecondInfoList(Long id) {
		List<TblCeaExamineTopicSecond> examineTopic = tblCeaExamineTopicSecondService.getTblCeaExamineTopicSecond(id);
		Map<Long, TblCeaExamineTopicFirst> map = tblCeaExamineTopicFirstService.getMap();
		examineTopic.forEach(x -> {
			if (map.containsKey(x.getFirstId())) {
				x.setEmploymentObjective(map.get(x.getFirstId()).getEmploymentObjective());
				x.setSerialNumber(map.get(x.getFirstId()).getSerialNumber());
				x.setExamineEmphasis(map.get(x.getFirstId()).getExamineEmphasis());
			}
		});
		return ResponseFormat.retParam(200, 200, examineTopic);
	}

	/**
	 * 考核题目详情列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblCeaExamineTopicInfoList(TblCeaExamineTopicQueryParam param) {
		List<TblCeaExamineTopic> tblCeaExamineTopic = tblCeaExamineTopicService.getTblCeaExamineTopic(param);
		return ResponseFormat.retParam(200, 200, tblCeaExamineTopic);
	}
}
