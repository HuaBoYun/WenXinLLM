<template>
  <!-- 工程专项 -->
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
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.projectName"
                clearable
                v-if="item.name === '审计项目名称'"
                placeholder="审计项目名称"
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
        <el-button type="success" @click="handleEdit(null)">新增</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column
          align="center"
          label="序号"
          prop="sheetCode"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.sheetCode }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column width="1" /> -->

        <!-- 表格 -->
        <el-table-column align="center" label="排序" prop="sort" width="100" />
        <el-table-column align="center" label="审计项目名称" prop="projectName">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, true)"
              style="white-space: pre-line; line-height: 16px"
            >
              {{ row.projectName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '立项理由及审计目的'"
            align="center"
            label="立项理由及审计目的"
            prop="projectPurpose"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '重点关注内容'"
            align="center"
            label="重点关注内容"
            prop="concernsContent"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单位范围'"
            align="center"
            label="单位范围"
            prop="unitRange"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '时间范围'"
            align="center"
            label="时间范围"
            prop="timeRange"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            v-if="item.name === '建议科室'"
            align="center"
            label="建议科室"
          >
            <template #default="{ row }">
              {{ row.suggestDept?.orgname }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            prop="status"
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

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :disabled="!row.status"
                  @click.native="handleDeal(row)"
                >
                  办理
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="!!row.status"
                  @click.native="handleSubmit(row)"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="!!row.status"
                  @click.native="handleDelete(row)"
                >
                  删除
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
    <gczxpxbEdit ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  // import { getDgListAll, dgDetail } from '@/api/monitor/question'
  // import DraftInfo from "./components/DraftInfo";
  import { gczxpxbList, gczxpxbDelete } from '@/oapi/audit/plan'
  import gczxpxbEdit from './components/gczxpxbEdit'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import LRJJZRSQview from '@/views/oilAudit/lrjjzr/components/lrjjzrsqView.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'

  export default {
    name: 'gczxpxb',
    components: {
      LRJJZRSQview,
      // DraftInfo,
      gczxpxbEdit,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          sheetcode: '',
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '建议科室' },
          { name: '立项理由及审计目的' },
          { name: '重点关注内容' },
          { name: '单位范围' },
          { name: '时间范围' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-fgldhz-search',
        tableKey: 'oilAudit-jhlx-fgldhz-list',
        searchMore: true,
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
        return [{ name: '审计项目名称', key: 'projectName' }]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          sheetcode: '',
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
        const {
          data: { tlist, totalRecord },
        } = await gczxpxbList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        const data = await dgDetail({ sheetid: row.sheetId })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      async handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      // async handleEdit(row) {
      //   const data = await dgDetail({ sheetid: row.sheetid })
      //   await this.$refs['edit'].showEdit('edit', data.data)
      // },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await gczxpxbDelete({ ids: row.id })
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
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 117,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      handleSubmit(row) {
        this.$baseConfirm('你确定要提交审批当前项吗', null, async () => {
          this.$refs['process'].save(117, row.id)
          // GCZXPXB gczxpxb
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
