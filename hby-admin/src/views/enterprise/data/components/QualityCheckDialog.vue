<template>
  <el-dialog
    title="数据质量检查"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="检查配置" name="config">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="检查名称" prop="checkName">
                <el-input v-model="form.checkName" placeholder="请输入检查名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检查类型" prop="checkType">
                <el-select v-model="form.checkType" placeholder="请选择检查类型">
                  <el-option label="完整性检查" value="completeness"></el-option>
                  <el-option label="准确性检查" value="accuracy"></el-option>
                  <el-option label="一致性检查" value="consistency"></el-option>
                  <el-option label="有效性检查" value="validity"></el-option>
                  <el-option label="及时性检查" value="timeliness"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="检查范围">
            <el-checkbox-group v-model="form.checkScope">
              <el-checkbox label="financial">财务数据</el-checkbox>
              <el-checkbox label="operation">经营数据</el-checkbox>
              <el-checkbox label="personnel">人员数据</el-checkbox>
              <el-checkbox label="asset">资产数据</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item label="检查规则">
            <el-button type="primary" size="small" @click="addRule" style="margin-bottom: 10px;">
              添加规则
            </el-button>
            <el-table :data="form.checkRules" border>
              <el-table-column label="序号" width="60">
                <template slot-scope="scope">
                  {{ scope.$index + 1 }}
                </template>
              </el-table-column>
              <el-table-column label="规则名称" width="200">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.ruleName" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="检查字段" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.fieldName" size="small"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="规则类型" width="120">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.ruleType" size="small">
                    <el-option label="非空检查" value="notNull"></el-option>
                    <el-option label="格式检查" value="format"></el-option>
                    <el-option label="范围检查" value="range"></el-option>
                    <el-option label="唯一性检查" value="unique"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="规则参数">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.ruleParam" size="small" placeholder="规则参数"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button type="danger" size="mini" @click="removeRule(scope.$index)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          
          <el-form-item label="检查设置">
            <el-checkbox v-model="form.autoFix">自动修复</el-checkbox>
            <el-checkbox v-model="form.generateReport">生成报告</el-checkbox>
            <el-checkbox v-model="form.notifyResult">结果通知</el-checkbox>
          </el-form-item>
        </el-form>
        
        <div style="margin-top: 20px;">
          <el-button type="primary" @click="startCheck" :loading="checking">
            开始检查
          </el-button>
          <el-button @click="saveConfig">保存配置</el-button>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="检查结果" name="result">
        <el-alert
          :title="`质量检查完成，共检查 ${checkResult.totalCount} 条数据`"
          type="success"
          :closable="false"
          style="margin-bottom: 20px;"
          v-if="checkResult.totalCount > 0"
        ></el-alert>
        
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="6">
            <el-statistic title="检查总数" :value="checkResult.totalCount" suffix="条">
              <template slot="prefix">
                <i class="el-icon-document" style="color: #409EFF"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="通过检查" :value="checkResult.passCount" suffix="条">
              <template slot="prefix">
                <i class="el-icon-success" style="color: #67C23A"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="发现问题" :value="checkResult.issueCount" suffix="条">
              <template slot="prefix">
                <i class="el-icon-warning" style="color: #E6A23C"></i>
              </template>
            </el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="质量评分" :value="checkResult.qualityScore" suffix="分">
              <template slot="prefix">
                <i class="el-icon-star-on" style="color: #F7BA2A"></i>
              </template>
            </el-statistic>
          </el-col>
        </el-row>
        
        <el-divider content-position="left">问题详情</el-divider>
        <el-table :data="checkResult.issues" border>
          <el-table-column label="序号" width="60">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="ruleName" label="规则名称" width="150"></el-table-column>
          <el-table-column prop="fieldName" label="字段名称" width="120"></el-table-column>
          <el-table-column prop="issueType" label="问题类型" width="120">
            <template slot-scope="scope">
              <el-tag size="small" :type="getIssueTypeTag(scope.row.issueType)">
                {{ scope.row.issueType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="issueCount" label="问题数量" width="100"></el-table-column>
          <el-table-column prop="issueDescription" label="问题描述"></el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="viewIssueDetail(scope.row)">
                查看详情
              </el-button>
              <el-button type="text" size="small" @click="fixIssue(scope.row)">
                修复
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      
      <el-tab-pane label="历史记录" name="history">
        <el-table :data="checkHistory" border>
          <el-table-column prop="checkTime" label="检查时间" width="180"></el-table-column>
          <el-table-column prop="checkName" label="检查名称" width="200"></el-table-column>
          <el-table-column prop="checkType" label="检查类型" width="120"></el-table-column>
          <el-table-column prop="totalCount" label="检查数量" width="100"></el-table-column>
          <el-table-column prop="issueCount" label="问题数量" width="100"></el-table-column>
          <el-table-column prop="qualityScore" label="质量评分" width="100"></el-table-column>
          <el-table-column prop="operator" label="操作人" width="120"></el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="viewHistoryDetail(scope.row)">
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportReport" v-if="checkResult.totalCount > 0">
        导出报告
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { runDataQualityCheck, getDataQualityList } from '@/api/enterprise/data'

export default {
  name: 'QualityCheckDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    enterpriseId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      activeTab: 'config',
      checking: false,
      form: {
        checkName: '',
        checkType: '',
        checkScope: [],
        checkRules: [],
        autoFix: false,
        generateReport: true,
        notifyResult: true
      },
      rules: {
        checkName: [
          { required: true, message: '请输入检查名称', trigger: 'blur' }
        ],
        checkType: [
          { required: true, message: '请选择检查类型', trigger: 'change' }
        ]
      },
      checkResult: {
        totalCount: 0,
        passCount: 0,
        issueCount: 0,
        qualityScore: 0,
        issues: []
      },
      checkHistory: []
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
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
        this.loadHistory()
      }
    }
  },
  methods: {
    initForm() {
      this.form = {
        checkName: '',
        checkType: '',
        checkScope: ['financial'],
        checkRules: [
          {
            ruleName: '营业收入非空检查',
            fieldName: 'revenue',
            ruleType: 'notNull',
            ruleParam: ''
          }
        ],
        autoFix: false,
        generateReport: true,
        notifyResult: true
      }
      this.checkResult = {
        totalCount: 0,
        passCount: 0,
        issueCount: 0,
        qualityScore: 0,
        issues: []
      }
    },
    addRule() {
      this.form.checkRules.push({
        ruleName: '',
        fieldName: '',
        ruleType: '',
        ruleParam: ''
      })
    },
    removeRule(index) {
      this.form.checkRules.splice(index, 1)
    },
    startCheck() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.checking = true
          const params = {
            enterpriseId: this.enterpriseId,
            checkName: this.form.checkName,
            checkType: this.form.checkType,
            checkScope: this.form.checkScope,
            checkRules: this.form.checkRules,
            autoFix: this.form.autoFix,
            generateReport: this.form.generateReport,
            notifyResult: this.form.notifyResult
          }
          runDataQualityCheck(params).then(res => {
            this.checking = false
            if (res.code === 1) {
              const data = res.data || {}
              this.checkResult = {
                totalCount: data.totalCount || 0,
                passCount: data.passCount || 0,
                issueCount: data.issueCount || 0,
                qualityScore: data.qualityScore || 0,
                issues: data.issues || []
              }
              this.activeTab = 'result'
              this.$message.success('质量检查完成')
            } else {
              this.$message.error(res.msg || '质量检查失败')
            }
          }).catch(err => {
            this.checking = false
            this.$message.error('质量检查请求失败: ' + (err.message || '网络错误'))
          })
        }
      })
    },
    saveConfig() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$message.success('配置保存成功')
        }
      })
    },
    loadHistory() {
      const params = {
        enterpriseId: this.enterpriseId,
        pageNum: 1,
        pageSize: 20
      }
      getDataQualityList(params).then(res => {
        if (res.code === 1) {
          this.checkHistory = res.data.list || res.data || []
        } else {
          this.checkHistory = []
        }
      }).catch(() => {
        this.checkHistory = []
      })
    },
    getIssueTypeTag(type) {
      const typeMap = {
        '数据缺失': 'danger',
        '格式错误': 'warning',
        '数据异常': 'info',
        '逻辑错误': 'primary'
      }
      return typeMap[type] || 'info'
    },
    viewIssueDetail(row) {
      this.$message.info('查看问题详情：' + row.issueDescription)
    },
    fixIssue(row) {
      this.$emit('fix-issue', row)
    },
    viewHistoryDetail(row) {
      this.$message.info('查看历史详情：' + row.checkName)
    },
    exportReport() {
      this.$message.success('质量报告导出中...')
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
