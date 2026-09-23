<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="700px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-tree
      ref="tree"
      :data="treeData"
      :props="defaultProps"
      :check-strictly="true"
      show-checkbox
      node-key="id"
      default-expand-all
      highlight-current
      @check="getname"
    ></el-tree>

    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { orgList } from '@/api/audit/implement'
  export default {
    name: 'OrgTree',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        dialogFormVisible: false,
        title: '选择部门',
        info: null,
        treeData: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
        },
      }
    },
    computed: {},
    async created() {
      await this.fetchTree()
    },
    mounted() {},
    methods: {
      // 选择会触发getname方法
      getname(data) {
        this.$refs.tree.setCheckedKeys([])
        this.$refs.tree.setCheckedKeys([data.id])
        this.info = data
      },

      handleCheckChange(data, checked, indeterminate) {
        if (checked) {
          // this.formData.orgid = data.id
          this.$refs['tree'].setCheckedKeys([data.id])
        }
      },
      async fetchTree() {
        const {
          data: { orgTree },
        } = await orgList()
        let treeData = JSON.parse(orgTree)
        this.treeData = treeData
      },
      showEdit() {
        this.dialogFormVisible = true
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },

      add() {
        if (!this.info) {
          this.$baseMessage('请选择部门！', 'error')
        } else {
          this.$emit('getOrgInfo', this.info)
          this.close()
        }
      },
    },
  }
</script>

<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
