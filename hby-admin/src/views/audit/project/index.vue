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
                v-if="item.name === '项目名称'"
                v-model="queryForm.prjoectName"
                clearable
                placeholder="项目名称"
              />
              <el-row v-if="item.name === '项目经理'">
                <el-input
                  v-model="queryForm.pmId"
                  clearable
                  placeholder="项目经理"
                  :style="{ width: '256px' }"
                  disabled
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  @click="showGroupLeader"
                >
                  选择
                </el-button>
              </el-row>
              <el-input
                v-if="item.name === '项目来源'"
                v-model="queryForm.projectSource"
                clearable
                placeholder="项目来源"
              />
              <el-select
                v-if="item.name === '项目目前状态'"
                v-model="queryForm.status"
                clearable
                filterable
                placeholder="项目目前状态"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in statusArr"
                  :key="item.key"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-date-picker
                v-if="item.name === '项目时间'"
                clearable
                v-model="queryForm.Date"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
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
            <el-form-item style="cursor: pointer">
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
      <vab-query-form>
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
          <el-button type="success" @click="handleEdit(false, false)">
            新建
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="项目编号"
          prop="projectCode"
          width="180"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, true)"
              :style="`color:${
                row.projectId == currProjectId ? '#10d06d' : ''
              }`"
            >
              {{ row.projectCode }}
            </el-button>
          </template>
        </el-table-column>

        <template v-if="!loading">
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              v-if="item.name === '项目名称'"
              align="center"
              label="项目名称"
              prop="prjoectName"
            />
            <el-table-column
              v-if="item.name === '项目来源'"
              align="center"
              label="项目来源"
              prop="projectSource"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '项目经理'"
              align="center"
              label="项目经理"
              prop="pmStaff.realname"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '项目目前状态'"
              align="center"
              label="项目目前状态"
              prop="status"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{
                  row.status == '0'
                    ? '未启动'
                    : row.status == '1'
                    ? '启动'
                    : row.status == '2'
                    ? '实施 '
                    : row.status == '3'
                    ? '完成'
                    : '归档'
                }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="item.name === '计划审计时间'"
              align="center"
              label="计划审计时间"
              prop="startDate"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '计划验收时间'"
              align="center"
              label="计划验收时间"
              prop="endDate"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '项目实施期间(天)'"
              align="center"
              label="项目实施期间(天)"
              prop="days"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '批复总投资(经费:万元)'"
              align="center"
              label="批复总投资(经费:万元)"
              prop="costs"
              show-overflow-tooltip
            />
            <!-- <el-table-column
              v-if="item.name === '状态'"
              align="center"
              label="状态"
              prop="status"
              sortable="custom"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ setStatus(row.status) }}
              </template>
            </el-table-column> -->
            <el-table-column
              v-if="item.name === '审批状态'"
              align="center"
              label="审批状态"
              prop="examineType"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{
                  row.examineType == 1
                    ? '审批中'
                    : row.examineType == 2
                    ? '已退回'
                    : row.examineType == 3
                    ? '已撤回'
                    : row.examineType == 4
                    ? '已终止'
                    : row.examineType == 5
                    ? '已跟踪'
                    : row.examineType == 6
                    ? '已完成'
                    : '未审批'
                }}
              </template>
            </el-table-column>
          </div>
        </template>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, false)"
              :disabled="
                !!row.examineType || userInfo.staffid != row.createStaffId
              "
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    @click="handleManage(row)"
                    :disabled="!row.examineType"
                    type="text"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    :disabled="!!row.examineType || btnLoading"
                    type="text"
                    @click="submitApproval(row)"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="row.projectId == currProjectId"
                    @click="implementation(row)"
                  >
                    实施
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="
                      !!row.examineType || userInfo.staffid != row.createStaffId
                    "
                    @click="handleDelete(row)"
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
    <IndexEdit ref="edit" @fetch-data="fetchData" />
    <!-- 选择组长组员子组件 -->
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import {
    projectDel,
    submitProjectApproval,
    xmProjectPlan,
    submitPjAprWC,
  } from '@/api/audit/project'
  import { projectList } from '@/api/audit/rectify'
  import { getContractTypes, getFlowPkInfo } from '@/api/contract/manage'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  export default {
    name: 'Download',
    mixins: [searchTableMixis],

    components: {
      IndexEdit: () => import('./components/IndexEditNew.vue'),
      filterSearch,
      filterTable,
      selectTeam,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        statusArr: [
          { key: '1', label: '启动', value: 1 },
          { key: '0', label: '未启动', value: 0 },
        ],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          Date: [],
          pmId: '',
        },
        currProjectId: '',

        /*  */
        typeOptions: [],
        loading: false,
        search: {
          pageSize: 10,
          pageNum: 1,
          tagStatus: '',
        },
        filedAll: [
          { name: '项目名称' },
          { name: '项目来源' },
          { name: '项目经理' },
          { name: '项目目前状态' },
          { name: '计划审计时间' },
          { name: '计划验收时间' },
          { name: '项目实施期间(天)' },
          { name: '批复总投资(经费:万元)' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-project-index-search',
        tableKey: 'audit-project-index-list',
        searchMore: true,
        btnLoading: false,
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
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
    methods: {
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.projectId,
          tableId: 8,
        })

        this.$refs.wfqdDeal.show(res.data, false)
      },
      // handleApproval(row) {
      //   this.$refs['process'].save(46, row.planid)
      // },
      setStatus(status) {
        switch (status) {
          // case 0:
          //   return '未启动'
          case 1:
            return '启动'
          case 2:
            return '实施'
          case 3:
            return '完成'
          // case 4:
          //   return '归档'
          default:
            return '未启动'
        }
      },
      getFiled() {
        return [
          { name: '项目名称', key: 'prjoectName' },
          { name: '项目经理', key: 'pmId' },
          { name: '项目来源', key: 'projectSource' },
          { name: '项目目前状态', key: 'status' },
          { name: '项目时间', key: 'Date' },
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
      /*  */

      selectTeamList(val, flagTitle) {
        if (flagTitle) {
          this.leaderId = val[0].staffid
          this.queryForm.pmId = val[0].realname
        } else {
          console.log(val)
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          Date: [],
          pmId: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
        this.btnLoading = false
        this.listLoading = true
        let { Date, pmId, ...other } = this.queryForm
        console.log(this.queryForm)
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        if (pmId) {
          pmId = this.leaderId
        } else {
          pmId = ''
        }

        const {
          data: {
            pageInfo: { tlist, totalRecord },
            currProjectId,
          },
        } = await projectList({
          ...other,
          pmId,
          startDate,
          endDate,
        })
        tlist.forEach((item) => {
          item.endDate = item.endDate
          item.startDate = item.startDate
          item.days = ''
          item.days = this.days(item.startDate, item.endDate)
        })
        this.currProjectId = currProjectId

        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.planNum = tlist[0].projectCode
      },
      days(start, end) {
        let s = new Date(start)
        let e = new Date(end)
        let hours = (e - s) / (1000 * 60 * 60 * 24)
        return hours + '天'
      },
      async implementation(row) {
        let res = await xmProjectPlan({
          projectid: row.projectId,
        })
        if (res.code === 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      async submitApproval(row) {
        // this.listLoading = true
        // let res = await submitPjAprWC({
        //   projectId: row.projectId,
        // })
        // this.listLoading = false
        // this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
        // if (res.code === 1) {
        //   this.fetchData()
        // }
        try {
          this.btnLoading = true
          const tableId = 8
          const fromId = row.projectId
          this.$refs['process'].save(tableId, fromId)
        } catch (error) {
          this.btnLoading = false
        }
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await projectDel({ projectid: row.projectId })
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
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

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>
