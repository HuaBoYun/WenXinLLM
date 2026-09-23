<template>
  <el-dialog
    title="文件夹详情"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
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
                  v-model="queryForm.fileExtName"
                  clearable
                  placeholder="文件扩展名（如：pdf、docx）"
                  v-if="item.name === '文件扩展名'"
                />
                <!-- <el-input
                  v-model="queryForm.fileRemark"
                  clearable
                  placeholder="文件备注"
                  v-if="item.name === '文件备注'"
                /> -->
                <el-input
                  v-model="queryForm.fileSecurityLevel"
                  clearable
                  placeholder="文件名"
                  v-if="item.name === '文件名'"
                />
                <!-- <el-input
                  v-model="queryForm.fileVerCode"
                  clearable
                  placeholder="文件版本号"
                  v-if="item.name === '文件版本号'"
                /> -->
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  native-type="submit"
                  type="primary"
                  @click="handleSearch"
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

      <el-card shadow="never" v-loading="loading">
        <el-table :data="detailsList" stripe border class="detail-table">
          <el-table-column
            label="文件名称"
            prop="fileName"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            label="部门名称"
            prop="departmentName"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              {{ formatDepartmentName(scope.row.departmentName) }}
            </template>
          </el-table-column>
          <el-table-column
            label="文件备注"
            prop="fileRemark"
            width="100"
            align="center"
          />

          <el-table-column
            label="文件扩展名"
            prop="extName"
            width="100"
            align="center"
          ></el-table-column>
          <el-table-column
            label="版本号"
            prop="fileCurVerNumStr"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column
            align="center"
            label="密级"
            prop="securityLevelName"
          />
          <el-table-column
            label="文件版本编码"
            prop="fileVerCode"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column label="操作" width="120" align="center">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                @click="downloadFile(scope.row)"
              >
                预览
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </el-card>
    </div>
  </el-dialog>
</template>

<script>
  import {
    knowledgeSearch,
    getFilePreviewUrl,
  } from '@/api/setting/knowledge.js'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'FolderDetails',
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable },
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
      folderId: {
        type: [Number, String],
        default: '',
      },

      folderPath: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        loading: false,
        detailsList: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          folderId: '',
          fileRemark: '', // 文件备注
          fileExtName: '', // 文件扩展名
          fileVerCode: '', // 文件版本号
          fileSecurityLevel: '', // 文件密级
        },
        // 筛选列表配置
        filedAll: [
          { name: '文件名称' },
          { name: '部门名称' },
          { name: '文件备注' },
          { name: '文件扩展名' },
          { name: '创建时间' },
          { name: '创建人' },
          { name: '版本号' },
          { name: '文件版本编码' },
        ], // 所有表格项
        searchAll: this.getFiled(), // 所有搜索项
        searchNow: [], // 当前所有搜索项
        searchItem: [], // 可见搜索项
        localKey: 'workbench-controlLib-folderDetails-search',
        tableKey: 'workbench-controlLib-folderDetails-list',
        searchMore: true,
      }
    },
    computed: {
      dialogVisible: {
        get() {
          return this.visible
        },
        set(val) {
          this.$emit('update:visible', val)
        },
      },
    },
    watch: {
      visible(val) {
        if (val && this.folderId) {
          this.queryForm.folderId = this.folderId
          this.fetchData()
          this.initTable()
          this.searchNow = this.getFiled()
          this.searchItem = this.searchNow.slice(0, 4)
          this.initSearch()
        }
      },
    },
    methods: {
      getFiled() {
        return [
          { name: '文件扩展名', key: 'fileExtName' },
          { name: '文件备注', key: 'fileRemark' },
          { name: '文件名', key: 'fileSecurityLevel' },
          { name: '文件版本号', key: 'fileVerCode' },
        ]
      },

      // 获取详情数据
      async fetchData() {
        this.loading = true

        try {
          console.log(
            '调用knowledgeSearch - folderId:',
            this.queryForm.folderId
          )
          const {
            data: { fileList, totalCount },
            code,
            msg,
          } = await knowledgeSearch({
            folderId: this.queryForm.folderId,
            pageSize: this.queryForm.pageSize,
            pageNumber: this.queryForm.pageNumber,
            fileExtName: this.queryForm.fileExtName, // 文件扩展名
            fileRemark: this.queryForm.fileRemark, // 文件备注
            fileSecurityLevel: this.queryForm.fileSecurityLevel, // 文件密级
            fileVerCode: this.queryForm.fileVerCode, // 文件版本号
          })

          console.log('knowledgeSearch结果:', {
            fileList,
            totalCount,
            code,
            msg,
          })
          this.detailsList = fileList
          this.total = totalCount
        } catch (error) {
          console.error('获取文件夹详情失败:', error)
          this.$message.error('获取详情失败，请稍后重试')
        } finally {
          this.loading = false
        }
      },
      // 分页处理
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      // 关闭弹窗
      handleClose() {
        this.dialogVisible = false
        this.detailsList = []
        this.queryForm.pageNumber = 1
      },
      // 搜索
      handleSearch() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      // 重置搜索条件
      resetSearch() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          folderId: this.folderId,
          fileRemark: '', // 文件备注
          fileExtName: '', // 文件扩展名
          fileVerCode: '', // 文件版本号
          fileSecurityLevel: '', // 文件密级
        }
        this.fetchData()
      },
      // 刷新数据
      refreshData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      // 预览文件
      async downloadFile(val) {
        try {
          const {
            code,
            data: { data },
            msg,
          } = await getFilePreviewUrl({ fileId: val.fileId })
          if (code == 200) {
            // 在新窗口打开预览
            window.open(data, '_blank')
          } else {
            this.$message.error(msg)
          }
        } catch (error) {
          console.error('获取预览链接失败:', error)
          this.$message.error('获取预览链接失败')
        }
      },
      // 格式化部门名称，移除括号中的数字
      formatDepartmentName(departmentName) {
        if (!departmentName) return ''
        // 使用正则表达式移除英文括号和中文括号及其中的内容
        return departmentName.replace(/[\(（][^\)）]*[\)）]/g, '')
      },
    },
  }
</script>

<style scoped lang="scss">
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }

  .header-tools {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }

  .folder-info {
    font-size: 14px;
    color: #606266;
  }

  .detail-table {
    margin-bottom: 15px;
  }

  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .margin-b0 {
    margin-bottom: 0;
  }

  .biaoge {
    margin-right: 10px;
  }
</style>
