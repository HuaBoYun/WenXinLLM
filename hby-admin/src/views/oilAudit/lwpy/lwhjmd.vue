<template>
  <div class="system-log-container">
    <!-- <vab-query-form>
      <el-card shadow="never">
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
      </el-card>
    </vab-query-form> -->

    <el-card shadow="never">
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        :span-method="arraySpanMethod"
        :row-class-name="tableRowClassName"
      >
        <el-table-column align="center" label="论文题目" prop="papername">
          <template #default="{ row }">
            <div v-if="row.title">{{ row.title }}({{ row.num }}篇)</div>
            <div v-else>
              <el-button type="text" @click="handleDetail(row)">
                {{ row.papername }}
              </el-button>
            </div>
          </template>
        </el-table-column>

        <el-table-column align="center" label="单位" prop="tbrgname" />
        <el-table-column align="center" label="作者" prop="zxrname" />
      </el-table>
    </el-card>
    <Edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getLwhjmdList } from '@/oapi/audit/lwpy'
  import Edit from '@/views/oilAudit/lwpy/components/lwsbEdit.vue'
  import { parseTime } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'lwhjmd',
    components: { Edit, filterSearch, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        queryForm: {
          noticeTitle: '',
        },
        filedAll: [], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-lwpy-lwhjmd-search',
        tableKey: 'oilAudit-lwpy-lwhjmd-list',
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
        return [{ name: '标题', key: 'noticeTitle' }]
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      resetQueryForm() {
        this.queryForm = {
          noticeTitle: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
        // this.listLoading = true
        let res = await getLwhjmdList(this.queryForm)
        this.list.push({
          title: '一等奖',
          num: res.data.list1.length,
          ...res.data.list1[0],
        })
        this.list = this.list.concat(res.data.list1)
        this.list.push({
          title: '二等奖',
          num: res.data.list2.length,
          ...res.data.list2[0],
        })
        this.list = this.list.concat(res.data.list2)
        this.list.push({
          title: '三等奖',
          num: res.data.list3.length,
          ...res.data.list3[0],
        })
        this.list = this.list.concat(res.data.list3)
        // this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit('detail', row)
      },
      async handleEdit(row) {
        await this.$refs['edit'].showEdit('edit', row)
      },
      arraySpanMethod({ row, column, rowIndex, columnIndex }) {
        if (row.title) {
          let len = Object.keys(row).length
          if (columnIndex === 0) {
            return [1, len]
          } else {
            return [0, 0]
          }
        }
      },
      tableRowClassName({ row, rowIndex }) {
        if (row.title) {
          return 'row-color'
        }
        return ''
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .margin-b0 {
    margin-bottom: 0;
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>

<style>
  .el-table .row-color {
    background: #f6f8f9 !important;
  }
</style>
