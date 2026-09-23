<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <vab-query-form>
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
        <el-form-item>
          <el-input
            v-model="queryForm.userName"
            clearable
            placeholder="用户名"
          />
        </el-form-item>
        <el-form-item>
          <el-input v-model="queryForm.realName" clearable placeholder="真实姓名" />
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
        <el-button style="float: right" type="primary" @click="save">
          确定
        </el-button>
      </el-form>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" label="用户真实姓名" prop="realname" />
      <el-table-column align="center" label="所属部门" prop="orgName" />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </el-dialog>
</template>

<script>
import {
  getListByThemeHouse,
  cancelBiStaff,
} from '@/api/setting/themeRepertory'

export default {
  name: 'Send',
  data() {
    return {
      title: '取消模块下发',
      dialogFormVisible: false,
      list: [],
      total: 0,
      queryForm: {
        userName: '',
        realName: '',
        pageids: '',
        pageNumber: 1,
        pageSize: 20,
      },
      layout: 'total, sizes, prev, pager, next, jumper',
      options: [],
      listLoading: false,
      multipleSelection: [],
      pid: -1,
    }
  },
  created() {},
  methods: {
    handleSelectionChange(val) {
      // console.warn('handleSelectionChange', val)
      this.multipleSelection = val
    },
    showEdit(row) {
      this.dialogFormVisible = true
      this.queryForm.pageids = row.ids
      this.pid = row.pid
      this.fetchData()
    },
    close() {
      this.dialogFormVisible = false
      this.pid = -1
    },
    async save() {
      if (this.multipleSelection.length == 0) {
        this.$message.error('请选择')
        return
      }
      const rightids = this.multipleSelection.map((i) => i.staffid).join(',')

      const { msg } = await cancelBiModule({
        staffid: rightids,
        staffid: this.queryForm.pageids,
        type: !this.pid ? 1 : 2,
      })
      this.$baseMessage(msg || '设置成功', 'success', 'vab-hey-message-success')

      this.$emit('fetch-data')
      this.close()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    resetSearch(){
      this.queryForm.realName = ""
      this.queryForm.userName = ""
      this.queryForm.pageNumber = 1

      this.fetchData()
    },
    async fetchData() {
      this.listLoading = true
      const { data } = await getListByThemeHouse(this.queryForm)
      this.list = data.list
      this.total = 0
      this.listLoading = false
    },
  },
}
</script>
