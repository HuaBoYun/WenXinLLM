<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
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
        <el-col :span="12">
          <el-form-item label="标题" prop="fname">
            <el-input
              v-model="formData.fname"
              :style="{ width: '100%' }"
              disabled
              placeholder="请输入标题"
            />
          </el-form-item>
          <el-col :span="24">
            <el-form-item label="sql内容" prop="fsql">
              <el-input
                v-model="formData.fsql"
                :style="{ width: '100%' }"
                clearable
                type="textarea"
                placeholder="请输入sql内容"
                :rows="8"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="特定条件" prop="specificityCol">
              <el-input
                v-model="formData.specificityCol"
                :style="{ width: '100%' }"
                clearable
                type="textarea"
                placeholder="请输入特定条件"
                :rows="10"
              />
            </el-form-item>
          </el-col>
        </el-col>
        <el-col :span="12">
          <el-form-item label="标题列" prop="finitcol">
            <div
              style="
                width: 100%;
                height: 200px;
                overflow: auto;
                border: 1px solid #dcdfe6;
              "
            >
              <table style="width: 100%">
                <tbody>
                  <tr v-for="(item, index) in parsedFields" :key="index">
                    <td>{{ item.field }}</td>
                    <td>{{ item.description }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </el-form-item>
          <el-col :span="24">
            <el-form-item label="增量配置" prop="incrementcol">
              <el-input
                v-model="formData.incrementcol"
                :style="{ width: '100%' }"
                clearable
                type="textarea"
                placeholder="请输入增量配置"
                :rows="10"
              />
            </el-form-item>
          </el-col>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="check" type="success">测试</el-button>
      <el-button @click="execute" type="success">执行</el-button>
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <result-modal ref="resultModal" />
  </el-dialog>
</template>

<script>
  import {
    saveCjsql,
    getCjsqlDetail,
    getCjsqlAddDetail,
    testCjsql,
    executeCjsql,
  } from '@/api/cwsc'
  import ResultModal from './resultModal.vue'
  export default {
    components: {
      ResultModal,
    },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        tableData: [],
        formData: {
          fid: '',
          fname: '',
          finitsqlid: '',
          fplanid: '',
          fsql: '',
          finitcol: '',
          incrementcol: '',
          specificityCol: '',
        },
        footer: true,
        rules: {
          fsql: [
            {
              required: true,
              message: '请输入sql内容',
              trigger: 'blur',
            },
          ],
          dataBaseConnectionAddress: [
            {
              required: true,
              message: '请输入数据库连接',
              trigger: 'blur',
            },
          ],
          dataBaseUsers: [
            {
              required: true,
              message: '请输入数据库用户',
              trigger: 'blur',
            },
          ],
          dataBasePassWord: [
            {
              required: true,
              message: '请输入数据库密码',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        // rawString: `PK_ORG ,--主键\n CODE,--编码\n NAME,\t\t\t    --名称\n INNERCODE,\t    --内部编码\n SHORTNAME,    --简称\n MNECODE,\t\t--助记码\n PK_FATHERORG,\t--上级业务单元 \n ENABLESTATE\t--启用状态 1=未启用;2=已启用;3=已停用;`,
        parsedFields: [],
        rawString: '',
      }
    },
    computed: {},

    methods: {
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row, title) {
        this.dialogFormVisible = true

        if (row) {
          //相当于新增
          if (!row.fid && row.sqlconfigid) {
            const res = await getCjsqlAddDetail({ fid: row.sqlconfigid })
            Object.keys(this.formData).forEach((key) => {
              this.formData[key] = res.data[key]
            })
            this.formData.fsql = res.data.finitsql
            this.formData.finitsqlid = res.data.fid
            this.formData.specificityCol = res.data.finSpecificityCol
            this.rawString = res.data.finitcol
            this.parseFields()
            this.formData.fid = ''
            // this.formData.incrementcol = res.data.finitcol
            // 从url里取参数
            const { query } = this.$route
            this.formData.fplanid = query.fid
          }
          if (row.fid) {
            const res = await getCjsqlDetail({ fid: row.fid })
            Object.keys(this.formData).forEach((key) => {
              this.formData[key] = res.data.ps[key]
            })

            this.rawString = res.data.bsc.finitcol
            this.parseFields()
          }
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
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.formData = {
          fid: '',
          fname: '',
          finitsqlid: '',
          fplanid: '',
          fsql: '',
          finitcol: '',
          incrementcol: '',
          specificityCol: '',
        }
        this.fanid = ''
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await saveCjsql(params)
            if (res && res.code == 1) {
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
      parseFields() {
        this.parsedFields = this.rawString.split('\n').map((line) => {
          const [field, description] = line.split(',--')
          return {
            field: field.trim(),
            description: description ? description.trim() : '',
          }
        })
      },
      async check() {
        const res = await testCjsql(this.formData)
        if (res.msg == '成功') {
          this.$baseMessage('测试成功', 'success', 'vab-hey-message-success')
        }
      },
      async execute() {
        const res = await executeCjsql(this.formData)
        if (res.msg == '成功') {
          this.$baseMessage('执行成功', 'success', 'vab-hey-message-success')
          this.$refs['resultModal'].show(res.data)
        } else {
          this.$baseMessage('执行失败', 'error', 'vab-hey-message-error')
        }
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
