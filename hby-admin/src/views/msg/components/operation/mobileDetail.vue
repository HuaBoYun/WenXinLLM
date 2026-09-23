<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <CreateDetailContent
      v-if="!isSendBack"
      :current-edit="currentEdit"
      :form="formData"
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
    <template #footer>
      <!-- <el-button @click="close">取 消</el-button> -->
    </template>
  </el-dialog>

  <div v-else>
    <div v-if="flowType == 'TblCyhwUnit'">
      <CreateDetailContent
        v-if="!isSendBack"
        :current-edit="currentEdit"
        :form="formData"
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
        :is-wfqdedit="isWfqdedit"
        ref="edit"
      />
    </div>
    <div v-if="flowType == 'XDFWH'">
      <MaintainDetailContent
        v-if="!isSendBack"
        :formData="XDFWHformData"
        :current="XDFWHcurrent"
      />
      <MaintainDetailContentEdit
        v-else
        ref="edit"
        :formData="XDFWHformData"
        :current="XDFWHcurrent"
      />
    </div>
    <div v-if="flowType == 'HTFB'">
      <TemplateDetailContent :formData="HTFBformData" v-if="!isSendBack" />
      <TemplateDetailContentEdit ref="edit" :formData="HTFBformData" v-else />
    </div>
    <div v-if="flowType == 'HTBG'">
      <ChangeDetailContent
        v-if="!isSendBack"
        :current-edit="HTBGcurrentEdit"
        :form="HTBGformData"
        :form-fields="HTBGformFields"
        :show-sub-fields="HTBGshowSubFields"
        :sub-fields="HTBGsubFields"
        :sub-title="HTBGsubTitle"
      />
      <ChangeDetailContentEdit
        v-else
        :current-edit="HTBGcurrentEdit"
        :form="HTBGformData"
        :form-fields="HTBGformFields"
        :show-sub-fields="HTBGshowSubFields"
        :sub-fields="HTBGsubFields"
        :sub-title="HTBGsubTitle"
        ref="edit"
      />
    </div>
    <div v-if="flowType == 'HTYY'">
      <ContractSealDetailContent
        :form-data="HTYYformData"
        :node="HTYYnode"
        v-if="!isSendBack"
      />
      <ContractSealDetailContentEdit
        v-else
        ref="edit"
        :form-data="HTYYformData"
        :node="HTYYnode"
      />
    </div>
    <!-- 黑名单管理 -->
    <div v-if="flowType == 'HMDGL'">
      <BlacklistEdit
        v-if="!isSendBack"
        ref="blacklistEdit"
        :isDialog="false"
        :isLiuCheng="false"
      />
      <BlacklistEdit
        ref="blacklistEdit"
        :isDialog="false"
        :isLiuCheng="true"
        v-else
      />
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
      <PlanDetail ref="planDetail" />
    </div>
    <!-- 审计人员管理 -->
    <div v-if="flowType == 'SJRNGL'">
      <PersonModal ref="Person" />
    </div>
    <!-- 评价管理 -->
    <div v-if="flowType == 'PJGL'">
      <EvaluationsModal ref="Evaluations" :isWfqdedit="isWfqdedit" />
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
    <div v-if="flowType == 'ZGLS'">
      <ZGLSDetail ref="zglsDetail" />
    </div>
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
  </div>
</template>

<script>
  import {
    getContractDetail,
    getContractItem,
    getContractSealDetail,
    viewDealInfo,
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
  import CreateDetailContentEdit from '@/views/contract/contractManage/components/contractsEdit/CreateDetailContentEdit.vue'
  import { comboFields } from '@/views/contract/contractManage/components/contractsEdit/methods'
  import TemplateDetailContent from '@/views/contract/contractManage/components/TemplateDetailContent'
  import TemplateDetailContentEdit from '@/views/contract/contractManage/components/TemplateDetailContentEdit'
  import BlacklistEdit from '@/views/contract/opposite/components/BlacklistEdit'
  import BlacklistEditEdit from '@/views/contract/opposite/components/BlacklistForm.vue'
  import MaintainDetailContent from '@/views/contract/opposite/components/MaintainDetailContent'
  import MaintainDetailContentEdit from '@/views/contract/opposite/components/MaintainDetailContentEdit'

  import EvaluationsModal from '@/views/msg/components/newProcess/evaluationsModal'
  import PapersDetail from '@/views/msg/components/newProcess/papersDetails.vue'
  import PersonModal from '@/views/msg/components/newProcess/personModal.vue'
  import ProjectDetail from '@/views/msg/components/newProcess/projectModal.vue'
  import EvidenceDetail from '@/views/msg/components/newProcess/auditEvidence.vue'
  import SJJYKDetail from '@/views/msg/components/newProcess/sjjykDetail.vue'
  import ZGLSDetail from '@/views/msg/components/newProcess/zglsDetails.vue'
  import DAJYDetail from '@/views/msg/components/newProcess/dajyDetail.vue'

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
  import TansfercontractEdit from '../mobile/TansfercontractEdit'
  import ndkhView from './ndkhView.vue'

  export default {
    name: 'CreateDetail',
    components: {
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
      ZGLSDetail,
      DAJYDetail,
      zxglView,
      lawsuitEdit,
      pfjhEdit,
      TansfercontractEdit,
      ndkhView,
    },
    provide() {
      return {
        fatherFetchItem: this.fetchItem,
      }
    },
    props: {
      formId: {
        type: Number,
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
        type: Number,
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
    },
    data() {
      return {
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
        ymFromId: '', //我发起的获取的流程表单id
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
          date: undefined,
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
          contractname: '',
          contracttype: '',
          recordtype: 'HTGL007',
          momoconcat: '',
          describe: '',
        },
        HTBGcurrentEdit: 'moren',
        HTBGformData: {
          flowid: 622325,
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
          nodeList: [],
          informationList: [],
        },
        HTBGformFields: {
          changetype: undefined,
        },
        HTBGshowSubFields: false,
        HTBGsubFields: [
          { value: 'infoname', label: '货物名称' },
          { value: 'infodesc', label: '运输说明' },
          { value: 'infoxh', label: '规格型号' },
          { value: 'infoprice', label: '货物价值' },
        ],
        HTBGsubTitle: '',
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
      }
    },
    computed: {
      myFlowType() {
        if (this.flowType == 'TblCyhwUnit') {
          //传入id，编辑器获取内容
          this.$store.commit('acl/contractidd', this.formData.contractid)
        } else if (this.flowType == 'XDFWH') {
          this.getOppositeInfo(this.formId)
        } else if (this.flowType == 'HTFB') {
          this.getViewDealInfo(this.formId)
        } else if (this.flowType == 'HTYY') {
          this.getContractSealDetailInfo(this.formId)
        } else if (this.flowType == 'HTBG') {
          this.getContractItemInfo(this.formId)
        } else if (this.flowType == 'HTJY') {
          this.getBorrowDetail(this.formId)
        } else if (this.flowType == 'HMDGL') {
          this.getBlackCounterPartDetail(this.formId)
        } else if (this.flowType == 'JHGL') {
          this.getPlanDetail(this.formId)
        } else if (this.flowType == 'SJRNGL') {
          this.getPersonDetail(this.formId)
        } else if (this.flowType == 'PJGL') {
          this.getEvaluationsDetail(this.formId)
        } else if (this.flowType == 'SJXMGL') {
          this.getProjectDetail(this.formId)
        } else if (this.flowType == 'WDDG') {
          this.getPapersDetail(this.formId)
        } else if (this.flowType == 'ZGLS') {
          this.getZGLSDetail(this.formId)
        } else if (this.flowType == 'SJQZD') {
          this.getEvidenceDetail(this.formId)
        } else if (this.flowType == 'SJJYK') {
          this.getSJJYKDetail(this.formId)
        } else if (this.flowType == 'DAJY') {
          this.getDAJYDetail(this.formId)
        } else if (this.flowType == 'NDJH') {
          this.getNDJH(this.formId)
        } else if (this.flowType == 'JYSXSH') {
          this.getJYSXSH(this.formId)
        } else if (this.flowType == 'ZDSH') {
          this.getZDSH(this.formId)
        } else if (this.flowType == 'ZYSQ') {
          this.getZYSQ(this.formId)
        } else if (this.flowType == 'ZYSQZX') {
          this.getZYSQ(this.formId)
        } else if (this.flowType == 'QTWJBS') {
          this.getQTWJBS(this.formId)
        } else if (this.flowType == 'ZFLGW') {
          this.getZFLGW(this.formId)
        } else if (this.flowType == 'FWJGJFZR') {
          this.getFWJGJFZR(this.formId)
        } else if (this.flowType == 'FWRY') {
          this.getFWRY(this.formId)
        } else if (this.flowType == 'JFDJ') {
          this.getJFDJ(this.formId)
        } else if (this.flowType == 'XSGC') {
          this.getXSGC(this.formId)
        } else if (this.flowType == 'XSGCJG') {
          this.getXSGC(this.formId, true)
        } else if (this.flowType == 'ZXGL') {
          this.getZXGL(this.formId)
        } else if (this.flowType == 'SSHZS') {
          this.getSSHZS(this.formId)
        } else if (this.flowType == 'PFJH') {
          this.getPFJH(this.formId)
        } else if (this.flowType == 'HTYJ') {
          this.getHTYJ(this.formId)
        } else if (this.flowType == 'NDKH') {
          this.getNDKH(this.formId)
        }

        return this.flowType
      },
    },
    watch: {
      myFlowType(newValue) {
        this.flowType = newValue
      },

      'formData.contracttype': {
        handler(val) {
          if (this.isSendBack == true) {
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
      'HTBGformData.changetype'(val) {
        if (val) {
          if (val == '合同名称变更') {
            this.HTBGformFields['contractname'].disabled = false
            return
          }
          if (val == '合同项目变更') {
            this.HTBGformFields['contractitem'].disabled = false
            return
          }
          if (val == '合同期限变更') {
            this.HTBGformFields['startdate'].disabled = false
            this.HTBGformFields['enddate'].disabled = false
            return
          }
          if (val == '签约主体变更') {
            this.HTBGformFields['budgetname'].disabled = false
            return
          }
          if (val == '执行信息变更') {
            this.HTBGformFields['orgname'].disabled = false
            this.HTBGformFields['realname'].disabled = false
            return
          }
          if (val == '合同金额变更') {
            this.HTBGformFields['contractmoney'].disabled = false
            return
          }
          if (val == '合同内容变更') {
            this.HTBGformFields['momoconcat'].disabled = false
          }
          if (val == '全部内容变更') {
            Object.keys(this.HTBGformFields).forEach((key) => {
              this.HTBGformFields[key].disabled = false
            })
          }
        }
      },
    },
    created() {},
    mounted() {},
    methods: {
      async fetchItem(row, formId) {
        const res = await getContractItem({
          contractId: this.isWdcy ? formId : row.taskid,
          flowId: row.flowId || '',
          flowname: row.recordtype || this.formData.recordtype,
        })
        if (res.data) {
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data && res.data.tcu && res.data.tcu[key]
          })
          this.formData.entrustStaffName = res.data.tcu.entrustStaffName
          this.formData.entrustStaffId = res.data.tcu.entrustStaffId
          this.formData.flowId = res.data.flowid
          this.formData.attList = res.data.attList
          this.formData.nodeList = res.data.nodeList
          this.formData.informationList = res.data.informationList
          this.formData.signingList = res.data.signingList
          const { payList, parentList, colList } = res.data
          this.formData.payList = payList
          this.formData.parentList = parentList
          this.formData.colList = colList
          const {
            counterpartno,
            budgetname,
            totaltmoney,
            projectstagegoal,
            counterpartaddress,
            counterpartphone,
            counterpartcode,
          } = res.data.tcu
          const oppositeList = {
            counterpartno,
            budgetname,
            totaltmoney,
            projectstagegoal,
            counterpartaddress,
            counterpartphone,
            counterpartcode,
          }
          this.formData.oppositeList = [oppositeList]
          const { bankinfo } = res.data.tcu
          if (bankinfo) {
            this.formData.bankaccount = bankinfo.bankaccount
            this.formData.bankkhyh = bankinfo.bankkhyh
          }
        }
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
          } else {
            that.isSendBack = false
          }
        }

        this.fetchItem(row, formId)
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
      async getOppositeInfo(formId) {
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
      async getViewDealInfo(formId) {
        const res = await viewDealInfo({
          flowId: '733271',
          contractId: formId,
        })

        this.HTFBformData = res.data.tcu

        //
      },
      async getContractSealDetailInfo(formId) {
        const res = await getContractSealDetail({
          budgetId: formId,
          flowId: '622324',
        })
        Object.keys(this.HTYYformData).forEach((key) => {
          this.HTYYformData[key] = res.data.tcpb[key]
        })
        this.HTYYformData.flowId = res.data.flowId
        this.HTYYnode = res.data.tcu
        this.HTYYformData.attList = res.data.signingList.map((i) => {
          return {
            attname: i.singingName,
            attsize: i.singingSize,
            uploader: i.uploaderName,
            attid: i.singingId,
          }
        })
      },
      async getContractItemInfo(formId) {
        const res = await getContractItem({
          contractId: formId,
          flowId: '622325',
          flowname: 'HTGL002',
        })

        const {
          formData,
          formFields,
          subFields,
          showSubFields,
          subTitle,
          currentEdit,
        } = comboFields(res.data.tcu.contracttype, true)
        this.HTBGformData = formData
        this.HTBGformFields = formFields
        this.HTBGformFields
        this.HTBGcurrentEdit = currentEdit
        this.HTBGsubFields = subFields
        this.HTBGshowSubFields = showSubFields
        this.HTBGsubTitle = subTitle

        Object.keys(this.HTBGformData).forEach((key) => {
          this.HTBGformData[key] = res.data.tcu[key]
        })
        this.HTBGformData.flowId = res.data.flowid
        this.HTBGformData.attList = res.data.attList
        this.HTBGformData.nodeList = res.data.nodeList
        this.HTBGformData.informationList = res.data.informationList
        this.HTBGformData.signingList = res.data.signingList
        const { payList, parentList, colList } = res.data
        this.HTBGformData.payList = payList
        this.HTBGformData.parentList = parentList
        this.HTBGformData.colList = colList
        const {
          counterpartno,
          budgetname,
          totaltmoney,
          projectstagegoal,
          counterpartaddress,
          counterpartphone,
          counterpartcode,
        } = res.data.tcu
        const oppositeList = {
          counterpartno,
          budgetname,
          totaltmoney,
          projectstagegoal,
          counterpartaddress,
          counterpartphone,
          counterpartcode,
        }
        this.HTBGformData.oppositeList = [oppositeList]
        const { bankinfo } = res.data.tcu
        if (bankinfo) {
          this.HTBGformData.bankaccount = bankinfo.bankaccount
          this.HTBGformData.bankkhyh = bankinfo.bankkhyh
        }
      },
      async getBorrowDetail(formId) {
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
      async getBlackCounterPartDetail(formId) {
        const res = await getBlackCounterPartInfo({ budgetId: formId })
        this.$refs['blacklistEdit'].showDetail(
          res.data.budget,
          this.formData.contracttype,
          this.formId,
          this.flowtaskinfoflowid,
          this.ymFromId,
          this.status
        )
      },

      getPlanDetail(formId) {
        this.$nextTick(() => {
          this.$refs['planDetail'].showEdit(
            { planid: formId },
            !this.isSendBack,
            null,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isSendBack,
            this.status
          )
        })
      },
      getPersonDetail(formId) {
        this.$nextTick(() => {
          this.$refs['Person'].showEdit(
            { staffid: formId },
            this.isSendBack ? '修改' : '查看',
            null,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      getEvaluationsDetail(formId) {
        this.$nextTick(() => {
          this.$refs['Evaluations'].showEdit(
            this.isSendBack ? '编辑' : '查看',
            { staffScoreid: formId },
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      getProjectDetail(formId) {
        this.$nextTick(() => {
          this.$refs['Project'].showEdit(
            { projectId: formId },
            this.isSendBack ? false : true,
            null,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      async getPapersDetail(formId) {
        const res = await myDraftDetail({ sheetid: formId })

        this.$refs['Papers'].showEdit(
          this.isSendBack ? 'edit' : 'detail',
          res.data,
          formId,
          this.flowtaskinfoflowid,
          this.ymFromId,
          this.userId,
          this.isWfqdedit,
          this.status
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
      async getEvidenceDetail(formId) {
        const data = await imPlementOrderDetail({
          certificateId: formId,
        })
        this.$nextTick(() => {
          this.$refs['evidenceDetail'].showEdit(
            this.isSendBack ? 'edit' : 'detail',
            data.data.certificate,
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.isWfqdedit,
            this.status
          )
        })
      },
      async getSJJYKDetail(formId) {
        this.$nextTick(() => {
          this.$refs['sjjykDetail'].showEdit(
            this.isSendBack ? '修改' : '详情',
            { jykid: formId },
            formId,
            this.flowtaskinfoflowid,
            this.ymFromId,
            this.status
          )
        })
      },
      async getZGLSDetail(formId) {
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
      async getDAJYDetail(formId) {
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
      // async getPFJH(formId) {
      //   this.$nextTick(() => {
      //     this.$refs['pfjhEdit'].showEdit({
      //       proceedid: formId,
      //       type: this.isSendBack ? 'edit' : 'detail',
      //       formId: formId,
      //       flowtaskinfoflowid: this.flowtaskinfoflowid,
      //       ymFromId: this.ymFromId,
      //     })
      //   })
      // },
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
            this.status
          )
        })
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
