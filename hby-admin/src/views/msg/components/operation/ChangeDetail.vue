<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <ChangeDetailContent
      v-if="!isSendBack"
      :current-edit="currentEdit"
      :form="formData"
      :form-fields="formFields"
      :show-sub-fields="showSubFields"
      :sub-fields="subFields"
      :sub-title="subTitle"
    />
    <ChangeDetailContentEdit
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
    <ChangeDetailContent
      v-if="!isSendBack"
      :current-edit="currentEdit"
      :form="formData"
      :form-fields="formFields"
      :show-sub-fields="showSubFields"
      :sub-fields="subFields"
      :sub-title="subTitle"
    />
    <ChangeDetailContentEdit
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
  </div>
</template>
<script>
  import { getContractItem } from '@/api/contract/manage'
  import ChangeDetailContent from '@/views/contract/contractManage/components/contractsEdit/ChangeDetailContent.vue'
  import ChangeDetailContentEdit from '@/views/contract/contractManage/components/contractsEdit/ChangeDetailContentEdit.vue'
  import { comboFields } from '@/views/contract/contractManage/components/contractsEdit/methods'

  export default {
    name: 'ChangeDetail',
    components: {
      ChangeDetailContent,
      ChangeDetailContentEdit,
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
      isWdcy: {
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
          flowId: 622325,
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
        title: '',
        dialogFormVisible: false,
        options: [],
        typeOptions: [],
        xdfOptions: [],
        changeOptions: [
          {
            label: '合同名称变更',
            value: '合同名称变更',
          },
          {
            label: '合同项目变更',
            value: '合同项目变更',
          },
          {
            label: '合同期限变更',
            value: '合同期限变更',
          },
          {
            label: '相对方变更',
            value: '相对方变更',
          },
          {
            label: '执行信息变更',
            value: '执行信息变更',
          },
          {
            label: '合同金额变更',
            value: '合同金额变更',
          },
          {
            label: '合同内容变更',
            value: '合同内容变更',
          },
          {
            label: '全部信息变更',
            value: '全部信息变更',
          },
        ],
        disabled: false,
      }
    },
    watch: {
      'formData.changetype'(val) {
        if (val) {
          if (val == '合同名称变更') {
            this.formFields['contractname'].disabled = false
            return
          }
          if (val == '合同项目变更') {
            this.formFields['contractitem'].disabled = false
            return
          }
          if (val == '合同期限变更') {
            this.formFields['startdate'].disabled = false
            this.formFields['enddate'].disabled = false
            return
          }
          if (val == '签约主体变更') {
            this.formFields['budgetname'].disabled = false
            return
          }
          if (val == '执行信息变更') {
            this.formFields['orgname'].disabled = false
            this.formFields['realname'].disabled = false
            return
          }
          if (val == '合同金额变更') {
            this.formFields['contractmoney'].disabled = false
            return
          }
          if (val == '合同内容变更') {
            this.formFields['momoconcat'].disabled = false
          }
          if (val == '全部内容变更') {
            Object.keys(this.formFields).forEach((key) => {
              this.formFields[key].disabled = false
            })
          }
        }
      },
      'formData.contracttype': {
        handler(val) {
          if (this.isSendBack === true) {
            this.$nextTick(() => {
              this.$refs['edit'].showEdit(
                this.formData,
                this.formData.contracttype
              )
            })
          }
        },
      },
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
      showDetail(row, type, isWdcy) {
        const {
          formData,
          formFields,
          subFields,
          showSubFields,
          subTitle,
          currentEdit,
        } = comboFields(type, true)
        this.formData = formData
        this.formFields = formFields
        this.formFields
        this.currentEdit = currentEdit
        this.subFields = subFields
        this.showSubFields = showSubFields
        this.subTitle = subTitle

        this.title = '查看'
        this.disabled = true
        this.fetchItem(row)
        this.dialogFormVisible = true

        let userInfo = JSON.parse(localStorage.getItem('userInfo'))

        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (
          row.cystaffid == userInfo.staffid &&
          row.cystate === '需调整' &&
          !isWdcy
        ) {
          this.isSendBack = true
        } else {
          this.isSendBack = false
        }
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
