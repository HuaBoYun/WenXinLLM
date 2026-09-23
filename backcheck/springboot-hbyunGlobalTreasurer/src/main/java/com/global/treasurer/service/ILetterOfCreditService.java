package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.LetterOfCreditDTO;
import com.global.treasurer.dto.LetterOfCreditQueryDTO;
import com.global.treasurer.entity.TblLetterOfCredit;
import com.global.treasurer.vo.LetterOfCreditVO;

import java.util.List;
import java.util.Map;

/**
 * 信用证Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface ILetterOfCreditService {

    /**
     * 分页查询信用证列表
     */
    PageInfo<LetterOfCreditVO> selectLetterOfCreditList(LetterOfCreditQueryDTO queryDTO);

    /**
     * 根据ID查询信用证详情
     */
    LetterOfCreditVO selectLetterOfCreditById(Long lcId);

    /**
     * 新增信用证申请
     */
    TblLetterOfCredit insertLetterOfCredit(LetterOfCreditDTO dto);

    /**
     * 修改信用证申请
     */
    TblLetterOfCredit updateLetterOfCredit(LetterOfCreditDTO dto);

    /**
     * 批量删除信用证
     */
    boolean deleteLetterOfCreditByIds(Long[] lcIds);

    /**
     * 提交信用证申请
     */
    boolean submitLCApplication(Long lcId);

    /**
     * 信用证申请审批
     */
    boolean approveLCApplication(Long lcId, Map<String, Object> approvalData);

    /**
     * 开立信用证
     */
    boolean issueLetterOfCredit(Long lcId);

    /**
     * 修改信用证
     */
    boolean amendLetterOfCredit(Long lcId, Map<String, Object> amendData);

    /**
     * 单据处理
     */
    boolean processDocuments(Long lcId, Map<String, Object> documentData);

    /**
     * 获取单据列表
     */
    List<Map<String, Object>> getDocumentList(Long lcId, Map<String, Object> params);
}

