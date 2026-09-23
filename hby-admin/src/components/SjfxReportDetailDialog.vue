<template>
  <el-dialog
    :visible.sync="dialogVisible"
    title="审计分析报告详情"
    width="85%"
    :before-close="handleClose"
    append-to-body
  >
    <div v-loading="loading">
      <el-alert
        :title="`项目ID：${projectId || '-'} | 年度：${year || '-'}`"
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
          label="问题描述"
          prop="questionMemo"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="预计完成时间"
          prop="planDeadline"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{ scope.row.planDeadline ? scope.row.planDeadline.split(' ')[0] : '-' }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="责任部门"
          prop="orgName"
          min-width="150"
          show-overflow-tooltip
        />
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
      </el-table>

      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="'total, sizes, prev, pager, next, jumper'"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
  </el-dialog>
</template>

<script>
  import { getZgwtDetail } from '@/api/audit/sjfx'

  export default {
    name: 'SjfxReportDetailDialog',
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
      projectId: {
        type: [String, Number],
        default: null,
      },
      year: {
        type: String,
        default: '',
      },
      queryType: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        loading: false,
        tableData: [],
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
        },
      }
    },
    computed: {
      dialogVisible: {
        get() {
          return this.visible
        },
        set(val) {
          this.$emit('update:visible', val)
        },
      },
    },
    watch: {
      visible(val) {
        console.log('===== SjfxReportDetailDialog visible changed =====', val)
        console.log('projectId:', this.projectId)
        console.log('year:', this.year)

        if (val) {
          // 弹窗打开时，无论是否有 projectId 都加载数据
          this.queryForm.pageNumber = 1
          this.fetchData()
        } else {
          this.resetData()
        }
      },
      // 监听 projectId 变化
      projectId(newVal) {
        console.log('===== SjfxReportDetailDialog projectId changed =====', newVal)
        if (this.visible) {
          this.fetchData()
        }
      }
    },
    methods: {
      async fetchData() {
        this.loading = true
        try {
          const params = {
            projectId: this.projectId,
            year: this.year,
            queryType: this.queryType,
            pageNumber: this.queryForm.pageNumber,
            pageSize: this.queryForm.pageSize,
          }

          console.log('审计分析报告详情查询参数:', params)

          const response = await getZgwtDetail(params)

          console.log('审计分析报告详情响应:', response)

          // 接口返回格式: { code: 1, msg: "成功", data: { pageInfo: { tlist: [], totalRecord: 1 } } }
          if (response && response.code === 1) {
            this.tableData = response.data?.pageInfo?.tlist || []
            this.total = response.data?.pageInfo?.totalRecord || 0
          } else {
            this.$message.error(response?.msg || '查询失败')
            this.tableData = []
            this.total = 0
          }
        } catch (error) {
          console.error('查询审计分析报告详情失败:', error)
          this.$message.error('查询失败，请稍后重试')
          this.tableData = []
          this.total = 0
        } finally {
          this.loading = false
        }
      },
      handleCurrentChange(page) {
        this.queryForm.pageNumber = page
        this.fetchData()
      },
      handleSizeChange(size) {
        this.queryForm.pageSize = size
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      handleClose() {
        this.dialogVisible = false
      },
      resetData() {
        this.tableData = []
        this.total = 0
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 10
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
    },
  }
</script>

<style scoped lang="scss">
  // Alert组件样式 - 隐藏边框
  ::v-deep .el-alert {
    border: none !important;
  }

  ::v-deep .el-alert--info {
    border: none !important;
  }

  ::v-deep .el-alert--info.is-light {
    border: none !important;
  }
</style>
