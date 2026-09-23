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
      </vab-query-form>
      <el-tree
        class="sidebar"
        :data="treeData"
        node-key="typeId"
        default-expand-all
        :props="defaultProps"
        @node-click="handleNodeClick"
        :expand-on-click-node="false"
      ></el-tree>
    </el-dialog>
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { findOrganization } from '@/api/setting/org'
  import { getNbsjTypeOfList } from '@/api/workbench/auditTools'
  export default {
    name: 'CompanyTree',
    props: {
      alwaysRoot: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      return {
        list: [],
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [userInfo.currentOrg.orgid],
        dialogVisible: false,
        multipleSelection: {},
        treeData: [
          {
            typeId: 1,
            auditType: '全部',
            childrenList: [],
          },
        ],
        clickTimer: null,  // 计时器
        lastClickTime: 0,  // 上次点击时间
      }
    },
    created() {
      this.fetchData()
    },
    computed: {
      defaultProps() {
        return {
          children: 'childrenList',
          label: 'auditType',
        }
      },
    },
    methods: {
      handleNodeClick(data) {
        console.log(data, 'data')
        this.multipleSelection = data

        const now = Date.now();
        // 判断是否为双击（300ms内）
        if (now - this.lastClickTime < 300) {
          clearTimeout(this.clickTimer);
          this.lastClickTime = 0;
          console.log('双击节点:', data);
          this.save();
        } else {
          this.lastClickTime = now;
          this.clickTimer = setTimeout(() => {
            // 处理单击事件（如果需要）
            this.lastClickTime = 0;
          }, 300);
        }
      },
      showEdit() {
        this.dialogVisible = true
        this.fetchData()
        // this.getExecutorTree()
      },
      save() {
        if (!this.multipleSelection.auditType) {
          this.$baseMessage(
            '请选择审计类型！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('submit', this.multipleSelection)
        this.close()
      },
      close() {
        this.dialogVisible = false
        this.multipleSelection = {}
      },
      async fetchData() {
        this.getParamentList()
        this.getChildList()
      },
      async getParamentList() {
        const { data } = await getNbsjTypeOfList({ parentId: 0 })
        this.treeData[0].childrenList = data.date
      },
      async getChildList(typeId = 1) {
        const { data } = await getNbsjTypeOfList({ parentId: typeId })
        this.tableData = data.tlist || []
        this.total = data.totalRecord || 0
      },
      //静默处理左侧树结构
      filterTreeData(array, targetTypeId) {
        for (let i = 0; i < array.length; i++) {
          const item = array[i]
          // 检查当前项的 typeId 是否匹配
          if (item.typeId === targetTypeId) {
            // 删除当前项及其子项
            array.splice(i, 1)
            return true // 如果删除成功，返回 true
          }
          // 如果当前项有子项并且是数组，递归检查子项
          if (Array.isArray(item.childrenList)) {
            const found = this.filterTreeData(item.childrenList, targetTypeId)
            if (found) {
              // 如果在子项中找到并删除了匹配的项，也需要检查并可能删除当前项
              if (item.typeId === targetTypeId) {
                array.splice(i, 1)
                return true
              }
            }
          }
        }
        return false // 如果没有找到匹配的项，返回 false
      },
    },
  }
</script>
