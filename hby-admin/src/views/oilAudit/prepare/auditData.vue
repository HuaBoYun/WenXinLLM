<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form>
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
                v-model="queryForm.prjoectName"
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
      </vab-query-form>
    </el-card>

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
        <el-button type="success" @click="handleAdd" v-if="isOpen && isShow">
          导入资料
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="项目名称"
          width="100"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit('recordInfo', row)">
              {{ row.prjoectName }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="项目经理"
            v-if="item.name === '项目经理'"
            prop="realname"
          />
          <el-table-column
            align="center"
            label="被审计单位"
            v-if="item.name === '被审计单位'"
            prop="auditOrgInfo.orgname"
            show-overflow-tooltip
          >
            <template slot-scope="{ row }">
              {{ row.auditOrgName }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="项目类型"
            v-if="item.name === '项目类型'"
            prop="auditType"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="项目来源"
            v-if="item.name === '项目来源'"
            prop="projectSource"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="开始时间"
            v-if="item.name === '开始时间'"
            prop="startDate"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="实际开始时间"
            v-if="item.name === '实际开始时间'"
            prop="assigbedpmTime"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="实际结束时间"
            v-if="item.name === '实际结束时间'"
            prop="endDate"
            show-overflow-tooltip
            width="120"
          />
        </div>

        <el-table-column width="1" />
        <!-- <el-table-column
          align="center"
          label="状态"
          prop="status"
          show-overflow-tooltip
          width="120"
        >
          <template slot-scope="scope">
            {{ scope.row.status == 0 ? '已归档' : '未归档' }}
          </template>
        </el-table-column> -->
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
    <RecordInfo ref="recordInfo" @fetch-data="fetchData" />
    <!-- <ImportData ref="importData" @fetch-data="fetchData" /> -->
  </div>
</template>

<script>
  import { getProjectProposal, ifPmOrLeader } from '@/oapi/audit/preparation'
  import { doDelete } from '@/oapi/table'
  import { UTCformat } from '@/utils'
  import RecordInfo from '@/views/oilAudit/auditRecord/components/RecordInfo.vue'
  import ImportData from '@/views/oilAudit/plan/components/ImportData'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    components: { RecordInfo, ImportData, filterSearch, filterTable },
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
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        isOpen: false,
        filedAll: [
          { name: '项目经理' },
          { name: '被审计单位' },
          { name: '项目类型' },
          { name: '项目来源' },
          { name: '开始时间' },
          { name: '实际开始时间' },
          { name: '实际结束时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-prepare-auditData-search',
        tableKey: 'oilAudit-prepare-auditData-list',
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.ifOpen()
    },
    methods: {
      getFiled() {
        return [{ name: '项目名称', key: 'prjoectName' }]
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
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
      async ifOpen() {
        const res = await ifPmOrLeader()
        if (res.data && res.data.ifLeader) {
          this.isOpen = true
        } else {
          this.isOpen = false
        }
      },
      async fetchData() {
        this.listLoading = true
        getProjectProposal({
          ...this.queryForm,
          projectId: this.projectId,
        })
          .then((res) => {
            const {
              data: {
                pageInfo: { tlist, totalRecord },
              },
            } = res
            this.list = tlist
            this.list.forEach((item) => {
              item.startDate = UTCformat(item.startDate)
              item.assigbedpmTime = UTCformat(item.assigbedpmTime)
              item.endDate = UTCformat(item.endDate)
            })
            this.total = totalRecord
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      handleAdd() {
        this.$refs['importData'].showEdit()
      },
      handleEdit(type, row) {
        this.$refs[type].showEdit(row)
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
