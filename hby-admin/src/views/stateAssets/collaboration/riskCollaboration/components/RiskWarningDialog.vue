<template>
  <el-dialog
    title="风险预警管理"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <div class="risk-warning-container">
      <!-- 风险基本信息 -->
      <el-card class="risk-info-card">
        <div slot="header">
          <span>风险基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">企业名称：</span>
              <span class="info-value">{{ riskData.enterpriseName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">风险名称：</span>
              <span class="info-value">{{ riskData.riskName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">风险评分：</span>
              <el-progress
                :percentage="riskData.riskScore"
                :color="getRiskScoreColor(riskData.riskScore)"
                :stroke-width="15"
                text-inside>
              </el-progress>
            </div>
          </el-col>
        </el-row>
      </el-card>
      
      <!-- 预警配置 -->
      <el-card class="warning-config-card">
        <div slot="header">
          <span>预警配置</span>
        </div>
        <el-form ref="warningForm" :model="warningData" :rules="warningRules" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预警级别" prop="warningLevel">
                <el-select v-model="warningData.warningLevel" placeholder="请选择预警级别" style="width: 100%">
                  <el-option label="红色预警" value="red"></el-option>
                  <el-option label="橙色预警" value="orange"></el-option>
                  <el-option label="黄色预警" value="yellow"></el-option>
                  <el-option label="蓝色预警" value="blue"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预警类型" prop="warningType">
                <el-select v-model="warningData.warningType" placeholder="请选择预警类型" style="width: 100%">
                  <el-option label="风险阈值预警" value="threshold"></el-option>
                  <el-option label="趋势预警" value="trend"></el-option>
                  <el-option label="异常预警" value="anomaly"></el-option>
                  <el-option label="合规预警" value="compliance"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="触发条件" prop="triggerCondition">
                <el-input v-model="warningData.triggerCondition" placeholder="请输入触发条件"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预警阈值" prop="warningThreshold">
                <el-input-number
                  v-model="warningData.warningThreshold"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 100%">
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="通知方式" prop="notificationMethods">
            <el-checkbox-group v-model="warningData.notificationMethods">
              <el-checkbox label="email">邮件通知</el-checkbox>
              <el-checkbox label="sms">短信通知</el-checkbox>
              <el-checkbox label="system">系统通知</el-checkbox>
              <el-checkbox label="phone">电话通知</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item label="通知对象" prop="notificationTargets">
            <el-select
              v-model="warningData.notificationTargets"
              multiple
              placeholder="请选择通知对象"
              style="width: 100%">
              <el-option label="风险管理员" value="risk_manager"></el-option>
              <el-option label="企业负责人" value="enterprise_manager"></el-option>
              <el-option label="监管人员" value="supervisor"></el-option>
              <el-option label="相关部门" value="related_department"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="预警描述" prop="warningDescription">
            <el-input
              v-model="warningData.warningDescription"
              type="textarea"
              :rows="4"
              placeholder="请输入预警描述">
            </el-input>
          </el-form-item>
        </el-form>
      </el-card>
      
      <!-- 预警记录 -->
      <el-card class="warning-records-card">
        <div slot="header">
          <span>预警记录</span>
          <el-button style="float: right; padding: 3px 0" type="text" @click="refreshRecords">刷新</el-button>
        </div>
        <el-table :data="warningRecords" style="width: 100%">
          <el-table-column prop="warningTime" label="预警时间" width="180"></el-table-column>
          <el-table-column prop="warningLevel" label="预警级别" width="120">
            <template slot-scope="scope">
              <el-tag :type="getWarningLevelTagType(scope.row.warningLevel)">
                {{ getWarningLevelText(scope.row.warningLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningType" label="预警类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getWarningTypeTagType(scope.row.warningType)">
                {{ getWarningTypeText(scope.row.warningType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningContent" label="预警内容"></el-table-column>
          <el-table-column prop="handleStatus" label="处理状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getHandleStatusTagType(scope.row.handleStatus)">
                {{ getHandleStatusText(scope.row.handleStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleWarning(scope.row)">处理</el-button>
              <el-button size="mini" type="text" @click="viewDetails(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      
      <!-- 预警统计 -->
      <el-card class="warning-stats-card">
        <div slot="header">
          <span>预警统计</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value red">{{ warningStats.redWarnings }}</div>
              <div class="stat-label">红色预警</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value orange">{{ warningStats.orangeWarnings }}</div>
              <div class="stat-label">橙色预警</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value yellow">{{ warningStats.yellowWarnings }}</div>
              <div class="stat-label">黄色预警</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-item">
              <div class="stat-value blue">{{ warningStats.blueWarnings }}</div>
              <div class="stat-label">蓝色预警</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveWarning">保存预警配置</el-button>
      <el-button type="warning" @click="handleTriggerWarning">触发预警</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'RiskWarningDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    riskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      warningData: {
        warningLevel: 'yellow',
        warningType: 'threshold',
        triggerCondition: '风险评分超过80分',
        warningThreshold: 80,
        notificationMethods: ['email', 'system'],
        notificationTargets: ['risk_manager'],
        warningDescription: ''
      },
      warningRules: {
        warningLevel: [
          { required: true, message: '请选择预警级别', trigger: 'change' }
        ],
        warningType: [
          { required: true, message: '请选择预警类型', trigger: 'change' }
        ],
        triggerCondition: [
          { required: true, message: '请输入触发条件', trigger: 'blur' }
        ],
        warningThreshold: [
          { required: true, message: '请输入预警阈值', trigger: 'blur' }
        ],
        notificationMethods: [
          { required: true, message: '请选择通知方式', trigger: 'change' }
        ],
        notificationTargets: [
          { required: true, message: '请选择通知对象', trigger: 'change' }
        ]
      },
      warningRecords: [
        {
          warningTime: '2024-01-15 14:30:00',
          warningLevel: 'red',
          warningType: 'threshold',
          warningContent: '市场风险评分达到85分，超过预警阈值',
          handleStatus: 'pending'
        },
        {
          warningTime: '2024-01-14 10:15:00',
          warningLevel: 'yellow',
          warningType: 'trend',
          warningContent: '风险评分呈上升趋势，需要关注',
          handleStatus: 'handled'
        }
      ],
      warningStats: {
        redWarnings: 2,
        orangeWarnings: 5,
        yellowWarnings: 8,
        blueWarnings: 3
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
  methods: {
    // 获取风险评分颜色
    getRiskScoreColor(score) {
      if (score >= 80) return '#F56C6C'
      if (score >= 60) return '#E6A23C'
      return '#67C23A'
    },
    
    // 获取预警级别标签类型
    getWarningLevelTagType(level) {
      const levelMap = {
        red: 'danger',
        orange: 'warning',
        yellow: 'primary',
        blue: 'info'
      }
      return levelMap[level] || 'default'
    },
    
    // 获取预警级别文本
    getWarningLevelText(level) {
      const levelMap = {
        red: '红色预警',
        orange: '橙色预警',
        yellow: '黄色预警',
        blue: '蓝色预警'
      }
      return levelMap[level] || level
    },
    
    // 获取预警类型标签类型
    getWarningTypeTagType(type) {
      const typeMap = {
        threshold: 'danger',
        trend: 'warning',
        anomaly: 'primary',
        compliance: 'success'
      }
      return typeMap[type] || 'default'
    },
    
    // 获取预警类型文本
    getWarningTypeText(type) {
      const typeMap = {
        threshold: '阈值预警',
        trend: '趋势预警',
        anomaly: '异常预警',
        compliance: '合规预警'
      }
      return typeMap[type] || type
    },
    
    // 获取处理状态标签类型
    getHandleStatusTagType(status) {
      const statusMap = {
        pending: 'warning',
        handling: 'primary',
        handled: 'success',
        ignored: 'info'
      }
      return statusMap[status] || 'default'
    },
    
    // 获取处理状态文本
    getHandleStatusText(status) {
      const statusMap = {
        pending: '待处理',
        handling: '处理中',
        handled: '已处理',
        ignored: '已忽略'
      }
      return statusMap[status] || status
    },
    
    // 刷新记录
    refreshRecords() {
      this.$message.info('正在刷新预警记录...')
      // 这里应该调用API刷新数据
    },
    
    // 处理预警
    handleWarning(record) {
      this.$prompt('请输入处理说明', '处理预警', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(({ value }) => {
        record.handleStatus = 'handled'
        this.$message.success('预警处理成功')
      })
    },
    
    // 查看详情
    viewDetails(record) {
      this.$message.info(`查看预警详情：${record.warningContent}`)
      // 这里应该打开详情页面
    },
    
    // 保存预警配置
    handleSaveWarning() {
      this.$refs.warningForm.validate((valid) => {
        if (valid) {
          this.$message.success('预警配置保存成功')
        }
      })
    },
    
    // 触发预警
    handleTriggerWarning() {
      this.$confirm('确认手动触发预警吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('预警已触发')
        // 添加新的预警记录
        this.warningRecords.unshift({
          warningTime: new Date().toLocaleString(),
          warningLevel: this.warningData.warningLevel,
          warningType: this.warningData.warningType,
          warningContent: '手动触发的预警',
          handleStatus: 'pending'
        })
      })
    },
    
    // 关闭
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.risk-warning-container {
  max-height: 600px;
  overflow-y: auto;
}

.risk-info-card,
.warning-config-card,
.warning-records-card,
.warning-stats-card {
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 15px;
}

.info-label {
  font-weight: bold;
  color: #606266;
  display: inline-block;
  width: 80px;
}

.info-value {
  color: #303133;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-value.red {
  color: #F56C6C;
}

.stat-value.orange {
  color: #E6A23C;
}

.stat-value.yellow {
  color: #F7BA2A;
}

.stat-value.blue {
  color: #409EFF;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.dialog-footer {
  text-align: right;
}
</style>
