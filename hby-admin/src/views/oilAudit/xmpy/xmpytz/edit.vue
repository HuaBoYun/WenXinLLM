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
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-form-item label="通知标题" prop="noticeTitle">
            <el-input
              v-model="formData.noticeTitle"
              clearable
              placeholder="请输通知标题"
              :disabled="!disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="通知内容" prop="noticeContent">
            <el-input
              v-model="formData.noticeContent"
              clearable
              placeholder="请输通知内容"
              :disabled="!disabled"
              type="textarea"
              :rows="2"
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              clearable
              placeholder="请输创建人"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" label-width="140px" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="选择创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="disabled">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              action=""
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
              :multiple="true"
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
                <el-button type="text" @click="handleDowns(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>

    <template #footer v-if="disabled">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    jhyjgSaveOrUpdate,
    jhyjgList,
    jhyjgDetail,
    download,
    deleteReportFile,
  } from '@/oapi/audit/report'
  import { getXmpytzDetail, addOrUpdateXmpytz } from '@/oapi/audit/xmpy'
  import { implementPlanList } from '@/oapi/audit/project'
  import store from '@/store'
  import { formatDate, formatDay } from '@/utils/index'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  const { baseURL } = require('@/config')
  export default {
    name: 'xmpytzEdit',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          noticeContent: undefined,
          noticeTitle: undefined,
          id: '',
        },
        auditProjectSelectList: [],
        tableData: [],
        fileList: [], // 用于存储所有选择的文件
        disabled: true,
        rules: {
          title: [
            {
              required: true,
              message: '请输入标题',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
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
          console.error('baseApi, api, headers, key 或 iv 未正确设置')
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
          .then(() => {
            console.log('自定义上传完成')
          })
          .catch((error) => {
            console.error('自定义上传失败:', error)
          })
      },
      async showEdit(title, row) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getXmpytzDetail({ id: row.id })
          this.tableData = res.data.file
          this.formData = res.data.data
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = false
        } else {
          this.title = '新增'
          this.disabled = true
          let userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData = {
            ...this.formData,
            cjr: userInfo.realname,
            cjsj: new Date().toJSON().split('T')[0],
          }
        }
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.disabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attIds = ''
            if (this.tableData && this.tableData.length > 0) {
              this.tableData.map((item) => {
                attIds += item.attid
                attIds += ','
              })
              attIds = attIds.substring(0, attIds.length - 1)
            }
            let params = { ...this.formData }
            delete params.attachments
            const data = await addOrUpdateXmpytz({
              ...params,
              fileIds: attIds.toString(),
            })
            if (data.code == 200) {
              this.$baseMessage('保存成功', 'success')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            return false
          }
        })
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
          console.log('自定义下载完成')
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
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
      //批量下载示例
      // async handleBatchDownload() {
      //   try {
      //     await handleDowns(this.tableData, this.apiEndpoint, this.downloadMethod);
      //     console.log('所有文件下载完成');
      //   } catch (error) {
      //     console.error('批量下载失败:', error);
      //   }
      // },
      // async handleDown(row) {
      //   try {
      //     // 下载加密文件
      //     const response = await download({ attId: row.attid });
      //     let arrayBuffer;

      //     if (response instanceof Blob) {
      //       arrayBuffer = await response.arrayBuffer();
      //     } else {
      //       arrayBuffer = response;
      //     }

      //     // 将 ArrayBuffer 转换为 Base64 字符串（CryptoJS 需要 Base64 格式）
      //     const base64String = btoa(new Uint8Array(arrayBuffer).reduce((data, byte) => data + String.fromCharCode(byte), ''));

      //     // 进行 AES 解密
      //     const secretKey = 'your-32-byte-secret-key-here'; // 你的AES解密密钥
      //     const iv = 'your-16-byte-iv-here'; // 确保是16字节

      //     const decryptedData = CryptoJS.AES.decrypt(
      //       { ciphertext: CryptoJS.enc.Base64.parse(base64String) },
      //       CryptoJS.enc.Utf8.parse(secretKey),
      //       {
      //         iv: CryptoJS.enc.Utf8.parse(iv),
      //         mode: CryptoJS.mode.CBC,
      //         padding: CryptoJS.pad.Pkcs7
      //       }
      //     );

      //     // 检查解密后的数据是否为空或无效
      //     if (!decryptedData || decryptedData.words.length === 0) {
      //       throw new Error('解密后数据为空');
      //     }

      //     // 将解密后的 WordArray 转换为 Uint8Array
      //     const uint8Array = new Uint8Array(decryptedData.words.map(word =>
      //       [(word >>> 24) & 0xff, (word >>> 16) & 0xff, (word >>> 8) & 0xff, word & 0xff]
      //     ).flat());

      //     // 将 Uint8Array 转换为 Blob
      //     const decryptedBlob = new Blob([uint8Array], { type: 'application/octet-stream' }); // 根据实际文件类型调整 MIME 类型

      //     // 创建并触发下载链接的辅助函数
      //     function createAndTriggerDownloadLink(blob, filename) {
      //       const url = window.URL.createObjectURL(blob);
      //       const link = document.createElement('a');
      //       link.style.display = 'none';
      //       link.href = url;
      //       link.setAttribute('download', filename);
      //       document.body.appendChild(link); // 添加到 body 中以确保触发点击事件
      //       link.click();
      //       document.body.removeChild(link);
      //       window.URL.revokeObjectURL(url);
      //     }

      //     // 更新文件名，移除 .enc 扩展名
      //     let originalFilename = row.attname.replace('.enc', '');

      //     // 创建下载链接并触发下载
      //     createAndTriggerDownloadLink(decryptedBlob, originalFilename);

      //   } catch (error) {
      //     console.error('处理下载时出错:', error);
      //     throw error;
      //   }
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
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.code == 200) {
          // let list = this.tableData
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
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
