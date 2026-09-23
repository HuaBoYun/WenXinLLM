<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="800px"
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
          <el-form-item label="项目编码" prop="projectCode">
            <el-input
              v-model="form.projectCode"
              placeholder="请输入项目编码"
              :disabled="isEdit"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="form.projectName"
              placeholder="请输入项目名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目类型" prop="projectType">
            <el-select
              v-model="form.projectType"
              placeholder="请选择项目类型"
              style="width: 100%"
            >
              <el-option label="研发项目" value="1" />
              <el-option label="建设项目" value="2" />
              <el-option label="投资项目" value="3" />
              <el-option label="其他项目" value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目状态" prop="status">
            <el-select
              v-model="form.status"
              placeholder="请选择项目状态"
              style="width: 100%"
            >
              <el-option label="立项中" value="1" />
              <el-option label="进行中" value="2" />
              <el-option label="已完成" value="3" />
              <el-option label="已暂停" value="4" />
              <el-option label="已取消" value="5" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算金额" prop="budgetAmount">
            <el-input-number
              v-model="form.budgetAmount"
              :precision="2"
              :step="1000"
              :min="0"
              style="width: 100%"
              placeholder="请输入预算金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际成本" prop="actualAmount">
            <el-input-number
              v-model="form.actualAmount"
              :precision="2"
              :step="1000"
              :min="0"
              style="width: 100%"
              placeholder="请输入实际成本"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker
              v-model="form.startDate"
              type="date"
              placeholder="选择开始日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker
              v-model="form.endDate"
              type="date"
              placeholder="选择结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目经理" prop="projectManager">
            <el-input
              v-model="form.projectManager"
              placeholder="请输入项目经理"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目进度" prop="progress">
            <el-slider
              v-model="form.progress"
              :min="0"
              :max="100"
              :step="5"
              show-input
              style="margin-right: 20px"
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
      
      <el-form-item label="项目描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入项目描述"
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
import { saveOrUpdateProjectCost, getProjectCostById } from '@/api/financialSharing/specialCost'
import { getCostCenterOptions } from '@/api/financialSharing/costCenter'

export default {
  name: 'ProjectCostEdit',
  data() {
    return {
      dialogVisible: false,
      buttonLoading: false,
      isEdit: false,
      title: '',
      form: {
        id: null,
        projectCode: '',
        projectName: '',
        projectType: '',
        status: '1',
        budgetAmount: 0,
        actualAmount: 0,
        startDate: '',
        endDate: '',
        projectManager: '',
        progress: 0,
        department: '',
        costCenter: '',
        description: '',
        remark: ''
      },
      rules: {
        projectCode: [
          { required: true, message: '请输入项目编码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        projectType: [
          { required: true, message: '请选择项目类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择项目状态', trigger: 'change' }
        ],
        budgetAmount: [
          { required: true, message: '请输入预算金额', trigger: 'blur' },
          { type: 'number', min: 0, message: '预算金额必须大于等于0', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ],
        projectManager: [
          { required: true, message: '请输入项目经理', trigger: 'blur' }
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
      this.title = row ? '编辑项目成本' : '新增项目成本'
      
      if (row) {
        this.buttonLoading = true
        const { code, data } = await getProjectCostById(row.id)
        if (code === 200) {
          this.form = { ...data }
        }
        this.buttonLoading = false
      } else {
        this.form = {
          id: null,
          projectCode: '',
          projectName: '',
          projectType: '',
          status: '1',
          budgetAmount: 0,
          actualAmount: 0,
          startDate: '',
          endDate: '',
          projectManager: '',
          progress: 0,
          department: '',
          costCenter: '',
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
          
          // 计算预算差异
          this.form.variance = this.form.actualAmount - this.form.budgetAmount
          
          const { code, msg } = await saveOrUpdateProjectCost(this.form)
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
