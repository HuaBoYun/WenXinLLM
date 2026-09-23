<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="研发项目编码" prop="rdProjectCode">
            <el-input
              v-model="form.rdProjectCode"
              placeholder="请输入研发项目编码"
              :disabled="isEdit"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="研发项目名称" prop="rdProjectName">
            <el-input
              v-model="form.rdProjectName"
              placeholder="请输入研发项目名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="研发类型" prop="rdType">
            <el-select
              v-model="form.rdType"
              placeholder="请选择研发类型"
              style="width: 100%"
            >
              <el-option label="基础研究" value="basic_research" />
              <el-option label="应用研究" value="applied_research" />
              <el-option label="试验发展" value="experimental_development" />
              <el-option label="技术改进" value="technology_improvement" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="研发成本" prop="rdCost">
            <el-input-number
              v-model="form.rdCost"
              :precision="2"
              :step="1000"
              :min="0"
              style="width: 100%"
              placeholder="请输入研发成本"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预期收益" prop="expectedBenefit">
            <el-input-number
              v-model="form.expectedBenefit"
              :precision="2"
              :step="1000"
              :min="0"
              style="width: 100%"
              placeholder="请输入预期收益"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投资回报率">
            <el-input
              :value="getRoiRatio() + '%'"
              disabled
              placeholder="自动计算"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属部门" prop="department">
            <el-input
              v-model="form.department"
              placeholder="请输入所属部门"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成本中心" prop="costCenter">
            <el-select
              v-model="form.costCenter"
              placeholder="请选择成本中心"
              style="width: 100%"
              filterable
            >
              <el-option
                v-for="item in costCenterOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="研发开始日期" prop="rdStartDate">
            <el-date-picker
              v-model="form.rdStartDate"
              type="date"
              placeholder="选择开始日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="研发结束日期" prop="rdEndDate">
            <el-date-picker
              v-model="form.rdEndDate"
              type="date"
              placeholder="选择结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="研发描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入研发项目描述"
        />
      </el-form-item>
      
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button
        type="primary"
        :loading="buttonLoading"
        @click="save"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateRdCost, getRdCostById } from '@/api/financialSharing/specialCost'
import { getCostCenterOptions } from '@/api/financialSharing/costCenter'

export default {
  name: 'RdCostEdit',
  data() {
    return {
      dialogVisible: false,
      buttonLoading: false,
      isEdit: false,
      title: '',
      form: {
        id: null,
        rdProjectCode: '',
        rdProjectName: '',
        rdType: '',
        rdCost: 0,
        expectedBenefit: 0,
        department: '',
        costCenter: '',
        rdStartDate: '',
        rdEndDate: '',
        description: '',
        remark: ''
      },
      rules: {
        rdProjectCode: [
          { required: true, message: '请输入研发项目编码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        rdProjectName: [
          { required: true, message: '请输入研发项目名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        rdType: [
          { required: true, message: '请选择研发类型', trigger: 'change' }
        ],
        rdCost: [
          { required: true, message: '请输入研发成本', trigger: 'blur' },
          { type: 'number', min: 0, message: '研发成本必须大于等于0', trigger: 'blur' }
        ],
        rdStartDate: [
          { required: true, message: '请选择研发开始日期', trigger: 'change' }
        ],
        rdEndDate: [
          { required: true, message: '请选择研发结束日期', trigger: 'change' },
          { validator: this.validateEndDate, trigger: 'change' }
        ]
      },
      costCenterOptions: []
    }
  },
  created() {
    this.loadCostCenterOptions()
  },
  methods: {
    async showEdit(row) {
      this.dialogVisible = true
      this.isEdit = !!row
      this.title = row ? '编辑研发项目' : '新增研发项目'
      
      if (row) {
        this.buttonLoading = true
        const { code, data } = await getRdCostById(row.id)
        if (code === 200) {
          this.form = { ...data }
        }
        this.buttonLoading = false
      } else {
        this.form = {
          id: null,
          rdProjectCode: '',
          rdProjectName: '',
          rdType: '',
          rdCost: 0,
          expectedBenefit: 0,
          department: '',
          costCenter: '',
          rdStartDate: '',
          rdEndDate: '',
          description: '',
          remark: ''
        }
      }
      
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    close() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    async save() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.buttonLoading = true
          
          const { code, msg } = await saveOrUpdateRdCost(this.form)
          if (code === 200) {
            this.$baseMessage(msg, 'success')
            this.$emit('fetch-data')
            this.close()
          } else {
            this.$baseMessage(msg, 'error')
          }
          this.buttonLoading = false
        }
      })
    },
    async loadCostCenterOptions() {
      try {
        const { code, data } = await getCostCenterOptions()
        if (code === 200) {
          this.costCenterOptions = data.map(item => ({
            label: item.centerName,
            value: item.centerId
          }))
        }
      } catch (error) {
        console.error('加载成本中心选项失败:', error)
      }
    },
    getRoiRatio() {
      if (!this.form.rdCost || this.form.rdCost === 0) return '0.00'
      return (((this.form.expectedBenefit || 0) - this.form.rdCost) / this.form.rdCost * 100).toFixed(2)
    },
    validateEndDate(rule, value, callback) {
      if (value && this.form.rdStartDate) {
        if (new Date(value) <= new Date(this.form.rdStartDate)) {
          callback(new Error('结束日期必须晚于开始日期'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
