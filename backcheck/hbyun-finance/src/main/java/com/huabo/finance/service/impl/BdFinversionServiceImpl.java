package com.huabo.finance.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.finance.entity.BdFinversion;
import com.huabo.finance.mapper.BdFinversionMapper;
import com.huabo.finance.service.BdFinversionService;
import com.huabo.finance.vo.BdFinversionVo;

/**
 * <p>
 * 财务数据版本 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-10
 */
@Service
public class BdFinversionServiceImpl extends ServiceImpl<BdFinversionMapper, BdFinversion> implements BdFinversionService {

	@Resource
	private BdFinversionMapper bdFinversionMapper;
	
	@Override
	public JsonBean findAllList(TblStaffUtil staff, BdFinversionVo vo) throws Exception {

		QueryWrapper<BdFinversion> wrapper = new QueryWrapper<BdFinversion>();
		List<BdFinversion> falist = new ArrayList<BdFinversion>(0);
		List<BdFinversion> chlist = new ArrayList<BdFinversion>(0);

		try {
			if(StringUtils.isNotBlank(vo.getHandtext())) {
				// 查询子级
				wrapper.like("HANDTEXT", vo.getHandtext());
				wrapper.isNotNull("PID");
				// 限制查询数量，防止内存溢出
				wrapper.last("LIMIT 1000");
				chlist = this.bdFinversionMapper.selectList(wrapper);

				if(chlist == null || chlist.size() == 0) {
					return ResponseFormat.retParam(1, 50001, falist);
				}

				Set<String> ids = chlist.stream()
					.map(BdFinversion::getPid)
					.filter(pid -> pid != null && !pid.isEmpty())
					.collect(Collectors.toSet());

				if(ids.isEmpty()) {
					return ResponseFormat.retParam(1, 200, falist);
				}

				wrapper.clear();
				wrapper.in("FId", ids);
				falist = this.bdFinversionMapper.selectList(wrapper);
			} else {
				// 查询父级（顶级节点）
				wrapper.isNull("pid");
				// 限制查询数量，防止内存溢出
				wrapper.last("LIMIT 500");
				falist = this.bdFinversionMapper.selectList(wrapper);

				wrapper.clear();
				wrapper.isNotNull("PID");
				// 限制查询数量，防止内存溢出
				wrapper.last("LIMIT 2000");
				chlist = this.bdFinversionMapper.selectList(wrapper);
			}

			// 构建有效父节点ID集合，用于过滤无效的PID
			Set<String> validParentIds = falist.stream()
				.map(BdFinversion::getFid)
				.filter(fid -> fid != null && !fid.isEmpty())
				.collect(Collectors.toSet());

			// 过滤掉无效的子节点（PID不在有效父节点集合中）
			chlist = chlist.stream()
				.filter(obj -> obj != null && obj.getPid() != null && validParentIds.contains(obj.getPid()))
				.collect(Collectors.toList());

			// 构建父子关系
			for (BdFinversion fa : falist) {
				if(fa.getFid() != null) {
					String parentFid = fa.getFid();
					List<BdFinversion> children = chlist.stream()
						.filter(obj -> obj != null && obj.getPid() != null && obj.getPid().equals(parentFid))
						// 防止循环引用：子节点的FID不能等于父节点的FID
						.filter(obj -> !obj.getFid().equals(parentFid))
						.collect(Collectors.toList());
					fa.setChildrenList(children);
				}
			}

			return ResponseFormat.retParam(1, 200, falist);
		} catch (OutOfMemoryError e) {
			// 内存溢出时返回错误信息
			return ResponseFormat.retParam(0, 500, "数据量过大，请使用搜索条件进行过滤");
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public JsonBean save(TblStaffUtil staff, BdFinversion fv) throws Exception {
		
		if(StringUtils.isBlank(fv.getFid())) {
			fv.setFid(RandomUtil.uuStringId());
			fv.setCreationtime(new Date());
			fv.setCreator(staff.getStaffid());
			this.bdFinversionMapper.insert(fv);
		}else {
			fv.setModifiedtime(new Date());
			fv.setModifier(staff.getStaffid());
			this.bdFinversionMapper.updateById(fv);
		}
		
		return ResponseFormat.retParam(1, 200, fv);
	}

	@Override
	public JsonBean getOne(String fid) throws Exception {
		BdFinversion fv = this.bdFinversionMapper.selectById(fid);
		return ResponseFormat.retParam(1, 200, fv);
	}

	@Override
	public JsonBean remove(String fid) throws Exception {
		this.bdFinversionMapper.deleteById(fid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getParentList(TblStaffUtil staff) throws Exception {
		QueryWrapper<BdFinversion> wrapper = new QueryWrapper<BdFinversion>();
		List<BdFinversion> falist = new ArrayList<BdFinversion>(0);
		wrapper.isNull("pid");
		falist = this.bdFinversionMapper.selectList(wrapper);
		return ResponseFormat.retParam(1, 200, falist);
	}

}
