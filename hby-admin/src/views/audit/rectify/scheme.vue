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
              v-for="(item, index) in searchItem"
              :prop="item.key"
              :key="index"
            >
              <el-input
                v-if="item.name === '通知编号'"
                v-model="queryForm.planCode"
                clearable
                placeholder="通知编号"
              />

              <el-input
                v-if="item.name === '通知名称'"
                v-model="queryForm.planName"
                clearable
                placeholder="通知名称"
              />

              <el-select
                v-if="item.name === '通知类别'"
                v-model="queryForm.planType"
                placeholder="请选择通知类别"
                clearable
              >
                <el-option label="审计" value="1" />
                <el-option label="内控" value="2" />
                <el-option label="非系统实施" value="3" />
                <el-option label="外部审计" value="4" />
                <el-option label="风险" value="5" />
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
        <el-table-column align="center" label="通知编号" prop="planCode">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.planCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '通知名称'"
            align="center"
            label="通知名称"
            prop="planName"
          />
          <el-table-column
            v-if="item.name === '通知类别'"
            align="center"
            label="通知类别"
            prop="planType"
            #default="{ row }"
          >
            {{
              ['', '审计', '内控', '非系统实施', '外部审计', '风险'][row.planType]
            }}
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '创建人'"
            label="创建人"
            prop="createStaffName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="创建日期"
            v-if="item.name === '创建日期'"
            prop="createTime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            label="截止日期"
            v-if="item.name === '截止日期'"
            prop="deadlineTime"
            show-overflow-tooltip
            :formatter="formatDate"
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
            v-if="item.name === '状态'"
            label="状态"
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
                    :disabled="!!row.status || btnLoading"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="row.zgfs != '0'">
                  <el-button
                    type="text"
                    @click.native="issue(row)"
                    :disabled="row.status != 6 && row.status != 8"
                  >
                    下发
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="start(row)"
                    :disabled="
                      row.zgfs == '0' ? row.status != 6 : row.status != 8
                    "
                  >
                    启动
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    :disabled="row.status > 0 && row.status < 6"
                    type="text"
                    @click.native="shut(row)"
                  >
                    关闭
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleDelete(row)"
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
    <scheme-info
      v-if="showDialog"
      ref="edit"
      @closeDialog="closeDialog"
      @fetch-data="fetchData"
    />
    <executor-options ref="issue" @projectManage="handleIssueSelected" />

    <ProcessList ref="process" @fetch-data="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import { closeSolutionaa } from '@/api/audit/rectify'
  import {
    getRectificationPlanList,
    getRectificationPlanDetail,
    exportRectificationPlanLedger,
    delRectification,
    saveRectificationPlan,
  } from '@/api/zgzz/index.js'
  import { formatDay } from '@/utils/index'
  import ProjectDataTree from '@/views/audit/prepare/components/ProjectDataTree'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import SchemeInfo from './components/SchemeInfo'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import { saveDistribution } from '@/api/setting/system'

  export default {
    name: 'Scheme',
    components: {
      SchemeInfo,
      ProjectDataTree,
      ExecutorOptions,
      filterSearch,
      filterTable,
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
          planCode: '',
          planName: '',
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
          { name: '通知名称' },
          { name: '通知类别' },
          { name: '创建人' },
          { name: '创建日期' },
          { name: '截止日期' },
          { name: '整改责任人' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-scheme-search',
        tableKey: 'audit-rectify-scheme-list',
        searchMore: true,
        showDialog: false,
        currentRow: '',
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
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(95, row.planId)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.planId,
          tableId: 95,
        })
        this.listLoading = false

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          { name: '通知编号', key: 'planCode' },
          { name: '通知名称', key: 'planName' },
          { name: '通知类别', key: 'planType' },
          { name: '创建人', key: 'createStaffName' },
          { name: '状态', key: 'status' },
          { name: '创建日期', key: 'Date' },
        ]
      },
      handleExecutorSelected(node) {
        this.queryForm.createStaffName = node.realname
        this.queryForm.createStaff = node.staffid
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
          planCode: '',
          planName: '',
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.btnLoading = false
        this.listLoading = true
        const obj = { ...this.queryForm }
        obj.createTimeStart = obj.Date[0]
        obj.createTimeEnd = obj.Date[1]
        delete obj.Date
        const {
          data: { tlist, totalRecord },
        } = await getRectificationPlanList({ ...obj, selectType: 1 })
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
          const res = await getRectificationPlanDetail({ planId: row.planId })
          await this.$refs['edit'].showEdit('detail', res.data)
        })
      },
      handleEdit(row) {
        if (row.status != 0) {
          this.$baseMessage('通知整改中,不能修改', 'error')
          return
        }
        this.showDialog = true
        this.$nextTick(async () => {
          const res = await getRectificationPlanDetail({ planId: row.planId })
          await this.$refs['edit'].showEdit('edit', res.data)
        })
      },
      closeDialog() {
        this.showDialog = false
      },
      start(row) {
        console.log(row)
        // 启动
        if (row.zgfs == '0' && row.status == 6) {
          this.$baseConfirm('你确定要启动当前项吗', null, async () => {
            const { msg, code } = await saveRectificationPlan({
              response: row.response,
              planId: row.planId,
              status: 7,
            })
            if (code == 1) {
              this.$baseMessage(msg, 'success')
            } else {
              this.$baseMessage(msg, 'error')
            }
            await this.fetchData()
          })
        } else {
          if (row.status != 8) {
            this.$baseMessage('当前流程未下发！', 'error')
            return
          }
          this.$baseConfirm('你确定要启动当前项吗', null, async () => {
            const { msg, code } = await saveRectificationPlan({
              response: row.response,
              planId: row.planId,
              status: 7,
            })
            if (code == 1) {
              this.$baseMessage(msg, 'success')
            } else {
              this.$baseMessage(msg, 'error')
            }
            await this.fetchData()
          })
        }
      },
      issue(row) {
        // 下发
        if (row.status != 6 && row.status != 8) {
          this.$baseMessage('当前流程未审批完！', 'error')
          return
        }
        this.$refs['issue'].showEdit(null, row.secrectLevelId)
        this.currentRow = row
      },
      async handleIssueSelected(node) {
        // 下发选择人后
        this.$baseConfirm('你确定要下发当前项吗', null, async () => {
          const { msg, code } = await saveRectificationPlan({
            response: node[0].staffid,
            planId: this.currentRow.planId,
            status: 8,
          })
          if (code == 1) {
            await saveDistribution({
              tableId: '97',
              distributionTitle: this.currentRow.planName,
              formId: this.currentRow.planId,
              isread: 0,
              moduleType: 'ZGFA',
              reciver: node[0].staffid,
            })
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          this.currentRow = ''
          await this.fetchData()
        })
      },
      shut(row) {
        if (row.status == 11) {
          this.$baseMessage('通知已关闭', 'error')
          return
        }
        this.$baseConfirm('整改未完成，确认关闭吗？', null, async () => {
          const { msg, code } = await closeSolutionaa({
            planId: row.planId,
            status: 11,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      handleDelete(row) {
        if (row.status != 0) {
          this.$baseMessage('通知整改中', 'error')
          return
        }
        if (row.status == 3) {
          this.$baseMessage('通知已关闭', 'error')
          return
        }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await delRectification({
            planId: row.planId,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
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
