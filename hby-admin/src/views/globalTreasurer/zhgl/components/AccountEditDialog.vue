<template>
  <el-dialog
    :title="isEdit ? '修改账户信息' : '新增账户信息'"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="账户号码" prop="accountNumber">
            <el-input
              v-model="form.accountNumber"
              placeholder="请输入账户号码"
              :disabled="isEdit"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户名称" prop="accountName">
            <el-input
              v-model="form.accountName"
              placeholder="请输入账户名称"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="账户英文名称">
            <el-input
              v-model="form.accountNameEng"
              placeholder="请输入账户英文名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户类型" prop="accountType">
            <el-select v-model="form.accountType" placeholder="请选择账户类型" style="width: 100%">
              <el-option label="基本账户" value="BASIC" />
              <el-option label="一般账户" value="GENERAL" />
              <el-option label="专用账户" value="SPECIAL" />
              <el-option label="临时账户" value="TEMPORARY" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="银行编码" prop="bankCode">
            <el-input
              v-model="form.bankCode"
              placeholder="请输入银行编码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行名称" prop="bankName">
            <el-input
              v-model="form.bankName"
              placeholder="请输入银行名称"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="分行编码">
            <el-input
              v-model="form.branchCode"
              placeholder="请输入分行编码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分行名称">
            <el-input
              v-model="form.branchName"
              placeholder="请输入分行名称"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="币种代码" prop="currencyCode">
            <el-select v-model="form.currencyCode" placeholder="请选择币种" style="width: 100%">
              <el-option label="人民币(CNY)" value="CNY" />
              <el-option label="美元(USD)" value="USD" />
              <el-option label="欧元(EUR)" value="EUR" />
              <el-option label="日元(JPY)" value="JPY" />
              <el-option label="英镑(GBP)" value="GBP" />
              <el-option label="港币(HKD)" value="HKD" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户日期">
            <el-date-picker
              v-model="form.openDate"
              type="date"
              placeholder="选择开户日期"
              style="width: 100%"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="isEdit">
        <el-col :span="8">
          <el-form-item label="账户余额">
            <el-input-number
              v-model="form.balance"
              :precision="2"
              :min="0"
              style="width: 100%"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="可用余额">
            <el-input-number
              v-model="form.availableBalance"
              :precision="2"
              :min="0"
              style="width: 100%"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="冻结余额">
            <el-input-number
              v-model="form.frozenBalance"
              :precision="2"
              :min="0"
              style="width: 100%"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="账户状态">
            <el-select v-model="form.accountStatus" placeholder="请选择账户状态" style="width: 100%">
              <el-option label="活跃" value="ACTIVE" />
              <el-option label="非活跃" value="INACTIVE" />
              <el-option label="冻结" value="FROZEN" />
              <el-option label="关闭" value="CLOSED" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="设置选项">
            <el-checkbox v-model="isDefault">设为默认账户</el-checkbox>
            <el-checkbox v-model="isDirectConnect">启用银企直联</el-checkbox>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="isDirectConnect">
        <el-col :span="12">
          <el-form-item label="直联类型">
            <el-select v-model="form.directConnectType" placeholder="请选择直联类型" style="width: 100%">
              <el-option label="查询" value="QUERY" />
              <el-option label="支付" value="PAYMENT" />
              <el-option label="收款" value="RECEIPT" />
              <el-option label="转账" value="TRANSFER" />
              <el-option label="全部" value="ALL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleConfirm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { checkAccountNumberUnique } from '@/api/globalTreasurer/zhgl'

export default {
  name: 'AccountEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        accountId: null,
        accountNumber: '',
        accountName: '',
        accountNameEng: '',
        accountType: 'GENERAL',
        bankCode: '',
        bankName: '',
        branchCode: '',
        branchName: '',
        currencyCode: 'CNY',
        balance: 0,
        availableBalance: 0,
        frozenBalance: 0,
        accountStatus: 'ACTIVE',
        openDate: null,
        closeDate: null,
        isDefault: 0,
        isDirectConnect: 0,
        directConnectType: null,
        remark: ''
      },
      rules: {
        accountNumber: [
          { required: true, message: '账户号码不能为空', trigger: 'blur' },
          { min: 10, max: 50, message: '账户号码长度在 10 到 50 个字符', trigger: 'blur' },
          { validator: this.validateAccountNumber, trigger: 'blur' }
        ],
        accountName: [
          { required: true, message: '账户名称不能为空', trigger: 'blur' },
          { min: 2, max: 200, message: '账户名称长度在 2 到 200 个字符', trigger: 'blur' }
        ],
        accountType: [
          { required: true, message: '账户类型不能为空', trigger: 'change' }
        ],
        bankCode: [
          { required: true, message: '银行编码不能为空', trigger: 'blur' }
        ],
        bankName: [
          { required: true, message: '银行名称不能为空', trigger: 'blur' }
        ],
        currencyCode: [
          { required: true, message: '币种代码不能为空', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    isDefault: {
      get() {
        return this.form.isDefault === 1
      },
      set(val) {
        this.form.isDefault = val ? 1 : 0
      }
    },
    isDirectConnect: {
      get() {
        return this.form.isDirectConnect === 1
      },
      set(val) {
        this.form.isDirectConnect = val ? 1 : 0
        if (!val) {
          this.form.directConnectType = null
        }
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initForm()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    initForm() {
      if (this.isEdit && this.formData) {
        this.form = { ...this.formData }
      } else {
        this.resetForm()
      }
    },
    resetForm() {
      this.form = {
        accountId: null,
        accountNumber: '',
        accountName: '',
        accountNameEng: '',
        accountType: 'GENERAL',
        bankCode: '',
        bankName: '',
        branchCode: '',
        branchName: '',
        currencyCode: 'CNY',
        balance: 0,
        availableBalance: 0,
        frozenBalance: 0,
        accountStatus: 'ACTIVE',
        openDate: null,
        closeDate: null,
        isDefault: 0,
        isDirectConnect: 0,
        directConnectType: null,
        remark: ''
      }
    },
    validateAccountNumber(rule, value, callback) {
      if (!value) {
        callback()
        return
      }
      
      const orgId = this.$store.getters.orgId
      const excludeId = this.isEdit ? this.form.accountId : null
      
      checkAccountNumberUnique(value, orgId, excludeId).then(response => {
        if (response.code === 200 && response.data) {
          callback()
        } else {
          callback(new Error('账户号码已存在'))
        }
      }).catch(() => {
        callback(new Error('验证账户号码失败'))
      })
    },
    handleConfirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$emit('confirm', { ...this.form })
        }
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
