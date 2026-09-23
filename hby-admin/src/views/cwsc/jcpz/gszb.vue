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
                v-model="queryForm.bookName"
                placeholder="账簿名称"
                v-if="item.name === '账簿名称'"
              />
              <el-input
                v-model="queryForm.accbooktypecode"
                placeholder="账簿类型编码"
                v-if="item.name === '账簿类型编码'"
              />
              <el-input
                v-model="queryForm.accbooktypename"
                placeholder="账簿类别名称"
                v-if="item.name === '账簿类别名称'"
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
        <el-button type="success" @click="handleSelect">选择</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column align="center" label="账簿名称">
          <template #default="{ row }">
            <el-button type="text" @click="handleRead(row)">
              {{ row.bookName }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label=" " width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="采集方案名称"
            prop="planName"
            v-if="item.name === '采集方案名称'"
          />
          <el-table-column
            align="center"
            label="账簿类别编码"
            prop="accbooktypecode"
            v-if="item.name === '账簿类别编码'"
          />
          <el-table-column
            align="center"
            label="账簿类型名称"
            prop="accbooktypename"
            v-if="item.name === '账簿类型名称'"
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="creationtime"
            v-if="item.name === '创建时间'"
          >
            <template #default="{ row }">
              {{ row.creationtime.slice(0, 10) }}
            </template>
          </el-table-column>
        </div>
        <!-- <el-table-column align="center" label="操作" width="200">
          <template #default="{ row }">
            <el-button type="text" @click="handleAuth(row)">授权</el-button>
            <el-button type="text" @click="handleCancelAuth(row)">
              取消授权
            </el-button>
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </el-card>
    <zblxEdit ref="edit" @fetchData="fetchData"></zblxEdit>
    <jueseModal ref="auth" @roleSelect="handleAuthSelect"></jueseModal>
    <hasAuthModal
      ref="cancelAuth"
      @roleSelect="handleCancelAuthSelect"
    ></hasAuthModal>
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
  import { getGsZbglAuthRoleList, sureGsZb } from '@/api/cwsc'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import zblxEdit from './components/zblxEdit.vue'
  import jueseModal from './components/jueseModal.vue'
  import hasAuthModal from './components/hasAuthModal.vue'

  export default {
    name: 'NormalReportList',
    components: {
      filterTable,
      filterSearch,
      zblxEdit,
      jueseModal,
      hasAuthModal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          accbooktypename: '',
          bookName: '',
        },
        filedAll: [
          { name: '采集方案名称' },
          { name: '账簿类别编码' },
          { name: '账簿类型名称' },
          { name: '创建时间' },
          // { name: '账簿类别名称' },
          // { name: '外币折算日期' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'cwsc-jcpz-gszb-search',
        tableKey: 'cwsc-jcpz-gszb-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        row: {},
        select: [],
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
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '账簿名称', key: 'bookName' },
          { name: '账簿类型编码', key: 'accbooktypecode' },
          { name: '账簿类别名称', key: 'accbooktypename' },
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
        } = await getGsZbglAuthRoleList(this.queryForm)
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
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteZbgl({ pkAccbookinfo: row.pkAccbookinfo })
          if (res.code == 200) {
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
          fname: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },

      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val
      },
      async handleSelect() {
        if (this.select.length == 0) {
          this.$baseMessage('请选择账簿', 'error', 'vab-hey-message-error')
          return
        }
        const res = await sureGsZb({
          pkAccbookinfo: this.select
            .map((item) => item.pkAccbookinfo)
            .join(','),
        })
        if (res.code == 1) {
          this.$baseMessage('选择成功', 'success', 'vab-hey-message-success')
          await this.fetchData()
        }
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
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
