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
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-if="item.name === '问题来源'"
                v-model="queryForm.SOURCEPROBLEM"
                clearable
                placeholder="问题来源"
              />
              <el-select
                v-if="item.name === '问题类别'"
                v-model="queryForm.PROBLEMTYPE"
                clearable
                placeholder="问题类别"
                style="width: 100%"
              >
                <el-option
                  v-for="item in problemTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-select
                v-if="item.name === '整改类型'"
                v-model="queryForm.CORTYPE"
                clearable
                placeholder="整改类型"
                style="width: 100%"
              >
                <el-option
                  v-for="item in corTypeOptions"
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
          </el-form>
        </vab-query-form-left-panel>
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
        <el-button
          type="success"
          @click="handleDownloadTemplate"
          style="margin-right: 10px"
        >
          下载模板
        </el-button>
        <el-dropdown @command="handleExportCommand" style="margin-right: 10px">
          <el-button type="success">
            导出
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="exportAll">
              导出问题清单
            </el-dropdown-item>
            <el-dropdown-item command="exportSelected">
              导出责任清单
            </el-dropdown-item>
            <el-dropdown-item command="exportFiltered">
              导出整改清单
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload>
        <el-button
          type="success"
          @click="handleBatchDelete"
          :disabled="selectedRows.length === 0"
        >
          批量删除
        </el-button>
      </vab-query-form-right-panel>

      <el-table
        v-loading="listLoading"
        :data="list"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column align="center" label="一级单位" prop="firstorg" />
        <el-table-column
          align="center"
          label="具体责任单位"
          prop="specificdept"
        />
        <el-table-column align="center" label="问题来源" prop="sourceproblem" />
        <el-table-column align="center" label="问题类别" prop="problemtype" />
        <el-table-column align="center" label="整改类型" prop="cortype" />
        <el-table-column
          align="center"
          label="是否已完成整改"
          prop="corstatus"
        />
        <el-table-column align="center" label="操作" width="120">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleViewDetail(scope.row)"
            >
              查看详情
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

    <!-- 详情弹窗 -->
    <detail-dialog :visible.sync="dialogVisible" :detail-data="detailData" />
  </div>
</template>

<script>
  import {
    getBeforeList,
    deleteBeforeList,
    exportWTQD,
    exportZRQD,
    exportZGQD,
  } from '@/api/zgzz/index'
  import { baseURL } from '@/config'
  import store from '@/store'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import DetailDialog from './components/DetailDialog'

  const token = store.getters['user/token']
  export default {
    name: 'Wqzgqdhz',
    components: { filterSearch, filterTable, DetailDialog },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/zgzz/importBefore',
        headers: { token },
        listLoading: false,
        list: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          ID: '',
          SOURCEPROBLEM: '',
          PROBLEMTYPE: '',
          CORTYPE: '',
        },
        searchItem: [],
        searchAll: [],
        filedAll: this.getFiled(),
        filedNow: [],
        localKey: 'wqzgqdhz_search',
        tableKey: 'wqzgqdhz_table',
        btnLoading: false,
        dialogVisible: false,
        detailData: {},
        problemTypeOptions: [
          {
            label: '贯彻国家重大决策部署及国资委监管任务',
            value: '贯彻国家重大决策部署及国资委监管任务',
          },
          {
            label: '公司治理（战略执行、改革改制）',
            value: '公司治理（战略执行、改革改制）',
          },
          { label: '财务资金与会计核算', value: '财务资金与会计核算' },
          { label: '投资管理', value: '投资管理' },
          { label: '采购及销售管理', value: '采购及销售管理' },
          { label: '金融业务', value: '金融业务' },
          { label: '信息化管理', value: '信息化管理' },
          { label: '境外国有资产管理', value: '境外国有资产管理' },
          { label: '八项规定和廉洁从业', value: '八项规定和廉洁从业' },
          { label: '其他', value: '其他' },
        ],
        corTypeOptions: [
          { label: '立行立改', value: '立行立改' },
          { label: '分阶段整改', value: '分阶段整改' },
          { label: '持续整改', value: '持续整改' },
        ],
        selectedRows: [],
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
          { name: '序号', key: 'id' },
          { name: '问题来源', key: 'sourceproblem' },
          { name: '问题类别', key: 'problemtype' },
          { name: '整改类型', key: 'cortype' },
        ]
      },
      async fetchData() {
        this.listLoading = true
        const {
          code,
          data: { tlist, totalRecord },
          msg,
        } = await getBeforeList(this.queryForm)
        console.log('🚀 ~ fetchData ~ tlist:', tlist)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      resetSearch() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          ID: '',
          SOURCEPROBLEM: '',
          PROBLEMTYPE: '',
          CORTYPE: '',
        }
        this.fetchData()
      },
      initSearch() {
        // 初始化搜索
      },
      initTable() {
        // 初始化表格
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg || '导入失败', 'error')
        }
      },
      //下载模板
      handleDownloadTemplate() {
        // 获取当前域名和协议
        const baseUrl = window.location.origin
        // 拼接完整的文件URL
        const fileUrl = `${baseUrl}/files/整改清单.xlsx`

        // 创建一个隐藏的a标签用于下载
        const link = document.createElement('a')
        link.href = fileUrl
        link.setAttribute('download', '整改清单.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      // 处理导出命令
      handleExportCommand(command) {
        switch (command) {
          case 'exportAll':
            this.exportWTQD()
            break
          case 'exportSelected':
            this.exportZRQD()
            break
          case 'exportFiltered':
            this.exportZGQD()
            break
          default:
            break
        }
      },
      // 导出问题清单
      async exportWTQD() {
        try {
          const response = await exportWTQD(this.queryForm)
          // 处理文件下载
          const blob = new Blob([response], {
            type: 'application/vnd.ms-excel',
          })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = `问题清单_${new Date().getTime()}.xlsx`
          a.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } catch (error) {
          this.$message.error('导出失败：' + error.message)
        }
      },
      // 导出责任清单
      async exportZRQD() {
        try {
          const response = await exportZRQD(this.queryForm)
          // 处理文件下载
          const blob = new Blob([response], {
            type: 'application/vnd.ms-excel',
          })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = `责任清单_${new Date().getTime()}.xlsx`
          a.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } catch (error) {
          this.$message.error('导出失败：' + error.message)
        }
      },
      // 导出整改清单
      async exportZGQD() {
        try {
          const response = await exportZGQD(this.queryForm)
          // 处理文件下载
          const blob = new Blob([response], {
            type: 'application/vnd.ms-excel',
          })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = `整改清单_${new Date().getTime()}.xlsx`
          a.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } catch (error) {
          this.$message.error('导出失败：' + error.message)
        }
      },
      // 查看详情
      async handleViewDetail(row) {
        this.listLoading = true
        try {
          const {
            code,
            data: { tlist },
          } = await getBeforeList({
            ID: row.id,
            pageNumber: 1,
            pageSize: 1,
          })
          if (code === 1 && tlist && tlist.length > 0) {
            this.detailData = tlist[0]
            this.dialogVisible = true
          } else {
            this.$baseMessage('获取详情失败', 'error')
          }
        } catch (error) {
          console.error('获取详情出错:', error)
          this.$baseMessage('获取详情出错', 'error')
        } finally {
          this.listLoading = false
        }
      },

      // 表格选择项变化
      handleSelectionChange(selection) {
        this.selectedRows = selection
      },

      // 批量删除
      handleBatchDelete() {
        if (this.selectedRows.length === 0) {
          this.$baseMessage('请选择要删除的数据', 'warning')
          return
        }

        this.$confirm('确认删除选中的数据吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            // 获取选中行的ID
            const ids = this.selectedRows.map((row) => row.id).join(',')
            this.deleteRecords(ids)
          })
          .catch(() => {
            this.$baseMessage('已取消删除', 'info')
          })
      },

      // 调用删除接口
      async deleteRecords(ids) {
        this.listLoading = true
        try {
          // 这里需要替换为实际的删除API
          const response = await deleteBeforeList({
            ids,
          })
          if (response.code === 1) {
            this.$baseMessage('删除成功', 'success')
            this.selectedRows = []
            this.fetchData()
          } else {
            this.$baseMessage(response.msg || '删除失败', 'error')
          }
        } catch (error) {
          console.error('删除出错:', error)
          this.$baseMessage('删除出错', 'error')
        } finally {
          this.listLoading = false
        }
      },
    },
  }
</script>

<style lang="scss" scoped>
  .upload-demo {
    display: inline-block;
    margin-right: 10px;
  }
</style>
