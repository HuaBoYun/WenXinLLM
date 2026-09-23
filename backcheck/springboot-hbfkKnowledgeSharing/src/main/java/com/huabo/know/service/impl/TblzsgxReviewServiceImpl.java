package com.huabo.know.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.know.entity.*;
import com.huabo.know.mapper.*;
import com.huabo.know.page.BasePageParam;
import com.huabo.know.service.TblzsgxReviewService;
import com.huabo.know.vo.result.ReviewItemInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblzsgxReviewServiceImpl implements TblzsgxReviewService {

	@Autowired
	private TblzsgxReviewMapper tblzsgxReviewMapper;
	@Autowired
	private TblzsgxReviewItemMapper tblzsgxReviewItemMapper;
	@Autowired
	private TblZsgxReviewItemInfoMapper tblZsgxReviewItemInfoMapper;
	@Autowired
	private UserProvider userProvider;

	@Override
	public JsonBean getReviewItemTree(String reviewId) throws Exception {
		//鉴权
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		Map<String,Object> resultMap = new HashMap<String,Object>(1);

		//查询第一级风险清单配置项
		List<TblzsgxReviewItem> topReviewItems = getTopReviewItems(reviewId);
		if (CollectionUtils.isEmpty(topReviewItems)) {
			// 没有风险清单，直接返回null
			resultMap.put("data", Collections.emptyList());
			return ResponseFormat.retParam(1, 200, resultMap);
		}

		// 查询风险清单配置项树形结构
		List<ReviewItemInfo> tree = this.getReviewTree(reviewId);
		resultMap.put("data", tree);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	/**
	 * 查询风险清单配置项树形结构
	 * @param reviewId
	 * @return
	 */
	private List<ReviewItemInfo> getReviewTree(String reviewId) {
		//查询第一级风险清单配置项
		List<TblzsgxReviewItem> topReviewItems = getTopReviewItems(reviewId);
		if (CollectionUtils.isEmpty(topReviewItems)) {
			//未查询到配置项，返回空列表
			return Collections.emptyList();
		}

		// 查询第二级别风险清单配置项
		List<String> topIds = topReviewItems.stream().map(TblzsgxReviewItem::getId).collect(Collectors.toList());
		List<TblzsgxReviewItem> secondReviewItems = getReviewItemsByParent(topIds);
		if (CollectionUtils.isEmpty(secondReviewItems)) {
			// 没有第二层级，返回第一层级结果
			return topReviewItems.stream().map(source->{
				ReviewItemInfo reviewItemInfo = new ReviewItemInfo();
				BeanUtils.copyProperties(source,reviewItemInfo);
				return reviewItemInfo;
			}).collect(Collectors.toList());
		}

		// 查询第三级别风险清单配置项
		List<String> secondIds = secondReviewItems.stream().map(TblzsgxReviewItem::getId).collect(Collectors.toList());
		List<TblzsgxReviewItem> thirdReviewItems = getReviewItemsByParent(secondIds);

		// 树形数据格式
		Map<String,List<ReviewItemInfo>> thirdMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(thirdReviewItems)) {
			thirdMap = thirdReviewItems.stream().map(source -> {
				ReviewItemInfo reviewItemInfo = new ReviewItemInfo();
				BeanUtils.copyProperties(source, reviewItemInfo);
				return reviewItemInfo;
			}).sorted(Comparator.comparing(ReviewItemInfo::getSort)).collect(Collectors.groupingBy(ReviewItemInfo::getPid));
		}

		Map<String, List<ReviewItemInfo>> finalThirdMap = thirdMap;
		Map<String, List<ReviewItemInfo>> secondMap = secondReviewItems.stream().map(source -> {
			ReviewItemInfo reviewItemInfo = new ReviewItemInfo();
			BeanUtils.copyProperties(source, reviewItemInfo);
			reviewItemInfo.setChildren(finalThirdMap.get(reviewItemInfo.getId()));
			return reviewItemInfo;
		}).sorted(Comparator.comparing(ReviewItemInfo::getSort)).collect(Collectors.groupingBy(ReviewItemInfo::getPid));

		List<ReviewItemInfo> rootList = new ArrayList<>();
		for (TblzsgxReviewItem topReviewItem : topReviewItems) {
			ReviewItemInfo reviewItemInfo = new ReviewItemInfo();
			BeanUtils.copyProperties(topReviewItem, reviewItemInfo);
			reviewItemInfo.setChildren(secondMap.get(reviewItemInfo.getId()));
			rootList.add(reviewItemInfo);
		}

		return rootList;
	}

	/**
	 * 获取风险清单配置顶级
	 * @param reviewId 可选
	 * @return
	 */
	private List<TblzsgxReviewItem> getTopReviewItems(String reviewId) {
		QueryWrapper<TblzsgxReviewItem> queryWrapper = new QueryWrapper();

		queryWrapper.eq("REVIEW_ID", reviewId);
		queryWrapper.eq("DELETED", 0).eq("PID", "-1");
		queryWrapper.orderByAsc("SORT");

		return tblzsgxReviewItemMapper.selectList(queryWrapper);
	}

	/**
	 * 根据父级id批量获取风险清单配置列表
	 * @param pids
	 * @return
	 */
	private List<TblzsgxReviewItem> getReviewItemsByParent(List<String> pids) {
		QueryWrapper<TblzsgxReviewItem> queryWrapper = new QueryWrapper();
		queryWrapper.in("PID",pids);
		queryWrapper.eq("DELETED", 0);
		queryWrapper.orderByAsc("SORT");
		return tblzsgxReviewItemMapper.selectList(queryWrapper);
	}

	/**
	 * 查询风险清单
	 * @return
	 * @throws Exception
	 */
	@Override
	public JsonBean getReviewCheckList(BasePageParam pageParam)  throws Exception {
		//鉴权
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		// 分页查询风险清单
		IPage<TblzsgxReview> reviewPage = this.getReviewCheck(pageParam);

		Map<String,Object> resultMap = new HashMap<String,Object>(1);
		resultMap.put("data", reviewPage);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	private IPage<TblzsgxReview>  getReviewCheck(BasePageParam pageParam) {
		IPage<TblzsgxReview> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
		QueryWrapper<TblzsgxReview> queryWrapper = new QueryWrapper();
		queryWrapper.eq("DELETED",0);
		queryWrapper.orderByAsc("SORT");
		return tblzsgxReviewMapper.selectPage(page, queryWrapper);
	}

	/**
	 * 查询风险清单配置项详情
	 * @param reviewItemId
	 * @return
	 * @throws Exception
	 */
	@Override
	public JsonBean getReviewItemInfo(String reviewItemId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		QueryWrapper<TblZsgxReviewItemInfo> queryWrapper = new QueryWrapper();
		queryWrapper.eq("DELETED",0).eq("REVIEW_ITEM_ID",reviewItemId);
		TblZsgxReviewItemInfo tblZsgxReviewItemInfo = tblZsgxReviewItemInfoMapper.selectOne(queryWrapper);

		Map<String,Object> resultMap = new HashMap<String,Object>(1);
		resultMap.put("data", tblZsgxReviewItemInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

}
