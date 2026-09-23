package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblVideoType;
import com.huabo.system.mapper.TblVideoTypeMapper;
import com.huabo.system.service.TblVideoTypeService;

@Service("tblVideoTypeImpl")
public class TblVideoTypeImpl implements TblVideoTypeService {

    @Resource
    private TblVideoTypeMapper tblVideoTypeMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public String save(TblVideoType tnt, String token, String staffId) {
        if (token == null) {
            return null;
        }
        TblStaffUtil staff = null;
        try {
            staff = userProvider.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tnt != null && tnt.getTypeId() == null) {
            // 新建
            tnt.setVersion(1);
            tnt.setTypename(tnt.getTypename().trim());
            tnt.setOrgid(staff.getCurrentOrg().getOrgid());
            tnt.setTypeId(RandomUtil.uuBigDecimalId());
            this.tblVideoTypeMapper.insertTblVideoType(tnt);
            return JsonBean.success("1");
        } else {
            // 修改
            tnt.setOrgid(staff.getCurrentOrg().getOrgid());
            tnt.setTypename(tnt.getTypename().trim());
            tnt.setVersion(tnt.getVersion() + 1);
            this.tblVideoTypeMapper.updateTblVideoType(tnt);
            return JsonBean.success("2");
        }

    }

    @Override
    public TblVideoType findByName(String typename) {
        List<TblVideoType> list = tblVideoTypeMapper.findBytypename(typename);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Map<String, Object> findAll(String token, String staffId, Integer pageNumber, Integer pageSize) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                if (token == null) {
                    resultMap.put("code", "0");
                    resultMap.put("msg", "用户已失效！");
                    return resultMap;
                }
                TblStaffUtil staff = userProvider.get();
                Integer orgid = staff.getCurrentOrg().getOrgid().intValue();
                PageInfo<TblVideoType> pageInfo = new PageInfo<TblVideoType>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                Page<TblVideoType> page = new Page<TblVideoType>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblVideoType> pageList = tblVideoTypeMapper.selectListByPageInfo(page, orgid);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int) pageList.getTotal());
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public Map<String, Object> findByid(String selectid) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            TblVideoType tblVideoType = tblVideoTypeMapper.findByid(selectid);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", tblVideoType);
            return resultMap;
    }

    @Override
    public void deleteById(String typeId) {
    	tblVideoTypeMapper.deleteByTypeId(typeId);
    }

    @Override
    public Map<String, Object> findAll(String token, String staffId) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil staff = userProvider.get();
                BigDecimal orgid = staff.getCurrentOrg().getOrgid();
                List<TblVideoType> list = tblVideoTypeMapper.selectListByVideoType(orgid);
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("data", list);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
    }

}
