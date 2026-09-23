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
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-if="item.name === '整改报告编号'"
                v-model="queryForm.reportcode"
                clearable
                placeholder="整改报告编号"
              />

              <el-input
                v-if="item.name === '整改报告名称'"
                v-model="queryForm.reportname"
                clearable
                placeholder="整改报告名称"
              />

              <el-input
                v-if="item.name === '报告编制人'"
                v-model="queryForm.createStaffName"
                clearable
                placeholder="报告编制人"
                :style="{ width: '256px' }"
                disabled
              />
              <el-button
                v-if="item.name === '报告编制人'"
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.executor.showEdit()"
              >
                选择
              </el-button>

              <el-input
                v-if="item.name === '单位名称'"
                v-model="queryForm.orgName"
                clearable
                placeholder="单位名称"
                :style="{ width: '256px' }"
                disabled
              />
              <el-button
                v-if="item.name === '单位名称'"
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.departmentSelect.showEdit()"
              >
                选择
              </el-button>

              <el-select
                v-if="item.name === '状态'"
                v-model="queryForm.status"
                placeholder="请选择状态"
              >
                <el-option label="未审批" :value="0"></el-option>
                <el-option label="审批中" :value="1"></el-option>
                <el-option label="需调整" :value="2"></el-option>
                <el-option label="已撤回" :value="3"></el-option>
                <el-option label="已完成" :value="6"></el-option>
              </el-select>
              <el-date-picker
                v-if="item.name === '报告编制时间'"
                v-model="queryForm.Date"
                clearable
                end-placeholder="报告结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="报告开始日期"
                :style="{ width: '100%' }"
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
        </vab-query-form-top-panel>
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
        <el-button type="success" @click="handleAdd">新建</el-button>
        <!-- <el-button type="primary" @click="handleAdd">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="整改报告编号" prop="reportcode">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.reportcode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '整改报告名称'"
            align="center"
            label="整改报告名称"
            prop="reportname"
          />
          <el-table-column
            align="center"
            v-if="item.name === '报告编制人'"
            label="报告编制人"
            prop="createStaffName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="报告编制时间"
            v-if="item.name === '报告编制时间'"
            prop="createdate"
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
            <!-- row.status == "0"
                  ? "未评价"
                  : row.status == "1"
                  ? "审批中"
                  : row.status == "2"
                  ? "已退回"
                  : row.status == "3"
                  ? "已撤销"
                  : row.status == "6"
                  ? "已完成"
                  : "未分派" -->
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

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.status != 0"
            >
              修改
            </el-button>
            <!-- <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
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
                    :disabled="!!row.status"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <!-- <el-dropdown-item v-if="row.status == 6">
                  <el-button type="text" @click="handleAddFile(row)">
                    上传会议文件
                  </el-button>
                </el-dropdown-item> -->
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!row.status"
                  >
                    删除
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
    <ReportInfo
      v-if="showDialog"
      ref="edit"
      @closeDialog="closeDialog"
      @fetch-data="fetchData"
    />
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <department-select
      ref="departmentSelect"
      @submit="handleDepartmentSelected"
    />
    <!-- <executor-options ref="issue" @selected="handleIssueSelected" /> -->

    <FileModal ref="file" @fetch-data="fetchData" />

    <ProcessList ref="process" @fetch-data="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    getReportList,
    getReportDetail,
    delReport,
  } from '@/api/zgzz/index.js'
  import { formatDay } from '@/utils/index'
  import ProjectDataTree from '@/views/audit/prepare/components/ProjectDataTree'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import DepartmentSelect from '@/components/departmentSelect.vue'
  import ReportInfo from '@/views/audit/rectify/components/ReportInfo'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import FileModal from './components/fileModal.vue'
  import '@/views/audit/rectify/components/AllDataDetail.js'

  export default {
    name: 'report',
    components: {
      ReportInfo,
      ProjectDataTree,
      ExecutorOptions,
      DepartmentSelect,
      filterSearch,
      filterTable,
      FileModal,
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
        issueInfo: null,
        queryForm: {
          reportcode: '',
          reportname: '',
          createStaffName: '',
          createStaff: '',
          orgName: '',
          orgid: '',
          orgLevel: '',
          businessField: '',
          punishmentOrg: '',
          status: '',
          Date: '',
          startDate: '',
          endDate: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '整改报告名称' },
          { name: '报告编制人' },
          { name: '报告编制部门' },
          { name: '报告编制时间' },
          { name: '截止日期' },
          { name: '整改责任人' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-report-search',
        tableKey: 'audit-rectify-report-list',
        searchMore: true,
        showDialog: false,
        currentRow: '',
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
        //提交审批
        this.$refs['process'].save(96, row.reportid)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.reportid,
          tableId: 96,
        })
        this.listLoading = false

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          { name: '整改报告编号', key: 'reportcode' },
          { name: '整改报告名称', key: 'reportname' },
          { name: '报告编制人', key: 'createStaffName' },
          { name: '单位名称', key: 'orgName' },
          { name: '状态', key: 'status' },
          { name: '报告编制时间', key: 'Date' },
        ]
      },
      handleExecutorSelected(node) {
        this.queryForm.createStaffName = node[0].realname
        this.queryForm.createStaff = node[0].staffid
      },
      handleDepartmentSelected(node) {
        this.queryForm.orgName = node.label
        this.queryForm.orgid = node.id
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
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm
        this.queryForm = {
          reportcode: '',
          reportname: '',
          createStaffName: '',
          createStaff: '',
          orgName: '',
          orgid: '',
          orgLevel: '',
          businessField: '',
          punishmentOrg: '',
          status: '',
          Date: '',
          startDate: '',
          endDate: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const obj = { ...this.queryForm }
        obj.startDate = obj.Date[0]
        obj.endDate = obj.Date[1]
        delete obj.Date
        const {
          data: { tlist, totalRecord },
        } = await getReportList({ ...obj })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.showDialog = true
        this.$nextTick(() => {
          this.$refs['edit'].showEdit('add', null)
        })
      },
      handleDetail(row) {
        this.showDialog = true
        this.$nextTick(async () => {
          const res = await getReportDetail({ reportid: row.reportid })
          await this.$refs['edit'].showEdit('detail', res.data)
        })
      },
      handleEdit(row) {
        if (row.status != 0) {
          this.$baseMessage('方案整改中,不能修改', 'error')
          return
        }
        this.showDialog = true
        this.$nextTick(async () => {
          const res = await getReportDetail({ reportid: row.reportid })
          await this.$refs['edit'].showEdit('edit', res.data)
        })
      },
      closeDialog() {
        this.showDialog = false
      },
      // start(row) { // 启动
      //   if (row.status != 6) {
      //     this.$baseMessage("当前流程未审批完！", "error")
      //     return
      //   }
      //   this.$baseConfirm("你确定要启动当前项吗", null, async () => {
      //       const { msg, code } = await saveRectificationPlan({
      //         response: row.response,
      //         planId: row.planId,
      //         status: 7
      //       })
      //       if (code == 1) {
      //         this.$baseMessage(msg, "success")
      //       } else {
      //         this.$baseMessage(msg, "error")
      //       }
      //       await this.fetchData()
      //     })
      // },
      // issue(row) { // 下发
      //   if (row.status != 7) {
      //     this.$baseMessage("当前流程未启动！", "error")
      //     return
      //   }
      //   this.$refs["issue"].show()
      //   this.currentRow = row
      // },
      // async handleIssueSelected(node) { // 下发选择人后
      //   this.$baseConfirm("你确定要下发当前项吗", null, async () => {
      //       const { msg, code } = await saveRectificationPlan({
      //         response: node.staffid,
      //         planId: this.currentRow.planId,
      //         status: 8
      //       })
      //       if (code == 1) {
      //         this.$baseMessage(msg, "success")
      //       } else {
      //         this.$baseMessage(msg, "error")
      //       }
      //       this.currentRow = ''
      //       await this.fetchData()
      //     })
      // },
      // shut(row) {
      //   if (row.status == 3) {
      //     this.$baseMessage("方案已关闭", "error")
      //     return
      //   }
      //   this.$baseConfirm("整改未完成，确认关闭吗？", null, async () => {
      //     const { msg, code } = await closeSolution({
      //       planId: row.planId,
      //     })
      //     if (code == 1) {
      //       this.$baseMessage(msg, "success")
      //     } else {
      //       this.$baseMessage(msg, "error")
      //     }
      //     await this.fetchData()
      //   })
      // },
      handleDelete(row) {
        if (row.status != 0) {
          this.$baseMessage('方案整改中', 'error')
          return
        }
        if (row.status == 3) {
          this.$baseMessage('方案已关闭', 'error')
          return
        }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await delReport({
            reportid: row.reportid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      handleAddFile(row) {
        this.$refs['file'].show(row)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 90%;
  }
</style>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
