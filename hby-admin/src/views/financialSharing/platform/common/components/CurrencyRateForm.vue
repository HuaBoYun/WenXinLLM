<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="mode === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="币种编码" prop="currencyCode">
            <el-input
              v-model="form.currencyCode"
              placeholder="请输入币种编码"
              maxlength="10"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种名称" prop="currencyName">
            <el-input
              v-model="form.currencyName"
              placeholder="请输入币种名称"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="汇率类型" prop="rateType">
            <el-select
              v-model="form.rateType"
              placeholder="请选择汇率类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in RATE_TYPE_OPTIONS"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="汇率日期" prop="rateDate">
            <el-date-picker
              v-model="form.rateDate"
              type="date"
              placeholder="请选择汇率日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="汇率" prop="exchangeRate">
            <el-input-number
              v-model="form.exchangeRate"
              placeholder="请输入汇率"
              :precision="6"
              :min="0.000001"
              :max="999999.999999"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否本位币" prop="isBaseCurrency">
            <el-radio-group v-model="form.isBaseCurrency">
              <el-radio
                v-for="item in IS_BASE_CURRENCY_OPTIONS"
                :key="item.value"
                :label="item.value"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否启用" prop="isEnabled">
            <el-radio-group v-model="form.isEnabled">
              <el-radio
                v-for="item in ENABLED_STATUS_OPTIONS"
                :key="item.value"
                :label="item.value"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="mode !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="submitLoading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getCurrencyRateById,
  saveOrUpdateCurrencyRate,
  checkCurrencyAndDateExists
} from '@/api/financialSharing/system'
import {
  RATE_TYPE_OPTIONS,
  IS_BASE_CURRENCY_OPTIONS,
  ENABLED_STATUS_OPTIONS,
  DEFAULT_TENANT_CONFIG
} from '../../consts'

export default {
  name: 'CurrencyRateForm',
  data() {
    // 自定义验证规则
    const validateCurrencyAndDate = (rule, value, callback) => {
      if (!value || !this.form.currencyCode) {
        callback()
        return
      }
      
      // 检查币种和日期是否重复
      checkCurrencyAndDateExists({
        currencyCode: this.form.currencyCode,
        rateDate: value,
        bookId: this.form.bookId,
        tenantId: this.form.tenantId,
        excludeId: this.form.rateId
      }).then(response => {
        if (response.code === 1 && response.data === true) {
          callback(new Error('该币种在此日期的汇率已存在'))
        } else {
          callback()
        }
      }).catch(() => {
        callback()
      })
    }
    
    return {
      // 常量
      RATE_TYPE_OPTIONS,
      IS_BASE_CURRENCY_OPTIONS,
      ENABLED_STATUS_OPTIONS,
      
      // 弹窗状态
      dialogVisible: false,
      mode: 'add', // add, edit, view
      submitLoading: false,
      
      // 表单数据
      form: {
        rateId: null,
        currencyCode: '',
        currencyName: '',
        rateType: null,
        exchangeRate: null,
        rateDate: '',
        isBaseCurrency: 0,
        isEnabled: 1,
        version: null,
        ...DEFAULT_TENANT_CONFIG
      },
      
      // 验证规则
      rules: {
        currencyCode: [
          { required: true, message: '请输入币种编码', trigger: 'blur' },
          { max: 10, message: '币种编码长度不能超过10个字符', trigger: 'blur' }
        ],
        currencyName: [
          { required: true, message: '请输入币种名称', trigger: 'blur' },
          { max: 50, message: '币种名称长度不能超过50个字符', trigger: 'blur' }
        ],
        rateType: [
          { required: true, message: '请选择汇率类型', trigger: 'change' }
        ],
        exchangeRate: [
          { required: true, message: '请输入汇率', trigger: 'blur' },
          { type: 'number', min: 0.000001, message: '汇率必须大于0', trigger: 'blur' }
        ],
        rateDate: [
          { required: true, message: '请选择汇率日期', trigger: 'change' },
          { validator: validateCurrencyAndDate, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增币种汇率',
        edit: '编辑币种汇率',
        view: '查看币种汇率'
      }
      return titleMap[this.mode] || '币种汇率'
    }
  },
  methods: {
    // 打开弹窗
    open(mode, rateId = null) {
      this.mode = mode
      this.dialogVisible = true
      
      if (rateId) {
        this.loadData(rateId)
      } else {
        this.resetForm()
      }
    },
    
    // 加载数据
    async loadData(rateId) {
      try {
        const response = await getCurrencyRateById(rateId)
        if (response.code === 1 && response.data) {
          this.form = {
            ...this.form,
            ...response.data
          }
        } else {
          this.$message.error(response.msg || '获取数据失败')
          this.handleClose()
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('获取数据失败')
        this.handleClose()
      }
    },
    
    // 重置表单
    resetForm() {
      this.form = {
        rateId: null,
        currencyCode: '',
        currencyName: '',
        rateType: null,
        exchangeRate: null,
        rateDate: '',
        isBaseCurrency: 0,
        isEnabled: 1,
        version: null,
        ...DEFAULT_TENANT_CONFIG
      }
      
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) {
          return
        }
        
        this.submitLoading = true
        try {
          const response = await saveOrUpdateCurrencyRate(this.form)
          if (response.code === 1) {
            this.$message.success(`${this.mode === 'add' ? '新增' : '更新'}成功`)
            this.handleClose()
            this.$emit('success')
          } else {
            this.$message.error(response.msg || `${this.mode === 'add' ? '新增' : '更新'}失败`)
          }
        } catch (error) {
          console.error('提交失败:', error)
          this.$message.error(`${this.mode === 'add' ? '新增' : '更新'}失败`)
        } finally {
          this.submitLoading = false
        }
      })
    },
    
    // 关闭弹窗
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
