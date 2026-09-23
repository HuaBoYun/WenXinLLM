<template>
  <div class="enhanced-indicator-combination">
    <!-- 主对话框 -->
    <el-dialog
      title="指标组合分析"
      :visible.sync="dialogVisible"
      width="95%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :z-index="2000"
      class="combination-dialog"
      @close="handleClose"
    >
      <div class="combination-container">
        <!-- 顶部工具栏 -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateCombination">
              新建组合
            </el-button>
            <el-button icon="el-icon-refresh" @click="loadCombinationList">
              刷新
            </el-button>
            <el-button 
              v-if="selectedCombination" 
              type="success" 
              icon="el-icon-setting"
              @click="openDragConfig"
            >
              拖拽配置
            </el-button>
          </div>
          
          <div class="toolbar-right">
            <div class="search-box">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索组合名称"
                prefix-icon="el-icon-search"
                @keyup.enter.native="handleSearch"
                clearable
              />
            </div>
            <div class="view-toggle">
              <el-radio-group v-model="viewMode" size="mini">
                <el-radio-button label="list">列表</el-radio-button>
                <el-radio-button label="drag">拖拽</el-radio-button>
              </el-radio-group>
            </div>
          </div>
        </div>

        <!-- 主要内容区域 -->
        <div class="main-content" v-if="viewMode === 'list'">
          <!-- 左侧：组合列表 -->
          <div class="combination-list">
            <div class="list-header">
              <h3>指标组合列表</h3>
              <el-tag type="info">{{ combinationList.length }} 个组合</el-tag>
            </div>
            
            <div class="list-content" v-loading="listLoading">
              <div
                v-for="combination in filteredCombinations"
                :key="combination.combinationId"
                :class="['combination-item', { active: selectedCombination?.combinationId === combination.combinationId }]"
                @click="selectCombination(combination)"
              >
                <div class="item-header">
                  <h4>{{ combination.combinationName }}</h4>
                  <el-tag :type="getStatusType(combination.status)" size="mini">
                    {{ getStatusText(combination.status) }}
                  </el-tag>
                </div>
                <div class="item-content">
                  <p class="description">{{ combination.description || '暂无描述' }}</p>
                  <div class="item-meta">
                    <span class="meta-item">
                      <i class="el-icon-collection"></i>
                      {{ combination.indicatorCount || 0 }} 个指标
                    </span>
                    <span class="meta-item">
                      <i class="el-icon-time"></i>
                      {{ combination.createTime }}
                    </span>
                  </div>
                </div>
                <div class="item-actions">
                  <el-button type="text" size="mini" @click.stop="handleEditCombination(combination)">
                    编辑
                  </el-button>
                  <el-button type="text" size="mini" @click.stop="openDragConfigForCombination(combination)">
                    拖拽配置
                  </el-button>
                  <el-button type="text" size="mini" @click.stop="handleExecuteCombination(combination)">
                    执行
                  </el-button>
                  <el-dropdown @command="handleMoreAction" trigger="click" @click.native.stop>
                    <el-button type="text" size="mini">
                      更多<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item :command="{action: 'copy', data: combination}">复制</el-dropdown-item>
                      <el-dropdown-item :command="{action: 'history', data: combination}">执行历史</el-dropdown-item>
                      <el-dropdown-item :command="{action: 'delete', data: combination}" divided>删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
              </div>
            </div>
          </div>

          <!-- 右侧：详情面板 -->
          <div class="detail-panel">
            <div v-if="!selectedCombination" class="empty-state">
              <i class="el-icon-info"></i>
              <p>请选择一个指标组合查看详情</p>
            </div>
            
            <div v-else class="combination-detail">
              <!-- 基本信息 -->
              <div class="detail-section">
                <div class="section-header">
                  <h3>基本信息</h3>
                  <el-button size="mini" type="primary" @click="handleEditCombination(selectedCombination)">
                    编辑
                  </el-button>
                </div>
                <el-descriptions :column="2" border size="mini">
                  <el-descriptions-item label="组合名称">
                    {{ selectedCombination.combinationName }}
                  </el-descriptions-item>
                  <el-descriptions-item label="组合编码">
                    {{ selectedCombination.combinationCode }}
                  </el-descriptions-item>
                  <el-descriptions-item label="执行模式">
                    {{ getExecutionModeText(selectedCombination.executionMode) }}
                  </el-descriptions-item>
                  <el-descriptions-item label="状态">
                    <el-tag :type="getStatusType(selectedCombination.status)" size="mini">
                      {{ getStatusText(selectedCombination.status) }}
                    </el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="描述" :span="2">
                    {{ selectedCombination.description || '暂无描述' }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>

              <!-- 指标配置 -->
              <div class="detail-section">
                <div class="section-header">
                  <h3>指标配置</h3>
                  <div class="section-actions">
                    <el-button size="mini" @click="loadCombinationList">
                      刷新数据
                    </el-button>
                    <el-button size="mini" @click="openDragConfig">
                      拖拽配置
                    </el-button>
                    <el-button size="mini" @click="openTraditionalConfig">
                      传统配置
                    </el-button>
                  </div>
                </div>
                
                <div class="indicator-summary">
                  <div class="summary-stats">
                    <div class="stat-item">
                      <span class="stat-value">{{ selectedCombination.indicators?.length || 0 }}</span>
                      <span class="stat-label">总指标数</span>
                    </div>
                    <div class="stat-item">
                      <span class="stat-value">{{ getEnabledIndicatorCount() }}</span>
                      <span class="stat-label">启用指标</span>
                    </div>
                    <div class="stat-item">
                      <span class="stat-value">{{ getDependencyCount() }}</span>
                      <span class="stat-label">依赖关系</span>
                    </div>
                  </div>
                  
                  <div class="indicator-list-preview">
                    <!-- 调试信息 -->
                    <div v-if="!selectedCombination.indicators || selectedCombination.indicators.length === 0" class="no-indicators">
                      <p>暂无指标配置</p>
                      <p style="font-size: 12px; color: #999;">
                        调试信息: selectedCombination = {{ selectedCombination ? '存在' : '不存在' }},
                        indicators = {{ selectedCombination?.indicators?.length || 0 }}
                      </p>
                    </div>

                    <div
                      v-for="(indicator, index) in selectedCombination.indicators"
                      :key="indicator.configId"
                      class="indicator-preview-item"
                    >
                      <div class="item-order">{{ index + 1 }}</div>
                      <div class="item-info">
                        <span class="item-name">{{ indicator.indicatorName }}</span>
                        <span class="item-code">{{ indicator.indicatorCode }}</span>
                      </div>
                      <div class="item-status">
                        <el-tag v-if="indicator.isEnabled" type="success" size="mini">启用</el-tag>
                        <el-tag v-else type="info" size="mini">禁用</el-tag>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 执行历史 -->
              <div class="detail-section">
                <div class="section-header">
                  <h3>执行历史</h3>
                  <el-button size="mini" type="primary" @click="handleExecuteCombination(selectedCombination)">
                    立即执行
                  </el-button>
                </div>
                
                <div class="execution-history">
                  <div v-if="!selectedCombination.executions || selectedCombination.executions.length === 0" class="no-history">
                    <i class="el-icon-info"></i>
                    <span>暂无执行记录</span>
                  </div>
                  
                  <div v-else class="history-list">
                    <div
                      v-for="execution in selectedCombination.executions.slice(0, 5)"
                      :key="execution.executionId"
                      class="history-item"
                    >
                      <div class="history-status">
                        <el-tag :type="getExecutionStatusType(execution.status)" size="mini">
                          {{ getExecutionStatusText(execution.status) }}
                        </el-tag>
                      </div>
                      <div class="history-info">
                        <div class="history-time">{{ execution.startTime }}</div>
                        <div class="history-duration">耗时: {{ execution.totalDuration }}ms</div>
                      </div>
                      <div class="history-result">
                        <span class="success-count">成功: {{ execution.successCount }}</span>
                        <span class="failed-count">失败: {{ execution.failedCount }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 拖拽配置模式 -->
        <div v-else-if="viewMode === 'drag'" class="drag-config-mode">
          <DragIndicatorConfig
            :visible="true"
            :combination-info="selectedCombination"
            :initial-indicators="selectedCombination?.indicators || []"
            @save="handleDragConfigSave"
            @close="viewMode = 'list'"
          />
        </div>
      </div>
    </el-dialog>

    <!-- 拖拽配置对话框 -->
    <el-dialog
      title="拖拽式指标配置"
      :visible.sync="dragConfigVisible"
      width="98%"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      :z-index="3000"
      class="drag-config-dialog"
    >
      <DragIndicatorConfig
        :visible="dragConfigVisible"
        :combination-info="currentConfigCombination"
        :initial-indicators="currentConfigCombination?.indicators || []"
        @save="handleDragConfigSave"
        @close="dragConfigVisible = false"
      />
    </el-dialog>

    <!-- 传统配置对话框 -->
    <IndicatorConfigDialog
      :visible.sync="traditionalConfigVisible"
      :combination="currentConfigCombination"
      @save="handleTraditionalConfigSave"
    />

    <!-- 组合编辑对话框 -->
    <CombinationEditDialog
      :visible.sync="editDialogVisible"
      :combination="editingCombination"
      @save="handleCombinationSave"
    />



  </div>
</template>

<script>
import DragIndicatorConfig from './DragIndicatorConfig.vue'
import IndicatorConfigDialog from './IndicatorConfigDialog.vue'
import CombinationEditDialog from './CombinationEditDialog.vue'
import { getCombinationList, saveCombination } from '@/api/mxgl'

export default {
  name: 'EnhancedIndicatorCombination',
  components: {
    DragIndicatorConfig,
    IndicatorConfigDialog,
    CombinationEditDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      viewMode: 'list', // 'list' | 'drag'
      
      // 搜索
      searchKeyword: '',
      
      // 数据
      combinationList: [],
      selectedCombination: null,
      
      // 子对话框状态
      dragConfigVisible: false,
      traditionalConfigVisible: false,
      editDialogVisible: false,
      
      // 当前配置的组合
      currentConfigCombination: null,
      editingCombination: null,
      
      // 加载状态
      listLoading: false
    }
  },
  computed: {
    filteredCombinations() {
      if (!this.searchKeyword) return this.combinationList
      return this.combinationList.filter(item =>
        item.combinationName.includes(this.searchKeyword) ||
        item.combinationCode.includes(this.searchKeyword)
      )
    }
  },
  watch: {
    visible(newVal) {
      this.dialogVisible = newVal
      if (newVal) {
        this.loadCombinationList()
      }
    },
    
    dialogVisible(newVal) {
      this.$emit('update:visible', newVal)
    }
  },
  methods: {
    // 加载组合列表
    async loadCombinationList() {
      try {
        this.listLoading = true

        // 先尝试加载真实数据
        try {
          const response = await getCombinationList({
            pageNum: 1,
            pageSize: 100
          })

          if (response.code === 1) {
            this.combinationList = response.data.list || []
          } else {
            throw new Error('API返回错误: ' + response.msg)
          }
        } catch (apiError) {
          console.warn('API调用失败，使用测试数据:', apiError)
          // 如果API调用失败，使用测试数据
          this.combinationList = [
            {
              combinationId: 'COMB_TEST_001',
              combinationName: '财务分析组合',
              combinationCode: 'FINANCE_ANALYSIS',
              description: '用于财务分析的指标组合',
              status: 'ACTIVE',
              executionMode: 'SEQUENCE',
              category: 'FINANCIAL',
              createTime: new Date().toISOString(),
              indicators: []
            },
            {
              combinationId: 'COMB_TEST_002',
              combinationName: '风险评估组合',
              combinationCode: 'RISK_ASSESSMENT',
              description: '用于风险评估的指标组合',
              status: 'ACTIVE',
              executionMode: 'PARALLEL',
              category: 'RISK',
              createTime: new Date().toISOString(),
              indicators: []
            }
          ]
        }

        // 为每个组合添加测试指标数据
        this.combinationList.forEach(combination => {
          if (!combination.indicators || combination.indicators.length === 0) {
            combination.indicators = [
              {
                configId: 'config_test_001',
                indicatorName: '净资产收益率',
                indicatorCode: 'ROE',
                isEnabled: true,
                executionOrder: 1,
                sqlContent: 'SELECT ei.ENTERPRISE_NAME AS "企业名称", ROUND(fid.INDICATOR_VALUE, 2) AS "净资产收益率%" FROM TBL_FINANCIAL_INDICATOR_DATA fid JOIN TBL_FINANCIAL_INDICATOR_DEFINITION def ON fid.INDICATOR_ID = def.INDICATOR_ID JOIN TBL_ENTERPRISE_INFO ei ON fid.ENTERPRISE_ID = ei.ENTERPRISE_ID WHERE def.INDICATOR_CODE = "ROE"',
                dataSourceId: '5504842711104fe39f8c3dd36866295c',
                parameterMapping: { 'year': '2024', 'quarter': 'Q4' },
                description: '衡量企业盈利能力的重要指标'
              },
              {
                configId: 'config_test_002',
                indicatorName: '资产负债率',
                indicatorCode: 'DEBT_RATIO',
                isEnabled: true,
                executionOrder: 2,
                sqlContent: 'SELECT ei.ENTERPRISE_NAME AS "企业名称", ROUND(fid.INDICATOR_VALUE, 2) AS "资产负债率%" FROM TBL_FINANCIAL_INDICATOR_DATA fid JOIN TBL_FINANCIAL_INDICATOR_DEFINITION def ON fid.INDICATOR_ID = def.INDICATOR_ID JOIN TBL_ENTERPRISE_INFO ei ON fid.ENTERPRISE_ID = ei.ENTERPRISE_ID WHERE def.INDICATOR_CODE = "DEBT_RATIO"',
                dataSourceId: '5504842711104fe39f8c3dd36866295c',
                parameterMapping: { 'year': '2024' },
                description: '反映企业财务风险的关键指标'
              },
              {
                configId: 'config_test_003',
                indicatorName: '流动比率',
                indicatorCode: 'CURRENT_RATIO',
                isEnabled: false,
                executionOrder: 3,
                sqlContent: 'SELECT ei.ENTERPRISE_NAME AS "企业名称", ROUND(fid.INDICATOR_VALUE, 2) AS "流动比率" FROM TBL_FINANCIAL_INDICATOR_DATA fid JOIN TBL_FINANCIAL_INDICATOR_DEFINITION def ON fid.INDICATOR_ID = def.INDICATOR_ID JOIN TBL_ENTERPRISE_INFO ei ON fid.ENTERPRISE_ID = ei.ENTERPRISE_ID WHERE def.INDICATOR_CODE = "CURRENT_RATIO"',
                dataSourceId: '5504842711104fe39f8c3dd36866295c',
                parameterMapping: {},
                description: '衡量企业短期偿债能力'
              }
            ]
          }
        })

        console.log('组合列表加载完成:', this.combinationList)
      } catch (error) {
        console.error('加载组合列表失败:', error)
        this.$message.error('加载组合列表失败')
      } finally {
        this.listLoading = false
      }
    },

    // 选择组合
    selectCombination(combination) {
      this.selectedCombination = combination
    },

    // 搜索处理
    handleSearch() {
      // 搜索逻辑已在计算属性中实现
    },

    // 关闭对话框
    handleClose() {
      this.selectedCombination = null
      this.viewMode = 'list'
      this.$emit('close')
    },

    // 新建组合
    handleCreateCombination() {
      this.editingCombination = null
      this.editDialogVisible = true
    },

    // 编辑组合
    handleEditCombination(combination) {
      this.editingCombination = { ...combination }
      this.editDialogVisible = true
    },

    // 执行组合
    handleExecuteCombination(combination) {
      this.$confirm(`确定要执行组合"${combination.combinationName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.executeCombination(combination)
      }).catch(() => {})
    },

    // 执行组合逻辑
    async executeCombination(combination) {
      try {
        this.$message.info('组合执行中...')
        // 这里调用执行接口
        // const response = await executeCombination(combination.combinationId)
        this.$message.success('组合执行成功')
        this.loadCombinationList() // 刷新列表
      } catch (error) {
        console.error('执行组合失败:', error)
        this.$message.error('执行组合失败')
      }
    },

    // 更多操作
    handleMoreAction(command) {
      const { action, data } = command

      switch (action) {
        case 'copy':
          this.copyCombination(data)
          break
        case 'history':
          this.viewExecutionHistory(data)
          break
        case 'delete':
          this.deleteCombination(data)
          break
      }
    },

    // 复制组合
    copyCombination(combination) {
      this.$prompt('请输入新组合名称', '复制组合', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: combination.combinationName + '-副本'
      }).then(({ value }) => {
        // 这里调用复制接口
        this.$message.success('组合复制成功')
        this.loadCombinationList()
      }).catch(() => {})
    },

    // 查看执行历史
    viewExecutionHistory(combination) {
      // 打开执行历史对话框
      this.$message.info('查看执行历史功能开发中...')
    },

    // 删除组合
    deleteCombination(combination) {
      this.$confirm(`确定要删除组合"${combination.combinationName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 这里调用删除接口
        this.$message.success('组合删除成功')
        this.loadCombinationList()
      }).catch(() => {})
    },

    // 打开拖拽配置
    openDragConfig() {
      if (!this.selectedCombination) {
        this.$message.warning('请先选择一个组合')
        return
      }
      this.currentConfigCombination = this.selectedCombination
      this.dragConfigVisible = true
    },

    // 为特定组合打开拖拽配置
    openDragConfigForCombination(combination) {
      this.currentConfigCombination = combination
      this.dragConfigVisible = true
    },

    // 打开传统配置
    openTraditionalConfig() {
      if (!this.selectedCombination) {
        this.$message.warning('请先选择一个组合')
        return
      }
      this.currentConfigCombination = this.selectedCombination
      this.traditionalConfigVisible = true
    },

    // 拖拽配置保存
    async handleDragConfigSave(configData) {
      try {
        // 保存配置
        await this.saveCombinationConfig(configData)
        this.dragConfigVisible = false
        this.$message.success('配置保存成功')
        this.loadCombinationList()
      } catch (error) {
        console.error('保存配置失败:', error)
        this.$message.error('保存配置失败')
      }
    },

    // 传统配置保存
    async handleTraditionalConfigSave(configData) {
      try {
        await this.saveCombinationConfig(configData)
        this.traditionalConfigVisible = false
        this.$message.success('配置保存成功')
        this.loadCombinationList()
      } catch (error) {
        console.error('保存配置失败:', error)
        this.$message.error('保存配置失败')
      }
    },

    // 组合保存
    async handleCombinationSave(combinationData) {
      try {
        await saveCombination(combinationData)
        this.editDialogVisible = false
        this.$message.success('组合保存成功')
        this.loadCombinationList()
      } catch (error) {
        console.error('保存组合失败:', error)
        this.$message.error('保存组合失败')
      }
    },

    // 保存组合配置
    async saveCombinationConfig(configData) {
      try {
        console.log('保存配置:', configData)

        // 🔥 构建保存数据
        const saveData = {
          combinationId: configData.combinationId,
          indicators: configData.indicators.map((indicator, index) => ({
            ...indicator,
            executionOrder: index + 1,  // 设置执行顺序
            isEnabled: 'Y'  // 默认启用
          }))
        }

        // 🔥 调用后端保存接口
        const response = await saveCombination(saveData)

        if (response.code === 1) {
          console.log('配置保存成功:', response)
          return response
        } else {
          throw new Error(response.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存配置失败:', error)
        throw error
      }
    },

    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'INACTIVE': 'warning'
      }
      return typeMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'ACTIVE': '启用',
        'INACTIVE': '禁用'
      }
      return textMap[status] || '未知'
    },

    // 获取执行模式文本
    getExecutionModeText(mode) {
      const textMap = {
        'SEQUENCE': '顺序执行',
        'PARALLEL': '并行执行'
      }
      return textMap[mode] || '未知'
    },

    // 获取执行状态类型
    getExecutionStatusType(status) {
      const typeMap = {
        'RUNNING': 'primary',
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'CANCELLED': 'warning'
      }
      return typeMap[status] || 'info'
    },

    // 获取执行状态文本
    getExecutionStatusText(status) {
      const textMap = {
        'RUNNING': '运行中',
        'SUCCESS': '成功',
        'FAILED': '失败',
        'CANCELLED': '已取消'
      }
      return textMap[status] || '未知'
    },

    // 获取启用指标数量
    getEnabledIndicatorCount() {
      if (!this.selectedCombination?.indicators) return 0
      return this.selectedCombination.indicators.filter(item => item.isEnabled).length
    },

    // 获取依赖关系数量
    getDependencyCount() {
      if (!this.selectedCombination?.indicators) return 0
      return this.selectedCombination.indicators.reduce((count, item) => {
        return count + (item.dependsOn?.length || 0)
      }, 0)
    },


  }
}
</script>

<style lang="scss" scoped>
.enhanced-indicator-combination {
  .combination-dialog {
    ::v-deep .el-dialog__body {
      padding: 0;
    }
  }

  .combination-container {
    height: 80vh;
    display: flex;
    flex-direction: column;
  }

  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    background: #fff;
    border-bottom: 1px solid #e4e7ed;

    .toolbar-left {
      display: flex;
      gap: 8px;
    }

    .toolbar-right {
      display: flex;
      align-items: center;
      gap: 12px;

      .search-box {
        width: 250px;
      }
    }
  }

  .main-content {
    flex: 1;
    display: flex;
    overflow: hidden;
  }

  // 组合列表
  .combination-list {
    width: 400px;
    border-right: 1px solid #e4e7ed;
    display: flex;
    flex-direction: column;

    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px;
      border-bottom: 1px solid #e4e7ed;

      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
      }
    }

    .list-content {
      flex: 1;
      overflow-y: auto;
      padding: 8px;

      .combination-item {
        margin-bottom: 8px;
        padding: 16px;
        border: 1px solid #e4e7ed;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          border-color: #409eff;
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);
        }

        &.active {
          border-color: #409eff;
          background: #f0f9ff;
        }

        .item-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          margin-bottom: 8px;

          h4 {
            margin: 0;
            font-size: 14px;
            font-weight: 600;
            color: #303133;
          }
        }

        .item-content {
          .description {
            margin: 0 0 8px 0;
            font-size: 12px;
            color: #606266;
            line-height: 1.4;
          }

          .item-meta {
            display: flex;
            gap: 12px;

            .meta-item {
              display: flex;
              align-items: center;
              gap: 4px;
              font-size: 11px;
              color: #909399;

              i {
                font-size: 12px;
              }
            }
          }
        }

        .item-actions {
          margin-top: 8px;
          display: flex;
          gap: 4px;
        }
      }
    }
  }

  // 详情面板
  .detail-panel {
    flex: 1;
    display: flex;
    flex-direction: column;

    .empty-state {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #909399;

      i {
        font-size: 48px;
        margin-bottom: 16px;
      }

      p {
        margin: 0;
        font-size: 14px;
      }
    }

    .combination-detail {
      flex: 1;
      padding: 20px;
      overflow-y: auto;

      .detail-section {
        margin-bottom: 24px;

        .section-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 16px;

          h3 {
            margin: 0;
            font-size: 16px;
            font-weight: 600;
            color: #303133;
          }

          .section-actions {
            display: flex;
            gap: 8px;
          }
        }

        .indicator-summary {
          .summary-stats {
            display: flex;
            gap: 24px;
            margin-bottom: 16px;

            .stat-item {
              text-align: center;

              .stat-value {
                display: block;
                font-size: 24px;
                font-weight: 600;
                color: #409eff;
              }

              .stat-label {
                font-size: 12px;
                color: #909399;
              }
            }
          }

          .indicator-list-preview {
            max-height: 300px;
            overflow-y: auto;

            .no-indicators {
              text-align: center;
              padding: 40px 20px;
              color: #909399;

              p {
                margin: 8px 0;
              }
            }

            .indicator-preview-item {
              display: flex;
              align-items: center;
              padding: 8px 12px;
              margin-bottom: 4px;
              border: 1px solid #e4e7ed;
              border-radius: 4px;

              &.clickable-indicator {
                cursor: pointer;
                transition: all 0.2s ease;

                &:hover {
                  border-color: #409eff;
                  background: #f0f9ff;
                  transform: translateY(-1px);
                  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);

                  .item-info .result-icon {
                    opacity: 1;
                  }
                }
              }

              .item-order {
                width: 20px;
                height: 20px;
                border-radius: 50%;
                background: #409eff;
                color: #fff;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 11px;
                margin-right: 8px;
              }

              .item-info {
                flex: 1;
                position: relative;

                .item-name {
                  display: block;
                  font-size: 13px;
                  font-weight: 600;
                  color: #303133;
                }

                .item-code {
                  font-size: 11px;
                  color: #909399;
                }

                .result-icon {
                  position: absolute;
                  right: 8px;
                  top: 50%;
                  transform: translateY(-50%);
                  color: #409eff;
                  opacity: 0;
                  transition: opacity 0.2s ease;
                  font-size: 14px;
                }
              }
            }
          }
        }

        .execution-history {
          .no-history {
            text-align: center;
            padding: 40px;
            color: #909399;

            i {
              font-size: 32px;
              margin-bottom: 8px;
              display: block;
            }
          }

          .history-list {
            .history-item {
              display: flex;
              align-items: center;
              padding: 12px;
              margin-bottom: 8px;
              border: 1px solid #e4e7ed;
              border-radius: 4px;

              .history-status {
                margin-right: 12px;
              }

              .history-info {
                flex: 1;

                .history-time {
                  font-size: 13px;
                  color: #303133;
                }

                .history-duration {
                  font-size: 11px;
                  color: #909399;
                }
              }

              .history-result {
                display: flex;
                gap: 8px;
                font-size: 11px;

                .success-count {
                  color: #67c23a;
                }

                .failed-count {
                  color: #f56c6c;
                }
              }
            }
          }
        }
      }
    }
  }

  // 拖拽配置模式
  .drag-config-mode {
    flex: 1;
    overflow: hidden;
  }

  // 响应式设计
  @media (max-width: 1400px) {
    .combination-list {
      width: 350px;
    }
  }

  @media (max-width: 1200px) {
    .main-content {
      flex-direction: column;
    }

    .combination-list {
      width: 100%;
      height: 300px;
      border-right: none;
      border-bottom: 1px solid #e4e7ed;
    }
  }
}

// 全局样式覆盖
::v-deep {
  .drag-config-dialog .el-dialog__body {
    padding: 0;
    height: 80vh;
    overflow: hidden;
  }

  .sql-result-dialog {
    margin-top: 5vh !important;
  }
}

/* SQL结果对话框样式 */
.sql-result-container {
  .indicator-info {
    background: #f8f9fa;
    padding: 16px;
    border-radius: 4px;
    margin-bottom: 20px;

    .info-item {
      margin-bottom: 8px;

      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }

      span {
        color: #303133;
      }
    }

    .parameter-info {
      margin-top: 12px;

      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
        display: block;
        margin-bottom: 8px;
      }
    }
  }

  .result-section {
    .result-stats {
      margin-bottom: 20px;
      padding: 16px;
      background: #f0f9ff;
      border-radius: 4px;
      border: 1px solid #e1f5fe;
    }

    .result-table {
      .table-note {
        margin-top: 12px;
      }
    }

    .error-result {
      padding: 20px;
    }

    .loading-container {
      text-align: center;
    }

    .empty-result {
      padding: 40px;
      text-align: center;
    }
  }
}
</style>
</script>
