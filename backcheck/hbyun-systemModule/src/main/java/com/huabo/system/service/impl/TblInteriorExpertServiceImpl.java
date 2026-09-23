package com.huabo.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblInteriorExpert;
import com.huabo.system.mapper.TblInteriorExpertDao;
import com.huabo.system.service.TblInteriorExpertService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblInteriorExpertServiceImpl implements TblInteriorExpertService {

    @Resource
    private TblInteriorExpertDao tblInteriorExpertDao;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public TblInteriorExpert findInterior(TblInteriorExpert interior) {
        List<TblInteriorExpert> tblInterior = tblInteriorExpertDao.findInterior(interior);
        return null;
    }
    
    @Override
    public void save(TblInteriorExpert tie) {
        tblInteriorExpertDao.insertInteriorExpert(tie);
    }


    @Override
    public void update(TblInteriorExpert tblinter) {
        tblInteriorExpertDao.updateInteriorExpert(tblinter);
    }

    @Override
    public Map<String, Object> getExperList(Find find, String staffId, Integer pageNumber, Integer pageSize, String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            BigDecimal orgid = user.getCurrentOrg().getOrgid();
            PageInfo<TblInteriorExpert> pageInfo = new PageInfo<TblInteriorExpert>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<TblInteriorExpert> page = new Page<TblInteriorExpert>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblInteriorExpert> pageList = tblInteriorExpertDao.selectListByPageInfoo(page, orgid, find);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public TblInteriorExpert findInteriorExpertId(BigDecimal userid) {
        List<TblInteriorExpert> list = this.tblInteriorExpertDao.findByRid(userid);
        return list != null && list.size() > 0 ? (TblInteriorExpert) list.get(0) : null;
    }

    @Override
    public Map<String, Object> removeNbzj(BigDecimal interiorid) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                this.tblInteriorExpertDao.deleteNbzj(interiorid);
                resultMap.put("code", "1");
                resultMap.put("msg", "删除成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
    }

}
