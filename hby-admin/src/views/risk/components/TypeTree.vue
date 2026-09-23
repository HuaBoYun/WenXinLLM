<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-07-20 23:13:27
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-23 23:36:35
 * @FilePath: \hb-admin\src\views\risk\components\TypeTree.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <div v-if="editable" class="top-action">
      <el-button type="success" @click="$refs.typeAdd.showEdit()">
        新建
      </el-button>
      <el-button type="primary" @click="$refs.typeAdd.showEdit(1)">
        修改
      </el-button>
    </div>
    <el-tree
      :data="data"
      :expand-on-click-node="false"
      :default-expanded-keys="[topLevelId]"
      node-key="riskcatid"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
    <type-tree-add ref="typeAdd" />
  </div>
</template>

<script>
  import { getRiskEventsTree } from '@/api/risk/riskEvents'
  import TypeTreeAdd from './TypeTreeAdd.vue'
  export default {
    name: 'TypeTree',
    components: { TypeTreeAdd },
    props: {
      editable: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'riskcatname',
        },
        topLevelId: '',
        data: [],
      }
    },
    created() {
      this.getRiskEventsTreeData()
    },
    methods: {
      // 获取树状结构
      async getRiskEventsTreeData() {
        const res = await getRiskEventsTree()
        this.$emit('select', res.data.tree[0])
        if (res.code === 1) {
          this.data = res.data.tree
          this.topLevelId = res.data.tree[0].riskcatid
        }
      },
      handleNodeClick(data) {
        this.$emit('select', data)
      },
      getData(){
        return this.data
      },
    },
  }
</script>
<style scoped>
  .top-action {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
</style>
