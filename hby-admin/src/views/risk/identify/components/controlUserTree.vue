<template>
  <div>
    <el-tree
      :data="data"
      :default-checked-keys="[topLevelId]"
      :default-expanded-keys="[topLevelId]"
      :expand-on-click-node="false"
      node-key="value"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
import TypeTreeAdd from './TypeTreeAdd.vue'
import { formatOptions } from '@/utils/validate'
import { zgjkLeft } from '@/api/setting/org'
export default {
  name: 'TypeTree',
  components: { TypeTreeAdd },
  props: {
    editable: {
      type: Boolean,
      default: false,
    },
    tableData: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      data: [],
      oldData: [],
      topLevelId: '',
    }
  },
  async created() {
    this.getBelongstoTextOptions()
  },
  methods: {
    async getBelongstoTextOptions() {
      const result = await zgjkLeft()
      this.data = formatOptions(result, 'name', 'id')
      if (this.data.length > 0) {
        this.topLevelId = this.data[0].value
        this.$emit('fetch-data', this.topLevelId)
      }
    },
    handleNodeClick(data) {
      this.topLevelId = data.value
      this.$emit('fetch-data', this.topLevelId)
    },
    handleCreate() {
      // if(this.topLevelId != '' && this.checkId()) {
      //   this.$emit('fetchData', this.topLevelId)
      //   this.$baseMessage('顶级不允许再添加', 'error', 'vab-hey-message-error')
      // } else if(this.topLevelId != '' && !this.checkId() && this.tableData.length > 0) {
      //   this.$baseMessage('该节点已有风险信息，无法新增下级风险类型', 'error', 'vab-hey-message-error')
      // } else if(this.topLevelId != '' && !this.checkId()){
      //   this.$refs.typeAdd.showEdit()
      // }
      this.$refs.typeAdd.showEdit('add', this.getNode())
    },
    handleEdit() {
      this.$refs.typeAdd.showEdit()
    },
    checkId() {
      return this.data[0].value == this.topLevelId
    },
    getNode(list, result = []) {
      let value = list ? list : this.oldData
      value.forEach((item) => {
        if (this.topLevelId === item.riskcatid) {
          result.push(item)
        }
        if (item.children && item.children.length > 0) {
          this.getNode(item.children, result)
        }
      })
      return result
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
