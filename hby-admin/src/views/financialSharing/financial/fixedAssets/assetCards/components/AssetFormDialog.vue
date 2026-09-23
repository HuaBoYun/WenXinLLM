<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="assetForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="small"
    >
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="资产编码" prop="assetCode">
                <el-input v-model="formData.assetCode" placeholder="请输入资产编码" :disabled="isView" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="资产名称" prop="assetName">
                <el-input v-model="formData.assetName" placeholder="请输入资产名称" :disabled="isView" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="资产类别" prop="categoryId">
                <el-select v-model="formData.categoryId" placeholder="请选择资产类别" style="width: 100%" :disabled="isView">
                  <el-option label="房屋建筑物" value="1001" />
                  <el-option label="机器设备" value="1002" />
                  <el-option label="运输工具" value="1003" />
                  <el-option label="电子设备" value="1004" />
                  <el-option label="办公设备" value="1005" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="资产状态" prop="status">
                <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%" :disabled="isView">
                  <el-option label="正常" value="NORMAL" />
                  <el-option label="闲置" value="IDLE" />
                  <el-option label="维修" value="MAINTENANCE" />
                  <el-option label="报废" value="SCRAPPED" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="资产原值" prop="originalValue">
                <el-input-number
                  v-model="formData.originalValue"
                  :precision="2"
                  :min="0"
                  :max="999999999.99"
                  controls-position="right"
                  style="width: 100%"
                  :disabled="isView"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="购置日期" prop="purchaseDate">
                <el-date-picker
                  v-model="formData.purchaseDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="yyyy-MM-dd"
                  style="width: 100%"
                  :disabled="isView"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="使用部门" prop="deptId">
                <el-input
                  v-model="deptName"
                  placeholder="请选择使用部门"
                  :disabled="isView"
                  readonly
                >
                  <el-button
                    slot="append"
                    icon="el-icon-search"
                    @click="handleSelectDepartment"
                    :disabled="isView"
                  />
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="责任人" prop="responsiblePerson">
                <el-input v-model="formData.responsiblePerson" placeholder="请输入责任人" :disabled="isView" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="存放地点" prop="location">
                <el-input v-model="formData.location" placeholder="请输入存放地点" :disabled="isView" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="规格型号" prop="specification">
                <el-input v-model="formData.specification" placeholder="请输入规格型号" :disabled="isView" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>

        <el-tab-pane label="折旧信息" name="depreciation">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="折旧方法" prop="depreciationMethod">
                <el-select v-model="formData.depreciationMethod" placeholder="请选择折旧方法" style="width: 100%" :disabled="isView">
                  <el-option label="平均年限法" value="STRAIGHT_LINE" />
                  <el-option label="工作量法" value="UNITS_OF_PRODUCTION" />
                  <el-option label="双倍余额递减法" value="DOUBLE_DECLINING" />
                  <el-option label="年数总和法" value="SUM_OF_YEARS" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="使用年限" prop="usefulLife">
                <el-input-number
                  v-model="formData.usefulLife"
                  :min="1"
                  :max="100"
                  controls-position="right"
                  style="width: 100%"
                  :disabled="isView"
                />
                <span style="margin-left: 8px">年</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预计净残值" prop="residualValue">
                <el-input-number
                  v-model="formData.residualValue"
                  :precision="2"
                  :min="0"
                  controls-position="right"
                  style="width: 100%"
                  :disabled="isView"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="开始折旧日期" prop="startDepreciationDate">
                <el-date-picker
                  v-model="formData.startDepreciationDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="yyyy-MM-dd"
                  style="width: 100%"
                  :disabled="isView"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ isView ? '关闭' : '取消' }}</el-button>
      <el-button v-if="!isView" type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </div>

    <!-- 部门选择器 -->
    <DepartmentTreeModal ref="departmentTree" @selected="handleDepartmentSelected" />
  </el-dialog>
</template>

<script>
import DepartmentTreeModal from '@/components/DepartmentTreeModal'

export default {
  name: 'AssetFormDialog',
  components: {
    DepartmentTreeModal
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    mode: {
      type: String,
      default: 'add' // add, edit, view
    },
    assetData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'basic',
      submitting: false,
      deptName: '', // 部门名称（用于显示）
      formData: this.getInitialFormData(),
      formRules: {
        assetCode: [{ required: true, message: '请输入资产编码', trigger: 'blur' }],
        assetName: [{ required: true, message: '请输入资产名称', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择资产类别', trigger: 'change' }],
        originalValue: [{ required: true, message: '请输入资产原值', trigger: 'blur' }],
        purchaseDate: [{ required: true, message: '请选择购置日期', trigger: 'change' }],
        deptId: [{ required: true, message: '请选择使用部门', trigger: 'change' }],
        startDepreciationDate: [{ required: true, message: '请选择开始折旧日期', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增资产',
        edit: '编辑资产',
        view: '查看资产详情'
      }
      return titleMap[this.mode] || '资产信息'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  watch: {
    visible(val) {
      if (val && this.mode !== 'add') {
        this.formData = { ...this.assetData }
        // 如果有部门名称，显示出来
        if (this.assetData.deptName) {
          this.deptName = this.assetData.deptName
        }
      }
    },
    // 监听购置日期变化，自动设置开始折旧日期
    'formData.purchaseDate'(newVal) {
      if (newVal && !this.formData.startDepreciationDate) {
        // 自动设置为购置日期的次月1日
        const date = new Date(newVal)
        date.setMonth(date.getMonth() + 1)
        date.setDate(1)
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = '01'
        this.formData.startDepreciationDate = `${year}-${month}-${day}`
      }
    }
  },
  methods: {
    getInitialFormData() {
      return {
        assetCode: '',
        assetName: '',
        categoryId: '',
        status: 'NORMAL',
        originalValue: 0,
        purchaseDate: '',
        deptId: '', // 修改为 deptId
        responsiblePerson: '',
        location: '',
        specification: '',
        depreciationMethod: 'STRAIGHT_LINE',
        usefulLife: 10,
        residualValue: 0,
        startDepreciationDate: '' // 修改为 startDepreciationDate，与后端一致
      }
    },
    handleSelectDepartment() {
      this.$refs.departmentTree.show()
    },
    handleDepartmentSelected(department) {
      if (department) {
        // 确保部门ID转换为字符串类型
        this.formData.deptId = String(department.id)
        this.deptName = department.name
        // 手动触发表单验证
        this.$refs.assetForm.validateField('deptId')
      }
    },
    handleSubmit() {
      this.$refs.assetForm.validate(valid => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$refs.assetForm.resetFields()
      this.formData = this.getInitialFormData()
      this.deptName = ''
      this.activeTab = 'basic'
      this.$emit('update:visible', false)
    }
  }
}
</script>

