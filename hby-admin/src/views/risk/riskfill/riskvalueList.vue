<template>
  <div class="system-log-container1">
    <div style="text-align: right; margin-bottom: 20px">
      <el-button @click="handleExport()" type="success">导出</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      ref="multipleTable"
      @select-all="handleSelectAll"
      @select="handleSelection"
    >
      <el-table-column
        width="48"
        type="selection"
        :reserve-selection="true"
      ></el-table-column>
      <el-table-column
        align="center"
        label="序号"
        width="100"
        type="index"
      ></el-table-column>
      <el-table-column align="center" label="风险名称" prop="impRiskName" />
      <el-table-column
        align="center"
        label="牵头领导"
        prop="impWayStaffName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="牵头责任部门"
        prop="impWayDeptName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="公司相关责任单位"
        prop="impDutyUnitName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="风险描述"
        prop="impRiskDetails"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="本季度风险防控情况"
        prop="impThisControl"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="已发生的风险事件及应对处置情况"
        prop="impSolutions"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="需要提示的问题和风险"
        prop="impTips"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="下季度主要风险研判及相应防控措施"
        prop="impTextControl"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="其他需要说明的情况"
        prop="impOther"
        show-overflow-tooltip
      />

      <el-table-column
        label="操作"
        #default="{ row }"
        fixed="right"
        align="center"
        width="150"
      >
        <el-button
          type="text"
          @click="handleValue(row)"
          :disabled="!!row.status"
        >
          查看评估列表
        </el-button>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      title="评估列表"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="dialogFormVisible = false"
    >
      <valueList
        v-if="dialogFormVisible"
        :riskData="curRow"
        @fetchData="fetchData"
        @close="dialogFormVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script>
  import riskfillEdit from '@/views/risk/riskfill/riskfillEdit'
  import valueList from '@/views/risk/riskvalue/valueList'
  import { formatDate } from '@/utils/index'
  import {
    issuedImplementList,
    riskReportingDelete,
    exportXiafaList,
  } from '@/api/risk/riskfill'

  export default {
    name: 'Download',
    components: { valueList },
    props: {
      pushId: {
        type: { String, Number },
        default: '',
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          impRiskName: '',
          pageNumber: 1,
          pageSize: 20,
          reportingId: '',
        },
        dialogTitle: '新增',
        dialogFormVisible: false,
        curRow: null,
        select: [],
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      resetQueryForm() {
        this.queryForm = {
          impRiskName: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        // 清空已选择的数据
        this.select = []
        // 清空表格选择状态
        if (this.$refs.multipleTable) {
          this.$refs.multipleTable.clearSelection()
        }
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
        // 清空已选择的数据
        this.select = []
        // 清空表格选择状态
        if (this.$refs.multipleTable) {
          this.$refs.multipleTable.clearSelection()
        }
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        issuedImplementList({ ...this.queryForm, reportingId: this.pushId })
          .then((res) => {
            if (
              res &&
              res.data &&
              res.data.data &&
              res.data.data.pageInfo &&
              res.data.data.pageInfo.tlist
            ) {
              this.list = res.data.data.pageInfo.tlist
              this.total = res.data.data.pageInfo.totalRecord
            }
            this.setCheckedRows()
          })
          .finally(() => {
            this.listLoading = false
          })
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, true)
      },
      handleValue(row) {
        this.$router.push({
          path: '/riskfill/riskvalueList',
          query: {
            implementId: row.id,
            disabled: true,
          },
          params: {
            implementId: row.id,
            disabled: true,
          },
        })
        // this.curRow = row
        // this.curRow.disabled = true
        // this.dialogFormVisible = true
      },

      //导出
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const data = await exportXiafaList({
          id: ids.toString(),
          reportingId: this.pushId,
        })
        let fileName = '重大风险汇总'
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
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    // background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
