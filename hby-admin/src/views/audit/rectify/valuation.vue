<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-left-panel>
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

              <!-- <el-select
                v-if="item.name === '方案类别'"
                v-model="queryForm.planType"
                placeholder="请选择方案类别"
                clearable
              >
                <el-option label="审计" value="1" />
                <el-option label="内控" value="2" />
                <el-option label="外部" value="3" />
              </el-select>

              <el-select
                v-if="item.name === '状态'"
                v-model="queryForm.status"
                placeholder="请选择状态"
              >
                <el-option label="未审批" value="0" />
                <el-option label="审批中" value="1" />
                <el-option label="已退回" value="2" />
                <el-option label="已撤销" value="3" />
                <el-option label="未下发" value="6" />
                <el-option label="未分派" value="7" />
                <el-option label="未启动" value="8" />
                <el-option label="开始整改" value="9" />
                <el-option label="整改完成" value="10" />
              </el-select>

              <el-date-picker
                v-if="item.name === '创建日期'"
                v-model="queryForm.Date"
                clearable
                end-placeholder="创建结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="创建开始日期"
                :style="{ width: '100%' }"
                type="daterange"
                value-format="yyyy-MM-dd"
              /> -->
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
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="问题编号"
          prop="planCode"
          width="100"
        >
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
            align="center"
            label="整改通知"
            prop="rectificationPlan"
            v-if="item.name === '整改通知'"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '预计完成时间'"
            align="center"
            label="预计完成时间"
            prop="deadline"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            v-if="item.name === '状态'"
            label="状态"
            prop="status"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
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
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作" width="160">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="evaluate(row)"
              :disabled="!!row.status"
            >
              评价
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!row.status"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!row.status || !row.coludSP || btnLoading"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
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

    <!-- <valuationTable v-if="showValuationForm" ref="valuationTable" @fetch-data="fetchData" @closeDialog="closeDialog" /> -->
    <valuationForm
      v-if="showValuationForm"
      ref="valuationForm"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
    />
    <SchemeInfo
      v-if="showSchemeInfo"
      ref="SchemeInfo"
      @closeDialog="closeDialog"
      @fetch-data="fetchData"
    />
    <ProcessList ref="process" @fetch-data="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    getRectificationValuation,
    completeRectificationEval,
    getZgzzeEvaluationDetail,
    getIssuesAllDetailInfo,
  } from '@/api/zgzz/index.js'
  import { formatDay } from '@/utils/index'
  import valuationTable from './components/table/valuationTable.vue'
  import SchemeInfo from '@/views/audit/rectify/components/SchemeInfo.vue'
  import valuationForm from '@/views/audit/rectify/components/form/valuationForm'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'

  export default {
    name: 'Valuation',
    components: {
      filterSearch,
      filterTable,
      valuationForm,
      SchemeInfo,
      ProcessList,
      WfqdDeal,
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
          createStaffName: '',
          createStaff: '',
          status: '',
          Date: '',
          createTimeStart: '',
          createTimeEnd: '',
          planType: '',
          pageNumber: 1,
          pageSize: 20,
        },

        filedAll: [
          { name: '问题名称' },
          { name: '整改通知' },
          { name: '预计完成时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-valuation-search',
        tableKey: 'audit-rectify-valuation-list',
        searchMore: true,
        showValuationForm: false,
        showSchemeInfo: false,
        btnLoading: false,
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
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
      handleApproval(row) {
        if (!row.evalId) return this.$baseMessage('未评价，无法提交！', 'error')
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(98, row.evalId)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.evalId,
          tableId: 98,
        })
        this.listLoading = false

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          { name: '问题编号', key: 'issuesCode' },
          { name: '问题名称', key: 'issuesName' },
          // { name: '方案类别', key: 'planType' },
          // { name: '创建人', key: 'createStaffName' },
          // { name: '状态', key: 'status' },
          // { name: '创建日期', key: 'Date' },
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
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          issuesCode: '',
          issuesName: '',
          createStaff: '',
          status: '',
          Date: '',
          createTimeStart: '',
          createTimeEnd: '',
          planType: '',
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
        obj.createTimeStart = obj.Date[0]
        obj.createTimeEnd = obj.Date[1]
        delete obj.Date
        const {
          data: { tlist, totalRecord },
        } = await getRectificationValuation(obj)
        this.list = tlist.map((x) => {
          const { issues, valua = {}, ...other } = x
          if (valua) {
            return {
              ...issues,
              ...other,
              status: valua.status,
              evalId: valua.evalId,
              coludSP: true,
            }
          } else {
            return {
              ...issues,
              ...other,
              status: 0,
              coludSP: false,
            }
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      async evaluate(row) {
        this.showValuationForm = true
        this.$nextTick(async () => {
          let res = null
          console.log('row.valua', row.valua)
          if (row.valua && row.valua.evalId) {
            res = await getZgzzeEvaluationDetail({ evalId: row.valua.evalId })
          } else {
            res = await getIssuesAllDetailInfo({ relaId: row.relaId })
          }
          if (res && res.data) {
            this.$refs.valuationForm.showEdit('edit', res.data)
          }
        })
      },
      async complete(row) {
        this.$baseConfirm('你确定整改完成当前项吗', null, async () => {
          const { msg } = await completeRectificationEval({
            planId: row.planId,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        })
      },
      async handleDetail(row) {
        this.showValuationForm = true
        this.$nextTick(async () => {
          const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
          await this.$refs.valuationForm.showEdit('detail', res.data)
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
      closeDialog() {
        this.showValuationForm = false
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
