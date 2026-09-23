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
              placeholder="报告名称"
            />
          </el-form-item>
          <el-form-item>
            <el-date-picker
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
        <el-button type="primary" @click="exportData">导出初稿</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <!-- <el-table-column type="selection" width="55" /> -->
      <el-table-column align="center" label="报告名称" prop="reportname">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.reportname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="报告时间"
        prop="reporttime"
        :formatter="formatDay"
      />
      <el-table-column
        align="center"
        label="报告版本"
        prop="reporttype"
        show-overflow-tooltip
      />
      />
      <el-table-column align="center" label="报告方式" prop="reportmode" />
      <!-- <el-table-column align="center" label="复核状态" prop="reportstatus">
        <template #default="{ row }">
          {{ statusName[row.reportstatus] || '未审核' }}
        </template>
      </el-table-column> -->
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
          >
            修改
          </el-button>
          <!-- <el-button
            type="text"
            :disabled="statusName[row.reportstatus] !== '未审批'"
            @click="handleFH(row)"
          >
            复核
          </el-button>
          <el-button
            type="text"
            @click="handleZQYJ(row)"
            :disabled="statusName[row.reportstatus] !== '复核通过'"
          >
            征求意见
          </el-button>
          <el-button
            type="text"
            :disabled="statusName[row.reportstatus] !== '征求意见通过'"
            @click="handleSP(row)"
          >
            审批
          </el-button> -->
          <el-button
            type="text"
            @click="handleDelete(row)"
            :disabled="statusName[row.reportstatus] !== '未审批'"
          >
            删除
          </el-button>
          <el-button type="text" @click="handleExport(row)">导出</el-button>
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
    exportReportChuGao,
    report,
    reportDel,
    reportDetail,
    reportExport,
  } from '@/oapi/audit/report'
  import { formatDay } from '@/utils/index'
  import IndexView from '@/views/oilAudit/report/components/IndexView'

  export default {
    name: 'Download',
    components: { IndexView },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        statusName: [
          '未审批',
          '审批中',
          '已退回',
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
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      formatDay(row, column) {
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
        } = await report({ ...other, startDate, endDate, type: 'nbsj' })
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
      async exportData() {
        const data = await exportReportChuGao()
        let fileName = '报告编制初稿'
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
      },
    },
  }
</script>
