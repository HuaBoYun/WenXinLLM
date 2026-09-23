<template>
  <div class="system-log-container">
    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel :span="24" class="option-row">
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
        <el-button type="success" @click="handleAdd" v-if="hasAuth('SJPJadd')">
          新建
        </el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column
          align="center"
          label="公司名称"
          prop="companyName"
          show-overflow-tooltip
        />

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="系统版本"
            prop="fvendor"
            show-overflow-tooltip
            v-if="item.name === '系统版本'"
          />
          <el-table-column
            align="center"
            label="数据库类型"
            prop="sourceDbType"
            show-overflow-tooltip
            v-if="item.name === '数据库类型'"
          />
          <el-table-column
            align="center"
            label="年份"
            v-if="item.name === '年份'"
            sortable="custom"
          >
            <template #default="{ row }">
              <span>{{ row.startdate }}-{{ row.enddate }}</span>
            </template>
          </el-table-column>
        </div>

        <el-table-column
          align="center"
          label="操作"
          prop="type"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="hasAuth('SJPJedit')"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="hasAuth('SJPJdelete')"
            >
              删除
            </el-button>
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
    <sjpz-edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getConfigList,
    deleteConfig,
    getDbVersion,
  } from '@/api/setting/dataGather'
  import SjpzEdit from './components/SjpzEdit'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Sjdr',
    components: { SjpzEdit, filterTable },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        tableKey: 'setting-dataGather-sjpz-list',
        filedAll: [
          { name: '系统版本' },
          { name: '数据库类型' },
          { name: '年份' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
      this.getDbVersion()
      this.initTable()
    },
    methods: {
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
          this.sortFlag = 'asc'
        } else if (order === 'descending') {
          this.sortFlag = 'desc'
        } else {
          this.sortFlag = ''
        }
        await this.fetchData()
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getConfigList({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,})
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async getDbVersion() {
        const res = await getDbVersion()
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteConfig({ orderId: row.orderId })
          if (res.code == 1) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
    },
  }
</script>

<style scoped lang="scss">
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
