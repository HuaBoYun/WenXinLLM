<template>
  <el-dialog
    title="风险预警配置"
    :visible.sync="dialogVisible"
    width="70%"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="config-container">
      <el-tabs v-model="activeTab" type="card">
        <!-- 基础配置 -->
        <el-tab-pane label="基础配置" name="basic">
          <el-form
            ref="basicForm"
            :model="basicConfig"
            :rules="basicRules"
            label-width="150px"
          >
            <el-form-item label="预警功能" prop="warningEnabled">
              <el-switch
                v-model="basicConfig.warningEnabled"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>

            <el-form-item label="自动评估" prop="autoAssessmentEnabled">
              <el-switch
                v-model="basicConfig.autoAssessmentEnabled"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>

            <el-form-item label="通知功能" prop="notificationEnabled">
              <el-switch
                v-model="basicConfig.notificationEnabled"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>

            <el-form-item label="高风险阈值" prop="highRiskThreshold">
              <el-input-number
                v-model="basicConfig.highRiskThreshold"
                :min="0"
                :max="100"
                :precision="1"
                controls-position="right"
                style="width: 200px"
              />
              <span class="input-suffix">分</span>
            </el-form-item>

            <el-form-item label="极高风险阈值" prop="extremeRiskThreshold">
              <el-input-number
                v-model="basicConfig.extremeRiskThreshold"
                :min="0"
                :max="100"
                :precision="1"
                controls-position="right"
                style="width: 200px"
              />
              <span class="input-suffix">分</span>
            </el-form-item>

            <el-form-item label="自动评估频率" prop="autoAssessmentFrequency">
              <el-select
                v-model="basicConfig.autoAssessmentFrequency"
                placeholder="请选择评估频率"
                style="width: 200px"
              >
                <el-option label="每天" :value="1" />
                <el-option label="每3天" :value="3" />
                <el-option label="每周" :value="7" />
                <el-option label="每月" :value="30" />
              </el-select>
            </el-form-item>

            <el-form-item label="预警保留天数" prop="warningRetentionDays">
              <el-input-number
                v-model="basicConfig.warningRetentionDays"
                :min="1"
                :max="365"
                controls-position="right"
                style="width: 200px"
              />
              <span class="input-suffix">天</span>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 风险等级配置 -->
        <el-tab-pane label="风险等级配置" name="riskLevel">
          <el-table
            :data="riskLevelConfig"
            border
            stripe
          >
            <el-table-column
              prop="level"
              label="风险等级"
              width="100"
              align="center"
            >
              <template #default="{ row }">
                <el-tag :type="getRiskLevelTagType(row.level)">
                  {{ getRiskLevelName(row.level) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="minScore"
              label="最低分数"
              width="120"
              align="center"
            >
              <template #default="{ row }">
                <el-input-number
                  v-model="row.minScore"
                  :min="0"
                  :max="100"
                  :precision="1"
                  size="small"
                  controls-position="right"
                />
              </template>
            </el-table-column>
            <el-table-column
              prop="maxScore"
              label="最高分数"
              width="120"
              align="center"
            >
              <template #default="{ row }">
                <el-input-number
                  v-model="row.maxScore"
                  :min="0"
                  :max="100"
                  :precision="1"
                  size="small"
                  controls-position="right"
                />
              </template>
            </el-table-column>
            <el-table-column
              prop="color"
              label="显示颜色"
              width="120"
              align="center"
            >
              <template #default="{ row }">
                <el-color-picker
                  v-model="row.color"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column
              prop="warningEnabled"
              label="启用预警"
              width="100"
              align="center"
            >
              <template #default="{ row }">
                <el-switch
                  v-model="row.warningEnabled"
                  size="small"
                />
              </template>
            </el-table-column>
            <el-table-column
              prop="description"
              label="描述"
              min-width="200"
            >
              <template #default="{ row }">
                <el-input
                  v-model="row.description"
                  size="small"
                  placeholder="请输入描述"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 通知配置 -->
        <el-tab-pane label="通知配置" name="notification">
          <el-form
            ref="notificationForm"
            :model="notificationConfig"
            label-width="150px"
          >
            <el-form-item label="邮件通知">
              <el-switch
                v-model="notificationConfig.emailEnabled"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>

            <el-form-item label="短信通知">
              <el-switch
                v-model="notificationConfig.smsEnabled"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>

            <el-form-item label="系统通知">
              <el-switch
                v-model="notificationConfig.systemEnabled"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>

            <el-form-item label="通知接收人">
              <el-select
                v-model="notificationConfig.recipients"
                multiple
                filterable
                placeholder="请选择通知接收人"
                style="width: 100%"
              >
                <el-option
                  v-for="user in userList"
                  :key="user.id"
                  :label="user.name"
                  :value="user.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="通知模板">
              <el-input
                v-model="notificationConfig.template"
                type="textarea"
                :rows="4"
                placeholder="请输入通知模板，支持变量：{projectName}, {riskLevel}, {riskScore}"
              />
            </el-form-item>

            <el-form-item label="通知频率限制">
              <el-checkbox-group v-model="notificationConfig.frequencyLimit">
                <el-checkbox label="sameWarning">相同预警24小时内只通知一次</el-checkbox>
                <el-checkbox label="sameProject">相同项目1小时内最多通知3次</el-checkbox>
                <el-checkbox label="dailyLimit">每日通知总数不超过50条</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 规则配置 -->
        <el-tab-pane label="规则配置" name="rules">
          <div class="rules-container">
            <div class="rules-header">
              <el-button type="primary" size="small" @click="addRule">
                <i class="el-icon-plus"></i> 添加规则
              </el-button>
            </div>
            
            <el-table
              :data="rulesConfig"
              border
              stripe
              class="rules-table"
            >
              <el-table-column
                prop="name"
                label="规则名称"
                width="150"
              >
                <template #default="{ row }">
                  <el-input
                    v-model="row.name"
                    size="small"
                    placeholder="规则名称"
                  />
                </template>
              </el-table-column>
              <el-table-column
                prop="condition"
                label="触发条件"
                min-width="200"
              >
                <template #default="{ row }">
                  <el-input
                    v-model="row.condition"
                    size="small"
                    placeholder="如：riskScore > 80"
                  />
                </template>
              </el-table-column>
              <el-table-column
                prop="action"
                label="执行动作"
                width="150"
              >
                <template #default="{ row }">
                  <el-select
                    v-model="row.action"
                    size="small"
                    placeholder="选择动作"
                  >
                    <el-option label="发送通知" value="notify" />
                    <el-option label="创建任务" value="createTask" />
                    <el-option label="发送邮件" value="sendEmail" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column
                prop="enabled"
                label="启用状态"
                width="100"
                align="center"
              >
                <template #default="{ row }">
                  <el-switch
                    v-model="row.enabled"
                    size="small"
                  />
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                width="100"
                align="center"
              >
                <template #default="{ row, $index }">
                  <el-button
                    type="text"
                    size="small"
                    @click="removeRule($index)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button @click="resetConfig">重置</el-button>
      <el-button type="primary" @click="saveConfig" :loading="saveLoading">
        保存配置
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getRiskWarningConfig,
  updateRiskWarningConfig
} from '@/api/contract/riskAssessment'

export default {
  name: 'RiskWarningConfig',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      saveLoading: false,
      activeTab: 'basic',
      basicConfig: {
        warningEnabled: true,
        autoAssessmentEnabled: true,
        notificationEnabled: true,
        highRiskThreshold: 70,
        extremeRiskThreshold: 85,
        autoAssessmentFrequency: 7,
        warningRetentionDays: 90
      },
      basicRules: {
        highRiskThreshold: [
          { required: true, message: '请设置高风险阈值', trigger: 'blur' }
        ],
        extremeRiskThreshold: [
          { required: true, message: '请设置极高风险阈值', trigger: 'blur' }
        ]
      },
      riskLevelConfig: [
        {
          level: 1,
          minScore: 0,
          maxScore: 49.9,
          color: '#67c23a',
          warningEnabled: false,
          description: '风险较低，正常监控即可'
        },
        {
          level: 2,
          minScore: 50,
          maxScore: 69.9,
          color: '#409eff',
          warningEnabled: false,
          description: '中等风险，需要关注'
        },
        {
          level: 3,
          minScore: 70,
          maxScore: 84.9,
          color: '#e6a23c',
          warningEnabled: true,
          description: '高风险，需要及时处理'
        },
        {
          level: 4,
          minScore: 85,
          maxScore: 100,
          color: '#f56c6c',
          warningEnabled: true,
          description: '极高风险，需要立即处理'
        }
      ],
      notificationConfig: {
        emailEnabled: true,
        smsEnabled: false,
        systemEnabled: true,
        recipients: [],
        template: '项目【{projectName}】出现{riskLevel}预警，风险评分：{riskScore}，请及时关注处理。',
        frequencyLimit: ['sameWarning', 'sameProject']
      },
      rulesConfig: [
        {
          name: '极高风险自动通知',
          condition: 'riskScore >= 85',
          action: 'notify',
          enabled: true
        },
        {
          name: '高风险邮件提醒',
          condition: 'riskScore >= 70',
          action: 'sendEmail',
          enabled: true
        }
      ],
      userList: []
    }
  },
  created() {
    this.fetchUserList()
  },
  methods: {
    async showConfig() {
      this.dialogVisible = true
      await this.loadConfig()
    },

    async loadConfig() {
      this.loading = true
      try {
        const response = await getRiskWarningConfig()
        if (response.code === 200) {
          const config = response.data
          // 合并配置数据
          Object.assign(this.basicConfig, config.basic || {})
          if (config.riskLevel) {
            this.riskLevelConfig = config.riskLevel
          }
          if (config.notification) {
            Object.assign(this.notificationConfig, config.notification)
          }
          if (config.rules) {
            this.rulesConfig = config.rules
          }
        }
      } catch (error) {
        this.$message.error('加载配置失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async saveConfig() {
      try {
        // 验证基础配置
        await this.$refs.basicForm.validate()
        
        this.saveLoading = true
        
        const config = {
          basic: this.basicConfig,
          riskLevel: this.riskLevelConfig,
          notification: this.notificationConfig,
          rules: this.rulesConfig
        }
        
        const response = await updateRiskWarningConfig(config)
        if (response.code === 200) {
          this.$message.success('配置保存成功')
          this.dialogVisible = false
        } else {
          this.$message.error(response.message || '配置保存失败')
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('配置保存失败：' + error.message)
        }
      } finally {
        this.saveLoading = false
      }
    },

    resetConfig() {
      this.$confirm('确定要重置所有配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loadConfig()
        this.$message.success('配置已重置')
      }).catch(() => {})
    },

    async fetchUserList() {
      // 暂时使用静态用户列表数据
      this.userList = [
        {
          id: '1',
          username: 'admin',
          realname: '系统管理员',
          orgname: '总公司'
        },
        {
          id: '2',
          username: 'zhangsan',
          realname: '张三',
          orgname: '技术部'
        },
        {
          id: '3',
          username: 'lisi',
          realname: '李四',
          orgname: '财务部'
        },
        {
          id: '4',
          username: 'wangwu',
          realname: '王五',
          orgname: '人事部'
        },
        {
          id: '5',
          username: 'zhaoliu',
          realname: '赵六',
          orgname: '市场部'
        }
      ]
      console.log('使用静态用户列表:', this.userList)
    },

    addRule() {
      this.rulesConfig.push({
        name: '',
        condition: '',
        action: 'notify',
        enabled: true
      })
    },

    removeRule(index) {
      this.rulesConfig.splice(index, 1)
    },

    getRiskLevelTagType(level) {
      const typeMap = {
        1: 'success',
        2: 'info',
        3: 'warning',
        4: 'danger'
      }
      return typeMap[level] || 'info'
    },

    getRiskLevelName(level) {
      const nameMap = {
        1: '低风险',
        2: '中风险',
        3: '高风险',
        4: '极高风险'
      }
      return nameMap[level] || '未知'
    }
  }
}
</script>

<style scoped>
.config-container {
  min-height: 400px;
}

.input-suffix {
  margin-left: 8px;
  color: #606266;
}

.rules-container {
  padding: 10px 0;
}

.rules-header {
  margin-bottom: 15px;
}

.rules-table {
  margin-top: 10px;
}

.dialog-footer {
  text-align: right;
}
</style>
