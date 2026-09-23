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
                v-model="queryForm.document"
                clearable
                placeholder="文号"
                v-if="item.name === '文号'"
              />
              <el-input
                v-model="queryForm.title"
                clearable
                placeholder="标题"
                v-if="item.name === '标题'"
              />
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
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
        <el-button type="success" @click="handleAdd" v-if="isShow">
          新建
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="文号"
          prop="document"
          width="170"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.document }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="标题"
            v-if="item.name === '标题'"
            prop="title"
          />
          <el-table-column
            align="center"
            label="主审"
            v-if="item.name === '主审'"
            prop="zsname"
          />
          <el-table-column
            align="center"
            label="创建人"
            v-if="item.name === '创建人'"
            prop="cjr"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="创建时间"
            v-if="item.name === '创建时间'"
            prop="cjsj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="项目名称"
            v-if="item.name === '项目名称'"
            prop="projectName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
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
              v-if="isShow"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleManage(row)"
                    :disabled="!row.status"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!row.status"
                    v-if="isShow"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!row.status"
                    v-if="isShow"
                  >
                    删除
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
    <SuggestView ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import { formatDate } from '@/utils/index'
  import { sjbgdgList, sjbgdgDelete } from '@/oapi/audit/report'
  import SuggestView from '@/views/oilAudit/report/components/SuggestView.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  export default {
    name: 'Download',
    components: {
      SuggestView,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },
    props: {
      isShow: {
        //项目查看传参
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
      },
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          document: '',
          title: '',
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '标题' },
          { name: '主审' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '项目名称' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-report-suggest-search',
        tableKey: 'oilAudit-report-suggest-list',
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
      handleApproval(row) {
        this.$refs['process'].save(178, row.sjbgdgid)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.sjbgdgid,
          tableId: 178,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      getFiled() {
        return [
          { name: '文号', key: 'document' },
          { name: '标题', key: 'title' },
          { name: '项目名称', key: 'projectName' },
        ]
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetQueryForm() {
        this.queryForm = {
          document: '',
          title: '',
          projectName: '',
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
        const {
          data: { tlist, totalRecord },
        } = await sjbgdgList({ ...this.queryForm, projectId: this.projectId })
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      async handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      async handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleInvalid(row) {
        if (row.status == 1) {
          this.$baseMessage('已作废的建议书不能作废', 'error')
          return
        }
        this.$baseConfirm('你确定要作废当前项吗', null, async () => {
          const { msg, code } = await auditSuggestCancel({
            sjbgdgid: row.sjbgdgid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await sjbgdgDelete({
            ids: row.sjbgdgid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
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
