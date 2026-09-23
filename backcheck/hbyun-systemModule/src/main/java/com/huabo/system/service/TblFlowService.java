package com.huabo.system.service;

import com.huabo.system.entity.*;
import com.huabo.system.oracle.vo.CopyVo;
import com.huabo.system.oracle.vo.TblFlowVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public interface TblFlowService {

    TblFlow findByIdFlows(BigDecimal flowid);

    TblFlow findById(String currentpid);

    List findByis(String var1, String var2);

    void modify(TblFlow dp);

    List findBySql(String sql);

    String findControlMatrixIdUniqueByFlowid(BigDecimal flowid);

    TblFlowBussiness findUniqueByFlowId(BigDecimal flowid);

    Map<String,Object> flowtree(HttpServletRequest request,String token, String staffId, String treeName, String orgid);

    Map<String,Object> flowtreevser(HttpServletRequest request, String token, String staffId, String treeName, String orgid);

    Map<String,Object> findBysqAll(String token,String staffId, String faflowid, Integer pageNumber, Integer pageSize, String name, String code);

    Map<String, Object> findBysqAllversion(String token,String staffId, String flowid, Integer pageNumber, Integer pageSize);

    Integer findTblFlowStartUpFiringStatus(BigDecimal flowid, BigDecimal firing);

    Integer modifyTblFlowFiringStatus(BigDecimal flowid, BigDecimal firing);

    TblRiskFlow findBysql(String flowid);

    void deleteTblRiskFlow(String flowid);

    List<TblFlow> findByFlownumber(String var1);

    void deleteBy(TblFlow flowid);

    List<TblFlow> findByfaflowid(String var1);

    void add(TblFlow var1);

    void excuteSql(TblFlow var1);

    Map<String, Object> listBySqlPage(String token, String staffId, String faflowid, String flowname, String flownumber, String stutes, String desc, Integer pageNumber, Integer pageSize, String belongsto,Integer firingStatus,String view);

    void update(TblFlow flow);

    Map<String, Object> SaveTblFlow(TblFlowVo vo, String token);

    Map<String, Object> UpdateTblFlow(TblFlowVo vo);

    String flowtreehy(String findOrgId);

    void InsertRiskFLOW(BigDecimal flowid, BigDecimal riskid);

    TblFlow findByIdFlow(String pid);

    Map<String, Object> finsByPageInfo(CopyVo vo, String token);

    void insertFlolw(TblFlow newFlow);

    String flowElseInsert(TblFlow newFlow, TblRisk risk, TblControlmatrix controlmatrix, TblFlowBussiness riskBussiness);

    Map<String, Object> findBylcfl(String token);

    String findFlowMatrixByFlowid(BigDecimal flowid);

    List<TblFlow> findByOrgid(BigDecimal orgid);

    Map<String, Object> findByOrgidAndFlowidobj(String flowid, Integer pageNumber, Integer pageSize);

    void updateFlow(TblFlow newFlow);

    void flowElseUpdate(TblFlow newFlow, TblRisk risk, TblControlmatrix controlmatrix, TblFlowBussiness riskBussiness);

}