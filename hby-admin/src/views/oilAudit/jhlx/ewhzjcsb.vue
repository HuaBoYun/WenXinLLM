<template>
  <!-- 工程项目验收计划 -->
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
              <el-date-picker
                v-if="item.name === '年份'"
                v-model="queryForm.queryYear"
                type="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="年份"
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
    <el-card shadow="never">
      <vab-query-form-right-panel :span="24">
        <!-- <el-tooltip
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
        </el-tooltip> -->
        <!-- <el-button type="success" @click="handleEdit(null)">新增</el-button> -->
        <!-- <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload> -->
        <el-button
          v-loading="exportLoading"
          type="success"
          icon="el-icon-download"
          @click="handleExport()"
        >
          导出
        </el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        :span-method="objectSpanMethod"
      >
        <el-table-column
          align="center"
          label="建设单位"
          prop="jsdw"
          width="200"
        />
        <el-table-column
          align="center"
          label="单位类别"
          prop="nwb"
          width="200"
        />
        <el-table-column
          align="center"
          label="抽审比例"
          prop="samplingRatio"
          width="200"
        />
        <el-table-column
          align="center"
          label="抽审项目数量"
          prop="esscjeCount"
          width="200"
        />
        <el-table-column
          align="center"
          label="抽审项目全额"
          prop="esscjeSum"
          width="200"
        />
        <el-table-column align="center" label="合计">
          <el-table-column
            align="center"
            label="项目数"
            prop="htbhCount"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额"
            prop="edjeSum"
            width="100"
          />
        </el-table-column>
        <el-table-column align="center" label="≥900万元">
          <el-table-column
            align="center"
            label="项目数"
            prop="countOverNineHund"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额"
            prop="sumOverNineHund"
            width="100"
          />
        </el-table-column>
        <el-table-column align="center" label="100~900万元">
          <el-table-column
            align="center"
            label="项目数"
            prop="countHundToNineHund"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额"
            prop="sumHundToNineHund"
            width="100"
          />
        </el-table-column>
        <el-table-column align="center" label="50~100万元">
          <el-table-column
            align="center"
            label="项目数"
            prop="countFiftyToHund"
          />
          <el-table-column
            align="center"
            label="金额"
            prop="sumFiftyToHund"
            width="100"
          />
        </el-table-column>
        <el-table-column align="center" label="20~50万元">
          <el-table-column
            align="center"
            label="项目数"
            prop="countTwentyToFifty"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额"
            prop="sumTwentyToFifty"
            width="100"
          />
        </el-table-column>
        <el-table-column align="center" label="20万元<">
          <el-table-column
            align="center"
            label="项目数"
            prop="countUnderTwenty"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额"
            prop="sumUnderTwenty"
            width="100"
          />
        </el-table-column>
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
  </div>
</template>

<script>
  import { exportEwhzjcsbList } from '@/oapi/audit/plan'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  export default {
    name: 'gcjgysjh',
    components: { filterSearch, filterTable },

    mixins: [searchTableMixis],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/gcxmjgysjh/importData',
        headers: { token },
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          queryYear: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          // { name: '建设单位' },
          // { name: '项目类别' },
          // { name: '项目总投资（万元）' },
          // { name: '项目投产时间' },
          // { name: '生产考核完成时间' },
          // { name: '专项验收' },
          // { name: '项目结算验收-计划完成时间' },
          // { name: '初步验收验收-计划完成时间' },
          // { name: '竣工验收验收-计划完成时间' },
          // { name: '备注' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-gcgl-gcjgysjh-search',
        tableKey: 'oilAudit-gcgl-gcjgysjh-list',
        searchMore: true,
        exportLoading: false,
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
        if (response.data) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg || '导入失败', 'error')
        }
      },
      async handleExport() {
        this.exportLoading = true
        try {
          // 构建导出参数,设置分页大小为20000
          const exportParams = {
            ...this.queryForm,
            pageSize: 20000,
            pageNumber: 1,
          }

          // 调用查询接口获取所有数据
          const {
            data: { listInto },
            code,
          } = await exportEwhzjcsbList(exportParams)

          if (code === 1 && listInto && listInto.length > 0) {
            // 与 fetchData 保持一致：每行数据完整，jsdw 不置空
            const flatData = listInto.flatMap((item) => {
              return item.list.map((subItem) => ({
                ...subItem,
                jsdw: item.jsdw,
              }))
            })

            // 动态导入excel工具
            import('@/utils/excel').then((excel) => {
              // 页面是2级表头，multiHeader 1行 + header 1行，共17列（A~Q）
              const multiHeader = [
                ['建设单位', '单位类别', '抽审比例', '抽审项目数量', '抽审项目全额', '合计', '', '≥900万元', '', '100~900万元', '', '50~100万元', '', '20~50万元', '', '20万元<', ''],
              ]
              const header = [
                '', '', '', '', '',
                '项目数', '金额',
                '项目数', '金额',
                '项目数', '金额',
                '项目数', '金额',
                '项目数', '金额',
                '项目数', '金额',
              ]

              // 定义字段映射（与 flatData 的 key 保持一致）
              const filterVal = [
                'jsdw', 'nwb', 'samplingRatio', 'esscjeCount', 'esscjeSum',
                'htbhCount', 'edjeSum',
                'countOverNineHund', 'sumOverNineHund',
                'countHundToNineHund', 'sumHundToNineHund',
                'countFiftyToHund', 'sumFiftyToHund',
                'countTwentyToFifty', 'sumTwentyToFifty',
                'countUnderTwenty', 'sumUnderTwenty',
              ]
              const data = this.formatJson(filterVal, flatData)

              // 表头共2行，数据从第3行开始
              // 表头合并：A~E 跨两行，F~Q 第1行横向合并
              const merges = [
                'A1:A2', 'B1:B2', 'C1:C2', 'D1:D2', 'E1:E2',
                'F1:G1', 'H1:I1', 'J1:K1', 'L1:M1', 'N1:O1', 'P1:Q1',
              ]

              // 动态计算 jsdw 列（A列）数据行的合并范围
              const headerRowCount = 2
              let currentRow = headerRowCount + 1
              listInto.forEach((item) => {
                const len = item.list.length
                if (len > 1) {
                  merges.push(`A${currentRow}:A${currentRow + len - 1}`)
                }
                currentRow += len
              })

              // 生成文件名(包含年份信息)
              const fileName = (this.queryForm.queryYear || '') + '额外资金结算上报'

              excel.export_json_to_excel({
                multiHeader,
                header,
                merges,
                data,
                filename: fileName,
                autoWidth: true,
                bookType: 'xlsx',
                border: true,
              })

              this.exportLoading = false
              this.$baseMessage('导出成功', 'success')
            })
          } else {
            this.exportLoading = false
            this.$baseMessage('没有可导出的数据', 'warning')
          }
        } catch (error) {
          this.exportLoading = false
          this.$baseMessage('导出失败: ' + (error.message || '未知错误'), 'error')
        }
      },
      formatJson(filterVal, jsonData) {
        return jsonData.map((v) =>
          filterVal.map((j) => {
            return v[j]
          })
        )
      },
      getFiled() {
        return [{ name: '年份', key: 'queryYear' }]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          queryYear: '',
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
        const {
          data: { listInto, totalRecord },
          code,
        } = await exportEwhzjcsbList(this.queryForm)
        if (code === 1) {
          const transformedData = listInto&&listInto.flatMap((item) => {
            return item.list.map((subItem) => ({
              ...subItem,
              jsdw: item.jsdw,
              listLength: item.list.length,
            }))
          })
          this.list = transformedData || []
          // this.total = totalRecord || 0
          this.listLoading = false
        }
      },
      objectSpanMethod({ row, column, rowIndex, columnIndex }) {
        if (columnIndex === 0) {
          const rows = this.list.filter((item) => item.jsdw === row.jsdw)
          const firstRow = rows[0]
          if (row === firstRow) {
            return {
              rowspan: rows.length,
              colspan: 1,
            }
          } else {
            return {
              rowspan: 0,
              colspan: 0,
            }
          }
        }
        // if (columnIndex === 0) {
        //   if (rowIndex % 2 === 0) {
        //     return {
        //       rowspan: 2,
        //       colspan: 1
        //     };
        //   } else {
        //     return {
        //       rowspan: 0,
        //       colspan: 0
        //     };
        //   }
        // }
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await engineeringProjectExaminePlanDelete({
            ids: row.gcxmjgysjhid,
          })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      async handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
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
