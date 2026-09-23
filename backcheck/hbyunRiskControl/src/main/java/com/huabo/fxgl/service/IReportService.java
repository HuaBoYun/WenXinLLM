package com.huabo.fxgl.service;

import com.huabo.fxgl.controller.ReportMsg;
import com.huabo.fxgl.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Staff;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
@SuppressWarnings("rawtypes")
public interface IReportService extends IService<Report> {

    JsonBean reportList(Find find, String type, String projectId, String view, Integer number, Integer pageSize,String token) throws Exception;

    public List search(String name, String time) throws ParseException;


    JsonBean reportDelete(String[] ids);


    JsonBean reportDetail(String id, String type);

     boolean add(Report report, String attids);

    boolean updateReport(Report report, String attids);

    boolean findbyId(String id);
    
    JsonBean reportToLeader( String token,BigDecimal id) throws Exception;

    /**
     * 总公司查看分公司上报的信息
     * @param find
     * @param type
     * @param projectId
     * @param view
     * @param pageNumber
     * @param pageSize
     * @param token
     * @return
     */
    JsonBean companyReportList(Find find, String type, String projectId, String view, Integer pageNumber, Integer pageSize, String token) throws Exception;


  Map<String, Object>  getRiskReportTypeCountByCompany(String token,String company)throws Exception;
}
