<template>
  <div class="dialog-drag-container">
    <el-button type="primary" @click="dialogVisible = true">
      点击打开可拖拽弹窗
    </el-button>

    <el-dialog
      v-drag
      :before-close="handleClose"
      title="按住我拖拽"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <span>
        爱到尽头 覆水难收 爱悠悠 恨悠悠 为何要到无法挽留 才又想起你的温柔
        给我关怀为我解忧 为我凭添许多愁 在深夜无尽等候 独自泪流 独自忍受
        多想说声我真的爱你 多想说声对不起你 你哭着说情缘已尽 难再续 难再续
        就请你给我多一点点时间 再多一点点问候
      </span>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">
          确 定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'DialogDrag',
    data() {
      return { dialogVisible: false }
    },
    methods: {
      handleClose() {
        this.dialogVisible = false
      },
    },
  }
</script>
