<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-form-item label="名称" prop="auditName">
            <el-input
              v-model="formData.auditName"
              clearable
              placeholder="请输入名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起草人" prop="creatorName">
            <el-input
              v-model="formData.creatorName"
              clearable
              placeholder="请输入起草人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起草部门" prop="workUnitName">
            <el-input
              v-model="formData.workUnitName"
              clearable
              placeholder="请输入起草部门"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="起草意见" prop="opinionName">
            <el-input
              v-model="formData.opinionName"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入起草意见"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="各部室及所属公司" prop="auditBelongGroupName">
            <el-input
              v-model.trim="formData.auditBelongGroupName"
              :style="{ width: '756px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.companyTree.show(1, null)"
              :disabled="!footer"
            >
              选择
            </el-button>
            <el-checkbox
              v-if="formData.auditBelongGroupName"
              v-model="formData.tzssgs"
              :disabled="!footer"
            >
              通知所属公司
            </el-checkbox>
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <el-divider>制度</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData2">
            <el-table-column
              align="center"
              label="序号"
              prop="institutionAuditExtId"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.institutionAuditExtId }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="制度名称"
              prop="institutionName"
            />
            <el-table-column
              align="center"
              label="制度分类"
              prop="institutionType"
            >
              <template slot-scope="scope">
                <span>
                  {{
                    scope.row.institutionType == '1'
                      ? '经营管理类-基本制度'
                      : scope.row.institutionType == '2'
                      ? '经营管理类-重要制度'
                      : scope.row.institutionType == '3'
                      ? '经营管理类-一般制度'
                      : scope.row.institutionType == '4'
                      ? '非经营管理类'
                      : ''
                  }}
                </span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="备注" prop="remark" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)" v-if="footer">
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete1(row)"
                  v-if="footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-form-item
            label="起草部门合规管理员意见"
            prop="draftAdministratorOpinion"
          >
            <el-input
              v-model="formData.draftAdministratorOpinion"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入起草部门合规管理员意见"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="起草部门意见" prop="draftDepartmentOpinion">
            <el-input
              v-model="formData.draftDepartmentOpinion"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入起草部门意见"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="所属公司审批意见" prop="companyGroupOpinion">
            <el-input
              v-model="formData.companyGroupOpinion"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入所属公司审批意见"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="各部室审批意见"
            prop="departmentBelongGroupOpinion"
          >
            <el-input
              v-model="formData.departmentBelongGroupOpinion"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入各部室审批意见"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item
            label="起草部门意见吸收情况"
            prop="draftDepartmentOpinionSuck"
          >
            <el-input
              v-model="formData.draftDepartmentOpinionSuck"
              clearable
              type="textarea"
              rows="3"
              placeholder="请输入起草部门意见吸收情况"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseURL + uploadApi"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
            >
              <template #default="{ row }">
                <div>
                  {{ row.fileSize / 1000 }}
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button
        v-if="
          (formData.state == 2 || formData.state == 3) && jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </div>

    <company-tree-model
      ref="companyTree"
      @selected="handleCompanyTreeSelected"
    />
    <zdView ref="zdView" @refush="setTableData2" />
    <!-- 提交 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </el-dialog>
</template>

<script>
  import { fetchApi, legalReviewInstitution, uploadApi } from '@/api/fwgl/api'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import { download } from '@/api/fwgl/zzxx'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import { baseURL } from '@/config'
  import store from '@/store'
  import zdView from './zdView'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  const token = store.getters['user/token']
  const { saveOrUpdate, detail, doZJDelete } = legalReviewInstitution
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    components: {
      CompanyTreeModel,
      zdView,
      CandidateUserSelect,
      Resubmit,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: uploadApi,
        headers: { token: token },
        tableData: [],
        tableData2: [],
        formData: {
          creatorName: '',
          staffid: '',
          workUnitName: '',
          belongGroup: '',
          institutionAuditId: '',
        },
        footer: true,
        rules: {
          auditName: [
            {
              required: true,
              message: '请输入名称',
              trigger: 'blur',
            },
          ],
          creatorName: [
            {
              required: true,
              message: '请输入起草人',
              trigger: 'blur',
            },
          ],
          workUnitName: [
            {
              required: true,
              message: '请输入起草部门',
              trigger: 'blur',
            },
          ],
          opinionName: [
            {
              required: true,
              message: '请输入意见',
              trigger: 'blur',
            },
          ],
          auditBelongGroupName: [
            {
              required: true,
              message: '请选择入各部室及所属公司',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        //提交
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      deWeight(arr) {
        for (var i = 0; i < arr.length - 1; i++) {
          for (var j = i + 1; j < arr.length; j++) {
            if (arr[i].institutionAuditExtId == arr[j].institutionAuditExtId) {
              arr.splice(j, 1)
              //因为数组长度减小1，所以直接 j++ 会漏掉一个元素，所以要 j--
              j--
            }
          }
        }
        return arr
      },
      setTableData2(list) {
        //
        // this.tableData2 = []
        let list2 = [...list, ...this.tableData2]
        this.tableData2 = this.deWeight(list2)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        this.$refs.zdView.showEdit('detail', row)
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs.zdView.showEdit('add', null)
      },
      handleEdit(row) {
        this.$refs.zdView.showEdit('edit', row)
      },
      async handleDelete1(row) {
        this.tableData2 = this.tableData2.filter(
          (item) => item.institutionAuditExtId !== row.institutionAuditExtId
        )
        const res = await fetchApi(doZJDelete, {
          id: row.institutionAuditExtId,
        })
        this.$message({ message: '删除成功', type: 'success' })
      },
      handleCompanyTreeSelected(node) {
        if (node instanceof Array) {
          const { auditBelongGroupName, auditBelongGroupId, ...other } =
            this.formData
          let formData = {
            auditBelongGroupName: node.map((item) => item.name).join(','),
            auditBelongGroupId: node.map((item) => item.id).join(','),
            ...other,
          }
          this.formData = formData
        } else {
          const { oid2, org2, ...other } = this.formData
          let formData = { oid2: node.id, org2: node.name, ...other }
          this.formData = formData
        }
      },
      handlePreview() {},
      handleSuccess(res, b, c) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
        }
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ fileId: row.fileId })
        let filename = row.fileName
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 类型
       * @param {*} row 行数据
       * @return {*}
       */      
      async showEdit(title, row) {
        this.tableData2 = []
        this.tableData = []
        this.dialogFormVisible = true
        this.formData = {
          creatorName: '',
          staffid: '',
          workUnitName: '',
          belongGroup: '',
        }
        if (row) {
          this.loading = true
          const res = await fetchApi(detail, { id: row.id })

          if (res && res.code === 200 && res.data) {
            this.formData = res.data.institutionAudit
            this.tableData = res.data.files || []
            if (res.data.institutionAuditExtList) {
              this.tableData2 = res.data.institutionAuditExtList
            }
            if (
              res.data.institutionAudit.state == 2 ||
              res.data.institutionAudit.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 18,
                formId: row.id,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.id + ''
                this.fromIdcopy = row.id + ''
                this.ymFromId = res2.data.id + ''

                const res3 = await getFaqiInfo({
                  id: res2.data.id,
                  flowId: res2.data.flowId,
                })
                if (res3.code == 1) {
                  this.status = res3.data.dataJson.flowTaskInfo.status
                }
              }
            }
            this.loading = false
          }
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.creatorName = userInfo.realname
          this.formData.staffid = userInfo.staffid
          this.formData.workUnitName = userInfo.linkDetp.orgname
          this.formData.belongGroup = userInfo.linkDetp.orgid
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.tableData = []
        this.clearType = true
        this.$emit('fetch-data')
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        this.loading = true
        this.formData.fileIds = this.tableData.map((x) => x.fileId).join(',')
        this.formData.institutionAuditExtId = this.tableData2
          .map((x) => x.institutionAuditExtId)
          .join(',')
        this.formData.auditType = 1
        this.loading = false
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await fetchApi(saveOrUpdate, this.formData)

            if (res && res.code === 200) {
              this.$message({ message: '保存成功', type: 'success' })
              this.formData = res.data
              this.close()
            } else {
              this.$message({ message: res.message, type: 'error' })
            }
          } else {
            return false
          }
        })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */      
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
      /**
       * @description: 引迈流程提交
       * @return {*}
       */      
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
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
