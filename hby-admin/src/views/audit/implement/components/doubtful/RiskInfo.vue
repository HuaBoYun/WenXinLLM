<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="风险编号" prop="risknumber">
            <el-input
              v-model="formData.risknumber"
              clearable
              placeholder="请输入风险编号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险名称" prop="riskname">
            <el-input
              v-model="formData.riskname"
              clearable
              placeholder="请输入风险名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现日期" prop="discovereddate">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.discovereddate"
              placeholder="请输入发现日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发生日期" prop="occureddate">
            <el-date-picker
              v-model="formData.occureddate"
              placeholder="请输入发生日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任部门" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入责任部门"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请输入发现人"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相关部门" prop="field108">
            <el-input
              v-model="formData.field108"
              clearable
              placeholder="请输入相关部门"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险描述" prop="riskeventdescription">
            <el-input
              v-model="formData.riskeventdescription"
              clearable
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>风险容忍度信息</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table>
            <el-table-column align="center" label="序号" prop="name" />
            <el-table-column align="center" label="基准线下边界" prop="name" />
            <el-table-column align="center" label="基准线上边界" prop="name" />
            <el-table-column align="center" label="颜色块编码" prop="name" />
            <el-table-column
              align="center"
              label="描述"
              show-overflow-tooltip
              width="120"
            />
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
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
  import { download } from '@/api/audit/implement'
  import { riskAdd } from '@/api/audit/question'
  import store from '@/store'
  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          risknumber: undefined,
          riskname: undefined,
          discovereddate: undefined,
          occureddate: undefined,
          riskeventdescription: undefined,
          realname: undefined,
          orgname: undefined,
        },
        footer: true,
        tableData: [],
        rules: {
          risknumber: [
            {
              required: true,
              message: '请输入风险编号',
              trigger: 'blur',
            },
          ],
          riskname: [
            {
              required: true,
              message: '请输入风险名称',
              trigger: 'blur',
            },
          ],
          discovereddate: [
            {
              required: true,
              message: '请输入发生日期',
              trigger: 'blur',
            },
          ],
          occureddate: [
            {
              required: true,
              message: '请输入发现日期',
              trigger: 'blur',
            },
          ],
          riskeventdescription: [
            {
              required: true,
              message: '请输入风险描述',
              trigger: 'blur',
            },
          ],
          realname: [
            {
              required: true,
              message: '请输入责任部门',
              trigger: 'blur',
            },
          ],
          orgname: [
            {
              required: true,
              message: '请输入相关部门',
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
       * @description: 打开表单
       * @param {*} row 传入数据
       * @return {*}
       */      
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = row.loginStaff
          this.formData.realname = row.loginStaff.realname
          this.formData.staffid = row.loginStaff.staffid
          this.formData.orgname = row.loginStaff.linkDetp.orgname
          this.formData.orgid = row.loginStaff.linkDetp.orgid
          let list = []
          list.push(row.attachment)
          this.tableData = list
        }
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const data = await riskAdd({
              ...this.formData,
              attids,
            })
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
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
