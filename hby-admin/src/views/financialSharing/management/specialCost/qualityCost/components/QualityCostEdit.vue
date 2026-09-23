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
          <el-form-item label="质量成本编码" prop="qualityCode">
            <el-input
              v-model="form.qualityCode"
              placeholder="请输入质量成本编码"
              :disabled="isEdit"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="质量成本名称" prop="qualityName">
            <el-input
              v-model="form.qualityName"
              placeholder="请输入质量成本名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="质量成本类型" prop="qualityType">
            <el-select
              v-model="form.qualityType"
              placeholder="请选择质量成本类型"
              style="width: 100%"
            >
              <el-option label="预防成本" value="prevention" />
              <el-option label="鉴定成本" value="appraisal" />
              <el-option label="内部失败成本" value="internal_failure" />
              <el-option label="外部失败成本" value="external_failure" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成本金额" prop="costAmount">
            <el-input-number
              v-model="form.costAmount"
              :precision="2"
              :step="1000"
              :min="0"
              style="width: 100%"
              placeholder="请输入成本金额"
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
          <el-form-item label="发生日期" prop="occurDate">
            <el-date-picker
              v-model="form.occurDate"
              type="date"
              placeholder="选择发生日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="质量成本描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入质量成本描述"
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
import { saveOrUpdateQualityCost, getQualityCostById } from '@/api/financialSharing/specialCost'
import { getCostCenterOptions } from '@/api/financialSharing/costCenter'

export default {
  name: 'QualityCostEdit',
  data() {
    return {
      dialogVisible: false,
      buttonLoading: false,
      isEdit: false,
      title: '',
      form: {
        id: null,
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        costAmount: 0,
        department: '',
        costCenter: '',
        occurDate: '',
        description: '',
        remark: ''
      },
      rules: {
        qualityCode: [
          { required: true, message: '请输入质量成本编码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        qualityName: [
          { required: true, message: '请输入质量成本名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        qualityType: [
          { required: true, message: '请选择质量成本类型', trigger: 'change' }
        ],
        costAmount: [
          { required: true, message: '请输入成本金额', trigger: 'blur' },
          { type: 'number', min: 0, message: '成本金额必须大于等于0', trigger: 'blur' }
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
      this.title = row ? '编辑质量成本' : '新增质量成本'
      
      if (row) {
        this.buttonLoading = true
        const { code, data } = await getQualityCostById(row.id)
        if (code === 200) {
          this.form = { ...data }
        }
        this.buttonLoading = false
      } else {
        this.form = {
          id: null,
          qualityCode: '',
          qualityName: '',
          qualityType: '',
          costAmount: 0,
          department: '',
          costCenter: '',
          occurDate: '',
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
          
          const { code, msg } = await saveOrUpdateQualityCost(this.form)
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
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
