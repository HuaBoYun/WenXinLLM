<template>
  <el-dialog title="库存预警" :visible.sync="dialogVisible" width="900px" @close="handleClose">
    <div v-loading="loading">
      <el-alert :title="`共 ${alertList.length} 条库存异常记录需要关注`" type="warning" :closable="false" style="margin-bottom:16px" />
      <el-table :data="alertList" border stripe size="small">
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column prop="materialCode" label="物料编码" width="120" />
        <el-table-column prop="materialName" label="物料名称" width="160" show-overflow-tooltip />
        <el-table-column prop="category" label="分类" width="90" />
        <el-table-column prop="currentStock" label="当前库存" width="90">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.currentStock <= 0 ? '#F56C6C' : '#E6A23C', fontWeight: 'bold' }">{{ scope.row.currentStock }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="最低库存" width="90" />
        <el-table-column prop="maxStock" label="最高库存" width="90" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="warehouse" label="仓库" width="100" />
        <el-table-column prop="supplierName" label="供应商" width="140" show-overflow-tooltip />
        <el-table-column prop="stockStatus" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getStatusType(scope.row.stockStatus)">{{ scope.row.stockStatus }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'InventoryAlertDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, alertList: [] } },
  watch: { visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadAlert() } } },
  methods: {
    handleClose() { this.$emit('close') },
    getStatusType(s) { return { '缺货': 'danger', '偏低': 'warning', '过高': 'primary' }[s] || 'info' },
    async loadAlert() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/inventory/alert', method: 'get' })
        if (res && res.data) this.alertList = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
