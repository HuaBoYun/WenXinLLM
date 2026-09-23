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
          <el-form-item label="对方谈判人" prop="counterpart">
            <el-input
              v-model="form.counterpart"
              clearable
              placeholder="请输入对方谈判人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方谈判人联系电话">
            <el-input
              v-model="form.counterpartphone"
              clearable
              placeholder="请输入对方谈判人联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否协商一致">
            <el-radio-group v-model="form.isaggree">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isaggree == 1" :span="12">
          <el-form-item label="解决方式">
            <el-radio-group v-model="form.solutionmode">
              <el-radio :label="1">私下调解</el-radio>
              <el-radio :label="2">司法调解</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isaggree == 2" :span="12">
          <el-form-item label="司法解决">
            <el-radio-group v-model="form.judicialsettlement">
              <el-radio :label="1">诉讼</el-radio>
              <el-radio :label="2">仲裁</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="隶属纠纷" prop="disputeitem">
            <el-input
              v-model.trim="form.disputeitem"
              clearable
              placeholder="请选择隶属纠纷"
              readonly
              :style="{ width: '256px' }"
              @input="change()"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.xzjf.show(0)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷合同">
            <el-input
              v-model="form.contractname"
              clearable
              placeholder="请输入纠纷合同"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="谈判结果">
            <el-input
              v-model="form.negetiaresult"
              clearable
              placeholder="请输入谈判结果"
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
        <el-divider>协商过程信息</el-divider>
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
          <el-table-column
            align="center"
            label="谈判时间"
            prop="negotiationtime"
          />
          <el-table-column
            align="center"
            label="对方谈判人"
            prop="recordcounterpart"
          />
          <el-table-column align="center" label="我方谈判人" prop="username" />
          <el-table-column
            align="center"
            label="谈判方式"
            prop="negotiationmode"
          />
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
          <el-button v-if="!form.negotiaid" type="success" @click="hold()">
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
          <el-form-item label="对方谈判人" prop="counterpart">
            <span>{{ form.counterpart }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方谈判人联系电话">
            <span>{{ form.counterpartphone }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否协商一致">
            <span>{{ form.isaggree == 1 ? '协商一致' : '协商不一致' }}</span>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isaggree == 1" :span="12">
          <el-form-item label="解决方式">
            <span>
              {{ form.solutionmode === 1 ? '私下调解' : '司法调解' }}
            </span>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isaggree == 2" :span="12">
          <el-form-item label="司法解决">
            <span>
              {{ form.judicialsettlement == 1 ? '诉讼' : '仲裁' }}
            </span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="隶属纠纷" prop="disputeitem">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷合同">
            <span>{{ form.contractname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="谈判结果">
            <span>{{ form.negetiaresult }}</span>
          </el-form-item>
        </el-col>
      </el-form>
      <el-col :span="24">
        <el-divider>协商过程信息</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column
            align="center"
            label="谈判时间"
            prop="negotiationtime"
          />
          <el-table-column
            align="center"
            label="对方谈判人"
            prop="recordcounterpart"
          />
          <el-table-column align="center" label="我方谈判人" prop="username" />
          <el-table-column
            align="center"
            label="谈判方式"
            prop="negotiationmode"
          />
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
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
    </template>
    <xzjf-options ref="xzjf" @selected="handleSsjd" />
    <consultEdit ref="editadd" @editadd="handleeditadd" />
  </el-dialog>
</template>

<script>
  import {
    negotiatedSettlemenSave,
    negotiateRecord,
    removeNegotiatedRecord,
    negotiatedSettlemenModify,
    findAttacheMent,
    deleAttacheMent,
  } from '@/api/contract/legal'
  import xzjfOptions from './options/xzjf.vue'
  import consultEdit from './smEdit/consultEdit2.vue'
  import store from '@/store'
  import { baseURL } from '@/config/net.config'
  export default {
    name: 'DraftEdit',
    components: { xzjfOptions, consultEdit },
    data() {
      return {
        form: {
          negotiaid: undefined,
          counterpart: undefined,
          counterpartphone: undefined,
          isaggree: 1,
          solutionmode: 1,
          disputeitem: undefined,
          negetiaresult: undefined,
          disputeId: undefined,
          attids: undefined,
          contractname: undefined,
          judicialsettlement: 1,
        },
        rules: {
          counterpart: [
            {
              required: true,
              message: '请输入对方谈判人',
              trigger: 'blur',
            },
          ],
          disputeitem: [
            {
              required: true,
              message: '请输入隶属纠纷',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '698869',
        },
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
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
          type: 2,
          bid: this.form.negotiaid,
        }
      },
    },
    created() {},
    methods: {
      hold() {
        this.$message.error('请先保存基本信息!')
      },
      change() {
        this.$forceUpdate()
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
          this.title = '添加'
          this.listLoading = false
        } else {
          this.listLoading = true
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = '编辑'
          }
          Object.keys(this.form).forEach((key) => (this.form[key] = row[key]))
          this.netotiaId = row.negotiaid
          this.form.disputeId = row.disputeid
          this.form.negotiaid = row.negotiaid
          this.uploadList()
          this.getEditList()
        }
        this.dialogFormVisible = true
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
            const bCreate = !this.form.negotiaid
            const func = bCreate
              ? negotiatedSettlemenSave
              : negotiatedSettlemenModify
            const msg = bCreate ? '新增成功' : '修改成功'
            func(this.form).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.negotiaid = res.data
                }
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              }
            })
            this.$emit('fetch-data')
            // this.close()
          }
        })
      },
      showEditAdd(row) {
        // this.$refs['editadd'].showEdit(row)
        if (!this.form.negotiaid) {
          this.$baseMessage(
            '请先保存基本信息',
            'success',
            'vab-hey-message-success'
          )
          return
        }
        this.$refs['editadd'].showEdit({
          netotiaId: this.form.negotiaid,
          ...row,
        })
      },
      //附件列表
      async uploadList() {
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 2,
          bid: this.form.negotiaid,
        })
        this.uploadlist = data
        this.$refs.upload.clearFiles()
        this.$refs.upload.uploadFiles.length = 0
        // this.listLoading = false
      },
      //附件删除
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 2 })
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
      //上传前的校验
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
        }
        return isLt2M
      },
      handleRemove(file, fileList) {},
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeNegotiatedRecord({
            recordId: row.recordid,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.getEditList()
        })
      },
      //回调
      handleSsjd(val) {
        this.form.disputeId = val.disputeid
        this.form.disputeitem = val.disputeitem
        this.form.contractname = val.contractname
      },
      handleeditadd() {
        this.getEditList()
      },
      //请求数据
      async getEditList() {
        this.listLoading = true
        this.queryForm.negotiaId = this.form.negotiaid
        const {
          date: { tlist, totalRecord },
        } = await negotiateRecord(this.queryForm)
        this.list = tlist.map((i) => {
          return {
            ...i,
            username: i.wFTPR,
            realname: i.lRR,
          }
        })

        this.total = totalRecord
        this.listLoading = false
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
