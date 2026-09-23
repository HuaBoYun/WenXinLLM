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
              v-model="queryForm.contractno"
              clearable
              placeholder="合同编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.contractname"
              clearable
              placeholder="合同名称"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="getList"
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
      <el-table-column align="center" label="合同编号" prop="contractno" />
      <el-table-column align="center" label="合同名称" prop="contractname" />
      <el-table-column align="center" label="合同类型" prop="contracttype" />
      <el-table-column align="center" label="合同金额" prop="contractmoney" />
      <el-table-column align="center" label="合同项目" prop="contractitem" />
      <el-table-column align="center" label="合同标的" prop="contractbd" />
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
  import { findContractInfo } from '@/api/contract/legal'
  export default {
    name: 'XdfOptions',
    data() {
      return {
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        listLoading: false,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          idname: 'contractId',
          textname: 'contractName',
          othername: 'contractNo,jbStaffName',
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
        this.getList()
      },
      show() {
        this.dialogFormVisible = true
        this.getList()
      },
      async getList() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await findContractInfo(this.queryForm)
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
        this.getList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getList()
      },
      //回调
      handleSelected(val) {
        this.$emit('selecteded', val)
        this.dialogFormVisible = false
      },
      close() {},
    },
  }
</script>
