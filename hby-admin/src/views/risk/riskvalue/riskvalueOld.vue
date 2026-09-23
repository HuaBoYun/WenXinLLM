<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
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
                v-model="queryForm.impRiskName"
                clearable
                v-if="item.name === '风险名称'"
                placeholder="风险名称"
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
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>
    <el-card shadow="never">
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
        <el-button type="success" @click="handleExport()">导出</el-button>
        <!-- <el-button type="success" @click="handleEdit(null)">新增</el-button> -->
        <!-- <el-button type="success" @click="hadnlePush">下发</el-button> -->
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
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

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '风险名称'"
            align="center"
            label="风险名称"
            prop="impRiskName"
          />
          <el-table-column
            v-if="item.name === '牵头领导'"
            align="center"
            label="牵头领导"
            prop="impWayStaffName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '牵头责任部门'"
            align="center"
            label="牵头责任部门"
            prop="impWayDeptName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '公司相关责任单位'"
            align="center"
            label="公司相关责任单位"
            prop="impDutyUnitName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险描述'"
            align="center"
            label="风险描述"
            prop="impRiskDetails"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '本季度风险防控情况'"
            align="center"
            label="本季度风险防控情况"
            prop="impThisControl"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '已发生的风险事件及应对处置情况'"
            align="center"
            label="已发生的风险事件及应对处置情况"
            prop="impSolutions"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '需要提示的问题和风险'"
            align="center"
            label="需要提示的问题和风险"
            prop="impTips"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '下季度主要风险研判及相应防控措施'"
            align="center"
            label="下季度主要风险研判及相应防控措施"
            prop="impTextControl"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '其他需要说明的情况'"
            align="center"
            label="其他需要说明的情况"
            prop="impOther"
            show-overflow-tooltip
          />
        </div>

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
          width="120"
        >
          <el-button
            type="text"
            @click="handleValue(row)"
            :disabled="!!row.status"
          >
            查看评估列表
          </el-button>

          <!-- <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="!!row.status"
          >
            修改
          </el-button> -->
          <!-- <el-button
            type="text"
            @click="handlePush(row)"
            :disabled="!!row.status"
          >
            下发
          </el-button> -->
          <!-- <el-button
            type="text"
            @click.native="handleDelete(row)"
            :disabled="!!row.status"
          >
            删除
          </el-button> -->
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
    <lxjybSelectModal @projectManage="getChildlistPro" ref="person" />
  </div>
</template>

<script>
  import lxjybSelectModal from '@/components/selectPerson'
  import riskfillEdit from '@/views/risk/riskfill/riskfillEdit'
  import valueList from '@/views/risk/riskvalue/valueList'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import {
    issuedImplementList,
    riskReportingDelete,
    exportZDFXYDList,
  } from '@/api/risk/riskfill'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'

  export default {
    name: 'Download',
    components: {
      lxjybSelectModal,
      filterSearch,
      filterTable,
      valueList,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/require/suggestion/import?isCover=1',
        headers: { token },
        select: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        isCover: 0,
        queryForm: {
          impRiskName: '',
          pageNumber: 1,
          pageSize: 20,
          reportingld: '',
        },
        filedAll: [
          { name: '风险名称' },
          { name: '牵头领导' },
          { name: '牵头责任部门' },
          { name: '公司相关责任单位' },
          { name: '风险描述' },
          { name: '本季度风险防控情况' },
          { name: '已发生的风险事件及应对处置情况' },
          { name: '需要提示的问题和风险' },
          { name: '下季度主要风险研判及相应防控措施' },
          { name: '其他需要说明的情况' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-fxgk-riskvalue-search',
        tableKey: 'oilAudit-fxgk-riskvalue-list',
        searchMore: true,
        dialogTitle: '新增',
        dialogFormVisible: false,
        riskfillEdit2Visable: false,
        curRow: null,
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
      handleSuccess(response) {
        if (response.code == 1) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      handlePush(row) {
        this.curRow = JSON.parse(JSON.stringify(row))
        this.riskfillEdit2Visable = true
        // if (this.select && this.select.length > 0) {
        //   this.$refs.person.showEdit()
        // } else {
        //   this.$baseMessage(
        //     '请选择需要下发的数据',
        //     'error',
        //     'vab-hey-message-error'
        //   )
        // }
      },
      getFiled() {
        return [{ name: '风险名称', key: 'impRiskName' }]
      },
      resetQueryForm() {
        this.queryForm = {
          impRiskName: '',
          pageNumber: 1,
          pageSize: 20,
          reportingld: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        issuedImplementList(this.queryForm)
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
          },
        })
        // this.curRow = JSON.parse(JSON.stringify(row))
        // this.dialogFormVisible = true
      },
      async handleEdit(row, disabled) {
        this.curRow = JSON.parse(JSON.stringify(row))
        if (row && disabled) {
          this.dialogTitle = '详情'
          this.curRow.disabled = true
        } else if (row && !disabled) {
          this.dialogTitle = '修改'
        } else {
          this.dialogTitle = '新增'
        }
        this.dialogFormVisible = true
        // this.$refs['edit'].showEdit(row, disabled)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.listLoading = true
          const res = await riskReportingDelete({ id: row.id })
          this.listLoading = false
          if (res && res.code == 1) {
            this.$message.success('操作成功！')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败！')
          }
        })
      },
      async getChildlistPro(val) {
        const ids = this.select.map((res) => res.id)
        const titles = this.select.map((res) => res.concernsContent)
        const names = val.map((res) => res.staffid)

        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'yqns',
            })
          }
        }
        //下发保存
        // xqjybXf({
        //   ids: ids.toString(),
        //   personIds: names.toString(),
        // })
        //下发通知
        xiafaListNew({
          tableId: '1386',
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
      //导出
      async handleExport(val) {
        const ids = this.select.map((res) => res.id)
        const data = await exportZDFXYDList({
          id: ids.toString(),
          ...this.queryForm,
        })
        let fileName = '重大风险月度评估'
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

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
