<template>
  <el-dialog
    title="重大风险填报"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :append-to-body="true"
    @close="close"
  >
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
        <vab-query-form>
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
            <el-button
              v-if="createId == createstaffid"
              :disabled="select.length == 0"
              @click="transfer()"
              type="primary"
            >
              转派
            </el-button>
            <!-- <el-button type="success" @click="handleExport()">导出</el-button> -->
            <!-- <el-button type="success" @click="handleEdit(null)">新增</el-button> -->
            <!-- <el-button type="success" @click="hadnlePush">下发</el-button> -->
          </vab-query-form-right-panel>
        </vab-query-form>
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
            label="风险名称"
            prop="impRiskName"
            #default="{ row }"
            width="200"
          >
            <el-button type="text" @click="handleEdit(row, 'detail')">
              {{ row.impRiskName }}
            </el-button>
          </el-table-column>
          <el-table-column width="1"></el-table-column>
          <div v-for="(item, index) in filedNow" :key="index">
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
              v-if="item.name === '重点事项'"
              align="center"
              label="重点事项"
              prop="impKeyIssues"
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
              :label="getThisControlLabel"
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
              v-if="
                item.name === '下季度主要风险研判及相应防控措施' &&
                !isFourthQuarter
              "
              align="center"
              :label="getNextControlLabel"
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
            v-if="!isTracking"
          >
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.toreport == 1"
            >
              填报
            </el-button>

            <!-- <el-button
              type="text"
              @click.native="handleDelete(row)"
              :disabled="row.toreport == 1"
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
        :title="dialogTitle"
        :visible.sync="riskfillEdit2Visable"
        width="1000px"
        @close="riskfillEdit2Visable = false"
      >
        <riskfillEdit2
          v-if="riskfillEdit2Visable"
          :curRow="curRow"
          @fetchData="fetchData"
          @close="riskfillEdit2Visable = false"
          :isEdit="isEdit"
        />
      </el-dialog>
    </div>
    <executor-options
      ref="executor"
      @projectManage="handleExecutor"
      :secrectLevelId="secrectLevelId"
    />
  </el-dialog>
</template>

<script>
  import lxjybSelectModal from '@/components/selectPerson'
  import valueList from '@/views/risk/riskvalue/valueList'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import {
    issuedImplementDelete,
    exportXiafaList,
    reportToLeader,
  } from '@/api/risk/riskfill'
  import { getInsideList, majorTransfer } from '@/api/risk/create'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import riskfillEdit2 from '@/views/risk/riskfill/riskfillEdit2'
  import ExecutorOptions from '@/components/danxuanPerson.vue'

  export default {
    name: 'Download',
    components: {
      lxjybSelectModal,
      filterSearch,
      filterTable,
      valueList,
      riskfillEdit2,
      ExecutorOptions,
    },
    mixins: [searchTableMixis],
    props: {
      isTracking: {
        type: Boolean,
        default: false,
      },
    },
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
          { name: '重点事项' },
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
        riskfillEdit2Visable: false,
        curRow: null,
        dialogVisible: false,
        majorid: '',
        isEdit: false,
        secrectLevelId: '',
        createstaffid: '',
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
        parentQuarterName: '', // 存储父组件传来的季度名称
      }
    },
    computed: {
      // 当前季度风险防控情况标签
      getThisControlLabel() {
        if (!this.parentQuarterName) return '本季度风险防控情况'

        if (this.parentQuarterName.includes('一季度')) {
          return '本季度风险防控情况'
        } else if (this.parentQuarterName.includes('二季度')) {
          return '上半年风险防控情况'
        } else if (this.parentQuarterName.includes('三季度')) {
          return '第三季度风险防控情况'
        } else if (this.parentQuarterName.includes('四季度')) {
          return '第四季度风险防控情况'
        }
        return '本季度风险防控情况'
      },

      // 下季度主要风险研判标签
      getNextControlLabel() {
        if (!this.parentQuarterName) return '下季度主要风险研判及相应防护措施'

        if (this.parentQuarterName.includes('一季度')) {
          return '下季度主要风险研判及相应防护措施'
        } else if (this.parentQuarterName.includes('二季度')) {
          return '下半年主要风险研判及相应防护措施'
        } else if (this.parentQuarterName.includes('三季度')) {
          return '下季度主要风险研判及相应防护措施'
        }
        return '下季度主要风险研判及相应防护措施'
      },

      // 是否是第四季度
      isFourthQuarter() {
        return (
          this.parentQuarterName && this.parentQuarterName.includes('四季度')
        )
      },
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      showEdit(row) {
        this.majorid = row.id
        this.secrectLevelId = row.secrectlevelid
        this.createstaffid = row.createstaffid
        this.curRow = row // 保存当前行数据
        this.parentQuarterName = row.quartername // 从父组件获取季度名称
        this.dialogVisible = true
        this.fetchData()
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      async handlePush(row) {
        this.$confirm('确定上报吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          reportToLeader({ id: row.id }).then((res) => {
            if (res.code == 1) {
              this.$baseMessage('上报成功', 'success')
              this.fetchData()
            }
          })
        })
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
        getInsideList({ ...this.queryForm, majorid: this.majorid })
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
        // 将父组件的季度信息传递给子组件
        this.curRow.quartername = this.parentQuarterName
        this.isEdit = false
        if (row && disabled) {
          this.dialogTitle = '详情'
          this.curRow.disabled = true
        } else if (row && !disabled) {
          this.dialogTitle = '修改'
          this.isEdit = true
        } else {
          this.dialogTitle = '新增'
        }
        this.riskfillEdit2Visable = true
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.listLoading = true
          const res = await issuedImplementDelete({ id: row.id })
          this.listLoading = false
          if (res && res.code == 1) {
            this.$message.success('操作成功！')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败！')
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
        const data = await exportXiafaList({
          id: ids.toString(),
          ...this.queryForm,
        })
        let fileName = '重大风险填报'
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
      close() {
        this.dialogVisible = false
        this.secrectLevelId = ''
        this.fetchData()
        this.select = []
        this.$nextTick(() => {
          this.$refs.multipleTable && this.$refs.multipleTable.clearSelection()
        })
      },
      // 转派
      transfer() {
        // this.id = row.id
        this.$refs['executor'].showEdit()
      },

      async handleExecutor(val) {
        const ids = this.select.map((res) => res.id)
        const { code } = await majorTransfer({
          ids: ids.toString(),
          staffId: val[0].staffid,
        })
        if (code == 1) this.$message.success('转派成功')
        this.select = []
        this.$nextTick(() => {
          this.$refs.multipleTable && this.$refs.multipleTable.clearSelection()
        })
        this.fetchData()
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
