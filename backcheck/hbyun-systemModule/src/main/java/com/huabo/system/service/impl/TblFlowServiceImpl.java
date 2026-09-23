package com.huabo.system.service.impl;

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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblControlmatrix;
import com.huabo.system.entity.TblFlow;
import com.huabo.system.entity.TblFlowBussiness;
import com.huabo.system.entity.TblFlowMatrix;
import com.huabo.system.entity.TblInnerrule;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblOuterrule;
import com.huabo.system.entity.TblRisk;
import com.huabo.system.entity.TblRiskFlow;
import com.huabo.system.mapper.TblControlmatrixMapper;
import com.huabo.system.mapper.TblFlowBussinessMapper;
import com.huabo.system.mapper.TblFlowMapper;
import com.huabo.system.mapper.TblInnerruleMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblOuterruleMapper;
import com.huabo.system.mapper.TblRiskMapper;
import com.huabo.system.oracle.vo.CopyVo;
import com.huabo.system.oracle.vo.TblFlowVo;
import com.huabo.system.service.TblFlowService;

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
    private UserProvider userProvider;

    @Override
    public TblFlow findByIdFlows(BigDecimal flowid) {
        return tblFlowMapper.findById(flowid);
    }


    @Override
    public TblFlow findById(String currentpid) {
        return tblFlowMapper.findById(new BigDecimal(currentpid));
    }

    @Override
    public List findByis(String flowcode, String orgid) {
    	return tblFlowMapper.selectByTrim(flowcode, orgid);
    }

    @Override
    public void modify(TblFlow dp) {
        tblFlowMapper.insert(dp);
    }

    @Override
    public List findBySql(String sql) {
            return tblFlowMapper.selectByS(sql);
    }

    @Override
    public String findControlMatrixIdUniqueByFlowid(BigDecimal flowid) {
            List<TblFlowBussiness> list = this.bussinessMapper.findBysql(flowid);
            return list != null && list.size() != 0 ? String.valueOf((TblFlowBussiness) list.get(0)) : null;
    }

    @Override
    public TblFlowBussiness findUniqueByFlowId(BigDecimal flowid) {
        return this.bussinessMapper.findBy(flowid);
    }

    @Override
    public Map<String, Object> flowtree(HttpServletRequest request, String token, String staffId, String treeName, String orgid) {
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
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public Map<String, Object> flowtreevser(HttpServletRequest request, String token, String staffId, String treeName, String orgid) {
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
    }


    @Override
    public Map<String, Object> findBysqAll(String token, String staffId, String faflowid, Integer pageNumber, Integer pageSize, String name, String code) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal orgid = user.getCurrentOrg().getOrgid();
                PageInfo<TblFlow> pageInfo = new PageInfo<TblFlow>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                
                Page<TblFlow> page = new Page<TblFlow>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblFlow> pageList = tblFlowMapper.selectTblFlowList(page, orgid.toString(), faflowid, name, code);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public Map<String, Object> findBysqAllversion(String token, String staffId, String flowid, Integer pageNumber, Integer pageSize) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal orgid = user.getCurrentOrg().getOrgid();
                PageInfo<TblFlow> pageInfo = new PageInfo<TblFlow>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                
                Page<TblFlow> page = new Page<TblFlow>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblFlow> pageList = tblFlowMapper.findBysqAllversion(page, orgid.toString(), flowid);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
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
    }

    @Override
    public Integer findTblFlowStartUpFiringStatus(BigDecimal flowid, BigDecimal firing) {
            TblFlow flow = this.findById(flowid.toString());
            Integer count = this.tblFlowMapper.findOtherFiringStatusByParentId(flow.getFatherflowid(), flow.getFlowid());
            return count;
    }

    @Override
    public Integer modifyTblFlowFiringStatus(BigDecimal flowid, BigDecimal firing) {
            this.tblFlowMapper.excuteSql(flowid, firing);
            return 0;
    }

    @Override
    public TblRiskFlow findBysql(String flowid) {
        List<TblRiskFlow> list = this.tblFlowMapper.findBysql(flowid);
        return list != null && list.size() > 0 ? (TblRiskFlow) list.get(0) : null;
    }

    @Override
    public void deleteTblRiskFlow(String flowid) {
    	tblFlowMapper.deleteTblRiskFlow(flowid);
    }

    @Override
    public List<TblFlow> findByFlownumber(String flownumber) {
        return tblFlowMapper.selectFlownumber(flownumber);
    }

    @Override
    public void deleteBy(TblFlow flowid) {
        tblFlowMapper.deleteById(flowid.getFlowid());
    }

    @Override
    public List<TblFlow> findByfaflowid(String flowid) {
        return this.tblFlowMapper.findByFlowid(flowid);
    }

    @Override
    public void add(TblFlow flow) {
        this.tblFlowMapper.insert(flow);
    }

    @Override
    public void excuteSql(TblFlow tblFlows) {
        tblFlowMapper.insert(tblFlows);
    }

    @Override
    public Map<String, Object> listBySqlPage(String token, String staffId, String faflowid, String flowname, String flownumber, String stutes, String desc, Integer pageNumber, Integer pageSize, String belongsto, Integer firingStatus, String view) {
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
                
                Page<TblFlow> page = new Page<TblFlow>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblFlow> pageList = tblFlowMapper.findListByPageInfo(page, orgid, faflowid, flowname, flownumber, stutes, desc, belongsto, fatherFlowid, firingStatus);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                List list = new ArrayList();

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
    }

    @Override
    public void update(TblFlow flow) {
        this.tblFlowMapper.updateByFlowId(flow);
    }

    @Override
    public Map<String, Object> SaveTblFlow(TblFlowVo vo, String token) {
    	
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
                    flow.setFlowid(RandomUtil.uuBigDecimalId());
                    tblFlowMapper.insert(flow);
                    resultMap.put("code", "1");
                    resultMap.put("msg", "数据访问成功");
                    resultMap.put("data", flow);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public Map<String, Object> UpdateTblFlow(TblFlowVo vo) {
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
            tblFlowMapper.updateById(flow);
            resultMap.put("code", "1");
            resultMap.put("msg", "更新成功");
            resultMap.put("data", flow);
            return resultMap;
    }

    @Override
    public String flowtreehy(String orgid) {
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
    }

    @Override
    public void InsertRiskFLOW(BigDecimal flowid, BigDecimal riskid) {
    	this.tblFlowMapper.insertRiskFlow(flowid, riskid);
    }

    @Override
    public TblFlow findByIdFlow(String pid) {
        return this.tblFlowMapper.findByIdPid(pid);
    }

    @Override
    public Map<String, Object> finsByPageInfo(CopyVo vo, String token) {
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
                
                Page<TblFlow> page = new Page<TblFlow>(vo.getPageNumber(),vo.getPageSize());
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblFlow> pageList = tblFlowMapper.findListByPageInfoFlow(page, orgid, vo);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int) pageList.getTotal());
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
    }

    @Override
    public void insertFlolw(TblFlow newFlow) {
        this.tblFlowMapper.insert(newFlow);
    }

    @Override
    public String flowElseInsert(TblFlow newFlow, TblRisk risk, TblControlmatrix controlmatrix, TblFlowBussiness riskBussiness) {
        this.tblRiskMapper.insert(risk);
        TblRiskFlow tblRiskFlow = new TblRiskFlow();
        tblRiskFlow.setRiskid(risk.getRiskid());
        tblRiskFlow.setFlowid(newFlow.getFlowid());
        this.tblRiskMapper.insertByzj(tblRiskFlow);
        riskBussiness.setFlowid(newFlow.getFlowid());
        this.bussinessMapper.insert(riskBussiness);
        this.tblControlmatrixMapper.insert(controlmatrix);
        TblFlowMatrix flowMatrix = new TblFlowMatrix();
        flowMatrix.setConmatid(controlmatrix.getConmatid());
        flowMatrix.setFlowid(newFlow.getFlowid());
        this.tblControlmatrixMapper.insertMatrixZJ(flowMatrix);
        return JsonBean.success();
    }

    @Override
    public Map<String, Object> findBylcfl(String token) {
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
    }

    @Override
    public String findFlowMatrixByFlowid(BigDecimal flowid) {
        return this.tblFlowMapper.findFlowMatrixByFlowid(flowid);
    }

    @Override
    public List<TblFlow> findByOrgid(BigDecimal orgid) {
        return this.tblFlowMapper.findByOrgid(orgid);
    }

    @Override
    public Map<String, Object> findByOrgidAndFlowidobj(String flowid, Integer pageNumber, Integer pageSize) {
    	
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            Map<String, Object> dataMap = new HashMap<String, Object>(0);
            if (flowid != null) {
                TblFlow flow = tblFlowMapper.findById(new BigDecimal(flowid));
                dataMap.put("flow", flow);
                // 内规分页
                PageInfo<TblInnerrule> innerrulePageInfo = new PageInfo<TblInnerrule>();
                innerrulePageInfo.setPageSize(pageSize);
                innerrulePageInfo.setCurrentPage(pageNumber);

                Page<TblInnerrule> innpage = new Page<TblInnerrule>(pageNumber,pageSize);
                innpage.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblInnerrule> innpageList = tblInnerruleMapper.findListByPageInfoFlow(innpage, flowid);
                innerrulePageInfo.setTlist(innpageList.getRecords());
                innerrulePageInfo.setTotalRecord((int) innpageList.getTotal());

                // 外规分页
                PageInfo<TblOuterrule> outPageInfo = new PageInfo<TblOuterrule>();
                outPageInfo.setPageSize(pageSize);
                outPageInfo.setCurrentPage(pageNumber);
                
                Page<TblOuterrule> outpage = new Page<TblOuterrule>(pageNumber,pageSize);
                outpage.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblOuterrule> outpageList = tblOuterruleMapper.findListByPageInfoFlow(outpage, flowid);
                outPageInfo.setTlist(outpageList.getRecords());
                outPageInfo.setTotalRecord((int)outpageList.getTotal());

                dataMap.put("innerrulePageInfo", innerrulePageInfo);
                dataMap.put("outPageInfo", outPageInfo);
                dataMap.put("flowid", flowid);
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", dataMap);
            return resultMap;
    }

    @Override
    public void updateFlow(TblFlow newFlow) {
        this.tblFlowMapper.updateById(newFlow);
    }

    @Override
    public void flowElseUpdate(TblFlow newFlow, TblRisk risk, TblControlmatrix controlmatrix, TblFlowBussiness riskBussiness) {
        this.tblRiskMapper.updateById(risk);
        this.bussinessMapper.updateById(riskBussiness);
        this.tblControlmatrixMapper.updateById(controlmatrix);
    }

}