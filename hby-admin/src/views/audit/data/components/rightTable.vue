<template>
  <div class="table_box" v-loading="loading">
    <!-- 项目查看 -->
    <el-form
      :model="form"
      v-if="nodeName === '' || nodeName === '项目情况一览'"
    >
      <el-row :gutter="24">
        <!-- <el-col :span="12" v-if="showMJ">
          <el-form-item label="密级" prop="secrectLevelId"
            :rules="[{ required: true, trigger: 'change', message: '请选择密级' }]">
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="disabled"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
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
            <el-select
              v-if="item.type === 'select'"
              :disabled="true"
              v-model="form[item.prop]"
              style="width: 100%"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
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
    <AuditTaskList v-if="nodeName === '审计任务清单'" :projectId="projectId" />
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
            {{
              item.prop1
                ? row[item.prop]
                  ? row[item.prop][item.prop1]
                  : ''
                : row[item.prop]
            }}
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
      >
        <template v-if="item.hasAction" #default="{ row }">
          <el-button
            type="text"
            :disabled="
              row.status
                ? !row.status
                : row.reportstatus
                ? !row.reportstatus
                : !row.state
            "
            @click="handleProcess(row, nodeName)"
          >
            办理
          </el-button>
          <el-button
            type="text"
            v-show="item.report"
            :disabled="item.reportDisabled"
            @click="handleExport(row, item.report)"
          >
            导出
          </el-button>
        </template>
        <template v-else-if="item.hasActionTwo" #default="{ row }">
          <el-button type="text" @click="handleProcess(row, nodeName)">
            查看关联底稿
          </el-button>
        </template>
      </el-table-column>
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
    <WorkReport ref="工作小结" :projectId="projectId" />
    <YDGL ref="疑点管理" />
    <WfqdDeal ref="wfqddeal" />
    <relateDraftModalList ref="modal" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
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
    workReportList,
    workReportDetail,
    doubtfulDetail,
    doubtfulList,
    qZDExport,
    myDraftExport,
    projectStandardDpLis,
  } from '@/api/audit/implement'
  import { myTaskDisp } from '@/oapi/audit/implement.js'
  import {
    getDataprojectList,
    getListZy,
    getNoticeList,
  } from '@/api/audit/preparation'
  import { getProjectDetails } from '@/api/audit/projectData'
  import {
    auditSuggestDetail,
    auditSuggestList,
    report,
    reportDetail,
    reportExport,
  } from '@/api/audit/report'
  import DoubtfulInfo from '@/views/audit/implement/components/auditEvidenceInfo.vue'
  import DraftManageInfo from '@/views/audit/implement/components/myDraftInfo.vue'
  import RecordListInfo from '@/views/audit/implement/components/RecordListInfo.vue'
  import NoticeInfo from '@/views/audit/prepare/components/NoticeInfo.vue'
  import ProjectDataInfo from '@/views/audit/prepare/components/ProjectDataInfo.vue'
  import IndexView from '@/views/audit/report/components/IndexView'
  import SuggestView from '@/views/audit/report/components/SuggestView'
  import Project from './project.vue'
  import WorkReport from '@/views/audit/implement/components/LogInfo.vue'
  import YDGL from '@/views/audit/implement/components/DoubtfulInfo.vue'
  import AuditTaskList from '@/views/audit/prepare/guide.vue'
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
    workSummary,
    doubtManagement,
  } from './table'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import relateDraftModalList from '@/views/audit/implement/components/options/relateDraftModal.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
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
      WorkReport,
      WfqdDeal,
      AuditTaskList,
      YDGL,
      relateDraftModalList,
      ZXPerson,
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
          工作小结: workSummary,
          疑点管理: doubtManagement,
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
        showMJ: false,
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
    async created() {
      this.showMJ = couldMJ()
      console.log('created', this.showMJ)
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('PlanIndex')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    mounted() {
      this.getDataByNodeName(this.nodeName)
    },
    methods: {
      async handleExport(row, report) {
        console.log(report)
        let data, fileName
        if (report == '审计取证单') {
          data = await qZDExport({ certificateId: row.certificateId })
          fileName = '审计取证单'
        } else if (report == '底稿管理') {
          data = await myDraftExport({ sheetid: row.sheetId })
          fileName = '底稿管理.docx'
        } else if (report == '审计报告') {
          data = await reportExport({ reportid: row.reportid })
          fileName = row.reportname + '.doc'
        }
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
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
          // console.log(row, 'row')
          const data = await draftManageDelete({ sheetid: row.sheetId })
          this.$refs['底稿管理'].showEdit('detail', data.data)
        } else if (this.nodeName === '审计通知书') {
          this.$refs['审计通知书'].showEdit(row, true)
        } else if (this.nodeName === '审计建议书') {
          const data = await auditSuggestDetail({ proid: row.proid })
          await this.$refs['审计建议书'].showEdit('detail', data.data)
        } else if (this.nodeName === '项目资料') {
          this.$refs['项目资料'].showEdit(row, false)
        } else if (this.nodeName == '工作小结') {
          this.logoState = true
          const data = await workReportDetail({ reportid: row.reportid })
          this.$nextTick(() => {
            this.$refs['工作小结'].showEdit('detail', data.data)
          })
        } else if (this.nodeName == '疑点管理') {
          this.logoState = true
          const data = await doubtfulDetail({ dpointid: row.dpointid })
          this.$nextTick(() => {
            this.$refs['疑点管理'].showEdit('detail', data.data)
          })
        } else {
          console.log('🚀 ~ handleDetail ~ row:', row)
          const data = await myTaskDisp({ programid: row.programid })
          this.$refs['recordListInfo'].showEdit(data.data)
          // const info = {
          //   programId: +row.programid ? +row.programid : +row.programId,
          // }
          // this.$refs['recordListInfo'].showEdit(info, true)
        }
      },
      getDataByNodeName(val) {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
        }
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
          case '工作小结':
            this.getWorkSummary()
            break
          case '疑点管理':
            this.getDoubtfulList()
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
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getDataByNodeName(this.nodeName)
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
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
        console.log(list, 'list')
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
      //工作小结
      async getWorkSummary() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        // const {
        //   data: {
        //     pageInfo: { tlist: list, totalRecord: total },
        //   },
        // } = await workReportList(obj)
        let res = await workReportList(obj)
        this.tableData = res.data.pageInfo?.tlist || []
        this.total = res.data.pageInfo?.totalRecord || 0
        this.loading = false
      },
      //疑点管理
      async getDoubtfulList() {
        this.loading = true
        let obj = { ...this.queryForm }
        if (this.projectId > 0) {
          obj.projectId = this.projectId
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await projectStandardDpLis(obj)
        this.tableData = list
        this.total = total
        this.loading = false
      },
      //办理按钮
      handleProcess(row, nodeName) {
        console.log('🚀 ~ handleProcess ~ nodeName:', nodeName)
        switch (nodeName) {
          case '审计取证单':
            this.handleDeal(row.certificateId, 16)
            break
          case '底稿管理':
            this.handleDeal(row.sheetId, 10)
            break
          case '审计报告':
            this.handleDeal(row.reportid, 207)
            break
          case '任务查看':
            this.checkRelate(row)
            break
          default:
            console.log('未知表格类型')
        }
      },
      async handleDeal(id, tableId) {
        const res = await getFlowPkInfo({
          formId: id,
          tableId: tableId,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
      //任务查看
      checkRelate(row) {
        this.$refs['modal'].showEdit(row.operateid, this.projectId)
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.form, 'staffScopeIds', ids)
        this.$set(this.form, 'staffScopeNames', names)
      },
    },
  }
</script>

<style lang="scss" scoped></style>
