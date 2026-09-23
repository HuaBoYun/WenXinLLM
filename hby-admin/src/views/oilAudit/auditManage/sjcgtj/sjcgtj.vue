<template>
  <div class="system-log-container">
    <!-- <vab-query-form class="margin-b0">
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
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
 
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search" 
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
    </vab-query-form> -->
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
        <el-table-column align="center" label="项目名称" prop="projectName" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column align="center" v-if="item.name === '问题个数'" label="问题个数" prop="numberCount" />
          <el-table-column align="center" v-if="item.name === '审计发现问题金额'" label="审计发现问题金额" prop="sumMoney" />
          <el-table-column align="center" v-if="item.name === '直接经济成果数'" label="直接经济成果数" prop="sumZjjjcgje" />
          <el-table-column align="center" v-if="item.name === '移送处理人员数量'" label="移送处理人员数量" prop="sumYsclr" />
        </div>
        <el-table-column width="1"></el-table-column>
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
  </div>
</template>

<script>
import { selectWtzgAuditResultsStatistics } from '@/oapi/audit/manage' 
import { formatDate } from '@/utils/index'
import filterSearch from '@/components/filterSearch'
import filterTable from '@/components/filterTable'
import { searchTableMixis } from '@/mixis/index'

export default {
  name: 'gcxmzjzjb',
  components: { filterSearch, filterTable },
  mixins: [searchTableMixis],
  data() {
    return {
      list: [],
      listLoading: false,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        projectName: '',
        pageNumber: 1,
        pageSize: 20,
      },
      filedAll: [
        { name: '问题个数' },
        { name: '审计发现问题金额' }, 
        { name: '直接经济成果数' }, 
        { name: '移送处理人员数量' }, 
      ], //所有表格项
      filedNow: [], //当前表格项
      searchAll: this.getFiled(), //所有搜索项
      searchNow: [], //当前所有搜索项
      searchItem: [], //可见搜索项
      localKey: 'oilAudit-manage-sjcgtj-search',
      tableKey: 'oilAudit-manage-sjcgtj-list',
      searchMore: true,
    }
  },
  created() {
    this.fetchData()
    this.initTable()
    this.searchNow = this.getFiled()
    console.log('searchNow', this.searchNow)
    this.searchItem = this.searchNow.slice(0, 4)
    this.initSearch()
  },
  methods: {
    getFiled () {
      return [{ name: '项目名称', key: 'projectName' }]
    },
    resetQueryForm() {
      this.queryForm = {
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
    async fetchData() {
      this.listLoading = true
      const res = await selectWtzgAuditResultsStatistics({ 
        ...this.queryForm,
      })
      this.listLoading = false 
      this.list = res ? res.data.pageInfo.tlist : []
      this.total = res.data.pageInfo.totalRecord || 0
    }
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
