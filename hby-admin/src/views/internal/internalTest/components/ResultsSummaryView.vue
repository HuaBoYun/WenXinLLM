<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="查看"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.planname"
                clearable
                placeholder="方案名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '方案名称'"
              />
              <el-date-picker
                v-model="queryForm.year"
                placeholder="实施年度"
                style="width: 200px; margin-right: 20px"
                type="year"
                v-if="item.name === '实施年度'"
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
        <el-table-column
          align="center"
          label="年度"
          prop="PLANYEAR"
          width="80"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="方案名称"
            prop="PLANNAME"
            #default="{ row }"
            v-if="item.name === '方案名称'"
          >
            <el-button
              style="color: red"
              type="text"
              @click="handleDeatil(row)"
            >
              {{ row.PLANNAME }}
            </el-button>
          </el-table-column>
          <el-table-column
            align="center"
            label="	被测机构"
            prop="TESTEDORGS"
            show-overflow-tooltip
            v-if="item.name === '被测机构'"
          />
          <el-table-column
            align="center"
            label="控制总数"
            prop="ALLCOUNT"
            show-overflow-tooltip
            v-if="item.name === '控制总数'"
          >
            <template #default="{ row }">
              <el-button
                v-if="row.ALLCOUNT > 0"
                style="color: red"
                type="text"
                @click="handleNumber(row, 'ALLCOUNT')"
              >
                {{ row.ALLCOUNT }}
              </el-button>
              <div v-else>{{ row.ALLCOUNT }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="有效控制"
            prop="YCOUNT"
            show-overflow-tooltip
            v-if="item.name === '有效控制'"
          >
            <template #default="{ row }">
              <el-button
                v-if="row.YCOUNT > 0"
                style="color: red"
                type="text"
                @click="handleNumber(row, 'YCOUNT')"
              >
                {{ row.YCOUNT }}
              </el-button>
              <div v-else>{{ row.YCOUNT }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="无效控制"
            prop="WCOUNT"
            show-overflow-tooltip
            v-if="item.name === '无效控制'"
          >
            <template #default="{ row }">
              <el-button
                v-if="row.WCOUNT > 0"
                style="color: red"
                type="text"
                @click="handleNumber(row, 'WCOUNT')"
              >
                {{ row.WCOUNT }}
              </el-button>
              <div v-else>{{ row.WCOUNT }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="不适用"
            prop="BCOUNT"
            show-overflow-tooltip
            v-if="item.name === '不适用'"
          >
            <template #default="{ row }">
              <el-button
                v-if="row.BCOUNT > 0"
                style="color: red"
                type="text"
                @click="handleNumber(row, 'BCOUNT')"
              >
                {{ row.BCOUNT }}
              </el-button>
              <div v-else>{{ row.BCOUNT }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="PLANSTATUS"
            show-overflow-tooltip
            v-if="item.name === '状态'"
          ></el-table-column>
        </div>

        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">详细结果</el-button>
            <el-button @click="copyData(row)" type="text" v-if="isUEditor">
              复制
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
    <ResultskView ref="edit" @fetch-data="fetchData" />
    <ResultsView ref="ResultsView" />
    <PlanView ref="planDetail" />
  </el-dialog>
</template>

<script>
  import { groupResultCountList } from '@/api/internal/plan.js'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ResultskView from '@/views/internal/internalTest/components/ResultskView'
  import ResultsView from '@/views/internal/internalTest/components/ResultsView'
  import PlanView from '@/views/internal/internalTest/components/PlanView'
  export default {
    //编辑器传入,判断是否有复制按钮
    props: {
      isUEditor: {
        type: Boolean,
        default: false,
      },
    },
    name: 'Results',
    components: {
      ResultskView,
      ResultsView,
      PlanView,
      filterTable,
      filterSearch,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          year: '',
          planname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '方案名称' },
          { name: '被测机构' },
          { name: '控制总数' },
          { name: '有效控制' },
          { name: '无效控制' },
          { name: '不适用' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-internalTest-results-search',
        tableKey: 'internal-internalTest-results-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        dialogFormVisible: false,
        groupId: '',
      }
    },

    created() {},
    methods: {
      showEdit(row) {
        this.groupId = row.id
        this.title = '查看'
        this.dialogFormVisible = true
        this.fetchData()
        this.initTable() //初始化表格
        this.searchNow = this.getFiled()
        this.searchItem = this.searchNow.slice(0, 4)
        this.initSearch()
      },
      close() {
        this.dialogFormVisible = false
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '方案名称', key: 'planname' },
          { name: '实施年度', key: 'year' },
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
      resetQueryForm() {
        this.queryForm = {
          year: '',
          planname: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const { year, ...other } = this.queryForm
        const {
          data: {
            pageBean: { records, total },
          },
        } = await groupResultCountList({
          year: year ? year.getFullYear() : '',
          ...other,
          groupId: this.groupId,
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleNumber(row, type) {
        this.$refs['ResultsView'].showEdit(row, type)
      },
      handleDeatil(row) {
        this.$refs['planDetail'].showEdit(
          {
            testplanid: row.TESTPLANID,
          },
          true
        )
      },
      //复制
      copyData(row) {
        // 提取需要的字段
        const textToCopy = `年度：${row.PLANYEAR}  方案名称：${row.PLANNAME}  被测机构：${row.TESTEDORGS}  控制总数：${row.ALLCOUNT}  有效控制：${row.YCOUNT}  无效控制：${row.WCOUNT}  不适用：${row.BCOUNT}`

        // 复制到剪贴板
        navigator.clipboard
          .writeText(textToCopy)
          .then(() => {
            this.$message.success('复制成功')
          })
          .catch((err) => {
            this.$message.error('复制失败')
            console.error('复制失败:', err)
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
  .pagination {
    margin-bottom: 20px !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
