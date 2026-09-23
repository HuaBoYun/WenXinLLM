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
        <el-col :span="12" style="height: 29px">
          <el-form-item label="群组名称" prop="groupName">
            <el-input
              v-model="formData.groupName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入群组名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人员" prop="staffName">
            <el-input
              v-model="formData.staffName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择人员"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              :style="{ width: '100%' }"
              disabled
              placeholder="请选择创建人"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="cjsj">
            <el-input
              v-model="formData.cjsj"
              :style="{ width: '100%' }"
              disabled
              placeholder="请选择创建时间"
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="24">
          <el-divider>文件上传</el-divider>
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
        </el-col> -->
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <!-- 选择人员弹窗 -->
    <executor-options
      ref="executor"
      :isCheckout="true"
      @projectManage="handleExecutorSelected"
    />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/qz'
  import ExecutorOptions from '@/components/selectPerson.vue'

  const token = store.getters['user/token']

  export default {
    components: { ExecutorOptions },
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
          id: '',
          cjr: '',
          cjsj: '',
          groupName: '',
          staffIds: '',
          // staffId: '',
          staffName: '',
        },
        footer: true,
        rules: {
          groupName: [
            {
              required: true,
              message: '请输入群组名称',
              trigger: 'blur',
            },
          ],
          staffName: [
            {
              required: true,
              message: '请选择人员',
              trigger: ['blur'],
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
      // 选择人员
      handleExecutorSelected(node) {
        let name = ''
        let idStr = ''
        node.map((item, index) => {
          if (index !== node.length - 1) {
            name += item.realname + ','
            idStr += item.staffid + ','
          } else {
            name += item.realname
            idStr += item.staffid
          }
        })
        this.$set(this.formData, 'staffName', name)
        this.$set(this.formData, 'staffIds', idStr)
        // this.$set(this.formData, 'staffId', idStr)
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getInfoDetail({ id: row.id })
          // this.formData.createType = res.data.createType
          // this.formData.dataBaseConnectionAddress =
          //   res.data.dataBaseConnectionAddress
          const name =
            res.data.staffs &&
            res.data.staffs.map((res) => res.realName).toString()
          const ids =
            res.data.staffs &&
            res.data.staffs.map((res) => res.staffId).toString()
          this.formData.staffName = name
          this.formData.staffIds = ids
          this.formData.groupName = res.data.groupName
          this.formData.cjr = res.data.creatorName
          this.formData.cjsj = res.data.createdTime
          this.formData.id = res.data.id
          // this.formData = JSON.parse(JSON.stringify(row))
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.cjr = userInfo.realname
          const year = new Date().getFullYear() //得到年份
          const month = new Date().getMonth() //得到月份
          const date = new Date().getDate() //得到日期
          this.formData.cjsj = `${year}-${month < 10 ? '0' + month : month}-${
            date < 10 ? '0' + date : date
          }`
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
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
        this.formData.id = ''
        this.formData.cjr = ''
        this.formData.cjsj = ''
        this.formData.groupName = ''
        this.formData.staffIds = 'Oracle'
        // this.formData.staffId = 'Oracle'
        this.formData.staffName = ''
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfor(params)
            if (res && res.code == 200) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
          }
        })
      },
      async checkLink() {
        return
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
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
