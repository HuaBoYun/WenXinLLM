<template>
  <div class="enterprise-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.enterpriseName"
                clearable
                placeholder="企业名称"
                v-if="item.name === '企业名称'"
              />
              <el-input
                v-model="queryForm.creditCode"
                clearable
                placeholder="统一社会信用代码"
                v-if="item.name === '统一社会信用代码'"
              />
              <el-select
                v-model="queryForm.enterpriseType"
                filterable
                placeholder="企业类型"
                style="width: 100%"
                v-if="item.name === '企业类型'"
              >
                <el-option
                  v-for="item in enterpriseTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-select
                v-model="queryForm.supervisionLevel"
                filterable
                placeholder="监管层级"
                style="width: 100%"
                v-if="item.name === '监管层级'"
              >
                <el-option
                  v-for="item in supervisionLevelOptions"
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
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel :span="24">
        <el-tooltip
          class="item"
          effect="dark"
          content="表格筛选"
          placement="top"
        >
          <el-popover placement="left" trigger="click">
            <filter-table
              v-if="true"
              :list="tableAll"
              :name="localKey"
              @updateTableShow="initTable"
            />
            <el-button slot="reference" style="height: 32px">
              <vab-icon icon="filter" :is-custom-svg="true" />
            </el-button>
          </el-popover>
        </el-tooltip>
        <el-button
          icon="el-icon-plus"
          type="primary"
          @click="handleAdd"
        >
          新增企业
        </el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        :element-loading-text="elementLoadingText"
        @selection-change="setSelectRows"
        @sort-change="tableSortChange"
      >
        <el-table-column
          show-overflow-tooltip
          type="selection"
          width="55"
        />
        <el-table-column
          v-for="(item, index) in tableItem"
          :key="index"
          :label="item.label"
          :prop="item.prop"
          :width="item.width"
          show-overflow-tooltip
          sortable="custom"
        >
          <template #default="{ row }">
            <span v-if="item.prop === 'enterpriseStatus'">
              <el-tag
                :type="getStatusType(row.enterpriseStatus)"
                size="small"
              >
                {{ getStatusLabel(row.enterpriseStatus) }}
              </el-tag>
            </span>
            <span v-else-if="item.prop === 'listingStatus'">
              <el-tag
                :type="row.listingStatus === 'LISTED' ? 'success' : 'info'"
                size="small"
              >
                {{ row.listingStatus === 'LISTED' ? '已上市' : '未上市' }}
              </el-tag>
            </span>
            <span v-else-if="item.prop === 'registeredCapital'">
              {{ formatCurrency(row.registeredCapital) }}
            </span>
            <span v-else>{{ row[item.prop] }}</span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="200"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleDetail(row)"
            >
              详情
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              @click="handleHierarchy(row)"
            >
              层级关系
            </el-button>
            <el-button
              type="text"
              style="color: #f56c6c"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <!-- 新增/编辑弹框 -->
    <enterprise-form
      ref="enterpriseForm"
      @refresh="fetchData"
    />

    <!-- 详情弹框 -->
    <enterprise-detail
      ref="enterpriseDetail"
    />

    <!-- 层级关系弹框 -->
    <enterprise-hierarchy
      ref="enterpriseHierarchy"
      @refresh="fetchData"
    />
  </div>
</template>

<script>
import { getEnterpriseList, deleteEnterprise } from '@/api/stateAssets/enterprise'
import EnterpriseForm from './components/EnterpriseForm'
import EnterpriseDetail from './components/EnterpriseDetail'
import EnterpriseHierarchy from './components/EnterpriseHierarchy'

export default {
  name: 'Enterprise',
  components: {
    EnterpriseForm,
    EnterpriseDetail,
    EnterpriseHierarchy,
  },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        enterpriseName: undefined,
        creditCode: undefined,
        enterpriseType: undefined,
        supervisionLevel: undefined,
        pageNumber: 1,
        pageSize: 20,
      },
      elementLoadingText: '正在加载...',
      localKey: 'enterprise',
      searchItem: [],
      searchAll: [
        { name: '企业名称', show: true },
        { name: '统一社会信用代码', show: true },
        { name: '企业类型', show: true },
        { name: '监管层级', show: true },
      ],
      tableItem: [],
      tableAll: [
        { label: '企业名称', prop: 'enterpriseName', width: 200, show: true },
        { label: '统一社会信用代码', prop: 'creditCode', width: 180, show: true },
        { label: '企业类型', prop: 'enterpriseType', width: 120, show: true },
        { label: '注册资本(万元)', prop: 'registeredCapital', width: 150, show: true },
        { label: '法定代表人', prop: 'legalRepresentative', width: 120, show: true },
        { label: '监管层级', prop: 'supervisionLevel', width: 100, show: true },
        { label: '上市状态', prop: 'listingStatus', width: 100, show: true },
        { label: '企业状态', prop: 'enterpriseStatus', width: 100, show: true },
        { label: '成立日期', prop: 'establishDate', width: 120, show: true },
        { label: '创建时间', prop: 'createTime', width: 160, show: true },
      ],
      searchMore: false,
      enterpriseTypeOptions: [
        { label: '国有独资', value: 'STATE_OWNED' },
        { label: '国有控股', value: 'STATE_HOLDING' },
        { label: '国有参股', value: 'STATE_PARTICIPATING' },
      ],
      supervisionLevelOptions: [
        { label: '中央', value: 'CENTRAL' },
        { label: '地方', value: 'LOCAL' },
      ],
    }
  },
  created() {
    this.initSearch()
    this.initTable()
    this.fetchData()
  },
  methods: {
    initSearch() {
      this.searchItem = this.searchAll.filter((item) => item.show)
    },
    initTable() {
      this.tableItem = this.tableAll.filter((item) => item.show)
    },
    showMore() {
      this.searchMore = !this.searchMore
    },
    setSelectRows(val) {
      this.selectRows = val
    },
    tableSortChange() {
      this.fetchData()
    },
    resetQueryForm() {
      this.queryForm = {
        enterpriseName: undefined,
        creditCode: undefined,
        enterpriseType: undefined,
        supervisionLevel: undefined,
        pageNumber: 1,
        pageSize: 20,
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
        const response = await getEnterpriseList(this.queryForm)
        if (response && response.data && response.data.pageInfo) {
          this.list = response.data.pageInfo.tlist || []
          this.total = response.data.pageInfo.totalRecord || 0
        } else {
          // 接口失败时显示空数据
          this.list = []
          this.total = 0
          console.warn('获取企业列表失败:', response)
        }
      } catch (error) {
        console.error('获取企业列表异常:', error)
        // 发生异常时显示空数据，不弹出错误提示
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    handleAdd() {
      this.$refs.enterpriseForm.show()
    },
    handleDetail(row) {
      this.$refs.enterpriseDetail.show(row)
    },
    handleEdit(row) {
      this.$refs.enterpriseForm.show(row)
    },
    handleHierarchy(row) {
      this.$refs.enterpriseHierarchy.show(row)
    },
    async handleDelete(row) {
      try {
        await this.$baseConfirm('确定要删除该企业吗？')
        const response = await deleteEnterprise({ enterpriseId: row.enterpriseId })
        if (response && response.code === 1) {
          this.$baseMessage('删除成功', 'success')
          this.fetchData()
        } else {
          this.$baseMessage('删除功能暂未开放', 'warning')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$baseMessage('删除功能暂未开放', 'warning')
        }
      }
    },
    getStatusType(status) {
      const statusMap = {
        NORMAL: 'success',
        CANCELLED: 'danger',
        MERGED: 'warning',
        SUSPENDED: 'info',
      }
      return statusMap[status] || 'info'
    },
    getStatusLabel(status) {
      const statusMap = {
        NORMAL: '正常',
        CANCELLED: '注销',
        MERGED: '合并',
        SUSPENDED: '暂停',
      }
      return statusMap[status] || '未知'
    },
    formatCurrency(value) {
      if (!value) return '-'
      return new Intl.NumberFormat('zh-CN').format(value)
    },
  },
}
</script>

<style lang="scss" scoped>
.enterprise-container {
  padding: 0 !important;
  background: #f0f2f5;
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
}
</style>
