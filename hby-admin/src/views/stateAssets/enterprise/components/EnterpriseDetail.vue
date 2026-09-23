<template>
  <el-dialog
    title="企业详情"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
  >
    <div class="enterprise-detail">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="企业名称">
          {{ detail.enterpriseName }}
        </el-descriptions-item>
        <el-descriptions-item label="统一社会信用代码">
          {{ detail.creditCode }}
        </el-descriptions-item>
        <el-descriptions-item label="企业类型">
          {{ getEnterpriseTypeLabel(detail.enterpriseType) }}
        </el-descriptions-item>
        <el-descriptions-item label="注册资本">
          {{ formatCurrency(detail.registeredCapital) }}万元
        </el-descriptions-item>
        <el-descriptions-item label="法定代表人">
          {{ detail.legalRepresentative }}
        </el-descriptions-item>
        <el-descriptions-item label="成立日期">
          {{ detail.establishDate }}
        </el-descriptions-item>
        <el-descriptions-item label="监管层级">
          {{ getSupervisionLevelLabel(detail.supervisionLevel) }}
        </el-descriptions-item>
        <el-descriptions-item label="企业状态">
          <el-tag :type="getStatusType(detail.enterpriseStatus)" size="small">
            {{ getStatusLabel(detail.enterpriseStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="上市状态">
          <el-tag
            :type="detail.listingStatus === 'LISTED' ? 'success' : 'info'"
            size="small"
          >
            {{ detail.listingStatus === 'LISTED' ? '已上市' : '未上市' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="股票代码" v-if="detail.listingStatus === 'LISTED'">
          {{ detail.stockCode || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="行业分类代码">
          {{ detail.industryCode || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="地区代码">
          {{ detail.regionCode || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="联系人">
          {{ detail.contactPerson || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="联系电话">
          {{ detail.contactPhone || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="联系邮箱">
          {{ detail.contactEmail || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ detail.createTime }}
        </el-descriptions-item>
        <el-descriptions-item label="注册地址" :span="2">
          {{ detail.registeredAddress || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="经营范围" :span="2">
          <div class="business-scope">
            {{ detail.businessScope || '-' }}
          </div>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 母公司信息 -->
      <div v-if="parentEnterprise" class="parent-info">
        <h4>母公司信息</h4>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="母公司名称">
            {{ parentEnterprise.enterpriseName }}
          </el-descriptions-item>
          <el-descriptions-item label="统一社会信用代码">
            {{ parentEnterprise.creditCode }}
          </el-descriptions-item>
          <el-descriptions-item label="企业类型">
            {{ getEnterpriseTypeLabel(parentEnterprise.enterpriseType) }}
          </el-descriptions-item>
          <el-descriptions-item label="法定代表人">
            {{ parentEnterprise.legalRepresentative }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 子公司列表 -->
      <div v-if="childEnterprises.length > 0" class="child-info">
        <h4>子公司列表</h4>
        <el-table :data="childEnterprises" border size="small">
          <el-table-column prop="enterpriseName" label="企业名称" />
          <el-table-column prop="creditCode" label="统一社会信用代码" />
          <el-table-column prop="enterpriseType" label="企业类型">
            <template #default="{ row }">
              {{ getEnterpriseTypeLabel(row.enterpriseType) }}
            </template>
          </el-table-column>
          <el-table-column prop="legalRepresentative" label="法定代表人" />
          <el-table-column prop="enterpriseStatus" label="状态">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.enterpriseStatus)" size="mini">
                {{ getStatusLabel(row.enterpriseStatus) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getEnterpriseDetail } from '@/api/stateAssets/enterprise'

export default {
  name: 'EnterpriseDetail',
  data() {
    return {
      dialogVisible: false,
      detail: {},
      parentEnterprise: null,
      childEnterprises: [],
    }
  },
  methods: {
    async show(row) {
      this.dialogVisible = true
      try {
        const { data } = await getEnterpriseDetail({ enterpriseId: row.enterpriseId })
        this.detail = data.enterprise
        this.parentEnterprise = data.parentEnterprise
        this.childEnterprises = data.childEnterprises || []
      } catch (error) {
        this.$baseMessage('获取企业详情失败', 'error')
        this.detail = row
        this.parentEnterprise = null
        this.childEnterprises = []
      }
    },
    getEnterpriseTypeLabel(type) {
      const typeMap = {
        STATE_OWNED: '国有独资',
        STATE_HOLDING: '国有控股',
        STATE_PARTICIPATING: '国有参股',
      }
      return typeMap[type] || type
    },
    getSupervisionLevelLabel(level) {
      const levelMap = {
        CENTRAL: '中央',
        LOCAL: '地方',
      }
      return levelMap[level] || level
    },
    getStatusType(status) {
      const statusMap = {
        NORMAL: 'success',
        CANCELLED: 'danger',
        MERGED: 'warning',
        SUSPENDED: 'info',
      }
      return statusMap[status] || 'info'
    },
    getStatusLabel(status) {
      const statusMap = {
        NORMAL: '正常',
        CANCELLED: '注销',
        MERGED: '合并',
        SUSPENDED: '暂停',
      }
      return statusMap[status] || '未知'
    },
    formatCurrency(value) {
      if (!value) return '0'
      return new Intl.NumberFormat('zh-CN').format(value)
    },
  },
}
</script>

<style lang="scss" scoped>
.enterprise-detail {
  .business-scope {
    max-height: 100px;
    overflow-y: auto;
    line-height: 1.5;
  }
  
  .parent-info,
  .child-info {
    margin-top: 20px;
    
    h4 {
      margin-bottom: 10px;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
