<template>
  <el-dialog
    title="任务反馈"
    :visible.sync="dialogVisible"
    width="600px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="任务名称">
        <el-input v-model="taskData.taskName" readonly></el-input>
      </el-form-item>
      
      <el-form-item label="反馈类型" prop="feedbackType">
        <el-radio-group v-model="form.feedbackType">
          <el-radio label="progress">进度反馈</el-radio>
          <el-radio label="issue">问题反馈</el-radio>
          <el-radio label="suggestion">建议反馈</el-radio>
          <el-radio label="completion">完成反馈</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="当前进度" prop="currentProgress" v-if="form.feedbackType === 'progress'">
        <el-slider
          v-model="form.currentProgress"
          :max="100"
          show-input
          :format-tooltip="formatTooltip"
        ></el-slider>
      </el-form-item>
      
      <el-form-item label="问题级别" prop="issueLevel" v-if="form.feedbackType === 'issue'">
        <el-select v-model="form.issueLevel" placeholder="请选择问题级别">
          <el-option label="轻微" value="low"></el-option>
          <el-option label="一般" value="medium"></el-option>
          <el-option label="严重" value="high"></el-option>
          <el-option label="紧急" value="urgent"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="反馈内容" prop="feedbackContent">
        <el-input
          v-model="form.feedbackContent"
          type="textarea"
          :rows="5"
          placeholder="请输入详细的反馈内容"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="解决方案" v-if="form.feedbackType === 'issue'">
        <el-input
          v-model="form.solution"
          type="textarea"
          :rows="3"
          placeholder="请输入解决方案或建议"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="需要支持">
        <el-checkbox-group v-model="form.supportNeeded">
          <el-checkbox label="technical">技术支持</el-checkbox>
          <el-checkbox label="resource">资源支持</el-checkbox>
          <el-checkbox label="time">时间延期</el-checkbox>
          <el-checkbox label="guidance">指导帮助</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="附件上传">
        <el-upload
          class="upload-demo"
          :action="uploadUrl"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          multiple
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">支持上传截图、文档等相关文件</div>
        </el-upload>
      </el-form-item>
      
      <el-form-item label="通知相关人员">
        <el-checkbox-group v-model="form.notifyUsers">
          <el-checkbox label="creator">任务创建人</el-checkbox>
          <el-checkbox label="manager">项目经理</el-checkbox>
          <el-checkbox label="team">团队成员</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">提交反馈</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'TaskFeedbackDialog',
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
      uploadUrl: '/api/upload',
      fileList: [],
      form: {
        feedbackType: 'progress',
        currentProgress: 0,
        issueLevel: '',
        feedbackContent: '',
        solution: '',
        supportNeeded: [],
        notifyUsers: ['creator']
      },
      rules: {
        feedbackType: [
          { required: true, message: '请选择反馈类型', trigger: 'change' }
        ],
        feedbackContent: [
          { required: true, message: '请输入反馈内容', trigger: 'blur' }
        ],
        issueLevel: [
          { required: true, message: '请选择问题级别', trigger: 'change' }
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
      this.form = {
        feedbackType: 'progress',
        currentProgress: this.taskData.progress || 0,
        issueLevel: '',
        feedbackContent: '',
        solution: '',
        supportNeeded: [],
        notifyUsers: ['creator']
      }
      this.fileList = []
    },
    formatTooltip(val) {
      return `${val}%`
    },
    handlePreview(file) {
      console.log(file)
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
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
            this.$message.success('反馈提交成功')
            this.handleClose()
            this.$emit('refresh')
          }, 1000)
        }
      })
    }
  }
}
</script>

<style scoped>
.upload-demo {
  margin-top: 10px;
}
</style>
