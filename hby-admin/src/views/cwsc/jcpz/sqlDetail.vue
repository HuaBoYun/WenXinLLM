<template>
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
                v-model="queryForm.fname"
                placeholder="查询名称"
                v-if="item.name === '查询名称'"
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
              <el-button @click="resetSearch()" type="primary">重置</el-button>
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
        <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="名称"
            prop="fname"
            v-if="item.name === '名称'"
          >
            <template #default="{ row }">
              {{ row.fid ? row.fname : row.sqlconfigname }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="fname"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.bfrv
                  ? row.bfrv.iscompleted == 1
                    ? '采集中'
                    : row.bfrv.iscompleted == 2
                    ? '已完成'
                    : '采集中'
                  : '未抽取'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="250">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleChouqu(row)"
              :disabled="
                row.bfrv && (row.bfrv.iscompleted == 1 || !row.bfrv.iscompleted)
              "
            >
              执行抽取
            </el-button>
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button
              type="text"
              @click="handleStop(row)"
              :disabled="!row.bfrv"
            >
              停止采集
            </el-button>
            <el-button type="text" @click="handleDelete(row)">
              恢复数据
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <edit ref="edit" @fetchData="fetchData"></edit>
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
    getCjsqlList,
    deleteCjsql,
    executeChouqu,
    stopChouqu,
  } from '@/api/cwsc'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import edit from './components/sqlDetailEdit.vue'

  export default {
    name: 'NormalReportList',
    components: { filterTable, filterSearch, edit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          fname: '',
          fplanid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [{ name: '名称' }, { name: '状态' }], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'cwsc-jcpz-cjpz-search',
        tableKey: 'cwsc-jcpz-cjpz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      // 从url里取参数
      const { query } = this.$route
      if (query.fid) {
        this.queryForm.fplanid = query.fid
        this.fetchData()
      }
    },
    methods: {
      // 定义表单所有项
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '查询名称', key: 'fname' },
          { name: '状态', key: 'fstatus' },
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
      // 动态表格开始
      /**
       * @description: 从上一次缓存中初始化表头
       * @return {*}
       */
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
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { records, total },
        } = await getCjsqlList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleExport() {},
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要恢复数据吗', null, async () => {
          const res = await deleteCjsql({ fid: row.fid })
          if (res.msg == '成功') {
            this.$baseMessage('恢复成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          fname: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      handleChouqu(row) {
        executeChouqu({
          finitPlanid: this.queryForm.fplanid,
          finitsqlid: row.sqlconfigid,
          fid: row.fid ? row.fid : '',
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('执行成功', 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      handleStop(row) {
        stopChouqu({
          finitPlanid: this.queryForm.fplanid,
          finitsqlid: row.sqlconfigid,
          fid: row.fid || '',
          recordSqlid: row.bfrv.sqlid,
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('停止成功', 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
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
