<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
  >
    <el-form
      ref="form"
      :inline="true"
      label-width="0"
      :model="queryForm"
      @submit.native.prevent
    >
      <el-form-item>
        <el-input
          v-model="queryForm.issuesCode"
          clearable
          placeholder="问题编号"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="queryForm.issuesName"
          clearable
          placeholder="问题名称"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="queryForm.issuesTitle"
          clearable
          placeholder="问题标题"
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
        <el-button native-type="submit" type="primary" @click="queryData">
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list">
      <el-table-column
        align="center"
        label="问题编号"
        prop="issuesCode"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="问题名称"
        prop="issuesName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="问题标题"
        prop="issuesTitle"
        #default="{ row }"
      >
        <el-tooltip placement="top">
          <div slot="content" style="max-width: 600px; white-space: pre-wrap">
            {{ row.issuesTitle }}
          </div>
          <div class="showOverFlow">
            {{ row.issuesTitle }}
          </div>
        </el-tooltip>
      </el-table-column>
      <el-table-column
        align="center"
        label="整改方案"
        prop="rectificationPlan"
        #default="{ row }"
      >
        <el-tooltip placement="top">
          <div slot="content" style="max-width: 600px; white-space: pre-wrap">
            {{ row.rectificationPlan }}
          </div>
          <div class="showOverFlow">
            {{ row.rectificationPlan }}
          </div>
        </el-tooltip>
      </el-table-column>
      <el-table-column
        align="center"
        label="整改落实人"
        prop="implementerName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="整改期间时间"
        prop="deadline"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="valuation(row)">
            {{ optionBtn }}
          </el-button>
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
    <valuationForm
      ref="valuationForm"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
    />
  </el-dialog>
</template>
<script>
  import {
    getRectificationAllocationIssuesList,
    getIssuesAllDetailInfo,
    getZgzzeEvaluationDetail,
  } from '@/api/zgzz/index.js'
  import valuationForm from '@/views/audit/rectify/components/form/valuationForm'
  import * as dayjs from 'dayjs'

  export default {
    name: 'valuationTable',
    components: { valuationForm },
    inheritAttrs: false,
    props: {
      title: {
        type: String,
        default: '评价',
      },
      optionBtn: {
        type: String,
        default: '评价',
      },
    },
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        assignInfo: null,
        queryForm: {
          issuesCode: undefined,
          issuesName: undefined,
          issuesTitle: undefined,
          planId: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        showValuationForm: false,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.issuesCode = undefined
        this.queryForm.issuesName = undefined
        this.queryForm.issuesTitle = undefined
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getRectificationAllocationIssuesList(this.queryForm)
        this.list = tlist.map((x) => {
          const { issues, ...other } = x
          return {
            ...issues,
            ...other,
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      async valuation(row) {
        this.showValuationForm = true
        let res = null
        if (row.valua && row.valua.evalId) {
          res = await getZgzzeEvaluationDetail({ evalId: row.valua.evalId })
        } else {
          res = await getIssuesAllDetailInfo({ relaId: row.relaId })
        }
        if (res && res.data) {
          if (this.optionBtn == '跟踪') {
            this.$refs.valuationForm.showEdit('detail', res.data)
            return
          }
          this.$refs.valuationForm.showEdit('edit', res.data)
        }
      },
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          this.queryForm.planId = row.planId
          this.fetchData()
        }
      },
      close() {
        this.dialogFormVisible = false
        this.tableData = []
      },
      closeDialog() {
        this.showValuationForm = false
      },
      formatDate(row, column) {
        console.log('formatDate', row, column)
        // 获取单元格数据
        let data = row[column.property]
        console.log('data', data)
        if (data) {
          return dayjs(data).format('YYYY-MM-DD')
        } else {
          return ''
        }
      },
    },
  }
</script>

<style>
  .showOverFlow {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.5em;
    max-height: 3em;
    white-space: pre-wrap;
  }
</style>
