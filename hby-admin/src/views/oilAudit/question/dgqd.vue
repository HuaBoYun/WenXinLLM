<template>
  <div class="">
    <el-card shadow="never">
      <vab-query-form>
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="item.name + '_' + index"
            >
              <el-input
                v-model="queryForm.draftNumber"
                clearable
                placeholder="底稿编号"
                v-if="item.name === '底稿编号'"
              />
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="审计项目名称"
                v-if="item.name === '审计项目名称'"
              />
              <el-input
                v-model="queryForm.createUserName"
                clearable
                placeholder="拟稿人"
                v-if="item.name === '拟稿人'"
                :style="{ width: '256px' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.executor.showEdit()"
                v-if="item.name === '拟稿人'"
              >
                选择
              </el-button>
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
      </vab-query-form>
    </el-card>
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
        <el-button type="success" size="mini" @click="handleExport">
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
          label="序号"
          type="index"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="审计组填报"
          prop="sheetCode"
          width="170"
        >
          <el-table-column
            align="center"
            label="填报时间"
            prop="createTime"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="项目名称"
            prop="projectName"
            width="170"
          />
          <el-table-column
            align="center"
            label="被审计单位"
            prop="auditeeName"
            width="170"
          />
          <el-table-column
            align="center"
            label="底稿编号"
            prop="draftNumber"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计查证事实"
            prop="verificationDescription"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计结论及依据"
            prop="auditConclusion"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计处理意见及建议"
            prop="handlingOpinions"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="编制人"
            prop="sheetCode"
            width="170"
          ></el-table-column>
        </el-table-column>
        <el-table-column align="center" label="专业科室审核意见">
          <el-table-column
            align="center"
            label="审核时间"
            prop="zyksshDate"
            width="170"
            :formatter="formatDate"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审核证据是否准确,真实,合法"
            prop="evidenceAccurate"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审核查证事实描述是否详尽,充分"
            prop="verificationDescription"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计结论是否客观、公正"
            prop="auditConclusion"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计处理意见及建议是否正确、具有可操作性和建设性"
            prop=""
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="基础工作"
            prop="basicWork"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审核人"
            prop="zyksshPeople"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="复核人"
            prop="sheetCode"
            width="170"
          ></el-table-column>
        </el-table-column>
        <el-table-column
          align="center"
          label="审核组按照专业科室意见修改后"
          width="170"
        >
          <el-table-column
            align="center"
            label="修改后上报时间"
            prop="fqrShDate"
            width="170"
            :formatter="formatDate"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计查证事实"
            prop="sheetCode"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计结论及依据"
            prop="auditConclusion"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审计处理意见及建议"
            prop="handlingOpinions"
            width="170"
          ></el-table-column>
        </el-table-column>
        <el-table-column
          align="center"
          label="专业科室"
          prop="sheetCode"
          width="170"
        >
          <el-table-column
            align="center"
            label="专业科室审核时间"
            prop="zyksfhDate"
            width="170"
            :formatter="formatDate"
          ></el-table-column>
          <el-table-column
            align="center"
            label="专业科室审核意见"
            prop="zyksfhCommont"
            width="170"
          ></el-table-column>
          <el-table-column
            align="center"
            label="审核人"
            prop="thedeptstaffname"
            width="170"
          ></el-table-column>
        </el-table-column>
        <el-table-column
          align="center"
          label="审计组与被审计单位交换意见后修改审计查证事实描述"
          prop="sheetCode"
          width="170"
        >
          <el-table-column
            align="center"
            label="修改后上报时间"
            prop="sheetCode"
            width="170"
          ></el-table-column>
        </el-table-column>
        <el-table-column
          align="center"
          label="备注"
          prop="remarks"
          width="170"
        ></el-table-column>
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
    <project-manage
      @projectManage="getChildlistPro"
      ref="executor"
      :multiple="false"
    ></project-manage>
  </div>
</template>
<script>
  import { getDraftListPage, exportList } from '@/oapi/audit/question'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import projectManage from '@/components/selectPerson.vue'
  import { downloadFile } from '@/utils/otherUtils'
  import { formatDay } from '@/utils'
  export default {
    mixins: [searchTableMixis],
    components: {
      filterSearch,
      filterTable,
      projectManage,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
          realname: '',
          draftNumber: '',
          createUserName: '',
          createUser: '',
        },
        filedAll: [
          { name: '项目名称' },
          { name: '底稿名称' },
          { name: '被审计对象' },
          { name: '审计目标' },
          { name: '拟稿人' },
          { name: '拟稿日期' },
          { name: '所属项目' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-oilAudit-question-search',
        tableKey: 'oilAudit-oilAudit-question-list',
        searchMore: true,
        select: [],
      }
    },
    mounted() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [
          { name: '底稿编号', key: 'draftNumber' },
          { name: '审计项目名称', key: 'projectName' },
          { name: '拟稿人', key: 'realname' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
          realname: '',
          draftNumber: '',
          createUserName: '',
          createUser: '',
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
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getDraftListPage(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
        this.setCheckedRows()
      },
      getChildlistPro(val) {
        this.queryForm.createUserName = val[0].realname
        this.queryForm.createUser = val[0].staffid
      },
      async handleExport() {
        this.listLoading = true
        const ids = this.select.map((res) => res.id)
        const res = await exportList({
          ...this.queryForm,
          idList: ids.toString(),
        })
        downloadFile(res, '底稿清单.xlsx')
        this.listLoading = false
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
<style lang="scss" scoped></style>
