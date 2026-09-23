<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form class="margin-b0">
      <el-form
        ref="form"
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
        </el-form-item>
        <el-form-item>
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
          <el-button native-type="submit" type="primary" @click="resetSearch">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="list"
      ref="multipleTable"
      tooltip-effect="dark"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="风险编号"
        prop="risknumber"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="风险名称"
        prop="riskname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="风险类型"
        prop="riskcatname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="责任部门"
        prop="belongstoName"
        show-overflow-tooltip
      />
    </el-table>
    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </el-dialog>
</template>

<script>
  import { fxydListTZ } from '@/api/risk/index'
  import { getRiskLeft } from '@/api/risk/create'
  export default {
    name: 'fengxianModal',
    components: {},
    data() {
      return {
        dialogFormVisible: false,
        title: '关联风险',
        queryForm: {
          riskcatid: '', // 动态获取，不再硬编码
          risknumber: '',
          riskname: '',
          pgStatus: 'ypg',
          pageNo: 1,
          pageSize: 10,
        },
        list: [],
        current: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
      }
    },
    methods: {
      async showEdit() {
        this.dialogFormVisible = true
        // 先获取风险类型ID，再加载数据
        await this.getRiskCategoryId()
        this.fetchData()
      },
      // 获取风险类型的riskcatid
      async getRiskCategoryId() {
        try {
          const { data } = await getRiskLeft()
          // 接口返回结构: { code: 1, data: { tree: [...], orgid: "...", treeName: null } }
          if (data && data.tree && data.tree.length > 0) {
            // 递归查找"风险类型"节点
            const findRiskTypeNode = (nodes) => {
              for (const node of nodes) {
                if (node.riskcatname === '风险类型') {
                  return node.riskcatid
                }
                if (node.children && node.children.length > 0) {
                  const found = findRiskTypeNode(node.children)
                  if (found) return found
                }
              }
              return null
            }
            const riskcatid = findRiskTypeNode(data.tree)
            if (riskcatid) {
              this.queryForm.riskcatid = riskcatid
              console.log('成功获取 riskcatid:', riskcatid)
            } else {
              console.warn('未找到"风险类型"节点，使用默认值')
              this.queryForm.riskcatid = '1010491' // 降级使用默认值
            }
          } else {
            console.warn('接口返回数据为空，使用默认值')
            this.queryForm.riskcatid = '1010491'
          }
        } catch (error) {
          console.error('获取风险类型ID失败:', error)
          this.queryForm.riskcatid = '1010491' // 降级使用默认值
        }
      },
      async fetchData() {
        // 确保 riskcatid 已获取
        if (!this.queryForm.riskcatid) {
          console.warn('riskcatid 未获取，跳过查询')
          return
        }
        console.log('查询参数:', this.queryForm)
        try {
          const {
            data: {
              data: { list, total },
            },
          } = await fxydListTZ(this.queryForm)
          this.list = list || []
          this.total = total || 0
        } catch (error) {
          console.error('获取风险台账数据失败:', error)
          this.list = []
          this.total = 0
        }
      },
      resetSearch() {
        this.queryForm.risknumber = ''
        this.queryForm.riskname = ''
        this.queryForm.pageNo = 1
        // riskcatid 保持不变，不重置
        this.fetchData()
      },

      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },

      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.tableData = []
        this.footer = true
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
      },
      save() {
        if (this.current.length == 0) {
          this.$baseMessage('请选择风险！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selectList', this.current)
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
