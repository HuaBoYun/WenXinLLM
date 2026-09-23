package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopic;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaExamineTopicMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExamineTopicService;
import com.huabo.central.enterprises.audit.util.SnowflakeIdWorker;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TblCeaExamineTopicServiceImpl implements TblCeaExamineTopicService {

	private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

	@Resource
	private TblCeaExamineTopicMapper tblCeaExamineTopicMapper;

	/**
	 * 考核题目 列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaExamineTopic> getList(TblCeaExamineTopicQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaExamineTopicMapper.getList(param));
	}

	/**
	 * 考核题目 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(String scoreTransaction, List<TblCeaExamineTopic> param) {
		//scoreTransaction = 0 第一次新增
		//先删除历史的
		if (!StringUtils.equals("0", scoreTransaction)) {
			delete(scoreTransaction);
		}
		long transaction = snowflakeIdWorker.nextId(); //雪花算法
		String transactionId = String.valueOf(transaction);
		if (CollectionUtil.isEmpty(param)) {
			log.error("考核题目 新增/更新 对象为空");
			throw new ServiceException(400, "提交对象不能为空");
		}
		param.forEach(x -> {
			Date now = new Date();
			if (x.getId() == null) {
				x.setId(RandomUtil.uuLongId());
				x.setScoreTransaction(transactionId);
				x.setState(YesNo.YES);
				x.setCreatedTime(now);
				x.setUpdatedTime(now);
				tblCeaExamineTopicMapper.insertSelective(x);
			}
		});
	}

	/**
	 * 考核题目 删除
	 * @param scoreTransaction
	 */
	@Override
	public void delete(String scoreTransaction) {
		tblCeaExamineTopicMapper.delete(TblCeaExamineTopic.ofScoreTransaction(scoreTransaction));
	}

	/**
	 * 考核题目 详情 查询
	 * @param scoreTransaction
	 * @return
	 */
	@Override
	public List<TblCeaExamineTopic> findById(String scoreTransaction) {
		List<TblCeaExamineTopic> examineTopic = tblCeaExamineTopicMapper.select(TblCeaExamineTopic.ofScoreTransaction(scoreTransaction));
		if (examineTopic == null) {
			throw new ServiceException(400, 50001);
		}
		return examineTopic;
	}

	/**
	 * 根据事务id查询题目列表
	 * @param param
	 * @return
	 */
	@Override
	public List<TblCeaExamineTopic> getTblCeaExamineTopic(TblCeaExamineTopicQueryParam param) {
		Example example = new Example(TblCeaExamineTopic.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getScoreTransaction())) {
			criteria.andEqualTo("scoreTransaction", param.getScoreTransaction());
		}
		if (param.getExamineType() != null) {
			criteria.andEqualTo("examineType", param.getExamineType());
		}
//		if (param.getCreator() != null) {
//			criteria.andEqualTo("creator", param.getCreator());
//		}
//		if (param.getBelongGroup() != null) {
//			criteria.andEqualTo("belongGroup", param.getBelongGroup());
//		}
//		if (param.getWorkUnit() != null) {
//			criteria.andEqualTo("workUnit", param.getWorkUnit());
//		}
		example.setOrderByClause(" id desc ");
		return tblCeaExamineTopicMapper.selectByExample(example);
	}

	/**
	 * 根据id查询 计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaExamineTopicMapper.selectCount(TblCeaExamineTopic.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
