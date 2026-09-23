package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.*;
import com.huabo.fxgl.mapper.FlowMapper;
import com.huabo.fxgl.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.util.CopyObject;
import com.huabo.fxgl.util.DateUtils;
import com.huabo.fxgl.util.Tree;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@Service
@Slf4j
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements IFlowService {

    @Autowired
    private FlowMapper flowMapper;

    @Autowired
    private IRiskService riskService;
    @Autowired
    private IRiskFlowService riskFlowService;
    @Autowired
    private IFlowBussinessService flowBussinessService;
    @Autowired
    private IControlmatrixService controlmatrixService;
    @Autowired
    private IRiskControlmatrixService riskControlmatrixService;
    @Autowired
    private IFlowdesService flowdesService;

    @Override
    public List<Flow> findList(BigDecimal Orgid) {
        List<Flow> list = flowMapper.findList(Orgid);
        return list;
    }

    @Override
    public Flow findTblFlowByRiskId(String riskid) {

        List<Flow> tblFlowByRiskId = flowMapper.findTblFlowByRiskId(riskid);
        Flow flow = new Flow();
        if (tblFlowByRiskId != null && tblFlowByRiskId.size() > 0) {
            flow = tblFlowByRiskId.get(0);
        }
        return flow;
    }



    @Override
    public Flow findFlowByRiskId(String riskid) {
        List<Flow> list = flowMapper.selectFlowByRiskId(riskid);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public Flow findTblFlowByorgId(BigDecimal orgid) {
        List<Flow> tblFlowByorgId = flowMapper.findTblFlowByorgId(orgid);

        Flow flow = new Flow();
        if (tblFlowByorgId != null && tblFlowByorgId.size() > 0) {
            flow = tblFlowByorgId.get(0);
        }
        return flow;
    }

    @Override
    public List<Tree> flowtreehy(String orgid) {
        //String sql="SELECT f.FLOWID,f.FLOWNAME,f.FATHERFLOWID,f.company FROM TBL_FLOW f WHERE FLOWBYSYSTEM='1' and InFlowDB=1   AND f.COMPANY="+orgid;
        QueryWrapper<Flow> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("FLOWID", "FLOWNAME", "FATHERFLOWID", "COMPANY");
        queryWrapper.eq("FLOWBYSYSTEM", "1");
        queryWrapper.eq("InFlowDB", 1);
        queryWrapper.eq("COMPANY", orgid);
//        queryWrapper.eq("FATHERFLOWID", 0);
        queryWrapper.orderByAsc("FLOWID");
        List<Flow> flowList = list(queryWrapper);
        List<Tree> treeList = new LinkedList<>();
//        List<Tree> childTreeList = new LinkedList<>();
        for (Flow flow:flowList) {
            Tree tree = new Tree();
            tree.setId(flow.getFlowid());
            tree.setpId(flow.getFatherflowid());
            tree.setName(flow.getFlowname());
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("FLOWBYSYSTEM", "1");
            queryWrapper.eq("InFlowDB", 1);
            queryWrapper.eq("COMPANY", orgid);
            queryWrapper.eq("FATHERFLOWID", flow.getFlowid());
            tree.setIsParent(count(queryWrapper) > 0);
            treeList.add(tree);
        }
        return treeList;
    }

    @Autowired
    private IOrganizationService organizationService;

    @Override
    public IPage<Flow> hyFlowPage(String orgid, String fathlowid, String flowname, String flownumber, Integer pageNo, Integer pageSize) {
        IPage<Flow> page = new Page(pageNo, pageSize);
        QueryWrapper<Flow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("1", "1");
        if (StringUtils.isNotEmpty(flowname)) {
            queryWrapper.like("FLOWNAME", flowname);
        }
        if (StringUtils.isNotEmpty(flownumber)) {
            queryWrapper.like("FLOWNUMBER", flownumber);
        }

        if (StringUtils.isEmpty(fathlowid)) {
            baseMapper.hyFlowPage1(page, queryWrapper, orgid);
        } else {
            baseMapper.hyFlowPage2(page, queryWrapper, orgid, fathlowid);
        }
        for (Flow flow:page.getRecords()) {
            if (StringUtils.isNotEmpty(flow.getCompany())) {
                BigDecimal oid = new BigDecimal(flow.getCompany());
                flow.setCompanyName(organizationService.getById(oid).getOrgname());
            }
            if (StringUtils.isNotEmpty(flow.getDepartincharge())) {
                BigDecimal oid = new BigDecimal(flow.getDepartincharge());
                flow.setDepartinchargeName(organizationService.getById(oid).getOrgname());
            }
        }
        return page;
    }


    @Transactional
    @Override
    public String copyfromFlowToRisk(Risk oldrisk, Flow oldflow, Flow newflow, Controlmatrix oldcontrolmatrix, FlowBussiness oldriskBussiness, Riskcategory riskcat, String faflowid, String orgid) {
        //风险
        Risk newrisk = new Risk();
        newrisk.setBelongsto(oldrisk.getBelongsto());//公司
        newrisk.setReorg(oldrisk.getReorg());
        newrisk.setXgbmName(oldrisk.getXgbmName());
        newrisk.setRisknumber(new Date().getTime()+"");
        newrisk.setRiskname(oldrisk.getRiskname());
        newrisk.setRiskdes(oldrisk.getRiskdes());
        newrisk.setRiskprogram(oldrisk.getRiskprogram());
//        newrisk.setTblRiskcategory(oldrisk.getTblRiskcategory());
        newrisk.setBelongsto(oldrisk.getBelongsto());
        newrisk.setRiskcategory(riskcat);
        newrisk.setRiskcreatedt(new Date());
        newrisk.setVersion("1");//版本
        riskService.save(newrisk);

        newflow.setCompany(orgid);
        this.save(newflow);

        //关系表
        RiskFlow tblRiskFlow = new RiskFlow();
        tblRiskFlow.setRiskid(newrisk.getRiskid());
        tblRiskFlow.setFlowid(newflow.getFlowid());
        riskFlowService.save(tblRiskFlow);

        if (oldriskBussiness!=null) {
            //业务描述
            FlowBussiness newriskBussines = new FlowBussiness();
            newriskBussines.setBussinessdes(oldriskBussiness.getBussinessdes());
            newriskBussines.setBussinessname(oldriskBussiness.getBussinessname());
            newriskBussines.setBussinessnumber(oldriskBussiness.getBussinessnumber());
            newriskBussines.setFlowid(Long.parseLong(newflow.getFlowid().toString()));
            flowBussinessService.save(newriskBussines);
        }

        if (oldcontrolmatrix!=null) {
            //控制措施
//            ControlMatrixService service = (TblControlMatrixService) SpringContextHolder.getBean("TblControlMatrixService");
//            TblRiskControlMatrixService tblRiskControlMatrixService = (TblRiskControlMatrixService) SpringContextHolder.getBean("TblRiskControlMatrixService");
            Controlmatrix newcontrolmatrix = new Controlmatrix();
            newcontrolmatrix.setFlowcode(newflow.getFlownumber());
            newcontrolmatrix.setFlowname(newflow.getFlowname());
            newcontrolmatrix.setToplevelflowcat(oldcontrolmatrix.getToplevelflowcat());
            newcontrolmatrix.setControlfrequency(oldcontrolmatrix.getControlfrequency());
            newcontrolmatrix.setControltype(oldcontrolmatrix.getControltype());
            newcontrolmatrix.setControlmethod(oldcontrolmatrix.getControlmethod());
            newcontrolmatrix.setKeycontrol(oldcontrolmatrix.getKeycontrol());
            newcontrolmatrix.setEffective(oldcontrolmatrix.getEffective());
            newcontrolmatrix.setControltest(oldcontrolmatrix.getControltest());
            newcontrolmatrix.setFinancialreportidentify(oldcontrolmatrix.getFinancialreportidentify());
            newcontrolmatrix.setControldes(oldcontrolmatrix.getControldes());
            newcontrolmatrix.setConkzcs(oldcontrolmatrix.getConkzcs());
            newcontrolmatrix.setControltarget(oldcontrolmatrix.getControltarget());
            newcontrolmatrix.setControlmanager(oldcontrolmatrix.getControlmanager());
            newcontrolmatrix.setControlnumber(oldcontrolmatrix.getControlnumber());
            controlmatrixService.save(newcontrolmatrix);
            //关系表
            RiskControlmatrix riskControlMatrix = new RiskControlmatrix();
            riskControlMatrix.setConmatid(newcontrolmatrix.getConmatid());
            riskControlMatrix.setRiskid(newrisk.getRiskid());
            riskControlmatrixService.save(riskControlMatrix);

        }
        //流程描述
        if (newflow != null && newflow.getFlowid() != null) {
            List<Flowdes> lcList = flowdesService.returnFlowdesByRiskid(oldrisk.getRiskid().toString());
            for (int i = 0; i < lcList.size(); i++) {
                Flowdes flowdes = lcList.get(i);
                Flowdes newFlowdes = new Flowdes();
                CopyObject.copyPropertiesExclude(flowdes, newFlowdes, new String[] { "flowdesid" });
                newFlowdes.setFlowid(newflow.getFlowid());
                flowdesService.save(newFlowdes);
            }
        }
        return newrisk.getRiskid().toString();
    }

    @Override
    public String findControlMatrixIdUniqueByFlowid(BigDecimal flowid) {
        return baseMapper.selectTop1ConmatidByFlowId(flowid);
    }

    @Transactional
    @Override
    public String copyfromRiskToFlow(Risk oldrisk, Flow oldflow, Flow newflow, Controlmatrix oldcontrolmatrix, FlowBussiness oldriskBussiness, String faflowid, String orgid) {
        //保存newflow
        newflow.setCompany(orgid);
        newflow.setCreatetime(DateUtils.parseDate(new Date(), "yyyy-MM-dd"));
        save(newflow);
        //风险
        Risk newrisk = new Risk();
        newrisk.setBelongsto(oldrisk.getBelongsto());
        newrisk.setReorg(oldrisk.getReorg());
        newrisk.setXgbmName(oldrisk.getXgbmName());
        newrisk.setRisknumber(oldrisk.getRisknumber());
        newrisk.setRiskname(oldrisk.getRiskname());
        newrisk.setVersion(oldrisk.getVersion());
        newrisk.setRiskdes(oldrisk.getRiskdes());
        newrisk.setRiskprogram(oldrisk.getRiskprogram());
        riskService.save(newrisk);
        //关系表
        RiskFlow tblRiskFlow = new RiskFlow();
        tblRiskFlow.setRiskid(newrisk.getRiskid());
        tblRiskFlow.setFlowid(newflow.getFlowid());
        riskFlowService.save(tblRiskFlow);

        if (oldriskBussiness!=null) {
            //业务描述
            FlowBussiness newriskBussines = new FlowBussiness();
            newriskBussines.setBussinessdes(oldriskBussiness.getBussinessdes());
            newriskBussines.setBussinessname(oldriskBussiness.getBussinessname());
            newriskBussines.setBussinessnumber(oldriskBussiness.getBussinessnumber());
            newriskBussines.setFlowid(Long.parseLong(newflow.getFlowid().toString()));
            flowBussinessService.save(newriskBussines);
        }
        if (oldcontrolmatrix!=null) {
            //控制措施
            Controlmatrix newcontrolmatrix = new Controlmatrix();
            newcontrolmatrix.setFlowcode(newflow.getFlownumber());
            newcontrolmatrix.setFlowname(newflow.getFlowname());
            newcontrolmatrix.setToplevelflowcat(oldcontrolmatrix.getToplevelflowcat());
            newcontrolmatrix.setControlfrequency(oldcontrolmatrix.getControlfrequency());
            newcontrolmatrix.setControltype(oldcontrolmatrix.getControltype());
            newcontrolmatrix.setControlmethod(oldcontrolmatrix.getControlmethod());
            newcontrolmatrix.setKeycontrol(oldcontrolmatrix.getKeycontrol());
            newcontrolmatrix.setEffective(oldcontrolmatrix.getEffective());
            newcontrolmatrix.setControltest(oldcontrolmatrix.getControltest());
            newcontrolmatrix.setFinancialreportidentify(oldcontrolmatrix.getFinancialreportidentify());
            newcontrolmatrix.setControldes(oldcontrolmatrix.getControldes());
            newcontrolmatrix.setConkzcs(oldcontrolmatrix.getConkzcs());
            newcontrolmatrix.setControlmanager(oldcontrolmatrix.getControlmanager());
            newcontrolmatrix.setControlnumber(oldcontrolmatrix.getControlnumber());
            controlmatrixService.save(newcontrolmatrix);

            //保存中间表数据
            baseMapper.insertFlowMatrix(newflow.getFlowid(), newcontrolmatrix.getConmatid());
        }
        //流程描述
        if (newflow != null && newflow.getFlowid() != null) {
            List<Flowdes> lcList = flowdesService.returnFlowdesByRiskid(oldrisk.getRiskid().toString());
            for (int i = 0; i < lcList.size(); i++) {
                Flowdes flowdes = lcList.get(i);
                Flowdes newFlowdes = new Flowdes();
                CopyObject.copyPropertiesExclude(flowdes, newFlowdes, new String[] { "flowdesid" });
                newFlowdes.setFlowid(newflow.getFlowid());
                flowdesService.save(newFlowdes);
            }
        }
        return newflow.getFlowid().toString();
    }

    @Override
    public String flowNumberIsSole(String number, String orgid) {
        String is = "1";
        Object obj = flowMapper.selectNumberIsSole(number, orgid);
        log.info("" + obj);
        if (Integer.parseInt(obj.toString()) == 0) {
            is = "0";
        }
        return is;
    }

    @Override
    public BigDecimal newFlowId() {
        return new BigDecimal(baseMapper.selectMaxFlowId() + 1);
    }


}
