<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="建议层级类型" prop="code">
            <el-select
              v-model="value"
              placeholder="请选择建议层级类型"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建议涉及业务类型" prop="code">
            <el-select
              v-model="value"
              placeholder="请选择建议涉及业务类型"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="直接经济成果类型" prop="code">
            <el-select
              v-model="value"
              placeholder="请选择直接经济成果类型"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="直接经济成果金额（元）" prop="code">
            <el-input
              v-model="formData.code"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他经济成果类型" prop="code">
            <el-select
              v-model="value"
              placeholder="请选择其他经济成果类型"
              :style="{ width: '100%' }"
              disabled
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他经济成果金额（元）" prop="code">
            <el-input
              v-model="formData.code"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建议标题" prop="code">
            <el-input
              v-model="formData.code"
              :style="{ width: '100%' }"
              disabled
              placeholder="请输入建议标题"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联底稿" prop="code">
            <el-input
              v-model="formData.code"
              :style="{ width: '100%' }"
              disabled
              placeholder="请选择底稿"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="建议描述" prop="name">
            <el-input
              v-model="formData.name"
              :style="{ width: '100%' }"
              disabled
              placeholder="请输入建议描述"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="采纳描述" prop="name">
            <el-input
              v-model="formData.name"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入采纳描述"
              type="textarea"
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
  import { baseURL } from '@/config'
  import store from '@/store'

  import {
    editDataSource,
    getDataSourceDefaultInfo,
    LinkTest,
  } from '@/api/setting/org'

  const token = store.getters['user/token']

  export default {
    components: {},
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          timer: '2023-08-06 11:54:30',
          term: '',
          createType: 1,
          dataBaseConnectionAddress: '',
          dataBasePassWord: '',
          dataBaseOwnership: '',
          dataBaseType: 'Oracle',
          dataBaseUsers: '',
          id: '',
        },
        radio: '',
        footer: true,
        rules: {
          name: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          unit: [
            {
              required: true,
              message: '请输入被审计单位',
              trigger: 'blur',
            },
          ],
          term: [
            {
              required: true,
              message: '请选择审计任职期间',
              trigger: 'blur',
            },
          ],
          code: [
            {
              required: true,
              message: '请输入委托书编号',
              trigger: 'blur',
            },
          ],
          timer: [
            {
              required: true,
              message: '请输入委托书时间',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        options: [
          {
            value: '选项1',
            label: '黄金糕',
          },
          {
            value: '选项2',
            label: '双皮奶',
          },
          {
            value: '选项3',
            label: '蚵仔煎',
          },
          {
            value: '选项4',
            label: '龙须面',
          },
          {
            value: '选项5',
            label: '北京烤鸭',
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          // const res = await getDataSourceDefaultInfo({ id: row.id })
          // this.formData.createType = res.data.createType
          // this.formData.dataBaseConnectionAddress =
          //   res.data.dataBaseConnectionAddress
          // this.formData.dataBaseOwnership = res.data.dataBaseOwnership
          // this.formData.dataBasePassWord = res.data.dataBasePassWord
          // this.formData.dataBaseType = res.data.dataBaseType
          // this.formData.dataBaseUsers = res.data.dataBaseUsers
          // this.formData.id = res.data.id
          this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          createType: 1,
          dataBaseConnectionAddress: '',
          dataBasePassWord: '',
          dataBaseOwnership: '',
          dataBaseType: 'Oracle',
          dataBaseUsers: '',
          id: '',
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            return
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
