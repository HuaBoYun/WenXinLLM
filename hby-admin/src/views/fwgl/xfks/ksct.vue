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
                v-model="queryForm.title"
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
        <el-button type="primary" @click="startTrain">错题训练</el-button>
        <el-button type="danger" @click="handleDeleteMore">批量删除</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="题目内容"
          prop="title"
          width="550"
        >
          <template #default="{ row }">
            <span @click="checkDetail(row)">{{ row.title }}</span>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            label="错误次数"
            prop="wrongCount"
            align="center"
            v-if="item.name === '错误次数'"
          />

          <el-table-column
            label="更新时间"
            prop="updateTime"
            align="center"
            v-if="item.name === '更新时间'"
          />
        </div>
        <el-table-column
          align="center"
          label=""
          prop=""
          width="1"
        ></el-table-column>
        <!-- <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handlerDelete(row)">删除</el-button>
            <el-button type="text" @click="startTrain(row)">开始训练</el-button>
          </template>
        </el-table-column> -->
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

    <ctxl ref="ctxl" />
    <tmxq ref="tmxq" />
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ctxl from './components/modals/ctxl.vue'
  import MyPaperList from './components/paper'
  import tmxq from './components/modals/tmxq.vue'
  import { queryWrongTopic, deleteWrongTopic } from '@/api/fwgl/xfks/wdcj'

  export default {
    name: 'ksct',
    components: { filterTable, filterSearch, MyPaperList, ctxl, tmxq },
    data() {
      return {
        ids: [],
        list: [],
        multipleSelection: [],
        listLoading: true,
        dialogVisible: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          examId: '',
          title: '', //考试名称
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
        filedAll: [{ name: '错误次数' }, { name: '更新时间' }], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-ksct-search',
        tableKey: 'fwgl-flfw-ksct-list',
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
        let fields = [{ name: '题目内容', key: 'title' }]
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
        this.listLoading = true
        this.queryForm.examId = window.location.href.split('?')[1]
        if (type && type === 'reset') this.$refs['form'].resetFields()
        const {
          data: { records, total },
        } = await queryWrongTopic({ params: this.queryForm, ...this.pageData })
        this.list = records
        this.total = total
        this.listLoading = false

        // this.list = [
        //   {
        //     createTime: '2021-02-02 10:46:47',
        //     examId: '1356425140212076545',
        //     id: '1356433854633611266',
        //     quId: '1286859363650195458',
        //     sort: 4,
        //     title: '找出与众不同的一个：',
        //     updateTime: '2021-02-02 10:46:47',
        //     userId: '10001',
        //     wrongCount: 7,
        //   },
        //   {
        //     createTime: '2021-02-02 10:46:47',
        //     examId: '1356425140212076545',
        //     id: '1356433854662971393',
        //     quId: '1286859119977910274',
        //     sort: 5,
        //     title:
        //       '如果把这个大立方体的六个面全部涂上黑色，然后按图中虚线把它切成36个小方块，两面有黑色的小方块有多少个？',
        //     updateTime: '2021-02-02 10:46:47',
        //     userId: '10001',
        //     wrongCount: 7,
        //   },
        //   {
        //     createTime: '2021-02-02 10:46:47',
        //     examId: '1356425140212076545',
        //     id: '1356433854700720130',
        //     quId: '1286858826460516353',
        //     sort: 6,
        //     title: '角对于元相当于小时对于',
        //     updateTime: '2021-02-02 10:46:47',
        //     userId: '10001',
        //     wrongCount: 7,
        //   },
        //   {
        //     createTime: '2021-02-02 10:46:47',
        //     examId: '1356425140212076545',
        //     id: '1356433854746857474',
        //     quId: '1286858556779352066',
        //     sort: 7,
        //     title:
        //       '火车守车(车尾)长6.4米。机车的长度等于守车的长加上半节车厢的长。车厢长度等于守车长加上机车长。火车的机车、车厢、守车共长多少米？',
        //     updateTime: '2021-02-02 10:46:47',
        //     userId: '10001',
        //     wrongCount: 4,
        //   },
        //   {
        //     createTime: '2021-10-21 14:24:27',
        //     examId: '1356425140212076545',
        //     id: '1451071866911580161',
        //     quId: '1286859710305226754',
        //     sort: 8,
        //     title: '图中阴影部分占面积百分之几？',
        //     updateTime: '2021-10-21 14:24:27',
        //     userId: '10001',
        //     wrongCount: 5,
        //   },
        //   {
        //     createTime: '2021-10-21 14:24:27',
        //     examId: '1356425140212076545',
        //     id: '1451071866953523201',
        //     quId: '1286856786200055810',
        //     sort: 9,
        //     title: '五个答案中哪个是最好的类比？女儿对于父亲相当于侄女对于',
        //     updateTime: '2021-10-21 14:24:27',
        //     userId: '10001',
        //     wrongCount: 6,
        //   },
        //   {
        //     createTime: '2021-10-21 14:24:27',
        //     examId: '1356425140212076545',
        //     id: '1451071866987077633',
        //     quId: '1286860480865980417',
        //     sort: 10,
        //     title: '数数有多少个三角形',
        //     updateTime: '2021-10-21 14:24:27',
        //     userId: '10001',
        //     wrongCount: 4,
        //   },
        //   {
        //     createTime: '2022-10-30 10:33:23',
        //     examId: '1356425140212076545',
        //     id: '1586546778822250497',
        //     quId: '1461505751183912961',
        //     sort: 11,
        //     title: '中国东北三省是指（）',
        //     updateTime: '2022-10-30 10:33:23',
        //     userId: '10001',
        //     wrongCount: 3,
        //   },
        //   {
        //     createTime: '2022-10-30 10:33:23',
        //     examId: '1356425140212076545',
        //     id: '1586546778834833410',
        //     quId: '1461505492353413121',
        //     sort: 12,
        //     title: '以下哪些是中国的特别行政区？',
        //     updateTime: '2022-10-30 10:33:23',
        //     userId: '10001',
        //     wrongCount: 3,
        //   },
        //   {
        //     createTime: '2022-10-30 10:33:23',
        //     examId: '1356425140212076545',
        //     id: '1586546778855804930',
        //     quId: '1461505894142570497',
        //     sort: 14,
        //     title: '咖啡的故乡是非洲吗？',
        //     updateTime: '2022-10-30 10:33:23',
        //     userId: '10001',
        //     wrongCount: 2,
        //   },
        // ]
      },
      handleExport() {},
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['cnflfwView'].showEdit('add', null)
      },
      // 开始考试
      handleDeleteMore() {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          let res = await deleteWrongTopic({ ids: this.ids })
          if (res.msg === '请求成功！') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.fetchData()
            // this.fileIds.splice(index, 1)
            // this.tableDataFile.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      startTrain() {
        // 开始训练
        const examId = window.location.href.split('?')[1]
        this.$refs['ctxl'].showModal(examId)
      },
      checkDetail(row) {
        this.$refs['tmxq'].showModal(row.id)
      },
      handleSelectionChange(val) {
        this.ids = []
        val.forEach((item) => {
          this.ids.push(item.id)
        })

        // this.multipleSelection = val
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
