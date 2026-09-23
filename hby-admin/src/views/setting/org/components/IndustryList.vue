<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-tree
        v-loading="loading"
        :data="data"
        :default-expanded-keys="[1, 2]"
        :expand-on-click-node="false"
        :highlight-current="true"
        node-key="id"
        :props="defaultProps"
        @node-click="handleNodeClick"
      />
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { addhyaccredit, organAccreditByHY } from '@/api/setting/org'
  export default {
    name: 'Industrylist',
    data() {
      return {
        title: '选择权限',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        orgid: '',
        checklist: {},
        data: [],
        loading: false,
      }
    },
    created() {},
    methods: {
      async fetchTree(orgids) {
        this.loading = true
        const { data } = await organAccreditByHY({ orgids: orgids })
        this.loading = false
        console.log(data)
      },
      handleNodeClick(data) {
        console.log(data)
        this.data = data
        this.defaultProps.label = 'orgname'
      },
      showEdit(orgid) {
        this.fetchTree(orgid)
        this.dialogFormVisible = true
        // this.data = data
        this.orgid = orgid
        this.defaultProps.label = 'name'
      },
      close() {
        this.dialogFormVisible = false
      },
      save() {
        this.$baseConfirm('你确定要选择当前项吗', null, async () => {
          const { msg } = await addhyaccredit({
            pri_id: this.checklist.id,
            orgids: this.orgid,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
          this.close()
        })
      },
    },
  }
</script>
