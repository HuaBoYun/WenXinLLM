<template>
  <div class="system-log-container">
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
        <el-button
          type="success"
          @click="handleEdit('add')"
          v-if="!hasAuth('NKQXBZadd')"
        >
          新建
        </el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column align="center" label="缺陷级别" prop="bugcrilevel">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleShow('detail', row)"
              v-if="!hasAuth('NKQXBZdetail')"
            >
              {{ row.bugcrilevel }}
            </el-button>
            <div v-else>{{ row.bugcrilevel }}</div>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="定义"
            prop="bugcridefine"
            show-overflow-tooltip
            v-if="item.name === '定义'"
          />
          <el-table-column
            align="center"
            label="定量标准"
            prop="bugcristability"
            show-overflow-tooltip
            v-if="item.name === '定量标准'"
          />
          <el-table-column
            align="center"
            label="定性标准"
            prop="bugcriration"
            show-overflow-tooltip
            v-if="item.name === '定性标准'"
          />
        </div>

        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit('edit', row)"
              v-if="!hasAuth('NKQXBZedit')"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="!hasAuth('NKQXBZdelete')"
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
    <StandardEdit ref="edit" @fetch-data="fetchData" />
    <StandardShow ref="show" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { defQuexianList, defQuexianDel } from '@/api/internal/defectStandard'
  import { doDelete } from '@/api/table'
  import StandardEdit from './components/StandardEdit'
  import StandardShow from './components/StandardShow'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'
  import { hasAuth } from '@/utils'

  export default {
    name: 'StandardList',
    components: { StandardEdit, filterSearch, filterTable, StandardShow },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          orgid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '定义' },
          { name: '定量标准' },
          { name: '定性标准' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-intemalTools-defectStandard-search',
        tableKey: 'workbench-intemalTools-defectStandard-list',
        searchMore: true,
        sortFields: '',
        sortFlag: 'asc',
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
        return []
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
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.queryForm.orgid = userInfo.linkOrg.orgid
        this.listLoading = true
        const {
          data: {
            pageBean: { records: list, total },
          },
        } = await defQuexianList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleEdit(title, row) {
        this.$refs['edit'].showEdit(row, title)
      },
      handleShow(title, row) {
        this.$refs['show'].showEdit(row, title)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await defQuexianDel({ ids: row.bugcriid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
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
