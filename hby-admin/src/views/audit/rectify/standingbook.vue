<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-if="item.name === '整改通知编号'"
                v-model="queryForm.planCode"
                clearable
                placeholder="整改通知编号"
              />
              <el-input
                v-model="queryForm.projectName"
                v-if="item.name === '项目名称'"
                clearable
                placeholder="项目名称"
              />
              <el-select
                v-if="item.name === '通知类别'"
                v-model="queryForm.planType"
                placeholder="通知类别"
              >
                <el-option label="审计" value="1" />
                <el-option label="内控" value="2" />
                <el-option label="非系统实施" value="3" />
                <el-option label="外部审计" value="4" />
              </el-select>
              <el-select
                v-if="item.name === '状态'"
                v-model="queryForm.status"
                placeholder="状态"
              >
                <el-option label="未审批" value="0" />
                <el-option label="审批中" value="1" />
                <el-option label="已退回" value="2" />
                <el-option label="已撤销" value="3" />
                <el-option label="未启动" value="6" />
                <el-option label="未下发" value="7" />
                <el-option label="未分派" value="8" />
                <el-option label="开始整改" value="9" />
                <el-option label="整改完成" value="10" />
                <el-option label="关闭" value="11" />
              </el-select>

              <el-date-picker
                v-if="item.name === '截止日期'"
                v-model="queryForm.Date"
                clearable
                end-placeholder="截止日期结束时间"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="截止日期开始时间"
                :style="{ width: '300px' }"
                type="daterange"
                value-format="yyyy-MM-dd"
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
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="整改通知编号" prop="planCode">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.planCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            v-if="item.name === '整改通知名称'"
            label="整改通知名称"
            prop="planName"
          />
          <el-table-column
            align="center"
            v-if="item.name === '通知类别'"
            label="通知类别"
            prop="planType"
            #default="{ row }"
          >
            {{
              ['审计', '内控', '非系统实施', '外部审计'][
                Number(row.planType) - 1
              ]
            }}
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '项目名称'"
            label="项目名称"
            prop="projectName"
          />
          <el-table-column
            align="center"
            v-if="item.name === '创建人'"
            label="创建人"
            prop="createStaffName"
          />
          <el-table-column
            align="center"
            v-if="item.name === '截止时间'"
            label="截止时间"
            prop="deadlineTime"
            :formatter="formatDate"
          />

          <el-table-column
            align="center"
            label="整改经办人"
            prop="handlerName"
            v-if="item.name === '整改经办人'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="整改责任人"
            v-if="item.name === '整改责任人'"
            prop="zrrRealName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="status"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                {
                  '0': '未审批',
                  '1': '审批中',
                  '2': '已退回',
                  '3': '已撤销',
                  '6': '未下发',
                  '7': '未分派',
                  '8': '未启动',
                  '9': '开始整改',
                  '10': '整改完成',
                  '11': '关闭',
                  '12': '到期未整改',
                }[row.status] || '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column width="1"></el-table-column>
        <!-- <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="complete(row)">整改完成</el-button>
            <el-button type="text" @click="handleQuery(row)">查看</el-button>
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
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <unregisteredProblemForm ref="unregisteredProble" />
    <SchemeInfo
      v-if="showSchemeInfo"
      ref="SchemeInfo"
      @closeDialog="closeDialog"
      @fetch-data="fetchData"
    />
    <practicableTable ref="practicableTable" />
  </div>
</template>

<script>
  import {
    getRectificationPlanLedger,
    exportRectificationPlanLedger,
    getRectificationPlanDetail,
    completeRectificationEval,
  } from '@/api/zgzz/index.js'
  import { formatDate } from '@/utils/index'
  import DepartmentOptions from './components/options/department.vue'
  import unregisteredProblemForm from './components/form/unregisteredProblemForm'
  import SchemeInfo from '@/views/audit/rectify/components/SchemeInfo'
  import practicableTable from '@/views/audit/rectify/components/table/practicableTable'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Standingbook',
    components: {
      DepartmentOptions,
      filterSearch,
      filterTable,
      unregisteredProblemForm,
      SchemeInfo,
      practicableTable,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planCode: '',
          projectName: '',
          Date: '',
          deadlineStart: '',
          deadlineEnd: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '整改通知编号' },
          { name: '整改通知名称' },
          { name: '通知类别' },
          { name: '项目名称' },
          { name: '创建人' },
          { name: '截止时间' },
          { name: '整改经办人' },
          { name: '整改责任人' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-standingbook-search',
        tableKey: 'audit-rectify-standingbook-list',
        searchMore: true,
        showSchemeInfo: false,
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
          { name: '整改通知编号', key: 'planCode' },
          { name: '项目名称', key: 'projectName' },
          { name: '通知类别', key: 'planType' },
          { name: '状态', key: 'status' },
          { name: '截止日期', key: 'Date' },
        ]
      },
      async complete(row) {
        this.$baseConfirm('你确定整改完成当前项吗', null, async () => {
          const { msg, code } = await completeRectificationEval({
            planId: row.planId,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
          this.fetchData()
        })
      },
      handleDepartmentSelected(node) {
        this.queryForm.auditorg = node.id
        this.queryForm.orgname = node.name
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data).split(' ')[0]
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
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          planCode: '',
          projectName: '',
          Date: '',
          deadlineStart: '',
          deadlineEnd: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const obj = { ...this.queryForm }
        obj.deadlineStart = obj.Date[0]
        obj.deadlineEnd = obj.Date[1]
        delete obj.Date
        const {
          data: { tlist, totalRecord },
        } = await getRectificationPlanLedger(obj)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async handleQuery(row) {
        console.log('row', row)
        if (row.planId) {
          const res = await getRectificationPlanDetail({ planId: row.planId })
          await this.$refs.practicableTable.show({
            list:
              res.data.issuesList.map((x) => {
                const { issues, ...other } = x
                return {
                  ...issues,
                  ...other,
                }
              }) || [],
          })
        }
      },
      async handleDetail(row) {
        this.showSchemeInfo = true
        this.$nextTick(async () => {
          const res = await getRectificationPlanDetail({ planId: row.planId })
          res.data.showModels = { reimpl: true, valua: true }
          await this.$refs['SchemeInfo'].showEdit('detail', res.data, 1)
        })
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
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
      async handleExport() {
        const obj = { ...this.queryForm }
        obj.deadlineStart = obj.Date[0]
        obj.deadlineEnd = obj.Date[1]
        delete obj.Date
        const data = await exportRectificationPlanLedger(obj)
        let filename = '整改台账.xlsx'
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
      closeDialog() {
        this.showSchemeInfo = false
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
