<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <SupplierTabs
        :saveTeamId="saveTeamId"
        :type="'gysjk'"
        ref="Tab"
        @resetSearch="resetSearch"
      />
      <vab-query-form>
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.companyname"
                clearable
                placeholder="企业名"
                v-if="item.name === '企业名'"
              />
              <el-select
                v-model="queryForm.fxtype"
                clearable
                placeholder="风险状况"
                v-if="item.name === '风险状况'"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
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
              <el-button native-type="submit" @click="resetSearch">
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
        </vab-query-form-top-panel>
      </vab-query-form>
    </el-card>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel class="option-row">
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
        <el-button type="success" @click="handleAdd">添加公司</el-button>
        <el-button type="success" @click="changeFZInfo">修改分组</el-button>
      </vab-query-form-right-panel>
      <!-- 列表 -->
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="内部编码" prop="companyid" />

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="企业名称"
            prop="companyname"
            show-overflow-tooltip
            v-if="item.name === '企业名称'"
          >
            <template #default="{ row }">
              {{ row.companyname }}
              <!-- <el-button type="text" @click="handleDetail(row)">
                {{ row.companyname }}
              </el-button> -->
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="风险标签"
            prop="priceList"
            v-if="item.name === '风险标签'"
          >
            <template #default="{ row }">
              {{ changeLateName(row.priceList) }}
            </template>
          </el-table-column>
          >
          <el-table-column
            align="center"
            label="风险状况"
            prop="fxtype"
            v-if="item.name === '风险状况'"
          />
          <el-table-column
            align="center"
            label="风险变化"
            prop="fxtype"
            v-if="item.name === '风险变化'"
          />
          <el-table-column
            align="center"
            label="所属账户"
            prop="staff.realname"
            v-if="item.name === '所属账户'"
          />
          <el-table-column
            align="center"
            label="监控日期"
            prop="createdate"
            v-if="item.name === '监控日期'"
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            label="操作"
            prop="options"
            v-if="item.name === '操作'"
            :formatter="formatDate"
          >
            <template slot-scope="{ row }">
              <el-button type="text" @click="handleViewMonitor(row)">查看监控</el-button>
              <el-button type="text" @click="handleEdit(row)">修改</el-button>
              <el-button type="text" @click="handleDeleteCompany(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </div>

        <el-table-column width="1" />
      </el-table>
    </el-card>
    <el-pagination
      background
      class="pager"
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <!-- 新建/修改分组表单 -->

    <SupplierEdit ref="edit" :type="'gysjk'" @resetSearch="fetchData" />
    <CompanyModel ref="company" />
    <MonitorDetail ref="monitorDetail" :external-data="externalData" />
  </div>
</template>
<script>
  import { getTableList, deleteGroupCompany } from '@/api/risk/monitor'
  import SupplierEdit from './components/SupplierEdit.vue'
  import CompanyModel from './components/CompanyModel.vue'
  import SupplierTabs from './components/SupplierTabs.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { formatDate } from '@/utils/index'
  import { CJBDI_MONITOR_ITEMS } from '@/config/cjbdi-monitor-items'
  import MonitorDetail from '@/views/contract/opposite/components/MonitorDetail.vue'
  export default {
    components: {
      SupplierEdit,
      SupplierTabs,
      filterSearch,
      filterTable,
      CompanyModel,
      MonitorDetail,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          teamid: '',
          companyname: '',
          pageNo: 1,
          pageSize: 20,
          fxjktype: 'gysjk',
        },
        title: '',
        tabIndex: 2,
        dialogFormVisible: false,
        teamid: undefined,
        form: {
          name: undefined,
          title: undefined,
        },
        // CJBDI 监控项配置（排除企业名录，供 MonitorDetail 使用）
        externalData: CJBDI_MONITOR_ITEMS.filter(item => item.priceid !== 'cjbdi_02').map(item => ({
          priceid: item.priceid,
          interfacename: item.interfacename,
          isCjbdi: true,
          categoryId: item.categoryId,
          group: item.group,
        })),
        typeOptions: [
          {
            label: '特别预警',
            value: '特别预警',
          },
          {
            label: '一般预警',
            value: '一般预警',
          },
          {
            label: '关注',
            value: '关注',
          },
          {
            label: '正常',
            value: '正常',
          },
        ],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-monitor-supplier-search',
        tableKey: 'risk-monitor-supplier-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '企业名称' },
          { name: '风险标签' },
          { name: '风险状况' },
          { name: '风险变化' },
          { name: '所属账户' },
          { name: '监控日期' },
          { name: '操作' },
        ], //所有表格项
        filedNow: [],
      }
    },
    created() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    methods: {
      // 查看监控详情：将 priceList 转成逗号分隔的 priceid 字符串传给 MonitorDetail
      handleViewMonitor(row) {
        const priceid = (row.priceList || [])
          .map(item => String(item.priceid))
          .filter(id => id)
          .join(',')
        this.$refs['monitorDetail'].showDetail({ ...row, priceid })
      },
      handleEdit(e) {
        console.log(e, this.teamid, '修改=====')
        this.$refs['edit'].showEdit(e, 'edit')
      },
      async handleDeleteCompany(e) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { code } = await deleteGroupCompany({
            companyid: e.companyid,
          })
          if (code == 1) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            this.fetchData()
            this.$nextTick(() => {
              this.$refs['Tab'].reloadTabData()
            })
          } else {
            this.$baseMessage('删除失败', 'error', 'vab-hey-message-error')
          }
        })
      },
      handleDetail(row) {
        this.$refs.company.showEdit(row)
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '企业名', key: 'companyname' },
          { name: '风险状况', key: 'fxtype' },
        ]
        return fields
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = ''
              }
            }
          })

          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      // 动态表格开始
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData(type) {
        this.listLoading = true
        if (type && type == 'reset') this.$refs['form'].resetFields()
        getTableList(this.queryForm).then((res) => {
          this.list = res.data.pageBean.list
          this.total = res.data.pageBean.total
        })
        this.listLoading = false
      },
      resetQueryForm() {
        this.queryForm = {
          teamid: '',
          companyname: '',
          pageNo: 1,
          pageSize: 20,
          fxjktype: 'gysjk',
        }
        this.queryForm.teamid = this.teamid
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      showEdit(item) {
        if (!item) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form.teamid = item.name
          this.form.teamname = item.title
        }
        this.dialogFormVisible = true
      },
      handleAdd() {
        if (!this.teamid) {
          this.$baseMessage('请选择组织', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['edit'].showEdit(this.teamid, 'add')
      },

      changeLateName(row) {
        const aa = []
        row.forEach((res) => {
          const priceid = res.priceid != null ? String(res.priceid) : ''
          // CJBDI 监控项：从配置中查找中文名称
          if (priceid.startsWith('cjbdi_')) {
            const config = CJBDI_MONITOR_ITEMS.find((item) => item.priceid === priceid)
            aa.push(config ? config.interfacename : res.interfacename || priceid)
          } else {
            // 旧数值类型：直接使用后端返回的 interfacename
            aa.push(res.interfacename || priceid)
          }
        })
        return aa.join('、')
      },
      changeFZInfo() {
        if (!this.teamid) {
          this.$baseMessage('请选择组织', 'error', 'vab-hey-message-error')
          return
        }
        this.form.teamid = this.teamid
        this.$refs['Tab'].openModal(this.form)
      },

      saveTeamId(info) {
        this.teamid = info.teamid
        this.form.title = info.teamname
        this.queryForm.teamid = info.teamid
        this.fetchData()
      },
    },
  }
</script>
<style scoped lang="scss">
  .el-button.is-plain {
    border: 0;
  }
  .el-button.is-plain:hover,
  .el-button.is-plain:focus {
    border: 0;
  }
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }

  .pager {
    margin-bottom: 20px !important;
  }
</style>
