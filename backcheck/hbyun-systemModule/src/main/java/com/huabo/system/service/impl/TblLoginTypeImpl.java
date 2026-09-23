package com.huabo.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblLoginType;
import com.huabo.system.mapper.TblLoginTypeMapper;
import com.huabo.system.service.TblLoginTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service("TblLoginTypeService")
@Slf4j
@Transactional
public class TblLoginTypeImpl implements TblLoginTypeService {

    @Resource
    private TblLoginTypeMapper tblLoginTypeMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> findAll(String token, String staffId, Integer pageNumber, Integer pageSize) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil staff = userProvider.get();
                BigDecimal orgid = staff.getCurrentOrg().getOrgid();
                //BigDecimal orgid = new BigDecimal(1);

                PageInfo<TblLoginType> pageInfo = new PageInfo<TblLoginType>();
                if (pageSize != null) {
                    pageInfo.setPageSize(pageSize);
                }
                pageInfo.setCurrentPage(pageNumber);
                try {
                	
                	Page<TblLoginType> page = new Page<TblLoginType>(pageNumber,pageSize);
                    page.setOptimizeCountSql(false); // 禁用自动优化
                	IPage<TblLoginType> pageList = tblLoginTypeMapper.selectListByPageInfo(page, orgid);
                	
                    pageInfo.setTlist(pageList.getRecords());
                    pageInfo.setTotalRecord((int) pageList.getTotal());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public TblLoginType findByid(String id) {
        return tblLoginTypeMapper.findByLoginId(id);
    }

    @Override
    public void updatetblLoginType(TblLoginType tblt) {
        tblLoginTypeMapper.updatetblLoginType(tblt);
    }

    @Override
    public void save(TblLoginType tnt) {
        tblLoginTypeMapper.saveTblLoginType(tnt);
    }

    @Override
    public Map<String, Object> findByLoginId(String loginid) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            TblLoginType tblLoginType = tblLoginTypeMapper.findByLoginId(loginid);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", tblLoginType);
            return resultMap;
    }

    @Override
    public Map<String, Object> del(String loginid) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            tblLoginTypeMapper.deleteByLoginId(loginid);
            resultMap.put("code", "1");
            resultMap.put("msg", "删除成功");
            return resultMap;
    }


}
