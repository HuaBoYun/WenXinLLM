<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="表单组">
          <el-select v-model="queryForm.groupId" placeholder="请选择表单组" clearable>
            <el-option
              v-for="item in groupOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="模板名称">
          <el-input v-model="queryForm.templateName" placeholder="请输入模板名称" clearable />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="queryForm.templateType" placeholder="请选择模板类型" clearable>
            <el-option label="固定表" value="FIXED" />
            <el-option label="浮动表" value="FLOATING" />
          </el-select>
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

      <!-- 表单模板表格 -->
      <el-table
        v-loading="loading"
        :data="formTemplateList"
        border
      >
        <el-table-column prop="templateCode" label="模板编码" width="150" />
        <el-table-column prop="templateName" label="模板名称" width="200" />
        <el-table-column prop="groupName" label="所属表单组" width="150" />
        <el-table-column prop="templateType" label="模板类型" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.templateType === 'FIXED'">固定表</span>
            <span v-else-if="scope.row.templateType === 'FLOATING'">浮动表</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="versionNo" label="版本号" width="100" />
        <el-table-column prop="isDefault" label="默认版本" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isDefault === 'Y'" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
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
        <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              v-if="scope.row.isDefault !== 'Y'"
              size="mini"
              type="text"
              icon="el-icon-check"
              @click="handleSetDefault(scope.row)"
            >设为默认</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-document-copy"
              @click="handleCopy(scope.row)"
            >复制</el-button>
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

    <!-- 添加或修改表单模板对话框 -->
    <form-template-form
      ref="formTemplateForm"
      :visible.sync="dialogVisible"
      :form-data="formData"
      :group-options="groupOptions"
      @success="handleSuccess"
    />

    <!-- 复制模板对话框 -->
    <copy-template-dialog
      ref="copyTemplateDialog"
      :visible.sync="copyDialogVisible"
      :template-id="copyTemplateId"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getFormTemplateList, deleteFormTemplate, setDefaultVersion } from '@/api/financialSharing/enterpriseReport/formTemplate'
import { getFormGroupList } from '@/api/financialSharing/enterpriseReport/formGroup'
import FormTemplateForm from './components/FormTemplateForm'
import CopyTemplateDialog from './components/CopyTemplateDialog'

export default {
  name: 'FormTemplate',
  components: {
    FormTemplateForm,
    CopyTemplateDialog
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 查询参数
      queryForm: {
        groupId: '',
        templateName: '',
        templateType: '',
        status: ''
      },
      // 表单模板列表
      formTemplateList: [],
      // 表单组选项
      groupOptions: [],
      // 弹出层标题
      dialogVisible: false,
      // 表单参数
      formData: {},
      // 复制对话框
      copyDialogVisible: false,
      copyTemplateId: ''
    }
  },
  created() {
    this.getGroupOptions()
    this.getList()
  },
  methods: {
    /** 获取表单组选项 */
    getGroupOptions() {
      getFormGroupList({ status: 'ACTIVE' }).then(response => {
        if (response.code === 200) {
          this.groupOptions = (response.data || []).map(item => ({
            value: item.groupId,
            label: item.groupName
          }))
        }
      })
    },
    /** 查询表单模板列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getFormTemplateList(this.queryForm).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.formTemplateList = response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
          this.formTemplateList = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.formTemplateList = []
      })
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.queryForm = {
        groupId: '',
        templateName: '',
        templateType: '',
        status: ''
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.formData = {
        templateId: '',
        groupId: '',
        templateCode: '',
        templateName: '',
        templateType: 'FIXED',
        templateContent: '',
        versionNo: '1.0',
        startPeriod: '',
        endPeriod: '',
        isDefault: 'N',
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
    /** 设置默认版本 */
    handleSetDefault(row) {
      this.$confirm('是否确认将该模板设置为默认版本?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return setDefaultVersion(row.templateId)
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('设置成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '设置失败')
        }
      }).catch(() => {})
    },
    /** 复制按钮操作 */
    handleCopy(row) {
      this.copyTemplateId = row.templateId
      this.copyDialogVisible = true
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除该表单模板?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteFormTemplate(row.templateId)
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
      this.copyDialogVisible = false
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


