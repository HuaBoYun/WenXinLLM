<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="300"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="item.name + '_' + index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.templateName"
                clearable
                placeholder="请输入模板名称"
              />
              <!-- <el-select
                v-model="queryForm.templateName"
                class="filter-item"
                clearable
                placeholder="模板名称"
                v-if="item.name === '模板名称'"
              >
                <el-option
                  v-for="item in examineTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select> -->
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="fetchData('reset')" type="primary">
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAddOrUpdate()">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="loading" :data="list" ref="multipleTable">
        <el-table-column align="center" label="模板名称" prop="templateName" />

        <div v-for="(item, index) in filedNow" :key="item.name + '_' + index">
          <el-table-column
            label="关联审计类型"
            align="center"
            v-if="item.name === '关联审计类型'"
          >
            <template slot-scope="{ row }">
              {{ row.auditTypeList?.map((el) => el.typeName).join(',') }}
            </template>
          </el-table-column>
          <el-table-column
            label="创建时间"
            prop="createTime"
            align="center"
            v-if="item.name === '创建时间'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleAddOrUpdate(row)">
              编辑
            </el-button>
            <el-button type="text" @click="handlerDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

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

    <pfxglAdd ref="pfxglAdd" :typeOpts="typeOpts" @queryData="queryData" />
  </div>
</template>

<script>
  import { getList, handleDelete, getTypeOpts } from '@/oapi/baseConfig/gcsjmb'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import pfxglAdd from './components/gcsjmbView.vue'
  export default {
    name: 'pfxgl',
    components: { filterTable, filterSearch, pfxglAdd },
    data() {
      const fields = [{ name: '模板名称', key: 'templateName' }] // 定义表单所有项
      return {
        list: [],
        typeOpts: [],
        loading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          templateName: undefined,
        },
        filedAll: [
          {
            name: '关联审计类型',
          },
          {
            name: '创建时间',
          },
        ], //所有表格项
        filedNow: [],
        searchAll: fields, //所有搜索项
        localKey: 'oilAudit-pfgl-gcsjmb-search',
        tableKey: 'oilAudit-pfgl-gcsjmb-list',
        searchNow: fields, //当前所有搜索项
        searchItem: fields.slice(0, 4), //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.initSearch()
      this.getTypeOpts()
    },
    methods: {
      async getTypeOpts() {
        const {
          data: { auditTypeList },
        } = await getTypeOpts()
        this.typeOpts = auditTypeList
      },
      // 动态表格开始
      initTable() {
        this.$nextTick(() => {
          let data = localStorage.getItem(this.tableKey)
          if (data) {
            data = JSON.parse(data)
            this.filedNow = data.filter((item) => item.show)
          } else {
            this.filedNow = this.filedAll
          }
        })
      },
      initSearch() {
        this.$nextTick(() => {
          let data = localStorage.getItem(this.localKey)
          if (data) {
            data = JSON.parse(data)
            this.searchNow = data.filter((item) => item.show)
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
      async fetchData(type) {
        this.loading = true
        if (type && type === 'reset') {
          this.queryForm.templateName = undefined
          this.queryForm.pageNumber = 1
        }

        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getList(this.queryForm)

        this.list = tlist
        this.total = totalRecord
        this.loading = false
      },
      handleAddOrUpdate(row) {
        this.$refs['pfxglAdd'].showModal(row)
      },
      handlerDelete(row) {
        // 删除题目
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            handleDelete({ id: row.id }).then(() => {
              this.fetchData()
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
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
