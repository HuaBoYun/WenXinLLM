<template>
  <div class="app-container invest-page">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-document"></i><span>投资项目详情</span></div>
      <div class="page-header-desc">查看投资项目完整信息与生命周期</div>
    </div>
    <el-card shadow="never">
      <div slot="header"><span>基本信息</span><el-button style="float: right" size="small" icon="el-icon-back" @click="$router.go(-1)">返回列表</el-button></div>
      <el-descriptions :column="2" border v-if="detail">
        <el-descriptions-item label="项目名称">{{ detail.projectName }}</el-descriptions-item>
        <el-descriptions-item label="投资类型">{{ detail.investType }}</el-descriptions-item>
        <el-descriptions-item label="投资金额(万元)">{{ detail.investAmount }}</el-descriptions-item>
        <el-descriptions-item label="目标企业">{{ detail.targetCompany }}</el-descriptions-item>
        <el-descriptions-item label="审批状态"><el-tag :type="statusTagType(detail.approvalStatus)" size="small">{{ statusText(detail.approvalStatus) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    <!-- 生命周期时间轴 -->
    <el-card shadow="never" style="margin-top: 10px">
      <div slot="header"><span>项目生命周期</span></div>
      <el-timeline>
        <el-timeline-item v-for="(item, idx) in lifecycle" :key="idx" :timestamp="item.time" :type="item.type" placement="top">
          <el-card shadow="hover"><h4>{{ item.stage }}</h4><p>{{ item.description }}</p></el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-if="lifecycle.length === 0" description="暂无生命周期数据" />
    </el-card>
  </div>
</template>
<script>
import { getInvestProjectDetail } from '@/api/stateAssets/investPenetration'
export default {
  name: 'InvestProjectDetail',
  data() {
    return {
      detail: null,
      lifecycle: [],
    }
  },
  created() { this.fetchDetail() },
  methods: {
    async fetchDetail() {
      const id = this.$route.query.id || this.$route.params.id
      if (!id) return
      try {
        const res = await getInvestProjectDetail(id)
        if (res && res.result === 200) {
          this.detail = res.data || {}
          this.lifecycle = this.buildLifecycle(this.detail)
        }
      } catch (e) { console.error('获取详情失败:', e) }
    },
    buildLifecycle(d) {
      const stages = [
        { stage: '立项', time: d.createTime ? d.createTime.substring(0, 10) : '', description: '项目立项阶段', type: 'primary' },
        { stage: '审批', time: d.approvalDate || '', description: '投资审批阶段', type: 'warning' },
        { stage: '执行', time: '', description: '投资执行阶段', type: 'success' },
        { stage: '投后管理', time: '', description: '投后评价阶段', type: 'info' },
        { stage: '退出', time: '', description: '投资退出阶段', type: 'danger' },
      ]
      if (d.projectStatus === 'EXECUTING' || d.projectStatus === 'COMPLETED') stages[2].time = '执行中'
      if (d.projectStatus === 'COMPLETED') { stages[2].time = '已完成'; stages[3].time = '已完成' }
      return stages
    },
    statusTagType(s) { return { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger' }[s] || 'info' },
    statusText(s) { return { PENDING: '待审批', APPROVED: '已通过', REJECTED: '已驳回' }[s] || s },
  },
}
</script>
<style lang="scss" scoped>
.invest-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.el-descriptions { margin-top: 10px; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .el-timeline-item__wrapper h4 { color: #409EFF; }
</style>

