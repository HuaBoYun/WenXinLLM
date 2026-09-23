package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.oracle.mapper.TblFlowMapper;
import com.huabo.audit.service.TblCirculationService;
//import com.huabo.system.mysql.mapper.TblCirculationMySqlMapper;
//import com.huabo.system.mysql.mapper.TblControlmatrixMySqlMapper;
//import com.huabo.system.mysql.mapper.TblFlowMySqlMapper;
//import com.huabo.system.mysql.mapper.TblRiskeventMySqlMapper;
//import com.huabo.system.oracle.mapper.TblControlmatrixMapper;
//import com.huabo.system.oracle.mapper.TblRiskeventMapper;

@Service
public class TblCirculationServiceImpl implements TblCirculationService {

    @Resource
    private TblCirculationMapper tblCirculationMapper;


    @Resource
    private TblFlowMapper tblFlowMapper;

    @Resource
    private UserProvider userProvider;
    
    @Override
    public TblCirculation getOneBytaskid(String taskId) {
        List<TblCirculation> list = this.tblCirculationMapper.findByTaskId(taskId);
        return list != null && list.size() > 0 ? (TblCirculation) this.tblCirculationMapper.findByTaskId(taskId).get(0) : null;
    }

//    @Override
//    public TblCirculationMySql getOneByMySqltaskid(String taskId) {
//        List<TblCirculationMySql> list = this.tblCirculationMySqlMapper.findByTaskId(taskId);
//        return list != null && list.size() > 0 ? (TblCirculationMySql) this.tblCirculationMySqlMapper.findByTaskId(taskId).get(0) : null;
//    }

    @Override
    public TblCirculation get(String cyid) {
        return this.tblCirculationMapper.findById(cyid);
    }

//    @Override
//    public TblCirculationMySql getMySql(String cyid) {
//        return this.tblCirculationMySqlMapper.findById(cyid);
//    }

    @Override
    public void upateTblCirculation(TblCirculation tblCirculation) {
        this.tblCirculationMapper.updateCirculationInfoById(tblCirculation);
    }

//    @Override
//    public void upateMySqlTblCirculation(TblCirculationMySql tblCirculation) {
//        this.tblCirculationMySqlMapper.updateCirculationInfoById(tblCirculation);
//    }

    @Override
    public TblCirculation saveTblCirculationnew(String cytype, String cycode, String cyname, String cyurl, BigDecimal cystaffid, String buskey, String definitionId, String taskid) {
        TblCirculation c = new TblCirculation();
        c.setCytype(cytype);
        c.setCycode(cycode);
        c.setCyname(cyname);
        c.setCydate(new Date());
        c.setCystate("审批中");
        c.setCyurl(cyurl);
        c.setCyStaffid(cystaffid.toString());
        c.setBusinesskey(buskey);
        c.setDefinitionid(definitionId);
        c.setTaskid(taskid);
        tblCirculationMapper.saveTblCirculation(c);
        return c;
    }

//    @Override
//    public TblCirculationMySql saveMySqlTblCirculationnew(String type, String number, String name, String url, BigDecimal loginUser, String buskey, String definitionId, String taskid) {
//        TblCirculationMySql c = new TblCirculationMySql();
//        c.setCytype(type);
//        c.setCycode(number);
//        c.setCyname(name);
//        c.setCydate(new Date());
//        c.setCystate("审批中");
//        c.setCyurl(url);
//        c.setCystaffid(loginUser.toString());
//        c.setBusinesskey(buskey);
//        c.setDefinitionid(definitionId);
//        c.setTaskid(taskid);
//        tblCirculationMySqlMapper.saveTblCirculation(c);
//        return c;
//    }

//    @Override
//    public Map<String, Object> findByLendid(String lendid, String token) {
//        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
//            Map<String, Object> resultMap = new HashMap<String, Object>(0);
//            Map<String, Object> dataMap = new HashMap<String, Object>(0);
//            TblStaffUtil user = null;
//            try {
//                user = userProvider.get();
//                TblContractLend lend = tblContractLendMapper.findById(lendid);
//                TblCirculation cy = tblCirculationMapper.getOneBytaskid(lendid);
//                List<TblMyTask> list = null;
//                if (user.getTrole() != null && user.getTrole().getRname() != null) {
//                    list = HttpClient.findByTask(user.getTrole().getRname(), user.getStaffid().toString(), 1, 50000);
//                } else {
//                    list = HttpClient.findByTask("", user.getStaffid().toString(), 1, 50000);
//                }
//                if (list != null && list.size() > 0) {
//                    for (TblMyTask myTask : list) {
//                        if (cy != null && myTask.getProcessInstanceId().equals(cy.getBusinesskey())) {
//                            Map<String, Object> map = HttpClient.lczxProcessJson(myTask.getTaskId());
//                            String object = (String) map.get("data");
//                            String[] results = object.replace("\"", "").split(",");
//                            if (results.length > 0 && !results[0].equals("")) {
//                                List<String> stringB = Arrays.asList(results);
//                                dataMap.put("results", stringB);
//                                dataMap.put("number", stringB.size());
//                            } else {
//                                dataMap.put("results", null);
//                                dataMap.put("number", 0);
//                            }
//                            dataMap.put("task", myTask);
//                            break;
//                        }
//                    }
//                }
//
//                dataMap.put("cy", cy);
//                dataMap.put("tblContractLead", lend);//合同借阅信息
//                if (user.getTrole() != null && user.getTrole().getRname() != null) {
//                    dataMap.put("rolename", user.getTrole().getRname() != null);
//                }
//                resultMap.put("code", "1");
//                resultMap.put("msg", "成功");
//                resultMap.put("data", dataMap);
//                return resultMap;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return resultMap;
//        } else {
//            Map<String, Object> resultMap = new HashMap<String, Object>(0);
//            Map<String, Object> dataMap = new HashMap<String, Object>(0);
//            TblStaffUtil user = null;
//            try {
//                user = userProvider.get();
//                TblContractLendMySql lend = tblContractLendMySqlMapper.findById(lendid);
//                TblCirculationMySql cy = tblCirculationMySqlMapper.getOneBytaskid(lendid);
//                List<TblMyTaskMySql> list = null;
//                if (user.getTrole() != null && user.getTrole().getRname() != null) {
//                    list = HttpClient.findByMySqlTask(user.getTrole().getRname(), user.getStaffid().toString(), 1, 1500);
//                } else {
//                    list = HttpClient.findByMySqlTask("", user.getStaffid().toString(), 1, 1500);
//                }
//                if (list != null && list.size() > 0) {
//                    for (TblMyTaskMySql myTask : list) {
//                        if (cy != null && myTask.getProcessInstanceId().equals(cy.getBusinesskey())) {
//                            Map<String, Object> map = HttpClient.lczxProcessJson(myTask.getTaskId());
//                            String object = (String) map.get("data");
//                            String[] results = object.replace("\"", "").split(",");
//                            if (results.length > 0 && !results[0].equals("")) {
//                                List<String> stringB = Arrays.asList(results);
//                                dataMap.put("results", stringB);
//                                dataMap.put("number", stringB.size());
//                            } else {
//                                dataMap.put("results", null);
//                                dataMap.put("number", 0);
//                            }
//                            dataMap.put("task", myTask);
//                            break;
//                        }
//                    }
//                }
//
//                dataMap.put("cy", cy);
//                dataMap.put("tblContractLead", lend);//合同借阅信息
//                if (user.getTrole() != null && user.getTrole().getRname() != null) {
//                    dataMap.put("rolename", user.getTrole().getRname() != null);
//                }
//                resultMap.put("code", "1");
//                resultMap.put("msg", "成功");
//                resultMap.put("data", dataMap);
//                return resultMap;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return resultMap;
//        }
//
//    }
}
