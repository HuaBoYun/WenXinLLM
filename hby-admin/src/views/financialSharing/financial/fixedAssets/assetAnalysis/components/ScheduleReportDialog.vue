<template>
  <el-dialog
    title="定时报表配置"
    :visible.sync="visible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="scheduleForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="small"
    >
      <el-form-item label="报表名称" prop="reportName">
        <el-input v-model="formData.reportName" placeholder="请输入报表名称" />
      </el-form-item>

      <el-form-item label="报表类型" prop="reportType">
        <el-select v-model="formData.reportType" placeholder="请选择报表类型" style="width: 100%">
          <el-option label="资产清单报表" value="ASSET_LIST" />
          <el-option label="折旧明细报表" value="DEPRECIATION_DETAIL" />
          <el-option label="资产变动报表" value="ASSET_CHANGE" />
          <el-option label="资产利用率报表" value="UTILIZATION" />
          <el-option label="资产价值分析报表" value="VALUE_ANALYSIS" />
          <el-option label="综合分析报表" value="COMPREHENSIVE" />
        </el-select>
      </el-form-item>

      <el-form-item label="生成频率" prop="frequency">
        <el-select v-model="formData.frequency" placeholder="请选择生成频率" style="width: 100%">
          <el-option label="每日" value="DAILY" />
          <el-option label="每周" value="WEEKLY" />
          <el-option label="每月" value="MONTHLY" />
          <el-option label="每季度" value="QUARTERLY" />
          <el-option label="每年" value="YEARLY" />
        </el-select>
      </el-form-item>

      <el-form-item label="生成时间" prop="generateTime">
        <el-time-picker
          v-model="formData.generateTime"
          placeholder="选择生成时间"
          format="HH:mm"
          value-format="HH:mm"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="开始日期" prop="startDate">
        <el-date-picker
          v-model="formData.startDate"
          type="date"
          placeholder="选择开始日期"
          value-format="yyyy-MM-dd"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="结束日期" prop="endDate">
        <el-date-picker
          v-model="formData.endDate"
          type="date"
          placeholder="选择结束日期（可选）"
          value-format="yyyy-MM-dd"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="资产类别" prop="categoryIds">
        <el-select v-model="formData.categoryIds" multiple placeholder="请选择资产类别" style="width: 100%">
          <el-option label="全部" value="ALL" />
          <el-option label="房屋建筑物" value="1001" />
          <el-option label="机器设备" value="1002" />
          <el-option label="运输工具" value="1003" />
          <el-option label="电子设备" value="1004" />
          <el-option label="办公设备" value="1005" />
        </el-select>
      </el-form-item>

      <el-form-item label="使用部门" prop="departmentIds">
        <el-select v-model="formData.departmentIds" multiple placeholder="请选择使用部门" style="width: 100%">
          <el-option label="全部" value="ALL" />
          <el-option label="生产部" value="D001" />
          <el-option label="研发部" value="D002" />
          <el-option label="销售部" value="D003" />
          <el-option label="行政部" value="D004" />
          <el-option label="财务部" value="D005" />
        </el-select>
      </el-form-item>

      <el-form-item label="导出格式" prop="exportFormat">
        <el-checkbox-group v-model="formData.exportFormat">
          <el-checkbox label="EXCEL">Excel</el-checkbox>
          <el-checkbox label="PDF">PDF</el-checkbox>
          <el-checkbox label="CSV">CSV</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="接收人邮箱" prop="recipients">
        <el-select
          v-model="formData.recipients"
          multiple
          filterable
          allow-create
          placeholder="请输入邮箱地址，按回车添加"
          style="width: 100%"
        >
          <el-option label="zhangsan@company.com" value="zhangsan@company.com" />
          <el-option label="lisi@company.com" value="lisi@company.com" />
          <el-option label="wangwu@company.com" value="wangwu@company.com" />
        </el-select>
      </el-form-item>

      <el-form-item label="是否启用" prop="enabled">
        <el-switch v-model="formData.enabled" />
      </el-form-item>

      <el-form-item label="备注">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ScheduleReportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    scheduleData: {
      type: Object,
      default: () => null
    }
  },
  data() {
    return {
      submitting: false,
      formData: {
        reportName: '',
        reportType: '',
        frequency: '',
        generateTime: '',
        startDate: '',
        endDate: '',
        categoryIds: [],
        departmentIds: [],
        exportFormat: ['EXCEL'],
        recipients: [],
        enabled: true,
        remark: ''
      },
      formRules: {
        reportName: [{ required: true, message: '请输入报表名称', trigger: 'blur' }],
        reportType: [{ required: true, message: '请选择报表类型', trigger: 'change' }],
        frequency: [{ required: true, message: '请选择生成频率', trigger: 'change' }],
        generateTime: [{ required: true, message: '请选择生成时间', trigger: 'change' }],
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        recipients: [{ required: true, message: '请添加接收人邮箱', trigger: 'change' }]
      }
    }
  },
  watch: {
    visible(val) {
      if (val && this.scheduleData) {
        this.formData = { ...this.scheduleData }
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.scheduleForm.validate(valid => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$refs.scheduleForm.resetFields()
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.el-form {
  max-height: 500px;
  overflow-y: auto;
  padding-right: 10px;
}
</style>

