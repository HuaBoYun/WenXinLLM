<template>
  <el-dialog
    title="选择人员"
    :visible.sync="dialogVisible"
    width="1200px"
    :append-to-body="true"
    :close-on-click-modal="false"
    v-if="dialogVisible"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <el-tree
            ref="tree"
            :check-strictly="true"
            :data="dataTree"
            default-expand-all
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
          />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button
                @click="
                  dialogVisible = false
                  list = []
                  dataTree = []
                "
              >
                取 消
              </el-button>
              <el-button
                type="primary"
                @click="save"
                :disabled="!selectedPerson"
              >
                确 定
              </el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-table
            ref="personTable"
            :data="list"
            :row-key="getRowKeys"
            @row-click="handleRowClick"
            highlight-current-row
          >
            <el-table-column
              label="用户真实名"
              prop="realname"
            ></el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
          </el-table>
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
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import { ZZUserList } from '@/api/internal/project'
  import { getOrgTree } from '@/api/risk/monitoring'

  export default {
    name: 'PersonSelectDialog',
    data() {
      return {
        dialogVisible: false,
        dataTree: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '选择人员',
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          z: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: false,
        list: [],
        selectedPerson: null, // 单选的人员
        currentDept: null, // 当前部门信息
      }
    },
    methods: {
      // 显示弹窗
      showDialog(dept) {
        this.currentDept = dept
        // 兼容不同的部门名称字段
        const deptName = dept.name || dept.deptName
        // 兼容不同的部门ID字段
        const deptId = dept.deptId || dept.id

        this.title = `选择人员 - ${deptName}`
        this.dialogVisible = true
        this.selectedPerson = null
        this.queryForm.z = deptId
        this.getOrgTree(dept)
        this.getPersonList()
      },

      // 树节点点击
      handleNodeClick(val) {
        this.queryForm.z = val.id
        this.getPersonList()
      },

      // 行点击选择（单选）
      handleRowClick(row) {
        this.selectedPerson = row
      },

      // 确认选择
      save() {
        if (!this.selectedPerson) {
          this.$message.error('请选择人员')
          return
        }

        this.$emit('personSelected', {
          dept: this.currentDept,
          person: this.selectedPerson,
        })
        this.list = []
        this.dataTree = []
        this.dialogVisible = false
      },

      // 分页
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getPersonList()
      },

      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getPersonList()
      },

      // 获取组织树
      async getOrgTree() {
        try {
          const res = await getOrgTree({ z: this.queryForm.z })
          this.dataTree = res.data.tree
        } catch (error) {
          console.error('获取组织树失败:', error)
          this.dataTree = []
        }
      },

      // 获取人员列表
      async getPersonList() {
        this.listLoading = true
        try {
          // 构建请求参数，nodeId和z使用相同的值
          const params = {
            ...this.queryForm,
            nodeId: this.queryForm.z,
          }
          const res = await ZZUserList(params)
          if (res && res.data && res.data.pageBean) {
            this.list = res.data.pageBean.list || []
            this.total = res.data.pageBean.total || 0
          } else {
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('获取人员列表失败:', error)
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
      },

      // 设置row-key
      getRowKeys(row) {
        return row.staffid
      },
    },
  }
</script>

<style scoped lang="scss">
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
