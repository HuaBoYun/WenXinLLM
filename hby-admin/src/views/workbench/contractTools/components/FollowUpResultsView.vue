<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="80%"
    @close="close"
    :close-on-click-modal="false"
    append-to-body
  >
    <div class="system-log-container">
      <vab-query-form>
        <el-card shadow="never">
          <vab-query-form-top-panel :span="24">
            <el-form
              ref="form"
              checkable
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.code"
                  clearable
                  placeholder="计划编号"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.name"
                  clearable
                  placeholder="计划名称"
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
                  type="primary"
                  native-type="submit"
                  @click="fetchData('reset')"
                >
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-top-panel>
        </el-card>
      </vab-query-form>
      <el-card shadow="never" class="secondCard">
        <vab-query-form-right-panel class="option-row">
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
          <el-table-column align="center" label="计划编号" prop="plancode">
            <template #default="{ row }">
              <el-button type="text" @click="handleRead(row)">
                {{ row.plancode }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column width="1" />

          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              align="center"
              label="计划名称"
              prop="planName"
              show-overflow-tooltip
              v-if="item.name === '计划名称'"
            />
            <el-table-column
              align="center"
              label="制定机构"
              prop="organization.memo"
              v-if="item.name === '制定机构'"
            />
            <el-table-column
              align="center"
              label="风险点总数"
              prop="count"
              show-overflow-tooltip
              v-if="item.name === '风险点总数'"
            />
            <el-table-column
              align="center"
              label="很低"
              prop="count1"
              show-overflow-tooltip
              v-if="item.name === '很低'"
            >
              <template #default="{ row }">
                <div
                  class="calCount1"
                  @click="row.count1 > 0 && handlePlanDetail(row, 1)"
                >
                  {{ row.count1 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="较低"
              prop="count2"
              show-overflow-tooltip
              v-if="item.name === '较低'"
            >
              <template #default="{ row }">
                <div
                  class="calCount2"
                  @click="row.count2 > 0 && handlePlanDetail(row, 2)"
                >
                  {{ row.count2 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="中等"
              prop="count3"
              show-overflow-tooltip
              v-if="item.name === '中等'"
            >
              <template #default="{ row }">
                <div
                  class="calCount3"
                  @click="row.count3 > 0 && handlePlanDetail(row, 3)"
                >
                  {{ row.count3 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="较高"
              prop="count4"
              show-overflow-tooltip
              v-if="item.name === '较高'"
            >
              <template #default="{ row }">
                <div
                  class="calCount4"
                  @click="row.count4 > 0 && handlePlanDetail(row, 4)"
                >
                  {{ row.count4 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="很高"
              prop="count5"
              show-overflow-tooltip
              v-if="item.name === '很高'"
            >
              <template #default="{ row }">
                <div
                  class="calCount5"
                  @click="row.count5 > 0 && handlePlanDetail(row, 5)"
                >
                  {{ row.count5 }}
                </div>
              </template>
            </el-table-column>
          </div>
          <el-table-column align="center" label="操作" width="100">
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit2(row)">热图</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-pagination
        background
        class="pager"
        :current-page="queryForm.pageNo"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
    <HeatMap ref="heatMap" @fetch-data="fetchData" />
    <PlanRead ref="read" />
    <TaskEdit ref="edit" />
  </el-dialog>
</template>

<script>
  import { groupQueryList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import HeatMap from '@/views/risk/assessment/result/components/HeatMap.vue'
  import PlanRead from '@/views/risk/assessment/task/components/PlanRead.vue'
  import TaskEdit from '@/views/risk/assessment/result/components/TaskEdit2.vue'

  export default {
    name: 'TaskList',
    components: {
      TaskEdit,
      HeatMap,
      PlanRead,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
        typeOptions: [
          {
            label: '年度计划',
            value: 1,
          },
          {
            label: '临时性计划',
            value: 2,
          },
        ],
        statusOptions: [
          {
            label: '未评估',
            value: 1,
          },
          {
            label: '评估中',
            value: 2,
          },
          {
            label: '已评估',
            value: 3,
          },
        ],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-assessment-result-search',
        tableKey: 'risk-assessment-result-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '计划名称' },
          { name: '制定机构' },
          { name: '风险点总数' },
          { name: '很低' },
          { name: '较低' },
          { name: '中等' },
          { name: '较高' },
          { name: '很高' },
        ], //所有表格项
        filedNow: [],
        dialogFormVisible: false,
        title: '',
        groupId: '',
      }
    },
    created() {},
    methods: {
      show(row) {
        this.dialogFormVisible = true
        this.searchNow = this.getFiled()
        this.searchItem = this.searchNow.slice(0, 4)
        this.initSearch()
        this.initTable()
        this.groupId = row.id
        this.title = '查看'
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '计划编号', key: 'code' },
          { name: '计划名称', key: 'name' },
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
                this.queryForm[x.key] = ''
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
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      /**
       * @description: 打开热图
       * @return {*}
       */
      handleEdit2(row) {
        this.$refs['heatMap'].showHeatMap(row)
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = true

        if (type && type == 'reset') {
          this.queryForm.code = ''
          this.queryForm.name = ''
          this.queryForm.pageNo = 1
          this.queryForm.pageSize = 20
        }
        let query = {
          ...this.queryForm,
          groupId: this.groupId,
        }
        const {
          data: {
            pageBean: { list, total },
          },
        } = await groupQueryList(query)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handlePlanDetail(row, level) {
        if (row.assplanid) {
          this.$refs['edit'].showEdit(row, 1, level)
        }
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
      /**
       * @description: 删除
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
    },
  }
</script>
<style scoped lang="scss">
  .calCount1 {
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #52ffb7;
    }
  }
  .calCount2 {
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #33d73b;
    }
  }
  .calCount3 {
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ffb500;
    }
  }
  .calCount4 {
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ff7f00;
    }
  }
  .calCount5 {
    position: relative;
    &::before {
      content: ' ';
      position: absolute;
      left: 0;
      top: 5px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #e92129;
    }
  }

  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }

  .pager {
    margin-bottom: 20px !important;
  }
</style>
