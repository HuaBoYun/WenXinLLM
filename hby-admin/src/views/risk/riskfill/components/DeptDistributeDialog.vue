<template>
  <el-dialog
    title="选择下发部门"
    :visible.sync="dialogVisible"
    width="800px"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <div class="dept-distribute-container">
      <el-table v-loading="listLoading" :data="deptList" style="width: 100%">
        <el-table-column
          prop="deptName"
          label="部门名称"
          min-width="200"
        ></el-table-column>
        <el-table-column
          prop="selectedPersonName"
          label="选择人员"
          min-width="150"
        >
          <template #default="{ row }">
            <span v-if="row.selectedPersonName">
              {{ row.selectedPersonName }}
            </span>
            <span v-else style="color: #999">未选择</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row, $index }">
            <el-button type="text" @click="handleSelectPerson(row, $index)">
              选择人员
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleConfirm">确 定</el-button>
    </span>

    <!-- 人员选择弹窗 -->
    <PersonSelectDialog
      ref="personSelectDialog"
      @personSelected="handlePersonSelected"
    />
  </el-dialog>
</template>

<script>
  import { getMonDictonaryDeptList } from '@/api/risk/monitoring'
  import PersonSelectDialog from './PersonSelectDialog.vue'

  export default {
    name: 'DeptDistributeDialog',
    components: {
      PersonSelectDialog,
    },
    data() {
      return {
        dialogVisible: false,
        listLoading: false,
        deptList: [],
        currentDeptIndex: null, // 当前选择人员的部门索引
      }
    },
    computed: {},
    methods: {
      // 显示弹窗
      async show() {
        this.dialogVisible = true
        await this.fetchDeptList()
      },

      // 获取部门列表
      async fetchDeptList() {
        this.listLoading = true
        try {
          const res = await getMonDictonaryDeptList({})
          if (res.code === 1) {
            this.deptList = (res.data.data || []).map((dept) => ({
              ...dept,
              selectedPersonId: null,
              selectedPersonName: null,
            }))
          } else {
            this.$message.error('获取部门列表失败')
          }
        } catch (error) {
          console.error('获取部门列表失败:', error)
          this.$message.error('获取部门列表失败')
        } finally {
          this.listLoading = false
        }
      },

      // 选择人员
      handleSelectPerson(row, index) {
        this.currentDeptIndex = index
        this.$refs.personSelectDialog.showDialog(row)
      },

      // 人员选择回调
      handlePersonSelected(data) {
        if (this.currentDeptIndex !== null) {
          const person = data.person
          const dept = this.deptList[this.currentDeptIndex]

          // 使用Vue.set分别设置属性以确保响应式更新
          this.$set(dept, 'selectedPersonId', person.staffid)
          this.$set(dept, 'selectedPersonName', person.realname)

          // 重置当前部门索引
          this.currentDeptIndex = null
        }
      },

      // 确认
      handleConfirm() {
        // 检查是否所有部门都已选择人员
        const unselectedDepts = this.deptList.filter(
          (dept) => !dept.selectedPersonId
        )

        if (unselectedDepts.length > 0) {
          const deptNames = unselectedDepts
            .map((dept) => dept.deptName)
            .join('、')
          this.$message.warning(`请为以下部门选择人员：${deptNames}`)
          return
        }

        const selectedData = this.deptList.map((dept) => ({
          deptId: dept.deptId,
          deptName: dept.deptName,
          staffid: dept.selectedPersonId,
          realname: dept.selectedPersonName,
          versionId: dept.versionId,
        }))

        this.$emit('deptPersonSelected', selectedData)
        this.handleClose()
      },

      // 关闭弹窗
      handleClose() {
        this.dialogVisible = false
        this.currentDeptIndex = null
        this.deptList = []
      },
    },
  }
</script>

<style scoped>
  .dept-distribute-container {
    max-height: 400px;
    overflow-y: auto;
  }

  .dialog-footer {
    text-align: right;
  }
</style>
