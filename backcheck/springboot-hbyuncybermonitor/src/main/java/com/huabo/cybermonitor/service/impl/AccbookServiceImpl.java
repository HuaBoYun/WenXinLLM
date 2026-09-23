package com.huabo.cybermonitor.service.impl;

import com.huabo.cybermonitor.entity.Accbook;
import com.huabo.cybermonitor.mapper.AccbookMapper;
import com.huabo.cybermonitor.service.IAccbookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@Service
public class AccbookServiceImpl extends ServiceImpl<AccbookMapper, Accbook> implements IAccbookService {

    @Resource
    AccbookMapper accbookMapper;


    @Override
    public List<Accbook> findBookIdByUserAll(BigDecimal staffid, String hbOrgEntityOrgid) {
        return accbookMapper.findBookIdByUserAll(staffid,hbOrgEntityOrgid);
    }

    @Override
    public Accbook findUserById(BigDecimal staffid, BigDecimal orgid) {
        List<Accbook> listBySql = accbookMapper.findBookByUser1(staffid, orgid);
        if (listBySql != null && listBySql.size() > 0) {
            return listBySql.get(0);
        } else {
            listBySql = accbookMapper.findBookByUser2(staffid, orgid);
            ;
            if (listBySql != null && listBySql.size() > 0) {
                return listBySql.get(0);
            }
        }
        return null;
    }
}
