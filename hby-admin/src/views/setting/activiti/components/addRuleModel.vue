<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="新增模块"
      :visible.sync="dialogFormVisible"
      width="400px"
      @close="close"
      v-if="dialogFormVisible"
    >
      <div
        class="right-wrapper"
        style="display: flex; justify-content: flex-end"
      >
        <el-button native-type="submit" type="primary" @click="saveBaseInfo">
          确定
        </el-button>
      </div>
      <div class="tree-wrapper">
        <el-tree
          ref="tree"
          :data="data"
          :default-checked-keys="checkedKeys"
          :default-expanded-keys="expandedKeys"
          :expand-on-click-node="false"
          node-key="id"
          :props="defaultProps"
          @node-click="handleNodeClick"
        />
      </div>
      <!-- <div>
        <el-input
          placeholder="模板编码"
          v-model="queryForm.sceneCode"
        ></el-input>
      </div> -->
    </el-dialog>
    <DetilEdit ref="add" />
  </div>
</template>

<script>
  import DetilEdit from './addDetail.vue'
  import { getAuthList, saveModelInfo } from '@/api/setting/auths'
  import { getModuleList } from '@/api/setting/system'
  export default {
    name: 'AddFormEdit',
    components: { DetilEdit },
    data() {
      return {
        checkedKeys: [],
        expandedKeys: [],
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [],
        title: '',
        dialogFormVisible: false,
        queryForm: {
          sceneCode: '',
          moduletype: '',
        },
        moduleLists: [],
        moduleType: '',
        parent: '',
      }
    },
    created() {},
    mounted() {},
    methods: {
      showEdit(row) {
        this.moduleType = row.moduleType
        this.parent = row.parentCatalogueId
        this.fetchData()
        this.dialogFormVisible = true
      },
      fetchData() {
        getAuthList({
          moduletype: this.moduleType,
          judge: 1,
        }).then((res) => {
          const data = res.data.rightList
          const arr = data.filter((res) => res.id === this.parent)
          this.data = this.formatTree(arr)

          // 默认展开全部的节点
          this.expandedKeys = this.data.map((item) => item.id)
        })
      },

      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await jobSave(this.form)
            this.$baseMessage('操作成功', 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleNodeClick(e) {
        this.treeInfo = e
      },
      formatTree(tree) {
        const list = tree.map((i) => {
          return {
            id: i.id,
            label: i.name,
            isParent: i.parent,
            moduletype: i.moduletype,
            perms: i.perms,
            path: i.path,
            children: this.formatTree(i.children || []),
          }
        })
        return list
      },
      addDetail() {
        this.$refs['add'].showEdit()
      },
      saveBaseInfo() {
        if (this.treeInfo.isParent === 0) {
          this.$baseMessage('请选择页面', 'warning', 'vab-hey-message-warning')
          return
        }
        console.log(this.treeInfo, 'treeInfo')
        saveModelInfo({
          moduleType: this.treeInfo.moduletype,
          parentCatalogueId: this.treeInfo.isParent,
          catalogueId: this.treeInfo.id,
          sceneCode: this.treeInfo.moduletype + '-' + this.treeInfo.path,
        }).then((res) => {
          if (res.code == 200) {
            this.dialogFormVisible = false
            this.$message.success('新增成功')
            this.$emit('fetch-data')
          } else {
            this.$baseMessage(res.msg, 'warning', 'vab-hey-message-warning')
          }
        })
      },
    },
  }
</script>

<style scoped lang="scss">
  .right-wrapper,
  .tree-wrapper {
    width: 95%;
    display: flex;
    //border: 1px red solid;
  }

  .right-wrapper {
    justify-content: space-between;
    align-items: center;
    padding-bottom: 10px;
    margin-bottom: 10px;
    width: 100%;

    .title {
      height: 100%;
      display: flex;
    }
  }

  ::v-deep .el-tree {
    width: 100%;
  }
</style>
