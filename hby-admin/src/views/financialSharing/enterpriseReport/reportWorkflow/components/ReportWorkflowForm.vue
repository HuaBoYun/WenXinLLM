<template>
  <el-dialog
    :title="formData.workflowId ? '修改报表工作流' : '新增报表工作流'"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="流程编码" prop="workflowCode">
            <el-input v-model="formData.workflowCode" placeholder="请输入流程编码" maxlength="50" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="流程名称" prop="workflowName">
            <el-input v-model="formData.workflowName" placeholder="请输入流程名称" maxlength="200" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属任务" prop="taskId">
            <el-select
              v-model="formData.taskId"
              placeholder="请选择所属任务"
              style="width: 100%"
            >
              <el-option
                v-for="item in taskOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="流程类型" prop="workflowType">
            <el-select v-model="formData.workflowType" placeholder="请选择流程类型" style="width: 100%">
              <el-option label="编制" value="PREPARE" />
              <el-option label="审批" value="APPROVE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="分支条件">
            <el-input
              v-model="formData.branchCondition"
              type="textarea"
              :rows="3"
              placeholder="请输入分支条件(JSON格式)"
            />
            <span class="form-tip">用于条件分支判断,如:{"field":"amount","operator":">","value":10000}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="参与人">
            <el-input
              v-model="formData.participants"
              type="textarea"
              :rows="2"
              placeholder="请输入参与人(JSON数组)"
            />
            <span class="form-tip">参与人列表,如:["user1","user2"]或[{"type":"role","value":"ADMIN"}]</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="抄送人">
            <el-input
              v-model="formData.ccUsers"
              type="textarea"
              :rows="2"
              placeholder="请输入抄送人(JSON数组)"
            />
            <span class="form-tip">抄送人列表,如:["user1","user2"]</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="消息提醒方式">
            <el-select v-model="formData.messageType" placeholder="请选择消息提醒方式" style="width: 100%">
              <el-option label="即时消息" value="IM" />
              <el-option label="邮件" value="EMAIL" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序号">
            <el-input-number
              v-model="formData.sortNo"
              :min="0"
              :max="9999"
              controls-position="right"
              style="width: 100%"
              placeholder="排序号"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="消息内容模板">
            <el-input
              v-model="formData.messageContent"
              type="textarea"
              :rows="3"
              placeholder="请输入消息内容模板"
            />
            <span class="form-tip">支持变量替换,如:您有一条新的${taskName}待处理</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio label="ACTIVE">启用</el-radio>
              <el-radio label="INACTIVE">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveReportWorkflow } from '@/api/financialSharing/enterpriseReport/reportWorkflow'

export default {
  name: 'ReportWorkflowForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    taskOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      rules: {
        workflowCode: [
          { required: true, message: '流程编码不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '流程编码长度在1到50个字符', trigger: 'blur' }
        ],
        workflowName: [
          { required: true, message: '流程名称不能为空', trigger: 'blur' },
          { min: 1, max: 200, message: '流程名称长度在1到200个字符', trigger: 'blur' }
        ],
        taskId: [
          { required: true, message: '所属任务不能为空', trigger: 'change' }
        ],
        workflowType: [
          { required: true, message: '流程类型不能为空', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 验证JSON格式
          if (this.formData.branchCondition && this.formData.branchCondition.trim()) {
            try {
              JSON.parse(this.formData.branchCondition)
            } catch (e) {
              this.$message.error('分支条件格式不正确,请输入有效的JSON格式')
              return
            }
          }
          if (this.formData.participants && this.formData.participants.trim()) {
            try {
              JSON.parse(this.formData.participants)
            } catch (e) {
              this.$message.error('参与人格式不正确,请输入有效的JSON数组')
              return
            }
          }
          if (this.formData.ccUsers && this.formData.ccUsers.trim()) {
            try {
              JSON.parse(this.formData.ccUsers)
            } catch (e) {
              this.$message.error('抄送人格式不正确,请输入有效的JSON数组')
              return
            }
          }

          this.submitLoading = true
          saveReportWorkflow(this.formData).then(response => {
            this.submitLoading = false
            if (response.code === 200) {
              this.$message.success(response.msg || '保存成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          }).catch(() => {
            this.submitLoading = false
          })
        }
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.$refs.form.resetFields()
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.form-tip {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
  display: block;
  margin-top: 5px;
}
</style>


