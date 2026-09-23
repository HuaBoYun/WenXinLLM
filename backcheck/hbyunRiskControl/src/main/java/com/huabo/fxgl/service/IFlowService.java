package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.util.Tree;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
public interface IFlowService extends IService<Flow> {
    Flow findFlowByRiskId(String riskid);
    public String flowNumberIsSole(String number,String orgid);
    BigDecimal newFlowId();
    List<Flow> findList(BigDecimal Orgid);
    Flow findTblFlowByRiskId(String riskid);
    Flow findTblFlowByorgId(BigDecimal orgid);
    List<Tree> flowtreehy(String orgid);
    IPage<Flow> hyFlowPage(String orgid,String fathlowid,String flowname, String flownumber, Integer pageNo, Integer pageSize);
    public String copyfromFlowToRisk(Risk oldrisk, Flow oldflow, Flow newflow, Controlmatrix oldcontrolmatrix, FlowBussiness oldriskBussiness, Riskcategory riskcat, String faflowid, String orgid);

    String findControlMatrixIdUniqueByFlowid(BigDecimal flowid);

    String copyfromRiskToFlow(Risk oldrisk, Flow oldflow, Flow newflow, Controlmatrix oldcontrolmatrix, FlowBussiness oldriskBussiness, String faflowid, String orgid);
}
