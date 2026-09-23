<template>
  <!-- 三级单位离任审计 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="分配"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="code">
            <el-input
              v-model="formData.code"
              placeholder="请输入编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="批次" prop="batc">
            <el-input
              v-model="formData.batc"
              placeholder="请输入批次"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="tbrgname">
            <el-input
              v-model="formData.tbrgname"
              disabled
              placeholder="请输入填报单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人">
            <el-input
              v-model="formData.createname"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间">
            <el-input
              v-model="formData.createdate"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="24">
          <el-table
            v-loading="listLoading"
            :data="tableData1"
            @selection-change="handleSelectAll"
            @select="handleSelection"
            ref="multipleTable"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column align="center" label="项目名称" prop="name">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetail(row)"
                  style="white-space: pre-line; line-height: 16px"
                >
                  {{ row.name }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="实施单位"
              prop="exePhraseUnit"
            />
            <el-table-column
              align="center"
              label="被审计单位"
              prop="auditUnit"
            ></el-table-column>
            <el-table-column
              align="center"
              label="小组"
              prop="auditGroup"
            ></el-table-column>
            <el-table-column
              align="center"
              label="督导人员"
              prop="fpksrynames"
            ></el-table-column>
            <el-table-column align="center" label="状态" prop="xfstatus">
              <template #default="{ row }">
                {{
                  row.xfstatus == '1'
                    ? '已上报'
                    : row.xfstatus == '2'
                    ? '退回'
                    : ''
                }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleGcxmDetail(row)"
                  :disabled="false"
                  v-if="row.gljhxmlx == '31' || row.gljhxmlx == '32'"
                >
                  分配
                </el-button>
                <el-button
                  type="text"
                  @click="handleCwxmDetail(row)"
                  :disabled="false"
                  v-if="row.gljhxmlx == '12'"
                >
                  分配
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- <el-pagination
            class="pagination"
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          /> -->
          <el-col :span="24" style="margin-top: 16px">
            <el-divider>文件上传</el-divider>
          </el-col>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <!-- <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-preview="handlePreview"
                :on-success="handleSuccess"
                :file-list="tableData"
              >
                <el-button type="success" disabled>上传</el-button>
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
                width="120"
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
                    @click="handleDeleteFile(row)"
                    disabled
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-col>
      </el-form>
    </el-row>
    <cwsjxmapbEdit ref="edit" @getNewData="fetchData"></cwsjxmapbEdit>

    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="handleBack">上报</el-button>
    </div>

    <gcsjxmapbView1 ref="gcsjxmapbView1" type="report" />
    <gcsjxmapbView2 ref="gcsjxmapbView2" type="report" />
  </el-dialog>
</template>

<script>
  import { sjdwlrsjlrExportData } from '@/oapi/audit/plan'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import {
    planOutArrangeSaveOrUpdate,
    planOutArrangeDetail,
    fundAuditProjectList,
    xfksrygc,
    gcfpksry,
    enginAuditProjectXmzsb,
    enginFileList,
    enginFileDel,
  } from '@/api/monitor/question'
  import cwsjxmapbEdit from '@/views/oilAudit/jhlx/components/gcsjxmapbEdit.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import gcsjxmapbView1 from './gcsjxmapbView1.vue'
  import gcsjxmapbView2 from './gcsjxmapbView2.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { cwsjxmapbEdit, gcsjxmapbView1, gcsjxmapbView2 },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/leave/audit3L/importData',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        listLoading: false,
        tableData: [],
        tableData1: [],
        formData: {
          batc: '', //批次
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDate(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          gljson: '',
        },
        rules: {
          batc: [
            {
              required: true,
              message: '请输入批次',
              trigger: 'blur',
            },
          ],
          tbrgname: [
            {
              required: true,
              message: '请输入填报单位',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        disabled: false,
        select: [],
        rows: {},
      }
    },
    mounted() {
      // this.getOption()
    },
    methods: {
      async showEdit(row, title) {
        this.dialogJdVisible = true
        this.title = title
        this.disabled = title == 'detail'
        this.rows = row
        if (row) {
          const {
            data: { data },
          } = await planOutArrangeDetail({ tbid: row.tbid })
          this.formData.batc = data.batc
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdate = data.createdate
          this.formData.createname = data.createname
          this.formData.tbid = data.tbid
          this.formData.code = data.code

          this.tableData1 = data.list.map((v) => {
            v.fpksrynames = v.fpksrynames || ''
            return v
          })
          // this.fetchData()

          let res = await enginFileList({ tbid: row.tbid })
          this.tableData = res.data.list
        }
      },
      close() {
        this.formData = {
          batc: '', //批次
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDate(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          gljson: '',
        }
        this.tableData = []
        this.tableData1 = []
        this.select = []
        this.dialogJdVisible = false
      },
      handleBack() {
        if (this.select.length > 0) {
          let ids = this.select.map((k) => k.id)
          this.$baseConfirm('你确定要上报当前项吗', null, async () => {
            const res = await enginAuditProjectXmzsb({ ids: ids.join() })
            if (res.code == 1) {
              this.$baseMessage('成功', 'success', 'vab-hey-message-success')
              await this.fetchData()
            }
          })
        } else {
          this.$message({
            message: '请选择需要上报的项目',
            type: 'error',
          })
        }
      },
      async fetchData() {
        const {
          data: { data },
        } = await planOutArrangeDetail({ tbid: this.rows.tbid })
        this.formData.batc = data.batc
        this.formData.tbrgname = data.tbrgname
        this.formData.tbrgid = data.tbrgid
        this.formData.createdate = data.createdate
        this.formData.createname = data.createname
        this.formData.tbid = data.tbid
        this.formData.code = data.code

        this.tableData1 = data.list.map((v) => {
          v.fpksrynames = v.fpksrynames || ''
          return v
        })

        // const arr = JSON.parse(JSON.stringify(this.tableData))
        // if (!data) {
        //   this.tableData = [...arr]
        //   return false
        // }
        // if (data?.index) {
        //   arr[data.index - 1] = data.data[0]
        //   this.tableData = [...arr]
        // } else {
        //   this.tableData = [...arr, ...data.data]
        // }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleCwxmDetail(row, index) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleGcxmDetail(row) {
        if (row.gljhxmlx == '31') {
          this.$refs['gcsjxmapbView1'].showEdit({ data: row })
        } else {
          this.$refs['gcsjxmapbView2'].showEdit({ data: row })
        }
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)

            delete this.formData.createdate
            const ids = this.tableData1.map((res) => res.id)
            const res = await planOutArrangeSaveOrUpdate({
              ...this.formData,
              gljson: ids.toString(),
              attids,
            })
            if (res && res.code === 1) {
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
      handleSuccess(response) {
        if (response.code == 1) {
          this.tableData1 = [...this.tableData1, ...response.data.data]
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      async handleExport() {
        const data = await sjdwlrsjlrExportData({
          ...this.queryForm,
          jdid: this.formData.jdid,
        })
        let fileName = '三级单位离任审计表'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, 'detail')
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        this.select = val
        // const curSelected = val.filter((x) => !!x)
        // if (curSelected && curSelected.length) {
        //   curSelected.map((row) => {
        //     if (row && !this.select.some((x) => x.id == row.id)) {
        //       this.select.push(row)
        //     }
        //   })
        // } else {
        //   this.list.map((row) => {
        //     const i = this.select.findIndex((x) => x.id == row.id)
        //     if (i >= 0) {
        //       this.select.splice(i, 1)
        //     }
        //   })
        // }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDeleteFile(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await fundFileDel({ attid: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      // handlePreview(file) {},
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
<style scoped lang="scss">
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
