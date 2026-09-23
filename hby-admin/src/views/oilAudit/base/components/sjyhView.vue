<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="600px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="24">
          <el-form-item label="数据库名称" prop=" dataBaseOwnership">
            <el-input
              v-model="formData.dataBaseOwnership"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数据库名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="数据库用户" prop="dataBaseUsers">
            <el-input
              v-model="formData.dataBaseUsers"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数据库用户"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="密码" prop="dataBasePassWord">
            <el-input
              v-model="formData.dataBasePassWord"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数据库密码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="确认密码" prop="dataBasePassWord1">
            <el-input
              v-model="formData.dataBasePassWord1"
              :style="{ width: '100%' }"
              clearable
              placeholder="请确认密码"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { editDataSource, getDataSourceDefaultInfo } from '@/oapi/setting/org'
  import { getPrivewAttInfo } from '@/oapi/contract/manage'

  export default {
    components: {},
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        formData: {
          createType: 2,
          dataBasePassWord: '',
          dataBasePassWord1: '',
          dataBaseUsers: '',
          id: '',
          dataBaseOwnership: '',
        },
        footer: true,
        rules: {
          // dataBaseUsers: [
          //   {
          //     required: true,
          //     message: '请输入数据库用户',
          //     trigger: 'blur',
          //   },
          // ],
          dataBaseUsers: [
            { required: true, message: '请输入数据库用户', trigger: 'change' },
            {
              validator: function (rule, value, callback) {
                if (/^[A-Z]+$/.test(value) == false) {
                  callback(new Error('请输入大写英文'))
                } else {
                  //校验通过
                  callback()
                }
              },
              trigger: 'change',
            },
          ],
          dataBasePassWord: [
            {
              required: true,
              message: '请输入密码',
              trigger: 'blur',
            },
          ],
          dataBasePassWord1: [
            {
              required: true,
              message: '请再次输入密码',
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
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getDataSourceDefaultInfo({ id: row.id })
          this.formData.dataBasePassWord1 = res.data.dataBasePassWord
          this.formData.dataBasePassWord = res.data.dataBasePassWord
          this.formData.dataBaseType = res.data.dataBaseType
          this.formData.dataBaseUsers = res.data.dataBaseUsers
          this.formData.dataBaseOwnership = res.data.dataBaseOwnership
          this.formData.id = res.data.id
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
        }
      },
      close() {
        ;(this.formData = {
          createType: 2,
          dataBasePassWord: '',
          dataBasePassWord1: '',
          dataBaseUsers: '',
          dataBaseOwnership: '',
          id: '',
        }),
          (this.dialogFormVisible = false)
        this.footer = true
      },
      async save() {
        if (this.formData.dataBasePassWord != this.formData.dataBasePassWord1) {
          this.$message({
            message: '两次密码不一致',
            type: 'error',
          })
          return
        }
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editDataSource(params)
            if (res && res.code == 200) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url = data.previewurl + '?url=' + encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })

        
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
