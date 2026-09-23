<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
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
                v-model="queryForm.jhmc"
                clearable
                v-if="item.name === '计划名称'"
                placeholder="计划名称"
              />
              <el-date-picker
                v-model="queryForm.sj"
                placeholder="时间"
                type="datetime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间'"
              />
              <el-input
                v-model="queryForm.cjr"
                clearable
                v-if="item.name === '编制人'"
                placeholder="编制人"
              />
              <el-date-picker
                v-model="queryForm.cjsj"
                placeholder="编制时间"
                type="datetime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '编制时间'"
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
        <el-button type="success" @click="handleAdd(null)">新增</el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '计划名称'"
            align="center"
            :key="index"
            label="计划名称"
            prop="jhmc"
            >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.jhmc }}
              </el-button>
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name === '时间'"
            align="center"
            label="时间"
            prop="sj"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '编制人'"
            align="center"
            label="编制人"
            prop="cjr"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '编制时间'"
            align="center"
            label="编制时间"
            prop="cjsj"
            show-overflow-tooltip
          />
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
    <jhcgEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { jhgljhcgList, jhgljhcgDetail, jhgljhcgDelete } from '@/api/monitor/question'
  // import DraftInfo from "./components/DraftInfo";
  import jhcgEdit from './components/jhcgEdit'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  export default {
    name: 'jhlxjymxb',
    components: {
      // DraftInfo,
      jhcgEdit,
      filterSearch,
      filterTable,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          jhmc: '',
          cjr: '',
          cjsj: '',
          sj: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '计划名称' },
          { name: '时间' },
          { name: '编制人' },
          { name: '编制时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-fgldhz-search',
        tableKey: 'oilAudit-jhlx-fgldhz-list',
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
          { name: '计划名称', key: 'jhmc' },
          { name: '时间', key: 'sj' },
          { name: '编制人', key: 'cjr' },
          { name: '编制时间', key: 'cjsj' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          jhmc: '',
          cjr: '',
          cjsj: '',
          sj: '',
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await jhgljhcgList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null)
      },
      async handleDetail(row) {
        const data = await jhgljhcgDetail({ jhcgid: row.jhcgid })
        await this.$refs['edit'].showEdit(data.data, true)
      },
      async handleEdit(row, disabled) {
        const data = await jhgljhcgDetail({ jhcgid: row.jhcgid })
        await this.$refs['edit'].showEdit(data.data)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await jhgljhcgDelete({
            ids: row.jhcgid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
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
</style>
