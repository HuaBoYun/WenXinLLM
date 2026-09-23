<!--
 * @Date: 2022-04-20 13:04:17
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-20 15:52:41
 * @FilePath: /hb-admin/src/views/contract/opposite/components/MaintainDetail.vue
-->
<template>
  <div>
    <el-dialog
      v-if="isDialog"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="dialogFormVisible = false"
      :close-on-click-modal="false"
    >
      <BlacklistDeatil
        :current="current"
        :formData="formData"
        :localList="localList"
      />
    </el-dialog>
    <BlacklistDeatil
      v-if="!isDialog && !isLiuCheng"
      :current="current"
      :formData="formData"
      :localList="localList"
    />
    <BlacklistEdit
      v-if="isLiuCheng && !isDialog"
      :current="current"
      :formData="formData"
      :localList="localList"
      :fromId="fromId"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
    />
  </div>
</template>

<script>
import { getOpposite, getPersonalData } from '@/api/contract/opposite'
import BlacklistDeatil from './BlacklistDeatil.vue'
import BlacklistEdit from './BlacklistEditEdit.vue'

export default {
  name: 'MaintainDetail',
  components: { BlacklistDeatil, BlacklistEdit },
  props: {
    isDialog: {
      type: Boolean,
      default: true,
    },
    isLiuCheng: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now()
        },
      },
      localList: [], // 本地缓存新增的附件列表
      formData: {
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
        pstartdateStr: undefined,
        penddateStr: undefined,
        cerType: 0,
        blacktype: 2,
        effectdate: undefined,
      },
      title: '',
      dialogFormVisible: false,
      current: null,
      fromIdcopy: 0,
      fromId: null,
      flowtaskinfoflowid: null,
      ymFromId: null,
    }
  },
  created() {},
  methods: {
    async fetchItem(row) {
      const {
        data: { attList },
      } = await getOpposite({
        flowId: row.flowid,
        budgetId: row.budgetid,
      })
      this.formData.attList = attList
      // 重置localList
      this.localList = []
    },
    showDetail(row, contracttype, fromId, flowtaskinfoflowid, ymFromId) {
      this.fetchItem(row)
      this.fetchPersonalData()

      this.title = '查看'
      Object.keys(this.formData).forEach((key) => {
        this.formData[key] = row[key]
      })
      const { pstartdate, penddate } = row
      this.formData.pdate = [pstartdate, penddate]
      this.formData.penddateStr = pstartdate
      this.formData.pstartdateStr = penddate
      this.current = row
      this.formData.flowId = 622322
      this.dialogFormVisible = true

      if (fromId) {
        this.fromId = fromId
        this.fromIdcopy = fromId // fromId为-1时，拷贝一份
      }
      if (flowtaskinfoflowid) {
        this.flowtaskinfoflowid = flowtaskinfoflowid
      }
      if (ymFromId) {
        this.ymFromId = ymFromId
      }
    },
    close() {
      this.$refs['form'].resetFields()
      this.formData = this.$options.data().formData
      this.dialogFormVisible = false
    },
    //请求数据
    async fetchPersonalData() {
      const res = await getPersonalData({
        flowId: this.formData.flowId,
      })
      const {
        counterpartno,
        createDate,
        staffInfo: { realname },
      } = res.data

      this.formData.staffid = realname
      this.formData.date = createDate
      // return {
      //   counterpartno,
      //   realname,
      //   createDate,
      // }
    },
  },
}
</script>
<style scoped></style>
