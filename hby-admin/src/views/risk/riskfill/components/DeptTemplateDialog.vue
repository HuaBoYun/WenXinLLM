<template>
  <el-dialog
    title="部门模板创建"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div class="dept-template-dialog">
      <!-- 部门选择 -->
      <div class="dept-select-section" style="margin-bottom: 20px">
        <el-form :inline="true">
          <el-form-item label="选择部门：">
            <div style="display: flex; align-items: center">
              <el-input
                v-model="deptName"
                disabled
                style="margin-right: 8px"
              ></el-input>
              <el-button
                type="primary"
                @click="$refs['comTreeRef'].show(false, [])"
              >
                选择
              </el-button>
              <el-button
                type="primary"
                @click="handleCancelDept"
                :disabled="selectedItems.length === 0"
                style="margin-left: 8px"
              >
                取消选中的部门
              </el-button>
            </div>
          </el-form-item>
        </el-form>
        <div style="color: red; font-weight: bold">
          请注意：模板数据需要一次性配置全
        </div>
      </div>

      <el-table
        ref="templateTable"
        v-loading="loading"
        :data="templateList"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="type1" label="一级分类" min-width="120" />
        <el-table-column
          prop="type2"
          label="二级分类"
          min-width="120"
        ></el-table-column>
        <el-table-column prop="type3" label="三级分类" min-width="120">
          <template slot-scope="scope">
            {{ scope.row.type3 || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="codeName" label="风险名称" min-width="200" />
        <el-table-column prop="deptName" label="所属部门" min-width="150" />
      </el-table>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="saving">
        确 定
      </el-button>
    </span>
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectCompany" />
  </el-dialog>
</template>

<script>
  import {
    getRiskMonDeptList,
    saveMonDictonaryDept,
  } from '@/api/risk/monitoring'
  import CompanyTreeModel from '@/components/CompanyTreeModel'

  export default {
    name: 'DeptTemplateDialog',
    components: {
      CompanyTreeModel,
    },
    data() {
      return {
        dialogVisible: false,
        loading: false,
        saving: false,
        templateList: [],
        originalTemplateList: [], // 保存原始数据用于对比
        selectedItems: [],
        selectedDeptId: null,
        deptName: '', // 选中的部门名称
      }
    },
    computed: {
      canConfirm() {
        // 检查是否有已分配部门的数据
        return this.templateList.some((item) => item.deptId && item.deptName)
      },
    },
    methods: {
      // 显示弹窗
      async show() {
        this.dialogVisible = true
        await this.fetchTemplateData()
      },

      // 获取模板数据
      async fetchTemplateData() {
        this.loading = true
        try {
          const res = await getRiskMonDeptList()
          if (res.code === 1) {
            // 确保从接口返回的数据中正确提取已分配的部门信息
            this.templateList = (res.data.data || []).map((item) => ({
              ...item,
              // 如果接口返回的数据中包含部门信息，确保正确映射
              deptId: +item.deptId || item.dept_id || null,
              deptName: item.deptName || item.dept_name || '',
            }))
            // 保存原始数据的深拷贝用于对比
            this.originalTemplateList = JSON.parse(
              JSON.stringify(this.templateList)
            )
          } else {
            this.$message.error('获取模板数据失败')
          }
        } catch (error) {
          console.error('获取模板数据失败:', error)
          this.$message.error('获取模板数据失败')
        } finally {
          this.loading = false
        }
      },

      // 处理选择变化
      handleSelectionChange(selection) {
        this.selectedItems = selection
      },
      // 关闭弹窗
      handleClose() {
        this.dialogVisible = false
        this.selectedItems = []
        this.templateList = []
        this.originalTemplateList = []
        this.selectedDeptId = null
        this.deptName = ''
      },
      // 检查数据是否有变化
      hasDataChanged() {
        // 对比当前数据和原始数据
        if (this.templateList.length !== this.originalTemplateList.length) {
          return true
        }

        for (let i = 0; i < this.templateList.length; i++) {
          const current = this.templateList[i]
          const original = this.originalTemplateList[i]

          // 比较关键字段：deptId 和 deptName
          if (
            current.deptId !== original.deptId ||
            current.deptName !== original.deptName
          ) {
            return true
          }
        }

        return false
      },

      // 确认选择并保存
      async handleConfirm() {
        // 检查数据是否有变化
        if (!this.hasDataChanged()) {
          this.dialogVisible = false
          return
        }

        this.saving = true
        try {
          // 构造保存数据 - 收集所有已分配部门的数据
          const saveData = []

          // 遍历模板列表，找出所有已分配部门的项目
          this.templateList.forEach((item) => {
            if (item.deptId && item.deptName) {
              saveData.push({
                dictonaryId: item.id,
                deptId: item.deptId,
              })
            }
          })

          if (saveData.length === 0) {
            this.$message.warning('请先为模板项目分配部门')
            return
          }

          const res = await saveMonDictonaryDept(saveData)
          if (res.code === 1) {
            this.$message.success(
              `部门模板分配成功，共分配${saveData.length}条记录`
            )
            this.selectedDeptId = null
            this.deptName = ''
            this.fetchTemplateData()
          } else {
            this.$message.error(res.msg || '保存失败')
          }
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error('保存失败')
        } finally {
          this.saving = false
        }
      },
      handleSelectCompany(e) {
        console.log('🚀 ~ handleSelectCompany ~ e:', e)
        this.selectedDeptId = e.id
        this.deptName = e.name

        // 给勾选的数据添加部门信息
        if (this.selectedItems.length > 0) {
          this.selectedItems.forEach((item) => {
            item.deptId = e.id
            item.deptName = e.name
          })

          // 更新表格中对应行的部门信息
          this.templateList.forEach((template) => {
            const selectedItem = this.selectedItems.find(
              (selected) => selected.id === template.id
            )
            if (selectedItem) {
              template.deptId = e.id
              template.deptName = e.name
            }
          })

          // 清空表格勾选状态
          this.$refs.templateTable.clearSelection()
          this.selectedItems = []
        }
      },
      // 取消当前选中的部门
      handleCancelDept() {
        this.selectedDeptId = null
        this.deptName = ''

        // 清空已选中项目的部门信息
        if (this.selectedItems.length > 0) {
          this.selectedItems.forEach((item) => {
            item.deptId = null
            item.deptName = ''
          })

          // 更新表格中对应行的部门信息
          this.templateList.forEach((template) => {
            const selectedItem = this.selectedItems.find(
              (selected) => selected.id === template.id
            )
            if (selectedItem) {
              template.deptId = null
              template.deptName = ''
            }
          })

          // 清空表格勾选状态
          this.$refs.templateTable.clearSelection()
          this.selectedItems = []
        }

        this.$message.success('已取消部门选择')
      },
    },
  }
</script>

<style scoped>
  .dept-template-dialog {
    max-height: 500px;
    overflow-y: auto;
  }

  .dialog-footer {
    text-align: right;
  }

  .el-table {
    border: 1px solid #ebeef5;
  }

  .el-table th {
    background-color: #f5f7fa;
  }
</style>
