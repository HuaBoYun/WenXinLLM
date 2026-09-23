package com.huabo.legal.exam.modules.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.legal.exam.modules.paper.dto.PaperDTO;
import com.huabo.legal.exam.modules.paper.dto.request.PaperListReqDTO;
import com.huabo.legal.exam.modules.paper.dto.response.PaperListRespDTO;
import com.huabo.legal.exam.modules.paper.entity.Paper;
import com.huabo.legal.util.PageResult;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 试卷Mapper
 * </p>
 *
 * @author 聪明笨狗
 * @since 2020-05-25 16:33
 */
public interface PaperMapper extends BaseMapper<Paper> {

	/**
	 * 查找试卷分页
	 * @param page
	 * @param query
	 * @return
	 */
	List<PaperListRespDTO> paging(Page page, @Param("query") PaperListReqDTO query);


	/**
	 * 试卷列表响应类
	 * @param query
	 * @return
	 */
	List<PaperListRespDTO> list(@Param("query") PaperDTO query);

	/**
	 * 更新 考试生离场排查 满90自动交卷
	 */
	void updateExamLeaveJob(@Param("id") String id);
}
