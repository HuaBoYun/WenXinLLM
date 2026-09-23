<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      class="form-edit"
      label-width="140px"
      :model="form"
      :rules="rules"
    >
      <el-form-item label="业务规则编码" prop="code">
        <el-input v-model.trim="form.code" />
      </el-form-item>
      <el-form-item label="规则名称" prop="name">
        <el-input v-model.trim="form.name" />
      </el-form-item>
      <el-form-item label="优先级">
        <el-radio-group v-model="form.check">
          <el-radio label="高" value="0" />
          <el-radio label="中" value="1" />
          <el-radio label="低" value="2" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="风险等级">
        <el-radio-group v-model="form.check">
          <el-radio label="高" value="0" />
          <el-radio label="中" value="1" />
          <el-radio label="低" value="2" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="规则状态">
        <el-radio-group v-model="form.check">
          <el-radio label="是" value="启用" />
          <el-radio label="否" value="禁用" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="规则所属部门">
        <el-cascader
          clearable
          :options="options"
          :props="{ multiple: true, checkStrictly: true }"
        />
      </el-form-item>
      <el-form-item label="数据连接字符串">
        <el-cascader
          clearable
          :options="options"
          :props="{ multiple: true, checkStrictly: true }"
        />
      </el-form-item>
      <el-form-item label="规则对应SQL">
        <el-input v-model.trim="form.remark" type="textarea" />
      </el-form-item>
      <el-form-item label="报表SQL" prop="name">
        <el-input v-model.trim="form.remark" type="textarea" />
      </el-form-item>
      <el-form-item label="规则描述">
        <el-input
          v-model.trim="form.remark"
          maxlength="300"
          show-word-limit
          type="textarea"
        />
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model.trim="form.remark"
          maxlength="300"
          show-word-limit
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'

  export default {
    name: 'RuleEdit',
    data() {
      return {
        form: {
          code: '',
          name: '',
          org: '长江集团有限公司',
          intro: '',
          remark: '',
          check: '0',
        },
        rules: {
          code: [{ required: true, trigger: 'blur', message: '请输入编号' }],
          name: [{ required: true, trigger: 'blur', message: '请输入名称' }],
        },
        title: '',
        dialogFormVisible: false,
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
        options: [
          {
            value: '1',
            label: '党委办公室',
          },
          {
            value: '2',
            label: '审计监察部',
          },
          {
            value: '3',
            label: '风险管理部',
          },
          {
            value: '4',
            label: '总会办公室',
          },
          {
            value: '5',
            label: '总经理办公室',
          },
          {
            value: '6',
            label: '人力资源部',
          },
          {
            value: '7',
            label: '合同管理部',
          },
          {
            value: '8',
            label: '安全管理部',
          },
          {
            value: '9',
            label: '设备运营部',
          },
          {
            value: '10',
            label: '工程管理部',
          },
          {
            value: '11',
            label: '综合管理部',
          },
          {
            value: '12',
            label: '信息运维管理部',
          },
        ],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
<style>
  .form-edit .el-cascader {
    width: 100%;
  }
</style>
