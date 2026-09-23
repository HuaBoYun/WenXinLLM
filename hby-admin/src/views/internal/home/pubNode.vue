<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <!-- 搜索条件 -->
      <vab-query-form>
        <vab-query-form-left-panel :span="21">
          <el-form :model="queryForm" :inline="true" @submit.native.prevent>
            <el-form-item>
              <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                @change="handleDateChange"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="queryData">查询</el-button>
              <el-button @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </vab-query-form>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="标题" prop="title" width="200">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.title }}
            </el-button>
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          label="内容"
          prop="acontent"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="过期时间" prop="expirationTime">
          <template #default="{ row }">
            {{ row.expirationTime }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime">
          <template #default="{ row }">
            {{ row.createTime }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="创建人" prop="staffName" />
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pager"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <PubNodeEdit ref="editDialog" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getIndexList } from '@/api/internal/pubNode.js'
  import filterTable from '@/components/filterTable.vue'
  import PubNodeEdit from '@/views/workbench/internalTools/components/PubNodeEdit.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'PubNodeIndex',
    components: { filterTable, PubNodeEdit },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        dateRange: [],
        queryForm: {
          start: '',
          end: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '内容' },
          { name: '过期时间' },
          { name: '创建时间' },
          { name: '创建人' },
        ], //所有表格项
        filedNow: [], //当前表格项
        tableKey: 'workbench-intemalTools-pubNodeIndex-list',
      }
    },
    created() {
      this.fetchData()
      this.initTable()
    },
    methods: {
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
      handleDateChange(dateRange) {
        if (dateRange && dateRange.length === 2) {
          this.queryForm.start = dateRange[0]
          this.queryForm.end = dateRange[1]
        } else {
          this.queryForm.start = ''
          this.queryForm.end = ''
        }
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      resetQuery() {
        this.queryForm = {
          start: '',
          end: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.dateRange = []
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        try {
          const { data, code } = await getIndexList({ ...this.queryForm })
          if (code === 1) {
            this.list = data.list || []
            this.total = data.total || 0
          } else {
            this.$message.error(data.msg || '获取数据失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('获取首页公告列表失败:', error)
          this.$message.error('获取数据失败')
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      },
      handleDetail(row) {
        this.$refs.editDialog.showEdit(row, true)
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }

  .pager {
    margin-bottom: 20px !important;
  }
</style>
