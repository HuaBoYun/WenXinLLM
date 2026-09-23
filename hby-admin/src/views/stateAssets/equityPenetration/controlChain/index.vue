<template>
  <div class="app-container equity-page" :style="themeVars">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-link"></i><span>控制链分析</span></div>
      <div class="page-header-desc">分析企业控制链路径、强度与环路检测</div>
    </div>
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-connection" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.totalChains || 0 }}
              </div>
              <div class="statistics-label">控制链总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-cpu" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.avgStrength || 0 }}%
              </div>
              <div class="statistics-label">平均控制强度</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-refresh" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">
                {{ statistics.loopCount || 0 }}
              </div>
              <div class="statistics-label">控制环路</div>
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
                {{ statistics.riskChains || 0 }}
              </div>
              <div class="statistics-label">风险控制链</div>
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
        <el-form-item label="控制类型">
          <el-select
            v-model="queryForm.controlType"
            placeholder="请选择控制类型"
            clearable
            style="width: 150px"
          >
            <el-option label="直接控制" value="DIRECT"></el-option>
            <el-option label="间接控制" value="INDIRECT"></el-option>
            <el-option label="混合控制" value="MIXED"></el-option>
            <el-option label="代理控制" value="PROXY"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="控制强度">
          <el-input
            v-model="queryForm.minStrength"
            placeholder="最小强度"
            style="width: 100px"
          ></el-input>
          <span style="margin: 0 10px">-</span>
          <el-input
            v-model="queryForm.maxStrength"
            placeholder="最大强度"
            style="width: 100px"
          ></el-input>
        </el-form-item>
        <el-form-item label="链路长度">
          <el-input
            v-model.number="queryForm.maxLength"
            placeholder="最大长度"
            clearable
            style="width: 120px"
            @keyup.enter.native="handleQuery"
          ></el-input>
        </el-form-item>
        <el-form-item label="稳定性">
          <el-select
            v-model="queryForm.stabilityLevel"
            placeholder="请选择稳定性"
            clearable
            style="width: 120px"
          >
            <el-option label="高稳定" value="HIGH"></el-option>
            <el-option label="中稳定" value="MEDIUM"></el-option>
            <el-option label="低稳定" value="LOW"></el-option>
            <el-option label="不稳定" value="UNSTABLE"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">
            查询
          </el-button>
          <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">
            新增分析
          </el-button>
          <el-button
            type="warning"
            @click="handleBatchAnalyze"
            icon="el-icon-data-analysis"
            :disabled="!multipleSelection.length"
          >
            批量分析
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
          prop="enterpriseName"
          label="企业名称"
          min-width="200"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="controllerName"
          label="控制方"
          min-width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="controlType"
          label="控制类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getControlTypeTag(scope.row.controlType)">
              {{ getControlTypeText(scope.row.controlType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="controlStrength"
          label="控制强度"
          width="120"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.controlStrength"
              :color="getStrengthColor(scope.row.controlStrength)"
              :show-text="false"
              style="width: 60px"
            ></el-progress>
            <span style="margin-left: 10px">
              {{ scope.row.controlStrength }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="chainLength"
          label="链路长度"
          width="100"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            <el-tag
              size="mini"
              :type="scope.row.chainLength > 5 ? 'danger' : 'primary'"
            >
              {{ scope.row.chainLength }}级
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="nodeCount"
          label="节点数量"
          width="100"
          align="center"
          sortable="custom"
        ></el-table-column>
        <el-table-column
          prop="stabilityLevel"
          label="稳定性"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getStabilityTag(scope.row.stabilityLevel)">
              {{ getStabilityText(scope.row.stabilityLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="hasLoop"
          label="环路检测"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.hasLoop ? 'danger' : 'success'"
              size="mini"
            >
              {{ scope.row.hasLoop ? '存在' : '无' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="riskLevel"
          label="风险等级"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              :type="getRiskLevelTag(scope.row.riskLevel)"
              v-if="scope.row.riskLevel"
            >
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="lastAnalysisTime"
          label="最后分析时间"
          width="160"
          align="center"
        ></el-table-column>
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
              @click="handleViewMap(scope.row)"
              icon="el-icon-share"
            >
              图谱
            </el-button>
            <el-button
              size="mini"
              type="success"
              @click="handleAnalyze(scope.row)"
              icon="el-icon-data-analysis"
            >
              分析
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
                  :command="{ action: 'paths', row: scope.row }"
                  icon="el-icon-guide"
                >
                  控制路径
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'simulate', row: scope.row }"
                  icon="el-icon-cpu"
                >
                  模拟变更
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'optimize', row: scope.row }"
                  icon="el-icon-magic-stick"
                >
                  优化建议
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

    <!-- 控制链分析对话框 -->
    <ControlChainDialog
      :visible.sync="dialogVisible"
      :form-data="currentRow"
      :dialog-type="dialogType"
      @refresh="getList"
    />

    <!-- 控制链图谱对话框 -->
    <ControlChainMapDialog
      :visible.sync="mapDialogVisible"
      :chain-data="currentRow"
    />

    <!-- 控制路径对话框 -->
    <ControlPathDialog
      :visible.sync="pathDialogVisible"
      :chain-data="currentRow"
    />

    <!-- 变更模拟对话框 -->
    <ControlSimulateDialog
      :visible.sync="simulateDialogVisible"
      :chain-data="currentRow"
    />

    <!-- 优化建议对话框 -->
    <ControlOptimizeDialog
      :visible.sync="optimizeDialogVisible"
      :chain-data="currentRow"
    />

    <!-- 分析报告对话框 -->
    <ControlReportDialog
      :visible.sync="reportDialogVisible"
      :chain-data="currentRow"
    />
  </div>
</template>

<script>
  import {
    getControlChainList,
    deleteControlChain,
    getControlChainStatistics,
    batchUpdateControlChain,
    exportControlChainData,
  } from '@/api/stateAssets/controlChain'
  import Pagination from '@/components/Pagination'
import { mapGetters } from 'vuex'
  import ControlChainDialog from './components/ControlChainDialog'
  import ControlChainMapDialog from './components/ControlChainMapDialog'
  import ControlPathDialog from './components/ControlPathDialog'
  import ControlSimulateDialog from './components/ControlSimulateDialog'
  import ControlOptimizeDialog from './components/ControlOptimizeDialog'
  import ControlReportDialog from './components/ControlReportDialog'
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
    name: 'ControlChain',
    mixins: [investThemeMixin],
    components: {
      Pagination,
      ControlChainDialog,
      ControlChainMapDialog,
      ControlPathDialog,
      ControlSimulateDialog,
      ControlOptimizeDialog,
      ControlReportDialog,
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
          controlType: '',
          minStrength: '',
          maxStrength: '',
          maxLength: '',
          stabilityLevel: '',
        },
        dialogVisible: false,
        mapDialogVisible: false,
        pathDialogVisible: false,
        simulateDialogVisible: false,
        optimizeDialogVisible: false,
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
      // 获取列表数据
      async getList() {
        this.loading = true
        try {
          // 构建请求参数，过滤掉空值避免后端误判
          const params = {
            pageNumber: this.queryForm.pageNumber,
            pageSize: this.queryForm.pageSize,
          }
          if (this.queryForm.enterpriseId) params.enterpriseId = this.queryForm.enterpriseId
          if (this.queryForm.enterpriseName) params.enterpriseName = this.queryForm.enterpriseName
          if (this.queryForm.controlType) params.controlType = this.queryForm.controlType
          if (this.queryForm.minStrength) params.minStrength = this.queryForm.minStrength
          if (this.queryForm.maxStrength) params.maxStrength = this.queryForm.maxStrength
          if (this.queryForm.maxLength) params.maxLength = this.queryForm.maxLength
          if (this.queryForm.stabilityLevel) params.stabilityLevel = this.queryForm.stabilityLevel
          if (this.queryForm.orderBy) params.orderBy = this.queryForm.orderBy
          if (this.queryForm.orderDirection) params.orderDirection = this.queryForm.orderDirection

          const response = await getControlChainList(params)
          if (response && response.result === 200) {
            this.tableData = response.data?.tlist || response.data?.list || []
            this.total = response.data?.totalRecord || response.data?.total || 0
          } else {
            this.tableData = []
            this.total = 0
            console.warn('获取控制链列表失败:', response?.msg)
          }
        } catch (error) {
          console.error('获取控制链列表异常:', error)
          this.tableData = []
          this.total = 0
        } finally {
          this.loading = false
        }
      },

      // 获取统计数据
      async getStatistics() {
        try {
          const response = await getControlChainStatistics({})
          if (response && response.result === 200) {
            this.statistics = {
              totalChains: response.data?.totalChains || 0,
              avgStrength: response.data?.avgStrength || 0,
              loopCount: response.data?.loopCount || 0,
              riskChains: response.data?.riskChains || 0,
            }
          } else {
            this.statistics = { totalChains: 0, avgStrength: 0, loopCount: 0, riskChains: 0 }
          }
        } catch (error) {
          console.error('获取统计数据异常:', error)
          this.statistics = { totalChains: 0, avgStrength: 0, loopCount: 0, riskChains: 0 }
        }
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
          controlType: '',
          minStrength: '',
          maxStrength: '',
          maxLength: '',
          stabilityLevel: '',
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

      // 查看图谱
      handleViewMap(row) {
        this.currentRow = { ...row }
        this.mapDialogVisible = true
      },

      // 执行分析
      handleAnalyze(row) {
        this.$confirm('确认对该控制链执行深度分析？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行控制链分析...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })

          batchUpdateControlChain({
            chainIds: [row.chainId],
            action: 'analyze',
          })
            .then((response) => {
              loading.close()
              if (response && response.result === 200) {
                this.$message.success('控制链分析完成')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.warning(response?.msg || '分析失败')
              }
            })
            .catch((error) => {
              loading.close()
              console.error('分析失败:', error)
              this.$message.error('分析请求失败')
            })
        }).catch(() => {})
      },

      // 批量分析
      handleBatchAnalyze() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要分析的记录')
          return
        }
        this.$confirm(
          `确认对选中的${this.multipleSelection.length}条记录执行批量分析？`,
          '提示',
          { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
        ).then(() => {
          const loading = this.$loading({
            lock: true,
            text: '正在执行批量分析...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)',
          })

          const chainIds = this.multipleSelection.map(
            (item) => item.chainId
          )
          batchUpdateControlChain({
            chainIds,
            action: 'analyze',
            updateBy: this.$store.getters?.name || 'system',
          })
            .then((response) => {
              loading.close()
              if (response && response.result === 200) {
                this.$message.success('批量分析任务已提交')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.warning(response?.msg || '批量分析失败')
              }
            })
            .catch((error) => {
              loading.close()
              console.error('批量分析失败:', error)
              this.$message.error('批量分析请求失败')
            })
        }).catch(() => {})
      },

      // 导出数据
      handleExport() {
        const loading = this.$loading({
          lock: true,
          text: '正在导出数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
        })

        // 构建导出参数（只传有值的筛选条件）
        const params = {}
        if (this.queryForm.enterpriseName) params.enterpriseName = this.queryForm.enterpriseName
        if (this.queryForm.controlType) params.controlType = this.queryForm.controlType
        if (this.queryForm.minStrength) params.minStrength = this.queryForm.minStrength
        if (this.queryForm.maxStrength) params.maxStrength = this.queryForm.maxStrength
        if (this.queryForm.stabilityLevel) params.stabilityLevel = this.queryForm.stabilityLevel

        exportControlChainData(params)
          .then((res) => {
            loading.close()
            // 响应拦截器对blob返回整个response对象，数据在res.data中
            const blobData = res.data || res
            if (blobData) {
              const blob = new Blob([blobData], { type: 'application/vnd.ms-excel;charset=UTF-8' })
              const url = window.URL.createObjectURL(blob)
              const link = document.createElement('a')
              link.href = url
              link.download = `控制链分析数据_${new Date().getTime()}.csv`
              link.click()
              window.URL.revokeObjectURL(url)
              this.$message.success('导出成功')
            }
          })
          .catch((error) => {
            loading.close()
            console.error('导出失败:', error)
            this.$message.error('导出失败')
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
          case 'paths':
            this.currentRow = { ...row }
            this.pathDialogVisible = true
            break
          case 'simulate':
            this.currentRow = { ...row }
            this.simulateDialogVisible = true
            break
          case 'optimize':
            this.currentRow = { ...row }
            this.optimizeDialogVisible = true
            break
          case 'report':
            this.handleGenerateReport(row)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      },

      // 生成报告
      handleGenerateReport(row) {
        this.currentRow = { ...row }
        this.reportDialogVisible = true
      },

      // 删除
      handleDelete(row) {
        this.$confirm('确认删除该控制链分析记录？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          deleteControlChain(row.chainId)
            .then((response) => {
              if (response && response.result === 200) {
                this.$message.success('删除成功')
                this.getList()
                this.getStatistics()
              } else {
                this.$message.warning(response?.msg || '删除失败')
              }
            })
            .catch((error) => {
              console.error('删除失败:', error)
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

      // 获取控制类型标签
      getControlTypeTag(type) {
        const tagMap = {
          DIRECT: 'success',
          INDIRECT: 'primary',
          MIXED: 'warning',
          PROXY: 'danger',
        }
        return tagMap[type] || 'info'
      },

      // 获取控制类型文本
      getControlTypeText(type) {
        const textMap = {
          DIRECT: '直接控制',
          INDIRECT: '间接控制',
          MIXED: '混合控制',
          PROXY: '代理控制',
        }
        return textMap[type] || type
      },

      // 获取稳定性标签
      getStabilityTag(level) {
        const tagMap = {
          STABLE: 'success',
          RELATIVELY_STABLE: 'primary',
          HIGH: 'success',
          MEDIUM: 'primary',
          LOW: 'warning',
          UNSTABLE: 'danger',
        }
        return tagMap[level] || 'info'
      },

      // 获取稳定性文本
      getStabilityText(level) {
        const textMap = {
          STABLE: '稳定',
          RELATIVELY_STABLE: '相对稳定',
          HIGH: '高稳定',
          MEDIUM: '中稳定',
          LOW: '低稳定',
          UNSTABLE: '不稳定',
        }
        return textMap[level] || level
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
          LOW: '低风险',
          MEDIUM: '中风险',
          HIGH: '高风险',
          CRITICAL: '严重',
        }
        return textMap[level] || level
      },

      // 获取控制强度颜色
      getStrengthColor(strength) {
        if (strength >= 80) return '#F56C6C'
        if (strength >= 60) return '#E6A23C'
        if (strength >= 40) return '#409EFF'
        return '#67C23A'
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
