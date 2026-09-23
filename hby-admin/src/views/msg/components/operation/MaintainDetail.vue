<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    :modal-append-to-body="false"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <!-- <MaintainDetailContent :current="current" :form-data="formData" /> -->
    <MaintainDetailContent
      v-if="!isSendBack"
      :current="current"
      :form-data="formData"
    />
    <MaintainDetailContentEdit
      v-else
      :form-data="formData"
      :isSendBack="isSendBack"
      ref="edit"
    />
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
  <!-- <MaintainDetailContent v-else :current="current" :form-data="formData" /> -->
  <div v-else>
    <MaintainDetailContent
      v-if="!isSendBack"
      :current="current"
      :form-data="formData"
    />
    <MaintainDetailContentEdit v-else :form-data="formData" ref="edit" />
  </div>
</template>

<script>
  import { getOpposite } from '@/api/contract/opposite'
  import MaintainDetailContent from '@/views/contract/opposite/components/MaintainDetailContent.vue'
  import MaintainDetailContentEdit from '@/views/contract/opposite/components/MaintainDetailContentEdit.vue'

  export default {
    name: 'MaintainDetail',
    components: { MaintainDetailContent, MaintainDetailContentEdit },
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
          // flowId: 622322,
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
          bankInfoList: [],
        },
        title: '',
        dialogFormVisible: false,
        current: null,
        isSendBack: false,
      }
    },
    created() {},
    mounted() {},
    methods: {
      async fetchItem(row) {
        const {
          data: { attList, bankInfoList, budget },
        } = await getOpposite({
          flowId: '622322',
          budgetId: row.taskid,
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
        this.formData.flowId = '622322'
        // 重置localList
        this.localList = []
        this.current = budget
      },
      async showDetail(row, type, isWdcy) {
        this.formData.flowId = '622322'
        this.$set(this.formData, 'flowIds', 622322)
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        var that = this
        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (
          row.cystaffid == userInfo.staffid &&
          row.cystate === '需调整' &&
          !isWdcy
        ) {
          that.isSendBack = true
          this.$nextTick(() => {
            this.$refs['edit'].fetchItem(row)
          })
        }
        this.title = '查看'
        this.fetchItem(row)
        // this.formData.flowId = '622322'
        this.$set(this.formData, 'flowIds', 622322)
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped></style>
