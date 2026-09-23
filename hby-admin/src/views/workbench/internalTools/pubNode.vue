<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <!-- 搜索条件 -->
      <vab-query-form>
        <vab-query-form-left-panel :span="21">
          <el-form :model="queryForm" :inline="true" @submit.native.prevent>
            <el-form-item>
              <el-input
                v-model="queryForm.title"
                placeholder="请输入标题"
                clearable
              />
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="失效开始日期"
                end-placeholder="失效结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                @change="handleDateChange"
              />
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="createDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="创建开始日期"
                end-placeholder="创建结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                @change="handleCreateDateChange"
              />
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="queryForm.type"
                placeholder="请选择是否生效"
                clearable
              >
                <el-option label="有效" :value="1" />
                <el-option label="无效" :value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="queryData">查询</el-button>
              <el-button @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </vab-query-form>

      <vab-query-form-right-panel class="option-row">
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
        <el-button type="success" @click="handleCreate()">新建</el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="标题" prop="title" width="200">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.title }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="内容"
            prop="acontent"
            v-if="item.name === '内容'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="过期时间"
            prop="expirationTime"
            v-if="item.name === '过期时间'"
          >
            <template #default="{ row }">
              {{ row.expirationTime }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            v-if="item.name === '创建时间'"
          >
            <template #default="{ row }">
              {{ row.createTime }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="创建人"
            prop="staffName"
            v-if="item.name === '创建人'"
          />
        </div>

        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
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
  import { getList, remove } from '@/api/internal/pubNode.js'
  import PubNodeEdit from './components/PubNodeEdit.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'
  import axios from 'axios'

  export default {
    name: 'PubNode',
    components: { PubNodeEdit, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        dateRange: [],
        createDateRange: [],
        queryForm: {
          title: '',
          start: '',
          end: '',
          createTimeStart: '',
          createTimeEnd: '',
          type: '',
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
        tableKey: 'workbench-intemalTools-pubNode-list',
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
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
      handleCreateDateChange(dateRange) {
        if (dateRange && dateRange.length === 2) {
          this.queryForm.createTimeStart = dateRange[0]
          this.queryForm.createTimeEnd = dateRange[1]
        } else {
          this.queryForm.createTimeStart = ''
          this.queryForm.createTimeEnd = ''
        }
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      resetQuery() {
        this.queryForm = {
          title: '',
          start: '',
          end: '',
          createTimeStart: '',
          createTimeEnd: '',
          type: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.dateRange = []
        this.createDateRange = []
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        try {
          const { data, code } = await getList({ ...this.queryForm })
          if (code === 1) {
            this.list = data.list || []
            this.total = data.total || 0
          } else {
            this.$message.error(data.msg || '获取数据失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('获取公告列表失败:', error)
          this.$message.error('获取数据失败')
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      },
      handleCreate() {
        this.$refs.editDialog.showEdit(null, false)
      },
      handleEdit(row) {
        this.$refs.editDialog.showEdit(row, false)
      },
      handleDelete(row) {
        console.log('🚀 ~ handleDelete ~ row:', row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            // 这里需要根据实际的删除接口进行调用
            const data = await remove({ id: row.id })
            this.$baseMessage(data.msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          } catch (error) {
            this.$message.error('删除失败')
          }
        })
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
