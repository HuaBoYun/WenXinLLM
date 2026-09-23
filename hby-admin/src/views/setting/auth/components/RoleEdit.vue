<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="100px" :model="form" :rules="rules">
      <el-form-item label="角色名称" prop="rname">
        <el-input v-model.trim="form.rname" />
      </el-form-item>
      <el-form-item label="是否启用">
        <el-radio-group v-model="form.rstatus">
          <el-radio
            v-for="(item, index) in rstatusoptions"
            :key="index"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model.trim="form.rdesc" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ProcessList ref="process" />
  </el-dialog>
</template>

<script>
  import { roleSave } from '@/api/setting/auth'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'RoleEdit',
    components: { ProcessList },
    data() {
      return {
        form: {
          rid: undefined,
          rname: '',
          rstatus: 1,
          rdesc: '',
        },
        rules: {
          rname: [
            { required: true, trigger: 'blur', message: '请输入角色名称' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        rstatusoptions: [
          {
            value: 1,
            label: '是',
          },
          {
            value: 0,
            label: '否',
          },
        ],
        options: [],
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
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.rstatus = row.rstatus == 1 ? 1 : 0
          console.log('this.form', this.form)
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
            const { msg, data, confirm } = await roleSave(this.form)

            // 流程校验
            if (this.requireValuedata) {
              //  查询当前是否有流程
              getFlowList({
                targetId: confirm.recordId,
                targetType: 'role',
                operationType: this.title == '编辑' ? 2 : 1,
              }).then((res) => {
                if (res.data == 0) {
                  // 可以提交流程
                  this.$refs['process'].save(220, confirm.recordId)
                  this.$baseMessage(
                    '审批流程提交成功,请等待审批',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                  this.$emit('fetch-data')
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
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
    },
  }
</script>
