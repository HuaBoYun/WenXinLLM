<template>
  <el-dialog
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table
      v-loading="listLoading"
      :data="list"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center" width="55" />
      <!-- <el-table-column align="center" label="序号" type="index" width="50" /> -->
      <el-table-column
        align="center"
        label="部门名称"
        prop="orgTreeNames"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column align="center" label="部门编号" prop="orgnumber" />
      <!-- <el-table-column align="center" label="部门id" prop="orgid" /> -->
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

    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="confirm" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getUserPartDept } from '@/api/setting/auth'
import { formatDate } from '@/utils/index'

export default {
  name: 'selectGczjModal',
  components: {},
  data() {
    return {
      dialogFormVisible: false,
      title: '选择移交部门数据',
      list: [],
      listLoading: false,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        gcmc: '',
        htbh: '',
        createYear: '',
        pageNumber: 1,
        pageSize: 20,
      },
      multipleSelection: [],
    }
  },
  methods: {
    showEdit(id) {
      this.fetchData(id)
      this.dialogFormVisible = true
    },
    resetQueryForm() {
      this.queryForm = {
        gcmc: '',
        htbh: '',
        createYear: '',
        pageNumber: 1,
        pageSize: 20,
      }
    },
    resetSearch() {
      this.resetQueryForm()
      this.fetchData()
    },
    formatDate(row, column) {
      // 获取单元格数据
      let data = row[column.property]
      return formatDate(data)
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
    async fetchData(id) {
      this.listLoading = true
      const {
        data
      } = await getUserPartDept({staffId: id})
      this.list = data
      this.listLoading = false
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    confirm() {
      if (this.multipleSelection.length < 1) {
        return this.$message.error('尚未选择数据！')
      }
      this.$emit('selected', this.multipleSelection)
      this.close()
    },
    close() {
      this.dialogFormVisible = false
      this.multipleSelection = []
    },
  },
}
</script>
<style scoped>
.el-form-item__content span {
  font-size: 14px;
  font-weight: 500;
  color: darkgray;
}
</style>
