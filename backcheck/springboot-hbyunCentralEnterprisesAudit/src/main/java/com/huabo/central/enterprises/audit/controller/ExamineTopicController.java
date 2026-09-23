package com.huabo.central.enterprises.audit.controller;

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
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopic;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicFirst;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicSecond;
import com.huabo.central.enterprises.audit.service.CeaExamineTopicService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicFirstQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicSecondQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="评分-所有接口",description="评分-所有接口")
@RequestMapping(value = "/api-auth/examine/topic")
@Slf4j
public class ExamineTopicController {

	@Resource
	private CeaExamineTopicService examineTopicService;

	@Operation(summary = "考核题目首部分列表 查询")
	@PostMapping("/first/getList")
	public JsonBean getTblCeaExamineTopicFirstList(@RequestBody TblCeaExamineTopicFirstQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblCeaExamineTopicFirstList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目首部分列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目首部分 新增/更新")
	@PostMapping("/first/saveOrUpdate")
	public JsonBean saveOrUpdateTblCeaExamineTopicFirst(@RequestBody @Validated TblCeaExamineTopicFirst param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.saveOrUpdateTblCeaExamineTopicFirst(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目首部分 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目首部分 刪除")
	@DeleteMapping("/first/{id}")
	public JsonBean deleteTblCeaExamineTopicFirst(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.deleteTblCeaExamineTopicFirst(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目首部分 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目首部分详情 查询")
	@GetMapping("/first/{id}")
	public JsonBean getTblCeaExamineTopicFirst(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblCeaExamineTopicFirst(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目首部分详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目第二部分列表 查询")
	@PostMapping("/second/getList")
	public JsonBean getTblCeaExamineTopicSecondList(@RequestBody TblCeaExamineTopicSecondQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblCeaExamineTopicSecondList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目第二部分列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "评分项明细列表 查询 {id}为首部分主键id")
	@GetMapping("/second/info/{id}")
	public JsonBean getTblCeaExamineTopicSecondInfoList(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblCeaExamineTopicSecondInfoList(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("评分项明细列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目第二部分 新增/更新")
	@PostMapping("/second/saveOrUpdate")
	public JsonBean saveOrUpdateTblCeaExamineTopicSecond(@RequestBody @Validated List<TblCeaExamineTopicSecond> param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.saveOrUpdateTblCeaExamineTopicSecond(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目第二部分列表 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目第二部分 刪除")
	@DeleteMapping("/second/{transactionId}")
	public JsonBean deleteTblCeaExamineTopicSecond(@PathVariable String transactionId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.deleteTblCeaExamineTopicSecond(transactionId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目第二部分 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目第二部分详情 查询")
	@GetMapping("/second/{transactionId}")
	public JsonBean getTblCeaExamineTopicSecond(@PathVariable String transactionId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblCeaExamineTopicSecond(transactionId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目第二部分详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目列表 查询")
	@PostMapping("/getList")
	public JsonBean getTblCeaExamineTopicList(@RequestBody TblCeaExamineTopicQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblCeaExamineTopicList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目 新增/更新")
	@PostMapping("/saveOrUpdate/{scoreTransaction}")
	public JsonBean saveOrUpdateTblCeaExamineTopic(@PathVariable String scoreTransaction, @RequestBody @Validated List<TblCeaExamineTopic> param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.saveOrUpdateTblCeaExamineTopic(scoreTransaction, param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目 刪除")
	@DeleteMapping("/{scoreTransaction}")
	public JsonBean deleteTblCeaExamineTopic(@PathVariable String scoreTransaction) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.deleteTblCeaExamineTopic(scoreTransaction);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目详情 查询")
	@GetMapping("/{scoreTransaction}")
	public JsonBean getTblCeaExamineTopic(@PathVariable String scoreTransaction) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblCeaExamineTopic(scoreTransaction);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核题目详情列表 查询")
	@PostMapping("/info/getList")
	public JsonBean getTblCeaExamineTopicInfoList(@RequestBody TblCeaExamineTopicQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examineTopicService.getTblCeaExamineTopicInfoList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核题目详情列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}
}
