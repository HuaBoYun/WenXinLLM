<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="dialogTitle"
    width="85%"
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
        <el-table-column align="center" label="序号" type="index" width="60" />
        <el-table-column
          align="center"
          label="问题编号"
          prop="issuesCode"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="问题名称"
          prop="issuesName"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="整改通知"
          prop="rectificationPlan"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="预计完成时间"
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
          label="被审计单位"
          prop="auditObjectName"
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
            <el-tag
              :type="getConclusionTagType(scope.row.conclusion)"
              size="small"
            >
              {{ scope.row.conclusion || '未整改' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="是否销号"
          prop="isxh"
          min-width="100"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.isxh === '1' ? 'success' : 'info'"
              size="small"
            >
              {{ scope.row.isxh === '1' ? '已销号' : '未销号' }}
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
  import { getZgwtIssuesDetail } from '@/api/audit/sjfx'

  export default {
    name: 'ZgwtIssuesDetailDialog',
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
      queryType: {
        type: String,
        default: '',
      },
      orgName: {
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
      dialogTitle() {
        const typeMap = {
          yzg: '已整改问题详情',
          wzg: '未整改问题详情',
          zs: '整改问题总数详情',
          yxh: '已销号问题详情',
          wxh: '未销号问题详情',
          xhzs: '销号问题总数详情',
        }
        return typeMap[this.queryType] || '整改问题详情'
      },
      alertTitle() {
        return `被审计单位：${this.orgName || '全部'} | 年度：${
          this.year || '全部'
        } | 查询类型：${this.dialogTitle}`
      },
    },
    watch: {
      visible(val) {
        if (val) {
          this.queryForm.pageNumber = 1
          this.fetchData()
        } else {
          this.resetData()
        }
      },
    },
    methods: {
      async fetchData() {
        if (!this.queryType) {
          this.$message.warning('查询类型不能为空')
          return
        }

        this.loading = true
        try {
          const params = {
            orgName: this.orgName,
            queryType: this.queryType,
            year: this.year,
            pageNumber: this.queryForm.pageNumber,
            pageSize: this.queryForm.pageSize,
          }

          console.log('整改问题一览表详情查询参数:', params)

          const response = await getZgwtIssuesDetail(params)

          console.log('整改问题一览表详情响应:', response)

          // 接口返回格式: { code: 1, msg: "成功", data: { tlist: [], totalRecord: 1 } }
          if (response && response.code === 1) {
            this.tableData = response.data?.tlist || []
            this.total = response.data?.totalRecord || 0
          } else {
            this.$message.error(response?.msg || '查询失败')
            this.tableData = []
            this.total = 0
          }
        } catch (error) {
          console.error('查询整改问题详情失败:', error)
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
        // conclusion为null或空时，表示未整改
        if (!conclusion) {
          return 'warning'
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
