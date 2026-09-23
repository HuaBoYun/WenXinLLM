<template>
  <div class="indicator-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>考核指标配置</h2>
      <p>管理项目考核指标体系，配置指标权重和计算方法</p>
    </div>

    <!-- 操作按钮 -->
    <el-card class="operation-card" shadow="never">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
        新增指标
      </el-button>
      <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">
        导出配置
      </el-button>
    </el-card>

    <!-- 指标树形表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="indicatorTree"
        style="width: 100%"
        row-key="id"
        border
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        v-loading="loading"
      >
        <el-table-column prop="indicatorCode" label="指标编码" width="150" />
        <el-table-column prop="indicatorName" label="指标名称" width="200" />
        <el-table-column prop="indicatorType" label="指标类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.indicatorType === 1 ? 'success' : 'info'">
              {{ scope.row.indicatorType === 1 ? '定量' : '定性' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="weight" label="权重" width="100">
          <template slot-scope="scope">
            {{ (scope.row.weight * 100).toFixed(2) }}%
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="targetValue" label="目标值" width="100" />
        <el-table-column prop="dataSource" label="数据来源" width="150" />
        <el-table-column prop="isActive" label="状态" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isActive"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleAddChild(scope.row)">
              添加子指标
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑指标对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="indicatorForm"
        :model="indicatorForm"
        :rules="indicatorRules"
        label-width="120px"
      >
        <el-form-item label="指标编码" prop="indicatorCode">
          <el-input v-model="indicatorForm.indicatorCode" placeholder="请输入指标编码" />
        </el-form-item>
        <el-form-item label="指标名称" prop="indicatorName">
          <el-input v-model="indicatorForm.indicatorName" placeholder="请输入指标名称" />
        </el-form-item>
        <el-form-item label="父指标" prop="parentId">
          <el-select v-model="indicatorForm.parentId" placeholder="请选择父指标" clearable>
            <el-option
              v-for="item in parentOptions"
              :key="item.id"
              :label="item.indicatorName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="指标类型" prop="indicatorType">
          <el-radio-group v-model="indicatorForm.indicatorType">
            <el-radio :label="1">定量指标</el-radio>
            <el-radio :label="2">定性指标</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权重" prop="weight">
          <el-input-number
            v-model="indicatorForm.weight"
            :precision="4"
            :step="0.01"
            :max="1"
            :min="0"
            placeholder="请输入权重"
          />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="indicatorForm.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="目标值" prop="targetValue">
          <el-input-number
            v-model="indicatorForm.targetValue"
            :precision="2"
            placeholder="请输入目标值"
          />
        </el-form-item>
        <el-form-item label="计算方法" prop="calculationMethod">
          <el-input
            v-model="indicatorForm.calculationMethod"
            type="textarea"
            :rows="3"
            placeholder="请输入计算方法"
          />
        </el-form-item>
        <el-form-item label="数据来源" prop="dataSource">
          <el-input v-model="indicatorForm.dataSource" placeholder="请输入数据来源" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="indicatorForm.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getIndicatorTree,
  saveIndicator,
  updateIndicator,
  deleteIndicator,
  updateIndicatorStatus
} from '@/api/contract/assessment'

export default {
  name: 'AssessmentIndicator',
  data() {
    return {
      loading: false,
      submitting: false,
      indicatorTree: [],
      parentOptions: [],
      dialogVisible: false,
      dialogTitle: '新增指标',
      isEdit: false,
      indicatorForm: {
        id: null,
        indicatorCode: '',
        indicatorName: '',
        parentId: null,
        indicatorType: 1,
        weight: 0,
        unit: '',
        targetValue: null,
        calculationMethod: '',
        dataSource: '',
        sortOrder: 0
      },
      indicatorRules: {
        indicatorCode: [
          { required: true, message: '请输入指标编码', trigger: 'blur' }
        ],
        indicatorName: [
          { required: true, message: '请输入指标名称', trigger: 'blur' }
        ],
        indicatorType: [
          { required: true, message: '请选择指标类型', trigger: 'change' }
        ],
        weight: [
          { required: true, message: '请输入权重', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.loadIndicatorTree()
  },
  methods: {
    // 加载指标树
    async loadIndicatorTree() {
      this.loading = true
      try {
        const response = await getIndicatorTree()
        if (response.code === 1) {
          this.indicatorTree = response.data || []
          this.buildParentOptions()
        } else {
          this.$message.error(response.msg || '加载指标树失败')
        }
      } catch (error) {
        this.$message.error('加载指标树失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 构建父指标选项
    buildParentOptions() {
      this.parentOptions = []
      const buildOptions = (nodes, level = 0) => {
        nodes.forEach(node => {
          this.parentOptions.push({
            id: node.id,
            indicatorName: '　'.repeat(level) + node.indicatorName
          })
          if (node.children && node.children.length > 0) {
            buildOptions(node.children, level + 1)
          }
        })
      }
      buildOptions(this.indicatorTree)
    },

    // 新增指标
    handleAdd() {
      this.dialogTitle = '新增指标'
      this.isEdit = false
      this.resetForm()
      this.dialogVisible = true
    },

    // 添加子指标
    handleAddChild(row) {
      this.dialogTitle = '新增子指标'
      this.isEdit = false
      this.resetForm()
      this.indicatorForm.parentId = row.id
      this.dialogVisible = true
    },

    // 编辑指标
    handleEdit(row) {
      this.dialogTitle = '编辑指标'
      this.isEdit = true
      this.indicatorForm = { ...row }
      this.dialogVisible = true
    },

    // 删除指标
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该指标吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteIndicator(row.id)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadIndicatorTree()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 状态变更
    async handleStatusChange(row) {
      try {
        const response = await updateIndicatorStatus(row.id, row.isActive)
        if (response.code === 1) {
          this.$message.success('状态更新成功')
        } else {
          this.$message.error(response.msg || '状态更新失败')
          row.isActive = row.isActive === 1 ? 0 : 1 // 回滚状态
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
        row.isActive = row.isActive === 1 ? 0 : 1 // 回滚状态
      }
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.indicatorForm.validate()
        this.submitting = true
        
        const response = this.isEdit 
          ? await updateIndicator(this.indicatorForm)
          : await saveIndicator(this.indicatorForm)
          
        if (response.code === 1) {
          this.$message.success(this.isEdit ? '更新成功' : '新增成功')
          this.dialogVisible = false
          this.loadIndicatorTree()
        } else {
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        if (error !== false) { // 表单验证失败时error为false
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.submitting = false
      }
    },

    // 刷新
    handleRefresh() {
      this.loadIndicatorTree()
    },

    // 导出配置
    handleExport() {
      this.$message.info('导出功能开发中...')
    },

    // 重置表单
    resetForm() {
      this.indicatorForm = {
        id: null,
        indicatorCode: '',
        indicatorName: '',
        parentId: null,
        indicatorType: 1,
        weight: 0,
        unit: '',
        targetValue: null,
        calculationMethod: '',
        dataSource: '',
        sortOrder: 0
      }
      if (this.$refs.indicatorForm) {
        this.$refs.indicatorForm.resetFields()
      }
    },

    // 对话框关闭
    handleDialogClose() {
      this.resetForm()
    },

    // 获取计算方式名称
    getCalculationMethodName(method) {
      const methodMap = {
        '1': '直接取值',
        '2': '百分比计算',
        '3': '加权平均',
        '4': '累计求和',
        '直接取值': '直接取值',
        '百分比计算': '百分比计算',
        '加权平均': '加权平均',
        '累计求和': '累计求和'
      }
      return methodMap[method] || method
    }
  }
}
</script>

<style scoped>
.indicator-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.operation-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
