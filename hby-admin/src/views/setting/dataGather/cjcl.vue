<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form>
        <vab-query-form-left-panel :span="24">
          <DynamicQuery
            :query-form="queryForm"
            @fetch-data="fetchData"
            @reset-search="resetSearch"
          />
        </vab-query-form-left-panel>
      </vab-query-form>
    </el-card>

    <el-card shadow="never" class="secondCard">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd" v-if="hasAuth('CJCLadd')">
          新建
        </el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column
          align="center"
          label="策略名称"
          prop="infoName"
          show-overflow-tooltip
        />

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="采集公司"
            prop="orgName"
            show-overflow-tooltip
            v-if="item.name === '采集公司'"
          />
          <el-table-column
            align="center"
            label="采集频率"
            prop="acWeek"
            show-overflow-tooltip
            v-if="item.name === '采集频率'"
          >
            <template #default="{ row }">
              {{ getWeekNames(row.acWeek) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="采集时间"
            prop="setDate"
            show-overflow-tooltip
            v-if="item.name === '采集时间'"
            sortable="custom"
          >
            <template #default="{ row }">
              {{ getHourNames(row.setDate) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            show-overflow-tooltip
            v-if="item.name === '创建时间'"
            sortable="custom"
          />
          <el-table-column
            align="center"
            label="创建人"
            prop="realName"
            show-overflow-tooltip
            v-if="item.name === '创建人'"
          />
          <el-table-column
            align="center"
            label="采集状态"
            prop="type"
            show-overflow-tooltip
            v-if="item.name === '采集状态'"
          >
            <template #default="{ row }">
              <span v-if="row.status === 0">
                <span class="vab-dot vab-dot-blue"><span></span></span>
                未启动
              </span>
              <span v-if="row.status === 1">
                <span class="vab-dot vab-dot-success"><span></span></span>
                已启动
              </span>
              <span v-if="row.status === 2">
                <span class="vab-dot vab-dot-error"><span></span></span>
                已暂停
              </span>
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
            <span v-if="row.status === 1">
              <el-button
                circle
                class="font-size-16"
                icon="el-icon-video-play"
                type="danger"
                @click="handleStatus(row, 0)"
              />
            </span>
            <span v-else>
              <el-button
                circle
                class="font-size-16"
                icon="el-icon-video-pause"
                type="success"
                @click="handleStatus(row, 1)"
              />
            </span>
            <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="hasAuth('CJCLedit')"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="hasAuth('CJCLdelete')"
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
    <cjcl-edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getStrategyList,
    deleteStrategy,
    updateStrategy,
  } from '@/api/setting/dataGather'
  import CjclEdit from './components/CjclEdit'
  import * as dayjs from 'dayjs'
  import DynamicQuery from './DynamicQuery.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Cjcl',
    components: { CjclEdit, DynamicQuery, filterTable },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          typeName: undefined,
          strType: undefined,
          strVal: undefined,
          infoName: undefined,
          acWeek: [],
          setDate: [],
          createTime: undefined,
          realName: undefined,
          status: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        tableKey: 'setting-dataGather-cjcl-list',
        filedAll: [
          { name: '采集公司' },
          { name: '采集频率' },
          { name: '采集时间' },
          { name: '创建时间' },
          { name: '创建人' },
          { name: '采集状态' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
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
      getWeekNames(s) {
        const arr = this.$refs['edit'].acWeekOptions
        return s
          .split('-')
          .map((i) => arr[i - 1].label)
          .join('、')
      },
      getHourNames(s) {
        const arr = this.$refs['edit'].acHourOptions
        return s
          .split('-')
          .map((i) => {
            return arr.find((j) => j.value === parseInt(i)).label
          })
          .join('、')
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm
        this.$refs['form'].resetFields()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      async fetchData(query) {
        const { pageNumber, pageSize } = this.queryForm
        let params = Object.assign({ pageNumber, pageSize })
        if (query && query.typeName && query.strType && query.strVal) {
          const { typeName, strType, strVal } = query
          params.typeName = typeName
          params.strType = strType
          if (typeName == 'acWeek' || typeName == 'setDate') {
            params.strVal = strVal.join('lhpbfh9501')
          } else {
            params.strVal = strVal
          }
        }
        if (query && query.typeName && query.typeName == 'status') {
          const { typeName, strVal } = query
          params.typeName = typeName
          params.strType = strVal
        }
        this.listLoading = true
        const {
          pageInfo: { tlist, totalRecord },
        } = await getStrategyList({...params,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,})
        this.list = tlist.map((i) => {
          return {
            ...i,
            createTime: dayjs(i.createTime).format('YYYY-MM-DD HH:mm:ss'),
            acHour: i.setDate,
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      async handleStatus(row, status) {
        if (hasAuth('CJCLstatus')) return
        const msg = await updateStrategy({
          infoId: row.infoId,
          status,
        }).msg
        this.$baseMessage(msg || '成功', 'success', 'vab-hey-message-success')
        await this.fetchData()
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteStrategy({ infoStr: row.infoId })
          this.$baseMessage(
            msg || '删除成功',
            'success',
            'vab-hey-message-success'
          )
          await this.fetchData()
        })
      },
    },
  }
</script>
<style scoped>
  .font-size-16 {
    font-size: 12px;
    padding: 4px;
    margin-right: 8px;
  }
  .width-130 {
    width: 130px;
  }

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
