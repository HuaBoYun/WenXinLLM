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
                v-model="queryForm.auxiliaryCode"
                clearable
                placeholder="核算编码"
                v-if="item.name === '核算编码'"
              />
              <el-input
                v-model="queryForm.auxiliaryName"
                clearable
                placeholder="核算名称"
                v-if="item.name === '核算名称'"
              />
              <el-select
                v-model="queryForm.auxiliaryType"
                clearable
                placeholder="核算类型"
                v-if="item.name === '核算类型'"
              >
                <el-option label="客户" value="customer" />
                <el-option label="供应商" value="supplier" />
                <el-option label="部门" value="department" />
                <el-option label="员工" value="employee" />
                <el-option label="项目" value="project" />
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
        <el-table-column type="selection" />
        <el-table-column width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            v-if="item.name === 'ID'"
            label="ID"
            prop="auxiliaryId"
            width="80"
          />
          <el-table-column
            align="center"
            v-if="item.name === '核算编码'"
            label="核算编码"
            prop="auxiliaryCode"
            width="120"
          />
          <el-table-column
            align="center"
            v-if="item.name === '核算名称'"
            label="核算名称"
            prop="auxiliaryName"
            min-width="150"
          />
          <el-table-column
            align="center"
            v-if="item.name === '核算类型'"
            label="核算类型"
            prop="auxiliaryTypeName"
            width="100"
          >
            <template slot-scope="scope">
              <el-tag :type="getAuxiliaryTypeTag(scope.row.auxiliaryType)">
                {{ scope.row.auxiliaryTypeName }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            v-if="item.name === '上级核算'"
            label="上级核算"
            prop="parentName"
            width="120"
          />
          <el-table-column
            align="center"
            v-if="item.name === '级次'"
            label="级次"
            prop="auxiliaryLevel"
            width="60"
          />
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
    getList as getAuxiliaryList,
    saveOrUpdate as saveOrUpdateAuxiliary,
    deleteAuxiliary,
    updateStatus as updateAuxiliaryStatus,
    batchDelete as batchDeleteAuxiliary,
    batchUpdateStatus as batchUpdateAuxiliaryStatus,
  } from '@/api/financial/auxiliaryAccounting'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'AccountAssist',
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
          auxiliaryCode: undefined,
          auxiliaryName: undefined,
          auxiliaryType: undefined,
          isEnabled: undefined,
          pageNo: 1,
          pageSize: 20,
        },
        // 筛选列表配置
        filedAll: [
          { name: 'ID' },
          { name: '核算编码' },
          { name: '核算名称' },
          { name: '核算类型' },
          { name: '上级核算' },
          { name: '级次' },
          { name: '启用状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'financial-account-auxiliary-search',
        tableKey: 'financial-account-auxiliary-list',
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
      getFiled() {
        return [
          { name: '核算编码', key: 'auxiliaryCode' },
          { name: '核算名称', key: 'auxiliaryName' },
          { name: '核算类型', key: 'auxiliaryType' },
          { name: '启用状态', key: 'isEnabled' },
        ]
      },
      getAuxiliaryTypeTag(type) {
        const tags = {
          customer: 'primary',    // 客户
          supplier: 'success',    // 供应商
          department: 'warning',  // 部门
          employee: 'info',       // 员工
          project: 'danger',      // 项目
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
          const response = await getAuxiliaryList(this.queryForm)
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
        this.$message.info('新增功能待实现')
      },
      handleEdit(row) {
        this.$message.info('编辑功能待实现')
      },
      async handleToggleStatus(row) {
        const action = row.isEnabled === 1 ? '禁用' : '启用'
        try {
          const response = await updateAuxiliaryStatus(row.auxiliaryId, row.isEnabled === 1 ? 0 : 1)
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
          await this.$confirm(`确认删除"${row.auxiliaryName}"吗？`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          })

          const response = await deleteAuxiliary(row.auxiliaryId)
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
          return this.$message.warning('请选择要启用的核算项')
        }
        await this.batchUpdateStatus(1, '启用')
      },
      async handleBatchDisable() {
        if (this.selectedRows.length === 0) {
          return this.$message.warning('请选择要禁用的核算项')
        }
        await this.batchUpdateStatus(0, '禁用')
      },
      async handleBatchDelete() {
        if (this.selectedRows.length === 0) {
          return this.$message.warning('请选择要删除的核算项')
        }
        try {
          await this.$confirm(`确认删除选中的${this.selectedRows.length}个核算项吗？`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          })

          const auxiliaryIds = this.selectedRows.map(row => row.auxiliaryId)
          const response = await batchDeleteAuxiliary(auxiliaryIds)
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
          const auxiliaryIds = this.selectedRows.map(row => row.auxiliaryId)
          const response = await batchUpdateAuxiliaryStatus(auxiliaryIds, isEnabled)
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