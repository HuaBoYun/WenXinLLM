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
            :model="queryForm.params"
            @submit.native.prevent
          >
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="index"
              :prop="item.key"
            >
              <el-select
                v-model="queryForm.params.quType"
                class="filter-item"
                clearable
                placeholder="题目类型"
                v-if="item.name === '题目类型'"
              >
                <el-option
                  v-for="item in quTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>

              <repo-select
                v-model="queryForm.params.repoIds"
                :multi="true"
                v-if="item.name === '选择题库'"
              />

              <el-input
                v-model="queryForm.params.content"
                placeholder="题目内容"
                class="filter-item"
                v-if="item.name === '题目内容'"
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button @click="handleExport">导出</el-button>
        <el-button
          :disabled="multipleSelection.length == 0"
          @click="handlerDeleteAll"
        >
          批量删除
        </el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="loading"
        :data="list"
        ref="multipleTable"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>

        <el-table-column align="center" label="题目类型" prop="title">
          <template slot-scope="scope">
            {{ titleType(scope.row.quType) }}
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            label="所属题库"
            prop="title"
            align="center"
            v-if="item.name === '所属题库'"
          />
          <el-table-column
            label="题目内容"
            prop="content"
            align="center"
            v-if="item.name === '题目内容'"
          >
            <template slot-scope="scope">
              <!-- <a @click="handleEdit(scope.row)">
                {{ scope.row.content }}
              </a> -->
              {{ scope.row.content }}
            </template>
          </el-table-column>

          <!-- <el-table-column
            label="创建时间"
            prop="createTime"
            align="center"
            v-if="item.name === '创建时间'"
          /> -->
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handlerEdit(row)">编辑</el-button>
            <el-button type="text" @click="handlerDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- :current-page="queryForm.current"
    :page-size="queryForm.size" -->
    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.current"
      :layout="layout"
      :page-size="queryForm.size"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <el-dialog :visible.sync="dialogVisible" title="考试明细" width="60%">
      <div class="el-dialog-div">
        <my-paper-list :exam-id="examId" :user-id="userId" />
      </div>
    </el-dialog>

    <stglAdd ref="stglAdd" @get="fetchData" />
    <tmxq ref="tmxq" />
  </div>
</template>

<script>
  import { deleteTopic, getTopicList, exportTopic } from '@/api/fwgl/xfks/stgl'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import stglAdd from './components/modals/stglAdd.vue'
  import tmxq from './components/modals/tmxq.vue'
  import RepoSelect from './components/RepoSelect'
  import { downloadFile } from '@/utils/otherUtils'
  // getTopicList
  export default {
    name: 'stgl',
    components: { filterTable, filterSearch, RepoSelect, stglAdd, tmxq },
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
          current: 1,
          size: 20,
          params: {
            quType: '',
          },
        },
        quTypes: [
          {
            value: 1,
            label: '单选题',
          },
          {
            value: 2,
            label: '多选题',
          },
          {
            value: 3,
            label: '判断题',
          },
        ],
        filedAll: [
          { name: '所属题库' },
          { name: '题目内容' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-stgl-search',
        tableKey: 'fwgl-flfw-stgl-list',
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
        let fields = [
          { name: '题目类型', key: 'quType' },
          { name: '选择题库', key: 'repoIds' },
          { name: '题目内容', key: 'content' },
        ]
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
              if (Array.isArray(this.queryForm.params[x.key])) {
                this.queryForm.params[x.key] = []
              } else if (this.queryForm.params[x.key] instanceof Object) {
                this.queryForm.params[x.key] = {}
              } else {
                this.queryForm.params[x.key] = null
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
        this.queryForm.size = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.current = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.current = 1
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
          this.queryForm.params.quType = ''
          this.queryForm.params.repoIds = []
          this.queryForm.params.content = ''
          // this.$refs['form'].resetFields()
        }

        const {
          data: { tlist, totalRecord },
        } = await getTopicList(this.queryForm)

        this.list = tlist
        this.total = totalRecord
        this.loading = false
      },
      async handleExport() {
        this.loading = true

        const exportParams = {
          // 导出固定分页参数
          current: 1,
          size: 20000,
          params: {
            // 可选参数：根据查询条件传递
            quType: this.queryForm.params.quType || '',
            repoIds: this.queryForm.params.repoIds || [],
            content: this.queryForm.params.content || '',
          },
        }

        try {
          const res = await exportTopic(exportParams)
          this.loading = false
          downloadFile(res, '试题列表.xlsx')
        } catch (error) {
          this.loading = false
          this.$message.error('导出失败，请重试')
        }
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['stglAdd'].showModal()
      },
      handlerEdit(row) {
        // 编辑
        this.$refs['stglAdd'].showModal(row)
      },
      handlerDeleteAll() {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            let list = []
            this.multipleSelection.map((item) => {
              list.push(item.id)
            })
            deleteTopic({ ids: list }).then((res) => {
              if (res.code == 1) {
                this.fetchData()
                this.$message({
                  type: 'success',
                  message: '删除成功!',
                })
              }
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handlerDelete(row) {
        // 删除题目

        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            if (row.id) {
              deleteTopic({ ids: [row.id] }).then((res) => {
                if (res.code == 1) {
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
