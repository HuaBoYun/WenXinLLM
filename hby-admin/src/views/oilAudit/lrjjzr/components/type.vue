<template>
  <div>
    <el-dialog
      title="项目类型"
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
      />
    </el-dialog>
  </div>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  import { findOrganization } from '@/api/setting/org'
  import {
    getNbsjTypeOfList, 
  } from '@/api/workbench/auditTools'
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
        defaultProps: {
          children: 'childrenList',
          label: 'auditType',
        },
        list: [],
        orgid: '',
        data: [],
        loading: false,
        expandedKeys: [userInfo.currentOrg.orgid],
        dialogVisible: false,
        multipleSelection: null,
        treeData:[],
        clickTimer: null,  // 计时器
        lastClickTime: 0,  // 上次点击时间
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleNodeClick(data) {
        console.log(data)
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
      fetchData(node, resolve) {
        this.getParamentList()
      },
      async getParamentList() {
        const { data } = await getNbsjTypeOfList({ parentId: 0 })
        this.treeData =  data.date
      },
      showEdit() {
        this.dialogVisible = true
        this.fetchData()
        // this.getExecutorTree()
      },
      save() {
        if (!this.multipleSelection) {
          this.$baseMessage('请选择单位！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('submit', this.multipleSelection)
        this.close()
      },
      close() {
        this.dialogVisible = false
        this.multipleSelection = {}
      },
    },
  }
</script>
