<template>
  <!-- 工程项目造价中间表 -->
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
                v-model="queryForm.tblYqnsGcxmzj.gcmc"
                clearable
                placeholder="工程名称"
                v-if="item.name == '工程名称'"
              />
              <el-input
                v-model="queryForm.jsdw"
                clearable
                placeholder="建设单位"
                v-if="item.name === '建设单位'"
              />
              <el-input
                v-model="queryForm.tblYqnsGcxmzj.htbh"
                clearable
                placeholder="合同编号"
                v-if="item.name == '合同编号'"
              />
              <el-date-picker
                v-model="queryForm.createYear"
                type="year"
                value-format="yyyy"
                placeholder="请选择创建年度"
                v-if="item.name == '创建年度'"
              ></el-date-picker>
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
        <el-button type="success" @click="handleEdit(null)">新增</el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload>
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange" @sort-change="sortChange">
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column
          align="center"
          label="序号"
          prop="gcxmzjZjbNo"
          sortable="custom"
        />
        <el-table-column align="center" label="合同编号" prop="htbh">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.htbh }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '工程名称'"
            align="center"
            label="工程名称"
            prop="gcmc"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '建设单位'"
            align="center"
            label="建设单位"
            prop="jsdw"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '二审审查金额（万元）'"
            align="center"
            label="二审审查金额（万元）"
            prop="esscjewy"
            show-overflow-tooltip
            min-width="90"
            :formatter="(row) => row.esscjewy?.toFixed(2)"
          />
          <el-table-column
            v-if="item.name === '额度（万元）'"
            align="center"
            label="额度（万元）"
            prop="edje"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '内外部'"
            align="center"
            label="内外部"
            prop="nwb"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '施工单位'"
            align="center"
            label="施工单位"
            prop="sgdw"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '联系人'"
            align="center"
            label="联系人"
            prop="lxr"
          />
          <el-table-column
            v-if="item.name === '联系电话'"
            align="center"
            label="联系电话"
            prop="lxdh"
          />
          <el-table-column
            v-if="item.name === '项目类型'"
            align="center"
            label="项目类型"
            prop="xmzttype"
          />
          <el-table-column
            v-if="item.name === '项目状态'"
            align="center"
            label="项目状态"
            prop="xmstatus"
          >
            <template #default="{ row }">
              {{ row.xmstatus == 1 ? '已做审计项目' : '未做审计项目' }}
            </template>
          </el-table-column>
        </div>

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
        >
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
    <gcxmzjzjbEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    engineeringCostCenterTableList,
    engineeringCostCenterTableDelete,
    engineeringCostCenterTableExport,
  } from '@/oapi/audit/plan'
  import gcxmzjzjbEdit from './components/gcxmzjzjbEdit'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  export default {
    name: 'gcxmzjzjb',
    components: { filterSearch, filterTable, gcxmzjzjbEdit },
    mixins: [searchTableMixis],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/gcxmzjzjb/importData',
        headers: { token },
        select: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          tblYqnsGcxmzj: {
            gcmc: '',
            htbh: '',
          },
          createYear: '',
          jsdw: '',
          pageNumber: 1,
          pageSize: 20,
          order: 1,
        },
        filedAll: [
          { name: '合同编号' },
          { name: '工程名称' },
          { name: '建设单位' },
          { name: '二审审查金额（万元）' },
          { name: '额度（万元）' },
          { name: '内外部' },
          { name: '施工单位' },
          { name: '联系人' },
          { name: '联系电话' },
          { name: '项目类型' },
          { name: '项目状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-gcgl-gcxmzjzjb-search',
        tableKey: 'oilAudit-gcgl-gcxmzjzjb-list',
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
      handleSelectionChange(val) {
        this.select = val;
      },
      async handleExport() {
        const ids = this.select.map((res) => res.gcxmzjzjbid)
        const data = await engineeringCostCenterTableExport({
          'tblYqnsGcxmzj.gcmc': this.queryForm.tblYqnsGcxmzj.gcmc,
          'tblYqnsGcxmzj.htbh': this.queryForm.tblYqnsGcxmzj.htbh,
          createYear: this.queryForm.createYear,
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize,
          ids: ids.join(),
        })
        let fileName = '工程项目造价中间表'
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
      handleSuccess(response) {
        if (response.code == 1) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      getFiled() {
        return [
          { name: '工程名称', key: 'gcmc' },
          { name: '建设单位', key: 'jsdw' },
          { name: '合同编号', key: 'htbh' },
          { name: '创建年度', key: 'createYear' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          tblYqnsGcxmzj: {
            gcmc: '',
            htbh: '',
          },
          createYear: '',
          jsdw: '',
          pageNumber: 1,
          pageSize: 20,
          order: 1,
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
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
          code,
        } = await engineeringCostCenterTableList({
          'tblYqnsGcxmzj.gcmc': this.queryForm.tblYqnsGcxmzj.gcmc,
          'tblYqnsGcxmzj.htbh': this.queryForm.tblYqnsGcxmzj.htbh,
          createYear: this.queryForm.createYear,
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize,
          order: this.queryForm.order,
          jsdw: this.queryForm.jsdw,
        })
        this.listLoading = false
        if (code === 1) {
          this.list =
            tlist.map((item) => ({
              ...item,
              ...item.tblYqnsGcxmzj,
            })) || []
          this.total = totalRecord || 0
        }
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, true)
      },
      async handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.listLoading = true
          const res = await engineeringCostCenterTableDelete({
            ids: row.gcxmzjzjbid,
          })
          this.listLoading = false
          if (res && res.code == 1) {
            this.$message.success('操作成功！')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败！')
          }
        })
      },
      async sortChange(column) {
        let { order } = column
        if (order === 'ascending') {
          this.queryForm.order = 0
        } else if (order === 'descending') {
          this.queryForm.order = 1
        } else {
          this.queryForm.order = 1
        }
        await this.fetchData()
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
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
