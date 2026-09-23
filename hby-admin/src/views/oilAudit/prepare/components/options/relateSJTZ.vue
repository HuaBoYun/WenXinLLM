<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <div>
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <div>
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            v-if="isCheckout"
            type="selection"
            width="55"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计通知书编号"
            prop="advicecoed"
          />
          <el-table-column
            align="center"
            label="审计通知书名称"
            prop="advicename"
          />
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
    </div>
  </el-dialog>
</template>
<script>
  import { getNoticeList } from '@/oapi/audit/preparation'
  export default {
    name: 'ProjectOptions',
    props: {
      isCheckout: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
      }
    },
    created() {},
    methods: {
      // 列表多选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      show() {
        this.current = undefined
        this.dialogFormVisible = true
        this.getExecutorList()
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getNoticeList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
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
        if (!this.current && !this.isCheckout) {
          this.$baseMessage(
            '请选择变更通知书！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        if (
          !(this.multipleSelection && this.multipleSelection.length) &&
          this.isCheckout
        ) {
          this.$baseMessage(
            '请选择变更通知书！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit(
          'selected',
          this.isCheckout ? this.multipleSelection : this.current
        )
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 20%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }
</style>

