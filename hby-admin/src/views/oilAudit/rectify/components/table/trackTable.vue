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
      <el-form-item>
        <el-button native-type="submit" type="primary" @click="resetSearch">
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list">
      <el-table-column align="center" label="问题编号" prop="code" />
      <el-table-column align="center" label="被审计单位" prop="company" />
      <el-table-column
        align="center"
        label="问题详情"
        prop="details"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="问题来源" prop="source" />
      <el-table-column align="center" label="发现人" prop="discoverer" />
      <el-table-column align="center" label="整改执行人" prop="zgzxxrname" />
      <el-table-column align="center" label="状态" prop="status">
        <template #default="{ row }">
          {{
            row.status == '1'
              ? '开始整改'
              : row.status == '2'
              ? '整改中'
              : '整改完成'
          }}
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
  </el-dialog>
</template>
<script>
  import { getgzContentsList } from '@/oapi/audit/rectify'
  export default {
    name: 'trackTable',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        title: '整改跟踪',
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
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
        this.queryForm.solutionid = this.solutionid
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getgzContentsList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
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
