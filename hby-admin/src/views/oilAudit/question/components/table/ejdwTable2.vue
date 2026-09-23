<template>
  <el-card shadow="never">
    <el-divider>二、已离任未委托及预计离任的行政正职</el-divider>

    <el-table v-loading="listLoading" :data="list" :max-height="500">
      <el-table-column
        align="center"
        label="序号"
        type="index"
        width="80">
      </el-table-column>
      <el-table-column
        align="center"
        label="单位"
        prop="org"
        width="140"
      >
        <template #default="{ row }">
            {{ row.tblOrganization? row.tblOrganization.orgname : '' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="审计情况"
        prop=""
      />
      <el-table-column
        align="center"
        label="审计时间"
        prop="auditTime"
      />
      <el-table-column
        align="center"
        label="截至本年未审年限"
        prop=""
      />
      <el-table-column
        align="center"
        label="未审计月数"
        prop="unauditMonth"
      />
      <el-table-column
        align="center"
        label="最近一次审计情况"
        prop=""
      >
        <el-table-column
          align="center"
          label="项目名称"
          prop="projectName"
        />
        <el-table-column
          align="center"
          label="任职时间（审计范围）"
          prop="workTime"
        />
        <el-table-column
          align="center"
          label="审计实施时间"
          prop="doAuditTime"
        />
        <el-table-column
          align="center"
          label="组长"
          prop="teamLeaderName"
        />
        <el-table-column
          align="center"
          label="副组长"
          prop=""
        />
        <el-table-column
          align="center"
          label="牵头人"
          prop="leaderName"
        />
        <el-table-column
          align="center"
          label="主审"
          prop="chiefReviewerName"
        />
        <el-table-column
          align="center"
          label="助审"
          prop="deputyReviewerName"
        />
      </el-table-column>

      <el-table-column width="1" />
    </el-table>

    <el-pagination
      background
      :current-page="queryForm.currentPage"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :page-sizes="pageSizes"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </el-card>
</template>

<script>
import { wwtsjGetlist } from '@/oapi/audit/implement'
import { formatDate, formatDay } from '@/utils/index'

export default {
  name: 'ejdwTable1',
  components: {
  },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        projectName: '',
        pageNumber: 1,
        pageSize: 20,
      },
      filedAll: [
        { name: '项目承担单位（实施单位）' },
        { name: '被审计单位名称' },
        { name: '审计项目名称' },
        // { name: '项目级别（二级机构/三级机构）' },
        // { name: '业务领域' },
        // { name: '具体业务' },
        { name: '经责科负责人' },
        { name: '审计组人数' },
        { name: '现场审计开始日期' },
        { name: '实际现场结束日期' },
        { name: '实际工作天数' },
        { name: '投入资源（人日）' },
        { name: '工效比' },
        { name: '复合底稿数量' },
        { name: '问题底稿数量' },
      ], //所有表格项
      filedNow: [], //当前表格项
      searchAll: this.getFiled(), //所有搜索项
      searchNow: [], //当前所有搜索项
      searchItem: [], //可见搜索项
      localKey: 'oilAudit-oilAudit-question-search',
      tableKey: 'oilAudit-oilAudit-question-list',
      searchMore: true,
    }
  },
  created() {
    this.fetchData()
    this.initTable()
    this.searchNow = this.getFiled()
    this.searchItem = this.searchNow.slice(0, 4)
    this.initSearch()
  },
  methods: {
    getFiled() {
      return [
        { name: '批次', key: 'pc' },
        { name: '项目名称', key: 'projectname' },
        { name: '被审计单位名称', key: 'auditorgname' },
      ]
    },
    resetQueryForm() {
      this.queryForm = {
        projectName: '',
        pageNumber: 1,
        pageSize: 100,
      }
      this.fetchData()
    },
    resetSearch() {
      this.resetQueryForm()
      this.fetchData()
    },
    formatDate(row, column) {
      // 获取单元格数据
      let data = row[column.property]
      return formatDay(data)
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    queryData() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    async fetchData() {
      this.listLoading = true
      const {
        data: {
          tlist, totalRecord
        }
      } = await wwtsjGetlist(this.queryForm)
      this.list = tlist
      this.total = totalRecord
      this.listLoading = false
    },
    // handleAdd() {
    //   this.$refs['edit'].showEdit('add', null)
    // },
    async handleDetail(row) {
      // const data = await dgDetail({ sheetid: Number(row.sheetId) })
      await this.$refs['edit'].showEdit('detail', row)
    },
    // async handleEdit(row) {
    //   const data = await dgDetail({ sheetid: row.sheetid })
    //   await this.$refs['edit'].showEdit('edit', data.data)
    // },
    // handleDelete(row) {
    //   this.$baseConfirm('你确定要删除当前项吗', null, async () => {
    //     const { msg, code } = await riskDel({
    //       riskid: row.riskid,
    //     })
    //     if (code == 0) {
    //       this.$baseMessage(msg, 'success')
    //     } else {
    //       this.$baseMessage(msg, 'error')
    //     }
    //     await this.fetchData()
    //   })
    // },
    sendModel() {
      this.$refs['sendModel'].showEdit()
    },
    send() {
      this.$refs['send'].showEdit()
    },
    handleObject() {
      this.$refs['audiTree'].showEdit()
    },
    getChildlistObj(val, flag) {
      if (flag == 'right') {
        this.pdDx = 'yh'
        this.$set(this.queryForm, 'orgName', val[0].realname)
        this.$set(this.queryForm, 'auditStaffId', val[0].staffid)
        this.$set(this.queryForm, 'auditOrgId', '')
      } else {
        this.pdDx = 'bm'
        this.$set(this.queryForm, 'auditOrgId', val.id)
        this.$set(this.queryForm, 'orgName', val.name)
        this.$set(this.queryForm, 'auditStaffId', '')
      }
      this.$forceUpdate()
    },
  },
}
</script>

<style scoped lang="scss">
.system-log-container {
  padding: 0 !important;
  background: #f6f8f9 !important;
}
.margin-b0 {
  margin-bottom: 0;
}
.upload-demo {
  display: inline-block;
  margin: 0 10px;
}
</style>
