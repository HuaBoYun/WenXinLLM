<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.reportname"
                clearable
                placeholder="报告名称"
                v-if="item.name === '报告名称'"
              />
              <el-date-picker
                align="right"
                v-if="item.name === '日期'"
                end-placeholder="报告结束日期"
                range-separator="至"
                format="yyyy-MM-dd"
                start-placeholder="报告开始日期"
                type="daterange"
                unlink-panels
                v-model="queryForm.Date"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
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
          </el-form>
        </vab-query-form-top-panel>
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
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button type="primary" @click="exportData">导出初稿</el-button>
        <el-button type="success" @click="handleSend()">下发</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column align="center" label="报告名称" prop="reportname">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.reportname }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="报告时间"
            v-if="item.name === '报告时间'"
            prop="reporttime"
            :formatter="formatDay"
          />
          <el-table-column
            align="center"
            label="报告版本"
            v-if="item.name === '报告版本'"
            prop="reporttype"
            show-overflow-tooltip
          />
          />
          <el-table-column
            align="center"
            label="报告方式"
            prop="reportmode"
            v-if="item.name === '报告方式'"
          />
          <!-- <el-table-column align="center" label="复核状态" prop="reportstatus">
            <template #default="{ row }">
              {{ statusName[row.reportstatus] || '未审核' }}
            </template>
          </el-table-column> -->
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.reportstatus == 1
                  ? '审批中'
                  : row.reportstatus == 2
                  ? '已退回'
                  : row.reportstatus == 3
                  ? '已撤回'
                  : row.reportstatus == 4
                  ? '已终止'
                  : row.reportstatus == 5
                  ? '已跟踪'
                  : row.reportstatus == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column label="操作" fixed="right" align="center">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.reportstatus || createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!row.reportstatus"
                    @click.native="handleDeal(row)"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="
                      !!row.reportstatus ||
                      btnLoading ||
                      createId != row.createstaffid
                    "
                    @click.native="handleShenPi(row)"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="
                      !!row.reportstatus || createId != row.createstaffid
                    "
                    @click.native="handleDelete(row)"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click.native="handleExport(row)">
                    导出
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <IndexView ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <project-manage @projectManage="getChildlistPro" ref="manage" />
  </div>
</template>

<script>
  import {
    auditFH,
    auditSP,
    auditYJZJ,
    exportReportChuGao,
    report,
    reportDel,
    reportDetail,
    reportExport,
  } from '@/api/audit/report'
  import { formatDay } from '@/utils/index'
  import IndexView from '@/views/audit/report/components/IndexView'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import projectManage from '@/components/selectPerson'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import { sjqzdXF } from '@/api/audit/implement'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterTable,
      filterSearch,
      IndexView,
      WfqdDeal,
      ProcessList,
      projectManage,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        statusName: [
          '未审批',
          '审批中',
          '已退回',
          '已通过',
          '已终止',
          '复核中',
          '复核调整',
          '征求意见',
          '征求意见调整',
          '复核通过',
          '复核终止',
          '征求意见通过',
          '征求意见终止',
        ],
        queryForm: {
          reportname: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        // 筛选、表格头自定义
        filedAll: [
          { name: '报告时间' },
          { name: '报告版本' },
          { name: '报告方式' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'audit-implement-index-search',
        tableKey: 'audit-implement-index-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '报告名称', key: 'reportname' },
          { name: '日期', key: 'Date' },
        ]
        return fields
      },
      formatDay(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      resetQueryForm() {
        this.queryForm = {
          reportname: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await report({ ...other, startDate, endDate, type: 'nbsj' })
        this.list = list
        this.total = total
        this.listLoading = false
        this.setCheckedRows()
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await reportDetail({ reportid: row.reportid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        const data = await reportDetail({ reportid: row.reportid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await reportDel({
            reportid: row.reportid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      async handleExport(row) {
        const data = await reportExport({ reportid: row.reportid })
        let fileName = row.reportname + '.doc'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
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
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleFH(row) {
        auditFH({
          reportid: row.reportid,
        }).then((res) => {
          if (res.msg === '成功') {
            this.fetchData()
          }
        })
      },
      handleSP(row) {
        auditSP({
          reportid: row.reportid,
        }).then((res) => {
          if (res.msg === '成功') {
            this.fetchData()
          }
        })
      },
      handleZQYJ(row) {
        auditYJZJ({
          reportid: row.reportid,
        }).then((res) => {
          if (res.msg === '成功') {
            this.fetchData()
          }
        })
      },
      async exportData() {
        const data = await exportReportChuGao()
        let fileName = '报告编制初稿'
        let blob = new Blob([data], {
          type: 'application/msword',
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
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.reportid,
          tableId: 207,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      handleShenPi(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        try {
          this.$baseConfirm('你确定要审核当前项吗', null, async () => {
            this.btnLoading = true
            const tableId = 207
            const fromId = row.reportid
            this.$refs['process'].save(tableId, fromId)
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      handleSend() {
        if (this.select && this.select.length > 0) {
          this.$refs.manage.showEdit()
        } else {
          this.$baseMessage(
            '请选择需要下发的数据',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      async getChildlistPro(val) {
        const ids = this.select.map((res) => res.reportid)
        const titles = this.select.map((res) => res.reportname)
        const names = val.map((res) => res.staffid)
        const userNames = val.map((res) => res.realname)

        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'znsj',
            })
          }
        }
        //下发保存
        sjqzdXF({
          ids: ids.toString(),
          userids: names.toString(),
          usernames: userNames.toString(),
        })
        //下发通知
        xiafaListNew({
          tableId: '231',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.select = []
          }
        })
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.reportid == row.reportid)
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
            if (row && !this.select.some((x) => x.reportid == row.reportid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.reportid == row.reportid)
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
                return row.reportid == item.reportid
              }),
              true
            )
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
