<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form class="margin-b0">
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
                v-model="queryForm.templenumber"
                clearable
                placeholder="模板编号"
                v-if="item.name === '模板编号'"
              />
              <el-input
                v-model="queryForm.templename"
                clearable
                placeholder="模板名称"
                v-if="item.name === '模板名称'"
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
          @click="handleEdit()"
          v-if="!hasAuth('NKPJMBadd')"
        >
          新建
        </el-button>
        <!-- <el-button type="primary" @click="handleExport">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
        @sort-change="sortChange"
      >
        <!-- <el-table-column
          align="center"
          prop="data"
          width="120"
          type="selection"
        /> -->

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="评价模板编号"
            v-if="item.name === '评价模板编号'"
            sortable="custom"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="handleView(row)"
                v-if="!hasAuth('NKPJMBdetail')"
              >
                {{ row.templenumber }}
              </el-button>
              <div v-else>{{ row.templenumber }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="评价模板名称"
            prop="templename"
            v-if="item.name === '评价模板名称'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="评价模板说明"
            prop="templedes"
            v-if="item.name === '评价模板说明'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="适用机构"
            prop="reorgText"
            v-if="item.name === '适用机构'"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            align="center"
            label="最新维护人"
            prop="realname"
            v-if="item.name === '最新维护人'"
          /> -->
          <el-table-column
            align="center"
            label="建立时间"
            v-if="item.name === '建立时间'"
            sortable="custom"
          >
            <template #default="{ row }">
              {{ row.modifydatetime.substring(0, 10) }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.numbers > 0 || createId != row.staffid"
              v-if="!hasAuth('NKPJMBedit')"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleCopy(row)"
              v-if="!hasAuth('NKPJMBcopy')"
            >
              复制
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="row.numbers > 0 || createId != row.staffid"
              v-if="!hasAuth('NKPJMBdelete')"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pager"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <EvaluationEdit ref="edit" @fetch-data="fetchData" />
    <EvaluationView ref="view" @fetch-data="fetchData" />
    <CopyEvaluation ref="copy" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { defTmplList, defTmplDel } from '@/api/internal/evaluationTemplate'
  import EvaluationEdit from './components/EvaluationEdit'
  import EvaluationView from './components/EvaluationView'
  import CopyEvaluation from './components/CopyEvaluation'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'
  import { hasAuth } from '@/utils'

  export default {
    name: 'StandardList',
    components: {
      EvaluationEdit,
      filterSearch,
      filterTable,
      CopyEvaluation,
      EvaluationView,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          templename: '',
          templenumber: '',
          choiceSearch: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '评价模板编号' },
          { name: '评价模板名称' },
          { name: '评价模板说明' },
          { name: '适用机构' },
          // { name: '最新维护人' },
          { name: '建立时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-intemalTools-evaluationTemplate-search',
        tableKey: 'workbench-intemalTools-evaluationTemplate-list',
        searchMore: true,
        sortFields: '',
        sortFlag: 'asc',
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    async mounted() {},
    methods: {
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
          this.sortFlag = 'asc'
        } else if (order === 'descending') {
          this.sortFlag = 'desc'
        } else {
          this.sortFlag = ''
        }
        await this.fetchData()
      },
      getFiled() {
        return [
          { name: '模板编号', key: 'bugnumber' },
          { name: '模板名称', key: 'bugcriid' },
        ]
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
                this.queryForm[x.key] = null
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
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
          data: {
            pageBean: { records, total },
          },
        } = await defTmplList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = records

        this.total = total
        this.listLoading = false
      },
      handleEdit(row) {
        // if (row) {
        //   if (this.createId != row.staffid) {
        //     return this.$message.error('只有创建人可以操作')
        //   }
        // }
        this.$refs['edit'].showEdit(row)
      },
      handleCopy(row) {
        this.$refs['copy'].showEdit(row)
      },
      handleDelete(row) {
        // if (this.createId != row.staffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await defTmplDel({ tmplId: row.asstemid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleSelectionChange() {},
      handleView(row) {
        this.$refs['view'].showEdit(row)
      },
      resetSearch() {
        this.queryForm = {
          templename: '',
          templenumber: '',
          choiceSearch: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }

  .pager {
    margin-bottom: 20px !important;
  }
</style>
