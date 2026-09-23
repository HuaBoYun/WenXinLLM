<template>
  <el-dialog
    title="资产追踪"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-timeline>
      <el-timeline-item
        v-for="(item, index) in traceData"
        :key="index"
        :timestamp="item.timestamp"
        :type="item.type"
      >
        <h4>{{ item.title }}</h4>
        <p>{{ item.description }}</p>
      </el-timeline-item>
    </el-timeline>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AssetTraceDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      traceData: [
        {
          timestamp: '2024-01-01',
          title: '资产登记',
          description: '资产首次登记入账',
          type: 'primary'
        },
        {
          timestamp: '2024-01-15',
          title: '资产转移',
          description: '资产转移至子公司A',
          type: 'success'
        }
      ]
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
