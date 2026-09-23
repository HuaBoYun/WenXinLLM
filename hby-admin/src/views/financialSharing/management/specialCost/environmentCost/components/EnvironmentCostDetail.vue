<template>
  <el-dialog
    title="环境成本详情"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="environment-detail">
      <!-- 基本信息 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>环境成本编码：</label>
              <span>{{ environmentData.environmentCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>环境成本名称：</label>
              <span>{{ environmentData.environmentName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>环境成本类型：</label>
              <el-tag :type="getEnvironmentTypeTag(environmentData.environmentType)">
                {{ getEnvironmentTypeName(environmentData.environmentType) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>成本金额：</label>
              <span class="amount-text">{{ formatAmount(environmentData.costAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>环境效益：</label>
              <span class="benefit-text">{{ formatAmount(environmentData.environmentBenefit) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>效益比：</label>
              <span :class="getBenefitRatioClass(getBenefitRatio())">
                {{ getBenefitRatio() }}
              </span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>所属部门：</label>
              <span>{{ environmentData.department }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>成本中心：</label>
              <span>{{ environmentData.costCenter }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>发生日期：</label>
              <span>{{ environmentData.occurDate }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>创建时间：</label>
              <span>{{ environmentData.createTime }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>更新时间：</label>
              <span>{{ environmentData.updateTime }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>创建人：</label>
              <span>{{ environmentData.createBy }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>更新人：</label>
              <span>{{ environmentData.updateBy }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 效益分析 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>效益分析</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="analysis-item">
              <div class="analysis-label">投入产出比</div>
              <div class="analysis-value">
                <span :class="getBenefitRatioClass(getBenefitRatio())">
                  {{ getBenefitRatio() }}
                </span>
                <span class="analysis-desc">
                  {{ getBenefitRatioDesc(getBenefitRatio()) }}
                </span>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="analysis-item">
              <div class="analysis-label">净效益</div>
              <div class="analysis-value">
                <span :class="getNetBenefitClass()">
                  {{ formatAmount(getNetBenefit()) }}
                </span>
                <span class="analysis-desc">
                  {{ getNetBenefitDesc() }}
                </span>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 环境成本描述 -->
      <el-card shadow="never" class="detail-card" v-if="environmentData.description">
        <div slot="header" class="card-header">
          <span>环境成本描述</span>
        </div>
        <div class="description-content">
          {{ environmentData.description }}
        </div>
      </el-card>

      <!-- 备注信息 -->
      <el-card shadow="never" class="detail-card" v-if="environmentData.remark">
        <div slot="header" class="card-header">
          <span>备注信息</span>
        </div>
        <div class="remark-content">
          {{ environmentData.remark }}
        </div>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getEnvironmentCostById } from '@/api/financialSharing/specialCost'

export default {
  name: 'EnvironmentCostDetail',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      environmentData: {}
    }
  },
  methods: {
    async showDetail(row) {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getEnvironmentCostById(row.id)
        if (code === 200) {
          this.environmentData = data
        }
      } catch (error) {
        this.$baseMessage('获取环境成本详情失败', 'error')
      } finally {
        this.loading = false
      }
    },
    handleEdit() {
      this.dialogVisible = false
      this.$parent.$refs.edit.showEdit(this.environmentData)
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getEnvironmentTypeName(type) {
      const typeMap = {
        'protection': '环境保护成本',
        'treatment': '环境治理成本',
        'monitoring': '环境监测成本',
        'loss': '环境损失成本'
      }
      return typeMap[type] || '未知'
    },
    getEnvironmentTypeTag(type) {
      const tagMap = {
        'protection': 'success',
        'treatment': 'primary',
        'monitoring': 'warning',
        'loss': 'danger'
      }
      return tagMap[type] || 'info'
    },
    getBenefitRatio() {
      if (!this.environmentData.costAmount || this.environmentData.costAmount === 0) return '0.00'
      return ((this.environmentData.environmentBenefit || 0) / this.environmentData.costAmount).toFixed(2)
    },
    getBenefitRatioClass(ratio) {
      const ratioNum = parseFloat(ratio)
      if (ratioNum >= 1) return 'text-success'
      if (ratioNum >= 0.5) return 'text-warning'
      return 'text-danger'
    },
    getBenefitRatioDesc(ratio) {
      const ratioNum = parseFloat(ratio)
      if (ratioNum >= 1.5) return '效益优秀'
      if (ratioNum >= 1) return '效益良好'
      if (ratioNum >= 0.5) return '效益一般'
      return '效益较差'
    },
    getNetBenefit() {
      return (this.environmentData.environmentBenefit || 0) - (this.environmentData.costAmount || 0)
    },
    getNetBenefitClass() {
      const netBenefit = this.getNetBenefit()
      if (netBenefit > 0) return 'text-success'
      if (netBenefit === 0) return 'text-warning'
      return 'text-danger'
    },
    getNetBenefitDesc() {
      const netBenefit = this.getNetBenefit()
      if (netBenefit > 0) return '产生正效益'
      if (netBenefit === 0) return '收支平衡'
      return '产生负效益'
    }
  }
}
</script>

<style lang="scss" scoped>
.environment-detail {
  .detail-card {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .card-header {
    font-weight: bold;
    color: #303133;
  }
  
  .detail-item {
    margin-bottom: 15px;
    
    label {
      font-weight: bold;
      color: #606266;
      margin-right: 8px;
    }
    
    span {
      color: #303133;
    }
    
    .amount-text {
      font-size: 16px;
      font-weight: bold;
      color: #e6a23c;
    }
    
    .benefit-text {
      font-size: 16px;
      font-weight: bold;
      color: #67c23a;
    }
  }
  
  .analysis-item {
    text-align: center;
    padding: 20px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
    
    .analysis-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 10px;
    }
    
    .analysis-value {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 5px;
      
      .analysis-desc {
        display: block;
        font-size: 12px;
        color: #909399;
        font-weight: normal;
        margin-top: 5px;
      }
    }
  }
  
  .description-content,
  .remark-content {
    line-height: 1.6;
    color: #606266;
    padding: 10px 0;
  }
}

.text-success {
  color: #67c23a;
}

.text-warning {
  color: #e6a23c;
}

.text-danger {
  color: #f56c6c;
}

.dialog-footer {
  text-align: right;
}
</style>
