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
        <el-button native-type="submit" type="primary" @click="resetSearch">
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list">
      <el-table-column align="center" label="问题编号" prop="code" />
      <el-table-column align="center" label="被审计单位" prop="company" />
      <el-table-column align="center" label="问题详情" prop="details" />
      <el-table-column align="center" label="问题来源" prop="source" />
      <el-table-column align="center" label="发现人" prop="discoverer" />
      <el-table-column align="center" label="整改执行人" prop="zgzxxrname" />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="{ row }">
          {{
            row.status == '1'
              ? '开始'
              : row.status == '2'
              ? '整改中'
              : row.status == '3'
              ? '整改完成'   
              : '未分派'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="result(row)">结果</el-button>
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
    <queryForm ref="query" />
  </el-dialog>
</template>
<script>
  import { getcxContentsList, getzgjgReformByid } from '@/api/audit/rectify'
  import queryForm from '../form/queryForm'
  export default {
    name: 'queryTable',
    components: { queryForm },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        title: '整改查询',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: undefined,
          solutionid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        solutionid: undefined,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
        this.queryForm.solutionid = this.solutionid
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
        } = await getcxContentsList(this.queryForm)
        this.list = list
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async result(row) {
        const data = await getzgjgReformByid({ reformid: row.reformid })
        await this.$refs['query'].showEdit(data.data)
      },
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          this.queryForm.solutionid = row
          this.solutionid = row
          this.fetchData()
        }
      },
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.queryForm.solutionid = undefined
        this.resetQueryForm()
      },
    },
  }
</script>

<style></style>
