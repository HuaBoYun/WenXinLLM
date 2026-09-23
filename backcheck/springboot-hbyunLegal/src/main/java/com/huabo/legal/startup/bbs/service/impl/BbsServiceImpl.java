package com.huabo.legal.startup.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.config.DateBaseConfig;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.startup.bbs.domain.Bbs;
import com.huabo.legal.startup.bbs.mapper.BbsMapper;
import com.huabo.legal.startup.bbs.mapper.BbsOracleMapper;
import com.huabo.legal.startup.bbs.service.BbsService;
import com.huabo.legal.startup.bbs.service.dto.BbsDto;
import com.huabo.legal.startup.bbs.service.dto.BbsQueryDto;
import com.huabo.legal.vo.param.BbsAdminParam;
import com.huabo.legal.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 留言接口实现类
 *
 * @author zhuhuix
 * @date 2022-06-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BbsServiceImpl implements BbsService {

	private final BbsMapper bbsMapper;

	private final BbsOracleMapper bbsOracleMapper;

	@Resource
	private TblStaffOracleService tblStaffOracleService;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public Bbs create(Bbs bbs) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			bbs.setBbsType(1);
			bbs.setReplied(false);
			bbs.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			if (bbsOracleMapper.insert(bbs) > 0) {
				return bbs;
			}
			throw new RuntimeException("新增留言失败");
		} else {
			bbs.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			if (bbsMapper.insert(bbs) > 0) {
				return bbs;
			}
			throw new RuntimeException("新增留言失败");
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean delete(BbsAdminParam ids) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			if (bbsOracleMapper.deleteBatchIds(ids.getList()) > 0) {
				return true;
			}
			throw new RuntimeException("删除留言失败");
		} else {
			if (bbsMapper.deleteBatchIds(ids.getList()) > 0) {
				return true;
			}
			throw new RuntimeException("删除留言失败");
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Bbs saveOrUpdate(Bbs bbs) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			if (StringUtils.isEmpty(bbs.getId())) {
				if (StringUtils.isEmpty(bbs.getParentId())) {
					throw new ServiceException(0, "新增回复字段parentId 不能为空");
				}
				//新增 回复数据
				bbs.setBbsType(2);
				bbs.setKnowledgeType(bbs.getKnowledgeType());
				bbs.setReplyTime(Timestamp.valueOf(LocalDateTime.now()));
				if (bbsOracleMapper.insert(bbs) > 0) {
					update(bbs.getParentId());
					return bbs;
				}
			} else {
				bbs.setBbsType(2);
				bbs.setKnowledgeType(bbs.getKnowledgeType());
				bbs.setReplyTime(Timestamp.valueOf(LocalDateTime.now()));
				if (bbsOracleMapper.updateById(bbs) > 0) {
					update(bbs.getParentId());
					return bbs;
				}
			}
			throw new RuntimeException("更新留言失败");
		} else {
			bbs.setReplyTime(Timestamp.valueOf(LocalDateTime.now()));
			if (bbsMapper.updateById(bbs) > 0) {
				return bbs;
			}
			throw new RuntimeException("更新留言失败");
		}
	}

	/**
	 * 更新提问状态 已回复
	 * @param id
	 */
	private void update(String id) {
		QueryWrapper<Bbs> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Bbs::getId, id);
		Bbs model = new Bbs();
		model.setReplied(true);
		bbsOracleMapper.update(model, queryWrapper);
	}

	@Override
	public Bbs findById(Long id, Integer staffId) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			QueryWrapper<Bbs> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().eq(Bbs::getParentId, id).eq(Bbs::getReplyName, staffId);
			Bbs bbs = bbsOracleMapper.selectOne(queryWrapper);
			if (bbs == null) {
				return null;
			}
			if (StringUtils.isEmpty(bbs.getNickName())) {
				bbs.setNickNames(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(bbs.getNickName())));
			}
			if (!StringUtils.isEmpty(bbs.getReplyName())) {
				bbs.setReplyNames(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(bbs.getReplyName())));
			}
			return bbs;
		} else {
			return bbsMapper.selectById(id);
		}

	}

	@Override
	public BbsDto page(BbsQueryDto bbsQueryDto, boolean flags) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			QueryWrapper<Bbs> queryWrapper = new QueryWrapper<>();
			if (!StringUtils.isEmpty(bbsQueryDto.getNickName())) {
				queryWrapper.lambda().like(Bbs::getNickName, bbsQueryDto.getNickName());
			}

			if (bbsQueryDto.getReplied() != null) {
				queryWrapper.lambda().eq(Bbs::getReplied, bbsQueryDto.getReplied());
			}

			if (bbsQueryDto.getKnowledgeType() != null) {
				queryWrapper.lambda().eq(Bbs::getKnowledgeType, bbsQueryDto.getKnowledgeType());
			}

			if (!StringUtils.isEmpty(bbsQueryDto.getCreateTimeStart()) && !StringUtils.isEmpty(bbsQueryDto.getCreateTimeEnd())) {
				queryWrapper.and(wrapper -> wrapper.lambda()
						.between(Bbs::getCreateTime, new Timestamp(bbsQueryDto.getCreateTimeStart()), new Timestamp(bbsQueryDto.getCreateTimeEnd())));
			}
			queryWrapper.lambda().eq(Bbs::getBbsType, 1);
			queryWrapper.orderByDesc("createtime");

			Page<Bbs> page = new Page<>(bbsQueryDto.getCurrentPage(), bbsQueryDto.getPageSize());
			bbsOracleMapper.selectPage(page, queryWrapper);

			if (CollectionUtil.isEmpty(page.getRecords())) {
				BbsDto bbsDto = new BbsDto();
				bbsDto.setCurrentPage(bbsQueryDto.getCurrentPage());
				bbsDto.setPageSize(bbsQueryDto.getPageSize());
				bbsDto.setTotal(page.getTotal());
				bbsDto.setBbsList(page.getRecords());
				return bbsDto;
			}
			//查询回复ID、提问人ID
			List<UserInfo> replyNames;
			List<UserInfo> nickNames;
			Map<String, String> replyNamesMap = new HashMap<>();
			Map<String, String> nickNamesMap = new HashMap<>();
			String replyNameIds = page.getRecords().stream().map(Bbs::getReplyName).distinct().collect(Collectors.joining(","));
			String nickNameIds = page.getRecords().stream().map(Bbs::getNickName).distinct().collect(Collectors.joining(","));
			if (!StringUtils.isEmpty(replyNameIds)) {
				replyNames = tblStaffOracleService.getCreatorUserInfos(replyNameIds);
				if (CollectionUtil.isNotEmpty(replyNames)) {
					replyNamesMap = replyNames.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
			}
			if (!StringUtils.isEmpty(nickNameIds)) {
				nickNames = tblStaffOracleService.getCreatorUserInfos(nickNameIds);
				if (CollectionUtil.isNotEmpty(nickNames)) {
					nickNamesMap = nickNames.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
			}
			Map<String, String> finalReplyNamesMap = replyNamesMap;
			Map<String, String> finalNickNamesMap = nickNamesMap;
			page.getRecords().forEach(x -> {
				if (!StringUtils.isEmpty(x.getReplyName())) {
					x.setReplyNames(finalReplyNamesMap.getOrDefault(x.getReplyName(), ""));
				}
				if (!StringUtils.isEmpty(x.getNickName())) {
					x.setNickNames(finalNickNamesMap.getOrDefault(x.getNickName(), ""));
				}
				x.setIsReplyDisplay(flags);
			});
			//只有授权回复人员才能回复
			BbsDto bbsDto = new BbsDto();
			bbsDto.setCurrentPage(bbsQueryDto.getCurrentPage());
			bbsDto.setPageSize(bbsQueryDto.getPageSize());
			bbsDto.setTotal(page.getTotal());
			bbsDto.setBbsList(page.getRecords());
			return bbsDto;
		} else {
			QueryWrapper<Bbs> queryWrapper = new QueryWrapper<>();
			if (!StringUtils.isEmpty(bbsQueryDto.getNickName())) {
				queryWrapper.lambda().like(Bbs::getNickName, bbsQueryDto.getNickName());
			}

			if (bbsQueryDto.getReplied() != null) {
				queryWrapper.lambda().eq(Bbs::getReplied, bbsQueryDto.getReplied());
			}

			if (!StringUtils.isEmpty(bbsQueryDto.getCreateTimeStart()) && !StringUtils.isEmpty(bbsQueryDto.getCreateTimeEnd())) {
				queryWrapper.and(wrapper -> wrapper.lambda()
						.between(Bbs::getCreateTime, new Timestamp(bbsQueryDto.getCreateTimeStart()), new Timestamp(bbsQueryDto.getCreateTimeEnd())));
			}

			queryWrapper.orderByDesc("create_time");

			Page<Bbs> page = new Page<>(bbsQueryDto.getCurrentPage(), bbsQueryDto.getPageSize());
			bbsMapper.selectPage(page, queryWrapper);

			BbsDto bbsDto = new BbsDto();
			bbsDto.setCurrentPage(bbsQueryDto.getCurrentPage());
			bbsDto.setPageSize(bbsQueryDto.getPageSize());
			bbsDto.setTotal(page.getTotal());
			bbsDto.setBbsList(page.getRecords());

			return bbsDto;
		}

	}

	/**
	 * 回复列表接口
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getReplyList(Long id) {
		QueryWrapper<Bbs> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Bbs::getParentId, id).or().eq(Bbs::getId, id);
		queryWrapper.orderBy(true, true, "id");
		List<Bbs> bbs = bbsMapper.selectList(queryWrapper);
		if (CollectionUtil.isEmpty(bbs)) {
			return ResponseFormat.retParam(200, 200, null);
		}
		//查询回复ID、提问人ID
		List<UserInfo> replyNames;
		List<UserInfo> nickNames;
		Map<String, String> replyNamesMap = new HashMap<>();
		Map<String, String> nickNamesMap = new HashMap<>();
		String replyNameIds = bbs.stream().map(Bbs::getReplyName).distinct().collect(Collectors.joining(","));
		String nickNameIds = bbs.stream().map(Bbs::getNickName).distinct().collect(Collectors.joining(","));
		if (!StringUtils.isEmpty(replyNameIds)) {
			replyNames = tblStaffOracleService.getCreatorUserInfos(replyNameIds);
			if (CollectionUtil.isNotEmpty(replyNames)) {
				replyNamesMap = replyNames.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
			}
		}
		if (!StringUtils.isEmpty(nickNameIds)) {
			nickNames = tblStaffOracleService.getCreatorUserInfos(nickNameIds);
			if (CollectionUtil.isNotEmpty(nickNames)) {
				nickNamesMap = nickNames.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
			}
		}
		Map<String, String> finalReplyNamesMap = replyNamesMap;
		Map<String, String> finalNickNamesMap = nickNamesMap;
		bbs.forEach(x -> {
			if (!StringUtils.isEmpty(x.getReplyName())) {
				x.setReplyNames(finalReplyNamesMap.getOrDefault(x.getReplyName(), ""));
			}
			if (!StringUtils.isEmpty(x.getNickName())) {
				x.setNickNames(finalNickNamesMap.getOrDefault(x.getNickName(), ""));
			}
		});
		return ResponseFormat.retParam(200, 200, bbs);
	}
}
