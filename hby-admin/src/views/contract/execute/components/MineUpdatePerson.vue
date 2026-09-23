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
              v-model="queryForm.username"
              clearable
              placeholder="账号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.realname"
              clearable
              placeholder="姓名"
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
      <vab-query-form-right-panel :span="6">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @current-change="handleSelected"
    >
      <template>
        <el-table-column align="center" label="账号" prop="username" />
        <el-table-column align="center" label="姓名" prop="realname" />
        <el-table-column align="center" label="部门" prop="orgName" />
      </template>
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
  import { getChangeContractStaffList } from '@/api/contract/fulfil'

  export default {
    name: 'XdfOptions',
    components: {},
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          realname: '',
          username: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {},
    methods: {
      show(row) {
        this.dialogFormVisible = true
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        // this.resetQueryForm()
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getChangeContractStaffList(this.queryForm)
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
        this.current = val
        // this.$emit('selected', val, this.field)
        // this.dialogFormVisible = false
      },
      //前置条件
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }

        this.$emit('selected', this.current)
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
