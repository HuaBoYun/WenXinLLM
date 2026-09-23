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
                v-model="queryForm.changething"
                clearable
                placeholder="变更事项"
                v-if="item.name === '变更事项'"
              />
              <el-input
                v-model="queryForm.changebefore"
                clearable
                placeholder="变更前内容"
                v-if="item.name === '变更前内容'"
              />
              <el-input
                v-model="queryForm.changeafter"
                clearable
                placeholder="变更后内容"
                v-if="item.name === '变更后内容'"
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
        <el-button
          type="success"
          @click="handleAdd(false, false)"
          v-if="isShow"
        >
          新建
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="单位（科室）" prop="orgname">
          <template #default="{ row }">
            <el-button type="text" @click="handleAdd(row, true)">
              {{ row.orgname }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '审计项目名称'"
            align="center"
            label="审计项目名称"
            prop="projectName"
          />
          <el-table-column
            v-if="item.name === '变更事项'"
            align="center"
            label="变更事项"
            prop="changething"
          />
          <el-table-column
            v-if="item.name === '变更前内容'"
            align="center"
            label="变更前内容"
            prop="changebefore"
          />
          <el-table-column
            v-if="item.name === '变更后内容'"
            align="center"
            label="变更后内容"
            prop="changeafter"
          />
          <el-table-column
            v-if="item.name === '变更原因'"
            align="center"
            label="变更原因"
            prop="changereason"
          />
          <el-table-column
            v-if="item.name === '经办人'"
            align="center"
            label="经办人"
            prop="jbr"
          />
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
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleAdd(row, false)"
              :disabled="!!row.status"
              v-if="isShow"
            >
              修改
            </el-button>
            <!-- <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="row.status !== 0"
            >
              删除
            </el-button> -->
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
    <ProcessList ref="process" />
    <WfqdDeal ref="wfqdDeal" />
    <NoticeBiangengView ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getNoticeChangeList,
    deleteNoticeItem,
  } from '@/oapi/audit/preparation'
  import { UTCformat } from '@/utils'
  import NoticeBiangengView from './components/noticeBiangengView.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    components: {
      NoticeBiangengView,
      ProcessList,
      WfqdDeal,
      filterSearch,
      filterTable,
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
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          changething: '',
          changebefore: '',
          changeafter: '',
          pageNumber: 1,
          pageSize: 20,
          // Date: [],
        },
        filedAll: [
          { name: '审计项目名称' },
          { name: '变更事项' },
          { name: '变更前内容' },
          { name: '变更后内容' },
          { name: '变更原因' },
          { name: '经办人' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-prepare-notice_biangeng-search',
        tableKey: 'oilAudit-prepare-notice_biangeng-list',
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
    methods: {
      getFiled() {
        return [
          { name: '变更事项', key: 'changething' },
          { name: '变更前内容', key: 'changebefore' },
          { name: '变更后内容', key: 'changeafter' },
        ]
      },
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(171, row.changeid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.changeid,
          tableId: 171,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      resetQueryForm() {
        this.queryForm = {
          changething: '',
          changebefore: '',
          changeafter: '',
          pageNumber: 1,
          pageSize: 20,
          // Date: [],
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
        this.btnLoading = false
        this.listLoading = false
        // const { Date, ...other } = this.queryForm
        // let startDate = ''
        // let endDate = ''
        // if (Date) {
        //   startDate = Date[0]
        //   endDate = Date[1]
        // }
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getNoticeChangeList({
          ...this.queryForm,
          projectId: this.projectId,
        })
        this.list = tlist
        // this.list.forEach((item) => {
        //   item.creatrtime = UTCformat(item.creatrtime)
        // })
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteNoticeItem({ changeid: row.changeid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
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
