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
                v-model="queryForm.reportname"
                clearable
                placeholder="日志名称"
                v-if="item.name === '日志名称'"
              />
              <el-select
                v-model="queryForm.reporttype"
                placeholder="报告类型"
                v-if="item.name === '报告类型'"
              >
                <el-option label="日报" value="日报" />
                <el-option label="周报" value="周报" />
                <el-option label="总结报告" value="总结报告" />
              </el-select>
              <el-date-picker
                v-model="queryForm.Date"
                clearable
                end-placeholder="结束日期"
                v-if="item.name === '日期'"
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
          <el-button type="success" @click="handleAdd">新建</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="日志名称"
          prop="reportname"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.reportname }}
            </el-button>
            <!-- <el-button type="text" @click="handleDetail(row)"></el-button> -->
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="报告类型"
            v-if="item.name === '报告类型'"
            prop="reporttype"
          />
          <el-table-column
            align="center"
            label="报告人"
            v-if="item.name === '报告人'"
            prop="reporter.realname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="报告部门"
            v-if="item.name === '报告部门'"
            prop="reportdepartment.orgname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="日志时间"
            v-if="item.name === '日志时间'"
            prop="reporttime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="createId != row.createstaffid"
            >
              删除
            </el-button>
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
    <LogInfo
      v-if="logoState"
      ref="edit"
      @close="
        () => {
          this.logoState = false
        }
      "
      @fetch-data="fetchData"
    />
  </div>
</template>

<script>
  import {
    workReportDelete,
    workReportDetail,
    workReportList,
  } from '@/api/audit/implement'
  import { formatDay } from '@/utils/index'
  import LogInfo from './components/LogInfo'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, LogInfo },
    data() {
      return {
        logoState: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          reportname: '',
          reporttype: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
        // 筛选列表配置
        filedAll: [
          { name: '报告类型' },
          { name: '报告人' },
          { name: '报告部门' },
          { name: '日志时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-implement-log-search',
        tableKey: 'audit-implement-log-list',
        searchMore: true,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
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
      getFiled() {
        return [
          { name: '日志名称', key: 'reportname' },
          { name: '报告类型', key: 'reporttype' },
          { name: '日期', key: 'Date' },
        ]
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      resetQueryForm() {
        this.queryForm = {
          reportname: '',
          reporttype: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const { Date, ...other } = this.queryForm
        let starttime = ''
        let endtime = ''
        if (Date) {
          starttime = Date[0]
          endtime = Date[1]
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await workReportList({ ...other, starttime, endtime })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.logoState = true
        this.$nextTick(() => {
          this.$refs['edit'].showEdit('add', null)
        })
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        this.logoState = true
        const data = await workReportDetail({ reportid: row.reportid })
        this.$nextTick(() => {
          this.$refs['edit'].showEdit('detail', data.data)
        })
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        this.logoState = true
        const data = await workReportDetail({ reportid: row.reportid })
        this.$nextTick(() => {
          this.$refs['edit'].showEdit('edit', data.data)
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await workReportDelete({
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
  .detail {
    cursor: pointer;
  }
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
