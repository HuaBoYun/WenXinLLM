<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="1000"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      @close="close"
    >
      <el-row :gutter="24">
        <el-form
          ref="ruleForm"
          label-width="100px"
          :model="formData"
          :rules="rules"
          size="mini"
        >
          <el-col :span="12">
            <el-form-item label="问题线索编码" label-width="140px" prop="code">
              <el-input
                v-model="formData.code"
                clearable
                placeholder="请输入问题线索编码"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="涉及企业名称"
              label-width="140px"
              prop="businessName"
            >
              <el-input
                v-model="formData.businessName"
                clearable
                placeholder="请输入涉及企业名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="问题线索名称" label-width="140px" prop="name">
              <el-input
                v-model="formData.name"
                clearable
                placeholder="请输入问题线索名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="问题线索类别"
              label-width="140px"
              prop="category"
            >
              <el-select
                v-model="formData.category"
                placeholder="请选择"
                style="width: 100%"
              >
                <el-option label="类别一" value="类别一"></el-option>
                <el-option label="类别二" value="类别二"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="问题线索来源"
              label-width="140px"
              prop="original"
            >
              <el-select
                v-model="formData.original"
                placeholder="请选择"
                style="width: 100%"
              >
                <el-option label="来源一" value="来源一"></el-option>
                <el-option label="来源二" value="来源二"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="资产损失等级"
              label-width="140px"
              prop="lossLevel"
            >
              <el-select
                v-model="formData.lossLevel"
                placeholder="请选择"
                style="width: 100%"
              >
                <el-option label="状态一" value="状态一"></el-option>
                <el-option label="状态二" value="状态二"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="报告时间"
              label-width="140px"
              prop="reportDate"
            >
              <el-date-picker
                style="width: 100%"
                v-model="formData.reportDate"
                placeholder="选择报告时间"
                type="datetime"
                format="yyyy-MM-dd hh:mm:ss"
                value-format="yyyy-MM-dd hh:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" label-width="140px" prop="status">
              <el-select
                v-model="formData.status"
                placeholder="请选择"
                style="width: 100%"
              >
                <el-option label="等级一" value="等级一"></el-option>
                <el-option label="等级二" value="等级二"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button @click="add" type="primary">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    props: ['title'],
    data() {
      return {
        dialogVisible: true,
        formData: {},
        rules: {
          code: [
            {
              required: true,
              message: '请输入问题线索编码',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入问题线索编码',
              trigger: 'blur',
            },
          ],
          businessName: [
            {
              required: true,
              message: '请输入问题线索编码',
              trigger: 'blur',
            },
          ],
          category: [
            {
              required: true,
              message: '请选择问题线索类别',
              trigger: 'blur',
            },
          ],
          original: [
            {
              required: true,
              message: '请选择问题线索来源',
              trigger: 'blur',
            },
          ],
          lossLevel: [
            {
              required: true,
              message: '请选择资产损失等级',
              trigger: 'blur',
            },
          ],
          reportDate: [
            {
              required: true,
              message: '请选择注册公告日期',
              trigger: 'blur',
            },
          ],
          status: [
            {
              required: true,
              message: '请选择状态',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    methods: {
      close() {
        this.$emit('close')
      },
      add() {
        console.log('hello')
      },
    },
  }
</script>
