<template>
  <el-dialog
    title="问题修复"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="问题描述" prop="issueDescription">
        <el-input
          v-model="form.issueDescription"
          type="textarea"
          :rows="3"
          placeholder="请描述发现的问题"
          readonly
        ></el-input>
      </el-form-item>
      
      <el-form-item label="问题类型" prop="issueType">
        <el-tag :type="getIssueTypeTag(form.issueType)">
          {{ getIssueTypeText(form.issueType) }}
        </el-tag>
      </el-form-item>
      
      <el-form-item label="问题级别" prop="issueLevel">
        <el-tag :type="getIssueLevelTag(form.issueLevel)">
          {{ getIssueLevelText(form.issueLevel) }}
        </el-tag>
      </el-form-item>
      
      <el-form-item label="修复方案" prop="fixSolution">
        <el-radio-group v-model="form.fixSolution">
          <el-radio label="auto">自动修复</el-radio>
          <el-radio label="manual">手动修复</el-radio>
          <el-radio label="ignore">忽略问题</el-radio>
          <el-radio label="escalate">上报处理</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="修复内容" prop="fixContent" v-if="form.fixSolution === 'manual'">
        <el-input
          v-model="form.fixContent"
          type="textarea"
          :rows="4"
          placeholder="请输入具体的修复内容"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="自动修复规则" v-if="form.fixSolution === 'auto'">
        <el-select v-model="form.autoFixRule" placeholder="请选择自动修复规则">
          <el-option label="数据格式标准化" value="format"></el-option>
          <el-option label="空值填充默认值" value="fillNull"></el-option>
          <el-option label="重复数据去重" value="deduplicate"></el-option>
          <el-option label="异常值修正" value="outlier"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="忽略原因" prop="ignoreReason" v-if="form.fixSolution === 'ignore'">
        <el-input
          v-model="form.ignoreReason"
          type="textarea"
          :rows="3"
          placeholder="请说明忽略此问题的原因"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="上报对象" prop="escalateTo" v-if="form.fixSolution === 'escalate'">
        <el-select v-model="form.escalateTo" placeholder="请选择上报对象">
          <el-option label="数据管理员" value="dataAdmin"></el-option>
          <el-option label="系统管理员" value="sysAdmin"></el-option>
          <el-option label="业务负责人" value="businessOwner"></el-option>
          <el-option label="技术负责人" value="techLead"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="修复说明" prop="fixNote">
        <el-input
          v-model="form.fixNote"
          type="textarea"
          :rows="3"
          placeholder="请输入修复说明或备注"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="通知相关人员">
        <el-checkbox-group v-model="form.notifyUsers">
          <el-checkbox label="dataOwner">数据负责人</el-checkbox>
          <el-checkbox label="auditor">审核人员</el-checkbox>
          <el-checkbox label="manager">部门经理</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    
    <el-divider content-position="left">问题详情</el-divider>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="发现时间">
        {{ issueData.discoveryTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="发现人">
        {{ issueData.discoverer || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="影响范围">
        {{ issueData.impactScope || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="数据来源">
        {{ issueData.dataSource || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="相关字段">
        {{ issueData.relatedFields || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="错误数量">
        {{ issueData.errorCount || 0 }} 条
      </el-descriptions-item>
    </el-descriptions>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ getConfirmButtonText() }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'IssueFixDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    issueData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        issueDescription: '',
        issueType: '',
        issueLevel: '',
        fixSolution: '',
        fixContent: '',
        autoFixRule: '',
        ignoreReason: '',
        escalateTo: '',
        fixNote: '',
        notifyUsers: []
      },
      rules: {
        fixSolution: [
          { required: true, message: '请选择修复方案', trigger: 'change' }
        ],
        fixContent: [
          { required: true, message: '请输入修复内容', trigger: 'blur' }
        ],
        ignoreReason: [
          { required: true, message: '请说明忽略原因', trigger: 'blur' }
        ],
        escalateTo: [
          { required: true, message: '请选择上报对象', trigger: 'change' }
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
        issueDescription: this.issueData.issueDescription || '',
        issueType: this.issueData.issueType || '',
        issueLevel: this.issueData.issueLevel || '',
        fixSolution: '',
        fixContent: '',
        autoFixRule: '',
        ignoreReason: '',
        escalateTo: '',
        fixNote: '',
        notifyUsers: []
      }
    },
    getIssueTypeTag(type) {
      const typeMap = {
        'dataFormat': 'warning',
        'dataAccuracy': 'danger',
        'dataIntegrity': 'info',
        'dataConsistency': 'primary'
      }
      return typeMap[type] || 'info'
    },
    getIssueTypeText(type) {
      const textMap = {
        'dataFormat': '数据格式问题',
        'dataAccuracy': '数据准确性问题',
        'dataIntegrity': '数据完整性问题',
        'dataConsistency': '数据一致性问题'
      }
      return textMap[type] || type
    },
    getIssueLevelTag(level) {
      const levelMap = {
        'low': 'info',
        'medium': 'warning',
        'high': 'danger',
        'critical': 'danger'
      }
      return levelMap[level] || 'info'
    },
    getIssueLevelText(level) {
      const textMap = {
        'low': '低',
        'medium': '中',
        'high': '高',
        'critical': '严重'
      }
      return textMap[level] || level
    },
    getConfirmButtonText() {
      const textMap = {
        'auto': '执行自动修复',
        'manual': '提交修复方案',
        'ignore': '忽略问题',
        'escalate': '上报问题'
      }
      return textMap[this.form.fixSolution] || '确定'
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.loading = true

          try {
            await request({
              url: '/monitor/v1/enterprise/data/quality/issue/fix',
              method: 'post',
              headers: { 'Content-Type': 'application/json;charset=UTF-8' },
              data: {
                issueId: this.issueData.id,
                ...this.form
              }
            })
            let message = ''
            switch (this.form.fixSolution) {
              case 'auto':
                message = '自动修复已执行完成'
                break
              case 'manual':
                message = '修复方案已提交'
                break
              case 'ignore':
                message = '问题已标记为忽略'
                break
              case 'escalate':
                message = '问题已上报处理'
                break
            }
            this.$message.success(message)
            this.handleClose()
            this.$emit('refresh')
          } catch (error) {
            this.$message.error(error.message || '修复操作失败')
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>
