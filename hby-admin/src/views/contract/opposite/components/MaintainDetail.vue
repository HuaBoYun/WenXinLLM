<!--
 * @Date: 2022-04-20 13:04:17
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-24 18:26:47
 * @FilePath: /hb-admin/src/views/contract/opposite/components/MaintainDetail.vue
-->
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
    <MaintainDetailContent :current="current" :form-data="formData" />
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
  <MaintainDetailContent v-else :current="current" :form-data="formData" />
</template>

<script>
import { getOpposite } from '@/api/contract/opposite'
import MaintainDetailContent from './MaintainDetailContent.vue'

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
        attList: [],
        bankInfoList: [],
      },
      title: '',
      dialogFormVisible: false,
      current: null,
    }
  },
  created() {},
  methods: {
    async fetchItem(row) {
      const {
        data: { attList, bankInfoList, budget },
      } = await getOpposite({
        flowId: row.flowid,
        budgetId: row.budgetid,
      })
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
