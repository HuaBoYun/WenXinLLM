<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <!-- 查看模式 -->
    <div v-if="dialogType === 'view'" class="view-mode">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="企业名称">{{ form.companyName }}</el-descriptions-item>
        <el-descriptions-item label="统计期间">{{ form.period }}</el-descriptions-item>
        <el-descriptions-item label="营业总收入(万元)">{{ form.totalRevenue | numberFormat }}</el-descriptions-item>
        <el-descriptions-item label="净利润(万元)">{{ form.netProfit | numberFormat }}</el-descriptions-item>
        <el-descriptions-item label="总资产(万元)">{{ form.totalAssets | numberFormat }}</el-descriptions-item>
        <el-descriptions-item label="贡献率(%)">{{ form.contribution }}</el-descriptions-item>
        <el-descriptions-item label="增长率(%)">{{ form.growthRate }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- 编辑/新增模式 -->
    <el-form
      v-else
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="companyName">
            <el-input v-model="form.companyName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="统计期间" prop="period">
            <el-input v-model="form.period" placeholder="如：2025-Y" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="营业总收入" prop="totalRevenue">
            <el-input-number
              v-model="form.totalRevenue"
              :min="0"
              :precision="2"
              placeholder="万元"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="净利润" prop="netProfit">
            <el-input-number
              v-model="form.netProfit"
              :precision="2"
              placeholder="万元"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="总资产" prop="totalAssets">
            <el-input-number
              v-model="form.totalAssets"
              :min="0"
              :precision="2"
              placeholder="万元"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="贡献率" prop="contribution">
            <el-input-number
              v-model="form.contribution"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="%"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="增长率" prop="growthRate">
            <el-input-number
              v-model="form.growthRate"
              :precision="2"
              placeholder="%"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PerformanceConsolidationDialog',
  filters: {
    numberFormat(val) {
      if (val === null || val === undefined) return '--'
      return Number(val).toLocaleString()
    }
  },
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) },
    dialogType: { type: String, default: 'add' }
  },
  data() {
    return {
      form: {
        companyName: '',
        period: '',
        totalRevenue: null,
        netProfit: null,
        totalAssets: null,
        contribution: null,
        growthRate: null
      },
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        period: [{ required: true, message: '请输入统计期间', trigger: 'blur' }],
        totalRevenue: [{ required: true, message: '请输入营业总收入', trigger: 'blur' }],
        netProfit: [{ required: true, message: '请输入净利润', trigger: 'blur' }],
        totalAssets: [{ required: true, message: '请输入总资产', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    dialogTitle() {
      const map = { add: '新增绩效数据', edit: '编辑绩效数据', view: '查看绩效数据' }
      return map[this.dialogType] || '绩效合并分析'
    }
  },
  watch: {
    visible(val) {
      if (val && this.data) {
        this.form = { ...this.form, ...this.data }
      }
      if (!val) {
        this.resetForm()
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    resetForm() {
      this.form = {
        companyName: '',
        period: '',
        totalRevenue: null,
        netProfit: null,
        totalAssets: null,
        contribution: null,
        growthRate: null
      }
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })
    },
    handleSubmit() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$emit('refresh', { ...this.form })
          this.$message.success('操作成功')
          this.handleClose()
        }
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.view-mode { padding: 10px 0; }
</style>