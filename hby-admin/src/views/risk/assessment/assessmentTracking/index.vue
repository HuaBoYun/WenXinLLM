<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <DepTree ref="leftlist" @select="handleTreeSelect" />
      </div>
      <div class="right">
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
                <el-form-item
                  v-for="(item, index) in searchItem"
                  :key="index"
                  :prop="item.key"
                >
                  <el-input
                    v-model="queryForm.plancode"
                    clearable
                    placeholder="计划编号"
                    v-if="item.name === '计划编号'"
                  />
                  <el-input
                    v-model="queryForm.planName"
                    clearable
                    placeholder="计划名称"
                    v-if="item.name === '计划名称'"
                  />
                  <el-input
                    v-model="queryForm.orgName"
                    clearable
                    placeholder="制定机构"
                    v-if="item.name === '制定机构'"
                  />
                  <!-- <el-date-picker
                v-model="queryForm.startDate"
                placeholder="计划日期"
                value-format="yyyy-MM-dd"
                format="yyyy-MM-dd"
                type="date"
                v-if="item.name === '计划开始日期'"
              /> -->
                  <el-date-picker
                    v-model="queryForm.Date"
                    type="daterange"
                    align="right"
                    unlink-panels
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :picker-options="pickerOptions"
                    v-if="item.name === '计划日期'"
                  ></el-date-picker>

                  <!-- <el-date-picker
                v-model="queryForm.time"
                end-placeholder="结束日期"
                range-separator="至"
                start-placeholder="开始日期"
                value-format="yyyy-MM-dd"
                format="yyyy-MM-dd"
                type="daterange"
                v-if="item.name === '日期'"
              /> -->
                  <el-select
                    v-model="queryForm.planType"
                    clearable
                    placeholder="计划类型"
                    v-if="item.name === '计划类型'"
                  >
                    <el-option
                      v-for="item in typeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                  <el-select
                    v-model="queryForm.planStatus"
                    clearable
                    placeholder="计划状态"
                    v-if="item.name === '计划状态'"
                  >
                    <el-option
                      v-for="item in statusOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                  <el-select
                    v-model="queryForm.aprstatus"
                    clearable
                    placeholder="请选择审批状态"
                    style="width: 140px; margin-right: 20px"
                    v-if="item.name === '审批状态'"
                  >
                    <el-option
                      v-for="item in aprstatusList"
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
                    @click="queryData"
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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
                <!-- <i class="el-icon-delete" slot="reference"></i> -->
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
              label="计划编号"
              prop="plancode"
              width="100"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row, 'detail')">
                  {{ row.plancode }}
                </el-button>
              </template>
            </el-table-column>

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
                prop="orgName"
                v-if="item.name === '制定机构'"
              />
              <el-table-column
                align="center"
                label="部门"
                prop="deptName"
                v-if="item.name === '部门'"
              />
              <el-table-column
                align="center"
                label="计划开始时间"
                prop="startDate"
                v-if="item.name === '计划开始时间'"
              />
              <el-table-column
                align="center"
                label="计划结束时间"
                prop="endDate"
                v-if="item.name === '计划结束时间'"
              />
              <el-table-column
                align="center"
                label="计划状态"
                prop="data"
                v-if="item.name === '计划状态'"
              >
                <template #default="{ row }">
                  {{ planStatus(row.planStatus) }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="审批状态"
                prop="aprstatus"
                v-if="item.name === '审批状态'"
              >
                <template #default="{ row }">
                  {{
                    row.aprstatus == 1
                      ? '审批中'
                      : row.aprstatus == 2
                      ? '需调整'
                      : row.aprstatus == 3
                      ? '已撤销'
                      : row.aprstatus == 4
                      ? '已终止'
                      : row.aprstatus == 5
                      ? '已跟踪'
                      : row.aprstatus == 6
                      ? '已完成'
                      : '未审批'
                  }}
                </template>
              </el-table-column>
            </div>
            <el-table-column align="center" label="操作">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click.native="handleManage(row)"
                  :disabled="!row.aprstatus"
                >
                  办理
                </el-button>
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
    </div>
    <Edit ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />

    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import { riplanTrackList, riskPlanSituation } from '@/api/systemLog'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { formatDate } from '@/utils'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import DepTree from '@/views/risk/GroupRiskDatabase/companyDep.vue'

  export default {
    name: 'PlanList',
    components: {
      Edit: () => import('./components/Edit.vue'),
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
      DepTree,
    },
    data() {
      return {
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
        list: [],
        dialogFormVisible: false,
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          aprstatus: '',
          pageNo: 1,
          pageSize: 20,
          plancode: undefined,
          planName: undefined,
          startDate: undefined,
          endDate: undefined,
          planType: undefined,
          planStatus: undefined,
          Date: undefined,
          orgid: undefined,
        },
        aprstatusList: [
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
          {
            label: '已完成',
            value: '6',
          },
          {
            label: '未审批',
            value: '0',
          },
        ],
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
            label: '未开始',
            value: 1,
          },
          {
            label: '评估中',
            value: 2,
          },
          {
            label: '已完成',
            value: 3,
          },
        ],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-assessment-plan-search',
        tableKey: 'risk-assessment-plan-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '计划名称' },
          { name: '制定机构' },
          { name: '部门' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '计划状态' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    /**
     * @description: 流程提交 回调
     * @return {*}
     */
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      /**
       * @description: 提交审批
       * @return {*}
       */
      handleApproval(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        try {
          this.btnLoading = true
          //提交审批
          const situation = {
            id: row.assplanid,
          }
          riskPlanSituation(situation).then((res) => {
            console.log('res', res)
            if (res.msg == '成功') {
              this.$refs['process'].save(91, row.assplanid)
            } else {
              this.$message.error(res.msg)
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      /**
       * @description: 办理
       * @return {*}
       */
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.assplanid,
          tableId: 91,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '计划编号', key: 'plancode' },
          { name: '计划名称', key: 'planName' },
          { name: '制定机构', key: 'orgName' },
          { name: '计划日期', key: 'startDate' },
          { name: '计划类型', key: 'planType' },
          { name: '计划状态', key: 'planStatus' },
          { name: '审批状态', key: 'aprstatus' },
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
       * @description: 数字转文字，字典
       * @return {*}
       */
      planStatus(v) {
        const arr = ['未开始', '评估中', '已完成']
        return arr[v - 1]
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
       * @description: 分页 初始化
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
        this.btnLoading = false
        if (type && type == 'reset') {
          this.queryForm.Date = []
          this.$refs['form'].resetFields()
        }
        this.listLoading = true
        const { Date, ...other } = this.queryForm
        if (Date && Date.length) {
          other.startDate = formatDate(Date[0])
          other.endDate = formatDate(Date[1])
        }
        const {
          data: { pageBean },
        } = await riplanTrackList(other)

        this.list = pageBean.list
        this.total = pageBean.total
        this.listLoading = false
      },
      /**
       * @description: 打开编辑
       * @return {*}
       */
      handleEdit(row, type) {
        this.$refs['edit'].showEdit(row, type)
      },
      handleTreeSelect(org) {
        console.log(org.id)
        this.queryForm.orgid = org.id
        this.queryForm.pageNo = 1
        this.fetchData()
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 300px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .lr-layout > .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
