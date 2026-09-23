<template>
  <el-dialog
    v-if="dialogFormVisible"
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
        <el-button native-type="submit" type="primary" @click="resetSearch">
          重置
        </el-button>
      </el-form-item>
    </el-form>
    <div style="display: flex; margin-bottom: 20px">
      <el-button
        :disabled="list.length < 0"
        type="primary"
        @click="submit()"
        style="margin-left: 90%"
      >
        提交
      </el-button>
      <el-button type="primary" @click="close()" style="margin-left: 1%">
        确定
      </el-button>
    </div>

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
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="valuation(row)">评价</el-button>
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

    <valuationForm ref="valuation" @fetch-data="fetchData" />
  </el-dialog>
</template>
<script>
  import {
    getpjContentsList,
    getpjReformByid,
    tjpjReform,
  } from '@/oapi/audit/rectify'
  import valuationForm from '../form/valuationForm'
  export default {
    name: 'valuationTable',
    components: { valuationForm },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        title: '整改评价',
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
        } = await getpjContentsList(this.queryForm)
        this.list = list
        this.list = list
        this.total = total
        this.listLoading = false
      },
      submit(row) {
        this.$baseConfirm('你确定要提交当前项吗', null, async () => {
          const { msg, code } = await tjpjReform({
            solutionid: this.queryForm.solutionid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            this.dialogFormVisible = false
            this.$emit('fetch-data')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      async valuation(row) {
        const data = await getpjReformByid({
          solutinid: this.queryForm.solutionid,
          reformid: row.reformid,
        })
        await this.$refs['valuation'].showEdit(data.data)
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
        this.$emit('fetch-data')
      },
    },
  }
</script>

<style></style>
