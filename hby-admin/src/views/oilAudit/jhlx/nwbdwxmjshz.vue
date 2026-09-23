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
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="单位" prop="jsdw" width="200" />
        <el-table-column align="center" label="总计">
          <el-table-column
            align="center"
            label="数量"
            prop="htbhCount"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额（万元）"
            prop="edjeSum"
            width="130"
          />
        </el-table-column>
        <el-table-column align="center" label="抽审合计">
          <el-table-column
            align="center"
            label="数量"
            prop="esscjeCount"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额（万元）"
            prop="esscjeSum"
            width="130"
          />
        </el-table-column>
        <el-table-column align="center" label="外部单位">
          <el-table-column
            align="center"
            label="数量"
            prop="externalunitCount"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额（万元）"
            prop="externalunitSum"
            width="130"
          />
        </el-table-column>
        <el-table-column align="center" label="内部单位">
          <el-table-column align="center" label="内部单位小计">
            <el-table-column
              align="center"
              label="数量"
              prop="tengfeIorinternalCount"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="金额（万元）"
              prop="tengfeIorinternalSum"
              width="130"
            />
          </el-table-column>
          <el-table-column align="center" label="腾飞">
            <el-table-column
              align="center"
              label="数量"
              prop="tengfeiCount"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="金额（万元）"
              prop="tengfeiSum"
              width="130"
            />
          </el-table-column>
          <el-table-column align="center" label="内部多经单位">
            <el-table-column
              align="center"
              label="数量"
              prop="internalmultipathCount"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="金额（万元）"
              prop="internalmultipathSum"
              width="130"
            />
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="工程建设公司">
          <el-table-column
            align="center"
            label="数量"
            prop="engineeringconstructionCount"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="金额（万元）"
            prop="engineeringconstructionSum"
            width="130"
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
  import { exportNwbdwxmjshzList } from '@/oapi/audit/plan'
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
          } = await exportNwbdwxmjshzList(exportParams)

          if (code === 1 && listInto && listInto.length > 0) {
            // 动态导入excel工具
            import('@/utils/excel').then((excel) => {
              // 定义多级表头（共15列：A~O）
              // 第2行为空且需要向上合并的列，将第3行(header)的值提前填入第2行，
              // 这样合并后左上角单元格有值才能正常显示
              const multiHeader = [
                ['单位', '总计', '', '抽审合计', '', '外部单位', '', '内部单位', '', '', '', '', '', '工程建设公司', ''],
                ['', '数量', '金额（万元）', '数量', '金额（万元）', '数量', '金额（万元）', '内部单位小计', '', '腾飞', '', '内部多经单位', '', '数量', '金额（万元）'],
              ]
              const header = [
                '',
                '',
                '',
                '',
                '',
                '',
                '',
                '数量',
                '金额（万元）',
                '数量',
                '金额（万元）',
                '数量',
                '金额（万元）',
                '',
                '',
              ]

              // 定义字段映射
              const filterVal = [
                'jsdw',
                'htbhCount',
                'edjeSum',
                'esscjeCount',
                'esscjeSum',
                'externalunitCount',
                'externalunitSum',
                'tengfeIorinternalCount',
                'tengfeIorinternalSum',
                'tengfeiCount',
                'tengfeiSum',
                'internalmultipathCount',
                'internalmultipathSum',
                'engineeringconstructionCount',
                'engineeringconstructionSum',
              ]
              const data = this.formatJson(filterVal, listInto)

              // 定义合并单元格（行号基于实际写入顺序：第1行=multiHeader[0], 第2行=multiHeader[1], 第3行=header）
              const merges = [
                'A1:A3', // 单位(跨三行)
                // 第1行横向合并
                'B1:C1', // 总计
                'D1:E1', // 抽审合计
                'F1:G1', // 外部单位
                'H1:M1', // 内部单位
                'N1:O1', // 工程建设公司
                // 第2行横向合并（有值的分组）
                'H2:I2', // 内部单位小计
                'J2:K2', // 腾飞
                'L2:M2', // 内部多经单位
                // 第2行为空的列，第2~3行纵向合并（向上合并）
                'B2:B3', // 总计-数量
                'C2:C3', // 总计-金额
                'D2:D3', // 抽审合计-数量
                'E2:E3', // 抽审合计-金额
                'F2:F3', // 外部单位-数量
                'G2:G3', // 外部单位-金额
                'N2:N3', // 工程建设公司-数量
                'O2:O3', // 工程建设公司-金额
              ]

              // 生成文件名(包含年份信息)
              const fileName =
                (this.queryForm.queryYear || '') + '内外部单位项目结算汇总'

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
        } = await exportNwbdwxmjshzList(this.queryForm)
        if (code === 1) {
          this.list = listInto || []
          // this.total = totalRecord || 0
          this.listLoading = false
        }
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
