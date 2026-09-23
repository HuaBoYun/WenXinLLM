package com.huabo.legal.exam.modules.user.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.modules.exam.dto.ExamDTO;
import com.huabo.legal.exam.modules.user.UserUtils;
import com.huabo.legal.exam.modules.user.book.dto.UserBookDTO;
import com.huabo.legal.exam.modules.user.exam.dto.request.UserExamReqDTO;
import com.huabo.legal.exam.modules.user.exam.dto.response.UserExamRespDTO;
import com.huabo.legal.exam.modules.user.exam.entity.UserExam;
import com.huabo.legal.exam.modules.user.exam.mapper.UserExamMapper;
import com.huabo.legal.exam.modules.user.exam.service.UserExamService;
import com.huabo.legal.util.PageResult;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
* <p>
* 考试记录业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-09-21 15:13
*/
@Service
public class UserExamServiceImpl extends ServiceImpl<UserExamMapper, UserExam> implements UserExamService {

    @Override
    public JsonBean paging(PagingReqDTO<UserExamReqDTO> reqDTO) {

        //转换结果
        //IPage<UserExamRespDTO> pageData = 
        		//baseMapper.paging(reqDTO.toPage(), reqDTO.getParams());
        		PageInfo<UserExamReqDTO> objectPageInfo = PageMethod.startPage(reqDTO.getCurrent(), reqDTO.getSize())
        				.doSelectPageInfo(() -> baseMapper.paging(reqDTO.getParams()));
        		 return ResponseFormat.retParam(200, 200, new PageResult<UserExamReqDTO>().build(objectPageInfo));
     }



    @Override
    public JsonBean myPaging(PagingReqDTO<UserExamReqDTO> reqDTO) {

        UserExamReqDTO params = reqDTO.getParams();


        if(params==null){
            params = new UserExamReqDTO();
        }

        params.setUserId(UserUtils.getUserId());

        //转换结果
       // PageInfo<UserExamRespDTO> pageData = baseMapper.paging(params);
        PageInfo<UserExamReqDTO> objectPageInfo = PageMethod.startPage(reqDTO.getCurrent(), reqDTO.getSize())
				.doSelectPageInfo(() -> baseMapper.paging(reqDTO.getParams()));
        
        return ResponseFormat.retParam(200, 200, new PageResult<UserExamReqDTO>().build(objectPageInfo));
    }

    @Override
    public void joinResult(String userId, String examId, Integer score, boolean passed) {

        //查询条件
        QueryWrapper<UserExam> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserExam::getUserId, userId)
                .eq(UserExam::getExamId, examId);

        UserExam record = this.getOne(wrapper, false);
        if(record == null){
            record = new UserExam();
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            record.setUserId(userId);
            record.setExamId(examId);
            record.setMaxScore(score);
            record.setPassed(passed);
            this.save(record);
            return;
        }

        // 修复低分数不加入统计问题
        record.setTryCount(record.getTryCount()+1);
        record.setUpdateTime(new Date());

        if(record.getMaxScore() < score){
            record.setMaxScore(score);
            record.setPassed(passed);
        }

        this.updateById(record);


    }

}
