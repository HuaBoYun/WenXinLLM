<template>
  <el-dialog
    title="汇率换算"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="convertForm"
      :rules="rules"
      :model="convertData"
      label-position="left"
      label-width="120px"
      style="width: 500px; margin-left:50px;"
    >
      <el-form-item label="原始金额" prop="amount">
        <el-input-number
          v-model="convertData.amount"
          :precision="2"
          :min="0"
          :max="999999999"
          placeholder="请输入原始金额"
          style="width: 100%"
        />
      </el-form-item>
      
      <el-form-item label="原始币种" prop="baseCurrency">
        <el-select
          v-model="convertData.baseCurrency"
          placeholder="请选择原始币种"
          style="width: 100%"
          @change="handleCurrencyChange"
        >
          <el-option label="人民币(CNY)" value="CNY" />
          <el-option label="美元(USD)" value="USD" />
          <el-option label="欧元(EUR)" value="EUR" />
          <el-option label="日元(JPY)" value="JPY" />
          <el-option label="英镑(GBP)" value="GBP" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="目标币种" prop="targetCurrency">
        <el-select
          v-model="convertData.targetCurrency"
          placeholder="请选择目标币种"
          style="width: 100%"
          @change="handleCurrencyChange"
        >
          <el-option label="人民币(CNY)" value="CNY" />
          <el-option label="美元(USD)" value="USD" />
          <el-option label="欧元(EUR)" value="EUR" />
          <el-option label="日元(JPY)" value="JPY" />
          <el-option label="英镑(GBP)" value="GBP" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="汇率日期" prop="rateDate">
        <el-date-picker
          v-model="convertData.rateDate"
          type="date"
          placeholder="请选择汇率日期"
          style="width: 100%"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          @change="handleDateChange"
        />
      </el-form-item>
      
      <el-form-item label="汇率类型" prop="rateType">
        <el-select
          v-model="convertData.rateType"
          placeholder="请选择汇率类型"
          style="width: 100%"
          @change="handleRateTypeChange"
        >
          <el-option label="即期汇率" value="SPOT" />
          <el-option label="远期汇率" value="FORWARD" />
          <el-option label="中间价" value="MIDDLE" />
        </el-select>
      </el-form-item>
      
      <!-- 汇率信息显示 -->
      <el-form-item label="当前汇率">
        <el-input
          v-model="currentRate"
          readonly
          placeholder="请先选择币种和日期"
        >
          <template slot="append">
            <el-button @click="refreshRate" :loading="rateLoading">
              <i class="el-icon-refresh"></i>
            </el-button>
          </template>
        </el-input>
      </el-form-item>
      
      <!-- 换算结果显示 -->
      <el-form-item label="换算结果">
        <el-input
          v-model="convertResult"
          readonly
          placeholder="换算结果将在这里显示"
        >
          <template slot="prepend">{{ convertData.targetCurrency }}</template>
        </el-input>
      </el-form-item>
      
      <!-- 换算说明 -->
      <el-form-item v-if="convertResult">
        <el-alert
          :title="convertDescription"
          type="info"
          :closable="false"
          show-icon
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        关闭
      </el-button>
      <el-button type="primary" @click="handleConvert" :loading="convertLoading">
        换算
      </el-button>
      <el-button type="success" @click="handleClear">
        清空
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getLatestExchangeRate, convertCurrency } from '@/api/globalTreasurer/czgg'

export default {
  name: 'CurrencyConvertDialog',
  data() {
    return {
      dialogVisible: false,
      convertData: {
        amount: null,
        baseCurrency: '',
        targetCurrency: '',
        rateDate: '',
        rateType: 'SPOT',
        orgId: null
      },
      currentRate: '',
      convertResult: '',
      convertDescription: '',
      rateLoading: false,
      convertLoading: false,
      rules: {
        amount: [
          { required: true, message: '请输入原始金额', trigger: 'blur' }
        ],
        baseCurrency: [
          { required: true, message: '请选择原始币种', trigger: 'change' }
        ],
        targetCurrency: [
          { required: true, message: '请选择目标币种', trigger: 'change' }
        ],
        rateDate: [
          { required: true, message: '请选择汇率日期', trigger: 'change' }
        ],
        rateType: [
          { required: true, message: '请选择汇率类型', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
      this.resetData()
      
      // 设置默认日期为今天
      const today = new Date()
      this.convertData.rateDate = this.formatDate(today)
      this.convertData.orgId = this.$store.getters.orgId
      
      this.$nextTick(() => {
        this.$refs['convertForm'].clearValidate()
      })
    },
    
    resetData() {
      this.convertData = {
        amount: null,
        baseCurrency: '',
        targetCurrency: '',
        rateDate: '',
        rateType: 'SPOT',
        orgId: null
      }
      this.currentRate = ''
      this.convertResult = ''
      this.convertDescription = ''
    },
    
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    
    handleCurrencyChange() {
      this.currentRate = ''
      this.convertResult = ''
      this.convertDescription = ''
      if (this.convertData.baseCurrency && this.convertData.targetCurrency && this.convertData.rateDate) {
        this.refreshRate()
      }
    },
    
    handleDateChange() {
      this.currentRate = ''
      this.convertResult = ''
      this.convertDescription = ''
      if (this.convertData.baseCurrency && this.convertData.targetCurrency && this.convertData.rateDate) {
        this.refreshRate()
      }
    },
    
    handleRateTypeChange() {
      this.currentRate = ''
      this.convertResult = ''
      this.convertDescription = ''
      if (this.convertData.baseCurrency && this.convertData.targetCurrency && this.convertData.rateDate) {
        this.refreshRate()
      }
    },
    
    refreshRate() {
      if (!this.convertData.baseCurrency || !this.convertData.targetCurrency || !this.convertData.rateDate) {
        return
      }
      
      if (this.convertData.baseCurrency === this.convertData.targetCurrency) {
        this.currentRate = '1.000000'
        return
      }
      
      this.rateLoading = true
      getLatestExchangeRate({
        baseCurrency: this.convertData.baseCurrency,
        targetCurrency: this.convertData.targetCurrency,
        rateType: this.convertData.rateType,
        orgId: this.convertData.orgId
      }).then(response => {
        this.rateLoading = false
        if (response.success && response.data) {
          this.currentRate = response.data.rateValue
        } else {
          this.currentRate = '未找到汇率'
          this.$message.warning('未找到对应的汇率数据')
        }
      }).catch(() => {
        this.rateLoading = false
        this.currentRate = '获取失败'
      })
    },
    
    handleConvert() {
      this.$refs['convertForm'].validate((valid) => {
        if (valid) {
          if (this.convertData.baseCurrency === this.convertData.targetCurrency) {
            this.convertResult = this.convertData.amount.toFixed(2)
            this.convertDescription = `${this.convertData.amount} ${this.convertData.baseCurrency} = ${this.convertResult} ${this.convertData.targetCurrency} (相同币种)`
            return
          }
          
          if (!this.currentRate || this.currentRate === '未找到汇率' || this.currentRate === '获取失败') {
            this.$message.error('请先获取有效的汇率')
            return
          }
          
          this.convertLoading = true
          convertCurrency({
            amount: this.convertData.amount,
            baseCurrency: this.convertData.baseCurrency,
            targetCurrency: this.convertData.targetCurrency,
            rateDate: this.convertData.rateDate,
            rateType: this.convertData.rateType,
            orgId: this.convertData.orgId
          }).then(response => {
            this.convertLoading = false
            if (response.success) {
              this.convertResult = response.data.toFixed(2)
              this.convertDescription = `${this.convertData.amount} ${this.convertData.baseCurrency} × ${this.currentRate} = ${this.convertResult} ${this.convertData.targetCurrency}`
            } else {
              this.$message.error(response.message || '换算失败')
            }
          }).catch(() => {
            this.convertLoading = false
            this.$message.error('换算失败')
          })
        }
      })
    },
    
    handleClear() {
      this.resetData()
      const today = new Date()
      this.convertData.rateDate = this.formatDate(today)
      this.convertData.orgId = this.$store.getters.orgId
      this.$refs['convertForm'].clearValidate()
    },
    
    handleClose() {
      this.resetData()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
