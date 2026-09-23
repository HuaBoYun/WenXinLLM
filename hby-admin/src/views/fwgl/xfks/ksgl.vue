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
              <el-select
                v-model="queryForm.openType"
                class="filter-item"
                placeholder="开放类型"
                v-if="item.name === '开放类型'"
                clearable
              >
                <el-option
                  v-for="item in openTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>

              <el-date-picker
                v-model="queryForm.startTime"
                class="filter-item"
                value-format="yyyy-MM-dd"
                type="date"
                placeholder="考试开始时间"
                v-if="item.name === '考试开始时间'"
              />

              <el-date-picker
                v-model="queryForm.endTime"
                class="filter-item"
                value-format="yyyy-MM-dd"
                type="date"
                placeholder="考试结束时间"
                v-if="item.name === '考试结束时间'"
              />

              <el-input
                v-model="queryForm.title"
                placeholder="考试名称"
                v-if="item.name === '考试名称'"
                class="filter-item"
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
        <!-- <el-button>导出</el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="loading" :data="list">
        <el-table-column align="center" label="考试名称" prop="title">
          <template slot-scope="scope">
            {{ scope.row.title }}
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            label="考试类型"
            align="center"
            v-if="item.name === '考试类型'"
          >
            <template slot-scope="scope">
              {{ openTypes[scope.row.openType - 1].label }}
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

          <el-table-column
            label="状态"
            align="center"
            v-if="item.name === '状态'"
          >
            <template slot-scope="scope">
              {{ states[+scope.row.state] }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作" width="260">
          <template #default="{ row }">
            <el-button type="text" @click="handlerEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDetail(row)">
              考试详情
            </el-button>
            <el-button type="text" @click="Delete(row)">删除</el-button>
            <el-button type="text" @click="checkPerson(row)">下发</el-button>
            <el-button type="text" @click="start(row)">启动</el-button>
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

    <ksglAdd ref="ksglAdd" @fetch="fetchData" />
    <ksglDetail ref="ksglDetail" />
  </div>
</template>

<script>
  import { deleteList, getKsglList, setExamUpdate } from '@/api/fwgl/ksgl'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ksglAdd from './components/modals/ksglAdd.vue'
  import ksglDetail from './components/modals/ksglDetail.vue'
  import selectPerson from './components/personSelect/index.vue'
  // import { legalService, fetchApi } from '@/api/fwgl/api'

  export default {
    name: 'stgl',
    components: { filterTable, filterSearch, ksglAdd, ksglDetail, selectPerson },
    data() {
      return {
        list: [],
        loading: true,
        dialogVisible: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          startTime: '',
          endTime: '',
          title: '',
        },
        pageData: {
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
        states: ['进行中', '已禁用', '待开始', '已结束'],
        listQuery: {
          current: 1,
          size: 10,
          params: {
            title: '',
          },
        },
        filedAll: [
          { name: '考试类型' },
          { name: '考试时间' },
          { name: '考试总分' },
          { name: '及格线' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-ksgl-search',
        tableKey: 'fwgl-flfw-ksgl-list',
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
          { name: '考试开始时间', key: 'startTime' },
          { name: '考试结束时间', key: 'endTime' },
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
        this.loading = true
        if (type && type === 'reset') this.$refs['form'].resetFields()
        const {
          data: { tlist, totalRecord },
        } = await getKsglList({ params: this.queryForm, ...this.pageData })
        this.loading = false
        this.list = tlist
        this.total = totalRecord
      },
      handleExport() {},
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['ksglAdd'].showModal()
      },
      handlerEdit(row) {
        // 编辑
        this.$refs['ksglAdd'].showModal(row)
      },
      handleDetail(row) {
        // 考试详情
        this.$refs['ksglDetail'].showModal(row)
      },
      async Delete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          try {
            await deleteList({ ids: [row.id] })
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            // 立即从本地列表中移除对应数据，UI即时更新
            const index = this.list.findIndex(item => item.id === row.id)
            if (index !== -1) {
              this.list.splice(index, 1)
              // 更新总数
              if (this.total > 0) {
                this.total -= 1
              }
            }
            // 不再调用fetchData，避免重新加载可能返回的缓存数据
            // 删除已经成功，本地数据已更新，无需重新请求
          } catch (error) {
            this.$message({
              type: 'error',
              message: '删除失败，请重试',
            })
          }
        }).catch(() => {
          // 用户点击取消，不做任何操作
        })
      },

      checkPerson(row) {
        this.$router.push(`/fwgl/ryxf?${row.id}`)
      },
      async start(row) {
        const res = await setExamUpdate({ examId: row.id })
        if (res.code == 1) {
          this.$message({
            type: 'success',
            message: '启动成功!',
          })
          this.fetchData()
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
</style>
