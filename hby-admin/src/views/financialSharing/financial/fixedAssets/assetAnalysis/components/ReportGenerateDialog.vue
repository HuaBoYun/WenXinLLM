<template>
  <el-dialog
    title="生成报表"
    :visible.sync="visible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="reportForm"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      size="small"
    >
      <el-form-item label="报表类型" prop="reportType">
        <el-select v-model="formData.reportType" placeholder="请选择报表类型" style="width: 100%">
          <el-option label="资产清单报表" value="ASSET_LIST" />
          <el-option label="折旧明细报表" value="DEPRECIATION_DETAIL" />
          <el-option label="资产变动报表" value="ASSET_CHANGE" />
          <el-option label="资产利用率报表" value="UTILIZATION" />
          <el-option label="资产价值分析报表" value="VALUE_ANALYSIS" />
          <el-option label="资产处置报表" value="DISPOSAL" />
        </el-select>
      </el-form-item>
      <el-form-item label="统计期间" prop="dateRange">
        <el-date-picker
          v-model="formData.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="资产类别" prop="categoryIds">
        <el-select v-model="formData.categoryIds" multiple placeholder="请选择资产类别" style="width: 100%">
          <el-option label="房屋建筑物" value="1001" />
          <el-option label="机器设备" value="1002" />
          <el-option label="运输工具" value="1003" />
          <el-option label="电子设备" value="1004" />
          <el-option label="办公设备" value="1005" />
        </el-select>
      </el-form-item>
      <el-form-item label="使用部门" prop="departmentIds">
        <el-select v-model="formData.departmentIds" multiple placeholder="请选择使用部门" style="width: 100%">
          <el-option label="生产部" value="D001" />
          <el-option label="研发部" value="D002" />
          <el-option label="销售部" value="D003" />
          <el-option label="行政部" value="D004" />
          <el-option label="财务部" value="D005" />
        </el-select>
      </el-form-item>
      <el-form-item label="导出格式" prop="exportFormat">
        <el-radio-group v-model="formData.exportFormat">
          <el-radio label="EXCEL">Excel</el-radio>
          <el-radio label="PDF">PDF</el-radio>
          <el-radio label="CSV">CSV</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleGenerate" :loading="generating">生成</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ReportGenerateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      generating: false,
      formData: {
        reportType: '',
        dateRange: [],
        categoryIds: [],
        departmentIds: [],
        exportFormat: 'EXCEL'
      },
      formRules: {
        reportType: [{ required: true, message: '请选择报表类型', trigger: 'change' }],
        dateRange: [{ required: true, message: '请选择统计期间', trigger: 'change' }]
      }
    }
  },
  methods: {
    handleGenerate() {
      this.$refs.reportForm.validate(valid => {
        if (valid) {
          this.$emit('generate', this.formData)
        }
      })
    },
    handleClose() {
      this.$refs.reportForm.resetFields()
      this.$emit('update:visible', false)
    }
  }
}
</script>

