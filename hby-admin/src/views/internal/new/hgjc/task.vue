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
                v-model="queryForm.suspectedIssue"
                clearable
                :placeholder="$translateTitle('疑似问题')"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '疑似问题'"
              />

              <el-input
                v-model="queryForm.questionType"
                clearable
                :placeholder="$translateTitle('问题类型')"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '问题类型'"
              />

              <el-select
                v-model="queryForm.isConfirm"
                clearable
                :placeholder="$translateTitle('确认情况')"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '确认情况'"
              >
                <el-option
                  v-for="item2 in statusList"
                  :key="item2.value"
                  :label="item2.label"
                  :value="item2.value"
                />
              </el-select>

              <el-select
                v-model="queryForm.isRectification"
                clearable
                :placeholder="$translateTitle('待整改问题')"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '待整改问题'"
              >
                <el-option
                  v-for="item2 in statusList2"
                  :key="item2.value"
                  :label="item2.label"
                  :value="item2.value"
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
          :label="$translateTitle('疑似问题')"
          prop="suspectedIssue"
        >
          <template #default="{ row }">
            <el-button
              style="color: red"
              type="text"
              @click="handleDeatil(row)"
            >
              {{ row.suspectedIssue }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            :label="$translateTitle('问题类型')"
            prop="questionType"
            v-if="item.name === '问题类型'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('发现时间')"
            prop="discoverTime"
            v-if="item.name === '发现时间'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('业务领域')"
            prop="businessArea"
            v-if="item.name === '业务领域'"
          />
          <el-table-column
            align="center"
            :label="$translateTitle('描述')"
            prop="describe"
            v-if="item.name === '描述'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            :label="$translateTitle('确认情况')"
            prop="isConfirm"
            show-overflow-tooltip
            v-if="item.name === '确认情况'"
          >
            <template #default="{ row }">
              <span>{{ row.isConfirm == 0 ? '未确认' : '确认' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            :label="$translateTitle('待整改问题')"
            prop="isRectification"
            show-overflow-tooltip
            v-if="item.name === '待整改问题'"
          >
            <template #default="{ row }">
              <span>
                {{
                  row.isRectification == 0
                    ? '否'
                    : row.isRectification == '1'
                    ? '是'
                    : ''
                }}
              </span>
            </template>
          </el-table-column>
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
        <el-table-column width="1" />
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
    <TaskView ref="edit" @fetch-data="fetchData" />
    <TestListInfo ref="testListInfo" />
    <ApportionView ref="apportionView" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import {
    impctrltestPlanList,
    deleteImp,
    startPlan,
  } from '@/api/internal/new/plan'
  import { formatDay } from '@/utils/index'
  // import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ApportionView from '@/views/internal/new/hgjc/components/options/ApportionView'
  import TaskView from '@/views/internal/new/hgjc/components/TaskView'
  import TestListInfo from '@/views/internal/new/hgjc/components/TestListInfo'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'

  export default {
    name: 'Plan',
    components: {
      TaskView,
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
          suspectedIssue: '',
          questionType: '',
          isConfirm: '',
          isRectification: '',
          pageNumber: 1,
          pageSize: 20,
        },
        statusList: [
          {
            label: '确认',
            value: '1',
          },
          {
            label: '未确认',
            value: '0',
          },
        ],
        statusList2: [
          {
            label: '是',
            value: '1',
          },
          {
            label: '否',
            value: '0',
          },
        ],
        filedAll: [
          { name: '疑似问题' },
          { name: '问题类型' },
          { name: '发现时间' },
          { name: '业务领域' },
          { name: '描述' },
          { name: '确认情况' },
          { name: '待整改问题' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-internalTest-task-search',
        tableKey: 'internal-internalTest-task-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      handleApproval(row) {
        this.$refs['process'].save(82, row.id)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 82,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '疑似问题', key: 'plannumber' },
          { name: '发现时间', key: 'planname' },
          { name: '问题类型', key: 'planname2' },
          { name: '确认情况', key: 'status' },
          { name: '待整改问题', key: 'status2' },
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
          planname: '',
          plannumber: '',
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
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await impctrltestPlanList({ ...this.queryForm })
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
          const { msg, code, data } = await deleteImp({
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
