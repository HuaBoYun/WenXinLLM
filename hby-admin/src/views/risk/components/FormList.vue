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
  export default {
    name: 'FormList',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [
          {
            id: 1,
            label: '长江投资(中国)有限公司',
            children: [
              {
                id: 2,
                label: '系统演示公司',
                children: [
                  {
                    label: '星光',
                    children: [
                      {
                        label: '星光',
                        children: [
                          {
                            label: '星光2号',
                          },
                        ],
                      },
                    ],
                  },
                  {
                    label: '长江7号',
                  },
                  {
                    label: '长江8号',
                  },
                  {
                    label: '长江9号',
                  },
                  {
                    label: '长江10号',
                  },
                ],
              },
              {
                label: '长江集团有限公司',
                children: [
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                ],
              },
              {
                label: '长江省财政厅',
                children: [
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                ],
              },
            ],
          },
        ],
      }
    },
    created() {},
    methods: {
      handleNodeClick(data) {
        console.log(data)
      },
    },
  }
</script>
