<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-29 21:21:59
 * @FilePath: /hb-admin/src/views/setting/system/components/LcdyView/FlowDiagram.vue
-->
<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <img :src="src" />
  </el-dialog>
</template>

<script>
  import { getViewInstanceDiagram } from '@/api/setting/system'
  export default {
    name: 'Diagram',
    data() {
      return {
        title: '',
        src: undefined,
        dialogVisible: false,
      }
    },
    created() {},
    methods: {
      async show(row) {
        const { processDefinitionId: definitionId, businessKey } = row
        const { data } = await getViewInstanceDiagram({
          definitionId,
          businessKey,
        })

        // this.src = `/process/picture?definitionId=${row.processDefinitionId}&businessKey=${row.businessKey}`
        this.dialogVisible = true
      },
      close() {
        this.dialogVisible = false
      },
    },
  }
</script>
