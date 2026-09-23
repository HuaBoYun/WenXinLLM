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

    <el-table :data="list" :key="tableKey">
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
        show-overflow-tooltip
      />
      <!-- <el-table-column
        align="center"
        label="整改方案"
        prop="rectificationPlan"
        show-overflow-tooltip
      ></el-table-column> -->
      <!-- <el-table-column
        align="center"
        label="整改责任人"
        prop="rectificationMeasures"
        show-overflow-tooltip
      ></el-table-column> -->
      <el-table-column
        align="center"
        label="整改落实人"
        prop="implementerName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="预计完成时间"
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
          <el-button type="text" @click="assignPerson(row)">指派人员</el-button>
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
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
  </el-dialog>
</template>
<script>
  import {
    getRectificationAllocationIssuesList,
    saveIssuesRelaImpementer,
  } from '@/api/zgzz/index.js'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import * as dayjs from 'dayjs'
  import { saveDistribution } from '@/api/setting/system'

  export default {
    name: 'assignTable',
    components: { ExecutorOptions },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        title: '整改分派',
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
        currentRow: {},
        tableKey: new Date().getTime(),
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async handleExecutorSelected(node) {
        const { msg, code, data } = await saveIssuesRelaImpementer({
          relaId: this.assignInfo.relaId,
          impementerId: node[0].staffid,
        })
        this.tableKey = new Date().getTime()
        if (code == 1) {
          this.$baseMessage(msg, 'success')
          if (data && data.isSend == 1) {
            data.issuesList.map(async (item) => {
              let issuesName = item.issues.issuesName
              let issuesId = item.issues.issuesId
              await saveDistribution({
                tableId: '97',
                distributionTitle: issuesName,
                formId: issuesId,
                isread: 0,
                moduleType: 'zgzz',
                reciver: node.staffid,
              })
            })
          }
        } else {
          this.$baseMessage(msg, 'error')
        }
        this.assignInfo = null
        await this.fetchData()
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
      assignPerson(row) {
        if (this.currentRow.status <= '6') {
          this.$baseMessage('审批未完成，无法再分派人员', 'error')
          return
        }
        if (this.currentRow.status == '10') {
          this.$baseMessage('整改已完成，无法再分派人员', 'error')
          return
        }
        if (this.currentRow.status == '11') {
          this.$baseMessage('整改已关闭，无法再分派人员', 'error')
          return
        }
        this.assignInfo = row
        this.$refs.executor.showEdit()
      },
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          this.currentRow = row
          this.queryForm.planId = row.planId
          this.fetchData()
        }
      },
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.$emit('fetch')
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

<style></style>
