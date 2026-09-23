package com.huabo.know.service;

import com.hbfk.util.JsonBean;
import com.huabo.know.entity.TblZsgxLegalPractice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.know.vo.param.JudicialCaseListParam;
import com.huabo.know.vo.param.LawRegulationParam;
import com.huabo.know.vo.param.LegalPracticeListParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
public interface ZshService extends IService<TblZsgxLegalPractice> {

    /**
     * 查询法律实务列表
     * @param token 用户登录token
     * @param legalPracticeListParam    检索条件
     * @return  法律实务列表
     */
    JsonBean getLegalPracticeList(String token, LegalPracticeListParam legalPracticeListParam) throws Exception;

    /**
     * 查询法律实务详情
     * @param token 用户登录token
     * @param id    法律实务id
     * @return  法律实务详情
     */
    JsonBean getLegalPracticeInfo(String token, String id) throws Exception;

    /**
     * 查询法律案例列表
     * @param token 用户登录token
     * @param judicialCaseListParam 检索条件
     * @return  法律案例列表
     */
    JsonBean getJudicialCaseList(String token, JudicialCaseListParam judicialCaseListParam) throws Exception;

    /**
     * 查询法律案例详情
     * @param token 用户登录token
     * @param id    法律案例id
     * @return  法律案例详情
     */
    JsonBean getJudicialCaseInfo(String token, String id) throws Exception;

    /**
     * 查询法律法规列表
     * @param token 用户登录token
     * @param lawRegulationParam    检索条件
     * @return  法律法规列表
     */
    JsonBean getLawRegulationList(String token, LawRegulationParam lawRegulationParam) throws Exception;

    /**
     * 查询法律法规详情
     * @param token 用户登录token
     * @param id    法律法规id
     * @return  法律法规详情
     */
    JsonBean getLawRegulationInfo(String token, String id) throws Exception;


    /**
     * 查询条件导航栏
     * @param types
     * @return
     */
    JsonBean sidebar(List<String> types) throws Exception;

    /**
     * 法律实务下载
     *
     * @param id       主键id
     * @param response
     * @return
     */
    JsonBean legalPracticeDownload(String id, HttpServletResponse response) throws Exception;

    /**
     * 法律实务批量下载
     * @param ids   主键ids
     * @param response
     * @return
     */
    JsonBean legalPracticeZipDownload(List<String> ids, HttpServletResponse response) throws Exception;

    /**
     * 法律案例下载
     * @param id    主键id
     * @param response
     * @return
     */
    JsonBean judicialCaseDownload(String id, HttpServletResponse response) throws Exception;

    /**
     * 法律案例批量下载
     * @param ids   主键ids
     * @param response
     * @return
     */
    JsonBean judicialCaseZipDownload(List<String> ids, HttpServletResponse response) throws Exception;

    /**
     * 法律法规下载
     * @param id    主键id
     * @param response
     * @return
     */
    JsonBean lawRegulationDownload(String id, HttpServletResponse response) throws Exception;

    /**
     * 法律法规批量下载
     * @param ids   主键ids
     * @param response
     * @return
     */
    JsonBean lawRegulationZipDownload(List<String> ids, HttpServletResponse response) throws Exception;
}
