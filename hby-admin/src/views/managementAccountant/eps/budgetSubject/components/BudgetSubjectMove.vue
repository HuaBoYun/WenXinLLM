<template>
  <el-dialog
    title="移动科目"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading">
      <div class="move-info">
        <h4>当前科目信息</h4>
        <div class="subject-info">
          <p><strong>科目编码：</strong>{{ currentSubject.subjectCode }}</p>
          <p><strong>科目名称：</strong>{{ currentSubject.subjectName }}</p>
          <p><strong>当前层级：</strong>{{ currentSubject.subjectLevel }}</p>
          <p><strong>当前路径：</strong>{{ currentSubject.subjectPath }}</p>
        </div>
      </div>

      <el-divider></el-divider>

      <div class="move-target">
        <h4>选择目标位置</h4>
        <el-form :model="form" :rules="rules" ref="form" label-width="100px">
          <el-form-item label="目标父科目" prop="targetParentId">
            <el-cascader
              v-model="targetParentPath"
              :options="subjectTreeOptions"
              :props="cascaderProps"
              placeholder="请选择目标父科目（留空表示移动到根级别）"
              style="width: 100%"
              clearable
              @change="handleTargetChange"
            />
          </el-form-item>
          
          <el-form-item label="目标位置" prop="targetPosition">
            <el-input-number
              v-model="form.targetPosition"
              :min="1"
              placeholder="请输入目标位置"
              style="width: 100%"
            />
            <div class="help-text">
              <small>位置序号，数字越小越靠前</small>
            </div>
          </el-form-item>
        </el-form>

        <!-- 预览移动后的结果 -->
        <div class="move-preview" v-if="previewInfo">
          <h4>移动预览</h4>
          <div class="preview-info">
            <p><strong>移动后层级：</strong>{{ previewInfo.newLevel }}</p>
            <p><strong>移动后路径：</strong>{{ previewInfo.newPath }}</p>
            <p><strong>影响的子科目：</strong>{{ previewInfo.affectedChildren }} 个</p>
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
        确认移动
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getBudgetSubjectTree,
  getBudgetSubjectById,
  getChildBudgetSubjects,
  moveBudgetSubject
} from '@/api/managementAccountant/eps/budgetSubject'

export default {
  name: 'BudgetSubjectMove',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitLoading: false,
      
      currentSubject: {},
      
      form: {
        subjectId: null,
        targetParentId: null,
        targetPosition: 1
      },
      
      rules: {
        targetPosition: [
          { required: true, message: '请输入目标位置', trigger: 'blur' },
          { type: 'number', min: 1, message: '位置必须大于0', trigger: 'blur' }
        ]
      },
      
      // 科目树选项
      subjectTreeOptions: [],
      // 目标父科目路径
      targetParentPath: [],
      // 级联选择器配置
      cascaderProps: {
        value: 'id',
        label: 'label',
        children: 'children',
        checkStrictly: true,
        emitPath: false
      },
      
      // 预览信息
      previewInfo: null
    }
  },
  
  methods: {
    // 打开对话框
    async open(subjectData) {
      this.dialogVisible = true
      this.currentSubject = subjectData
      this.form.subjectId = subjectData.subjectId || subjectData.id
      
      await this.loadSubjectDetail()
      await this.getSubjectTreeOptions()
      this.generatePreview()
    },
    
    // 加载科目详情
    async loadSubjectDetail() {
      try {
        this.loading = true
        const response = await getBudgetSubjectById(this.form.subjectId)
        if (response.code === 200) {
          this.currentSubject = response.data
        }
      } catch (error) {
        this.$message.error('加载科目详情失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    // 获取科目树选项（排除当前科目及其子科目）
    async getSubjectTreeOptions() {
      try {
        const response = await getBudgetSubjectTree({
          systemId: this.currentSubject.systemId,
          includeDisabled: false
        })
        if (response.code === 200) {
          this.subjectTreeOptions = this.filterTreeOptions(response.data || [])
        }
      } catch (error) {
        console.error('获取科目树失败:', error)
      }
    },
    
    // 过滤树选项，排除当前科目及其子科目
    filterTreeOptions(options) {
      return options.filter(option => {
        // 排除当前科目
        if (option.id === this.form.subjectId) {
          return false
        }
        
        // 排除当前科目的子科目
        if (this.isChildOfCurrent(option)) {
          return false
        }
        
        // 递归处理子节点
        if (option.children && option.children.length > 0) {
          option.children = this.filterTreeOptions(option.children)
        }
        
        return true
      })
    },
    
    // 判断是否为当前科目的子科目
    isChildOfCurrent(option) {
      // 简单的路径判断，实际项目中可能需要更复杂的逻辑
      if (this.currentSubject.subjectPath && option.data && option.data.subjectPath) {
        return option.data.subjectPath.startsWith(this.currentSubject.subjectPath + '/')
      }
      return false
    },
    
    // 目标父科目变化
    handleTargetChange(value) {
      this.form.targetParentId = value || null
      this.generatePreview()
    },
    
    // 生成移动预览
    async generatePreview() {
      try {
        let newLevel = 1
        let newPath = '/' + this.currentSubject.subjectCode
        
        if (this.form.targetParentId) {
          // 查找目标父科目信息
          const parentOption = this.findOptionById(this.subjectTreeOptions, this.form.targetParentId)
          if (parentOption && parentOption.data) {
            newLevel = (parentOption.data.subjectLevel || 0) + 1
            newPath = (parentOption.data.subjectPath || '') + '/' + this.currentSubject.subjectCode
          }
        }
        
        // 获取子科目数量
        const childrenResponse = await getChildBudgetSubjects(this.form.subjectId)
        const affectedChildren = childrenResponse.code === 200 ? (childrenResponse.data || []).length : 0
        
        this.previewInfo = {
          newLevel,
          newPath,
          affectedChildren
        }
      } catch (error) {
        console.error('生成预览失败:', error)
        this.previewInfo = null
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
    
    // 提交移动
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        
        // 确认移动
        await this.$confirm(
          `确定要将科目"${this.currentSubject.subjectName}"移动到新位置吗？`,
          '确认移动',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        this.submitLoading = true
        
        const response = await moveBudgetSubject(
          this.form.subjectId,
          this.form.targetParentId,
          this.form.targetPosition
        )
        
        if (response.code === 200) {
          this.$message.success('移动成功')
          this.handleClose()
          this.$emit('success')
        } else {
          this.$message.error(response.message || '移动失败')
        }
      } catch (error) {
        if (error !== 'cancel' && error !== false) {
          this.$message.error('移动失败')
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
      this.currentSubject = {}
      this.form = {
        subjectId: null,
        targetParentId: null,
        targetPosition: 1
      }
      this.targetParentPath = []
      this.previewInfo = null
      this.subjectTreeOptions = []
      
      if (this.$refs.form) {
        this.$refs.form.clearValidate()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.move-info {
  margin-bottom: 20px;
  
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
  
  .subject-info {
    background: #f5f7fa;
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

.move-target {
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
  
  .help-text {
    margin-top: 5px;
    
    small {
      color: #909399;
    }
  }
}

.move-preview {
  margin-top: 20px;
  
  h4 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
  
  .preview-info {
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
