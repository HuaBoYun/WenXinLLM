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
                placeholder="名字"
                v-if="item.name === '名字'"
                class="filter-item"
              />
              <el-input
                v-model="queryForm.userName"
                placeholder="用户名"
                v-if="item.name === '用户名'"
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
        <el-button type="success" @click="handleAdd">下发</el-button>
        <el-button type="" @click="back">返回</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="loading" :data="list">
        <el-table-column
          label="姓名"
          align="center"
          prop="realName"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            label="用户名"
            align="center"
            prop="userName"
            v-if="item.name === '昵称'"
          ></el-table-column>
          <el-table-column
            label="手机"
            prop="miblePhone"
            align="center"
            v-if="item.name === '手机'"
          />
          <el-table-column
            label="电子邮箱"
            prop="email"
            align="center"
            v-if="item.name === '电子邮箱'"
          />
        </div>
        <el-table-column align="center" label="操作" width="260">
          <template #default="{ row }">
            <el-button type="text" @click="Delete(row)">删除</el-button>
          </template>
        </el-table-column>
        <!-- <el-table-column label="" prop="" align="" width="1px" /> -->
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
    <Person ref="persom" :examId="this.examId" @fetchData="fetchData"></Person>
  </div>
</template>

<script>
  import { deleteList, getKSRYList, setExamStaffDelete } from '@/api/fwgl/ksgl'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import Person from './modal.vue'

  export default {
    name: 'stgl',
    components: { filterTable, filterSearch, Person },
    data() {
      return {
        list: [],
        loading: true,
        dialogVisible: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          realName: '',
          userName: '',
          pageNumber: 1,
          pageSize: 20,
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
        },
        filedAll: [
          { name: '姓名' },
          { name: '昵称' },
          { name: '手机' },
          { name: '电子邮箱' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-ksgl-search',
        tableKey: 'fwgl-flfw-ksgl-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        examId: '',
      }
    },
    created() {
      this.examId = window.location.href.split('?')[1] || ''
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
        let fields = [
          { name: '名字', key: 'realName' },
          { name: '用户名', key: 'userName' },
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
        if (type && type === 'reset') this.$refs['form'].resetFields()
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getKSRYList({ ...this.queryForm, examId: this.examId })
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
        this.$refs['persom'].showEdit()
      },
      handlerEdit(row) {
        // 编辑
        this.$refs['ksglAdd'].showModal(row)
      },
      handleDetail() {
        // 考试详情
        this.$router.push('/fwgl/ksxq')
      },
      Delete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          setExamStaffDelete({ staffId: row.staffId }).then((res) => {
            if (res.code == 1) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.fetchData()
            }
          })
        })
      },

      checkPerson() {
        this.$router.push('/fwgl/ryxf')
      },
      back() {
        this.$router.push('/xfks/ksgl')
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
