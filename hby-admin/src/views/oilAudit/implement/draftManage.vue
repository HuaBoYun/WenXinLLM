<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.draftNumber"
                clearable
                placeholder="底稿编号"
                v-if="item.name === '底稿编号'"
              />
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="审计项目名称"
                v-if="item.name === '审计项目名称'"
              />
              <el-input
                v-model="queryForm.realname"
                clearable
                placeholder="拟稿人"
                v-if="item.name === '拟稿人'"
                :style="{ width: '256px' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.executor.show()"
                v-if="item.name === '拟稿人'"
              >
                选择
              </el-button>
              <el-select
                v-model="queryForm.status"
                placeholder="审核状态"
                v-if="item.name === '审核状态'"
              >
                <el-option label="未复核" value="1" />
                <el-option label="复核中" value="2" />
                <el-option label="复核终止" value="3" />
                <el-option label="复核通过" value="4" />
                <el-option label="需调整" value="5" />
              </el-select>
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
          @click="handleNumber()"
          v-if="canShowGenerateButton"
        >
          生成编号
        </el-button>
        <el-button type="success" @click="handleExport()">导出</el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="底稿编号"
            prop="draftNumber"
            width="170"
            v-if="item.name === '底稿编号'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.draftNumber }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="底稿名称"
            prop="draftName"
            show-overflow-tooltip
            v-if="item.name === '底稿名称'"
          />
          <el-table-column
            align="center"
            label="审计项目名称"
            v-if="item.name === '审计项目名称'"
            prop="projectName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审计事项"
            prop="auditMatters"
            v-if="item.name === '审计事项'"
          />
          <el-table-column
            align="center"
            label="被审计单位名称"
            prop="auditeeName"
            v-if="item.name === '被审计单位名称'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建人"
            prop="createUsername"
            v-if="item.name === '创建人'"
          ></el-table-column>
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

        <el-table-column width="1" />
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <newMyDraftView ref="edit"></newMyDraftView>
    <!-- <DraftManageInfo ref="edit" @fetch-data="fetchData" /> -->
    <executor-options ref="executor" @selected="handleExecutorSelected" />
  </div>
</template>

<script>
  import {
    draftManageDelete,
    draftManageList,
    getManuscriptById,
    myDraftExport,
    MyManuscriptAutoCode,
  } from '@/oapi/audit/implement'
  // import { myDraftExport } from '@/oapi/audit/newMyDraft'

  import { doDelete } from '@/oapi/table'
  import { formatDate } from '@/utils/index'
  import DraftManageInfo from './components/myDraftInfo'
  import ExecutorOptions from './components/options/executor.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import newMyDraftView from '@/views/oilAudit/implement/components/newMyDraftView.vue'
  import { implementPlanProject } from '@/oapi/audit/project.js'
  export default {
    name: 'Download',
    components: {
      DraftManageInfo,
      ExecutorOptions,
      filterSearch,
      filterTable,
      newMyDraftView,
    },
    mixins: [searchTableMixis],
    props: {
      isShow: {
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectName: '',
          draftNumber: '',
          realname: '',
          status: '',
          staffid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        projectInfo: {},
        filedAll: [
          { name: '底稿编号' },
          { name: '底稿名称' },
          { name: '审计项目名称' },
          { name: '审计事项' },
          { name: '被审计单位名称' },
          { name: '创建时间' },
          { name: '创建人' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-implement-draftManage-search',
        tableKey: 'oilAudit-implement-draftManage-list',
        searchMore: true,
        planInfo: {},
      }
    },
    computed: {
      // 判断当前用户是否有权限显示生成编号按钮
      canShowGenerateButton() {
        const staffid = JSON.parse(localStorage.getItem('userInfo')).staffid
        return this.planInfo.zsstaffid == staffid
      },
    },
    created() {
      this.fetchData()
      this.getImplementPlanProject()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      async getImplementPlanProject() {
        const {
          data: { pj },
        } = await implementPlanProject()
        this.planInfo = pj || {}
      },
      composeApprover(row) {
        if (row.yjfh && row.ejfh) {
          return row.yjfh + ',' + row.ejfh
        }
        return row.yjfh || '' || row.ejfh || ''
      },
      getFiled() {
        return [
          { name: '底稿编号', key: 'draftNumber' },
          { name: '审计项目名称', key: 'projectName' },
          // { name: '拟稿人', key: 'realname' },
          // { name: '审核状态', key: 'status' },
        ]
      },
      handleExecutorSelected(node) {
        this.queryForm.realname = node.realname
        this.queryForm.staffid = node.staffid
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetQueryForm() {
        this.queryForm = {
          projectName: '',
          draftNumber: '',
          realname: '',
          status: '',
          staffid: '',
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
        draftManageList({ ...this.queryForm, projectId: this.projectId })
          .then((res) => {
            const {
              data: {
                project,
                pageInfo: { tlist: list, totalRecord: total },
              },
            } = res
            this.projectInfo = project || {}
            this.list = list
            this.total = total
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      async handleDetail(row) {
        // const res = await getManuscriptById({ id: row.id })
        // this.$refs['edit'].showEdit('detail', res.data)
        this.$refs['edit'].showModal(row, false, true)
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
      async handleNumber() {
        const { msg, code } = await MyManuscriptAutoCode()
        if (code == 1) {
          this.$baseMessage(msg, 'success')
          this.fetchData()
        } else {
          this.$baseMessage(msg, 'error')
        }
      },
      async handleExport() {
        const data = await myDraftExport({ ...this.queryForm })
        let fileName = '底稿管理'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
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

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
