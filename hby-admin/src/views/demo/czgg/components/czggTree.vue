<template>
  <div v-loading="loading" style="background: #fff; padding: 10px">
    <el-input
      v-model="keyWord"
      @input="filterTree"
      placeholder="请搜索"
    ></el-input>
    <el-tree
      ref="tree"
      :data="data"
      :expand-on-click-node="false"
      :default-expand-all="true"
      node-key="name"
      :props="defaultProps"
      :filter-node-method="filterNode"
      @node-click="handleNodeClick"
    >
      <div class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <div class="icon-list">
          <el-tooltip
            v-for="(item, index) in iconList"
            :key="index"
            class="item"
            effect="dark"
            :content="item.content"
            placement="top"
          >
            <i
              :class="item.icon"
              @click="iconClick(item)"
              styly="margin-left: 4px"
            />
          </el-tooltip>
        </div>
      </div>
    </el-tree>
    <treeEdit ref="treeEdit"></treeEdit>
  </div>
</template>

<script>
  import { getLiftMenu, currSsProject } from '@/oapi/audit/projectData'
  import treeEdit from './treeEdit.vue'
  export default {
    name: 'czggTree',
    components: { treeEdit },
    //档案传入
    props: {
      projectid: {
        type: [Number, String],
        default: undefined,
      },
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'label',
          // isLeaf: (data, node) => {
          //   if (node === 0) {
          //     return false
          //   } else {
          //     return !data.isParent
          //   }
          // },
        },
        queryForm: {
          type: 'my',
          nodeId: undefined,
        },
        loading: false,
        data: [
          {
            label: '一级 1',
            children: [
              {
                label: '二级 1-1',
                children: [
                  {
                    label: '三级 1-1-1',
                  },
                ],
              },
            ],
          },
          {
            label: '一级 2',
            children: [
              {
                label: '二级 2-1',
                children: [
                  {
                    label: '三级 2-1-1',
                  },
                ],
              },
              {
                label: '二级 2-2',
                children: [
                  {
                    label: '三级 2-2-1',
                  },
                ],
              },
            ],
          },
          {
            label: '一级 3',
            children: [
              {
                label: '二级 3-1',
                children: [
                  {
                    label: '三级 3-1-1',
                  },
                ],
              },
              {
                label: '二级 3-2',
                children: [
                  {
                    label: '三级 3-2-1',
                  },
                ],
              },
            ],
          },
        ],
        iconList: [
          {
            icon: 'el-icon-edit',
            content: '编辑',
          },
          {
            icon: 'el-icon-circle-plus-outline',
            content: '新增',
          },
          {
            icon: 'el-icon-delete',
            content: '删除',
          },
        ],
        //最外层id
        fristId: 0,
        projectId: 0,
        keyWord: '',
      }
    },
    created() {
      // this.projectId = this.projectid;
      // if (this.$route.path == "/implement/look") {
      //   this.currSsProject();
      // } else {
      //   this.getLeftMueData(this.projectId);
      // }
    },
    methods: {
      async currSsProject() {
        const { data } = await currSsProject()
        this.projectId = data.pj.id
        this.getLeftMueData(data.pj.id)
      },
      //获取左侧菜单栏
      async getLeftMueData(id) {
        this.loading = true
        const data = await getLiftMenu({ projectid: id })
        this.loading = false
        this.data.push(data.data.tree)
      },
      handleNodeClick: function (data) {
        this.$emit('getChildParam', { data, projectId: this.projectId })
      },
      filterNode(value, data) {
        if (!value) return true
        return data.label.indexOf(value) !== -1
      },
      filterTree() {
        console.log(this.keyWord)
        this.$refs.tree.filter(this.keyWord)
      },
      iconClick(row) {
        if (row.content === '删除') {
          this.$confirm('确定要删除吗？', '删除节点', {
            type: 'warning',
          }).then(() => {
            // this.drawingList = []
            // this.idGlobal = 100
          })
        } else {
          this.$refs.treeEdit.showEdit(true)
        }
      },
    },
  }
</script>
<style scoped>
  .custom-tree-node {
    display: flex;
    justify-content: space-between;
  }
  .icon-list {
    display: flex;
  }
</style>
