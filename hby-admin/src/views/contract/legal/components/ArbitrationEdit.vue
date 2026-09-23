<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row v-if="show == 0" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="案件名称" prop="courtfirst">
            <el-input
              v-model="form.courtfirst"
              clearable
              placeholder="请输入案件名称"
              :style="{ width: '100%', height: '30px' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁金额">
            <el-input-number
              v-model="form.arbitrationamount"
              step-strictly
              min="0"
              placeholder="请输入仲裁金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁受理日期">
            <el-date-picker
              v-model="form.asdealdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入仲裁受理日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁首次开庭日期">
            <el-date-picker
              v-model="form.asfirsthearingdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入仲裁首次开庭日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="协商信息">
            <el-input
              v-model="form.disputeitem"
              clearable
              placeholder="请选择协商信息"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.xzss.show(2)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁结案日期">
            <el-date-picker
              v-model="form.arbitrationenddate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入仲裁结案日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="仲裁结果">
            <el-input
              v-model="form.arbitrationresult"
              clearable
              placeholder="请输入仲裁结果"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
      <!--      <el-col :span="24">-->
      <!--        <div style="text-align: right; margin-bottom: 5px">-->
      <!--          <el-button type="primary" @click="save">保 存</el-button>-->
      <!--        </div>-->
      <!--      </el-col>-->
      <el-col :span="24">
        <el-divider>仲裁过程信息</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button type="success" @click="showEditAdd">新增</el-button>
        </div>
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column align="center" label="仲裁阶段" prop="arstage" />
          <el-table-column
            align="center"
            label="仲裁机构联系人"
            prop="arcontactperson"
          />
          <el-table-column
            align="center"
            label="我方代理人"
            prop="ourcontractperson"
          />
          <el-table-column align="center" label="日期" prop="dealdate" />
          <el-table-column align="center" label="录入人" prop="realname" />
          <el-table-column align="center" label="录入时间" prop="createtime" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="showEditAdd(row)">修改</el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button v-if="!form.arbitraid" type="success" @click="hold()">
            上传
          </el-button>
          <el-upload
            v-else
            ref="upload"
            :accept="accept"
            :action="baseApi + api"
            :before-upload="handleBeforeUpload"
            :data="uploadData"
            :file-list="fileList"
            :headers="headers"
            :limit="1"
            :on-error="onError"
            :on-remove="handleRemove"
            :on-success="onSuccess"
          >
            <el-button type="success">上传</el-button>
          </el-upload>
        </div>
        <el-table :data="uploadlist">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="downloadData(row)">下载</el-button>
              <el-button type="text" @click="handleDelete1(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="案件名称" prop="courtfirst">
            <span>{{ form.courtfirst }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁金额">
            <span>{{ form.arbitrationamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁受理日期">
            <span>{{ form.asdealdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁首次开庭日期">
            <span>{{ form.asfirsthearingdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="协商信息">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁结案日期">
            <span>{{ form.arbitrationenddate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="仲裁结果">
            <span>{{ form.arbitrationresult }}</span>
          </el-form-item>
        </el-col>
      </el-form>
      <el-col :span="24">
        <el-divider>仲裁过程信息</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column align="center" label="仲裁阶段" prop="arstage" />
          <el-table-column
            align="center"
            label="仲裁机构联系人"
            prop="arcontactperson"
          />
          <el-table-column
            align="center"
            label="我方代理人"
            prop="ourcontractperson"
          />
          <el-table-column align="center" label="日期" prop="dealdate" />
          <el-table-column align="center" label="录入人" prop="realname" />
          <el-table-column align="center" label="录入时间" prop="createtime" />
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table :data="uploadlist">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="downloadData(row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show === 0" type="primary" @click="save">
        确 定
      </el-button>
    </template>
    <xzss-options ref="xzss" @selected="handleSsjd" />
    <arbitrationEdit ref="editadd" @editadd="handleeditadd" />
  </el-dialog>
</template>

<script>
  import {
    ArbitratSettlementSave,
    ArbitrationRecord,
    ArbitratSettlementModify,
    findAttacheMent,
    deleAttacheMent,
    removeArbitrationRecord,
  } from '@/api/contract/legal'
  import store from '@/store'
  import xzssOptions from './options/xzss.vue'
  import arbitrationEdit from './smEdit/arbitrationEdit2.vue'
  import { baseURL } from '@/config/net.config'
  export default {
    name: 'DraftEdit',
    components: { xzssOptions, arbitrationEdit },
    data() {
      return {
        form: {
          arbitraid: undefined,
          courtfirst: undefined,
          arbitrationamount: undefined,
          asdealdate: undefined,
          asfirsthearingdate: undefined,
          disputeitem: undefined,
          arbitrationenddate: undefined,
          arbitrationresult: undefined,
          negotiaId: undefined,
        },
        rules: {
          courtfirst: [
            {
              required: true,
              message: '请输入案件名称',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '698874',
        },
        show: 0,
        accept: '.pdf, .doc, .docx, .xls, .xlsx',
        api: '/contract/uploadFileAttInfo',
        // data: {
        //   attpath: '1649657363411.xlsx',
        //   attname: '1649657363411.xlsx',
        //   token: store.getters['user/token'],
        // },
        headers: {
          token: store.getters['user/token'],
          // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        uploadlist: [],
        value: {},
        beforeUpload: null,
        baseApi: baseURL,
        fileList: [],
      }
    },
    computed: {
      uploadData() {
        return {
          type: 4,
          bid: this.form.arbitraid,
        }
      },
    },
    created() {},
    methods: {
      hold() {
        this.$message.error('请先保存基本信息!')
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getEditList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getEditList()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.getEditList()
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      showEdit(row, disabled) {
        this.show = 0
        if (!row) {
          this.listLoading = false
          this.title = '添加'
        } else {
          this.listLoading = true
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = '编辑'
          }
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          this.form.negotiaid = row.negotiateinfo
          this.form.arbitraid = row.arbitraid
          this.uploadList()
          this.getEditList()
        }
        this.dialogFormVisible = true
      },
      //附件列表
      async uploadList() {
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 4,
          bid: this.form.arbitraid,
        })
        this.uploadlist = data
        this.$refs.upload.clearFiles()
        this.$refs.upload.uploadFiles.length = 0
        // this.listLoading = false
      },
      //附件删除
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 4 })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.uploadList()
        })
      },
      // 下载数据
      downloadData(row) {
        const fileName = row.attname
        const link = document.createElement('a')
        link.download = fileName
        link.href = this.baseApi + '/download?id=' + row.attid
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      //上传前校验
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleRemove(file, fileList) {},
      //回调
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.list = []
        this.uploadlist = []
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const bCreate = !this.form.arbitraid
            const func = bCreate
              ? ArbitratSettlementSave
              : ArbitratSettlementModify
            const msg = bCreate ? '新增成功' : '修改成功'
            const tempData = Object.assign({}, this.form)
            if (!bCreate) {
              delete tempData.d
            }
            func(this.form).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.arbitraid = res.data
                }
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              }
            })
            this.$emit('fetch-data')
          }
        })
      },
      //回调
      handleSsjd(val) {
        this.form.negotiaId = val.negotiaid
        this.form.disputeitem = val.disputeitem
      },
      handleeditadd() {
        this.getEditList()
      },
      //请求数据
      async getEditList() {
        this.listLoading = true
        this.queryForm.arrecordid = this.form.arbitraid
        const {
          data: { tlist, totalRecord },
        } = await ArbitrationRecord(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeArbitrationRecord({
            arrecordId: row.arrecordid,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.getEditList()
        })
      },
      showEditAdd(row) {
        // this.$refs['editadd'].showEdit(row)
        if (!this.form.arbitraid) {
          // this.$baseMessage(
          //   '请先保存基本信息',
          //   'success',
          //   'vab-hey-message-success'
          // )
          this.$message.error('请先保存基本信息!')
          return
        }
        this.$refs['editadd'].showEdit({
          arbitraId: this.form.arbitraid,
          ...row,
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
