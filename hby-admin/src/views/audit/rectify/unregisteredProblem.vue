<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
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
                v-if="item.name === '问题编号'"
                v-model="queryForm.issuesCode"
                clearable
                placeholder="问题编号"
              />
              <el-input
                v-if="item.name === '问题名称'"
                v-model="queryForm.issuesName"
                clearable
                placeholder="问题名称"
              />
              <el-input
                v-if="item.name === '问题标题'"
                v-model="queryForm.issuesTitle"
                clearable
                placeholder="问题标题"
              />
              <!-- <el-select
                v-if="item.name === '状态'"
                v-model="queryForm.status"
                placeholder="状态"
              >
                <el-option label="未整改" value="0" />
                <el-option label="审批中" value="1" />
                <el-option label="已退回" value="2" />
                <el-option label="已撤销" value="3" />
                <el-option label="已完成" value="6" />
                <el-option label="整改中" value="7" />
                <el-option label="整改完成" value="8" />
                <el-option label="未销号问题" value="9" />
                <el-option label="再次整改" value="10" />
              </el-select> -->
              <el-input
                v-if="item.name === '问题来源'"
                v-model="queryForm.issuesType"
                clearable
                placeholder="问题来源"
              />
              <el-input
                v-if="item.name === '被审计对象'"
                v-model="queryForm.auditObjectName"
                clearable
                placeholder="被审计对象"
              />
              <el-input
                v-if="item.name === '项目名称'"
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
              />
              <el-input
                v-if="item.name === '项目编号'"
                v-model="queryForm.projectNo"
                clearable
                placeholder="项目编号"
              />
              <el-date-picker
                v-if="item.name === '拟稿日期'"
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
        </vab-query-form-left-panel>
      </el-card>
      <!-- <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel> -->
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="问题编号"
          prop="issuesCode"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.issuesCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '问题名称'"
            align="center"
            label="问题名称"
            prop="issuesName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '方案编号'"
            align="center"
            label="方案编号"
            prop="planCode"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '方案名称'"
            align="center"
            label="方案名称"
            prop="planName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '问题标题'"
            align="center"
            label="问题标题"
            prop="issuesTitle"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '问题详情'"
            align="center"
            label="问题详情"
            prop="questionMemo"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '拟稿人'"
            align="center"
            label="拟稿人"
            prop="createStaffName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '拟稿日期'"
            align="center"
            label="拟稿日期"
            prop="createTime"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <!-- <el-table-column
              v-if="item.name === '状态'"
              align="center"
              label="状态"
              prop="status"
              show-overflow-tooltip
              #default="{ row }"
            >
              {{
                statusMap[row.status] || '未整改'
              }}
            </el-table-column> -->
        </div>

        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button type="text" @click="solution(row)">再次整改</el-button>
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
    <unregisteredProblemForm ref="unregisteredProble" />
    <zgqdEdit
      v-if="showZgqdEdit"
      ref="edit"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
    />
  </div>
</template>

<script>
  import {
    getUnresolvedIssuesList,
    getIssuesDetail,
    issuesRectificationAgain,
  } from '@/api/zgzz/index.js'
  import { doDelete } from '@/api/table'
  import { formatDate } from '@/utils/index'
  import unregisteredProblemForm from './components/form/unregisteredProblemForm'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import zgqdEdit from '@/views/audit/collect/components/edit'

  export default {
    name: 'UnregisteredProblem',
    components: {
      unregisteredProblemForm,
      filterSearch,
      filterTable,
      zgqdEdit,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          issuesName: '',
          issuesCode: '',
          createStaffName: '',
          issuesTitle: '',
          status: '',
          issuesType: '',
          auditObjectName: '',
          projectName: '',
          projectNo: '',
          Date: '',
          startDate: '',
          endDate: '',
          pageNumber: 1,
          pageSize: 20,
        },

        filedAll: [
          { name: '问题名称' },
          { name: '方案编号' },
          { name: '方案名称' },
          { name: '问题标题' },
          { name: '问题详情' },
          { name: '拟稿人' },
          // { name: '拟稿日期' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-unregisteredProblem-search',
        tableKey: 'audit-rectify-unregisteredProblem-list',
        searchMore: true,
        showZgqdEdit: false,
        statusMap: {
          0: '未整改',
          1: '审批中',
          2: '已退回',
          3: '已撤销',
          6: '已完成',
          7: '整改中',
          8: '整改完成',
          9: '未销号问题',
          10: '再次整改',
        },
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
          { name: '问题编号', key: 'issuesCode' },
          { name: '问题名称', key: 'issuesName' },
          { name: '问题标题', key: 'issuesTitle' },
          { name: '状态', key: 'status' },
          // { name: '问题来源', key: 'issuesType' },
          // { name: '被审计对象', key: 'auditObjectName' },
          // { name: '项目名称', key: 'projectName' },
          // { name: '项目编号', key: 'projectNo' },
          // { name: '拟稿日期', key: 'Date' },
        ]
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data).split(' ')[0]
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
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          issuesName: '',
          issuesCode: '',
          createStaffName: '',
          issuesTitle: '',
          status: '',
          issuesType: '',
          auditObjectName: '',
          projectName: '',
          projectNo: '',
          Date: '',
          startDate: '',
          endDate: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const obj = { ...this.queryForm }
        obj.startDate = obj.Date[0]
        obj.endDate = obj.Date[1]
        delete obj.Date
        const {
          data: { tlist, totalRecord },
        } = await getUnresolvedIssuesList(obj)
        this.list = tlist.map((x) => {
          const { issues, plan = {}, ...other } = x
          return {
            ...issues,
            ...plan,
            ...other,
            issuesId: issues.issuesId,
            createStaffName: issues.createStaffName,
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      async handleDetail(row) {
        // const data = await getzgjgReformByid({ reformid: row.reformid });
        // await this.$refs["unregisteredProble"].showEdit(data.data);
        this.showZgqdEdit = true
        this.$nextTick(async () => {
          const res = await getIssuesDetail({ issuesId: row.issuesId })
          this.$refs['edit'].showEdit('detail', res.data)
        })
      },
      closeDialog() {
        this.showZgqdEdit = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      solution(row) {
        this.$baseConfirm('确实再次发起整改吗', null, async () => {
          const { msg } = await issuesRectificationAgain({
            issuesId: row.issuesId,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
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
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
