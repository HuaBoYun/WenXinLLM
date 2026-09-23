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
          <p class="page-description">
            管理合作伙伴风险评估，包括风险等级、评估指标、预警机制和风控措施
          </p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增评估
          </el-button>
          <el-button
            type="warning"
            icon="el-icon-warning"
            @click="handleRiskAlert"
          >
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
              style="width: 150px"
              clearable
            />
          </el-form-item>
          <el-form-item label="伙伴名称">
            <el-input
              v-model="listQuery.partnerName"
              placeholder="请输入伙伴名称"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px"
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
              style="width: 120px"
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
              style="width: 120px"
            >
              <el-option label="初始评估" value="INITIAL" />
              <el-option label="定期评估" value="PERIODIC" />
              <el-option label="临时评估" value="TEMPORARY" />
              <el-option label="年度评估" value="ANNUAL" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              @click="handleFilter"
            >
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
      style="width: 100%"
    >
      <el-table-column
        label="伙伴编码"
        prop="partnerCode"
        sortable="custom"
        align="center"
        width="120"
      >
        <template slot-scope="{ row }">
          <span>{{ row.partnerCode }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="伙伴名称"
        width="200px"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          <span>{{ row.partnerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="伙伴类型" width="120px" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="getPartnerTypeColor(row.partnerType)" size="small">
            {{ getPartnerTypeName(row.partnerType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="100px" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
            {{ row.riskLevel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险评分" width="100px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.riskScore }}</span>
        </template>
      </el-table-column>
      <el-table-column label="风险状态" width="100px" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="getRiskStatusColor(row.riskStatus)" size="small">
            {{ getRiskStatusName(row.riskStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="评估类型" width="100px" align="center">
        <template slot-scope="{ row }">
          <el-tag
            :type="getAssessmentTypeColor(row.assessmentType)"
            size="small"
          >
            {{ getAssessmentTypeName(row.assessmentType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最近评估时间" width="180px" align="center">
        <template slot-scope="{ row }">
          <span>
            {{ row.lastAssessmentDate | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="下次评估时间" width="180px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.nextAssessmentDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{ row }">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="230"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row, $index }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="row.status != 'deleted'"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.pageNo"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />
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
  } from '@/api/globalTreasurer/partnerDirectConnectionPartnerRiskManage'

  export default {
    name: 'PartnerRiskManage',
    components: { Pagination },
    directives: { waves },
    data() {
      return {
        tableKey: 0,
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          pageNo: 1,
          pageSize: 20,
          partnerCode: undefined,
          partnerName: undefined,
          riskLevel: undefined,
          riskStatus: undefined,
          assessmentType: undefined,
        },
        // 统计数据
        totalRiskAssessments: 0,
        highRiskPartners: 0,
        mediumRiskPartners: 0,
        lowRiskPartners: 0,
        // 对话框相关
        dialogVisible: false,
        dialogStatus: '',
        dialogTitle: '',
        formLabelWidth: '120px',
        activeTab: 'basic',
        // 表单数据
        tempAssessment: {
          assessmentId: undefined,
          partnerId: undefined,
          partnerCode: '',
          partnerName: '',
          assessmentType: 'INITIAL',
          riskLevel: 'LOW',
          riskScore: undefined,
          assessmentDate: undefined,
          assessorId: undefined,
          assessorName: '',
          nextAssessmentDate: undefined,
          riskStatus: 'NORMAL',
          financialStabilityScore: undefined,
          creditHistoryScore: undefined,
          businessReputationScore: undefined,
          complianceRiskScore: undefined,
          overallRiskScore: undefined,
          riskFactors: '',
          mitigationMeasures: '',
          assessmentReportUrl: '',
        },
        // 表单验证规则
        rules: {
          partnerId: [
            { required: true, message: '请选择合作伙伴', trigger: 'change' },
          ],
          assessmentType: [
            { required: true, message: '请选择评估类型', trigger: 'change' },
          ],
          riskLevel: [
            { required: true, message: '请选择风险等级', trigger: 'change' },
          ],
          riskScore: [
            {
              required: true,
              type: 'number',
              message: '请输入风险评分',
              trigger: 'blur',
            },
          ],
          assessmentDate: [
            { required: true, message: '请选择评估日期', trigger: 'change' },
          ],
        },
        // 风险预警列表
        riskAlerts: [],
      }
    },
    created() {
      this.getRiskStatistics()
      this.getRiskAlerts()
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        getRiskAssessmentList(this.listQuery)
          .then((response) => {
            if (response.code === 1) {
              this.list = response.data.tlist || []
              this.total = response.data.totalRecord || 0
            } else {
              this.$message.error(response.message || '查询失败')
            }
            this.listLoading = false
          })
          .catch(() => {
            // 使用模拟数据用于演示
            this.list = [
              {
                assessmentId: 1,
                partnerId: 1,
                partnerCode: 'PARTNER001',
                partnerName: '中国工商银行股份有限公司',
                partnerType: 'BANK',
                riskLevel: 'LOW',
                riskScore: 85.5,
                riskStatus: 'NORMAL',
                assessmentType: 'ANNUAL',
                lastAssessmentDate: '2024-09-15 14:30:00',
                nextAssessmentDate: '2025-09-15',
                isEnabled: 1,
              },
              {
                assessmentId: 2,
                partnerId: 2,
                partnerCode: 'PARTNER002',
                partnerName: '中国建设银行股份有限公司',
                partnerType: 'BANK',
                riskLevel: 'MEDIUM',
                riskScore: 65.2,
                riskStatus: 'WATCH',
                assessmentType: 'PERIODIC',
                lastAssessmentDate: '2024-10-20 10:15:00',
                nextAssessmentDate: '2025-01-20',
                isEnabled: 1,
              },
            ]
            this.total = 2
            this.listLoading = false
          })
      },
      getRiskStatistics() {
        getRiskStatistics()
          .then((response) => {
            if (response.code === 1) {
              const stats = response.data
              this.totalRiskAssessments = stats.totalRiskAssessments || 0
              this.highRiskPartners = stats.highRiskPartners || 0
              this.mediumRiskPartners = stats.mediumRiskPartners || 0
              this.lowRiskPartners = stats.lowRiskPartners || 0
            }
          })
          .catch(() => {
            // 使用默认统计数据
            this.totalRiskAssessments = 245
            this.highRiskPartners = 15
            this.mediumRiskPartners = 68
            this.lowRiskPartners = 162
          })
      },
      getRiskAlerts() {
        getRiskAlerts()
          .then((response) => {
            if (response.code === 1) {
              this.riskAlerts = response.data || []
            }
          })
          .catch(() => {
            // 使用默认预警数据
            this.riskAlerts = [
              {
                partnerName: '某某供应商',
                riskLevel: 'HIGH',
                alertType: '信用评级下降',
                alertDate: '2024-12-01',
              },
              {
                partnerName: '某某银行',
                riskLevel: 'MEDIUM',
                alertType: '评估即将到期',
                alertDate: '2024-12-15',
              },
            ]
          })
      },
      handleFilter() {
        this.listQuery.pageNo = 1
        this.getList()
      },
      handleReset() {
        this.listQuery = {
          pageNo: 1,
          pageSize: 20,
          partnerCode: undefined,
          partnerName: undefined,
          riskLevel: undefined,
          riskStatus: undefined,
          assessmentType: undefined,
        }
        this.getList()
      },
      resetTemp() {
        this.tempAssessment = {
          assessmentId: undefined,
          partnerId: undefined,
          partnerCode: '',
          partnerName: '',
          assessmentType: 'INITIAL',
          riskLevel: 'LOW',
          riskScore: undefined,
          assessmentDate: undefined,
          assessorId: undefined,
          assessorName: '',
          nextAssessmentDate: undefined,
          riskStatus: 'NORMAL',
          financialStabilityScore: undefined,
          creditHistoryScore: undefined,
          businessReputationScore: undefined,
          complianceRiskScore: undefined,
          overallRiskScore: undefined,
          riskFactors: '',
          mitigationMeasures: '',
          assessmentReportUrl: '',
        }
      },
      handleCreate() {
        this.resetTemp()
        this.dialogStatus = 'create'
        this.dialogTitle = '新增风险评估'
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['assessmentForm'] &&
            this.$refs['assessmentForm'].clearValidate()
        })
      },
      handleUpdate(row) {
        this.tempAssessment = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogTitle = '编辑风险评估'
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs['assessmentForm'] &&
            this.$refs['assessmentForm'].clearValidate()
        })
      },
      createData() {
        this.$refs['assessmentForm'].validate((valid) => {
          if (valid) {
            const assessmentData = Object.assign({}, this.tempAssessment)
            delete assessmentData.assessmentId

            addRiskAssessment(assessmentData)
              .then((response) => {
                if (response.code === 1) {
                  this.dialogVisible = false
                  this.$notify({
                    title: '成功',
                    message: '新增成功',
                    type: 'success',
                    duration: 2000,
                  })
                  this.getList()
                  this.getRiskStatistics()
                } else {
                  this.$message.error(response.message || '新增失败')
                }
              })
              .catch(() => {
                this.$message.error('新增失败')
              })
          }
        })
      },
      updateData() {
        this.$refs['assessmentForm'].validate((valid) => {
          if (valid) {
            const assessmentData = Object.assign({}, this.tempAssessment)

            updateRiskAssessment(assessmentData)
              .then((response) => {
                if (response.code === 1) {
                  this.dialogVisible = false
                  this.$notify({
                    title: '成功',
                    message: '更新成功',
                    type: 'success',
                    duration: 2000,
                  })
                  this.getList()
                  this.getRiskStatistics()
                } else {
                  this.$message.error(response.message || '更新失败')
                }
              })
              .catch(() => {
                this.$message.error('更新失败')
              })
          }
        })
      },
      handleDelete(row, index) {
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deleteRiskAssessment({ assessmentId: row.assessmentId })
            .then((response) => {
              if (response.code === 1) {
                this.$notify({
                  title: '成功',
                  message: '删除成功',
                  type: 'success',
                  duration: 2000,
                })
                this.list.splice(index, 1)
                this.total--
                this.getRiskStatistics()
              } else {
                this.$message.error(response.message || '删除失败')
              }
            })
            .catch(() => {
              this.$message.error('删除失败')
            })
        })
      },
      handleExport() {
        this.$message.info('导出功能开发中...')
        // exportRiskReport(this.listQuery).then(response => {
        //   if (response.code === 1) {
        //     window.open(response.data.exportPath)
        //   } else {
        //     this.$message.error(response.message || '导出失败')
        //   }
        // })
      },
      handleRiskAlert() {
        if (this.riskAlerts.length > 0) {
          this.$alert(
            `<div>
            ${this.riskAlerts
              .map(
                (alert) =>
                  `<div style="margin-bottom: 10px; padding: 10px; border: 1px solid #ddd; border-radius: 4px;">
                <strong>${alert.partnerName}</strong> - ${alert.alertType}<br>
                <small>风险等级: ${this.getRiskLevelName(
                  alert.riskLevel
                )} | 时间: ${alert.alertDate}</small>
              </div>`
              )
              .join('')}
          </div>`,
            '风险预警信息',
            { dangerouslyUseHTMLString: true }
          )
        } else {
          this.$message.success('暂无风险预警信息')
        }
      },
      handleBatchAssess() {
        this.$message.info('批量评估功能开发中...')
        // const partnerIds = this.list.filter(item => item.isSelected).map(item => item.partnerId)
        // if (partnerIds.length === 0) {
        //   this.$message.warning('请选择要评估的合作伙伴')
        //   return
        // }
        // batchRiskAssessment({ partnerIds: partnerIds, assessmentType: 'PERIODIC' }).then(response => {
        //   if (response.code === 1) {
        //     this.$message.success('批量评估成功')
        //     this.getList()
        //     this.getRiskStatistics()
        //   } else {
        //     this.$message.error(response.message || '批量评估失败')
        //   }
        // })
      },
      getPartnerTypeName(type) {
        const typeMap = {
          BANK: '银行',
          FINANCIAL: '金融机构',
          SUPPLIER: '供应商',
          CUSTOMER: '客户',
          OTHER: '其他',
        }
        return typeMap[type] || type
      },
      getPartnerTypeColor(type) {
        const colorMap = {
          BANK: 'primary',
          FINANCIAL: 'success',
          SUPPLIER: 'warning',
          CUSTOMER: 'info',
          OTHER: 'default',
        }
        return colorMap[type] || 'default'
      },
      getRiskLevelName(level) {
        const levelMap = {
          AAA: 'AAA级',
          AA: 'AA级',
          A: 'A级',
          BBB: 'BBB级',
          BB: 'BB级',
          B: 'B级',
          C: 'C级',
          LOW: '低风险',
          MEDIUM: '中风险',
          HIGH: '高风险',
        }
        return levelMap[level] || level
      },
      getRiskLevelColor(level) {
        const colorMap = {
          AAA: 'success',
          AA: 'success',
          A: 'primary',
          BBB: 'primary',
          BB: 'warning',
          B: 'warning',
          C: 'danger',
          LOW: 'success',
          MEDIUM: 'warning',
          HIGH: 'danger',
        }
        return colorMap[level] || 'default'
      },
      getRiskStatusName(status) {
        const statusMap = {
          NORMAL: '正常',
          WATCH: '关注',
          WARNING: '预警',
          RESTRICTED: '限制',
          PROHIBITED: '禁止',
        }
        return statusMap[status] || status
      },
      getRiskStatusColor(status) {
        const colorMap = {
          NORMAL: 'success',
          WATCH: 'warning',
          WARNING: 'danger',
          RESTRICTED: 'warning',
          PROHIBITED: 'danger',
        }
        return colorMap[status] || 'default'
      },
      getAssessmentTypeName(type) {
        const typeMap = {
          INITIAL: '初始评估',
          PERIODIC: '定期评估',
          TEMPORARY: '临时评估',
          ANNUAL: '年度评估',
        }
        return typeMap[type] || type
      },
      getAssessmentTypeColor(type) {
        const colorMap = {
          INITIAL: 'primary',
          PERIODIC: 'success',
          TEMPORARY: 'warning',
          ANNUAL: 'info',
        }
        return colorMap[type] || 'default'
      },
    },
  }
</script>

<style scoped lang="scss">
  @import './partnerManage.scss';

  /* 页面特定样式 */
  .partner-risk-manage {
    padding: 20px;
    min-height: calc(100vh - 84px);
  }

  .risk-overview .overview-card {
    height: 120px;
  }
</style>
