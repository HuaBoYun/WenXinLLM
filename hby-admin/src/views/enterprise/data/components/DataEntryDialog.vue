<template>
  <el-dialog
    title="数据录入"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form :model="form" :rules="rules" ref="dataForm" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据类型" prop="dataType">
            <el-select v-model="form.dataType" placeholder="请选择数据类型">
              <el-option label="财务数据" value="财务数据"></el-option>
              <el-option label="经营数据" value="经营数据"></el-option>
              <el-option label="人员数据" value="人员数据"></el-option>
              <el-option label="资产数据" value="资产数据"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据类别" prop="dataCategory">
            <el-input v-model="form.dataCategory" placeholder="请输入数据类别"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告期间" prop="reportPeriod">
            <el-date-picker
              v-model="form.reportPeriod"
              type="month"
              placeholder="选择报告期间"
              value-format="yyyy-MM"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告年度" prop="reportYear">
            <el-input v-model="form.reportYear" placeholder="请输入报告年度"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="提交人" prop="submitter">
            <el-input v-model="form.submitter" placeholder="请输入提交人"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="数据项目">
        <el-table :data="form.dataItems" border>
          <el-table-column prop="itemCode" label="项目编码" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemCode" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="itemName" label="项目名称" width="200">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemName" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="itemValue" label="数值" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemValue" type="number" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="unit" label="单位" width="100">
            <template slot-scope="scope">
              <el-select v-model="scope.row.unit" size="small" placeholder="单位">
                <el-option label="万元" value="万元"></el-option>
                <el-option label="元" value="元"></el-option>
                <el-option label="人" value="人"></el-option>
                <el-option label="%" value="%"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="removeItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" size="small" @click="addItem" style="margin-top: 10px;">添加数据项</el-button>
      </el-form-item>
      
      <el-form-item label="数据验证">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card>
              <el-statistic title="数据项数量" :value="form.dataItems.length">
                <template slot="prefix">
                  <i class="el-icon-document" style="color: #409EFF"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <el-statistic title="完整性检查" :value="completenessRate" suffix="%">
                <template slot="prefix">
                  <i class="el-icon-success" style="color: #67C23A"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <el-statistic title="质量评分" :value="validationResult ? validationResult.qualityScore : qualityScore">
                <template slot="prefix">
                  <i class="el-icon-star-on" style="color: #E6A23C"></i>
                </template>
              </el-statistic>
            </el-card>
          </el-col>
        </el-row>
        
        <!-- 验证结果显示 -->
        <el-alert
          v-if="validationResult"
          :title="validationResult.valid ? '验证通过' : '验证失败'"
          :type="validationResult.valid ? 'success' : 'error'"
          :closable="false"
          style="margin-top: 15px;"
        >
          <div v-if="validationResult.valid">
            数据质量评分: <strong>{{ validationResult.qualityScore }}分</strong>
          </div>
          <div v-else>
            <div v-for="(error, index) in validationResult.errors" :key="index" style="margin: 5px 0;">
              • {{ error }}
            </div>
          </div>
        </el-alert>
      </el-form-item>
      
      <el-form-item label="备注说明">
        <el-input type="textarea" v-model="form.remark" :rows="3" placeholder="请输入数据录入说明(选填)"></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button @click="handleCopy" :disabled="!form.id">复制录入</el-button>
      <el-button @click="validateData" :loading="validationLoading">数据验证</el-button>
      <el-button type="primary" @click="saveData" :loading="saveLoading">保存数据</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addDataEntry, updateDataEntry, validateDataEntry, copyDataEntry } from '@/api/enterprise/data'

export default {
  name: 'DataEntryDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    entryData: {
      type: Object,
      default: () => ({})
    },
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      form: {
        id: '',
        enterpriseId: '',
        enterpriseName: '',
        dataType: '财务数据',
        dataCategory: '',
        reportPeriod: '',
        reportYear: new Date().getFullYear().toString(),
        status: '草稿',
        submitter: '',
        qualityScore: 0,
        remark: '',
        dataItems: []
      },
      rules: {
        dataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ],
        reportPeriod: [
          { required: true, message: '请选择报告期间', trigger: 'change' }
        ],
        submitter: [
          { required: true, message: '请输入提交人', trigger: 'blur' }
        ]
      },
      validationLoading: false,
      saveLoading: false,
      validationResult: null
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    completenessRate() {
      const totalFields = this.form.dataItems.length * 4
      const filledFields = this.form.dataItems.reduce((count, item) => {
        return count + 
          (item.itemCode ? 1 : 0) +
          (item.itemName ? 1 : 0) +
          (item.itemValue ? 1 : 0) +
          (item.unit ? 1 : 0)
      }, 0)
      return totalFields > 0 ? Math.round((filledFields / totalFields) * 100) : 0
    },
    qualityScore() {
      let score = 0
      if (this.completenessRate >= 90) score += 40
      else if (this.completenessRate >= 70) score += 30
      else if (this.completenessRate >= 50) score += 20
      
      if (this.form.dataItems.length >= 5) score += 30
      else if (this.form.dataItems.length >= 3) score += 20
      else score += 10
      
      if (this.form.dataType && this.form.reportPeriod) score += 30
      
      return score
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      if (this.entryData && this.entryData.id) {
        // 编辑模式
        this.form = { ...this.entryData }
      } else {
        // 新增模式
        this.form = {
          id: '',
          enterpriseId: this.enterpriseId,
          enterpriseName: this.enterpriseName || '',
          dataType: '财务数据',
          dataCategory: '',
          reportPeriod: '',
          reportYear: new Date().getFullYear().toString(),
          status: '草稿',
          submitter: '',
          qualityScore: 0,
          remark: '',
          dataItems: [
            {
              itemCode: 'REV001',
              itemName: '营业收入',
              itemValue: 0,
              unit: '万元',
              remark: '主营业务收入'
            }
          ]
        }
      }
      this.validationResult = null
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.dataForm && this.$refs.dataForm.resetFields()
    },
    addItem() {
      this.form.dataItems.push({
        itemCode: '',
        itemName: '',
        itemValue: '',
        unit: '',
        remark: ''
      })
    },
    removeItem(index) {
      this.form.dataItems.splice(index, 1)
    },
    async validateData() {
      try {
        this.validationLoading = true
        const response = await validateDataEntry(this.form)
        if (response.data && response.data.valid) {
          this.validationResult = {
            valid: true,
            qualityScore: response.data.qualityScore || this.qualityScore,
            errors: []
          }
          this.form.qualityScore = this.validationResult.qualityScore
          this.$message.success(`数据验证通过，质量评分: ${this.validationResult.qualityScore}分`)
        } else {
          const errors = response.data && response.data.errors ? response.data.errors : ['验证失败']
          this.validationResult = {
            valid: false,
            qualityScore: 0,
            errors: errors
          }
          this.$message.error('验证失败: ' + errors.join('，'))
        }
      } catch (error) {
        this.$message.error('数据验证失败')
        this.validationResult = {
          valid: false,
          qualityScore: 0,
          errors: ['验证异常']
        }
      } finally {
        this.validationLoading = false
      }
    },
    async saveData() {
      try {
        await this.$refs.dataForm.validate()
        
        // 计算质量评分
        this.form.qualityScore = this.qualityScore
        
        // 处理备注:如果没有填写,使用数据项的汇总
        if (!this.form.remark && this.form.dataItems.length > 0) {
          this.form.remark = `共录入${this.form.dataItems.length}条数据项`
        }
        
        this.saveLoading = true
        const api = this.form.id ? updateDataEntry : addDataEntry
        const response = await api(this.form)
        
        if (response.result == 200) {
          this.$message.success(this.form.id ? '更新成功' : '保存成功')
          this.handleClose()
          this.$emit('refresh')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        if (error.message) {
          this.$message.error(error.message)
        } else {
          this.$message.error('请完善必填信息')
        }
      } finally {
        this.saveLoading = false
      }
    },
    async handleCopy() {
      if (!this.form.id) {
        this.$message.warning('请先保存数据后再复制')
        return
      }
      try {
        const response = await copyDataEntry({ id: this.form.id })
        if (response.result == 200) {
          this.$message.success('复制成功')
          this.handleClose()
          this.$emit('refresh')
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败')
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
