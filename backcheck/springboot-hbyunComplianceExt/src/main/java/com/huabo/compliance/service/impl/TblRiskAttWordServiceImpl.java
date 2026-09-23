package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblRiskAttWord;
import com.huabo.compliance.mapper.TblRiskAttWordMapper;
import com.huabo.compliance.service.ITblRiskAttWordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-09-13
 */
@Service
public class TblRiskAttWordServiceImpl extends ServiceImpl<TblRiskAttWordMapper, TblRiskAttWord> implements ITblRiskAttWordService {
    @Resource
    TblRiskAttWordMapper riskAttWordMapper;
    @Override
    public TblRiskAttWord getFile(String reportType, String orgid, String id) {


        String sql = "select * from tbl_risk_att_word where orgid= '"+orgid+"'";
        if(reportType.equals("fygk")) {
            sql+=" and ASSID ='-1' ORDER BY ATTID DESC";
        }else if(reportType.equals("nkhg")) {
            sql+=" and ASSID="+id;
        }
        List<TblRiskAttWord> list = riskAttWordMapper.queryList(sql);
        if (list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }
}
