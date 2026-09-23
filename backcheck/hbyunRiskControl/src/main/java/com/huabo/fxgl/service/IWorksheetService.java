package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Worksheet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
public interface IWorksheetService extends IService<Worksheet> {

    public Boolean isSJByOrgId(String userOrgid);

    IPage<Worksheet> findAllTblWorksheetByorgid(String orgid, String orgtype, IPage page , String type, Find find);

    public void update(Worksheet worksheet);

    String findByTblWorkSheetnumber(String num, String type, BigDecimal orgId);

     List<Worksheet> findAllTblWorksheetAlls(String type,String orgid) ;

    List<Worksheet> findAllTblWorksheetAll(String type,String orgid);
}
