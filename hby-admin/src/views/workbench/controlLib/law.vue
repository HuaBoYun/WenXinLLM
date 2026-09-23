<template>
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
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.prulenumber"
                clearable
                placeholder="发文文号"
                v-if="item.name === '发文文号'"
              />
              <el-input
                v-model="queryForm.prulename"
                clearable
                placeholder="文件名称"
                v-if="item.name === '文件名称'"
              />
              <el-input
                v-model="queryForm.content"
                clearable
                placeholder="内容"
                v-if="item.name === '内容'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
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
        </vab-query-form-top-panel>
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
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="发文文号" prop="rulenumber">
          <template #default="{ row }">
            <el-button @click="handleView(row, true)" type="text">
              {{ row.rulenumber }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            v-if="item.name === '文件名称'"
            label="文件名称"
            prop="rulename"
          />
          <el-table-column
            align="center"
            v-if="item.name === '发文部门'"
            label="发文部门"
            prop="publishorg"
          />
          <el-table-column
            align="center"
            v-if="item.name === '效力级别'"
            label="效力级别"
            prop="effectivelevel"
          />
          <el-table-column
            align="center"
            v-if="item.name === '时效性'"
            label="时效性"
            prop="timeliness"
          />
          <el-table-column
            align="center"
            :formatter="formatDate"
            v-if="item.name === '发文日期'"
            label="发文日期"
            prop="publishdate"
          />
          <el-table-column
            align="center"
            :formatter="formatDate"
            v-if="item.name === '生效日期'"
            label="生效日期"
            prop="takeeffecttime"
          />
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDel(row)"
              :disabled="createId != row.createstaffid"
            >
              删除
            </el-button>
            <el-button type="text" @click="preview(row)">预览</el-button>
            <el-button type="text" @click="exportFile(row)">导出</el-button>
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
    <LcdyEdit ref="edit" @fetch-data="fetchData" />
    <LawPreview ref="preview" />
  </div>
</template>

<script>
  import { doDelete } from '@/api/table'
  import {
    delOutList,
    exportOutFile,
    getOutList,
  } from '@/api/workbench/auditTools'
  import { parseTime } from '@/utils/index'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import LawPreview from './components/LawPreview'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Consult',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, LcdyEdit, LawPreview },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
          type: 'nbsj',
        },
        // 筛选列表配置
        filedAll: [
          { name: '文件名称' },
          { name: '发文部门' },
          { name: '效力级别' },
          { name: '时效性' },
          { name: '发文日期' },
          { name: '生效日期' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-controlLib-law-search',
        tableKey: 'workbench-controlLib-law-list',
        searchMore: true,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
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
          { name: '发文文号', key: 'prulenumber' },
          { name: '文件名称', key: 'prulename' },
          { name: '内容', key: 'content' },
        ]
      },
      handleView(row, flag) {
        this.$refs['edit'].showEdit(row, flag)
      },
      async exportFile(row) {
        const data = await exportOutFile(row.outrulid)
        let fileName = row.rulename
        let blob = new Blob([data], {
          type: 'application/msword;charset=utf-8',
        })
        console.dir(111)
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
      handleDel(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await delOutList({ outrulid: row.outrulid })
          // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      preview(row) {
        console.dir(row)
        this.$refs['preview'].showEdit(row.bodyinfo)
      },
      resetQueryForm() {
        this.queryForm = {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
          type: 'nbsj',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
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
        } = await getOutList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
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
