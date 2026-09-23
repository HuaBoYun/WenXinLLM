<template>
  <div class="system-log-container">
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
                v-model="queryForm.documentName"
                clearable
                placeholder="文档名称"
                v-if="item.name === '文档名称'"
              />
              <el-select
                v-model="queryForm.documentType"
                clearable
                placeholder="文档类型"
                v-if="item.name === '文档类型'"
              >
                <el-option label="合同文件" :value="1" />
                <el-option label="技术文件" :value="2" />
                <el-option label="管理文件" :value="3" />
                <el-option label="财务文件" :value="4" />
              </el-select>
              <el-select
                v-model="queryForm.documentStatus"
                clearable
                placeholder="文档状态"
                v-if="item.name === '文档状态'"
              >
                <el-option label="有效" :value="1" />
                <el-option label="作废" :value="2" />
                <el-option label="归档" :value="3" />
              </el-select>
              <el-input
                v-model="queryForm.storageLocation"
                clearable
                placeholder="存储位置"
                v-if="item.name === '存储位置'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                type="primary"
                @click="queryData"
              >
                查询
              </el-button>
              <el-button @click="reset">重置</el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
      <vab-query-form-top-panel>
        <el-button
          icon="el-icon-plus"
          type="primary"
          @click="handleAdd"
        >
          新增文档
        </el-button>
        <el-button
          icon="el-icon-download"
          type="success"
          @click="handleExport"
        >
          导出
        </el-button>
      </vab-query-form-top-panel>

      <el-table
        v-loading="listLoading"
        :data="list"
        element-loading-text="正在查询中..."
        fit
        highlight-current-row
        @selection-change="setSelectRows"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          align="center"
          label="序号"
          width="95"
          type="index"
          :index="getTableIndex"
        />
        <el-table-column
          align="center"
          label="文档编号"
          prop="documentNo"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="文档名称"
          prop="documentName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="文档类型"
          prop="documentType"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-tag :type="getDocumentTypeStyle(scope.row.documentType)">
              {{ getDocumentTypeName(scope.row.documentType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="文档分类"
          prop="documentCategory"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="版本号"
          prop="versionNo"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="归档日期"
          prop="archiveDate"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="存储位置"
          prop="storageLocation"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="文档状态"
          prop="documentStatus"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-tag :type="getDocumentStatusStyle(scope.row.documentStatus)">
              {{ getDocumentStatusName(scope.row.documentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="操作" width="180">
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              @click="handleDownload(scope.row)"
            >
              下载
            </el-button>
            <el-button
              type="text"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        :layout="layout"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <DocumentArchiveEdit ref="edit" @fetch-data="fetchData" />
    <!-- 查看对话框 -->
    <DocumentArchiveView ref="view" />
  </div>
</template>

<script>
  import {
    getDocumentArchiveList,
    getDocumentArchiveById,
    deleteDocumentArchive
  } from '@/api/contract/archive'
  import DocumentArchiveEdit from './components/DocumentArchiveEdit'
  import DocumentArchiveView from './components/DocumentArchiveView'
  import { successCode } from '@/config/net.config'

  export default {
    name: 'DocumentArchive',
    components: {
      DocumentArchiveEdit,
      DocumentArchiveView
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        selectRows: '',
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          documentName: '',
          documentType: '',
          documentStatus: '',
          storageLocation: ''
        },
        searchItem: [
          { name: '文档名称' },
          { name: '文档类型' },
          { name: '文档状态' },
          { name: '存储位置' }
        ]
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      // 统一的响应成功判断方法
      isResponseSuccess(response) {
        return response && successCode.includes(response.code)
      },
      // 获取数据
      async fetchData() {
        this.listLoading = true
        try {
          console.log('开始获取文档归档列表...', this.queryForm)
          const response = await getDocumentArchiveList(this.queryForm)
          console.log('文档归档列表API响应:', response)

          if (response.code === 1) {
            // 修复数据结构处理：API返回的数据直接在data字段中，不是data.list
            this.list = response.data || []
            this.total = response.result?.total || 0
            console.log('文档归档列表数据:', this.list)
            console.log('总数:', this.total)
          } else {
            this.$message.error(response.msg || '获取数据失败')
          }
        } catch (error) {
          console.error('获取文档归档列表失败:', error)
          this.$message.error('获取数据失败')
        } finally {
          this.listLoading = false
        }
      },
      // 查询
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      // 重置
      reset() {
        this.$refs.form.resetFields()
        this.queryForm = {
          pageNumber: 1,
          pageSize: 10,
          documentName: '',
          documentType: '',
          documentStatus: '',
          storageLocation: ''
        }
        this.fetchData()
      },
      // 新增
      handleAdd() {
        this.$refs.edit.showEdit()
      },
      // 编辑
      handleEdit(row) {
        this.$refs.edit.showEdit(row)
      },
      // 查看
      handleView(row) {
        this.$refs.view.showView(row)
      },
      // 下载
      handleDownload(row) {
        this.$message.info('下载功能开发中...')
      },
      // 删除
      handleDelete(row) {
        this.$confirm('此操作将永久删除该文档记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          try {
            const response = await deleteDocumentArchive(row.id)
            console.log('删除接口返回数据:', response)
            console.log('删除响应状态码:', response.code)
            console.log('删除响应消息:', response.msg)

            // 使用统一的状态码判断方法
            if (this.isResponseSuccess(response)) {
              this.$message.success(response.msg || '删除成功')
              this.fetchData()
            } else {
              this.$message.error(response.message || response.msg || '删除失败')
            }
          } catch (error) {
            console.error('删除操作错误:', error)
            this.$message.error('删除失败：' + error.message)
          }
        })
      },
      // 导出
      handleExport() {
        this.$message.info('导出功能开发中...')
      },
      // 分页
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      // 多选
      setSelectRows(val) {
        this.selectRows = val
      },
      // 获取表格序号
      getTableIndex(index) {
        return (this.queryForm.pageNumber - 1) * this.queryForm.pageSize + index + 1
      },
      // 获取文档类型名称
      getDocumentTypeName(type) {
        const typeMap = {
          1: '合同文件',
          2: '技术文件',
          3: '管理文件',
          4: '财务文件'
        }
        return typeMap[type] || '未知'
      },
      // 获取文档类型样式
      getDocumentTypeStyle(type) {
        const styleMap = {
          1: 'primary',
          2: 'success',
          3: 'warning',
          4: 'info'
        }
        return styleMap[type] || 'info'
      },
      // 获取文档状态名称
      getDocumentStatusName(status) {
        const statusMap = {
          1: '有效',
          2: '作废',
          3: '归档'
        }
        return statusMap[status] || '未知'
      },
      // 获取文档状态样式
      getDocumentStatusStyle(status) {
        const styleMap = {
          1: 'success',
          2: 'danger',
          3: 'info'
        }
        return styleMap[status] || 'info'
      }
    }
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
