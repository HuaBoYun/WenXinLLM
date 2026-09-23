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
            <el-form-item>
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.unitName"
                clearable
                placeholder="被审计单位"
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
        <!-- <el-button type="success" @click="handleEdit(null)">新增</el-button> -->
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column align="center" label="序号" type="index" width="50" /> -->
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '项目名称'"
            align="center"
            label="项目名称"
            prop="tblIssueEntity.projectName"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            v-if="item.name === '项目年度'"
            align="center"
            label="项目年度"
            prop="tblIssueEntity.problemQualitative"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '项目类型'"
            align="center"
            label="项目类型"
            prop="wtje"
            show-overflow-tooltip
          /> -->
          <el-table-column
            v-if="item.name === '被审计单位'"
            align="center"
            label="被审计单位"
            prop="tblIssueEntity.unitName"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            v-if="item.name === '负责整改岗'"
            align="center"
            label="负责整改岗"
            prop="tblIssueEntity.qualitativeRule"
            show-overflow-tooltip
          />  -->
          <el-table-column
            align="center"
            label="后续整改涉及发现问题数"
            prop="wzgCount"
            v-if="item.name === '后续整改涉及发现问题数'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="已完成整改数"
            prop="ywcCount"
            v-if="item.name === '已完成整改数'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="无法整改数"
            prop="bzgCount"
            v-if="item.name === '无法整改数'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="当前处理人"
            prop="tblIssueEntity.rectPerName"
            v-if="item.name === '当前处理人'"
          ></el-table-column>
          <!-- <el-table-column
            align="center"
            label="审计意见及块定书下发时间"
            prop="issueDate"
            v-if="item.name === '审计意见及块定书下发时间'"
          ></el-table-column>  -->
          <el-table-column
            align="center"
            label="状态"
            prop="zgzt"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              <span>{{ returnView(row) }}</span>
              <!-- <span v-if="(row.zgzt == 3 ||  row.zgzt ==4) && (row.status ==6 ||row.status==7)">
                已完成 {{row.approvalDate}}
                </span>
                 <span v-else-if="row.status> 0&&row.status < 6">审核中</span>
                 <span v-else>可整改</span> -->
            </template>
          </el-table-column>
          <!-- <el-table-column
            align="center"
            label="直接经济成果类型"
            prop="dqzjjjcgtype"
            v-if="item.name === '直接经济成果类型'"
          ></el-table-column> -->
        </div>

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
        >
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="returnView(row) != '可整改'"
          >
            填报
          </el-button>
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
    <hxzgEdit ref="edit" @fetch-data="fetchData" />
    <hxzgTable ref="table" />
  </div>
</template>

<script>
  import { getList } from '@/oapi/yqns_sjzg/hxzg'
  import hxzgEdit from './components/hxzgEdit'
  import hxzgTable from './components/gzhf/hxzgTable'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  export default {
    name: 'hxzg',
    components: { filterSearch, filterTable, hxzgEdit, hxzgTable },

    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectName: '',
          unitName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          // { name: '立项单位' },
          { name: '项目名称' },
          { name: '项目年度' },
          { name: '项目类型' },
          { name: '被审计单位' },
          { name: '负责整改岗' },
          { name: '后续整改涉及发现问题数' },
          { name: '已完成整改数' },
          { name: '无法整改数' },
          { name: '当前处理人' },
          // { name: '审计意见及块定书下发时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-gcgl-gcxmzj-search',
        tableKey: 'oilAudit-gcgl-gcxmzj-list',
        searchMore: true,
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
      returnView(row) {
        if (
          parseInt(row.wzgCount) ==
          (parseInt(row.ywcCount) + parseInt(row.bzgCount) && row.spzCount == 0)
        ) {
          return '已完成'
        } else if (
          parseInt(row.wzgCount) ==
          (parseInt(row.ywcCount) +
            parseInt(row.bzgCount) +
            parseInt(row.spzCount) && row.spzCount != 0)
        ) {
          return '审核中'
        } else if (
          parseInt(row.wzgCount) >
          parseInt(row.ywcCount) +
            parseInt(row.bzgCount) +
            parseInt(row.spzCount)
        ) {
          return '可整改'
        } else {
          return '可整改'
        }
      },
      getFiled() {
        return [{ name: '在报告中的对应编号', key: 'projectCode' }]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          projectName: '',
          unitName: '',
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
          data: { tlist: list, totalRecord: total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleEdit(row, disabled) {
        this.$refs['table'].showEdit(row, disabled)
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
</style>
