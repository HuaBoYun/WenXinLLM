<template>
  <div class="system-log-container">
    <div
      v-if="breakShow"
      style="cursor: pointer; padding: 20px 20px 0px 20px"
      @click="toExam"
    >
      <el-alert
        :closable="false"
        title="您有正在进行的考试，离线太久考试将被作废哦，点击此处可继续考试！"
        type="error"
      />
    </div>
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
              <el-select
                v-model="queryForm.openType"
                class="filter-item"
                placeholder="开放类型"
                clearable
                v-if="item.name === '开放类型'"
              >
                <el-option
                  v-for="item in openTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>

              <el-input
                v-model="queryForm.title"
                placeholder="考试名称"
                class="filter-item"
                v-if="item.name === '考试名称'"
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
        <!-- <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button>导出</el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="loading" :data="list">
        <el-table-column
          align="center"
          label="考试名称"
          prop="title"
        ></el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            label="考试类型"
            align="center"
            v-if="item.name === '考试类型'"
          >
            <template slot-scope="scope">
              {{ scope.row.openType === 1 ? '完全开放' : '定向考试' }}
            </template>
          </el-table-column>

          <el-table-column
            label="考试时间"
            width="220px"
            align="center"
            v-if="item.name === '考试时间'"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.timeLimit">
                {{ formatDate(scope.row.startTime) }} ~
                {{ formatDate(scope.row.endTime) }}
              </span>
              <span v-else>不限时</span>
            </template>
          </el-table-column>

          <el-table-column
            label="考试时长"
            align="center"
            v-if="item.name === '考试时长'"
          >
            <template slot-scope="scope">
              {{ scope.row.totalTime }}分钟
            </template>
          </el-table-column>

          <el-table-column
            label="考试总分"
            prop="totalScore"
            align="center"
            v-if="item.name === '考试总分'"
          />

          <el-table-column
            label="及格线"
            prop="qualifyScore"
            align="center"
            v-if="item.name === '及格线'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <!-- v-if="scope.row.state === 0" -->
          <template slot-scope="scope">
            <el-button
              icon="el-icon-caret-right"
              type="primary"
              size="mini"
              @click="handlePre(scope.row.id)"
            >
              去考试
            </el-button>
            <!-- <el-button
              v-if="scope.row.state === 1"
              icon="el-icon-s-release"
              size="mini"
              disabled
            >
              已禁用
            </el-button>
            <el-button
              v-if="scope.row.state === 2"
              icon="el-icon-s-fold"
              size="mini"
              disabled
            >
              待开始
            </el-button>
            <el-button
              v-if="scope.row.state === 3"
              icon="el-icon-s-unfold"
              size="mini"
              disabled
            >
              已结束
            </el-button> -->
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
    <zbks ref="zbks" @start-exam="handleStartExam" />
    <ksks ref="ksks" @fetchDatas="fetchData" />
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ksks from './components/modals/ksks.vue'
  import zbks from './components/modals/zbks.vue'

  import { checkProcess, onlinePaging } from '@/api/fwgl/xfks/zxks'

  export default {
    name: 'zxks',
    components: { filterTable, filterSearch, zbks, ksks },
    data() {
      return {
        list: [],
        loading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          current: 1,
          size: 20,
        },
        openTypes: [
          {
            value: 1,
            label: '完全开放',
          },
          {
            value: 2,
            label: '定向考试',
          },
        ],
        filedAll: [
          { name: '考试类型' },
          { name: '考试时间' },
          { name: '考试时长' },
          { name: '考试总分' },
          { name: '及格线' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-zxks-search',
        tableKey: 'fwgl-flfw-zxks-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        breakShow: false,
        breakId: '',
        openTypes: [
          {
            value: 1,
            label: '完全开放',
          },
          {
            value: 2,
            label: '定向考试',
          },
        ],

        listQuery: {
          current: 1,
          size: 10,
          params: {},
        },
        options: {
          // 可批量操作
          multi: false,
          // 列表请求URL
          listUrl: '/exam/api/exam/exam/online-paging',
        },
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
      // this.check()
    },
    methods: {
      formatDate(row) {
        let n = new Date(row)
        return (
          n.toLocaleDateString().replace(/\//g, '-') +
          ' ' +
          n.toTimeString().substr(0, 8)
        )
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '开放类型', key: 'openType' },
          { name: '考试名称', key: 'title' },
        ]
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
        this.check()
        if (type && type === 'reset') this.$refs['form'].resetFields()
        const res = await onlinePaging(this.queryForm)
        this.loading = false
        if (res && res.data) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord
        }
      },
      handleExport() {},
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['cnflfwView'].showEdit('add', null)
      },
      handlePre(row) {
        this.$refs['zbks'].showModal(row)
        // 准备考试modal
      },
      handleStartExam(examId) {
        this.$refs['ksks'].showModal(examId)
      },
      async check() {
        const res = await checkProcess()
        if (res.data && res.data.id) {
          this.breakShow = true
          this.breakId = res.data.id
        } else {
          this.breakShow = false
        }
      },
      // 继续考试
      toExam() {
        this.handleStartExam(this.breakId)
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
