package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicSecond;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaExamineTopicSecondMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExamineTopicSecondService;
import com.huabo.central.enterprises.audit.util.SnowflakeIdWorker;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicSecondQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TblCeaExamineTopicSecondServiceImpl implements TblCeaExamineTopicSecondService {

	private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

	@Resource
	private TblCeaExamineTopicSecondMapper tblCeaExamineTopicSecondMapper;

	/**
	 * 考核题目-第二部分 列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaExamineTopicSecond> getList(TblCeaExamineTopicSecondQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaExamineTopicSecondMapper.getList(param));
	}

	/**
	 * 考核题目-第二部分 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public void saveOrUpdate(List<TblCeaExamineTopicSecond> param) {
		long transaction = snowflakeIdWorker.nextId(); //雪花算法
		String transactionId = String.valueOf(transaction);
		if (CollectionUtil.isEmpty(param)) {
			log.error("考核题目-第二部分 新增/更新 对象为空");
			throw new ServiceException(400, "提交对象不能为空");
		}
		param.forEach(x -> {
			Date now = new Date();
			if (x.getId() == null) {
				x.setId(RandomUtil.uuLongId());
				x.setTransactionId(transactionId);
				x.setState(YesNo.YES);
				x.setCreatedTime(now);
				x.setUpdatedTime(now);
				tblCeaExamineTopicSecondMapper.insertSelective(x);
			} else {
				if (idById(x.getId())) {
					throw new ServiceException(400, 50001);
				}
				x.setCreator(null);
				x.setTransactionId(transactionId);
				x.setCreatedTime(null);
				x.setUpdatedTime(now);
				tblCeaExamineTopicSecondMapper.updateByPrimaryKeySelective(x);
			}
		});
	}

	/**
	 * 考核题目-第二部分 删除
	 * @param transactionId
	 */
	@Override
	public void delete(String transactionId) {
		tblCeaExamineTopicSecondMapper.delete(TblCeaExamineTopicSecond.ofTransactionId(transactionId));
	}

	/**
	 * 考核题目-第二部分 详情 查询
	 * @param transactionId
	 * @return
	 */
	@Override
	public List<TblCeaExamineTopicSecond> findById(String transactionId) {
		Example example = new Example(TblCeaExamineTopicSecond.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("transactionId", transactionId);
		example.setOrderByClause(" sonSort asc,id desc ");
		return tblCeaExamineTopicSecondMapper.selectByExample(example);
	}

	/**
	 * 根据题目-首部主键ID 查询评分项列表
	 * @param id
	 * @return
	 */
	@Override
	public List<TblCeaExamineTopicSecond> getTblCeaExamineTopicSecond(Long id) {
		Example example = new Example(TblCeaExamineTopicSecond.class);
		example.createCriteria().andEqualTo("firstId", id);
		example.setOrderByClause(" sonScore asc,id desc ");
		return tblCeaExamineTopicSecondMapper.selectByExample(example);
	}

	/**
	 * 根据id查询 计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaExamineTopicSecondMapper.selectCount(TblCeaExamineTopicSecond.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
