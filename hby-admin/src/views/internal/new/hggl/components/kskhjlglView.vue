<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="考试/考核名称" prop="teamName">
            <el-input
              v-model="formData.teamName"
              clearable
              placeholder="请输入考试/考核名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时间" prop="teamName">
            <el-date-picker
              v-model="value1"
              type="date"
              placeholder="选择时间"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="考试/考核部门" prop="workUnitName">
            <el-input
              v-model.trim="formData.workUnitName"
              placeholder="请选择考试/考核部门"
              style="width: 75%"
              disabled
            />
            <el-button :style="{ marginLeft: '10px' }" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="考试/考核结果" prop="teamName">
            <el-input
              v-model="formData.teamName"
              clearable
              placeholder="请输入考试/考核结果"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被考试/考核人" prop="workUnitName">
            <el-input
              v-model.trim="formData.workUnitName"
              placeholder="请选被考试/考核人"
              style="width: 75%"
              disabled
            />
            <el-button :style="{ marginLeft: '10px' }" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="记录人" prop="workUnitName">
            <el-input
              v-model.trim="formData.workUnitName"
              placeholder="请选记录人"
              style="width: 75%"
              disabled
            />
            <el-button :style="{ marginLeft: '10px' }" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审批人" prop="workUnitName">
            <el-input
              v-model.trim="formData.workUnitName"
              placeholder="请选审批人"
              style="width: 75%"
              disabled
            />
            <el-button :style="{ marginLeft: '10px' }" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  export default {
    name: '',
    components: {},

    data() {
      return {
        loading: false,
        formData: {
          teamName: '',
          isCollaboration: '',
          entrustSubject: '',
          serviceProject: '',
          professionalAbilityGrade: 0,
          reactionEfficiencyGrade: 0,
          appreciationServiceGrade: 0,
          total: 0,
          average: 0,
          remark: '',
        },
        footer: true,
        rules: {
          teamName: [
            {
              required: true,
              message: '请输入团队名称',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        tableData: [],
      }
    },
    computed: {},

    created() {},
    mounted() {},
    methods: {
      async showEdit() {
        this.dialogFormVisible = true
      },

      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.footer = true
      },
      handleSuccess(res, file) {},
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
