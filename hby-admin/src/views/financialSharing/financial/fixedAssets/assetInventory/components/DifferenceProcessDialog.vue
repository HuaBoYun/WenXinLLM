<template>
  <el-dialog
    title="盘点差异处理"
    :visible.sync="visible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="difference-container">
      <el-alert
        title="差异信息"
        type="warning"
        :closable="false"
        show-icon
        style="margin-bottom: 20px"
      >
        <div slot="default">
          <p><strong>资产编码：</strong>{{ differenceData.assetCode }}</p>
          <p><strong>资产名称：</strong>{{ differenceData.assetName }}</p>
          <p><strong>账面数量：</strong>{{ differenceData.bookQuantity }}</p>
          <p><strong>实盘数量：</strong>{{ differenceData.actualQuantity }}</p>
          <p><strong>差异数量：</strong>
            <span :class="getDifferenceClass()">{{ getDifferenceValue() }}</span>
          </p>
          <p><strong>差异类型：</strong>
            <el-tag :type="getResultType()" size="small">{{ getResultText() }}</el-tag>
          </p>
        </div>
      </el-alert>

      <el-form
        ref="processForm"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        size="small"
      >
        <el-form-item label="处理方式" prop="processType">
          <el-select v-model="formData.processType" placeholder="请选择处理方式" style="width: 100%">
            <el-option label="账实调整" value="ADJUST" />
            <el-option label="资产报废" value="SCRAP" />
            <el-option label="资产补录" value="ADD" />
            <el-option label="重新盘点" value="RECOUNT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>

        <el-form-item label="处理说明" prop="processDescription">
          <el-input
            v-model="formData.processDescription"
            type="textarea"
            :rows="4"
            placeholder="请详细说明差异原因及处理方案"
          />
        </el-form-item>

        <el-form-item label="责任部门" prop="responsibleDepartment">
          <el-select v-model="formData.responsibleDepartment" placeholder="请选择责任部门" style="width: 100%">
            <el-option label="财务部" value="D001" />
            <el-option label="行政部" value="D002" />
            <el-option label="生产部" value="D003" />
            <el-option label="仓储部" value="D004" />
          </el-select>
        </el-form-item>

        <el-form-item label="责任人" prop="responsiblePerson">
          <el-input v-model="formData.responsiblePerson" placeholder="请输入责任人姓名" />
        </el-form-item>

        <el-form-item label="处理期限" prop="deadline">
          <el-date-picker
            v-model="formData.deadline"
            type="date"
            placeholder="选择处理期限"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="附件">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            multiple
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持上传图片、PDF等文件，单个文件不超过5MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">提交</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'DifferenceProcessDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    differenceData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitting: false,
      fileList: [],
      formData: {
        processType: '',
        processDescription: '',
        responsibleDepartment: '',
        responsiblePerson: '',
        deadline: ''
      },
      formRules: {
        processType: [{ required: true, message: '请选择处理方式', trigger: 'change' }],
        processDescription: [{ required: true, message: '请输入处理说明', trigger: 'blur' }],
        responsibleDepartment: [{ required: true, message: '请选择责任部门', trigger: 'change' }],
        deadline: [{ required: true, message: '请选择处理期限', trigger: 'change' }]
      }
    }
  },
  methods: {
    getDifferenceValue() {
      const diff = (this.differenceData.actualQuantity || 0) - (this.differenceData.bookQuantity || 0)
      return diff > 0 ? `+${diff}` : diff
    },
    getDifferenceClass() {
      const diff = (this.differenceData.actualQuantity || 0) - (this.differenceData.bookQuantity || 0)
      if (diff > 0) return 'difference-surplus'
      if (diff < 0) return 'difference-shortage'
      return 'difference-normal'
    },
    getResultText() {
      const diff = (this.differenceData.actualQuantity || 0) - (this.differenceData.bookQuantity || 0)
      if (diff > 0) return '盘盈'
      if (diff < 0) return '盘亏'
      return '正常'
    },
    getResultType() {
      const diff = (this.differenceData.actualQuantity || 0) - (this.differenceData.bookQuantity || 0)
      if (diff > 0) return 'success'
      if (diff < 0) return 'danger'
      return 'info'
    },
    handleFileChange(file, fileList) {
      this.fileList = fileList
    },
    handleSubmit() {
      this.$refs.processForm.validate(valid => {
        if (valid) {
          this.$emit('submit', {
            ...this.formData,
            differenceId: this.differenceData.id,
            files: this.fileList
          })
        }
      })
    },
    handleClose() {
      this.$refs.processForm.resetFields()
      this.fileList = []
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.difference-container {
  padding: 10px 0;

  .el-alert {
    p {
      margin: 5px 0;
      line-height: 1.6;
    }
  }
}

.difference-surplus {
  color: #67c23a;
  font-weight: 600;
}

.difference-shortage {
  color: #f56c6c;
  font-weight: 600;
}

.difference-normal {
  color: #909399;
}
</style>

