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
          <el-form-item label="科目编码" prop="subjectCode">
            <el-input
              v-model="internalFormData.subjectCode"
              placeholder="请输入科目编码"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="科目名称" prop="subjectName">
            <el-input
              v-model="internalFormData.subjectName"
              placeholder="请输入科目名称"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="上级科目" prop="parentSubjectId">
            <el-cascader
              ref="parentSubjectCascader"
              :key="cascaderKey"
              v-model="parentSubjectPath"
              :options="subjectTreeOptions"
              :props="cascaderProps"
              placeholder="请选择上级科目"
              clearable
              filterable
              style="width: 100%"
              @change="handleParentSubjectChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="科目类型" prop="subjectType">
            <el-select
              v-model="internalFormData.subjectType"
              placeholder="请选择科目类型"
              style="width: 100%"
            >
              <el-option
                v-for="(name, value) in SUBJECT_TYPE_NAME"
                :key="value"
                :label="name"
                :value="parseInt(value)"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="余额方向" prop="balanceDirection">
            <el-radio-group v-model="internalFormData.balanceDirection">
              <el-radio
                v-for="(name, value) in BALANCE_DIRECTION_NAME"
                :key="value"
                :label="parseInt(value)"
              >
                {{ name }}
              </el-radio>
            </el-radio-group>
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

      <el-row>
        <el-col :span="12">
          <el-form-item label="现金科目">
            <el-radio-group v-model="internalFormData.isCash">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行科目">
            <el-radio-group v-model="internalFormData.isBank">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="辅助核算类型">
            <el-input
              v-model="internalFormData.auxiliaryTypes"
              type="textarea"
              :rows="3"
              placeholder="请输入辅助核算类型，多个用逗号分隔"
              maxlength="500"
              show-word-limit
            />
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
  saveOrUpdateAccountSubject,
  getAccountSubjectTree,
  checkAccountSubjectCodeExists
} from '@/api/financialSharing/system'
import {
  SUBJECT_TYPE_NAME,
  BALANCE_DIRECTION_NAME,
  ENABLED_STATUS_NAME,
  DEFAULT_TENANT_CONFIG
} from '../../consts'

export default {
  name: 'AccountSubjectForm',
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
    const validateSubjectCode = (rule, value, callback) => {
      if (!value) {
        callback(new Error('科目编码不能为空'))
        return
      }
      
      if (this.formType === 'add' || (this.formType === 'edit' && value !== this.formData.subjectCode)) {
        checkAccountSubjectCodeExists(
          value,
          DEFAULT_TENANT_CONFIG.bookId,
          DEFAULT_TENANT_CONFIG.tenantId,
          this.formType === 'edit' ? this.formData.subjectId : null
        ).then(response => {
          if (response.code === 1 && response.data) {
            callback(new Error('科目编码已存在'))
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
        subjectId: null,
        subjectCode: '',
        subjectName: '',
        parentSubjectId: null,
        subjectType: null,
        balanceDirection: null,
        isEnabled: 1,
        isCash: 0,
        isBank: 0,
        auxiliaryTypes: '',
        ...DEFAULT_TENANT_CONFIG
      },
      parentSubjectPath: [],
      subjectTreeOptions: [],
      cascaderKey: Date.now(), // 用于强制刷新cascader组件
      cascaderProps: {
        value: 'id',
        label: 'name',
        children: 'children',
        checkStrictly: true,
        emitPath: false
      },
      formRules: {
        subjectCode: [
          { required: true, message: '科目编码不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' },
          { validator: validateSubjectCode, trigger: 'blur' }
        ],
        subjectName: [
          { required: true, message: '科目名称不能为空', trigger: 'blur' },
          { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' }
        ],
        subjectType: [
          { required: true, message: '科目类型不能为空', trigger: 'change' }
        ],
        balanceDirection: [
          { required: true, message: '余额方向不能为空', trigger: 'change' }
        ]
      },
      // 常量
      SUBJECT_TYPE_NAME,
      BALANCE_DIRECTION_NAME,
      ENABLED_STATUS_NAME
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增会计科目',
        edit: '修改会计科目',
        view: '查看会计科目'
      }
      return titleMap[this.formType] || '会计科目'
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val) {
          this.initForm()
          this.loadSubjectTree()
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
        subjectId: null,
        subjectCode: '',
        subjectName: '',
        parentSubjectId: null,
        subjectType: null,
        balanceDirection: null,
        isEnabled: 1,
        isCash: 0,
        isBank: 0,
        auxiliaryTypes: '',
        ...DEFAULT_TENANT_CONFIG,
        ...this.formData
      }

      // parentSubjectPath 将在科目树加载完成后设置，确保正确的时序

      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    
    /** 加载科目树 */
    loadSubjectTree() {
      getAccountSubjectTree(DEFAULT_TENANT_CONFIG.tenantId, DEFAULT_TENANT_CONFIG.bookId)
        .then(response => {
          console.log('接口返回数据:', response)
          console.log('response.code:', response.code)
          console.log('response.code === 1:', response.code === 1)

          if (response.code === 1) {
            // axios响应拦截器返回的直接是后端的data，所以response就是后端返回的数据
            // 后端返回的数据结构是 {code: 1, msg: "操作成功", data: [...], result: null}
            this.subjectTreeOptions = response.data || []
            console.log('科目树数据:', this.subjectTreeOptions)

            // 科目树加载完成后，设置上级科目的选中状态
            this.setParentSubjectValue()
          } else {
            this.subjectTreeOptions = []
            console.error('接口返回错误:', response.msg)
          }
        })
        .catch(error => {
          console.error('加载科目树异常:', error)
          this.subjectTreeOptions = []
        })
    },

    /** 设置上级科目选中值 */
    setParentSubjectValue() {
      this.$nextTick(() => {
        if (this.internalFormData.parentSubjectId) {
          console.log('设置上级科目ID:', this.internalFormData.parentSubjectId)
          console.log('parentSubjectId类型:', typeof this.internalFormData.parentSubjectId)
          // 确保ID是字符串类型，与后端返回的数据类型一致
          const parentId = String(this.internalFormData.parentSubjectId)
          console.log('转换后的parentSubjectId:', parentId, '类型:', typeof parentId)

          this.parentSubjectPath = parentId

          // 强制更新cascader组件
          this.$nextTick(() => {
            console.log('parentSubjectPath设置后:', this.parentSubjectPath)
            // 强制刷新cascader组件
            this.cascaderKey = Date.now()
            this.$nextTick(() => {
              console.log('cascaderKey更新后:', this.cascaderKey)
              // 手动触发cascader的选中状态更新
              if (this.$refs.parentSubjectCascader) {
                this.$refs.parentSubjectCascader.$forceUpdate()
              }
            })
          })
        } else {
          console.log('清空上级科目选择')
          this.parentSubjectPath = null
        }
      })
    },
    
    /** 构建树形结构 */
    buildTree(list) {
      const map = {}
      const roots = []
      
      // 创建映射
      list.forEach(item => {
        map[item.subjectId] = { ...item, children: [] }
      })
      
      // 构建树形结构
      list.forEach(item => {
        if (item.parentSubjectId && map[item.parentSubjectId]) {
          map[item.parentSubjectId].children.push(map[item.subjectId])
        } else {
          roots.push(map[item.subjectId])
        }
      })
      
      return roots
    },
    
    /** 上级科目变化 */
    handleParentSubjectChange(value) {
      this.internalFormData.parentSubjectId = value || null
    },
    
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        
        this.submitLoading = true
        const submitData = { ...this.internalFormData }
        
        saveOrUpdateAccountSubject(submitData)
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
