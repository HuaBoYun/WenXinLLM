<template>
  <div>
    <el-tree
      :data="data"
      :default-expanded-keys="[1, 2, 3]"
      :expand-on-click-node="false"
      :highlight-current="true"
      node-key="id"
      :props="defaultProps"
      :default-expand-all="true"
      @node-click="handleNodeClick"
    />
  </div>
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
        firstLevelId: undefined,
      }
    },
    created() {
      this.organization().then(() => {
        this.$emit('getFirstLevelId', this.firstLevelId)
      })
    },
    methods: {
      handleNodeClick(data) {
        this.$emit('select', data)
      },
      async organization() {
        const tree = await zgjkLeft()
        console.warn('tree', tree)
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
        this.$emit('select', _tree[0])
      },
    },
  }
</script>
