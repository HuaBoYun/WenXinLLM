<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <el-form
      ref="form"
      :class="{ disabled: disabled }"
      :disabled="disabled"
      label-width="140px"
      :model="form"
      :rules="rules"
    >
      <el-form-item label="公司编号" prop="orgnumber">
        <el-input v-model.trim="form.orgnumber" />
      </el-form-item>
      <el-form-item label="公司名称" prop="orgname">
        <el-input v-model.trim="form.orgname" />
      </el-form-item>
      <!-- <el-form-item label="社会统一信用代码" prop="unCreditCode">
        <el-input v-model.trim="form.unCreditCode" />
      </el-form-item>
      <el-form-item label="发文代字" prop="writtenByDept">
        <el-input v-model.trim="form.writtenByDept" />
      </el-form-item> -->
      <el-form-item label="公司介绍">
        <el-input v-model.trim="form.orgmeno" type="textarea" />
      </el-form-item>
      <el-form-item label="公司备注">
        <el-input v-model.trim="form.memo" type="textarea" />
      </el-form-item>
      <el-form-item label="是否开启望远镜功能">
        <el-radio-group v-model="form.iszy">
          <el-radio
            v-for="(item, index) in iszyoptions"
            :key="index"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否使用自动编号">
        <el-radio-group v-model="form.isautonumber">
          <!-- <el-radio label="1" value="1" />
          <el-radio label="0" value="0" /> -->
          <el-radio
            v-for="(item, index) in isautonumberoptions"
            :key="index"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否启用密级管理">
        <el-radio-group v-model="form.useSecrect">
          <el-radio
            v-for="(item, index) in mjOption"
            :key="index"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="!disabled" type="primary" @click="save">确 定</el-button>
    </template>
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import { orgSaveorg } from '@/api/setting/org'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'CompanyEdit',
    components: {
      ProcessList,
    },
    data() {
      return {
        disabled: false,
        form: {
          orgname: '',
          orgnumber: '',
          orgmeno: '',
          memo: '',
          writtenByDept: '',
          iszy: 0,
          isautonumber: 2,
          fatherorgid: '',
          orgtype: 2,
          auditType: 0,
          telescope: 2,
          unCreditCode: '',
          useSecrect: 0,
        },
        rules: {
          orgnumber: [
            { required: true, trigger: 'blur', message: '请输入编号' },
          ],
          orgname: [{ required: true, trigger: 'blur', message: '请输入名称' }],
          writtenByDept: [
            { required: true, trigger: 'blur', message: '请输入发文代字' },
          ],
          unCreditCode: [
            {
              required: true,
              trigger: 'blur',
              message: '请输入社会统一信用代码',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        isautonumberoptions: [
          {
            value: 1,
            label: '是',
          },
          {
            value: 2,
            label: '否',
          },
        ],
        iszyoptions: [
          {
            value: 1,
            label: '是',
          },
          {
            value: 0,
            label: '否',
          },
        ],
        mjOption: [
          {
            value: 1,
            label: '使用',
          },
          {
            value: 0,
            label: '不使用',
          },
        ],
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
      showEdit(pid, row, disabled) {
        this.disabled = disabled
        if (!row) {
          this.title = '添加'
          this.form.fatherorgid = pid
        } else {
          this.title = disabled ? '查看' : '编辑'
          this.form = Object.assign({}, row)
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
            const { msg, data } = await orgSaveorg(this.form)
            // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            // 流程校验
            if (this.requireValuedata && this.title != '添加') {
              //  查询当前是否有流程
              getFlowList({
                targetId: data.recordId,
                targetType: 'company',
                operationType: this.title == '添加' ? 1 : 2,
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
            } else if (this.title == '添加' && this.requireValuedata) {
              // 可以提交流程
              this.$refs['process'].save(220, data.recordId)
              this.$baseMessage(
                '审批流程提交成功,请等待审批',
                'success',
                'vab-hey-message-success'
              )
              this.close()
            } else {
              this.$emit('fetch-data')
              if (this.title == '添加') {
                this.$emit('fetch-tree')
              }
              this.close()
            }
          }
        })
      },
    },
  }
</script>
