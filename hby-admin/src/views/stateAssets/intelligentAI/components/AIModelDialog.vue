<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="modelForm"
      :model="modelForm"
      :rules="modelRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模型名称" prop="modelName">
            <el-input
              v-model="modelForm.modelName"
              placeholder="请输入模型名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型类型" prop="modelType">
            <el-select
              v-model="modelForm.modelType"
              placeholder="请选择模型类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="风险预测" value="RISK_PREDICTION"></el-option>
              <el-option label="财务分析" value="FINANCIAL_ANALYSIS"></el-option>
              <el-option label="异常检测" value="ANOMALY_DETECTION"></el-option>
              <el-option label="智能推荐" value="RECOMMENDATION"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模型版本" prop="version">
            <el-input
              v-model="modelForm.version"
              placeholder="请输入模型版本"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="准确率" prop="accuracy">
            <el-input-number
              v-model="modelForm.accuracy"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模型状态" prop="status">
            <el-select
              v-model="modelForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="运行中" value="RUNNING"></el-option>
              <el-option label="已停止" value="STOPPED"></el-option>
              <el-option label="训练中" value="TRAINING"></el-option>
              <el-option label="已部署" value="DEPLOYED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="训练数据量" prop="trainingDataSize">
            <el-input-number
              v-model="modelForm.trainingDataSize"
              :min="0"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="模型描述" prop="description">
        <el-input
          v-model="modelForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入模型描述"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="模型参数" prop="parameters">
        <el-input
          v-model="modelForm.parameters"
          type="textarea"
          :rows="4"
          placeholder="请输入模型参数配置"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="算法框架" prop="framework">
        <el-select
          v-model="modelForm.framework"
          placeholder="请选择算法框架"
          style="width: 100%"
          :disabled="dialogType === 'view'"
        >
          <el-option label="TensorFlow" value="TENSORFLOW"></el-option>
          <el-option label="PyTorch" value="PYTORCH"></el-option>
          <el-option label="Scikit-learn" value="SKLEARN"></el-option>
          <el-option label="XGBoost" value="XGBOOST"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="训练环境" prop="environment">
        <el-input
          v-model="modelForm.environment"
          placeholder="请输入训练环境信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="loading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveAIModel } from '@/api/stateAssets/intelligentAI'

export default {
  name: 'AIModelDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    modelData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    }
  },
  data() {
    return {
      loading: false,
      modelForm: {
        id: '',
        modelName: '',
        modelType: '',
        version: '',
        accuracy: 0,
        status: '',
        trainingDataSize: 0,
        description: '',
        parameters: '',
        framework: '',
        environment: ''
      },
      modelRules: {
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' }
        ],
        modelType: [
          { required: true, message: '请选择模型类型', trigger: 'change' }
        ],
        version: [
          { required: true, message: '请输入模型版本', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择模型状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      const titleMap = {
        add: '新增AI模型',
        edit: '编辑AI模型',
        view: '查看AI模型'
      }
      return titleMap[this.dialogType] || '新增AI模型'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.modelForm = {
          id: '',
          modelName: '',
          modelType: '',
          version: '',
          accuracy: 0,
          status: '',
          trainingDataSize: 0,
          description: '',
          parameters: '',
          framework: '',
          environment: ''
        }
      } else {
        this.modelForm = { ...this.modelData }
      }
      
      this.$nextTick(() => {
        if (this.$refs.modelForm) {
          this.$refs.modelForm.clearValidate()
        }
      })
    },

    handleSubmit() {
      this.$refs.modelForm.validate((valid) => {
        if (valid) {
          this.loading = true
          saveAIModel(this.modelForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存AI模型失败:', error)
            this.$message.error('操作失败')
            this.loading = false
          })
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.loading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
