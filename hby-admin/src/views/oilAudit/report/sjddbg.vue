<template>
  <!-- 审计督导报告 -->
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
                v-model="queryForm.reportName"
                clearable
                placeholder="报告名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '报告名称'"
              />
              <el-date-picker
                v-model="queryForm.reportTimeRange"
                clearable
                end-placeholder="报告结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="报告开始日期"
                style="width: 300px; margin-right: 20px"
                type="daterange"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间'"
                @change="onchangeReportTime"
              />
              <!-- <el-select
                v-model="queryForm.reportType"
                clearable
                placeholder="请选择报告类型"
                v-if="item.name === '请选择报告类型'"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in reporttypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-select
                v-model="queryForm.reportWay"
                clearable
                placeholder="请选择报告方式"
                v-if="item.name === '请选择报告方式'"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in reportmodeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select> -->
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
      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column align="center" label="报告名称" prop="reportName">
          <template #default="{ row }">
            <el-button
              style="color: red"
              type="text"
              @click="handleDeatil(row)"
            >
              {{ row.reportName }}
            </el-button>
            <!-- <div v-else>{{ row.reportName }}</div> -->
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="报告时间"
            prop="reporttime"
            :formatter="formatDate"
            v-if="item.name === '报告时间'"
            sortable="custom"
          >
            <template #default="{ row }">
              <span>{{ row.reportTime || '' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="报告类型"
            prop="reportType"
            show-overflow-tooltip
            v-if="item.name === '报告类型'"
          />
          <el-table-column
            align="center"
            label="报告方式"
            prop="reportWay"
            show-overflow-tooltip
            v-if="item.name === '报告方式'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '已退回'
                  : row.status == 3
                  ? '已撤回'
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

        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status"
            >
              修改
            </el-button>
            <!-- <el-button type="text" @click="handleDelete(row)">删除</el-button>
            <el-button type="text" @click="handExcel(row)">导出</el-button> -->
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleManage(row)"
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
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!row.status"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="handExcel(row)">
                    导出
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
    <sjddbgView ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import {
    auitSupervisorReportList,
    auitSupervisorReportDeleteFileAttach,
    auitSupervisorReportDelete,
  } from '@/oapi/audit/implement'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { formatDay } from '@/utils/index'
  import sjddbgView from './components/sjddbgView'
  import { hasAuth } from '@/utils'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'

  export default {
    name: 'Download',
    components: {
      sjddbgView,
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
          reportName: '', // 报告名称
          startTime: '',
          endTime: '',
          reportTimeRange: [], // 报告时间范围
          pageNumber: 1,
          pageSize: 20,
        },
        reporttypeOptions: [
          {
            label: '对内报告',
            value: '对内报告',
          },
          {
            label: '对外报告',
            value: '对外报告',
          },
        ],
        reportmodeOptions: [
          {
            label: '定期报告',
            value: '定期报告',
          },
          {
            label: '非定期报告',
            value: '非定期报告',
          },
        ],
        filedAll: [
          { name: '报告时间' },
          { name: '报告类型' },
          { name: '报告方式' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-report-index-search',
        tableKey: 'internal-report-index-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        sortFields: '',
        sortFlag: 'asc',
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
      handleApproval(row) {
        this.$refs['process'].save(155, row.id)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 155,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      onchangeReportTime(val) {
        // 选定时间后
        // this.queryForm.startTime = val[0]
        // this.queryForm.endTime = val[1]
      },
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
          this.sortFlag = 'asc'
        } else if (order === 'descending') {
          this.sortFlag = 'desc'
        } else {
          this.sortFlag = ''
        }
        await this.fetchData()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '报告名称', key: 'name' },
          { name: '时间', key: 'time' },
          // { name: '请选择报告类型', key: 'reportType' },
          // { name: '请选择报告方式', key: 'reportWay' },
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
          reportName: '', // 报告名称
          startTime: '',
          endTime: '',
          reportTimeRange: [], // 报告时间范围
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
        const params = {
          ...this.queryForm,
        }
        if (this.queryForm.reportTimeRange.length) {
          params.startTime = this.queryForm.reportTimeRange[0]
          params.endTime = this.queryForm.reportTimeRange[1]
        }
        delete params.reportTimeRange
        const {
          data: { pageInfo, totalRecord },
          code,
        } = await auitSupervisorReportList(params)
        if (code === 1) {
          this.list = pageInfo.tlist || []
          this.total = totalRecord || 0
          this.listLoading = false
        }
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDeatil(row) {
        this.$refs['edit'].showEdit(row, 'deatil')
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDelete(row) {
        console.log('row', row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await auitSupervisorReportDelete({
            ids: row.id,
          })
          if (code === 1) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }
        })
      },
      async handExcel(row) {
        const data = await expReportFile({
          reportid: row.reportid,
        })
        if (data) {
          let fileName = row.reportName + '.doc'
          let blob = new Blob([data], {
            type: 'application/msword',
          })
          if (window.navigator.msSaveOrOpenBlob) {
            navigator.msSaveBlob(blob, fileName)
          } else {
            let link = document.createElement('a')
            link.href = window.URL.createObjectURL(blob)
            link.download = fileName
            link.click()
            // 释放内存
            window.URL.revokeObjectURL(link.href)
          }
        } else {
          this.$baseMessage(
            row.reportName + '无内容',
            'error',
            'vab-hey-message-error'
          )
        }
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
