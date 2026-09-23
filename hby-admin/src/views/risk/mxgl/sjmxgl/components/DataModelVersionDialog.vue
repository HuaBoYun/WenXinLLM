<template>
  <el-dialog
    title="版本管理"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    append-to-body
  >
    <div class="version-management">
      <!-- 当前模型信息 -->
      <div class="model-info">
        <el-card shadow="never" style="margin-bottom: 20px;">
          <div slot="header" class="clearfix">
            <span style="font-weight: 500;">当前模型信息</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <span class="label">模型名称：</span>
                <span class="value">{{ modelData.modelName }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">模型编码：</span>
                <span class="value">{{ modelData.modelCode }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">当前版本：</span>
                <el-tag type="primary" size="small">{{ modelData.version }}</el-tag>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <!-- 工具栏 -->
      <div class="toolbar" style="margin-bottom: 20px;">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateVersion">
          创建新版本
        </el-button>
        <el-button icon="el-icon-refresh" @click="loadVersions">
          刷新
        </el-button>
      </div>

      <!-- 版本列表 -->
      <el-table
        :data="versionList"
        v-loading="loading"
        border
        style="width: 100%"
      >
        <el-table-column prop="versionNo" label="版本号" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.versionNo }}</span>
            <el-tag v-if="scope.row.isCurrent === 'Y'" type="success" size="mini" style="margin-left: 5px;">
              当前
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)" size="mini">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createUser" label="创建人" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="changeDescription" label="变更说明" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="280">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button size="mini" type="text" @click="handleCompare(scope.row)" v-if="scope.row.isCurrent !== 'Y'">
              对比
            </el-button>
            <el-button size="mini" type="text" @click="handlePublish(scope.row)"
                       v-if="scope.row.status === 'DRAFT' || scope.row.status === 'TESTING'">
              发布
            </el-button>
            <el-button size="mini" type="text" @click="handleSetCurrent(scope.row)"
                       v-if="scope.row.status === 'PUBLISHED' && scope.row.isCurrent !== 'Y'">
              设为当前
            </el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)"
                       v-if="scope.row.status === 'DRAFT' && scope.row.isCurrent !== 'Y'">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination" style="margin-top: 20px; text-align: right;">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getAllDataModelVersions,
  createDataModelVersion,
  deleteDataModelVersion,
  publishDataModelVersion,
  archiveDataModelVersion,
  setCurrentModelVersion,
  getDataModelVersionDetail,
  compareDataModelVersions,
  getNextDataModelVersionNo
} from '@/api/mxgl'

export default {
  name: 'DataModelVersionDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    modelData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      versionList: [],
      total: 0,
      queryForm: {
        pageNum: 1,
        pageSize: 20
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val && this.modelData.modelCode) {
        this.loadVersions()
      }
    }
  },
  methods: {
    // 加载版本列表
    async loadVersions() {
      if (!this.modelData.modelId) return

      try {
        this.loading = true
        // 使用新的API获取所有版本
        const response = await getAllDataModelVersions(this.modelData.modelId)

        if (response.code === 1) {
          this.versionList = response.data || []
          this.total = this.versionList.length
        } else {
          // 如果接口调用失败，显示当前版本作为示例
          this.versionList = [{
            versionId: this.modelData.modelId + '_v1',
            modelId: this.modelData.modelId,
            versionNo: this.modelData.version || 'v01',
            status: this.modelData.status || 'DRAFT',
            createUser: '系统',
            createTime: new Date(),
            updateTime: new Date(),
            isCurrent: 'Y'
          }]
          this.total = 1
          console.warn('版本管理接口调用失败，显示当前版本信息')
        }
      } catch (error) {
        console.error('获取版本列表失败:', error)
        // 接口调用失败时，显示当前版本作为示例
        this.versionList = [{
          versionId: this.modelData.modelId + '_v1',
          modelId: this.modelData.modelId,
          versionNo: this.modelData.version || 'v01',
          status: this.modelData.status || 'DRAFT',
          createUser: '系统',
          createTime: new Date(),
          updateTime: new Date(),
          isCurrent: 'Y'
        }]
        this.total = 1
      } finally {
        this.loading = false
      }
    },

    // 创建新版本
    async handleCreateVersion() {
      try {
        // 获取下一个版本号
        const versionNoResponse = await getNextDataModelVersionNo(this.modelData.modelId)
        const nextVersionNo = versionNoResponse.code === 1 ? versionNoResponse.data : 'v01'

        const result = await this.$prompt(`确定要基于当前模型创建新版本吗？\n版本号: ${nextVersionNo}`, '创建新版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '请输入变更说明（可选）',
          inputValue: '基于当前模型创建新版本'
        })

        if (result.value !== undefined) {
          const response = await createDataModelVersion({
            modelId: this.modelData.modelId,
            versionNo: nextVersionNo,
            changeDescription: result.value || '基于当前模型创建新版本'
          })

          if (response.code === 1) {
            this.$message.success('创建新版本成功')
            this.loadVersions()
            this.$emit('success')
          } else {
            this.$message.error(response.msg || '创建新版本失败')
          }
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('创建新版本失败:', error)
          this.$message.error('创建新版本失败')
        }
      }
    },

    // 查看版本
    async handleView(row) {
      try {
        const response = await getDataModelVersionDetail(row.versionId)
        if (response.code === 1) {
          // 显示版本详情对话框
          this.$alert(`
            <div style="text-align: left;">
              <p><strong>版本号:</strong> ${response.data.versionNo}</p>
              <p><strong>状态:</strong> ${this.getStatusLabel(response.data.status)}</p>
              <p><strong>创建人:</strong> ${response.data.createUser}</p>
              <p><strong>创建时间:</strong> ${this.formatTime(response.data.createTime)}</p>
              <p><strong>变更说明:</strong> ${response.data.changeDescription || '无'}</p>
            </div>
          `, '版本详情', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定'
          })
        } else {
          this.$message.error('获取版本详情失败')
        }
      } catch (error) {
        console.error('查看版本失败:', error)
        this.$message.error('查看版本失败')
      }
    },

    // 编辑版本
    handleEdit(row) {
      this.$message.info('编辑版本功能待实现')
    },

    // 对比版本
    handleCompare(row) {
      // 与当前版本对比
      const currentVersion = this.versionList.find(v => v.isCurrent === 'Y')
      if (!currentVersion) {
        this.$message.warning('未找到当前版本')
        return
      }

      if (currentVersion.versionId === row.versionId) {
        this.$message.warning('不能与自己对比')
        return
      }

      this.compareWithVersion(currentVersion.versionId, row.versionId)
    },

    // 版本对比
    async compareWithVersion(sourceVersionId, targetVersionId) {
      try {
        const response = await compareDataModelVersions(sourceVersionId, targetVersionId)
        if (response.code === 1) {
          const result = response.data
          const differences = result.differences || []

          if (differences.length === 0) {
            this.$message.info('两个版本没有差异')
            return
          }

          let diffHtml = '<div style="text-align: left;">'
          differences.forEach(diff => {
            diffHtml += `
              <div style="margin-bottom: 10px; padding: 8px; border-left: 3px solid #409EFF;">
                <strong>${diff.fieldLabel}:</strong><br>
                <span style="color: #F56C6C;">- ${diff.sourceValue || '(空)'}</span><br>
                <span style="color: #67C23A;">+ ${diff.targetValue || '(空)'}</span>
              </div>
            `
          })
          diffHtml += '</div>'

          this.$alert(diffHtml, `版本对比 (共${differences.length}处差异)`, {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '确定'
          })
        } else {
          this.$message.error('版本对比失败')
        }
      } catch (error) {
        console.error('版本对比失败:', error)
        this.$message.error('版本对比失败')
      }
    },

    // 删除版本
    async handleDelete(row) {
      try {
        await this.$confirm(`确定要删除版本 ${row.versionNo} 吗？`, '删除版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteDataModelVersion(row.versionId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadVersions()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除版本失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    // 发布版本
    async handlePublish(row) {
      try {
        await this.$confirm(`确定要发布版本 ${row.versionNo} 吗？`, '发布版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })

        const response = await publishDataModelVersion(row.versionId)
        if (response.code === 1) {
          this.$message.success('发布成功')
          this.loadVersions()
        } else {
          this.$message.error(response.msg || '发布失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('发布版本失败:', error)
          this.$message.error('发布失败')
        }
      }
    },

    // 设置为当前版本
    async handleSetCurrent(row) {
      try {
        await this.$confirm(`确定要将版本 ${row.versionNo} 设置为当前版本吗？`, '设置当前版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'info'
        })

        const response = await setCurrentModelVersion(row.versionId)
        if (response.code === 1) {
          this.$message.success('设置成功')
          this.loadVersions()
          this.$emit('success') // 通知父组件刷新
        } else {
          this.$message.error(response.msg || '设置失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('设置当前版本失败:', error)
          this.$message.error('设置失败')
        }
      }
    },

    // 分页相关
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNum = 1
      this.loadVersions()
    },

    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.loadVersions()
    },

    // 状态标签类型
    getStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'TESTING': 'warning', 
        'PUBLISHED': 'success'
      }
      return statusMap[status] || 'info'
    },

    // 状态标签文本
    getStatusLabel(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'TESTING': '测试中',
        'PUBLISHED': '已发布'
      }
      return statusMap[status] || status
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString('zh-CN')
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.version-management {
  min-height: 400px;
}

.info-item {
  margin-bottom: 8px;
}

.info-item .label {
  color: #606266;
  font-weight: 500;
}

.info-item .value {
  color: #303133;
  margin-left: 8px;
}

.dialog-footer {
  text-align: right;
}
</style>
