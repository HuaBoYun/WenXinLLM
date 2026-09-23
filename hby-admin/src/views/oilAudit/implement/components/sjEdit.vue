<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="type === 'edit' ? '编辑' : '添加' + '审减内容'"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="postForm"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="工程量计算" prop="gcljs">
            <el-input
              v-model="postForm.gcljs"
              clearable
              placeholder="请填写工程量计算"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="定额套用" prop="dety">
            <el-input
              v-model="postForm.dety"
              clearable
              placeholder="请填写定额套用"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现场实测" prop="xcsc">
            <el-input
              v-model="postForm.xcsc"
              clearable
              placeholder="请填写现场实测"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="物资价格" prop="wzjg">
            <el-input
              v-model="postForm.wzjg"
              clearable
              placeholder="请填写物资价格"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他审减" prop="qtsj">
            <el-input
              v-model="postForm.qtsj"
              clearable
              placeholder="请填写其他审减"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审减额" prop="sje">
            <el-input
              v-model="postForm.sje"
              clearable
              placeholder="请填写审减额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计人员" prop="sjry">
            <el-input
              v-model="postForm.sjry"
              clearable
              placeholder="请填写审计人员"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" prop="bz">
            <el-input
              v-model="postForm.bz"
              clearable
              placeholder="请填写备注"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { getDetail, addOrUpdate } from '@/oapi/audit/task'

  export default {
    data() {
      return {
        postForm: {
          gcljs: '',
          dety: '',
          xcsc: '',
          wzjg: '',
          qtsj: '',
          sje: '',
          sjry: '',
          bz: '',
        },
        rules: {
          // gcljs: [
          //   {
          //     required: true,
          //     message: '请填写工程量计算',
          //     trigger: 'blur',
          //   },
          // ],
          // dety: [
          //   {
          //     required: true,
          //     message: '请填写定额套用',
          //     trigger: 'blur',
          //   },
          // ],
          // xcsc: [
          //   {
          //     required: true,
          //     message: '请填写现场实测',
          //     trigger: 'blur',
          //   },
          // ],
          // wzjg: [
          //   {
          //     required: true,
          //     message: '请填写物资价格',
          //     trigger: 'blur',
          //   },
          // ],
          // qtsj: [
          //   {
          //     required: true,
          //     message: '请填写其他审减',
          //     trigger: 'blur',
          //   },
          // ],
          // sje: [
          //   {
          //     required: true,
          //     message: '请填写审减额',
          //     trigger: 'blur',
          //   },
          // ],
          // sjry: [
          //   {
          //     required: true,
          //     message: '请填写审计人员',
          //     trigger: 'blur',
          //   },
          // ],
          // bz: [
          //   {
          //     required: true,
          //     message: '请填写备注',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '新增',
        type: 'edit',
        id: '',
        typeRow:null,
      }
    },
    methods: {
      async showModal(row,type) {
        this.typeRow = type 
        this.dialogFormVisible = true
        this.type = row ? 'edit' : 'add'
        if (row) {
          // const res = await getDetail({ sjnrid: row.sjnrid })
          // this.postForm = res.data
          this.postForm = row
          this.id = row.sjnrid
        }
      },
      close() {
        this.$refs.ruleForm.resetFields()
        this.postForm = {
          gcljs: '',
          dety: '',
          xcsc: '',
          wzjg: '',
          qtsj: '',
          sje: '',
          sjry: '',
          bz: '',
        }
        this.dialogFormVisible = false
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let param = Object.assign({}, this.postForm)
            if (this.type === 'edit') {
              param.sjnrid = this.id
            }
            addOrUpdate({...param, projectId:this.typeRow.projectId,
              templateId: this.typeRow.templateId})
              .then(() => {
                this.$baseMessage('保存成功', 'success')
                this.$emit('queryData')
                this.close()
              })
              .catch((res) => {
                this.$baseMessage(res.msg, 'error')
              })
              .finally(() => {
                this.loading = false
              })
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
