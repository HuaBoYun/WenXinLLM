<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="目录名称">
          <el-input v-model="queryForm.directoryName" placeholder="请输入目录名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="ACTIVE" />
            <el-option label="停用" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="info"
            icon="el-icon-sort"
            size="mini"
            @click="toggleExpandAll"
          >展开/折叠</el-button>
        </el-col>
      </el-row>

      <!-- 表单目录树形表格 -->
      <el-table
        v-loading="loading"
        :data="directoryList"
        row-key="directoryId"
        :default-expand-all="isExpandAll"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      >
        <el-table-column prop="directoryCode" label="目录编码" width="180" />
        <el-table-column prop="directoryName" label="目录名称" width="200" />
        <el-table-column prop="directoryLevel" label="层级" width="80" />
        <el-table-column prop="sortNo" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'ACTIVE'" type="success">启用</el-tag>
            <el-tag v-else type="info">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleAdd(scope.row)"
            >新增</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改表单目录对话框 -->
    <form-directory-form
      ref="formDirectoryForm"
      :visible.sync="dialogVisible"
      :form-data="formData"
      :directory-options="directoryOptions"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getDirectoryTree, deleteDirectory } from '@/api/financialSharing/enterpriseReport/formDirectory'
import FormDirectoryForm from './components/FormDirectoryForm'

export default {
  name: 'FormDirectory',
  components: {
    FormDirectoryForm
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 是否展开,默认全部展开
      isExpandAll: true,
      // 查询参数
      queryForm: {
        directoryName: '',
        status: ''
      },
      // 表单目录树数据
      directoryList: [],
      // 表单目录树选项
      directoryOptions: [],
      // 弹出层标题
      dialogVisible: false,
      // 表单参数
      formData: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询表单目录列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getDirectoryTree(this.queryForm).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.directoryList = response.data || []
          this.directoryOptions = this.buildDirectoryOptions(this.directoryList)
        } else {
          this.$message.error(response.msg || '查询失败')
          this.directoryList = []
          this.directoryOptions = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.directoryList = []
        this.directoryOptions = []
      })
    },
    /** 构建目录选项树 */
    buildDirectoryOptions(directories, level = 0) {
      const options = []
      directories.forEach(dir => {
        const option = {
          value: dir.directoryId,
          label: '　'.repeat(level) + dir.directoryName,
          disabled: dir.status === 'INACTIVE'
        }
        options.push(option)
        if (dir.children && dir.children.length > 0) {
          options.push(...this.buildDirectoryOptions(dir.children, level + 1))
        }
      })
      return options
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.queryForm = {
        directoryName: '',
        status: ''
      }
      this.getList()
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.isExpandAll = !this.isExpandAll
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.formData = {
        directoryId: '',
        directoryCode: '',
        directoryName: '',
        parentDirectoryId: row ? row.directoryId : '',
        sortNo: 0,
        status: 'ACTIVE'
      }
      this.dialogVisible = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.formData = { ...row }
      this.dialogVisible = true
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除该表单目录?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteDirectory(row.directoryId)
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      }).catch(() => {})
    },
    /** 表单提交成功回调 */
    handleSuccess() {
      this.dialogVisible = false
      this.getList()
    },
    /** 时间格式化 */
    parseTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>


