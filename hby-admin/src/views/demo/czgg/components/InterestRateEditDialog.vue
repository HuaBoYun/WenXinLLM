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
          <el-form-item label="利率类型" prop="rateType">
            <el-select
              v-model="temp.rateType"
              placeholder="请选择利率类型"
              style="width: 100%"
            >
              <el-option label="存款利率" value="DEPOSIT" />
              <el-option label="贷款利率" value="LOAN" />
              <el-option label="同业拆借" value="INTERBANK" />
              <el-option label="央行基准" value="BENCHMARK" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="currencyCode">
            <el-select
              v-model="temp.currencyCode"
              placeholder="请选择币种"
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
          <el-form-item label="期限类型" prop="termType">
            <el-select
              v-model="temp.termType"
              placeholder="请选择期限类型"
              style="width: 100%"
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
        <el-col :span="12">
          <el-form-item label="利率值(%)" prop="rateValue">
            <el-input-number
              v-model="temp.rateValue"
              :precision="4"
              :min="0"
              :max="100"
              placeholder="请输入利率值"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="利率日期" prop="rateDate">
            <el-date-picker
              v-model="temp.rateDate"
              type="date"
              placeholder="请选择利率日期"
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
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计息方式" prop="interestMethod">
            <el-select
              v-model="temp.interestMethod"
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
              v-model="temp.dayCountBasis"
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
      
      <el-form-item label="状态" prop="isEnabled">
        <el-radio-group v-model="temp.isEnabled">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="temp.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
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
import { saveOrUpdateInterestRate } from '@/api/globalTreasurer/czgg'

export default {
  name: 'InterestRateEditDialog',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      temp: {
        rateId: null,
        rateType: '',
        currencyCode: '',
        termType: '',
        rateValue: null,
        rateDate: '',
        dataSource: 'MANUAL',
        effectiveTime: '',
        expiryTime: '',
        interestMethod: 'SIMPLE',
        dayCountBasis: 'ACT_365',
        isEnabled: 1,
        remark: '',
        orgId: null
      },
      rules: {
        rateType: [
          { required: true, message: '请选择利率类型', trigger: 'change' }
        ],
        currencyCode: [
          { required: true, message: '请选择币种', trigger: 'change' }
        ],
        termType: [
          { required: true, message: '请选择期限类型', trigger: 'change' }
        ],
        rateValue: [
          { required: true, message: '请输入利率值', trigger: 'blur' }
        ],
        rateDate: [
          { required: true, message: '请选择利率日期', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据来源', trigger: 'change' }
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
    show(row) {
      this.dialogVisible = true
      this.resetTemp()
      
      if (row) {
        this.dialogTitle = '编辑利率'
        this.temp = Object.assign({}, row)
        // 处理时间格式
        if (this.temp.effectiveTime) {
          this.temp.effectiveTime = this.temp.effectiveTime.replace('T', ' ').substring(0, 19)
        }
        if (this.temp.expiryTime) {
          this.temp.expiryTime = this.temp.expiryTime.replace('T', ' ').substring(0, 19)
        }
      } else {
        this.dialogTitle = '新增利率'
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
        rateType: '',
        currencyCode: '',
        termType: '',
        rateValue: null,
        rateDate: '',
        dataSource: 'MANUAL',
        effectiveTime: '',
        expiryTime: '',
        interestMethod: 'SIMPLE',
        dayCountBasis: 'ACT_365',
        isEnabled: 1,
        remark: '',
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
          // 验证失效时间必须大于生效时间
          if (this.temp.expiryTime && this.temp.effectiveTime) {
            if (new Date(this.temp.expiryTime) <= new Date(this.temp.effectiveTime)) {
              this.$message.error('失效时间必须大于生效时间')
              return
            }
          }
          
          // 验证利率值范围
          if (this.temp.rateValue < 0 || this.temp.rateValue > 100) {
            this.$message.error('利率值必须在0-100之间')
            return
          }
          
          // 设置创建/更新用户
          if (this.temp.rateId) {
            this.temp.updateUser = this.$store.getters.userId
          } else {
            this.temp.createUser = this.$store.getters.userId
            this.temp.updateUser = this.$store.getters.userId
          }
          
          saveOrUpdateInterestRate(this.temp).then(response => {
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
