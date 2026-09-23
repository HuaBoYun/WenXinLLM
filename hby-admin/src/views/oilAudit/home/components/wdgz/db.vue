<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading1" :data="list1">
      <el-table-column align="center" label="流程名称" prop="fullName" />
      <el-table-column align="center" label="任务名称" prop="flowName" />
      <el-table-column
        prop="status"
        label="流程状态"
        width="130"
        align="center"
      >
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.status == 1">等待审核</el-tag>
          <el-tag type="success" v-else-if="scope.row.status == 2">
            审核通过
          </el-tag>
          <el-tag type="danger" v-else-if="scope.row.status == 3">
            审核驳回
          </el-tag>
          <el-tag type="info" v-else-if="scope.row.status == 4">
            流程撤回
          </el-tag>
          <el-tag type="info" v-else-if="scope.row.status == 5">
            审核终止
          </el-tag>
          <el-tag type="warning" v-else>等待提交</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="创建时间"
        prop="creatorTime"
        :formatter="formatDate"
      ></el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail1(row)">审批</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm1.currentPage"
      :layout="layout"
      :page-size="queryForm1.pageSize"
      :page-sizes="pageSizes"
      :total="total1"
      @current-change="handleCurrentChange1"
      @size-change="handleSizeChange1"
    />

    <!-- <el-col v-if="showTitle" :span="24">
      <h3>我的待办</h3>
    </el-col> -->

    <Deal ref="deal" />
    <PaymentBanli ref="banlifk" />
    <ReceivingBanli ref="banlisk" />
    <Track ref="track" />
    <PapersDetails @fetch-data="fetchData" ref="paper" />
    <ConfirmDetail @fetch-data="fetchData" ref="confirm" />
    <ShenjiModal
      ref="shenji"
      @reload-data="reload"
      :UEditorCloudEdit="this.UEditorShenPiCloudEdit"
    />
    <Reference ref="reference" />
    <reportModal
      ref="report"
      @reload-data="reload"
      :UEditorCloudEdit="this.UEditorCloudEdit"
    />
    <EvaluationModal ref="evaluation" @fetch-data="fetchData" />
    <PersonModal ref="person" @fetch-data="fetchData" />

    <WddbDeal ref="Wddbdeal" @fetchData="fetchData" />
  </div>
</template>

<script>
  import PaymentBanli from '@//views/msg/components/operation/PaymentDetail'
  import { getToDoList, project_plan_list } from '@/oapi/setting/msg'
  import { handleHanlde, handleReport } from '@/oapi/workbench/auditTools'
  import { formatDate } from '@/utils/index'
  import PapersDetails from '@/views/msg/components/operation/papersDetails'
  import ReceivingBanli from '@/views/msg/components/operation/ReceivingDetail'
  import Reference from '@/views/msg/components/operation/ReferenceDetail.vue'
  import Deal from '@/views/msg/components/options/Deal'
  import Track from '@/views/msg/components/options/Track'
  import * as Dayjs from 'dayjs'
  import ConfirmDetail from '@/views/msg/components/operation/confirmDetail.vue'
  import EvaluationModal from '@/views/msg/components/operation/evaluationsModal.vue'
  import PersonModal from '@/views/msg/components/operation/personModal.vue'
  import reportModal from '@/views/msg/components/operation/reportModal.vue'
  import ShenjiModal from '@/views/msg/components/operation/shenpiModal.vue'
  import WddbDeal from '@/views/msg/components/options/WddbDeal.vue'
  export default {
    name: 'Wddb',
    components: {
      PaymentBanli,
      ReceivingBanli,
      Track,
      Deal,
      ShenjiModal,
      PapersDetails,
      Reference,
      ConfirmDetail,
      reportModal,
      EvaluationModal,
      PersonModal,
      WddbDeal,
    },
    props: {
      showTitle: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        list: [], //审计
        list1: [], //合同
        Dayjs: Dayjs,
        listLoading: false,
        listLoading1: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        total1: 0,
        pageSizes: [5, 10, 15, 20, 50, 100],
        queryForm: {
          pageNumber: 1,
          pageSize: 5,
        },
        queryForm1: {
          currentPage: 1,
          pageSize: 5,
        },
        type: '',
        showModal: false,
        UEditorCloudEdit: false,
        UEditorShenPiCloudEdit: false,
        timer: null,
        activeName: 'second',
      }
    },
    created() {
      this.fetchData1()
    },
    mounted() {
      this.$bus.$on('updateMsg', (type) => {
        if (type === 0) {
          this.fetchData1()
          this.$refs['Wddbdeal'].close()
        }
      })
      this.$nextTick(() => {
        if (this.$store.state.work.wddbState) {
          this.handleDetail(this.$store.state.work.wddbDetails)
        }
      })
    },

    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      //任意页面点击 小铃铛
      getRefsTowddb(e) {
        let num = 0
        let _this = this
        clearTimeout(this.timer)
        if (!_this.$refs['deal']) {
          _this.timer = setTimeout(() => {
            num = num + 1
            _this.getRefsTowddb(e)
          }, 800)
        } else {
          _this.handleDetail(e)
        }
      },
      resetUEditorStatus() {
        this.UEditorCloudEdit = false
        this.UEditorShenPiCloudEdit = false
      },
      reload() {
        this.resetUEditorStatus()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },

      handleSizeChange1(val) {
        this.queryForm1.pageSize = val
        this.fetchData1()
      },
      handleCurrentChange1(val) {
        this.queryForm1.currentPage = val
        this.fetchData1()
      },
      queryData1() {
        this.queryForm1.currentPage = 1
        this.fetchData1()
      },

      async fetchData1() {
        this.listLoading1 = true
        // const {
        //   data: { tlist, totalRecord },
        // } = await project_plan_list(this.queryForm);
        const {
          data: { list, totalCount },
        } = await getToDoList(this.queryForm1)
        this.list1 = list
        this.total1 = totalCount
        this.listLoading1 = false
      },
      handleDetail1(row) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.Wddbdeal.show(dataRow, true)
      },
      async handleDetail(row) {
        //判断用户信息，处理富文本框的状态
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        const ids = row.cyurl.split('=')[1]
        if (row.cytype == '收款管理' || row.cytype == '付款管理') {
          if (row.cytype == '收款管理') {
            let collectionid = row.cyurl.split('=')
            row.collectionId = collectionid[1]
            this.type = 'receiving'
            this.$refs['banlisk'].showEdit(row, 'wddb')
          }
          if (row.cytype == '付款管理') {
            let paymentid = row.cyurl.split('=')
            row.paymentId = paymentid[1]
            this.type = 'payment'
            this.$refs['banlifk'].showEdit(row, 'wddb')
          }
        } else {
          // if (row.cytype == '合同管理' && row.recordtype == 'HTGL007') {
          //   //合同范本
          //   this.type = 'sample'
          // }
          // if (row.cytype == '合同管理' && row.recordtype == 'HTGL002') {
          //   //合同起草
          //   this.type = 'create'
          // }
          // if (row.cytype == '合同管理' && row.recordtype == 'HTGL005') {
          //   //合同变更
          //   this.type = 'change'
          // }
          if (row.cytype == '合同管理') {
            //合同变更
            this.type = 'create'
          }

          if (row.cytype == '合同用印') {
            this.type = 'seal'
          }
          if (row.cytype == '合同借阅') {
            this.type = 'borrow'
          }
          if (row.cytype == '相对方维护') {
            this.type = 'opposite'
          }
          // 审计计划
          if (row.cytype == '计划审批') {
            this.type = 'IndexEdit'
          }
          if (row.cytype == '项目审批') {
            this.type = 'project'
          }
          if (row.cytype == '档案借阅') {
            this.$refs['reference'].showEdit('借鉴', row)
            return
          }
          if (row.cytype == '事实确认书') {
            this.type = 'confirm'
            this.$refs['confirm'].showEdit('事实确认书', row)

            return
          }
          if (row.cytype == '审计人员审核') {
            this.$refs['person'].showEdit(row, '查看')
            return
          }
          if (row.cytype == '评价审核') {
            this.$refs['evaluation'].showEdit('评价审核', row)
            return
          }
          if (row.cytype === '底稿复核') {
            this.type = 'paper'
            this.$refs['paper'].showEdit('复核', row)
            return
          }
          if (row.cytype === '审计通知书') {
            let res = await handleHanlde({
              adviceid: ids,
              cyId: row.cyid,
              taskId: row.taskid,
            })
            if (
              row.cystaffid == userInfo.staffid &&
              res.data.cy.cystate == '需调整'
            ) {
              this.UEditorShenPiCloudEdit = true
            } else {
              this.UEditorShenPiCloudEdit = false
            }
            this.$refs['shenji'].showEdit(row, '办理')
            return
          }
          if (
            row.cytype === '审计报告复核' ||
            row.cytype === '审计报告' ||
            row.cytype === '审计报告征求意见'
          ) {
            let res = await handleReport({
              cyId: row.cyid,
              reportid: ids,
              taskId: row.taskid,
            })
            if (
              row.cystaffid == userInfo.staffid &&
              res.data.cy.cystate == '需调整'
            ) {
              this.UEditorCloudEdit = true
            } else {
              this.UEditorCloudEdit = false
            }
            this.$refs['report'].showEdit(row, '办理')
            return
          }
          this.$refs['deal'].show(row, this.type, true)
        }
      },
      handleTrack(row) {
        this.$refs['track'].showEdit(row)
      },
    },
  }
</script>
