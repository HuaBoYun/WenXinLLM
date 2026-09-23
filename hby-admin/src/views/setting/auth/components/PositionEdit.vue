<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="100px" :model="form" :rules="rules">
      <el-form-item label="岗位名称" prop="jobname">
        <el-input v-model.trim="form.jobname" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { jobSave } from '@/api/setting/auth'

  export default {
    name: 'PositionEdit',
    data() {
      return {
        form: {
          jobname: '',
        },
        rules: {
          jobname: [
            { required: true, trigger: 'blur', message: '请输入岗位名称' },
          ],
        },
        title: '',
        dialogFormVisible: false,
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
        data: [
          {
            id: 1,
            label: '系统管理',
            children: [
              {
                id: 2,
                label: '合同管理',
                children: [
                  {
                    label: '数字风控',
                    children: [
                      {
                        label: '数字风控',
                        children: [
                          {
                            label: '数字风控2号',
                          },
                        ],
                      },
                    ],
                  },
                  {
                    label: '长江7号',
                  },
                  {
                    label: '长江8号',
                  },
                  {
                    label: '长江9号',
                  },
                  {
                    label: '长江10号',
                  },
                ],
              },
              {
                label: '内部审计',
                children: [
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                ],
              },
              {
                label: '智能分析',
                children: [
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                  {
                    label: 'XXXXXX',
                  },
                ],
              },
            ],
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
            const { msg } = await jobSave(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
