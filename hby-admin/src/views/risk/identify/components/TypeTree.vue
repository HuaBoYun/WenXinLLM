<template>
  <div class="tree">
    <div v-if="editable" class="top-action">
      <el-button type="success" @click="handleCreate">新建</el-button>
      <el-button type="success" @click="handleEdit">修改</el-button>
      <el-button type="success" @click="handleDelete">删除</el-button>
      <el-button type="success" @click="handleRefish">刷新</el-button>
    </div>
    <el-tree
      :data="data"
      :highlight-current="true"
      :default-checked-keys="[topLevelId]"
      :default-expanded-keys="[topLevelId]"
      :expand-on-click-node="false"
      node-key="value"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
    <type-tree-add ref="typeAdd" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import TypeTreeAdd from './TypeTreeAdd.vue'
  import { getCreationTreeData, getCreationTypeDel } from '@/api/risk'
  import { formatOptions } from '@/utils/validate'
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
        topLevel: {},
        fatherid: 0,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        const result = await getCreationTreeData({
          // orgid: userInfo.linkDetp.orgid,
          // treeName: userInfo.linkDetp.orgname,
        })
        if (result.data.tree && result.data.tree.length > 0) {
          this.data = formatOptions(
            result.data.tree,
            'riskcatname',
            'riskcatid'
          )
          this.oldData = result.data.tree
          // console.log("this.data", this.topLevelId )
          this.topLevelId = this.topLevelId
            ? this.topLevelId
            : result.data.tree[0].riskcatid
          console.log('this.data', result.data.tree[0].riskcatid)
        } else {
          this.data = []
          this.oldData = []
        }

        if (this.data.length > 0) {
          // this.topLevelId = this.data[0].value
          this.$emit('fetch-data', this.topLevelId)
        } else {
          this.$emit('fetch-data', '')
        }
      },
      getData() {
        return this.data
      },
      handleNodeClick(data, data1) {
        this.topLevelId = data.value
        this.topLevel = data1
        this.$emit('fetch-data', data.value)
        this.$emit('all-data', this.oldData)
        this.$emit('nodeData', data)
      },
      handleCreate() {
        if (this.topLevelId != '' && this.checkId()) {
          this.$emit('fetchData', this.topLevelId)
          this.$baseMessage(
            '顶级不允许再添加',
            'error',
            'vab-hey-message-error'
          )
        } else if (
          this.topLevelId != '' &&
          !this.checkId() &&
          this.tableData.length > 0
        ) {
          this.$baseMessage(
            '该节点已有风险信息，无法新增下级风险类型',
            'error',
            'vab-hey-message-error'
          )
        } else if (this.topLevelId != '' && !this.checkId()) {
          console.log('添加', this.getFatherNode())
          this.fatherid = this.topLevelId

          this.$refs['typeAdd'].showEdit('add', this.getFatherNode())
        }
      },
      handleDelete() {
        if (this.topLevelId != '' && this.checkId()) {
          this.$emit('fetchData', this.topLevelId)
          this.$baseMessage('顶级不允许删除', 'error', 'vab-hey-message-error')
        } else if (
          this.topLevelId != '' &&
          !this.checkId() &&
          this.tableData.length > 0
        ) {
          this.$baseMessage(
            '该节点已有风险信息，无法删除',
            'error',
            'vab-hey-message-error'
          )
        } else if (this.topLevelId != '' && !this.checkId()) {
          this.findChildren(this.oldData, this.topLevelId)
          console.log('删除', this.found)
          // return
          this.$baseConfirm('你确定要删除当前项吗', null, async () => {
            const { msg, code } = await getCreationTypeDel({
              ...this.found,
            })
            if (code == 1) {
              this.$baseMessage(
                '删除成功',
                'success',
                'vab-hey-message-success'
              )
              this.topLevelId = this.found.fatherriskcatid
              await this.fetchData()
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
          })
        }
      },
      handleEdit() {
        if (this.topLevelId != '' && this.checkId()) {
          this.$emit('fetchData', this.topLevelId)
          this.$baseMessage(
            '顶级不允许再修改',
            'error',
            'vab-hey-message-error'
          )
        } else if (this.topLevelId != '' && !this.checkId()) {
          this.fatherid = this.getNode()[0].fatherriskcatid
          let data = {
            father: this.getFatherNode()[0],
            child: this.getNode()[0],
          }
          this.$refs['typeAdd'].showEdit('edit', data)
        }
      },
      handleRefish() {
        this.fetchData()
      },
      checkId() {
        return this.data[0].value == this.topLevelId
      },
      getNode(list, result = []) {
        let value = list ? list : this.oldData
        value.forEach((item) => {
          if (this.topLevelId == item.riskcatid) {
            result.push(item)
          }
          if (item.children && item.children.length > 0) {
            this.getNode(item.children, result)
          }
        })
        return result
      },
      getFatherNode(list, result = []) {
        let value = list ? list : this.oldData
        value.forEach((item) => {
          if (this.fatherid === item.riskcatid) {
            result.push(item)
            return
          }
          if (item.children && item.children.length > 0) {
            this.getFatherNode(item.children, result)
          }
        })
        return result
      },
      findChildren(arr, id) {
        arr.some((x) => {
          if (x.riskcatid === id) {
            const findObj = JSON.parse(JSON.stringify(x))
            delete findObj.children
            this.found = findObj
            return true
          } else {
            if (x.children && x.children.length) {
              this.findChildren(x.children, id)
            }
          }
        })
      },
    },
  }
</script>
<style scoped>
  .tree {
    width: 100%;
  }
  .top-action {
    display: flex;
    width: 100%;
    widows: 90%;
    margin-bottom: 20px;
    flex-wrap: wrap;
  }
</style>
