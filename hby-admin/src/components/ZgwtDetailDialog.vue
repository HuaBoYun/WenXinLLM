<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="dialogTitle"
    width="80%"
    :before-close="handleClose"
    append-to-body
  >
    <div v-loading="loading">
      <el-alert
        :title="alertTitle"
        type="info"
        :closable="false"
        style="margin-bottom: 15px"
      />

      <el-table
        :data="tableData"
        border
        style="width: 100%"
        :height="500"
      >
        <el-table-column
          align="center"
          label="序号"
          type="index"
          width="60"
        />
        <el-table-column
          align="center"
          label="问题编号"
          prop="issueCode"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="问题名称"
          prop="issueName"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="整改方案"
          prop="rectificationPlan"
          min-width="250"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="截止时间"
          prop="deadline"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{ scope.row.deadline ? scope.row.deadline.split(' ')[0] : '-' }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="是否已完成整改"
          prop="conclusion"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-tag :type="getConclusionTagType(scope.row.conclusion)" size="small">
              {{ scope.row.conclusion || '-' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="主管部门"
          prop="orgName"
          min-width="150"
          show-overflow-tooltip
        />
      </el-table>

      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="'total, sizes, prev, pager, next, jumper'"
        :page-size="queryForm.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        style="margin-top: 20px; text-align: right"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getZgwtDetail } from '@/api/audit/sjfx'

export default {
  name: 'ZgwtDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    // 查询类型: 'yzg'(已整改) | 'wzg'(未整改) | 'zs'(整改总数) | 'yxh'(已销号) | 'wxh'(未销号) | 'xhzs'(销号总数)
    queryType: {
      type: String,
      default: '',
    },
    // 主管部门名称
    orgName: {
      type: String,
      default: '',
    },
    // 审计类型
    auditType: {
      type: String,
      default: '',
    },
    // 审计来源
    projectSource: {
      type: String,
      default: '',
    },
    year: {
      type: String,
      default: '',
    },
    // 项目ID
    projectId: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      tableData: [],
      total: 0,
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
      },
      themeColor: '#1890ff', // 默认蓝色
      themeColors: {
        'vab-theme-red': '#e50113',      // 红色主题
        'vab-theme-ocean': '#1890ff',    // 海洋主题(蓝色)
        'vab-theme-green': '#13ce66',    // 绿色主题
        'vab-theme-white': '#515a6e',    // 白色主题
      },
    }
  },
  computed: {
    dialogTitle() {
      const typeMap = {
        yzg: '已整改问题详细列表',
        wzg: '未整改问题详细列表',
        zs: '整改问题详细列表',
        yxh: '已销号问题详细列表',
        wxh: '未销号问题详细列表',
        xhzs: '销号问题详细列表',
      }
      const baseTitle = typeMap[this.queryType] || '整改问题详细列表'
      // 优先显示项目ID，如果没有则显示主管部门名称
      if (this.projectId) {
        return `项目ID: ${this.projectId} - ${baseTitle}`
      } else {
        return this.orgName ? `${this.orgName} - ${baseTitle}` : baseTitle
      }
    },
    alertTitle() {
      const typeMap = {
        yzg: '已整改',
        wzg: '未整改',
        zs: '全部',
        yxh: '已销号',
        wxh: '未销号',
        xhzs: '全部',
      }
      const typeName = typeMap[this.queryType] || ''
      // 优先显示项目ID，如果没有则显示主管部门名称
      if (this.projectId) {
        return `年度: ${this.year || '全部'} | 项目ID: ${this.projectId} | 类型: ${typeName} | 问题总数: ${this.total}`
      } else {
        return `年度: ${this.year || '全部'} | 主管部门: ${this.orgName || '全部'} | 类型: ${typeName} | 问题总数: ${this.total}`
      }
    },
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && (this.orgName || this.projectId)) {
        this.initThemeColor()
        this.fetchData()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    },
  },
  mounted() {
    // 初始化主题颜色
    this.initThemeColor()
    // 监听主题变化
    this.$nextTick(() => {
      this.observeThemeChange()
    })
  },
  beforeDestroy() {
    // 组件销毁时停止观察
    if (this.themeObserver) {
      this.themeObserver.disconnect()
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    // 整改结论标签颜色映射
    getConclusionTagType(conclusion) {
      if (!conclusion) {
        return 'info'
      }
      const text = String(conclusion).trim()
      // 根据结论文本返回对应的标签颜色
      if (text.includes('未整改') || text.includes('未到位')) {
        return 'warning'
      } else if (text.includes('已整改到位') || text.includes('整改到位')) {
        return 'success'
      } else if (text.includes('关闭')) {
        return 'info'
      } else if (text.includes('已整改')) {
        return 'success'
      }
      return 'info'
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    // 初始化主题颜色
    initThemeColor() {
      const body = document.body

      // 根据body的class获取主题色
      for (const themeClass in this.themeColors) {
        if (body.classList.contains(themeClass)) {
          this.themeColor = this.themeColors[themeClass]
          break
        }
      }

      // 设置CSS变量,使样式能够动态响应主题变化
      document.documentElement.style.setProperty('--zgwt-detail-theme-color', this.themeColor)
    },
    // 监听主题变化
    observeThemeChange() {
      const targetNode = document.body
      const config = { attributes: true, attributeFilter: ['class'] }

      const observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
          if (mutation.type === 'attributes' && mutation.attributeName === 'class') {
            this.initThemeColor()
          }
        })
      })

      observer.observe(targetNode, config)
      this.themeObserver = observer
    },
    fetchData() {
      console.log('=== ZgwtDetailDialog fetchData 开始 ===')
      console.log('orgName:', this.orgName)
      console.log('projectId:', this.projectId)
      console.log('auditType:', this.auditType)
      console.log('projectSource:', this.projectSource)
      console.log('queryType:', this.queryType)
      console.log('year:', this.year)

      // 优先使用projectId，如果没有projectId则使用orgName
      if (!this.projectId && !this.orgName) {
        console.error('projectId和orgName都为空,无法查询')
        this.$message.error('项目ID或主管部门名称不能为空')
        return
      }

      this.loading = true

      // 构造查询参数
      const params = {
        ...this.queryForm,
        year: this.year,
        queryType: this.queryType,
      }

      // 根据情况传递projectId或orgName
      if (this.projectId) {
        params.projectId = this.projectId
      } else {
        params.orgName = this.orgName
      }

      // 添加审计类型参数
      if (this.auditType) {
        params.audittype = this.auditType
      }

      // 添加审计来源参数
      if (this.projectSource) {
        params.projectsource = this.projectSource
      }

      console.log('请求参数:', params)

      getZgwtDetail(params)
        .then((res) => {
          console.log('接口返回结果:', res)
          if (res.data && res.data.pageInfo) {
            this.tableData = res.data.pageInfo.tlist || []
            this.total = res.data.pageInfo.totalRecord || 0
          } else if (res.data && res.data.list) {
            this.tableData = res.data.list || []
            this.total = res.data.list.length || 0
          } else {
            this.tableData = []
            this.total = 0
          }
          console.log('处理后数据:', {
            tableData: this.tableData,
            total: this.total
          })
          // 调试：打印每条数据的resultStatus
          this.tableData.forEach((item, index) => {
            console.log(`数据${index} - resultStatus:`, item.resultStatus, '类型:', typeof item.resultStatus)
          })
        })
        .catch((error) => {
          console.error('获取整改问题详细数据失败:', error)
          this.$message.error('获取数据失败,请稍后重试')
          this.tableData = []
          this.total = 0
        })
        .finally(() => {
          this.loading = false
          console.log('=== ZgwtDetailDialog fetchData 结束 ===')
        })
    },
  },
}
</script>

<style scoped lang="scss">
// 使用CSS变量来实现主题色动态适配
::v-deep .el-dialog__header {
  background: linear-gradient(135deg, var(--zgwt-detail-theme-color, #1890ff) 0%, color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 80%, white) 100%);
  color: #ffffff;
  padding: 15px 20px;
  border-radius: 4px 4px 0 0;
}

::v-deep .el-dialog__title {
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
}

::v-deep .el-dialog__headerbtn .el-dialog__close {
  color: #ffffff;
  font-size: 18px;
  transition: all 0.3s ease;

  &:hover {
    color: #f0f0f0;
    transform: rotate(90deg);
  }
}

::v-deep .el-dialog__body {
  padding: 20px;
}

// 表格样式 - 使用主题色
::v-deep .el-table {
  background: #ffffff !important;
  border-radius: 8px;
  overflow: hidden;

  th {
    background: linear-gradient(180deg, color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 8%, transparent) 0%, color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 15%, transparent) 100%) !important;
    color: color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 90%, black) !important;
    font-weight: 600;
    border-bottom: 2px solid var(--zgwt-detail-theme-color, #1890ff) !important;
  }

  td {
    background: #ffffff !important;
    color: #606266 !important;
    border-bottom: 1px solid #ebeef5 !important;
  }

  tr:hover > td {
    background: color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 5%, transparent) !important;
  }

  &.el-table--border {
    border: 1px solid #ebeef5 !important;

    &::after,
    &::before {
      background-color: #ebeef5 !important;
    }
  }
}

// 分页样式 - 使用主题色
.el-pagination {
  ::v-deep .el-pagination__total,
  ::v-deep .el-pagination__jump {
    color: #606266 !important;
  }

  ::v-deep .el-pager li {
    background: #ffffff !important;
    color: #606266;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    transition: all 0.3s ease;
    margin: 0 2px;

    &:hover {
      background: color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 8%, transparent) !important;
      border-color: var(--zgwt-detail-theme-color, #1890ff);
      color: var(--zgwt-detail-theme-color, #1890ff);
    }

    &.active {
      background: linear-gradient(135deg, var(--zgwt-detail-theme-color, #1890ff) 0%, color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 80%, white) 100%) !important;
      color: #ffffff;
      border-color: var(--zgwt-detail-theme-color, #1890ff);
    }
  }

  ::v-deep button {
    background: #ffffff !important;
    color: #606266;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    transition: all 0.3s ease;

    &:hover {
      background: color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 8%, transparent) !important;
      color: var(--zgwt-detail-theme-color, #1890ff);
      border-color: var(--zgwt-detail-theme-color, #1890ff);
    }

    &:disabled {
      opacity: 0.5;
      background: #f5f5f5 !important;
      color: #c0c4cc;
      border-color: #dcdfe6;
    }
  }

  ::v-deep .el-pagination__sizes .el-select .el-input__inner {
    background: #ffffff !important;
    color: #606266;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
  }
}

// Alert组件样式
::v-deep .el-alert {
  background: color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 5%, transparent) !important;
  border: 1px solid color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 30%, transparent) !important;

  .el-alert__title {
    color: #606266;
    font-weight: 500;
  }

  &.el-alert--info {
    .el-alert__icon {
      color: var(--zgwt-detail-theme-color, #1890ff);
    }
  }
}

// 按钮样式
::v-deep .el-button {
  border: 1px solid #dcdfe6;
  color: #606266;
  transition: all 0.3s ease;

  &:hover {
    background: color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 8%, transparent) !important;
    color: var(--zgwt-detail-theme-color, #1890ff);
    border-color: var(--zgwt-detail-theme-color, #1890ff);
  }

  &.el-button--primary {
    background: linear-gradient(135deg, var(--zgwt-detail-theme-color, #1890ff) 0%, color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 80%, white) 100%);
    border: none;
    color: #ffffff;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 4px 12px color-mix(in srgb, var(--zgwt-detail-theme-color, #1890ff) 40%, transparent);
      filter: brightness(1.05);
    }
  }
}

// Loading遮罩样式
::v-deep .el-loading-mask {
  background-color: rgba(255, 255, 255, 0.9) !important;

  .el-loading-spinner {
    .path {
      stroke: var(--zgwt-detail-theme-color, #1890ff);
    }
  }
}
</style>
