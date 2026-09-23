<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="180px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="线索编号" prop="cluenaber">
            <el-input
              v-model="formData.cluenaber"
              clearable
              placeholder="请输入线索编号"
              :style="{ width: '200px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="footer"
              @click="$refs.list.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核实内容" prop="verifycontent">
            <el-input
              v-model="formData.verifycontent"
              clearable
              placeholder="请输入核实内容"
              :style="{ width: '256px' }"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉及部门" prop="departmentname">
            <el-input
              v-model="formData.departmentname"
              clearable
              placeholder="请输入涉及部门"
              :style="{ width: '200px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="footer"
              @click="$refs.companyTreeModel.show(true)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉及人员" prop="personnel">
            <el-input
              v-model="formData.personnel"
              clearable
              placeholder="请输入涉及人员"
              :style="{ width: '200px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="footer"
              @click="$refs.manage.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="经办人" prop="jbrname">
            <el-input
              v-model="formData.jbrname"
              clearable
              placeholder="请输入经办人"
              :style="{ width: '68%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="footer"
              @click="$refs.manage1.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="问题和线索来源" prop="clueresource">
            <el-input
              v-model="formData.clueresource"
              clearable
              placeholder="请输入问题和线索来源"
              :style="{ width: '256px' }"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="违规情形核实情况" prop="hscontent">
            <el-input
              v-model="formData.hscontent"
              clearable
              type="textarea"
              row="4"
              placeholder="请输入违规情形核实情况"
              :style="{ width: '100%' }"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资产损失和损失风险核实情况" prop="zcssqk">
            <el-input
              v-model="formData.zcssqk"
              clearable
              type="textarea"
              row="4"
              placeholder="请输入资产损失和损失风险核实情况"
              :style="{ width: '100%' }"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="涉嫌违纪问题线索说明" prop="wjwtxs">
            <el-input
              v-model="formData.wjwtxs"
              clearable
              type="textarea"
              row="4"
              placeholder="请输入涉嫌违纪问题线索说明"
              :style="{ width: '100%' }"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              :disabled="footer"
              template="nbsj"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="!footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :multiple="true"
            >
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
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
                <el-button
                  type="text"
                  @click="handleDowns(row)"
                  :disabled="false"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="send" type="primary">确定</el-button>
    </div>
    <template #footer v-if="!footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="send" type="primary">确定</el-button>
    </template>
    <CompanyTreeModel
      ref="companyTreeModel"
      @selected="selectCompany"
      :lable="'涉及单位'"
    />
    <projectManage
      :modal="false"
      ref="manage"
      @projectManage="reviewTypeSelect"
    />
    <projectManage1
      :modal="false"
      ref="manage1"
      @projectManage="reviewTypeSelect1"
    />
    <projectList ref="list" @selected="handleProjectSelected" :type="3" />
  </el-dialog>
</template>

<script>
  import {
    wgjyysSave,
    wgjyysList,
    getwghcFileList,
    getwghcDetail,
  } from '@/api/audit/wgzz'
  import { shbgRemovefilue } from '@/api/audit/wgzz'
  import { download } from '@/api/audit/implement'
  import store from '@/store'
  import CompanyTreeModel from '@/components/CompanyTreeModel/index.vue'
  import projectManage from '@/components/danxuanPerson.vue'
  import projectManage1 from '@/components/danxuanPerson.vue'
  import projectList from '@/views/audit/wgzz/components/options/projectList.vue'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'FlawInfo',
    components: {
      UEditor: () => import('@/components/UEditor'),
      CompanyTreeModel,
      projectManage,
      projectManage1,
      projectList,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          cluenaber: undefined,
          hscontent: undefined,
          content: undefined,
          clueresource: undefined,
          jbrname: undefined,
          jbrid: undefined,
          departmentname: undefined,
          department: undefined,
          personnelid: undefined,
          personnel: undefined,
          id: undefined,
          wjwtxs: undefined,
          verifycontent: undefined,
          zcssqk: undefined,
        },
        footer: true,
        rules: {
          cluenaber: [
            {
              required: true,
              message: '请选择线索编号',
              trigger: 'blur',
            },
          ],
          verifycontent: [
            {
              required: true,
              message: '请输入核实内容',
              trigger: 'blur',
            },
          ],
          jbrname: [
            {
              required: true,
              message: '请选择经办人',
              trigger: 'blur',
            },
          ],
          clueresource: [
            {
              required: true,
              message: '请输入问题和线索来源',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新建',
        staffid: '',
        arr: [],
        templates: [],
        tableData: [],
      }
    },
    methods: {
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(title, row) {
        this.dialogFormVisible = true
        this.title = title
        if (title == '编辑' || title == '新增') {
          this.footer = false
        } else if (title == '详细') {
          this.footer = true
        }
        if (row) {
          getwghcDetail({ id: row.id }).then((res) => {
            Object.keys(this.formData).forEach((key) => {
              this.formData[key] = res.data[key]
            })
          })

          this.getFileList(row.id)
        }
      },
      async getFileList(id) {
        const data = await getwghcFileList({ id })

        if (data.code == '1') {
          this.tableData = data.data.data || []
        } else {
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.formData = {
          cluenaber: undefined,
          hscontent: undefined,
          content: undefined,
          clueresource: undefined,
          jbrname: undefined,
          jbrid: undefined,
          departmentname: undefined,
          department: undefined,
          personnelid: undefined,
          personnel: undefined,
          id: undefined,
          wjwtxs: undefined,
          verifycontent: undefined,
          zcssqk: undefined,
        }
        this.footer = true
        this.tableData = []
      },
      send() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const fileids = this.tableData.map((x) => x.attid).join(',')
            const data = await wgjyysSave({
              ...this.formData,
              fileids: fileids,
            })

            if (data.code == 1) {
              this.formData = {}
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      selectCompany(val) {
        let orgidnames = val.map((item) => item.name).join(',')
        let orgids = val.map((item) => item.id).join(',')
        this.$set(this.formData, 'departmentname', orgidnames)
        this.$set(this.formData, 'department', orgids)
        this.$refs['ruleForm'].clearValidate()
      },
      reviewTypeSelect(e) {
        this.$set(this.formData, 'personnel', e[0].realname)
        this.$set(this.formData, 'personnelid', e[0].staffid)
        this.$refs['ruleForm'].clearValidate()
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      async handleDelete(row) {
        let list = this.tableData || []
        list = list.filter((item) => item.attid != row.attid)
        const res = await shbgRemovefilue({ attId: row.attid })
        if (res.result == 200) {
          this.$baseMessage('删除成功', 'success')
        }
        this.tableData = list
      },

      reviewTypeSelect1(e) {
        this.$set(this.formData, 'jbrname', e[0].realname)
        this.$set(this.formData, 'jbrid', e[0].staffid)
      },
      handleProjectSelected(node) {
        console.log('node', node)
        this.$set(this.formData, 'cluenaber', node.cluenaber)
        this.$set(this.formData, 'verifycontent', node.verifycontentnew)
      },
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }
        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          // 更新文件列表
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
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
