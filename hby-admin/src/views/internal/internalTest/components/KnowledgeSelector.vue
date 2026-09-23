<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="选择相关规章制度"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <div class="knowledge-selector">
      <el-table ref="multipleTable" v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="文件夹名称"
          prop="folderName"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="部门"
          prop="departName"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="showDetails(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        class="pager"
        :current-page="queryForm.pageNumber"
        layout="total, sizes, prev, pager, next, jumper"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定</el-button>
    </div>

    <!-- 文件夹详情弹窗 -->
    <FolderDetails
      :visible.sync="folderDetailsVisible"
      :folder-id="selectedFolderId"
      :folder-path="selectedFolderPath"
      :enable-selection="true"
      @confirm="handleFolderDetailsConfirm"
    />
  </el-dialog>
</template>

<script>
  import { knowledge } from '@/api/setting/knowledge.js'
  import FolderDetails from './FolderDetails.vue'

  export default {
    name: 'KnowledgeSelector',
    components: {
      FolderDetails,
    },
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        dialogVisible: false,
        list: [],
        listLoading: false,
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
        },
        selectedItems: [], // 选中的项目
        // FolderDetails相关
        folderDetailsVisible: false,
        selectedFolderId: '',
        selectedFolderPath: '',
      }
    },
    watch: {
      visible: {
        handler(val) {
          this.dialogVisible = val
          if (val) {
            this.fetchData()
          }
        },
        immediate: true,
      },
    },
    methods: {
      /**
       * @description: 获取数据
       */
      async fetchData() {
        this.listLoading = true
        try {
          const {
            data: { folderOptCountList, totalCount },
          } = await knowledge({
            pageNumber: this.queryForm.pageNumber,
            pageSize: this.queryForm.pageSize,
          })
          this.list = folderOptCountList || []
          this.total = totalCount || 0
        } catch (error) {
          console.error('获取数据失败:', error)
          this.$message.error('获取数据失败，请稍后重试')
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
      },

      /**
       * @description: 分页大小变化
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },

      /**
       * @description: 当前页变化
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },

      /**
       * @description: 关闭弹窗
       */
      handleClose() {
        this.dialogVisible = false
        this.$emit('update:visible', false)
        // 清空选择
        this.selectedItems = []
      },

      /**
       * @description: 确认选择
       */
      handleConfirm() {
        // 收集所有有选中文件的文件夹
        const foldersWithFiles = this.list.filter(
          (folder) => folder.selectedFiles && folder.selectedFiles.length > 0
        )
        
        if (foldersWithFiles.length === 0) {
          this.$message.warning('请先选择文件')
          return
        }
        
        // 将选中的文件夹数据传递给父组件
        this.$emit('confirm', foldersWithFiles)
        this.handleClose()
      },

      /**
       * @description: 显示文件夹详情
       */
      showDetails(row) {
        this.selectedFolderId = row.folderId
        this.selectedFolderPath = row.folderPath || row.folderName
        this.folderDetailsVisible = true
      },

      /**
       * @description: 处理文件夹详情确认选择
       */
      handleFolderDetailsConfirm(selectedFiles) {
        // 将选中的文件添加到当前行的数据中
        const currentFolder = this.list.find(
          (item) => item.folderId === this.selectedFolderId
        )
        if (currentFolder) {
          // 将选中的文件信息添加到文件夹数据中
          currentFolder.selectedFiles = selectedFiles
          currentFolder.fileCount = selectedFiles.length

          // 如果当前文件夹已经在选中列表中，更新其文件信息
          const existingIndex = this.selectedItems.findIndex(
            (item) => item.folderId === this.selectedFolderId
          )
          if (existingIndex !== -1) {
            this.selectedItems[existingIndex] = { ...currentFolder }
          }
        }

        this.$message.success(`已选择 ${selectedFiles.length} 个文件`)
      },
    },
  }
</script>

<style scoped lang="scss">
  .knowledge-selector {
    .pager {
      margin-top: 20px;
      text-align: center;
    }
  }

  .dialog-footer {
    text-align: right;
  }
</style>
