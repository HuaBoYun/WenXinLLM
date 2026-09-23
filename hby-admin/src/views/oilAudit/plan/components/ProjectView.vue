<template>
  <div class="system-log-container">
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="项目名称" label-width="140px" prop="field102">
            <el-input
              v-model="formData.field102"
              clearable
              :disabled="true"
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计对象" label-width="140px" prop="field103">
            <el-input
              v-model="formData.field103"
              clearable
              :disabled="true"
              placeholder="请输入审计对象"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目经理" label-width="140px" prop="field104">
            <el-input
              v-model="formData.field104"
              clearable
              :disabled="true"
              placeholder="请输入项目经理"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计模式" label-width="140px" prop="field105">
            <el-input
              v-model="formData.field105"
              clearable
              :disabled="true"
              placeholder="请输入审计模式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="项目开始时间"
            label-width="140px"
            prop="field106"
          >
            <el-date-picker
              v-model="formData.field106"
              clearable
              :disabled="true"
              format="yyyy-MM-dd"
              placeholder="请选择项目开始时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="项目实际投入人员"
            label-width="140px"
            prop="field107"
          >
            <el-input
              v-model="formData.field107"
              clearable
              :disabled="true"
              placeholder="请输入项目实际投入人员"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="检查清单总数"
            label-width="140px"
            prop="field108"
          >
            <el-input
              v-model="formData.field108"
              clearable
              :disabled="true"
              placeholder="请输入检查清单总数"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="发现问题总数"
            label-width="140px"
            prop="field109"
          >
            <el-input
              v-model="formData.field109"
              clearable
              :disabled="true"
              placeholder="请输入发现问题总数"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: 'ProjectView',
    components: {},
    props: [],
    data() {
      return {
        formData: {
          field102: '专项审计',
          field103: '专项审计',
          field104: '专项审计',
          field105: '专项审计',
          field106: '2021-08-11',
          field107: '3',
          field108: '3',
          field109: '23',
        },
        rules: {
          field102: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'blur',
            },
          ],
          field103: [
            {
              required: true,
              message: '请输入审计对象',
              trigger: 'blur',
            },
          ],
          field104: [
            {
              required: true,
              message: '请输入项目经理',
              trigger: 'blur',
            },
          ],
          field105: [
            {
              required: true,
              message: '请输入审计模式',
              trigger: 'blur',
            },
          ],
          field106: [
            {
              required: true,
              message: '请选择项目开始时间',
              trigger: 'change',
            },
          ],
          field107: [
            {
              required: true,
              message: '请输入项目实际投入人员',
              trigger: 'blur',
            },
          ],
          field108: [
            {
              required: true,
              message: '请输入检查清单总数',
              trigger: 'blur',
            },
          ],
          field109: [
            {
              required: true,
              message: '请输入发现问题总数',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
    },
  }
</script>
<style></style>
