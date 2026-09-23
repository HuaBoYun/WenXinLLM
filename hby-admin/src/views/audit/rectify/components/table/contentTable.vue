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
        </el-form-item>
        <el-form-item>
          <el-button native-type="submit" type="primary" @click="resetSearch">
            重置
          </el-button>
        </el-form-item>
      </el-form>
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click.stop="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="问题编号" prop="issuesCode" />
        <el-table-column align="center" label="问题名称" prop="issuesName" />
        <el-table-column align="center" label="问题来源" prop="issueType" #default="{ row }">{{ ['审计', '内控', '非系统实施', '外部审计', '风险'][Number(row.issuesType) - 1] }}</el-table-column>
        <el-table-column align="center" label="被审计对象" prop="auditObjectName" />
        <el-table-column
          align="center"
          label="问题标题"
          prop="issuesTitle"
          #default="{ row }"
        >
          <el-tooltip placement="top">
            <div slot="content" style="max-width:600px;white-space: pre-wrap;">{{ row.issuesTitle }}</div>
            <div class="showOverFlow">
              {{ row.issuesTitle }}
            </div>
          </el-tooltip>
        </el-table-column>
        <el-table-column align="center" label="拟稿人" prop="createStaffName" />
        <el-table-column align="center" label="拟稿日期" prop="createTime" :formatter="formatDate" />
      </el-table>
      <!-- <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      /> -->
    </div>
  </el-dialog>
</template>
<script>
  import { getAfterProjectlssues } from '@/api/zgzz/index.js'
  import * as dayjs from "dayjs"
  export default {
    name: 'contentTable',
    components: { },
    data() {
      return {
        listLoading: false,
        list: [],
        // layout: 'total, sizes, prev, pager, next, jumper',
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
          planType: undefined,
          projectId: undefined,
          issuesCode: undefined,
          issuesName: undefined,
          issuesTitle: undefined,
          pageNumber: 1,
          pageSize: 99999999,
        },
        current: undefined,
        multipleSelection: [],
        issuesList: [],
        showQuery: true
      }
    },
    created() {},
    methods: {
      show(data, issuesList) {
        // console.log(data)
        this.queryForm.planType = data.planType
        this.queryForm.projectId = data.projectId
        this.issuesList = issuesList
        this.dialogFormVisible = true
        this.fetchData()
      },
      reportShowTable (list, issuesList) {
        // 从整改报告中过来，不要调接口
        this.showQuery = false // 不用查询
        if (issuesList) {
          this.list = list.filter(
            (item) => !issuesList.some((ele) => ele.issuesId === item.issuesId)
          )
        } else {
          this.list = list
        }
      },
      async fetchData() {
        this.listLoading = true
        const { data } = await getAfterProjectlssues(this.queryForm)

        if (this.issuesList) {
          this.list = data.filter(
            (item) => !this.issuesList.some((ele) => ele.issuesId === item.issuesId)
          )
        } else {
          this.list = data
        }

        // this.total = data.length
        this.listLoading = false
      },
      resetSearch() {
        this.queryForm.issuesCode = undefined,
        this.queryForm.issuesName = undefined,
        this.queryForm.issuesTitle = undefined,
        this.queryForm.pageNumber = 1,
        this.queryForm.pageSize = 20,
        this.fetchData()
      },
      // handleSizeChange(val) {
      //   this.queryForm.pageSize = val
      //   this.fetchData()
      // },
      // handleCurrentChange(val) {
      //   this.queryForm.pageNumber = val
      //   this.fetchData()
      // },
      handleSelected(val) {
        console.log(val)
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        console.log(this.multipleSelection)
        if (this.multipleSelection.length <= 0) {
          this.$baseMessage(
            '请选择整改清单！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.close()
      },
      close() {
        // console.log('close')
        this.queryForm = {}
        this.dialogFormVisible = false
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return dayjs(data).format("YYYY-MM-DD")
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
