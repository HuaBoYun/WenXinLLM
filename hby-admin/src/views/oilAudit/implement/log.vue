<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.reportname"
              clearable
              placeholder="日志名称"
            />
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.reporttype" placeholder="报告类型">
              <el-option label="日报" value="日报" />
              <el-option label="周报" value="周报" />
              <el-option label="总结报告" value="总结报告" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="queryForm.Date"
              clearable
              end-placeholder="结束日期"
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
              @click="fetchData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
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
      <el-table-column align="center" label="报告类型" prop="reporttype" />
      <el-table-column
        align="center"
        label="报告人"
        prop="reporter.realname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="报告部门"
        prop="reportdepartment.orgname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="日志时间"
        prop="reporttime"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
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
  } from '@/oapi/audit/implement'
  import { formatDay } from '@/utils/index'
  import LogInfo from './components/LogInfo'

  export default {
    name: 'Download',
    components: { LogInfo },
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
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
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
      handleAdd() {
        this.logoState = true
        this.$nextTick(() => {
          this.$refs['edit'].showEdit('add', null)
        })
      },
      async handleDetail(row) {
        this.logoState = true
        const data = await workReportDetail({ reportid: row.reportid })
        this.$nextTick(() => {
          this.$refs['edit'].showEdit('detail', data.data)
        })
      },
      async handleEdit(row) {
        this.logoState = true
        const data = await workReportDetail({ reportid: row.reportid })
        this.$nextTick(() => {
          this.$refs['edit'].showEdit('edit', data.data)
        })
      },
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

<style>
  .detail {
    cursor: pointer;
  }
</style>
