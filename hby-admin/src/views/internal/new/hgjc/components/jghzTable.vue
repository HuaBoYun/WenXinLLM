<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <!-- <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.counterpartno"
              clearable
              placeholder="编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.budgetname"
              clearable
              placeholder="名称"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form> -->
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="6">
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
      <template>
        <el-table-column align="center" label="疑似问题" prop="suspectedIssue">
          <template #default="{ row }">
            <el-button
              style="color: red"
              type="text"
              @click="handleDeatil(row)"
            >
              {{ row.suspectedIssue }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="问题类型" prop="questionType" />
        <el-table-column align="center" label="发现时间" prop="discoverTime" />
        <el-table-column align="center" label="业务领域" prop="businessArea" />
        <el-table-column
          align="center"
          label="描述"
          prop="describe"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="确认情况"
          prop="isConfirm"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <span>{{ row.isConfirm == 0 ? '未确认' : '确认' }}</span>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="创建人"
          prop="creatorName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="创建时间"
          prop="createdTime"
          show-overflow-tooltip
        ></el-table-column>
      </template>
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
    <TaskView ref="edit" @fetch-data="fetchData" />
  </el-dialog>
</template>
<script>
  import TaskView from '@/views/internal/new/hgjc/components/TaskView'
  import { impctrltestPlanList } from '@/api/internal/new/plan'
  export default {
    name: 'XdfOptions',
    components: {
      TaskView,
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
          suspectedIssue: '',
          questionType: '',
          isConfirm: '',
          isRectification: '1',
          pageNumber: 1,
          pageSize: 20,
          state: 6,
        },
        recordType: 'HTGL001', // 存储recordType，以防搜索重置时重置成相对方的搜索
      }
    },
    created() {},
    methods: {
      handleDeatil(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      show() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      async fetchData() {
        // this.resetQueryForm()
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await impctrltestPlanList({ ...this.queryForm, isRepeat: 1 })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSelected(val) {
        this.current = val
        // this.$emit('selected', val, this.field)
        // this.dialogFormVisible = false
      },
      confirm() {
        console.log(this.current, '111111')
        if (!this.current) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }

        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
    },
  }
</script>
