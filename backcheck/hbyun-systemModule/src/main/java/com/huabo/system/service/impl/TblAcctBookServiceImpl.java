package com.huabo.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblAcctBook;
import com.huabo.system.mapper.TblAcctBookDao;
import com.huabo.system.service.TblAcctBookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblAcctBookServiceImpl implements TblAcctBookService {

    @Resource
    private TblAcctBookDao tblAcctBookDao;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> findByTypeNewZB(String token, Integer pageNumber, Integer pageSize, BigDecimal pid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
                PageInfo<TblAcctBook> pageInfo = new PageInfo<TblAcctBook>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                TblStaffUtil staff = userProvider.get();
                if (pid == null) {
                	pid = staff.getCurrentOrg().getOrgid();
                }
                
                Page<TblAcctBook> page = new Page<TblAcctBook>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
            	IPage<TblAcctBook> pageList = tblAcctBookDao.findByTypeNewZB(page, pid);
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                
                resultMap.put("pageInfo", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }


}
