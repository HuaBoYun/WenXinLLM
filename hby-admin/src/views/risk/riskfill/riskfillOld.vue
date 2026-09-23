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
                v-model="queryForm.entName"
                clearable
                v-if="item.name === '涉及企业名称'"
                placeholder="涉及企业名称"
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
        <el-button type="success" @click="handleEdit(null)">新增</el-button>
        <el-button type="success" @click="handleExport()">导出</el-button>
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
            v-if="item.name === '涉及企业名称'"
            align="center"
            label="涉及企业名称"
            prop="entName"
          />
          <el-table-column
            v-if="item.name === '涉及企业层级'"
            align="center"
            label="涉及企业层级"
            prop="entLevel"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险事件名称'"
            align="center"
            label="风险事件名称"
            prop="riskName"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '风险类别'"
            align="center"
            label="风险类别"
            prop="riskType"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '事件发生时间'"
            align="center"
            label="事件发生时间"
            prop="eveTime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '当期情况描述'"
            align="center"
            label="当期情况描述"
            prop="sitDetails"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '损失（风险）金额（万元）'"
            align="center"
            label="损失（风险）金额（万元）"
            prop="lossAmount"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '处理进展情况'"
            align="center"
            label="处理进展情况"
            prop="disSituation"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '是否涉诉'"
            align="center"
            label="是否涉诉"
            prop="isIvn"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '是否境外'"
            align="center"
            label="是否境外"
            prop="isOver"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '备注'"
            align="center"
            label="备注"
            prop="remarks"
            show-overflow-tooltip
          />
        </div>

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
          width="130"
        >
          <template>
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  @click.native="handlePush(row)"
                  :disabled="!!row.status"
                >
                  <el-button type="text" :disabled="!!row.status">
                    下发
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handlePushList(row)">
                  <el-button type="text">下发记录</el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDelete(row)">
                  <el-button type="text">删除</el-button>
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
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="dialogTitle"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="dialogFormVisible = false"
    >
      <riskfillEdit
        v-if="dialogFormVisible"
        :curRow="curRow"
        @fetchData="fetchData"
        @close="dialogFormVisible = false"
      />
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      title="下发"
      :visible.sync="riskfillEdit2Visable"
      width="1000px"
      @close="riskfillEdit2Visable = false"
    >
      <riskfillEdit2
        v-if="riskfillEdit2Visable"
        :curRow="curRow"
        @fetchData="fetchData"
        @close="riskfillEdit2Visable = false"
      />
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      title="下发记录"
      :visible.sync="riskvalueListVisable"
      width="1000px"
      @close="riskvalueListVisable = false"
    >
      <riskvalueList
        v-if="riskvalueListVisable"
        :curRow="curRow"
        :pushId="pushId"
        @fetchData="fetchData"
        @close="riskfillEdit2Visable = false"
      />
    </el-dialog>
    <lxjybSelectModal @projectManage="getChildlistPro" ref="person" />
  </div>
</template>

<script>
  import lxjybSelectModal from '@/components/selectPerson'
  import riskfillEdit from '@/views/risk/riskfill/riskfillEdit'
  import riskfillEdit2 from '@/views/risk/riskfill/riskfillEdit2'
  import riskvalueList from '@/views/risk/riskfill/riskvalueList'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import {
    riskReportingList,
    riskReportingDelete,
    exportList,
  } from '@/api/risk/riskfill'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'

  export default {
    name: 'Download',
    components: {
      lxjybSelectModal,
      riskfillEdit,
      riskfillEdit2,
      riskvalueList,
      filterSearch,
      filterTable,
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
          entName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '涉及企业名称' },
          { name: '涉及企业层级' },
          { name: '风险事件名称' },
          { name: '风险类别' },
          { name: '事件发生时间' },
          { name: '当期情况描述' },
          { name: '损失（风险）金额（万元）' },
          { name: '处理进展情况' },
          { name: '是否涉诉' },
          { name: '是否境外' },
          { name: '备注' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-fxgk-riskfill-search',
        tableKey: 'oilAudit-fxgk-riskfill-list',
        searchMore: true,
        dialogTitle: '新增',
        dialogFormVisible: false,
        riskfillEdit2Visable: false,
        riskvalueListVisable: false,
        curRow: null,
        pushId: '', //列表id
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
      handlePushList(row) {
        this.pushId = row.id //列表id
        this.riskvalueListVisable = true
      },
      getFiled() {
        return [{ name: '涉及企业名称', key: 'entName' }]
      },
      resetQueryForm() {
        this.queryForm = {
          entName: '',
          pageNumber: 1,
          pageSize: 20,
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
        riskReportingList(this.queryForm)
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
        const data = await exportList({
          id: ids.toString(),
          ...this.queryForm,
        })
        let fileName = '重大风险事件填报'
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
