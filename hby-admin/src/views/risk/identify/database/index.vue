<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <!-- <CompanyTree
          :isSelectNode="false"
          ref="leftlist"
          @select="handleTreeSelect"
        /> -->
        <DepTree @select="handleTreeSelect" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="24">
              <el-form
                ref="form"
                checkable
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-select
                    v-model="queryForm.year"
                    placeholder="年份"
                    v-if="item.name === '年份'"
                  >
                    <el-option
                      v-for="(year, index) in yearList"
                      :key="index"
                      :label="year"
                      :value="year"
                    />
                  </el-select>
                  <el-input
                    v-model="queryForm.risknumber"
                    clearable
                    placeholder="风险编号"
                    v-if="item.name === '风险编号'"
                  />
                  <el-input
                    v-model="queryForm.riskname"
                    clearable
                    placeholder="风险名称"
                    v-if="item.name === '风险名称'"
                  />
                  <el-input
                    v-model="queryForm.unitname"
                    clearable
                    placeholder="公司名称"
                    v-if="item.name === '公司名称'"
                  />
                  <el-select
                    v-model="queryForm.riskcatname"
                    v-if="item.name === '风险领域'"
                    placeholder="请选择风险领域"
                    clearable
                  >
                    <el-option
                      v-for="(item, index) in selectList"
                      :key="index"
                      :label="item"
                      :value="item"
                    />
                    <!-- <el-option label="市场风险" value="市场风险" />
                    <el-option label="财务风险" value="财务风险" />
                    <el-option label="运营风险" value="运营风险" />
                    <el-option label="法律合规风险" value="法律合规风险" />
                    <el-option label="安全环保风险" value="安全环保风险" />
                    <el-option label="工程建设风险" value="工程建设风险" /> -->
                  </el-select>
                  <el-select
                    v-model="queryForm.riskcatidname"
                    v-if="item.name === '风险类型'"
                    placeholder="请选择风险类型"
                    clearable
                  >
                    <el-option
                      v-for="(item, index) in selectList1"
                      :key="index"
                      :label="item"
                      :value="item"
                    />
                    <!-- <el-option label="市场风险" value="市场风险" />
                    <el-option label="财务风险" value="财务风险" />
                    <el-option label="运营风险" value="运营风险" />
                    <el-option label="法律合规风险" value="法律合规风险" />
                    <el-option label="安全环保风险" value="安全环保风险" />
                    <el-option label="工程建设风险" value="工程建设风险" /> -->
                  </el-select>
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
                  <el-button
                    native-type="submit"
                    type="primary"
                    @click="resetQueryForm"
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
                <el-form-item>
                  <span
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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

        <el-card shadow="never" class="secondCard">
          <vab-query-form-right-panel class="option-row">
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
            <el-button type="success" @click="handleExport">导出</el-button>
          </vab-query-form-right-panel>

          <el-table
            v-loading="listLoading"
            :data="list"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              disabled
            ></el-table-column>
            <el-table-column
              align="center"
              width="140"
              label="风险编号"
              prop="risknumber"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.risknumber }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="风险名称"
                prop="riskname"
                show-overflow-tooltip
                v-if="item.name === '风险名称'"
              />
              <el-table-column
                align="center"
                label="风险描述"
                prop="riskdes"
                show-overflow-tooltip
                v-if="item.name === '风险描述'"
              />
              <!-- <el-table-column
                align="center"
                label="机构"
                prop="unit"
                v-if="item.name === '机构'"
              /> -->
              <el-table-column
                align="center"
                label="所属部门"
                prop="superiorDepartment"
                show-overflow-tooltip
                v-if="item.name === '所属部门'"
              />
              <el-table-column
                align="center"
                label="所属科室"
                prop="linkDeptName"
                show-overflow-tooltip
                v-if="item.name === '所属科室'"
              />
              <el-table-column
                align="center"
                label="风险领域"
                prop="riskcatname"
                show-overflow-tooltip
                v-if="item.name === '风险领域'"
              />
              <el-table-column
                align="center"
                label="风险类型"
                prop="riskcatidname"
                show-overflow-tooltip
                v-if="item.name === '风险类型'"
              />
              <el-table-column
                align="center"
                label="公司名称"
                prop="unitname"
                show-overflow-tooltip
                v-if="item.name === '公司名称'"
              />
              <el-table-column
                align="center"
                label="创建人"
                prop="staffname"
                show-overflow-tooltip
                v-if="item.name === '创建人'"
              />
              <el-table-column
                align="center"
                label="创建时间"
                prop="riskcreatedt"
                show-overflow-tooltip
                :formatter="formatDate"
                v-if="item.name === '创建时间'"
              />
            </div>
            <el-table-column width="1"></el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          class="pager"
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <RiskRead ref="read" />
    <RiskEdit ref="edit" :treeId="treeId" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getCreationDataExport,
    getRiskLedger,
    exportRiskLedger,
    getCreationTreeData,
  } from '@/api/risk'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import TypeTree from '@/views/risk/identify/components/TypeTree.vue'
  import RiskRead from '../creation/components/RiskRead.vue'
  import RiskEdit from '../creation/components/RiskEdit.vue'
  import { formatDay } from '@/utils/index'
  import { baseURL } from '@/config/index'
  import store from '@/store'
  // import CompanyTree from '@/views/setting/org/components/CompanyTree'
  import DepTree from '@/views/risk/GroupRiskDatabase/companyDep.vue'
  import { downloadFile } from '@/utils/otherUtils'
  export default {
    name: 'Fillin',
    components: {
      TypeTree,
      RiskRead,
      RiskEdit,
      filterSearch,
      filterTable,
      // CompanyTree,
      DepTree,
    },
    data() {
      return {
        treeId: '',
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          riskcode: '',
          riskname: '',
          riskcatidname: '',
          riskcatname: '',
          orgid: '',
          pageNo: 1,
          pageSize: 20,
          unitname: '',
          year: '',
        },
        exportArr: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-identify-database-search',
        tableKey: 'risk-identify-database-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '风险名称' },
          { name: '风险描述' },
          { name: '创建时间' },
          { name: '风险领域' },
          { name: '风险类型' },
          { name: '公司名称' },
          { name: '所属部门' },
          { name: '所属科室' },
          { name: '创建人' },
        ], //所有表格项
        filedNow: [],
        selectList: [],
        selectList1: [],
        yearList: [],
      }
    },
    created() {
      // 初始化年份列表
      const currentYear = new Date().getFullYear()
      this.yearList = [currentYear - 2, currentYear - 1, currentYear]
      this.queryForm.riskcatname = this.$route.query.name
      this.queryForm.unitname = this.$route.query.unitname
      this.queryForm.year = this.$route.query.year
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.getSelectData()
    },
    mounted() {},
    methods: {
      async getSelectData() {
        const { data } = await getCreationTreeData()
        this.selectList = []
        this.selectList1 = []

        // 将第一层children的riskcatname添加到selectList
        data.tree[0].children.map((item) => {
          this.selectList.push(item.riskcatname)
        })

        // 递归函数，用于遍历所有层级的children并添加riskcatname到selectList1
        const getAllRiskcatnames = (items) => {
          if (!items || !items.length) return

          items.forEach((item) => {
            // 将当前项的riskcatname添加到selectList1
            if (item.riskcatname) {
              this.selectList1.push(item.riskcatname)
            }

            // 如果有子项，递归处理
            if (item.children && item.children.length > 0) {
              getAllRiskcatnames(item.children)
            }
          })
        }

        // 从第一层children开始递归
        getAllRiskcatnames(data.tree[0].children)
      },
      // treeData(id) {
      //   this.queryForm.riskcatid = id
      //   this.fetchData()
      // },
      handleTreeSelect(org) {
        console.log(org.id)
        this.queryForm.orgid = org.id
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData(this.queryForm.riskcatid)
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleRead(row) {
        this.$refs['edit'].showEdit(row, this.queryForm.riskcatid, true)
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData(this.queryForm.riskcatid)
      },
      async fetchData(id) {
        this.listLoading = true
        this.queryForm.riskcatid =
          typeof id == 'string' || typeof id == 'number'
            ? id
            : this.queryForm.riskcatid
        const {
          data: {
            pageBean: { list, total },
          },
        } = await getRiskLedger(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleSelectionChange(data) {
        this.exportArr = []
        data.forEach((item) => {
          this.exportArr.push(item.riskid)
        })
      },
      async handleExport() {
        console.log(1231231231)
        let params = { ...this.queryForm }
        console.log('🚀 ~ handleExport ~ params:', params)
        params.ids = this.exportArr.join(',')
        delete params.pageNo
        delete params.pageSize

        const res = await exportRiskLedger(params)
        downloadFile(res, '风险台账.xlsx')
      },
      // 重置筛选项
      resetQueryForm() {
        this.queryForm = {
          riskcatid: this.queryForm.riskcatid,
          riskcatname: '',
          riskcatidname: '',
          risknumber: '',
          riskname: '',
          pageNumber: 1,
          pageSize: 20,
          unitname: '',
        }
        this.fetchData()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '年份', key: 'year' },
          { name: '风险编号', key: 'risknumber' },
          { name: '风险名称', key: 'riskname' },
          { name: '风险领域', key: 'riskcatname' },
          { name: '风险类型', key: 'riskcatidname' },
          { name: '公司名称', key: 'unitname' },
          { name: '创建人', key: 'riskname' },
        ]
        return fields
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = ''
              }
            }
          })

          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      // 动态表格开始
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      showMore() {
        this.searchMore = !this.searchMore
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 300px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .lr-layout > .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
