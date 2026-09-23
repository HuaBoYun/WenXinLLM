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
              v-model="queryForm.bankaccnum"
              clearable
              placeholder="账号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.bankaccname"
              clearable
              placeholder="户名"
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
      <el-table-column align="center" label="账号" prop="bankaccnum" />
      <el-table-column align="center" label="户名" prop="bankaccname" />
      <el-table-column align="center" label="账户名称" prop="bankname" />
      <el-table-column align="center" label="开户银行" prop="bankkhyh" />
      <el-table-column align="center" label="账户状态" prop="bankstate">
        <template #default="{ row }">
          {{
            2 >= row.bankstate && row.bankstate >= 0
              ? ['正常', '冻结', '部分冻结'][row.bankstate]
              : '销户'
          }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="启用状态" prop="bankstatus">
        <template #default="{ row }">
          {{ row.bankstatus === 1 ? '启用' : '非启用' }}
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
  import { choiceBankInfo } from '@/api/contract/financing'
  export default {
    name: 'XdfOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '银行账户',
        dialogFormVisible: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
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
      show() {
        this.dialogFormVisible = true
        this.getXdf()
      },
      async getXdf() {
        this.listLoading = true
        const {
          date: { tlist, totalRecord },
        } = await choiceBankInfo(this.queryForm)
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
        this.$emit('selectedyhzhsk', val)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
