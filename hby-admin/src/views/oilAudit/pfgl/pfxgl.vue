<template>
  <!-- 评议管理 > 添加评分 -->
  <div class="system-log-container">
    <!-- 搜索区 -->
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
              <el-select
                v-model="queryForm.type"
                class="filter-item"
                clearable
                placeholder="评分表类型"
                v-if="item.name === '评分表类型'"
              >
                <el-option
                  v-for="(item, index) in examineTypes"
                  :key="index"
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
              <el-button type="primary" @click="fetchData('reset')">重置</el-button>
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

    <!-- 列表筛选 -->
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

      <!-- 列表区 -->
      <el-table v-loading="loading" :data="list">
        <el-table-column align="center" label="评分表类型">
          <template #default="{ row }">
            {{ scoreTypeMap[row.type] }}
          </template>
        </el-table-column>

        <el-table-column label="状态" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="switchChange($event, row)"
            ></el-switch>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="item.name + '_' + index">
          <el-table-column
            label="归属重点"
            align="center"
            v-if="item.name === '归属重点'"
          >
            <template #default="{ row }">
              {{
                row.scoreItems[0]
                  ? row.scoreItems[0].scoreContent
                  : '归属重点为空'
              }}
            </template>
          </el-table-column>

          <el-table-column
            label="分值"
            prop="score"
            align="center"
            v-if="item.name === '分值'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handlerEdit(row)">编辑</el-button>
            <el-button type="text" @click="handlerDetail(row)">详情</el-button>
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

    <!--    <el-dialog :visible.sync="dialogVisible" title="考试明细" width="60%">-->
    <!--      <div class="el-dialog-div">-->
    <!--        <my-paper-list :exam-id="examId" :user-id="userId" />-->
    <!--      </div>-->
    <!--    </el-dialog>-->

    <pfxglAdd ref="pfxglAdd" @get="fetchData" />
  </div>
</template>

<script>
import {
  addScoreList,
  addScoreDelete,
  addScoreStatus,
} from '@/oapi/audit/scoreManage'
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
        type: null,
      },
      scoreTypeMap: [
        '',
        '审计实施方案制定及执行',
        '审计底稿',
        '审计报告',
        '审计管理系统上线',
        '奖惩事项',
      ], // 评分类型字典表
      examineTypes: [
        // 评分类型
        {
          value: 1,
          label: '审计实施方案制定及执行',
        },
        {
          value: 2,
          label: '审计底稿',
        },
        {
          value: 3,
          label: '审计报告',
        },
        {
          value: 4,
          label: '审计管理系统上线',
        },
        {
          value: 5,
          label: '奖惩事项',
        },
      ],
      filedAll: [{ name: '归属重点' }, { name: '分值' }], //所有表格项
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
    this.initTable() //初始化表格
    this.fetchData()
    this.searchNow = this.getFiled()
    this.searchItem = this.searchNow.slice(0, 4)
    this.initSearch()
  },
  methods: {
    switchChange($event, row) {
      // 表格中改变状态
      this.updateRowStatus(row)
    },
    // 修改状态
    updateRowStatus(params) {
      addScoreStatus(params).then((res) => {
        if (res.code === 1) {
          this.fetchData()
          this.$message({
            type: 'success',
            message: '状态修改成功!',
          })
        }
      })
    },
    handleSelectionChange(val) {
      // 下拉选择
      this.multipleSelection = val
    },
    // 定义表单所有项
    getFiled() {
      let fields = [{ name: '评分表类型', key: 'type' }]
      return fields
    },
    titleType(v) {
      const arr = ['单选题', '多选题', '判断题']
      return arr[v - 1]
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

      if (this.searchMore) {
        this.searchItem = this.searchNow
      } else {
        this.searchItem = this.searchNow.slice(0, 4)
      }
    },
    // 动态表格开始
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
    async fetchData(type) {
      this.loading = true
      if (type && type === 'reset') this.$refs['form'].resetFields()
      const {
        data: { tlist, totalRecord },
        msg,
      } = await addScoreList(this.queryForm)
      console.log('addScoreList', this.queryForm)
      if (msg === '成功') {
        this.list = tlist || []
        this.total = totalRecord || 0
        this.loading = false
        console.log('addScoreList', this.list)
      }
    },
    handleExport() {},
    handleAdd() {
      this.$refs['pfxglAdd'].showModal(null, 'add')
    },
    handlerEdit(row) {
      // 编辑
      this.$refs['pfxglAdd'].showModal(row, 'edit')
    },
    handlerDetail(row) {
      // 编辑
      this.$refs['pfxglAdd'].showModal(row, 'detail')
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
            addScoreDelete({ ids: row.id }).then((res) => {
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
