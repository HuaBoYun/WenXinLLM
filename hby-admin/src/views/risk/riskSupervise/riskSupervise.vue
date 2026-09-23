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
                v-model="queryForm.name"
                clearable
                v-if="item.name === '公司名称'"
                placeholder="公司名称"
              />

              <el-input
                v-model="queryForm.orgName"
                clearable
                v-if="item.name === '责任部门'"
                placeholder="责任部门"
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
            v-if="item.name === '分公司名称'"
            align="center"
            label="分公司名称"
            prop="branchName"
          />
          <el-table-column
            v-if="item.name === '责任部门'"
            align="center"
            label="责任部门"
            prop="orgName"
          />
          <el-table-column
            v-if="item.name === '总分'"
            align="center"
            label="总分"
            prop="scoreDetails"
          />
          <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            prop="status"
          />
        </div>

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
          width="120"
        >
          <template>
            <el-button type="text" @click="handleEdit(row)">详情</el-button>
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
      :visible.sync="superviseVisible"
      width="1000px"
      @close="
        superviseVisible = false
        fetchData()
      "
    >
      <div style="text-align: right; margin-bottom: 10px">
        <el-button type="success" @click="handleExportDetail()">导出</el-button>
      </div>

      <el-table
        v-loading="listLoading"
        :data="superviseList"
        v-if="superviseVisible"
        @select-all="handleSelectAll1"
        @select="handleSelection1"
        ref="multipleTable1"
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
        <el-table-column
          align="center"
          label="关联风险点"
          prop="risknumber"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="分公司名称"
          prop="branchName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="责任部门"
          prop="orgName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="年份"
          prop="years"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="月份"
          prop="month"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="是否上报"
          prop="isReport"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="扣分情况"
          prop="scoreDetails"
          show-overflow-tooltip
        />
        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
          width="120"
        >
          <template>
            <el-button type="text" @click="modifyRating(row)">改分</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        :current-page="dialogQueryForm.pageNumber"
        :layout="layout"
        :page-size="dialogQueryForm.pageSize"
        :total="dialogTotal"
        @current-change="handleDialogCurrentChange"
        @size-change="handleDialogSizeChange"
      />
    </el-dialog>
    <RatingDialog ref="rating" @fetch-data="handleEdit(fatherData)" />
  </div>
</template>

<script>
  import {
    riskImprovementList,
    riskImprovementDetailsList,
    exportFXJDGJList,
    exportFXJDGJDetailList,
  } from '@/api/risk/riskfill'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  const { baseURL } = require('@/config')
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import RatingDialog from './components/ratingDialog.vue'
  export default {
    name: 'valueList',
    components: { filterSearch, filterTable, RatingDialog },
    mixins: [searchTableMixis],
    data() {
      return {
        listLoading: false,
        dialogTitle: '',
        superviseVisible: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        formDisabled: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          implementId: '',
          name: '',
          orgName: '',
        },
        total: 0,
        dialogQueryForm: {
          pageNumber: 1,
          pageSize: 10, // 弹窗内表格的每页数量，可以根据需要调整
        },
        dialogTotal: 0,
        curRow: null,
        filedAll: [
          { name: '分公司名称' },
          { name: '责任部门' },
          { name: '总分' },
          // { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-fxgk-riskSupervise-search',
        tableKey: 'oilAudit-fxgk-riskSupervise-list',
        searchMore: true,
        superviseList: [],
        select: [],
        select1: [],
        detailId: '',
        orgName: '',
        fatherData: {},
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
          { name: '公司名称', key: 'name' },
          { name: '责任部门', key: 'orgName' },
        ]
      },
      resetSearch() {
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.queryForm.implementId = ''
        this.queryForm.name = ''
        this.queryForm.orgName = ''
        // 重置时清空已选择的数据
        this.select = []
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        riskImprovementList,
          riskImprovementList(this.queryForm)
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
                // 翻页后回显已勾选的数据
                this.setCheckedRows()
              }
            })
            .finally(() => {
              this.listLoading = false
            })
      },
      async handleEdit(row) {
        this.fatherData = row
        this.detailId = row.branchId
        this.orgName = row.orgName
        if (row) {
          this.dialogTitle = '详情'
          this.dialogQueryForm.pageNumber = 1 // 每次打开弹窗时重置页码到第一页
          this.select1 = [] // 清空详情页面的选择数据
          await this.fetchDialogData(row)
          this.superviseVisible = true
        }
      },
      async fetchDialogData(row) {
        this.listLoading = true // 可以考虑为弹窗表格设置独立的loading状态
        try {
          const res = await riskImprovementDetailsList({
            pageNumber: this.dialogQueryForm.pageNumber,
            pageSize: this.dialogQueryForm.pageSize,
            riskImplementID: row.branchId || 1000,
            orgName: row.orgName,
          })
          if (
            res &&
            res.data &&
            res.data.data &&
            res.data.data.pageInfo &&
            res.data.data.pageInfo.tlist
          ) {
            this.superviseList = res.data.data.pageInfo.tlist
            this.dialogTotal = res.data.data.pageInfo.totalRecord
            // 翻页后回显已勾选的数据
            this.setCheckedRows1()
          }
        } catch (error) {
          console.error('Failed to fetch dialog data:', error)
          this.superviseList = []
          this.dialogTotal = 0
        } finally {
          this.listLoading = false
        }
      },
      handleDialogSizeChange(val) {
        this.dialogQueryForm.pageSize = val
        this.fetchDialogData(this.fatherData) // 使用保存的fatherData重新获取数据
      },
      handleDialogCurrentChange(val) {
        this.dialogQueryForm.pageNumber = val
        this.fetchDialogData(this.fatherData) // 使用保存的fatherData重新获取数据
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },

      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      //导出
      async handleExport() {
        // 检查是否有选中的数据
        if (this.select.length === 0) {
          this.$message.warning('请先选择要导出的数据')
          return
        }

        // 按照指定格式组织导出数据
        const exportData = this.select.map((item) => ({
          branchName: item.branchName || '',
          orgName: item.orgName || '',
          score: item.scoreDetails || '',
        }))

        try {
          const data = await exportFXJDGJList({
            jsonString: JSON.stringify(exportData),
            name: this.queryForm.name,
            orgName: this.queryForm.orgName,
          })
          let fileName = '风险监督改进'
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

          // 导出成功后清空选中数据和表格选中状态
          this.select = []
          this.$refs.multipleTable.clearSelection()
          this.$message.success('导出成功')
        } catch (error) {
          console.error('导出失败:', error)
          this.$message.error('导出失败，请稍后重试')
        }
      },
      //导出
      async handleExportDetail() {
        const ids = this.select1.map((res) => res.id)
        const data = await exportFXJDGJDetailList({
          ids: ids.toString(),
          riskImplementID: this.detailId,
          orgName: this.orgName,
        })
        let fileName = '风险监督改进详情'
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
      handleSelection1(val, row) {
        const i = this.select1.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select1.push(row)
        } else {
          this.select1.splice(i, 1)
        }
      },
      handleSelectAll1(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select1.some((x) => x.id == row.id)) {
              this.select1.push(row)
            }
          })
        } else {
          this.superviseList.map((row) => {
            const i = this.select1.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select1.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows1() {
        this.$nextTick(() => {
          this.select1.forEach((row) => {
            this.$refs.multipleTable1.toggleRowSelection(
              this.superviseList.find((item) => {
                // 修改为 this.superviseList
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      //修改评分
      modifyRating(val) {
        this.$refs.rating.show(val)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
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
