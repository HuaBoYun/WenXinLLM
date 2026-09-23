<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.counterpartno"
              clearable
              placeholder="相对方编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.budgetname"
              clearable
              placeholder="相对方名称"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="getXdf"
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
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @row-click="handleSelected"
    >
      <el-table-column align="center" label="相对方编号" prop="counterpartno" />
      <el-table-column align="center" label="相对方名称" prop="budgetname">
        <!-- <template #default="{ row }">
          <el-button type="text">{{ row.budgetname }}</el-button>
        </template> -->
      </el-table-column>
      <el-table-column
        align="center"
        label="注册资本（万元）"
        prop="totaltmoney"
      />
      <el-table-column
        align="center"
        label="法定代人"
        prop="projectstagegoal"
      />
      <!-- <el-table-column
        align="center"
        label="相对方地址"
        prop="counterpartaddress"
      />
      <el-table-column align="center" label="开户银行" prop="counterparthank" /> -->
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
  </el-dialog>
</template>
<script>
  import { getContractBudgetList } from '@/api/contract/financing'
  export default {
    name: 'XdfOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '票据相对方',
        dialogFormVisible: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          contractId: '',
        },
      }
    },
    created() {},
    methods: {
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.getXdf()
      },
      show(contractid) {
        this.dialogFormVisible = true
        this.queryForm.contractId = contractid
        this.getXdf()
      },
      async getXdf() {
        this.listLoading = true
        const { date } = await getContractBudgetList(this.queryForm)
        this.list = date
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSelected(val) {
        this.$emit('selected', val)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
