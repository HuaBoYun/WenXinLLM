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
    >
      <span slot-scope="{ node }" class="custom-tree-node">
        <span>{{ node.label }}</span>
        <!-- <span>
          <el-button size="mini" type="text" @click="() => append(data)">
            Append
          </el-button>
          <el-button size="mini" type="text" @click="() => remove(node, data)">
            Delete
          </el-button>
        </span> -->
      </span>
    </el-tree>
  </div>
</template>

<script>
  import { getLeftTreeZy } from '@/api/audit/preparation'

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
            label: '离任审计',
            children: [
              {
                id: 2,
                label: '财务管理',
              },
              {
                id: 3,
                label: '风险管控',
              },
            ],
          },
        ],
      }
    },
    created() {
      this.getLeftTreeZyFun()
    },
    methods: {
      handleNodeClick: function (data) {
        this.$emit('getChildParam', data.id)
      },
      async getLeftTreeZyFun() {
        let res = await getLeftTreeZy()
      },
    },
  }
</script>
