<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="科目编码" prop="subjectCode">
            <el-input
              v-model="form.subjectCode"
              placeholder="请输入科目编码"
              :disabled="mode === 'edit'"
              @blur="validateSubjectCode"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="科目名称" prop="subjectName">
            <el-input
              v-model="form.subjectName"
              placeholder="请输入科目名称"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="科目类型" prop="subjectType">
            <el-select
              v-model="form.subjectType"
              placeholder="请选择科目类型"
              style="width: 100%"
            >
              <el-option label="收入类" value="INCOME" />
              <el-option label="支出类" value="EXPENSE" />
              <el-option label="资产类" value="ASSET" />
              <el-option label="负债类" value="LIABILITY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="科目分类" prop="subjectCategory">
            <el-select
              v-model="form.subjectCategory"
              placeholder="请选择科目分类"
              style="width: 100%"
            >
              <el-option label="基础科目" value="BASIC" />
              <el-option label="明细科目" value="DETAIL" />
              <el-option label="辅助科目" value="AUXILIARY" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算体系" prop="systemId">
            <el-select
              v-model="form.systemId"
              placeholder="请选择预算体系"
              style="width: 100%"
              :disabled="mode === 'edit'"
            >
              <el-option
                v-for="system in budgetSystems"
                :key="system.systemId"
                :label="system.systemName"
                :value="system.systemId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="父科目" prop="parentSubjectId">
            <el-cascader
              v-model="parentSubjectPath"
              :options="subjectTreeOptions"
              :props="cascaderProps"
              placeholder="请选择父科目"
              style="width: 100%"
              clearable
              @change="handleParentChange"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据类型" prop="dataType">
            <el-select
              v-model="form.dataType"
              placeholder="请选择数据类型"
              style="width: 100%"
            >
              <el-option label="金额" value="AMOUNT" />
              <el-option label="数量" value="QUANTITY" />
              <el-option label="比率" value="RATIO" />
              <el-option label="文本" value="TEXT" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计算方法" prop="calculationMethod">
            <el-select
              v-model="form.calculationMethod"
              placeholder="请选择计算方法"
              style="width: 100%"
            >
              <el-option label="手工录入" value="MANUAL" />
              <el-option label="公式计算" value="FORMULA" />
              <el-option label="汇总计算" value="SUMMARY" />
              <el-option label="引用计算" value="REFERENCE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计量单位" prop="unit">
            <el-input
              v-model="form.unit"
              placeholder="请输入计量单位"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="精度位数" prop="precisionScale">
            <el-input-number
              v-model="form.precisionScale"
              :min="0"
              :max="6"
              placeholder="请输入精度位数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="排序号" prop="sortOrder">
            <el-input-number
              v-model="form.sortOrder"
              :min="1"
              placeholder="请输入排序号"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-switch
              v-model="form.isEnabled"
              active-text="启用"
              inactive-text="禁用"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="科目描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入科目描述"
        />
      </el-form-item>

      <!-- 高级配置 -->
      <el-collapse v-model="activeCollapse">
        <el-collapse-item title="高级配置" name="advanced">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="是否叶子节点">
                <el-switch
                  v-model="form.isLeaf"
                  active-text="是"
                  inactive-text="否"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="允许负数">
                <el-switch
                  v-model="allowNegative"
                  active-text="允许"
                  inactive-text="不允许"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="必填项">
                <el-switch
                  v-model="isRequired"
                  active-text="必填"
                  inactive-text="非必填"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="显示小数">
                <el-switch
                  v-model="showDecimal"
                  active-text="显示"
                  inactive-text="不显示"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="科目配置">
            <el-input
              v-model="subjectConfigText"
              type="textarea"
              :rows="4"
              placeholder="请输入JSON格式的科目配置"
            />
          </el-form-item>
        </el-collapse-item>
      </el-collapse>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
        {{ mode === 'add' ? '创建' : mode === 'edit' ? '更新' : '复制创建' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  createBudgetSubject,
  updateBudgetSubject,
  getBudgetSubjectById,
  validateSubjectCode,
  getBudgetSubjectTree
} from '@/api/managementAccountant/eps/budgetSubject'
import { getBudgetSystemPage } from '@/api/managementAccountant/eps/budgetSystem'

export default {
  name: 'BudgetSubjectForm',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitLoading: false,
      mode: 'add', // add, edit, copy
      activeCollapse: [],
      
      form: {
        subjectId: null,
        systemId: null,
        subjectCode: '',
        subjectName: '',
        subjectType: '',
        subjectCategory: '',
        parentSubjectId: null,
        dataType: 'AMOUNT',
        calculationMethod: 'MANUAL',
        unit: '',
        precisionScale: 2,
        sortOrder: 1,
        isEnabled: true,
        isLeaf: true,
        description: '',
        subjectConfig: null
      },
      
      rules: {
        subjectCode: [
          { required: true, message: '请输入科目编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        subjectName: [
          { required: true, message: '请输入科目名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        subjectType: [
          { required: true, message: '请选择科目类型', trigger: 'change' }
        ],
        systemId: [
          { required: true, message: '请选择预算体系', trigger: 'change' }
        ],
        dataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ],
        calculationMethod: [
          { required: true, message: '请选择计算方法', trigger: 'change' }
        ]
      },
      
      // 预算体系列表
      budgetSystems: [],
      // 科目树选项
      subjectTreeOptions: [],
      // 父科目路径
      parentSubjectPath: [],
      // 级联选择器配置
      cascaderProps: {
        value: 'id',
        label: 'label',
        children: 'children',
        checkStrictly: true,
        emitPath: false
      },
      
      // 高级配置
      allowNegative: false,
      isRequired: false,
      showDecimal: true,
      subjectConfigText: ''
    }
  },
  
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增科目',
        edit: '编辑科目',
        copy: '复制科目'
      }
      return titleMap[this.mode] || '科目管理'
    }
  },
  
  methods: {
    // 打开对话框
    async open(mode, data = null, parentData = null) {
      this.mode = mode
      this.dialogVisible = true
      
      // 获取基础数据
      await this.getBudgetSystems()
      await this.getSubjectTreeOptions()
      
      if (mode === 'add') {
        this.resetForm()
        if (parentData) {
          this.form.systemId = parentData.systemId
          this.form.parentSubjectId = parentData.id || parentData.subjectId
          this.parentSubjectPath = [parentData.id || parentData.subjectId]
        }
      } else if (mode === 'edit' && data) {
        await this.loadSubjectData(data.subjectId || data.id)
      } else if (mode === 'copy' && data) {
        await this.loadSubjectData(data.subjectId || data.id)
        this.form.subjectId = null
        this.form.subjectCode = data.subjectCode + '_copy'
        this.form.subjectName = data.subjectName + '_副本'
      }
    },
    
    // 获取预算体系列表
    async getBudgetSystems() {
      try {
        const response = await getBudgetSystemPage({ current: 1, size: 100 })
        if (response.code === 200) {
          this.budgetSystems = response.data.records || []
        }
      } catch (error) {
        console.error('获取预算体系失败:', error)
      }
    },
    
    // 获取科目树选项
    async getSubjectTreeOptions() {
      try {
        const response = await getBudgetSubjectTree({
          systemId: this.form.systemId,
          includeDisabled: false
        })
        if (response.code === 200) {
          this.subjectTreeOptions = response.data || []
        }
      } catch (error) {
        console.error('获取科目树失败:', error)
      }
    },
    
    // 加载科目数据
    async loadSubjectData(subjectId) {
      try {
        this.loading = true
        const response = await getBudgetSubjectById(subjectId)
        if (response.code === 200) {
          const data = response.data
          Object.keys(this.form).forEach(key => {
            if (data.hasOwnProperty(key)) {
              this.form[key] = data[key]
            }
          })
          
          // 设置父科目路径
          if (data.parentSubjectId) {
            this.parentSubjectPath = [data.parentSubjectId]
          }
          
          // 解析科目配置
          if (data.subjectConfig) {
            try {
              const config = JSON.parse(data.subjectConfig)
              this.allowNegative = config.allowNegative || false
              this.isRequired = config.isRequired || false
              this.showDecimal = config.showDecimal !== false
              this.subjectConfigText = data.subjectConfig
            } catch (e) {
              this.subjectConfigText = data.subjectConfig
            }
          }
        }
      } catch (error) {
        this.$message.error('加载科目数据失败')
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    // 验证科目编码
    async validateSubjectCode() {
      if (!this.form.subjectCode) return
      
      try {
        const response = await validateSubjectCode(
          this.form.subjectCode,
          this.form.subjectId
        )
        if (response.code !== 200) {
          this.$message.warning(response.message)
        }
      } catch (error) {
        console.error('验证科目编码失败:', error)
      }
    },
    
    // 父科目变化
    handleParentChange(value) {
      this.form.parentSubjectId = value || null
    },
    
    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        
        this.submitLoading = true
        
        // 构建科目配置
        const config = {
          allowNegative: this.allowNegative,
          isRequired: this.isRequired,
          showDecimal: this.showDecimal
        }
        
        // 如果有自定义配置文本，尝试解析
        if (this.subjectConfigText) {
          try {
            const customConfig = JSON.parse(this.subjectConfigText)
            Object.assign(config, customConfig)
          } catch (e) {
            this.$message.warning('科目配置JSON格式不正确，将使用默认配置')
          }
        }
        
        this.form.subjectConfig = JSON.stringify(config)
        
        let response
        if (this.mode === 'edit') {
          response = await updateBudgetSubject(this.form)
        } else {
          response = await createBudgetSubject(this.form)
        }
        
        if (response.code === 200) {
          this.$message.success(this.mode === 'edit' ? '更新成功' : '创建成功')
          this.handleClose()
          this.$emit('success')
        } else {
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        if (error !== false) { // 表单验证失败时返回false
          this.$message.error('操作失败')
          console.error(error)
        }
      } finally {
        this.submitLoading = false
      }
    },
    
    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    
    // 重置表单
    resetForm() {
      this.form = {
        subjectId: null,
        systemId: null,
        subjectCode: '',
        subjectName: '',
        subjectType: '',
        subjectCategory: '',
        parentSubjectId: null,
        dataType: 'AMOUNT',
        calculationMethod: 'MANUAL',
        unit: '',
        precisionScale: 2,
        sortOrder: 1,
        isEnabled: true,
        isLeaf: true,
        description: '',
        subjectConfig: null
      }
      
      this.parentSubjectPath = []
      this.allowNegative = false
      this.isRequired = false
      this.showDecimal = true
      this.subjectConfigText = ''
      this.activeCollapse = []
      
      if (this.$refs.form) {
        this.$refs.form.clearValidate()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

::v-deep .el-collapse-item__header {
  font-weight: 500;
}

::v-deep .el-form-item__label {
  font-weight: 500;
}
</style>
