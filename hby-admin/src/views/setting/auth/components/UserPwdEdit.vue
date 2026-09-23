<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="700px"
      @close="close"
    >
      <el-form
        ref="form"
        :class="{ disabled: disabled }"
        :disabled="disabled"
        label-width="100px"
        :model="form"
        :rules="rules"
      >
        <el-form-item label="用户名" prop="username">
          {{ form.username }}
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          {{ form.realname }}
        </el-form-item>
        <el-form-item v-if="!disabled" label="密码" prop="password">
          <el-input
            v-model.trim="form.password"
            class="input-psword"
            show-password
          />
        </el-form-item>
        <el-form-item v-if="!disabled" label="密码确认" prop="password1">
          <el-input
            v-model.trim="form.password1"
            class="input-psword"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button v-if="!disabled" type="primary" @click="save">
          确 定
        </el-button>
      </template>
    </el-dialog>
    <ProcessList ref="process" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { userReset, getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'UserPwdEdit',
    components: {
      ProcessList,
    },
    data() {
      return {
        disabled: false,
        form: {
          staffid: undefined,
          username: '',
          realname: '',
          password: '',
          password1: '',
        },
        rules: {
          password: [
            { required: true, trigger: 'blur', message: '请输入新密码' },
          ],
          password1: [
            { required: true, trigger: 'blur', message: '请确认新密码' },
          ],
        },
        list: [],
        jobs: [],
        roles: [],
        title: '',
        dialogFormVisible: false,
        options: [
          {
            value: 1,
            label: '否',
          },
          {
            value: 0,
            label: '是',
          },
        ],
        data: [],
        requireValuedata: false, // 是否需要流程校验
      }
    },
    created() {
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      showEdit(row) {
        this.title = '修改密码'
        Object.keys(this.form).forEach((key) => (this.form[key] = row[key]))
        this.form.password = ''
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
            const { msg, data } = await userReset({
              id: this.form.staffid,
              password: this.form.password,
              password1: this.form.password1,
            })
            // 流程校验
            if (this.requireValuedata) {
              //  查询当前是否有流程
              getFlowList({
                targetId: data.recordId,
                targetType: 'user',
                operationType: 6,
              }).then((res) => {
                if (res.data == 0) {
                  // 可以提交流程
                  this.$refs['process'].save(220, data.recordId)
                  this.$baseMessage(
                    '审批流程提交成功,请等待审批',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.close()
                } else {
                  // 不可以提交流程
                  this.$baseMessage(
                    '当前用户流程已存在,请先走审批流程',
                    'error',
                    'vab-hey-message-error'
                  )
                  return
                }
              })
            } else {
              this.$baseMessage(
                msg || '修改成功！',
                'success',
                'vab-hey-message-success'
              )
              this.close()
            }
          }
        })
      },
    },
  }
</script>
<style scoped>
  .input-psword {
    -webkit-text-security: disc;
  }
</style>
