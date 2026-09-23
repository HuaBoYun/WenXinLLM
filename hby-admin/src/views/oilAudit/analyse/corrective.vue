<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form>
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
                v-model="queryForm.PROJECTCODE"
                clearable
                placeholder="项目编号"
                v-if="item.name === '项目编号'"
              />
              <el-input
                v-model="queryForm.PRJOECTNAME"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.AUDITORGNAME"
                clearable
                placeholder="审计单位"
                v-if="item.name === '审计单位'"
              />
              <el-input
                v-model="queryForm.ORGNAME"
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
      </vab-query-form>
    </el-card>

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
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="项目编号"
          prop="PROJECTCODE"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, true)">
              {{ row.PROJECTCODE }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="项目名称"
            v-if="item.name === '项目名称'"
            prop="PRJOECTNAME"
          />
          <el-table-column
            align="center"
            label="审计单位"
            v-if="item.name === '审计单位'"
            prop="AUDITORGNAME"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="被审计单位"
            v-if="item.name === '被审计单位'"
            prop="ORGNAME"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="计划年度"
            v-if="item.name === '计划年度'"
            prop="PLANYEAR"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="发现问题数量"
            v-if="item.name === '发现问题数量'"
            prop="WTZS"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="showDetailModal(1, row.PROJECTID)">
                {{ row.WTZS }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="待整改问题数量"
            v-if="item.name === '待整改问题数量'"
            prop="FQZGS"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="showDetailModal(2, row.PROJECTID)">
                {{ row.FQZGS }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="已整改问题数量"
            v-if="item.name === '已整改问题数量'"
            prop="YZG"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="showDetailModal(3, row.PROJECTID)">
                {{ row.YZG }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="未整改问题数量"
            v-if="item.name === '未整改问题数量'"
            prop="WZG"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-button type="text" @click="showDetailModal(4, row.PROJECTID)">
                {{ row.WZG }}
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
  } from '@/oapi/audit/analyse'
  import { doDelete } from '@/oapi/table'
  import ProjectEdit from '@/views/oilAudit/analyse/components/ProjectEdit'
  import IndexEdit from '@/views/oilAudit/project/components/IndexEdit.vue'
  import Modal from './components/modal.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'List',
    components: { ProjectEdit, IndexEdit, Modal, filterSearch, filterTable },
    mixins: [searchTableMixis],
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
        localKey: 'oilAudit-analyse-corrective-search',
        tableKey: 'oilAudit-analyse-corrective-list',
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
          { name: '项目编号', key: 'PROJECTCODE' },
          { name: '项目名称', key: 'PRJOECTNAME' },
          { name: '审计单位', key: 'AUDITORGNAME' },
          { name: '被审计单位', key: 'ORGNAME' },
        ]
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
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
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await rectificationQuestionList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      // handleEdit(type, row) {
      //   // this.$refs[type].showEdit(row)
      //   getProjectDetail({
      //     projectid: row.PROJECTID,
      //   }).then((res) => {
      //     this.$refs[type].showEdit(res.data.pj)
      //   })
      // },
      handleEdit(row, disabled) {
        const info = {
          startDate: row.STARTDATE,
          endDate: row.ENDDATE,
          projectId: row.PROJECTID,
        }
        this.$refs['edit'].showEdit(info, disabled, this.planNum)
      },
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
      async handleExport() {
        const data = await exportCorrective({
          ...this.queryForm,
        })
        let fileName = '整改问题分析'
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
</style>
