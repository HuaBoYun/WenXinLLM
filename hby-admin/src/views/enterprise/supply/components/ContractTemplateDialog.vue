<template>
  <el-dialog title="合同模板" :visible.sync="dialogVisible" width="750px" @close="handleClose">
    <div v-loading="loading">
      <el-alert title="合同模板基于已有合同按类型自动生成，可作为新建合同的参考" type="info" :closable="false" style="margin-bottom:16px" />
      <el-table :data="templates" border stripe size="small">
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column prop="templateName" label="模板名称" width="150" />
        <el-table-column prop="contractType" label="合同类型" width="120">
          <template slot-scope="scope">
            <el-tag size="mini">{{ scope.row.contractType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="referenceContract" label="参考合同" show-overflow-tooltip />
        <el-table-column prop="contractAmount" label="参考金额(元)" width="130" />
        <el-table-column prop="responsiblePerson" label="负责人" width="100" />
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleUseTemplate(scope.row)">使用</el-button>
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
  name: 'ContractTemplateDialog',
  props: { visible: { type: Boolean, default: false } },
  data() { return { dialogVisible: this.visible, loading: false, templates: [] } },
  watch: { visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadTemplates() } } },
  methods: {
    handleClose() { this.$emit('close') },
    handleUseTemplate(row) {
      this.$emit('use-template', row)
      this.handleClose()
    },
    async loadTemplates() {
      this.loading = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/contract/templates', method: 'get' })
        if (res && res.data) this.templates = res.data
      } catch (e) { console.error(e) } finally { this.loading = false }
    }
  }
}
</script>
