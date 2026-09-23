<template>
  <el-dialog
    title="完成任务"
    :visible.sync="dialogVisible"
    width="600px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="任务名称">
        <el-input v-model="taskData.taskName" readonly></el-input>
      </el-form-item>
      
      <el-form-item label="完成情况" prop="completionStatus">
        <el-radio-group v-model="form.completionStatus">
          <el-radio label="completed">已完成</el-radio>
          <el-radio label="partial">部分完成</el-radio>
          <el-radio label="failed">未完成</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="完成进度" prop="completionProgress">
        <el-slider
          v-model="form.completionProgress"
          :max="100"
          show-input
          :format-tooltip="formatTooltip"
        ></el-slider>
      </el-form-item>
      
      <el-form-item label="完成说明" prop="completionNote">
        <el-input
          v-model="form.completionNote"
          type="textarea"
          :rows="4"
          placeholder="请输入完成说明"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="遇到问题" v-if="form.completionStatus !== 'completed'">
        <el-input
          v-model="form.issues"
          type="textarea"
          :rows="3"
          placeholder="请描述遇到的问题"
        ></el-input>
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
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
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
  name: 'TaskCompleteDialog',
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
        completionStatus: 'completed',
        completionProgress: 100,
        completionNote: '',
        issues: ''
      },
      rules: {
        completionStatus: [
          { required: true, message: '请选择完成情况', trigger: 'change' }
        ],
        completionNote: [
          { required: true, message: '请输入完成说明', trigger: 'blur' }
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
    },
    'form.completionStatus'(val) {
      if (val === 'completed') {
        this.form.completionProgress = 100
      } else if (val === 'failed') {
        this.form.completionProgress = 0
      }
    }
  },
  methods: {
    initForm() {
      this.form = {
        completionStatus: 'completed',
        completionProgress: 100,
        completionNote: '',
        issues: ''
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
            this.$message.success('任务完成状态更新成功')
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
