package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.entity.BiDatasource;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.mapper.AttachmentMapper;
import com.huabo.cybermonitor.mapper.BiDatasourceMapper;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBiDatasourceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BiDatasourceServiceImpl extends ServiceImpl<BiDatasourceMapper, BiDatasource> implements IBiDatasourceService {


    @Override
    public String getTree(String orgid) {
        QueryWrapper  qm = new QueryWrapper();
        qm.eq("ORGID",orgid);
        qm.isNull("ISLEAF");
        long num = this.baseMapper.selectCount(qm);
        if (num == 0) {
            BiDatasource bi = new BiDatasource();
            bi.setDsname("基础数据");
            Organization o = new Organization();
            o.setOrgid(new BigDecimal(orgid));
            bi.setTblOrganization(o);
            bi.setCreatedate(new Date());
            bi.setFatherid(new BigDecimal("-1"));
            this.baseMapper.insert(bi);
        }

        QueryWrapper  qm1 = new QueryWrapper();
        qm1.eq("ORGID",orgid);
        qm1.isNull("ISLEAF");
        qm1.orderByAsc("DSID");
        List<BiDatasource> list = this.baseMapper.selectList(qm1);
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            BiDatasource bid = list.get(i);
            str += "tree.nodes['"+bid.getFatherid()+"_"+bid.getDsid()+"']=\"text:"+bid.getDsname()+";method:check("+bid.getDsid()+","+bid.getFatherid()+");\"\n";
            //str += "tree.nodes['"+ (i==0?-1:cat.getFatherriskcatid())+"_"+cat.getRiskcatid()+"']=\"text:"+cat.getRiskcatname()+";method:check("+cat.getRiskcatid()+","+(cat.getUnit()==null?-1:cat.getUnit())+","+(cat.getIsleaf()==null?0:cat.getIsleaf())+")\";\n";
        }
        return str;
    }
}
