<template>
  <div class="table-query-container">
    <!-- 返回数据画像按钮 - 仅在从数据画像跳转过来时显示 -->
    <div v-if="showBackButtons" class="back-buttons-wrapper">
      <data-portrait-back-buttons />
    </div>

    <!-- 主内容区域 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="title-text">数据库表查询工具</span>
      </div>

      <!-- 查询历史抽屉按钮 - 贴在header下边框 -->
      <div
        class="history-drawer-trigger"
        @mouseenter="showHistoryPopover"
        @mouseleave="hideHistoryPopover"
      >
        <i class="el-icon-d-arrow-right"></i>
        <span class="trigger-text">历史</span>

        <!-- 悬浮显示的历史面板 -->
        <transition name="el-zoom-in-top">
          <div
            v-show="historyPopoverVisible"
            class="history-popover-panel"
            @mouseenter="showHistoryPopover"
            @mouseleave="hideHistoryPopover"
          >
            <div class="history-popover-content">
              <!-- 搜索栏 -->
              <div class="history-popover-toolbar">
                <el-input
                  v-model="tableSearchKeyword"
                  placeholder="搜索表名或备注"
                  prefix-icon="el-icon-search"
                  size="mini"
                  clearable
                  style="width: 100%;"
                />
              </div>

              <!-- 分类列表 -->
              <div class="history-popover-category-list">
                <el-collapse v-model="activeCategories" accordion>
                  <el-collapse-item
                    v-for="category in categorizedTables"
                    :key="category.name"
                    :name="category.name"
                    :class="'category-' + category.name"
                  >
                    <template slot="title">
                      <div class="history-popover-category-header">
                        <i class="el-icon-folder-opened"></i>
                        <span class="category-name">{{ category.name }}</span>
                        <span class="category-count">({{ category.tables.length }})</span>
                      </div>
                    </template>

                    <!-- 可拖拽的表列表 -->
                    <draggable
                      v-model="category.tables"
                      :group="{ name: 'tables', pull: true, put: true }"
                      :animation="200"
                      handle=".drag-handle"
                      tag="div"
                      class="history-popover-table-list"
                      :data-category="category.name"
                      @change="handleDragChange($event, category.name)"
                    >
                      <div
                        v-for="table in category.tables"
                        :key="table.id || table.tableName"
                        class="history-popover-table-item"
                        :data-table-name="table.tableName"
                      >
                        <i class="el-icon-s-operation drag-handle" title="拖拽到其他分类"></i>
                        <i class="el-icon-document"></i>
                        <span class="table-name" @click="selectTableFromHistory(table)">
                          {{ table.tableComment || table.tableName }}
                        </span>
                        <!-- 修改分类下拉框 -->
                        <el-select
                          v-model="table.category"
                          size="mini"
                          placeholder="选择分类"
                          style="width: 100px; margin-left: 5px;"
                          @change="updateTableCategoryDirect(table)"
                          @click.native.stop
                        >
                          <el-option
                            v-for="cat in allCategories"
                            :key="cat"
                            :label="cat"
                            :value="cat"
                          />
                          <el-option label="+ 新建分类" value="__new__" />
                        </el-select>
                      </div>
                    </draggable>

                    <el-empty
                      v-if="category.tables.length === 0"
                      description="暂无数据"
                      :image-size="40"
                    />
                  </el-collapse-item>
                </el-collapse>
              </div>
            </div>
          </div>
        </transition>
      </div>

      <el-row :gutter="20">
        <!-- 查询区域和数据展示 -->
        <el-col :span="24">
          <!-- 查询表单 -->
          <el-card shadow="never" class="query-panel">
            <el-form :model="queryForm" label-width="100px">
              <el-row :gutter="20">
                <el-col :span="16">
                  <el-form-item label="表名">
                    <el-input
                      v-model="queryForm.tableName"
                      placeholder="请输入表名(例如: TBL_USER)"
                      clearable
                      class="table-name-input"
                      @keyup.enter.native="handleQueryTable"
                    >
                      <el-button
                        slot="append"
                        icon="el-icon-search"
                        @click="handleQueryTable"
                      >
                        查询
                      </el-button>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item>
                    <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">
                      刷新数据
                    </el-button>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-card>

          <!-- 字段查询模块 -->
          <el-card v-if="tableInfo.tableName" shadow="never" class="field-query-panel">
            <el-form
              ref="fieldQueryForm"
              :inline="true"
              label-width="0"
              :model="fieldQuery.conditions"
              @submit.native.prevent
            >
              <el-form-item v-for="(item, index) in fieldQuery.visibleFields" :key="index">
                <el-input
                  v-model="fieldQuery.conditions[item.columnName]"
                  :placeholder="item.columnComment || item.columnName"
                  clearable
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="handleFieldQuery"
                >
                  查询
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleResetFieldQuery">
                  重置
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="搜索筛选"
                  placement="top"
                >
                  <el-popover
                    placement="left"
                    trigger="click"
                    popper-class="field-filter-popover"
                  >
                    <filter-search
                      v-if="fieldQuery.allFields.length > 0"
                      :key="fieldQuery.localKey"
                      :list="fieldQuery.allFields"
                      :name="fieldQuery.localKey"
                      @updateSearchShow="initFieldSearch"
                    />
                    <el-button slot="reference" style="height: 32px">
                      <vab-icon icon="filter" :is-custom-svg="true" />
                    </el-button>
                  </el-popover>
                </el-tooltip>
              </el-form-item>
              <el-form-item>
                <span
                  :class="fieldQuery.showMore ? 'search-more is-opened' : 'search-more'"
                  @click="toggleFieldShowMore"
                >
                  <span>{{ fieldQuery.showMore ? '收起' : '展开' }}</span>
                  <i class="el-icon-arrow-down"></i>
                </span>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 表数据展示 -->
          <el-card v-if="tableInfo.tableName" shadow="never" class="data-panel">
            <div slot="header" class="panel-header">
              <i class="el-icon-s-grid"></i>
              <span>{{ tableInfo.tableComment || tableInfo.tableName }} (共 {{ pagination.total }} 条)</span>
              <div class="header-actions">
                <el-button
                  type="primary"
                  size="small"
                  icon="el-icon-download"
                  @click="handleExport"
                >
                  导出
                </el-button>
              </div>
            </div>
            <!-- 总体分析按钮 -->
            <div v-if="tableData.length > 0" style="margin-bottom: 10px; text-align: right;">
              <el-button
                type="warning"
                size="small"
                icon="el-icon-data-analysis"
                @click="handleAnalyzeAllData"
              >
                AI 风险分析 (总体)
              </el-button>
            </div>

            <el-table
              v-loading="dataLoading"
              :data="tableData"
              border
              stripe
              size="small"
              max-height="600"
            >
              <el-table-column
                v-for="column in displayColumns"
                :key="column.columnName"
                :prop="column.columnName"
                :label="column.columnComment || column.columnName"
                min-width="150"
                show-overflow-tooltip
              />
            </el-table>
            <el-pagination
              :current-page="pagination.pageNum"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="pagination.pageSize"
              :total="pagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </el-card>

          <!-- 空状态 -->
          <el-empty
            v-if="!tableInfo.tableName"
            description="请输入表名进行查询"
            :image-size="200"
          />
        </el-col>
      </el-row>
    </el-card>

    <!-- AI风险分析对话框 -->
    <el-dialog
      title="AI 风险分析"
      :visible.sync="aiAnalysisDialogVisible"
      width="800px"
      :close-on-click-modal="false"
      top="5vh"
    >
      <div class="ai-analysis-container">
        <!-- 分析状态 -->
        <div v-if="aiAnalysis.loading" class="analysis-loading">
          <i class="el-icon-loading"></i>
          <span>AI 正在分析数据...</span>
        </div>

        <!-- 分析结果 -->
        <div v-if="aiAnalysis.result" class="analysis-result">
          <div class="result-header">
            <i class="el-icon-warning-outline"></i>
            <span>风险分析报告</span>
          </div>
          <div class="result-content" v-html="formatAnalysisResult(aiAnalysis.result)"></div>
        </div>

        <!-- 错误信息 -->
        <div v-if="aiAnalysis.error" class="analysis-error">
          <i class="el-icon-circle-close"></i>
          <span>{{ aiAnalysis.error }}</span>
        </div>

        <!-- 空状态 -->
        <div v-if="!aiAnalysis.loading && !aiAnalysis.result && !aiAnalysis.error" class="analysis-empty">
          <i class="el-icon-info"></i>
          <span>等待分析...</span>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="aiAnalysisDialogVisible = false">关闭</el-button>
        <el-button
          v-if="aiAnalysis.result"
          type="primary"
          icon="el-icon-document-copy"
          @click="copyAnalysisResult"
        >
          复制结果
        </el-button>
      </div>
    </el-dialog>

    <!-- 新建分类对话框 -->
    <el-dialog
      title="新建分类"
      :visible.sync="newCategoryDialogVisible"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form :model="newCategoryForm" label-width="80px">
        <el-form-item label="分类名称">
          <el-input
            v-model="newCategoryForm.name"
            placeholder="请输入分类名称"
            @keyup.enter.native="confirmNewCategory"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="newCategoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmNewCategory">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  checkTableExists,
  getTableStructure,
  getTableData,
  getQueriedTableList,
  updateTableCategory
} from '@/api/finance/tableQuery'
import draggable from 'vuedraggable'
import filterSearch from '@/components/filterSearch.vue'
import DataPortraitBackButtons from '@/components/DataPortraitBackButtons'
import axios from 'axios'

export default {
  name: 'TableQuery',
  components: {
    draggable,
    filterSearch,
    DataPortraitBackButtons
  },
  data() {
    return {
      // 是否显示返回数据画像按钮
      showBackButtons: false,
      queryForm: {
        tableName: ''
      },
      tableSearchKeyword: '',
      activeTableName: '',
      queriedTableList: [],
      tableInfo: {
        tableName: '',
        tableComment: '',
        columns: []
      },
      tableData: [],
      dataLoading: false,
      pagination: {
        pageNum: 1,
        pageSize: 20,
        total: 0
      },
      // 历史悬浮面板相关
      historyPopoverVisible: false,
      historyPopoverTimer: null, // 用于延迟隐藏
      activeCategories: ['未分类'], // 默认展开未分类
      // 新建分类对话框相关
      newCategoryDialogVisible: false,
      newCategoryForm: {
        name: '',
        targetTable: null // 要修改分类的表
      },
      // 字段查询相关
      fieldQuery: {
        allFields: [], // 所有字段列表 (用于筛选器)
        visibleFields: [], // 当前显示的字段列表
        conditions: {}, // 字段查询条件 { fieldName: value }
        showMore: false, // 是否展开更多
        localKey: 'finance-tableQuery-fieldSearch' // localStorage key
      },

      // AI分析相关
      aiAnalysisDialogVisible: false,
      aiAnalysis: {
        loading: false,
        result: '',
        error: null
      }
    }
  },
  computed: {
    // 按分类组织的表列表
    categorizedTables() {
      const categories = {}
      const keyword = this.tableSearchKeyword.toLowerCase()

      // 过滤表列表
      const filteredList = this.queriedTableList.filter(table => {
        if (!keyword) return true
        return (table.tableName && table.tableName.toLowerCase().includes(keyword)) ||
               (table.tableComment && table.tableComment.toLowerCase().includes(keyword))
      })

      // 按分类分组
      filteredList.forEach(table => {
        const category = table.category || '未分类'
        if (!categories[category]) {
          categories[category] = {
            name: category,
            tables: []
          }
        }
        categories[category].tables.push(table)
      })

      // 转换为数组并排序
      const result = Object.values(categories)
      result.sort((a, b) => {
        if (a.name === '未分类') return 1
        if (b.name === '未分类') return -1
        return a.name.localeCompare(b.name)
      })

      return result
    },
    // 所有分类列表(用于下拉选择)
    allCategories() {
      const categories = new Set()
      this.queriedTableList.forEach(table => {
        if (table.category && table.category !== '未分类') {
          categories.add(table.category)
        }
      })
      const result = Array.from(categories).sort()
      result.push('未分类')
      return result
    },
    displayColumns() {
      return this.tableInfo.columns || []
    }
  },
  async mounted() {
    // 检查是否从数据画像跳转过来
    this.checkFromDataPortrait()

    await this.loadQueriedTableList()
    // 自动查询第一个分类的第一个表
    this.autoQueryFirstTable()
  },
  methods: {
    /**
     * 检查是否从数据画像跳转过来
     * 如果 URL 中包含 from=dataPortrait 或 from=dataPortraitScreen 参数，显示返回按钮
     */
    checkFromDataPortrait() {
      const { from } = this.$route.query
      if (from === 'dataPortrait' || from === 'dataPortraitScreen') {
        this.showBackButtons = true
      }
    },

    // 加载已查询表列表
    async loadQueriedTableList() {
      try {
        const response = await getQueriedTableList()
        if (response.code === 1 || response.code === 200) {
          this.queriedTableList = response.data || []
        }
      } catch (error) {
        console.error('加载已查询表列表失败:', error)
        this.$message.error('加载查询历史失败')
      }
    },

    // 查询表
    async handleQueryTable() {
      if (!this.queryForm.tableName) {
        this.$message.warning('请输入表名')
        return
      }

      const tableName = this.queryForm.tableName.trim().toUpperCase()

      try {
        // 1. 检查表是否存在
        const checkRes = await checkTableExists(tableName)
        if (checkRes.code !== 1 && checkRes.code !== 200) {
          this.$message.error(checkRes.msg || '表不存在')
          return
        }

        if (!checkRes.data || !checkRes.data.exists) {
          this.$message.error(`表 ${tableName} 不存在`)
          return
        }

        // 2. 获取表结构
        const structureRes = await getTableStructure(tableName)
        if (structureRes.code === 1 || structureRes.code === 200) {
          this.tableInfo = structureRes.data || {}
          this.activeTableName = tableName

          // 重置字段查询条件
          this.fieldQuery.conditions = {}

          // 初始化字段列表
          this.initFieldList()

          // 3. 加载表数据
          await this.loadTableData()

          // 4. 更新已查询表列表
          await this.loadQueriedTableList()

          this.$message.success('查询成功')
        } else {
          this.$message.error(structureRes.msg || '获取表结构失败')
        }
      } catch (error) {
        console.error('查询表失败:', error)
        this.$message.error('查询失败')
      }
    },

    // 加载表数据
    async loadTableData() {
      if (!this.tableInfo.tableName) return

      this.dataLoading = true
      try {
        // 构建查询参数
        const params = {
          tableName: this.tableInfo.tableName,
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }

        // 添加字段查询条件
        const fieldConditions = {}
        Object.keys(this.fieldQuery.conditions).forEach(fieldName => {
          const value = this.fieldQuery.conditions[fieldName]
          if (value && value.trim()) {
            fieldConditions[fieldName] = value.trim()
          }
        })

        if (Object.keys(fieldConditions).length > 0) {
          params.fieldConditions = fieldConditions
        }

        const response = await getTableData(params)

        if (response.code === 1 || response.code === 200) {
          const data = response.data || {}
          this.tableData = data.records || data.list || []
          this.pagination.total = data.total || 0
        } else {
          this.$message.error(response.msg || '加载数据失败')
        }
      } catch (error) {
        console.error('加载表数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.dataLoading = false
      }
    },

    // 选择表
    handleTableSelect(tableName) {
      this.queryForm.tableName = tableName
      this.handleQueryTable()
    },

    // 刷新数据
    handleRefresh() {
      if (!this.tableInfo.tableName) {
        this.$message.warning('请先查询表')
        return
      }
      this.pagination.pageNum = 1
      this.loadTableData()
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.pagination.pageNum = 1
      this.loadTableData()
    },

    // 当前页改变
    handleCurrentChange(page) {
      this.pagination.pageNum = page
      this.loadTableData()
    },

    // 导出
    handleExport() {
      this.$message.info('导出功能开发中...')
    },

    // 显示历史对话框
    async showHistoryDialog() {
      this.historyDialogVisible = true
      await this.loadQueriedTableList()
      // 默认展开所有分类
      this.$nextTick(() => {
        this.activeCategories = this.categorizedTables.map(cat => cat.name)
      })
    },

    // 从历史中选择表
    selectTableFromHistory(table) {
      this.queryForm.tableName = table.tableName
      this.historyDialogVisible = false
      this.handleQueryTable()
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
    },

    // 显示历史悬浮面板
    showHistoryPopover() {
      if (this.historyPopoverTimer) {
        clearTimeout(this.historyPopoverTimer)
        this.historyPopoverTimer = null
      }
      this.historyPopoverVisible = true

      // 加载历史数据
      if (this.queriedTableList.length === 0) {
        this.loadQueriedTableList()
      }
    },

    // 隐藏历史悬浮面板
    hideHistoryPopover() {
      // 延迟隐藏,给用户时间移动鼠标到面板上
      this.historyPopoverTimer = setTimeout(() => {
        this.historyPopoverVisible = false
      }, 200)
    },

    // 自动查询第一个分类的第一个表
    autoQueryFirstTable() {
      // 等待数据加载完成
      this.$nextTick(() => {
        if (this.categorizedTables && this.categorizedTables.length > 0) {
          const firstCategory = this.categorizedTables[0]
          if (firstCategory.tables && firstCategory.tables.length > 0) {
            const firstTable = firstCategory.tables[0]
            console.log('自动查询第一个表:', firstTable.tableName)
            this.queryForm.tableName = firstTable.tableName
            this.handleQueryTable()
          }
        }
      })
    },

    // 字段查询相关方法
    // 初始化字段列表
    initFieldList() {
      if (!this.tableInfo.columns || this.tableInfo.columns.length === 0) {
        this.fieldQuery.allFields = []
        this.fieldQuery.visibleFields = []
        return
      }

      // 为每个表设置独立的 localStorage key
      this.fieldQuery.localKey = `finance-tableQuery-fieldSearch-${this.tableInfo.tableName}`

      // 构建所有字段列表
      this.fieldQuery.allFields = this.tableInfo.columns.map(col => ({
        name: col.columnComment || col.columnName,
        key: col.columnName,
        columnName: col.columnName,
        columnComment: col.columnComment,
        show: true // 默认全部显示
      }))

      // 初始化显示字段
      this.initFieldSearch()
    },

    // 初始化字段搜索显示
    initFieldSearch() {
      this.$nextTick(() => {
        const data = localStorage.getItem(this.fieldQuery.localKey)
        let allVisibleFields = []

        if (data) {
          const savedFields = JSON.parse(data)
          // 从保存的配置中恢复字段,并从allFields中获取完整信息
          for (let i = 0; i < savedFields.length; i++) {
            if (savedFields[i].show) {
              // 从allFields中找到对应的字段,保持完整的字段信息
              const fullField = this.fieldQuery.allFields.find(f => f.key === savedFields[i].key)
              if (fullField) {
                allVisibleFields.push(fullField)
              }
            }
          }
        } else {
          allVisibleFields = [...this.fieldQuery.allFields]
        }

        // 重置非展示字段的查询条件
        this.fieldQuery.allFields.forEach(field => {
          if (!allVisibleFields.some(vf => vf.key === field.key)) {
            this.fieldQuery.conditions[field.columnName] = ''
          }
        })

        // 根据展开状态显示字段
        if (this.fieldQuery.showMore) {
          // 展开时显示所有可见字段
          this.fieldQuery.visibleFields = allVisibleFields
        } else {
          // 收起时只显示前4个
          this.fieldQuery.visibleFields = allVisibleFields.slice(0, 4)
        }
      })
    },

    // 切换展开/收起
    toggleFieldShowMore() {
      this.fieldQuery.showMore = !this.fieldQuery.showMore

      // 重新加载可见字段
      const data = localStorage.getItem(this.fieldQuery.localKey)
      let allVisibleFields = []

      if (data) {
        const savedFields = JSON.parse(data)
        // 从保存的配置中恢复字段,并从allFields中获取完整信息
        for (let i = 0; i < savedFields.length; i++) {
          if (savedFields[i].show) {
            const fullField = this.fieldQuery.allFields.find(f => f.key === savedFields[i].key)
            if (fullField) {
              allVisibleFields.push(fullField)
            }
          }
        }
      } else {
        allVisibleFields = [...this.fieldQuery.allFields]
      }

      if (this.fieldQuery.showMore) {
        this.fieldQuery.visibleFields = allVisibleFields
      } else {
        this.fieldQuery.visibleFields = allVisibleFields.slice(0, 4)
      }
    },

    // 重置字段查询
    handleResetFieldQuery() {
      // 清空所有条件
      this.fieldQuery.conditions = {}
      // 重新加载数据(不带查询条件)
      this.pagination.pageNum = 1
      this.loadTableData()
    },

    // 执行字段查询
    handleFieldQuery() {
      // 重置到第一页并加载数据
      this.pagination.pageNum = 1
      this.loadTableData()
    },

    // AI分析相关方法
    // 分析所有数据
    async handleAnalyzeAllData() {
      if (!this.tableData || this.tableData.length === 0) {
        this.$message.warning('当前没有数据可供分析')
        return
      }

      this.aiAnalysisDialogVisible = true
      this.aiAnalysis.loading = true
      this.aiAnalysis.result = ''
      this.aiAnalysis.error = null

      try {
        // 构建分析提示词
        const prompt = this.buildAnalysisPrompt()

        // 调用DeepSeek API进行流式分析
        await this.callDeepSeekAPI(prompt)
      } catch (error) {
        console.error('AI分析失败:', error)
        this.aiAnalysis.error = error.message || 'AI分析失败,请稍后重试'
      } finally {
        this.aiAnalysis.loading = false
      }
    },

    // 构建分析提示词 - 分析所有数据
    buildAnalysisPrompt() {
      // 获取表字段信息
      const fields = this.tableInfo.columns.map(col => ({
        name: col.columnName,
        comment: col.columnComment || col.columnName,
        type: col.dataType
      }))

      // 构建字段说明
      const fieldDescriptions = fields.map(f =>
        `- ${f.comment} (${f.name}, ${f.type})`
      ).join('\n')

      // 构建所有数据内容 (限制数量避免token过多)
      const maxRows = Math.min(this.tableData.length, 100) // 最多分析100条
      const dataRows = this.tableData.slice(0, maxRows).map((row, index) => {
        const rowData = fields.map(f => `${f.comment}: ${row[f.name] || '空'}`).join(', ')
        return `${index + 1}. ${rowData}`
      }).join('\n')

      const totalCount = this.tableData.length
      const analyzedCount = maxRows

      const prompt = `你是一个专业的数据风险分析专家。请分析以下数据库表的数据,识别可能存在的风险。

**表名**: ${this.tableInfo.tableName}
**表说明**: ${this.tableInfo.tableComment || '无'}
**数据总量**: ${totalCount} 条
**分析数量**: ${analyzedCount} 条 ${totalCount > maxRows ? '(仅分析前100条)' : ''}

**字段结构**:
${fieldDescriptions}

**数据内容**:
${dataRows}

请从以下几个维度进行**总体风险分析**:

1. **数据完整性风险**:
   - 统计有多少条数据存在关键字段缺失或为空
   - 分析缺失字段的分布情况
   - 评估对业务的影响

2. **数据合规性风险**:
   - 检查数据是否符合业务规则和法律法规要求
   - 识别不合规的数据模式
   - 评估合规风险等级

3. **数据一致性风险**:
   - 检查字段之间的逻辑关系是否合理
   - 发现数据矛盾或异常
   - 分析一致性问题的普遍性

4. **业务风险**:
   - 根据字段含义和数据内容,分析可能存在的业务风险
   - 识别异常业务模式
   - 评估对业务运营的影响

5. **安全风险**:
   - 检查是否存在敏感信息泄露等安全隐患
   - 识别潜在的数据安全问题
   - 评估安全风险等级

6. **数据质量总体评估**:
   - 给出数据质量的总体评分
   - 列出最严重的3-5个问题
   - 提供优先级建议

请以清晰的格式输出分析结果,每个风险点都要说明:
- 风险类型
- 风险描述 (包含统计数据)
- 风险等级 (高/中/低)
- 影响范围
- 建议措施

如果没有发现明显风险,请说明数据看起来正常,并给出数据质量评估。`

      return prompt
    },

    // 调用 vLLM 部署的 DeepSeek 模型 (OpenAI 兼容 API)
    async callDeepSeekAPI(prompt) {
      // vLLM OpenAI 兼容 API 端点
      const apiUrl = 'http://192.0.2.200:10040/v1/chat/completions'

      try {
        console.log('开始调用 vLLM API...')

        // 按照 OpenAI API 标准格式构建请求
        const requestBody = {
          model: 'deepseek', // vLLM 部署的模型名称
          messages: [
            {
              role: 'system',
              content: '你是一个专业的数据风险分析专家,擅长识别数据中的各类风险并提供专业建议。请用中文回答,并使用清晰的格式组织内容。'
            },
            {
              role: 'user',
              content: prompt
            }
          ],
          stream: true, // 启用流式响应
          temperature: 0.7,
          max_tokens: 4000,
          top_p: 0.95
        }

        console.log('请求参数:', JSON.stringify(requestBody, null, 2))

        // 使用 fetch 进行流式请求
        const response = await fetch(apiUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            // vLLM 不强制要求 API key,但需要 Authorization header
            'Authorization': 'Bearer EMPTY'
          },
          body: JSON.stringify(requestBody)
        })

        console.log('响应状态:', response.status, response.statusText)

        if (!response.ok) {
          const errorText = await response.text()
          console.error('API 错误响应:', errorText)
          throw new Error(`API请求失败: ${response.status} ${response.statusText}\n${errorText}`)
        }

        // 处理流式响应 (Server-Sent Events 格式)
        const reader = response.body.getReader()
        const decoder = new TextDecoder('utf-8')
        let buffer = ''
        let chunkCount = 0

        while (true) {
          const { done, value } = await reader.read()

          if (done) {
            console.log('流式响应完成,共接收', chunkCount, '个数据块')
            break
          }

          chunkCount++

          // 解码数据块
          buffer += decoder.decode(value, { stream: true })
          const lines = buffer.split('\n')

          // 保留最后一个不完整的行
          buffer = lines.pop() || ''

          // 处理每一行
          for (const line of lines) {
            const trimmedLine = line.trim()

            // 跳过空行
            if (trimmedLine === '') continue

            // 检查是否是结束标记
            if (trimmedLine === 'data: [DONE]') {
              console.log('收到结束标记 [DONE]')
              continue
            }

            // 解析 SSE 数据行 (格式: data: {...})
            if (trimmedLine.startsWith('data: ')) {
              try {
                const jsonStr = trimmedLine.substring(6)
                const data = JSON.parse(jsonStr)

                // OpenAI/vLLM 流式格式: choices[0].delta.content
                if (data.choices && data.choices.length > 0) {
                  const delta = data.choices[0].delta
                  if (delta && delta.content) {
                    // 实时追加内容到结果
                    this.aiAnalysis.result += delta.content
                  }
                }
              } catch (e) {
                console.warn('解析 JSON 失败:', e.message)
                console.warn('原始数据:', trimmedLine)
              }
            }
          }
        }

        // 处理剩余的 buffer
        if (buffer.trim() && buffer.trim().startsWith('data: ')) {
          try {
            const jsonStr = buffer.trim().substring(6)
            if (jsonStr !== '[DONE]') {
              const data = JSON.parse(jsonStr)
              if (data.choices && data.choices.length > 0) {
                const delta = data.choices[0].delta
                if (delta && delta.content) {
                  this.aiAnalysis.result += delta.content
                }
              }
            }
          } catch (e) {
            console.warn('解析最后的数据失败:', e.message)
          }
        }

        console.log('AI 分析完成')
        console.log('总字符数:', this.aiAnalysis.result.length)
      } catch (error) {
        console.error('调用 vLLM API 失败:', error)
        throw error
      }
    },

    // 格式化分析结果 (支持Markdown)
    formatAnalysisResult(text) {
      if (!text) return ''

      // 简单的Markdown转HTML
      let html = text
        // 标题
        .replace(/### (.*?)$/gm, '<h3>$1</h3>')
        .replace(/## (.*?)$/gm, '<h2>$1</h2>')
        .replace(/# (.*?)$/gm, '<h1>$1</h1>')
        // 粗体
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        // 列表
        .replace(/^- (.*?)$/gm, '<li>$1</li>')
        .replace(/^(\d+)\. (.*?)$/gm, '<li>$2</li>')
        // 换行
        .replace(/\n/g, '<br>')

      // 包裹列表
      html = html.replace(/(<li>.*?<\/li>)/g, '<ul>$1</ul>')

      return html
    },

    // 复制分析结果
    copyAnalysisResult() {
      const textarea = document.createElement('textarea')
      textarea.value = this.aiAnalysis.result
      document.body.appendChild(textarea)
      textarea.select()
      document.execCommand('copy')
      document.body.removeChild(textarea)
      this.$message.success('分析结果已复制到剪贴板')
    },

    // 直接通过下拉框更新表分类
    async updateTableCategoryDirect(table) {
      // 如果选择了"新建分类"
      if (table.category === '__new__') {
        this.newCategoryForm.targetTable = table
        this.newCategoryForm.name = ''
        this.newCategoryDialogVisible = true
        // 恢复原分类
        this.$nextTick(() => {
          const originalTable = this.queriedTableList.find(t => t.tableName === table.tableName)
          if (originalTable) {
            table.category = originalTable.category || '未分类'
          }
        })
        return
      }

      // 调用接口更新分类
      try {
        const response = await updateTableCategory({
          tableName: table.tableName,
          category: table.category,
          sortOrder: table.sortOrder || 0
        })

        if (response.code === 1) {
          this.$message.success('分类更新成功')
          await this.loadQueriedTableList()
          // 展开目标分类
          if (!this.activeCategories.includes(table.category)) {
            this.activeCategories.push(table.category)
          }
        } else {
          this.$message.error(response.msg || '分类更新失败')
          await this.loadQueriedTableList()
        }
      } catch (error) {
        console.error('更新分类失败:', error)
        this.$message.error('分类更新失败')
        await this.loadQueriedTableList()
      }
    },

    // 确认新建分类
    async confirmNewCategory() {
      if (!this.newCategoryForm.name || !this.newCategoryForm.name.trim()) {
        this.$message.warning('请输入分类名称')
        return
      }

      const categoryName = this.newCategoryForm.name.trim()
      const table = this.newCategoryForm.targetTable

      if (!table) {
        this.$message.error('未找到目标表')
        return
      }

      try {
        const response = await updateTableCategory({
          tableName: table.tableName,
          category: categoryName,
          sortOrder: 0
        })

        if (response.code === 1) {
          this.$message.success('分类创建成功')
          this.newCategoryDialogVisible = false
          await this.loadQueriedTableList()
          // 展开新分类
          this.$nextTick(() => {
            if (!this.activeCategories.includes(categoryName)) {
              this.activeCategories.push(categoryName)
            }
          })
        } else {
          this.$message.error(response.msg || '分类创建失败')
        }
      } catch (error) {
        console.error('创建分类失败:', error)
        this.$message.error('分类创建失败')
      }
    },

    // 处理拖拽变化
    async handleDragChange(evt, targetCategory) {
      console.log('拖拽事件:', evt, '目标分类:', targetCategory)

      // 如果是添加事件(从其他分类拖入)
      if (evt.added) {
        const table = evt.added.element
        const newIndex = evt.added.newIndex

        try {
          const response = await updateTableCategory({
            tableName: table.tableName,
            category: targetCategory,
            sortOrder: newIndex
          })

          if (response.code === 1) {
            this.$message.success('分类更新成功')
            await this.loadQueriedTableList()
          } else {
            this.$message.error(response.msg || '分类更新失败')
            await this.loadQueriedTableList()
          }
        } catch (error) {
          console.error('更新分类失败:', error)
          this.$message.error('分类更新失败')
          await this.loadQueriedTableList()
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.table-query-container {
  padding: 20px;
  position: relative;

  .box-card {
    position: relative; // 为抽屉按钮提供定位参考

    .title-text {
      font-size: 18px;
      font-weight: bold;
      color: #303133;
    }
  }

  .query-panel,
  .info-panel,
  .field-panel,
  .data-panel {
    margin-bottom: 20px;

    .panel-header {
      font-weight: bold;
      color: #303133;
      display: flex;
      align-items: center;
      justify-content: space-between;

      i {
        margin-right: 5px;
        color: #409eff;
      }

      .header-actions {
        margin-left: auto;
      }
    }
  }

  .el-pagination {
    margin-top: 15px;
    text-align: right;
  }

  // 字段查询面板样式
  .field-query-panel {
    margin-bottom: 20px;

    ::v-deep .el-form-item {
      margin-bottom: 10px; // 增加输入框上下边距
    }

    .search-more {
      cursor: pointer;
      color: #409eff;
      font-size: 14px;
      display: flex;
      align-items: center;

      i {
        margin-left: 5px;
        transition: transform 0.3s;
      }

      &.is-opened i {
        transform: rotate(180deg);
      }
    }
  }

  // 搜索筛选弹出框样式
  ::v-deep .field-filter-popover {
    max-height: 350px !important;
    overflow-y: auto !important;

    .page-filter-search {
      max-height: 350px !important;
      overflow-y: auto !important;
    }

    .el-card {
      max-height: 350px !important;
      overflow: visible !important;
    }

    .el-card__body {
      max-height: 290px !important;
      overflow-y: auto !important;
    }
  }

  // AI分析对话框样式
  .ai-analysis-container {
    min-height: 400px;
    max-height: 70vh;
    overflow-y: auto;
    padding: 20px;
    background: #f5f7fa;
    border-radius: 4px;

    .analysis-loading {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 60px 20px;
      color: #409eff;
      font-size: 16px;

      i {
        font-size: 48px;
        margin-bottom: 20px;
      }
    }

    .analysis-result {
      background: white;
      border-radius: 4px;
      padding: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      .result-header {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        padding-bottom: 15px;
        border-bottom: 2px solid #409eff;
        font-size: 18px;
        font-weight: bold;
        color: #303133;

        i {
          font-size: 24px;
          color: #e6a23c;
          margin-right: 10px;
        }
      }

      .result-content {
        line-height: 1.8;
        color: #606266;
        font-size: 14px;

        ::v-deep h1, ::v-deep h2, ::v-deep h3 {
          margin: 20px 0 10px;
          color: #303133;
        }

        ::v-deep h1 {
          font-size: 20px;
          border-bottom: 2px solid #409eff;
          padding-bottom: 10px;
        }

        ::v-deep h2 {
          font-size: 18px;
          color: #409eff;
        }

        ::v-deep h3 {
          font-size: 16px;
          color: #606266;
        }

        ::v-deep strong {
          color: #303133;
          font-weight: 600;
        }

        ::v-deep ul {
          margin: 10px 0;
          padding-left: 0;
          list-style: none;
        }

        ::v-deep li {
          margin: 8px 0;
          padding-left: 20px;
          position: relative;

          &:before {
            content: '•';
            position: absolute;
            left: 5px;
            color: #409eff;
            font-weight: bold;
          }
        }

        ::v-deep br {
          display: block;
          margin: 5px 0;
          content: '';
        }
      }
    }

    .analysis-error {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 60px 20px;
      color: #f56c6c;
      font-size: 16px;

      i {
        font-size: 48px;
        margin-bottom: 20px;
      }
    }

    .analysis-empty {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 60px 20px;
      color: #909399;
      font-size: 16px;

      i {
        font-size: 48px;
        margin-bottom: 20px;
      }
    }
  }

  // 查询历史抽屉式按钮 - 贴在header下边框
  .history-drawer-trigger {
    position: absolute;
    top: 60px; // 贴在header下方
    left: 50%;
    transform: translateX(-50%);
    width: 80px;
    height: 30px;
    background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
    border-radius: 0 0 15px 15px;
    box-shadow: 0 2px 8px rgba(103, 194, 58, 0.3);
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 5px;
    color: white;
    font-size: 14px;
    font-weight: 500;
    z-index: 100;
    transition: all 0.3s ease;

    &:hover {
      height: 35px;
      box-shadow: 0 4px 12px rgba(103, 194, 58, 0.5);
      background: linear-gradient(135deg, #85ce61 0%, #67c23a 100%);
    }

    i {
      font-size: 16px;
      font-weight: bold;
      transform: rotate(90deg); // 将右箭头旋转90度变成下箭头
      transition: transform 0.3s ease;
    }

    .trigger-text {
      font-size: 12px;
      letter-spacing: 1px;
    }

    // 悬浮显示的历史面板 - 使用fixed定位,提升到最高层级
    .history-popover-panel {
      position: fixed !important; // 使用fixed定位,不受父元素影响
      top: 95px !important; // 在按钮下方
      left: 50% !important;
      transform: translateX(-50%) !important;
      width: 600px;
      max-height: 500px;
      background: white;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      z-index: 99999 !important; // 提升到最高层级
      overflow: hidden;

      .history-popover-content {
        padding: 15px;
        max-height: 500px;
        overflow-y: auto;

        .history-popover-toolbar {
          margin-bottom: 15px;
        }

        .history-popover-category-list {
          .history-popover-category-header {
            display: flex;
            align-items: center;
            width: 100%;

            i {
              margin-right: 8px;
              color: #409eff;
              font-size: 14px;
            }

            .category-name {
              font-weight: bold;
              font-size: 13px;
            }

            .category-count {
              margin-left: 5px;
              color: #909399;
              font-size: 12px;
            }
          }

          .history-popover-table-list {
            min-height: 40px;
          }

          .history-popover-table-item {
            display: flex;
            align-items: center;
            padding: 8px 10px;
            margin: 5px 0;
            background: #f5f7fa;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.3s;
            font-size: 13px;

            &:hover {
              background: #ecf5ff;
              transform: translateX(3px);
            }

            .drag-handle {
              cursor: move;
              margin-right: 8px;
              color: #909399;
              font-size: 14px;

              &:hover {
                color: #409eff;
              }
            }

            .el-icon-document {
              margin-right: 6px;
              color: #409eff;
              font-size: 14px;
            }

            .table-name {
              flex: 1;
              color: #303133;
              font-weight: 500;
              cursor: pointer;
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
              min-width: 0; // 确保flex子元素可以收缩

              &:hover {
                color: #409eff;
              }
            }

            .el-select {
              flex-shrink: 0; // 防止下拉框被压缩
            }
          }
        }
      }
    }
  }

  // 表名输入框
  .table-name-input {
    width: 100%;
  }

  // 历史对话框样式(保留用于新建分类对话框)
  .history-dialog {
    .history-content {
      .history-toolbar {
        margin-bottom: 20px;
        display: flex;
        align-items: center;
      }

      .category-list {
        max-height: 500px;
        overflow-y: auto;

        .category-header {
          display: flex;
          align-items: center;
          width: 100%;

          i {
            margin-right: 8px;
            color: #409eff;
            font-size: 16px;
          }

          .category-name {
            font-weight: bold;
            font-size: 14px;
          }

          .category-count {
            margin-left: 5px;
            color: #909399;
            font-size: 12px;
          }

          .el-button {
            margin-left: auto;
          }
        }

        .table-list {
          min-height: 50px;
        }

        .table-item {
          display: flex;
          align-items: center;
          padding: 10px 15px;
          margin: 5px 0;
          background: #f5f7fa;
          border-radius: 4px;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            background: #ecf5ff;
            transform: translateX(5px);
          }

          .drag-handle {
            cursor: move;
            margin-right: 10px;
            color: #909399;
            font-size: 16px;

            &:hover {
              color: #409eff;
            }
          }

          .el-icon-document {
            margin-right: 8px;
            color: #409eff;
          }

          .table-name {
            flex: 1;
            font-size: 14px;
            color: #303133;
          }

          .table-time {
            font-size: 12px;
            color: #909399;
            margin-left: 10px;
          }
        }
      }
    }
  }
}

/* 返回数据画像按钮样式 */
.back-buttons-wrapper {
  margin-bottom: 16px;
  padding: 12px 16px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>

<!-- 全局样式,确保搜索筛选弹出框高度限制生效 -->
<style lang="scss">
.field-filter-popover {
  max-height: 350px !important;
  overflow-y: auto !important;

  .page-filter-search {
    max-height: 350px !important;
    overflow-y: auto !important;
  }

  .el-card {
    max-height: 350px !important;
    overflow: visible !important;
  }

  .el-card__body {
    max-height: 290px !important;
    overflow-y: auto !important;
  }
}
</style>
