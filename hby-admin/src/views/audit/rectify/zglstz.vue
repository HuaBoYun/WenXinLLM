<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-if="item.name === '问题编号'"
                v-model="queryForm.issuesCode"
                clearable
                placeholder="问题编号"
              />
              <el-input
                v-if="item.name === '问题名称'"
                v-model="queryForm.issuesName"
                clearable
                placeholder="问题名称"
              />

              <el-input
                v-if="item.name === '一级单位'"
                v-model="queryForm.oneorgname"
                clearable
                placeholder="请输入一级单位"
              />
              <el-input
                v-if="item.name == '具体责任单位'"
                v-model="queryForm.auditObjectName"
                clearable
                placeholder="请输入具体责任单位"
              />
              <el-input
                v-if="item.name == '问题来源'"
                v-model="queryForm.problemsrc"
                clearable
                placeholder="请输入问题来源"
              />
              <el-date-picker
                v-if="item.name == '审计报告出具年份'"
                v-model="queryForm.reportyear"
                placeholder="审计报告出具年份"
                type="year"
                format="yyyy"
                value-format="yyyy"
              />
              <el-select
                v-model="queryForm.problemtype"
                v-if="item.name == '问题类别'"
                placeholder="请选择问题类别"
              >
                <el-option
                  v-for="(item, index) in questionTypeList"
                  :key="index"
                  :label="item.auditType"
                  :value="item.auditType"
                ></el-option>
              </el-select>
              <el-select
                v-model="queryForm.recttype"
                v-if="item.name == '整改类型'"
                placeholder="请选择整改类型"
              >
                <el-option label="立行立改" value="立行立改" />
                <el-option label="分阶段整改" value="分阶段整改" />
                <el-option label="持续整改" value="持续整改" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>

            <el-form-item>
              <el-tooltip
                class="item"
                effect="dark"
                content="搜索筛选"
                placement="top"
              >
                <el-popover placement="left" trigger="click">
                  <filter-search
                    v-if="true"
                    :list="searchAll"
                    :name="localKey"
                    @updateSearchShow="initSearch"
                  />
                  <el-button slot="reference" style="height: 32px">
                    <vab-icon icon="filter" :is-custom-svg="true" />
                  </el-button>
                </el-popover>
              </el-tooltip>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>
    <el-card shadow="never">
      <vab-query-form-right-panel :span="24">
        <el-tooltip
          class="item"
          effect="dark"
          content="表格筛选"
          placement="top"
        >
          <el-popover placement="right" trigger="click">
            <filter-table
              :list="filedAll"
              :name="tableKey"
              @updateTableShow="initTable"
            />
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>

        <el-button type="success" @click="handleExport('问题清单')">
          问题清单导出
        </el-button>
        <el-button type="success" @click="handlePreviewExport('责任清单')">
          责任清单预览
        </el-button>
        <el-button type="success" @click="handlePreviewExport('整改清单')">
          整改清单预览
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="问题编号" prop="issuesCode">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.issuesCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '问题名称'"
            align="center"
            label="问题名称"
            prop="issuesName"
          />
          <el-table-column
            v-if="item.name === '单位名称'"
            align="center"
            label="单位名称"
            prop="issuesName"
          >
            <el-table-column
              align="center"
              label="一级单位"
              prop="oneorgname"
            />
            <el-table-column
              align="center"
              label="具体责任单位"
              prop="auditObjectName"
              width="200"
            />
          </el-table-column>
          <el-table-column
            align="center"
            label="问题来源"
            prop="problemsrc"
            v-if="item.name === '问题来源'"
          />
          <el-table-column
            align="center"
            label="审计报告出具年份"
            prop="reportyear"
            v-if="item.name === '审计报告出具年份'"
          />
          <el-table-column
            align="center"
            label="问题类别"
            prop="problemtype"
            v-if="item.name === '问题类别'"
          />
          <el-table-column
            align="center"
            label="整改目标清单"
            v-if="item.name === '整改目标清单'"
          >
            <el-table-column align="center" label="整改类型" prop="recttype" />
            <el-table-column
              align="center"
              label="整改时限"
              prop="recttimelimit"
            />
          </el-table-column>
          <el-table-column
            align="center"
            label="是否已完成整改"
            v-if="item.name === '是否已完成整改'"
            prop="state"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.conclusion }}
              <!-- {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '需调整'
                  : row.status == 3
                  ? '已撤销'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : row.status == 8
                  ? '整改完成'
                  : row.status == 9
                  ? '未销号问题'
                  : row.status == 10
                  ? '再次整改'
                  : row.status == 11
                  ? '关闭'
                  : '未整改'
              }} -->
            </template>
          </el-table-column>
        </div>
        <el-table-column width="1"></el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNum"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <practicableForm
      v-if="showPracticableForm"
      ref="practicableForm"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
    />
    <valuationForm
      v-if="showValuationForm"
      ref="valuationForm"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
    />

    <ProcessList ref="process" @fetch-data="fetchData" />
    <WfqdDeal ref="wfqddeal" />

    <!-- 单位组件 -->
    <company-select-modal ref="audiTree" @submit="handleCompanyTreeSelected" />

    <!-- 整改清单预览弹窗 -->
    <RectificationPreviewDialog
      :visible.sync="showPreviewDialog"
      @close="handlePreviewClose"
    />
    
    <!-- 责任清单预览弹窗 -->
    <ResponsibilityPreviewDialog
      :visible.sync="showResponsibilityDialog"
      @close="handleResponsibilityClose"
    />
  </div>
</template>

<script>
  import {
    getMyRectificationLedgerList,
    getIssuesAllDetailInfo,
    exportZGList,
    exportZRList,
    exportWTList,
  } from '@/api/zgzz/index.js'
  import { formatDay } from '@/utils/index'
  import valuationForm from '@/views/audit/rectify/components/form/valuationForm'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import practicableForm from '@/views/audit/rectify/components/form/practicableForm.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import CompanySelectModal from '@/views/oilAudit/jhlx/components/department.vue'
  import { getSJWTTypeDatas } from '@/api/audit/implement'
  import RectificationPreviewDialog from './components/RectificationPreviewDialog.vue'
  import ResponsibilityPreviewDialog from './components/ResponsibilityPreviewDialog.vue'

  export default {
    name: 'Download',
    components: {
      filterSearch,
      filterTable,
      practicableForm,
      ProcessList,
      WfqdDeal,
      valuationForm,
      CompanySelectModal,
      RectificationPreviewDialog,
      ResponsibilityPreviewDialog,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          issuesCode: '',
          issuesName: '',
          pageNum: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '问题名称' },
          { name: '单位名称' },
          { name: '问题来源' },
          { name: '审计报告出具年份' },
          { name: '问题类别' },
          { name: '整改目标清单' },
          { name: '是否已完成整改' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-zglstz-search',
        tableKey: 'audit-rectify-zglstz-list',
        searchMore: true,
        showPracticableForm: false,
        btnLoading: false,
        showValuationForm: false,
        questionTypeList: [],
        showPreviewDialog: false,
        showResponsibilityDialog: false,
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.getQuestionType()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      async getQuestionType() {
        const { data } = await getSJWTTypeDatas()
        this.questionTypeList = data.data
      },
      handleApproval(row) {
        if (!row.implId) {
          this.$baseMessage('未落实，无法提交审批', 'error')
          return
        }
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(97, row.implId)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.implId,
          tableId: 97,
        })
        this.listLoading = false

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          { name: '问题编号', key: 'issuesCode' },
          { name: '问题名称', key: 'issuesName' },
          { name: '一级单位', key: 'oneorgname' },
          { name: '具体责任单位', key: 'auditObjectName' },
          { name: '问题来源', key: 'problemsrc' },
          { name: '审计报告出具年份', key: 'reportyear' },
          { name: '问题类别', key: 'problemtype' },
          { name: '整改类型', key: 'recttype' },
        ]
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
        this.queryForm.pageNum = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          issuesCode: '',
          issuesName: '',
          pageNum: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNum = 1
        this.fetchData()
      },
      async fetchData() {
        this.btnLoading = false
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getMyRectificationLedgerList(this.queryForm)
        this.list = tlist.map((x) => {
          const { issues, reimpl, status, ...other } = x
          return {
            ...other,
            ...issues,
            ...reimpl,
            implId: other.implId,
            rectificationPlan: other.rectificationPlan,
            deadline: other.deadline ? new Date(other.deadline) : '',
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      async practicable(row) {
        this.showPracticableForm = true
        this.$nextTick(async () => {
          if (row.relaId) {
            const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
            if (res && res.data) {
              this.$refs.practicableForm.showEdit('edit', res.data)
            }
          }
        })
      },
      async handleDetail(row) {
        this.showValuationForm = true
        this.$nextTick(async () => {
          const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
          await this.$refs.valuationForm.showEdit('detail', res.data)
        })
      },
      // handleAdd() {
      //   this.$refs['edit'].showEdit()
      // },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      closeDialog() {
        this.showPracticableForm = false
        this.showValuationForm = false
      },
      async handleExport(type) {
        const obj = { ...this.queryForm }
        let data = null
        let filename = ''
        if (type === '问题清单') {
          data = await exportWTList(obj)
          filename = '问题清单.xlsx'
        } else if (type === '责任清单') {
          data = await exportZRList(obj)
          filename = '责任清单.xlsx'
        } else if (type === '整改清单') {
          data = await exportZGList(obj)
          filename = '整改清单.xlsx'
        }

        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      //选择单位
      handleCompanyTreeSelected(val) {
        this.$set(this.queryForm, 'oneorgname', val.label)
        this.$set(this.queryForm, 'oneorgid', val.id)
      },
      // 预览清单
      handlePreviewExport(type) {
        if (type === '整改清单') {
          this.showPreviewDialog = true
        } else if (type === '责任清单') {
          this.showResponsibilityDialog = true
        }
      },
      // 关闭预览弹窗
      handlePreviewClose() {
        this.showPreviewDialog = false
      },
      // 关闭责任清单预览弹窗
      handleResponsibilityClose() {
        this.showResponsibilityDialog = false
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
</style>
