<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.code"
              clearable
              placeholder="方案编号"
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
      </vab-query-form-left-panel>
      <!-- <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel> -->
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="方案编号"
        prop="solutioncode"
        width="170"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.solutioncode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="方案名称" prop="solutionname" />
      <el-table-column
        align="center"
        label="创建人"
        prop="createStaff.realname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建日期"
        prop="createdate"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="整改联络人"
        prop="reformUser.realname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="状态"
        prop="runstatus"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.runstatus == '0'
              ? '未分派'
              : row.runstatus == '1'
              ? '开始整改'
              : row.runstatus == '2'
              ? '整改中'
              : row.runstatus == '3'
              ? '方案已结束'
              : row.runstatus == '4'
              ? '方案审批中'
              : '整改完成'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="tailAfter(row)">跟踪</el-button>
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
    <scheme-info ref="edit" @fetch-data="fetchData" />
    <trackTable ref="table" />
  </div>
</template>

<script>
  import {
    getSolutionDetail,
    getZggzsolutionmgmtList,
  } from '@/oapi/audit/rectify'
  import { doDelete } from '@/oapi/table'
  import { formatDay } from '@/utils/index'
  import SchemeInfo from './components/SchemeInfo'
  import trackTable from './components/table/trackTable'

  export default {
    name: 'Download',
    components: { SchemeInfo, trackTable },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
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
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getZggzsolutionmgmtList(this.queryForm)
        this.list = list
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(row) {
        const data = await getSolutionDetail({ solutionid: row.solutionid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      async tailAfter(row) {
        await this.$refs['table'].showEdit(row.solutionid)
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
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
