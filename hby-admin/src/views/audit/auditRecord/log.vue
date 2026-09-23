<template>
  <div class="system-log-container">
    <vab-query-form>
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
              <el-input
                v-model="queryForm.projectCode"
                clearable
                placeholder="项目编号"
                v-if="item.name === '项目编号'"
              />
              <el-input
                v-model="queryForm.auditType"
                clearable
                placeholder="项目类别"
                v-if="item.name === '项目类别'"
              />
              <el-date-picker
                value-format="yyyy"
                format="yyyy"
                v-model="queryForm.projectYear"
                placeholder="档案年度"
                v-if="item.name === '档案年度'"
                style="width: 100%"
                type="year"
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
            <el-form-item>
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

    <el-card shadow="never" class="secondCard">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
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
          label="归档状态"
          prop="fileStatus"
          width="100"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="项目编号"
            v-if="item.name === '项目编号'"
            prop="projectCode"
          />
          <el-table-column
            align="center"
            label="项目名称"
            v-if="item.name === '项目名称'"
            prop="prjoectName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="项目类别"
            v-if="item.name === '项目类别'"
            prop="auditType"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="档案年度"
            v-if="item.name === '档案年度'"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审计单位"
            v-if="item.name === '审计单位'"
            prop="auditOrg"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="被审计单位"
            v-if="item.name === '被审计单位'"
            prop="orgIdNames"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="借阅次数"
            v-if="item.name === '借阅次数'"
            prop="externAlassig"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-button type="text" @click="handleEdit(scope.row)">
                {{ scope.row.pcount || 0 }}
              </el-button>
            </template>
          </el-table-column>
        </div>
        <el-table-column width="1"/>
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
    <LogInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getJyrzNewlist } from '@/api/audit/archives'
  import { doDelete } from '@/api/table'
  import LogInfo from './components/LogInfo'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Log',
    mixins: [searchTableMixis],
    components: {
      filterTable,
      filterSearch, LogInfo },
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
          projectYear: '',
          projectCode: '',
          auditType: '',
        },
        // 筛选、表格头自定义
        filedAll: [
          { name: '项目编号' },
          { name: '项目名称' },
          { name: '项目类别' },
          { name: '档案年度' },
          { name: '审计单位' },
          { name: '被审计单位' },
          { name: '借阅次数' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'audit-auditRecord-log-search',
        tableKey: 'audit-auditRecord-log-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '项目名称', key: 'prjoectName' },
          { name: '项目编号', key: 'projectCode' },
          { name: '项目类别', key: 'auditType' },
          { name: '档案年度', key: 'projectYear' },
        ]
        return fields
      },
      resetSearch() {
        this.queryForm = {
          prjoectName: '',
          pageNumber: 1,
          pageSize: 20,
          projectYear: '',
          projectCode: '',
          auditType: '',
        }
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
        } = await getJyrzNewlist(this.queryForm)
        this.list = tlist
        this.list.forEach((item) => {
          item.fileStatus = '已归档'
        })
        this.total = totalRecord
        this.listLoading = false
      },

      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
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
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
