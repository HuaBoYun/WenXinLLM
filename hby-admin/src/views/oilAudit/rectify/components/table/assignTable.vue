<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :inline="true"
      label-width="0"
      :model="queryForm"
      @submit.native.prevent
    >
      <el-form-item>
        <el-input v-model="queryForm.code" clearable placeholder="问题编号" />
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
    </el-form>

    <el-table :data="list">
      <el-table-column align="center" label="问题编号" prop="code" />
      <el-table-column align="center" label="被审计单位" prop="company" />
      <el-table-column align="center" label="问题详情" prop="details" show-overflow-tooltip/>
      <el-table-column align="center" label="问题来源" prop="source" />
      <el-table-column align="center" label="发现人" prop="discoverer" />
      <el-table-column align="center" label="整改执行人" prop="zgzxxrname" />
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
    <executor-options ref="executor" @selected="handleExecutorSelected" />
  </el-dialog>
</template>
<script>
  import { getfpContentsList, fpSolutionRy } from '@/oapi/audit/rectify'
  import ExecutorOptions from '../options/executor.vue'

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
          code: undefined,
          solutionid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async handleExecutorSelected(node) {
        const { msg, code } = await fpSolutionRy({
          solutionid: this.queryForm.solutionid,
          reformid: this.assignInfo.reformid,
          userid: node.staffid,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success')
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
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getfpContentsList(this.queryForm)
        this.list = list
        this.list = list
        this.total = total
        this.listLoading = false
      },
      assignPerson(row) {
        this.assignInfo = row
        this.$refs.executor.show()
      },
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          this.queryForm.solutionid = row
          this.fetchData()
        }
      },
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.queryForm.solutionid = undefined
      },
    },
  }
</script>

<style></style>
