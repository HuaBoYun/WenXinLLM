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
              v-model="queryForm.invoiceno"
              clearable
              placeholder="发票号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.invoiceheadtext"
              clearable
              placeholder="发票抬头"
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
      <el-table-column align="center" label="发票号" prop="invoiceno" />
      <el-table-column align="center" label="发票抬头" prop="invoiceheadtext" />
      <el-table-column align="center" label="开票单位" prop="invoicekporg" />
      <el-table-column align="center" label="发票类型" prop="invoicetype" />
      <el-table-column align="center" label="开票日期" prop="invoicedate" />
      <el-table-column align="center" label="发票状态" prop="invoicestatus">
        <!-- 1-未开票，2-已开票，3-未收款，4-已收款，5-已退票 -->
        <template #default="{ row }">
          <span v-if="row.invoicestatus == 1">未开票</span>
          <span v-if="row.invoicestatus == 2">已开票</span>
          <span v-if="row.invoicestatus == 3">未收款</span>
          <span v-if="row.invoicestatus == 4">已收款</span>
          <span v-if="row.invoicestatus == 5">已退票</span>
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
  import { collectionChoiceInvoice } from '@/api/contract/financing'
  export default {
    name: 'XdfOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '发票信息',
        dialogFormVisible: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          budgetId: undefined,
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
      show(contractid, budgetid) {
        this.dialogFormVisible = true
        this.queryForm.budgetId = budgetid
        this.getXdf()
      },
      async getXdf() {
        this.listLoading = true
        const {
          date: { tlist, totalRecord },
        } = await collectionChoiceInvoice(this.queryForm)
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
      handleSelected(val) {
        this.$emit('selectedfpxx', val)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
