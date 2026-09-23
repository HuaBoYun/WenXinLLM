<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="internalFormData"
      :rules="formRules"
      label-width="120px"
      :disabled="formType === 'view'"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="核算项编码" prop="auxiliaryCode">
            <el-input
              v-model="internalFormData.auxiliaryCode"
              placeholder="请输入核算项编码"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核算项名称" prop="auxiliaryName">
            <el-input
              v-model="internalFormData.auxiliaryName"
              placeholder="请输入核算项名称"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="核算类型" prop="auxiliaryType">
            <el-select
              v-model="internalFormData.auxiliaryType"
              placeholder="请选择核算类型"
              style="width: 100%"
              @change="handleTypeChange"
            >
              <el-option
                v-for="(name, value) in AUXILIARY_TYPE_NAME"
                :key="value"
                :label="name"
                :value="value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上级核算项" prop="parentId">
            <el-cascader
              v-model="parentPath"
              :options="auxiliaryTreeOptions"
              :props="cascaderProps"
              placeholder="请选择上级核算项"
              clearable
              filterable
              style="width: 100%"
              @change="handleParentChange"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="排序号">
            <el-input-number
              v-model="internalFormData.sortOrder"
              :min="0"
              :max="9999"
              placeholder="请输入排序号"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否启用">
            <el-radio-group v-model="internalFormData.isEnabled">
              <el-radio
                v-for="(name, value) in ENABLED_STATUS_NAME"
                :key="value"
                :label="parseInt(value)"
              >
                {{ name }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button v-if="formType !== 'view'" type="primary" @click="handleSubmit" :loading="submitLoading">
        确 定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  saveOrUpdateAuxiliaryItem,
  getAuxiliaryItemTree,
  checkAuxiliaryItemCodeExists
} from '@/api/financialSharing/system'
import {
  AUXILIARY_TYPE_NAME,
  ENABLED_STATUS_NAME,
  DEFAULT_TENANT_CONFIG
} from '../../consts'

export default {
  name: 'AuxiliaryItemForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formType: {
      type: String,
      default: 'add' // add/edit/view
    },
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    // 自定义验证规则
    const validateAuxiliaryCode = (rule, value, callback) => {
      if (!value) {
        callback(new Error('核算项编码不能为空'))
        return
      }
      
      if (!this.internalFormData.auxiliaryType) {
        callback(new Error('请先选择核算类型'))
        return
      }
      
      if (this.formType === 'add' || (this.formType === 'edit' && value !== this.formData.auxiliaryCode)) {
        checkAuxiliaryItemCodeExists(
          value,
          this.internalFormData.auxiliaryType,
          DEFAULT_TENANT_CONFIG.bookId,
          DEFAULT_TENANT_CONFIG.tenantId,
          this.formType === 'edit' ? this.formData.auxiliaryId : null
        ).then(response => {
          if (response.code === 1 && response.data) {
            callback(new Error('核算项编码已存在'))
          } else {
            callback()
          }
        }).catch(() => {
          callback()
        })
      } else {
        callback()
      }
    }

    return {
      dialogVisible: false,
      submitLoading: false,
      internalFormData: {
        auxiliaryId: null,
        auxiliaryCode: '',
        auxiliaryName: '',
        auxiliaryType: '',
        parentId: null,
        isLeaf: 1,
        isEnabled: 1,
        sortOrder: 0,
        ...DEFAULT_TENANT_CONFIG
      },
      parentPath: [],
      auxiliaryTreeOptions: [],
      cascaderProps: {
        value: 'auxiliaryId',
        label: 'auxiliaryName',
        children: 'children',
        checkStrictly: true,
        emitPath: false
      },
      formRules: {
        auxiliaryCode: [
          { required: true, message: '核算项编码不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' },
          { validator: validateAuxiliaryCode, trigger: 'blur' }
        ],
        auxiliaryName: [
          { required: true, message: '核算项名称不能为空', trigger: 'blur' },
          { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' }
        ],
        auxiliaryType: [
          { required: true, message: '核算类型不能为空', trigger: 'change' }
        ]
      },
      // 常量
      AUXILIARY_TYPE_NAME,
      ENABLED_STATUS_NAME
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增辅助核算项',
        edit: '修改辅助核算项',
        view: '查看辅助核算项'
      }
      return titleMap[this.formType] || '辅助核算项'
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val) {
          this.initForm()
          this.loadAuxiliaryTree()
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    /** 初始化表单 */
    initForm() {
      this.internalFormData = {
        auxiliaryId: null,
        auxiliaryCode: '',
        auxiliaryName: '',
        auxiliaryType: '',
        parentId: null,
        isLeaf: 1,
        isEnabled: 1,
        sortOrder: 0,
        ...DEFAULT_TENANT_CONFIG,
        ...this.formData
      }
      
      // 设置上级路径
      if (this.internalFormData.parentId) {
        this.parentPath = [this.internalFormData.parentId]
      } else {
        this.parentPath = []
      }
      
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    
    /** 加载辅助核算项树 */
    loadAuxiliaryTree() {
      const auxiliaryType = this.internalFormData.auxiliaryType || null
      getAuxiliaryItemTree(auxiliaryType, DEFAULT_TENANT_CONFIG.tenantId, DEFAULT_TENANT_CONFIG.bookId)
        .then(response => {
          if (response.code === 1) {
            this.auxiliaryTreeOptions = this.buildTree(response.data)
          }
        })
        .catch(() => {
          this.auxiliaryTreeOptions = []
        })
    },
    
    /** 构建树形结构 */
    buildTree(list) {
      const map = {}
      const roots = []
      
      // 过滤相同类型的数据
      const filteredList = list.filter(item => 
        !this.internalFormData.auxiliaryType || 
        item.auxiliaryType === this.internalFormData.auxiliaryType
      )
      
      // 创建映射
      filteredList.forEach(item => {
        map[item.auxiliaryId] = { ...item, children: [] }
      })
      
      // 构建树形结构
      filteredList.forEach(item => {
        if (item.parentId && map[item.parentId]) {
          map[item.parentId].children.push(map[item.auxiliaryId])
        } else {
          roots.push(map[item.auxiliaryId])
        }
      })
      
      return roots
    },
    
    /** 核算类型变化 */
    handleTypeChange(value) {
      // 清空上级选择
      this.parentPath = []
      this.internalFormData.parentId = null
      // 重新加载树形数据
      this.loadAuxiliaryTree()
    },
    
    /** 上级变化 */
    handleParentChange(value) {
      this.internalFormData.parentId = value || null
    },
    
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        
        this.submitLoading = true
        const submitData = { ...this.internalFormData }
        
        saveOrUpdateAuxiliaryItem(submitData)
          .then(response => {
            if (response.code === 1) {
              this.$message.success(this.formType === 'add' ? '新增成功' : '修改成功')
              this.$emit('success')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          })
          .catch(() => {
            this.$message.error('操作失败')
          })
          .finally(() => {
            this.submitLoading = false
          })
      })
    },
    
    /** 关闭对话框 */
    handleClose() {
      this.dialogVisible = false
      this.$refs.form && this.$refs.form.resetFields()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
