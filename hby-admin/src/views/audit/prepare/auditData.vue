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
            <el-form-item v-for="(item, index) in searchItem" :key="index">
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
                @click="queryData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button native-type="submit" type="primary" @click="resetSearch">
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
            <el-form-item style="cursor: pointer">
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
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
          <el-button type="success" @click="handleAdd">导入资料</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>

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
          <el-table-column v-if="item.name === '项目经理'" align="center" label="项目经理" prop="realname" />
          <el-table-column
            v-if="item.name === '被审计单位'"
            align="center"
            label="被审计单位"
            prop="auditOrgInfo.orgname"
            show-overflow-tooltip
          >
            <template slot-scope="{ row }">
              {{ row.auditStaffName ? row.auditStaffName : row.orgIdNames }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '项目类型'"
            align="center"
            label="项目类型"
            prop="auditType"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '项目来源'"
            align="center"
            label="项目来源"
            prop="projectSource"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '开始时间'"
            align="center"
            label="开始时间"
            prop="startDate"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            align="center"
            label="实际开始时间"
            prop="assigbedpmTime"
            show-overflow-tooltip
            width="120"
          /> -->
          <el-table-column
            v-if="item.name === '结束时间'"
            align="center"
            label="结束时间"
            prop="endDate"
            show-overflow-tooltip
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
    <ImportData ref="importData" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getProjectProposal, ifPmOrLeader } from '@/api/audit/preparation'
  import { doDelete } from '@/api/table'
  import { UTCformat } from '@/utils'
  import RecordInfo from '@/views/audit/auditRecord/components/RecordInfo.vue'
  import ImportData from '@/views/audit/plan/components/ImportData'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, RecordInfo, ImportData },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          prjoectName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        isOpen: false,
        // 筛选列表配置
        filedAll: [
          { name: '项目经理' },
          { name: '被审计单位' },
          { name: '项目类型' },
          { name: '项目来源' },
          { name: '开始时间' },
          { name: '结束时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-prepare-auditData-search',
        tableKey: 'audit-prepare-auditData-list',
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.ifOpen()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [
          { name: '项目名称', key: 'prjoectName' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          prjoectName: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
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
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getProjectProposal(this.queryForm)
        this.list = tlist
        this.list.forEach((item) => {
          item.startDate = UTCformat(item.startDate)
          item.assigbedpmTime = UTCformat(item.assigbedpmTime)
          item.endDate = UTCformat(item.endDate)
        })
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['importData'].showEdit()
      },
      handleEdit(type, row) {
        this.$refs[type].showEdit(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>
