<!--
 * @Date: 2022-04-18 10:48:25
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 20:32:07
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/contractsEdit/CreateDetail.vue
-->
<!-- el-dialog 加上modal="false"（去掉遮罩层）  原因：合同用印->用印->详细 跳到这个页面 遮罩层重叠-->
<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :modal="false"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <CreateDetailContent
      ref="createDetailContent"
      :current-edit="currentEdit"
      :form="formData"
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
    <template #footer>
      <!-- <el-button @click="close">取 消</el-button> -->
    </template>
  </el-dialog>
  <CreateDetailContent
    v-else
    ref="createDetailContent"
    :current-edit="currentEdit"
    :form="formData"
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
</template>

<script>
  import {
    getContractItem,
    contractPdfList,
    getCyhwUnitOAList,
    getOaurl,
    contractExamList,
  } from '@/api/contract/manage'
  import CreateDetailContent from './CreateDetailContent.vue'
  import { comboFields } from './methods'

  export default {
    name: 'CreateDetail',
    components: {
      CreateDetailContent,
    },
    provide() {
      return {
        fatherFetchItem: this.fetchItem,
      }
    },
    props: {
      isDialog: {
        type: Boolean,
        default: true,
      },
      //我的代办的审批单过来的参数
      info: {
        type: Object,
      },
    },
    data() {
      return {
        tableDataOA: [],
        pdfList: [],
        pdfList2: [],
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
          watermarkList: [],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
        typeOptions: [],
        xdfOptions: [],
        disabled: false,
        ticket: '',
        oaurl: '',
      }
    },
    created() {
      //如果是我的代办过来的参数，请求方法
      if (this.info) {
        this.showDetail(this.info, '')
      }
    },
    methods: {
      async getOAList(contractid) {
        const oaList = await getCyhwUnitOAList({
          contractId: contractid,
        })
        this.tableDataOA = oaList.data
      },
      async getOATicket() {
        const res = await getOaurl()
        this.ticket = res.data.ticket
        this.oaurl = res.data.oaurl.substring(0, res.data.oaurl.length - 1)
        //
      },
      //处理数据
      async fetchItem(row) {
        // const ss = await getattInfo({
        //   contractId: row.contractid,
        // })
        const res = await getContractItem({
          contractId: row.contractid,
          flowId: row.flowid,
          flowname: row.recordtype,
        })
        // this.getOAList(row.contractid)
        // this.getOATicket()
        const fileres = await contractPdfList({
          contractId: row.contractid,
          pageNumber: 1,
          pageSize: 20,
        })
        this.pdfList = fileres.data.tlist || []

        const fileres2 = await contractExamList({
          id: row.contractid,
          pageNumber: 1,
          pageSize: 20,
        })
        this.pdfList2 = fileres2.data.tlist || []
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = res.data.tcu[key]
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
        this.formData.typefl = res.data.tcu.typefl
        this.formData.ismany = res.data.tcu.ismany
        this.formData.agreementcount = res.data.tcu.agreementcount
        // const {
        //   counterpartno,
        //   budgetname,
        //   totaltmoney,
        //   projectstagegoal,
        //   counterpartaddress,
        //   counterpartphone,
        //   counterpartcode,
        // } = res.data.tcu
        // const oppositeList = {
        //   counterpartno,
        //   budgetname,
        //   totaltmoney,
        //   projectstagegoal,
        //   counterpartaddress,
        //   counterpartphone,
        //   counterpartcode,
        // }
        // this.formData.oppositeList = [oppositeList]
        this.formData.oppositeList = res.data.tcu.budgetList
        const { bankinfo } = res.data.tcu
        if (bankinfo) {
          this.formData.bankaccount = bankinfo.bankaccount
          this.formData.bankkhyh = bankinfo.bankkhyh
        }
      },
      showDetail(row, type) {
        const {
          formData,
          formFields,
          subFields,
          showSubFields,
          subTitle,
          currentEdit,
        } = comboFields(type, false)
        this.formData = formData
        if (this.formData.contractzd == '1') {
          this.formData.contractzd = '一般'
        } else if (this.formData.contractzd == '2') {
          this.formData.contractzd = '重要'
        } else if (this.formData.contractzd == '3') {
          this.formData.contractzd = '重大'
        }
        this.currentEdit = currentEdit
        this.formFields = formFields
        this.subFields = subFields
        this.showSubFields = showSubFields
        this.subTitle = subTitle

        this.title = '查看'
        this.disabled = true
        this.fetchItem(row)
        this.dialogFormVisible = true
      },
      close() {
        // this.$refs['form'].resetFields()
        // this.formData = this.$options.data().formData
        this.$emit('close')
        this.$refs.createDetailContent.clear()
        this.$parent.showDetail = false
        this.dialogFormVisible = false
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
