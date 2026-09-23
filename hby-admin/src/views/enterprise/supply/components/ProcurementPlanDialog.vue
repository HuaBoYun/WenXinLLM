<template>
  <el-dialog title="采购计划" :visible.sync="dialogVisible" width="900px" @close="handleClose">
    <div v-loading="loading">
      <el-alert title="以下为待审批、已审批、采购中的采购单，按预计到货日期排序" type="info" :closable="false" style="margin-bottom:16px" />
      <el-table :data="planList" border stripe size="small">
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column prop="procurementNo" label="采购单号" width="140" />
        <el-table-column prop="procurementName" label="采购名称" width="180" show-overflow-tooltip />
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="procurementType" label="类型" width="110">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getTypeColor(scope.row.procurementType)">{{ scope.row.procurementType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="金额(元)" width="120" />
        <el-table-column prop="expectedDate" label="预计到货" width="110">
          <template slot-scope="scope">
            <span :class="isOverdue(scope.row.expectedDate) ? 'overdue' : ''">{{ scope.row.expectedDate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicant" label="申请人" width="90" />
      </el-table>
      <div style="margin-top:12px;color:#909399;font-size:13px">共 {{ planList.length }} 条待处理采购计划</div>
    </div>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'ProcurementPlanDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, planList: [] } },
  watch: {
    visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadPlan() } }
  },
  methods: {
    handleClose() { this.$emit('close') },
    getTypeColor(type) { return { '设备采购': 'primary', '服务采购': 'success', '原材料采购': 'warning', '办公用品采购': 'info' }[type] || 'info' },
    getStatusType(s) { return { '待审批': 'warning', '已审批': 'primary', '采购中': 'info' }[s] || 'info' },
    isOverdue(date) {
      if (!date) return false
      return new Date(date) < new Date()
    },
    async loadPlan() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/procurement/plan', method: 'get' })
        if (res && res.data) this.planList = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
<style scoped>
.overdue { color: #F56C6C; font-weight: bold; }
</style>
