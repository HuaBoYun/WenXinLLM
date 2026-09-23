package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.BaseDao;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.YMFormDataMapper;
import com.huabo.system.service.YMFormDataService;


@Service
public class YMFormDataServiceImpl implements YMFormDataService {
    @Resource
    private YMFormDataMapper ymFormDataMapper;
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Override
    public HashMap<String, Object> setYmFormData(TblSystemSheetTable sheet, BigDecimal fromId, TblStaffUtil loginStaff) throws Exception {
        return this.setYmFormDataOracle(sheet, fromId, loginStaff);
    }


    //给OA待办发送信息方法
    @Override
    public HashMap<String, Object> setYmFormDataMessage(TblSystemSheetTable sheet, BigDecimal fromId) throws Exception {
        return this.setYmFormDataOracleMessage(sheet, fromId);
    }

    
    @Override
	public BigDecimal selectContractTypeIdByContracrtId(BigDecimal fromId) throws Exception {
		return this.ymFormDataMapper.selectContractTypeIdByContractId(fromId);
	}

    private HashMap<String, Object> setYmFormDataOracle(TblSystemSheetTable sheet, BigDecimal fromId, TblStaffUtil loginStaff) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        List<String> ridList = null;
        switch (sheet.getClassName()) {
            case "TblCyhwUnit": //合同订立
                dataMap = this.ymFormDataMapper.selectCyhwUnitMapById(fromId);
                resultMap = this.setHTDLresultMap(dataMap);
                break;
            case "HTFB": //合同范本
                dataMap = this.ymFormDataMapper.selectCyhwUnitMapById(fromId);
                resultMap = this.setHTFBresultMap(dataMap);
                break;
            case "HTYY":  //合同用印
                dataMap = this.ymFormDataMapper.selectHTYYMapById(fromId);
                resultMap = this.setHTYYresultMap(dataMap);
                break;
            case "HTBG": //合同变更
                dataMap = this.ymFormDataMapper.selectCyhwUnitBGMapById(fromId);
                resultMap = this.setHTBGresultMap(dataMap);
                break;
            case "HMDGL": //黑名单管理
                dataMap = this.ymFormDataMapper.selectHMDGLMapById(fromId);
                resultMap = this.setHMDGLresultMap(dataMap);
                break;
            case "JHGL": //计划管理
                dataMap = this.ymFormDataMapper.selectJHGLMapById(fromId);
                resultMap = this.setJHGLresultMap(dataMap);
                break;
            case "XMGL": //项目管理
                dataMap = this.ymFormDataMapper.selectXMGLMapById(fromId);
                resultMap = this.setXMGLresultMap(dataMap);
                break;
            case "ZDSH": //制度审核
            	ridList = this.ymFormDataMapper.selectRidListByOrgIdRoleName(loginStaff.getCurrentOrg().getOrgid(),"合规管理员");
                dataMap = this.ymFormDataMapper.selectZDSHMapById(fromId, String.join(",", ridList));
                resultMap = this.setZDSHresultMap(dataMap);
                break;
            case "JYSXSH": //经营事项审核
            	ridList = this.ymFormDataMapper.selectRidListByOrgIdRoleName(loginStaff.getCurrentOrg().getOrgid(),"合规管理员");
                dataMap = this.ymFormDataMapper.selectJYSXSHMapById(fromId, String.join(",", ridList));
                resultMap = this.setJYSXSHresultMap(dataMap);
                break;
            case "JFDJ": //纠纷管理纠纷登记
                dataMap = this.ymFormDataMapper.selectJFDJMapById(fromId);
                resultMap = this.setJFDJresultMap(dataMap);
                break;
            case "XSGC": //协商过程上报
                dataMap = this.ymFormDataMapper.selectXSGCMapById(fromId);
                resultMap = this.setXSGCresultMap(dataMap);
                break;
            case "XSGCJG": //协商过程结果
                dataMap = this.ymFormDataMapper.selectXSGCMapById(fromId);
                resultMap = this.setXSGCresultMap(dataMap);
                break;
            case "NDJH": //年度计划
                dataMap = this.ymFormDataMapper.selectNDJHMapById(fromId);
                resultMap = this.setNDJHresultMap(dataMap);
                break;
            case "NDKH": //年度考核
                dataMap = this.ymFormDataMapper.selectNDKHMapById(fromId);
                resultMap = this.setNDKHresultMap(dataMap);
                break;
            case "ZYSQ": //公司律师 执业申请 提交流程
                dataMap = this.ymFormDataMapper.selectZYSQMapById(fromId);
                resultMap = this.setZYSQresultMap(dataMap);
                break;
            case "ZFLGW": //总法律顾问
                dataMap = this.ymFormDataMapper.selectZFLGWMapById(fromId);
                resultMap = this.setZFLGWresultMap(dataMap);
                break;
            case "FWRY": //法务人员
                dataMap = this.ymFormDataMapper.selectFWRYMapById(fromId);
                resultMap = this.setFWRYresultMap(dataMap);
                break;
            case "PFJH": //普法计划
                dataMap = this.ymFormDataMapper.selectPFJHMapById(fromId);
                resultMap = this.setPFJHresultMap(dataMap);
                break;
            case "QTWJBS": //其他文件报送
                dataMap = this.ymFormDataMapper.selectQTWJBSMapById(fromId);
                resultMap = this.setQTWJBSresultMap(dataMap);
                break;
            case "SJXMGL": //审计项目管理
                dataMap = this.ymFormDataMapper.selectSJXMGLapById(fromId);
                resultMap = this.setSJXMGLresultMap(dataMap);
                break;
            case "WDDG": //我的底稿
                dataMap = this.ymFormDataMapper.selectWDDGMapById(fromId);
                resultMap = this.setWDDGresultMap(dataMap);
                break;
            case "PJGL": //评价管理
                dataMap = this.ymFormDataMapper.selectPJGLMapById(fromId);
                resultMap = this.setPJGLMresultMap(dataMap);
                break;
            case "SJRNGL": //审计人员管理
                dataMap = this.ymFormDataMapper.selectSJRNGLMapById(fromId);
                resultMap = this.setJRNGLresultMap(dataMap);
                break;
            case "SJQZD": //审计取证单
                dataMap = this.ymFormDataMapper.selectSJQZDMapById(fromId);
                resultMap = this.setSJQZDresultMap(dataMap);
                break;
            case "ZGLS": //整改落实
                dataMap = this.ymFormDataMapper.selectZGLSMapById(fromId);
                resultMap = this.setZGLSresultMap(dataMap);
                break;
            case "SJJYK": //审计经验库
                dataMap = this.ymFormDataMapper.selectSJJYKMapById(fromId);
                resultMap = this.setSJJYKresultMap(dataMap);
                break;
            case "ZXGL": //执行管理
                dataMap = this.ymFormDataMapper.selectZXGLMapById(fromId);
                resultMap = this.setZXGLresultMap(dataMap);
                break;
            case "SSHZS": //诉讼或再审
                dataMap = this.ymFormDataMapper.selectSSHZSMapById(fromId);
                resultMap = this.setSSHZSresultMap(dataMap);
                break;
            case "DAJY": //档案借阅
                dataMap = this.ymFormDataMapper.selectDAJYMapById(fromId);
                resultMap = this.setDAJYresultMap(dataMap);
                break;
            case "HTYJ": //合同移交
                dataMap = this.ymFormDataMapper.selectHTYJMapById(fromId);
                resultMap = this.setHTYJresultMap(dataMap);
                break;
            case "RYQJD": //人员请假单
                dataMap = this.ymFormDataMapper.selectRYQJDById(fromId);
                resultMap = this.setRYQJDresultMap(dataMap);
                break;
            case "YXSYD": //印信使用单
                dataMap = this.ymFormDataMapper.selectYXSYDById(fromId);
                resultMap = this.setYXSYDresultMap(dataMap);
                break;
            case "ZCTJSQ": //资产调剂申请
                dataMap = this.ymFormDataMapper.selectZCTJSQById(fromId);
                resultMap = this.setZCTJSQresultMap(dataMap);
                break;
            case "BGJYZC": //办公经费支出
                dataMap = this.ymFormDataMapper.selectBGJYZCById(fromId);
                resultMap = this.setBGJYZCresultMap(dataMap);
                break;
            case "XLFZC": //修理费支出
                dataMap = this.ymFormDataMapper.selectXLFZCById(fromId);
                resultMap = this.setXLFZCresultMap(dataMap);
                break;

            //风险管控BEGIN
            case "FXCJ": //风险创建
                dataMap = this.ymFormDataMapper.selectFXCJMapById(fromId);
                resultMap = this.setFXCJresultMap(dataMap);
                break;
            case "PGJH": //评估计划
                dataMap = this.ymFormDataMapper.selectPGJHMapById(fromId);
                resultMap = this.setPGJHresultMap(dataMap);
                break;
            case "FXYD": //风险应对
                dataMap = this.ymFormDataMapper.selectFXYDMapById(fromId);
                resultMap = this.setFXYDresultMap(dataMap);
                break;
            case "FXSJK": //风险事件库
                dataMap = this.ymFormDataMapper.selectFXSJKMapById(fromId);
                resultMap = this.setFXSJKresultMap(dataMap);
                break;
            case "FXBG": //风险报告
                dataMap = this.ymFormDataMapper.selectFXBGMapById(fromId);
                resultMap = this.setFXBGresultMap(dataMap);
                break;
            case "FXSCTZ": //风险审查台账
                dataMap = this.ymFormDataMapper.selectFXSCTZMapById(fromId);
                resultMap = this.setFXSCTZresultMap(dataMap);
                break;
            case "FXGL": //风险管理报告
                dataMap = this.ymFormDataMapper.selectFXGLMapById(fromId);
                resultMap = this.setFXGLresultMap(dataMap);
                break;

            //内控管理BEGIN
            case "PJLX": //评价立项
                dataMap = this.ymFormDataMapper.selectPJLXMapById(fromId);
                resultMap = this.setPJLXresultMap(dataMap);
                break;
            case "PJBG": //评价报告编制
                dataMap = this.ymFormDataMapper.selectPJBGMapById(fromId);
                resultMap = this.setPJBGresultMap(dataMap);
                break;
            case "WTFX": //测试任务-问题发现
                dataMap = this.ymFormDataMapper.selectWTFXMapById(fromId);
//    			Map<String, Object> mapBMFZR = this.ymFormDataMapper.selectBMFZRByOrgId("%"+(BigDecimal)this.getMapValue(dataMap,"MAINORG")+"%");
//    			String bmfzrid = this.getMapValue(mapBMFZR,"BMFZRID").toString();
//    			dataMap.put("MAINORG", bmfzrid);
                resultMap = this.setWTFXresultMap(dataMap);
                break;

            //违规追责BEGIN
            case "WGHS": //违规核实
                dataMap = this.ymFormDataMapper.selectWGHSMapById(fromId);
                resultMap = this.setWGHSresultMap(dataMap);
                break;
            case "SHBG": //审核报告
                dataMap = this.ymFormDataMapper.selectSHBGMapById(fromId);
                resultMap = this.setSHBGresultMap(dataMap);
                break;
            case "WGHC": //违规核查
                dataMap = this.ymFormDataMapper.selectWGHCMapById(fromId);
                resultMap = this.setWGHCresultMap(dataMap);
                break;
            case "WGYS": //违规移送
                dataMap = this.ymFormDataMapper.selectWGYSMapById(fromId);
                resultMap = this.setWGYSresultMap(dataMap);
                break;

            //合规管理BEGIN
            case "HGBGB": //合规报告表
                dataMap = this.ymFormDataMapper.selectHGBGBMapById(fromId);
                resultMap = this.setHGBGBresultMap(dataMap);
                break;
            case "HGSCGL": //合规手册管理
                dataMap = this.ymFormDataMapper.selectHGSCGLMapById(fromId);
                resultMap = this.setHGSCGLresultMap(dataMap);
                break;
            case "HGJHSPB": //合规计划审批
                dataMap = this.ymFormDataMapper.selectHGJHSPBMapById(fromId);
                resultMap = this.setHGJHSPBresultMap(dataMap);
                break;
            case "ZDGWHGZR": //重点岗位合规责任
                dataMap = this.ymFormDataMapper.selectZDGWHGZRMapById(fromId);
                resultMap = this.setZDGWHGZRresultMap(dataMap);
                break;
            case "HGGLYXXGL": //合规管理员信息管理
                dataMap = this.ymFormDataMapper.selectHGGLYXXGLMapById(fromId);
                resultMap = this.setHGGLYXXGLresultMap(dataMap);
                break;
            case "HGGLJCFA": //合规管理检查方案
                dataMap = this.ymFormDataMapper.selectHGGLJCFAMapById(fromId);
                resultMap = this.setHGGLJCFALresultMap(dataMap);
                break;
            case "HGGLJCSS": //合规管理检查实施
                dataMap = this.ymFormDataMapper.selectHGGLJCSSMapById(fromId);
                resultMap = this.setHGGLJCSSLresultMap(dataMap);
                break;
            case "HGGLWTZG": //合规管理问题整改
                dataMap = this.ymFormDataMapper.selectHGGLWTZGMapById(fromId);
                resultMap = this.setHGGLWTZGLresultMap(dataMap);
                break;
            case "HGGLFXSJTZ": //合规管理风险事件台账
                dataMap = this.ymFormDataMapper.selectHGGLFXSJTZMapById(fromId);
                resultMap = this.setHGGLFXSJTZLresultMap(dataMap);
                break;

            //计划编制
            case "XQJYB": //需求建议表流程
                dataMap = this.ymFormDataMapper.selectXQJYBMapById(fromId);
                resultMap = this.setXQJYBresultMap(dataMap);
                break;
            case "FWXQB": //服务需求表流程
                dataMap = this.ymFormDataMapper.selectFWXQBMapById(fromId);
                resultMap = this.setFWXQBresultMap(dataMap);
                break;
            case "LXJYB": //立项建议表流程
                dataMap = this.ymFormDataMapper.selectLXJYBMapById(fromId);
                resultMap = this.setLXJYBCresultMap(dataMap);
                break;
            case "FGLDHZ": //分管领导汇总流程
                dataMap = this.ymFormDataMapper.selectFGLDHZMapById(fromId);
                resultMap = this.setFGLDHZresultMap(dataMap);
                break;
            case "GCXMZJ": //工程项目造价流程:
                dataMap = this.ymFormDataMapper.selectGCXMZJMapById(fromId);
                resultMap = this.setGCXMZJresultMap(dataMap);
                break;
            case "GCXMZJZJB": //工程项目造价中间表流程
                dataMap = this.ymFormDataMapper.selectGCXMZJZJBMapById(fromId);
                resultMap = this.setGCXMZJZJBresultMap(dataMap);
                break;
            case "GCJGYSJH"://工程竣工验收计划流程
                dataMap = this.ymFormDataMapper.selectGCJGYSJHMapById(fromId);
                resultMap = this.setGCJGYSJHresultMap(dataMap);
                break;
            case "JHCG": //计划草稿流程
                dataMap = this.ymFormDataMapper.selectJHCGById(fromId);
                resultMap = this.setJHCGMap(dataMap);
                break;
            case "JHCHUG"://计划初稿流程
                dataMap = this.ymFormDataMapper.selectJHCHUGById(fromId);
                resultMap = this.setJHCHUGMap(dataMap);
                break;
            case "JH"://计划流程流程
                dataMap = this.ymFormDataMapper.selectJHById(fromId);
                resultMap = this.setJHMap(dataMap);
                break;
            case "JHXQ": //计划需求流程
                dataMap = this.ymFormDataMapper.selectJHXQById(fromId);
                resultMap = this.setJHXQMap(dataMap);
                break;
            case "JSXMJBQK": //建设项目基本情况
                dataMap = this.ymFormDataMapper.selectJSXMJBQKById(fromId);
                resultMap = this.setJSXMJBQKMap(dataMap);
                break;
            case "JSXMTZWCQK": //建设项目投资完成情况汇总
                dataMap = this.ymFormDataMapper.selectJSXMTZWCQKById(fromId);
                resultMap = this.setJSXMTZWCQKMap(dataMap);
                break;
            case "SJDWLRSJ"://三级单位离任审计
                dataMap = this.ymFormDataMapper.selectSJDWLRSJById(fromId);
                resultMap = this.setSJDWLRSJMap(dataMap);
                break;
            case "EJDWJCYDWLRSJ"://二级单位及成员单位离任审计
                dataMap = this.ymFormDataMapper.selectEJDWJCYDWLRSJById(fromId);
                resultMap = this.setEJDWJCYDWLRSJMap(dataMap);
                break;
            case "EJJGRZLXJY"://二级机构任中审计
                dataMap = this.ymFormDataMapper.selectEJJGRZLXJYById(fromId);
                resultMap = this.setEJJGRZLXJYMap(dataMap);
                break;
            case "WWTJYJLR"://未委托及预计离任
                dataMap = this.ymFormDataMapper.selectWWTJYJLRById(fromId);
                resultMap = this.setWWTJYJLRMap(dataMap);
                break;
            case "RZSJMX"://任中审计明细
                dataMap = this.ymFormDataMapper.selectRZSJMXById(fromId);
                resultMap = this.setRZSJMXMap(dataMap);
                break;
            case "GCZXPXB"://工程专项排序表
                dataMap = this.ymFormDataMapper.selectGCZXPXBById(fromId);
                resultMap = this.setGCZXPXBMap(dataMap);
                break;
            case "CWZXPXB"://财务专项排序表
                dataMap = this.ymFormDataMapper.selectCWZXPXBById(fromId);
                resultMap = this.setCWZXPXBMap(dataMap);
                break;
            case"GCSJXMAP"://工程审计项目安排
                dataMap = this.ymFormDataMapper.selectGCSJXMAPById(fromId);
                resultMap = this.setGCSJXMAPCresultMap(dataMap);
                break;
            case"CWSJXMAP"://财务审计项目安排
                dataMap = this.ymFormDataMapper.selectCWSJXMAPById(fromId);
                resultMap = this.setCWSJXMAPCresultMap(dataMap);
                break;
            case "ZLFXBG": //质量分析报告
                dataMap = this.ymFormDataMapper.selectZLFXBGById(fromId);
                resultMap = this.setZLFXBGCresultMap(dataMap);
                break;
            case "TZBG": //通知变更
                dataMap = this.ymFormDataMapper.selectTZBGById(fromId);
                resultMap = this.setTZBGCresultMap(dataMap);
                break;
            case "SJTZ": //审计通知
                dataMap = this.ymFormDataMapper.selectSJTZById(fromId);
                resultMap = this.setSJTZCresultMap(dataMap);
                break;
            case "XMYQSQ": //项目延期申请
                dataMap = this.ymFormDataMapper.selectXMYQSQById(fromId);
                resultMap = this.setXMYQSQMap(dataMap);
                break;
            case "LXJYZYPG": //立项建议专业评估
                dataMap = this.ymFormDataMapper.selectLXJYZYPGById(fromId);
                resultMap = this.setLXJYZYPGMap(dataMap);
                break;
            case "RZMXTB": //任中明细填报
                dataMap = this.ymFormDataMapper.selectRZMXTBById(fromId);
                resultMap = this.setRZMXTBMap(dataMap);
                break;
            case "YQNSXMGD": //项目归档
                dataMap = this.ymFormDataMapper.selectYQNSXMGDById(fromId);
                resultMap = this.setYQNSXMGDMap(dataMap);
                break;
            case "YQNSDAJY": //档案借阅
                dataMap = this.ymFormDataMapper.selectYQNSDAJYById(fromId);
                resultMap = this.setYQNSDAJYMap(dataMap);
                break;
            case "XMPYSBFZ": //项目评优申报分组
                dataMap = this.ymFormDataMapper.selectXMPYSBFZById(fromId);
                resultMap = this.setXMPYSBFZMap(dataMap);
                break;
            case "XMPYSB": //项目评优申报
                dataMap = this.ymFormDataMapper.selectXMPYSBById(fromId);
                resultMap = this.setXMPYSBMap(dataMap);
                break;
            case "JHBA": //计划备案
                dataMap = this.ymFormDataMapper.selectJHBAById(fromId);
                resultMap = this.setJHBAMap(dataMap);
                break;
            case "SJTZSP": //审计通知审批
                dataMap = this.ymFormDataMapper.selectSJTZSPById(fromId);
                resultMap = this.setSJTZSPMap(dataMap);
                break;
                
            case "LLYJSB": //理论研究上报
                dataMap = this.ymFormDataMapper.selectLLYJSBById(fromId);
                resultMap = this.setLLYJSBMap(dataMap);
                break;
            case "LWPX": //论文排序
                dataMap = this.ymFormDataMapper.selectLWPXById(fromId);
                resultMap = this.setLWPXMap(dataMap);
                break;
            case "SJJYS": //审计意见书
                dataMap = this.ymFormDataMapper.selectSJJYSById(fromId);
                resultMap = this.setSJJYSMap(dataMap);
                break;
            case "WTQD": //问题清单
                dataMap = this.ymFormDataMapper.selectWTQDById(fromId);
                resultMap = this.setWTQDMap(dataMap);
                break;
            case "GZHF": //跟踪回访
                dataMap = this.ymFormDataMapper.selectGZHFById(fromId);
                resultMap = this.setGZHFMap(dataMap);
                break;
            case "WTZG": //问题整改
                dataMap = this.ymFormDataMapper.selectWTZGById(fromId);
                resultMap = this.setWTZGMap(dataMap);
                break;
                
            case "YGLQSP": //员工离庆审批
                dataMap = this.ymFormDataMapper.selectYGLQSPById(fromId);
                resultMap = this.setYGLQSPMap(dataMap);
                break;
            case "ZB": //周报
                dataMap = this.ymFormDataMapper.selectZBById(fromId);
                resultMap = this.setZBMap(dataMap);
                break;
            case "HXZGSP": //后续整改审批
                dataMap = this.ymFormDataMapper.selectHXZGSPById(fromId);
                resultMap = this.setHXZGSPMap(dataMap);
                break;
            case "SJLXJYTZ": //审计立项建议通知审批
            	 dataMap = this.ymFormDataMapper.selectSJLXJYTZById(fromId);
                 resultMap = this.setSJLXJYTZMap(dataMap);
            	break;
                
            //综合管理
            case "WWDLFWGL": //外网代理服务管理
                dataMap = this.ymFormDataMapper.selectWWDLFWGLById(fromId);
                resultMap = this.setWWDLFWGLMap(dataMap);
                break;
            case "HYSQ": //会议申请
                dataMap = this.ymFormDataMapper.selectHYSQById(fromId);
                resultMap = this.setHYSQMap(dataMap);
                break;
            case "HYGL": //会议管理
                dataMap = this.ymFormDataMapper.selectHYGLById(fromId);
                resultMap = this.setHYGLMap(dataMap);
                break;
            case "IPDZGL": //IP地址管理
                dataMap = this.ymFormDataMapper.selectIPDZGLById(fromId);
                resultMap = this.setIPDZGLMap(dataMap);
                break;
            case "NBWZSQD": //内部网站申请单流程
                dataMap = this.ymFormDataMapper.selectNBWZSQDById(fromId);
                resultMap = this.setNBWZSQDMap(dataMap);
                break;
            case "WPSQD": //外派申请单流程
                dataMap = this.ymFormDataMapper.selectWPSQDById(fromId);
                resultMap = this.setWPSQDMap(dataMap);
                break;
            case "XJDSP": //销假单流程
                dataMap = this.ymFormDataMapper.selectXJDById(fromId);
                resultMap = this.setXJDMap(dataMap);
                break;
            case "DBTZD": //督办通知单流程
                dataMap = this.ymFormDataMapper.selectDBTZDById(fromId);
                resultMap = this.setDBTZDMap(dataMap);
                break;
            case "ZSYYXGL": //中石油邮箱管理流程
                dataMap = this.ymFormDataMapper.selectZSYYXGLById(fromId);
                resultMap = this.setZSYYXGLMap(dataMap);
                break;
            case "SZZSGL": //数字证书管理流程
                dataMap = this.ymFormDataMapper.selectSZZSGLById(fromId);
                resultMap = this.setSZZSGLMap(dataMap);
                break;
            case "VPNZHGL": //VPN账号管理流程
                dataMap = this.ymFormDataMapper.selectVPNZHGLById(fromId);
                resultMap = this.setVPNZHGLMap(dataMap);
                break;
            case "NBWJCB": //内部文件呈报流程
                dataMap = this.ymFormDataMapper.selectNBWJCBById(fromId);
                resultMap = this.setNBWJCBMap(dataMap);
                break;
            case "QXSQ": //权限申请流程
                dataMap = this.ymFormDataMapper.selectQXSQById(fromId);
                resultMap = this.setQXSQMap(dataMap);
                break;
            case "GWJD": //公务接待
                dataMap = this.ymFormDataMapper.selectGWJDById(fromId);
                resultMap = this.setGWJDMap(dataMap);
                break;

            case "SJSSWDDG": //审计实施我的底稿
                dataMap = this.ymFormDataMapper.selectSJSSWDDGById(fromId);
                resultMap = this.setSJSSWDDGMap(dataMap);
                break;
            case "SJJGQRS": //审计结果确认单
                dataMap = this.ymFormDataMapper.selectSJJGQRSById(fromId);
                resultMap = this.setSJJGQRSMap(dataMap);
                break;
            case "SJXMZK": //审计项目追款
                dataMap = this.ymFormDataMapper.selectSJXMZKById(fromId);
                resultMap = this.setSJXMZKMap(dataMap);
                break;
            case "SJGZJL": //审计工作记录
                dataMap = this.ymFormDataMapper.selectSJGZJLById(fromId);
                resultMap = this.setSJGZJLMap(dataMap);
                break;
            case "SJDDJL": //审计督导记录
                dataMap = this.ymFormDataMapper.selectSJDDJLById(fromId);
                resultMap = this.setSJDDJLMap(dataMap);
                break;
            case "SJDDBG": //审计督导报告
                dataMap = this.ymFormDataMapper.selectSJDDBGById(fromId);
                resultMap = this.setSJDDBGMap(dataMap);
                break;
            case "SJXMQKB": //审计项目情况表
                dataMap = this.ymFormDataMapper.selectSJXMQKBById(fromId);
                resultMap = this.setSJXMQKBMap(dataMap);
                break;
            case "XCSCZYNR": //现场审查主要内容
                dataMap = this.ymFormDataMapper.selectXCSCZYNRById(fromId);
                resultMap = this.setXCSCZYNRMap(dataMap);
                break;

            //项目管理
            case "JHBZ": //计划编制
                dataMap = this.ymFormDataMapper.selectJHBZById(fromId);
                resultMap = this.setJHBZCresultMap(dataMap);
                break;
            case "SQDCBG"://审前调查报告
                dataMap = this.ymFormDataMapper.selectSQDCBGById(fromId);
                resultMap = this.setSQDCBGCresultMap(dataMap);
                break;
            case "GZFA"://工作方案 
                dataMap = this.ymFormDataMapper.selectGZFAById(fromId);
                resultMap = this.setGZFACresultMap(dataMap);
                break;
            case "SSFA"://实施方案 
                dataMap = this.ymFormDataMapper.selectSSFAById(fromId);
                resultMap = this.setSSFACresultMap(dataMap);
                break;
            case "SJXMB"://审计项目表
                dataMap = this.ymFormDataMapper.selectSJXMBById(fromId);
                resultMap = this.setSJXMBCresultMap(dataMap);
                break;
            case "SJXMZD"://审计项目制度
                dataMap = this.ymFormDataMapper.selectSJXMZDById(fromId);
                resultMap = this.setSJXMZDCresultMap(dataMap);
                break;

            //审计报告
            case "JHYJG"://交换意见搞
                dataMap = this.ymFormDataMapper.selectJHYJGById(fromId);
                resultMap = this.setJHYJGCresultMap(dataMap);
                break;
            case "SLBG"://审理报告
                dataMap = this.ymFormDataMapper.selectSLBGById(fromId);
                resultMap = this.setSLBGCresultMap(dataMap);
                break;
            case "SJBGDG"://审计报告定稿
                dataMap = this.ymFormDataMapper.selectSJBGDGById(fromId);
                resultMap = this.setSJBGDGCresultMap(dataMap);
                break;
                
            case "YSJGWS"://移送结果文书
                dataMap = this.ymFormDataMapper.selectYSJGWSById(fromId);
                resultMap = this.setYSJGWSCresultMap(dataMap);
                break;
            case "WTXSHC"://问题线索核查
                dataMap = this.ymFormDataMapper.selectWTXSHCById(fromId);
                resultMap = this.setWTXSHCCresultMap(dataMap);
                break;
            case "WTXSDZ"://问题线索定责
                dataMap = this.ymFormDataMapper.selectWTXSDZById(fromId);
                resultMap = this.setWTXSDZCresultMap(dataMap);
                break;
            case "WTXSSL"://问题线索受理
                dataMap = this.ymFormDataMapper.selectWTXSSLById(fromId);
                resultMap = this.setWTXSSLCresultMap(dataMap);
                break;
            case "CLJG"://处理结果
                dataMap = this.ymFormDataMapper.selectCLJGById(fromId);
                resultMap = this.setCLJGCresultMap(dataMap);
                break;
            case "WPXJD"://外派销假单
                dataMap = this.ymFormDataMapper.selectWPXJDById(fromId);
                resultMap = this.setWPXJDCresultMap(dataMap);
                break;
              //整改追责
            case "ZGBG": //整改报告
                dataMap = this.ymFormDataMapper.selectZGBGMapById(fromId);
//                if("1".equals(dataMap.get("REPORTTYPE"))){
//                	dataMap.put("REPORTTYPE", "整改方案报告");
//                }else {
//                	dataMap.put("REPORTTYPE", "整改落实报告");
//                }
                //判断是否为审计部
                Integer deptid = loginStaff.getLinkDetp().getOrgid().intValue();
                if(deptid == 793993) {
                	dataMap.put("isAudit", "是");
                }else {
                	dataMap.put("isAudit", "否");
                }
                resultMap = this.setZGBGresultMap(dataMap);
                break;
            case "ZGFA": //整改方案
                dataMap = this.ymFormDataMapper.selectZGFAMapById(fromId);
                Object planType = dataMap.get("PLANTYPE");
                if ("1".equals(planType.toString())) {
                	dataMap.put("PLANTYPE", "审计");
                } else if ("2".equals(planType.toString())){
                    dataMap.put("PLANTYPE", "内控");
                } else {
                    dataMap.put("PLANTYPE", "外部");
                }
                String pkymOrgId = this.tblOrganizationMapper.selectPkYmIdByStaffId(dataMap.get("HANDLERID").toString());
                dataMap.put("HANDLERDEPT", this.getBuMenFuZeRenByPkStaffId(pkymOrgId));
                dataMap.put("HANDLERFGLD", this.getZhiShuZhuGuanByPkStaffId(dataMap.get("HANDLERID").toString()));
                resultMap = this.setZGFAresultMap(dataMap);
                break;
            case "ZGZZLS": //整改落实 ZCZGLS
                dataMap = this.ymFormDataMapper.selectZCZGLSMapById(fromId);
                Object issuesType = dataMap.get("ISSUESTYPE");
                if ("1".equals(issuesType.toString())) {
                	dataMap.put("ISSUESTYPE", "审计");
                } else if ("2".equals(issuesType.toString())){
                    dataMap.put("ISSUESTYPE", "内控");
                } else {
                    dataMap.put("ISSUESTYPE", "外部");
                }
                resultMap = this.setZCZGLSresultMap(dataMap);
                break;
            case "ZGPJ": //整改评价
                dataMap = this.ymFormDataMapper.selectZGPJMapById(fromId);
                Object resultStatus = dataMap.get("RESULTSTATUS");
                if ("4".equals(resultStatus.toString())) {
                	dataMap.put("RESULTSTATUS", "关闭");
                } else if ("2".equals(resultStatus.toString())){
                    dataMap.put("RESULTSTATUS", "已整改到位");
                } else if ("3".equals(resultStatus.toString())){
                    dataMap.put("RESULTSTATUS", "已整改未到位");
                } {
                    dataMap.put("RESULTSTATUS", "未整改");
                }
                resultMap = this.setZGPJresultMap(dataMap);
                break;
            case "ZDFXYDPG"://重大风险月度评估
                dataMap = this.ymFormDataMapper.selectZDFXYDPGById(fromId);
                resultMap = this.setZDFXYDPGresultMap(dataMap);
                break;
            case "YDPGFXGB"://月度评估风险关闭
                dataMap = this.ymFormDataMapper.selectYDPGFXGBById(fromId);
                resultMap = this.setYDPGFXGBresultMap(dataMap);
                break;
            case "SJJGWSCS"://审计结果文书初审
                dataMap = this.ymFormDataMapper.selectSJJGWSCSById(fromId);
                resultMap = this.setSJJGWSCSresultMap(dataMap);
                break;
            case "NKQXGL"://内控缺陷管理
                dataMap = this.ymFormDataMapper.selectNKQXGLById(fromId);
                resultMap = this.setNKQXGLresultMap(dataMap);
                break;
            case "ZDFXCJSP"://重大风险创建
                dataMap = this.ymFormDataMapper.selectZDFXCJById(fromId);
                resultMap = this.setZDFXCJresultMap(dataMap);
                break;
            case "ZDFXTBSP"://重大风险填报
                dataMap = this.ymFormDataMapper.selectZDFXTBById(fromId);
                resultMap = this.setZDFXTBresultMap(dataMap);
                break;
            case "HTJY"://合同借阅
                dataMap = this.ymFormDataMapper.selectHTJYById(fromId);
                resultMap = this.setHTJYresultMap(dataMap);
                break;
            case "JTNKCSJH"://集团内控测试计划
                dataMap = this.ymFormDataMapper.selectJTNKCSJHById(fromId);
                resultMap = this.setJTNKCSJHresultMap(dataMap);
                break;
            case "FXJCZBCJ"://风险监测指标创建
                dataMap = this.ymFormDataMapper.selectFXJCZBCJById(fromId);
                resultMap = this.setFXJCZBCJresultMap(dataMap);
                break;
            case "FXJCZBTB"://风险监测指标填报
                dataMap = this.ymFormDataMapper.selectFXJCZBTBById(fromId);
                resultMap = this.setFXJCZBTBresultMap(dataMap);
                break;
            case "XTXGQR": //系统组织用户菜单 确认流程
                dataMap = this.ymFormDataMapper.selectAuthorizationDataMapById(fromId);
                resultMap = this.setAuthorizationDataMap(dataMap);
                break;
            default:
                break;
        }
        return resultMap;
    }

	/**
     * 通过传入流程平台用户的主键   获取直属主管的主键信息
     * @param pkYmStaffId  -- 流程平台用户的主键
     * @return
     * @throws Exception
     */
    public String getZhiShuZhuGuanByPkStaffId(String pkYmStaffId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_ManagerId FROM base_user WHERE F_Id = '"+pkYmStaffId+"'");
			rs = ps.executeQuery();
			while (rs.next()) {
				fid = rs.getString("F_ManagerId");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}


	/**
     * 通过传入流程平台部门的主键   获取部门负责人的主键信息
     * @param pkYmOrgId  --流程平台部门的主键
     * @return
     * @throws Exception
     */
    public String getBuMenFuZeRenByPkStaffId(String pkYmOrgId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_ManagerId FROM base_organize WHERE F_Id = '"+pkYmOrgId+"'");
			rs = ps.executeQuery();
			while (rs.next()) {
				fid = rs.getString("F_ManagerId");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}

    private HashMap<String, Object> setAuthorizationDataMap(Map<String, Object> dataMap) {
    	HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("RECORDTEXT", this.getMapValue(dataMap, "RECORDTEXT"));
        return resultMap;
  	}

	private HashMap<String, Object> setZGPJresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("inspectionProcess", this.getMapValue(dataMap, "INSPECTIONPROCESS"));
        resultMap.put("resultStatus", this.getMapValue(dataMap, "RESULTSTATUS"));
        return resultMap;
    }

	private HashMap<String, Object> setZCZGLSresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("rectificationMeasures", this.getMapValue(dataMap, "RECTIFICATIONMEASURES"));
        resultMap.put("achivement", this.getMapValue(dataMap, "ACHIVEMENT"));
        resultMap.put("conclusion", this.getMapValue(dataMap, "CONCLUSION"));
        resultMap.put("issuesType", this.getMapValue(dataMap, "ISSUESTYPE"));
        resultMap.put("isxh", this.getMapValue(dataMap, "ISXH"));
        return resultMap;
    }

	private HashMap<String, Object> setZGFAresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("planCode", this.getMapValue(dataMap, "PLANCODE"));
        resultMap.put("planType", this.getMapValue(dataMap, "PLANTYPE"));
        resultMap.put("handlerId", this.getMapValue(dataMap, "HANDLERID"));
        resultMap.put("planName", this.getMapValue(dataMap, "PLANNAME"));
        resultMap.put("handlerDept", this.getMapValue(dataMap, "HANDLERDEPT"));
        resultMap.put("handlerFgld", this.getMapValue(dataMap, "HANDLERFGLD"));
        return resultMap;
    }


	private HashMap<String, Object> setZGBGresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("reportcode", this.getMapValue(dataMap, "REPORTCODE"));
        resultMap.put("reportname", this.getMapValue(dataMap, "REPORTNAME"));
        resultMap.put("reporttype", this.getMapValue(dataMap, "REPORTTYPE"));
        resultMap.put("isAudit", this.getMapValue(dataMap, "isAudit"));
        return resultMap;
    }
	


	private HashMap<String, Object> setSJLXJYTZMap(Map<String, Object> dataMap) {
    	 HashMap<String, Object> resultMap = new HashMap<>();
         resultMap.put("NAME", this.getMapValue(dataMap, "NAME"));
         return resultMap;
	}


	private HashMap<String, Object> setSJBGDGCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("DOCUMENT", this.getMapValue(dataMap, "DOCUMENT"));
        resultMap.put("TITLE", this.getMapValue(dataMap, "TITLE"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("SJBGDGID", this.getMapValue(dataMap, "SJBGDGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setYSJGWSCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("WSTITLE", this.getMapValue(dataMap, "WSTITLE"));
        return resultMap;
    }
    
    private HashMap<String, Object> setWTXSHCCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("HCNAME", this.getMapValue(dataMap, "HCNAME"));
        return resultMap;
    }
    
    private HashMap<String, Object> setWTXSDZCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("DZNAME", this.getMapValue(dataMap, "DZNAME"));
        return resultMap;
    }
    
    private HashMap<String, Object> setWTXSSLCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("SLNAME", this.getMapValue(dataMap, "SLNAME"));
        return resultMap;
    }
    
    private HashMap<String, Object> setCLJGCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setWPXJDCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        return resultMap;
    }
    
    private HashMap<String, Object> setZDFXYDPGresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setYDPGFXGBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("RISKID", this.getMapValue(dataMap, "RISKID"));
        resultMap.put("CLOSESTATUS", this.getMapValue(dataMap, "CLOSESTATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setSJJGWSCSresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("REPORTID", this.getMapValue(dataMap, "REPORTID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setNKQXGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("BUGID", this.getMapValue(dataMap, "BUGID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setZDFXCJresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setZDFXTBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setHTJYresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("LENDID", this.getMapValue(dataMap, "LENDID"));
        resultMap.put("LENDDATE", this.getMapValue(dataMap, "LENDDATE"));
        resultMap.put("RETURNDATE", this.getMapValue(dataMap, "RETURNDATE"));
        resultMap.put("MEMO", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("LENDSTATUS", this.getMapValue(dataMap, "LENDSTATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setJTNKCSJHresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setFXJCZBCJresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    private HashMap<String, Object> setFXJCZBTBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    

    private HashMap<String, Object> setSLBGCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("DOCUMENT", this.getMapValue(dataMap, "DOCUMENT"));
        resultMap.put("TITLE", this.getMapValue(dataMap, "TITLE"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("SLBGID", this.getMapValue(dataMap, "SLBGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("DEPUTY_DIRECTOR", this.getMapValue(dataMap, "DEPUTY_DIRECTOR"));
        resultMap.put("SECTION_CHIEF", this.getMapValue(dataMap, "SECTION_CHIEF"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("PROJECT_ID", this.getMapValue(dataMap, "PROJECT_ID"));
        return resultMap;

    }

    private HashMap<String, Object> setJHYJGCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("TITLE", this.getMapValue(dataMap, "TITLE"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("JHYJGID", this.getMapValue(dataMap, "JHYJGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("PROJECT_ID", this.getMapValue(dataMap, "PROJECT_ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("zzid", this.getMapValue(dataMap, "ZZID"));
        resultMap.put("zsid", this.getMapValue(dataMap, "ZSID"));
        return resultMap;
    }

    private HashMap<String, Object> setTZBGCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("CHANGEID", this.getMapValue(dataMap, "CHANGEID"));
        resultMap.put("ADVICEID", this.getMapValue(dataMap, "ADVICEID"));
        resultMap.put("PROGECTID", this.getMapValue(dataMap, "PROGECTID"));
        resultMap.put("CHANGETHING", this.getMapValue(dataMap, "CHANGETHING"));
        resultMap.put("CHANGEBEFORE", this.getMapValue(dataMap, "CHANGEBEFORE"));
        resultMap.put("CHANGEAFTER", this.getMapValue(dataMap, "CHANGEAFTER"));
        resultMap.put("CHANGEREASON", this.getMapValue(dataMap, "CHANGEREASON"));
        resultMap.put("CHANGETIME", this.getMapValue(dataMap, "CHANGETIME"));
        resultMap.put("JBR", this.getMapValue(dataMap, "JBR"));
        resultMap.put("CREATESTAFFID", this.getMapValue(dataMap, "CREATESTAFFID"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("CREATRTIME", this.getMapValue(dataMap, "CREATRTIME"));
        resultMap.put("zzid", this.getMapValue(dataMap, "PKYMSTAFFID"));
        resultMap.put("notename", this.getMapValue(dataMap, "ADVICENAME"));
        return resultMap;
    }
    
    private HashMap<String, Object> setSJTZCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ADVICEID", this.getMapValue(dataMap, "ADVICEID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("zzid", this.getMapValue(dataMap, "PKYMSTAFFID"));
        resultMap.put("projectname", this.getMapValue(dataMap, "ADVICENAME"));
        return resultMap;
    }
    
    private HashMap<String, Object> setXMYQSQMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("XMDQID", this.getMapValue(dataMap, "XMDQID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("zzid", this.getMapValue(dataMap, "PKYMSTAFFID"));
        resultMap.put("projectname", this.getMapValue(dataMap, "XMNAME"));
        return resultMap;
    }
    
    private HashMap<String, Object> setLXJYZYPGMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("TBID", this.getMapValue(dataMap, "TBID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setRZMXTBMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("TBID", this.getMapValue(dataMap, "TBID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setYQNSXMGDMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("GDSTATUS", this.getMapValue(dataMap, "GDSTATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setYQNSDAJYMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("BORROWID", this.getMapValue(dataMap, "BORROWID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setXMPYSBFZMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        return resultMap;
    }
    
    private HashMap<String, Object> setXMPYSBMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        return resultMap;
    }
    
    private HashMap<String, Object> setJHBAMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("PLANFILINGID", this.getMapValue(dataMap, "PLANFILINGID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setSJTZSPMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ADVICEID", this.getMapValue(dataMap, "ADVICEID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("projectname", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("zzid", this.getMapValue(dataMap, "PKYMSTAFFID"));
        return resultMap;
    }
    
    private HashMap<String, Object> setLLYJSBMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("TBID", this.getMapValue(dataMap, "TBID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setLWPXMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("TBID", this.getMapValue(dataMap, "TBID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setSJJYSMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("BGID", this.getMapValue(dataMap, "BGID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("CODE", this.getMapValue(dataMap, "CODE"));
        resultMap.put("BGNAME", this.getMapValue(dataMap, "BGNAME"));
        return resultMap;
    }
    
    private HashMap<String, Object> setWTQDMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("ISSUENUMBER", this.getMapValue(dataMap, "ISSUENUMBER"));
        resultMap.put("WTSJGLCJ", this.getMapValue(dataMap, "WTSJGLCJ"));
        resultMap.put("ASSETLOSS", this.getMapValue(dataMap, "ASSETLOSS"));
        resultMap.put("RISKLEVEL", this.getMapValue(dataMap, "RISKLEVEL"));
        resultMap.put("SJBGDGTITLE", this.getMapValue(dataMap, "SJBGDGTITLE"));
        resultMap.put("MONEY", this.getMapValue(dataMap, "MONEY"));
        resultMap.put("REVIEWMONEY", this.getMapValue(dataMap, "REVIEWMONEY"));
        return resultMap;
    }
    
    private HashMap<String, Object> setGZHFMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("WTZGID", this.getMapValue(dataMap, "WTZGID"));
        resultMap.put("HFSPSTATUS", this.getMapValue(dataMap, "HFSPSTATUS"));
        resultMap.put("DQZGJE", this.getMapValue(dataMap, "DQZGJE"));
        resultMap.put("ISSUENUMBER", this.getMapValue(dataMap, "ISSUENUMBER"));
        return resultMap;
    }
    
    private HashMap<String, Object> setWTZGMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("WTZGID", this.getMapValue(dataMap, "WTZGID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("DQZGJE", this.getMapValue(dataMap, "DQZGJE"));
        resultMap.put("ISSUENUMBER", this.getMapValue(dataMap, "ISSUENUMBER"));
        return resultMap;
    }
    
    private HashMap<String, Object> setYGLQSPMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("PERSONNELNATURE", this.getMapValue(dataMap, "PERSONNELNATURE"));
        resultMap.put("HEALTHSITUATION", this.getMapValue(dataMap, "HEALTHSITUATION"));
        resultMap.put("VEHICLE", this.getMapValue(dataMap, "VEHICLE"));
        resultMap.put("REALNAME", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("COMMITDEPTNAME", this.getMapValue(dataMap, "COMMITDEPTNAME"));
        return resultMap;
    }
    
    private HashMap<String, Object> setZBMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("WORKUNITNAME", this.getMapValue(dataMap, "WORKUNITNAME"));
        resultMap.put("FLAGSUMMARY", this.getMapValue(dataMap, "FLAGSUMMARY"));
        return resultMap;
    }
    
    private HashMap<String, Object> setHXZGSPMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("WTZGID", this.getMapValue(dataMap, "WTZGID"));
        resultMap.put("HXSPSTATUS", this.getMapValue(dataMap, "HXSPSTATUS"));
        resultMap.put("DQZGJE", this.getMapValue(dataMap, "DQZGJE"));
        resultMap.put("ISSUENUMBER", this.getMapValue(dataMap, "ISSUENUMBER"));
        return resultMap;
    }
    
    
    
    
    private HashMap<String, Object> setZLFXBGCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("DOCUMENTNUMBER", this.getMapValue(dataMap, "DOCUMENTNUMBER"));
        resultMap.put("TITLE", this.getMapValue(dataMap, "TITLE"));
        resultMap.put("CREATEUSER", this.getMapValue(dataMap, "CREATEUSER"));
        resultMap.put("CREATETIME", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("UPDATEUSER", this.getMapValue(dataMap, "UPDATEUSER"));
        resultMap.put("UPDATETIME", this.getMapValue(dataMap, "UPDATETIME"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setCWSJXMAPCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("CODE", this.getMapValue(dataMap, "CODE"));
        resultMap.put("BATC", this.getMapValue(dataMap, "BATC"));
        return resultMap;
    }

    private HashMap<String, Object> setGCSJXMAPCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("CODE", this.getMapValue(dataMap, "CODE"));
        resultMap.put("BATC", this.getMapValue(dataMap, "BATC"));
        return resultMap;
    }

    private HashMap<String, Object> setCWZXPXBMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("SUGGEST_DEPT", this.getMapValue(dataMap, "SUGGEST_DEPT"));
        resultMap.put("SORT", this.getMapValue(dataMap, "SORT"));
        resultMap.put("PROJECT_NAME", this.getMapValue(dataMap, "PROJECT_NAME"));
        resultMap.put("PROJECT_PURPOSE", this.getMapValue(dataMap, "PROJECT_PURPOSE"));
        resultMap.put("CONCERNS_CONTENT", this.getMapValue(dataMap, "CONCERNS_CONTENT"));
        resultMap.put("UNIT_RANGE", this.getMapValue(dataMap, "UNIT_RANGE"));
        resultMap.put("TIME_RANGE", this.getMapValue(dataMap, "TIME_RANGE"));
        resultMap.put("CREATE_USER", this.getMapValue(dataMap, "CREATE_USER"));
        resultMap.put("CREATE_TIME", this.getMapValue(dataMap, "CREATE_TIME"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setGCZXPXBMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("SUGGEST_DEPT_ID", this.getMapValue(dataMap, "SUGGEST_DEPT_ID"));
        resultMap.put("SORT", this.getMapValue(dataMap, "SORT"));
        resultMap.put("PROJECT_NAME", this.getMapValue(dataMap, "PROJECT_NAME"));
        resultMap.put("PROJECT_PURPOSE", this.getMapValue(dataMap, "PROJECT_PURPOSE"));
        resultMap.put("CONCERNS_CONTENT", this.getMapValue(dataMap, "CONCERNS_CONTENT"));
        resultMap.put("UNIT_RANGE", this.getMapValue(dataMap, "UNIT_RANGE"));
        resultMap.put("TIME_RANGE", this.getMapValue(dataMap, "TIME_RANGE"));
        resultMap.put("CREATE_USER", this.getMapValue(dataMap, "CREATE_USER"));
        resultMap.put("CREATE_TIME", this.getMapValue(dataMap, "CREATE_TIME"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setRZSJMXMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        return resultMap;
    }

    private HashMap<String, Object> setWWTJYJLRMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("NAME", this.getMapValue(dataMap, "NAME"));
        resultMap.put("RETIRE_TIME", this.getMapValue(dataMap, "RETIRE_TIME"));
        resultMap.put("AUDIT_TIME", this.getMapValue(dataMap, "AUDIT_TIME"));
        resultMap.put("PROJECT_NAME", this.getMapValue(dataMap, "PROJECT_NAME"));
        resultMap.put("WORK_START_TIME", this.getMapValue(dataMap, "WORK_START_TIME"));
        resultMap.put("WORK_END_TIME", this.getMapValue(dataMap, "WORK_END_TIME"));
        resultMap.put("DO_AUDIT_TIME", this.getMapValue(dataMap, "DO_AUDIT_TIME"));
        resultMap.put("TEAM_LEADER_ID", this.getMapValue(dataMap, "TEAM_LEADER_ID"));
        resultMap.put("LEADER_ID", this.getMapValue(dataMap, "LEADER_ID"));
        resultMap.put("CHIEF_REVIEWER_ID", this.getMapValue(dataMap, "CHIEF_REVIEWER_ID"));
        resultMap.put("DEPUTY_REVIEWER_ID", this.getMapValue(dataMap, "DEPUTY_REVIEWER_ID"));
        resultMap.put("CREATE_USER", this.getMapValue(dataMap, "CREATE_USER"));
        resultMap.put("CREATE_TIME", this.getMapValue(dataMap, "CREATE_TIME"));
        resultMap.put("PERSON_IDS", this.getMapValue(dataMap, "PERSON_IDS"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setEJJGRZLXJYMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("ORG", this.getMapValue(dataMap, "ORG"));
        resultMap.put("NAME", this.getMapValue(dataMap, "NAME"));
        resultMap.put("JOB", this.getMapValue(dataMap, "JOB"));
        resultMap.put("LV", this.getMapValue(dataMap, "LV"));
        resultMap.put("WORK_START_TIME", this.getMapValue(dataMap, "WORK_START_TIME"));
        resultMap.put("WORK_END_TIME", this.getMapValue(dataMap, "WORK_END_TIME"));
        resultMap.put("WORK_DURATION", this.getMapValue(dataMap, "WORK_DURATION"));
        resultMap.put("HAS_FINANCE_PROBLEM", this.getMapValue(dataMap, "HAS_FINANCE_PROBLEM"));
        resultMap.put("HAS_ECONOMIC_PROBLEM", this.getMapValue(dataMap, "HAS_ECONOMIC_PROBLEM"));
        resultMap.put("HAS_BEEN_COMPLAIN", this.getMapValue(dataMap, "HAS_BEEN_COMPLAIN"));
        resultMap.put("IS_LEAVE_NEXT_YEAR", this.getMapValue(dataMap, "IS_LEAVE_NEXT_YEAR"));
        resultMap.put("NEED_AUDIT", this.getMapValue(dataMap, "NEED_AUDIT"));
        resultMap.put("CREATE_USER", this.getMapValue(dataMap, "CREATE_USER"));
        resultMap.put("CREATE_TIME", this.getMapValue(dataMap, "CREATE_TIME"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setEJDWJCYDWLRSJMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("PROJECT_NAME", this.getMapValue(dataMap, "PROJECT_NAME"));
        resultMap.put("AUDIT_ORG", this.getMapValue(dataMap, "AUDIT_ORG"));
        resultMap.put("ENTRUST_NO", this.getMapValue(dataMap, "ENTRUST_NO"));
        resultMap.put("ENTRUST_TIME", this.getMapValue(dataMap, "ENTRUST_TIME"));
        resultMap.put("AUDIT_START_TIME", this.getMapValue(dataMap, "AUDIT_START_TIME"));
        resultMap.put("AUDIT_END_TIME", this.getMapValue(dataMap, "AUDIT_END_TIME"));
        resultMap.put("CREATE_USER", this.getMapValue(dataMap, "CREATE_USER"));
        resultMap.put("CREATE_TIME", this.getMapValue(dataMap, "CREATE_TIME"));
        resultMap.put("PERSON_IDS", this.getMapValue(dataMap, "PERSON_IDS"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setSJDWLRSJMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("JDID", this.getMapValue(dataMap, "JDID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setJSXMTZWCQKMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("HZID", this.getMapValue(dataMap, "HZID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setJSXMJBQKMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("HZID", this.getMapValue(dataMap, "HZID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setSJXMZDCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("SJXMZDID", this.getMapValue(dataMap, "SJXMZDID"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("EXT1", this.getMapValue(dataMap, "EXT1"));
        resultMap.put("EXT2", this.getMapValue(dataMap, "EXT2"));
        resultMap.put("EXT3", this.getMapValue(dataMap, "EXT3"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("BZ", this.getMapValue(dataMap, "BZ"));
        resultMap.put("XMMC", this.getMapValue(dataMap, "XMMC"));
        resultMap.put("JHMC", this.getMapValue(dataMap, "JHMC"));
        resultMap.put("ZDMC", this.getMapValue(dataMap, "ZDMC"));
        resultMap.put("FWBNR", this.getMapValue(dataMap, "FWBNR"));
        return resultMap;
    }

    private HashMap<String, Object> setSJXMBCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("SJXMBID", this.getMapValue(dataMap, "SJXMBID"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("EXT1", this.getMapValue(dataMap, "EXT1"));
        resultMap.put("EXT2", this.getMapValue(dataMap, "EXT2"));
        resultMap.put("EXT3", this.getMapValue(dataMap, "EXT3"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("BZ", this.getMapValue(dataMap, "BZ"));
        resultMap.put("HTBH", this.getMapValue(dataMap, "HTBH"));
        resultMap.put("GCMC", this.getMapValue(dataMap, "GCMC"));
        resultMap.put("SGDW", this.getMapValue(dataMap, "SGDW"));
        resultMap.put("ESSCJE", this.getMapValue(dataMap, "ESSCJE"));
        resultMap.put("BCSJRY", this.getMapValue(dataMap, "BCSJRY"));
        resultMap.put("RYIDS", this.getMapValue(dataMap, "RYIDS"));
        return resultMap;
    }

    private HashMap<String, Object> setSSFACresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("PLAN_ID", this.getMapValue(dataMap, "PLAN_ID"));
        resultMap.put("PLAN_PROJECT_ID", this.getMapValue(dataMap, "PLAN_PROJECT_ID"));
        resultMap.put("PLAN_NAME", this.getMapValue(dataMap, "PLAN_NAME"));
        resultMap.put("PLAN_PROJECT_NAME", this.getMapValue(dataMap, "PLAN_PROJECT_NAME"));
        resultMap.put("PROJECT_NAME", this.getMapValue(dataMap, "PROJECT_NAME"));
        resultMap.put("PROJECT_TYPE", this.getMapValue(dataMap, "PROJECT_TYPE"));
        resultMap.put("AUDIT_ORG_ID", this.getMapValue(dataMap, "AUDIT_ORG_ID"));
        resultMap.put("PLAN_YEAR", this.getMapValue(dataMap, "PLAN_YEAR"));
        resultMap.put("PLAN_TIME", this.getMapValue(dataMap, "PLAN_TIME"));
        resultMap.put("PROJECT_SUMMARY", this.getMapValue(dataMap, "PROJECT_SUMMARY"));
        resultMap.put("PROJECT_ORDER_ID", this.getMapValue(dataMap, "PROJECT_ORDER_ID"));
        resultMap.put("PLAN_STARTTIME", this.getMapValue(dataMap, "PLAN_STARTTIME"));
        resultMap.put("PLAN_ENDTIME", this.getMapValue(dataMap, "PLAN_ENDTIME"));
        resultMap.put("AUDIT_METHOD", this.getMapValue(dataMap, "AUDIT_METHOD"));
        resultMap.put("COST_ESTIMATION", this.getMapValue(dataMap, "COST_ESTIMATION"));
        resultMap.put("IS_WW", this.getMapValue(dataMap, "IS_WW"));
        resultMap.put("TEMP_ID", this.getMapValue(dataMap, "TEMP_ID"));
        resultMap.put("IMPLEMENT_TYPE", this.getMapValue(dataMap, "IMPLEMENT_TYPE"));
        resultMap.put("DEPT_ID", this.getMapValue(dataMap, "DEPT_ID"));
        resultMap.put("AUDIT_REQUIREMENT", this.getMapValue(dataMap, "AUDIT_REQUIREMENT"));
        resultMap.put("IMPLEMENT_STEPS", this.getMapValue(dataMap, "IMPLEMENT_STEPS"));
        resultMap.put("AUDIT_REASON", this.getMapValue(dataMap, "AUDIT_REASON"));
        resultMap.put("AUDIT_CONTENT", this.getMapValue(dataMap, "AUDIT_CONTENT"));
        resultMap.put("AUDIT_PROCESS", this.getMapValue(dataMap, "AUDIT_PROCESS"));
        resultMap.put("AUDIT_RESULT_USE", this.getMapValue(dataMap, "AUDIT_RESULT_USE"));
        resultMap.put("OTHER_CONTENT", this.getMapValue(dataMap, "OTHER_CONTENT"));
        resultMap.put("PROJECT_ORDER_NAME", this.getMapValue(dataMap, "PROJECT_ORDER_NAME"));
        resultMap.put("AUDIT_ORG_NAME", this.getMapValue(dataMap, "AUDIT_ORG_NAME"));
        resultMap.put("TEMP_NAME", this.getMapValue(dataMap, "TEMP_NAME"));
        resultMap.put("DEPT_NAME", this.getMapValue(dataMap, "DEPT_NAME"));
        resultMap.put("PROJECTTEMP_ID", this.getMapValue(dataMap, "PROJECTTEMP_ID"));
        resultMap.put("PROJECTTEMP_NAME", this.getMapValue(dataMap, "PROJECTTEMP_NAME"));
        resultMap.put("SJLX_ID", this.getMapValue(dataMap, "SJLX_ID"));
        resultMap.put("SJLX_NAME", this.getMapValue(dataMap, "SJLX_NAME"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("zykstype", this.getMapValue(dataMap, "ZYKSTYPE"));
        resultMap.put("zzid", this.getMapValue(dataMap, "ZZID"));
        resultMap.put("zyksryid", this.getMapValue(dataMap, "ZYKSRYID"));
        resultMap.put("zsid", this.getMapValue(dataMap, "ZSSTAFFID"));
        return resultMap;
    }

    private HashMap<String, Object> setGZFACresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("GZFAID", this.getMapValue(dataMap, "GZFAID"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("EXT1", this.getMapValue(dataMap, "EXT1"));
        resultMap.put("EXT2", this.getMapValue(dataMap, "EXT2"));
        resultMap.put("EXT3", this.getMapValue(dataMap, "EXT3"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("BZ", this.getMapValue(dataMap, "BZ"));
        resultMap.put("XMMC", this.getMapValue(dataMap, "XMMC"));
        resultMap.put("JHMC", this.getMapValue(dataMap, "JHMC"));
        resultMap.put("RYIDS", this.getMapValue(dataMap, "RYIDS"));
        resultMap.put("falx", this.getMapValue(dataMap, "FALX"));
        return resultMap;
    }

    private HashMap<String, Object> setSQDCBGCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("SQDCBGID", this.getMapValue(dataMap, "SQDCBGID"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("EXT1", this.getMapValue(dataMap, "EXT1"));
        resultMap.put("EXT2", this.getMapValue(dataMap, "EXT2"));
        resultMap.put("EXT3", this.getMapValue(dataMap, "EXT3"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("BZ", this.getMapValue(dataMap, "BZ"));
        resultMap.put("XMMC", this.getMapValue(dataMap, "XMMC"));
        resultMap.put("JHMC", this.getMapValue(dataMap, "JHMC"));
        return resultMap;
    }

    private HashMap<String, Object> setJHBZCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("PLANID", this.getMapValue(dataMap, "PLANID"));
        resultMap.put("PLANCODE", this.getMapValue(dataMap, "PLANCODE"));
        resultMap.put("PLANNAME", this.getMapValue(dataMap, "PLANNAME"));
        resultMap.put("PALNYEAR", this.getMapValue(dataMap, "PALNYEAR"));
        resultMap.put("PLANTYPE", this.getMapValue(dataMap, "PLANTYPE"));
        resultMap.put("AUDITORGID", this.getMapValue(dataMap, "AUDITORGID"));
        resultMap.put("PALNCOST", this.getMapValue(dataMap, "PALNCOST"));
        resultMap.put("STARTTIME", this.getMapValue(dataMap, "STARTTIME"));
        resultMap.put("ENDTIME", this.getMapValue(dataMap, "ENDTIME"));
        resultMap.put("PRINCIPALID", this.getMapValue(dataMap, "PRINCIPALID"));
        resultMap.put("LEADERID", this.getMapValue(dataMap, "LEADERID"));
        resultMap.put("REMARKS", this.getMapValue(dataMap, "REMARKS"));
        resultMap.put("CREATESTAFFID", this.getMapValue(dataMap, "CREATESTAFFID"));
        resultMap.put("CREATETIME", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("UPDATETIMR", this.getMapValue(dataMap, "UPDATETIMR"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("OPINIONSTATUS", this.getMapValue(dataMap, "OPINIONSTATUS"));
        resultMap.put("ISAUDITOR", this.getMapValue(dataMap, "ISAUDITOR"));
        return resultMap;
    }

    private HashMap<String, Object> setXCSCZYNRMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("DELETED", this.getMapValue(dataMap, "DELETED"));
        resultMap.put("CREATETIME", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("UPDATETIME", this.getMapValue(dataMap, "UPDATETIME"));
        resultMap.put("SETTLEPROJECTNUM", this.getMapValue(dataMap, "SETTLEPROJECTNUM"));
        resultMap.put("PROJECTNAME", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("SETTLEAMOUNT", this.getMapValue(dataMap, "SETTLEAMOUNT"));
        resultMap.put("BUILDORGID", this.getMapValue(dataMap, "BUILDORGID"));
        resultMap.put("CONSTRUCTIONORGID", this.getMapValue(dataMap, "CONSTRUCTIONORGID"));
        resultMap.put("REVIEWCONTENT", this.getMapValue(dataMap, "REVIEWCONTENT"));
        resultMap.put("REVIEWSTAFFID", this.getMapValue(dataMap, "REVIEWSTAFFID"));
        resultMap.put("BUILDUNITMANAGEID", this.getMapValue(dataMap, "BUILDUNITMANAGEID"));
        resultMap.put("SCENEREVIEWTIME", this.getMapValue(dataMap, "SCENEREVIEWTIME"));
        resultMap.put("RECHECKSTAFFID", this.getMapValue(dataMap, "RECHECKSTAFFID"));
        resultMap.put("BUILDORGNAME", this.getMapValue(dataMap, "BUILDORGNAME"));
        resultMap.put("CONSTRUCTIONORGNAME", this.getMapValue(dataMap, "CONSTRUCTIONORGNAME"));
        resultMap.put("REVIEWSTAFFNAME", this.getMapValue(dataMap, "REVIEWSTAFFNAME"));
        resultMap.put("BUILDUNITMANAGENAME", this.getMapValue(dataMap, "BUILDUNITMANAGENAME"));
        resultMap.put("RECHECKSTAFFNAME", this.getMapValue(dataMap, "RECHECKSTAFFNAME"));
        resultMap.put("zzid", this.getMapValue(dataMap, "ZZID"));
        resultMap.put("zyksryid", this.getMapValue(dataMap, "ZYKSRYID"));
        return resultMap;
    }

    private HashMap<String, Object> setSJXMQKBMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("WEEKWORK", this.getMapValue(dataMap, "WEEKWORK"));
        resultMap.put("NEXTWEEKWORK", this.getMapValue(dataMap, "NEXTWEEKWORK"));
        resultMap.put("OTHERMATTERS", this.getMapValue(dataMap, "OTHERMATTERS"));
        resultMap.put("PROJECTNAME", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("zzid", this.getMapValue(dataMap, "ZZID"));
        resultMap.put("zyksryid", this.getMapValue(dataMap, "ZYKSRYID"));
        return resultMap;
    }

    private HashMap<String, Object> setSJDDBGMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("REPORTNAME", this.getMapValue(dataMap, "REPORTNAME"));
        resultMap.put("REPORTTIME", this.getMapValue(dataMap, "REPORTTIME"));
        resultMap.put("REPORTTYPE", this.getMapValue(dataMap, "REPORTTYPE"));
        resultMap.put("REPORTWAY", this.getMapValue(dataMap, "REPORTWAY"));
        resultMap.put("REPORTDEPT", this.getMapValue(dataMap, "REPORTDEPT"));
        resultMap.put("REPORTER", this.getMapValue(dataMap, "REPORTER"));
        resultMap.put("REPORTCONTENT", this.getMapValue(dataMap, "REPORTCONTENT"));
        resultMap.put("ANNEX", this.getMapValue(dataMap, "ANNEX"));
        resultMap.put("PROJECTID", this.getMapValue(dataMap, "PROJECTID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setSJDDJLMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("PROJECTNAME", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("CHIEFAUDITOR", this.getMapValue(dataMap, "CHIEFAUDITOR"));
        resultMap.put("SUPERVISIONPARTICIPANTS", this.getMapValue(dataMap, "SUPERVISIONPARTICIPANTS"));
        resultMap.put("SUPERVISIONDATE", this.getMapValue(dataMap, "SUPERVISIONDATE"));
        resultMap.put("ONSITECONDITION", this.getMapValue(dataMap, "ONSITECONDITION"));
        resultMap.put("ISSUESANDCOORDINATION", this.getMapValue(dataMap, "ISSUESANDCOORDINATION"));
        resultMap.put("SUPERVISIONOPINIONS", this.getMapValue(dataMap, "SUPERVISIONOPINIONS"));
        resultMap.put("CREATEUSER", this.getMapValue(dataMap, "CREATEUSER"));
        resultMap.put("CREATETIME", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("UPDATEUSER", this.getMapValue(dataMap, "UPDATEUSER"));
        resultMap.put("UPDATETIME", this.getMapValue(dataMap, "UPDATETIME"));
        resultMap.put("DELFLAG", this.getMapValue(dataMap, "DELFLAG"));
        resultMap.put("REMARKS", this.getMapValue(dataMap, "REMARKS"));
        resultMap.put("SUPERVISIONPARTICIPANTSID", this.getMapValue(dataMap, "SUPERVISIONPARTICIPANTSID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("zykstype", this.getMapValue(dataMap, "ZYKSTYPE"));
        return resultMap;
    }

    private HashMap<String, Object> setSJGZJLMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("AUDITEENAME", this.getMapValue(dataMap, "AUDITEENAME"));
        resultMap.put("IMPLEMENTATIONTIME", this.getMapValue(dataMap, "IMPLEMENTATIONTIME"));
        resultMap.put("PROJECTNAME", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("RESPONSIBLECONTENT", this.getMapValue(dataMap, "RESPONSIBLECONTENT"));
        resultMap.put("CONTENTOBJECTIVES", this.getMapValue(dataMap, "CONTENTOBJECTIVES"));
        resultMap.put("EXECUTEDPROCEDURESPROCESSES", this.getMapValue(dataMap, "EXECUTEDPROCEDURESPROCESSES"));
        resultMap.put("VERIFICATIONSITUATION", this.getMapValue(dataMap, "VERIFICATIONSITUATION"));
        resultMap.put("CLUESSOURCES", this.getMapValue(dataMap, "CLUESSOURCES"));
        resultMap.put("CREATEUSER", this.getMapValue(dataMap, "CREATEUSER"));
        resultMap.put("CREATETIME", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("UPDATEUSER", this.getMapValue(dataMap, "UPDATEUSER"));
        resultMap.put("UPDATETIME", this.getMapValue(dataMap, "UPDATETIME"));
        resultMap.put("DELFLAG", this.getMapValue(dataMap, "DELFLAG"));
        resultMap.put("AUDITEENAMEID", this.getMapValue(dataMap, "AUDITEENAMEID"));
        resultMap.put("zzid", this.getMapValue(dataMap, "ZZID"));
        resultMap.put("zsid", this.getMapValue(dataMap, "ZSID"));
        resultMap.put("zyksryid", this.getMapValue(dataMap, "ZYKSRYID"));
        resultMap.put("zykstype", this.getMapValue(dataMap, "ZYKSTYPE"));//项目类别
        return resultMap;
    }

    private HashMap<String, Object> setSJXMZKMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("RESULT_ID", this.getMapValue(dataMap, "RESULT_ID"));
        resultMap.put("PROJECT_NAME", this.getMapValue(dataMap, "PROJECT_NAME"));
        resultMap.put("AUDIT_ORG_ID", this.getMapValue(dataMap, "AUDIT_ORG_ID"));
        resultMap.put("REASON", this.getMapValue(dataMap, "REASON"));
        resultMap.put("MONEY", this.getMapValue(dataMap, "MONEY"));
        resultMap.put("ZK_ORG_ID", this.getMapValue(dataMap, "ZK_ORG_ID"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        return resultMap;
    }

    private HashMap<String, Object> setSJJGQRSMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULTID", this.getMapValue(dataMap, "RESULTID"));
        resultMap.put("RESULTCODE", this.getMapValue(dataMap, "RESULTCODE"));
        resultMap.put("CREATESTAFF", this.getMapValue(dataMap, "CREATESTAFF"));
        resultMap.put("CREATETIME", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("PROJECTID", this.getMapValue(dataMap, "PROJECTID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("ORGIDS", this.getMapValue(dataMap, "ORGIDS"));
        resultMap.put("ORGIDNAMES", this.getMapValue(dataMap, "ORGIDNAMES"));
        resultMap.put("CONTRACTCODE", this.getMapValue(dataMap, "CONTRACTCODE"));
        resultMap.put("CONTRACTNAME", this.getMapValue(dataMap, "CONTRACTNAME"));
        resultMap.put("SGORGID", this.getMapValue(dataMap, "SGORGID"));
        resultMap.put("SGORGNAME", this.getMapValue(dataMap, "SGORGNAME"));
        resultMap.put("CONTRACTMONEY", this.getMapValue(dataMap, "CONTRACTMONEY"));
        resultMap.put("HZMONEY", this.getMapValue(dataMap, "HZMONEY"));
        resultMap.put("HJMONEY", this.getMapValue(dataMap, "HJMONEY"));
        resultMap.put("SDMONEY", this.getMapValue(dataMap, "SDMONEY"));
        resultMap.put("PROJECTNAME", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("zzid", this.getMapValue(dataMap, "ZZID"));
        resultMap.put("zsid", this.getMapValue(dataMap, "ZSID"));
        resultMap.put("zyksryid", this.getMapValue(dataMap, "ZYKSRYID"));
        resultMap.put("served ", this.getMapValue(dataMap, "SERVED"));
        resultMap.put("zykstype", this.getMapValue(dataMap, "ZYKSTYPE"));//项目类别
        return resultMap;
    }

    private HashMap<String, Object> setSJSSWDDGMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        //专业科室人员 
        resultMap.put("PKYMSTAFFID", this.getMapValue(dataMap, "PKYMSTAFFID"));
        resultMap.put("PROJECTNAME", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("AUDITMATTERS", this.getMapValue(dataMap, "AUDITMATTERS"));
        resultMap.put("BASICWORK", this.getMapValue(dataMap, "BASICWORK"));
        resultMap.put("EVIDENCEACCURATE", this.getMapValue(dataMap, "EVIDENCEACCURATE"));
        resultMap.put("VERIFICATIONDESCRIPTION", this.getMapValue(dataMap, "DGVERIFICATIONDESCRIPTION"));
        resultMap.put("AUDITCONCLUSION", this.getMapValue(dataMap, "DGAUDITCONCLUSION"));
        resultMap.put("HANDLINGOPINIONS", this.getMapValue(dataMap, "DGHANDLINGOPINIONS"));
        resultMap.put("PROBLEMDRAFT", this.getMapValue(dataMap, "PROBLEMDRAFT"));
        resultMap.put("CREATEUSER", this.getMapValue(dataMap, "CREATEUSER"));
        resultMap.put("CREATETIME", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("UPDATEUSER", this.getMapValue(dataMap, "UPDATEUSER"));
        resultMap.put("UPDATETIME", this.getMapValue(dataMap, "UPDATETIME"));
        resultMap.put("AUDITEENAMEID", this.getMapValue(dataMap, "AUDITEENAMEID"));
        resultMap.put("TYPEID", this.getMapValue(dataMap, "TYPEID"));
        resultMap.put("TEMPLATEID", this.getMapValue(dataMap, "TEMPLATEID"));
        resultMap.put("DRAFTNUMBER", this.getMapValue(dataMap, "DRAFTNUMBER"));
        resultMap.put("SUMMARYDRAFTMARK", this.getMapValue(dataMap, "SUMMARYDRAFTMARK"));
        resultMap.put("zzid", this.getMapValue(dataMap, "ZZID"));//组长id
        resultMap.put("zsid", this.getMapValue(dataMap, "ZSID"));//主审id
        resultMap.put("zykstype", this.getMapValue(dataMap, "ZYKSTYPE"));//项目类别
        return resultMap;
    }

    private HashMap<String, Object> setJHXQMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("JHXQID", this.getMapValue(dataMap, "JHXQID"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("EXT1", this.getMapValue(dataMap, "EXT1"));
        resultMap.put("EXT2", this.getMapValue(dataMap, "EXT2"));
        resultMap.put("EXT3", this.getMapValue(dataMap, "EXT3"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("JHXQMC", this.getMapValue(dataMap, "JHXQMC"));
        resultMap.put("SJXMLX", this.getMapValue(dataMap, "SJXMLX"));
        resultMap.put("BZ", this.getMapValue(dataMap, "BZ"));
        resultMap.put("XMLXR", this.getMapValue(dataMap, "XMLXR"));
        resultMap.put("LXDH", this.getMapValue(dataMap, "LXDH"));
        resultMap.put("TBDW", this.getMapValue(dataMap, "TBDW"));
        resultMap.put("TBSJ", this.getMapValue(dataMap, "TBSJ"));
        resultMap.put("ZZFS", this.getMapValue(dataMap, "ZZFS"));
        resultMap.put("JYSJSSSJ", this.getMapValue(dataMap, "JYSJSSSJ"));
        resultMap.put("SJYJ", this.getMapValue(dataMap, "SJYJ"));
        resultMap.put("SJMB", this.getMapValue(dataMap, "SJMB"));
        resultMap.put("SJFW", this.getMapValue(dataMap, "SJFW"));
        resultMap.put("QTQKSM", this.getMapValue(dataMap, "QTQKSM"));
        resultMap.put("XMTZQD", this.getMapValue(dataMap, "XMTZQD"));
        resultMap.put("GSPFTZJE", this.getMapValue(dataMap, "GSPFTZJE"));
        resultMap.put("XYWCTZJE", this.getMapValue(dataMap, "XYWCTZJE"));
        resultMap.put("YJZTZJE", this.getMapValue(dataMap, "YJZTZJE"));
        resultMap.put("QZXTWDWCJE", this.getMapValue(dataMap, "QZXTWDWCJE"));
        resultMap.put("NJGYSSJ", this.getMapValue(dataMap, "NJGYSSJ"));
        resultMap.put("SYXHGSJ", this.getMapValue(dataMap, "SYXHGSJ"));
        resultMap.put("JSWCSJ", this.getMapValue(dataMap, "JSWCSJ"));
        resultMap.put("JUESWCSJ", this.getMapValue(dataMap, "JUESWCSJ"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setJHMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("JHID", this.getMapValue(dataMap, "JHID"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("EXT1", this.getMapValue(dataMap, "EXT1"));
        resultMap.put("EXT2", this.getMapValue(dataMap, "EXT2"));
        resultMap.put("EXT3", this.getMapValue(dataMap, "EXT3"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("JHMC", this.getMapValue(dataMap, "JHMC"));
        resultMap.put("SJ", this.getMapValue(dataMap, "SJ"));
        resultMap.put("BZ", this.getMapValue(dataMap, "BZ"));
        resultMap.put("LXDWID", this.getMapValue(dataMap, "LXDWID"));
        resultMap.put("LXDWMC", this.getMapValue(dataMap, "LXDWMC"));
        resultMap.put("BSJDWID", this.getMapValue(dataMap, "BSJDWID"));
        resultMap.put("BSJDWMC", this.getMapValue(dataMap, "BSJDWMC"));
        resultMap.put("SSLX", this.getMapValue(dataMap, "SSLX"));
        resultMap.put("JWXM", this.getMapValue(dataMap, "JWXM"));
        resultMap.put("SSSJJGID", this.getMapValue(dataMap, "SSSJJGID"));
        resultMap.put("XMND", this.getMapValue(dataMap, "XMND"));
        resultMap.put("XMFZCKSID", this.getMapValue(dataMap, "XMFZCKSID"));
        resultMap.put("XMFZCKSMC", this.getMapValue(dataMap, "XMFZCKSMC"));
        resultMap.put("JHSSYF", this.getMapValue(dataMap, "JHSSYF"));
        resultMap.put("SSXMLX", this.getMapValue(dataMap, "SSXMLX"));
        resultMap.put("LXYJ", this.getMapValue(dataMap, "LXYJ"));
        resultMap.put("SJXMMC", this.getMapValue(dataMap, "SJXMMC"));
        resultMap.put("JHLX", this.getMapValue(dataMap, "JHLX"));
        resultMap.put("JHTRRR", this.getMapValue(dataMap, "JHTRRR"));
        resultMap.put("SFDQBJYHDSJ", this.getMapValue(dataMap, "SFDQBJYHDSJ"));
        resultMap.put("BGYY", this.getMapValue(dataMap, "BGYY"));
        resultMap.put("SSSJJGMC", this.getMapValue(dataMap, "SSSJJGMC"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setJHCHUGMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("JHCHUGID", this.getMapValue(dataMap, "JHCHUGID"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("EXT1", this.getMapValue(dataMap, "EXT1"));
        resultMap.put("EXT2", this.getMapValue(dataMap, "EXT2"));
        resultMap.put("EXT3", this.getMapValue(dataMap, "EXT3"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("JHMC", this.getMapValue(dataMap, "JHMC"));
        resultMap.put("SJ", this.getMapValue(dataMap, "SJ"));
        resultMap.put("BZ", this.getMapValue(dataMap, "BZ"));
        resultMap.put("LXDWID", this.getMapValue(dataMap, "LXDWID"));
        resultMap.put("LXDWMC", this.getMapValue(dataMap, "LXDWMC"));
        resultMap.put("BSJDWID", this.getMapValue(dataMap, "BSJDWID"));
        resultMap.put("BSJDWMC", this.getMapValue(dataMap, "BSJDWMC"));
        resultMap.put("SSLX", this.getMapValue(dataMap, "SSLX"));
        resultMap.put("JWXM", this.getMapValue(dataMap, "JWXM"));
        resultMap.put("SSSJJGID", this.getMapValue(dataMap, "SSSJJGID"));
        resultMap.put("XMND", this.getMapValue(dataMap, "XMND"));
        resultMap.put("XMFZCKSID", this.getMapValue(dataMap, "XMFZCKSID"));
        resultMap.put("XMFZCKSMC", this.getMapValue(dataMap, "XMFZCKSMC"));
        resultMap.put("JHSSYF", this.getMapValue(dataMap, "JHSSYF"));
        resultMap.put("SSXMLX", this.getMapValue(dataMap, "SSXMLX"));
        resultMap.put("LXYJ", this.getMapValue(dataMap, "LXYJ"));
        resultMap.put("SJXMMC", this.getMapValue(dataMap, "SJXMMC"));
        resultMap.put("JHLX", this.getMapValue(dataMap, "JHLX"));
        resultMap.put("JHTRRR", this.getMapValue(dataMap, "JHTRRR"));
        resultMap.put("SFDQBJYHDSJ", this.getMapValue(dataMap, "SFDQBJYHDSJ"));
        resultMap.put("BGYY", this.getMapValue(dataMap, "BGYY"));
        resultMap.put("SSSJJGMC", this.getMapValue(dataMap, "SSSJJGMC"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setJHCGMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("JHCGID", this.getMapValue(dataMap, "JHCGID"));
        resultMap.put("CJR", this.getMapValue(dataMap, "CJR"));
        resultMap.put("CJSJ", this.getMapValue(dataMap, "CJSJ"));
        resultMap.put("EXT1", this.getMapValue(dataMap, "EXT1"));
        resultMap.put("EXT2", this.getMapValue(dataMap, "EXT2"));
        resultMap.put("EXT3", this.getMapValue(dataMap, "EXT3"));
        resultMap.put("ORGID", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("GXR", this.getMapValue(dataMap, "GXR"));
        resultMap.put("GXSJ", this.getMapValue(dataMap, "GXSJ"));
        resultMap.put("SPZT", this.getMapValue(dataMap, "SPZT"));
        resultMap.put("JHMC", this.getMapValue(dataMap, "JHMC"));
        resultMap.put("SJ", this.getMapValue(dataMap, "SJ"));
        resultMap.put("BZ", this.getMapValue(dataMap, "BZ"));
        resultMap.put("LXDWID", this.getMapValue(dataMap, "LXDWID"));
        resultMap.put("LXDWMC", this.getMapValue(dataMap, "LXDWMC"));
        resultMap.put("BSJDWID", this.getMapValue(dataMap, "BSJDWID"));
        resultMap.put("BSJDWMC", this.getMapValue(dataMap, "BSJDWMC"));
        resultMap.put("SSLX", this.getMapValue(dataMap, "SSLX"));
        resultMap.put("JWXM", this.getMapValue(dataMap, "JWXM"));
        resultMap.put("SSSJJGID", this.getMapValue(dataMap, "SSSJJGID"));
        resultMap.put("XMND", this.getMapValue(dataMap, "XMND"));
        resultMap.put("XMFZCKSID", this.getMapValue(dataMap, "XMFZCKSID"));
        resultMap.put("XMFZCKSMC", this.getMapValue(dataMap, "XMFZCKSMC"));
        resultMap.put("JHSSYF", this.getMapValue(dataMap, "JHSSYF"));
        resultMap.put("SSXMLX", this.getMapValue(dataMap, "SSXMLX"));
        resultMap.put("LXYJ", this.getMapValue(dataMap, "LXYJ"));
        resultMap.put("SJXMMC", this.getMapValue(dataMap, "SJXMMC"));
        resultMap.put("JHLX", this.getMapValue(dataMap, "JHLX"));
        resultMap.put("JHTRRR", this.getMapValue(dataMap, "JHTRRR"));
        resultMap.put("SFDQBJYHDSJ", this.getMapValue(dataMap, "SFDQBJYHDSJ"));
        resultMap.put("BGYY", this.getMapValue(dataMap, "BGYY"));
        resultMap.put("SSSJJGMC", this.getMapValue(dataMap, "SSSJJGMC"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("STATUSS", this.getMapValue(dataMap, "STATUSS"));
        return resultMap;
    }

    private HashMap<String, Object> setQXSQMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("APPLYID", this.getMapValue(dataMap, "APPLYID"));
        resultMap.put("APPLYWORKUNIT", this.getMapValue(dataMap, "APPLYWORKUNIT"));
        resultMap.put("APPLYROLE", this.getMapValue(dataMap, "APPLYROLE"));
        resultMap.put("APPLYTIME", this.getMapValue(dataMap, "APPLYTIME"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    
    private HashMap<String, Object> setGWJDMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("VISITORSREASON", this.getMapValue(dataMap, "VISITORSREASON"));
        resultMap.put("RECEIVERTYPE", this.getMapValue(dataMap, "RECEIVERTYPE"));
        return resultMap;
    }
    

    private HashMap<String, Object> setNBWJCBMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("SYMBOL", this.getMapValue(dataMap, "SYMBOL"));
        resultMap.put("INSIDEREPORTEDNAME", this.getMapValue(dataMap, "INSIDEREPORTEDNAME"));
        resultMap.put("FILEIDS", this.getMapValue(dataMap, "FILEIDS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setVPNZHGLMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STAFFID", this.getMapValue(dataMap, "STAFFID"));
        resultMap.put("REALNAME", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("MAIL", this.getMapValue(dataMap, "MAIL"));
        resultMap.put("MOBILEPHONE", this.getMapValue(dataMap, "MOBILEPHONE"));
        resultMap.put("STAFFNUMBER", this.getMapValue(dataMap, "STAFFNUMBER"));
        resultMap.put("SERVICETYPE", this.getMapValue(dataMap, "SERVICETYPE"));
        resultMap.put("APPLYTIME", this.getMapValue(dataMap, "APPLYTIME"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        resultMap.put("POST", this.getMapValue(dataMap, "POST"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setSZZSGLMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("SERVICETYPE", this.getMapValue(dataMap, "SERVICETYPE"));
        resultMap.put("USERNAME", this.getMapValue(dataMap, "USERNAME"));
        resultMap.put("EMAIL", this.getMapValue(dataMap, "EMAIL"));
        resultMap.put("STAFFCODE", this.getMapValue(dataMap, "STAFFCODE"));
        resultMap.put("ORGANIZATIONNAME", this.getMapValue(dataMap, "ORGANIZATIONNAME"));
        resultMap.put("ORGANIZATIONNUM", this.getMapValue(dataMap, "ORGANIZATIONNUM"));
        resultMap.put("CONTACTPHONE", this.getMapValue(dataMap, "CONTACTPHONE"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("ISAGENCY", this.getMapValue(dataMap, "ISAGENCY"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        resultMap.put("AGENCYNAME", this.getMapValue(dataMap, "AGENCYNAME"));
        resultMap.put("AGENCYBELONGGROUP", this.getMapValue(dataMap, "AGENCYBELONGGROUP"));
        resultMap.put("AGENCYMAIL", this.getMapValue(dataMap, "AGENCYMAIL"));
        resultMap.put("AGENCYCONTACTPHONE", this.getMapValue(dataMap, "AGENCYCONTACTPHONE"));
        return resultMap;
    }

    private HashMap<String, Object> setZSYYXGLMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("APPLYNAME", this.getMapValue(dataMap, "APPLYNAME"));
        resultMap.put("STAFFCODE", this.getMapValue(dataMap, "STAFFCODE"));
        /*resultMap.put("SERVICETYPE", this.getMapValue(dataMap, "SERVICETYPE"));
        resultMap.put("CONTACTPHONE", this.getMapValue(dataMap, "CONTACTPHONE"));
        resultMap.put("MAILSUFFIX", this.getMapValue(dataMap, "MAILSUFFIX"));
        resultMap.put("LOGINNAME", this.getMapValue(dataMap, "LOGINNAME"));
        resultMap.put("LOGINNAMEONE", this.getMapValue(dataMap, "LOGINNAMEONE"));
        resultMap.put("LOGINNAMETWO", this.getMapValue(dataMap, "LOGINNAMETWO"));
        resultMap.put("LOGINNAMETHREE", this.getMapValue(dataMap, "LOGINNAMETHREE"));
        resultMap.put("CHANGETYPE", this.getMapValue(dataMap, "CHANGETYPE"));
        resultMap.put("APPLYMAIL", this.getMapValue(dataMap, "APPLYMAIL"));
        resultMap.put("CHANGECONTENT", this.getMapValue(dataMap, "CHANGECONTENT"));
        resultMap.put("LOGOUTREASON", this.getMapValue(dataMap, "LOGOUTREASON"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        resultMap.put("APPLYBELONGGROUP", this.getMapValue(dataMap, "APPLYBELONGGROUP"));*/
        return resultMap;
    }

    private HashMap<String, Object> setDBTZDMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("NOTICENUMBER", this.getMapValue(dataMap, "NOTICENUMBER"));
        resultMap.put("NOTICENAME", this.getMapValue(dataMap, "NOTICENAME"));
        resultMap.put("NOTICETIME", this.getMapValue(dataMap, "NOTICETIME"));
        resultMap.put("SUPERVISIONWORKUNIT", this.getMapValue(dataMap, "SUPERVISIONWORKUNIT"));
        resultMap.put("TRANSACTOR", this.getMapValue(dataMap, "TRANSACTOR"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("FILEIDS", this.getMapValue(dataMap, "FILEIDS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setXJDMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("PEOPLE", this.getMapValue(dataMap, "PEOPLE"));
        resultMap.put("PEOPLEWORKUNIT", this.getMapValue(dataMap, "PEOPLEWORKUNIT"));
        resultMap.put("LEAVEDAYS", this.getMapValue(dataMap, "FILLFORMTIME"));
        resultMap.put("LEAVEREASON", this.getMapValue(dataMap, "CANCELHOLIDAYTYPE"));
        resultMap.put("REASONS", this.getMapValue(dataMap, "REASONS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        resultMap.put("REALNAME", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("COMMITDEPTNAME", this.getMapValue(dataMap, "COMMITDEPTNAME"));
        return resultMap;
    }

    private HashMap<String, Object> setWPSQDMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("APPLYPEOPLE", this.getMapValue(dataMap, "APPLYPEOPLE"));
        resultMap.put("APPLYWORKUNIT", this.getMapValue(dataMap, "APPLYWORKUNIT"));
        resultMap.put("POST", this.getMapValue(dataMap, "POST"));
        resultMap.put("APPLYEXPATRIATETIME", this.getMapValue(dataMap, "APPLYEXPATRIATETIME"));
        resultMap.put("APPLYRETURNTIME", this.getMapValue(dataMap, "APPLYRETURNTIME"));
        resultMap.put("ACTUALRETURNTIME", this.getMapValue(dataMap, "ACTUALRETURNTIME"));
        resultMap.put("EXPATRIATEREASON", this.getMapValue(dataMap, "EXPATRIATEREASON"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setNBWZSQDMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("INFOTITLE", this.getMapValue(dataMap, "INFOTITLE"));
/*        resultMap.put("INFOPROVIDERWORKUNIT", this.getMapValue(dataMap, "INFOPROVIDERWORKUNIT"));
        resultMap.put("PROVIDERPEOPLE", this.getMapValue(dataMap, "PROVIDERPEOPLE"));
        resultMap.put("CONTACT", this.getMapValue(dataMap, "CONTACT"));
        resultMap.put("FAX", this.getMapValue(dataMap, "FAX"));
        resultMap.put("MAILADDRESS", this.getMapValue(dataMap, "MAILADDRESS"));
        resultMap.put("INFORELEASECOLUMN", this.getMapValue(dataMap, "INFORELEASECOLUMN"));
        resultMap.put("EXPECTONLINETIME", this.getMapValue(dataMap, "EXPECTONLINETIME"));
        resultMap.put("FAILURETIME", this.getMapValue(dataMap, "FAILURETIME"));
        resultMap.put("CONTENTABSTRACT", this.getMapValue(dataMap, "CONTENTABSTRACT"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
*/        return resultMap;
    }

    private HashMap<String, Object> setIPDZGLMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("APPLYPEOPLE", this.getMapValue(dataMap, "APPLYPEOPLE"));
        resultMap.put("APPLYBELONGGROUP", this.getMapValue(dataMap, "APPLYBELONGGROUP"));
        /*resultMap.put("APPLYWORKUNIT", this.getMapValue(dataMap, "APPLYWORKUNIT"));
        resultMap.put("NETWORKINTERFACE", this.getMapValue(dataMap, "NETWORKINTERFACE"));
        resultMap.put("OFFICEAREA", this.getMapValue(dataMap, "OFFICEAREA"));
        resultMap.put("ROOMNUMBER", this.getMapValue(dataMap, "ROOMNUMBER"));
        resultMap.put("PURPOSE", this.getMapValue(dataMap, "PURPOSE"));
        resultMap.put("EQUIPMENTTYPE", this.getMapValue(dataMap, "EQUIPMENTTYPE"));
        resultMap.put("EXTERNALNETWORKPERMISSIONS", this.getMapValue(dataMap, "EXTERNALNETWORKPERMISSIONS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));*/
        return resultMap;
    }

    private HashMap<String, Object> setHYGLMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("CONFERENCENAME", this.getMapValue(dataMap, "CONFERENCENAME"));
        resultMap.put("CONFERENCECOMPERE", this.getMapValue(dataMap, "CONFERENCECOMPERE"));
        resultMap.put("ATTENDEES", this.getMapValue(dataMap, "ATTENDEES"));
        resultMap.put("CONFERENCEPLACE", this.getMapValue(dataMap, "CONFERENCEPLACE"));
        resultMap.put("CONFERENCEREQUIRE", this.getMapValue(dataMap, "CONFERENCEREQUIRE"));
        resultMap.put("CONFERENCECONTENT", this.getMapValue(dataMap, "CONFERENCECONTENT"));
        resultMap.put("FILEIDS", this.getMapValue(dataMap, "FILEIDS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        return resultMap;
    }

    private HashMap<String, Object> setHYSQMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("CONFERENCENAME", this.getMapValue(dataMap, "CONFERENCENAME"));
        /*resultMap.put("CONFERENCECOMPERE", this.getMapValue(dataMap, "CONFERENCECOMPERE"));
        resultMap.put("ATTENDEES", this.getMapValue(dataMap, "ATTENDEES"));
        resultMap.put("CONFERENCETIMESTART", this.getMapValue(dataMap, "CONFERENCETIMESTART"));
        resultMap.put("CONFERENCETIMEEND", this.getMapValue(dataMap, "CONFERENCETIMEEND"));
        resultMap.put("CONFERENCEPLACE", this.getMapValue(dataMap, "CONFERENCEPLACE"));
        resultMap.put("CONFERENCECONTENT", this.getMapValue(dataMap, "CONFERENCECONTENT"));
        resultMap.put("CONFERENCEREQUIRE", this.getMapValue(dataMap, "CONFERENCEREQUIRE"));
        resultMap.put("FILEIDS", this.getMapValue(dataMap, "FILEIDS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));*/
        return resultMap;
    }

    private HashMap<String, Object> setWWDLFWGLMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("IPADDRESS", this.getMapValue(dataMap, "IPADDRESS"));
        resultMap.put("MACADDRESS", this.getMapValue(dataMap, "MACADDRESS"));
        resultMap.put("DEPARTMENTSNAME", this.getMapValue(dataMap, "DEPARTMENTSNAME"));
        resultMap.put("USEPEOPLE", this.getMapValue(dataMap, "USEPEOPLE"));
        resultMap.put("CONTACTPHONE", this.getMapValue(dataMap, "CONTACTPHONE"));
        resultMap.put("MAILADDRESS", this.getMapValue(dataMap, "MAILADDRESS"));
        resultMap.put("ANTIVIRUSSOFTWARE", this.getMapValue(dataMap, "ANTIVIRUSSOFTWARE"));
        resultMap.put("ANTIVIRUSMANUFACTURER", this.getMapValue(dataMap, "ANTIVIRUSMANUFACTURER"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        return resultMap;
    }

    private HashMap<String, Object> setGCJGYSJHresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("", this.getMapValue(dataMap, ""));
        return resultMap;
    }

    private HashMap<String, Object> setGCXMZJZJBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("", this.getMapValue(dataMap, ""));
        return resultMap;
    }

    private HashMap<String, Object> setGCXMZJresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("", this.getMapValue(dataMap, ""));
        return resultMap;
    }

    private HashMap<String, Object> setFGLDHZresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("", this.getMapValue(dataMap, ""));
        return resultMap;
    }

    private HashMap<String, Object> setLXJYBCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("", this.getMapValue(dataMap, ""));
        return resultMap;
    }


    private HashMap<String, Object> setFWXQBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("AUDIT_ITEM", this.getMapValue(dataMap, "AUDIT_ITEM"));
        resultMap.put("AUDIT_PURPOSE", this.getMapValue(dataMap, "AUDIT_PURPOSE"));
        resultMap.put("UNIT_ID", this.getMapValue(dataMap, "UNIT_ID"));
        resultMap.put("CONCERNS_CONTENT", this.getMapValue(dataMap, "CONCERNS_CONTENT"));
        resultMap.put("UNIT_RANGE", this.getMapValue(dataMap, "UNIT_RANGE"));
        resultMap.put("TIME_RANGE", this.getMapValue(dataMap, "TIME_RANGE"));
        resultMap.put("PROJECT_TYPE", this.getMapValue(dataMap, "PROJECT_TYPE"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("CREATE_USER", this.getMapValue(dataMap, "CREATE_USER"));
        resultMap.put("CREATE_TIME", this.getMapValue(dataMap, "CREATE_TIME"));
        resultMap.put("PERSON_IDS", this.getMapValue(dataMap, "PERSON_IDS"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }

    private HashMap<String, Object> setXQJYBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        return resultMap;
    }



    private HashMap<String, Object> setXLFZCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("REPAIRBELONGGROUP", this.getMapValue(dataMap, "REPAIRBELONGGROUP"));
        resultMap.put("VIN", this.getMapValue(dataMap, "VIN"));
        resultMap.put("REPAIRTIME", this.getMapValue(dataMap, "REPAIRTIME"));
        resultMap.put("AMOUNT", this.getMapValue(dataMap, "AMOUNT"));
        resultMap.put("REPAIRMANUFACTURER", this.getMapValue(dataMap, "REPAIRMANUFACTURER"));
        resultMap.put("TRANSACTOR", this.getMapValue(dataMap, "TRANSACTOR"));
        resultMap.put("FAULTCONDITION", this.getMapValue(dataMap, "FAULTCONDITION"));
        resultMap.put("REPAIRPROJECT", this.getMapValue(dataMap, "REPAIRPROJECT"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        resultMap.put("BELONGGROUPNAME", this.getMapValue(dataMap, "BELONGGROUPNAME"));
        return resultMap;
    }

    private HashMap<String, Object> setBGJYZCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("REASON", this.getMapValue(dataMap, "REASON"));
        resultMap.put("DETAILS", this.getMapValue(dataMap, "DETAILS"));
        resultMap.put("AMOUNT", this.getMapValue(dataMap, "AMOUNT"));
        resultMap.put("SUPPLYBELONGGROUP", this.getMapValue(dataMap, "SUPPLYBELONGGROUP"));
        resultMap.put("OFFICEEXPENSESTIME", this.getMapValue(dataMap, "OFFICEEXPENSESTIME"));
        resultMap.put("TRANSACTOR", this.getMapValue(dataMap, "TRANSACTOR"));
        resultMap.put("DEPARTMENTHEAD", this.getMapValue(dataMap, "DEPARTMENTHEAD"));
        resultMap.put("FILEIDS", this.getMapValue(dataMap, "FILEIDS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("BELONGGROUPNAME", this.getMapValue(dataMap, "BELONGGROUPNAME"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setZCTJSQresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("ASSETMGTID", this.getMapValue(dataMap, "ASSETMGTID"));
        resultMap.put("QUANTITY", this.getMapValue(dataMap, "QUANTITY"));
        resultMap.put("USEAGETIME", this.getMapValue(dataMap, "USEAGETIME"));
        resultMap.put("UTILITYAGETIME", this.getMapValue(dataMap, "UTILITYAGETIME"));
        resultMap.put("USEBELONGGROUP", this.getMapValue(dataMap, "USEBELONGGROUP"));
        resultMap.put("ORIGINALBELONGGROUP", this.getMapValue(dataMap, "ORIGINALBELONGGROUP"));
        resultMap.put("ORIGINALUSEBELONGGROUP", this.getMapValue(dataMap, "ORIGINALUSEBELONGGROUP"));
        resultMap.put("STAYADJUSTEDBELONGGROUP", this.getMapValue(dataMap, "STAYADJUSTEDBELONGGROUP"));
        resultMap.put("ORIGINALVALUE", this.getMapValue(dataMap, "ORIGINALVALUE"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("FILEIDS", this.getMapValue(dataMap, "FILEIDS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setYXSYDresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("SEALNAME", this.getMapValue(dataMap, "SEALNAME"));
        resultMap.put("SEALREASONS", this.getMapValue(dataMap, "SEALREASONS"));
        resultMap.put("SEALNUM", this.getMapValue(dataMap, "SEALNUM"));
        resultMap.put("SEALWORKUNIT", this.getMapValue(dataMap, "SEALWORKUNIT"));
        resultMap.put("TRANSACTOR", this.getMapValue(dataMap, "TRANSACTOR"));
        resultMap.put("AUDITWORKUNIT", this.getMapValue(dataMap, "AUDITWORKUNIT"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("FILEIDS", this.getMapValue(dataMap, "FILEIDS"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setRYQJDresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("STAFFID", this.getMapValue(dataMap, "STAFFID"));
        resultMap.put("STAFFNUMBER", this.getMapValue(dataMap, "STAFFNUMBER"));
        resultMap.put("LEAVEPERIODTIMESTART", this.getMapValue(dataMap, "LEAVEPERIODTIMESTART"));
        resultMap.put("LEAVEPERIODTIMEEND", this.getMapValue(dataMap, "LEAVEPERIODTIMEEND"));
        resultMap.put("LEAVEREASON", this.getMapValue(dataMap, "LEAVEREASON"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        resultMap.put("REALNAME", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("COMMITDEPTNAME", this.getMapValue(dataMap, "COMMITDEPTNAME"));
        return resultMap;
    }

    private HashMap<String, Object> setNDKHresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("annualExamineId", this.getMapValue(dataMap, "ANNUALEXAMINEID"));
        resultMap.put("annualExamineName", this.getMapValue(dataMap, "ANNUALEXAMINENAME"));
        resultMap.put("type", this.getMapValue(dataMap, "TYPE"));
        resultMap.put("examineTime", this.getMapValue(dataMap, "EXAMINETIME"));
        resultMap.put("annualExamineCreator", this.getMapValue(dataMap, "ANNUALEXAMINECREATOR"));
        return resultMap;
    }

    private HashMap<String, Object> setHTYJresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("tranId", this.getMapValue(dataMap, "TRANID"));
        resultMap.put("legalMemo", this.getMapValue(dataMap, "LEGALMEMO"));
        resultMap.put("tranStatus", this.getMapValue(dataMap, "TRANSTATUS"));
        resultMap.put("contractName", this.getMapValue(dataMap, "CONTRACTNAME"));
        resultMap.put("contractNo", this.getMapValue(dataMap, "CONTRACTNO"));
        resultMap.put("contractCnt", this.getMapValue(dataMap, "CONTRACTCNT"));
        resultMap.put("contractId", this.getMapValue(dataMap, "CONTRACTID"));
        resultMap.put("handStaffName", this.getMapValue(dataMap, "HANDSTAFFNAME"));
        resultMap.put("tranOrgName", this.getMapValue(dataMap, "TRANORGNAME"));
        resultMap.put("handDeptName", this.getMapValue(dataMap, "HANDDEPTNAME"));
        return resultMap;
    }


    private HashMap<String, Object> setPFJHresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("planname", this.getMapValue(dataMap, "POPULARIZELAWPLANNAME"));
        resultMap.put("planyear", this.getMapValue(dataMap, "PLANYEAR"));
        resultMap.put("createtime", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("creator", this.getMapValue(dataMap, "CREATOR"));
        return resultMap;
    }


    private HashMap<String, Object> setDAJYresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("status", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("borrowid", this.getMapValue(dataMap, "BORROWID"));
        resultMap.put("memo", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("createDate", this.getMapValue(dataMap, "CREATEDATE"));
        resultMap.put("returnDate", this.getMapValue(dataMap, "RETURNDATE"));
        resultMap.put("projectId", this.getMapValue(dataMap, "PROJECTID"));
        resultMap.put("projectName", this.getMapValue(dataMap, "PRJOECTNAME"));
        resultMap.put("projectCode", this.getMapValue(dataMap, "PROJECTCODE"));
        return resultMap;
    }


    private HashMap<String, Object> setSSHZSresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("proceedid", this.getMapValue(dataMap, "PROCEEDID"));
        resultMap.put("proceedno", this.getMapValue(dataMap, "PROCEEDNO"));
        resultMap.put("porceedstage", this.getMapValue(dataMap, "PORCEEDSTAGE"));
        resultMap.put("court", this.getMapValue(dataMap, "COURT"));
        resultMap.put("courtlink", this.getMapValue(dataMap, "COURTLINK"));
        resultMap.put("courtcontact", this.getMapValue(dataMap, "COURTCONTACT"));
        resultMap.put("filingtime", this.getMapValue(dataMap, "FILINGTIME"));
        resultMap.put("paymentremindtime", this.getMapValue(dataMap, "PAYMENTREMINDTIME"));
        resultMap.put("openingtime", this.getMapValue(dataMap, "OPENINGTIME"));
        resultMap.put("judgetiem", this.getMapValue(dataMap, "JUDGETIEM"));
        resultMap.put("isexternallawyer", this.getMapValue(dataMap, "ISEXTERNALLAWYER"));
        resultMap.put("createtime", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("casepromotion", this.getMapValue(dataMap, "CASEPROMOTION"));
        resultMap.put("existingdifficulties", this.getMapValue(dataMap, "EXISTINGDIFFICULTIES"));
        resultMap.put("realname", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("measurespromote", this.getMapValue(dataMap, "MEASURESPROMOTE"));
        return resultMap;
    }


    private HashMap<String, Object> setZXGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("disputename", this.getMapValue(dataMap, "DISPUTENAME"));
        resultMap.put("execuno", this.getMapValue(dataMap, "EXECUNO"));
        resultMap.put("execucourt", this.getMapValue(dataMap, "EXECUCOURT"));
        resultMap.put("executype", this.getMapValue(dataMap, "EXECUTYPE"));
        resultMap.put("execudamount", this.getMapValue(dataMap, "EXECUEDAMOUNT"));
        resultMap.put("noexecuamount", this.getMapValue(dataMap, "NOEXECUAMOUNT"));
        resultMap.put("litigationid", this.getMapValue(dataMap, "LITIGATIONID"));
        resultMap.put("atbitraid", this.getMapValue(dataMap, "ARBITRAID"));
        resultMap.put("createtime", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("createstaffid", this.getMapValue(dataMap, "CREATESTAFFID"));
        resultMap.put("username", this.getMapValue(dataMap, "USERNAME"));
        return resultMap;
    }


    private HashMap<String, Object> setJRNGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("realname", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("miblephone", this.getMapValue(dataMap, "MIBLEPHONE"));
        resultMap.put("birthday", this.getMapValue(dataMap, "BIRTHDAY"));
        resultMap.put("politicaloutlook", this.getMapValue(dataMap, "POLITICALOUTLOOK"));
        resultMap.put("education", this.getMapValue(dataMap, "EDUCATION"));
        resultMap.put("major", this.getMapValue(dataMap, "MAJOR"));
        resultMap.put("school", this.getMapValue(dataMap, "SCHOOL"));
        resultMap.put("officephone", this.getMapValue(dataMap, "OFFICEPHONE"));
        resultMap.put("worktime", this.getMapValue(dataMap, "WORKTIME"));
        resultMap.put("auditortype", this.getMapValue(dataMap, "AUDITORTYPE"));
        resultMap.put("isaudit", this.getMapValue(dataMap, "ISAUDIT"));
        resultMap.put("personType", this.getMapValue(dataMap, "PERSONTYPE"));
        resultMap.put("title", this.getMapValue(dataMap, "TITLE"));
        resultMap.put("qualification", this.getMapValue(dataMap, "QUALIFICATION"));
        resultMap.put("gender", this.getMapValue(dataMap, "GENDER"));
        resultMap.put("jobexperiences", this.getMapValue(dataMap, "JOBEXPERIENCES"));
        resultMap.put("memo", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("orgname", this.getMapValue(dataMap, "ORGNAME"));
        resultMap.put("orgmeno", this.getMapValue(dataMap, "ORGMENO"));
        return resultMap;
    }

    private HashMap<String, Object> setPJGLMresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("staffscoreId", this.getMapValue(dataMap, "STAFFSCORE_ID"));
        resultMap.put("realname", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("pmid", this.getMapValue(dataMap, "PMID"));
        resultMap.put("auditProjectName", this.getMapValue(dataMap, "AUDITPROJECTNAME"));
        return resultMap;
    }

    private HashMap<String, Object> setZGLSresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("solutionid", this.getMapValue(dataMap, "SOLUTIONID"));
        resultMap.put("solution", this.getMapValue(dataMap, "SOLUTIONCODE"));
        resultMap.put("solutionname", this.getMapValue(dataMap, "SOLUTIONNAME"));
        resultMap.put("realname", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("reformUser", this.getMapValue(dataMap, "REFORMUSERID"));
        resultMap.put("createdate", this.getMapValue(dataMap, "CREATEDATE"));
        resultMap.put("enddate", this.getMapValue(dataMap, "ENDDATE"));
        resultMap.put("runsataus", this.getMapValue(dataMap, "RUNSTATUS"));
        resultMap.put("memo", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("pmId", this.getMapValue(dataMap, "PMID"));
        resultMap.put("projectid", this.getMapValue(dataMap, "PROJECTID"));
        resultMap.put("bsjdwzrrid", this.getMapValue(dataMap, "BSJDWZRRID"));
        resultMap.put("bsjdwzfr", this.getMapValue(dataMap, "BSJDWZFR"));
        resultMap.put("prjoectName", this.getMapValue(dataMap, "PRJOECTNAME"));
        resultMap.put("zgstatus", this.getMapValue(dataMap, "ZGSTATUS"));
        return resultMap;
    }


    private HashMap<String, Object> setSJJYKresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("jykid", this.getMapValue(dataMap, "JYKID"));
        resultMap.put("tatle", this.getMapValue(dataMap, "TATLE"));
        resultMap.put("code", this.getMapValue(dataMap, "CODE"));
        resultMap.put("experiencetype", this.getMapValue(dataMap, "EXPERIENCETYPE"));
        resultMap.put("experiencetatle", this.getMapValue(dataMap, "EXPERIENCETATLE"));
        resultMap.put("overview", this.getMapValue(dataMap, "OVERVIEW"));
        resultMap.put("realname", this.getMapValue(dataMap, "REALNAME"));
        return resultMap;
    }


    private HashMap<String, Object> setWDDGresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("sheetid", this.getMapValue(dataMap, "SHEETID"));
        resultMap.put("sheeCode", this.getMapValue(dataMap, "SHEETCODE"));
        resultMap.put("sheeName", this.getMapValue(dataMap, "SHEETNAME"));
        resultMap.put("sheettarget", this.getMapValue(dataMap, "SHEETTARGET"));
        resultMap.put("createStaff", this.getMapValue(dataMap, "CREATESTAFF"));
        resultMap.put("createtime", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("updatetime", this.getMapValue(dataMap, "UPDATETIME"));
        resultMap.put("riskattrbution", this.getMapValue(dataMap, "RISKATTRBUTION"));
        resultMap.put("businessAffiliation", this.getMapValue(dataMap, "BUSINESSAFFILIATION"));
        resultMap.put("auditcourse", this.getMapValue(dataMap, "AUDITCOURSE"));
        resultMap.put("internalType", this.getMapValue(dataMap, "INTERNALTYPE"));
        resultMap.put("auditdiscoverable", this.getMapValue(dataMap, "AUDITDISCOVERABLE"));
        resultMap.put("approver", this.getMapValue(dataMap, "APPROVER"));
        resultMap.put("risklevel", this.getMapValue(dataMap, "RISKLEVEL"));
        resultMap.put("questitle", this.getMapValue(dataMap, "QUESTITLE"));
        resultMap.put("targetname", this.getMapValue(dataMap, "TARGETNAME"));
        resultMap.put("businesstype", this.getMapValue(dataMap, "BUSINESSTYPE"));
        resultMap.put("nozgreasion", this.getMapValue(dataMap, "NOZGREASION"));
        resultMap.put("hgdetailtype", this.getMapValue(dataMap, "HGDETAILTYPE"));
        resultMap.put("procategories", this.getMapValue(dataMap, "PROCATEGORIES"));
        resultMap.put("yjfh", this.getMapValue(dataMap, "YJFH"));
        resultMap.put("ejfh", this.getMapValue(dataMap, "EJFH"));
        return resultMap;
    }

    private HashMap<String, Object> setSJQZDresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("certificateId", this.getMapValue(dataMap, "CERTIFICATEID"));
        resultMap.put("auditAbstract", this.getMapValue(dataMap, "AUDITABSTRACT"));
        resultMap.put("evidenceOpinion", this.getMapValue(dataMap, "EVIDENCEOPINION"));
        resultMap.put("certificateUser", this.getMapValue(dataMap, "CERTIFICATEUSER"));
        resultMap.put("certificateDate", this.getMapValue(dataMap, "CERTIFICATEDATE"));
        resultMap.put("assistedzbuser", this.getMapValue(dataMap, "ASSISTEDZBUSER"));
        resultMap.put("assistedfgld", this.getMapValue(dataMap, "ASSISTEDFGLD"));
        resultMap.put("assistedbmfzr", this.getMapValue(dataMap, "ASSISTEDBMFZR"));
        resultMap.put("yjfh", this.getMapValue(dataMap, "YJFH"));
        resultMap.put("ejfh", this.getMapValue(dataMap, "EJFH"));
        resultMap.put("auditMatter", this.getMapValue(dataMap, "AUDITMATTER"));
        resultMap.put("auditUserId", this.getMapValue(dataMap, "AUDITUSERID"));
        resultMap.put("createDate", this.getMapValue(dataMap, "CREATEDATE"));
        resultMap.put("projectName", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("auditUserName", this.getMapValue(dataMap, "AUDITUSERNAME"));
        return resultMap;
    }

    private HashMap<String, Object> setSJXMGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("prjoectName", this.getMapValue(dataMap, "PRJOECTNAME"));
        resultMap.put("planYear", this.getMapValue(dataMap, "PLANYEAR"));
        resultMap.put("projectSource", this.getMapValue(dataMap, "PROJECTSOURCE"));
        resultMap.put("startDate", this.getMapValue(dataMap, "STARTDATE"));
        resultMap.put("pmId", this.getMapValue(dataMap, "PMID"));
        resultMap.put("tempId", this.getMapValue(dataMap, "TEMPID"));
        resultMap.put("costs", this.getMapValue(dataMap, "COSTS"));
        resultMap.put("purpose", this.getMapValue(dataMap, "PURPOSE"));
        resultMap.put("scopes", this.getMapValue(dataMap, "SCOPES"));
        resultMap.put("pursuant", this.getMapValue(dataMap, "PURSUANT"));
        resultMap.put("comments", this.getMapValue(dataMap, "COMMENTS"));
        resultMap.put("umpireId", this.getMapValue(dataMap, "UMPIREID"));
        resultMap.put("controlId", this.getMapValue(dataMap, "CONTROLID"));
        resultMap.put("auditType", this.getMapValue(dataMap, "AUDITTYPE"));
        resultMap.put("examineType", this.getMapValue(dataMap, "EXAMINETYPE"));
        resultMap.put("proDesc", this.getMapValue(dataMap, "PRO_DESC"));
        resultMap.put("proSjfs", this.getMapValue(dataMap, "PRO_SJFS"));
        resultMap.put("auditOrgInfo", this.getMapValue(dataMap, "AUDITSTAFFID"));
        resultMap.put("pprojectName", this.getMapValue(dataMap, "PPROJECTNAME"));
        resultMap.put("externAlassig", this.getMapValue(dataMap, "EXTERNALASSIG"));
        resultMap.put("implementaion", this.getMapValue(dataMap, "IMPLEMENTAION"));
        resultMap.put("implementaionsteps", this.getMapValue(dataMap, "IMPLEMENTAIONSTEPS"));
        resultMap.put("cospomsordepartment", this.getMapValue(dataMap, "COSPOMSORDEPARTMENT"));
        resultMap.put("cospomsordepartmentstaffid", this.getMapValue(dataMap, "COSPOMSORDEPARTMENTSTAFFID"));
        resultMap.put("projecttype", this.getMapValue(dataMap, "PROJECTTYPE"));
        resultMap.put("cntType", this.getMapValue(dataMap, "CNTTYPE"));
        return resultMap;
    }

    private HashMap<String, Object> setQTWJBSresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("otherFileMessageId", this.getMapValue(dataMap, "OTHERFILEMESSAGEID"));
        resultMap.put("fileName", this.getMapValue(dataMap, "FILENAME"));
        resultMap.put("otherFileMessageCreator", this.getMapValue(dataMap, "OTHERFILEMESSAGECREATOR"));
        resultMap.put("createdTime", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("state", this.getMapValue(dataMap, "STATE"));
        resultMap.put("content", this.getMapValue(dataMap, "CONTENT"));
        resultMap.put("creator", this.getMapValue(dataMap, "CREATOR"));
        return resultMap;
    }

    private HashMap<String, Object> setFWRYresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("personnelId", this.getMapValue(dataMap, "PERSONNELID"));
        resultMap.put("personnelName", this.getMapValue(dataMap, "PERSONNELNAME"));
        resultMap.put("belonggroupName", this.getMapValue(dataMap, "BELONGGROUPID"));
        resultMap.put("phone", this.getMapValue(dataMap, "PHONE"));
        resultMap.put("sex", this.getMapValue(dataMap, "SEX"));
        resultMap.put("jobNature", this.getMapValue(dataMap, "JOBNATURE"));
        resultMap.put("birthday", this.getMapValue(dataMap, "BIRTHDAY"));
        resultMap.put("identityCard", this.getMapValue(dataMap, "IDENTITYCARD"));
        resultMap.put("startWorkTime", this.getMapValue(dataMap, "STARTWORKTIME"));
        resultMap.put("politicsStatus", this.getMapValue(dataMap, "POLITICSSTATUS"));
        resultMap.put("education", this.getMapValue(dataMap, "EDUCATION"));
        resultMap.put("degree", this.getMapValue(dataMap, "DEGREE"));
        resultMap.put("duty", this.getMapValue(dataMap, "DUTY"));
        resultMap.put("dutytitle", this.getMapValue(dataMap, "DUTYTITLE"));
        resultMap.put("startYear", this.getMapValue(dataMap, "STARTYEAR"));
        resultMap.put("isLawSpecialty", this.getMapValue(dataMap, "ISLAWSPECIALTY"));
        resultMap.put("isLawOccupational", this.getMapValue(dataMap, "ISLAWOCCUPATIONAL"));
        resultMap.put("qualifications", this.getMapValue(dataMap, "QUALIFICATIONS"));
        resultMap.put("isLawAdviser", this.getMapValue(dataMap, "ISLAWADVISER"));
        resultMap.put("certificationnumber", this.getMapValue(dataMap, "CERTIFICATIONNUMBER"));
        resultMap.put("graduationgraduate", this.getMapValue(dataMap, "GRADUATIONGRADUATE"));
        resultMap.put("starthiretime", this.getMapValue(dataMap, "STARTHIRETIME"));
        resultMap.put("endhiretime", this.getMapValue(dataMap, "ENDHIRETIME"));
        resultMap.put("auditperson", this.getMapValue(dataMap, "AUDITPERSON"));
        resultMap.put("auditTime", this.getMapValue(dataMap, "AUDITTIME"));
        resultMap.put("auditPersonName", this.getMapValue(dataMap, "AUDITPERSONNAME"));
        resultMap.put("creatorName", this.getMapValue(dataMap, "CREATORNAME"));
        resultMap.put("workUnitName", this.getMapValue(dataMap, "WORKUNITNAME"));
        resultMap.put("fillInTime", this.getMapValue(dataMap, "FILLINTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setFWJGJFZRresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("organizationId", this.getMapValue(dataMap, "ORGANIZATIONID"));
        resultMap.put("comInputFieId103", this.getMapValue(dataMap, "ORGANIZATIONNAME"));
        resultMap.put("belongGroupId", this.getMapValue(dataMap, "BELONGGROUPID"));
        resultMap.put("workUnitId", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("responsiblePerson", this.getMapValue(dataMap, "RESPONSIBLEPERSON"));
        resultMap.put("responsiblePersonPhone", this.getMapValue(dataMap, "RESPONSIBLEPERSONPHONE"));
        resultMap.put("contact", this.getMapValue(dataMap, "CONTACT"));
        resultMap.put("contactMobilePhone", this.getMapValue(dataMap, "CONTACTMOBILEPHONE"));
        resultMap.put("contactEmail", this.getMapValue(dataMap, "CONTACTEMAIL"));
        resultMap.put("makingPeople", this.getMapValue(dataMap, "MAKINGPEOPLE"));
        resultMap.put("auditPerson", this.getMapValue(dataMap, "AUDITPERSON"));
        resultMap.put("auditTime", this.getMapValue(dataMap, "AUDITTIME"));
        resultMap.put("state", this.getMapValue(dataMap, "STATE"));
        return resultMap;
    }

    private HashMap<String, Object> setZFLGWresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("adviserId", this.getMapValue(dataMap, "ADVISERID"));
        resultMap.put("adviserName", this.getMapValue(dataMap, "ADVISERNAME"));
        resultMap.put("position", this.getMapValue(dataMap, "POSITION"));
        resultMap.put("mobilePhone", this.getMapValue(dataMap, "MOBILEPHONE"));
        resultMap.put("email", this.getMapValue(dataMap, "EMAIL"));
        resultMap.put("itemRank", this.getMapValue(dataMap, "ITEMRANK"));
        resultMap.put("fillInPerson", this.getMapValue(dataMap, "FILLINPERSON"));
        resultMap.put("FILLINTIME", this.getMapValue(dataMap, "FILLINTIME"));
        resultMap.put("auditPerson", this.getMapValue(dataMap, "AUDITPERSON"));
        resultMap.put("auditTime", this.getMapValue(dataMap, "AUDITTIME"));
        resultMap.put("state", this.getMapValue(dataMap, "STATE"));
        return resultMap;
    }

    private HashMap<String, Object> setZYSQresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("practiceApplyId", this.getMapValue(dataMap, "PRACTICEAPPLYID"));
        resultMap.put("practiceApplyName", this.getMapValue(dataMap, "PRACTICEAPPLYNAME"));
        resultMap.put("sex", this.getMapValue(dataMap, "SEX"));
        resultMap.put("age", this.getMapValue(dataMap, "AGE"));
        resultMap.put("nation", this.getMapValue(dataMap, "NATION"));
        resultMap.put("identityCard", this.getMapValue(dataMap, "IDENTITYCARD"));
        resultMap.put("politicsStatus", this.getMapValue(dataMap, "POLITICSSTATUS"));
        resultMap.put("certificationNumber", this.getMapValue(dataMap, "CERTIFICATIONNUMBER"));
        resultMap.put("technicalPosition", this.getMapValue(dataMap, "TECHNICALPOSITION"));
        resultMap.put("award", this.getMapValue(dataMap, "AWARD"));
        resultMap.put("punishment", this.getMapValue(dataMap, "PUNISHMENT"));
        resultMap.put("realName", this.getMapValue(dataMap, "REALNAME"));
        return resultMap;
    }

    private HashMap<String, Object> setNDJHresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("annualPlanId", this.getMapValue(dataMap, "ANNUALPLANID"));
        resultMap.put("annualPlanName", this.getMapValue(dataMap, "ANNUALPLANNAME"));
        resultMap.put("annualPlanCreator", this.getMapValue(dataMap, "ANNUALPLANCREATOR"));
        resultMap.put("annualPlanCreatedTime", this.getMapValue(dataMap, "ANNUALPLANCREATEDTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setXSGCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("counterpart", this.getMapValue(dataMap, "COUNTERPART"));
        resultMap.put("counterpartphone", this.getMapValue(dataMap, "COUNTERPARTPHONE"));
        resultMap.put("isaggree", this.getMapValue(dataMap, "ISAGGREE"));
        resultMap.put("negetiaresult", this.getMapValue(dataMap, "NEGETIARESULT"));
        resultMap.put("solutionmode", this.getMapValue(dataMap, "SOLUTIONMODE"));
        resultMap.put("ispresetcase", this.getMapValue(dataMap, "ISPRESETCASE"));
        resultMap.put("disputetype", this.getMapValue(dataMap, "DISPUTETYPE"));
        resultMap.put("disputeid", this.getMapValue(dataMap, "DISPUTEID"));
        resultMap.put("isuegent", this.getMapValue(dataMap, "ISUEGENT"));
        resultMap.put("litigationpos", this.getMapValue(dataMap, "LITIGATIONPOS"));
        resultMap.put("disputeitem", this.getMapValue(dataMap, "DISPUTEITEM"));
        resultMap.put("mediationscheme", this.getMapValue(dataMap, "MEDIATIONSCHEME"));
        return resultMap;
    }

    private HashMap<String, Object> setJFDJresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("disputeundertaker", this.getMapValue(dataMap, "DISPUTEUNDERTAKERNAME"));
        resultMap.put("disputeno", this.getMapValue(dataMap, "DISPUTENO"));
        resultMap.put("disputeitem", this.getMapValue(dataMap, "DISPUTEITEM"));
        resultMap.put("disputeid", this.getMapValue(dataMap, "DISPUTEID"));
        resultMap.put("disputestatus", this.getMapValue(dataMap, "DISPUTESTATUS"));
        resultMap.put("disputeType", this.getMapValue(dataMap, "DISPUTETYPE"));
        resultMap.put("isuegent", this.getMapValue(dataMap, "ISUEGENT"));
        resultMap.put("plaintiff", this.getMapValue(dataMap, "PLAINTIFF"));
        resultMap.put("defendant", this.getMapValue(dataMap, "DEFENDANT"));
        resultMap.put("solutionsuggestions", this.getMapValue(dataMap, "SOLUTIONSUGGESTIONS"));
        resultMap.put("disputecours", this.getMapValue(dataMap, "DISPUTECOURS"));
        resultMap.put("litigationpos", this.getMapValue(dataMap, "LITIGATIONPOS"));
        resultMap.put("solutionsuggestions", this.getMapValue(dataMap, "SOLUTIONSUGGESTIONS"));
        resultMap.put("urgentmemo", this.getMapValue(dataMap, "URGENTMEMO"));
        resultMap.put("disputetype", this.getMapValue(dataMap, "DISPUTETYPE"));
        resultMap.put("legalexam", this.getMapValue(dataMap, "LEGALEXAM"));
        resultMap.put("counselexam", this.getMapValue(dataMap, "COUNSELEXAM"));
        resultMap.put("chairmanexam", this.getMapValue(dataMap, "CHAIRMANEXAM"));
        resultMap.put("gmanexam", this.getMapValue(dataMap, "GMANEXAM"));
        resultMap.put("lastdealdate", this.getMapValue(dataMap, "LASTDEALDATE"));
        return resultMap;
    }

    private HashMap<String, Object> setJYSXSHresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("auditType", this.getMapValue(dataMap, "AUDITTYPE"));
        resultMap.put("auditName", this.getMapValue(dataMap, "AUDITNAME"));
        resultMap.put("institutionAuditId", this.getMapValue(dataMap, "INSTITUTIONAUDITID"));
        resultMap.put("mattersInformedPersonnel", this.getMapValue(dataMap, "MATTERSINFORMEDPERSONNEL"));
        resultMap.put("mattersInstructions", this.getMapValue(dataMap, "MATTERSINSTRUCTIONS"));
        resultMap.put("hostDepartmentOpinion", this.getMapValue(dataMap, "HOSTDEPARTMENTOPINION"));
        resultMap.put("relatedDepartmentOpinion", this.getMapValue(dataMap, "RELATEDDEPARTMENTOPINION"));
        resultMap.put("teamLeaderOpinion", this.getMapValue(dataMap, "TEAMLEADEROPINION"));
        resultMap.put("firmLeaderOpinion", this.getMapValue(dataMap, "FIRMLEADEROPINION"));
        resultMap.put("creator", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("complianName", this.getMapValue(dataMap, "COMPLIANCENAME"));
        resultMap.put("createdTime", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("workUnitName", this.getMapValue(dataMap, "WORKUNITNAME"));
        return resultMap;
    }

    private HashMap<String, Object> setZDSHresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("audittype", this.getMapValue(dataMap, "AUDITTYPE"));
        resultMap.put("auditName", this.getMapValue(dataMap, "AUDITNAME"));
        resultMap.put("opinionName", this.getMapValue(dataMap, "OPINIONNAME"));
        resultMap.put("draftadministratoropinion", this.getMapValue(dataMap, "DRAFTADMINISTRATOROPINION"));
        resultMap.put("draftdepartmentopinion", this.getMapValue(dataMap, "DRAFTDEPARTMENTOPINION"));
        resultMap.put("departmentbelonggroupopinion", this.getMapValue(dataMap, "DEPARTMENTBELONGGROUPOPINION"));
        resultMap.put("draftdepartmentopinionsuck", this.getMapValue(dataMap, "DRAFTDEPARTMENTOPINIONSUCK"));
        resultMap.put("institutionauditextid", this.getMapValue(dataMap, "INSTITUTIONAUDITEXTID"));
        resultMap.put("realName", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("complianName", this.getMapValue(dataMap, "COMPLIANCENAME"));
        resultMap.put("department", this.getMapValue(dataMap, "WORKUNIT"));
        return resultMap;
    }

    private HashMap<String, Object> setXMGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("prjoectName", this.getMapValue(dataMap, "PROJECTNAME"));
        resultMap.put("projecttype", this.getMapValue(dataMap, "PROJECTTYPE"));
        resultMap.put("auditobj", this.getMapValue(dataMap, "AUDITOBJ"));
        resultMap.put("planYear", this.getMapValue(dataMap, "PLANYEAR"));
        resultMap.put("auditType", this.getMapValue(dataMap, "AUDITTYPE"));
        resultMap.put("projectSource", this.getMapValue(dataMap, "PROJECTSOURCE"));
        resultMap.put("startDate", this.getMapValue(dataMap, "STARTDATE"));
        resultMap.put("endDate", this.getMapValue(dataMap, "ENDDATE"));
        resultMap.put("itempm", this.getMapValue(dataMap, "ITEMPM"));
        resultMap.put("proSjfs", this.getMapValue(dataMap, "PROSJFS"));
        resultMap.put("cntType", this.getMapValue(dataMap, "CNTTYPE"));
        resultMap.put("costs", this.getMapValue(dataMap, "COSTS"));
        resultMap.put("externAlassig", this.getMapValue(dataMap, "EXTERNALASSIG"));
        resultMap.put("cospomsordept", this.getMapValue(dataMap, "COSPOMSORDEPARTMENT"));
        resultMap.put("implementaion", this.getMapValue(dataMap, "IMPLEMENTAION"));
        resultMap.put("auditrequirements", this.getMapValue(dataMap, "AUDITREQUIREMENTS"));
        resultMap.put("implementaionsteps", this.getMapValue(dataMap, "IMPLEMENTAIONSTEPS"));
        resultMap.put("purpose", this.getMapValue(dataMap, "PURPOSE"));
        resultMap.put("scopes", this.getMapValue(dataMap, "SCOPES"));
        resultMap.put("pursuant", this.getMapValue(dataMap, "PURSUANT"));
        return resultMap;
    }


    private HashMap<String, Object> setJHGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("plancode", this.getMapValue(dataMap, "PLANCODE"));
        resultMap.put("planname", this.getMapValue(dataMap, "PLANNAME"));
        resultMap.put("panlyear", this.getMapValue(dataMap, "PALNYEAR"));
        resultMap.put("plantype", this.getMapValue(dataMap, "PLANTYPE"));
        resultMap.put("startDate", this.getMapValue(dataMap, "STARTTIME"));
        resultMap.put("endDate", this.getMapValue(dataMap, "ENDTIME"));
        resultMap.put("createtime", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("relname", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("orgname", this.getMapValue(dataMap, "AUDITORG"));
        return resultMap;
    }

    private HashMap<String, Object> setHMDGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("blacktype", this.getMapValue(dataMap, "BLACKTYPE"));
        resultMap.put("effectdate", this.getMapValue(dataMap, "EFFECTDATE"));
        resultMap.put("budgetname", this.getMapValue(dataMap, "BUDGETNAME"));
        resultMap.put("counterpartno", this.getMapValue(dataMap, "COUNTERPARTNO"));
        resultMap.put("projectrisk", this.getMapValue(dataMap, "PROJECTRISK"));
        resultMap.put("cretificateno", this.getMapValue(dataMap, "CRETIFICATENO"));
        resultMap.put("totaltmoney", this.getMapValue(dataMap, "TOTALMONEY"));
        resultMap.put("pstartdate", this.getMapValue(dataMap, "PSTARTDATE"));
        resultMap.put("penddate", this.getMapValue(dataMap, "PENDDATE"));
        resultMap.put("projectstagegoal", this.getMapValue(dataMap, "PROJECTSTAGEGOAL"));
        resultMap.put("resultdescription", this.getMapValue(dataMap, "RESULTDESCRIPTION"));
        resultMap.put("contacts", this.getMapValue(dataMap, "CONTACTS"));
        resultMap.put("counterpartdesc", this.getMapValue(dataMap, "COUNTERPARTDESC"));
        resultMap.put("servicetype", this.getMapValue(dataMap, "SERVICETYPE"));
        return resultMap;
    }


    private HashMap<String, Object> setHTBGresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("contractno", this.getMapValue(dataMap, "CONTRACTNO"));
        resultMap.put("contractname", this.getMapValue(dataMap, "CONTRACTNAME"));
        resultMap.put("contractitem", this.getMapValue(dataMap, "CONTRACTITEM"));
        resultMap.put("contracttype", this.getMapValue(dataMap, "CONTRACTTYPE"));
        resultMap.put("startdate", this.getMapValue(dataMap, "STARTDATE"));
        resultMap.put("enddate", this.getMapValue(dataMap, "ENDDATE"));
        resultMap.put("contractxdfxinfo", this.getMapValue(dataMap, "CONTRACTXDFXINFO"));
        resultMap.put("contractdatetype", this.getMapValue(dataMap, "CONTRACTDATETYPE"));
        resultMap.put("contractbd", this.getMapValue(dataMap, "CONTRACTBD"));
        resultMap.put("contractxz", this.getMapValue(dataMap, "CONTRACTXZ"));
        resultMap.put("zxunit", "[\"" + this.getMapValue(dataMap, "CONTRACTORG") + "\"]");
        resultMap.put("contractdept", this.getMapValue(dataMap, "CONTRACTDEPT"));
        resultMap.put("contractstaff", this.getMapValue(dataMap, "CONTRACTSTAFF"));
        resultMap.put("contractchildren", this.getMapValue(dataMap, "CONTRACTCHILDREN"));
        resultMap.put("dctype", this.getMapValue(dataMap, "DCTYPE"));
        resultMap.put("contractmoney", this.getMapValue(dataMap, "CONTRACTMONEY"));
        resultMap.put("moneytype", this.getMapValue(dataMap, "MONEYTYPE"));
        resultMap.put("entrustStaffName", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("bankaccount", this.getMapValue(dataMap, "BANKACCOUNT"));
        resultMap.put("bankname", this.getMapValue(dataMap, "BANKKHYH"));
        resultMap.put("changetype", this.getMapValue(dataMap, "CHANGETYPE"));
        resultMap.put("changedate", this.getMapValue(dataMap, "CHANGEDATE"));
        resultMap.put("changedesc", this.getMapValue(dataMap, "CHANGEDESC"));
        resultMap.put("changedate", this.getMapValue(dataMap, "CHANGEDATE"));
        resultMap.put("changedesc", this.getMapValue(dataMap, "CHANGEDESC"));
        resultMap.put("isbigmatter", this.getMapValue(dataMap, "ISBIGMATTER"));
        resultMap.put("matterorg", this.getMapValue(dataMap, "MATTERORG"));
        return resultMap;
    }

    private HashMap<String, Object> setHTFBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("contractno", this.getMapValue(dataMap, "CONTRACTNO"));
        resultMap.put("contractname", this.getMapValue(dataMap, "CONTRACTNAME"));
        resultMap.put("contracttype", this.getMapValue(dataMap, "CONTRACTTYPE"));
        return resultMap;
    }


    private HashMap<String, Object> setHTDLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("contractno", this.getMapValue(dataMap, "CONTRACTNO"));
        resultMap.put("contractname", this.getMapValue(dataMap, "CONTRACTNAME"));
        resultMap.put("contractitem", this.getMapValue(dataMap, "CONTRACTITEM"));
        resultMap.put("contracttype", this.getMapValue(dataMap, "CONTRACTTYPE"));
        resultMap.put("startdate", this.getMapValue(dataMap, "STARTDATE"));
        resultMap.put("enddate", this.getMapValue(dataMap, "ENDDATE"));
        resultMap.put("contractxdfxinfo", this.getMapValue(dataMap, "CONTRACTXDFXINFO"));
        resultMap.put("contractdatetype", this.getMapValue(dataMap, "CONTRACTDATETYPE"));
        resultMap.put("contractbd", this.getMapValue(dataMap, "CONTRACTBD"));
        resultMap.put("contractxz", this.getMapValue(dataMap, "CONTRACTXZ"));
        resultMap.put("zxunit", "[\"" + this.getMapValue(dataMap, "CONTRACTORG") + "\"]");
        resultMap.put("contractdept", this.getMapValue(dataMap, "CONTRACTDEPT"));
        resultMap.put("contractstaff", this.getMapValue(dataMap, "CONTRACTSTAFF"));
        resultMap.put("contractchildren", this.getMapValue(dataMap, "CONTRACTCHILDREN"));
        resultMap.put("dctype", this.getMapValue(dataMap, "DCTYPE"));
        resultMap.put("contractmoney", this.getMapValue(dataMap, "CONTRACTMONEY"));
        resultMap.put("moneytype", this.getMapValue(dataMap, "MONEYTYPE"));
        resultMap.put("entrustStaffName", this.getMapValue(dataMap, "REALNAME"));
        resultMap.put("bankaccount", this.getMapValue(dataMap, "BANKACCOUNT"));
        resultMap.put("bankname", this.getMapValue(dataMap, "BANKKHYH"));
        resultMap.put("isbigmatter", this.getMapValue(dataMap, "ISBIGMATTER"));
        resultMap.put("matterorg", this.getMapValue(dataMap, "MATTERORG"));
        return resultMap;
    }

    private HashMap<String, Object> setHTYYresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("contractno", this.getMapValue(dataMap, "CONTRACTNO"));
        resultMap.put("contractname", this.getMapValue(dataMap, "CONTRACTNAME"));
        resultMap.put("contracttype", this.getMapValue(dataMap, "CONTRACTTYPE"));
        resultMap.put("createtime", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("contractitem", this.getMapValue(dataMap, "CONTRACTITEM"));
        resultMap.put("counterparthank", this.getMapValue(dataMap, "COUNTERPARTHANK"));
        resultMap.put("counterpartcode", this.getMapValue(dataMap, "COUNTERPARTCODE"));
        resultMap.put("projectgoal", this.getMapValue(dataMap, "PROJECTGOAL"));
        return resultMap;
    }

    //==================================================风险管控BEGIN
    private HashMap<String, Object> setFXCJresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("riskid", this.getMapValue(dataMap, "RISKID"));
        resultMap.put("RISKNUMBER", this.getMapValue(dataMap, "RISKNUMBER"));
        resultMap.put("RISKNAME", this.getMapValue(dataMap, "RISKNAME"));
        resultMap.put("riskcatid", this.getMapValue(dataMap, "RISKCATID"));

//  		+ "TR.RISKDES AS RISKDES, "
//  		+ "TR.BELONGSTO AS BELONGSTO, "
//  		+ "TR.MEMO AS MEMO, "
//  		+ "TR.RISKCATID AS RISKCATID, "
//  		+ "TR.VERSION AS VERSION, "
//  		+ "TR.UNIT AS UNIT, "
//  		+ "TR.REORG AS REORG, "
//  		+ "TR.RISKCREATEDT AS RISKCREATEDT, "
//  		+ "TR.YDUSERID AS YDUSERID, "
//  		+ "TR.RISKPROGRAM AS RISKPROGRAM, "
//  		+ "TR.RISKLEVEL AS RISKLEVEL, "
//  		+ "TR.STATUS AS STATUS, "
//  		+ "TRC.RISKCATNAME AS RISKCATNAME, "
//  		+ "ZRBM.ORGNAME AS ZRBMNAME, "
//  		
//  		+ "TCM.CONMATID AS CONMATID, "
//  		+ "TCM.FLOWNAME AS FLOWNAME, "
//  		+ "TCM.CONTROLTARGET AS CONTROLTARGET, "
//  		+ "TCM.CONTROLNAME AS CONTROLNAME, "
//  		+ "TCM.CONTROLMANAGER AS CONTROLMANAGER, "
//  		+ "TCM.CONTROLFREQUENCY AS CONTROLFREQUENCY, "
//  		+ "TCM.CONTROLTYPE AS CONTROLTYPE, "
//  		+ "TCM.CONTROLMETHOD AS CONTROLMETHOD, "
//  		+ "TCM.CONTROLDES AS CONTROLDES, "
//  		+ "TCM.INSIDECONTROLTARGET AS INSIDECONTROLTARGET, "
//  		+ "TCM.KEYCONTROL AS KEYCONTROL, "
//  		+ "TCM.EFFECTIVE AS EFFECTIVE, "
//  		+ "TCM.CONTROLTEST AS CONTROLTEST, "
//  		+ "TCM.FINANCIALREPORTIDENTIFY AS FINANCIALREPORTIDENTIFY, "
//  		+ "TCM.CONKZCS AS CONKZCS, "
//  		+ "TCM.VERSIONTYPE AS VERSIONTYPE "
        return resultMap;
    }


    private HashMap<String, Object> setPGJHresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("assplanid", this.getMapValue(dataMap, "ASSPLANID"));
        resultMap.put("plancode", this.getMapValue(dataMap, "PLANCODE"));
        resultMap.put("planName", this.getMapValue(dataMap, "PLANNAME"));
        resultMap.put("startDate", this.getMapValue(dataMap, "STARTDATE"));
        resultMap.put("endDate", this.getMapValue(dataMap, "ENDDATE"));
        resultMap.put("planStatus", this.getMapValue(dataMap, "PLANSTATUS"));
        resultMap.put("aprstatus", this.getMapValue(dataMap, "APRSTATUS"));
        resultMap.put("unit", this.getMapValue(dataMap, "UNIT"));
        resultMap.put("memo", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("plandes", this.getMapValue(dataMap, "PLANDES"));
        resultMap.put("planType", this.getMapValue(dataMap, "PLANTYPE"));

        return resultMap;
    }

    private HashMap<String, Object> setFXYDresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("riskcopingid", this.getMapValue(dataMap, "RISKCOPINGID"));
        resultMap.put("copingplot", this.getMapValue(dataMap, "COPINGPLOT"));
        resultMap.put("copingstatus", this.getMapValue(dataMap, "COPINGSTATUS"));
        resultMap.put("copinghead", this.getMapValue(dataMap, "COPINGHEAD"));
        resultMap.put("copingdes", this.getMapValue(dataMap, "COPINGDES"));
        resultMap.put("riskid", this.getMapValue(dataMap, "RISKID"));
        resultMap.put("copingsource", this.getMapValue(dataMap, "COPINGSOURCE"));
        resultMap.put("riskhopevalue", this.getMapValue(dataMap, "RISKHOPEVALUE"));
        resultMap.put("yddes", this.getMapValue(dataMap, "YDDES"));
        resultMap.put("status", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("reviewer", this.getMapValue(dataMap, "REVIEWER"));
        resultMap.put("reviewer2", this.getMapValue(dataMap, "REVIEWER2"));

        return resultMap;
    }

    private HashMap<String, Object> setFXSJKresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("riseveid", this.getMapValue(dataMap, "RISEVEID"));
        resultMap.put("riskeventcode", this.getMapValue(dataMap, "RISKEVENTCODE"));
        resultMap.put("riskeventname", this.getMapValue(dataMap, "RISKEVENTNAME"));
        resultMap.put("riskeventdescription", this.getMapValue(dataMap, "RISKEVENTDESCRIPTION"));
        resultMap.put("indirectloss", this.getMapValue(dataMap, "INDIRECTLOSS"));
        resultMap.put("directloss", this.getMapValue(dataMap, "DIRECTLOSS"));
        resultMap.put("riskfactor1", this.getMapValue(dataMap, "RISKFACTOR1"));
        resultMap.put("riskfactor2", this.getMapValue(dataMap, "RISKFACTOR2"));
        resultMap.put("memo", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("occureddate", this.getMapValue(dataMap, "OCCUREDDATE"));
        resultMap.put("occureddepartment", this.getMapValue(dataMap, "OCCUREDDEPARTMENT"));
        resultMap.put("losseventcategory", this.getMapValue(dataMap, "LOSSEVENTCATEGORY"));
        resultMap.put("discovereddate", this.getMapValue(dataMap, "DISCOVEREDDATE"));
        resultMap.put("inriskeventdb", this.getMapValue(dataMap, "INRISKEVENTDB"));
        resultMap.put("indirectlossdes", this.getMapValue(dataMap, "INDIRECTLOSSDES"));
        resultMap.put("directlossdes", this.getMapValue(dataMap, "DIRECTLOSSDES"));
        resultMap.put("unit", this.getMapValue(dataMap, "UNIT"));
        resultMap.put("eventstatus", this.getMapValue(dataMap, "EVENTSTATUS"));
        resultMap.put("recordorg", this.getMapValue(dataMap, "RECORDORG"));
        resultMap.put("recorddepart", this.getMapValue(dataMap, "RECORDDEPART"));
        resultMap.put("subsystem", this.getMapValue(dataMap, "SUBSYSTEM"));
        resultMap.put("bussiness", this.getMapValue(dataMap, "BUSSINESS"));
        resultMap.put("maxestimateloss", this.getMapValue(dataMap, "MAXESTIMATELOSS"));
        resultMap.put("confirmeddirectloss", this.getMapValue(dataMap, "CONFIRMEDDIRECTLOSS"));
        resultMap.put("confirmeddirectlossa", this.getMapValue(dataMap, "CONFIRMEDDIRECTLOSSA"));
        resultMap.put("riskcatid", this.getMapValue(dataMap, "RISKCATID"));
        resultMap.put("status", this.getMapValue(dataMap, "STATUS"));

        return resultMap;
    }

    private HashMap<String, Object> setFXBGresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("reportid", this.getMapValue(dataMap, "REPORTID"));
        resultMap.put("status", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("REPORTNAME", this.getMapValue(dataMap, "REPORTNAME"));
        resultMap.put("REPORTTIME", this.getMapValue(dataMap, "REPORTTIME"));
        resultMap.put("REPORTTYPE", this.getMapValue(dataMap, "REPORTTYPE"));
        resultMap.put("REPORTMODE", this.getMapValue(dataMap, "REPORTMODE"));
        resultMap.put("REPORTER", this.getMapValue(dataMap, "REPORTER"));
        resultMap.put("REPORTDEPARTMENT", this.getMapValue(dataMap, "REPORTDEPARTMENT"));
        resultMap.put("reporttempid", this.getMapValue(dataMap, "REPORTTEMPID"));
        resultMap.put("reportstatus", this.getMapValue(dataMap, "REPORTSTATUS"));
        resultMap.put("reportfile", this.getMapValue(dataMap, "REPORTFILE"));
        resultMap.put("memo", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("type", this.getMapValue(dataMap, "TYPE"));
        resultMap.put("REPDESC", this.getMapValue(dataMap, "REPDESC"));
        resultMap.put("projectid", this.getMapValue(dataMap, "PROJECTID"));
        resultMap.put("orgid", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("yjdes", this.getMapValue(dataMap, "YJDES"));
        resultMap.put("fhstaffid", this.getMapValue(dataMap, "FHSTAFFID"));
        resultMap.put("zqyjstaffid", this.getMapValue(dataMap, "ZQYJSTAFFID"));
        resultMap.put("reportcode", this.getMapValue(dataMap, "REPORTCODE"));
        resultMap.put("sendtime", this.getMapValue(dataMap, "SENDTIME"));
        resultMap.put("fhstaffname", this.getMapValue(dataMap, "FHSTAFFNAME"));
        resultMap.put("reporterid", this.getMapValue(dataMap, "REPORTERID"));
        resultMap.put("reportdepartmentid", this.getMapValue(dataMap, "REPORTDEPARTMENTID"));
        resultMap.put("zqyjstaffname", this.getMapValue(dataMap, "ZQYJSTAFFNAME"));
        resultMap.put("REPORTLEVEL", this.getMapValue(dataMap, "REPORTLEVEL"));

        return resultMap;
    }

    private HashMap<String, Object> setFXSCTZresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("reviewid", this.getMapValue(dataMap, "REVIEWID"));
        resultMap.put("riskreviewcode", this.getMapValue(dataMap, "RISKREVIEWCODE"));
        resultMap.put("staffid", this.getMapValue(dataMap, "STAFFID"));
        resultMap.put("staffdept", this.getMapValue(dataMap, "STAFFDEPT"));
        resultMap.put("createtime", this.getMapValue(dataMap, "CREATETIME"));
        resultMap.put("mattername", this.getMapValue(dataMap, "MATTERNAME"));
        resultMap.put("matterprojectcode", this.getMapValue(dataMap, "MATTERPROJECTCODE"));
        resultMap.put("mattercode", this.getMapValue(dataMap, "MATTERCODE"));
        resultMap.put("mattercontext", this.getMapValue(dataMap, "MATTERCONTEXT"));
        resultMap.put("createstaffid", this.getMapValue(dataMap, "CREATESTAFFID"));
        return resultMap;
    }

    private HashMap<String, Object> setFXGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("reportid", this.getMapValue(dataMap, "REPORTID"));
        resultMap.put("reportname", this.getMapValue(dataMap, "REPORTNAME"));
        resultMap.put("reporttime", this.getMapValue(dataMap, "REPORTTIME"));
        resultMap.put("reporttype", this.getMapValue(dataMap, "REPORTTYPE"));
        resultMap.put("reportmode", this.getMapValue(dataMap, "REPORTMODE"));
        resultMap.put("reporter", this.getMapValue(dataMap, "REPORTER"));
        resultMap.put("reportdepartment", this.getMapValue(dataMap, "REPORTDEPARTMENT"));
        resultMap.put("reporttempid", this.getMapValue(dataMap, "REPORTTEMPID"));
        resultMap.put("reportstatus", this.getMapValue(dataMap, "REPORTSTATUS"));
        resultMap.put("reportfile", this.getMapValue(dataMap, "REPORTFILE"));
        resultMap.put("memo", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("type", this.getMapValue(dataMap, "TYPE"));
        resultMap.put("repdesc", this.getMapValue(dataMap, "REPDESC"));
        resultMap.put("projectid", this.getMapValue(dataMap, "PROJECTID"));
        resultMap.put("orgid", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("yjdes", this.getMapValue(dataMap, "YJDES"));
        resultMap.put("fhstaffid", this.getMapValue(dataMap, "FHSTAFFID"));
        resultMap.put("zqyjstaffid", this.getMapValue(dataMap, "ZQYJSTAFFID"));
        resultMap.put("reportcode", this.getMapValue(dataMap, "REPORTCODE"));
        resultMap.put("sendtime", this.getMapValue(dataMap, "SENDTIME"));
        resultMap.put("reporterid", this.getMapValue(dataMap, "REPORTERID"));
        resultMap.put("reportdepartmentid", this.getMapValue(dataMap, "REPORTDEPARTMENTID"));
        resultMap.put("fhstaffname", this.getMapValue(dataMap, "FHSTAFFNAME"));
        resultMap.put("zqyjstaffname", this.getMapValue(dataMap, "ZQYJSTAFFNAME"));
        resultMap.put("oaid", this.getMapValue(dataMap, "OAID"));
        resultMap.put("title", this.getMapValue(dataMap, "TITLE"));
        resultMap.put("url", this.getMapValue(dataMap, "URL"));
        resultMap.put("h5url", this.getMapValue(dataMap, "H5URL"));
        resultMap.put("status", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("reportlevel", this.getMapValue(dataMap, "REPORTLEVEL"));
        resultMap.put("createstaffid", this.getMapValue(dataMap, "CREATESTAFFID"));
        return resultMap;
    }

    //==================================================风险管控END

    //==================================================内控管理BEGIN
    private HashMap<String, Object> setPJLXresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ASSID", this.getMapValue(dataMap, "ASSID"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));

        return resultMap;
    }

    private HashMap<String, Object> setPJBGresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("reportid", this.getMapValue(dataMap, "REPORTID"));
        resultMap.put("status", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("reportname", this.getMapValue(dataMap, "REPORTNAME"));
        resultMap.put("reporttime", this.getMapValue(dataMap, "REPORTTIME"));
        resultMap.put("reporttype", this.getMapValue(dataMap, "REPORTTYPE"));
        resultMap.put("reportmode", this.getMapValue(dataMap, "REPORTMODE"));
        resultMap.put("reporter", this.getMapValue(dataMap, "REPORTER"));
        resultMap.put("reportdepartment", this.getMapValue(dataMap, "REPORTDEPARTMENT"));
        resultMap.put("reporttempid", this.getMapValue(dataMap, "REPORTTEMPID"));
        resultMap.put("reportstatus", this.getMapValue(dataMap, "REPORTSTATUS"));
        resultMap.put("reportfile", this.getMapValue(dataMap, "REPORTFILE"));
        resultMap.put("memo", this.getMapValue(dataMap, "MEMO"));
        resultMap.put("type", this.getMapValue(dataMap, "TYPE"));
        resultMap.put("repdesc", this.getMapValue(dataMap, "REPDESC"));
        resultMap.put("projectid", this.getMapValue(dataMap, "PROJECTID"));
        resultMap.put("orgid", this.getMapValue(dataMap, "ORGID"));
        resultMap.put("yjdes", this.getMapValue(dataMap, "YJDES"));
        resultMap.put("fhstaffid", this.getMapValue(dataMap, "FHSTAFFID"));
        resultMap.put("zqyjstaffid", this.getMapValue(dataMap, "ZQYJSTAFFID"));
        resultMap.put("reportcode", this.getMapValue(dataMap, "REPORTCODE"));
        resultMap.put("sendtime", this.getMapValue(dataMap, "SENDTIME"));
        resultMap.put("fhstaffname", this.getMapValue(dataMap, "FHSTAFFNAME"));
        resultMap.put("reporterid", this.getMapValue(dataMap, "REPORTERID"));
        resultMap.put("reportdepartmentid", this.getMapValue(dataMap, "REPORTDEPARTMENTID"));
        resultMap.put("zqyjstaffname", this.getMapValue(dataMap, "ZQYJSTAFFNAME"));
        resultMap.put("reportlevel", this.getMapValue(dataMap, "REPORTLEVEL"));

        return resultMap;
    }

    private HashMap<String, Object> setWTFXresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("findid", this.getMapValue(dataMap, "FINDID"));
        resultMap.put("testtaskid", this.getMapValue(dataMap, "TESTTASKID"));
        resultMap.put("createstaffid", this.getMapValue(dataMap, "CREATESTAFFID"));
        resultMap.put("oneprocess", this.getMapValue(dataMap, "ONEPROCESS"));
        resultMap.put("problemmemo", this.getMapValue(dataMap, "PROBLEMMEMO"));
        resultMap.put("defectmemo", this.getMapValue(dataMap, "DEFECTMEMO"));
        resultMap.put("problemtype", this.getMapValue(dataMap, "PROBLEMTYPE"));
        resultMap.put("DEFECTLEVEL", this.getMapValue(dataMap, "DEFECTLEVEL"));
        resultMap.put("quabasis", this.getMapValue(dataMap, "QUABASIS"));
        resultMap.put("MAINORG", this.getMapValue(dataMap, "MAINORG"));
//  		resultMap.put("MAINORGFZRID", this.getMapValue(dataMap,"MAINORGFZRID"));
        resultMap.put("feedback", this.getMapValue(dataMap, "FEEDBACK"));
        resultMap.put("reformplan", this.getMapValue(dataMap, "REFORMPLAN"));
        resultMap.put("estfinishdate", this.getMapValue(dataMap, "ESTFINISHDATE"));
        resultMap.put("reformstaffid", this.getMapValue(dataMap, "REFORMSTAFFID"));
        resultMap.put("status", this.getMapValue(dataMap, "STATUS"));
        return resultMap;
    }
    //==================================================内控管理END

    //违规核实
    private HashMap<String, Object> setWGHSresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("CLUENABER", this.getMapValue(dataMap, "CLUENABER"));
        resultMap.put("VERIFYCONTENT", this.getMapValue(dataMap, "VERIFYCONTENT"));
        resultMap.put("CLUENAME", this.getMapValue(dataMap, "CLUENAME"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("IMPCREATEUSERNAME", this.getMapValue(dataMap, "IMPCREATEUSERNAME"));
        resultMap.put("CLUEHEFW", this.getMapValue(dataMap, "CLUEHEFW"));
        resultMap.put("CLUEGZZZ", this.getMapValue(dataMap, "CLUEGZZZ"));
        resultMap.put("CLUEBMRY", this.getMapValue(dataMap, "CLUEBMRY"));
        resultMap.put("CLUEWGQX", this.getMapValue(dataMap, "CLUEWGQX"));
        resultMap.put("CLUESSQK", this.getMapValue(dataMap, "CLUESSQK"));
        resultMap.put("CLUEWJSM", this.getMapValue(dataMap, "CLUEWJSM"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("APTIME", this.getMapValue(dataMap, "APTIME"));
        return resultMap;
    }

    //审核报告
    private HashMap<String, Object> setSHBGresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("CLUENABER", this.getMapValue(dataMap, "CLUENABER"));
        resultMap.put("VERIFYCONTENT", this.getMapValue(dataMap, "VERIFYCONTENT"));
        resultMap.put("VERIFYCONKZGCQK", this.getMapValue(dataMap, "VERIFYCONKZGCQK"));
        resultMap.put("VERIFYCONHSJG", this.getMapValue(dataMap, "VERIFYCONHSJG"));
        resultMap.put("CLUEGZJY", this.getMapValue(dataMap, "CLUEGZJY"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("IMPCREATEUSERNAME", this.getMapValue(dataMap, "IMPCREATEUSERNAME"));
        return resultMap;
    }

    //违规核查
    private HashMap<String, Object> setWGHCresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("CLUEZRDX", this.getMapValue(dataMap, "CLUEZRDX"));
        resultMap.put("CLUESSRD", this.getMapValue(dataMap, "CLUESSRD"));
        resultMap.put("CLUECLJY", this.getMapValue(dataMap, "CLUECLJY"));
        resultMap.put("STATUS", this.getMapValue(dataMap, "STATUS"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("IMPCREATEUSERNAME", this.getMapValue(dataMap, "IMPCREATEUSERNAME"));
        resultMap.put("YSSTUTS", this.getMapValue(dataMap, "YSSTUTS"));
        return resultMap;
    }

    //违规移送
    private HashMap<String, Object> setWGYSresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("WGHCID", this.getMapValue(dataMap, "WGHCID"));
        resultMap.put("CLUENABER", this.getMapValue(dataMap, "CLUENABER"));
        resultMap.put("CLUERESOURCE", this.getMapValue(dataMap, "CLUERESOURCE"));
        resultMap.put("YSSTUTS", this.getMapValue(dataMap, "YSSTUTS"));
        return resultMap;
    }

    //合规报告
    private HashMap<String, Object> setHGBGBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("REPORTNAME", this.getMapValue(dataMap, "REPORTNAME"));
        resultMap.put("CONTENT", this.getMapValue(dataMap, "CONTENT"));
        resultMap.put("HIERARCHY", this.getMapValue(dataMap, "HIERARCHY"));
        resultMap.put("REPORTTTYPE", this.getMapValue(dataMap, "REPORTTTYPE"));
        resultMap.put("REPORTSTAGE", this.getMapValue(dataMap, "REPORTSTAGE"));
        resultMap.put("DEPARTMENTHEAD", this.getMapValue(dataMap, "DEPARTMENTHEAD"));
        resultMap.put("DRAFTSMAN", this.getMapValue(dataMap, "DRAFTSMAN"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    //合规手册
    private HashMap<String, Object> setHGSCGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("MANUALNAME", this.getMapValue(dataMap, "MANUALNAME"));
        resultMap.put("VERSIONNUMBER", this.getMapValue(dataMap, "VERSIONNUMBER"));
        resultMap.put("TRANSACTOR", this.getMapValue(dataMap, "TRANSACTOR"));
        resultMap.put("DEPARTMENTHEAD", this.getMapValue(dataMap, "DEPARTMENTHEAD"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    //合规计划申请
    private HashMap<String, Object> setHGJHSPBresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("setHGJHSPBresultMap", this.getMapValue(dataMap, "setHGJHSPBresultMap"));
        resultMap.put("PLANNAME", this.getMapValue(dataMap, "PLANNAME"));
        resultMap.put("FILEIDS", this.getMapValue(dataMap, "FILEIDS"));
        resultMap.put("COMPLIANCEPROCESS", this.getMapValue(dataMap, "COMPLIANCEPROCESS"));
        resultMap.put("HIERARCHY", this.getMapValue(dataMap, "HIERARCHY"));
        resultMap.put("COMPILER", this.getMapValue(dataMap, "COMPILER"));
        resultMap.put("DEPARTMENTHEAD", this.getMapValue(dataMap, "DEPARTMENTHEAD"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    //重点岗位合规责任
    private HashMap<String, Object> setZDGWHGZRresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("DEPARTMENT", this.getMapValue(dataMap, "DEPARTMENT"));
        resultMap.put("POSTNAME", this.getMapValue(dataMap, "POSTNAME"));
        resultMap.put("COMPLIANCETDR", this.getMapValue(dataMap, "COMPLIANCETDR"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    //合规管理员信息管理
    private HashMap<String, Object> setHGGLYXXGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("DEPARTMENT", this.getMapValue(dataMap, "DEPARTMENT"));
        resultMap.put("ADMINISTRATORNAME", this.getMapValue(dataMap, "ADMINISTRATORNAME"));
        resultMap.put("CONTACTINFORMATION", this.getMapValue(dataMap, "CONTACTINFORMATION"));
        resultMap.put("REMARK", this.getMapValue(dataMap, "REMARK"));
        resultMap.put("STATE", this.getMapValue(dataMap, "STATE"));
        resultMap.put("CREATOR", this.getMapValue(dataMap, "CREATOR"));
        resultMap.put("WORKUNIT", this.getMapValue(dataMap, "WORKUNIT"));
        resultMap.put("BELONGGROUP", this.getMapValue(dataMap, "BELONGGROUP"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        resultMap.put("UPDATEDTIME", this.getMapValue(dataMap, "UPDATEDTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setHGGLJCSSLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("SUSPECTEDISSUE", this.getMapValue(dataMap, "SUSPECTEDISSUE"));
        resultMap.put("QUESTIONTYPE", this.getMapValue(dataMap, "QUESTIONTYPE"));
        resultMap.put("DISCOVERTIME", this.getMapValue(dataMap, "DISCOVERTIME"));
        resultMap.put("BUSINESSAREA", this.getMapValue(dataMap, "BUSINESSAREA"));
        resultMap.put("DESCRIBE", this.getMapValue(dataMap, "DESCRIBE"));
        resultMap.put("ISCONFIRM", this.getMapValue(dataMap, "ISCONFIRM"));
        resultMap.put("ISRECTIFICATION", this.getMapValue(dataMap, "ISRECTIFICATION"));
        return resultMap;
    }

    private HashMap<String, Object> setHGGLJCFALresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("PLANCODE", this.getMapValue(dataMap, "PLANCODE"));
        resultMap.put("PLANNAME", this.getMapValue(dataMap, "PLANNAME"));
        resultMap.put("PLANYEAR", this.getMapValue(dataMap, "PLANYEAR"));
        resultMap.put("INSPECTTYPE", this.getMapValue(dataMap, "INSPECTTYPE"));
        resultMap.put("PLANTIMESTART", this.getMapValue(dataMap, "PLANTIMESTART"));
        resultMap.put("PLANTIMEEND", this.getMapValue(dataMap, "PLANTIMEEND"));
        resultMap.put("INSPECTCOMPANY", this.getMapValue(dataMap, "INSPECTCOMPANY"));
        resultMap.put("INSPECTDEPARTMENT", this.getMapValue(dataMap, "INSPECTDEPARTMENT"));
        resultMap.put("CREATEDTIME", this.getMapValue(dataMap, "CREATEDTIME"));
        return resultMap;
    }

    private HashMap<String, Object> setHGGLWTZGLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("IMPID", this.getMapValue(dataMap, "IMPID"));
        resultMap.put("RECTIFICATIONSTATE", this.getMapValue(dataMap, "RECTIFICATIONSTATE"));
        resultMap.put("CONTENT", this.getMapValue(dataMap, "CONTENT"));
        return resultMap;
    }

    private HashMap<String, Object> setHGGLFXSJTZLresultMap(Map<String, Object> dataMap) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("ID", this.getMapValue(dataMap, "ID"));
        resultMap.put("RISKNUMBER", this.getMapValue(dataMap, "RISKNUMBER"));
        resultMap.put("RISKNAME", this.getMapValue(dataMap, "RISKNAME"));
        resultMap.put("DEPARTMENT", this.getMapValue(dataMap, "DEPARTMENT"));
        resultMap.put("FINDTIME", this.getMapValue(dataMap, "FINDTIME"));
        resultMap.put("DISCOVERYTIME", this.getMapValue(dataMap, "DISCOVERYTIME"));
        resultMap.put("RISKTYPE", this.getMapValue(dataMap, "RISKTYPE"));
        return resultMap;
    }


    private Object getMapValue(Map<String, Object> dataMap, String key) {
        try {
            return dataMap.containsKey(key) ? dataMap.get(key) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private HashMap<String, Object> setYmFormDataOracleMessage(TblSystemSheetTable sheet, BigDecimal fromId) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        Map<String, Object> dataMap = new HashMap<String, Object>(0);
        resultMap.put("code", fromId); //id
        switch (sheet.getClassName()) {
            case "TblCyhwUnit": //合同订立
                dataMap = this.ymFormDataMapper.selectCyhwUnitMapById(fromId);
                resultMap.put("title", this.getMapValue(dataMap, "CONTRACTNAME")); //标题
                break;
            case "HTFB": //合同范本
                dataMap = this.ymFormDataMapper.selectCyhwUnitMapById(fromId);
                resultMap.put("title", this.getMapValue(dataMap, "CONTRACTNAME")); //标题
                break;
            case "HTYY":  //合同用印
                dataMap = this.ymFormDataMapper.selectHTYYMapById(fromId);
                resultMap.put("title", this.getMapValue(dataMap, "CONTRACTNAME")); //标题
                break;
            case "HTBG": //合同变更
                dataMap = this.ymFormDataMapper.selectCyhwUnitBGMapById(fromId);
                resultMap.put("title", this.getMapValue(dataMap, "CONTRACTNAME")); //标题
                break;
            case "HMDGL": //黑名单管理
                dataMap = this.ymFormDataMapper.selectHMDGLMapById(fromId);
                resultMap.put("title", this.getMapValue(dataMap, "BUDGETNAME"));
                break;
            case "JHGL": //计划管理
                dataMap = this.ymFormDataMapper.selectJHGLMapById(fromId);
                resultMap.put("title", this.getMapValue(dataMap, "PLANNAME"));
                break;
            case "XMGL": //项目管理
                dataMap = this.ymFormDataMapper.selectXMGLMapById(fromId);
                resultMap.put("title", this.getMapValue(dataMap, "PROJECTNAME"));
                break;
            default:
                break;
        }
        return resultMap;
    }
}
