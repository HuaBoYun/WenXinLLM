<template>
  <!-- 风险监测指标填报 -->
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-select
                v-model="queryForm.quartername"
                clearable
                placeholder="请选择季度"
                v-if="item.name === '季度'"
              >
                <el-option
                  v-for="option in quarterOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
              <el-date-picker
                v-if="item.name === '年度'"
                v-model="queryForm.riskyear"
                type="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="年度"
              ></el-date-picker>
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
              <el-button @click="resetSearch()" type="primary">重置</el-button>
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" ref="multipleTable">
        <el-table-column align="center" label="季度" prop="quartername">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleDetail(row)"
              style="white-space: pre-line; line-height: 16px"
            >
              {{ row.quartername }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="年度"
            prop="riskyear"
            v-if="item.name === '年度'"
          >
            <template #default="{ row }">
              {{ row.riskyear || '-' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="备注"
            prop="notes"
            v-if="item.name === '备注'"
            show-overflow-tooltip
          ></el-table-column>

          <el-table-column
            v-if="item.name === '审批状态'"
            align="center"
            label="审批状态"
            prop="status"
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
          <el-table-column
            v-if="item.name === '填报单位'"
            align="center"
            label="填报单位"
            prop="linkOrgName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '填报人员'"
            align="center"
            label="填报人员"
            prop="createname"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            v-if="item.name === '填报日期'"
            align="center"
            label="填报日期"
            prop="createtime"
            show-overflow-tooltip
            :formatter="formatDate"
          ></el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleFill(row)"
              :disabled="row.status"
            >
              填报
            </el-button>
            <el-dropdown>
              <el-button type="text" style="margin-left: 10px">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!+row.status"
                    @click="handleProcess(row)"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!!+row.status"
                    @click="handleSubmitApproval(row)"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!!+row.status"
                    @click="handleDelete(row)"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
                <!-- <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="row.status == 6 || row.reportstatus == 2"
                    @click="handleMonitoringCancel(row)"
                  >
                    报废
                  </el-button>
                </el-dropdown-item> -->
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
    <WfqdDeal ref="wfqddeal" />
    <IndicatorReport ref="IndicatorReport" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { formatDay } from '@/utils'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import IndicatorReport from './components/IndicatorReport.vue'
  import store from '@/store'
  import { baseURL } from '@/config'
  import {
    getList,
    deleteItem,
    monitoringCancel,
  } from '@/api/risk/monitoringFill'

  export default {
    name: 'RiskIndicatorReporting',
    components: {
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      IndicatorReport,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          quartername: '',
          pageNumber: 1,
          pageSize: 20,
          riskyear: '',
        },
        filedAll: [
          { name: '年度' },
          { name: '备注' },
          { name: '审批状态' },
          { name: '填报单位' },
          { name: '填报人员' },
          { name: '填报日期' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-indicator-reporting-search',
        tableKey: 'risk-indicator-reporting-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
        quarterOptions: [
          { value: '一季度', label: '一季度' },
          { value: '二季度', label: '二季度' },
          { value: '三季度', label: '三季度' },
          { value: '四季度', label: '四季度' },
        ],
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
      // 格式化日期，只显示年月日
      formatDate(row, column, cellValue) {
        if (!cellValue) return ''
        // 如果是完整的日期时间格式，提取年月日部分
        if (typeof cellValue === 'string' && cellValue.includes(' ')) {
          return cellValue.split(' ')[0]
        }
        return cellValue
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '季度', key: 'quartername' },
          { name: '年度', key: 'riskyear' },
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
        try {
          const {
            data: { pageInfo },
          } = await getList(this.queryForm)
          console.log('🚀 ~ fetchData ~ pageInfo:', pageInfo)
          this.list = pageInfo.tlist || []
          this.total = pageInfo.totalRecord || 0
        } catch (error) {
          console.error('获取数据失败:', error)
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
      },
      handleFill(row) {
        this.$refs['IndicatorReport'].showEdit(row, 'fill')
      },
      handleDetail(row) {
        this.$refs['IndicatorReport'].showEdit(row, 'detail')
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        const { pageSize } = this.queryForm
        this.queryForm = {
          quartername: '',
          pageNumber: 1,
          pageSize,
          riskyear: '',
        }
        this.fetchData()
      },

      handleProcess(row) {
        getFlowPkInfo({
          formId: row.id,
          tableId: 219,
        })
          .then((res) => {
            this.$refs.wfqddeal.show(res.data, false)
          })
          .catch((error) => {
            console.error(error)
          })
      },
      handleSubmitApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(219, row.id)
        } catch (error) {
          console.error(error)
        } finally {
          this.btnLoading = false
        }
      },

      handleDelete(row) {
        this.$confirm(
          '确认要删除该风险监测指标填报数据吗？删除后无法恢复！',
          '警告',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        )
          .then(() => {
            deleteItem({ id: row.id })
              .then((response) => {
                const { code, msg } = response
                if (code === 1) {
                  this.$message.success('删除成功！')
                  this.fetchData() // 刷新列表
                } else {
                  this.$message.error(msg || '删除失败')
                }
              })
              .catch((error) => {
                console.error('删除失败:', error)
                this.$message.error('删除失败，请重试')
              })
          })
          .catch(() => {
            // 用户取消
          })
      },
      handleMonitoringCancel(row) {
        this.$confirm(
          '确认要作废该风险监测指标填报数据吗？作废后无法恢复！',
          '警告',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        )
          .then(() => {
            monitoringCancel({ id: row.id })
              .then((response) => {
                const { code, msg } = response
                if (code == 1) {
                  this.$message.success('作废成功！')
                  this.fetchData()
                } else {
                  this.$message.error(msg || '作废失败')
                }
              })
              .catch((error) => {
                console.error('作废失败:', error)
                this.$message.error('作废失败，请重试')
              })
          })
          .catch(() => {
            // 用户取消
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
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }

  .info-section {
    .info-label {
      font-weight: bold;
      color: #606266;
      margin-right: 8px;
    }
    .info-value {
      color: #303133;
      font-weight: 500;
    }
  }
</style>
