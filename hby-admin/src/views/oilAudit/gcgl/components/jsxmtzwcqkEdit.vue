<template>
  <!-- 建设项目投资完成情况 新增/修改 -->
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
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="合同编号" prop="htbh">
            <div style="display: flex">
              <el-input
                v-model="formData.htbh"
                clearable
                placeholder="请输入合同编号"
                :style="{ width: '100%' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click.native="handleHtbh"
                size="mini"
              >
                选择
              </el-button>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工程或费用名称" prop="gchfymc">
            <el-input
              v-model="formData.gchfymc"
              clearable
              placeholder="请输入工程或费用名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施单位" prop="ssdw">
            <el-input
              v-model="formData.ssdw"
              clearable
              placeholder="请输入实施单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="tbdwName" style="height: 29px">
            <div style="display: flex">
              <el-input
                v-model="formData.tbdwName"
                disabled
                placeholder="请输入填报单位"
                :style="{ width: '100%' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click.native="showGroupLeader"
                size="mini"
              >
                选择
              </el-button>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="批复概算投资"
            prop="pfgstzje"
            style="height: 29px"
          >
            <el-input
              v-model="formData.pfgstzje"
              clearable
              @input="handleInput2"
              placeholder="请输入批复概算投资"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额" prop="htje">
            <el-input
              v-model="formData.htje"
              clearable
              @input="handleInput1"
              placeholder="请输入合同金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结算金额" prop="jsje">
            <el-input
              v-model="formData.jsje"
              clearable
              @input="handleInput"
              placeholder="请输入结算金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="投资节超（概算-实际完成）" prop="tzjc">
            <el-input
              v-model="formData.tzjc"
              clearable
              placeholder="请输入投资节超（概算-实际完成）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="项目类别" prop="fl">
            <el-select style="width: 100%" v-model="formData.fl" disabled>
              <el-option label="三类" value="三类"></el-option>
              <el-option label="四类" value="四类"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="投资节超情况说明" prop="tzjcqksm">
            <el-input
              v-model="formData.tzjcqksm"
              clearable
              placeholder="请输入投资节超情况说明"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="xmzttype">
            {{ formData.xmzttype }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目状态" prop="xmstatus">
            {{ formData.xmstatus == 1 ? '已做审计项目' : '未做审计项目' }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>（一）工程费用</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              @click="addSon(null, 'tableData1', 'add')"
            >
              新建
            </el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column align="center" label="项目名称" prop="gchfymc">
              <template #default="{ row }">
                <el-button
                  type="text"
                  :disabled="false"
                  @click="addSon(row, 'tableData1')"
                >
                  {{ row.gchfymc }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="合同编号" prop="htbh" />
            <!-- <el-table-column align="center" label="合同编号" prop="htbh">
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row, 'tableData1')">
                  {{ row.htbh }}
                </el-button>
              </template>
            </el-table-column> -->
            <el-table-column align="center" label="实施单位" prop="ssdw" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="addSon(row, 'tableData1', 'edit')"
                  v-if="!formDisabled"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteList(row, 1)"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>（二）其他费用</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              @click="addSon(null, 'tableData2', 'add')"
            >
              新建
            </el-button>
          </div>
          <el-table :data="tableData2">
            <el-table-column align="center" label="项目名称" prop="gchfymc">
              <template #default="{ row }">
                <el-button
                  type="text"
                  :disabled="false"
                  @click="addSon(row, 'tableData2')"
                >
                  {{ row.gchfymc }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="合同编号" prop="htbh" />
            <el-table-column align="center" label="实施单位" prop="ssdw" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="addSon(row, 'tableData2', 'edit')"
                  v-if="!formDisabled"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteList(row, 2)"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-divider>
            文件上传
            <span class="color-red">
              (需上传主管领导签字加盖二级机构公章扫描件)
            </span>
          </el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
            >
              <el-button type="success">上传</el-button>
            </el-upload> -->
            <el-upload
              style="text-align: right; margin-bottom: 5px"
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
          <el-table :data="tableDataUpload">
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
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>

    <!-- 选择单位（公司） -->
    <company-select-modal ref="audiTree" @submit="handleCompanyTreeSelected" />
    <htbhModal ref="htbhModal" @selected="handleSubmit" />
    <SonEdit
      ref="sonEdit"
      @tableData1="tableDataHandle1"
      @tableData2="tableDataHandle2"
    ></SonEdit>
  </el-dialog>
</template>

<script>
  import CompanySelectModal from '@/views/oilAudit/jhlx/components/department.vue'
  import htbhModal from '@/views/oilAudit/gcgl/components/htbhModal.vue'
  import {
    jsxmtzwcqkUpdate,
    jsxmtzwcqkDetail,
    jsxmtzwcqkDelete,
  } from '@/oapi/audit/plan'
  import { uploadApi } from '@/api/fwgl/api'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import store from '@/store'
  const token = store.getters['user/token']
  const { baseURL } = require('@/config')
  import SonEdit from './sonEdit.vue'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'jsxmtzwcqkEdit',
    inheritAttrs: false,
    props: [],
    components: { SonEdit, CompanySelectModal, htbhModal },
    data() {
      return {
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: { token: token },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          htbh: '',
          gchfymc: '',
          ssdw: '',
          pfgstzje: '',
          htje: '',
          jsje: '',
          tzjc: '',
          tzjcqksm: '',
          cjr: '',
          cjsj: '',
          fl: '',
          tbdwName: '',
          tbdwId: '',
        },
        formDisabled: true,
        tableData1: [],
        tableData2: [],
        tableDataUpload: [], // 上传回显table
        rules: {},
        dialogFormVisible: false,
        title: '新增',
        jsxmtzwcqkid: '',
        type: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async showEdit(row, title, type) {
        //由于手动取消,并且要回显到父组件,只能在新增时候清空
        this.formData = {
          htbh: '',
          gchfymc: '',
          ssdw: '',
          pfgstzje: '',
          htje: '',
          jsje: '',
          tzjc: '',
          tzjcqksm: '',
          cjr: '',
          cjsj: '',
          fl: '',
          tbdwName: '',
          tbdwId: '',
        }
        this.jsxmtzwcqkid = ''
        this.tableData1 = [] // 新建 1
        this.tableData2 = [] // 新建 2
        this.tableDataUpload = [] // 父编辑的上传文件集合
        this.type = title
        // 打开编辑
        this.dialogFormVisible = true
        if (title == 'edit') {
          this.title = '编辑'
          this.formDisabled = false
        } else if (title == 'detail') {
          this.title = '详细'
          this.formDisabled = true
        } else {
          this.$set(
            this.formData,
            'createUser',
            JSON.parse(localStorage.getItem('userInfo')).realname
          )
        }
        if (title == 'add') {
          this.formDisabled = false
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.cjr = userInfo.realname
          this.formData.cjsj = new Date().toJSON().split('T')[0]
          this.formData.fl = type
        } else {
          this.jsxmtzwcqkid = row.jsxmtzwcqkid
          const res = await jsxmtzwcqkDetail({ jsxmtzwcqkid: row.jsxmtzwcqkid })
          Object.assign(this.formData, res.data)
          this.tableDataUpload = res.data.attachments || []
          this.tableData1 = res.data.fymxGcfy ? res.data.fymxGcfy : []
          this.tableData2 = res.data.fymxQtfy ? res.data.fymxQtfy : []
          return
        }
      },
      close() {
        this.dialogFormVisible = false
        this.formDisabled = true
      },
      add() {
        // 子编辑 保存
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableDataUpload.map((item) => {
              attids += item.attid
              attids += ','
            })

            attids = attids.substring(0, attids.length - 1)
            delete this.formData.fymxGcfy
            delete this.formData.fymxQtfy
            delete this.formData.attachments
            const res = await jsxmtzwcqkUpdate({
              ...this.formData,
              attids,
            })
            if (res.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetchData', res.data, this.type)
              this.showEdit(res.data, 'edit')
            } else {
              this.$baseMessage(res.msg, 'error')
            }
          } else {
            return false
          }
        })
      },
      // 新建 1 提交后回调函数
      tableDataHandle1(row, type) {
        // row 新建 1 提交后返回值
        if (type == 'add') {
          this.tableData1.push(row)
        } else {
          this.tableData1.map((v, i) => {
            if (v.jsxmtzwcqkfymxid == row.jsxmtzwcqkfymxid) {
              this.$set(this.tableData1, i, row)
            }
            return v
          })
        }
      },
      // 新建 2 提交后回调函数
      tableDataHandle2(row, type) {
        if (type == 'add') {
          this.tableData2.push(row)
        } else {
          this.tableData2.map((v, i) => {
            if (v.jsxmtzwcqkfymxid == row.jsxmtzwcqkfymxid) {
              this.$set(this.tableData2, i, row)
            }
            return v
          })
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableDataUpload
            list = list.filter((item) => item.attid != row.attid)
            this.tableDataUpload = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },

      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url })
      // },
      // handleSuccess(file) {
      //   // 父编辑上传回显
      //   if (file.result == '200') {
      //     let list = this.tableDataUpload
      //     list.push(file.data)
      //     this.tableDataUpload = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      addSon(data, whichOne, type) {
        if (!this.jsxmtzwcqkid) {
          return this.$message.error('请先保存基本信息，再添加费用信息')
        }
        let row = null
        if (data) {
          row = data
        } else {
          row = this.jsxmtzwcqkid
        }
        this.$refs['sonEdit'].showEdit(row, whichOne, type)
      },
      //其它费用工程费用删除
      handleDeleteList(row, type) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await jsxmtzwcqkDelete({ ids: row.jsxmtzwcqkfymxid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = []
            if (type == 1) {
              list = this.tableData1
              list = list.filter(
                (item) => item.jsxmtzwcqkfymxid != row.jsxmtzwcqkfymxid
              )
              this.tableData1 = list
            } else {
              list = this.tableData2
              list = list.filter(
                (item) => item.jsxmtzwcqkfymxid != row.jsxmtzwcqkfymxid
              )
              this.tableData2 = list
            }
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      handleInput(event) {
        // 允许数字和小数点
        const regex = /^\d*\.?\d*$/
        // 如果输入不匹配，就撤回到上一个合法的值
        if (!regex.test(event)) {
          // 这里假设你已经有一个变量 value 来绑定输入的值
          this.formData.jsje = this.formData.jsje
            .replace(/[^0-9.]/g, '')
            .replace(/(\..*)\./g, '$1')
        }
      },
      handleInput1(event) {
        // 允许数字和小数点
        const regex = /^\d*\.?\d*$/
        // 如果输入不匹配，就撤回到上一个合法的值
        if (!regex.test(event)) {
          // 这里假设你已经有一个变量 value 来绑定输入的值
          this.formData.htje = this.formData.htje
            .replace(/[^0-9.]/g, '')
            .replace(/(\..*)\./g, '$1')
        }
      },
      handleInput2(event) {
        // 允许数字和小数点
        const regex = /^\d*\.?\d*$/
        // 如果输入不匹配，就撤回到上一个合法的值
        if (!regex.test(event)) {
          // 这里假设你已经有一个变量 value 来绑定输入的值
          this.formData.pfgstzje = this.formData.pfgstzje
            .replace(/[^0-9.]/g, '')
            .replace(/(\..*)\./g, '$1')
        }
      },
      handleHtbh() {
        this.$refs['htbhModal'].showEdit()
      },
      handleSubmit(val) {
        this.formData.htbh = val[0].htbh
        this.formData.gchfymc = val[0].ysxmmc
        this.formData.pfgstzje = val[0].jhtzje
      },
      showGroupLeader() {
        // 选公司
        this.$refs['audiTree'].showEdit()
      },
      handleCompanyTreeSelected(val) {
        // 选公司后处理
        this.formData.tbdwName = val.label
        this.formData.tbdwId = val.id
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
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
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
          this.fileList = [...this.fileList, ...file.data]
          this.tableDataUpload = [...this.tableDataUpload, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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
  .color-red {
    color: red;
    font-size: 12px;
  }
</style>
