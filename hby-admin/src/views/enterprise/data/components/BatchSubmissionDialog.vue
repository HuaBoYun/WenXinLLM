<template>
  <el-dialog
    title="批量提交"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-alert
      title="批量提交提示"
      type="info"
      :closable="false"
      style="margin-bottom: 20px;"
    >
      <template slot="description">
        已选择 <strong>{{ selectedItems.length }}</strong> 项数据进行批量提交操作
      </template>
    </el-alert>
    
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="提交类型" prop="submissionType">
        <el-radio-group v-model="form.submissionType">
          <el-radio label="normal">正常提交</el-radio>
          <el-radio label="urgent">紧急提交</el-radio>
          <el-radio label="draft">保存草稿</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="提交说明" prop="submissionNote">
        <el-input
          v-model="form.submissionNote"
          type="textarea"
          :rows="4"
          placeholder="请输入批量提交说明"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="审核人" prop="reviewer" v-if="form.submissionType !== 'draft'">
        <el-select v-model="form.reviewer" placeholder="请选择审核人">
          <el-option label="张三" value="zhangsan"></el-option>
          <el-option label="李四" value="lisi"></el-option>
          <el-option label="王五" value="wangwu"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="通知方式">
        <el-checkbox-group v-model="form.notifyMethods">
          <el-checkbox label="email">邮件通知</el-checkbox>
          <el-checkbox label="sms">短信通知</el-checkbox>
          <el-checkbox label="system">系统消息</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    
    <el-divider content-position="left">待提交数据列表</el-divider>
    <el-table :data="selectedItems" border max-height="300">
      <el-table-column label="序号" width="60">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="dataId" label="数据ID" width="120"></el-table-column>
      <el-table-column prop="dataName" label="数据名称"></el-table-column>
      <el-table-column prop="dataType" label="数据类型" width="100"></el-table-column>
      <el-table-column prop="reportPeriod" label="报告期" width="120"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag size="small" :type="getStatusType(scope.row.status)">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        确认批量提交
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BatchSubmissionDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    selectedItems: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      loading: false,
      form: {
        submissionType: 'normal',
        submissionNote: '',
        reviewer: '',
        notifyMethods: ['system']
      },
      rules: {
        submissionType: [
          { required: true, message: '请选择提交类型', trigger: 'change' }
        ],
        submissionNote: [
          { required: true, message: '请输入提交说明', trigger: 'blur' }
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
        submissionType: 'normal',
        submissionNote: '',
        reviewer: '',
        notifyMethods: ['system']
      }
    },
    getStatusType(status) {
      const statusMap = {
        '待提交': 'warning',
        '草稿': 'info',
        '已提交': 'success',
        '已驳回': 'danger'
      }
      return statusMap[status] || 'info'
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    handleConfirm() {
      if (this.selectedItems.length === 0) {
        this.$message.warning('请选择要提交的数据')
        return
      }
      
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$confirm(
            `确认对 ${this.selectedItems.length} 项数据执行${this.getActionText()}操作？`,
            '批量提交确认',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }
          ).then(() => {
            this.loading = true
            setTimeout(() => {
              this.loading = false
              this.$message.success(`批量${this.getActionText()}操作成功`)
              this.handleClose()
              this.$emit('refresh')
            }, 2000)
          })
        }
      })
    },
    getActionText() {
      const actionMap = {
        'normal': '提交',
        'urgent': '紧急提交',
        'draft': '保存草稿'
      }
      return actionMap[this.form.submissionType] || '提交'
    }
  }
}
</script>
