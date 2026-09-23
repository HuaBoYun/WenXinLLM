<template>
  <!-- 工程项目验收计划 edit -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="230px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="序号" prop="gcxmjgysjhNo">
            <el-input
              v-model="formData.gcxmjgysjhNo"
              clearable
              disabled
              placeholder="请输入序号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="xmmc">
            <el-input
              v-model="formData.xmmc"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建设单位" prop="jsdw">
            <el-input
              v-model="formData.jsdw"
              clearable
              placeholder="请输入建设单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类别" prop="xmlb">
            <el-select
              v-model="formData.xmlb"
              placeholder="项目类别"
              :style="{ width: '100%' }"
            >
              <el-option label="二类项目" value="二类项目" />
              <el-option label="三类项目" value="三类项目" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目总投资（万元）" prop="xmztzje">
            <el-input
              v-model="formData.xmztzje"
              clearable
              placeholder="请输入项目总投资（万元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目投产时间" prop="xmtcsj">
            <el-date-picker
              v-model="formData.xmtcsj"
              placeholder="请输入项目投产时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生产考核完成时间" prop="sckhwcsj">
            <el-date-picker
              v-model="formData.sckhwcsj"
              placeholder="请输入生产考核完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="消防设施验收-计划完成时间" prop="xfssysjhwcsj">
            <el-date-picker
              v-model="formData.xfssysjhwcsj"
              placeholder="请输入消防设施验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="环境保护验收-计划完成时间" prop="hjbhysjhwcsj">
            <el-date-picker
              v-model="formData.hjbhysjhwcsj"
              placeholder="请输入环境保护验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="安全设施验收-计划完成时间" prop="aqssysjhwcsj">
            <el-date-picker
              v-model="formData.aqssysjhwcsj"
              placeholder="请输入安全设施验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="职业病防护设施验收-计划完成时间"
            prop="zybfhssysjhwcsj"
          >
            <el-date-picker
              v-model="formData.zybfhssysjhwcsj"
              placeholder="请输入职业病防护设施验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="水土保持设施验收-计划完成时间"
            prop="stbcssysjhwcsj"
          >
            <el-date-picker
              v-model="formData.stbcssysjhwcsj"
              placeholder="请输入水土保持设施验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="土地利用验收-计划完成时间" prop="tdlyysjhwcsj">
            <el-date-picker
              v-model="formData.tdlyysjhwcsj"
              placeholder="请输入土地利用验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="节能验收-计划完成时间" prop="jnysjhwcsj">
            <el-date-picker
              v-model="formData.jnysjhwcsj"
              placeholder="请输入节能验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="安全防范系统验收-计划完成时间"
            prop="aqffxtysjhwcsj"
          >
            <el-date-picker
              v-model="formData.aqffxtysjhwcsj"
              placeholder="请输入安全防范系统验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="雷电防护装置验收-计划完成时间"
            prop="ldfhzzysjhwcsj"
          >
            <el-date-picker
              v-model="formData.ldfhzzysjhwcsj"
              placeholder="请输入雷电防护装置验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="档案验收-计划完成时间" prop="daysjhwcsj">
            <el-date-picker
              v-model="formData.daysjhwcsj"
              placeholder="请输入档案验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="竣工决算验收-上报审计时间" prop="jgjsyssbsjsj">
            <el-date-picker
              v-model="formData.jgjsyssbsjsj"
              placeholder="请输入竣工决算验收-上报审计时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目结算验收-计划完成时间" prop="xmjsysjhwcsj">
            <el-date-picker
              v-model="formData.xmjsysjhwcsj"
              placeholder="请输入项目结算验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="初步验收验收-计划完成时间" prop="cbysjhwcsj">
            <el-date-picker
              v-model="formData.cbysjhwcsj"
              placeholder="请输入初步验收验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="竣工验收验收-计划完成时间" prop="jgysjhwcsj">
            <el-date-picker
              v-model="formData.jgysjhwcsj"
              placeholder="请输入竣工验收验收-计划完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="bz">
            <el-input
              v-model="formData.bz"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
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
          <el-divider>文件上传</el-divider>
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
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
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
              <div v-if="!disabled" style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
          <el-table :data="fileList">
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
      <el-button @click="add" type="primary" :loading="loading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import {
    engineeringProjectExaminePlanDetail,
    engineeringProjectExaminePlanUpdate,
    getAutoNo,
  } from '@/oapi/audit/plan'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'gcjgysjhEdit',
    inheritAttrs: false,
    props: [],
    data() {
      const validator = (_rule, value, callback) => {
        if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          gcxmjgysjhNo: '',
          id: '',
          xmmc: '', // 项目名称
          jsdw: '', // 建设单位
          xmlb: '', // 项目类别
          xmztzje: '', // 项目总投资（万元）
          xmtcsj: '', // 项目投产时间
          sckhwcsj: '', // 生产考核完成时间
          xfssysjhwcsj: '', // 消防设施验收-计划完成时间
          hjbhysjhwcsj: '', // 环境保护验收-计划完成时间
          aqssysjhwcsj: '', // 安全设施验收-计划完成时间
          zybfhssysjhwcsj: '', // 职业病防护设施验收-计划完成时间
          stbcssysjhwcsj: '', // 水土保持设施验收-计划完成时间
          tdlyysjhwcsj: '', // 土地利用验收-计划完成时间
          jnysjhwcsj: '', // 节能验收-计划完成时间
          aqffxtysjhwcsj: '', // 安全防范系统验收-计划完成时间
          ldfhzzysjhwcsj: '', // 雷电防护装置验收-计划完成时间
          daysjhwcsj: '', // 档案验收-计划完成时间
          jgjsyssbsjsj: '', // 竣工决算验收-上报审计时间
          xmjsysjhwcsj: '', // 项目结算验收-计划完成时间
          cbysjhwcsj: '', // 初步验收验收-计划完成时间
          jgysjhwcsj: '', // 竣工验收验收-计划完成时间
          bz: '', // 备注
          attIds: [], // 附件主键集合
          cjr: '',
          cjsj: '',
        },
        formDisabled: true,
        tableData: [],
        tableData2: [],
        rules: {
          xmztzje: [
            {
              validator,
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
      async getNumber() {
        const { msg, code, data } = await getAutoNo({})
        if (code == 1) {
          this.formData.gcxmjgysjhNo = data
          this.$forceUpdate()
        } else {
          this.$baseMessage(msg, 'error')
        }
      },
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = disabled
        if (!row) {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.cjr = userInfo.realname
          this.formData.cjsj = new Date().toJSON().split('T')[0]
          this.getNumber()
        } else {
          const res = await engineeringProjectExaminePlanDetail({
            gcxmjgysjhid: row.gcxmjgysjhid,
          }) // 获取到详情回显
          if (res.code === 1) {
            this.formData = res.data
            if (res.data.attachments.length > 0) {
              this.fileList = res.data.attachments
              this.tableData = res.data.attachments
              this.tableData2 = res.data.attachments
            }
          }
        }
        if (row && !disabled) {
          this.title = '编辑'
        } else if (row && disabled) {
          this.title = '详细'
        }
      },
      close() {
        this.formData = {
          gcxmjgysjhNo: '',
          id: '',
          xmmc: '', // 项目名称
          jsdw: '', // 建设单位
          xmlb: '', // 项目类别
          xmztzje: '', // 项目总投资（万元）
          xmtcsj: '', // 项目投产时间
          sckhwcsj: '', // 生产考核完成时间
          xfssysjhwcsj: '', // 消防设施验收-计划完成时间
          hjbhysjhwcsj: '', // 环境保护验收-计划完成时间
          aqssysjhwcsj: '', // 安全设施验收-计划完成时间
          zybfhssysjhwcsj: '', // 职业病防护设施验收-计划完成时间
          stbcssysjhwcsj: '', // 水土保持设施验收-计划完成时间
          tdlyysjhwcsj: '', // 土地利用验收-计划完成时间
          jnysjhwcsj: '', // 节能验收-计划完成时间
          aqffxtysjhwcsj: '', // 安全防范系统验收-计划完成时间
          ldfhzzysjhwcsj: '', // 雷电防护装置验收-计划完成时间
          daysjhwcsj: '', // 档案验收-计划完成时间
          jgjsyssbsjsj: '', // 竣工决算验收-上报审计时间
          xmjsysjhwcsj: '', // 项目结算验收-计划完成时间
          cbysjhwcsj: '', // 初步验收验收-计划完成时间
          jgysjhwcsj: '', // 竣工验收验收-计划完成时间
          bz: '', // 备注
          attIds: [], // 附件主键集合
          cjr: '',
          cjsj: '',
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.fileList = []
        this.tableData2 = []
        this.formDisabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''
            let list = this.fileList
            list.map((item, index) => {
              this.tableData2.map((item2) => {
                if (item.attid == item2.attid) {
                  list.splice(index, 1)
                }
              })
            })
            console.log(list)
            list.map((item) => {
              attIds += item.attid + ','
            })

            attIds = attIds.substring(0, attIds.length - 1)
            this.loading = true
            let params = {
              ...this.formData,
              attIds,
            }
            delete params.attachments
            const res = await engineeringProjectExaminePlanUpdate(params)
            this.loading = false
            if (res.code === 1) {
              // 提交成功
              this.$emit('fetchData')
              this.close()
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
          } else {
            return false
          }
        })
      },
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
            let list = this.fileList
            list = list.filter((item) => item.attid != row.attid)
            this.fileList = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
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
      // handlePreview() {},
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
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
          this.tableData = [...this.tableData, ...file.data]
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
</style>
