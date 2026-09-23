<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="left">
        <el-tree
          ref="tree"
          :check-strictly="true"
          :data="data"
          default-expand-all
          :expand-on-click-node="false"
          highlight-current
          node-key="riskcatid"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-left-panel :span="18">
            <el-form
              ref="form"
              checkable
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.risknumber"
                  clearable
                  placeholder="风险编号"
                  style="width: 140px; margin-right: 20px"
                />
                <el-form-item></el-form-item>
                <el-input
                  v-model="queryForm.riskname"
                  clearable
                  placeholder="风险名称"
                  style="width: 140px; margin-right: 20px"
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
              </el-form-item>
              <el-form-item>
                <el-button native-type="submit" @click="resetQueryForm">
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </vab-query-form-left-panel>
          <vab-query-form-right-panel :span="6">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
        >
          <el-table-column align="center" label="风险编号" prop="risknumber">
            <template #default="{ row }">
              <el-button type="text" @click="handleRisknumberDeatil(row)">
                {{ row.risknumber }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="风险名称"
            prop="riskname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="风险描述"
            prop="riskdes"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="riskcreatedt"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column align="center" label="状态" prop="status">
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '需调整'
                  : row.status == 3
                  ? '已撤销'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <RiskRead ref="read" :fromNK="true" />
  </el-dialog>
</template>
<script>
  // import { findOrganizationByTreeAllss, user2list } from '@/api/audit/implement'
  import { getCreationTreeData, getCreationTestList } from '@/api/risk'
  import RiskRead from '@/views/risk/identify/creation/components/RiskEdit.vue'
  import { formatDay } from '@/utils/index'

  export default {
    name: 'riskList',
    components: { RiskRead },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'riskcatname',
          value: 'riskcatid',
        },
        data: [],
        queryForm: {
          status: 6,
          risknumber: '',
          riskname: '',
          orgid: undefined,
          pageNo: 1,
          pageSize: 20,
        },
        current: undefined,
      }
    },
    created() {},
    methods: {
      async show() {
        this.current = undefined
        this.dialogFormVisible = true
        // 先获取树数据并设置默认值
        await this.getExecutorTree()
        // 再获取列表数据
        this.getExecutorList()
      },

      async getExecutorTree() {
        const res = await getCreationTreeData()
        this.data = res.data.tree
        // 确保有数据时才设置默认值
        if (res.data.tree && res.data.tree.length > 0) {
          this.queryForm.riskcatid = res.data.tree[0].riskcatid
        }
      },
      fetchData() {
        this.getExecutorList()
      },
      async resetQueryForm() {
        this.queryForm = {
          status: 6,
          risknumber: '',
          riskname: '',
          pageNo: 1,
          pageSize: 20,
        }
        await this.getExecutorTree()
        this.getExecutorList()
      },

      async getExecutorList() {
        this.listLoading = true
        const {
          data: {
            pageBean: { list, total },
          },
        } = await getCreationTestList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.riskcatid = val.riskcatid
        this.getExecutorList()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      handleRisknumberDeatil(row) {
        this.$refs['read'].showEdit(row, '', true)
      },
      confirm() {
        if (!this.current) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }
        if (this.current.status != 6) {
          this.$baseMessage(
            '请选择已完成的风险！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 20%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }
</style>
