<template>
  <el-dialog
    title="数据导出"
    :visible.sync="dialogVisible"
    width="60%"
    :before-close="handleClose"
  >
    <el-form :model="form" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="导出类型">
            <el-select v-model="form.exportType" placeholder="请选择导出类型">
              <el-option label="Excel文件" value="excel"></el-option>
              <el-option label="CSV文件" value="csv"></el-option>
              <el-option label="PDF报告" value="pdf"></el-option>
              <el-option label="JSON数据" value="json"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据范围">
            <el-select v-model="form.dataRange" placeholder="请选择数据范围">
              <el-option label="全部数据" value="all"></el-option>
              <el-option label="当前页面" value="current"></el-option>
              <el-option label="选中数据" value="selected"></el-option>
              <el-option label="自定义范围" value="custom"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="form.timeRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据类型">
            <el-select v-model="form.dataTypes" multiple placeholder="请选择数据类型">
              <el-option label="财务数据" value="financial"></el-option>
              <el-option label="经营数据" value="operation"></el-option>
              <el-option label="人员数据" value="personnel"></el-option>
              <el-option label="资产数据" value="asset"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="导出字段">
        <el-transfer
          v-model="form.selectedFields"
          :data="availableFields"
          :titles="['可选字段', '导出字段']"
          :button-texts="['移除', '添加']"
          filterable
        ></el-transfer>
      </el-form-item>
      
      <el-form-item label="导出选项">
        <el-checkbox-group v-model="form.exportOptions">
          <el-checkbox label="includeHeader">包含表头</el-checkbox>
          <el-checkbox label="includeTotal">包含汇总</el-checkbox>
          <el-checkbox label="includeChart">包含图表</el-checkbox>
          <el-checkbox label="compressFile">压缩文件</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="文件名称">
        <el-input v-model="form.fileName" placeholder="请输入文件名称">
          <template slot="append">{{ getFileExtension() }}</template>
        </el-input>
      </el-form-item>
      
      <el-form-item label="导出预览">
        <el-card>
          <div slot="header">
            <span>导出信息预览</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-statistic title="预计记录数" :value="estimatedRecords">
                <template slot="prefix">
                  <i class="el-icon-document" style="color: #409EFF"></i>
                </template>
              </el-statistic>
            </el-col>
            <el-col :span="8">
              <el-statistic title="导出字段数" :value="form.selectedFields.length">
                <template slot="prefix">
                  <i class="el-icon-menu" style="color: #67C23A"></i>
                </template>
              </el-statistic>
            </el-col>
            <el-col :span="8">
              <el-statistic title="预计文件大小" :value="estimatedSize" suffix="MB">
                <template slot="prefix">
                  <i class="el-icon-folder" style="color: #E6A23C"></i>
                </template>
              </el-statistic>
            </el-col>
          </el-row>
        </el-card>
      </el-form-item>
      
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.remark" :rows="3" placeholder="请输入导出说明"></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button @click="previewExport">预览</el-button>
      <el-button type="primary" @click="startExport" :loading="exporting">
        {{ exporting ? '导出中...' : '开始导出' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { exportData } from '@/api/enterprise/data'

export default {
  name: 'DataExportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    exportData: {
      type: Object,
      default: () => ({})
    },
    enterpriseId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      exporting: false,
      form: {
        exportType: 'excel',
        dataRange: 'all',
        timeRange: ['2024-01-01', '2024-12-31'],
        dataTypes: ['financial', 'operation'],
        selectedFields: [1, 2, 3, 4, 5],
        exportOptions: ['includeHeader', 'includeTotal'],
        fileName: '企业数据导出',
        remark: ''
      },
      availableFields: [
        { key: 1, label: '企业名称' },
        { key: 2, label: '统一社会信用代码' },
        { key: 3, label: '注册资本' },
        { key: 4, label: '营业收入' },
        { key: 5, label: '净利润' },
        { key: 6, label: '总资产' },
        { key: 7, label: '净资产' },
        { key: 8, label: '员工人数' },
        { key: 9, label: '成立日期' },
        { key: 10, label: '所属行业' },
        { key: 11, label: '企业性质' },
        { key: 12, label: '经营状态' }
      ]
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    estimatedRecords() {
      // 根据数据范围和类型估算记录数
      let baseRecords = 1000
      if (this.form.dataRange === 'current') baseRecords = 50
      else if (this.form.dataRange === 'selected') baseRecords = 20
      
      return baseRecords * this.form.dataTypes.length
    },
    estimatedSize() {
      // 估算文件大小（MB）
      const recordSize = this.form.selectedFields.length * 0.1 // 每个字段约0.1KB
      const totalSize = (this.estimatedRecords * recordSize) / 1024 // 转换为MB
      return Math.round(totalSize * 100) / 100
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    getFileExtension() {
      const extensions = {
        'excel': '.xlsx',
        'csv': '.csv',
        'pdf': '.pdf',
        'json': '.json'
      }
      return extensions[this.form.exportType] || '.xlsx'
    },
    previewExport() {
      const typeLabels = { excel: 'Excel文件', csv: 'CSV文件', pdf: 'PDF报告', json: 'JSON数据' }
      const rangeLabels = { all: '全部数据', current: '当前页面', selected: '选中数据', custom: '自定义范围' }
      const summary = [
        `导出格式：${typeLabels[this.form.exportType] || this.form.exportType}`,
        `数据范围：${rangeLabels[this.form.dataRange] || this.form.dataRange}`,
        `导出字段数：${this.form.selectedFields.length} 个`,
        `预计记录数：${this.estimatedRecords} 条`,
        `预计文件大小：${this.estimatedSize} MB`,
        `文件名称：${this.form.fileName}${this.getFileExtension()}`
      ].join('\n')
      this.$alert(summary, '导出预览', {
        confirmButtonText: '确定',
        type: 'info',
        customClass: 'export-preview-alert'
      })
    },
    startExport() {
      if (!this.form.fileName) {
        this.$message.error('请输入文件名称')
        return
      }
      if (this.form.selectedFields.length === 0) {
        this.$message.error('请选择要导出的字段')
        return
      }
      this.exporting = true
      const params = { ...this.form }
      if (this.enterpriseId) {
        params.enterpriseId = this.enterpriseId
      }
      exportData(params).then(response => {
        // 兼容 axios blob 响应：response 可能是 Blob 本身，也可能是 response.data
        const blobData = response instanceof Blob ? response : (response.data || response)
        const mimeType = blobData.type || 'application/octet-stream'
        const blob = blobData instanceof Blob ? blobData : new Blob([blobData], { type: mimeType })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        // 后端统一返回CSV格式
        link.download = this.form.fileName + '.csv'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(link.href)
        this.$message.success('数据导出成功！文件已下载到本地')
        this.handleClose()
      }).catch(error => {
        const msg = (error && error.message) ? error.message : '数据导出失败，请稍后重试'
        this.$message.error(msg)
      }).finally(() => {
        this.exporting = false
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
