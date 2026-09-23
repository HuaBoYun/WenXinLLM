<!-- 收入结构分析 -->
<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.jsFinance"
                clearable
                placeholder="财务组织"
                v-if="item.name === '财务组织'"
              />
              <el-input
                v-model="queryForm.jsFinance"
                clearable
                placeholder="组织本币"
                v-if="item.name === '组织本币'"
              />
              <el-date-picker
                v-model="queryForm.day"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-if="item.name === '时间'"
              ></el-date-picker>
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="收入确认类型"
                v-if="item.name === '收入确认类型'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="客户基本分类"
                v-if="item.name === '客户基本分类'"
              />
              <el-input
                v-model="queryForm.supplier"
                clearable
                placeholder="物料分类"
                v-if="item.name === '物料分类'"
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
            <el-form-item>
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel style="width: 100%">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <!-- <el-button type="success" @click="handleEdit(false, false)">
          新建
        </el-button> -->
        <!-- <el-button type="primary">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="序号"
          type="index"
          width="60"
        ></el-table-column>
        <el-table-column
          align="center"
          label="财务组织名称"
          prop="orgName"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="收入确认类型"
          prop="recognitionType"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="收入类型"
          prop="revenueType"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="物料分类"
          prop="materialCategory"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="客户分类"
          prop="customerCategory"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="客户行业"
          prop="customerIndustry"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="客户地区"
          prop="customerRegion"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          align="center"
          label="计量单位"
          prop="unitName"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="数量"
          prop="quantity"
          width="80"
        ></el-table-column>
        <el-table-column
          align="center"
          label="币种"
          prop="currencyName"
          width="80"
        ></el-table-column>
        <el-table-column label="原币" align="center">
          <el-table-column
            align="center"
            label="无税金额"
            prop="originalNoTaxAmount"
            width="120"
          ></el-table-column>
          <el-table-column
            align="center"
            label="税额"
            prop="originalTaxAmount"
            width="100"
          ></el-table-column>
          <el-table-column
            align="center"
            label="价税合计"
            prop="originalTotalAmount"
            width="120"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="本币" align="center">
          <el-table-column
            align="center"
            label="无税金额"
            prop="localNoTaxAmount"
            width="120"
          ></el-table-column>
          <el-table-column
            align="center"
            label="税额"
            prop="localTaxAmount"
            width="100"
          ></el-table-column>
          <el-table-column
            align="center"
            label="价税合计"
            prop="localTotalAmount"
            width="120"
          ></el-table-column>
        </el-table-column>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="100"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
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
  </div>
</template>

<script>
  // 使用财务共享模块的收入结构分析API
  import {
    getRevenueStructureAnalysisList,
  } from '@/api/financialSharing/revenueManagement'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'srjgfx',
    mixins: [searchTableMixis],
    components: {
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        planNum: '',
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          startStatus: undefined,
          sxName: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
        },
        currProjectId: '',
        filedAll: [
          { name: '财务组织名称' },
          { name: '收入确认类型' },
          { name: '收入类型' },
          { name: '物料分类' },
          { name: '客户分类' },
          { name: '客户行业' },
          { name: '客户地区' },
          { name: '计量单位名称' },
          { name: '数量' },
          { name: '币种名称' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgl-srgl-srjgfx-search',
        tableKey: 'cwgl-srgl-srjgfx-list',
        searchMore: false,
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
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      getFiled() {
        return [
          { name: '财务组织', key: 'sxName' },
          { name: '组织本币', key: 'sxName' },
          { name: '时间', key: 'startStatus' },
          { name: '收入确认类型', key: 'startStatus1' },
          { name: '客户基本分类', key: 'startStatus2' },
          { name: '物料分类', key: 'startStatus2' },
        ]
      },
      selectTeamList(val, flagTitle) {
        console.log(val, flagTitle)
        if (flagTitle) {
          this.queryForm.projectOrderName = val[0].realname
          this.queryForm.projectOrderId = val[0].staffid
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      resetQueryForm() {
        this.queryForm = {
          projectOrderName: undefined,
          projectOrderId: undefined,
          pageNumber: 1,
          pageSize: 10,
          xmnd: '',
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
        try {
          const params = {
            pageNumber: this.queryForm.pageNumber,
            pageSize: this.queryForm.pageSize,
            orgName: this.queryForm.jsFinance,
            recognitionType: this.queryForm.supplier,
          }
          // 处理日期范围
          if (this.queryForm.day && this.queryForm.day.length === 2) {
            params.startDate = this.queryForm.day[0]
            params.endDate = this.queryForm.day[1]
          }
          const res = await getRevenueStructureAnalysisList(params)
          if (res && res.code === 1 && res.data) {
            const { tlist, totalRecord } = res.data
            this.list = tlist || []
            this.total = totalRecord || 0
          } else {
            this.list = []
            this.total = 0
            if (res && res.msg) {
              this.$message.warning(res.msg)
            }
          }
        } catch (error) {
          console.error('获取收入结构分析数据失败:', error)
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
      },
      handleView(row) {
        // 查看收入结构详情
        this.$message.info(`查看收入结构: ${row.orgName} - ${row.revenueType}`)
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
