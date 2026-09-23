<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-08-21 20:47:18
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-21 21:59:28
 * @FilePath: \hb-admin\src\vab\components\VabTree\index.vue
 * @Description:  
-->
<template>
  <el-tree
    lazy
    :load="onLoad"
    :data="data"
    :props="defaultProps"
    highlight-current
    node-key="id"
  ></el-tree>
</template>

<script>
  import { getOrgTreeByDepartment } from '@/api/common'
  export default {
    name: 'VabTree',
    props: {},
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
      }
    },
    methods: {
      async onLoad(node, resolve) {
        if (node.level === 0) {
          // const res = await getOrgTreeByCompany();
          const res = await getOrgTreeByDepartment()

          this.data = res
          resolve(res)
        }
        if (node.level > 0) {
          const res = await getOrgTreeByDepartment({ nodeId: node.data.id })
          resolve(res)
        }
      },
    },
  }
</script>

<style lang="less" scoped></style>
