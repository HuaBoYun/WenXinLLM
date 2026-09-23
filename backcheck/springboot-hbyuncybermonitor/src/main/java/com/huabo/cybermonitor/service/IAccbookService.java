package com.huabo.cybermonitor.service;

import com.huabo.cybermonitor.entity.Accbook;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface IAccbookService extends IService<Accbook> {

    List<Accbook> findBookIdByUserAll(BigDecimal staffid, String hbOrgEntityOrgid);

    Accbook findUserById(BigDecimal staffid, BigDecimal orgid);
}
