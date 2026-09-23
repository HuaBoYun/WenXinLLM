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
          @click="handleEdit(null)"
          v-if="!hasAuth('NKDJWHadd')"
        >
          新建
        </el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column
          align="center"
          label="级别名称"
          prop="levelname"
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.levelname }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="级别上限"
            prop="levelupper"
            v-if="item.name === '级别上限'"
          />
          <el-table-column
            align="center"
            label="级别下限"
            prop="levellower"
            v-if="item.name === '级别下限'"
          />
          <el-table-column
            align="center"
            label="级别说明"
            prop="leveldes"
            v-if="item.name === '级别说明'"
          />
          <el-table-column
            align="center"
            label="修改时间"
            v-if="item.name === '修改时间'"
            sortable="custom"
          >
            <template #default="{ row }">
              {{ row.modifieddate.substring(0, 10) }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="!hasAuth('NKDJWHedit')"
              :disabled="createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="!hasAuth('NKDJWHdelete')"
              :disabled="createId != row.createstaffid"
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
    <LevelEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getList } from '@/api/systemLog'
  import {
    getLevelList,
    levelDelete,
    getLevelListById,
  } from '@/api/internal/levelMaintenance'
  import { doDelete } from '@/api/table'
  import LevelEdit from './components/LevelEdit'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'
  import { hasAuth } from '@/utils'

  export default {
    name: 'StandardList',
    components: { LevelEdit, filterSearch, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '级别上限' },
          { name: '级别下限' },
          { name: '级别说明' },
          { name: '修改时间' },
          // { name: '最新维护人' },
          // { name: '建立时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-intemalTools-levelMaintenance-search',
        tableKey: 'workbench-intemalTools-levelMaintenance-list',
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
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getLevelList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        // const res = await getLevelList(this.queryForm)
        //
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleEdit(row) {
        // if (row) {
        //   if (this.createId != row.createstaffid) {
        //     return this.$message.error('只有创建人可以操作')
        //   }
        // }
        this.$refs['edit'].showEdit(row)
      },
      handleCopy(row) {},
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          //
          const { msg } = await levelDelete({ asslevid: row.asslevid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, true)
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
