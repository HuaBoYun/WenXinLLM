<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="`【${auditType}】审计项目类型详细数据`"
    width="80%"
    :before-close="handleClose"
    append-to-body
  >
    <div v-loading="loading">
      <el-alert
        :title="`年度：${year || '全部'} | 审计类型：${auditType} | 项目总数：${total}`"
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
          label="项目编号"
          prop="projectCode"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="项目名称"
          prop="prjoectName"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="被审计单位"
          prop="orgIdNames"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="审计类型"
          prop="auditType"
          width="120"
        />
        <el-table-column
          align="center"
          label="计划年度"
          prop="planYear"
          width="100"
        />
        <el-table-column
          align="center"
          label="项目经理"
          prop="realname"
          width="100"
        />
        <el-table-column
          align="center"
          label="项目状态"
          prop="status"
          width="100"
        >
          <template slot-scope="scope">
            <span v-if="isValidStatus(scope.row.status)">{{ formatStatus(scope.row.status) }}</span>
            <span v-else-if="scope.row.examineTypes">{{ scope.row.examineTypes }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
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
import { getAuditTypeDataDetail } from '@/api/cwztfx.js'

export default {
  name: 'AuditProjectTypeDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    auditType: {
      type: String,
      default: '',
    },
    year: {
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
        'vab-theme-ocean': '#1890ff',    // 海洋主题（蓝色）
        'vab-theme-green': '#13ce66',    // 绿色主题
        'vab-theme-white': '#515a6e',    // 白色主题
      },
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.auditType) {
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
    // 验证status字段是否有效(必须是数字或数字字符串)
    isValidStatus(status) {
      if (status === null || status === undefined || status === '') {
        return false
      }
      // 检查是否为数字或数字字符串(0-4)
      const statusNum = Number(status)
      return !isNaN(statusNum) && statusNum >= 0 && statusNum <= 4
    },
    // 格式化status字段
    formatStatus(status) {
      const statusMap = {
        0: '未启动',
        1: '启动',
        2: '实施',
        3: '完成',
        4: '归档'
      }
      const statusNum = Number(status)
      return statusMap[statusNum] || '-'
    },
    handleClose() {
      this.dialogVisible = false
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
      document.documentElement.style.setProperty('--dialog-type-theme-color', this.themeColor)
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
      if (!this.auditType) {
        return
      }

      this.loading = true
      getAuditTypeDataDetail({
        year: this.year,
        auditType: this.auditType,
        ...this.queryForm,
      })
        .then((res) => {
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
        })
        .catch((error) => {
          console.error('获取审计项目类型详细数据失败:', error)
          this.$message.error('获取数据失败,请稍后重试')
          this.tableData = []
          this.total = 0
        })
        .finally(() => {
          this.loading = false
        })
    },
  },
}
</script>

<style scoped lang="scss">
// 使用CSS变量来实现主题色动态适配
::v-deep .el-dialog__header {
  background: linear-gradient(135deg, var(--dialog-type-theme-color, #1890ff) 0%, color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 80%, white) 100%);
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
    background: linear-gradient(180deg, color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 8%, transparent) 0%, color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 15%, transparent) 100%) !important;
    color: color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 90%, black) !important;
    font-weight: 600;
    border-bottom: 2px solid var(--dialog-type-theme-color, #1890ff) !important;
  }

  td {
    background: #ffffff !important;
    color: #606266 !important;
    border-bottom: 1px solid #ebeef5 !important;
  }

  tr:hover > td {
    background: color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 5%, transparent) !important;
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
      background: color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 8%, transparent) !important;
      border-color: var(--dialog-type-theme-color, #1890ff);
      color: var(--dialog-type-theme-color, #1890ff);
    }

    &.active {
      background: linear-gradient(135deg, var(--dialog-type-theme-color, #1890ff) 0%, color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 80%, white) 100%) !important;
      color: #ffffff;
      border-color: var(--dialog-type-theme-color, #1890ff);
    }
  }

  ::v-deep button {
    background: #ffffff !important;
    color: #606266;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    transition: all 0.3s ease;

    &:hover {
      background: color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 8%, transparent) !important;
      color: var(--dialog-type-theme-color, #1890ff);
      border-color: var(--dialog-type-theme-color, #1890ff);
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
  background: color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 5%, transparent) !important;
  border: 1px solid color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 30%, transparent) !important;

  .el-alert__title {
    color: #606266;
    font-weight: 500;
  }

  &.el-alert--info {
    .el-alert__icon {
      color: var(--dialog-type-theme-color, #1890ff);
    }
  }
}

// 按钮样式
::v-deep .el-button {
  border: 1px solid #dcdfe6;
  color: #606266;
  transition: all 0.3s ease;

  &:hover {
    background: color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 8%, transparent) !important;
    color: var(--dialog-type-theme-color, #1890ff);
    border-color: var(--dialog-type-theme-color, #1890ff);
  }

  &.el-button--primary {
    background: linear-gradient(135deg, var(--dialog-type-theme-color, #1890ff) 0%, color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 80%, white) 100%);
    border: none;
    color: #ffffff;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 4px 12px color-mix(in srgb, var(--dialog-type-theme-color, #1890ff) 40%, transparent);
      filter: brightness(1.05);
    }
  }
}

// Loading遮罩样式
::v-deep .el-loading-mask {
  background-color: rgba(255, 255, 255, 0.9) !important;

  .el-loading-spinner {
    .path {
      stroke: var(--dialog-type-theme-color, #1890ff);
    }
  }
}
</style>
