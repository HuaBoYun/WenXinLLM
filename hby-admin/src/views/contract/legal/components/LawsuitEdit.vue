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
          <el-form-item label="案件名称" prop="firstcourt">
            <el-input
              v-model="form.firstcourt"
              clearable
              placeholder="请输入案件名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审判长">
            <el-input
              v-model="form.presidingjudge"
              clearable
              placeholder="请输入审判长"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合议庭组成">
            <el-input
              v-model="form.collegialpanel"
              clearable
              placeholder="请输入合议庭组成"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼受理日期">
            <el-date-picker
              v-model="form.dealdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入诉讼受理日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼标的物">
            <el-input
              v-model="form.actionobject"
              clearable
              placeholder="请输入诉讼标的物"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼金额">
            <el-input
              v-model="form.litigationamount"
              clearable
              placeholder="请输入诉讼金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首次开庭日期">
            <el-date-picker
              v-model="form.firsthearingdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入首次开庭日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼结案日期">
            <el-date-picker
              v-model="form.litigationenddate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入诉讼结案日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="判决金额">
            <el-input
              v-model="form.judgemoney"
              clearable
              placeholder="请输入判决金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="判决是否生效">
            <el-radio-group v-model="form.iseffect">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="隶属纠纷">
            <el-input
              v-model="form.disputeItem"
              clearable
              placeholder="请选择隶属纠纷"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.xzjf.show(2)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="谈判结果">
            <el-input
              v-model="form.litigationresult"
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
        <el-divider>诉讼过程记录</el-divider>
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
          <el-table-column align="center" label="案号" prop="proceedno" />
          <el-table-column
            align="center"
            label="诉讼阶段"
            prop="porceedstage"
          />
          <el-table-column align="center" label="审理法院" prop="court" />
          <el-table-column align="center" label="法院联系人" prop="courtlink" />
          <el-table-column
            align="center"
            label="法院联系方式"
            prop="courtcontact"
          />
          <el-table-column align="center" label="立案时间" prop="filingtime" />
          <el-table-column align="center" label="开庭时间" prop="openingtime" />
          <el-table-column
            align="center"
            label="收到判决时间"
            prop="judgetiem"
          />
          <el-table-column
            align="center"
            label="是否外聘律师"
            prop="isexternallawyer"
          >
            <template #default="{ row }">
              <span>{{ row.isexternallawyer == 1 ? '是' : '否' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="代理人" prop="negotiator" />
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
          <el-button v-if="!form.litigationid" type="success" @click="hold()">
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
          <el-form-item label="案件名称" prop="firstcourt">
            <span>{{ form.firstcourt }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审判长">
            <span>{{ form.presidingjudge }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合议庭组成">
            <span>{{ form.collegialpanel }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼受理日期">
            <span>{{ form.dealdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼标的物">
            <span>{{ form.actionobject }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼金额">
            <span>{{ form.litigationamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首次开庭日期">
            <span>{{ form.firsthearingdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="诉讼结案日期">
            <span>{{ form.litigationenddate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="判决金额">
            <span>{{ form.judgemoney }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="判决是否生效">
            <span>{{ form.iseffect == 1 ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="隶属纠纷">
            <span>{{ form.disputeItem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="谈判结果">
            <span>{{ form.litigationresult }}</span>
          </el-form-item>
        </el-col>
      </el-form>
      <el-col :span="24" />
      <el-col :span="24">
        <el-divider>诉讼过程记录</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column align="center" label="案号" prop="proceedno" />
          <el-table-column
            align="center"
            label="诉讼阶段"
            prop="porceedstage"
          />
          <el-table-column align="center" label="审理法院" prop="court" />
          <el-table-column align="center" label="法院联系人" prop="courtlink" />
          <el-table-column
            align="center"
            label="法院联系方式"
            prop="courtcontact"
          />
          <el-table-column align="center" label="立案时间" prop="filingtime" />
          <el-table-column align="center" label="开庭时间" prop="openingtime" />
          <el-table-column
            align="center"
            label="收到判决时间"
            prop="judgetiem"
          />
          <el-table-column
            align="center"
            label="是否外聘律师"
            prop="isexternallawyer"
          >
            <template #default="{ row }">
              <span>{{ row.isexternallawyer == 1 ? '是' : '否' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="代理人" prop="negotiator" />
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
    <lawsuitEdit ref="editadd" @editadd="handleeditadd" />
  </el-dialog>
</template>

<script>
  import {
    litigationSettlementSave,
    proceedingsRecord,
    removeLegalProceedingsRecord,
    litigationSettlementModify,
    findAttacheMent,
    deleAttacheMent,
  } from '@/api/contract/legal'
  import store from '@/store'
  import xzjfOptions from './options/xzjf.vue'
  import lawsuitEdit from './smEdit/lawsuitEdit2.vue'
  import { baseURL } from '@/config/net.config'
  export default {
    name: 'DraftEdit',
    components: { xzjfOptions, lawsuitEdit },
    data() {
      return {
        form: {
          litigationid: undefined,
          firstcourt: undefined,
          presidingjudge: undefined,
          collegialpanel: undefined,
          shouliDate: undefined,
          actionobject: undefined,
          litigationamount: undefined,
          firsthearingdate: undefined,
          litigationenddate: undefined,
          iseffect: 1,
          judgemoney: undefined,
          disputeItem: undefined,
          litigationresult: undefined,
          disputeId: undefined,
          attids: undefined,
          dealdate: undefined,
        },
        rules: {
          firstcourt: [
            {
              required: true,
              message: '请输入案件名称',
              trigger: 'blur',
            },
          ],
        },
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '698869',
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        litigationId: '',
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
          type: 3,
          bid: this.form.litigationid,
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
          this.form.litigationid = row.litigationid
          this.form.disputeId = row.disputeid
          this.uploadList()
          this.getEditList()
        }
        this.dialogFormVisible = true
      },
      //附件列表
      async uploadList() {
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 3,
          bid: this.form.litigationid,
        })
        this.uploadlist = data
        this.$refs.upload.clearFiles()
        this.$refs.upload.uploadFiles.length = 0
        // this.listLoading = false
      },
      //附件删除
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 3 })
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
      //上传前置校验
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
        }
        return isLt2M
      },
      handleRemove(file, fileList) {},
      //回调
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      //回调
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
            const bCreate = !this.form.litigationid
            const func = bCreate
              ? litigationSettlementSave
              : litigationSettlementModify
            const msg = bCreate ? '新增成功' : '修改成功'
            func(this.form).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.litigationid = res.data
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
        if (!this.form.litigationid) {
          // this.$baseMessage(
          //   '请先保存基本信息',
          //   'success',
          //   'vab-hey-message-success'
          // )
          this.$message.error('请先保存基本信息!')
          return
        }
        this.$refs['editadd'].showEdit({
          litigationId: this.form.litigationid,
          ...row,
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeLegalProceedingsRecord({
            proceedId: row.proceedid,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.getEditList()
        })
      },
      //回调
      handleSsjd(val) {
        this.form.disputeId = val.disputeid
        this.form.disputeItem = val.disputeitem
      },
      handleeditadd() {
        this.getEditList()
      },
      //请求数据
      async getEditList() {
        this.listLoading = true
        this.queryForm.litigationId = this.form.litigationid
        const {
          date: { tlist, totalRecord },
        } = await proceedingsRecord(this.queryForm)
        this.list = tlist
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
