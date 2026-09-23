<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-07-20 23:13:27
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-08-21 22:34:06
 * @FilePath: \hb-admin\src\views\contract\contractManage\components\options\sealDepartment.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    append-to-body
    v-loading="loading"
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogTreeVisible"
    width="500px"
    @close="close"
  >
    <!-- 搜索输入框和按钮一行显示 -->
    <div class="header-container">
      <div class="search-area">
        <el-input
          v-model="searchText"
          placeholder="请输入部门名称搜索"
          prefix-icon="el-icon-search"
          clearable
          @input="filterTree"
        ></el-input>
      </div>
      <div class="action-area">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </div>
    </div>

    <div class="tree-container">
      <el-tree
        ref="tree"
        :default-checked-keys="keys"
        :show-checkbox="multiple"
        :check-strictly="true"
        :data="filteredData"
        default-expand-all
        :expand-on-click-node="false"
        highlight-current
        node-key="id"
        :props="defaultProps"
        @check-change="handleCheckChange"
        @node-click="handleNodeClick"
        :filter-node-method="filterNode"
      />
    </div>
  </el-dialog>
</template>

<script>
  import { getOrgTreeByDepartment } from '@/api/common'
  export default {
    name: 'DepartmentOptions',
    props: {
      multiple: {
        type: Boolean,
        default: false,
      },
      title: {
        type: String,
        default: '部门',
      },
    },
    data() {
      return {
        loading: true,
        keys: [],
        dialogTreeVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        searchText: '',
        filteredData: [],
      }
    },
    methods: {
      show(keys = []) {
        if (this.multiple && keys) {
          this.keys = keys
        }
        this.dialogTreeVisible = true
        this.fetchTree()
      },
      handleNodeClick(data) {
        if (!this.multiple) {
          this.$refs.tree.setCurrentKey(data.id)
        }
      },
      async fetchTree() {
        this.loading = true
        try {
          const res = await getOrgTreeByDepartment()
          this.data = res
          this.filteredData = [...res]
        } finally {
          this.loading = false
        }
      },
      handleCheckChange(data) {},
      close() {
        this.dialogTreeVisible = false
        this.searchText = ''
      },
      confirm() {
        let selected
        if (!this.multiple) {
          selected = this.$refs.tree.getCurrentNode()
        } else {
          selected = this.$refs.tree.getCheckedNodes()
        }
        this.$emit('selected', selected)
        this.dialogTreeVisible = false
        this.searchText = ''
      },
      // 搜索过滤方法
      filterNode(value, data) {
        if (!value) return true
        return data.name.indexOf(value) !== -1
      },
      // 过滤树数据
      filterTree() {
        if (this.searchText) {
          this.$refs.tree.filter(this.searchText)
        } else {
          this.$refs.tree.filter('')
        }
      },
    },
  }
</script>
<style scoped>
  .header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    position: sticky;
    top: 0;
    background-color: #fff;
    z-index: 1;
    padding-bottom: 10px;
    border-bottom: 1px solid #e4e7ed;
  }

  .search-area {
    flex: 1;
    margin-right: 10px;
  }

  .action-area {
    white-space: nowrap;
  }

  .tree-container {
    max-height: 500px;
    overflow-y: auto;
  }
</style>
