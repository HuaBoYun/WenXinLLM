<template>
  <el-dialog title="质量标准" :visible.sync="dialogVisible" width="750px" @close="handleClose">
    <div v-loading="loading">
      <el-alert title="质量标准基于各检验类型的历史数据自动生成，标准合格率为95%" type="info" :closable="false" style="margin-bottom:16px" />
      <el-table :data="standards" border stripe size="small">
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column prop="inspectionType" label="检验类型" width="120">
          <template slot-scope="scope">
            <el-tag size="mini">{{ scope.row.inspectionType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalInspections" label="检验次数" width="90" />
        <el-table-column prop="avgQualificationRate" label="实际合格率(%)" width="130">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.isPass ? '#67C23A' : '#F56C6C', fontWeight: 'bold' }">{{ scope.row.avgQualificationRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="standardRate" label="标准合格率(%)" width="130" />
        <el-table-column label="达标状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isPass ? 'success' : 'danger'" size="mini">{{ scope.row.isPass ? '达标' : '未达标' }}</el-tag>
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
  name: 'QualityStandardDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, standards: [] } },
  watch: { visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadStandards() } } },
  methods: {
    handleClose() { this.$emit('close') },
    async loadStandards() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/quality/standards', method: 'get' })
        if (res && res.data) this.standards = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
