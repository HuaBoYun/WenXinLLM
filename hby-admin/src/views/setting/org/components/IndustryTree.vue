<template>
  <div>
    <el-tree
      v-loading="loading"
      :data="data"
      :default-expanded-keys="[1, 2, 3]"
      :expand-on-click-node="false"
      :highlight-current="true"
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { orghy_left } from '@/api/setting/org'

  export default {
    name: 'IndustryTree',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        list: [],
        data: [],
        loading: false,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleNodeClick(data) {
        console.log(data)
        this.$emit('select', data)
      },
      async fetchData() {
        this.loading = true
        orghy_left()
          .then((res) => {
            this.$emit('selectFirst', {
              id: res.orgid,
            })
            const tree = this.formatTree(res.tree, -1)
            this.data = tree
          })
          .finally(() => {
            this.loading = false
          })
      },
      formatTree(tree, pid) {
        const list = tree
          .filter((i) => i.fatherorgid == pid)
          .map((i) => {
            return {
              id: i.orgid,
              label: i.orgname,
              children: this.formatTree(tree, i.orgid),
            }
          })
        return list
      },
    },
  }
</script>
