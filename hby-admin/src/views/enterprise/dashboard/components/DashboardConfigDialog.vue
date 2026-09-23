<template>
  <el-dialog
    title="驾驶舱配置"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form :model="configForm" :rules="rules" ref="configForm" label-width="120px">
      <el-tabs v-model="activeTab">
        <!-- 显示配置 -->
        <el-tab-pane label="显示配置" name="display">
          <el-form-item label="刷新间隔">
            <el-select v-model="configForm.refreshInterval" placeholder="请选择刷新间隔">
              <el-option label="1分钟" value="1"></el-option>
              <el-option label="5分钟" value="5"></el-option>
              <el-option label="10分钟" value="10"></el-option>
              <el-option label="30分钟" value="30"></el-option>
              <el-option label="1小时" value="60"></el-option>
              <el-option label="手动刷新" value="0"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="显示模块">
            <el-checkbox-group v-model="configForm.displayModules">
              <el-checkbox label="overview">企业概览</el-checkbox>
              <el-checkbox label="indicators">关键指标</el-checkbox>
              <el-checkbox label="business">业务状态</el-checkbox>
              <el-checkbox label="risk">风险监控</el-checkbox>
              <el-checkbox label="submission">数据报送</el-checkbox>
              <el-checkbox label="financial">财务趋势</el-checkbox>
              <el-checkbox label="distribution">业务分布</el-checkbox>
              <el-checkbox label="plan">经营计划</el-checkbox>
              <el-checkbox label="budget">预算执行</el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item label="默认时间范围">
            <el-select v-model="configForm.defaultTimeRange" placeholder="请选择默认时间范围">
              <el-option label="最近3个月" value="3M"></el-option>
              <el-option label="最近6个月" value="6M"></el-option>
              <el-option label="最近1年" value="1Y"></el-option>
              <el-option label="最近3年" value="3Y"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="图表主题">
            <el-radio-group v-model="configForm.chartTheme">
              <el-radio label="default">默认主题</el-radio>
              <el-radio label="dark">深色主题</el-radio>
              <el-radio label="light">浅色主题</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-tab-pane>

        <!-- 预警配置 -->
        <el-tab-pane label="预警配置" name="warning">
          <el-form-item label="营收增长率预警">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-input v-model="configForm.revenueGrowthWarning.min" placeholder="最小值">
                  <template slot="append">%</template>
                </el-input>
              </el-col>
              <el-col :span="12">
                <el-input v-model="configForm.revenueGrowthWarning.max" placeholder="最大值">
                  <template slot="append">%</template>
                </el-input>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item label="利润增长率预警">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-input v-model="configForm.profitGrowthWarning.min" placeholder="最小值">
                  <template slot="append">%</template>
                </el-input>
              </el-col>
              <el-col :span="12">
                <el-input v-model="configForm.profitGrowthWarning.max" placeholder="最大值">
                  <template slot="append">%</template>
                </el-input>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item label="资产负债率预警">
            <el-input v-model="configForm.assetLiabilityWarning" placeholder="预警阈值">
              <template slot="append">%</template>
            </el-input>
          </el-form-item>

          <el-form-item label="ROE预警">
            <el-input v-model="configForm.roeWarning" placeholder="最小值">
              <template slot="append">%</template>
            </el-input>
          </el-form-item>

          <el-form-item label="风险评分预警">
            <el-input v-model="configForm.riskScoreWarning" placeholder="预警阈值">
              <template slot="append">分</template>
            </el-input>
          </el-form-item>

          <el-form-item label="数据质量预警">
            <el-input v-model="configForm.dataQualityWarning" placeholder="最小值">
              <template slot="append">分</template>
            </el-input>
          </el-form-item>

          <el-form-item label="预警通知方式">
            <el-checkbox-group v-model="configForm.warningNotificationMethods">
              <el-checkbox label="system">系统通知</el-checkbox>
              <el-checkbox label="email">邮件通知</el-checkbox>
              <el-checkbox label="sms">短信通知</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-tab-pane>

        <!-- 数据源配置 -->
        <el-tab-pane label="数据源配置" name="datasource">
          <el-form-item label="财务数据源">
            <el-select v-model="configForm.financialDataSource" placeholder="请选择财务数据源">
              <el-option label="ERP系统" value="erp"></el-option>
              <el-option label="财务系统" value="financial"></el-option>
              <el-option label="手工录入" value="manual"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="业务数据源">
            <el-select v-model="configForm.businessDataSource" placeholder="请选择业务数据源">
              <el-option label="业务系统" value="business"></el-option>
              <el-option label="CRM系统" value="crm"></el-option>
              <el-option label="手工录入" value="manual"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="风险数据源">
            <el-select v-model="configForm.riskDataSource" placeholder="请选择风险数据源">
              <el-option label="风险管理系统" value="risk"></el-option>
              <el-option label="合规系统" value="compliance"></el-option>
              <el-option label="手工录入" value="manual"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="数据同步频率">
            <el-select v-model="configForm.dataSyncFrequency" placeholder="请选择数据同步频率">
              <el-option label="实时同步" value="realtime"></el-option>
              <el-option label="每小时" value="hourly"></el-option>
              <el-option label="每日" value="daily"></el-option>
              <el-option label="每周" value="weekly"></el-option>
              <el-option label="每月" value="monthly"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="数据质量检查">
            <el-switch v-model="configForm.enableDataQualityCheck"></el-switch>
          </el-form-item>

          <el-form-item label="数据备份">
            <el-switch v-model="configForm.enableDataBackup"></el-switch>
          </el-form-item>
        </el-tab-pane>

        <!-- 权限配置 -->
        <el-tab-pane label="权限配置" name="permission">
          <el-form-item label="查看权限">
            <el-select v-model="configForm.viewPermission" multiple placeholder="请选择可查看的角色">
              <el-option label="企业管理员" value="enterprise_admin"></el-option>
              <el-option label="财务负责人" value="finance_manager"></el-option>
              <el-option label="业务负责人" value="business_manager"></el-option>
              <el-option label="风险管理员" value="risk_manager"></el-option>
              <el-option label="数据管理员" value="data_manager"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="配置权限">
            <el-select v-model="configForm.configPermission" multiple placeholder="请选择可配置的角色">
              <el-option label="企业管理员" value="enterprise_admin"></el-option>
              <el-option label="系统管理员" value="system_admin"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="导出权限">
            <el-select v-model="configForm.exportPermission" multiple placeholder="请选择可导出的角色">
              <el-option label="企业管理员" value="enterprise_admin"></el-option>
              <el-option label="财务负责人" value="finance_manager"></el-option>
              <el-option label="业务负责人" value="business_manager"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="数据敏感度">
            <el-radio-group v-model="configForm.dataSensitivity">
              <el-radio label="public">公开</el-radio>
              <el-radio label="internal">内部</el-radio>
              <el-radio label="confidential">机密</el-radio>
              <el-radio label="secret">秘密</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="访问日志">
            <el-switch v-model="configForm.enableAccessLog"></el-switch>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button @click="handleReset">重置</el-button>
      <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDashboardConfig, saveDashboardConfig } from '@/api/enterprise/dashboard'

export default {
  name: 'DashboardConfigDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    enterpriseId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dialogVisible: false,
      activeTab: 'display',
      saving: false,
      configForm: {
        // 显示配置
        refreshInterval: '10',
        displayModules: ['overview', 'indicators', 'business', 'risk', 'submission', 'financial', 'distribution', 'plan', 'budget'],
        defaultTimeRange: '1Y',
        chartTheme: 'default',
        
        // 预警配置
        revenueGrowthWarning: {
          min: '-10',
          max: '100'
        },
        profitGrowthWarning: {
          min: '-20',
          max: '100'
        },
        assetLiabilityWarning: '70',
        roeWarning: '8',
        riskScoreWarning: '60',
        dataQualityWarning: '80',
        warningNotificationMethods: ['system'],
        
        // 数据源配置
        financialDataSource: 'erp',
        businessDataSource: 'business',
        riskDataSource: 'risk',
        dataSyncFrequency: 'daily',
        enableDataQualityCheck: true,
        enableDataBackup: true,
        
        // 权限配置
        viewPermission: ['enterprise_admin', 'finance_manager', 'business_manager'],
        configPermission: ['enterprise_admin'],
        exportPermission: ['enterprise_admin', 'finance_manager'],
        dataSensitivity: 'internal',
        enableAccessLog: true
      },
      rules: {
        refreshInterval: [
          { required: true, message: '请选择刷新间隔', trigger: 'change' }
        ],
        defaultTimeRange: [
          { required: true, message: '请选择默认时间范围', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.enterpriseId) {
        this.loadConfig()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    // 加载配置
    async loadConfig() {
      try {
        const response = await getDashboardConfig(this.enterpriseId)
        if (response.data) {
          this.configForm = { ...this.configForm, ...response.data }
        }
      } catch (error) {
        console.error('加载配置失败:', error)
      }
    },

    // 保存配置
    async handleSave() {
      this.$refs.configForm.validate(async (valid) => {
        if (!valid) return

        this.saving = true
        try {
          await saveDashboardConfig({
            enterpriseId: this.enterpriseId,
            ...this.configForm
          })
          
          this.$message.success('配置保存成功')
          this.handleClose()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('配置保存失败')
        } finally {
          this.saving = false
        }
      })
    },

    // 重置配置
    handleReset() {
      this.$refs.configForm.resetFields()
      this.loadConfig()
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.$refs.configForm.resetFields()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.el-checkbox-group {
  display: flex;
  flex-wrap: wrap;
}

.el-checkbox-group .el-checkbox {
  margin-right: 20px;
  margin-bottom: 10px;
}
</style>
