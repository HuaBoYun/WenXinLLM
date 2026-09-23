<template>
  <el-dialog
    title="评价对象"
    :visible.sync="dialogVisible"
    width="500px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
    @close="close"
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
            :show-checkbox="multiple"
            :props="defaultProps"
            @check-change="handleCheckChange"
            @node-click="handleCheckChange"
          />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
  import { zgjkLeft } from '@/api/setting/org'
  export default {
    props: {
      multiple: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        dialogVisible: false,
        dataTree: [],
        ObjectInfo: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
        listLoading: false,
        list: [],
      }
    },
    methods: {
      showEdit() {
        this.dialogVisible = true
        this.current = undefined
        this.getExecutorTree()
      },
      handleCheckChange(data) {
        this.ObjectInfo = data
        this.current = data
      },

      save() {
        if (this.multiple) {
          const nodes = this.$refs.tree.getCheckedNodes()
          if (!nodes || !nodes.length) {
            this.$baseMessage('请选择评价对象', 'error', 'vab-hey-message-error')
            return
          }
          this.$emit('selectO', nodes)
        } else {
          if (!this.current) {
            this.$baseMessage('请选择评价对象', 'error', 'vab-hey-message-error')
            return
          }
          this.$emit('selectO', this.ObjectInfo)
        }
        this.dialogVisible = false
      },
      async getExecutorTree() {
        const res = await zgjkLeft(this.queryForm)
        this.dataTree = res
      },
      close() {
        this.dialogVisible = false
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
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 300px;
    // border-right: 1px solid ghostwhite;
    // margin-left: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
