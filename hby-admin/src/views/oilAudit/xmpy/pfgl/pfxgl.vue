<template>
  <div class="system-log-container">
    <!-- <vab-query-form>
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
              <el-select
                v-model="queryForm.examineType"
                class="filter-item"
                clearable
                placeholder="评分表类型"
                v-if="item.name === '评分表类型'"
              >
                <el-option
                  v-for="item in examineTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
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
              <el-button @click="fetchData('reset')" type="primary">重置</el-button>

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
    </vab-query-form> -->

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
        <el-button type="success" @click="handleAdd">新建</el-button>
        <!-- <el-button>导出</el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="loading" :data="list" ref="multipleTable">
        <!-- <el-table-column align="center" label="评分表类型" prop="examineType">
          <template #default="{ row }">
            {{ examineTypes.find((x) => (x.value = row.examineType)).label }}
          </template>
        </el-table-column> -->
        <el-table-column
          label="归属重点"
          prop="examineEmphasis"
          align="center"
        />
        <div v-for="(item, index) in filedNow" :key="item.name + '_' + index">
          <el-table-column
            label="分值"
            prop="totalScore"
            align="center"
            v-if="item.name === '分值'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handlerEdit(row)">编辑</el-button>
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

    <el-dialog :visible.sync="dialogVisible" title="考试明细" width="60%">
      <div class="el-dialog-div">
        <my-paper-list :exam-id="examId" :user-id="userId" />
      </div>
    </el-dialog>

    <pfxglAdd ref="pfxglAdd" @get="fetchData" />
  </div>
</template>

<script>
  import { getList, deleteApi } from '@/oapi/fwgl/pfgl/pfxgl'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import pfxglAdd from './components/pfxglAdd.vue'
  export default {
    name: 'pfxgl',
    components: { filterTable, filterSearch, pfxglAdd },
    data() {
      return {
        multipleSelection: [],
        list: [],
        loading: true,
        dialogVisible: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        examId: '',
        userId: '',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          examineType: undefined,
        },
        examineTypes: [
          {
            value: 1,
            label: '外部监管考核',
          },
          {
            value: 2,
            label: '子单位考核',
          },
        ],
        filedAll: [{ name: '分值' }], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-pfgl-pfxgl-search',
        tableKey: 'fwgl-pfgl-pfxgl-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      /**
       * @description: 下面四句：控制筛选项、表格的位置以及显示隐藏
       */
      this.initTable() //初始化表格 //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '评分表类型', key: 'examineType' }]
        return fields
      },
      titleType(v) {
        const arr = ['单选题', '多选题', '判断题']
        return arr[v - 1]
      },
      /**
       * @description: 从上一次缓存中获取搜索项初始化
       * @return {*}
       */
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
      /**
       * @description: 展开收起查询条件
       * @return {*}
       */
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      // 动态表格开始
      /**
       * @description: 从上一次缓存中初始化表头
       * @return {*}
       */
      initTable() {
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
       * @param {*} type
       * @return {*}
       */
      async fetchData(type) {
        this.loading = true
        if (type && type === 'reset') {
          this.queryForm.examineType = undefined
        }

        const {
          data: { tlist, totalRecord },
        } = await getList(this.queryForm)

        this.list = tlist
        this.total = totalRecord
        this.loading = false
      },
      handleExport() {},
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['pfxglAdd'].showModal()
      },
      handlerEdit(row) {
        // 编辑
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
            if (row.transactionId) {
              deleteApi({ id: row.transactionId }).then((res) => {
                if (res.code == 200) {
                  this.fetchData()
                  this.$message({
                    type: 'success',
                    message: '删除成功!',
                  })
                }
              })
            } else {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.fetchData()
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
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
