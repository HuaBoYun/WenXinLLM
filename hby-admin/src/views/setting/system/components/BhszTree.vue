<!--
 * @Date: 2022-03-09 09:05:52
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-25 18:30:42
 * @FilePath: /hb-admin/src/views/setting/system/components/BhszTree.vue
-->
<template>
  <div>
    <el-tree
      :data="data"
      :default-checked-keys="[0]"
      default-expand-all
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { getSnSettingTree } from '@/api/setting/system'
  export default {
    name: 'FlowCategoryList',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [],
      }
    },
    created() {
      this.fetchTree()
    },
    methods: {
      async fetchTree() {
        const res = await getSnSettingTree()
        let children = res.children.map((item) => {
          return {
            value: item.id,
            label: item.name,
          }
        })
        this.data = [
          {
            value: res.id,
            label: res.name,
            children: children,
          },
        ]
      },
      handleNodeClick(data) {},
    },
  }
</script>
<style scoped>
  .top-action {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
</style>
