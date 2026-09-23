<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="目录">
          <el-select v-model="queryForm.directoryId" placeholder="请选择目录" clearable>
            <el-option
              v-for="item in directoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="表单组名称">
          <el-input v-model="queryForm.groupName" placeholder="请输入表单组名称" clearable />
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
      </el-row>

      <!-- 表单组表格 -->
      <el-table
        v-loading="loading"
        :data="formGroupList"
        border
      >
        <el-table-column prop="groupCode" label="表单组编码" width="150" />
        <el-table-column prop="groupName" label="表单组名称" width="200" />
        <el-table-column prop="directoryName" label="所属目录" width="150" />
        <el-table-column prop="periodType" label="周期类型" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.periodType === 'YEAR'">年</span>
            <span v-else-if="scope.row.periodType === 'HALF_YEAR'">半年</span>
            <span v-else-if="scope.row.periodType === 'QUARTER'">季度</span>
            <span v-else-if="scope.row.periodType === 'MONTH'">月</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
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
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改表单组对话框 -->
    <form-group-form
      ref="formGroupForm"
      :visible.sync="dialogVisible"
      :form-data="formData"
      :directory-options="directoryOptions"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getFormGroupList, deleteFormGroup } from '@/api/financialSharing/enterpriseReport/formGroup'
import { getDirectoryList } from '@/api/financialSharing/enterpriseReport/formDirectory'
import FormGroupForm from './components/FormGroupForm'

export default {
  name: 'FormGroup',
  components: {
    FormGroupForm
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 查询参数
      queryForm: {
        directoryId: '',
        groupName: '',
        status: ''
      },
      // 表单组列表
      formGroupList: [],
      // 目录选项
      directoryOptions: [],
      // 弹出层标题
      dialogVisible: false,
      // 表单参数
      formData: {}
    }
  },
  created() {
    this.getDirectoryOptions()
    this.getList()
  },
  methods: {
    /** 获取目录选项 */
    getDirectoryOptions() {
      getDirectoryList({ status: 'ACTIVE' }).then(response => {
        if (response.code === 200) {
          this.directoryOptions = (response.data || []).map(item => ({
            value: item.directoryId,
            label: item.directoryName
          }))
        }
      })
    },
    /** 查询表单组列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getFormGroupList(this.queryForm).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.formGroupList = response.data.list || response.data.records || response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
          this.formGroupList = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.formGroupList = []
      })
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.queryForm = {
        directoryId: '',
        groupName: '',
        status: ''
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.formData = {
        groupId: '',
        directoryId: '',
        groupCode: '',
        groupName: '',
        fixedDimensions: '',
        parameterDimensions: '',
        periodType: '',
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
      this.$confirm('是否确认删除该表单组?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteFormGroup(row.groupId)
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


