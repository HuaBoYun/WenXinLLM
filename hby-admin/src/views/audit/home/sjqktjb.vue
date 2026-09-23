<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app" class="system-log-container page">
    <div style="margin-bottom: 20px">
      <el-card shadow="never">
        <el-form ref="form" :inline="true" label-width="0" :model="queryForm">
          <el-form-item v-for="(item, index) in searchItem" :key="index">
            <el-date-picker
              v-model="queryForm.year"
              type="year"
              format="yyyy"
              value-format="yyyy"
              placeholder="选择年份"
              v-if="item.name === '年份'"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" type="primary" @click="fetchData">
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="resetSearch">重置</el-button>
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
      </el-card>

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
            <el-button type="success" @click="handleAdd">新建</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          ref="filterTable"
          :data="projectSituationTable"
          v-loading="listLoading"
        >
          <el-table-column width="1" />
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              label="内审机构名称"
              v-if="item.name === '内审机构名称'"
              align="center"
              prop="situationName"
            />
            <!-- <el-table-column label="年份" prop="situationName" align="center" /> -->
            <el-table-column
              label="年份"
              v-if="item.name === '年份'"
              prop="situationYear"
              align="center"
            />
            <el-table-column
              label="创建人名称"
              v-if="item.name === '创建人名称'"
              prop="createStaffName"
              align="center"
            />
            <el-table-column
              label="创建时间"
              v-if="item.name === '创建时间'"
              prop="createTime"
              align="center"
            />
          </div>
          <el-table-column align="center" label="操作" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-button type="text" @click="handleEdit(scope.row, '修改')">
                修改
              </el-button>

              <el-button type="text" @click="handleDelete(scope.row)">
                删除
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
      <Edit ref="edit" @reload="fetchData"></Edit>
    </div>
  </div>
</template>

<script>
  import { getSJQKTJBList, sjqktjbDelete } from '@/api/audit/sjfx'
  import { formatDate } from '@/utils'
  import Edit from './components/sjqktjbEdit.vue'
  import * as dayjs from 'dayjs'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, Edit },
    data() {
      return {
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          year: '',
        },
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        activeYear: 2021,
        yearInquiryData: [],
        entryNumberData: [],
        projectTypeData: [],
        projectSituationTable: [],
        listLoading: false,
        // 筛选列表配置
        filedAll: [
          { name: '内审机构名称' },
          { name: '年份' },
          { name: '创建人名称' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-analyse-sjqktjb-search',
        tableKey: 'audit-analyse-sjqktjb-list',
        searchMore: true,
      }
    },
    mounted() {},
    created() {
      this.getProjectSituation()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [{ name: '年份', key: 'year' }]
      },
      getProjectSituation() {
        this.listLoading = true
        getSJQKTJBList(this.queryForm).then((res) => {
          this.projectSituationTable = res.data.pageInfo.tlist.map((v) => {
            if (v.situationYear) {
              v.situationYear = dayjs(v.situationYear).format('YYYY')
            }
            return v
          })
          this.total = res.data.pageInfo.totalRecord
          this.listLoading = false
        })
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getProjectSituation()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getProjectSituation()
      },

      getProjectType() {
        projectType(this.queryForm).then((res) => {
          this.projectTypeData = res.data.data
        })
      },

      fetchData() {
        this.getProjectSituation()
      },
      resetSearch() {
        this.queryForm.year = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.getProjectSituation()
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].show('新增', {})
      },
      handleEdit(row) {
        this.$refs['edit'].show('修改', row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await sjqktjbDelete({ situationId: row.id })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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

  h5 {
    font-size: 18px;
    margin: 2px;
    color: #333;
  }
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .table-title {
    cursor: pointer;
  }

  .table th {
    position: relative;
  }

  .table-filter {
    position: absolute;
    border: 1px solid gainsboro;
    padding: 5px;
    left: 0;
    right: 0;
    top: 40px;
    background: white;
    min-width: 160px;
  }

  .table-filter input {
    padding: 5px;
    font-size: 14px;
    margin-right: 5px;
  }

  .table-filter .form-check {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 14px;
    font-weight: 400;
    padding: 5px;
  }

  .table-filter button {
    font-size: 12px;
    padding: 2px 10px;
  }

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .select-year {
    display: flex;
    background: aliceblue;
    padding: 5px;
  }

  .select-year > div {
    margin-right: 10px;
    padding: 2px 5px;
    cursor: pointer;
  }

  .select-year .active {
    background: #ffaf0f;
    border-radius: 20px;
    color: white;
  }
  h5 {
    margin: 0 0 10px 0;
    font-size: 17px;
  }
</style>
