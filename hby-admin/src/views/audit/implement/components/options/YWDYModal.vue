<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    title="问题单元"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <div>
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @current-change="handleSelected"
      >
        <el-table-column align="center" label="问题单元" prop="businessType" />
        <el-table-column align="center" label="问题类型" prop="riskSource" />
        <el-table-column
          align="center"
          label="审计程序"
          prop="suditProcess"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="完成时间" prop="finishTime" />
        <el-table-column align="center" label="状态" prop="finish">
          <template #default="{ row }">
            {{ info[row.finish] }}
          </template>
        </el-table-column>
        >
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
    </div>
  </el-dialog>
</template>
<script>
  import { getYWDYlist } from '@/api/audit/implement'
  export default {
    name: 'ExecutorOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          bsunitname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        info: ['未完成', '已完成', '执行中'],
      }
    },
    created() {},
    methods: {
      show() {
        this.current = undefined
        this.dialogFormVisible = true

        this.getExecutorList()
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getYWDYlist(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择执行人！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
