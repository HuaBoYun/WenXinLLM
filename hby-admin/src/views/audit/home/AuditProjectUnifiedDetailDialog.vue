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

      <el-table :data="tableData" border style="width: 100%" :height="500">
        <el-table-column
          key="index"
          align="center"
          label="序号"
          type="index"
          width="60"
        />
        <el-table-column
          key="projectCode"
          align="center"
          label="项目编号"
          prop="projectCode"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          key="prjoectName"
          align="center"
          label="项目名称"
          prop="prjoectName"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          key="orgIdNames"
          align="center"
          label="被审计单位"
          prop="orgIdNames"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          v-if="queryType === 'company'"
          key="projectSource"
          align="center"
          label="项目来源"
          prop="projectSource"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          key="auditType"
          align="center"
          label="审计类型"
          prop="auditType"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          key="planYear"
          align="center"
          label="计划年度"
          prop="planYear"
          width="100"
          show-overflow-tooltip
        />
        <el-table-column
          key="realname"
          align="center"
          label="项目经理"
          prop="realname"
          min-width="100"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{ scope.row.realname || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          key="status"
          align="center"
          label="项目状态"
          prop="status"
          width="100"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-if="isValidStatus(scope.row.status)">
              {{ formatStatus(scope.row.status) }}
            </span>
            <span v-else-if="scope.row.examineTypes">
              {{ scope.row.examineTypes }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="queryType === 'company'"
          key="startDate"
          align="center"
          label="计划审计时间"
          prop="startDate"
          width="110"
          show-overflow-tooltip
        />
        <el-table-column
          v-if="queryType === 'company'"
          key="endDate"
          align="center"
          label="计划验收时间"
          prop="endDate"
          width="110"
          show-overflow-tooltip
        />
        <!-- <el-table-column
          v-if="queryType === 'company'"
          key="days"
          align="center"
          label="项目实施期间(天)"
          prop="days"
          width="140"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{ scope.row.days ? scope.row.days + '天' : '-' }}
          </template>
        </el-table-column> -->
        <el-table-column
          v-if="queryType === 'company'"
          key="costs"
          align="center"
          label="批复总投资(经费:万元)"
          prop="costs"
          width="180"
          show-overflow-tooltip
        />
        <el-table-column
          key="examineType"
          align="center"
          label="审批状态"
          prop="examineType"
          width="100"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{
              scope.row.examineType == 1
                ? '审批中'
                : scope.row.examineType == 2
                ? '已退回'
                : scope.row.examineType == 3
                ? '已撤回'
                : scope.row.examineType == 4
                ? '已终止'
                : scope.row.examineType == 5
                ? '已跟踪'
                : scope.row.examineType == 6
                ? '已完成'
                : '未审批'
            }}
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
  import { getSJXMDataDetail } from '@/api/cwztfx.js'

  export default {
    name: 'AuditProjectUnifiedDetailDialog',
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
      // 查询类型: 'company'(按公司) | 'type'(按类型) | 'status'(按完成状态) | 'month'(按月份) | 'company-status'(按公司+状态)
      queryType: {
        type: String,
        default: 'company',
      },
      // 查询值: 公司名称/审计类型/完成状态/月份
      queryValue: {
        type: String,
        default: '',
      },
      year: {
        type: String,
        default: '',
      },
      // 状态筛选条件,用于company-status类型(已完成/未完成)
      detailStatus: {
        type: String,
        default: null,
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
          'vab-theme-red': '#e50113', // 红色主题
          'vab-theme-ocean': '#1890ff', // 海洋主题(蓝色)
          'vab-theme-green': '#13ce66', // 绿色主题
          'vab-theme-white': '#515a6e', // 白色主题
        },
      }
    },
    computed: {
      dialogTitle() {
        const companyName = this.queryValue || '全部单位'
        const typeMap = {
          company: `【${companyName}】审计项目详细数据`,
          type: `【${this.queryValue}】审计项目类型详细数据`,
          status: `【${this.queryValue}】审计项目完成情况详细数据`,
          month: `【${this.queryValue}】审计项目详细数据`,
          'company-status': this.getStatusTitle(),
        }
        return typeMap[this.queryType] || '审计项目详细数据'
      },
      alertTitle() {
        const companyName = this.queryValue || '全部单位'
        const typeMap = {
          company: `年度:${this.year || '全部'} | 被审计单位:${
            companyName
          } | 项目总数:${this.total}`,
          type: `年度:${this.year || '全部'} | 审计类型:${
            this.queryValue
          } | 项目总数:${this.total}`,
          status: `年度:${this.year || '全部'} | 完成状态:${
            this.queryValue
          } | 项目总数:${this.total}`,
          month: `年度:${this.year || '全部'} | 月份:${
            this.queryValue
          } | 项目总数:${this.total}`,
          'company-status': `年度:${this.year || '全部'} | 被审计单位:${
            this.queryValue
          } | 完成状态:${this.getStatusText()} | 项目总数:${this.total}`,
        }
        return (
          typeMap[this.queryType] ||
          `年度:${this.year || '全部'} | 项目总数:${this.total}`
        )
      },
    },
    watch: {
      visible(val) {
        this.dialogVisible = val
        if (val) {
          this.initThemeColor()
          this.queryForm.pageNumber = 1
          if (this.canFetchData()) {
            this.fetchData()
          } else {
            this.tableData = []
            this.total = 0
          }
        }
      },
      queryType() {
        this.handleQueryChange()
      },
      queryValue() {
        this.handleQueryChange()
      },
      detailStatus() {
        this.handleQueryChange()
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
          4: '归档',
        }
        const statusNum = Number(status)
        return statusMap[statusNum] || '-'
      },
      canFetchData() {
        return this.queryType === 'company' || !!this.queryValue
      },
      handleQueryChange() {
        if (!this.dialogVisible) {
          return
        }
        this.queryForm.pageNumber = 1
        if (this.canFetchData()) {
          this.fetchData()
        } else {
          this.tableData = []
          this.total = 0
        }
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
      // 获取company-status类型的对话框标题
      getStatusTitle() {
        const statusText = this.getStatusText()
        return `【${this.queryValue}】${statusText}项目详细数据`
      },
      // 获取状态文本描述
      getStatusText() {
        if (!this.detailStatus) return '全部'

        // detailStatus已经是中文文本,直接返回
        if (this.detailStatus === '已完成' || this.detailStatus === '未完成') {
          return this.detailStatus
        }

        // 兼容旧格式(逗号分隔的数字)
        if (this.detailStatus === '3,4') {
          return '已完成'
        } else if (this.detailStatus === '0,1,2') {
          return '未完成'
        } else {
          return '筛选'
        }
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
        document.documentElement.style.setProperty(
          '--dialog-unified-theme-color',
          this.themeColor
        )
      },
      // 监听主题变化
      observeThemeChange() {
        const targetNode = document.body
        const config = { attributes: true, attributeFilter: ['class'] }

        const observer = new MutationObserver((mutations) => {
          mutations.forEach((mutation) => {
            if (
              mutation.type === 'attributes' &&
              mutation.attributeName === 'class'
            ) {
              this.initThemeColor()
            }
          })
        })

        observer.observe(targetNode, config)
        this.themeObserver = observer
      },
      fetchData() {
        if (!this.canFetchData()) {
          return
        }

        this.loading = true

        // 统一调用 getSJXMDataDetail 接口,根据queryType传递不同参数
        const params = {
          ...this.queryForm,
        }

        // 根据查询类型添加不同的参数
        if (this.queryType === 'company') {
          params.year = this.year // 按公司查询时传递year
          // queryValue 为空时表示查询全部单位
          if (this.queryValue) {
            params.companyName = this.queryValue
          }
        } else if (this.queryType === 'company-status') {
          // 按公司+状态查询
          params.year = this.year
          params.companyName = this.queryValue
          params.status = this.detailStatus // 传递状态筛选条件,例如: "3,4" 或 "0,1,2"
        } else if (this.queryType === 'type') {
          // 按类型查询时不传递year参数
          params.auditType = this.queryValue
        } else if (this.queryType === 'status') {
          params.year = this.year // 按状态查询时传递year
          params.status = this.queryValue
        } else if (this.queryType === 'month') {
          params.year = this.year // 按月份查询时传递year
          params.month = this.queryValue
        } else {
          params.year = this.year // 默认传递year
        }

        getSJXMDataDetail(params)
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
            console.error('获取审计项目详细数据失败:', error)
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
    background: linear-gradient(
      135deg,
      var(--dialog-unified-theme-color, #1890ff) 0%,
      color-mix(in srgb, var(--dialog-unified-theme-color, #1890ff) 80%, white)
        100%
    );
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
      background: linear-gradient(
        180deg,
        color-mix(
            in srgb,
            var(--dialog-unified-theme-color, #1890ff) 8%,
            transparent
          )
          0%,
        color-mix(
            in srgb,
            var(--dialog-unified-theme-color, #1890ff) 15%,
            transparent
          )
          100%
      ) !important;
      color: color-mix(
        in srgb,
        var(--dialog-unified-theme-color, #1890ff) 90%,
        black
      ) !important;
      font-weight: 600;
      border-bottom: 2px solid var(--dialog-unified-theme-color, #1890ff) !important;
    }

    td {
      background: #ffffff !important;
      color: #606266 !important;
      border-bottom: 1px solid #ebeef5 !important;
    }

    tr:hover > td {
      background: color-mix(
        in srgb,
        var(--dialog-unified-theme-color, #1890ff) 5%,
        transparent
      ) !important;
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
        background: color-mix(
          in srgb,
          var(--dialog-unified-theme-color, #1890ff) 8%,
          transparent
        ) !important;
        border-color: var(--dialog-unified-theme-color, #1890ff);
        color: var(--dialog-unified-theme-color, #1890ff);
      }

      &.active {
        background: linear-gradient(
          135deg,
          var(--dialog-unified-theme-color, #1890ff) 0%,
          color-mix(
              in srgb,
              var(--dialog-unified-theme-color, #1890ff) 80%,
              white
            )
            100%
        ) !important;
        color: #ffffff;
        border-color: var(--dialog-unified-theme-color, #1890ff);
      }
    }

    ::v-deep button {
      background: #ffffff !important;
      color: #606266;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      transition: all 0.3s ease;

      &:hover {
        background: color-mix(
          in srgb,
          var(--dialog-unified-theme-color, #1890ff) 8%,
          transparent
        ) !important;
        color: var(--dialog-unified-theme-color, #1890ff);
        border-color: var(--dialog-unified-theme-color, #1890ff);
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
    background: color-mix(
      in srgb,
      var(--dialog-unified-theme-color, #1890ff) 5%,
      transparent
    ) !important;
    border: none !important;

    .el-alert__title {
      color: #606266;
      font-weight: 500;
    }

    &.el-alert--info {
      border: none !important;

      .el-alert__icon {
        color: var(--dialog-unified-theme-color, #1890ff);
      }
    }

    &.el-alert--info.is-light {
      border: none !important;
    }
  }

  // 按钮样式
  ::v-deep .el-button {
    border: 1px solid #dcdfe6;
    color: #606266;
    transition: all 0.3s ease;

    &:hover {
      background: color-mix(
        in srgb,
        var(--dialog-unified-theme-color, #1890ff) 8%,
        transparent
      ) !important;
      color: var(--dialog-unified-theme-color, #1890ff);
      border-color: var(--dialog-unified-theme-color, #1890ff);
    }

    &.el-button--primary {
      background: linear-gradient(
        135deg,
        var(--dialog-unified-theme-color, #1890ff) 0%,
        color-mix(
            in srgb,
            var(--dialog-unified-theme-color, #1890ff) 80%,
            white
          )
          100%
      );
      border: none;
      color: #ffffff;

      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 12px
          color-mix(
            in srgb,
            var(--dialog-unified-theme-color, #1890ff) 40%,
            transparent
          );
        filter: brightness(1.05);
      }
    }
  }

  // Loading遮罩样式
  ::v-deep .el-loading-mask {
    background-color: rgba(255, 255, 255, 0.9) !important;

    .el-loading-spinner {
      .path {
        stroke: var(--dialog-unified-theme-color, #1890ff);
      }
    }
  }
</style>
