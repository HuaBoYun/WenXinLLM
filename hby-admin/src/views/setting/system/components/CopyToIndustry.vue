<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-05 14:39:28
 * @FilePath: /hb-admin/src/views/setting/system/components/CopyToIndustry.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <IndustryTree
      ref="industry-tree"
      :form-data="formData"
      :selectedid="selectedid"
      @selected="handleSelected"
    />
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { saveCopyToIndustry } from '@/api/setting/system'
  import IndustryTree from './IndustryTree.vue'

  export default {
    name: 'CopyToIndustry',
    components: { IndustryTree },
    data() {
      return {
        loadingOptions: false,
        loadingTree: false,
        title: '',
        dialogFormVisible: false,
        selectedid: undefined,
        // queryData: {
        //   selectedid: undefined,
        //   orgid: undefined,
        // },
        formData: {
          selectedId: undefined,
          faflowid: undefined,
          flownumber: undefined,
          orgid: undefined,
        },
        optionsData: [],
        treeData: [],
        defaultProps: {
          children: 'children',
          // label: 'text',
          // value: 'id',
        },
        currentNode: undefined,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        const { flowid, flownumber, fatherflowid } = row
        this.selectedid = flowid
        this.formData.selectedId = flowid
        this.formData.faflowid = fatherflowid
        this.formData.flownumber = flownumber
        this.title = '流程类型'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['industry-tree'].fetchTree()
        })
      },
      close() {
        this.dialogFormVisible = false
      },
      handleSelected(data) {
        this.currentNode = data
      },
      async save() {
        const { msg } = await saveCopyToIndustry(this.currentNode)
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        this.$emit('fetch-data')
        this.close()
      },
    },
  }
</script>
