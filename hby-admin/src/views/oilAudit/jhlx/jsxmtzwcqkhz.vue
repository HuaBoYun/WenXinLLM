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
              <el-date-picker
                v-if="item.name === '年份'"
                v-model="queryForm.year"
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
        <el-tooltip
          class="item"
          effect="dark"
          content="表格筛选"
          placement="top"
        ></el-tooltip>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="序号" type="index" />
        <el-table-column
          label="项目名称"
          prop="projectName"
          align="center"
          min-width="180"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="relaOrgName"
          label="被审计单位"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          prop="projectCount"
          label="单位数量"
          show-overflow-tooltip
          width="120"
        ></el-table-column>
        <el-table-column
          label="项目金额"
          align="center"
          prop="projectAmount"
        ></el-table-column>
        <el-table-column
          prop="remarks"
          label="备注"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { getJsxmtzhzList } from '@/oapi/audit/plan'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    components: { filterTable, filterSearch },
    mixins: [searchTableMixis],

    data() {
      return {
        list: [],
        searchItem: [], //可见搜索项
        queryForm: {
          year: '',
          // pageNumber: 1,
          // pageSize: 20,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-jhlx-jsxmtzwcqkhz-search',
        tableKey: 'oilAudit-jhlx-jsxmtzwcqkhz-list',
        listLoading: false,
      }
    },

    mounted() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },

    methods: {
      getFiled() {
        return [{ name: '年份', key: 'year' }]
      },
      resetQueryForm() {
        this.queryForm = {
          year: '',
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
      async fetchData() {
        this.listLoading = true
        const { data, code } = await getJsxmtzhzList(this.queryForm)
        if (code === 1) {
          this.list = data || []
          this.listLoading = false
        }
      },
    },
  }
</script>

<style lang="less" scoped></style>
