<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
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
                v-model="queryForm.subjectCode"
                clearable
                placeholder="科目编码"
                v-if="item.name === '科目编码'"
              />
              <el-input
                v-model="queryForm.subjectName"
                clearable
                placeholder="科目名称"
                v-if="item.name === '科目名称'"
              />
              <el-select
                v-model="queryForm.subjectType"
                clearable
                placeholder="科目类型"
                v-if="item.name === '科目类型'"
              >
                <el-option label="资产" :value="1" />
                <el-option label="负债" :value="2" />
                <el-option label="权益" :value="3" />
                <el-option label="收入" :value="4" />
                <el-option label="费用" :value="5" />
              </el-select>
              <el-select
                v-model="queryForm.balanceDirection"
                clearable
                placeholder="余额方向"
                v-if="item.name === '余额方向'"
              >
                <el-option label="借方" :value="1" />
                <el-option label="贷方" :value="2" />
              </el-select>
              <el-select
                v-model="queryForm.isEnabled"
                clearable
                placeholder="启用状态"
                v-if="item.name === '启用状态'"
              >
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
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
              <el-button type="primary" @click="handleAdd">新增</el-button>
              <el-button type="success" @click="handleBatchEnable">批量启用</el-button>
              <el-button type="warning" @click="handleBatchDisable">批量禁用</el-button>
              <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
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
            <el-form-item style="cursor: pointer">
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

    <el-card shadow="never">
      <vab-query-form>
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
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        ref="multipleTable"
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @select-all="handleSelectionChange"
        @select="handleSelectionChange"
      >
        <!-- @selection-change="handleSelectionChange" -->
        <el-table-column type="selection" />
        <el-table-column width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            v-if="item.name === '科目ID'"
            label="科目ID"
            prop="subjectId"
            width="80"
          />
          <el-table-column
            align="center"
            v-if="item.name === '科目编码'"
            label="科目编码"
            prop="subjectCode"
            width="120"
          />
          <el-table-column
            align="center"
            v-if="item.name === '科目名称'"
            label="科目名称"
            prop="subjectName"
            min-width="150"
          />
          <el-table-column
            align="center"
            v-if="item.name === '科目级次'"
            label="级次"
            prop="subjectLevel"
            width="60"
          />
          <el-table-column
            align="center"
            v-if="item.name === '上级科目'"
            label="上级科目"
            prop="parentSubjectName"
            width="120"
          />
          <el-table-column
            align="center"
            v-if="item.name === '科目类型'"
            label="科目类型"
            prop="subjectTypeName"
            width="100"
          >
            <template slot-scope="scope">
              <el-tag :type="getSubjectTypeTag(scope.row.subjectType)">
                {{ scope.row.subjectTypeName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '余额方向'"
            label="余额方向"
            prop="balanceDirectionName"
            width="100"
          >
            <template slot-scope="scope">
              <el-tag :type="scope.row.balanceDirection === 1 ? 'success' : 'warning'">
                {{ scope.row.balanceDirectionName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '末级科目'"
            label="末级"
            prop="isLeaf"
            width="60"
          >
            <template slot-scope="scope">
              <el-tag :type="scope.row.isLeaf === 1 ? 'success' : 'info'" size="mini">
                {{ scope.row.isLeaf === 1 ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '现金科目'"
            label="现金"
            prop="isCash"
            width="60"
          >
            <template slot-scope="scope">
              <el-tag :type="scope.row.isCash === 1 ? 'success' : 'info'" size="mini">
                {{ scope.row.isCash === 1 ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '银行科目'"
            label="银行"
            prop="isBank"
            width="60"
          >
            <template slot-scope="scope">
              <el-tag :type="scope.row.isBank === 1 ? 'success' : 'info'" size="mini">
                {{ scope.row.isBank === 1 ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '启用状态'"
            label="状态"
            prop="isEnabled"
            width="80"
          >
            <template slot-scope="scope">
              <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'" size="mini">
                {{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '辅助核算'"
            label="辅助核算"
            prop="auxiliaryTypes"
            width="120"
          />
        </div>
        <el-table-column
          label="操作"
          align="center"
          width="180"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              :type="scope.row.isEnabled === 1 ? 'warning' : 'success'"
              text
              size="small"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleDelete(scope.row)"
              style="color: #f56c6c"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
        <el-table-column width="1" />
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    </div>
</template>

<script>
  import {
    getList,
    saveOrUpdate,
    deleteAccountSubject,
    updateStatus,
    batchDelete,
    batchUpdateStatus,
  } from '@/api/financial/accountSubject'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'AccountSubjectManage',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        selectedRows: [],
        queryForm: {
          subjectCode: undefined,
          subjectName: undefined,
          subjectType: undefined,
          balanceDirection: undefined,
          isEnabled: undefined,
          pageNo: 1,
          pageSize: 20,
        },
        // 筛选列表配置
        filedAll: [
          { name: '科目ID' },
          { name: '科目编码' },
          { name: '科目名称' },
          { name: '科目级次' },
          { name: '上级科目' },
          { name: '科目类型' },
          { name: '余额方向' },
          { name: '末级科目' },
          { name: '现金科目' },
          { name: '银行科目' },
          { name: '启用状态' },
          { name: '辅助核算' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'financial-account-subject-search',
        tableKey: 'financial-account-subject-list',
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 5)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [
          { name: '科目编码', key: 'subjectCode' },
          { name: '科目名称', key: 'subjectName' },
          { name: '科目类型', key: 'subjectType' },
          { name: '余额方向', key: 'balanceDirection' },
          { name: '启用状态', key: 'isEnabled' },
        ]
      },
      getSubjectTypeTag(type) {
        const tags = {
          1: 'success', // 资产
          2: 'warning', // 负债
          3: 'info',    // 权益
          4: 'danger',  // 收入
          5: 'primary', // 费用
        }
        return tags[type] || 'info'
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        try {
          const response = await getList(this.queryForm)
          if (response.code === 1) {
            this.list = response.data.tlist || []
            this.total = response.data.totalRecord || 0
          } else {
            this.$message.error(response.message || '查询失败')
          }
        } catch (error) {
          this.$message.error('查询失败：' + error.message)
        } finally {
          this.listLoading = false
        }
      },
      handleAdd() {
        // 这里可以打开新增对话框或跳转到新增页面
        this.$message.info('新增功能待实现')
      },
      handleEdit(row) {
        // 这里可以打开编辑对话框或跳转到编辑页面
        this.$message.info('编辑功能待实现')
      },
      async handleToggleStatus(row) {
        const action = row.isEnabled === 1 ? '禁用' : '启用'
        try {
          const response = await updateStatus(row.subjectId, row.isEnabled === 1 ? 0 : 1)
          if (response.code === 1) {
            this.$message.success(`${action}成功`)
            this.fetchData()
          } else {
            this.$message.error(response.message || `${action}失败`)
          }
        } catch (error) {
          this.$message.error(`${action}失败：` + error.message)
        }
      },
      async handleDelete(row) {
        try {
          await this.$confirm(`确认删除科目"${row.subjectName}"吗？`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          })

          const response = await deleteAccountSubject(row.subjectId)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          if (error !== 'cancel') {
            this.$message.error('删除失败：' + error.message)
          }
        }
      },
      handleSelectionChange(selection) {
        this.selectedRows = selection
      },
      async handleBatchEnable() {
        if (this.selectedRows.length === 0) {
          return this.$message.warning('请选择要启用的科目')
        }
        await this.batchUpdateStatus(1, '启用')
      },
      async handleBatchDisable() {
        if (this.selectedRows.length === 0) {
          return this.$message.warning('请选择要禁用的科目')
        }
        await this.batchUpdateStatus(0, '禁用')
      },
      async handleBatchDelete() {
        if (this.selectedRows.length === 0) {
          return this.$message.warning('请选择要删除的科目')
        }
        try {
          await this.$confirm(`确认删除选中的${this.selectedRows.length}个科目吗？`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          })

          const subjectIds = this.selectedRows.map(row => row.subjectId)
          const response = await batchDelete(subjectIds)
          if (response.code === 1) {
            this.$message.success('批量删除成功')
            this.fetchData()
          } else {
            this.$message.error(response.message || '批量删除失败')
          }
        } catch (error) {
          if (error !== 'cancel') {
            this.$message.error('批量删除失败：' + error.message)
          }
        }
      },
      async batchUpdateStatus(isEnabled, action) {
        try {
          const subjectIds = this.selectedRows.map(row => row.subjectId)
          const response = await batchUpdateStatus(subjectIds, isEnabled)
          if (response.code === 1) {
            this.$message.success(`批量${action}成功`)
            this.fetchData()
          } else {
            this.$message.error(response.message || `批量${action}失败`)
          }
        } catch (error) {
          this.$message.error(`批量${action}失败：` + error.message)
        }
      },
    },
  }
</script>

<style scoped>
  .system-log-container >>> .el-table__header-wrapper.el-checkbox {
    display: none !important;
  }

  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
