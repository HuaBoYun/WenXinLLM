<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-left-panel>
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

        <!-- <el-button type="success" @click="handleExport('问题清单')">
          问题清单导出
        </el-button>
        <el-button type="success" @click="handleExport('责任清单')">
          责任清单导出
        </el-button>
        <el-button type="success" @click="handleExport('整改清单')">
          整改清单导出
        </el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="问题编号" prop="issuesCode">
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
          />
          <el-table-column
            align="center"
            label="整改通知"
            prop="rectificationPlan"
            v-if="item.name === '整改通知'"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '预计完成时间'"
            align="center"
            label="预计完成时间"
            prop="deadline"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="state"
            show-overflow-tooltip
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
        </div>

        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button
              type="text"
              @click="practicable(row)"
              :disabled="row.status > 0 && row.status < 6"
            >
              后续整改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!row.status"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!row.status || btnLoading"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
    <practicableForm
      v-if="showPracticableForm"
      ref="practicableForm"
      :isHxzg="true"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
    />

    <ProcessList ref="process" @fetch-data="fetchData" />
    <WfqdDeal ref="wfqddeal" />

    <ExecutorOptions ref="executor" @projectManage="handleExecutorSelected" />
  </div>
</template>

<script>
  import {
    zgzzGethxzgList,
    getIssuesAllDetailInfo,
    saveIssuesRelaImpementer,
    exportZGList,
    exportZRList,
    exportWTList,
  } from '@/api/zgzz/index.js'
  import { formatDay } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import practicableForm from '@/views/audit/rectify/components/form/practicableForm.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  export default {
    name: 'Download',
    components: {
      filterSearch,
      filterTable,
      practicableForm,
      ProcessList,
      WfqdDeal,
      ExecutorOptions,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          issuesCode: '',
          issuesName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '问题名称' },
          { name: '整改通知' },
          { name: '预计完成时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-practicable-search',
        tableKey: 'audit-rectify-practicable-list',
        searchMore: true,
        showPracticableForm: false,
        btnLoading: false,
        rows: {},
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      console.log('hxzg')
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        if (!row.implId) {
          this.$baseMessage('未落实，无法提交审批', 'error')
          return
        }
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(333, row.implId)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.implId,
          tableId: 333,
        })
        this.listLoading = false

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          { name: '问题编号', key: 'issuesCode' },
          { name: '问题名称', key: 'issuesName' },
        ]
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
          issuesCode: '',
          issuesName: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.btnLoading = false
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await zgzzGethxzgList(this.queryForm)
        this.list = tlist.map((x) => {
          const { issues, reimpl, status, ...other } = x
          return {
            ...other,
            ...issues,
            ...reimpl,
            implId: other.implId,
            rectificationPlan: other.rectificationPlan,
            deadline: other.deadline ? new Date(other.deadline) : '',
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      async practicable(row) {
        this.showPracticableForm = true
        this.$nextTick(async () => {
          if (row.relaId) {
            const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
            if (res && res.data) {
              this.$refs.practicableForm.showEdit('edit', res.data)
            }
          }
        })
      },
      async handleDetail(row) {
        this.showPracticableForm = true
        this.$nextTick(async () => {
          if (row.relaId) {
            const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
            if (res && res.data) {
              this.$refs.practicableForm.showEdit('detail', res.data)
            }
          }
        })
      },
      handleForward(row) {
        this.rows = row
        this.$refs['executor'].showEdit()
      },
      async handleExecutorSelected(val) {
        const res = await saveIssuesRelaImpementer({
          impementerId: val[0].staffid,
          relaId: this.rows.relaId,
        })
        console.log(res)
        this.$baseMessage('操作成功', 'success', 'vab-hey-message-success')
        await this.fetchData()
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
      closeDialog() {
        this.showPracticableForm = false
      },
      async handleExport(type) {
        const obj = { ...this.queryForm }
        let data = null
        let filename = ''
        if (type === '问题清单') {
          data = await exportWTList(obj)
          filename = '问题清单.xlsx'
        } else if (type === '责任清单') {
          data = await exportZRList(obj)
          filename = '责任清单.xlsx'
        } else if (type === '整改清单') {
          data = await exportZGList(obj)
          filename = '整改清单.xlsx'
        }

        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
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
