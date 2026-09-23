<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <type-tree
          :url="`/risk/risk_fxsjk_left`"
          @fetch-data="queryData"
          @handleNodeClick="handleNodeClick"
        />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel>
              <el-form
                ref="form"
                checkable
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item
                  v-for="(item, index) in searchItem"
                  :key="index"
                  :prop="item.key"
                >
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
                    @click="fetchData('reset')"
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
            </vab-query-form-top-panel>
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
          </vab-query-form-right-panel>

          <el-table v-loading="listLoading" :data="list">
            <el-table-column align="center" label="风险编号" prop="risknumber">
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
                label="机构"
                prop="unit"
                v-if="item.name === '机构'"
              />
              <el-table-column
                align="center"
                label="当前版本"
                prop="version"
                show-overflow-tooltip
                v-if="item.name === '当前版本'"
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
            <el-table-column align="center" label="操作">
              <template #default="{ row }">
                <el-button type="text" @click="handleHistory(row)">
                  查看历史版本
                </el-button>
              </template>
            </el-table-column>
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
    <History ref="history" />
    <RiskRead ref="read" />
    <RiskEdit ref="edit" :treeId="treeId" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getCreationVersionList } from '@/api/risk'
  import TypeTree from '@/views/risk/identify/components/TypeTree.vue'
  import History from './components/History.vue'
  import RiskRead from '../creation/components/RiskRead.vue'
  import RiskEdit from '../creation/components/RiskEdit.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { formatDay } from '@/utils/index'

  export default {
    name: 'Fillin',
    components: {
      TypeTree,
      History,
      RiskRead,
      RiskEdit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        treeId: '',
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          risknumber: '',
          riskname: '',
          riskcatid: '',
          iscurrentversion: 1,
          pageNo: 1,
          pageSize: 20,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-identify-version-search',
        tableKey: 'risk-identify-version-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '风险名称' },
          { name: '机构' },
          { name: '当前版本' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [],
      }
    },
    created() {
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '风险编号', key: 'risknumber' },
          { name: '风险名称', key: 'riskname' },
        ]
        return fields
      },
      /**
       * @description: 初始化搜索
       * @return {*}
       */
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
      /**
       * @description: 选择左侧树 回调
       * @return {*}
       */
      handleNodeClick(data) {
        this.queryForm.riskcatid = data.id
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['edit'].showEdit(row, this.queryForm.riskcatid, true)
      },
      /**
       * @description: 分页 初始化
       * @return {*}
       */
      queryData(type) {
        this.queryForm.pageNo = 1
        this.fetchData(type)
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = true
        if (type && type == 'reset') this.$refs['form'].resetFields()
        this.queryForm.riskcatid =
          typeof type == 'number' ? type : this.queryForm.riskcatid
        const {
          data: {
            pageBean: { list, total },
          },
        } = await getCreationVersionList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开历史
       * @return {*}
       */
      handleHistory(row) {
        this.$refs['history'].showHistory(row)
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
    width: 200px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
