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
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-form-item label="标题" label-width="140px" prop="title">
            <el-input
              v-model="formData.title"
              clearable
              placeholder="请输入疑点描述"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属类型" label-width="140px" prop="flowType">
            <el-input
              v-model="formData.flowType"
              clearable
              placeholder="请输入所属类型"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属分类" label-width="140px" prop="ssfl">
            <el-select
              v-model="formData.ssfl"
              clearable
              placeholder="请选择所属分类"
              style="width: 100%"
            >
              <el-option key="1" label="制度审核" value="1"></el-option>
              <el-option key="2" label="经营事项审核" value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="当前人接收时间"
            label-width="140px"
            prop="receiveTime"
          >
            <el-input
              v-model="formData.receiveTime"
              clearable
              placeholder="请输入当前人接收时间"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { modifyFlowInfo } from '@/api/contract/manage'

  export default {
    name: 'OAModel',

    props: [],
    data() {
      return {
        formData: {},
        dialogFormVisible: false,
        title: '法律审核分类',
        rules: {
          ssfl: [
            {
              required: true,
              message: '请选择所属分类',
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
      showEdit(row) {
        this.formData = row
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const data = await modifyFlowInfo({
              ...this.formData,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
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
