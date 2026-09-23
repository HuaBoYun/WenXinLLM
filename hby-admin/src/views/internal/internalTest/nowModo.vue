<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <!-- 搜索条件 -->
      <vab-query-form>
        <vab-query-form-left-panel :span="21">
          <el-form :model="queryForm" :inline="true" @submit.native.prevent>
            <el-form-item>
              <el-input
                v-model="queryForm.ruleName"
                placeholder="请输入文件名称"
                clearable
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.ruleNumber"
                placeholder="请输入发文文号"
                clearable
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.summaryInfo"
                placeholder="请输入摘要内容"
                clearable
              />
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
        <el-table-column
          align="center"
          label="文件名称"
          prop="ruleName"
          width="200"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.ruleName }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="文件编号"
            prop="ruleCode"
            v-if="item.name === '文件编号'"
            show-overflow-tooltip
          />

          <el-table-column
            align="center"
            label="发文文号"
            prop="ruleNumber"
            v-if="item.name === '发文文号'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="发文部门"
            prop="publishOrg"
            v-if="item.name === '发文部门'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="发文日期"
            prop="publishDate"
            v-if="item.name === '发文日期'"
          >
            <template #default="{ row }">
              {{ row.publishDate }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="生效日期"
            prop="takeEffectTime"
            v-if="item.name === '生效日期'"
          >
            <template #default="{ row }">
              {{ row.takeEffectTime }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="时效性"
            prop="timeLiness"
            v-if="item.name === '时效性'"
          />
          <el-table-column
            align="center"
            label="摘要"
            prop="summaryInfo"
            v-if="item.name === '摘要'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="录入人"
            prop="enteringPerson"
            v-if="item.name === '录入人'"
          />
          <el-table-column
            align="center"
            label="录入时间"
            prop="createTime"
            v-if="item.name === '录入时间'"
          >
            <template #default="{ row }">
              {{ row.createTime ? row.createTime.split(' ')[0] : '' }}
            </template>
          </el-table-column>
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
    <NowModoEdit ref="editDialog" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getList, remove } from '@/api/internal/nowModo.js'
  import NowModoEdit from './components/NowModoEdit.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'NowModo',
    components: { NowModoEdit, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          ruleName: '',
          ruleNumber: '',
          summaryInfo: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '文件编号' },
          { name: '发文文号' },
          { name: '发文部门' },
          { name: '发文日期' },
          { name: '生效日期' },
          { name: '时效性' },
          { name: '摘要' },
          { name: '录入人' },
          { name: '录入时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        tableKey: 'workbench-intemalTools-nowModo-list',
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

      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      resetQuery() {
        this.queryForm = {
          ruleName: '',
          ruleNumber: '',
          summaryInfo: '',
          pageNumber: 1,
          pageSize: 20,
        }
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
          console.error('获取现行标准列表失败:', error)
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
