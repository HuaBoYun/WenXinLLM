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
              v-model="queryForm.leavename"
              clearable
              placeholder="离场纪要名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.leavecoed"
              clearable
              placeholder="离场纪要编号"
            />
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
      <el-table-column align="center" label="离场纪要编号" prop="leavecoed">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.leavecoed }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="离场纪要名称" prop="leavename" />
      <el-table-column
        align="center"
        label="创建人"
        prop="createstaffid"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="creatrtime"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="状态"
        prop="status"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{ row.status == '1' ? '已作废' : '正常' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button
            v-show="row.status == 0"
            type="text"
            @click="handleEdit(row)"
          >
            修改
          </el-button>
          <!-- <el-button type="text" @click="handleExport(row)">导出</el-button> -->
          <el-button
            type="text"
            v-show="row.status != 1"
            @click="handleCancel(row)"
          >
            作废
          </el-button>
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
    <LeaveSummaryInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { formatDate } from '@/utils/index'
  import {
    leaveSummaryList,
    leaveSummaryCancel,
    leaveSummaryDisp,
    leaveSummaryExport,
  } from '@/api/audit/implement'
  import LeaveSummaryInfo from '@/views/audit/implement/components/LeaveSummaryInfo'

  export default {
    name: 'Download',
    components: { LeaveSummaryInfo },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          leavename: '',
          leavecoed: '',
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
        return formatDate(data)
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
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
        } = await leaveSummaryList({ ...other, startDate, endDate })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        const data = await leaveSummaryDisp({ leaveid: row.leaveid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        const data = await leaveSummaryDisp({ leaveid: row.leaveid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      handleCancel(row) {
        this.$baseConfirm('你确定要作废当前项吗', null, async () => {
          const { msg, code } = await leaveSummaryCancel({
            leaveid: row.leaveid,
          })
          if (code == 0) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      // handleDelete(row) {
      //   this.$baseConfirm('你确定要删除当前项吗', null, async () => {
      //     const { msg } = await doDelete({ ids: row.id })
      //     this.$baseMessage(msg, 'success', 'vab-hey-message-success')
      //     await this.fetchData()
      //   })
      // },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
