<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-09 18:01:12
 * @FilePath: /hb-admin/src/views/setting/dataGather/sjdr.vue
-->
<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-right-panel style="float: right">
        <el-button
          type="success"
          @click="handleImport"
          v-if="hasAuth('SJDRimport')"
        >
          导入
        </el-button>
        <el-button
          type="primary"
          @click="handleClear"
          v-if="hasAuth('SJDRclear')"
        >
          清空
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <table border="1" cellPadding="16" :style="{ width: '100%' }">
      <thead>
        <tr>
          <th width="55">序号</th>
          <th width="200">表名</th>
          <th width="200">年份</th>
          <th>选择文件</th>
          <th width="200">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>1</td>
          <td>科目表</td>
          <td>
            <el-date-picker
              v-model="formData.accYear"
              clearable
              :style="{ width: '100%' }"
              type="year"
              value-format="yyyy"
            />
          </td>
          <td>{{ formData.accFile && formData.accFile.name }}</td>
          <td>
            <el-button
              type="text"
              @click="handleDownload('科目表')"
              v-if="hasAuth('SJDRdown')"
            >
              下载模板
            </el-button>
            <el-upload
              accept=".xls,.xlsx"
              action="string"
              :auto-upload="false"
              :file-list="accFileList"
              :headers="{
                'X-Requested-With': 'XMLHttpRequest',
                token: token,
              }"
              :http-request="httpRequest"
              :limit="1"
              name="accFile"
              :on-change="OnAccFileChange"
              :on-remove="OnRemove"
              :on-success="handleSuccess"
              :show-file-list="false"
            >
              <el-button type="text" v-if="hasAuth('SJDRselect')">
                选择文件
              </el-button>
            </el-upload>
          </td>
        </tr>
        <tr>
          <td>2</td>
          <td>凭证明细表</td>
          <td>
            <el-date-picker
              v-model="formData.pzYear"
              clearable
              :style="{ width: '100%' }"
              type="year"
              value-format="yyyy"
            />
          </td>
          <td>{{ formData.pzFile && formData.pzFile.name }}</td>
          <td>
            <el-button
              type="text"
              @click="handleDownload('凭证明细表')"
              v-if="hasAuth('SJDRdown')"
            >
              下载模板
            </el-button>
            <el-upload
              accept=".xls,.xlsx"
              action="string"
              :auto-upload="false"
              :file-list="pzFileList"
              :headers="{
                'X-Requested-With': 'XMLHttpRequest',
                token: token,
              }"
              :http-request="httpRequest"
              :limit="1"
              name="pzFile"
              :on-change="OnPzFileChange"
              :on-remove="OnRemove"
              :on-success="handleSuccess"
              :show-file-list="false"
            >
              <el-button type="text" v-if="hasAuth('SJDRselect')">
                选择文件
              </el-button>
            </el-upload>
          </td>
        </tr>
        <tr>
          <td>3</td>
          <td>期初余额表</td>
          <td>
            <el-date-picker
              v-model="formData.balYear"
              clearable
              :style="{ width: '100%' }"
              type="year"
              value-format="yyyy"
            />
          </td>
          <td>{{ formData.balFile && formData.balFile.name }}</td>
          <td>
            <el-button
              type="text"
              @click="handleDownload('期初余额表')"
              v-if="hasAuth('SJDRdown')"
            >
              下载模板
            </el-button>
            <el-upload
              accept=".xls,.xlsx"
              action="string"
              :auto-upload="false"
              :file-list="balFileList"
              :headers="{
                'X-Requested-With': 'XMLHttpRequest',
                token: token,
              }"
              :http-request="httpRequest"
              :limit="1"
              name="balFile"
              :on-change="OnBalFileChange"
              :on-remove="OnRemove"
              :on-success="handleSuccess"
              :show-file-list="false"
            >
              <el-button type="text" v-if="hasAuth('SJDRselect')">
                选择文件
              </el-button>
            </el-upload>
          </td>
        </tr>
        <tr>
          <td>4</td>
          <td>生成财务账套</td>
          <td>
            <el-date-picker
              v-model="formData.bookYear"
              clearable
              :style="{ width: '100%' }"
              type="year"
              value-format="yyyy"
            />
          </td>
          <td></td>
          <td>
            <el-button
              type="text"
              @click="handleGenerate"
              v-if="hasAuth('SJDRgenerate')"
            >
              生成
            </el-button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import { download } from '@/api/setting/dataGather'
  // import FileUpload from '@/components/FileUpload.vue'
  import store from '@/store'
  import axios from 'axios'
  const { baseURL } = require('@/config')
  import { hasAuth } from '@/utils'

  export default {
    name: 'Sjdr',
    // components: { FileUpload },
    data() {
      return {
        baseApi: baseURL,
        token: store.getters['user/token'],
        api: '/setting/adddeploymentzip',
        accFileList: [],
        pzFileList: [],
        balFileList: [],
        formData: {},
      }
    },
    created() {
      this.resetForm()
    },
    methods: {
      OnAccFileChange(file) {
        this.formData.accFile = file
      },
      OnPzFileChange(file) {
        this.formData.pzFile = file
      },
      OnBalFileChange(file) {
        this.formData.balFile = file
      },
      //文件移除
      OnRemove(file, fileList) {
        // this.form.file = fileList
        // this.fileList = fileList
      },
      //覆盖默认上传，自定义方法
      httpRequest() {
        var formdata = new FormData()
        formdata.append('accFile', this.formData.accFile.raw)
        formdata.append('pzFile', this.formData.pzFile.raw)
        formdata.append('balFile', this.formData.pzFile.raw)
        formdata.append('accYear', this.formData.accYear)
        formdata.append('pzYear', this.formData.pzYear)
        formdata.append('balYear', this.formData.balFile)
        axios
          .post(this.baseApi + this.api, formdata, {
            onUploadProgress(progressEvent) {
              if (progressEvent.lengthComputable) {
                let val = (
                  (progressEvent.loaded / progressEvent.total) *
                  100
                ).toFixed(0) //获取百分比
                //progressEvent.loaded 上传到服务器多少秒
                //progressEvent.total 图片总大小
                var percent = parseInt(val)
              }
              //调用文件上传时钩子
              // this.onProgress(percent)
            },
          })
          .then((res) => {
            this.handleSuccess(res.data)
          })
          .catch((err) => {})
      },
      handleSuccess() {},
      async handleDownload(name) {
        const data = await download({ type: 1 })
        let fileName = name
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
      handleImport() {
        this.httpRequest()
      },
      resetForm() {
        this.formData = {
          url: undefined,
          accFile: undefined,
          accYear: undefined,
          pzFile: undefined,
          pzYear: undefined,
          balFile: undefined,
          balYear: undefined,
          bookYear: undefined,
        }
      },
      handleClear() {
        this.resetForm()
      },
      handleGenerate() {},
    },
  }
</script>
<style scoped>
  .font-size-16 {
    font-size: 20px;
    padding: 4px;
  }
  .wraper {
    display: flex;
  }
  .el-upload {
    display: inline-block;
  }
  table {
    border-collapse: collapse;
    border: 1px solid #dcdfe6;
  }
  th,
  td {
    border: 1px solid #dcdfe6;
  }
</style>
