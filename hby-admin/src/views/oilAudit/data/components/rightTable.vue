<template>
  <div class="table_box" v-loading="loading">
    <!-- 项目查看 -->
    <el-form
      :model="form"
      v-if="nodeName === '' || nodeName === '项目情况一览'"
    >
      <el-row :gutter="24">
        <el-col :span="12" v-for="item in projectFormOption" :key="item.prop">
          <el-form-item
            :label="item.label"
            :prop="item.prop"
            label-width="140px"
          >
            <el-input
              v-if="item.type === 'input'"
              :disabled="true"
              v-model="form[item.prop]"
            ></el-input>
            <el-date-picker
              :disabled="true"
              :style="{ width: '100%' }"
              v-if="item.type === 'date'"
              v-model="form[item.prop]"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <Project v-if="nodeName === '项目方案'" :projectId="projectId" />
    <!-- v-if="showTable.includes(nodeName)" -->
    <el-table
      v-if="showTable[this.nodeName] !== undefined"
      :data="tableData"
      v-loading="loading"
    >
      <el-table-column
        align="center"
        v-for="item in [showTable[this.nodeName][0]]"
        :key="item.prop + Math.ceil(Math.random() * 10000)"
        show-overflow-tooltip
        :label="item.label"
        :prop="item.prop"
        :formatter="item.formatter"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            <!-- {{ row[item.prop] }} -->
            {{ item.prop1 ? row[item.prop][item.prop1] : row[item.prop] }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        v-for="item in showTable[this.nodeName].slice(1)"
        :key="item.prop + Math.ceil(Math.random() * 10000)"
        show-overflow-tooltip
        :label="item.label"
        :prop="item.prop"
        :formatter="item.formatter"
      ></el-table-column>
    </el-table>
    <el-pagination
      v-if="showTable[this.nodeName] !== undefined"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <IndexView ref="审计报告" />
    <DoubtfulInfo ref="审计取证单" />
    <DraftManageInfo ref="底稿管理" />
    <NoticeInfo ref="审计通知书" />
    <SuggestView ref="审计建议书" />
    <record-list-info ref="recordListInfo" />
    <project-data-info ref="项目资料" />
  </div>
</template>

<script>
  import {
    approachSummaryList,
    confirmList,
    discoverList,
    draftManageDelete,
    getImPlementOrder,
    imPlementOrderDetail,
    leaveSummaryList,
    myDraftList,
    myTaskListData,
  } from '@/oapi/audit/implement'
  import {
    getDataprojectList,
    getListZy,
    getNoticeList,
  } from '@/oapi/audit/preparation'
  import { getProjectDetails } from '@/oapi/audit/projectData'
  import {
    auditSuggestDetail,
    auditSuggestList,
    report,
    reportDetail,
  } from '@/oapi/audit/report'
  import DoubtfulInfo from '@/views/oilAudit/implement/components/auditEvidenceInfo.vue'
  import DraftManageInfo from '@/views/oilAudit/implement/components/myDraftInfo.vue'
  import RecordListInfo from '@/views/oilAudit/plan/components/RecordListInfo'
  import NoticeInfo from '@/views/oilAudit/prepare/components/NoticeInfo.vue'
  import ProjectDataInfo from '@/views/oilAudit/prepare/components/ProjectDataInfo.vue'
  import IndexView from '@/views/oilAudit/report/components/IndexView'
  import SuggestView from '@/views/oilAudit/report/components/SuggestView'
  import Project from './project.vue'
  import {
    auditFound,
    auditingTableOption,
    auditNotice,
    auditProposal,
    auditReport,
    confirmationOfFact,
    inTheMinutes,
    leaveTheMinutes,
    projectFormOption,
    projects,
    qZDPapers,
    taskView,
    workingPapers,
  } from './table'

  export default {
    props: {
      nodeName: {
        type: String,
        default: '',
      },
      targetId: {
        type: Number,
        default: 0,
      },
      projectId: {
        type: Number,
        default: undefined,
      },
    },
    components: {
      Project,
      IndexView,
      DoubtfulInfo,
      DraftManageInfo,
      NoticeInfo,
      SuggestView,
      RecordListInfo,
      ProjectDataInfo,
    },
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        showTable: {
          审计指引: auditingTableOption,
          审计通知书: auditNotice,
          进场纪要: inTheMinutes,
          任务查看: taskView,
          底稿管理: workingPapers,
          审计取证单: qZDPapers,
          审计发现: auditFound,
          事实确认书: confirmationOfFact,
          离场纪要: leaveTheMinutes,
          审计报告: auditReport,
          审计建议书: auditProposal,
          项目资料: projects,
        },
        allTableName: [],
        tableData: [],
        loading: false,
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
        },
        form: { pmStaff: { realname: '' } },
        projectFormOption: projectFormOption,
      }
    },
    watch: {
      nodeName(val) {
        // if(this.showTable.includes(val))\
        this.getDataByNodeName(val)
      },
      targetId(val) {
        this.getDataByNodeName(this.nodeName)
      },
      projectId(val) {
        this.getDataByNodeName(this.nodeName)
      },
    },
    mounted() {
      this.getDataByNodeName(this.nodeName)
    },
    methods: {
      async handleDetail(row) {
        if (this.nodeName === '审计报告') {
          const data = await reportDetail({ reportid: row.reportid })
          await this.$refs['审计报告'].showEdit('detail', data.data)
        } else if (this.nodeName === '审计取证单') {
          const data = await imPlementOrderDetail({
            certificateId: row.certificateId,
          })
          this.$refs['审计取证单'].showEdit('detail', data.data.certificate)
        } else if (this.nodeName === '底稿管理') {
          const data = await draftManageDelete({ sheetid: row.sheetId })
          this.$refs['底稿管理'].showEdit('detail', data.data)
        } else if (this.nodeName === '审计发现') {
          const data = await draftManageDelete({ sheetid: row.sheetId })
          this.$refs['底稿管理'].showEdit('detail', data.data)
        } else if (this.nodeName === '审计通知书') {
          this.$refs['审计通知书'].showEdit(row, true)
        } else if (this.nodeName === '审计建议书') {
          const data = await auditSuggestDetail({ proid: row.proid })
          await this.$refs['审计建议书'].showEdit('detail', data.data)
        } else if (this.nodeName === '项目资料') {
          this.$refs['项目资料'].showEdit(row, false)
        } else {
          const info = {
            programId: +row.programid ? +row.programid : +row.programId,
          }
          this.$refs['recordListInfo'].showEdit(info, true)
        }
      },
      getDataByNodeName(val) {
        // this.resetQueryForm()
        switch (val) {
          case '审计指引':
            this.getAuditingTableOption()
            break
          case '审计通知书':
            this.getAuditNotice()
            break
          case '进场纪要':
            this.getInTheMinutes()
            break
          case '任务查看':
            this.getTaskView()
            break
          case '底稿管理':
            this.getWorkingPapers()
            break
          case '审计取证单':
            this.getQZDPapers()
            break
          case '审计发现':
            this.getAuditFound()
            break
          case '事实确认书':
            this.getConfirmationOfFact()
            break
          case '离场纪要':
            this.getLeaveTheMinutes()
            break
          case '审计报告':
            this.getAuditReport()
            break
          case '审计建议书':
            this.getAuditProposal()
            break
          case '项目资料':
            this.getProjectsData()
            break
          default:
            this.projectDetails()
        }
        // this.$forceUpdate()
      },
      // 重置筛选条件
      resetQueryForm() {
        this.tableData = []
        this.total = 0
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
        }
      },
      //分页查询
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getDataByNodeName(this.nodeName)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getDataByNodeName(this.nodeName)
      },
      //审计指引
      async getAuditingTableOption() {
        this.loading = true
        let obj = {}
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        if (this.targetId !== 0) {
          obj.targetId = this.targetId
        }
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getListZy(obj)
        this.tableData = tlist
        this.total = totalRecord
        this.loading = false
      },
      //项目查看
      projectDetails() {
        this.loading = true

        getProjectDetails({ projectId: this.projectId })
          .then((res) => {
            this.form = res.data.pj
            this.form.realname = res.data.pj.pmStaff.realname
            this.form.auditOrgName = res.data.pj.auditStaffName
              ? res.data.pj.auditStaffName
              : res.data.pj.auditOrgName
            this.form.sjtrry = res.data.sjtrry
            this.form.jcqd = res.data.jcqd
            this.form.fawt = res.data.fawt
            this.$forceUpdate()
            this.loading = false
          })
          .catch((err) => {})
      },
      // 审计通知书
      async getAuditNotice() {
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        this.loading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getNoticeList(obj)
        tlist.forEach((v) => {
          v.creatrtime = v.creatrtime.split('T')[0]
        })
        this.tableData = tlist
        this.total = totalRecord
        this.loading = false
      },
      // 进场纪要
      async getInTheMinutes() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await approachSummaryList(obj)
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 任务查看
      async getTaskView() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.targetId !== 0) {
          obj.targetId = this.targetId
        }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await myTaskListData(obj)
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 工作底稿
      async getWorkingPapers() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        let {
          data: {
            project: project,
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await myDraftList(obj)
        if (list && list.length) {
          list.map((item) => {
            item.targetName = project.auditStaffName
              ? project.auditStaffName
              : project.auditOrgName
            item.approver = item.yjfh + ',' + item.ejfh
          })
        }
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 审计取证单
      async getQZDPapers() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        let {
          data: {
            // project: project,
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getImPlementOrder(obj)
        // if (list && list.length) {
        //   list.map((item) => {
        //     item.targetName = project.auditStaffName
        //       ? project.auditStaffName
        //       : project.auditOrgName
        //     item.approver = item.yjfh + ',' + item.ejfh
        //   })
        // }
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 审计发现
      async getAuditFound() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        let {
          data: {
            project: project,
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await discoverList(obj)
        if (list && list.length) {
          list.map((item) => {
            item.targetName = project.auditStaffName
              ? project.auditStaffName
              : project.auditOrgName
          })
        }
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 事实确认书
      async getConfirmationOfFact() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await confirmList(obj)
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 离场纪要
      async getLeaveTheMinutes() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await leaveSummaryList(obj)
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 审计报告
      async getAuditReport() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await report(obj)
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 审计建议书
      async getAuditProposal() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await auditSuggestList(obj)
        this.tableData = list
        this.total = total
        this.loading = false
      },
      // 项目资料
      async getProjectsData() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getDataprojectList(obj)
        this.tableData = list
        this.total = total
        this.loading = false
      },
    },
  }
</script>

<style lang="scss" scoped></style>
