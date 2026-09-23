<template>
  <div class="system-log-container">
    <!-- <el-tabs v-model="queryForm.teamid" type="card" @tab-click="handleTabClick">
      <el-tab-pane
        v-for="item in editableTabs"
        :key="item.name"
        :label="item.title"
        :name="item.name"
      >
        <span slot="label">
          <vab-icon icon="group-fill" />
          {{ item.title }}
          <el-button
            icon="el-icon-edit"
            plain
            :style="{ color: '#333' }"
            type="text"
            @click.stop="showEdit(item)"
          />
        </span>
      </el-tab-pane>
      <el-tab-pane key="add" name="add">
        <span
          style="padding: 8px; font-size: 20px; font-weight: bold"
          slot="label"
          @click="showEdit()"
        >
          +
        </span>
      </el-tab-pane>
    </el-tabs> -->
    <vab-query-form style="margin-bottom: 0">
      <el-card shadow="never">
        <MonitorTabs
          ref="MonitorTabs"
          @rest="resetSearch"
          @select="handleTabClick"
          @openModal="showEdit"
        />
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-if="item.name === '企业名'"
                v-model="queryForm.companyname"
                clearable
                placeholder="企业名"
              />
              <el-select
                v-if="item.name === '风险状况'"
                v-model="queryForm.fxtype"
                clearable
                placeholder="风险状况"
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

    <el-card shadow="never" style="margin-top: 2px">
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
        <el-button type="success" @click="handleAddCompany">添加公司</el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          key="companyid"
          align="center"
          label="内部编码"
          prop="companyid"
        >
          <template #default="{ row }">
            {{ row.companyid }}
          </template>
        </el-table-column>
        <template v-if="!loading">
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              v-if="item.name === '企业名称'"
              align="center"
              label="企业名称"
              prop="companyname"
            />
            <el-table-column
              v-if="item.name === '风险标签'"
              align="center"
              label="风险标签"
            >
              <template #default="{ row }">
                {{ formatTag(row) }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="item.name === '风险状况'"
              align="center"
              label="风险状况"
              prop="fxtype"
            />
            <el-table-column
              v-if="item.name === '风险变化'"
              align="center"
              label="风险变化"
            >
              不变
            </el-table-column>
            <el-table-column
              v-if="item.name === '所属账户'"
              align="center"
              label="所属账户"
              width="140"
            >
              <template #default="{ row }">
                {{ (row.staff && row.staff.realname) || row.staffName || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              v-if="item.name === '监控日期'"
              align="center"
              label="监控日期"
              width="120"
            >
              <template #default="{ row }">
                {{ formatDate(row.createdate) }}
              </template>
            </el-table-column>
          </div>
        </template>
        <el-table-column width="1" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="viewMonitorDetail(row)">查看监控</el-button>
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
    <!-- 新建/修改分组表单 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible="dialogFormVisible"
    >
      <el-form :model="form">
        <el-form-item label="分组名" label-width="80px">
          <el-input v-model="form.teamname" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveTab">确 定</el-button>
      </div>
    </el-dialog>
    <MonitoringEdit
      ref="edit"
      :external-data="externalData"
      :internal-data="internalData"
    />
    <MonitorDetail
      ref="monitorDetail"
      :external-data="externalData"
    />
  </div>
</template>
<script>
  import {
    saveTeam,
  } from '@/api/contract/opposite'
  import { getCjbdiTeamList, queryCjbdiCompanyList } from '@/api/risk/cjbdi'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import MonitoringEdit from './components/MonitoringEdit.vue'
  import MonitorTabs from './components/monitorTabs.vue'
  import MonitorDetail from './components/MonitorDetail.vue'
  import { CJBDI_MONITOR_ITEMS } from '@/config/cjbdi-monitor-items'
  import { formatDate } from '@/utils/dateUtil'
  export default {
    components: { MonitoringEdit, filterSearch, filterTable, MonitorTabs, MonitorDetail },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {},
        title: '',
        editableTabs: [],
        dialogFormVisible: false,
        form: {
          teamid: undefined,
          teamname: undefined,
        },
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
        internalData: [],
        externalData: [],

        /*  */
        loading: false,
        search: {
          pageSize: 10,
          pageNum: 1,
          tagStatus: '',
        },
        filedAll: [
          { name: '企业名称' },
          { name: '风险标签' },
          { name: '风险状况' },
          { name: '风险变化' },
          { name: '所属账户' },
          { name: '监控日期' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'contract-opposite-monitoring-search',
        tableKey: 'contract-opposite-monitoring-list',
        searchMore: true,
      }
    },
    created() {
      this.resetQueryForm()
      this.fetchData()
      this.fetchTeamList()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 日期格式化（YYYY-MM-DD），暴露给 template 使用
      formatDate,
      /* 表单。 */
      getFiled() {
        return [
          { name: '企业名', key: 'companyname' },
          { name: '风险状况', key: 'fxtype' },
        ]
      },

      resetQueryForm() {
        this.queryForm = {
          companyname: undefined,
          fxtype: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求 - 使用新的CJBDI企业监控接口
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const { teamid, ...rest } = this.queryForm
        
        try {
          const res = await queryCjbdiCompanyList({
            pageIndex: rest.pageNumber || 1,
            pageSize: rest.pageSize || 20,
            teamid: teamid == 0 ? undefined : teamid,
            companyname: rest.companyname,
            fxtype: rest.fxtype
          })
          
          // 注意：这个接口返回格式：{ code: 200, msg: "查询成功", data: { list: [], total: 0 } }
          if (res.code === 200 && res.data) {
            // 适配后端返回的字段名
            const rawList = res.data.list || []
            this.total = res.data.total || res.data.totalRecord || 0
            
            // 转换字段名以匹配前端表格
            this.list = rawList.map(item => ({
              ...item,
              companyname: item.companyName || item.companyname, // 兼容两种字段名
              createdate: item.createdate || item.addTime, // 兼容两种字段名
              // 构建list字段用于formatTag显示风险标签
              list: this.buildRiskTags(item.priceid)
            }))
          } else {
            this.list = []
            this.total = 0
            if (res.msg) this.$message.warning(res.msg)
          }
        } catch (e) {
          console.error('查询监控名单失败', e)
          this.$message.error('查询监控名单失败')
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
      },
      showEdit(row) {
        console.log(11)
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form.teamid = row.name
          this.form.teamname = row.title
        }
        this.dialogFormVisible = true
      },
      //过滤
      formatTag(row) {
        return row.list.map((item) => item.interfacename).join(' ')
      },
      // 构建风险标签列表
      buildRiskTags(priceid) {
        if (!priceid) return []
        
        const priceids = priceid.split(',').filter(id => id.trim())
        return priceids.map(id => {
          // 尝试从externalData中查找对应的监控项
          const monitorItem = this.externalData.find(item => item.priceid === id.trim())
          return {
            priceid: id.trim(),
            interfacename: monitorItem ? monitorItem.interfacename : id.trim()
          }
        })
      },
      //添加公司
      handleAddCompany() {
        const { teamid } = this.queryForm
        if (!teamid || teamid == 0) {
          this.$baseMessage('请先选择分组', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['edit'].showEdit(teamid)
      },
      async saveTab() {
        const { msg } = await saveTeam(this.form)
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        this.$nextTick(() => {
          this.$refs['MonitorTabs'].reloadTabData()
        })
        this.fetchTeamList()
        this.dialogFormVisible = false
        this.form.teamname = ''
      },
      //请求数据 - 使用新的分组接口
      async fetchTeamList() {
        try {
          const res = await getCjbdiTeamList({})
          
          // 注意：这个接口没有code字段，成功时直接返回data
          if (res && res.data && res.data.teams) {
            const teams = res.data.teams.filter(team => team.teamname)
            
            const intialTab = [
              {
                name: undefined,
                title: '全部',
              },
            ]
            const teamList = teams.map((item) => {
              return {
                title: item.teamname,
                name: String(item.teamid),
                teamid: item.teamid
              }
            })
            this.editableTabs = intialTab.concat(teamList)
            
            // 使用分组数据构建internalData
            this.internalData = teams.map(team => ({
              teamid: team.teamid,
              teamname: team.teamname,
              pageChilds: team.pageChilds || []
            }))
            
            // 构建externalData（CJBDI监控项）
            this.externalData = CJBDI_MONITOR_ITEMS.map(item => ({
              priceid: item.priceid,
              interfacename: item.interfacename,
              isCjbdi: true,
              categoryId: item.categoryId,
              group: item.group,
            }))
          } else {
            // 如果接口失败，使用默认数据
            this.editableTabs = [{ name: undefined, title: '全部' }]
            this.internalData = []
            this.externalData = CJBDI_MONITOR_ITEMS.map(item => ({
              priceid: item.priceid,
              interfacename: item.interfacename,
              isCjbdi: true,
              categoryId: item.categoryId,
              group: item.group,
            }))
          }
        } catch (e) {
          console.error('加载分组列表失败', e)
          // 失败时使用默认数据
          this.editableTabs = [{ name: undefined, title: '全部' }]
          this.internalData = []
          this.externalData = CJBDI_MONITOR_ITEMS.map(item => ({
            priceid: item.priceid,
            interfacename: item.interfacename,
            isCjbdi: true,
            categoryId: item.categoryId,
            group: item.group,
          }))
        }
      },
      //回调
      handleTabClick(item) {
        this.queryForm.teamid = item.teamid
        this.fetchData()
      },
      // 查看监控详情
      viewMonitorDetail(row) {
        this.$refs['monitorDetail'].showDetail(row)
      },
    },
  }
</script>
<style scoped>
  .el-button.is-plain {
    border: 0;
  }
  .el-button.is-plain:hover,
  .el-button.is-plain:focus {
    border: 0;
  }
</style>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>
