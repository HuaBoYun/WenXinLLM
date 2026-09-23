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
              :key="index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.realName"
                placeholder="人员名称"
                class="filter-item"
                v-if="item.name === '人员名称'"
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column label="人员" prop="realName" align="center" />

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            label="考试次数"
            prop="tryCount"
            align="center"
            v-if="item.name === '考试次数'"
          />

          <el-table-column
            label="最高分"
            prop="maxScore"
            align="center"
            v-if="item.name === '最高分'"
          />

          <el-table-column
            label="是否通过"
            align="center"
            v-if="item.name === '是否通过'"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.passed" style="color: #00ff00">通过</span>
              <span v-else style="color: #ff0000">未通过</span>
            </template>
          </el-table-column>

          <el-table-column
            label="最后考试时间"
            prop="updateTime"
            align="center"
            v-if="item.name === '最后考试时间'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleExamDetail(row.examId, row.userId)"
            >
              考试明细
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- :current-page="queryForm.current"
    :page-size="queryForm.size" -->
    <el-pagination
      class="pagination"
      background
      :current-page="pageData.current"
      :layout="layout"
      :page-size="pageData.size"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <el-dialog :visible.sync="dialogVisible" title="考试明细" width="60%">
      <div class="el-dialog-div">
        <my-paper-list :exam-id="examId" :user-id="userId" />
      </div>
    </el-dialog>

    <ctxl ref="ctxl" />
  </div>
</template>

<script>
  import { getKSDetailList } from '@/api/fwgl/ksgl'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ctxl from './components/modals/ctxl.vue'
  import MyPaperList from './components/paper'
  export default {
    name: 'ksxq',
    components: { filterTable, filterSearch, MyPaperList, ctxl },
    data() {
      return {
        list: [],
        listLoading: true,
        dialogVisible: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        examId: '',
        userId: '',
        queryForm: {},
        pageData: {
          current: 1,
          size: 20,
        },
        filedAll: [
          { name: '考试次数' },
          { name: '最高分' },
          { name: '是否通过' },
          { name: '最后考试时间' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-ksxq-search',
        tableKey: 'fwgl-flfw-ksxq-list',
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
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '人员名称', key: 'realName' }]
        return fields
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
        this.pageData.size = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.pageData.current = val
        this.fetchData()
      },
      queryData() {
        this.pageData.current = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @param {*} type
       * @return {*}
       */
      async fetchData(type) {
        this.listLoading = true
        if (type && type === 'reset') this.$refs['form'].resetFields()

        const {
          data: { tlist, total },
        } = await getKSDetailList({
          params: this.queryForm,
          ...this.pageData,
        })
        this.listLoading = false
        this.list = tlist
        this.total = tlist.length
      },
      startTrain() {
        // 开始训练
        this.$refs['ctxl'].showModal()
      },
      handleExamDetail(examId, userId) {
        // 考试明细
        this.examId = examId
        this.userId = userId

        this.dialogVisible = true
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
