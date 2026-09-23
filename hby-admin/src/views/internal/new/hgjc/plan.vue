<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.planCode"
                clearable
                :placeholder="$translateTitle('方案编号')"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '方案编号'"
              />

              <el-input
                v-model="queryForm.planName"
                clearable
                :placeholder="$translateTitle('方案名称')"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '方案名称'"
              />

              <!-- <el-date-picker
                v-model="queryForm.time"
                clearable
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                format="yyyy-MM-dd"
                range-separator="-"
                style="width: 200px; margin-right: 10px"
                type="daterange"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间'"
              /> -->

              <el-select
                v-model="queryForm.status"
                clearable
                :placeholder="$translateTitle('方案状态')"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '方案状态'"
              >
                <el-option
                  v-for="item in statusList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                {{ $translateTitle('查询') }}
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                {{ $translateTitle('重置') }}
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
                <span>
                  {{
                    searchMore
                      ? $translateTitle('收起')
                      : $translateTitle('展开')
                  }}
                </span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">
          {{ $translateTitle('新建') }}
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column type="selection" width="55" /> -->
        <el-table-column
          align="center"
          :label="$translateTitle('方案编号')"
          prop="planCode"
        >
          <template #default="{ row }">
            <el-button
              style="color: red"
              type="text"
              @click="handleDeatil(row)"
            >
              {{ row.planCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            :label="$translateTitle('方案名称')"
            prop="planName"
            v-if="item.name === '方案名称'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('配合部门')"
            prop="cooperateDepartmentName"
            v-if="item.name === '配合部门'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('检查公司')"
            prop="inspectCompanyName"
            v-if="item.name === '检查公司'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('检查部门')"
            prop="inspectDepartmentName"
            v-if="item.name === '检查部门'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('方案开始时间')"
            prop="planTimeStart"
            show-overflow-tooltip
            :formatter="formatDate"
            v-if="item.name === '方案开始时间'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('方案结束时间')"
            prop="planTimeEnd"
            show-overflow-tooltip
            :formatter="formatDate"
            v-if="item.name === '方案结束时间'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('创建人')"
            prop="creatorName"
            v-if="item.name === '创建人'"
          ></el-table-column>
          <el-table-column
            align="center"
            :label="$translateTitle('创建时间')"
            prop="createdTime"
            v-if="item.name === '创建时间'"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            :label="$translateTitle('状态')"
            prop="state"
            show-overflow-tooltip
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '需调整'
                  : row.state == 3
                  ? '已撤销'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>

        <el-table-column
          align="center"
          :label="$translateTitle('操作')"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.state"
            >
              {{ $translateTitle('修改') }}
            </el-button>
            <el-dropdown style="margin-left: 10px" @command="handleCommand">
              <el-button type="text">{{ $translateTitle('更多') }}</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!row.state"
                  >
                    {{ $translateTitle('办理') }}
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleApproval(row)"
                  :disabled="!!row.state"
                >
                  {{ $translateTitle('提交审批') }}
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleDelete(row)"
                  :disabled="!!row.state"
                >
                  {{ $translateTitle('删除') }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ProcessList ref="process" @fetchData="fetchData" />
    <PlanView ref="edit" @fetch-data="fetchData" />
    <TestListInfo ref="testListInfo" />
    <ApportionView ref="apportionView" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import {
    ctrltestPlanList,
    deletePLan,
    startPlan,
  } from '@/api/internal/new/plan'
  import { formatDay } from '@/utils/index'
  // import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ApportionView from '@/views/internal/new/hgjc/components/options/ApportionView'
  import PlanView from '@/views/internal/new/hgjc/components/PlanView'
  import TestListInfo from '@/views/internal/new/hgjc/components/TestListInfo'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'

  export default {
    name: 'Plan',
    components: {
      PlanView,
      TestListInfo,
      ApportionView,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planCode: '',
          planName: '',
          time: [],
          status: '',
          pageNumber: 1,
          pageSize: 20,
        },
        statusList: [
          {
            label: '未审批',
            value: '0',
          },
          {
            label: '审批中',
            value: '1',
          },
          {
            label: '需调整',
            value: '2',
          },
          {
            label: '已撤销',
            value: '3',
          },
          // {
          //   label: '已终止',
          //   value: '4',
          // },
          //  {
          //   label: '已跟踪',
          //   value: '5',
          // },
          {
            label: '已完成',
            value: '6',
          },
        ],
        filedAll: [
          { name: '方案名称' },
          { name: '配合部门' },
          { name: '检查公司' },
          { name: '检查部门' },
          { name: '方案开始时间' },
          { name: '方案结束时间' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-internalTest-plan-search',
        tableKey: 'internal-internalTest-plan-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
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
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '方案编号', key: 'plannumber' },
          { name: '方案名称', key: 'planname' },
          { name: '时间', key: 'time' },
          { name: '方案状态', key: 'status' },
        ]
        return fields
      },
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
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },

      // 动态表格开始
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
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
        this.queryForm = {
          planCode: '',
          planName: '',
          time: [],
          status: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      handleApproval(row) {
        this.$refs['process'].save(81, row.id)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 81,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      async fetchData() {
        const { time, ...other } = this.queryForm
        let planYearStart = ''
        let planYearEnd = ''
        let planTimeStartStart = ''
        let planTimeStartEnd = ''
        let planTimeEndStart = ''
        let planTimeEndEnd = ''
        if (time.length > 0) {
          planYearStart = time[0]
          planYearEnd = time[1]
          planTimeStartStart = time[0]
          planTimeEndEnd = time[1]
        }
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
          // planYearStart, planYearEnd,
        } = await ctrltestPlanList({
          planTimeStartStart,
          planTimeEndEnd,
          ...other,
        })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit({}, 'add')
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDeatil(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleCommand(command) {
        // switch (command) {
        //   case 'allocation':
        //     console.log('分配')
        //     break
        //   case 'start':
        //     console.log('启动')
        //     break
        //   case 'delete':
        //   this.handleDelete()
        //     break
        // }
      },
      handleAllocation(row) {
        // console.log('planstatus', row.planstatus)
        if (row.planstatus == '未启动') {
          this.$refs['apportionView'].show(row)
        } else {
          this.$message({
            message: '该方案已启动或已完成',
            type: 'error',
          })
        }
      },
      handleStart(row) {
        this.$baseConfirm('你确定要启动当前项吗', null, async () => {
          const { msg, code, data } = await startPlan({
            selectProjectid: row.testplanid,
          })
          if (code == 1) {
            this.$baseMessage('启动成功', 'success')
            await this.fetchData()
          }
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code, data } = await deletePLan({
            id: row.id,
          })
          if (code == 1) {
            this.$baseMessage('删除成功', 'success')
            await this.fetchData()
          }
        })
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
