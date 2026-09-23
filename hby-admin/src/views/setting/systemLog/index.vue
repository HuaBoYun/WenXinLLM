<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.userName"
                clearable
                placeholder="用户名称"
                v-if="item.name === '用户名称'"
              />
              <el-input
                v-model="queryForm.businessModule"
                clearable
                placeholder="业务名称"
                v-if="item.name === '业务名称'"
              />
              <el-date-picker
                v-model="queryForm.date"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-if="item.name === '请求时间段'"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                :default-time="['08:00:00', '18:00:00']"
              ></el-date-picker>
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
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel style="width: 100%">
        <el-tooltip effect="dark" content="表格筛选" placement="top">
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
        <el-button
          type="success"
          @click="handleDelete"
          style="margin-left: 10px"
        >
          删除
        </el-button>
        <el-button
          type="success"
          @click="handleExport"
          style="margin-left: 10px"
        >
          导出
        </el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="用户名称"
          prop="username"
          width="100"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="真实名称"
            v-if="item.name === '真实名称'"
            prop="userAccount"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="请求时间"
            v-if="item.name === '请求时间'"
            prop="requestTime"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="请求耗时"
            v-if="item.name === '请求耗时(毫秒)'"
            prop="requestDurationMs"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="业务名称"
            v-if="item.name === '业务名称'"
            prop="businessModule"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="子业务名称"
            v-if="item.name === '子业务名称'"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ formatActionDescription(row) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="ip"
            v-if="item.name === 'ip'"
            prop="ip"
            show-overflow-tooltip
          />
        </div>
        <el-table-column width="1" />
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">详情</el-button>
            <!-- <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.currentPage"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      title="详情"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      v-if="dialogFormVisible"
    >
      <div class="item">
        <div class="sql_title">sql:</div>
        <div class="content">{{ JSON.parse(this.renderData.executedSql) }}</div>
      </div>
      <div class="item">
        <div class="sql_title">请求报文:</div>
        <div class="content">
          {{ JSON.parse(this.renderData.requestPayload) }}
        </div>
      </div>
      <div class="item">
        <div class="sql_title">返回报文:</div>
        <div class="content">
          {{ JSON.parse(this.renderData.responsePayload) }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { xmpypyDelete } from '@/oapi/audit/xmpy'
  import axios from 'axios'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { formatDay } from '@/utils/index'
  import {
    getLogBusList,
    deleteLoginLog,
    exportSystemLog,
    getLogBusDetail,
  } from '@/api/systemLog'
  export default {
    name: 'systemLog',
    components: { filterSearch, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        dialogFormVisible: false,
        queryForm: {
          businessModule: '',
          currentPage: 1,
          pageSize: 20,
          userName: '',
          date: [],
        },
        filedAll: [
          { name: '真实名称' },
          { name: '请求时间' },
          { name: '请求耗时' },
          { name: '业务名称' },
          { name: '子业务名称' },
          { name: 'ip' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'setting-systemLog-search',
        tableKey: 'setting-systemLog-list',
        searchMore: true,
        select: [],
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [
          { name: '用户名称', key: 'userName' },
          { name: '业务名称', key: 'businessModule' },
          { name: '请求时间段', key: 'date' },
        ]
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      formatActionDescription(row) {
        // 格式化子业务名称，过滤掉成功/失败字段
        let description = `${row.subBusinessModule}-${row.actionDescription}`
        // 移除包含成功或失败的部分
        description = description.replace(/[，,]?(成功|失败)$/g, '')
        description = description.replace(/[，,]?(success|fail)$/gi, '')
        return description
      },
      resetQueryForm() {
        this.queryForm = {
          businessModule: '',
          currentPage: 1,
          pageSize: 20,
          userName: '',
          date: [],
        }
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
        this.queryForm.currentPage = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.currentPage = 1
        this.fetchData()
      },
      async fetchData() {
        const { date } = this.queryForm
        let params = { ...this.queryForm }
        if (date) {
          params.requestTimeStart = date[0]
          params.requestTimeEnd = date[1]
        }
        delete params.date
        getLogBusList({
          ...params,
        }).then((res) => {
          if (res.code == 1) {
            this.list = res.data.records
            this.total = res.data.total
          }
        })
      },
      async handleDetail(row) {
        const { code, data, msg } = await getLogBusDetail({
          id: row.id,
        })
        if (code == 1) {
          this.renderData = data
        } else {
          this.$baseMessage(msg, 'error')
        }
        this.dialogFormVisible = true
      },
      handleDelete(row) {
        if (this.select.length === 0) {
          this.$baseMessage('请选择要删除的数据', 'warning')
          return
        }

        deleteLoginLog(this.select.map((item) => item.id)).then((res) => {
          if (res.code == 1) {
            // 提示成功
            this.$baseMessage('删除成功', 'success')
            // 刷新数据
            this.fetchData()
          }
        })
      },
      close() {
        this.dialogFormVisible = false
      },
      async handleExport(row) {
        // const data = await reportExport({ jhyjgid: row.jhyjgid })
        // let fileName = 'test'
        // let blob = new Blob([data], {
        //   type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        // })
        // if (window.navigator.msSaveOrOpenBlob) {
        //   navigator.msSaveBlob(blob, fileName)
        // } else {
        //   let link = document.createElement('a')
        //   link.href = window.URL.createObjectURL(blob)
        //   link.download = fileName
        //   link.click()
        //   // 释放内存
        //   window.URL.revokeObjectURL(link.href)
        // }
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      async handleExport(row) {
        if (this.queryForm.date.length > 0) {
          this.queryForm.requestTimeStart = this.queryForm.date[0]
          this.queryForm.requestTimeEnd = this.queryForm.date[1]
        }
        const data = await exportSystemLog(this.queryForm)
        let fileName = '系统日志'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
  .item {
    display: flex;
    margin-bottom: 20px;
  }
  .sql_title {
    font-size: 18px;
    margin-bottom: 20px;
    width: 100px;
    display: inline-block;
  }
  .content {
    display: inline-block;
    flex: 1;
  }
</style>
