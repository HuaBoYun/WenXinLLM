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
                v-model="queryForm.plannumber"
                clearable
                placeholder="方案编号"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '方案编号'"
              />

              <el-input
                v-model="queryForm.planname"
                clearable
                placeholder="方案名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '方案名称'"
              />

              <el-date-picker
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
              />

              <el-select
                v-model="queryForm.status"
                clearable
                placeholder="请选择状态"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '状态'"
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column type="selection" width="55" /> -->
        <el-table-column align="center" label="方案编号" prop="plannumber">
          <template #default="{ row }">
            <el-button
              style="color: red"
              type="text"
              @click="handleDeatil(row)"
            >
              {{ row.plannumber }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="方案名称"
            prop="planname"
            v-if="item.name === '方案名称'"
          />
          <el-table-column
            align="center"
            label="方案指定部门"
            prop="planmadedep"
            v-if="item.name === '方案指定部门'"
          />
          <el-table-column
            align="center"
            label="被测试部门"
            prop="testedorgs"
            v-if="item.name === '被测试部门'"
            show-overflow-tooltip
            width="320"
          />
          <el-table-column
            align="center"
            label="计划开始时间"
            prop="starttime"
            show-overflow-tooltip
            :formatter="formatDate"
            v-if="item.name === '计划开始时间'"
          />
          <el-table-column
            align="center"
            label="计划结束时间"
            prop="endtime"
            show-overflow-tooltip
            :formatter="formatDate"
            v-if="item.name === '计划结束时间'"
          />
          <!--
          <el-table-column
            align="center"
            label="评价计划编号"
            prop="assessname"
            show-overflow-tooltip
            v-if="item.name === '评价计划编号'"
            #default="{ row }"
          >
            <el-button type="text" @click="handleAssessPlanDetail(row)">
              {{ row.assidtem?.assessid }}
            </el-button>
          </el-table-column> -->

          <el-table-column
            align="center"
            label="状态"
            prop="planstatus"
            show-overflow-tooltip
            v-if="item.name === '状态'"
          ></el-table-column>
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.planstatus !== '未启动' || createId != row.creatid"
            >
              修改
            </el-button>
            <el-dropdown
              style="margin-left: 10px"
              @command="handleCommand"
              :disabled="row.planstatus !== '未启动'"
            >
              <el-button type="text" :disabled="row.planstatus !== '未启动'">
                更多
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button type="text" @click="handleAllocation(row)">
                    分配
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="handleStart(row)">
                    启动
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleConfirm(row)"
                    :disabled="row.issStatus == 1"
                  >
                    下发科室负责人
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="
                      createId != row.creatid ||
                      row.planstatus !== '未启动' ||
                      row.issStatus == 1
                    "
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
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <PlanView ref="edit" @fetch-data="fetchData" />
    <TestListInfo ref="testListInfo" />
    <ApportionView ref="apportionView" @fetch-data="fetchData" />
    <assessPlanDetail ref="assessPlanDetail" />
  </div>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import {
    ctrltestPlanList,
    deletePLan,
    startPlan,
    confirmIssuance,
  } from '@/api/internal/plan'
  import { formatDay } from '@/utils/index'
  // import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ApportionView from '@/views/internal/internalTest/components/options/ApportionView'
  import PlanView from '@/views/internal/internalTest/components/PlanView'
  import TestListInfo from '@/views/internal/internalTest/components/TestListInfo'
  import assessPlanDetail from '@/views/internal/evaluationManagement/components/planView'
  import { xiafaListNew } from '@/oapi/audit/preparation'

  export default {
    name: 'Plan',
    components: {
      PlanView,
      TestListInfo,
      ApportionView,
      filterTable,
      filterSearch,
      assessPlanDetail,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planname: '',
          plannumber: '',
          time: [],
          status: '',
          pageNumber: 1,
          pageSize: 20,
        },
        statusList: [
          {
            label: '未启动',
            value: '未启动',
          },
          {
            label: '已启动',
            value: '已启动',
          },
          {
            label: '执行中',
            value: '执行中',
          },
          {
            label: '已完成',
            value: '已完成',
          },
        ],

        filedAll: [
          { name: '方案名称' },
          { name: '方案指定部门' },
          { name: '被测试部门' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '评价计划编号' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-internalTest-plan-search',
        tableKey: 'internal-internalTest-plan-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '方案编号', key: 'plannumber' },
          { name: '方案名称', key: 'planname' },
          { name: '时间', key: 'time' },
          { name: '状态', key: 'status' },
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
      async fetchData() {
        const { time, ...other } = this.queryForm
        let starttime_max = ''
        let starttime_min = ''
        if (time.length > 0) {
          starttime_min = time[0]
          starttime_max = time[1]
        }
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await ctrltestPlanList({ starttime_max, starttime_min, ...other })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        // if (this.createId != row.creatid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['edit'].showEdit(row)
      },
      handleDeatil(row) {
        // this.$refs['testListInfo'].showEdit(row)
        this.$refs['edit'].showEdit(row, true)
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
            // 调用xiafa方法
            xiafaListNew({
              tableId: '183',
              jsondistribution: row.jsonString,
            }).then((response) => {
              if (response.msg == '成功') {
                this.$baseMessage('下发通知成功', 'success')
                this.fetchData()
              }
            })
          }
        })
      },
      handleDelete(row) {
        // if (this.createId != row.creatid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code, data } = await deletePLan({
            selectProjectid: row.testplanid,
          })
          if (code == 1) {
            this.$baseMessage('删除成功', 'success')
            await this.fetchData()
          }
        })
      },
      handleAssessPlanDetail(row) {
        this.$refs['assessPlanDetail'].showEdit(row, 'detail')
      },

      handleConfirm(row) {
        this.$baseConfirm('你确定要确认下发科室负责人吗', null, async () => {
          // 先调用下发科室接口
          const { msg, code, data } = await confirmIssuance({
            id: row.testplanid,
          })

          if (code == 1) {
            this.$baseMessage('确认下发科室负责人成功', 'success')

            // 再调用xiafa方法，使用数据
            const xiafaData = {
              formId: row.testplanid,
              distributionTitle: row.planname,
              reciver: row.staffid,
              isread: 0,
              moduleType: 'nkhg',
            }

            // 构造xiafa方法所需的参数格式
            const arr = [xiafaData]

            // 调用xiafa方法
            xiafaListNew({
              tableId: '182',
              jsondistribution: JSON.stringify(arr),
            }).then((response) => {
              if (response.msg == '成功') {
                this.$baseMessage('下发通知成功', 'success')
                this.fetchData()
              }
            })
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
