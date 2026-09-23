package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicSecondOracle;
import com.huabo.legal.oracle.mapper.TblFwglExamineTopicSecondOracleMapper;
import com.huabo.legal.oracle.service.TblFwglExamineTopicSecondOracleService;
import com.huabo.legal.util.SnowflakeIdWorker;
import com.huabo.legal.vo.param.TblFwglExamineTopicSecondQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TblFwglExamineTopicSecondOracleServiceImpl implements TblFwglExamineTopicSecondOracleService {

	private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

	@Resource
	private TblFwglExamineTopicSecondOracleMapper tblFwglExamineTopicSecondOracleMapper;

	/**
	 * 年度考核题目-第二部分 列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglExamineTopicSecondOracle> getList(TblFwglExamineTopicSecondQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglExamineTopicSecondOracleMapper.getList(param));
	}

	/**
	 * 年度考核题目-第二部分 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public void saveOrUpdate(List<TblFwglExamineTopicSecondOracle> param) {
		long transaction = snowflakeIdWorker.nextId(); //雪花算法
		String transactionId = String.valueOf(transaction);
		if (CollectionUtil.isEmpty(param)) {
			log.error("年度考核题目-第二部分 新增/更新 对象为空");
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
				tblFwglExamineTopicSecondOracleMapper.insertSelective(x);
			} else {
				if (idById(x.getId())) {
					throw new ServiceException(400, 50001);
				}
				x.setTransactionId(transactionId);
				x.setCreatedTime(null);
				x.setUpdatedTime(now);
				tblFwglExamineTopicSecondOracleMapper.updateByPrimaryKeySelective(x);
			}
		});
	}

	/**
	 * 年度考核题目-第二部分 删除
	 * @param transactionId
	 */
	@Override
	public void delete(String transactionId) {
		tblFwglExamineTopicSecondOracleMapper.delete(TblFwglExamineTopicSecondOracle.ofTransactionId(transactionId));
	}

	/**
	 * 年度考核题目-第二部分 详情 查询
	 * @param transactionId
	 * @return
	 */
	@Override
	public List<TblFwglExamineTopicSecondOracle> findById(String transactionId) {
		Example example = new Example(TblFwglExamineTopicSecondOracle.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("transactionId", transactionId);
		example.setOrderByClause(" sonSort asc,id desc ");
		return tblFwglExamineTopicSecondOracleMapper.selectByExample(example);
	}

	/**
	 * 根据题目-首部主键ID 查询评分项列表
	 * @param id
	 * @return
	 */
	@Override
	public List<TblFwglExamineTopicSecondOracle> getTblFwglExamineTopicSecond(Long id) {
		Example example = new Example(TblFwglExamineTopicSecondOracle.class);
		example.createCriteria().andEqualTo("firstId", id);
		example.setOrderByClause(" sonScore asc,id desc ");
		return tblFwglExamineTopicSecondOracleMapper.selectByExample(example);
	}

	/**
	 * 根据id查询 年度计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglExamineTopicSecondOracleMapper.selectCount(TblFwglExamineTopicSecondOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
