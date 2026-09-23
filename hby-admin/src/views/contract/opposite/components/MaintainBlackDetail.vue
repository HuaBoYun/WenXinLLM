<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    :append-to-body="true"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="120px">
        <el-col :span="12">
          <el-form-item label="黑名单类型" prop="blacktype">
            {{
              formData.blackRecord && formData.blackRecord.obrtype == 1
                ? '短期黑名单'
                : '长期黑名单'
            }}
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            v-if="formData.blackRecord && formData.blackRecord.obrtype == 1"
            label="黑名单有效期"
            prop="effectdate"
          >
            {{
              formData.blackRecord &&
              formatDay(formData.blackRecord.blackdeadtime)
            }}
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <MaintainDetailContent :current="current" :form-data="formData" />

    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>

  <div v-else>
    <el-row :gutter="15">
      <el-form ref="form" label-width="120px">
        <el-col :span="12">
          <el-form-item label="黑名单类型" prop="blacktype">
            {{
              formData.blackRecord.obrtype == 1 ? '短期黑名单' : '长期黑名单'
            }}
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            v-if="formData.blackRecord && formData.blackRecord.obrtype == 1"
            label="黑名单有效期"
            prop="effectdate"
          >
            {{
              formData.blackRecord &&
              formatDay(formData.blackRecord.blackdeadtime)
            }}
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <MaintainDetailContent :current="current" :form-data="formData" />
  </div>
</template>

<script>
  import { getOpposite } from '@/api/contract/opposite'
  import MaintainDetailContent from './MaintainDetailContent.vue'
  import { formatDay } from '@/utils'

  export default {
    name: 'MaintainDetail',
    components: { MaintainDetailContent },
    props: {
      isDialog: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        localList: [], // 本地缓存新增的附件列表
        formData: {
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
          pstartdateStr: undefined,
          penddateStr: undefined,
          cerType: 0,
          contacts: undefined,
          // contactsphone: undefined,
          // contactsadress: undefined,
          // contactsemail: undefined,
          // station: undefined,
          // callname: undefined,
          // remarks: undefined,
          effectdate: undefined,
          blacktype: undefined,
          attList: [],
          bankInfoList: [],
          blackRecord: {},
        },
        title: '',
        dialogFormVisible: false,
        current: null,
        formatDay,
      }
    },
    created() {},
    methods: {
      async fetchItem(row) {
        // const {
        //   data: { attList, bankInfoList, budget },
        // } = await getOpposite({
        //   flowId: row.flowid,
        //   budgetId: row.budgetid,
        // })
        const { attList, bankInfoList, budget } = row
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = budget[key]
        })
        const {
          pstartdate,
          penddate,
          createStaff: { username },
          createtime,
        } = budget
        this.formData.pdate = [pstartdate, penddate]
        this.formData.staffid = username
        this.formData.date = createtime
        this.formData.attList = attList
        this.formData.bankInfoList = bankInfoList
        this.formData.penddateStr = penddate
        this.formData.pstartdateStr = pstartdate
        // 重置localList
        this.localList = []
        this.current = budget
      },
      async showDetail(row) {
        console.log(row, '1231232132')
        this.title = '查看'
        this.fetchItem(row)
        this.formData.flowId = 622322
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped></style>
