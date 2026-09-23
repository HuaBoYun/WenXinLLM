<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
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
              placeholder="项目名称"
              v-if="item.name === '项目名称'"
            />
            <el-input
              v-model="queryForm.qdcode"
              clearable
              placeholder="项目编号"
              v-if="item.name === '项目编号'"
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
        </el-form>
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="项目编号"
          prop="qdcode"
          width="100"
        />
        <el-table-column align="center" width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '项目名称'"
            align="center"
            label="项目名称"
            prop="projectName"
          />
          <el-table-column
            v-if="item.name === '项目负责人'"
            align="center"
            label="项目负责人"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '审计类型'"
            align="center"
            label="审计类型"
            prop="sjlxName"
            show-overflow-tooltip
          >
            <!-- <template #default="{ row }">
              {{ row.projectType == 1 ? '计划内' : '归档' }}
            </template> -->
          </el-table-column>
          <el-table-column
            v-if="item.name === '计划开始时间'"
            align="center"
            label="计划开始时间"
            prop="planStarttime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '计划结束时间'"
            align="center"
            label="计划结束时间"
            prop="planEndtime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '计划年度'"
            align="center"
            label="计划年度"
            prop="planYear"
            show-overflow-tooltip
          />
        </div>
        <el-table-column align="center" label="借阅次数" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button type="text" @click="handleEdit(scope.row)">
              {{ scope.row.pcnt || 0 }}
            </el-button>
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
    <LogInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getJyrzList } from '@/oapi/audit/archives'
  import { doDelete } from '@/oapi/table'
  import LogInfo from './components/LogInfo'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Log',
    components: { LogInfo, filterSearch, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
          qdcode: '',
        },
        filedAll: [
          { name: '项目名称' },
          { name: '项目负责人' },
          { name: '审计类型' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '计划年度' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-auditRecord-log-search',
        tableKey: 'oilAudit-auditRecord-log-list',
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
        return [
          { name: '项目编号', key: 'qdcode' },
          { name: '项目名称', key: 'projectName' },
        ]
      },
      resetSearch() {
        this.queryForm = {
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
          qdcode: '',
        }
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
        } = await getJyrzList(this.queryForm)
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
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
