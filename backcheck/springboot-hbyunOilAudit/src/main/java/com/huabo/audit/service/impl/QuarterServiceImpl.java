package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.QuarterEntity;
import com.huabo.audit.oracle.entity.RequireSuggestionEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.service.QuarterService;
import com.huabo.audit.oracle.mapper.QuarterMapper;
import com.huabo.audit.util.PageInfoUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Service
public class QuarterServiceImpl extends ServiceImpl<QuarterMapper, QuarterEntity> implements QuarterService  {
	
	@Resource
    private UserProvider userProvider;
	
    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize,  QuarterEntity entity) throws Exception {

        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        Page<RequireSuggestionEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> this.baseMapper.selectByEntity(entity));
        PageInfo<RequireSuggestionEntity> pageInfo = new PageInfoUtil<RequireSuggestionEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }


    @Override
    public JsonBean findById(String id) throws Exception {
        QuarterEntity quarterEntity = this.baseMapper.findById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", quarterEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntity(QuarterEntity quarterEntity) throws Exception {
        this.updateById(quarterEntity);
    }

    @Override
    public void saveEntity(String token, QuarterEntity quarterEntity) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            quarterEntity.setCreateUser(tblStaff);
        }
        this.baseMapper.insertEntity(quarterEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception {
        this.baseMapper.deleteBatchIds(Arrays.stream(ids.split(",")).collect(Collectors.toList()));
    }
}
