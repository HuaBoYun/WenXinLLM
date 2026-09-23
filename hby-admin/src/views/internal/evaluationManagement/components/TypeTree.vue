<template>
  <div>
    <el-tree
      v-if="data.length > 0"
      :data="data"
      :default-checked-keys="[1]"
      :default-expanded-keys="[1]"
      :expand-on-click-node="false"
      :highlight-current="true"
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    >
      <span slot-scope="{ node }" class="custom-tree-node">
        <span class="text-1">{{ node.label }}</span>
      </span>
    </el-tree>
  </div>
</template>

<script>
  let id = 1000
  export default {
    name: 'TypeTree',
    props: {
      data: {
        type: Array,
        default: () => [],
      },
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name',
        },
        // data: [
        //   {
        //     id: 1,
        //     label: '对权力运行的制约情况',
        //     children: [],
        //   },
        //   {
        //     id: 2,
        //     label: '内部控制建设启动情况',
        //     children: [],
        //   },
        //   {
        //     id: 4,
        //     label: '内部控制制度完备情况',
        //     children: [],
        //   },
        //   {
        //     id: 6,
        //     label: '评价与监督执行情况',
        //     children: [],
        //   },
        //   {
        //     id: 7,
        //     label: '预算业务管理控制情况',
        //     children: [],
        //   },
        //   {
        //     id: 8,
        //     label: '收支业务管理控制情况',
        //     children: [],
        //   },
        //   {
        //     id: 9,
        //     label: '政府采购业务管理控制情况',
        //     children: [],
        //   },
        //   {
        //     id: 10,
        //     label: '资产管理控制情况',
        //     children: [],
        //   },
        // ],
      }
    },
    created() {},
    watch: {},
    methods: {
      handleNodeClick: function (data) {
        console.log(data)
        this.$emit('getChildParam', data.id)
      },
      append(data) {
        const newChild = { id: id++, label: 'testtest', children: [] }
        if (!data.children) {
          this.$set(data, 'children', [])
        }
        data.children.push(newChild)
      },

      remove(node, data) {
        const parent = node.parent
        const children = parent.data.children || parent.data
        const index = children.findIndex((d) => d.id === data.id)
        children.splice(index, 1)
      },
    },
  }
</script>

<style scoped>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .text-1 {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
</style>
