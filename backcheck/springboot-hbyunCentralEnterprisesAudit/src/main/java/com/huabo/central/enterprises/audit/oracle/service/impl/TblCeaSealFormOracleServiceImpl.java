package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSealFormOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaSealFormOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaSealFormOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSealFormQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TblCeaSealFormOracleServiceImpl implements TblCeaSealFormOracleService {

	@Resource
	private TblCeaSealFormOracleMapper tblCeaSealFormOracleMapper;

	@Override
	public PageInfo<TblCeaSealFormOracle> getList(TblCeaSealFormQueryParam param) {
		Example example = new Example(TblCeaSealFormOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (CollectionUtil.isNotEmpty(param.getIds())){
			criteria.andIn("id", param.getIds());
		}
		if (StringUtils.isNotBlank(param.getSealName())) {
			criteria.andLike("sealName", "%" + param.getSealName() + "%");
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" ( creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + ")))");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (Objects.nonNull(param.getSealNum())) {
			criteria.andEqualTo("sealNum", param.getSealNum());
		}
		if (Objects.nonNull(param.getState())) {
			criteria.andEqualTo("state", param.getState());
		}
		if (Objects.equals(param.getOrderBy(), 1)) {
			example.setOrderByClause(" sort,createdTime,id ");
		} else {
			example.setOrderByClause(" ID desc ");
		}
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaSealFormOracleMapper.selectByExample(example));
	}

	@Override
	public TblCeaSealFormOracle saveOrUpdate(TblCeaSealFormOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			Integer sort = tblCeaSealFormOracleMapper.maxSort();
			if (Objects.isNull(sort)) {
				sort = 1;
			}else {
				sort = sort + 1;
			}
			param.setSort(sort);
			tblCeaSealFormOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaSealFormOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaSealFormOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaSealFormOracle findById(Long id) {
		TblCeaSealFormOracle model = tblCeaSealFormOracleMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 印信使用单 台账-上移
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateTblCeaSealFormMoveUp(Long id) {
		TblCeaSealFormOracle model = tblCeaSealFormOracleMapper.selectByPrimaryKey(id);
		Integer sort = model.getSort();
		List<TblCeaSealFormOracle> move = tblCeaSealFormOracleMapper.getMoveUp(sort);
		if (CollectionUtil.isEmpty(move)) {
			throw new ServiceException(408, "上移位置最大不可上移");
		}
		TblCeaSealFormOracle tblCeaSealFormOracle = move.get(0);
		//当前一条数据
		TblCeaSealFormOracle update = new TblCeaSealFormOracle();
		update.setId(model.getId());
		update.setSort(tblCeaSealFormOracle.getSort());
		tblCeaSealFormOracleMapper.updateByPrimaryKeySelective(update);
		//上一条数据
		TblCeaSealFormOracle update1 = new TblCeaSealFormOracle();
		update1.setId(tblCeaSealFormOracle.getId());
		update1.setSort(sort);
		tblCeaSealFormOracleMapper.updateByPrimaryKeySelective(update1);
	}

	/**
	 * 印信使用单 台账-下移
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateTblCeaSealFormMoveDown(Long id) {
		TblCeaSealFormOracle model = tblCeaSealFormOracleMapper.selectByPrimaryKey(id);
		Integer sort = model.getSort();
		List<TblCeaSealFormOracle> move = tblCeaSealFormOracleMapper.moveDown(sort);
		if (CollectionUtil.isEmpty(move)) {
			throw new ServiceException(408, "下移位置最大不可上移");
		}
		TblCeaSealFormOracle tblCeaSealFormOracle = move.get(0);
		//当前一条数据
		TblCeaSealFormOracle update = new TblCeaSealFormOracle();
		update.setId(model.getId());
		update.setSort(tblCeaSealFormOracle.getSort());
		tblCeaSealFormOracleMapper.updateByPrimaryKeySelective(update);
		//上一条数据
		TblCeaSealFormOracle update1 = new TblCeaSealFormOracle();
		update1.setId(tblCeaSealFormOracle.getId());
		update1.setSort(sort);
		tblCeaSealFormOracleMapper.updateByPrimaryKeySelective(update1);
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaSealFormOracleMapper.selectCount(TblCeaSealFormOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
