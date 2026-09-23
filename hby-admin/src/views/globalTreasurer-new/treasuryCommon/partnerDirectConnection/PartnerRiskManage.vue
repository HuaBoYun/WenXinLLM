<template>
  <div class="partner-risk-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-warning"></i>
            合作伙伴风险管理
          </h2>
          <p class="page-description">管理合作伙伴风险评估，包括风险等级、评估指标、预警机制和风控措施</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增评估
          </el-button>
          <el-button type="warning" icon="el-icon-warning" @click="handleRiskAlert">
            风险预警
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 风险统计卡片 -->
    <div class="risk-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总评估数</div>
                <div class="card-value">{{ totalRiskAssessments }}</div>
                <div class="card-change">风险评估</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon high-risk-icon">
                <i class="el-icon-close"></i>
              </div>
              <div class="card-info">
                <div class="card-title">高风险</div>
                <div class="card-value">{{ highRiskPartners }}</div>
                <div class="card-change negative">需要关注</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon medium-risk-icon">
                <i class="el-icon-warning-outline"></i>
              </div>
              <div class="card-info">
                <div class="card-title">中风险</div>
                <div class="card-value">{{ mediumRiskPartners }}</div>
                <div class="card-change">定期监控</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon low-risk-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">低风险</div>
                <div class="card-value">{{ lowRiskPartners }}</div>
                <div class="card-change positive">风险可控</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="伙伴编码">
            <el-input
              v-model="listQuery.partnerCode"
              placeholder="请输入伙伴编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="伙伴名称">
            <el-input
              v-model="listQuery.partnerName"
              placeholder="请输入伙伴名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="AAA级" value="AAA" />
              <el-option label="AA级" value="AA" />
              <el-option label="A级" value="A" />
              <el-option label="BBB级" value="BBB" />
              <el-option label="BB级" value="BB" />
              <el-option label="B级" value="B" />
              <el-option label="C级" value="C" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险状态">
            <el-select
              v-model="listQuery.riskStatus"
              placeholder="请选择风险状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="NORMAL" />
              <el-option label="关注" value="WATCH" />
              <el-option label="预警" value="WARNING" />
              <el-option label="限制" value="RESTRICTED" />
              <el-option label="禁止" value="PROHIBITED" />
            </el-select>
          </el-form-item>
          <el-form-item label="评估类型">
            <el-select
              v-model="listQuery.assessmentType"
              placeholder="请选择评估类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="初始评估" value="INITIAL" />
              <el-option label="定期评估" value="PERIODIC" />
              <el-option label="临时评估" value="TEMPORARY" />
              <el-option label="年度评估" value="ANNUAL" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="伙伴编码" prop="partnerCode" sortable="custom" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.partnerCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="伙伴名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.partnerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="伙伴类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getPartnerTypeColor(row.partnerType)" size="small">
            {{ getPartnerTypeName(row.partnerType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
            {{ row.riskLevel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险评分" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.riskScore }}</span>
        </template>
      </el-table-column>
      <el-table-column label="风险状态" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskStatusColor(row.riskStatus)" size="small">
            {{ getRiskStatusName(row.riskStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="评估类型" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getAssessmentTypeColor(row.assessmentType)" size="small">
            {{ getAssessmentTypeName(row.assessmentType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最近评估时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.lastAssessmentDate | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下次评估时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.nextAssessmentDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合作伙伴" prop="partnerId">
              <el-select v-model="temp.partnerId" placeholder="请选择合作伙伴" style="width: 100%" filterable>
                <el-option
                  v-for="partner in partnerOptions"
                  :key="partner.partnerId"
                  :label="partner.partnerName"
                  :value="partner.partnerId"
                >
                  <span style="float: left">{{ partner.partnerCode }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ partner.partnerName }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估类型" prop="assessmentType">
              <el-select v-model="temp.assessmentType" placeholder="请选择评估类型" style="width: 100%">
                <el-option
                  v-for="type in assessmentTypeOptions"
                  :key="type.value"
                  :label="type.label"
                  :value="type.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="temp.riskLevel" placeholder="请选择风险等级" style="width: 100%">
                <el-option
                  v-for="level in riskLevelOptions"
                  :key="level.value"
                  :label="level.label"
                  :value="level.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险评分" prop="riskScore">
              <el-input-number v-model="temp.riskScore" :min="0" :max="100" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险状态" prop="riskStatus">
              <el-select v-model="temp.riskStatus" placeholder="请选择风险状态" style="width: 100%">
                <el-option label="正常" value="NORMAL" />
                <el-option label="关注" value="WATCH" />
                <el-option label="预警" value="WARNING" />
                <el-option label="限制" value="RESTRICTED" />
                <el-option label="禁止" value="PROHIBITED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次评估时间" prop="nextAssessmentDate">
              <el-date-picker
                v-model="temp.nextAssessmentDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="评估说明" prop="assessmentDescription">
          <el-input v-model="temp.assessmentDescription" type="textarea" :rows="3" placeholder="请输入评估说明" />
        </el-form-item>
        <el-form-item label="风控措施" prop="controlMeasures">
          <el-input v-model="temp.controlMeasures" type="textarea" :rows="3" placeholder="请输入风控措施" />
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="temp.remarks" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确定</el-button>
      </div>
    </el-dialog>

    <!-- 风险预警对话框 -->
    <el-dialog title="风险预警设置" :visible.sync="alertDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form ref="alertForm" :model="alertForm" label-position="right" label-width="120px">
        <el-form-item label="预警阈值">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="高风险阈值" label-width="100px">
                <el-input-number v-model="alertForm.highRiskThreshold" :min="0" :max="100" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="中风险阈值" label-width="100px">
                <el-input-number v-model="alertForm.mediumRiskThreshold" :min="0" :max="100" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="预警方式">
          <el-checkbox-group v-model="alertForm.alertMethods">
            <el-checkbox label="EMAIL">邮件通知</el-checkbox>
            <el-checkbox label="SMS">短信通知</el-checkbox>
            <el-checkbox label="SYSTEM">系统消息</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="预警频率">
          <el-select v-model="alertForm.alertFrequency" placeholder="请选择预警频率" style="width: 100%">
            <el-option label="实时" value="REALTIME" />
            <el-option label="每日" value="DAILY" />
            <el-option label="每周" value="WEEKLY" />
            <el-option label="每月" value="MONTHLY" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="alertDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAlertSettings">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="风险评估详情" :visible.sync="detailDialogVisible" width="900px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="伙伴编码">{{ currentDetail.partnerCode }}</el-descriptions-item>
        <el-descriptions-item label="伙伴名称">{{ currentDetail.partnerName }}</el-descriptions-item>
        <el-descriptions-item label="伙伴类型">{{ getPartnerTypeName(currentDetail.partnerType) }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="getRiskLevelColor(currentDetail.riskLevel)">{{ currentDetail.riskLevel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="风险评分">{{ currentDetail.riskScore }}</el-descriptions-item>
        <el-descriptions-item label="风险状态">
          <el-tag :type="getRiskStatusColor(currentDetail.riskStatus)">{{ getRiskStatusName(currentDetail.riskStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评估类型">
          <el-tag :type="getAssessmentTypeColor(currentDetail.assessmentType)">{{ getAssessmentTypeName(currentDetail.assessmentType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最近评估时间">{{ currentDetail.lastAssessmentDate | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</el-descriptions-item>
        <el-descriptions-item label="下次评估时间">{{ currentDetail.nextAssessmentDate | parseTime('{y}-{m}-{d}') }}</el-descriptions-item>
        <el-descriptions-item label="评估说明" :span="2">{{ currentDetail.assessmentDescription }}</el-descriptions-item>
        <el-descriptions-item label="风控措施" :span="2">{{ currentDetail.controlMeasures }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import {
  getRiskAssessmentList,
  getRiskAssessmentDetail,
  addRiskAssessment,
  updateRiskAssessment,
  deleteRiskAssessment,
  getRiskStatistics,
  getRiskAlerts,
  exportRiskReport,
  batchRiskAssessment,
  batchDeleteRiskAssessment,
  getRiskLevels,
  getAssessmentTypes,
  getPartners,
  updateRiskStatus
} from '@/api/globalTreasurer/partnerDirectConnectionPartnerRiskManage'

export default {
  name: 'PartnerRiskManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        partnerCode: undefined,
        partnerName: undefined,
        riskLevel: undefined,
        riskStatus: undefined,
        assessmentType: undefined
      },

      // 统计数据
      totalRiskAssessments: 0,
      highRiskPartners: 0,
      mediumRiskPartners: 0,
      lowRiskPartners: 0,

      // 弹窗相关
      dialogVisible: false,
      dialogTitle: '',
      dialogStatus: '',
      temp: {
        id: undefined,
        partnerId: undefined,
        assessmentType: undefined,
        riskLevel: undefined,
        riskScore: undefined,
        riskStatus: undefined,
        nextAssessmentDate: undefined,
        assessmentDescription: undefined,
        controlMeasures: undefined,
        remarks: undefined
      },

      // 详情弹窗
      detailDialogVisible: false,
      currentDetail: {},

      // 预警弹窗
      alertDialogVisible: false,
      alertForm: {
        highRiskThreshold: 70,
        mediumRiskThreshold: 50,
        alertMethods: ['SYSTEM'],
        alertFrequency: 'DAILY'
      },

      // 下拉选项
      partnerOptions: [],
      riskLevelOptions: [],
      assessmentTypeOptions: [],

      // 表单验证规则
      rules: {
        partnerId: [{ required: true, message: '请选择合作伙伴', trigger: 'change' }],
        assessmentType: [{ required: true, message: '请选择评估类型', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        riskScore: [{ required: true, type: 'number', message: '请输入风险评分', trigger: 'blur' }],
        riskStatus: [{ required: true, message: '请选择风险状态', trigger: 'change' }],
        nextAssessmentDate: [{ required: true, message: '请选择下次评估时间', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.getStatistics()
    this.loadOptions()
  },
  methods: {
    // 获取列表数据
    async getList() {
      this.listLoading = true
      try {
        const query = {
          pageNum: this.listQuery.page,
          pageSize: this.listQuery.limit,
          partnerCode: this.listQuery.partnerCode,
          partnerName: this.listQuery.partnerName,
          riskLevel: this.listQuery.riskLevel,
          riskStatus: this.listQuery.riskStatus,
          assessmentType: this.listQuery.assessmentType
        }
        const response = await getRiskAssessmentList(query)
        if (response.code === 1) {
          this.list = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.message || '获取列表失败')
        }
      } catch (error) {
        console.error('获取列表失败:', error)
        this.$message.error('获取列表失败')
      } finally {
        this.listLoading = false
      }
    },

    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getRiskStatistics()
        console.log('统计数据响应:', response)
        if (response.code === 1) {
          const stats = response.data
          console.log('统计数据:', stats)
          // 处理字符串类型的数字
          this.totalRiskAssessments = parseInt(stats.totalAssessments || stats.TOTALASSESSMENTS || 0)
          this.highRiskPartners = parseInt(stats.highRiskCount || stats.HIGHRISKCOUNT || 0)
          this.mediumRiskPartners = parseInt(stats.mediumRiskCount || stats.MEDIUMRISKCOUNT || 0)
          this.lowRiskPartners = parseInt(stats.lowRiskCount || stats.LOWRISKCOUNT || 0)
          console.log('设置后的值:', {
            totalRiskAssessments: this.totalRiskAssessments,
            highRiskPartners: this.highRiskPartners,
            mediumRiskPartners: this.mediumRiskPartners,
            lowRiskPartners: this.lowRiskPartners
          })
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 加载下拉选项
    async loadOptions() {
      try {
        // 获取合作伙伴列表
        const partnerRes = await getPartners()
        if (partnerRes.code === 1) {
          this.partnerOptions = partnerRes.data || []
        }

        // 获取风险等级
        const levelRes = await getRiskLevels()
        if (levelRes.code === 1) {
          this.riskLevelOptions = levelRes.data || []
        }

        // 获取评估类型
        const typeRes = await getAssessmentTypes()
        if (typeRes.code === 1) {
          this.assessmentTypeOptions = typeRes.data || []
        }
      } catch (error) {
        console.error('加载选项失败:', error)
      }
    },

    // 搜索
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    // 重置
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        partnerCode: undefined,
        partnerName: undefined,
        riskLevel: undefined,
        riskStatus: undefined,
        assessmentType: undefined
      }
      this.getList()
    },

    // 新增
    handleCreate() {
      this.dialogTitle = '新增风险评估'
      this.dialogStatus = 'create'
      this.temp = {
        id: undefined,
        partnerId: undefined,
        assessmentType: undefined,
        riskLevel: undefined,
        riskScore: undefined,
        riskStatus: undefined,
        nextAssessmentDate: undefined,
        assessmentDescription: undefined,
        controlMeasures: undefined,
        remarks: undefined
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 编辑
    handleUpdate(row) {
      this.dialogTitle = '编辑风险评估'
      this.dialogStatus = 'update'
      this.temp = Object.assign({}, row)
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    // 创建数据
    createData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await addRiskAssessment(this.temp)
            if (response.code === 1) {
              this.dialogVisible = false
              this.$message.success('创建成功')
              // 重置到第一页，确保新数据显示在最上面
              this.listQuery.page = 1
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.message || '创建失败')
            }
          } catch (error) {
            console.error('创建失败:', error)
            this.$message.error('创建失败')
          }
        }
      })
    },

    // 更新数据
    updateData() {
      this.$refs['dataForm'].validate(async (valid) => {
        if (valid) {
          try {
            const response = await updateRiskAssessment(this.temp)
            if (response.code === 1) {
              this.dialogVisible = false
              this.$message.success('更新成功')
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(response.message || '更新失败')
            }
          } catch (error) {
            console.error('更新失败:', error)
            this.$message.error('更新失败')
          }
        }
      })
    },

    // 删除
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteRiskAssessment(row.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
            this.getStatistics()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 风险预警
    handleRiskAlert() {
      this.alertDialogVisible = true
    },

    // 保存预警设置
    saveAlertSettings() {
      this.$message.success('预警设置保存成功')
      this.alertDialogVisible = false
    },

    // 导出报告
    async handleExport() {
      try {
        const response = await exportRiskReport(this.listQuery)
        if (response.code === 1) {
          this.$message.success('导出成功')
          // 这里可以处理文件下载
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 查看详情
    handleView(row) {
      getRiskAssessmentDetail({ id: row.id }).then(response => {
        if (response.code === 1) {
          this.currentDetail = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.message || '获取详情失败')
        }
      })
    },

    // 辅助方法
    getPartnerTypeName(type) {
      const typeMap = {
        'BANK': '银行',
        'FINANCIAL': '金融机构',
        'SUPPLIER': '供应商',
        'CUSTOMER': '客户',
        'OTHER': '其他'
      }
      return typeMap[type] || type
    },

    getPartnerTypeColor(type) {
      const colorMap = {
        'BANK': 'primary',
        'FINANCIAL': 'success',
        'SUPPLIER': 'warning',
        'CUSTOMER': 'info',
        'OTHER': ''
      }
      return colorMap[type] || ''
    },

    getRiskLevelColor(level) {
      const colorMap = {
        'AAA': 'success',
        'AA': 'success',
        'A': 'primary',
        'BBB': 'warning',
        'BB': 'warning',
        'B': 'danger',
        'C': 'danger'
      }
      return colorMap[level] || ''
    },

    getRiskStatusColor(status) {
      const colorMap = {
        'NORMAL': 'success',
        'WATCH': 'warning',
        'WARNING': 'danger',
        'RESTRICTED': 'danger',
        'PROHIBITED': 'info'
      }
      return colorMap[status] || ''
    },

    getRiskStatusName(status) {
      const nameMap = {
        'NORMAL': '正常',
        'WATCH': '关注',
        'WARNING': '预警',
        'RESTRICTED': '限制',
        'PROHIBITED': '禁止'
      }
      return nameMap[status] || status
    },

    getAssessmentTypeColor(type) {
      const colorMap = {
        'INITIAL': 'primary',
        'PERIODIC': 'success',
        'TEMPORARY': 'warning',
        'ANNUAL': 'info'
      }
      return colorMap[type] || ''
    },

    getAssessmentTypeName(type) {
      const nameMap = {
        'INITIAL': '初始评估',
        'PERIODIC': '定期评估',
        'TEMPORARY': '临时评估',
        'ANNUAL': '年度评估'
      }
      return nameMap[type] || type
    }
  }
}
</script>
