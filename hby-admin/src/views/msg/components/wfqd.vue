<template>
  <div class="system-log-container">
    <el-col v-if="showTitle" :span="24">
      <h3>我的待办</h3>
    </el-col>
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.flowName"
              clearable
              placeholder="流程标题"
            />
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.status" placeholder="流程状态">
              <el-option label="等待审核" value="1" />
              <el-option label="审核通过" value="2" />
              <el-option label="审核驳回" value="3" />
              <el-option label="流程撤回" value="4" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="queryData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="流程标题" prop="fullName" />
      <el-table-column align="center" label="所属流程" prop="flowName" />
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
        label="发起时间"
        prop="startTime"
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="170"
      >
        <template #default="{ row }">
          <el-button type="text" @click="showDetail(row, false)">
            详情
          </el-button>
          <el-button
            type="text"
            :disabled="[1, 2, 5].indexOf(row.status) > -1"
            @click="showDetail(row, true)"
          >
            编辑
          </el-button>
          <el-button
            type="text"
            @click="deleteData(row)"
            :disabled="row.status != 4"
          >
            删除
          </el-button>
          <el-button
            type="text"
            :disabled="row.status == 2 || row.status == 4"
            @click="chehui(row)"
          >
            撤销
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.currentPage"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :page-sizes="pageSizes"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <WfqdDeal ref="wfqddeal" />

    <reportModal
      ref="report"
      @reload-data="reload"
      :UEditorCloudEdit="this.UEditorCloudEdit"
    />
  </div>
</template>

<script>
  import {
    ymWorkActionsWithdraw,
    ymWorkActionsDelete,
  } from '@/api/contract/manage'
  import { my_faqi } from '@/api/setting/msg'
  import { formatDate } from '@/utils/index'
  import PapersDetails from '@/views/msg/components/operation/papersDetails'
  import PaymentBanli from '@/views/msg/components/operation/PaymentDetail'
  import ReceivingBanli from '@/views/msg/components/operation/ReceivingDetail'
  import Reference from '@/views/msg/components/operation/ReferenceDetail.vue'
  import Track from '@/views/msg/components/options/Track'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ConfirmDetail from './operation/confirmDetail.vue'
  import reportModal from './operation/reportModal.vue'
  import ShenjiModal from './operation/shenpiModal.vue'
  export default {
    name: 'Wddb',
    components: {
      PaymentBanli,
      ReceivingBanli,
      Track,
      WfqdDeal,
      ShenjiModal,
      PapersDetails,
      Reference,
      ConfirmDetail,
      reportModal,
    },
    props: {
      showTitle: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        pageSizes: [5, 10, 15, 20, 50, 100],
        queryForm: {
          flowName: '',
          status: undefined,
          currentPage: 1,
          pageSize: 10,
        },
        type: '',
        showModal: false,
        UEditorCloudEdit: false,
        UEditorShenPiCloudEdit: false,
      }
    },
    created() {
      this.fetchWfqdData()
    },

    mounted() {
      this.$bus.$on('updateMsg', (type) => {
        if (type === 0) {
          this.fetchWfqdData()
          this.$refs['wfqddeal'].close()
        }
      })

      this.$nextTick(() => {
        if (this.$store.state.work.wfqdState) {
          this.showDetail(this.$store.state.work.wfqdDetails, false)
        }
      })
    },

    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetUEditorStatus() {
        this.UEditorCloudEdit = false
        this.UEditorShenPiCloudEdit = false
      },
      reload() {
        this.fetchWfqdData()
        this.resetUEditorStatus()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchWfqdData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchWfqdData()
      },
      handleCurrentChange(val) {
        this.queryForm.currentPage = val
        this.fetchWfqdData()
      },
      queryData() {
        this.queryForm.currentPage = 1
        this.fetchWfqdData()
      },
      async fetchWfqdData() {
        this.listLoading = true
        const {
          data: { list, totalCount },
        } = await my_faqi(this.queryForm)
        this.list = list
        this.total = totalCount
        this.listLoading = false
      },
      async showDetail(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.wfqddeal.show(dataRow, isEdit)
        this.$store.dispatch('work/setWfqdStatesAction', false)
      },
      async chehui(row) {
        this.$baseConfirm(
          '是否确定撤销，撤销后流程退回到初始节点，需重新发起审批！',
          null,
          async () => {
            this.listLoading = true
            const { msg } = await ymWorkActionsWithdraw({
              id: row.id,
              flowId: row.flowId,
            })
            this.listLoading = false
            this.fetchWfqdData()
            this.$baseMessage('撤销成功', 'success', 'vab-hey-message-success')
          }
        )
      },
      handleTrack(row) {
        this.$refs['track'].showEdit(row)
      },
      async deleteData(row) {
        this.$confirm(' 是否继确认删除?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          const res = await ymWorkActionsDelete({
            id: row.id,
            flowId: row.flowId,
          })
          if (res && res.code == 1) {
            this.$message.success('流程删除成功')
            this.fetchWfqdData()
          } else {
            this.$message.error('流程删除失败')
          }
        })
      },
    },
  }
</script>
