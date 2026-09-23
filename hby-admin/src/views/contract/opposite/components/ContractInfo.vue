<!--
 * @Date: 2022-04-18 10:48:25
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-19 16:59:57
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/contractsEdit/CreateDetail.vue
-->
<template>
  <el-dialog
    v-if="isDialog"
    :close-on-click-modal="false"
    :modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <CreateDetailContent
      :current-edit="currentEdit"
      :form="formData"
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
    :current-edit="currentEdit"
    :form="formData"
    :form-fields="formFields"
    :show-sub-fields="showSubFields"
    :sub-fields="subFields"
    :sub-title="subTitle"
  />
</template>

<script>
  import { getContractItem } from '@/api/contract/manage'
  import CreateDetailContent from '@/views/contract/contractManage/components/contractsEdit/CreateDetailContent.vue'
  import { comboFields } from '@/views/contract/contractManage/components/contractsEdit/methods'

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
    },
    data() {
      return {
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
          flowId: 771398,
          flowid: 771398,
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
      }
    },
    created() {},
    methods: {
      async fetchItem(row) {
        const res = await getContractItem({
          contractId: row.contractid,
          flowId: row.flowid,
          flowname: row.recordtype,
        })
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = res.data.tcu[key]
        })
        this.formData.flowId = res.data.flowid
        this.formData.attList = res.data.attList || []
        this.formData.nodeList = res.data.nodeList
        this.formData.informationList = res.data.informationList
        this.formData.signingList = res.data.signingList

        // 处理相对方无数据问题
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
      },
      showDetail(row, type) {
        const {
          formData,
          formFields,
          subFields,
          showSubFields,
          subTitle,
          currentEdit,
        } = comboFields(type, true)
        this.formData = formData
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
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
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
