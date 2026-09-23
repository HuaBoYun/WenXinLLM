<template>
  <div style="width: fit-content">
    <el-tree
      :data="data"
      :default-checked-keys="[1]"
      :default-expanded-keys="defaultExpandedKeys"
      :expand-on-click-node="false"
      node-key="id"
      :props="defaultProps"
      :default-expand-all="defaultExpandAll"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
  import { findOrganizationByTreeAllss } from '@/api/audit/project'
  import { getOrgTreeByDepartment } from '@/api/common.js'

  export default {
    name: 'ProjectDataTree',
    props: {
      defaultExpandedH: {
        type: Number,
        default: 3,
      },
      defaultExpandAll: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
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
        defaultExpandedKeys: [],
      }
    },
    mounted() {
      this.getDataprojectLeftFun()
    },
    methods: {
      handleNodeClick: function (data) {
        this.$emit('getChildParam', data.id)
        this.$emit('changeNode', data)
      },
      async getDataprojectLeftFun() {
        let res = await getOrgTreeByDepartment()
        this.data = res
        this.defaultExpandedKeys = this.showExpandedNode(
          res,
          this.defaultExpandedH
        )
        console.dir(this.data)
      },
      /**
       * 默认展开层级
       * @param {Array} treeData tree数据
       * @param {Number} h 需要展开的层级
       */
      showExpandedNode(treeData, h) {
        const _t = []
        if (h && typeof h === 'number' && treeData && treeData.length) {
          let tempH = 0
          const recurrence = (arr) => {
            arr.forEach((x) => {
              tempH++
              _t.push(x.id)
              if (tempH < h && x.children && x.children.length)
                recurrence(x.children)
            })
          }
          recurrence(treeData)
        }
        return _t || []
      },
    },
  }
</script>
