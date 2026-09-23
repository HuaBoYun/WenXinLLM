<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="title"
    width="1200px"
    :close-on-click-modal="false"
    append-to-body
    @close="handleClose"
  >
    <el-form
      :inline="true"
      :model="queryForm"
      size="mini"
      style="margin-bottom: 10px"
    >
      <el-form-item>
        <el-input
          v-model="queryForm.companyname"
          clearable
          placeholder="公司名称"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="queryForm.Date"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="queryData">查询</el-button>
        <el-button @click="resetQueryForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%"
      max-height="800"
    >
      <el-table-column align="center" label="报告名称" prop="reportname">
        <template #default="{ row }">
          <el-button type="text" @click="handleRead(row)">
            {{ row.reportname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="所属公司" prop="linkorgname" />
      <el-table-column
        align="center"
        label="报告时间"
        prop="reporttime"
        :formatter="formatDay"
      />
      <el-table-column
        align="center"
        label="报告类型"
        prop="reporttype"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="报告方式" prop="reportmode" />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="{ row }">
          {{
            row.status == 1
              ? '审批中'
              : row.status == 2
              ? '需调整'
              : row.status == 3
              ? '已撤销'
              : row.status == 4
              ? '已终止'
              : row.status == 5
              ? '已跟踪'
              : row.status == 6
              ? '已完成'
              : '未审批'
          }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      style="margin-top: 10px; text-align: right"
      :current-page="queryForm.pageNumber"
      layout="total, sizes, prev, pager, next, jumper"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <NormalRead ref="read" />
  </el-dialog>
</template>

<script>
  import { getReportList1 } from '@/api/risk/report'
  import { formatDay } from '@/utils/index'
  import NormalRead from '@/views/risk/report/normal/components/NormalRead.vue'

  export default {
    name: 'ReportListDialog',
    components: {
      NormalRead,
    },
    data() {
      return {
        dialogVisible: false,
        title: '风险报告列表',
        list: [],
        listLoading: false,
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          companyname: '',
          type: 'fx_zdy',
          Date: [],
        },
      }
    },
    methods: {
      show(params = {}) {
        this.dialogVisible = true
        if (params.companyname) {
          this.queryForm.companyname = params.companyname
          this.title = `风险报告列表 - ${params.companyname}`
        }
        this.fetchData()
      },
      handleClose() {
        this.resetQueryForm()
        this.list = []
        this.title = '风险报告列表'
      },
      formatDay(row, column) {
        let data = row[column.property]
        return formatDay(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        try {
          const { Date, ...other } = this.queryForm
          let startDate = ''
          let endDate = ''
          if (Date && Date.length === 2) {
            startDate = Date[0]
            endDate = Date[1]
          }
          const res = await getReportList1({ ...other, startDate, endDate })
          this.list = res.data.page.list
          this.total = res.data.page.total
        } catch (error) {
          console.error('获取报告数据失败:', error)
        } finally {
          this.listLoading = false
        }
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          companyname: '',
          type: 'fx_zdy',
          Date: [],
        }
      },
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
    },
  }
</script>
