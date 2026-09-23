<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-tabs
      v-model="activeName"
      class="demo-tabs"
      type="card"
      @tab-click="handleClick"
    >
      <el-tab-pane label="基本信息" name="first">
        <ReceivingDetailContent v-if="!isSendBack" :form-data="form" />
        <ReceivingDetailContentEdit
          v-else
          :form-data="form"
          :isSendBack="isSendBack"
          ref="edit"
        />

        <Approval
          v-show="show === 1"
          ref="approval"
          :process-data="processData"
          process-type="blprocesssk"
          :results="results"
          :spinfo="spinfo"
        />
      </el-tab-pane>
      <el-tab-pane label="审批查看" name="second">
        <img
          alt="审批图"
          :src="imgurl"
          style="margin-bottom: 20px; width: 90%"
        />
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column align="center" label="流程ID" prop="processName" />
          <el-table-column align="center" label="办理人" prop="approver" />
          <el-table-column
            align="center"
            label="办理角色"
            prop="approvalRole"
          />
          <el-table-column align="center" label="办理结果	" prop="result" />
          <el-table-column align="center" label="办理意见" prop="examination" />
          <el-table-column
            align="center"
            label="办理时间"
            prop="approvaldate"
          />
          <el-table-column
            align="center"
            label="下一步：办理人/办理角色"
            prop="handle"
          />
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script>
  import {
    skgltosptzgl,
    tosptzglinfo,
    viewCollectionManagemen,
  } from '@/api/contract/financing'
  import Approval from '@/views/msg/components/options/Approval'
  import ReceivingDetailContent from '@/views/contract/financing/components/ReceivingDetailContent.vue'
  import ReceivingDetailContentEdit from '@/views/contract/financing/components/ReceivingDetailContentEdit.vue'

  export default {
    name: 'DraftEdit',
    components: {
      Approval,
      ReceivingDetailContent,
      ReceivingDetailContentEdit,
    },
    data() {
      return {
        spinfo: {},
        results: undefined, // 操作按钮
        contract: undefined,
        invoicestatusTextArr: [
          '未开票',
          '已开票',
          '未收款',
          '已收款',
          '已退票',
        ],
        activeName: 'first',
        form: {},
        // rules: {
        //   rule: [
        //     {
        //       required: true,
        //       message: '请输入账号',
        //       trigger: 'blur',
        //     },
        //   ],
        // },
        list: [],
        list1: [],
        listLoading: true,
        title: '业务审批',
        dialogFormVisible: false,
        imgurl: '',
        show: 0,
        processData: {},
        isSendBack: false,
      }
    },
    watch: {
      imgurl: {
        handler(val) {
          this.imgurl = this.imgurl
        },
      },
      list1: {
        handler(val) {
          this.listLoading = true
          this.getList2()
        },
      },
    },
    created() {},
    methods: {
      async getList(row) {
        this.listLoading = true
        const { data } = await tosptzglinfo({
          collectionId: row.collectionId,
          flowid: '765525',
        })
        this.processData = data
        this.spinfo = data
        this.list1 = data.cy
        if (data.results) {
          this.results = data.results
        } else {
          this.results = undefined
        }

        this.listLoading = false
      },
      async getList2() {
        this.listLoading = true
        var that = this
        if (this.list1.taskid) {
          const { data, url } = await skgltosptzgl({
            collectionId: that.list1.taskid,
            flowid: '765525',
            taskId: that.list1.businesskey,
          })
          that.$nextTick(() => {
            that.list = data
            that.imgurl = url
            that.listLoading = false
          })
        }
      },
      async fetchInfo(row) {
        this.listLoading = true
        const { collection } = await viewCollectionManagemen({
          collectionId: row.taskid,
        })

        this.form = collection
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        var that = this
        // 当viewOppsiteProcessInfo接口中的cystaffid与获取用户信息中的staffid相等且cystate等于需调整 基本信息改成可编辑 编号不可编辑
        if (row.cystaffid == userInfo.staffid && row.cystate == '需调整') {
          that.isSendBack = true
        }

        this.listLoading = false
      },
      handleClick(e) {
        if (e.label === '审批查看') {
          this.getList2()
        }
      },
      showEdit(row, type) {
        if (type == 'wddb') {
          this.show = 1
        }
        this.form = Object.assign({}, row)
        this.dialogFormVisible = true
        this.getList(row)
        this.getList2()
        this.fetchInfo(row)
        this.approval()
      },
      close() {
        this.dialogFormVisible = false
        this.activeName = 'first'
      },
      approval() {
        this.$nextTick(() => {
          this.$refs['approval'].showDetail()
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
