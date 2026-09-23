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
                v-model="queryForm.bookName"
                clearable
                placeholder="名称"
                v-if="item.name === '名称'"
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
          <el-button type="success" @click="handleSave">选择</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        ref="multipleTable"
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @select-all="selectAll"
        @select="handleSelectionChange"
        @current-change="handleRowChange"
      >
        <!-- @selection-change="handleSelectionChange" -->
        <el-table-column type="selection" />
        <el-table-column width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column align="center" v-if="item.name === 'ID'" label="ID" prop="bookId" />
          <el-table-column align="center" v-if="item.name === '名称'" label="名称" prop="bookName" />
          <el-table-column align="center" v-if="item.name === '公司'" label="公司" prop="orgName" />
          <el-table-column align="center" v-if="item.name === '年份'" label="年份" prop="bookYear" />
          <el-table-column align="center" v-if="item.name === '描述'" label="描述" prop="boodDesc" />
        </div>
        <el-table-column width="1" />
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <LcdyEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getList,
    checkBook,
    getSelectedBookInfo,
  } from '@/api/workbench/accountManage'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Consult',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, LcdyEdit },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        curUserSelected: {},
        curSelected: {},
        queryForm: {
          bookName: undefined,
          pageNo: 1,
          pageSize: 20,
        },
        // 筛选列表配置
        filedAll: [
          { name: 'ID' },
          { name: '名称' },
          { name: '公司' },
          { name: '年份' },
          { name: '描述' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-company-accountManage-search',
        tableKey: 'workbench-company-accountManage-list',
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
          { name: '名称', key: 'bookName' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          bookName: undefined,
          pageNo: 1,
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        await this.getSelectedBook()
        this.listLoading = false
      },
      async getSelectedBook() {
        const { data } = await getSelectedBookInfo()
        if (data) {
          const row = this.list.find((x) => x.bookId === data.bookId)
          this.curUserSelected = row
          localStorage.setItem('bookInfo', JSON.stringify(data))
          this.$nextTick(() => {
            this.singleSelect(row)
          })
        }
      },
      selectAll() {
        this.singleSelect(this.curUserSelected)
      },
      handleSelectionChange(selection, row) {
        this.singleSelect(row)
      },
      handleRowChange(row) {
        this.singleSelect(row)
      },
      singleSelect(row) {
        this.curSelected = row
        this.$refs.multipleTable.clearSelection()
        this.$refs.multipleTable.toggleRowSelection(row)
      },
      async handleSave() {
        if (!this.curSelected.bookId)
          return this.$message({ type: 'error', message: '请选择要操作的账套' })
        if (this.curSelected.bookId === this.curUserSelected?.bookId)
          return this.$message({ type: 'error', message: '无更改' })

        this.listLoading = true
        const { code } = await checkBook({
          bookId: this.curSelected.bookId,
        })
        if (code === 1) {
          this.$message({
            type: 'success',
            message: '操作成功!',
          })
          await this.getSelectedBook()
        } else {
          this.$message({
            type: 'error',
            message: '操作失败!',
          })
        }
        this.listLoading = false
      },
    },
  }
</script>

<style scoped>
  .system-log-container >>> .el-table__header-wrapper.el-checkbox {
    display: none !important;
  }

  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
