<template>
  <div>
    <el-card style="width: 100%; padding: 10px; margin-bottom: 10px">
      <div class="table-title">整改问题一览表</div>
      <el-table
        :data="zgwtList"
        v-loading="loading"
        @cell-click="handleCellClick"
      >
        <el-table-column
          align="center"
          label="被审计单位"
          prop="orgname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="已整改数量"
          prop="yzg"
          class-name="clickable-column"
        />
        <el-table-column
          align="center"
          label="未整改数量"
          prop="wzg"
          class-name="clickable-column"
        />
        <el-table-column
          align="center"
          label="整改总数"
          prop="zs"
          class-name="clickable-column"
        />
        <el-table-column
          align="center"
          label="已销号数量"
          prop="yxh"
          class-name="clickable-column"
        />
        <el-table-column
          align="center"
          label="未销号数量"
          prop="wxh"
          class-name="clickable-column"
        />
        <!-- 销号总数列已隐藏 -->
        <!-- <el-table-column
          align="center"
          label="销号总数"
          prop="xhzs"
          class-name="clickable-column"
        /> -->
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

    <!-- 整改问题详情弹窗 -->
    <ZgwtIssuesDetailDialog
      :visible.sync="zgwtDetailVisible"
      :query-type="detailQueryType"
      :org-name="detailOrgName"
      :year="year"
    />
  </div>
</template>

<script>
  import { getZgwtData } from '@/api/audit/sjfx'
  import ZgwtIssuesDetailDialog from './ZgwtIssuesDetailDialog.vue'

  export default {
    name: 'ZgwtTable',
    components: {
      ZgwtIssuesDetailDialog,
    },
    props: {
      year: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        zgwtList: [],
        total: 0,
        loading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
        },
        // 整改问题详情弹窗
        zgwtDetailVisible: false,
        detailQueryType: '', // 'yzg'(已整改) | 'wzg'(未整改) | 'zs'(整改总数) | 'yxh'(已销号) | 'wxh'(未销号) | 'xhzs'(销号总数)
        detailOrgName: '', // 主管部门名称
      }
    },
    watch: {
      year: {
        handler(newVal) {
          if (newVal) {
            this.getZgwtData()
          }
        },
        immediate: true,
      },
    },
    created() {
      this.getZgwtData()
    },
    methods: {
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getZgwtData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getZgwtData()
      },
      // 处理表格单元格点击事件
      handleCellClick(row, column, cell, event) {
        const prop = column.property

        // 只处理数字列的点击
        const clickableProps = ['yzg', 'wzg', 'zs', 'yxh', 'wxh', 'xhzs']
        if (!clickableProps.includes(prop)) {
          return
        }

        // 设置查询参数
        this.detailQueryType = prop
        this.detailOrgName = row.orgname
        this.zgwtDetailVisible = true
      },
      async getZgwtData() {
        this.loading = true
        const params = {
          year: this.year,
          ...this.queryForm,
        }
        const {
          code,
          data: { total, list },
        } = await getZgwtData(params)
        if (code == 1) {
          this.zgwtList = list || []
          this.total = total || 0
          this.loading = false
        } else {
          this.loading = false
        }
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
  div[style*='padding: 0px 0 20px 10px'] {
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
      background: linear-gradient(
        180deg,
        color-mix(in srgb, var(--theme-color, #1890ff) 8%, transparent) 0%,
        color-mix(in srgb, var(--theme-color, #1890ff) 15%, transparent) 100%
      ) !important;
      color: color-mix(
        in srgb,
        var(--theme-color, #1890ff) 90%,
        black
      ) !important;
      font-weight: 600;
      border-bottom: 2px solid var(--theme-color, #1890ff) !important;
    }

    td.el-table__cell {
      background: #ffffff !important;
      color: #606266 !important;
      border-bottom: 1px solid #ebeef5 !important;
    }

    tr:hover > td {
      background: color-mix(
        in srgb,
        var(--theme-color, #1890ff) 5%,
        transparent
      ) !important;
    }

    .el-table__body tr.current-row > td {
      background: color-mix(
        in srgb,
        var(--theme-color, #1890ff) 10%,
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

    .el-table__fixed-right::before,
    .el-table__fixed::before {
      background-color: #ebeef5 !important;
    }

    /* 可点击列样式 */
    .clickable-column {
      cursor: pointer !important;

      .cell {
        color: var(--theme-color, #1890ff) !important;
        transition: all 0.3s ease;

        &:hover {
          font-weight: bold;
        }
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
        background: color-mix(
          in srgb,
          var(--theme-color, #1890ff) 8%,
          transparent
        ) !important;
        border-color: var(--theme-color, #1890ff);
        color: var(--theme-color, #1890ff);
      }

      &.active {
        background: linear-gradient(
          135deg,
          var(--theme-color, #1890ff) 0%,
          color-mix(in srgb, var(--theme-color, #1890ff) 80%, white) 100%
        ) !important;
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
        background: color-mix(
          in srgb,
          var(--theme-color, #1890ff) 8%,
          transparent
        ) !important;
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
