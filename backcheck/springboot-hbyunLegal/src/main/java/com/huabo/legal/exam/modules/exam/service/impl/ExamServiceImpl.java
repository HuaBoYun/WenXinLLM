package com.huabo.legal.exam.modules.exam.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.core.enums.JoinType;
import com.huabo.legal.exam.core.enums.OpenType;
import com.huabo.legal.exam.core.exception.ServiceException;
import com.huabo.legal.exam.core.utils.BeanMapper;
import com.huabo.legal.exam.modules.exam.dto.ExamDTO;
import com.huabo.legal.exam.modules.exam.dto.ExamRepoDTO;
import com.huabo.legal.exam.modules.exam.dto.ext.ExamRepoExtDTO;
import com.huabo.legal.exam.modules.exam.dto.request.ExamSaveReqDTO;
import com.huabo.legal.exam.modules.exam.dto.response.ExamOnlineRespDTO;
import com.huabo.legal.exam.modules.exam.dto.response.ExamReviewRespDTO;
import com.huabo.legal.exam.modules.exam.entity.Exam;
import com.huabo.legal.exam.modules.exam.mapper.ExamMapper;
import com.huabo.legal.exam.modules.exam.service.ExamDepartService;
import com.huabo.legal.exam.modules.exam.service.ExamRepoService;
import com.huabo.legal.exam.modules.exam.service.ExamService;
import com.huabo.legal.util.PageResult;

/**
* <p>
* 考试业务实现类
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {


    @Autowired
    private ExamRepoService examRepoService;

    @Autowired
    private ExamDepartService examDepartService;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public void save(ExamSaveReqDTO reqDTO) {

        // ID
        String id = reqDTO.getId();

        if(StringUtils.isBlank(id)){
            id = IdWorker.getIdStr();
        }

        //复制参数
        Exam entity = new Exam();
        //考试初始状态
		entity.setState(0);
        // 计算分值
        this.calcScore(reqDTO);


        // 复制基本数据
        BeanMapper.copy(reqDTO, entity);
        entity.setId(id);

        // 修复状态
        if (reqDTO.getTimeLimit()!=null
                && !reqDTO.getTimeLimit()
                && reqDTO.getState()!=null
                && reqDTO.getState() == 2) {
            entity.setState(0);
        } else {
            entity.setState(reqDTO.getState());
        }

        // 题库组卷
        if(JoinType.REPO_JOIN.equals(reqDTO.getJoinType())){
            try {
                examRepoService.saveAll(id, reqDTO.getRepoList());
            }catch (DuplicateKeyException e){
                throw new ServiceException(1, "不能选择重复的题库！");
            }
        }

        // 开放的部门
        if(OpenType.DEPT_OPEN.equals(reqDTO.getOpenType())){
            examDepartService.saveAll(id, reqDTO.getDepartIds());
        }

        System.out.println(entity);
        this.saveOrUpdate(entity);

    }

    @Override
    public ExamSaveReqDTO findDetail(String id) {
        ExamSaveReqDTO respDTO = new ExamSaveReqDTO();
        Exam exam = this.getById(id);
        BeanMapper.copy(exam, respDTO);

        // 考试部门
        List<String> departIds = examDepartService.listByExam(id);
        respDTO.setDepartIds(departIds);

        // 题库
        List<ExamRepoExtDTO> repos = examRepoService.listByExam(id);
        respDTO.setRepoList(repos);

        return respDTO;
    }

    @Override
    public ExamDTO findById(String id) {
        ExamDTO respDTO = new ExamDTO();
        Exam exam = this.getById(id);
        BeanMapper.copy(exam, respDTO);
        return respDTO;
    }

	@Override
	public PageResult<ExamDTO> paging1(PagingReqDTO<ExamDTO> reqDTO) {
		//转换结果
		PageInfo<ExamDTO> objectPageInfo = PageMethod.startPage(reqDTO.getCurrent(), reqDTO.getSize())
				.doSelectPageInfo(() -> baseMapper.paging(reqDTO.getParams()));
		return new PageResult<ExamDTO>().build(objectPageInfo);
	}
	
    @Override
    public PageResult<ExamOnlineRespDTO> onlinePaging(PagingReqDTO<ExamDTO> reqDTO,String token) throws Exception {
    	 TblStaffUtil staff = userProvider.get();
    	 PageInfo<ExamOnlineRespDTO> objPageInfo = PageMethod.startPage(reqDTO.getCurrent(),reqDTO.getSize())
    			 .doSelectPageInfo(() ->baseMapper.online(reqDTO.getParams(),staff.getStaffid()));
    	 return new PageResult<ExamOnlineRespDTO>().build(objPageInfo);
    }



    @Override
    public PageResult<ExamReviewRespDTO> reviewPaging(PagingReqDTO<ExamDTO> reqDTO) {     
        PageInfo<ExamReviewRespDTO> objectPageInfo = PageMethod.startPage(reqDTO.getCurrent(), reqDTO.getSize())
				.doSelectPageInfo(() -> baseMapper.reviewPaging(reqDTO.getParams()));
		return new PageResult<ExamReviewRespDTO>().build(objectPageInfo);
    }


    /**
     * 计算分值
     * @param reqDTO
     */
    private void calcScore(ExamSaveReqDTO reqDTO){

        // 主观题分数
        int objScore = 0;

        // 题库组卷
        if(JoinType.REPO_JOIN.equals(reqDTO.getJoinType())){
            List<ExamRepoExtDTO> repoList = reqDTO.getRepoList();

            for(ExamRepoDTO item: repoList){
                if(item.getRadioCount()!=null
                        && item.getRadioCount()>0
                        && item.getRadioScore()!=null
                        && item.getRadioScore()>0){
                    objScore+=item.getRadioCount()*item.getRadioScore();
                }
                if(item.getMultiCount()!=null
                        && item.getMultiCount()>0
                        && item.getMultiScore()!=null
                        && item.getMultiScore()>0){
                    objScore+=item.getMultiCount()*item.getMultiScore();
                }
                if(item.getJudgeCount()!=null
                        && item.getJudgeCount()>0
                        && item.getJudgeScore()!=null
                        && item.getJudgeScore()>0){
                    objScore+=item.getJudgeCount()*item.getJudgeScore();
                }
            }
        }


        reqDTO.setTotalScore(objScore);
    }

}
