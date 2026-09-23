<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-if="item.name === '报告名称'"
                v-model="queryForm.reportname"
                clearable
                placeholder="报告名称"
              />
              <el-date-picker
                v-if="item.name === '报告时间'"
                align="right"
                end-placeholder="报告结束日期"
                range-separator="至"
                format="yyyy-MM-dd"
                start-placeholder="报告开始日期"
                type="daterange"
                unlink-panels
                v-model="queryForm.Date"
                value-format="yyyy-MM-dd"
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
        <el-button
          type="success"
          @click="handleAdd"
          v-if="hasAuth('JKBGZBJadd')"
        >
          新建
        </el-button>
        <!-- <el-button type="primary" @click="handleAdd">导出</el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <!-- <el-table-column type="selection" width="55" /> -->
        <el-table-column align="center" label="报告名称" prop="reportname">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleDetail(row)"
              v-if="hasAuth('JKBGZBJdetail')"
            >
              {{ row.reportname }}
            </el-button>
            <div v-else>{{ row.reportname }}</div>
          </template>
        </el-table-column>
        <template v-if="!loading">
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              v-if="item.name === '报告时间'"
              align="center"
              label="报告时间"
              prop="reporttime"
              :formatter="formatDay"
              sortable="custom"
            />
            <el-table-column
              v-if="item.name === '报告版本'"
              align="center"
              label="报告版本"
              prop="reporttype"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              v-if="item.name === '报告方式'"
              label="报告方式"
              prop="reportmode"
            />
            <el-table-column
              align="center"
              v-if="item.name === '复核状态'"
              label="复核状态"
              prop="reportstatus"
            >
              <template #default="{ row }">
                {{ statusName[row.reportstatus] || '未审核' }}
              </template>
            </el-table-column>
          </div>
        </template>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="270"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="
                statusName[row.reportstatus] !== '未审批' &&
                statusName[row.reportstatus] !== '复核通过'
              "
              v-if="hasAuth('JKBGZBJedit')"
            >
              修改
            </el-button>
            <el-button
              type="text"
              :disabled="statusName[row.reportstatus] !== '未审批'"
              @click="handleFH(row)"
              v-if="hasAuth('JKBGZBJfh')"
            >
              复核
            </el-button>
            <el-button
              type="text"
              @click="handleZQYJ(row)"
              :disabled="statusName[row.reportstatus] !== '复核通过'"
              v-if="hasAuth('JKBGZBJzqyj')"
            >
              征求意见
            </el-button>
            <el-button
              type="text"
              :disabled="statusName[row.reportstatus] !== '征求意见通过'"
              @click="handleSP(row)"
              v-if="hasAuth('JKBGZBJsp')"
            >
              审批
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="statusName[row.reportstatus] !== '未审批'"
              v-if="hasAuth('JKBGZBJdelete')"
            >
              删除
            </el-button>
            <el-button
              type="text"
              @click="handleExport(row)"
              v-if="hasAuth('JKBGZBJexport')"
            >
              导出
            </el-button>
            <!-- <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>复核</el-dropdown-item>
              <el-dropdown-item>征求意见</el-dropdown-item>
              <el-dropdown-item>审批</el-dropdown-item>
              <el-dropdown-item>发送邮件</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown> -->
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
    <IndexView ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    auditFH,
    auditSP,
    auditYJZJ,
    report,
    reportDel,
    reportDetail,
    reportExport,
  } from '@/api/monitor/monitorReport'
  import { searchTableMixis } from '@/mixis/index'
  import { formatDay } from '@/utils/index'
  import IndexView from '@/views/monitor/monitorReport/components/IndexView'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: { IndexView, filterSearch, filterTable },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        statusName: [
          '未审批',
          '审批中',
          '需调整',
          '已通过',
          '已终止',
          '复核中',
          '复核调整',
          '征求意见',
          '征求意见调整',
          '复核通过',
          '复核终止',
          '征求意见通过',
          '征求意见终止',
        ],
        queryForm: {
          reportname: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '报告时间' },
          { name: '报告版本' },
          { name: '报告方式' },
          { name: '复核状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-report-index-search',
        tableKey: 'audit-report-index-list',
        searchMore: true,
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
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
      getFiled() {
        return [
          { name: '报告名称', key: 'reportname' },
          { name: '报告时间', key: 'Date' },
        ]
      },

      formatDay(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          reportname: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
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
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await report({ ...other, startDate, endDate,sortFields: this.sortFields,
          sortFlag: this.sortFlag })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        const data = await reportDetail({ reportid: row.reportid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      async handleEdit(row) {
        const data = await reportDetail({ reportid: row.reportid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await reportDel({
            reportid: row.reportid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      async handleExport(row) {
        const data = await reportExport({ reportid: row.reportid })
        let fileName = row.reportname
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
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
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleFH(row) {
        auditFH({
          reportid: row.reportid,
        }).then((res) => {
          if (res.msg === '成功') {
            this.fetchData()
          }
        })
      },
      handleSP(row) {
        auditSP({
          reportid: row.reportid,
        }).then((res) => {
          if (res.msg === '成功') {
            this.fetchData()
          }
        })
      },
      handleZQYJ(row) {
        auditYJZJ({
          reportid: row.reportid,
        }).then((res) => {
          if (res.msg === '成功') {
            this.fetchData()
          }
        })
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
