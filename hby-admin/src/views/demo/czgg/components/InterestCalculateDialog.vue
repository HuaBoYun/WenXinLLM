<template>
  <el-dialog
    title="利息计算"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="calculateForm"
      :rules="rules"
      :model="calculateData"
      label-position="left"
      label-width="120px"
      style="width: 600px; margin-left:50px;"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="本金金额" prop="principal">
            <el-input-number
              v-model="calculateData.principal"
              :precision="2"
              :min="0"
              :max="999999999"
              placeholder="请输入本金金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="currencyCode">
            <el-select
              v-model="calculateData.currencyCode"
              placeholder="请选择币种"
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
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="利率类型" prop="rateType">
            <el-select
              v-model="calculateData.rateType"
              placeholder="请选择利率类型"
              style="width: 100%"
              @change="handleRateTypeChange"
            >
              <el-option label="存款利率" value="DEPOSIT" />
              <el-option label="贷款利率" value="LOAN" />
              <el-option label="同业拆借" value="INTERBANK" />
              <el-option label="央行基准" value="BENCHMARK" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="期限类型" prop="termType">
            <el-select
              v-model="calculateData.termType"
              placeholder="请选择期限类型"
              style="width: 100%"
              @change="handleTermTypeChange"
            >
              <el-option label="活期" value="DEMAND" />
              <el-option label="1个月" value="1M" />
              <el-option label="3个月" value="3M" />
              <el-option label="6个月" value="6M" />
              <el-option label="1年" value="1Y" />
              <el-option label="3年" value="3Y" />
              <el-option label="5年" value="5Y" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker
              v-model="calculateData.startDate"
              type="date"
              placeholder="请选择开始日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              @change="handleDateChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker
              v-model="calculateData.endDate"
              type="date"
              placeholder="请选择结束日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              @change="handleDateChange"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计息方式" prop="interestMethod">
            <el-select
              v-model="calculateData.interestMethod"
              placeholder="请选择计息方式"
              style="width: 100%"
            >
              <el-option label="单利" value="SIMPLE" />
              <el-option label="复利" value="COMPOUND" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计息基础" prop="dayCountBasis">
            <el-select
              v-model="calculateData.dayCountBasis"
              placeholder="请选择计息基础"
              style="width: 100%"
            >
              <el-option label="ACT/360" value="ACT_360" />
              <el-option label="ACT/365" value="ACT_365" />
              <el-option label="30/360" value="30_360" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <!-- 利率信息显示 -->
      <el-form-item label="当前利率(%)">
        <el-input
          v-model="currentRate"
          readonly
          placeholder="请先选择利率参数"
        >
          <template slot="append">
            <el-button @click="refreshRate" :loading="rateLoading">
              <i class="el-icon-refresh"></i>
            </el-button>
          </template>
        </el-input>
      </el-form-item>
      
      <!-- 计算天数显示 -->
      <el-form-item label="计息天数">
        <el-input
          v-model="calculateDays"
          readonly
          placeholder="计息天数将自动计算"
        />
      </el-form-item>
      
      <!-- 计算结果显示 -->
      <el-form-item label="利息金额">
        <el-input
          v-model="interestAmount"
          readonly
          placeholder="利息金额将在这里显示"
        >
          <template slot="prepend">{{ calculateData.currencyCode }}</template>
        </el-input>
      </el-form-item>
      
      <el-form-item label="本息合计">
        <el-input
          v-model="totalAmount"
          readonly
          placeholder="本息合计将在这里显示"
        >
          <template slot="prepend">{{ calculateData.currencyCode }}</template>
        </el-input>
      </el-form-item>
      
      <!-- 计算说明 -->
      <el-form-item v-if="calculateDescription">
        <el-alert
          :title="calculateDescription"
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
      <el-button type="primary" @click="handleCalculate" :loading="calculating">
        计算
      </el-button>
      <el-button type="success" @click="handleClear">
        清空
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getLatestInterestRate, calculateInterest } from '@/api/globalTreasurer/czgg'

export default {
  name: 'InterestCalculateDialog',
  data() {
    return {
      dialogVisible: false,
      calculateData: {
        principal: null,
        currencyCode: '',
        rateType: '',
        termType: '',
        startDate: '',
        endDate: '',
        interestMethod: 'SIMPLE',
        dayCountBasis: 'ACT_365',
        orgId: null
      },
      currentRate: '',
      calculateDays: '',
      interestAmount: '',
      totalAmount: '',
      calculateDescription: '',
      rateLoading: false,
      calculating: false,
      rules: {
        principal: [
          { required: true, message: '请输入本金金额', trigger: 'blur' }
        ],
        currencyCode: [
          { required: true, message: '请选择币种', trigger: 'change' }
        ],
        rateType: [
          { required: true, message: '请选择利率类型', trigger: 'change' }
        ],
        termType: [
          { required: true, message: '请选择期限类型', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ],
        interestMethod: [
          { required: true, message: '请选择计息方式', trigger: 'change' }
        ],
        dayCountBasis: [
          { required: true, message: '请选择计息基础', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
      this.resetData()
      
      // 设置默认日期
      const today = new Date()
      this.calculateData.startDate = this.formatDate(today)
      const nextMonth = new Date(today.getTime() + 30 * 24 * 60 * 60 * 1000)
      this.calculateData.endDate = this.formatDate(nextMonth)
      this.calculateData.orgId = this.$store.getters.orgId
      
      this.$nextTick(() => {
        this.$refs['calculateForm'].clearValidate()
      })
    },
    
    resetData() {
      this.calculateData = {
        principal: null,
        currencyCode: '',
        rateType: '',
        termType: '',
        startDate: '',
        endDate: '',
        interestMethod: 'SIMPLE',
        dayCountBasis: 'ACT_365',
        orgId: null
      }
      this.currentRate = ''
      this.calculateDays = ''
      this.interestAmount = ''
      this.totalAmount = ''
      this.calculateDescription = ''
    },
    
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    
    handleCurrencyChange() {
      this.clearResults()
      this.refreshRateIfReady()
    },
    
    handleRateTypeChange() {
      this.clearResults()
      this.refreshRateIfReady()
    },
    
    handleTermTypeChange() {
      this.clearResults()
      this.refreshRateIfReady()
    },
    
    handleDateChange() {
      this.clearResults()
      this.calculateDaysCount()
    },
    
    clearResults() {
      this.currentRate = ''
      this.calculateDays = ''
      this.interestAmount = ''
      this.totalAmount = ''
      this.calculateDescription = ''
    },
    
    refreshRateIfReady() {
      if (this.calculateData.currencyCode && this.calculateData.rateType && this.calculateData.termType) {
        this.refreshRate()
      }
    },
    
    calculateDaysCount() {
      if (this.calculateData.startDate && this.calculateData.endDate) {
        const startDate = new Date(this.calculateData.startDate)
        const endDate = new Date(this.calculateData.endDate)
        
        if (endDate <= startDate) {
          this.calculateDays = ''
          return
        }
        
        const timeDiff = endDate.getTime() - startDate.getTime()
        const daysDiff = Math.ceil(timeDiff / (1000 * 3600 * 24))
        this.calculateDays = daysDiff.toString()
      }
    },
    
    refreshRate() {
      if (!this.calculateData.currencyCode || !this.calculateData.rateType || !this.calculateData.termType) {
        return
      }
      
      this.rateLoading = true
      getLatestInterestRate({
        currencyCode: this.calculateData.currencyCode,
        rateType: this.calculateData.rateType,
        termType: this.calculateData.termType,
        orgId: this.calculateData.orgId
      }).then(response => {
        this.rateLoading = false
        if (response.success && response.data) {
          this.currentRate = response.data.rateValue
        } else {
          this.currentRate = '未找到利率'
          this.$message.warning('未找到对应的利率数据')
        }
      }).catch(() => {
        this.rateLoading = false
        this.currentRate = '获取失败'
      })
    },
    
    handleCalculate() {
      this.$refs['calculateForm'].validate((valid) => {
        if (valid) {
          // 验证日期
          if (new Date(this.calculateData.endDate) <= new Date(this.calculateData.startDate)) {
            this.$message.error('结束日期必须大于开始日期')
            return
          }
          
          if (!this.currentRate || this.currentRate === '未找到利率' || this.currentRate === '获取失败') {
            this.$message.error('请先获取有效的利率')
            return
          }
          
          this.calculating = true
          calculateInterest({
            principal: this.calculateData.principal,
            currencyCode: this.calculateData.currencyCode,
            rateType: this.calculateData.rateType,
            termType: this.calculateData.termType,
            startDate: this.calculateData.startDate,
            endDate: this.calculateData.endDate,
            interestMethod: this.calculateData.interestMethod,
            dayCountBasis: this.calculateData.dayCountBasis,
            orgId: this.calculateData.orgId
          }).then(response => {
            this.calculating = false
            if (response.success) {
              this.interestAmount = response.data.interestAmount.toFixed(2)
              this.totalAmount = response.data.totalAmount.toFixed(2)
              this.calculateDescription = `本金：${this.calculateData.principal} ${this.calculateData.currencyCode}，利率：${this.currentRate}%，天数：${this.calculateDays}天，${this.calculateData.interestMethod === 'SIMPLE' ? '单利' : '复利'}计息`
            } else {
              this.$message.error(response.message || '计算失败')
            }
          }).catch(() => {
            this.calculating = false
            this.$message.error('计算失败')
          })
        }
      })
    },
    
    handleClear() {
      this.resetData()
      const today = new Date()
      this.calculateData.startDate = this.formatDate(today)
      const nextMonth = new Date(today.getTime() + 30 * 24 * 60 * 60 * 1000)
      this.calculateData.endDate = this.formatDate(nextMonth)
      this.calculateData.orgId = this.$store.getters.orgId
      this.$refs['calculateForm'].clearValidate()
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
