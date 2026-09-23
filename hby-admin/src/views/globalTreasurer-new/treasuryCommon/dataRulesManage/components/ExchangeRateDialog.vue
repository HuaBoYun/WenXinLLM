<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="exchangeRateForm"
      :model="exchangeRateForm"
      :rules="rules"
      label-width="120px"
      class="exchange-rate-form"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="基准币种" prop="baseCurrency">
            <el-select
              v-model="exchangeRateForm.baseCurrency"
              placeholder="请选择基准币种"
              style="width: 100%"
              @change="handleCurrencyChange"
            >
              <el-option label="人民币 (CNY)" value="CNY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  人民币 (CNY)
                </span>
              </el-option>
              <el-option label="美元 (USD)" value="USD">
                <span class="currency-option">
                  <span class="currency-flag">$</span>
                  美元 (USD)
                </span>
              </el-option>
              <el-option label="欧元 (EUR)" value="EUR">
                <span class="currency-option">
                  <span class="currency-flag">€</span>
                  欧元 (EUR)
                </span>
              </el-option>
              <el-option label="日元 (JPY)" value="JPY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  日元 (JPY)
                </span>
              </el-option>
              <el-option label="英镑 (GBP)" value="GBP">
                <span class="currency-option">
                  <span class="currency-flag">£</span>
                  英镑 (GBP)
                </span>
              </el-option>
              <el-option label="港币 (HKD)" value="HKD">
                <span class="currency-option">
                  <span class="currency-flag">HK$</span>
                  港币 (HKD)
                </span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标币种" prop="targetCurrency">
            <el-select
              v-model="exchangeRateForm.targetCurrency"
              placeholder="请选择目标币种"
              style="width: 100%"
              @change="handleCurrencyChange"
            >
              <el-option label="人民币 (CNY)" value="CNY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  人民币 (CNY)
                </span>
              </el-option>
              <el-option label="美元 (USD)" value="USD">
                <span class="currency-option">
                  <span class="currency-flag">$</span>
                  美元 (USD)
                </span>
              </el-option>
              <el-option label="欧元 (EUR)" value="EUR">
                <span class="currency-option">
                  <span class="currency-flag">€</span>
                  欧元 (EUR)
                </span>
              </el-option>
              <el-option label="日元 (JPY)" value="JPY">
                <span class="currency-option">
                  <span class="currency-flag">¥</span>
                  日元 (JPY)
                </span>
              </el-option>
              <el-option label="英镑 (GBP)" value="GBP">
                <span class="currency-option">
                  <span class="currency-flag">£</span>
                  英镑 (GBP)
                </span>
              </el-option>
              <el-option label="港币 (HKD)" value="HKD">
                <span class="currency-option">
                  <span class="currency-flag">HK$</span>
                  港币 (HKD)
                </span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="汇率" prop="exchangeRate">
            <el-input-number
              v-model="exchangeRateForm.exchangeRate"
              :precision="6"
              :step="0.000001"
              :min="0"
              style="width: 100%"
              placeholder="请输入汇率"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="汇率类型" prop="rateType">
            <el-select
              v-model="exchangeRateForm.rateType"
              placeholder="请选择汇率类型"
              style="width: 100%"
            >
              <el-option label="现汇" value="SPOT" />
              <el-option label="现钞" value="CASH" />
              <el-option label="中间价" value="MIDDLE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="生效日期" prop="effectiveDate">
            <el-date-picker
              v-model="exchangeRateForm.effectiveDate"
              type="date"
              placeholder="选择生效日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失效日期">
            <el-date-picker
              v-model="exchangeRateForm.expireDate"
              type="date"
              placeholder="选择失效日期（可选）"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据来源" prop="dataSource">
            <el-select
              v-model="exchangeRateForm.dataSource"
              placeholder="请选择数据来源"
              style="width: 100%"
            >
              <el-option label="手工录入" value="MANUAL" />
              <el-option label="自动获取" value="AUTO" />
              <el-option label="API接口" value="API" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态">
            <el-radio-group v-model="exchangeRateForm.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注">
        <el-input
          v-model="exchangeRateForm.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>

      <!-- 货币对信息显示 -->
      <el-card class="info-card" v-if="exchangeRateForm.baseCurrency && exchangeRateForm.targetCurrency">
        <div slot="header">
          <span>货币对信息</span>
        </div>
        <div class="pair-info">
          <div class="pair-display">
            <span class="base-currency">{{ exchangeRateForm.baseCurrency }}</span>
            <i class="el-icon-right"></i>
            <span class="target-currency">{{ exchangeRateForm.targetCurrency }}</span>
          </div>
          <div class="rate-preview" v-if="exchangeRateForm.exchangeRate">
            <span class="preview-text">1 {{ exchangeRateForm.baseCurrency }} = {{ exchangeRateForm.exchangeRate.toFixed(6) }} {{ exchangeRateForm.targetCurrency }}</span>
          </div>
        </div>
      </el-card>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSave" :loading="saving">
        {{ dialogStatus === 'create' ? '创 建' : '更 新' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ExchangeRateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    exchangeRateData: {
      type: Object,
      default: () => ({})
    },
    status: {
      type: String,
      default: 'create'
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      dialogStatus: this.status,
      saving: false,
      exchangeRateForm: {
        id: null,
        baseCurrency: '',
        targetCurrency: '',
        exchangeRate: null,
        rateType: 'MIDDLE',
        effectiveDate: '',
        expireDate: '',
        dataSource: 'MANUAL',
        status: 1,
        remark: '',
        createTime: '',
        updateTime: ''
      },
      rules: {
        baseCurrency: [
          { required: true, message: '请选择基准币种', trigger: 'change' }
        ],
        targetCurrency: [
          { required: true, message: '请选择目标币种', trigger: 'change' }
        ],
        exchangeRate: [
          { required: true, message: '请输入汇率', trigger: 'blur' },
          { type: 'number', min: 0, message: '汇率必须大于0', trigger: 'blur' }
        ],
        rateType: [
          { required: true, message: '请选择汇率类型', trigger: 'change' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据来源', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.dialogStatus === 'create' ? '新增汇率配置' : '编辑汇率配置'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initForm()
      }
    },
    exchangeRateData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.exchangeRateForm = { ...this.exchangeRateForm, ...newVal }
        }
      },
      deep: true,
      immediate: true
    },
    status(newVal) {
      this.dialogStatus = newVal
    }
  },
  methods: {
    initForm() {
      if (this.dialogStatus === 'create') {
        this.exchangeRateForm = {
          id: null,
          baseCurrency: '',
          targetCurrency: '',
          exchangeRate: null,
          rateType: 'MIDDLE',
          effectiveDate: '',
          expireDate: '',
          dataSource: 'MANUAL',
          status: 1,
          remark: '',
          createTime: '',
          updateTime: ''
        }
      }
      this.$nextTick(() => {
        this.$refs.exchangeRateForm && this.$refs.exchangeRateForm.clearValidate()
      })
    },
    handleCurrencyChange() {
      // 如果选择了相同的币种，清空目标币种
      if (this.exchangeRateForm.baseCurrency === this.exchangeRateForm.targetCurrency) {
        this.$message.warning('基准币种和目标币种不能相同')
        this.exchangeRateForm.targetCurrency = ''
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.$emit('close')
    },
    handleSave() {
      this.$refs.exchangeRateForm.validate(async (valid) => {
        if (valid) {
          // 检查币种是否相同
          if (this.exchangeRateForm.baseCurrency === this.exchangeRateForm.targetCurrency) {
            this.$message.error('基准币种和目标币种不能相同')
            return
          }

          // 检查日期逻辑
          if (this.exchangeRateForm.expireDate &&
              new Date(this.exchangeRateForm.expireDate) <= new Date(this.exchangeRateForm.effectiveDate)) {
            this.$message.error('失效日期必须晚于生效日期')
            return
          }

          this.saving = true
          try {
            // 模拟API调用
            await this.simulateApiCall()

            this.$message({
              type: 'success',
              message: this.dialogStatus === 'create' ? '创建成功' : '更新成功'
            })

            this.$emit('success', this.exchangeRateForm)
            this.handleClose()
          } catch (error) {
            this.$message({
              type: 'error',
              message: error.message || '操作失败'
            })
          } finally {
            this.saving = false
          }
        }
      })
    },
    simulateApiCall() {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          // 模拟90%的成功率
          if (Math.random() > 0.1) {
            resolve()
          } else {
            reject(new Error('网络异常，请重试'))
          }
        }, 1000)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.exchange-rate-form {
  .currency-option {
    display: flex;
    align-items: center;

    .currency-flag {
      margin-right: 8px;
      font-weight: bold;
      color: #409eff;
    }
  }

  .info-card {
    margin-top: 20px;

    .pair-info {
      text-align: center;

      .pair-display {
        margin-bottom: 15px;
        font-size: 18px;
        font-weight: 600;

        .base-currency {
          color: #409eff;
        }

        .target-currency {
          color: #67c23a;
        }

        i {
          margin: 0 10px;
          color: #909399;
        }
      }

      .rate-preview {
        .preview-text {
          font-size: 16px;
          color: #e6a23c;
          font-weight: 500;
        }
      }
    }
  }

  .el-form-item {
    margin-bottom: 18px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>