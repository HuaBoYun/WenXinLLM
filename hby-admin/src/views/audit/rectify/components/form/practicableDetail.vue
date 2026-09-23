<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="110px"
        :model="formData"
        :rules="rules"
        disabled
        size="mini"
      >
        <el-col :span="24">
          <el-divider>整改落实</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题编号" prop="code">
            <el-input
              v-model="formData.code"
              clearable
              placeholder="请输入底稿编号"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="company">
            <el-input
              v-model="formData.company"
              clearable
              placeholder="请输入底稿名称"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="问题来源" prop="source">
            <el-input
              v-model="formData.source"
              clearable
              placeholder="请输入审计目标"
              :style="{ width: '256px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现人" prop="discoverer">
            <el-input
              v-model="formData.discoverer"
              clearable
              placeholder="请输入审计事项描述"
              disabled
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题详情" prop="details">
            <el-input
              v-model="formData.details"
              clearable
              placeholder="请输入审计对象"
              disabled
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="审计发现" prop="discoverer">
            <el-input
              v-model="formData.discoverer"
              clearable
              placeholder="请输入审计发现"
              type="textarea"
              rows="4"
              disabled
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item label="审计建议" prop="threason">
            <el-input
              v-model="formData.threason"
              clearable
              placeholder="请输入审计建议"
              type="textarea"
              rows="4"
              disabled
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item label="备注" prop="memo">
            <el-input v-model="formData.memo" clearable placeholder="请输入备注" type="textarea" rows="4" disabled />
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <el-divider>填写整改信息</el-divider>
        </el-col>

        <el-col :span="24">
          <el-form-item label="整改措施" prop="reformmeasure">
            <el-input
              v-model="formData.reformmeasure"
              clearable
              placeholder="请输入整改措施"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="整改落实情况" prop="reformcarryout">
            <el-input
              v-model="formData.reformcarryout"
              clearable
              placeholder="请输入整改落实情况"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="责任人处理情况" prop="handling">
            <el-input
              v-model="formData.handling"
              clearable
              placeholder="请输入责任人处理情况"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="完成时间" prop="reformdeadline">
            <el-date-picker
              v-model="formData.reformdeadline"
              placeholder="选择完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="整改结论" prop="reformresult">
            <el-select
              v-model="formData.reformresult"
              placeholder="请选择整改结论"
              clearable
            >
              <el-option label="未整改" value="未整改" />
              <el-option label="已整改未到位" value="已整改未到位" />
              <el-option label="已整改到位" value="已整改到位" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.reformresult != '已整改到位'">
          <el-form-item label="下一步整改措施" prop="nextmeasures">
            <el-input
              v-model="formData.nextmeasures"
              clearable
              placeholder="请输入下一步整改措施"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.reformresult != '已整改到位'">
          <el-form-item label="计划完成整改时间" prop="nextmplancomdate">
            <el-date-picker
              v-model="formData.nextmplancomdate"
              placeholder="选择计划完成整改时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <!-- <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div> -->
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
                <!-- <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template> -->
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import {
    getReformAttInfo,
    saveReformInfo,
    delRefromAttInfo,
  } from '@/api/audit/rectify'
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
          code: undefined,
          company: undefined,
          source: undefined,
          details: undefined,
          discoverer: undefined,
          threason: undefined,
          memo: undefined,
          reformmeasure: undefined,
          reformcarryout: undefined,
          handling: undefined,
          reformdeadline: undefined,
          reformresult: undefined,
          nextmeasures: undefined,
          nextmplancomdate: undefined,
        },
        footer: true,
        tableData: [],
        rules: {
          reformcarryout: [
            {
              required: true,
              message: '请输入整改落实情况',
              trigger: 'blur',
            },
          ],
          handling: [
            {
              required: true,
              message: '请输入责任人处理情况',
              trigger: 'blur',
            },
          ],
          reformdeadline: [
            {
              required: true,
              message: '选择完成时间',
              trigger: 'blur',
            },
          ],
          reformresult: [
            {
              required: true,
              message: '请输入整改结论',
              trigger: 'blur',
            },
          ],
          nextmeasures: [
            {
              required: true,
              message: '请输入下一步整改措施',
              trigger: 'blur',
            },
          ],
          nextmplancomdate: [
            {
              required: true,
              message: '选择计划完成整改时间',
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
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          const data = row.reform
          this.formData = {
            ...data,
          }
          this.getFileList(row.reform.reformid)
        }
        // else {
        //   const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        //   this.formData.realname = userInfo.realname
        // }
      },
      async getFileList(reformid) {
        const data = await getReformAttInfo({ reformid })
        if (data.code == '1') {
          this.tableData = data.data || []
        } else {
          this.tableData = []
        }
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs.elForm.validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const { reformdeadline, nextmplancomdate, ...other } = this.formData
            const data = await saveReformInfo({
              attids,
              linedate: reformdeadline,
              plancomdate: nextmplancomdate,
              ...other,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            console.log('error submit!!')
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        await delRefromAttInfo({ attid: row.attid })
        this.$baseMessage('删除成功', 'success')
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
