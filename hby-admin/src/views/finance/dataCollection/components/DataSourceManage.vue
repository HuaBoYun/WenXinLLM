<template>
  <el-dialog
    title="数据源管理"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <!-- 查询表单 -->
    <el-form :model="queryForm" inline label-width="100px" class="query-form">
      <el-form-item label="数据库类型">
        <el-select v-model="queryForm.financedbtype" placeholder="请选择数据库类型" clearable>
          <el-option label="Oracle" value="Oracle" />
          <el-option label="Mysql" value="Mysql" />
          <el-option label="SqlServer" value="SqlServer" />
          <el-option label="达梦" value="Dm" />
          <el-option label="OceanBase" value="OceanBase" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据源列表 -->
    <el-table
      :data="tableData"
      v-loading="loading"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column prop="fintext" label="数据源名称" min-width="150" align="center" />
      <el-table-column prop="financedbtype" label="数据库类型" width="120" align="center" />
      <el-table-column prop="financeconn" label="数据库连接" min-width="200" align="center" show-overflow-tooltip />
      <el-table-column prop="financeuser" label="数据库用户" width="150" align="center" />
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="pageNumber"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handlePageSizeChange"
      @current-change="handlePageChange"
      style="margin-top: 20px; text-align: right"
    />

    <!-- 数据源编辑对话框 -->
    <DataSourceEdit ref="dataSourceEdit" @refresh="loadData" />
  </el-dialog>
</template>

<script>
import { getDataSourceList, deleteDataSource } from '@/api/cwsc'
import DataSourceEdit from './DataSourceEdit.vue'

export default {
  name: 'DataSourceManage',
  components: {
    DataSourceEdit
  },
  data() {
    return {
      dialogVisible: false,
      queryForm: {
        financedbtype: '',
        pageNumber: 1,
        pageSize: 20
      },
      tableData: [],
      loading: false,
      pageNumber: 1,
      pageSize: 20,
      total: 0
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
      this.loadData()
    },
    handleClose() {
      this.dialogVisible = false
      this.queryForm = {
        financedbtype: '',
        pageNumber: 1,
        pageSize: 20
      }
    },
    handleQuery() {
      this.pageNumber = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm.financedbtype = ''
      this.pageNumber = 1
      this.loadData()
    },
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pageNumber,
          pageSize: this.pageSize
        }
        const res = await getDataSourceList(params)
        if (res.code === 1) {
          this.tableData = res.data.records || []
          this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '加载数据失败')
        }
      } catch (error) {
        this.$message.error('加载数据失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    handlePageChange(pageNumber) {
      this.pageNumber = pageNumber
      this.loadData()
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNumber = 1
      this.loadData()
    },
    handleAdd() {
      this.$refs.dataSourceEdit.showEdit(null, 'add')
    },
    handleEdit(row) {
      this.$refs.dataSourceEdit.showEdit(row, 'edit')
    },
    handleDelete(row) {
      this.$confirm('确定要删除该数据源吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteDataSource({ fid: row.fid })
          if (res.code === 1 || res.msg === '成功') {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
          console.error(error)
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.query-form {
  margin-bottom: 20px;
}
</style>

