package com.huabo.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblAccBook;
import com.huabo.system.entity.TblAcctBook;
import com.huabo.system.entity.TblAcquisitionRecord;
import com.huabo.system.mapper.TblAccBookMapper;
import com.huabo.system.mapper.TblAcquisitionRecordMapper;
import com.huabo.system.service.TblAcquisitionRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblAcquisitionRecordServiceImpl implements TblAcquisitionRecordService {

    @Resource
    private TblAccBookMapper accBookMapper;

    @Resource
    private TblAcquisitionRecordMapper tblAcquisitionRecordMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> findByPage(Integer pageNumber, Integer pageSize, String token, String staffId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();

            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
           
            	//获取可以采集的年份
                List<TblAccBook> acYearList = accBookMapper.findAcquisitionYear(orgid);
                //List<Integer> yearList1 = selectAcquisitionYear(yearList);
                if(acYearList == null || acYearList.size() == 0) {
                     resultMap.put("code", "0");
                     resultMap.put("msg", "未配置数据采集！");
                     return resultMap;
                }
                List<Integer> yearList = new ArrayList<Integer>(0);
                
                for (TblAccBook years : acYearList) {
					for (int i = years.getStartDate() ; i <= years.getEndDate() ; i ++) {
						yearList.add(i);
					}
				}
                
                //获取张涛名称
                String ztname = accBookMapper.findAcquisitoionName(orgid);
                List<TblAcctBook> accBookList = accBookMapper.findAccBookByOrgId(orgid);
                //获取数据采集记录
                PageInfo<TblAcquisitionRecord> pageInfo = new PageInfo<TblAcquisitionRecord>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                Page<TblAcquisitionRecord> page = new Page<TblAcquisitionRecord>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblAcquisitionRecord> pageList = tblAcquisitionRecordMapper.selectListByPageInfo(page, orgid);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("pageInfo", pageInfo);
                dataMap.put("yearList", yearList);
                dataMap.put("ztname", ztname);
                dataMap.put("companyName", staff.getCurrentOrg().getOrgname());
                dataMap.put("accBookList", accBookList);
                resultMap.put("data", dataMap);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

}

