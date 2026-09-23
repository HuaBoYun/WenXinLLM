<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="600px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="120px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="24">
          <el-form-item label="关联数据库" prop="dataBaseIdName">
            <el-input
              v-model="formData.dataBaseIdName"
              :style="{ width: '100%' }"
              disabled
              placeholder="请选择关联数据库"
            />
            <!-- <el-button
              style="margin-left: 10px"
              type="primary"
              @click="openModal()"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="title != '新增'">
          <el-form-item label="状态" prop="state">
            <el-input
              v-model="formData.state"
              :style="{ width: '100%' }"
              disabled
              placeholder="请输入导入生成表名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Excel导入" prop="dataBasePassWord">
            <div style="display: flex">
              <el-button type="text" v-if="tableData">
                {{ tableData.attname }}
              </el-button>
              <el-upload
                style="margin-left: 10px"
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-success="handleSuccess"
                :file-list="tableData"
              >
                <el-button type="text">上传</el-button>
              </el-upload>
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>

        <el-col :span="24">
          <el-form-item label="工作副本名校验" prop="tableNameEn">
            <el-input
              v-model="tableNameEn"
              :style="{ width: '80%' }"
              placeholder="请输入副本名"
            />
            <el-button
              style="margin-left: 10px"
              type="primary"
              @click="submitName"
            >
              校验
            </el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <SelectDataBase ref="modal" @getData="getData"></SelectDataBase>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { getPrivewAttInfo } from '@/oapi/contract/manage'
  import SelectDataBase from './selectDataBaseModal.vue'
  import {
    editExcelExport,
    getExcelExportDefaultInfo,
    checkName,
  } from '@/oapi/setting/org'
  const { baseURL } = require('@/config')
  import store from '@/store'
  export default {
    components: { SelectDataBase },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        formData: {
          dataBaseId: '',
          dataBaseIdName: '',
          state: '',
        },
        footer: true,
        dialogFormVisible: false,
        title: '新增',
        baseApi: baseURL,
        api: `/audit/model/excel/analysis/upload/${this.keyId}`,
        headers: {
          token: store.getters['user/token'],
        },
        tableData: {},
        keyId: '',
        status: ['', '进行中', '已完成', '失败'],
        tableNameEn: '',
        databaseConnectionAddress: '',
      }
    },
    created() {},
    mounted() {},
    methods: {
      async showEdit(row, title, info) {
        this.dialogFormVisible = true
        if (row) {
          this.getDD(row)
        }

        this.api = `/audit/model/excel/analysis/upload/${info.id}`
        this.formData.dataBaseIdName = info.dataBaseUsers
        this.formData.dataBaseId = info.id
        this.databaseConnectionAddress = info.dataBaseConnectionAddress
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
        }
      },
      async getDD(row) {
        const res = await getExcelExportDefaultInfo({ id: row.id })
        this.formData.dataBaseId = res.data.auditModelExcel.dataBaseId
        this.formData.state = this.status[+res.data.auditModelExcel.state]
        this.tableData = res.data.tblAttachmentEntity
      },
      close() {
        this.formData = {
          dataBaseId: '',
          state: '',
          dataBaseIdName: '',
        }
        this.tableData = {}
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.close()
        this.$emit('fetchData')
        // this.$refs['ruleForm'].validate(async (valid) => {
        //   if (valid) {
        //     const params = JSON.parse(JSON.stringify(this.formData))
        //     const res = await editExcelExport(params)
        //     if (res && res.code == 200) {
        //       this.close()
        //       this.$emit('fetchData')
        //       this.$message({
        //         message: '提交成功！',
        //         type: 'success',
        //       })
        //     } else {
        //       this.$message({
        //         message: '提交失败',
        //         type: 'error',
        //       })
        //     }
        //   }
        // })
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      openModal() {
        this.$refs['modal'].showEdit()
      },
      getData(data) {
        this.formData.dataBaseId = data[0].id
      },
      handleSuccess(file) {
        if (file.code == '200') {
          this.tableData = file.data
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      submitName() {
        checkName({
          tableNameEn: this.tableNameEn,
          databaseConnectionAddress: this.databaseConnectionAddress,
        }).then((res) => {
          if (res.code == 200) {
            this.$baseMessage('校验成功', 'success')
          } else {
            this.$baseMessage(res.msg, 'error')
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
