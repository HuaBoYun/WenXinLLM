<template>
  <el-dialog
    title="研发成本详情"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="rd-detail">
      <!-- 基本信息 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>研发项目编码：</label>
              <span>{{ rdData.rdProjectCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>研发项目名称：</label>
              <span>{{ rdData.rdProjectName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>研发类型：</label>
              <el-tag :type="getRdTypeTag(rdData.rdType)">
                {{ getRdTypeName(rdData.rdType) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>研发成本：</label>
              <span class="cost-text">{{ formatAmount(rdData.rdCost) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>预期收益：</label>
              <span class="benefit-text">{{ formatAmount(rdData.expectedBenefit) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>投资回报率：</label>
              <span :class="getRoiRatioClass(getRoiRatio())">
                {{ getRoiRatio() }}%
              </span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>所属部门：</label>
              <span>{{ rdData.department }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>成本中心：</label>
              <span>{{ rdData.costCenter }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>研发开始日期：</label>
              <span>{{ rdData.rdStartDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>研发结束日期：</label>
              <span>{{ rdData.rdEndDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>研发周期：</label>
              <span>{{ getRdDuration() }}天</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>创建时间：</label>
              <span>{{ rdData.createTime }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>更新时间：</label>
              <span>{{ rdData.updateTime }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>创建人：</label>
              <span>{{ rdData.createBy }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>更新人：</label>
              <span>{{ rdData.updateBy }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 投资回报分析 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>投资回报分析</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="analysis-item">
              <div class="analysis-label">投资回报率</div>
              <div class="analysis-value">
                <span :class="getRoiRatioClass(getRoiRatio())">
                  {{ getRoiRatio() }}%
                </span>
                <span class="analysis-desc">
                  {{ getRoiRatioDesc(getRoiRatio()) }}
                </span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="analysis-item">
              <div class="analysis-label">净收益</div>
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
          <el-col :span="8">
            <div class="analysis-item">
              <div class="analysis-label">投资效率</div>
              <div class="analysis-value">
                <span :class="getInvestmentEfficiencyClass()">
                  {{ getInvestmentEfficiency() }}
                </span>
                <span class="analysis-desc">
                  {{ getInvestmentEfficiencyDesc() }}
                </span>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 研发描述 -->
      <el-card shadow="never" class="detail-card" v-if="rdData.description">
        <div slot="header" class="card-header">
          <span>研发项目描述</span>
        </div>
        <div class="description-content">
          {{ rdData.description }}
        </div>
      </el-card>

      <!-- 备注信息 -->
      <el-card shadow="never" class="detail-card" v-if="rdData.remark">
        <div slot="header" class="card-header">
          <span>备注信息</span>
        </div>
        <div class="remark-content">
          {{ rdData.remark }}
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
import { getRdCostById } from '@/api/financialSharing/specialCost'

export default {
  name: 'RdCostDetail',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      rdData: {}
    }
  },
  methods: {
    async showDetail(row) {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getRdCostById(row.id)
        if (code === 200) {
          this.rdData = data
        }
      } catch (error) {
        this.$baseMessage('获取研发成本详情失败', 'error')
      } finally {
        this.loading = false
      }
    },
    handleEdit() {
      this.dialogVisible = false
      this.$parent.$refs.edit.showEdit(this.rdData)
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getRdTypeName(type) {
      const typeMap = {
        'basic_research': '基础研究',
        'applied_research': '应用研究',
        'experimental_development': '试验发展',
        'technology_improvement': '技术改进'
      }
      return typeMap[type] || '未知'
    },
    getRdTypeTag(type) {
      const tagMap = {
        'basic_research': 'primary',
        'applied_research': 'success',
        'experimental_development': 'warning',
        'technology_improvement': 'info'
      }
      return tagMap[type] || 'info'
    },
    getRoiRatio() {
      if (!this.rdData.rdCost || this.rdData.rdCost === 0) return '0.00'
      return (((this.rdData.expectedBenefit || 0) - this.rdData.rdCost) / this.rdData.rdCost * 100).toFixed(2)
    },
    getRoiRatioClass(ratio) {
      const ratioNum = parseFloat(ratio)
      if (ratioNum >= 20) return 'text-success'
      if (ratioNum >= 10) return 'text-warning'
      return 'text-danger'
    },
    getRoiRatioDesc(ratio) {
      const ratioNum = parseFloat(ratio)
      if (ratioNum >= 30) return '回报优秀'
      if (ratioNum >= 20) return '回报良好'
      if (ratioNum >= 10) return '回报一般'
      return '回报较低'
    },
    getNetBenefit() {
      return (this.rdData.expectedBenefit || 0) - (this.rdData.rdCost || 0)
    },
    getNetBenefitClass() {
      const netBenefit = this.getNetBenefit()
      if (netBenefit > 0) return 'text-success'
      if (netBenefit === 0) return 'text-warning'
      return 'text-danger'
    },
    getNetBenefitDesc() {
      const netBenefit = this.getNetBenefit()
      if (netBenefit > 0) return '预期盈利'
      if (netBenefit === 0) return '收支平衡'
      return '预期亏损'
    },
    getInvestmentEfficiency() {
      const duration = this.getRdDurationNum()
      if (!duration || duration === 0) return '0.00'
      return ((this.rdData.expectedBenefit || 0) / duration).toFixed(0)
    },
    getInvestmentEfficiencyClass() {
      const efficiency = parseFloat(this.getInvestmentEfficiency())
      if (efficiency >= 10000) return 'text-success'
      if (efficiency >= 5000) return 'text-warning'
      return 'text-danger'
    },
    getInvestmentEfficiencyDesc() {
      const efficiency = parseFloat(this.getInvestmentEfficiency())
      if (efficiency >= 10000) return '效率很高'
      if (efficiency >= 5000) return '效率一般'
      return '效率较低'
    },
    getRdDuration() {
      if (!this.rdData.rdStartDate || !this.rdData.rdEndDate) return '0'
      const start = new Date(this.rdData.rdStartDate)
      const end = new Date(this.rdData.rdEndDate)
      return Math.ceil((end - start) / (1000 * 60 * 60 * 24))
    },
    getRdDurationNum() {
      return parseInt(this.getRdDuration())
    }
  }
}
</script>

<style lang="scss" scoped>
.rd-detail {
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
    
    .cost-text {
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
