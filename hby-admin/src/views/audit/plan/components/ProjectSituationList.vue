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
    name: 'ProjectSituationList',
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [
          {
            id: 1,
            label: '项目情况一览表',
            children: [
              {
                id: 2,
                label: '审计指引',
                children: [
                  {
                    id: 2.1,
                    label: '指引模板节点一',
                  },
                  {
                    id: 2.2,
                    label: '指引模板节点二',
                  },
                ],
              },
              {
                label: '项目方案',
              },
              {
                label: '审计通知书',
              },
              {
                label: '进场纪要',
              },
              {
                label: '任务查看',
                children: [
                  {
                    label: '审计模板演示节点1',
                  },
                  {
                    label: '审计模板演示节点2',
                  },
                ],
              },
              {
                label: '工作底稿',
              },
              {
                label: '审计发现',
              },
              {
                label: '事实确认书',
              },
              {
                label: '离场纪要',
              },
              {
                label: '审计报告',
              },
              {
                label: '审计建议书',
              },
            ],
          },
        ],
      }
    },
    created() {},
    methods: {
      handleNodeClick: function (data) {
        this.$emit('getChildParam', data.id)
      },
    },
  }
</script>
