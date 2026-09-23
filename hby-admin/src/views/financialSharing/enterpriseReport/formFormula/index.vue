<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="所属模板">
          <el-select v-model="queryForm.templateId" placeholder="请选择所属模板" clearable>
            <el-option
              v-for="item in templateOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="公式名称">
          <el-input v-model="queryForm.formulaName" placeholder="请输入公式名称" clearable />
        </el-form-item>
        <el-form-item label="公式类型">
          <el-select v-model="queryForm.formulaType" placeholder="请选择公式类型" clearable>
            <el-option label="计算公式" value="CALCULATION" />
            <el-option label="汇总公式" value="SUMMARY" />
            <el-option label="校验公式" value="VALIDATION" />
            <el-option label="取数公式" value="FETCH" />
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
        <el-col :span="1.5">
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="selectedIds.length === 0"
            @click="handleBatchDelete"
          >批量删除</el-button>
        </el-col>
      </el-row>

      <!-- 表单公式表格 -->
      <el-table
        v-loading="loading"
        :data="formFormulaList"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="formulaCode" label="公式编码" width="150" />
        <el-table-column prop="formulaName" label="公式名称" width="200" />
        <el-table-column prop="templateName" label="所属模板" width="150" />
        <el-table-column prop="formulaType" label="公式类型" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.formulaType === 'CALCULATION'">计算公式</span>
            <span v-else-if="scope.row.formulaType === 'SUMMARY'">汇总公式</span>
            <span v-else-if="scope.row.formulaType === 'VALIDATION'">校验公式</span>
            <span v-else-if="scope.row.formulaType === 'FETCH'">取数公式</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="formulaExpression" label="公式表达式" min-width="200" show-overflow-tooltip />
        <el-table-column prop="validationType" label="校验类型" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.validationType === 'MANDATORY'">强制</span>
            <span v-else-if="scope.row.validationType === 'PROMPT'">提示</span>
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
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
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

    <!-- 添加或修改表单公式对话框 -->
    <form-formula-form
      ref="formFormulaForm"
      :visible.sync="dialogVisible"
      :form-data="formData"
      :template-options="templateOptions"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getFormFormulaList, deleteFormFormula, batchDeleteFormFormula } from '@/api/financialSharing/enterpriseReport/formFormula'
import { getFormTemplateList } from '@/api/financialSharing/enterpriseReport/formTemplate'
import FormFormulaForm from './components/FormFormulaForm'

export default {
  name: 'FormFormula',
  components: {
    FormFormulaForm
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 查询参数
      queryForm: {
        templateId: '',
        formulaName: '',
        formulaType: '',
        status: ''
      },
      // 表单公式列表
      formFormulaList: [],
      // 模板选项
      templateOptions: [],
      // 弹出层标题
      dialogVisible: false,
      // 表单参数
      formData: {},
      // 选中的ID列表
      selectedIds: []
    }
  },
  created() {
    this.getTemplateOptions()
    this.getList()
  },
  methods: {
    /** 获取模板选项 */
    getTemplateOptions() {
      getFormTemplateList({ status: 'ACTIVE' }).then(response => {
        if (response.code === 200) {
          this.templateOptions = (response.data || []).map(item => ({
            value: item.templateId,
            label: item.templateName
          }))
        }
      })
    },
    /** 查询表单公式列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getFormFormulaList(this.queryForm).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.formFormulaList = response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
          this.formFormulaList = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.formFormulaList = []
      })
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.queryForm = {
        templateId: '',
        formulaName: '',
        formulaType: '',
        status: ''
      }
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.formData = {
        formulaId: '',
        templateId: '',
        formulaCode: '',
        formulaName: '',
        formulaType: 'CALCULATION',
        formulaExpression: '',
        validationType: '',
        toleranceRange: null,
        effectiveCondition: '',
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
      this.$confirm('是否确认删除该表单公式?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteFormFormula(row.formulaId)
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      }).catch(() => {})
    },
    /** 批量删除按钮操作 */
    handleBatchDelete() {
      this.$confirm('是否确认删除选中的表单公式?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return batchDeleteFormFormula(this.selectedIds)
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      }).catch(() => {})
    },
    /** 表格选择变化 */
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.formulaId)
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


