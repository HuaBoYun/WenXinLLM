<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="dataForm"
      :rules="rules"
      :model="temp"
      label-position="left"
      label-width="120px"
      style="width: 600px; margin-left:50px;"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="基础币种" prop="baseCurrency">
            <el-select
              v-model="temp.baseCurrency"
              placeholder="请选择基础币种"
              style="width: 100%"
            >
              <el-option label="人民币(CNY)" value="CNY" />
              <el-option label="美元(USD)" value="USD" />
              <el-option label="欧元(EUR)" value="EUR" />
              <el-option label="日元(JPY)" value="JPY" />
              <el-option label="英镑(GBP)" value="GBP" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标币种" prop="targetCurrency">
            <el-select
              v-model="temp.targetCurrency"
              placeholder="请选择目标币种"
              style="width: 100%"
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
          <el-form-item label="汇率值" prop="rateValue">
            <el-input-number
              v-model="temp.rateValue"
              :precision="6"
              :min="0"
              :max="999999"
              placeholder="请输入汇率值"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="汇率类型" prop="rateType">
            <el-select
              v-model="temp.rateType"
              placeholder="请选择汇率类型"
              style="width: 100%"
            >
              <el-option label="即期汇率" value="SPOT" />
              <el-option label="远期汇率" value="FORWARD" />
              <el-option label="中间价" value="MIDDLE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="汇率日期" prop="rateDate">
            <el-date-picker
              v-model="temp.rateDate"
              type="date"
              placeholder="请选择汇率日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据来源" prop="dataSource">
            <el-select
              v-model="temp.dataSource"
              placeholder="请选择数据来源"
              style="width: 100%"
            >
              <el-option label="手工录入" value="MANUAL" />
              <el-option label="系统获取" value="SYSTEM" />
              <el-option label="银行接口" value="BANK" />
              <el-option label="外部接口" value="EXTERNAL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="生效时间" prop="effectiveTime">
            <el-date-picker
              v-model="temp.effectiveTime"
              type="datetime"
              placeholder="请选择生效时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失效时间" prop="expiryTime">
            <el-date-picker
              v-model="temp.expiryTime"
              type="datetime"
              placeholder="请选择失效时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="状态" prop="isEnabled">
        <el-radio-group v-model="temp.isEnabled">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="handleSave">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateExchangeRate } from '@/api/globalTreasurer/czgg'

export default {
  name: 'ExchangeRateEditDialog',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      temp: {
        rateId: null,
        baseCurrency: '',
        targetCurrency: '',
        rateValue: null,
        rateType: 'SPOT',
        rateDate: '',
        dataSource: 'MANUAL',
        effectiveTime: '',
        expiryTime: '',
        isEnabled: 1,
        orgId: null
      },
      rules: {
        baseCurrency: [
          { required: true, message: '请选择基础币种', trigger: 'change' }
        ],
        targetCurrency: [
          { required: true, message: '请选择目标币种', trigger: 'change' }
        ],
        rateValue: [
          { required: true, message: '请输入汇率值', trigger: 'blur' }
        ],
        rateType: [
          { required: true, message: '请选择汇率类型', trigger: 'change' }
        ],
        rateDate: [
          { required: true, message: '请选择汇率日期', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据来源', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    show(row) {
      this.dialogVisible = true
      this.resetTemp()
      
      if (row) {
        this.dialogTitle = '编辑汇率'
        this.temp = Object.assign({}, row)
        // 处理时间格式
        if (this.temp.effectiveTime) {
          this.temp.effectiveTime = this.temp.effectiveTime.replace('T', ' ').substring(0, 19)
        }
        if (this.temp.expiryTime) {
          this.temp.expiryTime = this.temp.expiryTime.replace('T', ' ').substring(0, 19)
        }
      } else {
        this.dialogTitle = '新增汇率'
        this.temp.orgId = this.$store.getters.orgId
        // 设置默认生效时间为当前时间
        const now = new Date()
        this.temp.effectiveTime = this.formatDateTime(now)
        this.temp.rateDate = this.formatDate(now)
      }
      
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    
    resetTemp() {
      this.temp = {
        rateId: null,
        baseCurrency: '',
        targetCurrency: '',
        rateValue: null,
        rateType: 'SPOT',
        rateDate: '',
        dataSource: 'MANUAL',
        effectiveTime: '',
        expiryTime: '',
        isEnabled: 1,
        orgId: null
      }
    },
    
    formatDateTime(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    
    handleSave() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 验证币种不能相同
          if (this.temp.baseCurrency === this.temp.targetCurrency) {
            this.$message.error('基础币种和目标币种不能相同')
            return
          }
          
          // 验证失效时间必须大于生效时间
          if (this.temp.expiryTime && this.temp.effectiveTime) {
            if (new Date(this.temp.expiryTime) <= new Date(this.temp.effectiveTime)) {
              this.$message.error('失效时间必须大于生效时间')
              return
            }
          }
          
          // 设置创建/更新用户
          if (this.temp.rateId) {
            this.temp.updateUser = this.$store.getters.userId
          } else {
            this.temp.createUser = this.$store.getters.userId
            this.temp.updateUser = this.$store.getters.userId
          }
          
          saveOrUpdateExchangeRate(this.temp).then(response => {
            if (response.success) {
              this.$message.success(this.temp.rateId ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.$emit('refresh')
            } else {
              this.$message.error(response.message || '保存失败')
            }
          })
        }
      })
    },
    
    handleClose() {
      this.resetTemp()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
