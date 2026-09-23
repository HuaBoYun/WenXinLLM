package com.huabo.legal.startup.bbs.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.startup.bbs.domain.Bbs;
import com.huabo.legal.startup.bbs.service.dto.BbsDto;
import com.huabo.legal.startup.bbs.service.dto.BbsQueryDto;
import com.huabo.legal.vo.param.BbsAdminParam;

/**
 * 留言服务接口
 *
 * @author zhuhuix
 * @date 2022-06-09
 */
public interface BbsService {

	/**
	 * 创建留言
	 * @param bbs 待新增的留言信息
	 * @return 新增成功的留言信息
	 */
	Bbs create(Bbs bbs);

	/**
	 * 删除留言
	 * @param ids 留言id集合
	 * @return 是否成功
	 */
	//Boolean delete(Set<Long> ids);
	Boolean delete(BbsAdminParam ids);

	/**
	 * 更新留言
	 * @param bbs 待更新的留言信息
	 * @return 更新成功的留言信息
	 */
	Bbs saveOrUpdate(Bbs bbs);

	/**
	 * 根据id查找留言
	 * @param id 留言id
	 * @return 留言信息
	 */
	Bbs findById(Long id, Integer staffId);


	/**
	 * 根据查询条件分页查找留言信息
	 * @param bbsQueryDto 查询条件
	 * @return 分页留言信息
	 */
	BbsDto page(BbsQueryDto bbsQueryDto, boolean flags);

	/**
	 *
	 * @param id
	 * @return
	 */
	JsonBean getReplyList(Long id);
}
