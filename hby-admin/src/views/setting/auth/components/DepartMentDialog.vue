<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    :append-to-body="true"
    width="700px"
    @close="close"
  >
    <div>
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
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { zgjkLeft } from '@/api/setting/org'

  export default {
    name: 'DepartMentDialog',
    components: {},
    data() {
      return {
        title: '选择部门',
        disabled: false,
        dialogFormVisible: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        selectedItem: undefined,
      }
    },
    created() {},
    methods: {
      handleNodeClick(data) {
        console.log(data)
        this.selectedItem = data
      },
      show(data) {
        if (data) {
          this.data = data
        } else {
          this.organization()
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
        this.selectedItem = undefined
      },
      confirm() {
        //
        if (!this.selectedItem) {
          this.$message.error('请选择')
          return
        }
        this.$emit('select', this.selectedItem)
        this.close()
      },
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
        this.data = _tree
      },
    },
  }
</script>
