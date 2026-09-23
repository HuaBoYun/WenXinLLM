<!--
 * @Date: 2022-03-28 15:04:10
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 17:05:56
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/department.vue
-->
<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="800px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">选 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="data"
      highlight-current-row
      @current-change="handleSelected"
    >
      <el-table-column align="center" label="用户真实姓名" prop="realname" />
      <el-table-column align="center" label="所属部门" prop="orgname" />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNum"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </el-dialog>
</template>

<script>
  import { userList } from '@/api/setting/auth'
  export default {
    name: 'DepartmentOptions',
    data() {
      return {
        title: '用户信息',
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'text',
          value: 'id',
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNum: 1,
          pageSize: 10,
          pid: JSON.parse(localStorage.userInfo).linkDetp.orgid,
        },
        data: [],
        orgname: JSON.parse(localStorage.userInfo).linkDetp.orgname,
        changedUser: {},
      }
    },
    created() {},
    methods: {
      show() {
        this.dialogTreeVisible = true
        this.fetchTree()
      },
      async fetchTree() {
        const res = await userList(this.queryForm)
        this.data = res.data.tlist
        this.total = res.data.totalRecord
      },
      handleSelected(val) {
        this.changedUser = val
      },
      close() {
        this.dialogTreeVisible = false
      },
      confirm() {
        const checked = this.changedUser
        this.$emit('selected', checked)
        this.dialogTreeVisible = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
    },
  }
</script>

<style scoped>
  .top-action {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
</style>
