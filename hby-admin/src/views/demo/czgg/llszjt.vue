利率设置-集团
<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel style="width: 100%">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="利率编码"
                v-if="item.name === '利率编码'"
              />
              <el-input
                v-model="queryForm.projectOrderName"
                clearable
                placeholder="利率名称"
                v-if="item.name === '利率名称'"
              />
              <el-select
                v-model="queryForm.contracttype"
                filterable
                placeholder="利率类型"
                v-if="item.name === '利率类型'"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
              <el-input
                v-model="queryForm.projectOrderName"
                clearable
                placeholder="年利率%"
                v-if="item.name === '年利率'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
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
            <el-form-item>
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel style="width: 100%">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
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
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="利率编码"
          prop="qdcode"
          width="100"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <!-- <el-table-column
            v-if="item.name === '利率编码'"
            align="center"
            label="利率编码"
            prop="projectName"
          /> -->
          <el-table-column
            v-if="item.name === '利率名称'"
            align="center"
            label="利率名称"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '利率类型'"
            align="center"
            label="利率类型"
            prop="sjlxName"
            show-overflow-tooltip
          >
            <!-- <template #default="{ row }">
              {{ row.projectType == 1 ? '计划内' : '归档' }}
            </template> -->
          </el-table-column>
          <!-- <el-table-column
            v-if="item.name === '计划开始时间'"
            align="center"
            label="计划开始时间"
            prop="planStarttime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '计划结束时间'"
            align="center"
            label="计划结束时间"
            prop="planEndtime"
            show-overflow-tooltip
          /> -->
          <el-table-column
            v-if="item.name === '日利率天数'"
            align="center"
            label="日利率天数"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '利率录入日期'"
            align="center"
            label="利率录入日期"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '起效日期'"
            align="center"
            label="起效日期"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '启用状态'"
            align="center"
            label="启用状态"
            prop="costEstimation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '年利率%'"
            align="center"
            label="年利率%"
          >
            <template #default="{ row }">
              {{
                row.status == 1 ? '启动' : row.status == 2 ? '实施' : '未启动'
              }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '融资利率类型'"
            align="center"
            label="融资利率类型"
          >
            <template #default="{ row }">
              {{
                row.spzt == 1
                  ? '审批中'
                  : row.spzt == 2
                  ? '已退回'
                  : row.spzt == 3
                  ? '已撤回'
                  : row.spzt == 4
                  ? '已终止'
                  : row.spzt == 5
                  ? '已跟踪'
                  : row.spzt == 6
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
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleEdit(scope.row, false)"
              :disabled="!!scope.row.spzt"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="scope.row.id == currProjectId"
                    @click.native="implementation(scope.row)"
                  >
                    实施
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(scope.row)"
                    :disabled="scope.row.spzt != 1 && scope.row.spzt != 6"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <!-- <el-dropdown-item>
                  <el-button type="text" @click.native="handleSend(scope.row)">
                    分工
                  </el-button>
                </el-dropdown-item> -->
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(scope.row)"
                    :disabled="scope.row.spzt == 1 || scope.row.spzt == 6"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    @click="handleDelete(scope.row)"
                    type="text"
                    :disabled="!!scope.row.spzt"
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
    <Views ref="edit" @fetch-data="fetchData"></Views>

    <!-- 选择组长组员子组件 -->
    <!-- <select-team ref="select" @selectTeamList="selectTeamList"></select-team> -->
    <ProcessList ref="process" />
    <WfqdDeal ref="wfqddeal" />
    <!-- 人员 -->
    <project-manage
      @projectManage="getChildlistPro"
      :multiple="false"
      ref="manage"
    />
  </div>
</template>

<script>
  import {
    implementPlanList,
    implementPlanDelete,
    implementPlanCycurr,
    fpzyksry,
  } from '@/oapi/audit/project'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { getFlowPkInfo } from '@/api/setting/system.js'
  import Views from './components/llszEdit.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import projectManage from '@/components/selectPerson'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'llszjt',
    mixins: [searchTableMixis],
    components: {
      ProcessList,
      Views,
      // selectTeam: () => import("./components/formComponents/selectTeam.vue"),
      WfqdDeal,
      filterSearch,
      filterTable,
      projectManage,
    },
    data() {
      return {
        list: [],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectOrderName: undefined,
          projectOrderId: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        },
        currProjectId: '',
        filedAll: [
          { name: '利率编码' },
          { name: '利率名称' },
          { name: '利率类型' },
          { name: '日利率天数' },
          { name: '利率录入日期' },
          { name: '起效日期' },
          { name: '启用状态' },
          { name: '年利率%' },
          { name: '融资利率类型' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'globalTreasurer-czgg-llszjt-search',
        tableKey: 'globalTreasurer-czgg-llszjt-list',
        searchMore: false,
        select: [],
        pickerOptions: {
          shortcuts: [
            {
              text: '最近一周',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
                picker.$emit('pick', [start, end])
              },
            },
            {
              text: '最近一个月',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
                picker.$emit('pick', [start, end])
              },
            },
            {
              text: '最近三个月',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
                picker.$emit('pick', [start, end])
              },
            },
          ],
        },
        typeOptions: [
          {
            label: '活期存款利率',
            value: '1',
          },
          {
            label: '定期存款利率',
            value: '2',
          },
          {
            label: '通知存款利率',
            value: '3',
          },
          {
            label: '融资利率',
            value: '4',
          },
          {
            label: '投资利率',
            value: '5',
          },
        ],
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
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = null
              }
            }
          })
          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      getFiled() {
        return [
          { name: '利率编码', key: 'code' },
          { name: '利率名称', key: 'name' },
          { name: '利率类型', key: 'type' },
          { name: '年利率', key: 'yearLl' },
        ]
      },
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(163, row.id)
        this.listLoading = true
      },
      // 分工
      handleSend(row) {
        this.select = [row]
        this.$refs.manage.showEdit()
      },
      async getChildlistPro(val) {
        console.log(val, '1111111111')
        // const ids = this.select.map((res) => res.gzfaid)
        // const titles = this.select.map((res) => res.xmmc)
        const ids = val.map((res) => res.staffid).join(',')
        const names = val.map((res) => res.realname).join(',')

        const arr = {
          projectid: this.select[0].id,
          zyksryids: ids,
          zyksryrwnames: names,
        }
        console.log(arr)
        fpzyksry(arr).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.select = []
          }
        })
      },
      // 办理
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 163,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
      selectTeamList(val, flagTitle) {
        console.log(val, flagTitle)
        if (flagTitle) {
          this.queryForm.projectOrderName = val[0].realname
          this.queryForm.projectOrderId = val[0].staffid
        } else {
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
        this.queryForm = {
          projectOrderName: undefined,
          projectOrderId: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
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
        this.listLoading = true
        let { ...other } = this.queryForm
        const {
          data: { tlist, totalRecord, currProjectId },
        } = await implementPlanList({
          ...other,
        })
        // tlist.forEach((item) => {
        //   item.planEndtime = item.planEndtime ? item.planEndtime.split('T')[0] : ''
        //   item.planStarttime = item.planStarttime ? item.planStarttime.split('T')[0] : ''
        //   item.days = ''
        //   item.days = this.days(item.planStarttime, item.planEndtime)
        // })
        this.listLoading = false
        return
        this.currProjectId = currProjectId

        this.list = tlist
        this.total = totalRecord
        this.planNum = tlist[0].projectCode
      },
      days(start, end) {
        let s = new Date(start)
        let e = new Date(end)
        let hours = (e - s) / (1000 * 60 * 60 * 24)
        return hours + '天'
      },
      async implementation(row) {
        let res = await implementPlanCycurr({
          projectid: row.id,
        })
        if (res.code == 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      async submitApproval(row) {
        const tableId = 8
        const fromId = row.projectId
        // this.$refs['process'].save(tableId, fromId)
        // let res = await submitProjectApproval({
        //   projectId: row.projectId,
        // })
        // this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
        // if (res.code === 1) {
        //   this.fetchData()
        // }
      },
      handleEdit(row, disabled, type) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum, type)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await implementPlanDelete({ ids: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      color(row) {
        if (row.id == this.currProjectId) {
          return { color: '#7fcf7c' }
        } else {
          return { color: '' }
        }
      },
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
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
