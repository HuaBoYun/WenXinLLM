<template>
  <div>
    <el-dialog
      title="单位"
      :visible.sync="dialogVisible"
      width="500px"
      :append-to-body="true"
      :close-on-click-modal="false"
      v-if="dialogVisible"
    >
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
        <vab-query-form-left-panel :span="24" style="display: flex">
          <el-input
            v-model="orgName"
            clearable
            placeholder="公司名称"
            @change="change()"
            style="margin-bottom: 10px; width: 50%; margin-right: 10px"
          />
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button
            type="primary"
            @click="resetSearch"
            style="margin-bottom: 10px"
          >
            重置
          </el-button>
        </vab-query-form-left-panel>
      </vab-query-form>
      <el-tree
        ref="tree"
        v-loading="loading"
        :data="data"
        :default-expanded-keys="expandedKeys"
        :default-expand-all="false"
        :expand-on-click-node="false"
        node-key="id"
        :props="defaultProps"
        :load="fetchData"
        :show-checkbox="isMultiple"
        :check-strictly="isMultiple"
        lazy
        @node-click="handleNodeClick"
        @check="handleCheckChange"
      />
    </el-dialog>
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  // import { findOrganization } from '@/api/setting/org'
  import { getAllOrgInfoTree } from '@/api/setting/org'

  export default {
    name: 'CompanyTree',
    props: {
      alwaysRoot: {
        type: Boolean,
        default: false,
      },
      multiple: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
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
        dialogVisible: false,
        multipleSelection: {},
        multipleSelections: [],
        orgName: '',
        lastClickTime: 0,
        clickTimeout: 500, // 双击阈值时间，单位为毫秒
      }
    },
    created() {
      this.orgid = JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgid
      this.fetchData()
    },
    computed: {
      isMultiple() {
        return this.multiple
      },
    },
    methods: {
      queryData() {
        if (this.orgName == '') {
          this.$refs.tree.$data.store.lazy = true // 开启懒加载
          this.fetchData()
        } else {
          this.data = []
          this.$refs.tree.$data.store.lazy = false
          this.fetchData() //加载数据
        }
        this.$forceUpdate()
      },
      resetSearch() {
        this.orgName = ''
        this.$refs.tree.$data.store.lazy = true // 开启懒加载
        this.fetchData()
        this.$forceUpdate()
      },
      handleNodeClick(data) {
        // 如果是多选模式，单击只选中节点，不触发提交
        if (this.isMultiple) {
          this.multipleSelection = data
          return
        }
        
        // 单选模式保持原有逻辑
        const currentTime = new Date().getTime()
        const timeDiff = currentTime - this.lastClickTime
        if (timeDiff < this.clickTimeout) {
          this.$emit('submit', data)
          this.close()
        } else {
          this.multipleSelection = data
        }
        this.lastClickTime = currentTime
      },
      
      handleCheckChange(data, checked) {
        // 多选模式下处理复选框变化
        if (checked.checkedNodes && checked.checkedNodes.length > 0) {
          this.multipleSelections = checked.checkedNodes
        } else {
          this.multipleSelections = []
        }
      },

      async fetchData(node, resolve) {
        if (node && node.level === 0) {
          return
        }
        if (node && node.level === 1) {
          resolve(this.data[0].children)
          return
        }

        this.loading = true

        getAllOrgInfoTree({
          fatherorgid: !node ? this.orgid : node.data && node.data.id,
          orgname: this.orgName,
        })
          .then((res) => {
            const tree = this.formatTree(res.data)
            console.log('tree:data', res.data)

            if (node && node.level > 0) {
              console.log('tree:', tree)
              resolve(tree[0].children)
              return
            }
            console.log('node', !node)
            if (!node) {
              this.$emit('select', {
                id: tree[0].id,
                label: tree[0].name,
              })
              this.expandedKeys.push(tree[0].id)
              console.log('tree:lll', tree)
              this.data = tree
            }

            if (this.orgName) {
              let that = this
              this.data = tree
              var Date2 = window.setTimeout(function () {
                console.log('Date2', Date2)
                that.setAllExpand()
              }, 1000)
            }
          })
          .finally(() => {
            this.loading = false
          })
        this.loading = false
      },
      setAllExpand() {
        for (var i = 0; i < this.$refs.tree.store._getAllNodes().length; i++) {
          this.$refs.tree.store._getAllNodes()[i].expanded = true
        }
      },
      showEdit() {
        this.dialogVisible = true
        this.fetchData()
        // this.getExecutorTree()
      },
      save() {
        if (this.isMultiple) {
          // 多选模式
          if (this.multipleSelections.length === 0) {
            this.$baseMessage('请选择单位！', 'error', 'vab-hey-message-error')
            return
          }
          this.$emit('submit', this.multipleSelections)
        } else {
          // 单选模式
          if (!this.multipleSelection.label) {
            this.$baseMessage('请选择单位！', 'error', 'vab-hey-message-error')
            return
          }
          this.$emit('submit', this.multipleSelection)
        }
        this.close()
      },
      close() {
        this.dialogVisible = false
        this.multipleSelection = {}
        this.multipleSelections = []
      },
      formatTree(tree) {
        const list = tree.map((i) => {
          return {
            id: i.id,
            label: i.name,
            isLeaf: !i.isParent,
            children: this.formatTree(i.children || []),
          }
        })
        return list
      },
    },
  }
</script>
