<template>
  <div class="fin-page">
    <div class="page-header mb-16">
      <div class="page-header-left"><i class="el-icon-s-check"></i>担保记录台账</div>
      <div class="page-header-desc">登记各级企业对外担保信息，识别互保/连环担保风险</div>
    </div>
    <el-row :gutter="16" class="mb-16">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <el-card class="stat-card" shadow="hover" :body-style="{padding:'16px'}">
          <div class="stat-inner">
            <div class="stat-icon" :style="{background:item.bg}"><i :class="item.icon" :style="{color:item.color}"></i></div>
            <div><div class="stat-value">{{ item.value }}<span class="stat-unit">{{ item.unit }}</span></div><div class="stat-label">{{ item.label }}</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card shadow="never" :body-style="{padding:'16px'}">
      <el-form :model="query" inline size="small" @submit.native.prevent="handleQuery">
        <el-form-item label="担保方"><el-input v-model="query.guarantor" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="被担保方"><el-input v-model="query.guaranteed" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="担保类型">
          <el-select v-model="query.guaranteeType" placeholder="全部" clearable>
            <el-option label="一般担保" value="一般担保" /><el-option label="连带担保" value="连带担保" /><el-option label="抵押担保" value="抵押担保" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable>
            <el-option label="生效中" value="生效中" /><el-option label="已到期" value="已到期" /><el-option label="已解除" value="已解除" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="tableData" v-loading="loading" border stripe style="width:100%">
        <el-table-column prop="guarantor" label="担保方" min-width="120" show-overflow-tooltip />
        <el-table-column prop="guaranteed" label="被担保方" min-width="120" show-overflow-tooltip />
        <el-table-column prop="guaranteeType" label="担保类型" width="100" />
        <el-table-column prop="amount" label="担保金额(万元)" width="130" align="right" />
        <el-table-column prop="startDate" label="起始日期" width="110" />
        <el-table-column prop="endDate" label="到期日期" width="110" />
        <el-table-column prop="riskLevel" label="风险等级" width="90" align="center">
          <template #default="{row}"><el-tag :type="row.riskLevel==='高'?'danger':row.riskLevel==='中'?'warning':'success'" size="mini">{{ row.riskLevel }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{row}"><el-tag :type="row.status==='生效中'?'success':row.status==='已到期'?'info':'warning'" size="mini">{{ row.status }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{row}"><el-button type="text" size="mini" @click="handleDetail(row)">详情</el-button></template>
        </el-table-column>
      </el-table>
      <el-pagination class="mt-16" background layout="total,sizes,prev,pager,next,jumper" :total="total" :page-size.sync="query.pageSize" :current-page.sync="query.pageNum" @current-change="getList" @size-change="getList" />
    </el-card>
  </div>
</template>
<script>
import { getGuaranteeRecordList } from '@/api/stateAssets/financialRiskPenetration'
import { mapGetters } from 'vuex'
export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'Dbjltz',
  data() {
    return {
      loading: false, tableData: [], total: 0,
      query: { guarantor: '', guaranteed: '', guaranteeType: '', status: '', pageNum: 1, pageSize: 20 },
      statCards: [
        { label: '担保总额', value: '-', unit: '亿', icon: 'el-icon-money', color: '#1677FF', bg: 'rgba(22,119,255,0.1)' },
        { label: '担保笔数', value: '-', unit: '笔', icon: 'el-icon-document', color: '#52C41A', bg: 'rgba(82,196,26,0.1)' },
        { label: '高风险担保', value: '-', unit: '笔', icon: 'el-icon-warning', color: '#FF4D4F', bg: 'rgba(255,77,79,0.1)' },
        { label: '互保企业', value: '-', unit: '家', icon: 'el-icon-connection', color: '#FA8C16', bg: 'rgba(250,140,22,0.1)' }
      ]
    }
  },
  created() { this.getList() },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await getGuaranteeRecordList(this.query)
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
          if (res.data.stats) {
            const s = res.data.stats
            this.statCards[0].value = s.totalAmount || '-'
            this.statCards[1].value = s.totalCount || '-'
            this.statCards[2].value = s.highRiskCount || '-'
            this.statCards[3].value = s.mutualCount || '-'
          }
        }
      } catch (e) { this.$message.error('查询失败') } finally { this.loading = false }
    },
    handleQuery() { this.query.pageNum = 1; this.getList() },
    resetQuery() { this.query = { guarantor: '', guaranteed: '', guaranteeType: '', status: '', pageNum: 1, pageSize: 20 }; this.getList() },
    handleDetail(row) { this.$message.info('详情功能开发中') }
  }
}
</script>
<style lang="scss" scoped>
.fin-page { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.mb-16 { margin-bottom: 16px; }
.mt-16 { margin-top: 16px; }
.page-header { border-radius: 8px; padding: 16px 24px; color: #fff; }
.page-header-left { display: flex; align-items: center; gap: 10px; font-size: 18px; font-weight: 700; i { font-size: 24px; } }
.page-header-desc { font-size: 13px; color: rgba(255,255,255,0.8); margin-top: 4px; }
.stat-card { border-radius: 6px; }
.stat-inner { display: flex; align-items: center; }
.stat-icon { width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 20px; } }
.stat-value { font-size: 20px; font-weight: 700; .stat-unit { font-size: 12px; color: #909399; margin-left: 2px; } }
.stat-label { font-size: 12px; color: #909399; margin-top: 2px; }
</style>
