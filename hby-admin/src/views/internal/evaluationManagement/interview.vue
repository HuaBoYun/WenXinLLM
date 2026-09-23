<template>
  <div class="system-log-container">
    <vab-query-form>
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
                v-model="queryForm.content"
                clearable
                placeholder="一般访谈内容"
                v-if="item.name === '一般访谈内容'"
              />
              <el-input
                v-model="queryForm.module"
                clearable
                placeholder="业务模块"
                v-if="item.name === '业务模块'"
              />
            </el-form-item>
            <span>
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
              <el-form-item>
                <span
                  :class="searchMore ? 'search-more is-opened' : 'search-more'"
                  @click="showMore"
                >
                  <span>{{ searchMore ? '收起' : '展开' }}</span>
                  <i class="el-icon-arrow-down"></i>
                </span>
              </el-form-item>
            </span>
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
              :key="filterKey"
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="隶属部门"
          prop="deptname"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDeatil(row)">
              {{ row.deptname }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="被访谈人"
            prop="interviewee"
            show-overflow-tooltip
            v-if="item.name === '被访谈人'"
          />
          <el-table-column
            align="center"
            label="业务模块"
            prop="module"
            show-overflow-tooltip
            v-if="item.name === '业务模块'"
          />
          <el-table-column
            align="center"
            label="时间"
            prop="times"
            show-overflow-tooltip
            v-if="item.name === '时间'"
          />
          <el-table-column
            align="center"
            label="职务"
            prop="post"
            show-overflow-tooltip
            v-if="item.name === '职务'"
          />
          <el-table-column
            align="center"
            label="一般访谈内容"
            prop="content"
            show-overflow-tooltip
            v-if="item.name === '一般访谈内容'"
          />
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="160"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status || createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-button
              @click="handleDelete(row)"
              type="text"
              :disabled="!!row.status || createId != row.createstaffid"
            >
              删除
            </el-button>
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
    <InterviewView ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getPjftHomepageList, deletePjftQx } from '@/api/internal/project'

  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import InterviewView from '@/views/internal/evaluationManagement/components/interviewView'
  import TableColumns from '@/components/customForm/TableColumns.vue'

  export default {
    name: 'evaluationPlan',
    components: {
      InterviewView,
      filterTable,
      filterSearch,
      TableColumns,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          content: '',
          module: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '被访谈人' },
          { name: '业务模块' },
          { name: '时间' },
          { name: '职务' },
          { name: '一般访谈内容' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-evaluationManagement-project-search',
        tableKey: 'internal-evaluationManagement-project-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filterKey: 'defect',
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    async created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '一般访谈内容', key: 'content' },
          { name: '业务模块', key: 'module' },
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
        console.log(this.searchMore)
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
      resetQueryForm() {
        this.queryForm = {
          assName: '',
          assNumnber: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      async fetchCustomerFormData() {
        getDefaultRenderData({
          sceneCode: 'nkgl-pjlx',
        }).then((res) => {
          this.filedAll = res.data || []
          this.filterKey = 'filterKey_' + (Math.random() + 1) * 100 // 为了解决组件不更新的问题，改变组件的key值强制更新
          this.initTable()
        })
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { pageBean },
        } = await getPjftHomepageList({ ...this.queryForm })
        this.list = pageBean.list
        this.total = pageBean.total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDeatil(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleEdit(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deletePjftQx({ id: row.id })
          if (code == 1) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          } else {
            this.$message.error('删除失败')
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
