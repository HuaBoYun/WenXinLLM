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
                v-model="queryForm.projectCode"
                clearable
                placeholder="项目编号"
                v-if="item.name === '项目编号'"
              />
              <el-input
                v-model="queryForm.prjoectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.auditOrgName"
                clearable
                placeholder="审计单位"
                v-if="item.name === '审计单位'"
              />
              <el-input
                v-model="queryForm.orgname"
                clearable
                placeholder="被审计单位"
                v-if="item.name === '被审计单位'"
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
          <el-button type="success" @click="handleExport">导出</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="项目编号"
          prop="projectCode"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, true)">
              {{ row.projectCode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="项目名称"
            v-if="item.name === '项目名称'"
            prop="prjoectName"
          />
          <el-table-column
            align="center"
            label="审计单位"
            v-if="item.name === '审计单位'"
            prop="auditOrgName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="被审计单位"
            v-if="item.name === '被审计单位'"
            prop="orgname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="计划年度"
            v-if="item.name === '计划年度'"
            prop="planYear"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="发现问题数量"
            v-if="item.name === '发现问题数量'"
            prop="wtzs"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="showDetailModal(1, row.projectId)">
                {{ row.wtzs }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="待整改问题数量"
            v-if="item.name === '待整改问题数量'"
            prop="fqzgs"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="showDetailModal(2, row.projectId)">
                {{ row.fqzgs }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="已整改问题数量"
            v-if="item.name === '已整改问题数量'"
            prop="yzg"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="showDetailModal(3, row.projectId)">
                {{ row.yzg }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="未整改问题数量"
            v-if="item.name === '未整改问题数量'"
            prop="wzg"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="showDetailModal(4, row.projectId)">
                {{ row.wzg }}
              </el-button>
            </template>
          </el-table-column>
        </div>
        <el-table-column width="1" />
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
    <IndexEdit ref="edit" />
    <Modal ref="modal" />
  </div>
</template>

<script>
  import {
    rectificationQuestionList,
    exportCorrective,
  } from '@/api/audit/analyse'
  import { doDelete } from '@/api/table'
  import ProjectEdit from '@/views/audit/analyse/components/ProjectEdit'
  import IndexEdit from '@/views/audit/project/components/IndexEditNew.vue'
  import Modal from './components/modal.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'List',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, ProjectEdit, IndexEdit, Modal },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        // 筛选列表配置
        filedAll: [
          { name: '项目名称' },
          { name: '审计单位' },
          { name: '被审计单位' },
          { name: '计划年度' },
          { name: '发现问题数量' },
          { name: '待整改问题数量' },
          { name: '已整改问题数量' },
          { name: '未整改问题数量' },
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
          { name: '项目编号', key: 'projectCode' },
          { name: '项目名称', key: 'prjoectName' },
          { name: '审计单位', key: 'auditOrgName' },
          { name: '被审计单位', key: 'orgname' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
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
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await rectificationQuestionList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      // handleEdit(type, row) {
      //   // this.$refs[type].showEdit(row)
      //   getProjectDetail({
      //     projectid: row.projectId,
      //   }).then((res) => {
      //     this.$refs[type].showEdit(res.data.pj)
      //   })
      // },
      handleEdit(row, disabled) {
        const info = {
          startDate: row.STARTDATE,
          endDate: row.ENDDATE,
          projectId: row.projectId,
        }
        this.$refs['edit'].showEdit(info, disabled, this.planNum)
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
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      showDetailModal(type, projectId) {
        this.$refs['modal'].show(type, projectId)
      },
      /**
       * @description: 导出
       * @return {*}
       */
      async handleExport() {
        const data = await exportCorrective({
          ...this.queryForm,
          AUDITORGNAME: this.queryForm.auditOrgName,
          ORGNAME: this.queryForm.orgname,
          PROJECTCODE: this.queryForm.projectCode,
          PRJOECTNAME: this.queryForm.prjoectName,
        })
        let fileName = '整改问题分析.xls'
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
