package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblAuditOption;
import com.huabo.system.mapper.TblAuditOptionMapper;
import com.huabo.system.service.TblAuditOptionService;

@Service
public class TblAuditOptionServiceImpl implements TblAuditOptionService {

    @Resource
    private TblAuditOptionMapper tblAuditOptionMapper;

    @Override
    public List<TblAuditOption> findOptionByRelationId(BigDecimal planid) {
        return tblAuditOptionMapper.findOptionByRelationId(planid);

    }

    @Override
    public List<TblAuditOption> findOptionByRelation(String sheetid, String cyid) {
        List<Object[]> list = this.tblAuditOptionMapper.OBJlistBySql(sheetid, cyid);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<TblAuditOption> aoList = new ArrayList();
        for (int i = 0; i < list.size(); ++i) {
            Object[] o = (Object[]) list.get(i);
            TblAuditOption ao = new TblAuditOption();
            //ao.setStaffidName(o[0].toString());该字段实体类与数据库均没有，也没有任何关联
            ao.setOptDesc(o[1].toString());
            ao.setOptState(o[2].toString());
            ao.setOptStaffid(new BigDecimal(o[3].toString()));

            try {
                if (o[4] != null && o[4] != "") {
                    String str2 = o[4].toString();
                    Date date = sdf.parse(str2);
                    ao.setCreateDate(date);
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }

            aoList.add(ao);
        }
        return aoList;
    }

}
