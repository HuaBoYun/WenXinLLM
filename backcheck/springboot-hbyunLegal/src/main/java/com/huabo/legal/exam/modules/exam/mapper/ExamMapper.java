package com.huabo.legal.exam.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.legal.exam.modules.exam.dto.ExamDTO;
import com.huabo.legal.exam.modules.exam.dto.response.ExamReviewRespDTO;
import com.huabo.legal.exam.modules.exam.dto.response.ExamOnlineRespDTO;
import com.huabo.legal.exam.modules.exam.entity.Exam;
import com.huabo.legal.util.PageResult;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* <p>
* 考试Mapper
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
public interface ExamMapper extends BaseMapper<Exam> {

    /**
     * 查找分页内容
     * @param page
     * @param query
     * @return
     */
   // IPage<ExamDTO> paging(Page page, @Param("query") ExamDTO query);

    /**
     * 查找分页内容
     * @param page
     * @param query
     * @return
     */
    List<ExamReviewRespDTO> reviewPaging(@Param("query") ExamDTO query);

    /**
     * 在线考试分页响应类-考生视角
     * @param page
     * @param query
     * @return
     */
  //  IPage<ExamOnlineRespDTO> online(Page page, @Param("query") ExamDTO query,@Param("userId") BigDecimal userId);
    List<ExamOnlineRespDTO> online(@Param("query") ExamDTO query,@Param("userId") BigDecimal userId);


	/**
	 * 查找分页内容
	 * @param query
	 * @return
	 */
	List<ExamDTO> paging(@Param("query") ExamDTO query);
}
