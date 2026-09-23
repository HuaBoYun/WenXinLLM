<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
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
              v-if="item.name == '编号'"
              placeholder="编号"
              v-model="queryForm.no"
            ></el-input>
            <el-input
              v-if="item.name == '审计项目名称'"
              placeholder="审计项目名称"
              v-model="queryForm.planprojectname"
            ></el-input>
            <el-input
              v-if="item.name == '实施审计机构名称'"
              placeholder="实施审计机构名称"
              v-model="queryForm.planimplementationunitname"
            ></el-input>
            <el-select
              v-if="item.name == '实施类型'"
              v-model="queryForm.planimplementationtype"
              placeholder="实施类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in implTypeoptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <el-input
              v-if="item.name == '被审计单位'"
              placeholder="被审计单位"
              v-model="queryForm.planauditunitname"
            ></el-input>
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
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
    <vab-query-form-right-panel :span="24">
      <el-tooltip
        class="item"
        effect="dark"
        content="表格筛选"
        placement="top"
      ></el-tooltip>
    </vab-query-form-right-panel>
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
        <el-button type="success" @click="handleEdit('add', null)">
          新增
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column align="center" label="序号" type="index" /> -->
        <el-table-column
          align="center"
          label="审计项目名称"
          prop="planprojectname"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit('detail', row)">
              {{ row.planprojectname }}
            </el-button>
          </template>
        </el-table-column>
        {{ filedNow }}
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            prop="planauditunitname"
            label="被审计单位"
            show-overflow-tooltip
            v-if="item.name === '被审计单位'"
          ></el-table-column>
          <el-table-column
            label="实施类型"
            v-if="item.name === '实施类型'"
            prop="planimplementationtype"
          ></el-table-column>
          <el-table-column
            label="审计项目类型"
            v-if="item.name === '审计项目类型'"
            prop="planauditprojecttypename"
          ></el-table-column>
          <el-table-column
            prop="planprojectapprovalunitname"
            label="立项单位"
            show-overflow-tooltip
            v-if="item.name === '立项单位'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '已退回'
                  : row.status == 3
                  ? '已撤回'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row, $index }">
            <el-button
              type="text"
              @click="handleEdit('edit', row, $index)"
              :disabled="!!row.status"
            >
              编辑
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  @click.native="handleApprovalDetail(row)"
                  :disabled="!row.status"
                >
                  办理
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleApproval(row)"
                  :disabled="!!row.status || btnLoading"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleDelete(row)"
                  :disabled="!!row.status"
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
    <!-- 提交流程 -->
    <ProcessList ref="process" @fetchData="fetchData" />
    <!-- 办理查看 -->
    <WfqdDeal ref="wfqddeal" />
    <jhbaView ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { getPlanFilingList, getPlanFilingDel } from '@/oapi/audit/plan'
  import { searchTableMixis } from '@/mixis/index'
  import { formatDate } from '@/utils/index'
  import jhbaView from '@/views/oilAudit/jhlx/components/jhbaView.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    components: { filterTable, filterSearch, jhbaView, ProcessList, WfqdDeal },
    mixins: [searchTableMixis],

    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        filedAll: [
          { name: '被审计单位' },
          { name: '实施类型' },
          { name: '审计项目类型' },
          { name: '立项单位' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        list: [],
        searchItem: [], //可见搜索项
        queryForm: {
          planprojectname: '',
          planauditunitname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-jhlx-jhba-search',
        tableKey: 'oilAudit-jhlx-jhba-list',
        listLoading: false,
        total: 0,
        searchMore: true,
        implTypeoptions: [
          {
            label: '自审',
            value: '自审',
          },
          {
            label: '外包',
            value: '外包',
          },
          {
            label: '单位委托审计',
            value: '单位委托审计',
          },
        ],
        btnLoading: false,
      }
    },

    mounted() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },

    methods: {
      handleApproval(row) {
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(187, row.planfilingid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleApprovalDetail(row) {
        // 办理
        const res = await getFlowPkInfo({
          formId: row.planfilingid,
          tableId: 187,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          // { name: '编号', key: 'no' },
          { name: '审计项目名称', key: 'planprojectname' },
          {
            name: '被审计单位',
            key: 'planauditunitname',
          },
          { name: '实施审计机构名称', key: 'planimplementationunitname' },
          { name: '实施类型', key: 'planimplementationtype' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          planprojectname: '',
          planauditunitname: '',
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
      async fetchData() {
        this.btnLoading = false
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
          code,
        } = await getPlanFilingList(this.queryForm)
        if (code === 1) {
          this.list = tlist
          this.listLoading = false
          this.total = totalRecord
        }
      },
      handleEdit(type, row) {
        this.$refs['edit'].showEdit(type, row)
      },
      handleDelete(row) {
        console.log('🚀 ~ handleDelete ~ row:', row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await getPlanFilingDel({ id: row.planfilingid })
          if (res.code != 1) return
          this.$baseMessage('成功', 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
    },
  }
</script>

<style lang="less" scoped></style>
