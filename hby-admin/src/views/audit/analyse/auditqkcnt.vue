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
                v-model="queryForm.unitName"
                clearable
                placeholder="单位名称"
                v-if="item.name === '单位名称'"
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
          <el-button type="success" @click="handleAdd">新建</el-button>
          <el-button type="success" @click="handleExport">导出</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="单位名称"
          prop="unitName"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, 'detail')">
              {{ row.unitName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="是否设立董事会"
            v-if="item.name === '是否设立董事会'"
            prop="hasBoardOfDirectors"
          />
          <el-table-column
            align="center"
            label="是否设立审计委员会"
            v-if="item.name === '是否设立审计委员会'"
            prop="hasAuditCommittee"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="是否设立总审计师"
            v-if="item.name === '是否设立总审计师'"
            prop="hasChiefDesigner"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="总审计师职位层级"
            v-if="item.name === '总审计师职位层级'"
            prop="chiefDesignerPositionLevel"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="总审计师任职方式"
            v-if="item.name === '总审计师任职方式'"
            prop="chiefDesignerEmploymentMode"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="是否设置内部审计机构"
            v-if="item.name === '是否设置内部审计机构'"
            prop="hasInternalAuditDepartment"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="内部审计工作的领导机构"
            v-if="item.name === '内部审计工作的领导机构'"
            prop="internalAuditLeadershipOrganization"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="内部审计机构层级"
            v-if="item.name === '内部审计机构层级'"
            prop="internalAuditDepartmentLevel"
            show-overflow-tooltip
          />
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, 'edit')"
            >
              修改
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
    <ProjectEdit ref="projectEdit" @fetch-data="fetchData" />

    <AuditqkcntEdit ref="edit"" @fetchData="fetchData" />
  </div>
</template>

<script>
  import {
    auditStatisticsGetPage,
    auditStatisticsExport,
  } from '@/api/audit/analyse'
  import { doDelete } from '@/api/table'
  import ProjectEdit from '@/views/audit/analyse/components/ProjectEdit'
  import AuditqkcntEdit from './components/auditqkcntEdit.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'List',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, ProjectEdit, AuditqkcntEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          unitName: '',
        },
        // 筛选列表配置
        filedAll: [
          { name: '是否设立董事会' },
          { name: '是否设立审计委员会' },
          { name: '是否设立总审计师' },
          { name: '总审计师职位层级' },
          { name: '总审计师任职方式' },
          { name: '是否设置内部审计机构' },
          { name: '内部审计工作的领导机构' },
          { name: '内部审计机构层级' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-analyse-corrective-search',
        tableKey: 'audit-analyse-corrective-list',
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
          { name: '单位名称', key: 'unitName' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          unitName: '',
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
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await auditStatisticsGetPage(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleEdit(row, type) {
        this.$refs['edit'].showEdit(row, type)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport() {
        const data = await auditStatisticsExport({
          ...this.queryForm,
        })
        let fileName = '审计情况统计表.xlsx'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
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
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>