<template>
  <el-dialog
    title="批量操作科目"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading">
      <!-- 选中的科目列表 -->
      <div class="selected-subjects">
        <h4>已选择 {{ selectedSubjects.length }} 个科目</h4>
        <div class="subject-list">
          <el-tag
            v-for="subject in selectedSubjects"
            :key="subject.subjectId"
            closable
            @close="removeSubject(subject)"
            style="margin: 0 8px 8px 0;"
          >
            {{ subject.subjectCode }} - {{ subject.subjectName }}
          </el-tag>
        </div>
      </div>

      <el-divider></el-divider>

      <!-- 批量操作选项 -->
      <div class="batch-operations">
        <h4>选择操作类型</h4>
        <el-form :model="form" :rules="rules" ref="form" label-width="120px">
          <el-form-item label="操作类型" prop="operationType">
            <el-select
              v-model="form.operationType"
              placeholder="请选择操作类型"
              style="width: 100%"
              @change="handleOperationChange"
            >
              <el-option label="批量启用" value="enable" />
              <el-option label="批量禁用" value="disable" />
              <el-option label="批量移动" value="move" />
              <el-option label="批量修改类型" value="changeType" />
              <el-option label="批量修改分类" value="changeCategory" />
              <el-option label="批量设置单位" value="setUnit" />
              <el-option label="批量设置精度" value="setPrecision" />
              <el-option label="批量删除" value="delete" />
            </el-select>
          </el-form-item>

          <!-- 批量移动 -->
          <el-form-item
            v-if="form.operationType === 'move'"
            label="目标父科目"
            prop="targetParentId"
          >
            <el-cascader
              v-model="targetParentPath"
              :options="subjectTreeOptions"
              :props="cascaderProps"
              placeholder="请选择目标父科目"
              style="width: 100%"
              clearable
              @change="handleTargetChange"
            />
          </el-form-item>

          <!-- 批量修改类型 -->
          <el-form-item
            v-if="form.operationType === 'changeType'"
            label="目标科目类型"
            prop="targetSubjectType"
          >
            <el-select
              v-model="form.targetSubjectType"
              placeholder="请选择科目类型"
              style="width: 100%"
            >
              <el-option label="收入类" value="INCOME" />
              <el-option label="支出类" value="EXPENSE" />
              <el-option label="资产类" value="ASSET" />
              <el-option label="负债类" value="LIABILITY" />
            </el-select>
          </el-form-item>

          <!-- 批量修改分类 -->
          <el-form-item
            v-if="form.operationType === 'changeCategory'"
            label="目标科目分类"
            prop="targetSubjectCategory"
          >
            <el-select
              v-model="form.targetSubjectCategory"
              placeholder="请选择科目分类"
              style="width: 100%"
            >
              <el-option label="基础科目" value="BASIC" />
              <el-option label="明细科目" value="DETAIL" />
              <el-option label="辅助科目" value="AUXILIARY" />
            </el-select>
          </el-form-item>

          <!-- 批量设置单位 -->
          <el-form-item
            v-if="form.operationType === 'setUnit'"
            label="计量单位"
            prop="targetUnit"
          >
            <el-input
              v-model="form.targetUnit"
              placeholder="请输入计量单位"
            />
          </el-form-item>

          <!-- 批量设置精度 -->
          <el-form-item
            v-if="form.operationType === 'setPrecision'"
            label="精度位数"
            prop="targetPrecision"
          >
            <el-input-number
              v-model="form.targetPrecision"
              :min="0"
              :max="6"
              placeholder="请输入精度位数"
              style="width: 100%"
            />
          </el-form-item>

          <!-- 操作说明 -->
          <el-form-item label="操作说明">
            <el-input
              v-model="form.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入操作说明（可选）"
            />
          </el-form-item>
        </el-form>

        <!-- 操作预览 -->
        <div class="operation-preview" v-if="form.operationType">
          <h4>操作预览</h4>
          <div class="preview-content">
            <p><strong>操作类型：</strong>{{ getOperationLabel(form.operationType) }}</p>
            <p><strong>影响科目：</strong>{{ selectedSubjects.length }} 个</p>
            <p v-if="form.operationType === 'move' && targetParentName">
              <strong>目标位置：</strong>{{ targetParentName }}
            </p>
            <p v-if="form.operationType === 'changeType' && form.targetSubjectType">
              <strong>目标类型：</strong>{{ getTypeLabel(form.targetSubjectType) }}
            </p>
            <p v-if="form.operationType === 'changeCategory' && form.targetSubjectCategory">
              <strong>目标分类：</strong>{{ getCategoryLabel(form.targetSubjectCategory) }}
            </p>
            <p v-if="form.operationType === 'setUnit' && form.targetUnit">
              <strong>目标单位：</strong>{{ form.targetUnit }}
            </p>
            <p v-if="form.operationType === 'setPrecision' && form.targetPrecision !== null">
              <strong>目标精度：</strong>{{ form.targetPrecision }} 位小数
            </p>
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        type="primary"
        @click="handleSubmit"
        :loading="submitLoading"
        :disabled="!form.operationType || selectedSubjects.length === 0"
      >
        执行操作
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getBudgetSubjectTree,
  batchOperateSubjects
} from '@/api/managementAccountant/eps/budgetSubject'

export default {
  name: 'BudgetSubjectBatch',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitLoading: false,
      
      selectedSubjects: [],
      
      form: {
        operationType: '',
        targetParentId: null,
        targetSubjectType: '',
        targetSubjectCategory: '',
        targetUnit: '',
        targetPrecision: null,
        remark: ''
      },
      
      rules: {
        operationType: [
          { required: true, message: '请选择操作类型', trigger: 'change' }
        ],
        targetParentId: [
          { required: true, message: '请选择目标父科目', trigger: 'change' }
        ],
        targetSubjectType: [
          { required: true, message: '请选择目标科目类型', trigger: 'change' }
        ],
        targetSubjectCategory: [
          { required: true, message: '请选择目标科目分类', trigger: 'change' }
        ],
        targetUnit: [
          { required: true, message: '请输入计量单位', trigger: 'blur' }
        ],
        targetPrecision: [
          { required: true, message: '请输入精度位数', trigger: 'blur' }
        ]
      },
      
      // 科目树选项
      subjectTreeOptions: [],
      // 目标父科目路径
      targetParentPath: [],
      // 目标父科目名称
      targetParentName: '',
      // 级联选择器配置
      cascaderProps: {
        value: 'id',
        label: 'label',
        children: 'children',
        checkStrictly: true,
        emitPath: false
      }
    }
  },
  
  methods: {
    // 打开对话框
    async open(selectedSubjects) {
      this.selectedSubjects = [...selectedSubjects]
      this.dialogVisible = true
      
      if (this.selectedSubjects.length > 0) {
        await this.getSubjectTreeOptions()
      }
    },
    
    // 获取科目树选项
    async getSubjectTreeOptions() {
      try {
        // 获取第一个科目的体系ID
        const systemId = this.selectedSubjects[0].systemId
        
        const response = await getBudgetSubjectTree({
          systemId: systemId,
          includeDisabled: false
        })
        if (response.code === 200) {
          this.subjectTreeOptions = response.data || []
        }
      } catch (error) {
        console.error('获取科目树失败:', error)
      }
    },
    
    // 移除科目
    removeSubject(subject) {
      const index = this.selectedSubjects.findIndex(s => s.subjectId === subject.subjectId)
      if (index > -1) {
        this.selectedSubjects.splice(index, 1)
      }
    },
    
    // 操作类型变化
    handleOperationChange(value) {
      // 清除相关验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
      
      // 重置相关字段
      this.form.targetParentId = null
      this.form.targetSubjectType = ''
      this.form.targetSubjectCategory = ''
      this.form.targetUnit = ''
      this.form.targetPrecision = null
      this.targetParentPath = []
      this.targetParentName = ''
    },
    
    // 目标父科目变化
    handleTargetChange(value) {
      this.form.targetParentId = value || null
      
      // 查找目标父科目名称
      if (value) {
        const parentOption = this.findOptionById(this.subjectTreeOptions, value)
        this.targetParentName = parentOption ? parentOption.label : ''
      } else {
        this.targetParentName = '根级别'
      }
    },
    
    // 在树选项中查找指定ID的节点
    findOptionById(options, id) {
      for (const option of options) {
        if (option.id === id) {
          return option
        }
        if (option.children && option.children.length > 0) {
          const found = this.findOptionById(option.children, id)
          if (found) {
            return found
          }
        }
      }
      return null
    },
    
    // 提交批量操作
    async handleSubmit() {
      try {
        // 根据操作类型进行不同的验证
        const fieldsToValidate = ['operationType']
        
        if (this.form.operationType === 'move') {
          fieldsToValidate.push('targetParentId')
        } else if (this.form.operationType === 'changeType') {
          fieldsToValidate.push('targetSubjectType')
        } else if (this.form.operationType === 'changeCategory') {
          fieldsToValidate.push('targetSubjectCategory')
        } else if (this.form.operationType === 'setUnit') {
          fieldsToValidate.push('targetUnit')
        } else if (this.form.operationType === 'setPrecision') {
          fieldsToValidate.push('targetPrecision')
        }
        
        // 验证必填字段
        for (const field of fieldsToValidate) {
          await this.$refs.form.validateField(field)
        }
        
        // 确认操作
        const operationLabel = this.getOperationLabel(this.form.operationType)
        await this.$confirm(
          `确定要对选中的 ${this.selectedSubjects.length} 个科目执行"${operationLabel}"操作吗？`,
          '确认批量操作',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        this.submitLoading = true
        
        // 构建批量操作数据
        const batchData = {
          operationType: this.form.operationType,
          subjectIds: this.selectedSubjects.map(s => s.subjectId),
          operationParams: {},
          remark: this.form.remark
        }
        
        // 根据操作类型设置参数
        switch (this.form.operationType) {
          case 'move':
            batchData.operationParams.targetParentId = this.form.targetParentId
            break
          case 'changeType':
            batchData.operationParams.targetSubjectType = this.form.targetSubjectType
            break
          case 'changeCategory':
            batchData.operationParams.targetSubjectCategory = this.form.targetSubjectCategory
            break
          case 'setUnit':
            batchData.operationParams.targetUnit = this.form.targetUnit
            break
          case 'setPrecision':
            batchData.operationParams.targetPrecision = this.form.targetPrecision
            break
        }
        
        const response = await batchOperateSubjects(batchData)
        
        if (response.code === 200) {
          this.$message.success('批量操作成功')
          this.handleClose()
          this.$emit('success')
        } else {
          this.$message.error(response.message || '批量操作失败')
        }
      } catch (error) {
        if (error !== 'cancel' && error !== false) {
          this.$message.error('批量操作失败')
          console.error(error)
        }
      } finally {
        this.submitLoading = false
      }
    },
    
    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    
    // 重置表单
    resetForm() {
      this.selectedSubjects = []
      this.form = {
        operationType: '',
        targetParentId: null,
        targetSubjectType: '',
        targetSubjectCategory: '',
        targetUnit: '',
        targetPrecision: null,
        remark: ''
      }
      this.targetParentPath = []
      this.targetParentName = ''
      this.subjectTreeOptions = []
      
      if (this.$refs.form) {
        this.$refs.form.clearValidate()
      }
    },
    
    // 获取操作标签
    getOperationLabel(operationType) {
      const operationMap = {
        'enable': '批量启用',
        'disable': '批量禁用',
        'move': '批量移动',
        'changeType': '批量修改类型',
        'changeCategory': '批量修改分类',
        'setUnit': '批量设置单位',
        'setPrecision': '批量设置精度',
        'delete': '批量删除'
      }
      return operationMap[operationType] || operationType
    },
    
    // 获取类型标签
    getTypeLabel(type) {
      const typeMap = {
        'INCOME': '收入类',
        'EXPENSE': '支出类',
        'ASSET': '资产类',
        'LIABILITY': '负债类'
      }
      return typeMap[type] || type
    },
    
    // 获取分类标签
    getCategoryLabel(category) {
      const categoryMap = {
        'BASIC': '基础科目',
        'DETAIL': '明细科目',
        'AUXILIARY': '辅助科目'
      }
      return categoryMap[category] || category
    }
  }
}
</script>

<style lang="scss" scoped>
.selected-subjects {
  margin-bottom: 20px;
  
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
  
  .subject-list {
    max-height: 120px;
    overflow-y: auto;
    padding: 10px;
    background: #f5f7fa;
    border-radius: 4px;
  }
}

.batch-operations {
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
}

.operation-preview {
  margin-top: 20px;
  
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
  
  .preview-content {
    background: #e6f7ff;
    border: 1px solid #91d5ff;
    padding: 15px;
    border-radius: 4px;
    
    p {
      margin: 0 0 8px 0;
      font-size: 14px;
      color: #606266;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      strong {
        color: #303133;
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
