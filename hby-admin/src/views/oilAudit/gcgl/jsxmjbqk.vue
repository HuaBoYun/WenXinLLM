<template>
  <!-- 基本情况 -->
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
                v-model="queryForm.hzname"
                clearable
                placeholder="请输入编号"
                v-if="item.name === '编号'"
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
        <el-dropdown style="margin-right: 10px">
          <el-button type="success">
            新建
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="handleEdit(null, 'add', '三类')">
              三类
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleEdit(null, 'add', '四类')">
              四类
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="编号" prop="hzname">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.hzname }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="填报单位" prop="orgname" />
        <el-table-column align="center" label="项目类别" prop="fl" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '备注'"
            align="center"
            label="备注"
            prop="hzbz"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            prop="status"
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

        <el-table-column label="操作" fixed="right" align="center" width="100">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, 'edit')"
              :disabled="!!row.status"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :disabled="!row.status"
                  @click.native="handleDeal(row)"
                >
                  办理
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="!!row.status || btnLoading"
                  @click.native="handleSubmit(row)"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="!!row.status"
                  @click.native="handleDelete(row)"
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
    <jsxmjbqkView ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <!-- 选择单位（公司） -->
  </div>
</template>

<script>
  import {
    jsxmtzwcqkhzHzHzList,
    jsxmjbqkHzDelete,
    flVerify,
  } from '@/oapi/audit/plan'
  import jsxmjbqkView from './components/jsxmjbqkView'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  export default {
    name: 'jsxmjbqk',
    components: {
      filterSearch,
      filterTable,
      jsxmjbqkView,
      ProcessList,
      WfqdDeal,
      SelectDepartment,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          htbh: undefined,
          tborgid: undefined,
          tborg: undefined,
          createyear: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [{ name: '备注' }, { name: '状态' }], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [{ name: '编号', key: 'hzname' }], //可见搜索项
        localKey: 'oilAudit-gcgl-jsxmjbqk-search',
        tableKey: 'oilAudit-gcgl-jsxmjbqk-list',
        searchMore: true,
        btnLoading: false,
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
      getFiled() {
        return [{ name: '编号', key: 'hzname' }]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
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
        this.btnLoading = false
        this.listLoading = true
        let params = { ...this.queryForm }
        delete params.tborg

        const {
          data: { tlist, totalRecord },
          code,
        } = await jsxmtzwcqkhzHzHzList(params)
        if (code === 1) {
          this.list = tlist || []
          this.total = totalRecord || 0
          this.listLoading = false
        }
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, 'detail')
      },
      async handleEdit(row, title, fl) {
        if (!row && fl === '四类') {
          const flVerifyRes = await flVerify({ fl })
          if (!flVerifyRes || !flVerifyRes.data) {
            return
          }
        }
        this.$refs['edit'].showEdit(row, title, fl)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.listLoading = true
          const res = await jsxmjbqkHzDelete({
            hzid: row.hzid,
          })
          this.listLoading = false
          if (res && res.code === 1) {
            this.$message.success('操作成功！')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败！')
          }
        })
      },
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.hzid,
          tableId: 110,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      handleSubmit(row) {
        try {
          this.$baseConfirm('你确定要提交审批当前项吗', null, async () => {
            this.btnLoading = true
            this.$refs['process'].save(110, row.hzid)
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      handleCompanyTreeSelected(node) {
        this.queryForm.tborg = node.name
        this.queryForm.tborgid = node.id
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
