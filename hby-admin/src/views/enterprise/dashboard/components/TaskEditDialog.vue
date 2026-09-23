<template>
  <el-dialog
    title="编辑任务"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input v-model="form.taskName" placeholder="请输入任务名称"></el-input>
      </el-form-item>
      
      <el-form-item label="任务类型" prop="taskType">
        <el-select v-model="form.taskType" placeholder="请选择任务类型">
          <el-option label="数据收集" value="collect"></el-option>
          <el-option label="数据审核" value="audit"></el-option>
          <el-option label="数据上报" value="report"></el-option>
          <el-option label="数据分析" value="analysis"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="优先级" prop="priority">
        <el-radio-group v-model="form.priority">
          <el-radio label="high">高</el-radio>
          <el-radio label="medium">中</el-radio>
          <el-radio label="low">低</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="截止时间" prop="deadline">
        <el-date-picker
          v-model="form.deadline"
          type="datetime"
          placeholder="选择截止时间"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>
      
      <el-form-item label="执行人" prop="assignee">
        <el-select v-model="form.assignee" placeholder="请选择执行人">
          <el-option label="张三" value="zhangsan"></el-option>
          <el-option label="李四" value="lisi"></el-option>
          <el-option label="王五" value="wangwu"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择任务状态">
          <el-option label="待开始" value="待开始"></el-option>
          <el-option label="进行中" value="进行中"></el-option>
          <el-option label="已暂停" value="已暂停"></el-option>
          <el-option label="已完成" value="已完成"></el-option>
          <el-option label="已取消" value="已取消"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="完成进度" prop="progress">
        <el-slider
          v-model="form.progress"
          :max="100"
          show-input
          :format-tooltip="formatTooltip"
        ></el-slider>
      </el-form-item>
      
      <el-form-item label="任务描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="4"
          placeholder="请输入任务描述"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="备注">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        ></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'TaskEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    taskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        taskName: '',
        taskType: '',
        priority: 'medium',
        deadline: '',
        assignee: '',
        status: '待开始',
        progress: 0,
        description: '',
        remark: ''
      },
      rules: {
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        taskType: [
          { required: true, message: '请选择任务类型', trigger: 'change' }
        ],
        deadline: [
          { required: true, message: '请选择截止时间', trigger: 'change' }
        ],
        assignee: [
          { required: true, message: '请选择执行人', trigger: 'change' }
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
      if (this.taskData && this.taskData.taskId) {
        // 编辑模式
        this.form = {
          taskName: this.taskData.taskName || '',
          taskType: this.taskData.taskType || '',
          priority: this.taskData.priority || 'medium',
          deadline: this.taskData.deadline || '',
          assignee: this.taskData.assignee || '',
          status: this.taskData.status || '待开始',
          progress: this.taskData.progress || 0,
          description: this.taskData.description || '',
          remark: this.taskData.remark || ''
        }
      } else {
        // 新增模式
        this.form = {
          taskName: '',
          taskType: '',
          priority: 'medium',
          deadline: '',
          assignee: '',
          status: '待开始',
          progress: 0,
          description: '',
          remark: ''
        }
      }
    },
    formatTooltip(val) {
      return `${val}%`
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          setTimeout(() => {
            this.loading = false
            this.$message.success('任务保存成功')
            this.handleClose()
            this.$emit('refresh')
          }, 1000)
        }
      })
    }
  }
}
</script>
