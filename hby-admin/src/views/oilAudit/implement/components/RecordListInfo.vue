<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form ref="ruleForm" label-width="100px" :model="formData" size="mini">
        <el-col :span="12">
          <el-form-item label="问题单元" prop="businessType">
            <el-input
              v-model="formData.businessType"
              clearable
              :disabled="true"
              placeholder="审计指引问题单元一"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题类型" prop="businessType">
            <el-input
              v-model="formData.businessType"
              clearable
              :disabled="true"
              placeholder="请输入问题类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计问题" prop="riskPoint">
            <el-input
              v-model="formData.riskPoint"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :disabled="true"
              placeholder="请输入审计问题"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="重点关注事项" prop="control">
            <el-input
              v-model="formData.control"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :disabled="true"
              placeholder="请输入重点关注事项"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计程序" prop="suditProcess">
            <el-input
              v-model="formData.suditProcess"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :disabled="true"
              placeholder="请输入审计程序"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="所需资料" prop="bioData">
            <el-input
              v-model="formData.bioData"
              :autosize="{ minRows: 4, maxRows: 4 }"
              :disabled="true"
              placeholder="请输入所需资料"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  components: {},
  inheritAttrs: false,
  props: [],
  data() {
    return {
      formData: {
        operateid: '指引模板业务单元一',
        businessType: '合规性',
        riskPoint: '指引模板业务单元一',
        control: '指引模板业务单元一',
        suditProcess: '指引模板业务单元一',
        bioData: '指引模板业务单元一',
      },
      dialogFormVisible: false,
      title: '详情',
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    async showEdit(row) {
      this.dialogFormVisible = true
      if (row) {
        this.formData = row.entermeeting
      }
    },
    close() {
      this.dialogFormVisible = false
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
