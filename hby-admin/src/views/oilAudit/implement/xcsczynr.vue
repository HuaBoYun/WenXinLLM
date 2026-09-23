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
                v-model="queryForm.settleProjectNum"
                clearable
                placeholder="结算项目编号"
                v-if="item.name === '结算项目编号'"
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
      <vab-query-form>
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
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="结算项目编号"
          prop="settleProjectNum"
          width="170"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.settleProjectNum }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="项目名称"
            v-if="item.name === '项目名称'"
            prop="projectName"
          />
          <el-table-column
            align="center"
            label="结算金额（元）"
            v-if="item.name === '结算金额（元）'"
            prop="settleAmount"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="建设单位"
            v-if="item.name === '建设单位'"
            prop="buildOrgName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="施工单位"
            v-if="item.name === '施工单位'"
            prop="constructionOrgName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="核实主要内容"
            v-if="item.name === '核实主要内容'"
            prop="reviewContent"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审计人员"
            v-if="item.name === '审计人员'"
            prop="reviewStaffName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="建设单位项目经理"
            v-if="item.name === '建设单位项目经理'"
            prop="buildUnitManageName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="现场审查时间"
            v-if="item.name === '现场审查时间'"
            prop="sceneReviewTime"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="复审人员"
            v-if="item.name === '复审人员'"
            prop="recheckStaffName"
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
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              v-if="isShow"
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status"
            >
              修改
            </el-button>
            <!-- <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
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
                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!row.status || btnLoading"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!row.status"
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
    <xcsczynrView ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import {
    siteReviewDelete,
    siteReviewFindOneById,
    siteReviewList,
  } from '@/oapi/audit/implement'
  import xcsczynrView from './components/xcsczynrView.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    components: {
      xcsczynrView,
      ProcessList,
      WfqdDeal,
      filterSearch,
      filterTable,
    },
    mixins: [searchTableMixis],
    props: {
      isShow: {
        //项目查看传参
        type: Boolean,
        default: true,
      },
      row: {
        type: Object,
        default: () => {},
      },
      projectId: {
        //项目查看传参
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        flawStatus: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          settleProjectNum: '',
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '项目名称' },
          { name: '结算金额（元）' },
          { name: '建设单位' },
          { name: '施工单位' },
          { name: '核实主要内容' },
          { name: '审计人员' },
          { name: '建设单位项目经理' },
          { name: '现场审查时间' },
          { name: '复审人员' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-implement-xcsczynr-search',
        tableKey: 'oilAudit-implement-xcsczynr-list',
        searchMore: true,
        btnLoading: false,
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
      getFiled() {
        return [
          { name: '结算项目编号', key: 'settleProjectNum' },
          { name: '项目名称', key: 'projectName' },
        ]
      },
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(157, row.id)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 157,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      resetQueryForm() {
        this.queryForm.settleProjectNum = ''
        this.queryForm.projectName = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
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
        this.btnLoading = false
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await siteReviewList({
          ...this.queryForm,
          projectId: (this.row && this.row.projectId) || this.projectId,
          templateId: this.row && this.row.templateId,
        })

        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null, this.row)
      },
      async handleDetail(row) {
        const data = await siteReviewFindOneById({ siteReviewId: row.id })
        this.$refs['edit'].showEdit('detail', data.data)
      },
      async handleEdit(row) {
        const data = await siteReviewFindOneById({ siteReviewId: row.id })
        // await this.$refs['edit'].showEdit('edit', data.data)
        await this.$refs['edit'].showEdit('edit', data.data, this.row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await siteReviewDelete({ id: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
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
