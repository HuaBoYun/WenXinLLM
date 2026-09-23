<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <div>
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
          <el-form-item>
            <el-input
              v-model="queryForm.planCode"
              clearable
              placeholder="项目编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.planName"
              clearable
              placeholder="项目名称"
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
      </el-form>
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
          <el-table-column align="center" label="项目编号" prop="planCode" />
          <el-table-column align="center" label="项目名称" prop="planName" />
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
  import { formatDay } from '@/utils/index'
  import { projectList, projectWbList } from '@/api/audit/rectify'
  import { ctrltestPlanList } from '@/api/internal/plan'
  import { getSolutionProjectList } from '@/api/zgzz/index.js'
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
          planCode: undefined,
          planName: undefined,
          planType: '',
          pageNumber: 1,
          pageSize: 20,
        },
        current: '',
        type: 0
      }
    },
    created() {},
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      show(type) {
        console.log(type)
        this.type = type
        this.queryForm.planType = type
        this.current = ''
        this.dialogFormVisible = true
        this.list = []
        this.total = 0
        this.fetchData()
        
      },
      async getExecutorWbList() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await projectWbList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getSolutionProjectList(this.queryForm)
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
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      resetSearch () {
        this.queryForm.planCode = ''
        this.queryForm.planName = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.fetchData()
      },
      confirm() {
        // console.log(this.current)
        if (!this.current) {
          this.$baseMessage(
            '请输入关联审计项目',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.queryForm.planCode = ''
        this.queryForm.planName = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
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
