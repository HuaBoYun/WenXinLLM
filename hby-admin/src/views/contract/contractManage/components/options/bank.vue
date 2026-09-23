<!--
 * @Date: 2022-03-28 14:30:57
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 11:55:17
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/bank.vue
-->
<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.bankaccount"
              clearable
              placeholder="银行账号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.bankaccname"
              clearable
              placeholder="账户"
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
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @current-change="handleSelected"
    >
      <el-table-column align="center" label="银行账号" prop="bankaccount" />
      <el-table-column align="center" label="账户" prop="bankaccname" />
      <el-table-column align="center" label="开户银行" prop="bankkhyh" />
      <el-table-column align="center" label="账户性质" prop="banknature">
        <template #default="{ row }">
          <span>{{ row.banknature == 0 ? '公司' : '个人' }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" prop="bankstatus">
        <template #default="{ row }">
          <span>
            {{ row.bankstatus == 0 ? '弃用' : '启用' }}
          </span>
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
  import { getBankOptions } from '@/api/contract/manage'
  export default {
    name: 'BankOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {},
        budgetId: undefined,
      }
    },
    created() {},
    methods: {
      resetQueryForm() {
        this.queryForm = {
          budgetId: undefined,
          bankaccount: undefined,
          bankaccname: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      show(data) {
        this.budgetId = data.budgetId
        this.resetSearch()
        this.dialogFormVisible = true
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.queryForm.budgetId = this.budgetId
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getBankOptions(this.queryForm)
        this.list = tlist
        this.total = totalRecord
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
      //回调
      handleSelected(val) {
        this.$emit('selected', val)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
    },
  }
</script>
