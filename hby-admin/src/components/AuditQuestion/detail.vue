<template>
  <el-row :gutter="14">
    <el-form ref="ruleForm" label-width="110px" :model="formData" size="mini">
      <el-col :span="24">
        <el-form-item label="定性编码" prop="qualitativeCode">
          <el-input
            v-model="formData.qualitativeCode"
            :style="{ width: '100%' }"
            readonly
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="定性名称" prop="qualitativeName">
          <el-input
            v-model="formData.qualitativeName"
            :style="{ width: '100%' }"
            readonly
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="是否填写金额" prop="amountFilledIn">
          <el-select v-model="formData.amountFilledIn" disabled>
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="是否实质性问题" prop="substantiveIssue">
          <el-select v-model="formData.substantiveIssue" disabled>
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="问题适用范围" prop="scopeProblem">
          <UEditor
            ref="ueditor"
            v-model="formData.scopeProblem"
            :height="100"
            style="margin-left: 0"
            disabled
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="法律法规名称" prop="regulatoryNames">
          <el-input
            v-model="formData.regulatoryNames"
            :style="{ width: '100%' }"
            readonly
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="文号" prop="regulatoryNumber">
          <el-input
            v-model="formData.regulatoryNumber"
            :style="{ width: '100%' }"
            readonly
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="引用的法规制度条款" prop="regulatoryProvisions">
          <UEditor
            ref="ueditor"
            v-model="formData.regulatoryProvisions"
            :height="100"
            style="margin-left: 0"
            disabled
          />
        </el-form-item>
      </el-col>
    </el-form>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
    </div>
  </el-row>
</template>

<script>
  import UEditor from '@/components/UEditor'
  import { getWTDXDetail } from '@/api/setting/sjzy'
  export default {
    name: 'Detail',
    props: ['formData'],
    components: { UEditor },
    data() {
      return {
        formData: {
          qualitativeCode: '',
          qualitativeName: '',
          amountFilledIn: '',
          substantiveIssue: '',
          scopeProblem: '',
          regulatoryNames: '',
          regulatoryNumber: '',
          regulatoryProvisions: '',
          id: '',
        },
        dialogFormVisible: false,
        title: '详情',
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      async init() {
        const res = await getWTDXDetail({ id: this.formData.id })
        this.formData = res.data
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
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
