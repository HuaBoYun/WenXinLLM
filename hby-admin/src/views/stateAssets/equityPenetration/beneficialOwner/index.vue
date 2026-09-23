<template>
  <div class="app-container equity-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-user-solid"></i><span>实际控制人识别</span></div>
      <div class="page-header-desc">识别与验证企业实际控制人及控制路径</div>
    </div>
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-user-solid" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.totalOwners || 0 }}
              </div>
              <div class="statistics-label">实际控制人总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-check" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.actualControllerCount || 0 }}
              </div>
              <div class="statistics-label">实际控制人数(≥50%)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.riskOwners || 0 }}
              </div>
              <div class="statistics-label">高风险控制人(≥80%)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-info" style="color: #909399"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ Math.round(statistics.avgControlRatio || 0) }}%
              </div>
              <div class="statistics-label">平均控制比例</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行统计卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-user" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.individualCount || 0 }}
              </div>
              <div class="statistics-label">个人控制人</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-office-building" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.enterpriseCount || 0 }}
              </div>
              <div class="statistics-label">企业控制人</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-school" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.governmentCount || 0 }}
              </div>
              <div class="statistics-label">政府控制人</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-warning-outline" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.mediumRiskCount || 0 }}
              </div>
              <div class="statistics-label">中风险(50%-80%)</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="mb-20">
      <el-form
        :model="queryForm"
        ref="queryForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="企业名称">
          <el-input
            v-model="queryForm.enterpriseName"
            placeholder="请输入企业名称"
            clearable
            style="width: 200px"
            @keyup.enter.native="handleQuery"
          ></el-input>
        </el-form-item>
        <el-form-item label="控制人类型">
          <el-select
            v-model="queryForm.ownerType"
            placeholder="请选择控制人类型"
            clearable
            style="width: 150px"
          >
            <el-option label="个人" value="INDIVIDUAL"></el-option>
            <el-option label="企业" value="ENTERPRISE"></el-option>
            <el-option label="机构" value="INSTITUTION"></el-option>
            <el-option label="政府" value="GOVERNMENT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="控制方式">
          <el-select
            v-model="queryForm.controlMethod"
            placeholder="请选择控制方式"
            clearable
            style="width: 150px"
          >
            <el-option label="股权控制" value="SHAREHOLDING"></el-option>
            <el-option label="协议控制" value="AGREEMENT"></el-option>
            <el-option label="表决权控制" value="VOTING_RIGHT"></el-option>
            <el-option label="混合控制" value="MIXED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="控制比例">
          <el-input
            v-model="queryForm.minControlRatio"
            placeholder="最小比例"
            style="width: 100px"
          ></el-input>
          <span style="margin: 0 10px">-</span>
          <el-input
            v-model="queryForm.maxControlRatio"
            placeholder="最大比例"
            style="width: 100px"
          ></el-input>
        </el-form-item>
        <el-form-item label="验证状态">
          <el-select
            v-model="queryForm.verificationStatus"
            placeholder="请选择验证状态"
            clearable
            style="width: 120px"
          >
            <el-option label="待确认" value="PENDING"></el-option>
            <el-option label="已确认" value="CONFIRMED"></el-option>
            <el-option label="已拒绝" value="REJECTED"></el-option>
            <el-option label="有争议" value="DISPUTED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">
            查询
          </el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">
            新增识别
          </el-button>
          <el-button
            type="warning"
            @click="handleBatchIdentify"
            icon="el-icon-user-solid"
            :disabled="!multipleSelection.length"
          >
            批量识别
          </el-button>
          <el-button type="info" @click="handleExport" icon="el-icon-download">
            导出
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="controlledEnterpriseName"
          label="企业名称"
          min-width="200"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="controllerEnterpriseName"
          label="实际控制人"
          min-width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="controllerType"
          label="控制人类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getOwnerTypeTag(scope.row.controllerType)">
              {{ getOwnerTypeText(scope.row.controllerType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="controlMethod"
          label="控制方式"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getControlMethodTag(scope.row.controlMethod)">
              {{ getControlMethodText(scope.row.controlMethod) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="totalShareholdingRatio"
          label="控制比例"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.totalShareholdingRatio"
              :color="getControlRatioColor(scope.row.totalShareholdingRatio)"
              :show-text="false"
              style="width: 60px"
            ></el-progress>
            <span style="margin-left: 10px">{{ scope.row.totalShareholdingRatio }}%</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="controlLevel"
          label="控制路径"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              size="mini"
              :type="scope.row.controlLevel > 3 ? 'warning' : 'success'"
            >
              {{ scope.row.controlLevel }}层
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="confirmationStatus"
          label="验证状态"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              :type="getVerificationStatusTag(scope.row.confirmationStatus)"
            >
              {{ getVerificationStatusText(scope.row.confirmationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="controlRiskLevel"
          label="风险等级"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              :type="getRiskLevelTag(scope.row.controlRiskLevel)"
              v-if="scope.row.controlRiskLevel"
            >
              {{ getRiskLevelText(scope.row.controlRiskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="identificationTime"
          label="识别时间"
          width="160"
          align="center"
        >
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.identificationTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleView(scope.row)"
              icon="el-icon-view"
            >
              查看
            </el-button>
            <el-button
              size="mini"
              type="primary"
              @click="handleIdentify(scope.row)"
              icon="el-icon-user-solid"
            >
              识别
            </el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleViewMap(scope.row)"
              icon="el-icon-share"
            >
              控制图
            </el-button>
            <el-dropdown @command="handleCommand" style="margin-left: 10px">
              <el-button size="mini" type="info">
                更多
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :command="{ action: 'edit', row: scope.row }"
                  icon="el-icon-edit"
                >
                  编辑
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'verify', row: scope.row }"
                  icon="el-icon-check"
                >
                  验证身份
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'trace', row: scope.row }"
                  icon="el-icon-guide"
                >
                  追溯来源
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'history', row: scope.row }"
                  icon="el-icon-time"
                >
                  变更历史
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'report', row: scope.row }"
                  icon="el-icon-document"
                >
                  生成报告
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'delete', row: scope.row }"
                  icon="el-icon-delete"
                  divided
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryForm.pageNumber"
        :limit.sync="queryForm.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 实际控制人对话框 -->
    <BeneficialOwnerDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @refresh="getList"
    />

    <!-- 控制图谱对话框 -->
    <BeneficialOwnerMapDialog
      :visible.sync="mapDialogVisible"
      :owner-data="currentRow"
    />

    <!-- 身份验证对话框 -->
    <OwnerVerificationDialog
      :visible.sync="verificationDialogVisible"
      :owner-data="currentRow"
      @refresh="getList"
    />

    <!-- 控制追溯对话框 -->
    <ControlTraceDialog
      :visible.sync="traceDialogVisible"
      :owner-data="currentRow"
    />

    <!-- 变更历史对话框 -->
    <OwnerHistoryDialog
      :visible.sync="historyDialogVisible"
      :owner-data="currentRow"
    />

    <!-- 生成报告对话框 -->
    <ReportDialog
      :visible.sync="reportDialogVisible"
      :owner-data="currentRow"
    />
  </div>
</template>

<script>
  import {
    getBeneficialOwnerList,
    deleteBeneficialOwner,
    identifyBeneficialOwner,
    getBeneficialOwnerStatistics,
    batchUpdateBeneficialOwner,
    exportBeneficialOwnerData,
  } from '@/api/stateAssets/beneficialOwner'
  import Pagination from '@/components/Pagination'
import { mapGetters } from 'vuex'
  import BeneficialOwnerDialog from './components/BeneficialOwnerDialog'
  import BeneficialOwnerMapDialog from './components/BeneficialOwnerMapDialog'
  import OwnerVerificationDialog from './components/OwnerVerificationDialog'
  import ControlTraceDialog from './components/ControlTraceDialog'
  import OwnerHistoryDialog from './components/OwnerHistoryDialog'
  import ReportDialog from './components/ReportDialog'
  import { investThemeMixin } from '../../themeMixin'

  export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
    name: 'BeneficialOwner',
    mixins: [investThemeMixin],
    components: {
      Pagination,
      BeneficialOwnerDialog,
      BeneficialOwnerMapDialog,
      OwnerVerificationDialog,
      ControlTraceDialog,
      OwnerHistoryDialog,
      ReportDialog,
    },
    data() {
      return {
        loading: false,
        tableData: [],
        total: 0,
        multipleSelection: [],
        statistics: {},
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          enterpriseId: '',
          enterpriseName: '',
          ownerType: '',
          controlMethod: '',
          minControlRatio: '',
          maxControlRatio: '',
          verificationStatus: '',
        },
        dialogVisible: false,
        mapDialogVisible: false,
        verificationDialogVisible: false,
        traceDialogVisible: false,
        historyDialogVisible: false,
        reportDialogVisible: false,
        dialogType: 'add',
        currentRow: {},
      }
    },
    created() {
      this.getList()
      this.getStatistics()
    },
    methods: {
      /** 获取列表数据 */
      getList() {
        this.loading = true
        getBeneficialOwnerList(this.queryForm).then(res => {
          if (res.result === 200) {
            this.tableData = res.data.tlist || []
            this.total = res.data.totalRecord || 0
          } else {
            this.$message.error(res.msg || '查询失败')
          }
        }).catch(() => {
          this.$message.error('请求失败，请检查网络')
        }).finally(() => {
          this.loading = false
        })
      },

      /** 获取统计数据 */
      getStatistics() {
        getBeneficialOwnerStatistics().then(res => {
          if (res.result === 200) {
            this.statistics = res.data || {}
          }
        }).catch(() => {})
      },

      // 查询
      handleQuery() {
        this.queryForm.pageNumber = 1
        this.getList()
      },

      // 重置查询
      resetQuery() {
        this.$refs.queryForm.resetFields()
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          enterpriseId: '',
          enterpriseName: '',
          ownerType: '',
          controlMethod: '',
          minControlRatio: '',
          maxControlRatio: '',
          verificationStatus: '',
        }
        this.getList()
      },

      // 新增
      handleAdd() {
        this.currentRow = {}
        this.dialogType = 'add'
        this.dialogVisible = true
      },

      // 查看详情
      handleView(row) {
        this.currentRow = { ...row }
        this.dialogType = 'view'
        this.dialogVisible = true
      },

      // 查看控制图
      handleViewMap(row) {
        this.currentRow = { ...row }
        this.mapDialogVisible = true
      },

      /** 执行识别 */
      handleIdentify(row) {
        this.$confirm('确认对该企业执行实际控制人识别？', '提示', {
          confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning',
        }).then(() => {
          const loading = this.$loading({ lock: true, text: '正在执行实际控制人识别...', background: 'rgba(0, 0, 0, 0.7)' })
          identifyBeneficialOwner({ controllerId: row.controllerId, enterpriseId: row.controlledEnterpriseId }).then(res => {
            if (res.result === 200) {
              this.$message.success(`「${row.controlledEnterpriseName}」实际控制人识别完成`)
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(res.msg || '识别失败')
            }
          }).catch(() => {
            this.$message.error('识别请求失败')
          }).finally(() => {
            loading.close()
          })
        }).catch(() => {})
      },

      /** 批量识别 */
      handleBatchIdentify() {
        if (!this.multipleSelection.length) { this.$message.warning('请选择要识别的记录'); return }
        this.$confirm(`确认对选中的${this.multipleSelection.length}条记录执行批量识别？`, '提示', {
          confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning',
        }).then(() => {
          const loading = this.$loading({ lock: true, text: '正在执行批量识别...', background: 'rgba(0, 0, 0, 0.7)' })
          const ids = this.multipleSelection.map(item => item.controllerId)
          // 调用批量识别专用接口
          batchUpdateBeneficialOwner({ ids }).then(res => {
            if (res.result === 200) {
              this.$message.success(`批量识别完成，共识别 ${this.multipleSelection.length} 条记录`)
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(res.msg || '批量识别失败')
            }
          }).catch(() => {
            this.$message.error('批量识别请求失败')
          }).finally(() => {
            loading.close()
          })
        }).catch(() => {})
      },

      /** 导出数据 */
      handleExport() {
        const loading = this.$loading({ lock: true, text: '正在导出数据...', background: 'rgba(0, 0, 0, 0.7)' })
        exportBeneficialOwnerData(this.queryForm).then(res => {
          // 拦截器对 blob 响应返回整个 axios response 对象，真正的 blob 在 res.data
          const blobData = res && res.data ? res.data : res
          if (!blobData) {
            this.$message.error('导出失败：未收到文件数据')
            return
          }
          // 如果后端返回的是 JSON 错误（content-type 不是 xlsx），读取错误信息
          const contentType = res && res.headers && res.headers['content-type']
          if (contentType && contentType.includes('application/json')) {
            const reader = new FileReader()
            reader.onload = () => {
              try {
                const errObj = JSON.parse(reader.result)
                this.$message.error('导出失败：' + (errObj.msg || '服务器错误'))
              } catch (e) {
                this.$message.error('导出失败')
              }
            }
            reader.readAsText(blobData)
            return
          }
          // 正常下载
          const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = '实际控制人数据.xlsx'
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        }).catch((err) => {
          console.error('导出失败', err)
          this.$message.error('导出失败，请检查网络或联系管理员')
        }).finally(() => {
          loading.close()
        })
      },

      // 下拉菜单命令处理
      handleCommand(command) {
        const { action, row } = command
        switch (action) {
          case 'edit':
            this.currentRow = { ...row }
            this.dialogType = 'edit'
            this.dialogVisible = true
            break
          case 'verify':
            this.currentRow = { ...row }
            this.verificationDialogVisible = true
            break
          case 'trace':
            this.currentRow = { ...row }
            this.traceDialogVisible = true
            break
          case 'history':
            this.currentRow = { ...row }
            this.historyDialogVisible = true
            break
          case 'report':
            this.handleGenerateReport(row)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      },

      // 生成报告 - 打开报告弹窗
      handleGenerateReport(row) {
        this.currentRow = { ...row }
        this.reportDialogVisible = true
      },

      /** 删除 */
      handleDelete(row) {
        this.$confirm('确认删除该实际控制人记录？', '提示', {
          confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning',
        }).then(() => {
          deleteBeneficialOwner({ controllerId: row.controllerId }).then(res => {
            if (res.result === 200) {
              this.$message.success('删除成功')
              this.getList()
              this.getStatistics()
            } else {
              this.$message.error(res.msg || '删除失败')
            }
          }).catch(() => {
            this.$message.error('删除请求失败')
          })
        }).catch(() => {})
      },

      // 多选变化
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },

      // 排序变化
      handleSortChange({ column, prop, order }) {
        this.queryForm.orderBy = prop
        this.queryForm.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
        this.getList()
      },

      // 获取控制人类型标签
      getOwnerTypeTag(type) {
        const tagMap = {
          ENTERPRISE: 'success',
          INDIVIDUAL: 'primary',
          GOVERNMENT: 'danger',
          INSTITUTION: 'warning',
          FUND: '',
          TRUST: 'info',
        }
        return tagMap[type] || 'info'
      },

      // 获取控制人类型文本
      getOwnerTypeText(type) {
        const textMap = {
          ENTERPRISE: '企业',
          INDIVIDUAL: '个人',
          GOVERNMENT: '政府',
          INSTITUTION: '机构',
          FUND: '基金',
          TRUST: '信托',
        }
        return textMap[type] || type
      },

      // 获取控制方式标签
      getControlMethodTag(method) {
        const tagMap = {
          SHAREHOLDING: 'success',
          VOTING_RIGHT: 'success',
          AGREEMENT: 'primary',
          MANAGEMENT: 'warning',
          FINANCIAL: 'warning',
          OPERATIONAL: 'warning',
          MIXED: '',
          BOARD_CONTROL: 'primary',
          TRUST: 'danger',
          OTHER: 'info',
        }
        return tagMap[method] || 'info'
      },

      // 获取控制方式文本
      getControlMethodText(method) {
        const textMap = {
          SHAREHOLDING: '股权控制',
          VOTING_RIGHT: '表决权控制',
          AGREEMENT: '协议控制',
          MANAGEMENT: '管理控制',
          FINANCIAL: '财务控制',
          OPERATIONAL: '运营控制',
          MIXED: '混合控制',
          BOARD_CONTROL: '董事会控制',
          TRUST: '信托控制',
          OTHER: '其他',
        }
        return textMap[method] || method
      },

      // 获取验证状态标签
      getVerificationStatusTag(status) {
        const tagMap = {
          CONFIRMED: 'success',
          PENDING: 'info',
          REJECTED: 'danger',
          DISPUTED: 'warning',
        }
        return tagMap[status] || 'info'
      },

      // 获取验证状态文本
      getVerificationStatusText(status) {
        const textMap = {
          CONFIRMED: '已确认',
          PENDING: '待确认',
          REJECTED: '已拒绝',
          DISPUTED: '有争议',
        }
        return textMap[status] || status
      },

      // 获取风险等级标签
      getRiskLevelTag(level) {
        const tagMap = {
          LOW: 'success',
          MEDIUM: 'warning',
          HIGH: 'danger',
          CRITICAL: 'danger',
        }
        return tagMap[level] || 'info'
      },

      // 获取风险等级文本
      getRiskLevelText(level) {
        const textMap = {
          LOW: '低',
          MEDIUM: '中',
          HIGH: '高',
          CRITICAL: '严重',
        }
        return textMap[level] || level
      },

      // 获取控制比例颜色
      getControlRatioColor(ratio) {
        if (ratio >= 80) return '#F56C6C'
        if (ratio >= 60) return '#E6A23C'
        if (ratio >= 40) return '#409EFF'
        return '#67C23A'
      },

      // 格式化日期时间
      formatDateTime(dateStr) {
        if (!dateStr) return '—'
        const date = new Date(dateStr)
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        const seconds = String(date.getSeconds()).padStart(2, '0')
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      },
    },
  }
</script>

<style lang="scss" scoped>
  .equity-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
  .page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
  .page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
  .page-header-desc { font-size: 13px; opacity: 0.85; }
  ::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
  ::v-deep .el-card { border-radius: 6px; }

  .statistics-card { margin-bottom: 20px; }
  .statistics-content { display: flex; align-items: center; }
  .statistics-icon { font-size: 40px; margin-right: 20px; }
  .statistics-info { flex: 1; }
  .statistics-number { font-size: 24px; font-weight: bold; color: #303133; line-height: 1; }
  .statistics-label { font-size: 14px; color: #909399; margin-top: 5px; }
  .mb-20 { margin-bottom: 20px; }
</style>
