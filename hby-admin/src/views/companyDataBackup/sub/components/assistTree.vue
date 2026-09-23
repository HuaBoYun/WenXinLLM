<template>
  <div>
    <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" />
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { getAuxiliaryInfoTree } from '@/api/cwsc'
  export default {
    name: 'AssistTree',
    // props: {
    //   alwaysRoot: {
    //     type: Boolean,
    //     default: false,
    //   },
    // },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
          isLeaf: 'isLeaf',
        },
        list: [],
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [],
        nodeid: '',
      }
    },
    computed: {},
    created() {
      this.fetchData()
    },
    methods: {
      handleNodeClick(data) {
        //
        // this.nodeid = data.id
        this.$emit('select', data)
      },

      fetchData(node, resolve, orgId) {
        getAuxiliaryInfoTree().then((res) => {
          if (res.data) {
            const info = res.data.map((i) => {
              return {
                label: i.name,
                value: i.pkAccassitem,
                children: [
                  { label: '辅助信息表', value: i.pkAccassitem },
                  {
                    label: '辅助余额表',
                    value: i.pkAccassitem,
                  },
                  {
                    label: '辅助总表',
                    value: i.pkAccassitem,
                  },
                ],
              }
            })
            this.data = info
          }
        })
      },
    },
  }
</script>
