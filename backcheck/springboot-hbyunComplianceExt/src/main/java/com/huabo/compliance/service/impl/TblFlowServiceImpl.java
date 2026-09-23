package com.huabo.compliance.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.config.DateBaseConfig;
import com.huabo.compliance.entity.TblControlmatrix;
import com.huabo.compliance.entity.TblFlow;
import com.huabo.compliance.entity.TblFlowBussiness;
import com.huabo.compliance.entity.TblFlowMatrix;
import com.huabo.compliance.entity.TblInnerrule;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.entity.TblOuterrule;
import com.huabo.compliance.entity.TblRisk;
import com.huabo.compliance.entity.TblRiskFlow;
import com.huabo.compliance.mapper.TblControlmatrixMapper;
import com.huabo.compliance.mapper.TblFlowBussinessMapper;
import com.huabo.compliance.mapper.TblFlowMapper;
import com.huabo.compliance.mapper.TblInnerruleMapper;
import com.huabo.compliance.mapper.TblOrganizationMapper;
import com.huabo.compliance.mapper.TblOuterruleMapper;
import com.huabo.compliance.mapper.TblRiskMapper;
import com.huabo.compliance.mysql.entity.TblControlmatrixMySql;
import com.huabo.compliance.mysql.entity.TblFlowBussinessMySql;
import com.huabo.compliance.mysql.entity.TblFlowMatrixMySql;
import com.huabo.compliance.mysql.entity.TblFlowMySql;
import com.huabo.compliance.mysql.entity.TblInnerruleMySql;
import com.huabo.compliance.mysql.entity.TblOrganizationMySql;
import com.huabo.compliance.mysql.entity.TblOuterruleMySql;
import com.huabo.compliance.mysql.entity.TblRiskFlowMySql;
import com.huabo.compliance.mysql.entity.TblRiskMySql;
import com.huabo.compliance.mysql.mapper.TblControlmatrixMySqlMapper;
import com.huabo.compliance.mysql.mapper.TblFlowBussinessMySqlMapper;
import com.huabo.compliance.mysql.mapper.TblFlowMySqlMapper;
import com.huabo.compliance.mysql.mapper.TblInnerruleMySqlMapper;
import com.huabo.compliance.mysql.mapper.TblOrganizationMySqlMapper;
import com.huabo.compliance.mysql.mapper.TblOuterruleMySqlMapper;
import com.huabo.compliance.mysql.mapper.TblRiskMySqlMapper;
import com.huabo.compliance.service.TblFlowService;
import com.huabo.compliance.vo.CopyVo;
import com.huabo.compliance.vo.TblFlowVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TblFlowServiceImpl implements TblFlowService {

    @Resource
    private TblFlowMapper tblFlowMapper;

    @Resource
    private TblFlowBussinessMapper bussinessMapper;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Resource
    private TblRiskMapper tblRiskMapper;

    @Resource
    private TblControlmatrixMapper tblControlmatrixMapper;

    @Resource
    private TblInnerruleMapper tblInnerruleMapper;

    @Resource
    private TblOuterruleMapper tblOuterruleMapper;

    @Resource
    private TblFlowMySqlMapper tblFlowMySqlMapper;

    @Resource
    private TblFlowBussinessMySqlMapper bussinessMySqlMapper;

    @Resource
    private TblOrganizationMySqlMapper tblOrganizationMySqlMapper;

    @Resource
    private TblRiskMySqlMapper tblRiskMySqlMapper;

    @Resource
    private TblControlmatrixMySqlMapper tblControlmatrixMySqlMapper;

    @Resource
    private TblInnerruleMySqlMapper tblInnerruleMySqlMapper;

    @Resource
    private TblOuterruleMySqlMapper tblOuterruleMySqlMapper;
    
    @Resource
    private UserProvider userProvider;
    

    @Override
    public TblFlow findByIdFlows(BigDecimal flowid) {
        return tblFlowMapper.findById(flowid);
    }

    @Override
    public TblFlowMySql findByMySqlIdFlows(BigDecimal flowid) {
        return tblFlowMySqlMapper.findById(flowid);
    }


    @Override
    public TblFlow findById(String currentpid) {
        return tblFlowMapper.findById(new BigDecimal(currentpid));
    }

    @Override
    public TblFlowMySql findByMySqlId(String currentpid) {
        return tblFlowMySqlMapper.findById(new BigDecimal(currentpid));
    }

    @Override
    public List findByis(String flowcode, String orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            return tblFlowMapper.selectByTrim(flowcode, orgid);
        } else {
            return tblFlowMySqlMapper.selectByTrim(flowcode, orgid);
        }
    }

    @Override
    public void modify(TblFlow dp) {
        tblFlowMapper.insert(dp);
    }

    @Override
    public void modifyMySql(TblFlowMySql MySqldp) {
        tblFlowMySqlMapper.insert(MySqldp);
    }

    @Override
    public List findBySql(String sql) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            return tblFlowMapper.selectByS(sql);
        } else {
            return tblFlowMySqlMapper.selectByS(sql);
        }
    }

    @Override
    public String findControlMatrixIdUniqueByFlowid(BigDecimal flowid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List<TblFlowBussiness> list = this.bussinessMapper.findBysql(flowid);
            return list != null && list.size() != 0 ? String.valueOf((TblFlowBussiness) list.get(0)) : null;
        } else {
            List<TblFlowBussinessMySql> list = this.bussinessMySqlMapper.findBysql(flowid);
            return list != null && list.size() != 0 ? String.valueOf((TblFlowBussinessMySql) list.get(0)) : null;
        }
    }

    @Override
    public TblFlowBussiness findUniqueByFlowId(BigDecimal flowid) {
        return this.bussinessMapper.findBy(flowid);
    }

    @Override
    public TblFlowBussinessMySql findUniqueByMySqlFlowId(BigDecimal flowid) {
        return this.bussinessMySqlMapper.findBy(flowid);
    }


    @Override
    public Map<String, Object> flowtree(HttpServletRequest request, String token, String staffId, String treeName, String orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                request.getSession().setAttribute("currentProcess", "0");
                TblStaffUtil user = userProvider.get();
                if (StringUtils.isEmpty(orgid)) {
                    orgid = user.getCurrentOrg().getOrgid().toString();
                    treeName = user.getCurrentOrg().getOrgname();
                    request.getSession().setAttribute("ogridb", orgid);
                }
                String tree = "";
                TblOrganization or = tblOrganizationMapper.selectByOrgId(new BigDecimal(orgid));
                String sql = "";
                List<TblFlow> list = new ArrayList<>();
                if (or.getOrgtype() != null && or.getOrgtype() == 0) {
                    list = tblFlowMapper.findByFlow(orgid, user.getCurrentOrg().getOrgid().toString());
                } else {
                    list = tblFlowMapper.findByFlows(orgid);
                }
                for (TblFlow flow : list) {
                    if (flow.getFatherflowid().toEngineeringString().equals("0")
                            || flow.getFlowid().toEngineeringString().equals("0")) {
                        tree += "tree.nodes["
                                + flow.getFatherflowid().toEngineeringString() + "_"
                                + flow.getFlowid().toString() + "]=text:"
                                + flow.getFlowname() + ";method:check("
                                + flow.getFlowid().toString() + ","
                                + flow.getFatherflowid().toString() + ");";
                    } else {
                        TblFlow fatherFlow = this.tblFlowMapper.findByIdFlow(flow.getFatherflowid());
                        if (fatherFlow != null && fatherFlow.getFatherflowid() != null
                                && fatherFlow.getFatherflowid().toEngineeringString()
                                .equals("0")) {
                            tree += "tree.nodes["
                                    + flow.getFatherflowid().toEngineeringString()
                                    + "_" + flow.getFlowid().toString() + "]=text:"
                                    + flow.getFlowname() + ";method:check("
                                    + flow.getFlowid().toString() + ","
                                    + flow.getFatherflowid().toString() + ");";
                            if (or.getOrgtype() != null && or.getOrgtype() == 0) {
                                tree += "tree.nodes[" +
                                        fatherFlow.getFatherflowid().toEngineeringString() + "_" +
                                        fatherFlow.getFlowid().toString() + "]=text:" +
                                        fatherFlow.getFlowname() +
                                        ";method:check(" + fatherFlow.getFlowid().toString() + "," +
                                        fatherFlow.getFlowid().toString() + ");url:listanalysis?pid=" +
                                        fatherFlow.getFlowid().toString() + ";";
                            }
                        }
                    }
                }
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("treeName", treeName);
                dataMap.put("tree", tree);
                dataMap.put("targetFrame", "mainFramex");
                dataMap.put("orgid", orgid);
                dataMap.put("userflow", user);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                request.getSession().setAttribute("currentProcess", "0");
                TblStaffUtil user = userProvider.get();
                if (StringUtils.isEmpty(orgid)) {
                    orgid = user.getCurrentOrg().getOrgid().toString();
                    treeName = user.getCurrentOrg().getOrgname();
                    request.getSession().setAttribute("ogridb", orgid);
                }
                String tree = "";
                TblOrganizationMySql or = tblOrganizationMySqlMapper.selectByOrgId(new BigDecimal(orgid));
                String sql = "";
                List<TblFlowMySql> list = new ArrayList<>();
                if (or.getOrgtype() != null && or.getOrgtype() == 0) {
                    list = tblFlowMySqlMapper.findByFlow(orgid, user.getCurrentOrg().getOrgid().toString());
                } else {
                    list = tblFlowMySqlMapper.findByFlows(orgid);
                }
                for (TblFlowMySql flow : list) {
                    if (flow.getFatherflowid().toEngineeringString().equals("0")
                            || flow.getFlowid().toEngineeringString().equals("0")) {
                        tree += "tree.nodes["
                                + flow.getFatherflowid().toEngineeringString() + "_"
                                + flow.getFlowid().toString() + "]=text:"
                                + flow.getFlowname() + ";method:check("
                                + flow.getFlowid().toString() + ","
                                + flow.getFatherflowid().toString() + ");";
                    } else {
                        TblFlowMySql fatherFlow = this.tblFlowMySqlMapper.findByIdFlow(flow.getFatherflowid());
                        if (fatherFlow != null && fatherFlow.getFatherflowid() != null
                                && fatherFlow.getFatherflowid().toEngineeringString()
                                .equals("0")) {
                            tree += "tree.nodes["
                                    + flow.getFatherflowid().toEngineeringString()
                                    + "_" + flow.getFlowid().toString() + "]=text:"
                                    + flow.getFlowname() + ";method:check("
                                    + flow.getFlowid().toString() + ","
                                    + flow.getFatherflowid().toString() + ");";
                            if (or.getOrgtype() != null && or.getOrgtype() == 0) {
                                tree += "tree.nodes[" +
                                        fatherFlow.getFatherflowid().toEngineeringString() + "_" +
                                        fatherFlow.getFlowid().toString() + "]=text:" +
                                        fatherFlow.getFlowname() +
                                        ";method:check(" + fatherFlow.getFlowid().toString() + "," +
                                        fatherFlow.getFlowid().toString() + ");url:listanalysis?pid=" +
                                        fatherFlow.getFlowid().toString() + ";";
                            }
                        }
                    }
                }
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("treeName", treeName);
                dataMap.put("tree", tree);
                dataMap.put("targetFrame", "mainFramex");
                dataMap.put("orgid", orgid);
                dataMap.put("userflow", user);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> flowtreevser(HttpServletRequest request, String token, String staffId, String treeName, String orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                if (StringUtils.isEmpty(orgid)) {
                    orgid = user.getCurrentOrg().getOrgid().toString();
                    treeName = user.getCurrentOrg().getOrgname();
                } else {
                    request.getSession().setAttribute("orgid", orgid);
                }
                String tree = "";
                TblOrganization or = tblOrganizationMapper.selectByOrgId(new BigDecimal(orgid));
                String sql = "";
                List<TblFlow> list = new ArrayList<>();
                if (or.getOrgtype() != null && or.getOrgtype() == 0) {
                    list = tblFlowMapper.findBysqlFlow(orgid);
                } else {
                    list = tblFlowMapper.findBysqlFlowByType(orgid);
                }
                for (TblFlow flow : list) {
                    // TblFlow flow = (TblFlow)o;
                    if (flow.getFatherflowid().toEngineeringString().equals("0")
                            || flow.getFlowid().toEngineeringString().equals("0")) {
                        tree += "tree.nodes[\'"
                                + flow.getFatherflowid().toEngineeringString() + "_"
                                + flow.getFlowid().toString() + "]=text:"
                                + flow.getFlowname() + ";method:check("
                                + flow.getFlowid().toString() + ","
                                + flow.getFatherflowid().toString() + ");";
                    } else {
                        TblFlow fatherFlow = this.tblFlowMapper.findByIdFlow(flow.getFatherflowid());
                        if (fatherFlow != null && fatherFlow.getFatherflowid() != null
                                && fatherFlow.getFatherflowid().toEngineeringString()
                                .equals("0")) {
                            tree += "tree.nodes[\'"
                                    + flow.getFatherflowid().toEngineeringString()
                                    + "_" + flow.getFlowid().toString() + "]=text:"
                                    + flow.getFlowname() + ";method:check("
                                    + flow.getFlowid().toString() + ","
                                    + flow.getFatherflowid().toString() + ");";
                            if (or.getOrgtype() != null && or.getOrgtype() == 0) {
                                tree += "tree.nodes[\'" +
                                        fatherFlow.getFatherflowid().toEngineeringString() + "_" +
                                        fatherFlow.getFlowid().toString() + "]=text:" +
                                        fatherFlow.getFlowname() +
                                        ";method:check(" + fatherFlow.getFlowid().toString() + "," +
                                        fatherFlow.getFlowid().toString() + ");url:/nbkz/ywlc/list_version?pid=" +
                                        fatherFlow.getFlowid().toString() + ";";
                            }
                        }
                    }
                }
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("treeName", treeName);
                dataMap.put("tree", tree);
                dataMap.put("targetFrame", "mainFramex");
                dataMap.put("orgid", orgid);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                if (StringUtils.isEmpty(orgid)) {
                    orgid = user.getCurrentOrg().getOrgid().toString();
                    treeName = user.getCurrentOrg().getOrgname();
                } else {
                    request.getSession().setAttribute("orgid", orgid);
                }
                String tree = "";
                TblOrganization or = tblOrganizationMapper.selectByOrgId(new BigDecimal(orgid));
                String sql = "";
                List<TblFlowMySql> list = new ArrayList<>();
                if (or.getOrgtype() != null && or.getOrgtype() == 0) {
                    list = tblFlowMySqlMapper.findBysqlFlow(orgid);
                } else {
                    list = tblFlowMySqlMapper.findBysqlFlowByType(orgid);
                }
                for (TblFlowMySql flow : list) {
                    // TblFlow flow = (TblFlow)o;
                    if (flow.getFatherflowid().toEngineeringString().equals("0")
                            || flow.getFlowid().toEngineeringString().equals("0")) {
                        tree += "tree.nodes[\'"
                                + flow.getFatherflowid().toEngineeringString() + "_"
                                + flow.getFlowid().toString() + "]=text:"
                                + flow.getFlowname() + ";method:check("
                                + flow.getFlowid().toString() + ","
                                + flow.getFatherflowid().toString() + ");";
                    } else {
                        TblFlowMySql fatherFlow = this.tblFlowMySqlMapper.findByIdFlow(flow.getFatherflowid());
                        if (fatherFlow != null && fatherFlow.getFatherflowid() != null
                                && fatherFlow.getFatherflowid().toEngineeringString()
                                .equals("0")) {
                            tree += "tree.nodes[\'"
                                    + flow.getFatherflowid().toEngineeringString()
                                    + "_" + flow.getFlowid().toString() + "]=text:"
                                    + flow.getFlowname() + ";method:check("
                                    + flow.getFlowid().toString() + ","
                                    + flow.getFatherflowid().toString() + ");";
                            if (or.getOrgtype() != null && or.getOrgtype() == 0) {
                                tree += "tree.nodes[\'" +
                                        fatherFlow.getFatherflowid().toEngineeringString() + "_" +
                                        fatherFlow.getFlowid().toString() + "]=text:" +
                                        fatherFlow.getFlowname() +
                                        ";method:check(" + fatherFlow.getFlowid().toString() + "," +
                                        fatherFlow.getFlowid().toString() + ");url:/nbkz/ywlc/list_version?pid=" +
                                        fatherFlow.getFlowid().toString() + ";";
                            }
                        }
                    }
                }
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("treeName", treeName);
                dataMap.put("tree", tree);
                dataMap.put("targetFrame", "mainFramex");
                dataMap.put("orgid", orgid);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        }
    }


    @Override
    public Map<String, Object> findBysqAll(String token, String staffId, String faflowid, Integer pageNumber, Integer pageSize, String name, String code) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal orgid = user.getCurrentOrg().getOrgid();
                PageInfo<TblFlow> pageInfo = new PageInfo<TblFlow>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setTlist(tblFlowMapper.selectTblFlowList(pageInfo, orgid.toString(), faflowid, name, code));
                pageInfo.setTotalRecord(tblFlowMapper.selectTblFlowCount(pageInfo, orgid.toString(), faflowid, name, code));
//        if (pageInfo.getTlist() != null) {
//            List<TblFlow> flows = new ArrayList<TblFlow>();
//            for (Object ob : pageInfo.getTlist()) {
//                Object[] obj = (Object[]) ob;
//                TblFlow flow = new TblFlow();
//                flow.setFlowid(obj[0] != null ? new BigDecimal(obj[0].toString()) : null);
//                flow.setFlowname(obj[1] != null ? obj[1].toString() : "");
//                flow.setFlownumber(obj[2] != null ? obj[2].toString() : "");
//                flow.setComName(obj[3] != null ? obj[3].toString() : "");
//                flow.setDeparChargeName(obj[4] != null ? obj[4].toString() : "");
//                flow.setCreatetime(obj[5] != null ? obj[5].toString() : "");
//                flows.add(flow);
//            }
//            pageInfo.setTlist(flows);
//
//        }
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal orgid = user.getCurrentOrg().getOrgid();
                PageInfo<TblFlowMySql> pageInfo = new PageInfo<TblFlowMySql>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setTlist(tblFlowMySqlMapper.selectTblFlowList(pageInfo, orgid.toString(), faflowid, name, code));
                pageInfo.setTotalRecord(tblFlowMySqlMapper.selectTblFlowCount(pageInfo, orgid.toString(), faflowid, name, code));
//        if (pageInfo.getTlist() != null) {
//            List<TblFlow> flows = new ArrayList<TblFlow>();
//            for (Object ob : pageInfo.getTlist()) {
//                Object[] obj = (Object[]) ob;
//                TblFlow flow = new TblFlow();
//                flow.setFlowid(obj[0] != null ? new BigDecimal(obj[0].toString()) : null);
//                flow.setFlowname(obj[1] != null ? obj[1].toString() : "");
//                flow.setFlownumber(obj[2] != null ? obj[2].toString() : "");
//                flow.setComName(obj[3] != null ? obj[3].toString() : "");
//                flow.setDeparChargeName(obj[4] != null ? obj[4].toString() : "");
//                flow.setCreatetime(obj[5] != null ? obj[5].toString() : "");
//                flows.add(flow);
//            }
//            pageInfo.setTlist(flows);
//
//        }
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> findBysqAllversion(String token, String staffId, String flowid, Integer pageNumber, Integer pageSize) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal orgid = user.getCurrentOrg().getOrgid();
                PageInfo<TblFlow> pageInfo = new PageInfo<TblFlow>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setTlist(tblFlowMapper.findBysqAllversion(pageInfo, orgid.toString(), flowid));
                pageInfo.setTotalRecord(tblFlowMapper.findBysqAllversionCount(pageInfo, orgid.toString(), flowid));
//        if (pageInfo.getTlist() != null && pageInfo.getTotalRecord() > 0) {
//            List<TblFlow> flows = new ArrayList<TblFlow>();
//            for (Object o : pageInfo.getTlist()) {
//                TblFlow flow = new TblFlow();
//                Object[] obj = (Object[]) o;
//                flow.setFlowid(obj[0] != null ? new BigDecimal(obj[0].toString()) : null);
//                flow.setFlowname(obj[1] != null ? obj[1].toString() : "");
//                flow.setFlownumber(obj[2] != null ? obj[2].toString() : "");
//                flow.setComName(obj[3] != null ? obj[3].toString() : "");
//                flow.setDeparChargeName(obj[4] != null ? obj[4].toString() : "");
//                flow.setCreatetime(obj[5] != null ? obj[5].toString() : "");
//                flow.setLastmodifiedtime(obj[6] != null ? obj[6].toString() : "");
//                flow.setVersion(obj[7] != null ? Double.parseDouble(obj[7].toString()) : null);
//                flows.add(flow);
//            }
//            pageInfo.setTlist(flows);
//        }
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                if (flowid != null && flowid.length() > 0) {
                    TblFlow flow = this.tblFlowMapper.findById(new BigDecimal(flowid));
                    dataMap.put("flow", flow);
                }
                dataMap.put("pageInfo", pageInfo);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal orgid = user.getCurrentOrg().getOrgid();
                PageInfo<TblFlowMySql> pageInfo = new PageInfo<TblFlowMySql>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setTlist(tblFlowMySqlMapper.findBysqAllversion(pageInfo, orgid.toString(), flowid));
                pageInfo.setTotalRecord(tblFlowMySqlMapper.findBysqAllversionCount(pageInfo, orgid.toString(), flowid));
//        if (pageInfo.getTlist() != null && pageInfo.getTotalRecord() > 0) {
//            List<TblFlow> flows = new ArrayList<TblFlow>();
//            for (Object o : pageInfo.getTlist()) {
//                TblFlow flow = new TblFlow();
//                Object[] obj = (Object[]) o;
//                flow.setFlowid(obj[0] != null ? new BigDecimal(obj[0].toString()) : null);
//                flow.setFlowname(obj[1] != null ? obj[1].toString() : "");
//                flow.setFlownumber(obj[2] != null ? obj[2].toString() : "");
//                flow.setComName(obj[3] != null ? obj[3].toString() : "");
//                flow.setDeparChargeName(obj[4] != null ? obj[4].toString() : "");
//                flow.setCreatetime(obj[5] != null ? obj[5].toString() : "");
//                flow.setLastmodifiedtime(obj[6] != null ? obj[6].toString() : "");
//                flow.setVersion(obj[7] != null ? Double.parseDouble(obj[7].toString()) : null);
//                flows.add(flow);
//            }
//            pageInfo.setTlist(flows);
//        }
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                if (flowid != null && flowid.length() > 0) {
                    TblFlowMySql flowMySql = this.tblFlowMySqlMapper.findById(new BigDecimal(flowid));
                    dataMap.put("flow", flowMySql);
                }
                dataMap.put("pageInfo", pageInfo);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public Integer findTblFlowStartUpFiringStatus(Integer flowid, Integer firing) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            TblFlow flow = this.findById(flowid.toString());
            Integer count = this.tblFlowMapper.findOtherFiringStatusByParentId(flow.getFatherflowid(), flow.getFlowid());
            return count;
        } else {
            TblFlow flow = this.findById(flowid.toString());
            Integer count = this.tblFlowMySqlMapper.findOtherFiringStatusByParentId(flow.getFatherflowid(), flow.getFlowid());
            return count;
        }
    }

    @Override
    public Integer modifyTblFlowFiringStatus(Integer flowid, Integer firing) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            this.tblFlowMapper.excuteSql(flowid, firing);
            return 0;
        } else {
            this.tblFlowMySqlMapper.excuteSql(flowid, firing);
            return 0;
        }
    }

    @Override
    public TblRiskFlow findBysql(String flowid) {
        List<TblRiskFlow> list = this.tblFlowMapper.findBysql(flowid);
        return list != null && list.size() > 0 ? (TblRiskFlow) list.get(0) : null;
    }

    @Override
    public TblRiskFlowMySql findByMySql(String flowid) {
        List<TblRiskFlowMySql> list = this.tblFlowMySqlMapper.findBysql(flowid);
        return list != null && list.size() > 0 ? (TblRiskFlowMySql) list.get(0) : null;
    }

    @Override
    public void deleteTblRiskFlow(String flowid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            tblFlowMapper.deleteTblRiskFlow(flowid);
        } else {
            tblFlowMySqlMapper.deleteTblRiskFlow(flowid);
        }
    }

    @Override
    public List<TblFlow> findByFlownumber(String flownumber) {
        return tblFlowMapper.selectFlownumber(flownumber);
    }

    @Override
    public List<TblFlowMySql> findByMySqlFlownumber(String flownumber) {
        return tblFlowMySqlMapper.selectFlownumber(flownumber);
    }

    @Override
    public void deleteBy(TblFlow flowid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            tblFlowMapper.deleteById(flowid.getFlowid());
        } else {
            tblFlowMySqlMapper.deleteById(flowid);
        }
    }

    @Override
    public void deleteByMySql(TblFlowMySql flowid) {

    }

    @Override
    public List<TblFlow> findByfaflowid(String flowid) {
        return this.tblFlowMapper.findByFlowid(flowid);
    }

    @Override
    public List<TblFlowMySql> findByMySqlfaflowid(String flowid) {
        return this.tblFlowMySqlMapper.findByFlowid(flowid);
    }

    @Override
    public void add(TblFlow flow) {
        this.tblFlowMapper.insert(flow);
    }

    @Override
    public void addMySql(TblFlowMySql MySqlflow) {
        this.tblFlowMySqlMapper.insert(MySqlflow);
    }

    @Override
    public void excuteSql(TblFlow tblFlows) {
        tblFlowMapper.insert(tblFlows);
    }

    @Override
    public void excuteSqlMySql(TblFlowMySql tblFlowMySql) {
        tblFlowMySqlMapper.insert(tblFlowMySql);
    }

    @Override
    public Map<String, Object> listBySqlPage(String token, String staffId, String faflowid, String flowname, String flownumber, String stutes, String desc, Integer pageNumber, Integer pageSize, String belongsto, Integer firingStatus, String view) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal orgid = user.getCurrentOrg().getOrgid();
                if (faflowid == null) {
                    faflowid = "0";
                }
                TblFlow falow = tblFlowMapper.findByFaflowid(faflowid);
                BigDecimal fatherFlowid = falow.getFatherflowid();
                PageInfo<TblFlow> pageInfo = new PageInfo<TblFlow>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setTlist(tblFlowMapper.findListByPageInfo(pageInfo, orgid, faflowid, flowname, flownumber, stutes, desc, belongsto, fatherFlowid, firingStatus));
                pageInfo.setTotalRecord(tblFlowMapper.findCountByPageInfo(orgid, faflowid, flowname, flownumber, stutes, desc, belongsto, fatherFlowid, firingStatus));
                List list = new ArrayList();
//        for (Object o : pageInfo.getTlist()) {
//            Object[] oa = (Object[]) o;
//            TblFlow flow2 = new TblFlow();
//            flow2.setFlowid(new BigDecimal(oa[0].toString()));
//            flow2.setFlownumber((String) oa[1]);
//            flow2.setFlowname((String) oa[2]);
//            if (oa[3] != null && !oa[3].equals("")) {
//                TblOrganization organ = tblOrganizationMapper.selectByOrgId(new BigDecimal((String) oa[3]));
//                if (organ != null) {
//                    flow2.setCompany(organ != null ? organ.getOrgname() : "");
//                    flow2.setComName(organ.getOrgname());
//                }
//            } else {
//                flow2.setCompany("");
//            }
//            if (oa[4] != null && !oa[4].equals("")) {
//                TblOrganization organ = tblOrganizationMapper.selectByOrgId(new BigDecimal((String) oa[4]));
//                if (organ != null) {
//                    flow2.setDepartincharge(organ != null ? organ.getOrgname() : "");
//                    flow2.setDeparChargeName(organ.getOrgname());
//                }
//            } else {
//                flow2.setDepartincharge("");
//            }
//            flow2.setCreatetime((String) oa[5]);
//            flow2.setFlowstatus((String) oa[6]);
//            flow2.setFatherflowid(new BigDecimal(oa[7] == null ? "0" : oa[7].toString()));
//            flow2.setDepartassist(oa[8] == null ? null : oa[8].toString());
//            flow2.setStatus(oa[9] == null ? null : Integer.parseInt(oa[9].toString()));
//            flow2.setFiringStatus(oa[12] == null ? null : Integer.parseInt(oa[12].toString()));
//            list.add(flow2);
//        }
//        pageInfo.setTlist(list);
                for (TblFlow o : pageInfo.getTlist()) {
                    TblFlow flow2 = new TblFlow();
                    flow2.setFlowid(o.getFlowid());
                    flow2.setFlownumber(o.getFlownumber());
                    flow2.setFlowname(o.getFlowname());
                    if (o.getCompany() != null && !o.getCompany().equals("")) {
                        TblOrganization organ = tblOrganizationMapper.selectByOrgId(new BigDecimal(o.getCompany()));
                        if (organ != null) {
                            flow2.setCompany(organ != null ? organ.getOrgname() : "");
                            flow2.setComName(organ.getOrgname());
                        }
                    } else {
                        flow2.setCompany("");
                    }
                    if (o.getDepartincharge() != null && !o.getDepartincharge().equals("")) {
                        TblOrganization organ = tblOrganizationMapper.selectByOrgId(new BigDecimal(o.getDepartincharge()));
                        if (organ != null) {
                            flow2.setDepartincharge(organ != null ? organ.getOrgid().toString() : "");
                            flow2.setDeparChargeName(organ.getOrgname());
                        }
                    } else {
                        flow2.setDepartincharge("");
                    }
                    flow2.setCreatetime(o.getCreatetime());
                    flow2.setFlowstatus(o.getFlowstatus());
                    flow2.setFatherflowid(new BigDecimal(o.getFatherflowid() == null ? "0" : o.getFatherflowid().toString()));
                    flow2.setDepartassist(o.getDepartassist() == null ? null : o.getDepartassist().toString());
                    flow2.setStatus(o.getStatus() == null ? null : Integer.parseInt(o.getStatus().toString()));
                    flow2.setFiringStatus(o.getFiringStatus() == null ? null : Integer.parseInt(o.getFiringStatus().toString()));
                    flow2.setSettingid(o.getSettingid() == null ? "" : o.getSettingid().toString());
                    list.add(flow2);
                }
                pageInfo.setTlist(list);

                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("pageInfo", pageInfo);
                dataMap.put("falow", falow);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal orgid = user.getCurrentOrg().getOrgid();
                if (faflowid == null) {
                    faflowid = "0";
                }
                TblFlowMySql falow = tblFlowMySqlMapper.findByFaflowid(faflowid);
                BigDecimal fatherFlowid = falow.getFatherflowid();
                PageInfo<TblFlowMySql> pageInfo = new PageInfo<TblFlowMySql>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setTlist(tblFlowMySqlMapper.findListByPageInfo(pageInfo, orgid, faflowid, flowname, flownumber, stutes, desc, belongsto, fatherFlowid, firingStatus));
                pageInfo.setTotalRecord(tblFlowMySqlMapper.findCountByPageInfo(orgid, faflowid, flowname, flownumber, stutes, desc, belongsto, fatherFlowid, firingStatus));
                List list = new ArrayList();
//        for (Object o : pageInfo.getTlist()) {
//            Object[] oa = (Object[]) o;
//            TblFlow flow2 = new TblFlow();
//            flow2.setFlowid(new BigDecimal(oa[0].toString()));
//            flow2.setFlownumber((String) oa[1]);
//            flow2.setFlowname((String) oa[2]);
//            if (oa[3] != null && !oa[3].equals("")) {
//                TblOrganization organ = tblOrganizationMapper.selectByOrgId(new BigDecimal((String) oa[3]));
//                if (organ != null) {
//                    flow2.setCompany(organ != null ? organ.getOrgname() : "");
//                    flow2.setComName(organ.getOrgname());
//                }
//            } else {
//                flow2.setCompany("");
//            }
//            if (oa[4] != null && !oa[4].equals("")) {
//                TblOrganization organ = tblOrganizationMapper.selectByOrgId(new BigDecimal((String) oa[4]));
//                if (organ != null) {
//                    flow2.setDepartincharge(organ != null ? organ.getOrgname() : "");
//                    flow2.setDeparChargeName(organ.getOrgname());
//                }
//            } else {
//                flow2.setDepartincharge("");
//            }
//            flow2.setCreatetime((String) oa[5]);
//            flow2.setFlowstatus((String) oa[6]);
//            flow2.setFatherflowid(new BigDecimal(oa[7] == null ? "0" : oa[7].toString()));
//            flow2.setDepartassist(oa[8] == null ? null : oa[8].toString());
//            flow2.setStatus(oa[9] == null ? null : Integer.parseInt(oa[9].toString()));
//            flow2.setFiringStatus(oa[12] == null ? null : Integer.parseInt(oa[12].toString()));
//            list.add(flow2);
//        }
//        pageInfo.setTlist(list);
                for (TblFlowMySql o : pageInfo.getTlist()) {
                    TblFlow flow2 = new TblFlow();
                    flow2.setFlowid(o.getFlowid());
                    flow2.setFlownumber(o.getFlownumber());
                    flow2.setFlowname(o.getFlowname());
                    if (o.getCompany() != null && !o.getCompany().equals("")) {
                        TblOrganizationMySql organ = tblOrganizationMySqlMapper.selectByOrgId(new BigDecimal(o.getCompany()));
                        if (organ != null) {
                            flow2.setCompany(organ != null ? organ.getOrgname() : "");
                            flow2.setComName(organ.getOrgname());
                        }
                    } else {
                        flow2.setCompany("");
                    }
                    if (o.getDepartincharge() != null && !o.getDepartincharge().equals("")) {
                        TblOrganizationMySql organ = tblOrganizationMySqlMapper.selectByOrgId(new BigDecimal(o.getDepartincharge()));
                        if (organ != null) {
                            flow2.setDepartincharge(organ != null ? organ.getOrgid().toString() : "");
                            flow2.setDeparChargeName(organ.getOrgname());
                        }
                    } else {
                        flow2.setDepartincharge("");
                    }
                    flow2.setCreatetime(o.getCreatetime());
                    flow2.setFlowstatus(o.getFlowstatus());
                    flow2.setFatherflowid(new BigDecimal(o.getFatherflowid() == null ? "0" : o.getFatherflowid().toString()));
                    flow2.setDepartassist(o.getDepartassist() == null ? null : o.getDepartassist().toString());
                    flow2.setStatus(o.getStatus() == null ? null : Integer.parseInt(o.getStatus().toString()));
                    flow2.setFiringStatus(o.getFiringStatus() == null ? null : Integer.parseInt(o.getFiringStatus().toString()));
                    flow2.setSettingid(o.getSettingid() == null ? "" : o.getSettingid().toString());
                    list.add(flow2);
                }
                pageInfo.setTlist(list);

                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("pageInfo", pageInfo);
                dataMap.put("falow", falow);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public void update(TblFlow flow) {
        this.tblFlowMapper.updateByFlowId(flow);
    }

    @Override
    public void updateMySql(TblFlowMySql flowMySql) {
        this.tblFlowMySqlMapper.updateByFlowId(flowMySql);
    }

    @Override
    public Map<String, Object> SaveTblFlow(TblFlowVo vo, String token) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);

            try {
                TblStaffUtil user = userProvider.get();
                if (vo.getFatherflowid() == null || "".equals(vo.getFatherflowid())) {
                    vo.setFatherflowid("0");
                }
                if (vo.getFlownumber() != null) {
                    TblFlow flow = new TblFlow();
                    flow.setFlownumber(vo.getFlownumber());
                    flow.setFlowname(vo.getFlowName());
                    flow.setFatherflowid(new BigDecimal(vo.getFatherflowid()));
                    flow.setFlowbysystem("1");
                    flow.setVersion(1);
                    flow.setEditor(user.getRealname());
                    SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    flow.setCreatetime(fo.format(date));
                    if (vo.getIsFlowdb().equals("1")) {
                        flow.setInflowdb(Integer.parseInt("1"));
                        flow.setCompany(vo.getCompany());
                    } else {
                        if (vo.getDepartincharge() != null && vo.getDepartincharge().length() > 0) {
                            flow.setDepartincharge(vo.getDepartincharge());
                        } else {
                            flow.setDepartincharge(user.getCurrentOrg().getOrgid().toString());
                        }
                        flow.setCompany(user.getCurrentOrg().getOrgid().toString());
                    }
                    tblFlowMapper.insert(flow);
                    resultMap.put("code", "1");
                    resultMap.put("msg", "数据访问成功");
                    resultMap.put("data", flow);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);

            try {
                TblStaffUtil user = userProvider.get();
                if (vo.getFatherflowid() == null || "".equals(vo.getFatherflowid())) {
                    vo.setFatherflowid("0");
                }
                if (vo.getFlownumber() != null) {
                    TblFlowMySql flow = new TblFlowMySql();
                    flow.setFlownumber(vo.getFlownumber());
                    flow.setFlowname(vo.getFlowName());
                    flow.setFatherflowid(new BigDecimal(vo.getFatherflowid()));
                    flow.setFlowbysystem("1");
                    flow.setVersion(1);
                    flow.setEditor(user.getRealname());
                    SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    flow.setCreatetime(fo.format(date));
                    if (vo.getIsFlowdb().equals("1")) {
                        flow.setInflowdb(Integer.parseInt("1"));
                        flow.setCompany(vo.getCompany());
                    } else {
                        if (vo.getDepartincharge() != null && vo.getDepartincharge().length() > 0) {
                            flow.setDepartincharge(vo.getDepartincharge());
                        } else {
                            flow.setDepartincharge(user.getCurrentOrg().getOrgid().toString());
                        }
                        flow.setCompany(user.getCurrentOrg().getOrgid().toString());
                    }
                    tblFlowMySqlMapper.insert(flow);
                    resultMap.put("code", "1");
                    resultMap.put("msg", "数据访问成功");
                    resultMap.put("data", flow);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultMap;
        }
    }

    @Override
    public Map<String, Object> UpdateTblFlow(TblFlowVo vo) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            TblFlow flow = new TblFlow();
//        if (vo.getFathername().equals("行业流程")) {
//            vo.setFathername("流程创建");
//        }
//        if (vo.getIsFlowdb() != null && !"".equals(vo.getIsFlowdb()) && vo.getIsFlowdb().equals("1")) {
//            flow.setInFlowdb(1);
//        }
            flow.setFlowid(vo.getFlowid());
            flow.setFlownumber(vo.getFlownumber());
            flow.setFlowname(vo.getFlowName());
            //flow.setFatherflowid(new BigDecimal(vo.getFatherflowid()));
            tblFlowMapper.updateByFlowId(flow);
            resultMap.put("code", "1");
            resultMap.put("msg", "更新成功");
            resultMap.put("data", flow);
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            TblFlowMySql flow = new TblFlowMySql();
//        if (vo.getFathername().equals("行业流程")) {
//            vo.setFathername("流程创建");
//        }
//        if (vo.getIsFlowdb() != null && !"".equals(vo.getIsFlowdb()) && vo.getIsFlowdb().equals("1")) {
//            flow.setInFlowdb(1);
//        }
            flow.setFlowid(vo.getFlowid());
            flow.setFlownumber(vo.getFlownumber());
            flow.setFlowname(vo.getFlowName());
            //flow.setFatherflowid(new BigDecimal(vo.getFatherflowid()));
            tblFlowMySqlMapper.updateByFlowId(flow);
            resultMap.put("code", "1");
            resultMap.put("msg", "更新成功");
            resultMap.put("data", flow);
            return resultMap;
        }
    }

    @Override
    public String flowtreehy(String orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            String tree = "tree.nodes['-1_0']=\"text:行业流程;method:check(0,-1)\";\n";
            List<TblFlow> list = this.tblFlowMapper.findBysqlFlowh(orgid);
            for (TblFlow flow : list) {
                if (flow.getFatherflowid().toEngineeringString().equals("0")
                        || flow.getFlowid().toEngineeringString().equals("0")) {
                    tree += "tree.nodes[\'"
                            + flow.getFatherflowid().toEngineeringString() + "_"
                            + flow.getFlowid().toString() + "\']=\"text:"
                            + flow.getFlowname() + ";method:check("
                            + flow.getFlowid().toString() + ","
                            + flow.getFatherflowid().toString() + ")\";\n";
                } else {
                    TblFlow fatherFlow = this.tblFlowMapper.findByFatherFlowId(flow.getFatherflowid());
                    if (fatherFlow != null) {
                        if (fatherFlow.getFatherflowid() != null
                                && fatherFlow.getFatherflowid().toEngineeringString()
                                .equals("0")) {
                            tree += "tree.nodes[\'"
                                    + flow.getFatherflowid().toEngineeringString()
                                    + "_" + flow.getFlowid().toString() + "\']=\"text:"
                                    + flow.getFlowname() + ";method:check("
                                    + flow.getFlowid().toString() + ","
                                    + flow.getFatherflowid().toString() + ")\";\n";
                        }
                    }
                }
            }
            return tree;
        } else {
            String tree = "tree.nodes['-1_0']=\"text:行业流程;method:check(0,-1)\";\n";
            List<TblFlowMySql> list = this.tblFlowMySqlMapper.findBysqlFlowh(orgid);
            for (TblFlowMySql flow : list) {
                if (flow.getFatherflowid().toEngineeringString().equals("0")
                        || flow.getFlowid().toEngineeringString().equals("0")) {
                    tree += "tree.nodes[\'"
                            + flow.getFatherflowid().toEngineeringString() + "_"
                            + flow.getFlowid().toString() + "\']=\"text:"
                            + flow.getFlowname() + ";method:check("
                            + flow.getFlowid().toString() + ","
                            + flow.getFatherflowid().toString() + ")\";\n";
                } else {
                    TblFlowMySql fatherFlow = this.tblFlowMySqlMapper.findByFatherFlowId(flow.getFatherflowid());
                    if (fatherFlow != null) {
                        if (fatherFlow.getFatherflowid() != null
                                && fatherFlow.getFatherflowid().toEngineeringString()
                                .equals("0")) {
                            tree += "tree.nodes[\'"
                                    + flow.getFatherflowid().toEngineeringString()
                                    + "_" + flow.getFlowid().toString() + "\']=\"text:"
                                    + flow.getFlowname() + ";method:check("
                                    + flow.getFlowid().toString() + ","
                                    + flow.getFatherflowid().toString() + ")\";\n";
                        }
                    }
                }
            }
            return tree;
        }
    }

    @Override
    public void InsertRiskFLOW(BigDecimal flowid, BigDecimal riskid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            this.tblFlowMapper.insertRiskFlow(flowid, riskid);
        } else {
            this.tblFlowMySqlMapper.insertRiskFlow(flowid, riskid);
        }
    }

    @Override
    public TblFlow findByIdFlow(String pid) {
        return this.tblFlowMapper.findByIdPid(pid);
    }

    @Override
    public TblFlowMySql findByMySqlIdFlow(String pid) {
        return this.tblFlowMySqlMapper.findByIdPid(pid);
    }

    @Override
    public Map<String, Object> finsByPageInfo(CopyVo vo, String token) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                String orgid = user.getCurrentOrg().getOrgid().toString();
                if (StringUtils.isNotEmpty(vo.getPid())) {
                    TblFlow flows = tblFlowMapper.findByIdFlow(new BigDecimal(vo.getPid()));
                    orgid = flows.getCompany();
                }
                PageInfo<TblFlow> pageInfo = new PageInfo<TblFlow>();
                pageInfo.setPageSize(vo.getPageSize());
                pageInfo.setCurrentPage(vo.getPageNumber());
                pageInfo.setTlist(tblFlowMapper.findListByPageInfoFlow(pageInfo, orgid, vo));
                pageInfo.setTotalRecord(tblFlowMapper.findCountByPageInfoFlow(orgid, vo));
                List list = new ArrayList();
                if (pageInfo != null && pageInfo.getTlist() != null) {
                    for (TblFlow o : pageInfo.getTlist()) {
                        TblFlow flow2 = new TblFlow();
                        flow2.setFlowid(o.getFlowid());
                        flow2.setFlownumber(o.getFlownumber());
                        flow2.setFlowname(o.getFlowname());
                        if (o.getCompany() != null && !o.getCompany().equals("")) {
                            TblOrganization organ = tblOrganizationMapper.selectByOrgId(new BigDecimal(o.getCompany()));
                            if (organ != null) {
                                flow2.setCompany(organ != null ? organ.getOrgname() : "");
                                flow2.setComName(organ.getOrgname());
                            }
                        } else {
                            flow2.setCompany("");
                        }
                        if (o.getDepartincharge() != null && !o.getDepartincharge().equals("")) {
                            TblOrganization organ = tblOrganizationMapper.selectByOrgId(new BigDecimal(o.getDepartincharge()));
                            if (organ != null) {
                                flow2.setDepartincharge(organ != null ? organ.getOrgid().toString() : "");
                                flow2.setDeparChargeName(organ.getOrgname());
                            }
                        } else {
                            flow2.setDepartincharge("");
                        }
                        flow2.setCreatetime(o.getCreatetime());
                        flow2.setFlowstatus(o.getFlowstatus());
                        list.add(flow2);
                    }
                }
                pageInfo.setTlist(list);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            CopyVo vo1 = new CopyVo();
            try {
                TblStaffUtil user = userProvider.get();
                String orgid = user.getCurrentOrg().getOrgid().toString();
                if (StringUtils.isNotEmpty(vo1.getPid())) {
                    TblFlowMySql flows = tblFlowMySqlMapper.findByIdFlow(new BigDecimal(vo1.getPid()));
                    orgid = flows.getCompany();
                }
                PageInfo<TblFlowMySql> pageInfo = new PageInfo<TblFlowMySql>();
                pageInfo.setPageSize(vo1.getPageSize());
                pageInfo.setCurrentPage(vo1.getPageNumber());
                pageInfo.setTlist(tblFlowMySqlMapper.findListByPageInfoFlow(pageInfo, orgid, vo1));
                pageInfo.setTotalRecord(tblFlowMySqlMapper.findCountByPageInfoFlow(orgid, vo1));
                List list = new ArrayList();
                if (pageInfo != null && pageInfo.getTlist() != null) {
                    for (TblFlowMySql o : pageInfo.getTlist()) {
                        TblFlow flow2 = new TblFlow();
                        flow2.setFlowid(o.getFlowid());
                        flow2.setFlownumber(o.getFlownumber());
                        flow2.setFlowname(o.getFlowname());
                        if (o.getCompany() != null && !o.getCompany().equals("")) {
                            TblOrganizationMySql organ = tblOrganizationMySqlMapper.selectByOrgId(new BigDecimal(o.getCompany()));
                            if (organ != null) {
                                flow2.setCompany(organ != null ? organ.getOrgname() : "");
                                flow2.setComName(organ.getOrgname());
                            }
                        } else {
                            flow2.setCompany("");
                        }
                        if (o.getDepartincharge() != null && !o.getDepartincharge().equals("")) {
                            TblOrganizationMySql organ = tblOrganizationMySqlMapper.selectByOrgId(new BigDecimal(o.getDepartincharge()));
                            if (organ != null) {
                                flow2.setDepartincharge(organ != null ? organ.getOrgid().toString() : "");
                                flow2.setDeparChargeName(organ.getOrgname());
                            }
                        } else {
                            flow2.setDepartincharge("");
                        }
                        flow2.setCreatetime(o.getCreatetime());
                        flow2.setFlowstatus(o.getFlowstatus());
                        list.add(flow2);
                    }
                }
                pageInfo.setTlist(list);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public void insertFlolw(TblFlow newFlow) {
        this.tblFlowMapper.insert(newFlow);
    }

    @Override
    public void insertMySqlFlolw(TblFlowMySql flowMySql) {
        this.tblFlowMySqlMapper.insert(flowMySql);
    }

    @Override
    public String flowElseInsert(TblFlow newFlow, TblRisk risk, TblControlmatrix controlmatrix, TblFlowBussiness riskBussiness) {
        this.tblRiskMapper.insert(risk);
        TblRiskFlow tblRiskFlow = new TblRiskFlow();
        tblRiskFlow.setRiskid(Long.parseLong(risk.getRiskid().toString()));
        tblRiskFlow.setFlowid(Long.parseLong(newFlow.getFlowid().toString()));
        this.tblRiskMapper.insertByzj(tblRiskFlow);
        riskBussiness.setFlowid(Long.parseLong(newFlow.getFlowid().toString()));
        this.bussinessMapper.insert(riskBussiness);
        this.tblControlmatrixMapper.insert(controlmatrix);
        TblFlowMatrix flowMatrix = new TblFlowMatrix();
        flowMatrix.setConmatid(controlmatrix.getConmatid());
        flowMatrix.setFlowid(newFlow.getFlowid());
        this.tblControlmatrixMapper.insertMatrixZJ(flowMatrix);
        return JsonBean.success();
    }

    @Override
    public String flowElseMySqlInsert(TblFlowMySql newFlow, TblRiskMySql risk, TblControlmatrixMySql controlmatrix, TblFlowBussinessMySql riskBussiness) {
        TblControlmatrixMySql controlmatrixMySql = new TblControlmatrixMySql();
        TblFlowBussinessMySql riskBussinessMySql = new TblFlowBussinessMySql();
        this.tblRiskMySqlMapper.insert(risk);
        TblRiskFlowMySql tblRiskFlowMySql = new TblRiskFlowMySql();
        tblRiskFlowMySql.setRiskid(Long.parseLong(risk.getRiskid().toString()));
        tblRiskFlowMySql.setFlowid(Long.parseLong(newFlow.getFlowid().toString()));
        this.tblRiskMySqlMapper.insertByzj(tblRiskFlowMySql);
        riskBussinessMySql.setFlowid(Long.parseLong(newFlow.getFlowid().toString()));
        this.bussinessMySqlMapper.insert(riskBussinessMySql);
        this.tblControlmatrixMySqlMapper.insert(controlmatrixMySql);
        TblFlowMatrixMySql flowMatrixMySql = new TblFlowMatrixMySql();
        flowMatrixMySql.setConmatid(controlmatrixMySql.getConmatid());
        flowMatrixMySql.setFlowid(newFlow.getFlowid());
        this.tblControlmatrixMySqlMapper.insertMatrixZJ(flowMatrixMySql);
        return JsonBean.success();
    }

    @Override
    public Map<String, Object> findBylcfl(String token) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                List<TblFlow> flows = this.tblFlowMapper.selectByOrgid(user.getCurrentOrg().getOrgid());
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", flows);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                List<TblFlowMySql> flows = this.tblFlowMySqlMapper.selectByOrgid(user.getCurrentOrg().getOrgid());
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", flows);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public String findFlowMatrixByFlowid(BigDecimal flowid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            return this.tblFlowMapper.findFlowMatrixByFlowid(flowid);
        } else {
            return this.tblFlowMySqlMapper.findFlowMatrixByFlowid(flowid);
        }
    }

    @Override
    public List<TblFlow> findByOrgid(BigDecimal orgid) {
        return this.tblFlowMapper.findByOrgid(orgid);
    }

    @Override
    public List<TblFlowMySql> findByMySqlOrgid(BigDecimal orgid) {
        return this.tblFlowMySqlMapper.findByOrgid(orgid);
    }

    @Override
    public Map<String, Object> findByOrgidAndFlowidobj(String flowid, Integer pageNumber, Integer pageSize) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            Map<String, Object> dataMap = new HashMap<String, Object>(0);
            if (flowid != null) {
                TblFlow flow = tblFlowMapper.findById(new BigDecimal(flowid));
                dataMap.put("flow", flow);
                // 内规分页
                PageInfo<TblInnerrule> innerrulePageInfo = new PageInfo<TblInnerrule>();
                innerrulePageInfo.setPageSize(pageSize);
                innerrulePageInfo.setCurrentPage(pageNumber);
                innerrulePageInfo.setTlist(tblInnerruleMapper.findListByPageInfoFlow(innerrulePageInfo, flowid));
                innerrulePageInfo.setTotalRecord(tblInnerruleMapper.findCountByPageInfoFlow(flowid));

                // 外规分页
                PageInfo<TblOuterrule> outPageInfo = new PageInfo<TblOuterrule>();
                outPageInfo.setPageSize(pageSize);
                outPageInfo.setCurrentPage(pageNumber);
                outPageInfo.setTlist(tblOuterruleMapper.findListByPageInfoFlow(outPageInfo, flowid));
                outPageInfo.setTotalRecord(tblOuterruleMapper.findCountByPageInfoFlow(flowid));

                dataMap.put("innerrulePageInfo", innerrulePageInfo);
                dataMap.put("outPageInfo", outPageInfo);
                dataMap.put("flowid", flowid);
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", dataMap);
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            Map<String, Object> dataMap = new HashMap<String, Object>(0);
            if (flowid != null) {
                TblFlowMySql flow = tblFlowMySqlMapper.findById(new BigDecimal(flowid));
                dataMap.put("flow", flow);
                // 内规分页
                PageInfo<TblInnerruleMySql> innerrulePageInfo = new PageInfo<TblInnerruleMySql>();
                innerrulePageInfo.setPageSize(pageSize);
                innerrulePageInfo.setCurrentPage(pageNumber);
                innerrulePageInfo.setTlist(tblInnerruleMySqlMapper.findListByPageInfoFlow(innerrulePageInfo, flowid));
                innerrulePageInfo.setTotalRecord(tblInnerruleMySqlMapper.findCountByPageInfoFlow(flowid));

                // 外规分页
                PageInfo<TblOuterruleMySql> outPageInfo = new PageInfo<TblOuterruleMySql>();
                outPageInfo.setPageSize(pageSize);
                outPageInfo.setCurrentPage(pageNumber);
                outPageInfo.setTlist(tblOuterruleMySqlMapper.findListByPageInfoFlow(outPageInfo, flowid));
                outPageInfo.setTotalRecord(tblOuterruleMySqlMapper.findCountByPageInfoFlow(flowid));

                dataMap.put("innerrulePageInfo", innerrulePageInfo);
                dataMap.put("outPageInfo", outPageInfo);
                dataMap.put("flowid", flowid);
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", dataMap);
            return resultMap;
        }
    }

    @Override
    public void updateFlow(TblFlow newFlow) {
        this.tblFlowMapper.updateByFlowId(newFlow);
    }

    @Override
    public void updateMySqlFlow(TblFlowMySql tblFlowMySql) {
        this.tblFlowMySqlMapper.updateByFlowId(tblFlowMySql);
    }

    @Override
    public void flowElseUpdate(TblFlow newFlow, TblRisk risk, TblControlmatrix controlmatrix, TblFlowBussiness riskBussiness) {
        this.tblRiskMapper.updateById(risk);
        this.bussinessMapper.updateById(riskBussiness);
        this.tblControlmatrixMapper.updateById(controlmatrix);
    }

    @Override
    public void flowElseMySqlUpdate(TblFlowMySql newFlow, TblRiskMySql tblRiskMySql, TblControlmatrixMySql controlmatrixMySql, TblFlowBussinessMySql riskBussinessMySql) {
        this.tblRiskMySqlMapper.updateById(tblRiskMySql);
        this.bussinessMySqlMapper.updateById(riskBussinessMySql);
        this.tblControlmatrixMySqlMapper.updateById(controlmatrixMySql);
    }


}