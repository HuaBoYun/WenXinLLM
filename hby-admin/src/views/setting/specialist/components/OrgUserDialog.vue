<template>
  <el-dialog
    :before-close="close"
    size="800px"
    :title="title"
    :visible.sync="dialogFormVisible"
    :close-on-click-modal="false"
    append-to-body
  >
    <div class="system-log-container lr-layout">
      <div class="left">
        <el-tree
          :data="data"
          :default-expanded-keys="[1, 2, 3]"
          :expand-on-click-node="false"
          :highlight-current="true"
          node-key="id"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button @click="close">取消</el-button>
            <el-button native-type="submit" type="primary" @click="confirm()">
              确定
            </el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
        >
          <el-table-column
            align="center"
            label="用户真实姓名"
            prop="realname"
          />
          <el-table-column align="center" label="所属部门" prop="orgname" />
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
    <!--    <template #footer>-->
    <!--      <el-button @click="close">取消</el-button>-->
    <!--      <el-button native-type="submit" type="primary" @click="confirm()">-->
    <!--        确定-->
    <!--      </el-button>-->
    <!--    </template>-->
  </el-dialog>
</template>

<script>
  import { ztNewlist } from '@/api/setting/auth'
  import { zgjkLeft } from '@/api/setting/org'
  export default {
    name: 'AuthUser',
    components: {},
    data() {
      return {
        queryForm: {
          pid: undefined,
          pageNo: 1,
          pageSize: 20,
        },
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '用户信息',
        dialogFormVisible: false,
        list: [],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        orgid: '',
        data: [],
        currentRow: undefined,
      }
    },
    created() {
      this.organization()
    },
    methods: {
      show() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
        this.queryForm.pid = ''
        this.currentRow = undefined
      },
      async fetchData() {
        const {
          data: { tlist, totalRecord },
        } = await ztNewlist(this.queryForm)
        this.list = tlist
        this.total = totalRecord
      },
      confirm() {
        if (!this.currentRow) {
          this.$message.error('请选择')
          return
        }
        this.$emit('select', this.currentRow)
        this.close()
      },
      handleSelected(val) {
        // console.warn('currentRow', val)
        this.currentRow = val
      },
      handleNodeClick(data) {
        this.queryForm.pid = data.id
        this.fetchData()
      },
      //左侧列表
      async organization() {
        const tree = await zgjkLeft()
        console.warn('tree', tree)
        // this.queryForm.pid = orgid
        const func = (tree) => {
          const list = tree.map((i) => {
            return {
              id: i.id,
              label: i.name,
              children: func(i.children),
            }
          })
          return list
        }
        const _tree = func(tree, -1)
        //
        this.data = _tree
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: calc(100% - 200px);
  }
</style>
