<template>
  <el-dialog
    title="质量检查详情"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="企业名称">
        {{ qualityData.enterpriseName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="检查类型">
        <el-tag>{{ qualityData.checkType || '-' }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="检查项">
        {{ qualityData.checkItem || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="检查结果">
        <el-tag :type="qualityData.checkResult === '通过' ? 'success' : 'danger'">
          {{ qualityData.checkResult || '-' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="数据来源">
        {{ qualityData.dataSource || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="检查时间">
        {{ qualityData.checkTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ qualityData.createTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="更新时间">
        {{ qualityData.updateTime || '-' }}
      </el-descriptions-item>
    </el-descriptions>

    <div v-if="qualityData.errorDesc" style="margin-top: 20px;">
      <el-divider content-position="left">问题描述</el-divider>
      <el-alert
        :title="qualityData.errorDesc"
        type="error"
        :closable="false"
        show-icon
      ></el-alert>
    </div>

    <div v-if="qualityData.checkResult === '通过'" style="margin-top: 20px;">
      <el-divider content-position="left">检查结论</el-divider>
      <el-alert
        title="数据质量检查通过，未发现问题"
        type="success"
        :closable="false"
        show-icon
      ></el-alert>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'QualityDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    qualityData: {
      type: Object,
      default: () => ({})
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
