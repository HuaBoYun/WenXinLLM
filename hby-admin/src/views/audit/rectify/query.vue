<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-if="item.name === '问题编号'"
                v-model="queryForm.issuesCode"
                clearable
                placeholder="问题编号"
              />
              <!-- <el-input
                v-if="item.name === '问题名称'"
                v-model="queryForm.issuesName"
                clearable
                placeholder="问题名称"
              /> -->
              <el-input
                v-if="item.name === '问题详情'"
                v-model="queryForm.questionMemo"
                clearable
                placeholder="问题详情"
              />
              <el-select
                v-if="item.name === '方案类别'"
                v-model="queryForm.issuesType"
                placeholder="方案类别"
              >
                <el-option label="审计" value="1" />
                <el-option label="内控" value="2" />
                <el-option label="非系统实施" value="3" />
                <el-option label="外部审计" value="4" />
              </el-select>
              <!-- <el-input
                v-if="item.name === '整改责任人'"
                v-model="queryForm.responsiblePersonName"
                clearable
                placeholder="整改责任人"
              /> -->
              <el-input
                v-if="item.name === '责任部门'"
                v-model="queryForm.responsibleDeptName"
                clearable
                placeholder="责任部门"
              />
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
              <el-button type="primary" @click="resetSearch">重置</el-button>
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
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
      <vab-query-form-right-panel :span="24">
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>

      <el-table
        v-loading="listLoading"
        :data="list"
        :span-method="spanMethod"
        @sort-change="handleSortChange"
        border
      >
        <el-table-column
          align="center"
          label="问题编号"
          prop="issuesCode"
          width="120"
          sortable="custom"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.issuesCode }}
            </el-button>
          </template>
        </el-table-column>

        <!-- <el-table-column
          align="center"
          label="问题名称"
          prop="issuesName"
          show-overflow-tooltip
        /> -->

        <el-table-column
          align="center"
          label="问题详情"
          prop="questionMemo"
          sortable="custom"
          show-overflow-tooltip
        />

        <el-table-column
          align="center"
          label="整改通知"
          prop="rectificationPlan"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="rectificationPlanDetail(row)">
              {{ row.rectificationPlan }}
            </el-button>
          </template>
        </el-table-column>

        <!-- <el-table-column
          align="center"
          label="整改措施"
          prop="rectificationMeasures"
          show-overflow-tooltip
        /> -->
        <el-table-column
          align="center"
          label="成果体现"
          prop="resultMemo"
          show-overflow-tooltip
        />

        <el-table-column
          align="center"
          label="整改时限"
          prop="deadline"
          show-overflow-tooltip
          :formatter="formatDate"
        />

        <el-table-column
          align="center"
          label="整改状态"
          prop="resultStatus"
          show-overflow-tooltip
          #default="{ row }"
        >
          {{
            ['未整改', '已整改未到位', '已整改到位', '关闭'][
              Number(row.resultStatus) - 1
            ] || '未整改'
          }}
        </el-table-column>

        <el-table-column
          align="center"
          label="整改责任人"
          prop="zgzrr"
          show-overflow-tooltip
        />

        <el-table-column
          align="center"
          label="责任部门"
          prop="responsibleDeptName"
          show-overflow-tooltip
        />

        <!-- <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              :disabled="!!(row.status && row.status != 0)"
              @click="handleEdit(row)"
            >
              修改
            </el-button>
            <el-button
              type="text"
              :disabled="!!(row.status && row.status != 0)"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <zgqdEdit
      v-if="showZgqdEdit"
      ref="zgqdEdit"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
    />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <ProcessList ref="process" @fetch-data="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <practicableForm
      ref="practicableForm"
      @closeDialog="closeDialog"
      :routerType="1"
    />
  </div>
</template>

<script>
  import {
    getIssuesDetail,
    delIssues,
    getRectificationIssuesLedgetList,
    exportIssuesLedgetList,
    getIssuesAllDetailInfo,
  } from '@/api/zgzz/index.js'
  import { getContractTypes, getFlowPkInfo } from '@/api/contract/manage'
  import { searchTableMixis } from '@/mixis/index'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import zgqdEdit from '@/views/audit/collect/components/edit'
  import ExecutorOptions from '@/views/audit/implement/components/options/executor.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import '@/views/audit/rectify/components/AllDataDetail.js'
  import practicableForm from '@/views/audit/rectify/components/form/practicableForm.vue'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      zgqdEdit,
      ExecutorOptions,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
      practicableForm,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          issuesType: '',
          issuesName: '',
          issuesCode: '',
          createStaffName: '',
          questionMemo: '',
          responsiblePersonName: '',
          responsibleDeptName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        sortFlag: '',
        sortField: '',
        projectInfo: {},
        typeOptions: [],
        loading: false,
        search: {
          pageSize: 10,
          pageNum: 1,
          tagStatus: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-collect-zgqd-search',
        tableKey: 'audit-collect-zgqd-list',
        searchMore: true,
        projectInfo: {},
        showZgqdEdit: false,
      }
    },
    created() {
      this.fetchData()
      // this.fetchTypes()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleSortChange({ prop, order }) {
        if (order) {
          this.sortField = prop
          this.sortFlag = order === 'ascending' ? 1 : 2
        } else {
          this.sortField = this.sortFlag = ''
        }
        this.fetchData()
      },
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(49, row.issuesId)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.issuesId,
          tableId: 49,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          { name: '问题编号', key: 'issuesCode' },
          // { name: '问题名称', key: 'issuesName' },
          { name: '问题详情', key: 'questionMemo' },
          { name: '方案类别', key: 'issuesType' },
          // { name: '整改责任人', key: 'responsiblePersonName' },
          { name: '责任部门', key: 'responsibleDeptName' },
        ]
      },
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList.reduce((prev, cur) => {
          const data = cur.childrenList.map((item) => {
            return {
              label: item.typename,
              value: item.typeid,
            }
          })
          return prev.concat(data)
        }, [])
      },
      handleExecutorSelected(node) {
        this.queryForm.createStaffName = node.realname
        this.queryForm.staffid = node.staffid
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data).split(' ')[0]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          issuesName: '',
          issuesCode: '',
          createStaffName: '',
          questionMemo: '',
          responsiblePersonName: '',
          responsibleDeptName: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      async resetSearch() {
        await this.resetQueryForm()
        this.fetchData()
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
        const obj = {
          ...this.queryForm,
          sortField: this.sortField,
          sortFlag: this.sortFlag,
        }
        const {
          data: { tlist, totalRecord },
        } = await getRectificationIssuesLedgetList(obj)
        let sign = 0,
          relaObject = {},
          tempList = tlist.map((x, i) => {
            const { relaCount, relaList, ...other } = x
            x.rowspan = 1
            x.colspan = 1
            if (relaCount === 1) {
              sign++
              const reimpl = relaList[0].reimpl || {}
              return {
                zgzrr: relaList[0].responsiblePersonName || '',
                situationoverView: reimpl.situationoverView || '',
                rectificationMeasures: relaList[0].resultMemo || '',
                resultMemo: relaList[0].resultMemo || '',
                achivement: reimpl.achivement || '',
                deadline: relaList[0].deadline,
                relaId: relaList[0].relaId,
                resultStatus: relaList[0].valua
                  ? relaList[0].valua.resultStatus
                  : '',
                ...x,
                rectificationPlan: relaList[0].rectificationPlan || '',
              }
            } else if (relaCount > 1) {
              relaObject[sign] = relaList.map((r, ri) => {
                const reimpl = r.reimpl || {}
                const newRow = {
                  ...other,
                  resultStatus: r.valua ? r.valua.resultStatus : '',
                  situationoverView: reimpl.situationoverView || '',
                  rectificationMeasures: r.resultMemo || '',
                  resultMemo: r.resultMemo || '',
                  zgzrr: r.responsiblePersonName || '',
                  rectificationPlan: r.rectificationPlan || '',
                  achivement: reimpl.achivement || '',
                  relaId: r.relaId,
                  deadline: r.deadline,
                }

                if (ri === 0) {
                  newRow.rowspan = relaCount
                  newRow.colspan = 1
                  return { ...x, ...newRow }
                }

                newRow.rowspan = 0
                newRow.colspan = 0
                return newRow
              })
              sign += relaCount
            } else {
              sign++
            }
            return x
          })

        Object.keys(relaObject).forEach((sign) => {
          const left = tempList.slice(0, Number(sign))
          const right = tempList.slice(Number(sign) + 1)
          tempList = [...left, ...relaObject[sign], ...right]
        })
        this.list = tempList
        this.total = totalRecord
        this.listLoading = false
      },
      async handleExport() {
        const data = await exportIssuesLedgetList(this.queryForm)
        let filename = '整改清单台账.xls'
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
      handleDetail(row) {
        this.showZgqdEdit = true
        this.$nextTick(async () => {
          const res = await getIssuesDetail({ issuesId: row.issuesId })
          this.$refs['zgqdEdit'].showEdit('detail', res.data)
        })
      },
      handleEdit(row) {
        this.showZgqdEdit = true
        this.$nextTick(async () => {
          const res = await getIssuesDetail({ issuesId: row.issuesId })
          this.$refs['zgqdEdit'].showEdit('edit', res.data)
        })
      },
      closeDialog() {
        this.showZgqdEdit = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await delIssues({ issuesId: row.issuesId })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          }
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      spanMethod({ row, column, rowIndex, columnIndex }) {
        if (columnIndex <= 1 || columnIndex >= 7) {
          return [row.rowspan, row.colspan]
        }
      },
      async rectificationPlanDetail(row) {
        console.log('row', row)
        if (row.relaId) {
          const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
          if (res && res.data) {
            this.$refs.practicableForm.showEdit('detail', res.data)
          }
          // this.$showAllDataDetailDialog(res.data)
        }
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
