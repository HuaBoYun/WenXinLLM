<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
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
          <el-form-item
            label="企业规章制度审核件数"
            label-width="140px"
            prop="firmRegulationsAuditNumber"
          >
            <el-input
              v-model="formData.firmRegulationsAuditNumber"
              clearable
              placeholder="请输入企业规章制度审核件数"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="规章制度审核率(%)"
            label-width="140px"
            prop="regulationsAuditRatio"
          >
            <el-input
              v-model="formData.regulationsAuditRatio"
              clearable
              placeholder="请输入规章制度审核率(%)"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="企业合同审核件数"
            label-width="140px"
            prop="firmEconomicsContractNumber"
          >
            <el-input
              v-model="formData.firmEconomicsContractNumber"
              clearable
              placeholder="请输入企业合同审核件数"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="合同审核率(%)"
            label-width="140px"
            prop="economicsContractRatio"
          >
            <el-input
              v-model="formData.economicsContractRatio"
              clearable
              placeholder="请输入合同审核率(%)"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="企业重要决策审核件数"
            label-width="140px"
            prop="firmMajorDecisionNumber"
          >
            <el-input
              v-model="formData.firmMajorDecisionNumber"
              clearable
              placeholder="请输入企业重要决策审核件数"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="重要决策审核率(%)"
            label-width="140px"
            prop="majorDecisionRatio"
          >
            <el-input
              v-model="formData.majorDecisionRatio"
              clearable
              placeholder="请输入重要决策审核率(%)"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" label-width="140px" prop="remark">
            <el-input
              v-model="formData.remark"
              clearable
              type="textarea"
              rows="2"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { addFLSH } from '@/api/fwgl/zzxx'
  export default {
    name: 'SummanyInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          economicsContractRatio: '',
          firmEconomicsContractNumber: '',
          firmMajorDecisionNumber: '',
          firmRegulationsAuditNumber: '',
          majorDecisionRatio: '',
          regulationsAuditRatio: '',
          remark: '',
        },
        footer: true,
        rules: {
          entercoed: [
            {
              required: true,
              message: '请输入进场纪要编号',
              trigger: 'blur',
            },
          ],
          entername: [
            {
              required: true,
              message: '请输入进场纪要名称',
              trigger: 'blur',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入编辑器',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = Object.assign({}, row)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false

        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        addFLSH(this.formData).then((res) => {
          if (res.msg == '成功') {
            this.dialogFormVisible = false
            this.$emit('addList', res.data)
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
