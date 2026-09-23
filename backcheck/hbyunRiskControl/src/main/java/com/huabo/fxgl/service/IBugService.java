package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Bug;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Bug;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Risk;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IBugService extends IService<Bug> {

    JsonBean defectDetail(String id, String token) throws Exception;

    JsonBean defectList(String orgid, String orgtype, Integer pageNumber, Integer pageSize, String searchbegintime, String searchendtime, String plancode, String plantype, String state, String buglevelquery, String type, String token) throws Exception;

    JsonBean defectDelete(String selectProjectid, String wt, String type, String wtorgid, String orgid, String choiceSearch);

    Bug findByCode(String code, String type, String orgid);

    IPage<Bug> getBugList(Bug bug, String startdate, String enddate, IPage page, String orgid, String orgtype, String buglevel, String type);

    /*    List<Bug> findALL(String orgid,String type);*/
    List<Object[]> qxglExport(String orgid, String type);

}
