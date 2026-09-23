<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="检查编码">
        <el-input v-model="queryForm.checkCode" placeholder="请输入检查编码" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="检查名称">
        <el-input v-model="queryForm.checkName" placeholder="请输入检查名称" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="检查类型">
        <el-select v-model="queryForm.checkType" placeholder="请选择检查类型" clearable style="width: 120px">
          <el-option label="手动" value="MANUAL" />
          <el-option label="自动" value="AUTO" />
        </el-select>
      </el-form-item>
      <el-form-item label="对象类型">
        <el-select v-model="queryForm.targetType" placeholder="请选择对象类型" clearable style="width: 120px">
          <el-option label="归集任务" value="TASK" />
          <el-option label="数据表" value="TABLE" />
          <el-option label="字段" value="FIELD" />
        </el-select>
      </el-form-item>
      <el-form-item label="检查状态">
        <el-select v-model="queryForm.checkStatus" placeholder="请选择检查状态" clearable style="width: 120px">
          <el-option label="待检查" value="PENDING" />
          <el-option label="检查中" value="CHECKING" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="失败" value="FAILED" />
        </el-select>
      </el-form-item>
      <el-form-item label="质量等级">
        <el-select v-model="queryForm.qualityLevel" placeholder="请选择质量等级" clearable style="width: 120px">
          <el-option label="优秀" value="EXCELLENT" />
          <el-option label="良好" value="GOOD" />
          <el-option label="一般" value="FAIR" />
          <el-option label="较差" value="POOR" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        <el-button type="success" icon="el-icon-video-play" @click="handleExecute">执行检查</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="检查编码" prop="checkCode" width="150" />
      <el-table-column label="检查名称" prop="checkName" width="200" show-overflow-tooltip />
      <el-table-column label="检查类型" prop="checkType" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.checkType === 'MANUAL'" type="primary">手动</el-tag>
          <el-tag v-else type="success">自动</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="对象类型" prop="targetType" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.targetType === 'TASK'" type="primary">归集任务</el-tag>
          <el-tag v-else-if="scope.row.targetType === 'TABLE'" type="success">数据表</el-tag>
          <el-tag v-else type="info">字段</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="对象名称" prop="targetName" width="150" show-overflow-tooltip />
      <el-table-column label="检查状态" prop="checkStatus" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.checkStatus === 'PENDING'" type="info">待检查</el-tag>
          <el-tag v-else-if="scope.row.checkStatus === 'CHECKING'" type="warning">检查中</el-tag>
          <el-tag v-else-if="scope.row.checkStatus === 'COMPLETED'" type="success">已完成</el-tag>
          <el-tag v-else type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="质量评分" prop="qualityScore" width="100" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.qualityScore !== null && scope.row.qualityScore !== undefined">
            <el-progress
              :percentage="parseFloat(scope.row.qualityScore)"
              :color="getScoreColor(scope.row.qualityScore)"
              :format="() => scope.row.qualityScore + '分'"
            />
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="质量等级" prop="qualityLevel" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.qualityLevel === 'EXCELLENT'" type="success">优秀</el-tag>
          <el-tag v-else-if="scope.row.qualityLevel === 'GOOD'" type="primary">良好</el-tag>
          <el-tag v-else-if="scope.row.qualityLevel === 'FAIR'" type="warning">一般</el-tag>
          <el-tag v-else-if="scope.row.qualityLevel === 'POOR'" type="danger">较差</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="规则统计" width="180" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.totalRules">
            总:{{ scope.row.totalRules }} /
            通过:{{ scope.row.passedRules }} /
            失败:{{ scope.row.failedRules }} /
            警告:{{ scope.row.warningRules }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" prop="startTime" width="160">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column label="执行时长" prop="checkDuration" width="100" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.checkDuration">{{ formatDuration(scope.row.checkDuration) }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleViewDetail(scope.row)">查看明细</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 执行检查对话框 -->
    <el-dialog title="执行质量检查" :visible.sync="executeDialogVisible" width="800px" @close="closeExecuteDialog">

      <el-form ref="executeForm" :model="executeForm" :rules="executeRules" label-width="120px">
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查名称" prop="checkName">
              <el-input v-model="executeForm.checkName" placeholder="请输入检查名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查类型" prop="checkType">
              <el-radio-group v-model="executeForm.checkType">
                <el-radio label="MANUAL">手动</el-radio>
                <el-radio label="AUTO">自动</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 检查对象 -->
        <el-divider content-position="left">检查对象</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="对象类型" prop="targetType">
              <el-select v-model="executeForm.targetType" placeholder="请选择对象类型" style="width: 100%">
                <el-option label="归集任务" value="TASK" />
                <el-option label="数据表" value="TABLE" />
                <el-option label="字段" value="FIELD" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="对象ID" prop="targetId">
              <el-input v-model="executeForm.targetId" placeholder="请输入对象ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="对象名称" prop="targetName">
              <el-input v-model="executeForm.targetName" placeholder="请输入对象名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 选择规则 -->
        <el-divider content-position="left">选择规则</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="质量规则">
              <el-checkbox-group v-model="executeForm.ruleIds">
                <el-row>
                  <el-col v-for="rule in enabledRules" :key="rule.ruleId" :span="12" style="margin-bottom: 10px">
                    <el-checkbox :label="rule.ruleId">
                      {{ rule.ruleName }}
                      <el-tag size="mini" :type="getRuleTypeTag(rule.ruleType)" style="margin-left: 5px">
                        {{ getRuleTypeText(rule.ruleType) }}
                      </el-tag>
                      <el-tag size="mini" :type="getCheckLevelTag(rule.checkLevel)" style="margin-left: 5px">
                        {{ getCheckLevelText(rule.checkLevel) }}
                      </el-tag>
                    </el-checkbox>
                  </el-col>
                </el-row>
              </el-checkbox-group>
              <div v-if="enabledRules.length === 0" style="color: #999; text-align: center; padding: 20px">
                暂无启用的质量规则
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeExecuteDialog">取 消</el-button>
        <el-button type="primary" :loading="executing" @click="handleSubmitExecute">执 行</el-button>
      </div>
    </el-dialog>

    <!-- 查看明细对话框 -->
    <el-dialog title="检查明细" :visible.sync="detailDialogVisible" width="1200px" @close="closeDetailDialog">
      <!-- 检查概要 -->
      <el-descriptions :column="3" border>
        <el-descriptions-item label="检查编码">{{ checkDetail.checkCode }}</el-descriptions-item>
        <el-descriptions-item label="检查名称">{{ checkDetail.checkName }}</el-descriptions-item>
        <el-descriptions-item label="检查类型">
          <el-tag v-if="checkDetail.checkType === 'MANUAL'" type="primary">手动</el-tag>
          <el-tag v-else type="success">自动</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="对象类型">
          <el-tag v-if="checkDetail.targetType === 'TASK'" type="primary">归集任务</el-tag>
          <el-tag v-else-if="checkDetail.targetType === 'TABLE'" type="success">数据表</el-tag>
          <el-tag v-else type="info">字段</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="对象名称">{{ checkDetail.targetName }}</el-descriptions-item>
        <el-descriptions-item label="检查状态">
          <el-tag v-if="checkDetail.checkStatus === 'COMPLETED'" type="success">已完成</el-tag>
          <el-tag v-else-if="checkDetail.checkStatus === 'FAILED'" type="danger">失败</el-tag>
          <el-tag v-else type="warning">{{ checkDetail.checkStatus }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="质量评分">
          <span style="font-size: 18px; font-weight: bold; color: #409EFF">
            {{ checkDetail.qualityScore }}分
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="质量等级">
          <el-tag v-if="checkDetail.qualityLevel === 'EXCELLENT'" type="success" size="medium">优秀</el-tag>
          <el-tag v-else-if="checkDetail.qualityLevel === 'GOOD'" type="primary" size="medium">良好</el-tag>
          <el-tag v-else-if="checkDetail.qualityLevel === 'FAIR'" type="warning" size="medium">一般</el-tag>
          <el-tag v-else type="danger" size="medium">较差</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行时长">{{ formatDuration(checkDetail.checkDuration) }}</el-descriptions-item>
        <el-descriptions-item label="规则总数">{{ checkDetail.totalRules }}</el-descriptions-item>
        <el-descriptions-item label="通过规则">
          <span style="color: #67C23A">{{ checkDetail.passedRules }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="失败规则">
          <span style="color: #F56C6C">{{ checkDetail.failedRules }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="警告规则">
          <span style="color: #E6A23C">{{ checkDetail.warningRules }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ formatDateTime(checkDetail.startTime) }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ formatDateTime(checkDetail.endTime) }}</el-descriptions-item>
      </el-descriptions>

      <el-divider></el-divider>

      <!-- 检查明细表格 -->
      <el-table :data="detailList" border style="width: 100%">
        <el-table-column label="规则编码" prop="ruleCode" width="120" />
        <el-table-column label="规则名称" prop="ruleName" width="150" show-overflow-tooltip />
        <el-table-column label="规则类型" prop="ruleType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getRuleTypeTag(scope.row.ruleType)">
              {{ getRuleTypeText(scope.row.ruleType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="检查级别" prop="checkLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="getCheckLevelTag(scope.row.checkLevel)">
              {{ getCheckLevelText(scope.row.checkLevel) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="检查结果" prop="checkResult" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.checkResult === 'PASS'" type="success">通过</el-tag>
            <el-tag v-else-if="scope.row.checkResult === 'FAIL'" type="danger">失败</el-tag>
            <el-tag v-else type="warning">警告</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="检查值" prop="checkValue" width="100" align="center" />
        <el-table-column label="阈值" prop="thresholdValue" width="100" align="center" />
        <el-table-column label="错误数量" prop="errorCount" width="100" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.errorCount > 0" style="color: #F56C6C">{{ scope.row.errorCount }}</span>
            <span v-else>0</span>
          </template>
        </el-table-column>
        <el-table-column label="错误信息" prop="errorMessage" show-overflow-tooltip />
        <el-table-column label="改进建议" prop="suggestion" show-overflow-tooltip />
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDetailDialog">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryQualityCheckPage,
  queryQualityCheckById,
  queryQualityCheckDetails,
  executeQualityCheck,
  deleteQualityCheck,
  batchDeleteQualityCheck
} from '@/api/financialSharing/dataCollection'
import { queryEnabledQualityRules } from '@/api/financialSharing/dataCollection'
import { formatDateTime } from '@/utils/dateUtil'

export default {
  name: 'DataQualityCheck',
  data() {
    return {
      // 加载状态
      loading: false,
      // 查询表单
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        checkCode: null,
        checkName: null,
        checkType: null,
        targetType: null,
        checkStatus: null,
        qualityLevel: null
      },
      // 数据列表
      dataList: [],
      // 总记录数
      total: 0,
      // 选中的ID列表
      selectedIds: [],
      // 执行检查对话框
      executeDialogVisible: false,
      executing: false,
      executeForm: {
        checkName: '',
        checkType: 'MANUAL',
        targetType: '',
        targetId: null,
        targetName: '',
        ruleIds: []
      },
      executeRules: {
        checkName: [
          { required: true, message: '请输入检查名称', trigger: 'blur' }
        ],
        checkType: [
          { required: true, message: '请选择检查类型', trigger: 'change' }
        ],
        targetType: [
          { required: true, message: '请选择对象类型', trigger: 'change' }
        ],
        targetName: [
          { required: true, message: '请输入对象名称', trigger: 'blur' }
        ]
      },
      // 启用的规则列表
      enabledRules: [],
      // 查看明细对话框
      detailDialogVisible: false,
      checkDetail: {},
      detailList: []
    }
  },
  created() {
    this.getList()
    this.getEnabledRules()
  },
  methods: {
    // 格式化日期时间
    formatDateTime(dateTime) {
      return formatDateTime(dateTime)
    },

    // 格式化时长
    formatDuration(ms) {
      if (!ms) return '-'
      if (ms < 1000) return ms + 'ms'
      if (ms < 60000) return (ms / 1000).toFixed(2) + 's'
      return (ms / 60000).toFixed(2) + 'm'
    },

    // 获取评分颜色
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 60) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取规则类型标签
    getRuleTypeTag(type) {
      const map = {
        COMPLETENESS: 'primary',
        ACCURACY: 'success',
        CONSISTENCY: 'info',
        TIMELINESS: 'warning',
        VALIDITY: 'danger'
      }
      return map[type] || ''
    },

    // 获取规则类型文本
    getRuleTypeText(type) {
      const map = {
        COMPLETENESS: '完整性',
        ACCURACY: '准确性',
        CONSISTENCY: '一致性',
        TIMELINESS: '及时性',
        VALIDITY: '有效性'
      }
      return map[type] || type
    },

    // 获取检查级别标签
    getCheckLevelTag(level) {
      const map = {
        ERROR: 'danger',
        WARNING: 'warning',
        INFO: 'info'
      }
      return map[level] || ''
    },

    // 获取检查级别文本
    getCheckLevelText(level) {
      const map = {
        ERROR: '错误',
        WARNING: '警告',
        INFO: '提示'
      }
      return map[level] || level
    },

    // 查询列表
    getList() {
      this.loading = true
      queryQualityCheckPage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records || []
          this.total = response.data.total || 0
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    // 获取启用的规则列表
    getEnabledRules() {
      queryEnabledQualityRules().then(response => {
        if (response.code === 1) {
          this.enabledRules = response.data || []
        }
      })
    },

    // 查询按钮
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置按钮
    handleReset() {
      this.queryForm = {
        pageNumber: 1,
        pageSize: 10,
        checkCode: null,
        checkName: null,
        checkType: null,
        targetType: null,
        checkStatus: null,
        qualityLevel: null
      }
      this.getList()
    },

    // 执行检查按钮
    handleExecute() {
      this.resetExecuteForm()
      this.executeDialogVisible = true
    },

    // 提交执行检查
    handleSubmitExecute() {
      this.$refs.executeForm.validate(valid => {
        if (valid) {
          if (this.executeForm.ruleIds.length === 0) {
            this.$message.warning('请至少选择一个质量规则')
            return
          }
          this.executing = true
          executeQualityCheck(this.executeForm).then(response => {
            if (response.code === 1) {
              this.$message.success('执行成功')
              this.executeDialogVisible = false
              this.getList()
            }
            this.executing = false
          }).catch(() => {
            this.executing = false
          })
        }
      })
    },

    // 查看明细按钮
    handleViewDetail(row) {
      this.checkDetail = {}
      this.detailList = []
      this.detailDialogVisible = true

      // 查询检查记录详情
      queryQualityCheckById({ checkId: row.checkId }).then(response => {
        if (response.code === 1) {
          this.checkDetail = response.data || {}
        }
      })

      // 查询检查明细列表
      queryQualityCheckDetails({ checkId: row.checkId }).then(response => {
        if (response.code === 1) {
          this.detailList = response.data || []
        }
      })
    },

    // 删除按钮
    handleDelete(row) {
      this.$confirm('确认删除该检查记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteQualityCheck({ checkId: row.checkId }).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认删除选中的检查记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteQualityCheck(this.selectedIds).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.checkId)
    },

    // 关闭执行对话框
    closeExecuteDialog() {
      this.executeDialogVisible = false
      this.resetExecuteForm()
    },

    // 关闭明细对话框
    closeDetailDialog() {
      this.detailDialogVisible = false
      this.checkDetail = {}
      this.detailList = []
    },

    // 重置执行表单
    resetExecuteForm() {
      this.executeForm = {
        checkName: '',
        checkType: 'MANUAL',
        targetType: '',
        targetId: null,
        targetName: '',
        ruleIds: []
      }
      if (this.$refs.executeForm) {
        this.$refs.executeForm.clearValidate()
      }
    }
  }
}
</script>

<style scoped>
.query-form {
  background: #fff;
  padding: 20px;
  margin-bottom: 10px;
}
</style>

