<template>
  <div class="system-log-container">
    <vab-query-form>
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
              <el-input
                v-model="queryForm.xmname"
                clearable
                placeholder="论文名称"
                v-if="item.name === '论文名称'"
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
      <vab-query-form-right-panel :span="24" style="margin-bottom: 10px">
        <el-button type="primary" @click="returnLw">退回</el-button>
        <el-button type="success" @click="handleExport()">导出</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column align="center" label="序号" type="index" width="50" />
        <el-table-column align="center" label="论文名称" prop="papername">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.papername }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="填报单位"
          prop="tbrgname"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="撰写人" prop="zxrname" />
        <el-table-column
          align="center"
          label="备注"
          prop="remarks"
          show-overflow-tooltip
        />
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
    <Edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getLwtzList,
    deleteLwsb,
    lwsbXfry,
    lwtzExport,
    returnThlw,
  } from '@/oapi/audit/lwpy'
  import Edit from '@/views/oilAudit/lwpy/components/lwsbEdit.vue'
  import { parseTime } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'lwtz',
    components: { Edit, filterSearch, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          xmname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '填报单位' },
          { name: '撰写人' },
          { name: '备注' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-lwpy-lwtz-search',
        tableKey: 'oilAudit-lwpy-lwtz-list',
        searchMore: true,
        projectNoticeId: '',
        multipleSelection: [],
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
        return [{ name: '论文名称', key: 'xmname' }]
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      resetQueryForm() {
        this.queryForm = {
          xmname: '',
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
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getLwtzList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.select = []
        this.listLoading = false
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
      async handleExport() {
        const data = await lwtzExport({xmname: this.queryForm.xmname})
        let fileName = '论文台账'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      async returnLw() {
        if (this.multipleSelection.length === 0) {
          return this.$message.error('请选择数据')
        }
        let arr = this.multipleSelection
          .map((item) => {
            return item.perid
          })
          .join(',')
        const { code } = await returnThlw({ perid: arr })
        if (code != 1) return
        this.$message.success('退回成功')
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
