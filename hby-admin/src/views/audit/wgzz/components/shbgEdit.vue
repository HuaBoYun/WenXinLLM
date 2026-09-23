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
        label-width="160px"
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
              :style="{ width: '246px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
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
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="初步核实开展过程情况" prop="verifyconkzgcqk">
            <el-input
              v-model="formData.verifyconkzgcqk"
              clearable
              type="textarea"
              row="4"
              placeholder="请输入初步核实开展过程情况"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="初步核实结果" prop="verifyconhsjg">
            <el-input
              v-model="formData.verifyconhsjg"
              clearable
              type="textarea"
              row="4"
              placeholder="请输入初步核实结果"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="工作建议" prop="cluegzjy">
            <el-input
              v-model="formData.cluegzjy"
              clearable
              type="textarea"
              row="4"
              placeholder="请输入工作建议"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="是否发现需要追究责任事项" prop="cluezrdx">
            <el-radio-group
              v-model="formData.cluezrdx"
              :disabled="!footer"
              @input="handleChange"
            >
              <el-radio label="是">是</el-radio>
              <el-radio label="否">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="分类处置"
            prop="checkedCities"
            v-if="formData.cluezrdx == '是'"
          >
            <el-checkbox-group
              v-model="formData.checkedCities"
              :disabled="!footer"
            >
              <el-checkbox v-for="city in cities" :label="city" :key="city">
                {{ city }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              :disabled="!footer"
              template="nbsj"
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
    <projectList ref="list" @selected="handleProjectSelected" :type="1" />
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import {
    wgbgFileList,
    shbgSave,
    shbgRemovefilue,
    getSHBGFileList,
  } from '@/api/audit/wgzz'
  import projectList from './options/projectList.vue'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'FlawInfo',
    components: { UEditor: () => import('@/components/UEditor'), projectList },
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
          id: undefined,
          cluenaber: undefined,
          clueid: undefined,
          verifycontent: undefined,
          verifyconkzgcqk: undefined,
          verifyconhsjg: undefined,
          cluegzjy: undefined,
          filue: undefined,
          content: undefined,
          cluezrdx: undefined,
          checkedCities: [],
        },
        templates: [],
        footer: true,
        tableData: [],
        tableData2: [],
        rules: {
          cluenaber: [
            {
              required: true,
              message: '请输入线索编号',
              trigger: 'blur',
            },
          ],
          checkedCities: [
            {
              required: true,
              message: '请选择分类处置',
              trigger: 'blur',
            },
          ],
          cluezrdx: [
            {
              required: true,
              message: '请选择是否发现需要追究责任事项',
              trigger: 'blur',
            },
          ],
          verifyconkzgcqk: [
            {
              required: true,
              message: '请输入初步核实开展过程情况',
              trigger: 'blur',
            },
          ],
          verifyconhsjg: [
            {
              required: true,
              message: '请输入初步核实结果',
              trigger: 'blur',
            },
          ],
          cluegzjy: [
            {
              required: true,
              message: '请输入工作建议',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        staffid: '',
        arr: [],
        cities: [
          '报告集团公司',
          '公司开展核查工作',
          '移送有关纪检监察机构',
          '移送公司有关部门',
          '向相关国家监察机关或司法机关报案',
          '其他处置方式',
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = row[key]
          })
          this.formData.checkedCities = row.cluessrd
            ? row.cluessrd.split(',')
            : []

          this.getFileList(row.id)
          this.getFileList2(row.id)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
        }
      },
      async getFileList(id) {
        const data = await wgbgFileList({ id })
        if (data.code == '1') {
          this.tableData = data.data.data || []
        } else {
          this.tableData = []
        }
      },
      getFileList2(id) {
        getSHBGFileList({ id: id }).then((res) => {
          this.tableData2 = res.data.list
          this.$forceUpdate()
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.formData = {
          id: undefined,
          cluenaber: undefined,
          clueid: undefined,
          verifycontent: undefined,
          verifyconkzgcqk: undefined,
          verifyconhsjg: undefined,
          cluegzjy: undefined,
          filue: undefined,
          content: undefined,
          cluezrdx: undefined,
          checkedCities: [],
        }
        this.tableData = []
        this.tableData2 = []
        this.footer = true
      },
      add() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            const cluessrd = this.formData.checkedCities.toString()
            delete this.formData.impcreateusername
            const data = await shbgSave({
              ...this.formData,
              attIds,
              cluessrd,
            })

            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
              this.close()
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      async handleDelete(row) {
        let list = this.tableData || []
        list = list.filter((item) => item.attid != row.attid)
        await shbgRemovefilue({ attid: row.attid })
        this.$message.success('删除成功')
        this.tableData = list
      },
      handlePreview(file) {},

      handleProjectSelected(node) {
        console.log('node', node)
        this.$set(this.formData, 'cluenaber', node.cluenaber)
        this.$set(this.formData, 'verifycontent', node.verifycontent)
        this.$set(this.formData, 'clueid', node.clueid)
        // this.$refs['ruleForm'].clearValidate()
      },
      handleChange() {
        this.formData.checkedCities = []
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
