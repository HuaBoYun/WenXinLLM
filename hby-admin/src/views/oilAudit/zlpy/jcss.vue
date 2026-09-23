<template>
  <!-- 5奖惩实施 -->
  <div class="system-log-container">
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
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="审计项目名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '审计项目名称'"
              ></el-input>
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
              <el-button type="primary" @click="resetSearch()">重置</el-button>
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">评分</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '审计项目名称'"
            align="center"
            label="审计项目名称"
            prop="project"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.project.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '负责科室'"
            align="center"
            label="负责科室"
            prop="org"
          >
            <template #default="{ row }">
              <span>{{ row.org.orgname }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '方案实施扣分'"
            align="center"
            label="方案实施扣分"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <span>{{ row?.score || '' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '实施时间'"
            align="center"
            label="实施时间"
          >
            <template #default="{ row }">
              <span>{{ row?.startTime || '' }}</span>
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <Score ref="edit" @fetchData="fetchData"></Score>
    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
  import {
    qualityScoreList,
    qualityScoreDelete,
  } from '@/oapi/audit/qualityScore'
  import Score from './components/jcssScore.vue'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  export default {
    components: {
      Score,
      filterTable,
      filterSearch,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          type: 5,
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '审计项目名称' },
          { name: '负责科室' },
          { name: '方案实施扣分' },
          { name: '实施时间' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-zlpy-jcss-search',
        tableKey: 'oilAudit-zlpy-jcss-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '审计项目名称', key: 'name' }]
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
        // 列表接口
        let tableDefaultData = {
          project: { projectName: '' },
          org: { orgname: '' },
          score: null,
          startTime: '',
        }
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
          code,
        } = await qualityScoreList(this.queryForm)
        if (code === 1) {
          let list = tlist.map((item) => {
            return {
              ...tableDefaultData,
              ...item,
            }
          })
          this.list = list || []
          this.total = totalRecord || 0
          this.listLoading = false
        }
      },
      handleExport() {},
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await qualityScoreDelete({ ids: row.id })
          if (res.code === 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          type: 5,
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
