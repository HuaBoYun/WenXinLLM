package com.huabo.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblFlow;
import com.huabo.system.entity.TblSystemModelFlow;
import com.huabo.system.entity.TblSystemModule;
import com.huabo.system.mapper.TblFlowMapper;
import com.huabo.system.mapper.TblSystemModelFlowMapper;
import com.huabo.system.mapper.TblSystemModuleMapper;
import com.huabo.system.service.TblSystemModuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TblSystemModuleServiceImpl implements TblSystemModuleService {

    @Resource
    private TblSystemModuleMapper systemModuleMapper;

    @Resource
    private TblSystemModelFlowMapper tblSystemModelFlowMapper;

    @Resource
    private TblFlowMapper tblFlowMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> selectPageInfoByOrgId(Integer pageNumber, Integer pageSize, String token, String staffId, TblSystemModule module) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            PageInfo<TblSystemModule> pageInfo = new PageInfo<TblSystemModule>();
            pageInfo.setPageSize(pageSize);
            pageInfo.setCurrentPage(pageNumber);
            
            Page<TblSystemModule> page = new Page<TblSystemModule>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblSystemModule> pageList = systemModuleMapper.selectPageInfoByOrgId(page, orgid, module);
            
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            for (TblSystemModule mo : pageInfo.getTlist()) {
                List<TblSystemModelFlow> flow = systemModuleMapper.findFlowByModule(mo.getModelId());
                List<TblFlow> flowList = new ArrayList<>();
                for (TblSystemModelFlow fl : flow) {
                    TblFlow tblFlow = new TblFlow();
                    tblFlow = tblFlowMapper.findByflowId(fl.getFlowId());
                    flowList.add(tblFlow);
                }
                mo.setFlowList(flowList);
            }

            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Integer checkReplay(TblSystemModule module, int type, String token, String staffId) {
            Integer count = 0;
            try {
                TblStaffUtil user = userProvider.get();
                module.setCreateTime(new Date());
                module.setCreatePerson(user.getStaffid());
                module.setModelStatus(0);
                module.setModelOrg(user.getCurrentOrg().getOrgid());
                module.setModifyTime(new Date());
                module.setModifyPerson(user.getStaffid());
                String sql;
                BigDecimal orgid = module.getModelOrg();
                BigDecimal modelId = module.getModelId();
                String modelName = module.getModelName();
                String modelNo = module.getModelNo();
                if (type == 1) {
                    //修改时验证
                    count = systemModuleMapper.selectUpdateCount(orgid, modelId, modelName);
                } else {
                    //新增时候验证
                    count = systemModuleMapper.selectInsertCount(orgid, modelNo, modelName);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return count;
    }

    @Override
    public Map<String, Object> saveEntity(String token, String staffId, TblSystemModule module, BigDecimal[] flowid,
                                          Integer[] flowOrderNo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            module.setCreateTime(new Date());
            module.setCreatePerson(user.getStaffid());
            module.setModelStatus(0);
            module.setModelOrg(user.getCurrentOrg().getOrgid());
            module.setModelId(RandomUtil.uuBigDecimalId());
            systemModuleMapper.insertSystemModule(module);
            TblSystemModelFlow modelFlow = null;
            List<TblSystemModelFlow> list = new ArrayList<TblSystemModelFlow>(0);
            if (flowid != null) {
                for (int i = 0; i < flowid.length; i++) {
                    modelFlow = new TblSystemModelFlow();
                    modelFlow.setFlowId(flowid[i]);
                    modelFlow.setModelNo(flowOrderNo[i]);
                    list.add(modelFlow);
                }
            }
            for (TblSystemModelFlow flow : list) {
                flow.setModelId(module.getModelId());
                tblSystemModelFlowMapper.insertFlow(flow);
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", "0");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public TblSystemModule findAllInfoById(BigDecimal moduleId) {
        return systemModuleMapper.selectBymoduleId(moduleId);
    }

    @Override
    public Map<String, Object> modifyEntity(String token, String staffId, TblSystemModule module, BigDecimal[] flowid, Integer[] flowOrderNo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
            module.setModelOrg(user.getCurrentOrg().getOrgid());
            module.setModifyTime(new Date());
            module.setModifyPerson(user.getStaffid());
            TblSystemModule oldModule = this.systemModuleMapper.selectBymoduleId(module.getModelId());
            this.systemModuleMapper.deleteByModulId(module.getModelId());
            oldModule.setModelName(module.getModelName());
            oldModule.setModifyPerson(module.getModifyPerson());
            oldModule.setModifyTime(module.getModifyTime());
            oldModule.setModelUrl(module.getModelUrl());
            oldModule.setModelType(module.getModelType());
            oldModule.setModelNo(module.getModelNo());
            this.systemModuleMapper.updateByModule(oldModule);
            TblSystemModelFlow modelFlow = null;
            List<TblSystemModelFlow> list = new ArrayList<TblSystemModelFlow>(0);
            if (flowid != null) {
                for (int i = 0; i < flowid.length; ++i) {
                    modelFlow = new TblSystemModelFlow();
                    modelFlow.setFlowId(flowid[i]);
                    //modelFlow.setModelNo(flowOrderNo[i]);
                    list.add(modelFlow);
                }
            }
            for (TblSystemModelFlow flow : list) {
                flow.setModelId(module.getModelId());
                tblSystemModelFlowMapper.insertFlow(flow);
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", "0");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public void modifyFlowModifyStatus(Integer moduleStatus, BigDecimal moduleId) {
            systemModuleMapper.updateByModuleId(moduleStatus, moduleId);
    }

    @Override
    public String findModelTypeByModuleId(BigDecimal moduleId) {
            return systemModuleMapper.selectByModuleId(moduleId);
    }

    @Override
    public Integer selectOrgManageRight(String id, String rightId) {
    	return systemModuleMapper.selectOrgManageRightId(id, rightId);
    }

    @Override
    public void saveOrgRightRelation(String id, String rightId, int type) {
            if (type == 1) {
            	//有一级菜单直接保存关系
                this.systemModuleMapper.insertModule(id, rightId);
            } else {
            	//没有一级菜单，则先生成相应的一级菜单
                this.systemModuleMapper.insertModuleByrightId(id, rightId);
            }
    }

    @Override
    public void dealModuleFlowOrganizationRealtion(BigDecimal moduleId, String orgId) {
            this.systemModuleMapper.deleteByIdAndModuleId(moduleId, orgId);
            this.systemModuleMapper.insertByIdAndModuleId(moduleId, orgId);
    }

    @Override
    public void removeStaffModuleRelation(BigDecimal moduleId, String orgId) {
        this.systemModuleMapper.deleteBymoduleIdAndOrgId(moduleId, orgId);
    }

    @Override
    public void removeModelFlowRelation(String[] flowIds, Integer modelId) {
            for (String s : flowIds) {
                this.systemModuleMapper.removeModelFlowRelation(s, modelId);
            }
    }
}
