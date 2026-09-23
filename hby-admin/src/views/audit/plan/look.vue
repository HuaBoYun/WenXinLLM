<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.planName"
                clearable
                placeholder="计划名称"
                v-if="item.name === '计划名称'"
              />
              <el-input
                v-model="queryForm.planYear"
                clearable
                placeholder="计划年度"
                v-if="item.name === '计划年度'"
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
        </vab-query-form-left-panel>
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
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="计划年度"
          prop="palnyear"
          width="100"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="计划名称"
            prop="planname"
            v-if="item.name === '计划名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row, true)">
                {{ row.planname }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="项目总数"
            prop="totalItem"
            width="100"
            v-if="item.name === '项目总数'"
          />
          <el-table-column
            align="center"
            label="未实施项目"
            prop="unenforcedItem"
            v-if="item.name === '未实施项目'"
          />
          <el-table-column
            align="center"
            label="进行项目"
            prop="condectItem"
            width="100"
            v-if="item.name === '进行项目'"
          />
          <el-table-column
            align="center"
            label="完成项目"
            prop="completeItem"
            v-if="item.name === '完成项目'" />
        </div>
        <el-table-column width="1" />
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
    <LookEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { auditPlanListLook } from '@/api/audit/plan'
  import LookEdit from './components/LookEdit'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, LookEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planName: '',
          planYear: '',
          pageNumber: 1,
          pageSize: 20,
        },
        // 筛选列表配置
        filedAll: [
          { name: '计划名称' },
          { name: '项目总数' },
          { name: '未实施项目' },
          { name: '进行项目' },
          { name: '完成项目' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-project-look-search',
        tableKey: 'audit-project-look-list',
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
          { name: '计划名称', key: 'planName' },
          { name: '计划年度', key: 'planYear' },
        ]
      },
        /**
       * @description  重置筛选，把筛选条件清空
       * @param {*}  
        * @return {*}
       */    
      resetQueryForm() {
        this.queryForm = {
          planName: '',
          planYear: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
        /**
       * @description  重置按钮，点击触发的函数
       * @param {*}  
        * @return {*}
       */    
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
        /**
       * @description  分页，选择每页几条数据，查询每页多少条数据
       * @param {*}  
        * @return {*}
       */    
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
       * @description  分页，选择页码，查询第几页的数据
       * @param {*}  
        * @return {*}
       */    
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
        /**
       * @description  查询按钮，回到第一页，查询数据
       * @param {*}  
        * @return {*}
       */    
       queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
        /**
       * @description  查询接口，条件查询
       * @param {*}  
        * @return {*}
       */    
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        let result = await auditPlanListLook(this.queryForm)
        this.list = result.data.pageInfo.tlist
        this.total = result.data.pageInfo.totalRecord
        this.listLoading = false
      },
        /**
       * @description  新建按钮触发，唤起新建弹框
       * @param {*}  
        * @return {*}
       */    
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
        /**
       * @description  编辑按钮触发，唤起新建弹框
       * @param {*}  
        * @return {*}
       */    
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
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