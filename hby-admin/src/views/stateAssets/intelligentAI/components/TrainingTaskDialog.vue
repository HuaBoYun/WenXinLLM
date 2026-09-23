<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="trainingForm"
      :model="trainingForm"
      :rules="trainingRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskName">
            <el-input
              v-model="trainingForm.taskName"
              placeholder="请输入任务名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联模型" prop="modelId">
            <el-select
              v-model="trainingForm.modelId"
              placeholder="请选择关联模型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option
                v-for="model in modelList"
                :key="model.id"
                :label="model.modelName"
                :value="model.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="训练类型" prop="trainingType">
            <el-select
              v-model="trainingForm.trainingType"
              placeholder="请选择训练类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="初始训练" value="INITIAL"></el-option>
              <el-option label="增量训练" value="INCREMENTAL"></el-option>
              <el-option label="重新训练" value="RETRAIN"></el-option>
              <el-option label="微调训练" value="FINE_TUNE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务状态" prop="status">
            <el-select
              v-model="trainingForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="待开始" value="PENDING"></el-option>
              <el-option label="训练中" value="TRAINING"></el-option>
              <el-option label="已完成" value="COMPLETED"></el-option>
              <el-option label="已失败" value="FAILED"></el-option>
              <el-option label="已取消" value="CANCELLED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据集大小" prop="datasetSize">
            <el-input-number
              v-model="trainingForm.datasetSize"
              :min="1"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
            <span style="margin-left: 10px; color: #909399;">条</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="训练轮数" prop="epochs">
            <el-input-number
              v-model="trainingForm.epochs"
              :min="1"
              :max="1000"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="学习率" prop="learningRate">
            <el-input-number
              v-model="trainingForm.learningRate"
              :min="0.0001"
              :max="1"
              :step="0.0001"
              :precision="4"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="批次大小" prop="batchSize">
            <el-input-number
              v-model="trainingForm.batchSize"
              :min="1"
              :max="1024"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker
              v-model="trainingForm.startTime"
              type="datetime"
              placeholder="选择开始时间"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计完成时间" prop="estimatedEndTime">
            <el-date-picker
              v-model="trainingForm.estimatedEndTime"
              type="datetime"
              placeholder="选择预计完成时间"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="trainingForm.status === 'TRAINING' || trainingForm.status === 'COMPLETED'">
        <el-col :span="12">
          <el-form-item label="当前进度" prop="progress">
            <el-progress :percentage="trainingForm.progress" :status="getProgressStatus()"></el-progress>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="当前损失" prop="currentLoss">
            <el-input-number
              v-model="trainingForm.currentLoss"
              :precision="6"
              style="width: 100%"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="trainingForm.status === 'COMPLETED'">
        <el-col :span="12">
          <el-form-item label="最终准确率" prop="finalAccuracy">
            <el-input-number
              v-model="trainingForm.finalAccuracy"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
            <span style="margin-left: 10px; color: #909399;">%</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="训练时长" prop="trainingDuration">
            <el-input
              v-model="trainingForm.trainingDuration"
              placeholder="如：2小时30分钟"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="训练参数" prop="trainingParams">
        <el-input
          v-model="trainingForm.trainingParams"
          type="textarea"
          :rows="3"
          placeholder="请输入训练参数配置（JSON格式）"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="数据集路径" prop="datasetPath">
        <el-input
          v-model="trainingForm.datasetPath"
          placeholder="请输入数据集文件路径"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="模型保存路径" prop="modelSavePath">
        <el-input
          v-model="trainingForm.modelSavePath"
          placeholder="请输入模型保存路径"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="训练日志" prop="trainingLog" v-if="trainingForm.status !== 'PENDING'">
        <el-input
          v-model="trainingForm.trainingLog"
          type="textarea"
          :rows="4"
          placeholder="训练日志信息"
          :disabled="true"
        />
      </el-form-item>

      <el-form-item label="任务描述" prop="description">
        <el-input
          v-model="trainingForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入任务描述"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="trainingForm.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view' && trainingForm.status === 'PENDING'"
        type="success"
        @click="handleStartTraining"
        :loading="loading"
      >
        开始训练
      </el-button>
      <el-button
        v-if="dialogType !== 'view' && trainingForm.status === 'TRAINING'"
        type="warning"
        @click="handleStopTraining"
        :loading="loading"
      >
        停止训练
      </el-button>
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
import { saveTrainingTask, startTraining, stopTraining } from '@/api/stateAssets/intelligentAI'

export default {
  name: 'TrainingTaskDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    trainingData: {
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
      modelList: [], // 模型列表
      trainingForm: {
        id: '',
        taskName: '',
        modelId: '',
        trainingType: '',
        status: 'PENDING',
        datasetSize: 1000,
        epochs: 100,
        learningRate: 0.001,
        batchSize: 32,
        startTime: '',
        estimatedEndTime: '',
        progress: 0,
        currentLoss: 0,
        finalAccuracy: 0,
        trainingDuration: '',
        trainingParams: '',
        datasetPath: '',
        modelSavePath: '',
        trainingLog: '',
        description: '',
        remarks: ''
      },
      trainingRules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        modelId: [
          { required: true, message: '请选择关联模型', trigger: 'change' }
        ],
        trainingType: [
          { required: true, message: '请选择训练类型', trigger: 'change' }
        ],
        datasetSize: [
          { required: true, message: '请输入数据集大小', trigger: 'blur' }
        ],
        epochs: [
          { required: true, message: '请输入训练轮数', trigger: 'blur' }
        ],
        learningRate: [
          { required: true, message: '请输入学习率', trigger: 'blur' }
        ],
        batchSize: [
          { required: true, message: '请输入批次大小', trigger: 'blur' }
        ],
        datasetPath: [
          { required: true, message: '请输入数据集路径', trigger: 'blur' }
        ],
        modelSavePath: [
          { required: true, message: '请输入模型保存路径', trigger: 'blur' }
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
        add: '新建训练任务',
        edit: '编辑训练任务',
        view: '查看训练任务'
      }
      return titleMap[this.dialogType] || '新建训练任务'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
        this.loadModelList()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.trainingForm = {
          id: '',
          taskName: '',
          modelId: '',
          trainingType: '',
          status: 'PENDING',
          datasetSize: 1000,
          epochs: 100,
          learningRate: 0.001,
          batchSize: 32,
          startTime: '',
          estimatedEndTime: '',
          progress: 0,
          currentLoss: 0,
          finalAccuracy: 0,
          trainingDuration: '',
          trainingParams: '',
          datasetPath: '',
          modelSavePath: '',
          trainingLog: '',
          description: '',
          remarks: ''
        }
      } else {
        this.trainingForm = { ...this.trainingData }
      }
      
      this.$nextTick(() => {
        if (this.$refs.trainingForm) {
          this.$refs.trainingForm.clearValidate()
        }
      })
    },

    loadModelList() {
      // 模拟加载模型列表
      this.modelList = [
        { id: '1', modelName: '风险预测模型v1.0' },
        { id: '2', modelName: '财务分析模型v2.1' },
        { id: '3', modelName: '股权穿透模型v1.5' },
        { id: '4', modelName: '智能推荐模型v3.0' }
      ]
    },

    getProgressStatus() {
      if (this.trainingForm.status === 'COMPLETED') {
        return 'success'
      } else if (this.trainingForm.status === 'FAILED') {
        return 'exception'
      } else {
        return null
      }
    },

    handleStartTraining() {
      this.$confirm('确定要开始训练任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        startTraining({ taskId: this.trainingForm.id }).then(response => {
          if (response.code === 1) {
            this.$message.success('训练任务已开始')
            this.trainingForm.status = 'TRAINING'
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '开始训练失败')
          }
          this.loading = false
        }).catch(error => {
          console.error('开始训练失败:', error)
          this.$message.error('开始训练失败')
          this.loading = false
        })
      })
    },

    handleStopTraining() {
      this.$confirm('确定要停止训练任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        stopTraining({ taskId: this.trainingForm.id }).then(response => {
          if (response.code === 1) {
            this.$message.success('训练任务已停止')
            this.trainingForm.status = 'CANCELLED'
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '停止训练失败')
          }
          this.loading = false
        }).catch(error => {
          console.error('停止训练失败:', error)
          this.$message.error('停止训练失败')
          this.loading = false
        })
      })
    },

    handleSubmit() {
      this.$refs.trainingForm.validate((valid) => {
        if (valid) {
          this.loading = true
          saveTrainingTask(this.trainingForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存训练任务失败:', error)
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
