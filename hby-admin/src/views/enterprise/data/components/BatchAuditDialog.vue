<template>
  <el-dialog
    title="批量审核"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-alert
      title="批量审核提示"
      type="info"
      :closable="false"
      style="margin-bottom: 20px;"
    >
      <template slot="description">
        已选择 <strong>{{ selectedItems.length }}</strong> 项数据进行批量审核操作
      </template>
    </el-alert>
    
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="审核操作" prop="auditAction">
        <el-radio-group v-model="form.auditAction">
          <el-radio label="pass">批量通过</el-radio>
          <el-radio label="reject">批量驳回</el-radio>
          <el-radio label="return">批量退回</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="审核意见" prop="auditComment">
        <el-input
          v-model="form.auditComment"
          type="textarea"
          :rows="4"
          placeholder="请输入批量审核意见"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="下一审核人" prop="nextAuditor" v-if="form.auditAction === 'pass'">
        <el-select v-model="form.nextAuditor" placeholder="请选择下一审核人">
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
    
    <el-divider content-position="left">待审核数据列表</el-divider>
    <el-table :data="selectedItems" border max-height="300">
      <el-table-column label="序号" width="60">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="dataId" label="数据ID" width="120"></el-table-column>
      <el-table-column prop="dataName" label="数据名称"></el-table-column>
      <el-table-column prop="dataType" label="数据类型" width="100"></el-table-column>
      <el-table-column prop="submitTime" label="提交时间" width="150"></el-table-column>
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
        确认批量审核
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'BatchAuditDialog',
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
        auditAction: '',
        auditComment: '',
        nextAuditor: '',
        notifyMethods: ['system']
      },
      rules: {
        auditAction: [
          { required: true, message: '请选择审核操作', trigger: 'change' }
        ],
        auditComment: [
          { required: true, message: '请输入审核意见', trigger: 'blur' }
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
        auditAction: '',
        auditComment: '',
        nextAuditor: '',
        notifyMethods: ['system']
      }
    },
    getStatusType(status) {
      const statusMap = {
        '待审核': 'warning',
        '审核中': 'primary',
        '已通过': 'success',
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
        this.$message.warning('请选择要审核的数据')
        return
      }
      
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$confirm(
            `确认对 ${this.selectedItems.length} 项数据执行${this.getActionText()}操作？`,
            '批量审核确认',
            {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }
          ).then(() => {
            this.loading = true
            request({
              url: '/monitor/v1/enterprise/data/quality/batchAudit',
              method: 'post',
              headers: { 'Content-Type': 'application/json;charset=UTF-8' },
              data: {
                ids: this.selectedItems.map(item => item.id),
                auditAction: this.form.auditAction,
                auditComment: this.form.auditComment,
                nextAuditor: this.form.nextAuditor,
                notifyMethods: this.form.notifyMethods
              }
            }).then(() => {
              this.$message.success(`批量${this.getActionText()}操作成功`)
              this.handleClose()
              this.$emit('refresh')
            }).catch((error) => {
              this.$message.error(error.message || '批量审核操作失败')
            }).finally(() => {
              this.loading = false
            })
          })
        }
      })
    },
    getActionText() {
      const actionMap = {
        'pass': '通过',
        'reject': '驳回',
        'return': '退回'
      }
      return actionMap[this.form.auditAction] || '审核'
    }
  }
}
</script>
