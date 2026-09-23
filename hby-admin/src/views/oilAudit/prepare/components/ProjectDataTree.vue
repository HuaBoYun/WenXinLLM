<template>
  <div>
    <el-tree
      :data="data"
      :default-checked-keys="[1]"
      :default-expanded-keys="[1, 2]"
      :expand-on-click-node="false"
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  // import { getDataprojectLeft } from '@/oapi/audit/preparation'

  export default {
    name: 'ProjectDataTree',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [
          {
            id: 1,
            label: '长江集团有限公司',
            children: [
              {
                id: 2,
                label: '风险管理部',
              },
              {
                label: '人力资源部',
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
        this.$emit('getChildParam', data.id)
      },
      async getDataprojectLeftFun() {
        // let res = await getDataprojectLeft()
      },
    },
  }
</script>
