package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicFirstOracle;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicOracle;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicSecondOracle;
import com.huabo.legal.oracle.service.TblFwglAnnualExamineOracleService;
import com.huabo.legal.oracle.service.TblFwglExamineTopicFirstOracleService;
import com.huabo.legal.oracle.service.TblFwglExamineTopicOracleService;
import com.huabo.legal.oracle.service.TblFwglExamineTopicSecondOracleService;
import com.huabo.legal.service.ExamineTopicService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglExamineTopicFirstQueryParam;
import com.huabo.legal.vo.param.TblFwglExamineTopicQueryParam;
import com.huabo.legal.vo.param.TblFwglExamineTopicSecondQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ExamineTopicServiceImpl implements ExamineTopicService {

	@Resource
	private TblFwglExamineTopicFirstOracleService tblFwglExamineTopicFirstOracleService;
	@Resource
	private TblFwglExamineTopicSecondOracleService tblFwglExamineTopicSecondOracleService;
	@Resource
	private TblFwglExamineTopicOracleService tblFwglExamineTopicOracleService;
	@Resource
	private TblFwglAnnualExamineOracleService tblFwglAnnualExamineOracleService;


	/**
	 * 考核题目首部分列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglExamineTopicFirstList(TblFwglExamineTopicFirstQueryParam param) {
		param.setCreator(null);
		param.setWorkUnit(null);
		PageInfo<TblFwglExamineTopicFirstOracle> pageInfo = tblFwglExamineTopicFirstOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglExamineTopicFirstOracle> build = new PageResult<TblFwglExamineTopicFirstOracle>().build(pageInfo);
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
	public JsonBean saveOrUpdateTblFwglExamineTopicFirst(TblFwglExamineTopicFirstOracle param) {
		TblFwglExamineTopicFirstOracle examineTopicFirst = tblFwglExamineTopicFirstOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, examineTopicFirst);
	}

	/**
	 * 考核题目首部分 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglExamineTopicFirst(Long id) {
		tblFwglExamineTopicFirstOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目首部分详情
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglExamineTopicFirst(Long id) {
		TblFwglExamineTopicFirstOracle examineTopicFirst = tblFwglExamineTopicFirstOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, examineTopicFirst);
	}

	/**
	 * 考核题目第二部分列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglExamineTopicSecondList(TblFwglExamineTopicSecondQueryParam param) {
		PageInfo<TblFwglExamineTopicSecondOracle> pageInfo = tblFwglExamineTopicSecondOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			Map<Long, TblFwglExamineTopicFirstOracle> map = tblFwglExamineTopicFirstOracleService.getMap();
			pageInfo.getList().forEach(x -> {
				if (map.containsKey(x.getFirstId())) {
					x.setEmploymentObjective(map.get(x.getFirstId()).getEmploymentObjective());
					x.setSerialNumber(map.get(x.getFirstId()).getSerialNumber());
					x.setExamineEmphasis(map.get(x.getFirstId()).getExamineEmphasis());
				}
			});
			PageResult<TblFwglExamineTopicSecondOracle> build = new PageResult<TblFwglExamineTopicSecondOracle>().build(pageInfo);
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
	public JsonBean saveOrUpdateTblFwglExamineTopicSecond(List<TblFwglExamineTopicSecondOracle> param) {
		tblFwglExamineTopicSecondOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目第二部分 刪除
	 * @param transactionId
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglExamineTopicSecond(String transactionId) {
		tblFwglExamineTopicSecondOracleService.delete(transactionId);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目第二部分详情 查询
	 * @param transactionId
	 * @return
	 */
	@Override
	public JsonBean getTblFwglExamineTopicSecond(String transactionId) {
		List<TblFwglExamineTopicSecondOracle> examineTopicSecond = tblFwglExamineTopicSecondOracleService.findById(transactionId);
		Map<Long, TblFwglExamineTopicFirstOracle> map = tblFwglExamineTopicFirstOracleService.getMap();
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
	public JsonBean getTblFwglExamineTopicList(TblFwglExamineTopicQueryParam param) {
		PageInfo<TblFwglExamineTopicOracle> pageInfo = tblFwglExamineTopicOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglExamineTopicOracle> build = new PageResult<TblFwglExamineTopicOracle>().build(pageInfo);
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
	public JsonBean saveOrUpdateTblFwglExamineTopic(String scoreTransaction, List<TblFwglExamineTopicOracle> param) {
		tblFwglExamineTopicOracleService.saveOrUpdate(scoreTransaction, param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目 刪除
	 * @param scoreTransaction
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglExamineTopic(String scoreTransaction) {
		tblFwglExamineTopicOracleService.delete(scoreTransaction);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 考核题目详情 查询
	 * @param scoreTransaction
	 * @return
	 */
	@Override
	public JsonBean getTblFwglExamineTopic(String scoreTransaction) {
		List<TblFwglExamineTopicOracle> examineTopic = tblFwglExamineTopicOracleService.findById(scoreTransaction);
		return ResponseFormat.retParam(200, 200, examineTopic);
	}

	/**
	 * 评分项明细列表 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglExamineTopicSecondInfoList(Long id) {
		List<TblFwglExamineTopicSecondOracle> examineTopic = tblFwglExamineTopicSecondOracleService.getTblFwglExamineTopicSecond(id);
		Map<Long, TblFwglExamineTopicFirstOracle> map = tblFwglExamineTopicFirstOracleService.getMap();
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
	public JsonBean getTblFwglExamineTopicInfoList(TblFwglExamineTopicQueryParam param) {
		List<TblFwglExamineTopicOracle> tblFwglExamineTopic = tblFwglExamineTopicOracleService.getTblFwglExamineTopic(param);
		return ResponseFormat.retParam(200, 200, tblFwglExamineTopic);
	}

	/**
	 * 题目是否被使用 true-是 false-否
	 * @param topicTransactionId
	 * @return
	 */
	@Override
	public JsonBean isTopicTransactionId(String topicTransactionId) {
		Boolean flags = tblFwglAnnualExamineOracleService.isTopicTransactionId(topicTransactionId);
		return ResponseFormat.retParam(200, 200, flags);
	}
}
