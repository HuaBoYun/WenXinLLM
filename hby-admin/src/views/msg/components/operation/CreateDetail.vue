<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1100px"
    @close="close"
    :close-on-click-modal="false"
  >
    <CreateDetailContent
      v-if="!isSendBack"
      :form="formData"
      :current-edit="currentEdit"
      :tableDataOA="tableDataOA"
      :ticket="ticket"
      :oaurl="oaurl"
      :pdfList="pdfList"
      :pdfList2="pdfList2"
      :form-fields="formFields"
      :show-sub-fields="showSubFields"
      :sub-fields="subFields"
      :sub-title="subTitle"
    />

    <CreateDetailContentEdit
      v-else
      :current-edit="currentEdit"
      :form="formData"
      :form-fields="formFields"
      :show-sub-fields="showSubFields"
      :sub-fields="subFields"
      :sub-title="subTitle"
      :contracttype="formData.contracttype"
      ref="edit"
    />
  </el-dialog>

  <div v-else>
    <div v-if="flowType == 'TblCyhwUnit'">
      <!-- <CreateDetailContent
        v-if="!isSendBack"
        :current-edit="currentEdit"
        :form="formData"
        :fwType="fwType"
        :fwryType="fwryType"
        :tableDataOA="tableDataOA"
        :ticket="ticket"
        :oaurl="oaurl"
        :pdfList="pdfList"
        :pdfList2="pdfList2"
        :form-fields="formFields"
        :show-sub-fields="showSubFields"
        :sub-fields="subFields"
        :sub-title="subTitle"
      /> -->
      <CreateEdit v-if="!isSendBack" ref="CreateDetail" />
      <CreateDetailContentEdit
        v-else
        :current-edit="currentEdit"
        :form="formData"
        :form-fields="formFields"
        :show-sub-fields="showSubFields"
        :sub-fields="subFields"
        :sub-title="subTitle"
        :contracttype="formData.contracttype"
        :is-wfqdedit="isWfqdedit"
        ref="edit"
      />
    </div>
    <div v-if="flowType == 'XDFWH' || flowType == 'XDFGL'">
      <MaintainDetailContent
        v-if="!isSendBack"
        :formData="XDFWHformData"
        :current="XDFWHcurrent"
      />
      <MaintainDetailContentEdit
        v-else
        ref="XDFGLedit"
        :formData="XDFWHformData"
        :current="XDFWHcurrent"
      />
    </div>
    <div v-if="flowType == 'HTFB'">
      <TemplateDetailContent :formData="HTFBformData" v-if="!isSendBack" />
      <TemplateDetailContentEdit
        ref="HTFBedit"
        :formData="HTFBformData"
        v-else
      />
    </div>
    <div v-if="flowType == 'HTBG'">
      <ChangeDetailContentEdit v-if="!isSendBack" ref="HTBGDetail" />
      <ChangeDetailContentEdit v-else ref="HTBGEdit" />
    </div>
    <div v-if="flowType == 'HTYY'">
      <ContractSealDetailContentEdit ref="htyyEdit" v-if="!isSendBack" />
      <ContractSealDetailContentEdit v-else ref="htyyEdit" />
    </div>
    <!-- 黑名单管理 -->
    <div v-if="flowType == 'HMDGL'">
      <MaintainBlackDetail
        :isDialog="false"
        v-if="!isSendBack"
        ref="MaintainBlackDetail"
      />
      <MaintainBlackEdit ref="MaintainBlackEdit" :isDialog="false" v-else />
    </div>
    <!-- 合同移交 -->
    <div v-if="flowType == 'HTYJ'">
      <TansfercontractEdit ref="TansfercontractEdit" />
    </div>
    <!-- 合同借阅 -->
    <div v-if="flowType == 'HTJY'">
      <BorrowDetail v-if="!isSendBack" ref="borrowDetail" />
      <BorrowEdit v-else ref="borrowDetail" />
    </div>
    <!-- 计划管理 -->
    <div v-if="flowType == 'JHGL'">
      <PlanDetail ref="planDetail" :isFlow="2" />
    </div>
    <!-- 审计人员管理 -->
    <div v-if="flowType == 'SJRNGL'">
      <PersonModal ref="Person" />
    </div>
    <!-- 评价管理 -->
    <div v-if="flowType == 'PJGL'">
      <EvaluationsModal ref="Evaluations" />
    </div>
    <!-- 项目管理 -->
    <div v-if="flowType == 'SJXMGL'">
      <ProjectDetail ref="Project" />
    </div>
    <!-- 我的底稿 -->
    <div v-if="flowType == 'WDDG'">
      <PapersDetail ref="Papers" />
    </div>
    <!-- 整改落实 -->
    <!-- <div v-if="flowType == 'ZGLS'">
      <ZGLSDetail ref="zglsDetail" />
    </div> -->
    <!-- 审计取证单 -->
    <div v-if="flowType == 'SJQZD'">
      <EvidenceDetail ref="evidenceDetail" />
    </div>
    <!-- 审计经验库 -->
    <div v-if="flowType == 'SJJYK'">
      <SJJYKDetail ref="sjjykDetail" />
    </div>
    <!-- 档案借阅 -->
    <div v-if="flowType == 'DAJY'">
      <DAJYDetail ref="dajyDetail" />
    </div>

    <!-- 法务详细 编辑页面 -->
    <!-- 总法律顾问 -->
    <div v-if="flowType == 'ZFLGW'">
      <flgwView ref="flgwView" />
    </div>
    <!-- 法务人员 -->
    <div v-if="flowType == 'FWRY'">
      <fwryView ref="fwryView" />
    </div>
    <!-- 法务机构及负责人 -->
    <div v-if="flowType == 'FWJGJFZR'">
      <fwjgfzrView ref="fwjgfzrView" />
    </div>
    <!-- 纠纷登记 -->
    <div v-if="flowType == 'JFDJ'">
      <disputeEdit ref="disputeEdit" />
    </div>
    <!-- 协商过程 -->
    <div v-if="flowType == 'XSGC'">
      <consultEdit ref="consultEdit" />
    </div>
    <!-- 协商过程 结果-->
    <div v-if="flowType == 'XSGCJG'">
      <consultEdit ref="consultEdit" />
    </div>
    <!-- 年度计划 -->
    <div v-if="flowType == 'NDJH'">
      <ndjhView ref="ndjhView" />
    </div>
    <!-- 其他文件报送 -->
    <div v-if="flowType == 'QTWJBS'">
      <qtwjbsView ref="qtwjbsView" />
    </div>
    <!-- 公司律师 执业申请 -->
    <div v-if="flowType == 'ZYSQ'">
      <gslsView ref="gslsView" />
    </div>
    <!-- 公司律师 执业申请（注销） -->
    <div v-if="flowType == 'ZYSQZX'">
      <gslsView ref="gslsView" />
    </div>
    <!-- 经营事项 -->
    <div v-if="flowType == 'JYSXSH'">
      <jysxshView ref="jysxshView" />
    </div>
    <!-- 制度事项 -->
    <div v-if="flowType == 'ZDSH'">
      <zdshView ref="zdshView" />
    </div>
    <div v-if="flowType == 'ZXGL'">
      <zxglView ref="zxglView" />
    </div>
    <div v-if="flowType == 'SSHZS'">
      <lawsuitEdit ref="lawsuitEdit" />
    </div>
    <!-- 普法计划 -->
    <div v-if="flowType == 'PFJH'">
      <pfjhEdit ref="pfjhEdit" />
    </div>
    <!-- 年度考核 -->
    <div v-if="flowType == 'NDKH'">
      <ndkhView ref="ndkhView" />
    </div>

    <!-- 计划编制 -->
    <!-- 计划草稿 -->
    <jhcgView ref="jhcgView" v-if="flowType == 'JHCG'" />
    <!-- 需求建议表 -->
    <xqjybEdit ref="xqjybEdit" v-if="flowType == 'XQJYB'" />
    <!-- 服务需求表 -->
    <fwxqbEdit ref="fwxqbEdit" v-if="flowType == 'FWXQB'" />
    <!-- 立项建议表 -->
    <lxjybEdit ref="lxjybEdit" v-if="flowType == 'LXJYB'" />
    <!-- 二级单位及成员单位离任审计 -->
    <lrjyjlrsjView ref="lrjyjlrsjView" v-if="flowType == 'EJDWJCYDWLRSJ'" />
    <!-- 财务专项排序表 -->
    <cwzxpxbEdit ref="cwzxpxbEdit" v-if="flowType == 'CWZXPXB'" />
    <!-- 分管领导汇总 -->
    <fgldhz ref="fgldhz" v-if="flowType == 'FGLDHZ'" />
    <!-- 建设项目基本情况 -->
    <jsxmjbqk ref="jsxmjbqk" v-if="flowType == 'JSXMJBQK'" />
    <!-- 建设项目投资完成情况 -->
    <jsxmtzwcqk ref="jsxmtzwcqk" v-if="flowType == 'JSXMTZWCQK'" />
    <!-- 三级单位离任审计 -->
    <lrjjzrsq ref="lrjjzrsq" v-if="flowType == 'SJDWLRSJ'" />
    <!-- 未委托及预计离任 -->
    <wwtjyjlr ref="wwtjyjlr" v-if="flowType == 'WWTJYJLR'" />
    <!-- 工程专项排序表 -->
    <gczxpxb ref="gczxpxb" v-if="flowType == 'GCZXPXB'" />
    <!-- 二级机构任中立项建议 -->
    <ejjgrzlxjy ref="ejjgrzlxjy" v-if="flowType == 'EJJGRZLXJY'" />
    <jhbaView ref="jhba" v-if="flowType == 'JHBA'" />

    <!-- 计划初稿 -->
    <div v-if="flowType == 'JHCHUG'">
      <jhfirstView ref="jhfirstView" />
    </div>
    <!-- 计划 -->
    <div v-if="flowType == 'JH'">
      <jhlistView ref="jhlistView" />
    </div>
    <!-- 计划需求 -->
    <div v-if="flowType == 'JHXQ'">
      <jhxqView ref="jhxqView" />
    </div>
    <!-- 工程审计项目安排 -->
    <div v-if="flowType == 'GCSJXMAP'">
      <gcsjxmapbView ref="gcsjxmapbView" />
    </div>
    <!-- 财务审计项目安排 -->
    <div v-if="flowType == 'CWSJXMAP'">
      <cwsjxmapbView ref="cwsjxmapbView" />
    </div>
    <!-- 审前调查报告 -->
    <div v-if="flowType == 'SQDCBG'">
      <sqdcbgView ref="sqdcbgView" />
    </div>
    <!-- 工作方案 -->
    <div v-if="flowType == 'GZFA'">
      <gzfaView ref="gzfaView" />
    </div>
    <!-- 离任 -->
    <div v-if="flowType == 'LXJYZYPG'">
      <lxjyzypgView ref="lxjyzypg" />
    </div>
    <!-- 任中审计 -->
    <div v-if="flowType == 'RZMXTB'">
      <rzsjmxView ref="rzsjmx" />
    </div>

    <!-- 审计实施 -->
    <!-- 我的底稿 -->
    <div v-if="flowType == 'SJSSWDDG'">
      <NewMyDraftView ref="sjsswddg" />
    </div>
    <!-- 审计结果文书 -->
    <div v-if="flowType == 'SJJGWSCS'">
      <SjjgwscsView ref="sjjgwscs" />
    </div>
    <!-- 审计结果确认单 -->
    <div v-if="flowType == 'SJJGQRS'">
      <SjjgqrdView ref="sjjgqrd" />
    </div>
    <!-- 审计项目追款 -->
    <div v-if="flowType == 'SJXMZK'">
      <SjxmzkView ref="sjxmzk" />
    </div>
    <!-- 审计工作记录 -->
    <div v-if="flowType == 'SJGZJL'">
      <SjgzjlView ref="sjgzjl" />
    </div>
    <!-- 审计督导任务 -->
    <div v-if="flowType == 'SJDDJL'">
      <SjddjlView ref="sjddjl" />
    </div>
    <!-- 审计督导报告 -->
    <div v-if="flowType == 'SJDDBG'">
      <SjddbgView ref="sjddbg" />
    </div>
    <!-- 审计项目情况表 -->
    <div v-if="flowType == 'SJXMQKB'">
      <!-- <SjxmqkbNewView ref="sjxmqkb" /> -->
      <SjxmqkbView ref="sjxmqkb" />
    </div>
    <!-- 现场审查主要内容 -->
    <div v-if="flowType == 'XCSCZYNR'">
      <XcsczynrView ref="scsczynr" />
    </div>
    <!-- 通知变更-->
    <div v-if="flowType == 'TZBG'">
      <TzbgView ref="tzbg" />
    </div>
    <!-- 质量分析报告-->
    <div v-if="flowType == 'ZLFXBG'">
      <ZlfxbgView ref="zlfxbg" />
    </div>
    <!-- 审计通知-->
    <div v-if="flowType == 'SJTZ'">
      <noticeView ref="notice" />
    </div>
    <!-- 项目查看-->
    <xmckView ref="xmck" v-if="flowType == 'YQNSXMGD'" />

    <!-- 审计-综合管理 -->
    <!-- 人员请假单 -->
    <ryqjdViews ref="ryqjdViews" v-if="flowType == 'RYQJD'" />
    <!-- 印信使用单 -->
    <yxsydViews ref="yxsydViews" v-if="flowType == 'YXSYD'" />
    <!-- 资产调剂申请 -->
    <zctjsqViews ref="zctjsqViews" v-if="flowType == 'ZCTJSQ'" />
    <!-- 办公经费支出 -->
    <bgjfzcViews ref="bgjfzcViews" v-if="flowType == 'BGJYZC'" />
    <!-- 修理费支出 -->
    <xlfzcViews ref="xlfzcViews" v-if="flowType == 'XLFZC'" />

    <!-- 央企内审-综合管理 -->
    <!-- 外网代理服务管理流程 -->
    <WWDLFWGL ref="wfqddeal" v-if="flowType == 'WWDLFWGL'" />
    <Hysq ref="hysq" v-if="flowType == 'HYSQ'" />
    <Hygl ref="hygl" v-if="flowType == 'HYGL'" />
    <IpViews ref="ipViews" v-if="flowType == 'IPDZGL'" />
    <Nbwzsqd ref="nbwzsqd" v-if="flowType == 'NBWZSQD'" />
    <Wpsqd ref="wpsqd" v-if="flowType == 'WPSQD'" />
    <Xjd ref="xjd" v-if="flowType == 'XJD'" />
    <wpxjd ref="wpxjd" v-if="flowType == 'WPXJD'" />
    <Dbtzd ref="dbtzd" v-if="flowType == 'DBTZD'" />
    <Email ref="email" v-if="flowType == 'ZSYYXGL'" />
    <Szzsgl ref="szzsgl" v-if="flowType == 'SZZSGL'" />
    <Vpnzhgl ref="vpnzhgl" v-if="flowType == 'VPNZHGL'" />
    <Nbwjcb ref="nbwjcb" v-if="flowType == 'NBWJCB'" />
    <Qxsq ref="qxsq" v-if="flowType == 'QXSQ'" />
    <Gwjd ref="gwjd" v-if="flowType == 'GWJD'" />
    <Sjlxjytz ref="sjlxjytz" v-if="flowType == 'SJLXJYTZ'" />

    <!-- 项目管理 -->
    <!-- 审计项目表 -->
    <AuditProject ref="sjxmb" v-if="flowType == 'SJXMB'" />
    <!-- 审计项目制度流程 -->
    <Sjxmzdlc ref="sjxmzdlc" v-if="flowType == 'SJXMZD'" />
    <!-- 实施方案 -->
    <Ssfa ref="ssfa" v-if="flowType == 'SSFA'" />
    <!-- 计划编制 -->
    <Jhbz ref="jhbz" v-if="flowType == 'JHBZ'" />
    <!-- 项目延期申请 -->
    <xmyqsq ref="xmyqsq" v-if="flowType == 'XMYQSQ'" />
    <!-- 审计通知审批 -->
    <noticeaprView ref="noticeapr" v-if="flowType == 'SJTZSP'" />

    <!-- 审计报告 -->
    <!-- 审理报告 -->
    <Slbg ref="slbg" v-if="flowType == 'SLBG'" />
    <!-- 审计报告定稿 -->
    <Sjbgdg ref="sjbgdg" v-if="flowType == 'SJBGDG'" />
    <!-- 交换意见稿 -->
    <Jhyjg ref="jhyjg" v-if="flowType == 'JHYJG'" />

    <!-- 借阅档案 -->
    <ReadView ref="read" v-if="flowType == 'YQNSDAJY'" />

    <!-- 项目评优 -->

    <!-- 项目评优申报 -->
    <xmpysbView ref="xmpysb" v-if="flowType == 'XMPYSB'" />
    <xmpysbfzView ref="xmpysbfz" v-if="flowType == 'XMPYSBFZ'" />
    <!-- 论文排序 -->
    <lwpxView ref="lwpx" v-if="flowType == 'LWPX'" />
    <llyjsbView ref="llyjsb" v-if="flowType == 'LLYJSB'" />
    <sjjysView ref="sjjys" v-if="flowType == 'SJJYS'" />

    <!-- 问题清单 WTQD-->
    <wtqdView ref="wtqd" v-if="flowType == 'WTQD'" />
    <gzhfView ref="gzhf" v-if="flowType == 'GZHF'" />
    <wtzgView ref="wtzg" v-if="flowType == 'WTZG'" />
    <wtzgViewHX ref="wtzgHX" v-if="flowType == 'HXZGSP'" />
    <!-- 员工离庆审批 -->
    <yglqspView ref="yglqsp" v-if="flowType == 'YGLQSP'" />
    <zbView ref="zb" v-if="flowType == 'ZB'" />

    <!-- 审计移送 -->
    <ysjgwsView ref="ysjgws" v-if="flowType == 'YSJGWS'" />

    <!-- 违规责任追究 -->
    <!-- 问题线索受理 -->
    <wtslView ref="wtsl" v-if="flowType == 'WTXSSL'" />
    <!-- 问题线索核查 -->
    <wthcView ref="wthc" v-if="flowType == 'WTXSHC'" />
    <!-- 问题线索定责 -->
    <wtdzView ref="wtdz" v-if="flowType == 'WTXSDZ'" />
    <!-- 处理结果 -->
    <cljgView ref="cljg" v-if="flowType == 'CLJG'" />
    <!-- 风险创建 -->
    <fxcjView ref="fxcj" v-if="flowType == 'FXCJ'" />
    <!-- 风险应对 -->
    <fxydView ref="fxyd" v-if="flowType == 'FXYD'" />
    <!-- 风险事件 -->
    <EventEdit ref="fxsjk" v-if="flowType == 'FXSJK'" />
    <!-- 风险报告 -->
    <NormalEdit ref="fxbg" v-if="flowType == 'FXBG'" />
    <!-- 风险审查 -->
    <ReviewEdit ref="fxsctz" v-if="flowType == 'FXSCTZ'" />
    <!-- 风险评估计划 -->
    <PlanEdit ref="pgjh" v-if="flowType == 'PGJH'" :isPlan="isPlan" />
    <!-- 月度评估风险关闭 -->
    <RiskMonth ref="ydpgfxgb" v-if="flowType == 'YDPGFXGB'" />
    <!-- 月度评估风险子列表上报 -->
    <RiskMonthChild ref="zdfxydpg" v-if="flowType == 'ZDFXYDPG'" />

    <!-- 整改追责 -->
    <!-- 整改方案 -->
    <div v-if="flowType == 'ZGFA'">
      <SchemeInfo ref="SchemeInfo" />
    </div>
    <!-- 整改报告 -->
    <div v-if="flowType == 'ZGBG'">
      <ReportInfo ref="ReportInfo" />
    </div>
    <!-- 整改落实 -->
    <div v-if="flowType == 'ZGZZLS'">
      <practicableForm ref="practicableForm" />
    </div>
    <!-- 整改评价 -->
    <div v-if="flowType == 'ZGPJ'">
      <valuationForm ref="valuationForm" />
    </div>
    <!-- 评价计划 -->
    <div v-if="flowType == 'PJLX'">
      <pjjhView ref="pjlx" />
    </div>
    <!-- 集团测试计划 -->
    <div v-if="flowType == 'JTNKCSJH'">
      <testPlanView ref="jtnkcsjh" />
    </div>
    <!-- 评价报告编制 -->
    <div v-if="flowType == 'PJBG'">
      <pjbgbzView ref="pjbg" />
    </div>
    <!-- 问题发现 -->
    <div v-if="flowType == 'WTFX'">
      <wtfxView ref="wtfx" />
    </div>
    <!-- 评价缺陷 -->
    <div v-if="flowType == 'NKQXD'">
      <pjqxView ref="nkqxd" />
    </div>
    <!-- 缺陷缺陷 -->
    <div v-if="flowType == 'NKQXGL'">
      <qxglView ref="qxgl" />
    </div>
    <!-- 重大风险创建审批 -->
    <div v-if="flowType == 'ZDFXCJSP'">
      <ZdfxcjView ref="zdfxcj" />
    </div>
    <!-- 重大风险填报 -->
    <div v-if="flowType == 'ZDFXTBSP'">
      <ZdfxtbView ref="zdfxtb" />
    </div>
    <!-- 集团评估计划 -->
    <div v-if="flowType == 'JTFXPGJH'">
      <GroupPlan ref="jtfxpgjh" />
    </div>

    <!-- 收款管理 -->
    <div v-if="flowType == 'SKGLLC'">
      <Skgl ref="skgl" />
    </div>
    <!-- 付款管理 -->
    <div v-if="flowType == 'FKGLLC'">
      <Fkgl ref="fkgl" />
    </div>
    <!-- 风险监测指标创建 -->
    <div v-if="flowType == 'FXJCZBCJ'">
      <RiskIndicatorCreation ref="fxjczbcj" />
    </div>
    <!-- 风险监测指标填报 -->
    <div v-if="flowType == 'FXJCZBTB'">
      <RiskIndicatorReporting ref="fxjczbtb" />
    </div>

    <!-- 系统信息流程 -->
    <div v-if="flowType == 'XTXGQR'">
      <SystemInfo ref="xtxgqr" />
    </div>
    <!-- 初步核实 -->
    <div v-if="flowType == 'SHBG'">
      <ShbgEdit ref="shbg" />
    </div>
    <!-- 违规责任追究 -->
    <div v-if="flowType == 'WGHS'">
      <WghsEdit ref="wghs" />
    </div>
    <!-- 移送函 -->
    <div v-if="flowType == 'WGYS'">
      <WgysEdit ref="wgys" />
    </div>
  </div>
</template>

<script>
  import {
    getContractDetail,
    getContractItem,
    getContractSealDetail,
    viewDealInfo,
    contractPdfList,
    contractExamList,
    getCyhwUnitOAList,
    getOaurl,
  } from '@/api/contract/manage'
  import { getSolutionDetail } from '@/api/audit/rectify'
  import { myDraftDetail, imPlementOrderDetail } from '@/api/audit/implement'
  import { getBlackCounterPartInfo, getOpposite } from '@/api/contract/opposite'
  import PlanDetail from '@/views/audit/plan/components/IndexEditDialog'
  import BorrowDetail from '@/views/contract/contractManage/components/BorrowDetail'
  import BorrowEdit from '@/views/contract/contractManage/components/BorrowEditEdit'
  import ContractSealDetailContent from '@/views/contract/contractManage/components/ContractSealDetailContent'
  import ContractSealDetailContentEdit from '@/views/contract/contractManage/components/ContractSealDetailContentEdit'
  import ChangeDetailContent from '@/views/contract/contractManage/components/contractsEdit/ChangeDetailContent'
  import ChangeDetailContentEdit from '@/views/contract/contractManage/components/contractsEdit/ChangeDetailContentEdit'
  import CreateDetailContent from '@/views/contract/contractManage/components/contractsEdit/CreateDetailContent.vue'
  import CreateEdit from '@/views/contract/contractManage/components/contractsEdit/CreateEdit.vue'
  import CreateDetailContentEdit from '@/views/contract/contractManage/components/contractsEdit/CreateDetailContentEdit.vue'
  import { comboFields } from '@/views/contract/contractManage/components/contractsEdit/methods'
  import TemplateDetailContent from '@/views/contract/contractManage/components/TemplateDetailContent'
  import TemplateDetailContentEdit from '@/views/contract/contractManage/components/TemplateDetailContentEdit'
  import BlacklistEdit from '@/views/contract/opposite/components/BlacklistEdit'
  import BlacklistEditEdit from '@/views/contract/opposite/components/BlacklistForm.vue'
  import MaintainDetailContent from '@/views/contract/opposite/components/MaintainDetailContent'
  import MaintainDetailContentEdit from '@/views/contract/opposite/components/MaintainDetailContentEdit'
  import MaintainBlackDetail from '@/views/contract/opposite/components/MaintainBlackDetail'
  import MaintainBlackEdit from '@/views/contract/opposite/components/MaintainBlackContentEdit'
  import EvaluationsModal from '@/views/msg/components/newProcess/evaluationsModal.vue'
  import PapersDetail from '@/views/msg/components/newProcess/papersDetails.vue'
  import PersonModal from '@/views/msg/components/newProcess/personModal.vue'
  // import ProjectDetail from '@/views/msg/components/newProcess/projectModal.vue'
  import ProjectDetail from '@/views/msg/components/newProcess/projectModal2.vue'
  import EvidenceDetail from '@/views/msg/components/newProcess/auditEvidence.vue'
  import SJJYKDetail from '@/views/msg/components/newProcess/sjjykDetail.vue'
  // import ZGLSDetail from '@/views/msg/components/newProcess/zglsDetails.vue'
  import DAJYDetail from '@/views/msg/components/newProcess/dajyDetail.vue'
  import SjjgwscsView from '@/views/msg/components/newProcess/SjjgwscsView.vue'

  import flgwView from './flgwView'
  import fwryView from './fwryView'
  import fwjgfzrView from './fwjgfzrView'
  import disputeEdit from './DisputeEdit'
  import consultEdit from './ConsultEdit'
  import ndjhView from './ndjhView'
  import qtwjbsView from './qtwjbsView'
  import gslsView from './gslsView'
  import jysxshView from './jysxshView'
  import zdshView from './zdshView'
  import zxglView from './zxglView'
  import lawsuitEdit from './lawsuitEdit'
  import pfjhEdit from './pfjhDetail.vue'
  import TansfercontractEdit from './TansfercontractEdit.vue'
  import ndkhView from './ndkhView.vue'

  // 综合管理-审计
  // 人员请假单
  import ryqjdViews from './ryqjdViews.vue'
  import yxsydViews from './yxsydViews.vue'
  import zctjsqViews from './zctjsqViews.vue'
  import bgjfzcViews from './bgjfzcViews.vue'
  import xlfzcViews from './xlfzcViews.vue'
  // 审计实施
  import NewMyDraftView from '../sjss/newMyDraftView'
  import SjjgqrdView from '../sjss/sjjgqrdView.vue'
  import SjxmzkView from '../sjss/sjxmzkView'
  import SjgzjlView from '../sjss/sjgzjlView.vue'
  import SjddjlView from '../sjss/sjddjlView.vue'
  import SjddbgView from '../sjss/sjddbgView.vue'
  import SjxmqkbView from '../sjss/sjxmqkbNewView.vue'
  import SjxmqkbNewView from '../sjss/sjxmqkbNewView.vue'
  import XcsczynrView from '../sjss/xcsczynrView.vue'
  import TzbgView from '../sjss/tzbgView.vue'
  import ZlfxbgView from '../sjss/zlfxbgView.vue'
  import noticeView from '../sjss/noticeView.vue'
  import xmckView from '../sjss/xmck.vue'
  //央企内审-综合管理
  import WWDLFWGL from '../zhgl/wwdlfwglViews.vue'
  import Hysq from '../zhgl/hysqView.vue'
  import Hygl from '../zhgl/hyglView.vue'
  import IpViews from '../zhgl/ipViews.vue'
  import Nbwzsqd from '../zhgl/nbwzsqdViews.vue'
  import Wpsqd from '../zhgl/wpsqdViews.vue'
  import Xjd from '../zhgl/xjdViews.vue'
  import wpxjd from '../zhgl/wpxjdViews.vue'
  import Dbtzd from '../zhgl/dbtzdViews.vue'
  import Email from '../zhgl/emailViews.vue'
  import Szzsgl from '../zhgl/szzsglViews.vue'
  import Vpnzhgl from '../zhgl/vpnViews.vue'
  import Nbwjcb from '../zhgl/nbwjcbViews.vue'
  import Qxsq from '../zhgl/qxsqViews.vue'
  import Gwjd from '../zhgl/gwjdViews.vue'

  // 央企内审-计划编制
  import fgldhz from '@/views/msg/components/jhbz/fgldhzEdit.vue'
  import jsxmjbqk from '@/views/msg/components/jhbz/jsxmjbqkView.vue'
  import jsxmtzwcqk from '@/views/msg/components/jhbz/jsxmtzwcqkEdit.vue'
  // import lrjjzrsq from '@/views/oilAudit/lrjjzr/components/lrjjzrsqJdView'
  import lrjjzrsq from '@/views/msg/components/jhbz/lrjjzrsqView.vue'
  import wwtjyjlr from '@/views/msg/components/jhbz/wwtjyjlrView.vue'
  import gczxpxb from '@/views/msg/components/jhbz/gczxpxbEdit.vue'
  import ejjgrzlxjy from '@/views/msg/components/jhbz/ejjgrzlxjyView.vue'
  import xqjybEdit from '@/views/msg/components/jhbz/xqjybEdit.vue'
  import fwxqbEdit from '@/views/msg/components/jhbz/fwxqbEdit.vue'
  import lxjybEdit from '@/views/msg/components/jhbz/lxjybEdit.vue'
  import lrjyjlrsjView from '@/views/msg/components/jhbz/lrjyjlrsjView.vue'
  import cwzxpxbEdit from '@/views/msg/components/jhbz/cwzxpxbEdit.vue'
  import jhcgView from '@/views/msg/components/jhbz/jhcgView.vue'
  import jhfirstView from '@/views/msg/components/jhbz/jhfirstView.vue'
  // import jhlistView from '@/views/msg/components/jhbz/jhlistEdit.vue'
  import jhlistView from '@/views/msg/components/jhbz/jhcgEdit2.vue'
  import jhxqView from '@/views/msg/components/jhbz/jhlistEdit.vue'
  import gcsjxmapbView from '@/views/msg/components/jhbz/gcsjxmapbEdit.vue'
  import cwsjxmapbView from '@/views/msg/components/jhbz/cwsjxmapbEdit.vue'
  import sqdcbgView from '@/views/msg/components/jhbz/sqdcbgView.vue'
  import gzfaView from '@/views/msg/components/jhbz/gzfaView.vue'
  import lxjyzypgView from '@/views/msg/components/jhbz/lxjyzypgView.vue'
  import rzsjmxView from '@/views/msg/components/jhbz/rzsjmxView.vue'
  import jhbaView from '@/views/msg/components/jhbz/jhbaView.vue'
  import Sjlxjytz from '@/views/msg/components/jhbz/sjlxjytzView.vue'

  //论文评优
  import lwpxView from '@/views/msg/components/lwpy/lwpxView.vue'
  import llyjsbView from '@/views/msg/components/lwpy/llyjsbView.vue'

  //项目管理
  // 审计项目表
  import AuditProject from '../xmgl/auditProjectView.vue'
  import Sjxmzdlc from '../xmgl/sjxmzdlcView.vue'
  import Ssfa from '../xmgl/ssfaView.vue'
  import Jhbz from '../xmgl/jhbzView.vue'
  import xmyqsq from '../xmgl/xmyqsqView.vue'
  import noticeaprView from '../xmgl/noticeaprView.vue'

  import wtqdView from '../sjzg/wtqdView.vue'
  import gzhfView from '../sjzg/gzhfView.vue'
  import wtzgView from '../sjzg/wtzgView.vue'
  import wtzgViewHX from '../sjzg/wtzgViewHX.vue'

  // 审计报告
  //审理报告
  import Slbg from '@/views/msg/components/report/Slbg.vue'
  // 审计报告定稿
  import Sjbgdg from '@/views/msg/components/report/Sjbgdg.vue'
  // 交换意见稿
  import Jhyjg from '@/views/msg/components/report/Custom.vue'

  //档案借阅
  import ReadView from '@/views/msg/components/auditRecord/ReadView.vue'
  import xmpysbView from '@/views/msg/components/xmpy/xmpysbView.vue'
  import xmpysbfzView from '@/views/msg/components/xmpy/xmpysbfzView.vue'
  import sjjysView from '@/views/msg/components/report/sjjysView.vue'
  //员工离庆审批
  import yglqspView from '@/views/msg/components/zhgl/yglqspView.vue'
  import zbView from '@/views/msg/components/zhgl/sjzbView.vue'

  //审计移送
  import ysjgwsView from '@/views/msg/components/sjys/ysjgwsView.vue'
  //违规责任追究
  //问题线索核查
  import wthcView from '@/views/msg/components/wgzrzj/wthcView.vue'
  //问题线索受理
  import wtslView from '@/views/msg/components/wgzrzj/wtslView.vue'
  //问题线索受理
  import wtdzView from '@/views/msg/components/wgzrzj/wtdzView.vue'
  //处理结果
  import cljgView from '@/views/msg/components/wgzrzj/cljgView.vue'

  //风险创建
  import fxcjView from '@/views/msg/components/znfk/fxcjView.vue'
  //风险应对
  import fxydView from '@/views/msg/components/znfk/fxydView.vue'
  //风险事件
  import EventEdit from '@/views/msg/components/znfk/EventEdit.vue'
  //风险报告
  import NormalEdit from '@/views/msg/components/znfk/NormalEdit.vue'
  //风险审查
  import ReviewEdit from '@/views/msg/components/znfk/ReviewEdit.vue'
  //风险评估计划
  import PlanEdit from '@/views/msg/components/znfk/PlanEdit.vue'
  //风险评估计划
  import RiskMonth from '@/views/msg/components/znfk/RiskMonth.vue'
  //风险评估计划-子列表
  import RiskMonthChild from '@/views/msg/components/znfk/RiskMonthChild.vue'
  import pjjhView from '@/views/msg/components/znfk/pjjhView.vue'
  import testPlanView from '@/views/msg/components/znfk/testPlanView.vue'
  import pjqxView from '@/views/msg/components/znfk/pjqxView.vue'
  import pjbgbzView from '@/views/msg/components/znfk/pjbgbzView.vue'
  import wtfxView from '@/views/msg/components/znfk/wtfxView.vue'

  // 整改追责
  import SchemeInfo from '@/views/msg/components/wgzz/SchemeInfo.vue'
  import ReportInfo from '@/views/msg/components/wgzz/ReportInfo.vue'
  import practicableForm from '@/views/msg/components/wgzz/practicableForm.vue'
  import valuationForm from '@/views/msg/components/wgzz/valuationForm.vue'
  import qxglView from '@/views/msg/components/newProcess/qxglView.vue'
  import ShbgEdit from '@/views/msg/components/wgzz/shbgEdit.vue'
  import WghsEdit from '@/views/msg/components/wgzz/wghsEdit.vue'
  import WgysEdit from '@/views/msg/components/wgzz/wgysEdit.vue'

  //重大风险创建
  import ZdfxcjView from '@/views/msg/components/znfk/zdfxcjView.vue'
  //重大风险填报
  import ZdfxtbView from '@/views/msg/components/znfk/zdfxtbView.vue'
  //集团评估计划
  import GroupPlan from '@/views/msg/components/znfk/groupPlan.vue'
  //风险监测指标创建
  import RiskIndicatorCreation from '@/views/msg/components/znfk/riskIndicatorCreation.vue'
  //风险监测指标填报
  import RiskIndicatorReporting from '@/views/msg/components/znfk/riskIndicatorReporting.vue'
  import {
    getRectificationPlanDetail,
    getReportDetail,
    getRectificationImplDetailInfo,
    getZgzzeEvaluationDetail,
  } from '@/api/zgzz/index.js'

  //成果运用-缺陷管理
  import { reportDetail } from '@/api/audit/report'

  import Skgl from '@/views/msg/components/skgl.vue'
  import Fkgl from '@/views/msg/components/fkgl.vue'

  // 系统信息流程
  import SystemInfo from '@/views/msg/components/system/SystemInfo.vue'

  export default {
    name: 'CreateDetailOption',
    components: {
      /* 别用异步引入 */
      wtzgView,
      wtzgViewHX,
      wtqdView,
      gzhfView,
      jhcgView,
      jhfirstView,
      jhlistView,
      jhxqView,
      gcsjxmapbView,
      cwsjxmapbView,
      sqdcbgView,
      gzfaView,
      //审计实施
      xmckView,
      NewMyDraftView,
      SjjgqrdView,
      MaintainBlackDetail,
      MaintainBlackEdit,
      SjxmzkView,
      SjgzjlView,
      SjddjlView,
      SjddbgView,
      SjxmqkbView,
      XcsczynrView,
      TzbgView,
      ZlfxbgView,
      CreateEdit,
      CreateDetailContent,
      CreateDetailContentEdit,
      MaintainDetailContent,
      TemplateDetailContent,
      TemplateDetailContentEdit,
      ChangeDetailContent,
      ChangeDetailContentEdit,
      ContractSealDetailContent,
      ContractSealDetailContentEdit,
      BorrowDetail,
      BorrowEdit,
      BlacklistEdit,
      BlacklistEditEdit,
      PlanDetail,
      PersonModal,
      EvaluationsModal,
      ProjectDetail,
      PapersDetail,
      MaintainDetailContentEdit,
      flgwView,
      fwryView,
      fwjgfzrView,
      disputeEdit,
      consultEdit,
      ndjhView,
      qtwjbsView,
      gslsView,
      jysxshView,
      zdshView,
      EvidenceDetail,
      SJJYKDetail,
      // ZGLSDetail,
      DAJYDetail,
      zxglView,
      lawsuitEdit,
      pfjhEdit,
      TansfercontractEdit,
      ndkhView,
      ryqjdViews,
      yxsydViews,
      zctjsqViews,
      bgjfzcViews,
      xlfzcViews,
      WWDLFWGL,
      Hysq,
      Hygl,
      IpViews,
      Nbwzsqd,
      Wpsqd,
      Xjd,
      wpxjd,
      Dbtzd,
      Email,
      Szzsgl,
      Vpnzhgl,
      Nbwjcb,
      Qxsq,
      Gwjd,
      AuditProject,
      Sjxmzdlc,
      xqjybEdit,
      fwxqbEdit,
      lxjybEdit,
      lrjyjlrsjView,
      cwzxpxbEdit,
      fgldhz,
      jsxmjbqk,
      jsxmtzwcqk,
      lrjjzrsq,
      wwtjyjlr,
      gczxpxb,
      ejjgrzlxjy,
      Ssfa,
      Jhbz,
      noticeView,
      xmyqsq,
      lxjyzypgView,
      rzsjmxView,
      Slbg,
      Sjbgdg,
      Jhyjg,
      ReadView,
      xmpysbView,
      xmpysbfzView,
      jhbaView,
      noticeaprView,
      lwpxView,
      llyjsbView,
      sjjysView,
      yglqspView,
      zbView,
      ysjgwsView,
      wthcView,
      wtslView,
      cljgView,
      wtdzView,
      Sjlxjytz,
      fxcjView,
      fxydView,
      EventEdit,
      NormalEdit,
      ReviewEdit,
      PlanEdit,
      RiskMonth,
      RiskMonthChild,
      // 整改追责
      SchemeInfo,
      ReportInfo,
      practicableForm,
      valuationForm,
      SjjgwscsView,
      pjjhView,
      pjbgbzView,
      wtfxView,
      pjqxView,
      qxglView,
      ZdfxcjView,
      ZdfxtbView,
      GroupPlan,
      testPlanView,
      Skgl,
      Fkgl,
      RiskIndicatorCreation,
      RiskIndicatorReporting,
      SystemInfo,
      ShbgEdit,
      WghsEdit,
      WgysEdit,
    },
    provide() {
      return {
        fatherFetchItem: this.fetchItem,
      }
    },
    props: {
      formId: {
        type: Number | String,
        default: '',
      },
      flowType: {
        type: String,
        default: '',
      },
      userName: {
        type: String,
        default: '',
      },
      userId: {
        type: Number | String,
        default: '',
      },
      isDialog: {
        type: Boolean,
        default: true,
      },
      isWdcy: {
        type: Boolean,
        default: false,
      },
      isWfqdedit: {
        type: Boolean,
        default: false,
      },
      flowTaskInfo: {
        type: Object,
        default: () => ({}),
      },
      flowTaskNodeList: {
        type: Object,
        default: () => ({}),
      },
      nextNodeName: {
        type: String,
        default: '',
      },
      nextStepId: {
        type: String,
        default: '',
      },
      // 风险数据-集团风险数据库传入
      isPlan: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        tableDataOA: [],
        pdfList: [],
        pdfList2: [],
        ticket: '',
        oaurl: '',
        isSendBack: false,
        currentEdit: 'moren',
        showSubFields: false,
        subTitle: '',
        subFields: [
          { value: 'infoname', label: '货物名称' },
          { value: 'infodesc', label: '运输说明' },
          { value: 'infoxh', label: '规格型号' },
          { value: 'infoprice', label: '货物价值' },
        ],
        formFields: {
          changetype: undefined,
        },
        formData: {
          flowId: 622316,
          flowid: 622316,
          flowname: 'HTGL002',
          recordtype: 'HTGL002',
          contractid: undefined,
          contractno: '',
          contractname: undefined,
          contractitem: undefined,
          contracttype: null,
          startdate: undefined,
          enddate: undefined,
          counterparttype: undefined,
          contractdatetype: undefined,
          contractxz: undefined,
          contractbd: undefined,
          contractzd: undefined,
          zxunit: undefined, // 执行单位key
          orgname: undefined, // 执行单位value
          contractdept: undefined, // 执行部门key
          orgmeno: undefined, // 执行部门value
          contractstaff: undefined, // 执行人key
          realname: undefined, // 执行人value
          contractxdfxinfo: undefined, //相对方主键key
          budgetname: undefined, // 相对方value
          topicname: undefined, // 立项信息key（是name）
          topicid: undefined, // 立项信息value
          counterpartbank: undefined, // 银行key
          bankaccount: undefined, // 银行value
          bankkhyh: undefined, // 开户银行
          momoconcat: undefined,
          dctype: undefined,
          contractmoney: undefined,
          moneytype: undefined,
          hzsumowing: undefined,
          describe: undefined,
          account: undefined,
          typefl: undefined,
          attList: [],
          nodeList: [],
          informationList: [],
          signingList: [],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
        typeOptions: [],
        xdfOptions: [],
        disabled: false,
        getcontracttype: '',
        flowtaskinfoflowid: '', //我发起的提交时前置所需
        ymFromId: '', //我发起的获取的引迈表单id
        XDFWHformData: {
          flowId: 622322,
          counterpartno: undefined,
          budgetid: undefined,
          budgetname: undefined,
          // othermoney: undefined,
          // financemoney: undefined,
          // counterparttype: undefined,
          // oppositenature: undefined,
          projectrisk: undefined,
          cretificateno: undefined,
          pdate: undefined,
          pstartdate: undefined,
          penddate: undefined,
          servicetype: 1,
          totaltmoney: undefined,
          projectstagegoal: undefined,
          // projectcondition: undefined,
          // counterpartcode: undefined,
          // counterpartnetaddress: undefined,
          // director: undefined,
          // counterpartphone: undefined,
          counterpartaddress: undefined,
          resultdescription: undefined,
          counterpartdesc: undefined,
          staffid: undefined,
          cerType: 0,
          date: undefined,
          penddateStr: undefined,
          pstartdateStr: undefined,
          contacts: undefined,
          // contactsphone: undefined,
          // contactsadress: undefined,
          // contactsemail: undefined,
          // station: undefined,
          // callname: undefined,
          // remarks: undefined,
          attList: [],
          bankInfoList: [],
        },
        XDFWHcurrent: null,
        HTYYformData: {
          flowId: 622324,
          budgetid: undefined,
          recordparent: undefined,
          counterpartcode: undefined,
          counterparthank: undefined,
          createtime: undefined,
          projectgoal: undefined,
          attList: [],
        },
        HTYYnode: {},
        HTFBformData: {
          flowId: 733271,
          contractid: undefined,
          contractno: '',
          jborgName: '',
          contractname: '',
          contracttype: '',
          recordtype: 'HTGL007',
          momoconcat: '',
          describe: '',
        },

        HMDGLcurrent: [],
        HMDGLformData: {
          flowId: 622322,
          counterpartno: undefined,
          budgetid: undefined,
          budgetname: undefined,
          othermoney: undefined,
          financemoney: undefined,
          counterparttype: undefined,
          oppositenature: undefined,
          projectrisk: undefined,
          cretificateno: undefined,
          pdate: undefined,
          servicetype: 1,
          totaltmoney: undefined,
          projectstagegoal: undefined,
          projectcondition: undefined,
          counterpartcode: undefined,
          counterpartnetaddress: undefined,
          director: undefined,
          counterpartphone: undefined,
          counterpartaddress: undefined,
          resultdescription: undefined,
          counterpartdesc: undefined,
          staffid: undefined,
          date: undefined,
          contacts: undefined,
          contactsphone: undefined,
          contactsadress: undefined,
          contactsemail: undefined,
          station: undefined,
          callname: undefined,
          remarks: undefined,
          attList: [],
          datetext: undefined,
          blacktype: 2,
          effectdate: undefined,
          status: 0,
        },
        HMDGLlocalList: [],
        fwType: false,
        fwryType: false,
      }
    },
    computed: {
      myFlowType() {
        // 流程表单获取数据函数定义,根据 get + flowType的形式定; 例: "getXXX()"
        console.log(
          'oilAudit/report/components/options/department',
          this.flowType
        )
        const func = this['get' + this.flowType]
        if (func && typeof func === 'function') func(this.formId)
        return this.flowType
      },
    },
    watch: {
      myFlowType(newValue) {
        this.flowType = newValue
      },

      'formData.contracttype': {
        handler(val) {
          if (this.isSendBack == true && this.flowType != 'HTYY') {
            this.$nextTick(() => {
              this.$refs['edit'].showEdit(
                this.formData,
                this.formData.contracttype,
                this.formId,
                this.flowtaskinfoflowid,
                this.ymFromId,
                this.status
              )
            })
          }
        },
      },
    },
    created() {},
    mounted() {},
    methods: {
      // 添加获取整改方案数据的方法
      getSchemeData() {
        if (this.flowType === 'ZGFA' && this.$refs.SchemeInfo) {
          return {
            issuesList: this.$refs.SchemeInfo.issuesList || [],
          }
        }
        return null
      },
      getEvidenceData() {
        if (this.flowType === 'SJQZD' && this.$refs.evidenceDetail) {
          return {
            evidenceOpinion:
              this.$refs.evidenceDetail.formData.evidenceOpinion || '',
          }
        }
        return null
      },
      // 调用审计取证单的保存方法
      async saveEvidenceData() {
        if (this.flowType === 'SJQZD' && this.$refs.evidenceDetail) {
          return await this.$refs.evidenceDetail.add()
        }
        return true
      },
      getDefault(viewRef, nextNodeName, nextStepId) {
        console.log('🚀 ~ getDefault ~ nextStepId:', nextStepId)
        // 默认获取数据方式
        this.$nextTick(() => {
          this.$refs[viewRef].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status,
            nextNodeName,
            this.flowType,
            nextStepId
          )
        })
      },
      getTblCyhwUnit() {
        //传入id，编辑器获取内容
        this.$store.commit('acl/contractidd', this.formData.contractid)
      },
      getIsFWRY() {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        let type = false
        let fwryType = false
        if (userInfo.roleNames) {
          let list = userInfo.roleNames.split(',')
          list.map((item) => {
            //
            if (item.indexOf('法务') != -1) {
              type = true
            }
            if (item.indexOf('法务人员') != -1) {
              fwryType = true
            }
          })
        }

        this.fwryType = fwryType
        this.fwType = type
        this.$forceUpdate()
      },
      async getOAList(contractid) {
        const oaList = await getCyhwUnitOAList({
          contractId: contractid,
        })
        this.tableDataOA = oaList.data
        //
      },
      async getOATicket() {
        const res = await getOaurl()
        this.ticket = res.data.ticket
        this.oaurl = res.data.oaurl
          ? res.data.oaurl.substring(0, res.data.oaurl.length - 1)
          : ''
        //
      },
      async fetchItem(row, formId) {
        console.log(22222222, '我是第二层')

        const res = await getContractItem({
          contractId: this.isWdcy ? formId : row.taskid,
          flowId: row.flowId || '',
          flowname: row.recordtype || this.formData.recordtype,
        })

        // this.getOAList(row.contractid)
        // this.getOATicket()
        const fileres = await contractPdfList({
          contractId: row.contractid,
          pageNumber: 1,
          pageSize: 20,
        })
        this.pdfList = (fileres && fileres.data.tlist) || []
        if (row.contractid) {
          const fileres2 = await contractExamList({
            id: row.contractid,
            pageNumber: 1,
            pageSize: 20,
          })
          this.pdfList2 = fileres2.data.tlist || []
        }
        this.getIsFWRY()
        if (res.data) {
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data && res.data.tcu && res.data.tcu[key]
          })
          this.formData.entrustStaffName = res.data.tcu.entrustStaffName
          this.formData.entrustStaffId = res.data.tcu.entrustStaffId
          this.formData.flowId = res.data.flowid
          this.formData.attList = res.data.attList
          this.formData.nodeList = res.data.nodeList
          this.formData.typefl = res.data.tcu.typefl
          this.formData.informationList = res.data.informationList
          this.formData.signingList = res.data.signingList
          const { payList, parentList, colList } = res.data
          this.formData.payList = payList
          this.formData.parentList = parentList
          this.formData.colList = colList

          this.formData.oppositeList = res.data.tcu.budgetList
          const { bankinfo } = res.data.tcu
          if (bankinfo) {
            this.formData.bankaccount = bankinfo.bankaccount
            this.formData.bankkhyh = bankinfo.bankkhyh
          }
          if (!this.isSendBack) {
            this.$refs['CreateDetail'].showDetail(
              this.formData,
              this.formData.contracttype,
              true
            )
          }
        }

        this.$forceUpdate()
      },
      showDetail(
        row,
        type,
        isWdcy,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        status
      ) {
        console.log(
          row,
          type,
          isWdcy,
          formId,
          flowtaskinfoflowid,
          ymFromId,
          status
        )
        const {
          formData,
          formFields,
          subFields,
          showSubFields,
          subTitle,
          currentEdit,
        } = comboFields(type, false)
        this.formData = formData
        this.currentEdit = currentEdit
        this.formFields = formFields
        this.subFields = subFields
        this.showSubFields = showSubFields
        this.subTitle = subTitle
        this.getcontracttype = formData.contracttype
        this.formId = formId
        this.ymFromId = ymFromId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.status = status
        this.title = '查看'
        this.disabled = true

        this.dialogFormVisible = true

        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        var that = this
        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (
          (row.cystaffid == userInfo.staffid &&
            row.cystate === '需调整' &&
            !this.isWdcy) ||
          this.isWfqdedit
        ) {
          that.isSendBack = true
        } else {
          that.isSendBack = false
        }

        if (this.flowType == 'TblCyhwUnit') {
          if (
            row.createuser == userInfo.staffid &&
            this.isWdcy &&
            this.isWfqdedit
          ) {
            that.isSendBack = true
            console.log('sssss1')
          } else {
            that.isSendBack = false
            console.log('sssss2')
          }
        }

        this.fetchItem(row, formId)
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
      async getXDFGL(formId) {
        const {
          data: { attList, bankInfoList, budget },
        } = await getOpposite({
          flowId: '622322',
          budgetId: formId,
        })

        Object.keys(this.XDFWHformData).forEach((key) => {
          this.XDFWHformData[key] = budget[key]
        })
        const {
          pstartdate,
          penddate,
          createStaff: { username },
          createtime,
        } = budget
        this.XDFWHformData.pdate = [pstartdate, penddate]
        this.XDFWHformData.staffid = username
        this.XDFWHformData.date = createtime
        this.XDFWHformData.attList = attList
        this.XDFWHformData.bankInfoList = bankInfoList
        this.XDFWHformData.penddateStr = penddate
        this.XDFWHformData.pstartdateStr = pstartdate
        // 重置localList
        // this.localList = []
        this.XDFWHcurrent = budget

        if (this.isSendBack) {
          this.$refs.XDFGLedit.showEdit(
            budget,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        }
      },
      async getXDFWH(formId) {
        const {
          data: { attList, bankInfoList, budget },
        } = await getOpposite({
          flowId: '622322',
          budgetId: formId,
        })

        Object.keys(this.XDFWHformData).forEach((key) => {
          this.XDFWHformData[key] = budget[key]
        })
        const {
          pstartdate,
          penddate,
          createStaff: { username },
          createtime,
        } = budget
        this.XDFWHformData.pdate = [pstartdate, penddate]
        this.XDFWHformData.staffid = username
        this.XDFWHformData.date = createtime
        this.XDFWHformData.attList = attList
        this.XDFWHformData.bankInfoList = bankInfoList
        // 重置localList
        // this.localList = []
        this.XDFWHcurrent = budget
      },
      async getHTFB(formId) {
        const res = await getContractItem({
          contractId: formId,
          flowId: '733271',
          flowname: 'HTGL007',
        })

        // const res = await viewDealInfo({
        //   flowId: '733271',
        //   contractId: formId,
        // })
        this.HTFBformData = res.data.tcu
        if (this.isSendBack) {
          this.$nextTick(() => {
            this.$refs.HTFBedit.showEdit(
              res.data.tcu,
              '',
              formId,
              this.flowtaskinfoflowid,
              this.ymFromId,
              this.status
            )
          })
        }
      },
      async getHTYY(formId) {
        if (!this.isSendBack) {
          this.$nextTick(() => {
            this.$refs['htyyEdit'].showDetail({ budgetid: formId })
          })
        } else {
          this.$nextTick(() => {
            this.$refs['htyyEdit'].showEdit(
              { budgetid: formId },
              1,
              formId,
              this.flowtaskinfoflowid,
              this.ymFromId,
              this.status
            )
          })
        }
      },
      async getHTBG(formId) {
        if (!this.isSendBack) {
          console.log('detail')
          this.$nextTick(() => {
            this.$refs['HTBGDetail'].showDetail({ contractid: formId }, '')
          })
        } else {
          console.log('edit')
          this.$nextTick(() => {
            this.$refs['HTBGEdit'].showEdit(
              { contractid: formId },
              '',
              this.formId,
              this.flowtaskinfoflowid,
              this.ymFromId,
              this.status
            )
          })
        }
      },
      async getHTJY(formId) {
        const res = await getContractDetail({
          lendId: formId,
        })
        this.$refs['borrowDetail'].showDetail(
          res.data.lend,
          this.formData.contracttype,
          this.formId,
          this.flowtaskinfoflowid,
          this.ymFromId,
          this.status
        )
      },
      async getHMDGL(formId) {
        const res = await getBlackCounterPartInfo({ blackid: formId })
        if (!this.isSendBack) {
          this.$refs['MaintainBlackDetail'].showDetail(res.data)
        } else {
          this.$refs['MaintainBlackEdit'].showEdit(
            res.data,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        }
      },
      getZGFA(formId) {
        this.$nextTick(async () => {
          const res = await getRectificationPlanDetail({ planId: formId })
          await this.$refs['SchemeInfo'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            res.data,
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status,
            this.isWdcy,
            this.flowType
          )
        })
      },
      getZGBG() {
        this.getDefault('ReportInfo')
      },
      getZGZZLS(formId) {
        this.$nextTick(async () => {
          const res = await getRectificationImplDetailInfo({ implId: formId })
          await this.$refs['practicableForm'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            res.data,
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status,
            this.flowTaskNodeList
          )
        })
      },
      getZGPJ(formId) {
        this.$nextTick(async () => {
          const res = await getZgzzeEvaluationDetail({ evalId: formId })
          await this.$refs['valuationForm'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            res.data,
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      getJHGL(formId) {
        this.$nextTick(() => {
          this.$refs['planDetail'].showEdit(
            { planid: formId },
            !this.isSendBack,
            null,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status,
            this.flowType
          )
        })
      },

      getSJRNGL(formId) {
        this.$nextTick(() => {
          this.$refs['Person'].showEdit(
            { staffid: formId },
            this.isSendBack ? '修改' : '查看',
            null,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status,
            this.flowType
          )
        })
      },
      getPJGL(formId) {
        this.$nextTick(() => {
          this.$refs['Evaluations'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status,
            this.flowType
          )
        })
      },
      getSJXMGL(formId) {
        this.$nextTick(() => {
          this.$refs['Project'].showEdit(
            { projectId: formId },
            this.isSendBack ? false : true,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status,
            this.flowType
          )
        })
      },
      async getWDDG() {
        const res = await myDraftDetail({ sheetid: this.formId })

        this.$refs['Papers'].showEdit(
          this.isSendBack ? 'edit' : 'detail',
          res.data,
          this.formId,
          this.flowtaskinfoflowid,
          this.ymFromId,
          this.userId,
          this.isWfqdedit,
          this.status,
          this.flowType
        )
      },
      getNDJH(formId) {
        this.$nextTick(() => {
          this.$refs['ndjhView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      getJYSXSH(formId) {
        this.$nextTick(() => {
          this.$refs['jysxshView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.userId,
            this.isWfqdedit,
            this.status
          )
        })
      },
      getZDSH(formId) {
        this.$nextTick(() => {
          this.$refs['zdshView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.userId,
            this.isWfqdedit,
            this.status
          )
        })
      },
      getZYSQZX(formId, type) {
        this.getZYSQ(formId, type)
      },
      getZYSQ(formId, type) {
        this.$nextTick(() => {
          //
          this.$refs['gslsView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.userId,
            this.isWfqdedit,
            type,
            this.status
          )
        })
      },
      getQTWJBS(formId) {
        this.$nextTick(() => {
          this.$refs['qtwjbsView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      getZFLGW(formId) {
        this.$nextTick(() => {
          this.$refs['flgwView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      getFWJGJFZR(formId) {
        this.$nextTick(() => {
          this.$refs['fwjgfzrView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      getFWRY(formId) {
        this.$nextTick(() => {
          this.$refs['fwryView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      getJFDJ(formId) {
        this.$nextTick(() => {
          this.$refs['disputeEdit'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.userId,
            this.isWfqdedit,
            this.status
          )
        })
      },
      getXSGCJG(formId, type) {
        this.getXSGC(formId, type)
      },
      getXSGC(formId, type) {
        this.$nextTick(() => {
          this.$refs['consultEdit'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            type ? true : false,
            this.status
          )
        })
      },
      async getSJQZD() {
        const data = await imPlementOrderDetail({
          certificateId: this.formId,
        })
        this.$nextTick(() => {
          this.$refs['evidenceDetail'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            data.data.certificate,
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status,
            this.nextNodeName,
            this.flowType
          )
        })
      },
      async getSJJYK(formId) {
        this.$nextTick(() => {
          this.$refs['sjjykDetail'].showEdit(
            this.isSendBack ? '修改' : '详情',
            { jykid: formId },
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status,
            this.flowType
          )
        })
      },
      async getZGLS(formId) {
        const data = await getSolutionDetail({ solutionid: formId })
        this.$nextTick(() => {
          this.$refs['zglsDetail'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            data.data,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      async getDAJY(formId) {
        this.$nextTick(() => {
          this.$refs['dajyDetail'].showEdit(
            this.isSendBack ? '编辑' : '详细',
            { id: formId },
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      async getZXGL(formId) {
        this.$nextTick(() => {
          this.$refs['zxglView'].show(
            null,
            this.isSendBack ? '编辑' : '详细',
            {
              id: formId,
            },
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      async getSSHZS(formId) {
        this.$nextTick(() => {
          this.$refs['lawsuitEdit'].showEdit({
            proceedid: formId,
            type: this.isSendBack ? 'edit' : 'detail',
            formId: formId,
            flowtaskinfoflowid: this.flowtaskinfoflowid,
            ymFromId: this.ymFromId,
            status: this.status,
          })
        })
      },
      async getPFJH(formId) {
        this.$nextTick(() => {
          this.$refs['pfjhEdit'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            {
              popularizeLawPlanId: formId,
            },
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      async getHTYJ(formId) {
        this.$nextTick(() => {
          this.$refs['TansfercontractEdit'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            {
              id: formId,
            },
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status
          )
        })
      },
      getNDKH(formId) {
        this.$nextTick(() => {
          this.$refs['ndkhView'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            {
              annualExamineId: formId,
            },
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status,
            this.flowType
          )
        })
      },

      // 审计实施

      getSJSSWDDG(formId) {
        const isFQRFH = this.flowTaskInfo.thisStep === '发起人复核'
        this.$nextTick(() => {
          this.$refs['sjsswddg'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status,
            this.flowTaskInfo,
            isFQRFH
          )
        })
      },
      getlrjjzrsq(formId) {
        this.$nextTick(() => {
          this.$refs['lrjjzrsq'].showEdit(
            { jdid: this.formId },
            this.isSendBack ? 'edit' : 'detail',
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status
          )
        })
      },
      getNotice(formId) {
        this.$nextTick(() => {
          this.$refs['notice'].showEdit(
            { adviceid: this.formId },
            this.isSendBack ? 'edit' : 'detail',
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status
          )
        })
      },
      getSJJGWSCS() {
        this.getDefault('sjjgwscs', this.nextNodeName, this.nextStepId)
        // const data = await reportDetail({ reportid: this.formId })
        // console.log('data', data)
        // this.$nextTick(() => {
        //   this.$refs['notice'].showEdit(
        //     this.isSendBack ? 'edit' : 'detail',
        //     data.data,
        //     this.formId,
        //     this.flowtaskinfoflowid,
        //     this.ymFromId,
        //     this.isWfqdedit,
        //     this.status
        //   )
        // })
      },
      async getPJLX() {
        this.$nextTick(() => {
          this.$refs['pjlx'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status,
            this.flowType
          )
        })
      },
      async getPJBG() {
        this.$nextTick(() => {
          this.$refs['pjbg'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status,
            this.flowType
          )
        })
      },
      async getWTFX() {
        this.$nextTick(() => {
          this.$refs['wtfx'].show(
            this.isSendBack ? 'edit' : 'detail',
            this.formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status,
            this.flowType
          )
        })
      },
      getWTQD() {
        this.getDefault('wtqd')
      },
      getWTZG() {
        this.getDefault('wtzg')
      },
      getHXZGSP() {
        this.getDefault('wtzgHX')
      },
      getGZHF() {
        this.getDefault('gzhf')
      },
      getSJJGQRS() {
        this.getDefault('sjjgqrd')
      },
      getSJXMZK() {
        this.getDefault('sjxmzk')
      },
      getSJGZJL() {
        this.getDefault('sjgzjl')
      },
      getSJDDJL() {
        this.getDefault('sjddjl')
      },
      getSJDDBG() {
        this.getDefault('sjddbg')
      },
      getSJXMQKB() {
        this.getDefault('sjxmqkb')
      },
      getXCSCZYNR() {
        this.getDefault('scsczynr')
      },
      // 通知变更
      getTZBG() {
        this.getDefault('tzbg')
      },
      // 质量分析报告
      getZLFXBG() {
        this.getDefault('zlfxbg')
      },
      getRYQJD() {
        this.getDefault('ryqjdViews')
      },
      getYXSYD() {
        this.getDefault('yxsydViews')
      },
      getZCTJSQ() {
        this.getDefault('zctjsqViews')
      },
      getBGJYZC() {
        this.getDefault('bgjfzcViews')
      },
      getXLFZC() {
        this.getDefault('xlfzcViews')
      },

      //央企内审-综合管理
      //外网代理
      getWWDLFWGL() {
        this.getDefault('wfqddeal')
      },
      getHYSQ() {
        this.getDefault('hysq')
      },
      getHYGL() {
        this.getDefault('hygl')
      },
      getIPDZGL() {
        this.getDefault('ipViews')
      },
      getNBWZSQD() {
        this.getDefault('nbwzsqd')
      },
      getWPSQD() {
        this.getDefault('wpsqd')
      },
      getXJD() {
        this.getDefault('xjd')
      },
      getWPXJD() {
        this.getDefault('wpxjd')
      },
      getDBTZD() {
        this.getDefault('dbtzd')
      },
      getZSYYXGL() {
        this.getDefault('email')
      },
      getSZZSGL() {
        this.getDefault('szzsgl')
      },
      getVPNZHGL() {
        this.getDefault('vpnzhgl')
      },
      getNBWJCB() {
        this.getDefault('nbwjcb')
      },
      getQXSQ() {
        this.getDefault('qxsq')
      },
      // 央企内审-计划编制
      getFGLDHZ() {
        this.getDefault('fgldhz')
      },
      getJSXMJBQK() {
        this.getDefault('jsxmjbqk')
      },
      getJSXMTZWCQK() {
        this.getDefault('jsxmtzwcqk')
      },
      getSJDWLRSJ() {
        this.getlrjjzrsq('lrjjzrsq')
      },
      getWWTJYJLR() {
        this.getDefault('wwtjyjlr')
      },
      getGCZXPXB() {
        this.getDefault('gczxpxb')
      },
      getEJJGRZLXJY() {
        this.getDefault('ejjgrzlxjy')
      },

      getJHCG() {
        this.getDefault('jhcgView')
      },
      getJHBA() {
        this.getDefault('jhba')
      },
      getSJLXJYTZ() {
        this.getDefault('sjlxjytz')
      },
      //需求建议表
      getXQJYB() {
        this.getDefault('xqjybEdit')
      },
      //服务需求表
      getFWXQB() {
        this.getDefault('fwxqbEdit')
      },
      //立项建议表
      getLXJYB() {
        this.getDefault('lxjybEdit')
      },
      //二级单位及成员单位离任审计
      getEJDWJCYDWLRSJ() {
        this.getDefault('lrjyjlrsjView')
      },
      //财务专项排序表
      getCWZXPXB() {
        this.getDefault('cwzxpxbEdit')
      },
      getJHCHUG() {
        this.getDefault('jhfirstView')
      },
      getJH() {
        this.getDefault('jhlistView')
      },
      getJHXQ() {
        this.getDefault('jhxqView')
      },
      getGCSJXMAP() {
        this.getDefault('gcsjxmapbView')
      },
      getCWSJXMAP() {
        this.getDefault('cwsjxmapbView')
      },
      getSQDCBG() {
        this.getDefault('sqdcbgView')
      },
      getGZFA() {
        this.getDefault('gzfaView')
      },
      getSJXMB() {
        this.getDefault('sjxmb')
      },
      getSJXMZD() {
        this.getDefault('sjxmzdlc')
      },
      getSSFA() {
        this.getDefault('ssfa')
      },
      getJHBZ() {
        this.getDefault('jhbz')
      },
      getSJTZ() {
        this.getNotice('notice')
      },
      getXMYQSQ() {
        this.getDefault('xmyqsq')
      },
      getLXJYZYPG() {
        this.getDefault('lxjyzypg')
      },
      getRZMXTB() {
        this.getDefault('rzsjmx')
      },
      getSLBG() {
        this.getDefault('slbg')
      },
      getSJBGDG() {
        this.getDefault('sjbgdg')
      },
      getJHYJG() {
        this.getDefault('jhyjg')
      },
      getYQNSXMGD() {
        this.getDefault('xmck')
      },
      getYQNSDAJY() {
        this.getDefault('read')
      },
      getXMPYSB() {
        this.getDefault('xmpysb')
      },
      getXMPYSBFZ() {
        this.getDefault('xmpysbfz')
      },
      getSJTZSP() {
        this.getDefault('noticeapr')
      },
      getGWJD() {
        this.getDefault('gwjd')
      },
      getLWPX() {
        this.getDefault('lwpx')
      },
      getLLYJSB() {
        this.getDefault('llyjsb')
      },
      getSJJYS() {
        this.getDefault('sjjys')
      },
      getYGLQSP() {
        this.getDefault('yglqsp')
      },
      getZB() {
        this.getDefault('zb')
      },
      getYSJGWS() {
        this.getDefault('ysjgws')
      },
      getWTXSHC() {
        this.getDefault('wthc')
      },
      getWTXSSL() {
        this.getDefault('wtsl')
      },
      getCLJG() {
        this.getDefault('cljg')
      },
      getWTXSDZ() {
        this.getDefault('wtdz')
      },
      getFXCJ() {
        this.getDefault('fxcj')
      },
      getFXYD() {
        this.getDefault('fxyd')
      },
      getFXSJK() {
        this.getDefault('fxsjk')
      },
      getFXBG() {
        this.getDefault('fxbg')
      },
      getFXSCTZ() {
        this.getDefault('fxsctz')
      },
      getPGJH() {
        this.getDefault('pgjh')
      },
      getYDPGFXGB() {
        this.getDefault('ydpgfxgb')
      },
      getZDFXYDPG() {
        this.getDefault('zdfxydpg')
      },
      getNKQXD() {
        this.getDefault('nkqxd')
      },
      getNKQXGL() {
        console.log(this.getDefault('qxgl'))
        this.getDefault('qxgl')
      },
      // 添加重大风险创建审批方法
      getZDFXCJSP() {
        this.getDefault('zdfxcj')
      },
      // 重大风险填报
      getZDFXTBSP() {
        this.getDefault('zdfxtb')
      },
      // 集团评估计划
      getJTFXPGJH() {
        this.getDefault('jtfxpgjh')
      },
      // 集团内控测试计划
      getJTNKCSJH() {
        this.getDefault('jtnkcsjh')
      },
      // 集团内控测试计划
      getSKGLLC() {
        this.getDefault('skgl')
      },
      // 付款管理
      getFKGLLC() {
        this.getDefault('fkgl')
      },
      // 风险监测指标创建
      getFXJCZBCJ() {
        this.getDefault('fxjczbcj')
      },
      // 风险监测指标填报
      getFXJCZBTB() {
        this.getDefault('fxjczbtb')
      },
      // 系统信息流程
      getXTXGQR() {
        this.getDefault('xtxgqr')
      },
      // 初步核实
      getSHBG() {
        this.getDefault('shbg')
      },
      // 违规核查
      getWGHS() {
        this.getDefault('wghs')
      },
      // 移送函
      getWGYS() {
        this.getDefault('wgys')
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .el-table {
    margin-top: 10px;
    margin-bottom: 18px;
  }
</style>
