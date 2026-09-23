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
          <el-form-item
            label="进场纪要编号"
            label-width="140px"
            prop="entercoed"
          >
            <el-input
              v-model="formData.entercoed"
              clearable
              placeholder="请输入进场纪要编号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="进场纪要名称"
            label-width="140px"
            prop="entername"
          >
            <el-input
              v-model="formData.entername"
              clearable
              placeholder="请输入进场纪要名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="content">
            <!-- <tinymce
              v-model="formData.content"
              :height="300"
              placeholder="请输入编辑器"
            /> -->
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              template="jcjy"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
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
  import {
    approachSummarySave,
    deleteFile,
    download,
    inMeetRecordFileList,
  } from '@/api/audit/implement'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  const { baseURL } = require('@/config')
  export default {
    name: 'SummanyInfo',
    components: { Tinymce, UEditor },
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
          entercoed: undefined,
          entername: undefined,
          content: undefined,
        },
        templates: [],
        footer: true,
        tableData: [],
        rules: {
          entercoed: [
            {
              required: true,
              message: '请输入进场纪要编号',
              trigger: 'blur',
            },
          ],
          entername: [
            {
              required: true,
              message: '请输入进场纪要名称',
              trigger: 'blur',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入编辑器',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {
      'formData.describe'(val) {
        if (this.$refs['ueditor'].editor.openTemplate) {
          this.$refs['ueditor'].editor.openTemplate = false
          let s = val
          const arr = [
            ['$[contract.contractno]', 'contractno'],
            ['$[contract.contractname]', 'contractname'],
            ['$[contract.contractamount]', 'contractmoney'],
            ['$[contract.contractItem]', 'contractitem'],
            ['$[contract.executor]', 'realname'],
            ['$[contract.rmbinwords]', 'hzsumowing'],

            ['$[counterpart.coupersion]', 'counterpartcode'],
            ['$[counterpart.personincharge]', 'contractbd'],
            ['$[counterpart.counterpartHank]', 'bankkhyh'],
            ['$[counterpart.counumber]', 'counterpartno'],
            ['$[counterpart.couname]', 'budgetname'],
            ['$[counterpart.couaddress]', 'counterpartaddress'],
            ['$[counterpart.coupersion]', 'contacts'],
            ['$[counterpart.contactsPhone]', 'contactsphone'],
            ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
            ['$[counterpart.legarepresentative]', 'contacts'],
            ['$[counterpart.pctelephonenumber]', 'contractzd'],
            // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
          ]
          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.content = s
        }
      },
    },
    created() {},
    mounted() {},
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = row.entermeeting
          this.getFileList(row.entermeeting.enterid)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        }
      },
      async getFileList(enterid) {
        const data = await inMeetRecordFileList({ enterid })
        this.tableData = data.data.data || []
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
            const userInfo = JSON.parse(localStorage.getItem('userInfo'))
            const { creatrtime, status, ...other } = this.formData
            attids = attids.substring(0, attids.length - 1)
            const data = await approachSummarySave({
              ...other,
              attids,
              createstaffid: userInfo.staffid,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.message, 'error')
            }
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData || []
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
