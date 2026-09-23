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
                v-model="queryForm.contractcode"
                clearable
                placeholder="合同编号"
                v-if="item.name === '合同编号'"
              />
              <el-input
                v-model="queryForm.contractname"
                clearable
                placeholder="工程名称"
                v-if="item.name === '工程名称'"
              />
              <el-input
                v-model="queryForm.contractmoney"
                clearable
                placeholder="报审金额"
                v-if="item.name === '报审金额'"
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
        <!-- <el-button type="success" @click="handleAdd(null)">新增</el-button> -->
        <el-button type="success" @click="handleExport">导出</el-button>
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
          label="序号"
          type="index"
          fixed="left"
          width="80"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="合同编号"
            prop="contractcode"
            v-if="item.name === '合同编号'"
            width="170"
          >
            <!-- <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.contractcode }}
              </el-button>
            </template> -->
          </el-table-column>

          <el-table-column
            align="center"
            label="工程名称"
            v-if="item.name === '工程名称'"
            prop="contractname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="施工单位"
            v-if="item.name === '施工单位'"
            prop="sgorgname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="报审金额"
            v-if="item.name === '报审金额'"
            prop="contractmoney"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审定金额"
            v-if="item.name === '审定金额'"
            prop="sdmoney"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审减金额"
            v-if="item.name === '审减金额'"
            prop="hjmoney"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审减率"
            v-if="item.name === '审减率'"
            prop="hjl"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="工程量计算"
            v-if="item.name === '工程量计算'"
            prop="gcljs"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="定额套用"
            v-if="item.name === '定额套用'"
            prop="dety"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="现场实例"
            v-if="item.name === '现场实例'"
            prop="xcsc"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="物资价格"
            v-if="item.name === '物资价格'"
            prop="wzjg"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="其它审减"
            v-if="item.name === '其它审减'"
            prop="qtsj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审计人员"
            v-if="item.name === '审计人员'"
            prop="rwnames"
            show-overflow-tooltip
          />
        </div>

        <!-- <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </el-card>

    <!-- <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
    <gcsjxmapbEdit ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { sjglGetList, sjsjExport } from '@/oapi/audit/report'
  import gcsjxmapbEdit from './components/sjsjnrView.vue'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    name: 'gcsjxmapb',
    components: {
      filterSearch,
      filterTable,
      gcsjxmapbEdit,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          contractcode: '',
          contractname: '',
          contractmoney: '',
          // pageNumber: 1,
          // pageSize: 20,
        },
        filedAll: [
          { name: '合同编号' },
          { name: '工程名称' },
          { name: '施工单位' },
          { name: '报审金额' },
          { name: '审定金额' },
          { name: '审减金额' },
          { name: '审减率' },
          { name: '工程量计算' },
          { name: '定额套用' },
          { name: '现场实例' },
          { name: '物资价格' },
          { name: '其它审减' },
          { name: '审计人员' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-gcgl-gcxmzj-search',
        tableKey: 'oilAudit-gcgl-gcxmzj-list',
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
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) this.fetchData()
      })
    },
    methods: {
      getFiled() {
        return [
          { name: '合同编号', key: 'contractcode' },
          { name: '工程名称', key: 'contractname' },
          { name: '报审金额', key: 'contractmoney' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          contractcode: '',
          contractname: '',
          contractmoney: '',
          // pageNumber: 1,
          // pageSize: 20,
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
        const data = await sjglGetList(this.queryForm)
        this.list = data.data.data || []
        this.listLoading = false
        this.setCheckedRows()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      async handleExport() {
        const ids = this.select.map((res) => res.resultid)
        const data = await sjsjExport({
          ...this.queryForm,
          idList: ids.toString(),
        })
        let fileName = '审计审减内容'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
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
        const i = this.select.findIndex((x) => x.resultid == row.resultid)
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
            if (row && !this.select.some((x) => x.resultid == row.resultid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.resultid == row.resultid)
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
                return row.resultid == item.resultid
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
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
