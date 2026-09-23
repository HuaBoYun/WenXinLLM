<template>
  <div>
    <el-tree
      :data="data"
      :default-checked-keys="[1]"
      :default-expanded-keys="[1, 2]"
      :expand-on-click-node="false"
      node-key="id"
      :default-expand-all="true"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { getFlawTreeData } from '@/api/risk/question'

  export default {
    name: 'ProjectDataTree',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        data: [
          {
            id: 1,
            name: '长江集团有限公司',
            children: [
              {
                id: 2,
                name: '风险管理部',
              },
              {
                name: '人力资源部',
              },
            ],
          },
        ],
      }
    },
    created() {
      this.getDataprojectLeftFun()
    },
    methods: {
      handleNodeClick: function (data) {
        // console.log(data)
        this.$emit('getChildParam', data)
      },
      async getDataprojectLeftFun() {
        let res = await getFlawTreeData()
        this.data = res
      },
    },
  }
</script>
