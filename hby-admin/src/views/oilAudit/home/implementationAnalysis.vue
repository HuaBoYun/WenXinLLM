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
              <el-date-picker
                v-model="queryForm.xmnd"
                type="year"
                value-format="yyyy"
                placeholder="请选择"
                v-if="item.name == '项目年度'"
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
        <!-- <el-button type="success" @click="handleEdit(null)">新增</el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload>
        <el-button type="success" @click="handleExport">导出</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column align="center" label="序号" prop="gcxmzjZjbNo" /> -->
        <!-- <el-table-column align="center" label="合同编号" prop="htbh">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.htbh }}
            </el-button>
          </template>
        </el-table-column> -->
        <el-table-column
            align="center"
            label="项目年度"
            prop="xmnd"
          > 
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <!-- <el-table-column
            v-if="item.name === '项目年度'"
            align="center"
            label="项目年度"
            prop="xmnd"
          >
          <template #default="{ row }"> 
              {{ row.xmnd }} 
          </template>
        </el-table-column> -->
          <el-table-column
            v-if="item.name === '正在实施项目'"
            align="center"
            label="正在实施项目"
            prop="sszCount"
            show-overflow-tooltip
          > 
          <template #default="{ row }" > 
             <span style="cursor: pointer;color: red;" @click="handleDetail(row,2)">{{ row.sszCount }} </span> 
          </template> 
          </el-table-column>
          <el-table-column
            v-if="item.name === '未启动审计项目'"
            align="center"
            label="未启动审计项目"
            prop="wqdCount"
            show-overflow-tooltip
          >
          <template #default="{ row }"> 
            <span  style="cursor: pointer;color: red;" @click="handleZ(row)">{{ row.wqdCount }} </span> 
          </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '已完成项目'"
            align="center"
            label="已完成项目"
            prop="ywcCount"
            show-overflow-tooltip
          >
          <template #default="{ row }"> 
            <span style="cursor: pointer;color: red;" @click="handleDetail(row,1)">{{ row.ywcCount }} </span> 
          </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '执行率'"
            align="center"
            label="执行率"
            prop="zxl"
            show-overflow-tooltip
          >
          <template #default="{ row }"> 
              {{ row.zxl }} %
          </template>
          </el-table-column>
        </div>
        <el-table-column width="1"></el-table-column>

        <!-- <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
        >
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </el-table-column> -->
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
    <implementationAnalysisModel ref="xmnum"/>
    <jhlistEdit ref="jhlistEdit"/>
  </div>
</template>

<script>
import {
  planAnalysis, 
} from '@/oapi/audit/plan' 
import { formatDate } from '@/utils/index'
import filterSearch from '@/components/filterSearch'
import filterTable from '@/components/filterTable'
import { searchTableMixis } from '@/mixis/index'
import { baseURL } from '@/config'
import store from '@/store'
const token = store.getters['user/token']
import implementationAnalysisModel from './components/implementationAnalysisModel'
import jhlistEdit from './components/jhlistEdit'

export default {
  name: 'gcxmzjzjb',
  components: { filterSearch, filterTable, implementationAnalysisModel ,jhlistEdit},
  mixins: [searchTableMixis],
  data() {
    return {
      baseApi: baseURL,
      api: '/oiaudit/gcxmzjzjb/importData',
      headers: { token },
      list: [],
      listLoading: false,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: { 
        xmnd: '',
        pageNumber: 1,
        pageSize: 20,
      },
      filedAll: [
        // { name: '项目年度' },
        { name: '正在实施项目' },
        { name: '未启动审计项目' }, 
        { name: '已完成项目' }, 
        { name: '执行率' }, 
      ], //所有表格项
      filedNow: [], //当前表格项
      searchAll: this.getFiled(), //所有搜索项
      searchNow: [], //当前所有搜索项
      searchItem: [], //可见搜索项
      localKey: 'oilAudit-gcgl-implementationAnalysis-search',
      tableKey: 'oilAudit-gcgl-implementationAnalysis-list',
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
    async handleExport() {
      const data = await engineeringCostCenterTableExport({
        'tblYqnsGcxmzj.gcmc': this.queryForm.tblYqnsGcxmzj.gcmc,
        'tblYqnsGcxmzj.htbh': this.queryForm.tblYqnsGcxmzj.htbh,
        createYear: this.queryForm.createYear,
        pageNumber: this.queryForm.pageNumber,
        pageSize: this.queryForm.pageSize,
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
    handleZ(row) {
      this.$refs['jhlistEdit'].showEdit(row, true)
    }, 
    getFiled() {
      return [
        { name: '项目年度', key: 'xmnd' }, 
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
      this.listLoading = true
      const {
        data:{ pageInfo:{ tlist, totalRecord }},
        code,
      } = await planAnalysis({ 
        ...this.queryForm
      })
      this.listLoading = false
      if (code === 1) {
        this.list = tlist
        this.total = totalRecord || 0
      }
    },
    async handleDetail(row,type) {
      await this.$refs['xmnum'].showEdit(row, type)
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
