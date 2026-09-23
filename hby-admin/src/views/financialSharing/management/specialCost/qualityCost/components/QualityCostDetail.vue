<template>
  <el-dialog
    title="质量成本详情"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="quality-detail">
      <!-- 基本信息 -->
      <el-card shadow="never" class="detail-card">
        <div slot="header" class="card-header">
          <span>基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>质量成本编码：</label>
              <span>{{ qualityData.qualityCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>质量成本名称：</label>
              <span>{{ qualityData.qualityName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>质量成本类型：</label>
              <el-tag :type="getQualityTypeTag(qualityData.qualityType)">
                {{ getQualityTypeName(qualityData.qualityType) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>成本金额：</label>
              <span class="amount-text">{{ formatAmount(qualityData.costAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>所属部门：</label>
              <span>{{ qualityData.department }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>成本中心：</label>
              <span>{{ qualityData.costCenter }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>发生日期：</label>
              <span>{{ qualityData.occurDate }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>创建时间：</label>
              <span>{{ qualityData.createTime }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>更新时间：</label>
              <span>{{ qualityData.updateTime }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>创建人：</label>
              <span>{{ qualityData.createBy }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>更新人：</label>
              <span>{{ qualityData.updateBy }}</span>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 质量成本描述 -->
      <el-card shadow="never" class="detail-card" v-if="qualityData.description">
        <div slot="header" class="card-header">
          <span>质量成本描述</span>
        </div>
        <div class="description-content">
          {{ qualityData.description }}
        </div>
      </el-card>

      <!-- 备注信息 -->
      <el-card shadow="never" class="detail-card" v-if="qualityData.remark">
        <div slot="header" class="card-header">
          <span>备注信息</span>
        </div>
        <div class="remark-content">
          {{ qualityData.remark }}
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
import { getQualityCostById } from '@/api/financialSharing/specialCost'

export default {
  name: 'QualityCostDetail',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      qualityData: {}
    }
  },
  methods: {
    async showDetail(row) {
      this.dialogVisible = true
      this.loading = true
      
      try {
        const { code, data } = await getQualityCostById(row.id)
        if (code === 200) {
          this.qualityData = data
        }
      } catch (error) {
        this.$baseMessage('获取质量成本详情失败', 'error')
      } finally {
        this.loading = false
      }
    },
    handleEdit() {
      this.dialogVisible = false
      this.$parent.$refs.edit.showEdit(this.qualityData)
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getQualityTypeName(type) {
      const typeMap = {
        'prevention': '预防成本',
        'appraisal': '鉴定成本',
        'internal_failure': '内部失败成本',
        'external_failure': '外部失败成本'
      }
      return typeMap[type] || '未知'
    },
    getQualityTypeTag(type) {
      const tagMap = {
        'prevention': 'success',
        'appraisal': 'primary',
        'internal_failure': 'warning',
        'external_failure': 'danger'
      }
      return tagMap[type] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.quality-detail {
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
  }
  
  .description-content,
  .remark-content {
    line-height: 1.6;
    color: #606266;
    padding: 10px 0;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
