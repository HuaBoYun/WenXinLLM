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
              v-for="(item, index) in queryForm.filterColList"
              :key="item.fid"
            >
              <el-input
                v-model="item.queryData"
                clearable
                :placeholder="item.fname"
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
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <div style="display: flex">
      <el-card style="width: 220px" class="table-list-card">
        <div slot="header" class="card-header">
          <span>数据表列表</span>
        </div>
        <div class="table-list-container">
          <div
            v-for="item in tableList"
            :key="item.fid"
            :class="[
              'table-item',
              { 'table-item-active': curTable.fid === item.fid },
            ]"
            @click="onSelectTable(item)"
          >
            <i class="el-icon-document table-icon"></i>
            <span class="table-name">{{ item.fname }}</span>
          </div>
        </div>
      </el-card>
      <el-card shadow="never" style="flex: 1">
        <el-table
          ref="multipleTable"
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @select-all="selectAll"
          @select="handleSelectionChange"
          @current-change="handleRowChange"
          style="width: 100%"
        >
          <!-- <el-table-column type="selection" /> -->
          <el-table-column
            align="center"
            v-for="item in pageColList"
            :key="item.fid"
            :label="item.fname"
            :prop="item.oursColname"
          />

          <el-table-column width="1" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="80"
          >
            <template #default="{ row }">
              <el-button type="text" @click="onDetail(row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <el-dialog
      title="详情"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="dialogFormVisible = false"
      :close-on-click-modal="false"
    >
      <accountManageForm
        v-if="dialogFormVisible"
        ref="accountManageForm"
        :colList="colList"
        :dataMap="dataMap"
      />
    </el-dialog>
  </div>
</template>

<script>
  import {
    showTableList,
    showBusinessDataList,
    showBusinessDataDetail,
    downloadTemplate,
  } from '@/api/workbench/accountManage'
  import accountManageForm from '@/views/cwsc/jcpz/companyData/accountManageForm.vue'
  import { mapGetters } from 'vuex'

  export default {
    name: 'Consult',
    components: {
      accountManageForm,
    },
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        curUserSelected: {},
        curSelected: {},
        queryForm: {
          filterColList: [],
          pageNumber: 1,
          pageSize: 20,
        },

        tableList: [],
        curTable: {},
        pageColList: [],
        colList: [],
        dataMap: {},
        dialogFormVisible: false,
        showButton: false,

        // 导入相关
        importModalVisible: false,
        recordModalVisible: false,
        fileList: [],
      }
    },
    created() {
      this.fetchTabelList()
      this.updateThemeColors()
    },
    watch: {
      theme: {
        handler() {
          this.updateThemeColors()
        },
        deep: true,
      },
    },
    methods: {
      updateThemeColors() {
        // 根据当前主题设置CSS变量
        const themeColors = {
          default: { primary: '#1890ff', light: '#e6f7ff', rgb: '24, 144, 255' },
          white: { primary: '#1890ff', light: '#e6f7ff', rgb: '24, 144, 255' },
          ocean: { primary: '#399efd', light: '#e6f7ff', rgb: '57, 158, 253' },
          green: { primary: '#41b584', light: '#e8f5f0', rgb: '65, 181, 132' },
          red: { primary: '#e50113', light: '#fde8e8', rgb: '229, 1, 19' },
        }
        
        const currentTheme = this.theme?.themeName || 'default'
        const colors = themeColors[currentTheme] || themeColors.default
        
        // 设置CSS变量
        const root = document.documentElement
        root.style.setProperty('--theme-color', colors.primary)
        root.style.setProperty('--theme-color-light', colors.light)
        root.style.setProperty('--theme-color-rgb', colors.rgb)
      },
      resetQueryForm() {
        this.queryForm.filterColList = []
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData('1')
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      async onDetail(row) {
        this.listLoading = true
        const res = await showBusinessDataDetail({
          tableId: this.curTable.fid,
          filterColList: this.pageColList.filter((x) => {
            if (x.isPrimaryKey == 0) {
              x.queryData = row[x.oursColname]
              return true
            }
            return false
          }),
        })
        this.listLoading = false
        if (res && res.code == 1) {
          this.colList = res.data.colList
          this.dataMap = res.data.dataMap
          this.dialogFormVisible = true
        }
      },
      onSelectTable(item) {
        // 判断是否显示按钮
        this.showButton = item.financeType === 2 || item.financeType === 3
        if (item.fid !== this.curTable.fid) {
          this.curTable = item
          this.fetchData('1')
        }
      },
      async fetchTabelList() {
        this.listLoading = true
        const res = await showTableList()
        if (res && res.code == 1) {
          if (res.data && res.data.length) {
            this.tableList = res.data
            this.curTable = res.data[0]
            this.showButton =
              this.curTable.financeType === 2 || this.curTable.financeType === 3
            await this.fetchData('1')
            return
          }
        }
        this.listLoading = false
      },
      async fetchData(switchTable) {
        console.log('switchTable', switchTable)
        this.listLoading = true
        const res = await showBusinessDataList({
          filterColList: this.queryForm.filterColList.filter(
            (x) => !!x.queryData
          ),
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize,
          tableId: this.curTable.fid,
        })
        if (res && res.code == 1) {
          if (switchTable === '1') {
            this.queryForm.filterColList = res.data.filter
          }
          this.pageColList = res.data.pageColList
          this.list = res.data.page.records
          this.total = res.data.page.total
        }
        this.listLoading = false
      },
      async getSelectedBook() {
        const { data } = await getSelectedBookInfo()
        if (data) {
          const row = this.list.find((x) => x.bookId === data.bookId)
          this.curUserSelected = row
          localStorage.setItem('bookInfo', JSON.stringify(data))
          this.$nextTick(() => {
            this.singleSelect(row)
          })
        }
      },
      selectAll() {
        this.singleSelect(this.curUserSelected)
      },
      handleSelectionChange(selection, row) {
        this.singleSelect(row)
      },
      handleRowChange(row) {
        this.singleSelect(row)
      },
      singleSelect(row) {
        this.curSelected = row
        this.$refs.multipleTable.clearSelection()
        this.$refs.multipleTable.toggleRowSelection(row)
      },
      async handleSave() {
        if (!this.curSelected.bookId)
          return this.$message({ type: 'error', message: '请选择要操作的账套' })
        if (this.curSelected.bookId === this.curUserSelected?.bookId)
          return this.$message({ type: 'error', message: '无更改' })

        this.listLoading = true
        const { code } = await checkBook({
          bookId: this.curSelected.bookId,
        })
        if (code === 1) {
          this.$message({
            type: 'success',
            message: '操作成功!',
          })
          // await this.getSelectedBook()
        } else {
          this.$message({
            type: 'error',
            message: '操作失败!',
          })
        }
        this.listLoading = false
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

  /* 表格列表卡片样式 */
  .table-list-card {
    margin-right: 16px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .card-header {
    font-weight: 600;
    color: #303133;
    font-size: 16px;
  }

  .table-list-container {
    max-height: 1000px;
    overflow-y: auto;
  }

  .table-item {
    display: flex;
    align-items: center;
    padding: 12px 8px;
    margin-bottom: 4px;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 1px solid transparent;
  }

  .table-item:hover {
    background-color: #f5f7fa;
    border-color: #e4e7ed;
  }

  .table-item-active {
    background-color: var(--theme-color-light, #ecf5ff);
    border-color: var(--theme-color, #409eff);
    color: var(--theme-color, #409eff);
  }

  .table-item-active:hover {
    background-color: var(--theme-color-light, #ecf5ff);
    border-color: var(--theme-color, #409eff);
  }

  .table-icon {
    margin-right: 8px;
    font-size: 16px;
    color: #909399;
  }

  .table-item-active .table-icon {
    color: #409eff;
  }

  .table-name {
    font-size: 14px;
    line-height: 1.4;
    word-break: break-all;
  }

  /* 滚动条样式 */
  .table-list-container::-webkit-scrollbar {
    width: 6px;
  }

  .table-list-container::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  .table-list-container::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;
  }

  .table-list-container::-webkit-scrollbar-thumb:hover {
    background: #a8a8a8;
  }
</style>
