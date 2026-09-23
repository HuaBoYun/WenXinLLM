package com.huabo.legal.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicFirstOracle;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicOracle;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicSecondOracle;
import com.huabo.legal.service.ExamineTopicService;
import com.huabo.legal.vo.param.TblFwglExamineTopicFirstQueryParam;
import com.huabo.legal.vo.param.TblFwglExamineTopicQueryParam;
import com.huabo.legal.vo.param.TblFwglExamineTopicSecondQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-评分所有接口",description="法务管理-评分所有接口")
@RequestMapping(value = "/api-auth/examine/topic")
@Slf4j
public class ExamineTopicController {

	@Resource
	private ExamineTopicService examineTopicService;

	@Operation(summary = "考核题目首部分列表 查询")
	@PostMapping("/first/getList")
	public JsonBean getTblFwglExamineTopicFirstList(@RequestBody TblFwglExamineTopicFirstQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblFwglExamineTopicFirstList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目首部分列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目首部分 新增/更新")
	@PostMapping("/first/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglExamineTopicFirst(@RequestBody @Validated TblFwglExamineTopicFirstOracle param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.saveOrUpdateTblFwglExamineTopicFirst(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目首部分 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目首部分 刪除")
	@DeleteMapping("/first/{id}")
	public JsonBean deleteTblFwglExamineTopicFirst(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.deleteTblFwglExamineTopicFirst(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目首部分 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目首部分详情 查询")
	@GetMapping("/first/{id}")
	public JsonBean getTblFwglExamineTopicFirst(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblFwglExamineTopicFirst(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目首部分详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目第二部分列表 查询")
	@PostMapping("/second/getList")
	public JsonBean getTblFwglExamineTopicSecondList(@RequestBody TblFwglExamineTopicSecondQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblFwglExamineTopicSecondList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目第二部分列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "评分项明细列表 查询 {id}为首部分主键id")
	@GetMapping("/second/info/{id}")
	public JsonBean getTblFwglExamineTopicSecondInfoList(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblFwglExamineTopicSecondInfoList(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("评分项明细列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目第二部分 新增/更新")
	@PostMapping("/second/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglExamineTopicSecond(@RequestBody @Validated List<TblFwglExamineTopicSecondOracle> param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.saveOrUpdateTblFwglExamineTopicSecond(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目第二部分列表 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目第二部分 刪除")
	@DeleteMapping("/second/{transactionId}")
	public JsonBean deleteTblFwglExamineTopicSecond(@PathVariable String transactionId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.deleteTblFwglExamineTopicSecond(transactionId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目第二部分 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目第二部分详情 查询")
	@GetMapping("/second/{transactionId}")
	public JsonBean getTblFwglExamineTopicSecond(@PathVariable String transactionId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblFwglExamineTopicSecond(transactionId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目第二部分详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目列表 查询")
	@PostMapping("/getList")
	public JsonBean getTblFwglExamineTopicList(@RequestBody TblFwglExamineTopicQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblFwglExamineTopicList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目 新增/更新")
	@PostMapping("/saveOrUpdate/{scoreTransaction}")
	public JsonBean saveOrUpdateTblFwglExamineTopic(@PathVariable String scoreTransaction,
			@RequestBody @Validated List<TblFwglExamineTopicOracle> param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.saveOrUpdateTblFwglExamineTopic(scoreTransaction, param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目 刪除")
	@DeleteMapping("/{scoreTransaction}")
	public JsonBean deleteTblFwglExamineTopic(@PathVariable String scoreTransaction) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.deleteTblFwglExamineTopic(scoreTransaction);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目详情 查询")
	@GetMapping("/{scoreTransaction}")
	public JsonBean getTblFwglExamineTopic(@PathVariable String scoreTransaction) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblFwglExamineTopic(scoreTransaction);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目详情列表 查询")
	@PostMapping("/info/getList")
	public JsonBean getTblFwglExamineTopicInfoList(@RequestBody TblFwglExamineTopicQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblFwglExamineTopicInfoList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目详情列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "题目是否被使用 true-是 false-否")
	@GetMapping("/isTopicTransactionId/{topicTransactionId}")
	public JsonBean isTopicTransactionId(@PathVariable String topicTransactionId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.isTopicTransactionId(topicTransactionId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("题目是否被使用 true-是 false-否 ...接口 异常", e);
		}
		return jsonBean;
	}


}
