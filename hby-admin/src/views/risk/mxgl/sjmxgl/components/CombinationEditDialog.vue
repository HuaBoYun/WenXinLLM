<template>
  <el-dialog
    :title="isEdit ? '编辑组合' : '新建组合'"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :lock-scroll="true"
    custom-class="combination-edit-dialog risk-mxgl-sjmxgl-page"
    :z-index="2800"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="100px"
      size="small"
    >
      <el-form-item label="组合名称" prop="combinationName">
        <el-input
          v-model="formData.combinationName"
          placeholder="请输入组合名称"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="组合编码" prop="combinationCode">
        <el-input
          v-model="formData.combinationCode"
          placeholder="请输入组合编码"
          maxlength="50"
          :disabled="isEdit"
        />
      </el-form-item>
      
      <el-form-item label="领域分类" prop="category">
        <el-select
          v-model="formData.category"
          placeholder="请选择领域分类"
          style="width: 100%"
        >
          <el-option label="投资穿透" value="INVESTMENT_PENETRATION" />
          <el-option label="产权穿透" value="PROPERTY_PENETRATION" />
          <el-option label="财务穿透" value="FINANCIAL_PENETRATION" />
          <el-option label="金融风险穿透" value="FINANCIAL_RISK_PENETRATION" />
          <el-option label="会计穿透" value="ACCOUNTING_PENETRATION" />
          <el-option label="薪酬分配" value="SALARY_DISTRIBUTION" />
          <el-option label="军品穿透" value="MILITARY_PENETRATION" />
          <el-option label="采购与供应链" value="PROCUREMENT_SUPPLY_CHAIN" />
          <el-option label="境外穿透" value="OVERSEAS_PENETRATION" />
          <el-option label="合同穿透" value="CONTRACT_PENETRATION" />
          <el-option label="行业穿透" value="INDUSTRY_PENETRATION" />
          <el-option label="资金穿透" value="FUND_PENETRATION" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="执行模式" prop="executionMode">
        <el-radio-group v-model="formData.executionMode">
          <el-radio label="SEQUENCE">顺序执行</el-radio>
          <el-radio label="PARALLEL">并行执行</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio label="DRAFT">草稿</el-radio>
          <el-radio label="ACTIVE">启用</el-radio>
          <el-radio label="INACTIVE">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="描述">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入组合描述"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" :loading="saving">
        {{ isEdit ? '更新' : '创建' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'CombinationEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    combination: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      saving: false,
      formData: {
        combinationId: '',
        combinationName: '',
        combinationCode: '',
        category: '',
        executionMode: 'SEQUENCE',
        status: 'DRAFT',
        description: ''
      },
      rules: {
        combinationName: [
          { required: true, message: '请输入组合名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        combinationCode: [
          { required: true, message: '请输入组合编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_-]+$/, message: '编码只能包含大写字母、数字、下划线和横线', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择领域分类', trigger: 'change' }
        ],
        executionMode: [
          { required: true, message: '请选择执行模式', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    isEdit() {
      return this.combination && this.combination.combinationId
    }
  },
  watch: {
    visible(newVal) {
      this.dialogVisible = newVal
      if (newVal) {
        this.initFormData()
        // 确保对话框在可视区域内显示
        this.$nextTick(() => {
          this.ensureDialogVisible()
        })
      }
    },
    
    dialogVisible(newVal) {
      this.$emit('update:visible', newVal)
    },
    
    combination: {
      handler() {
        if (this.visible) {
          this.initFormData()
        }
      },
      deep: true
    }
  },
  methods: {
    // 初始化表单数据
    initFormData() {
      if (this.combination) {
        this.formData = {
          combinationId: this.combination.combinationId || '',
          combinationName: this.combination.combinationName || '',
          combinationCode: this.combination.combinationCode || '',
          category: this.combination.category || '',
          executionMode: this.combination.executionMode || 'SEQUENCE',
          status: this.combination.status || 'DRAFT',
          description: this.combination.description || ''
        }
      } else {
        this.formData = {
          combinationId: '',
          combinationName: '',
          combinationCode: '',
          category: '',
          executionMode: 'SEQUENCE',
          status: 'DRAFT',
          description: ''
        }
      }
      
      // 清除验证
      this.$nextTick(() => {
        this.$refs.form?.clearValidate()
      })
    },
    
    // 保存
    async handleSave() {
      try {
        await this.$refs.form.validate()
        
        this.saving = true
        
        // 生成ID（新建时）
        if (!this.isEdit) {
          this.formData.combinationId = 'COMB_' + Date.now()
        }
        
        // 发送到父组件
        this.$emit('save', { ...this.formData })
        
      } catch (error) {
        // 表单验证失败
      } finally {
        this.saving = false
      }
    },

    // 确保对话框在可视区域内显示
    ensureDialogVisible() {
      try {
        // 查找新建指标组合对话框
        const dialogElement = document.querySelector('.combination-edit-dialog .el-dialog')
        if (dialogElement) {
          // 获取对话框的位置信息
          const rect = dialogElement.getBoundingClientRect()
          const viewportHeight = window.innerHeight
          const viewportWidth = window.innerWidth

          // 检查对话框是否在可视区域内
          const isVisible = rect.top >= 0 &&
                           rect.left >= 0 &&
                           rect.bottom <= viewportHeight &&
                           rect.right <= viewportWidth

          if (!isVisible) {
            // 如果对话框不在可视区域内，滚动到对话框位置
            dialogElement.scrollIntoView({
              behavior: 'smooth',
              block: 'center',
              inline: 'center'
            })

            console.log('✅ 新建指标组合对话框已滚动到可视区域')
          }

          // 确保对话框在正确的层级
          const dialogWrapper = document.querySelector('.combination-edit-dialog')
          if (dialogWrapper) {
            dialogWrapper.style.zIndex = '2800'
            dialogElement.style.zIndex = '2801'
          }
        }
      } catch (error) {
        console.error('❌ 确保对话框可见失败:', error)
      }
    },

    // 关闭
    handleClose() {
      this.dialogVisible = false
      this.saving = false
    }
  }
}
</script>

<style lang="scss" scoped>
.el-form {
  .el-form-item {
    margin-bottom: 20px;
  }

  .el-input,
  .el-select {
    width: 100%;
  }

  .el-radio-group {
    .el-radio {
      margin-right: 20px;
    }
  }
}
</style>

<style lang="scss">
/* 确保新建指标组合对话框正确显示 */
.combination-edit-dialog {
  z-index: 2800 !important;

  .el-dialog__wrapper {
    z-index: 2800 !important;
  }

  .el-dialog {
    z-index: 2801 !important;
    position: relative !important;
    margin-top: 5vh !important; /* 确保对话框不会太靠上 */
  }

  /* 确保遮罩层在正确层级 */
  + .v-modal {
    z-index: 2799 !important;
  }
}

/* 修复可能的定位问题 */
.risk-mxgl-sjmxgl-page .combination-edit-dialog .el-dialog {
  max-height: 90vh !important;
  overflow-y: auto !important;
}
</style>
