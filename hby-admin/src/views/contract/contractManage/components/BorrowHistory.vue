<!--
 * @Date: 2022-03-31 15:03:53
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-07 11:18:00
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/BorrowHistory.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="合同编号" prop="contractno" />
      <el-table-column align="center" label="合同名称" prop="contractname" />
      <el-table-column align="center" label="借阅人" prop="realname" />
      <el-table-column align="center" label="借阅日期" prop="lenddate" />
      <el-table-column align="center" label="归还日期" prop="returndate" />
      <el-table-column align="center" label="备注" prop="memo" />
      <el-table-column align="center" label="借阅状态" prop="lendstatus">
        <template #default="{ row }">
          {{ mapLendStatus(row) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" prop="data">
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleDetail(row)"
            :disabled="row.lendstatus != 6"
          >
            详情
          </el-button>
          <el-button type="text" @click="handleManage(row)">办理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
    <WfqdDeal ref="wfqddeal" />
    <CreateDetail ref="common" />
  </el-dialog>
</template>
<script>
  import { getBorrowHistory } from '@/api/contract/manage'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import CreateDetail from '../components/contractsEdit/CreateDetail.vue'
  export default {
    name: 'BorrowHistory',
    components: { WfqdDeal, CreateDetail },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        dialogVisible: false,
        title: '借阅记录',
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogVisible = true
        this.fetchData()
      },
      mapLendStatus(row) {
        const { lendstatus } = row
        if (lendstatus == 1) {
          return '审批中'
        } else if (lendstatus == 2) {
          return '已退回'
        } else if (lendstatus == 3) {
          return '已撤回'
        } else if (lendstatus == 4) {
          return '已终止'
        } else if (lendstatus == 5) {
          return '已跟踪'
        } else if (lendstatus == 6) {
          return '已完成'
        } else {
          return '待审批'
        }
      },

      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getBorrowHistory(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleDeal(row) {
        this.$refs['deal'].show(row, 'borrow')
      },
      close() {
        this.dialogVisible = false
      },
      async handleDetail(row) {
        this.$refs['common'].showDetail(row, row.contracttype)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.lendid,
          tableId: 13,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
    },
  }
</script>
