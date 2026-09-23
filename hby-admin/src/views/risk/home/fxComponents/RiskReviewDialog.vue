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
          v-model="queryForm.mattername"
          clearable
          placeholder="项目名称"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="queryForm.unitname"
          clearable
          placeholder="公司名称"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="queryForm.mattercode"
          clearable
          placeholder="三重一大事项编码"
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
      <el-table-column align="center" label="项目名称" prop="mattername">
        <template #default="{ row }">
          <el-button type="text" @click="handleRead(row)">
            {{ row.mattername }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="三重一大事项编码"
        prop="mattercode"
      />
      <el-table-column
        align="center"
        label="风险审查报告编码"
        prop="riskreviewcode"
      />
      <el-table-column align="center" label="公司名称" prop="orgname" />
      <el-table-column
        align="center"
        label="经办人"
        prop="staffidname"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="经办部门" prop="staffdeptname" />
      <el-table-column
        align="center"
        label="申请时间"
        prop="createtime"
        :formatter="formatDay"
      />
      <el-table-column align="center" label="状态" prop="state">
        <template #default="{ row }">
          {{
            row.state == 1
              ? '审批中'
              : row.state == 2
              ? '需调整'
              : row.state == 3
              ? '已撤销'
              : row.state == 4
              ? '已终止'
              : row.state == 5
              ? '已跟踪'
              : row.state == 6
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

    <ReviewEdit ref="edit" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import { riskReviewOpinionList } from '@/api/risk/riskReview'
  import { formatDay } from '@/utils/index'
  import ReviewEdit from '@/views/risk/riskReview/RiskReviewComments/components/ReviewEdit.vue'

  export default {
    name: 'RiskReviewDialog',
    components: {
      ReviewEdit,
    },
    data() {
      return {
        dialogVisible: false,
        title: '风险审查列表',
        list: [],
        listLoading: false,
        total: 0,
        queryForm: {
          mattername: '',
          unitname: '',
          mattercode: '',
          riskreviewcode: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    methods: {
      show(params = {}) {
        this.dialogVisible = true
        if (params.unitname) {
          this.queryForm.unitname = params.unitname
          this.title = `风险审查列表 - ${params.unitname}`
        }
        this.fetchData()
      },
      handleClose() {
        this.resetQueryForm()
        this.list = []
        this.title = '风险审查列表'
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
          const res = await riskReviewOpinionList({ ...this.queryForm })
          this.list = res.data.pageInfo.list
          this.total = res.data.pageInfo.total
        } catch (error) {
          console.error('获取风险审查数据失败:', error)
        } finally {
          this.listLoading = false
        }
      },
      resetQueryForm() {
        this.queryForm = {
          mattername: '',
          unitname: '',
          mattercode: '',
          riskreviewcode: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
    },
  }
</script>
