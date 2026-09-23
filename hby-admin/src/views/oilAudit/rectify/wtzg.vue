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
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.issueNumber"
                clearable
                placeholder="在报告中的对应编号"
                v-if="item.name === '在报告中的对应编号'"
              />
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              ></el-input>
              <el-input
                v-model="queryForm.unitName"
                clearable
                placeholder="被审计单位"
                v-if="item.name === '被审计单位'"
              ></el-input>
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
        <!-- <el-button type="success" @click="handleEdit(null)">新增</el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column align="center" label="序号" type="index" width="50" /> -->
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '在报告中的对应编号'"
            align="center"
            label="在报告中的对应编号"
            prop="tblIssueEntity.issueNumber"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row, true)">
                {{ row.tblIssueEntity.issueNumber }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '项目名称'"
            align="center"
            label="项目名称"
            prop="tblIssueEntity.projectName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '问题所属单位名称'"
            align="center"
            label="问题所属单位名称"
            prop="tblIssueEntity.unitName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '报告中的问题定性'"
            align="center"
            label="报告中的问题定性"
            prop="tblIssueEntity.problemQualitative"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '问题金额（元）'"
            align="center"
            label="问题金额（元）"
            prop="tblIssueEntity.money"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '定性（定性词典）'"
            align="center"
            label="定性（定性词典）"
            prop="tblIssueEntity.qualitative"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '定性法规依据'"
            align="center"
            label="定性法规依据"
            prop="tblIssueEntity.qualitativeRule"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            align="center"
            label="处理意见或整改建议 "
            prop="tblIssueEntity.qualitativeRule2"
            v-if="item.name === '处理意见或整改建议 '"
          ></el-table-column> -->
          <el-table-column
            align="center"
            label="整改时限"
            prop="tblIssueEntity.timeLimit"
            v-if="item.name === '整改时限'"
          ></el-table-column>
          <!-- <el-table-column
            align="center"
            label="整改督促牵头部门或单位"
            prop="zgqtbm"
            v-if="item.name === '整改督促牵头部门或单位'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="整改责任人"
            prop="zrr"
            v-if="item.name === '整改责任人'"
          ></el-table-column> -->
          <el-table-column
            align="center"
            label="整改状态"
            prop="zgzt"
            v-if="item.name === '整改状态'"
          >
            <template #default="{ row }">
              {{
                row.zgzt == 3
                  ? '整改完毕'
                  : row.zgzt == 2
                  ? '正在整改'
                  : row.zgzt == 1
                  ? '尚未开始整改'
                  : row.zgzt == 0
                  ? '不接受审计意见'
                  : ''
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '状态'"
            label="状态"
            prop="status"
            show-overflow-tooltip
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
          <!-- <el-table-column
            align="center"
            label="直接经济成果类型"
            prop="dqzjjjcgtype"
            v-if="item.name === '直接经济成果类型'"
          ></el-table-column> -->
        </div>

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
        >
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="!!row.status"
          >
            整改
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
                >
                  提交审批
                </el-button>
              </el-dropdown-item>
              <!-- <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!row.status"
                  >
                    删除
                  </el-button>
                </el-dropdown-item> -->
            </el-dropdown-menu>
          </el-dropdown>
          <!-- <el-button type="text" @click="handleEdit2(row)">审批</el-button> -->
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
    <wtzgEdit ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
    <!-- <wtzgEdit2 ref="edit2" @fetch-data="fetchData" /> -->
  </div>
</template>

<script>
  import wtzgEdit from './components/wtzgEdit'
  // import wtzgEdit2 from './components/wtzgEdit2'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { getList, deleteInfo } from '@/oapi/yqns_sjzg/wtzg'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'

  export default {
    name: 'wtzg',
    components: {
      filterSearch,
      filterTable,
      wtzgEdit,
      // wtzgEdit2,
      ProcessList,
      WfqdDeal,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          issueNumber: null,
          projectName: null,
          unitName: null,
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '在报告中的对应编号' },
          { name: '项目名称' },
          { name: '问题所属单位名称' },
          { name: '报告中的问题定性' },
          { name: '问题金额（元）' },
          { name: '定性（定性词典）' },
          { name: '定性法规依据' },
          { name: '处理意见或整改建议 ' },
          { name: '整改时限' },
          { name: '整改督促牵头部门或单位' },
          { name: '整改责任人' },
          { name: '整改状态' },
          { name: '直接经济成果类型' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-gcgl-gcxmzj-search',
        tableKey: 'oilAudit-gcgl-gcxmzj-list',
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
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        this.$refs['process'].save(195, row.wtzgid)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.wtzgid,
          tableId: 195,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      getFiled() {
        let fields = [
          { name: '在报告中的对应编号', key: 'issueNumber' },
          { name: '项目名称', key: 'projectName' },
          { name: '被审计单位', key: 'unitName' },
        ]
        return fields
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          issueNumber: null,
          projectName: null,
          unitName: null,
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
          data: { tlist: list, totalRecord: total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      async handleEdit2(row, disabled) {
        this.$refs['edit2'].showEdit(row, disabled)
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
