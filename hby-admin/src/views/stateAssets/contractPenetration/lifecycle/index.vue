<template>
  <div class="lifecycle-page" v-loading="loading">
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title"><i class="el-icon-time" style="margin-right:8px;"/>合同全生命周期详情</div>
        <div class="banner-sub">从起草到终止的完整生命周期追踪 · 审批链路 · 付款进度 · 交付里程碑</div>
      </div>
      <div v-if="currentContract" class="banner-right">
        <el-button size="mini" plain @click="goCompliance"><i class="el-icon-finished"/> 合规追踪</el-button>
        <el-button size="mini" plain @click="goExecution"><i class="el-icon-timer"/> 履行监控</el-button>
        <el-button size="mini" plain @click="goDrillDown"><i class="el-icon-zoom-in"/> 穿透分析</el-button>
      </div>
    </div>

    <!-- 合同选择 -->
    <el-card shadow="never" style="margin-bottom:16px;">
      <div style="display:flex;align-items:center;gap:12px;">
        <span style="font-weight:600;color:#303133;flex-shrink:0;">选择合同：</span>
        <el-select v-model="selectedContractNo" placeholder="请输入合同编号或名称" filterable style="width:320px;" @change="loadContract">
          <el-option v-for="c in contractOptions" :key="c.contractNo" :label="c.contractNo + ' — ' + c.contractName" :value="c.contractNo"/>
        </el-select>
        <div v-if="currentContract" style="margin-left:16px;display:flex;gap:8px;align-items:center;">
          <span class="con-tag" :style="typeTagStyle(currentContract.contractType)">{{ currentContract.contractType }}</span>
          <span style="font-size:13px;color:#595959;">合同金额：<strong>{{ currentContract.amount.toLocaleString() }}万元</strong></span>
          <el-tag :type="currentContract.complianceStatus==='COMPLIANT'?'success':'danger'" size="small">{{ currentContract.complianceStatus==='COMPLIANT'?'合规':'违规' }}</el-tag>
        </div>
      </div>
    </el-card>

    <template v-if="currentContract">
      <el-row :gutter="16">
        <!-- 左侧：生命周期时间轴 -->
        <el-col :span="12">
          <el-card shadow="never" style="margin-bottom:16px;">
            <div slot="header" style="font-weight:600;"><i class="el-icon-date" style="color:#1677FF;margin-right:6px;"/>生命周期时间轴</div>
            <el-timeline>
              <el-timeline-item v-for="(node, i) in currentContract.lifecycle" :key="i" :type="node.type" :timestamp="node.time" placement="top">
                <div class="timeline-node">
                  <div class="node-step-title">
                    <strong>{{ node.step }}</strong>
                    <el-tag v-if="node.statusTag" :type="node.statusTagType" size="mini" style="margin-left:8px;">{{ node.statusTag }}</el-tag>
                  </div>
                  <div class="node-detail">
                    <div v-if="node.operator">经办人：{{ node.operator }}</div>
                    <div v-if="node.remark" style="color:#8c8c8c;margin-top:2px;">{{ node.remark }}</div>
                  </div>
                </div>
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>

        <!-- 右侧：付款进度 + 交付里程碑 -->
        <el-col :span="12">
          <el-card shadow="never" style="margin-bottom:16px;">
            <div slot="header" style="font-weight:600;"><i class="el-icon-money" style="color:#52C41A;margin-right:6px;"/>付款进度</div>
            <div class="payment-section">
              <div class="pay-row">
                <span class="pay-label">合同总额</span>
                <span class="pay-val" style="color:#1677FF;">{{ currentContract.amount.toLocaleString() }}万元</span>
              </div>
              <div class="pay-row">
                <span class="pay-label">已付金额</span>
                <span class="pay-val" style="color:#52C41A;">{{ currentContract.payment.paid.toLocaleString() }}万元</span>
              </div>
              <div class="pay-row">
                <span class="pay-label">待付金额</span>
                <span class="pay-val" style="color:#FA8C16;">{{ (currentContract.amount - currentContract.payment.paid).toLocaleString() }}万元</span>
              </div>
              <div style="margin: 12px 0 6px; font-size: 12px; color: #8c8c8c;">已付进度</div>
              <el-progress :percentage="Math.round(currentContract.payment.paid/currentContract.amount*100)" :color="currentContract.payment.overdue>0?'#F5222D':'#52C41A'" :stroke-width="14"/>
              <div class="pay-row" style="margin-top:10px;">
                <span class="pay-label">超期付款</span>
                <span :style="{color: currentContract.payment.overdue>0?'#F5222D':'#52C41A', fontWeight:'600'}">
                  {{ currentContract.payment.overdue>0 ? currentContract.payment.overdue.toLocaleString()+'万元' : '✓ 付款正常' }}
                </span>
              </div>
            </div>
          </el-card>

          <el-card shadow="never" style="margin-bottom:16px;">
            <div slot="header" style="font-weight:600;"><i class="el-icon-flag" style="color:#FA8C16;margin-right:6px;"/>交付里程碑</div>
            <el-table :data="currentContract.milestones" size="small" border>
              <el-table-column prop="name" label="里程碑" min-width="130"/>
              <el-table-column prop="plannedDate" label="计划完成" width="105"/>
              <el-table-column prop="actualDate" label="实际完成" width="105">
                <template slot-scope="{row}"><span :style="{color:row.actualDate?'#52C41A':'#8c8c8c'}">{{ row.actualDate || '—' }}</span></template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="90" align="center">
                <template slot-scope="{row}">
                  <el-tag :type="row.milestoneStatus==='DONE'?'success':row.milestoneStatus==='DOING'?'primary':'info'" size="mini">{{ {DONE:'已完成',DOING:'进行中',TODO:'未开始'}[row.milestoneStatus] }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>

          <el-card shadow="never">
            <div slot="header" style="font-weight:600;"><i class="el-icon-edit-outline" style="color:#FA8C16;margin-right:6px;"/>合同变更记录</div>
            <el-empty v-if="!currentContract.changes || !currentContract.changes.length" description="暂无变更记录" :image-size="60"/>
            <el-table v-else :data="currentContract.changes" size="small" border>
              <el-table-column prop="changeNo" label="变更编号" width="120"/>
              <el-table-column prop="content" label="变更内容" min-width="140"/>
              <el-table-column prop="amountChange" label="金额变化(万)" width="110" align="right">
                <template slot-scope="{row}"><span :style="{color:row.amountChange>0?'#F5222D':'#52C41A'}">{{ row.amountChange>0?'+':'' }}{{ row.amountChange }}</span></template>
              </el-table-column>
              <el-table-column prop="approver" label="审批人" width="90"/>
              <el-table-column prop="changeDate" label="变更日期" width="105"/>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </template>
    <el-empty v-else description="请先选择要查看的合同" :image-size="120" style="margin-top:60px;"/>
  </div>
</template>

<script>
import { getContractList, getContractLifecycle } from '@/api/stateAssets/contractPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'ContractLifecycle',
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
  data() {
    return {
      selectedContractNo: '',
      currentContract: null,
      contractOptions: [],
      loading: false,
    }
  },
  created() { this.loadContractOptions() },
  methods: {
    async loadContractOptions() {
      this.loading = true
      try {
        const res = await getContractList({ pageNumber: 1, pageSize: 200 })
        if (res && res.result === 200 && res.data) {
          this.contractOptions = (res.data.tlist || []).filter(c => c.contractNo || c.contractId).map(c => ({
            contractNo: c.contractNo || c.contractId || '',
            contractName: c.contractName || '未命名合同',
            contractType: c.contractType || '',
            amount: Number(c.contractAmount || c.amount) || 0,
            complianceStatus: c.complianceStatus || 'COMPLIANT',
            paidAmount: Number(c.paidAmount) || 0,
            overdueAmount: Number(c.overdueAmount) || 0,
          }))
          // 支持路由参数跳转到指定合同
          const routeNo = this.$route.query.contractNo
          if (routeNo && this.contractOptions.find(c => c.contractNo === routeNo)) {
            this.selectedContractNo = routeNo
          } else if (this.contractOptions.length > 0) {
            // 默认选中第一条合同
            this.selectedContractNo = this.contractOptions[0].contractNo
          }
          if (this.selectedContractNo) {
            this.loadContract(this.selectedContractNo)
          }
        } else {
          this.$message.warning('暂无合同数据')
        }
      } catch (e) {
        this.$message.error('加载合同列表失败')
        console.error('加载合同列表失败', e)
      } finally {
        this.loading = false
      }
    },
    async loadContract(no) {
      if (!no) { this.currentContract = null; return }
      this.loading = true
      try {
        // 先从已加载的合同列表中获取基础信息
        const baseInfo = this.contractOptions.find(c => c.contractNo === no) || {}
        const res = await getContractLifecycle(no)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          // nodes 就是生命周期节点数组，接口返回内容映射到前端所需字段
          const nodes = (d.nodes || []).map(n => ({
            step: n.nodeName || n.step || n.name || '',
            time: n.nodeTime || n.time || n.date || '',
            type: n.nodeType || n.type || 'primary',
            statusTag: n.status || n.statusTag || '',
            statusTagType: n.statusTagType || 'info',
            operator: n.operator || n.handledBy || '',
            remark: n.remark || n.description || '',
          }))
          // nodes 为空时生成占位时间轴
          const lifecycle = nodes.length > 0 ? nodes : [
            { step: '合同起草', time: '', type: 'info', statusTag: '历史节点', statusTagType: 'info', operator: '', remark: '暂无详细节点数据' },
          ]
          this.currentContract = {
            contractNo: d.contractId || no,
            contractName: baseInfo.contractName || d.contractId || no,
            contractType: baseInfo.contractType || '',
            amount: baseInfo.amount || 0,
            complianceStatus: baseInfo.complianceStatus || 'COMPLIANT',
            lifecycle,
            payment: {
              paid: baseInfo.paidAmount || 0,
              overdue: baseInfo.overdueAmount || 0,
            },
            milestones: d.milestones || [],
            changes: d.changes || [],
          }
        } else {
          this.currentContract = null
          this.$message.warning('未获取到该合同的生命周期数据')
        }
      } catch (e) {
        this.currentContract = null
        this.$message.error('加载生命周期数据失败')
        console.error('加载生命周期失败', e)
      } finally {
        this.loading = false
      }
    },
    /** 跳转到合规追踪页面 */
    goCompliance() {
      this.$router.push('/monitorExecute/Htsphgzz')
    },
    /** 跳转到履行监控页面 */
    goExecution() {
      this.$router.push('/monitorExecute/Htlxjk1')
    },
    /** 跳转到穿透分析页面 */
    goDrillDown() {
      this.$router.push('/monitorExecute/Htctfx')
    },
    typeTagStyle(v) {
      const map = {
        '采购合同': { background: '#EBF1FF', color: '#1677FF', border: '1px solid #ADC6FF' },
        '销售合同': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '工程合同': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '租赁合同': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
        '金融合同': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '劳务合同': { background: '#E8F4FF', color: '#0050A0', border: '1px solid #91CAFF' },
        '服务合同': { background: '#F0FFF4', color: '#389E0D', border: '1px solid #95DE64' },
      }
      return map[v] || {}
    },
  },
}
</script>

<style lang="scss" scoped>
.lifecycle-page { padding: 16px; background: #f5f7fa; min-height: 100%; }
.page-banner {
  border-radius: 8px; padding: 20px 32px; margin-bottom: 16px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-left { flex: 1; }
  .banner-title { font-size: 20px; font-weight: 700; color: #fff; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.8); margin-top: 4px; }
  .banner-right {
    display: flex; gap: 8px;
    .el-button { background: rgba(255,255,255,0.15); color: #fff; border-color: rgba(255,255,255,0.3); }
    .el-button:hover { background: rgba(255,255,255,0.25); }
  }
}
.con-tag { padding: 2px 8px; border-radius: 4px; font-size: 12px; }
.timeline-node { padding: 4px 0; }
.node-step-title { display: flex; align-items: center; font-size: 14px; }
.node-detail { font-size: 12px; color: #595959; margin-top: 4px; }
.payment-section { padding: 4px 0; }
.pay-row { display: flex; justify-content: space-between; align-items: center; padding: 6px 0; border-bottom: 1px solid #f5f5f5; }
.pay-label { font-size: 13px; color: #8c8c8c; }
.pay-val { font-size: 15px; font-weight: 700; }
</style>
