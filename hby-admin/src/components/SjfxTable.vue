<template>
  <div>
    <el-card style="width: 100%; padding: 10px; margin-bottom: 10px">
      <div class="table-title">
        审计分析报告
      </div>
      <el-table
        :data="sjfxList"
        v-loading="loading"
        @cell-click="handleCellClick"
      >
        <el-table-column
          align="center"
          label="审计单位"
          prop="orgname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="项目名称"
          prop="prjoectname"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="审计开始时间" prop="startdate">
          <template slot-scope="scope">
            {{ scope.row.startdate ? scope.row.startdate.split(' ')[0] : '' }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="审计类型" prop="audittype" />
        <el-table-column
          align="center"
          label="整改进展情况"
          prop="status"
          v-if="false"
        />
        <el-table-column align="center" label="审计来源" prop="projectsource" />
        <el-table-column align="center" label="审计结束时间" prop="enddate">
          <template slot-scope="scope">
            {{ scope.row.enddate ? scope.row.enddate.split(' ')[0] : '' }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="未整改数量"
          prop="wzg"
          class-name="clickable-column"
        />
        <el-table-column
          align="center"
          label="已整改数量"
          prop="yzg"
          class-name="clickable-column"
        />
        <el-table-column
          align="center"
          label="整改总数"
          prop="zs"
          class-name="clickable-column"
        />
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </el-card>

    <!-- 审计分析报告详情弹窗 -->
    <SjfxReportDetailDialog
      :visible.sync="reportDetailVisible"
      :project-id="selectedProjectId"
      :year="year"
      :query-type="queryType"
    />
  </div>
</template>

<script>
  import { getSjslData } from '@/api/audit/sjfx'
  import SjfxReportDetailDialog from './SjfxReportDetailDialog.vue'

  export default {
    name: 'SjfxTable',
    components: {
      SjfxReportDetailDialog,
    },
    props: {
      year: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        sjfxList: [],
        loading: false,
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
        },
        reportDetailVisible: false,
        selectedProjectId: null,
        queryType: '', // 'wzg'(未整改) | 'yzg'(已整改) | 'zs'(整改总数)
      }
    },
    watch: {
      year: {
        handler() {
          this.getSjslData()
        },
        immediate: true,
      },
    },
    methods: {
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getSjslData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getSjslData()
      },
      async getSjslData() {
        this.loading = true
        const params = {
          year: this.year,
          ...this.queryForm,
        }
        const {
          code,
          data: { total, list },
        } = await getSjslData(params)
        if (code == 1) {
          this.sjfxList = list || []
          this.total = total || 0
          this.loading = false
        } else {
          this.loading = false
        }
      },
      handleCellClick(row, column, cell, event) {
        const prop = column.property

        // 只处理整改数量列的点击
        if (prop !== 'wzg' && prop !== 'yzg' && prop !== 'zs') {
          return
        }

        // 打印调试信息
        console.log('点击整改数量列:', prop)
        console.log('当前行数据:', row)
        console.log('当前行所有字段:', Object.keys(row))

        // 尝试多种可能的项目ID字段名
        this.selectedProjectId = row.PROJECTID || row.projectid || row.projectId || row.PROJECT_ID || row.project_id || ''

        // 根据点击的列设置queryType
        this.queryType = prop // 'wzg'(未整改) | 'yzg'(已整改) | 'zs'(整改总数)

        console.log('PROJECTID:', row.PROJECTID)
        console.log('projectid:', row.projectid)
        console.log('projectId:', row.projectId)
        console.log('PROJECT_ID:', row.PROJECT_ID)
        console.log('project_id:', row.project_id)
        console.log('最终selectedProjectId:', this.selectedProjectId)
        console.log('queryType:', this.queryType)

        console.log('传递给弹窗的参数:', {
          projectId: this.selectedProjectId,
          year: this.year,
          queryType: this.queryType
        })

        // 直接显示详情弹窗，不校验projectId
        this.reportDetailVisible = true
      },
    },
  }
</script>

<style scoped lang="scss">
  /* 标题样式 - 使用CSS变量跟随主题 */
  .table-title {
    color: var(--theme-color, #1890ff) !important;
    font-size: 16px !important;
    font-weight: bold !important;
    padding: 0 0 15px 0 !important;
    margin: 0;
    background: transparent !important;
    padding-top: 8px;
    padding-bottom: 8px;
  }

  /* 兼容旧的行内样式标题 */
  div[style*="padding: 0px 0 20px 10px"] {
    color: var(--theme-color, #1890ff) !important;
    font-size: 16px !important;
    font-weight: bold !important;
    padding: 0 0 15px 0 !important;
    margin: 0;
    background: transparent !important;
    padding-top: 8px;
    padding-bottom: 8px;
  }

  /* 表格样式 - 使用CSS变量跟随主题 */
  ::v-deep .el-table {
    background: #ffffff !important;
    border-radius: 8px;
    overflow: hidden;

    th.el-table__cell {
      background: linear-gradient(180deg, color-mix(in srgb, var(--theme-color, #1890ff) 8%, transparent) 0%, color-mix(in srgb, var(--theme-color, #1890ff) 15%, transparent) 100%) !important;
      color: color-mix(in srgb, var(--theme-color, #1890ff) 90%, black) !important;
      font-weight: 600;
      border-bottom: 2px solid var(--theme-color, #1890ff) !important;
    }

    td.el-table__cell {
      background: #ffffff !important;
      color: #606266 !important;
      border-bottom: 1px solid #ebeef5 !important;
    }

    tr:hover > td {
      background: color-mix(in srgb, var(--theme-color, #1890ff) 5%, transparent) !important;
    }

    .el-table__body tr.current-row > td {
      background: color-mix(in srgb, var(--theme-color, #1890ff) 10%, transparent) !important;
    }

    &.el-table--border {
      border: 1px solid #ebeef5 !important;

      &::after,
      &::before {
        background-color: #ebeef5 !important;
      }
    }

    .el-table__fixed-right::before,
    .el-table__fixed::before {
      background-color: #ebeef5 !important;
    }
  }

  /* 可点击列样式 */
  ::v-deep .clickable-column {
    cursor: pointer !important;

    .cell {
      color: var(--theme-color, #1890ff) !important;
      transition: all 0.3s ease;

      &:hover {
        font-weight: bold;
      }
    }
  }

  /* 分页样式 - 使用CSS变量跟随主题 */
  ::v-deep .el-pagination {
    .el-pagination__total,
    .el-pagination__jump {
      color: #606266 !important;
    }

    .el-pager li {
      background: #ffffff !important;
      color: #606266;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      transition: all 0.3s ease;
      margin: 0 2px;

      &:hover {
        background: color-mix(in srgb, var(--theme-color, #1890ff) 8%, transparent) !important;
        border-color: var(--theme-color, #1890ff);
        color: var(--theme-color, #1890ff);
      }

      &.active {
        background: linear-gradient(135deg, var(--theme-color, #1890ff) 0%, color-mix(in srgb, var(--theme-color, #1890ff) 80%, white) 100%) !important;
        color: #ffffff;
        border-color: var(--theme-color, #1890ff);
      }
    }

    button {
      background: #ffffff !important;
      color: #606266;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      transition: all 0.3s ease;

      &:hover {
        background: color-mix(in srgb, var(--theme-color, #1890ff) 8%, transparent) !important;
        color: var(--theme-color, #1890ff);
        border-color: var(--theme-color, #1890ff);
      }

      &:disabled {
        opacity: 0.5;
        background: #f5f5f5 !important;
        color: #c0c4cc;
        border-color: #dcdfe6;
      }
    }

    .el-pagination__sizes .el-select .el-input__inner {
      background: #ffffff !important;
      color: #606266;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
    }
  }
</style>
