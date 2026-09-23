<template>
  <el-dialog
    title="数据质量检查"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <div class="quality-check-container">
      <!-- 检查概览 -->
      <el-card class="overview-card">
        <div slot="header">
          <span>质量检查概览</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="overview-item">
              <div class="overview-value">{{ checkData.totalRecords || 0 }}</div>
              <div class="overview-label">总记录数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="overview-value">{{ checkData.validRecords || 0 }}</div>
              <div class="overview-label">有效记录</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="overview-value">{{ checkData.invalidRecords || 0 }}</div>
              <div class="overview-label">无效记录</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="overview-item">
              <div class="overview-value">{{ qualityScore }}%</div>
              <div class="overview-label">质量评分</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 检查规则 -->
      <el-card class="rules-card">
        <div slot="header">
          <span>检查规则</span>
        </div>
        <el-table :data="checkRules" style="width: 100%">
          <el-table-column prop="ruleName" label="规则名称" width="200"></el-table-column>
          <el-table-column prop="ruleType" label="规则类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getRuleTypeTagType(scope.row.ruleType)">
                {{ getRuleTypeText(scope.row.ruleType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="checkResult" label="检查结果" width="120">
            <template slot-scope="scope">
              <el-tag :type="scope.row.checkResult === 'pass' ? 'success' : 'danger'">
                {{ scope.row.checkResult === 'pass' ? '通过' : '失败' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="errorCount" label="错误数量" width="100"></el-table-column>
          <el-table-column prop="description" label="规则描述"></el-table-column>
        </el-table>
      </el-card>
      
      <!-- 质量问题 -->
      <el-card class="issues-card">
        <div slot="header">
          <span>质量问题详情</span>
        </div>
        <el-table :data="qualityIssues" style="width: 100%">
          <el-table-column prop="issueType" label="问题类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getIssueTypeTagType(scope.row.issueType)">
                {{ getIssueTypeText(scope.row.issueType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="fieldName" label="字段名称" width="150"></el-table-column>
          <el-table-column prop="issueDescription" label="问题描述"></el-table-column>
          <el-table-column prop="affectedRecords" label="影响记录数" width="120"></el-table-column>
          <el-table-column prop="severity" label="严重程度" width="100">
            <template slot-scope="scope">
              <el-tag :type="getSeverityTagType(scope.row.severity)">
                {{ getSeverityText(scope.row.severity) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      
      <!-- 修复建议 -->
      <el-card class="suggestions-card">
        <div slot="header">
          <span>修复建议</span>
        </div>
        <div class="suggestions-list">
          <div
            v-for="(suggestion, index) in repairSuggestions"
            :key="index"
            class="suggestion-item">
            <div class="suggestion-title">
              <i class="el-icon-warning-outline"></i>
              {{ suggestion.title }}
            </div>
            <div class="suggestion-content">{{ suggestion.content }}</div>
            <div class="suggestion-actions">
              <el-button size="mini" type="primary" @click="applySuggestion(suggestion)">
                应用建议
              </el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleRecheck">重新检查</el-button>
      <el-button type="success" @click="handleExport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'QualityCheckDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    collaborationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      checkData: {
        totalRecords: 1000,
        validRecords: 850,
        invalidRecords: 150
      },
      checkRules: [
        {
          ruleName: '数据完整性检查',
          ruleType: 'completeness',
          checkResult: 'pass',
          errorCount: 0,
          description: '检查必填字段是否为空'
        },
        {
          ruleName: '数据格式检查',
          ruleType: 'format',
          checkResult: 'fail',
          errorCount: 25,
          description: '检查数据格式是否符合规范'
        },
        {
          ruleName: '数据一致性检查',
          ruleType: 'consistency',
          checkResult: 'pass',
          errorCount: 0,
          description: '检查数据之间的逻辑一致性'
        },
        {
          ruleName: '数据唯一性检查',
          ruleType: 'uniqueness',
          checkResult: 'fail',
          errorCount: 15,
          description: '检查主键字段是否重复'
        }
      ],
      qualityIssues: [
        {
          issueType: 'format_error',
          fieldName: '企业代码',
          issueDescription: '企业代码格式不符合18位统一社会信用代码规范',
          affectedRecords: 25,
          severity: 'high'
        },
        {
          issueType: 'duplicate',
          fieldName: '企业名称',
          issueDescription: '存在重复的企业名称记录',
          affectedRecords: 15,
          severity: 'medium'
        },
        {
          issueType: 'missing_value',
          fieldName: '注册资本',
          issueDescription: '注册资本字段存在空值',
          affectedRecords: 8,
          severity: 'low'
        }
      ],
      repairSuggestions: [
        {
          title: '修复企业代码格式错误',
          content: '建议使用标准的18位统一社会信用代码格式，可以通过数据清洗工具自动修复。',
          action: 'fix_format'
        },
        {
          title: '处理重复企业名称',
          content: '建议人工审核重复记录，确认是否为同一企业的不同记录，或进行数据合并。',
          action: 'merge_duplicates'
        },
        {
          title: '补充缺失的注册资本信息',
          content: '建议从企业工商信息系统中补充缺失的注册资本数据。',
          action: 'fill_missing'
        }
      ]
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
    qualityScore() {
      if (this.checkData.totalRecords === 0) return 0
      return Math.round((this.checkData.validRecords / this.checkData.totalRecords) * 100)
    }
  },
  methods: {
    // 获取规则类型标签类型
    getRuleTypeTagType(type) {
      const typeMap = {
        completeness: 'primary',
        format: 'warning',
        consistency: 'success',
        uniqueness: 'info'
      }
      return typeMap[type] || 'default'
    },
    
    // 获取规则类型文本
    getRuleTypeText(type) {
      const typeMap = {
        completeness: '完整性',
        format: '格式',
        consistency: '一致性',
        uniqueness: '唯一性'
      }
      return typeMap[type] || type
    },
    
    // 获取问题类型标签类型
    getIssueTypeTagType(type) {
      const typeMap = {
        format_error: 'warning',
        duplicate: 'danger',
        missing_value: 'info'
      }
      return typeMap[type] || 'default'
    },
    
    // 获取问题类型文本
    getIssueTypeText(type) {
      const typeMap = {
        format_error: '格式错误',
        duplicate: '重复数据',
        missing_value: '缺失值'
      }
      return typeMap[type] || type
    },
    
    // 获取严重程度标签类型
    getSeverityTagType(severity) {
      const severityMap = {
        high: 'danger',
        medium: 'warning',
        low: 'info'
      }
      return severityMap[severity] || 'default'
    },
    
    // 获取严重程度文本
    getSeverityText(severity) {
      const severityMap = {
        high: '高',
        medium: '中',
        low: '低'
      }
      return severityMap[severity] || severity
    },
    
    // 应用修复建议
    applySuggestion(suggestion) {
      this.$confirm(`确认应用修复建议：${suggestion.title}？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('修复建议已应用，正在处理...')
        // 这里应该调用相应的修复API
      })
    },
    
    // 重新检查
    handleRecheck() {
      this.$message.info('正在重新检查数据质量...')
      // 这里应该调用重新检查的API
    },
    
    // 导出报告
    handleExport() {
      this.$message.success('质量检查报告导出成功')
      // 这里应该调用导出API
    },
    
    // 关闭
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.quality-check-container {
  max-height: 600px;
  overflow-y: auto;
}

.overview-card,
.rules-card,
.issues-card,
.suggestions-card {
  margin-bottom: 20px;
}

.overview-item {
  text-align: center;
}

.overview-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.overview-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.suggestions-list {
  max-height: 300px;
  overflow-y: auto;
}

.suggestion-item {
  border: 1px solid #EBEEF5;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 10px;
  background-color: #FAFAFA;
}

.suggestion-title {
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.suggestion-title i {
  color: #E6A23C;
  margin-right: 5px;
}

.suggestion-content {
  color: #606266;
  line-height: 1.5;
  margin-bottom: 10px;
}

.suggestion-actions {
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
