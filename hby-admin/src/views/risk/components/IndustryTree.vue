<template>
  <div>

    <el-select v-model="value" placeholder="行业" @change="handleHY">
      <el-option v-for="item in data" :key="item.id" :label="item.name" :value="item.id">
      </el-option>
    </el-select>
    <el-tree :data="treeData" :default-expand-all="true" :expand-on-click-node="false" node-key="riskcatid"
      :props="defaultProps" @node-click="handleNodeClick">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <el-tooltip class="item" effect="dark" :content="node.label" placement="top-start">
          <span> {{ ellipsis(node.label,8) }} </span>
        </el-tooltip>
        <div>
        </div>
      </span>
    </el-tree>
  </div>
</template>

<script>
import { findOrganizationByTreeAll, leftanalysishyCopy } from '@/api/systemLog'
export default {
  name: 'HbAdminIndustryTree',

  data() {
    return {
      data: [],
      value: '',
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'name',
        value: 'id'
      },
    };
  },

  mounted() {
    this.getRiskEventsTreeData()
  },

  methods: {

    async getRiskEventsTreeData() {
      const res = await findOrganizationByTreeAll()
      this.data = res[0].children
      this.handleHY(this.data[0].id)
    },

    async getTree(id) {
      let orgid = id ? id : null
      const res = await leftanalysishyCopy({ moduletype: 'fxcj', orgid })
      this.treeData = res
      if (res && res.length > 0) {
        this.handleNodeClick({ id: res[0].id })
      } else {
        this.handleNodeClick({ id: null })
      }
    },
    handleHY(v) {
      this.value = v
      this.getTree(v)
    },
    handleNodeClick(data) {
      this.$emit('fetch-data', data.id)
    },
    ellipsis(value, len) {
      if (!value) return ''
      if (value.length > len) {
        return value.slice(0, len) + '...'
      }
      return value
    }
  },
};
</script>

<style lang="scss" scoped>

</style>