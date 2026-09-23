package com.huabo.legal.exam.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.modules.exam.dto.ExamDTO;
import com.huabo.legal.exam.modules.exam.dto.request.ExamSaveReqDTO;
import com.huabo.legal.exam.modules.exam.dto.response.ExamOnlineRespDTO;
import com.huabo.legal.exam.modules.exam.dto.response.ExamReviewRespDTO;
import com.huabo.legal.exam.modules.exam.entity.Exam;
import com.huabo.legal.util.PageResult;

/**
* <p>
* 考试业务类
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
public interface ExamService extends IService<Exam> {

    /**
     * 保存考试信息
     * @param reqDTO
     */
    void save(ExamSaveReqDTO reqDTO);

    /**
     * 查找考试详情
     * @param id
     * @return
     */
    ExamSaveReqDTO findDetail(String id);

    /**
     * 查找考试详情--简要信息
     * @param id
     * @return
     */
    ExamDTO findById(String id);
    
	/**
	 * 分页查询数据
	 * @param reqDTO
	 * @return
	 */
	PageResult<ExamDTO> paging1(PagingReqDTO<ExamDTO> reqDTO);

    /**
     * 在线考试分页响应类-考生视角
     * @param reqDTO
     * @return
     * @throws Exception 
     */
	PageResult<ExamOnlineRespDTO> onlinePaging(PagingReqDTO<ExamDTO> reqDTO,String token) throws Exception;

    /**
     * 待阅试卷列表
     * @param reqDTO
     * @return
     */
	PageResult<ExamReviewRespDTO> reviewPaging(PagingReqDTO<ExamDTO> reqDTO);
}
