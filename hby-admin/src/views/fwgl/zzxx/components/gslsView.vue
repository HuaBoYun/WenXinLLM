<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-col :span="24">
        <el-divider>基本信息</el-divider>
      </el-col>
      <el-form ref="ruleForm" label-width="100px" :model="formData" size="mini">
        <el-col :span="8">
          <el-form-item label="姓名" prop="lawyerName">
            <el-input
              v-model="formData.lawyerName"
              clearable
              :disabled="!footer"
              placeholder="姓名"
              :style="{ width: '100%' }"
            />
          </el-form-item>

          <el-form-item label="年龄" prop="age">
            <el-input
              v-model="formData.age"
              clearable
              :disabled="!footer"
              placeholder="年龄"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="身份证号" prop="identityCard">
            <el-input
              v-model="formData.identityCard"
              clearable
              :disabled="!footer"
              placeholder="身份证号"
              :style="{ width: '100%' }"
            />
          </el-form-item>

          <el-form-item label="资格证书号" prop="certificationNumber">
            <el-input
              v-model="formData.certificationNumber"
              clearable
              :disabled="!footer"
              placeholder="律师资格或法律职业资格证书号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="毕业学校" prop="schooloFgraduation">
            <el-input
              v-model="formData.schooloFgraduation"
              clearable
              :disabled="!footer"
              placeholder="毕业学校"
              :style="{ width: '100%' }"
            />
          </el-form-item>

          <el-form-item label="专业" prop="specialty">
            <el-input
              v-model="formData.specialty"
              clearable
              :disabled="!footer"
              placeholder="专业"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="具体工作部门" prop="workDepartment">
            <el-input
              v-model="formData.workDepartment"
              clearable
              :disabled="!footer"
              placeholder="具体工作部门"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="邮编" prop="postalCode">
            <el-input
              v-model="formData.postalCode"
              clearable
              :disabled="!footer"
              placeholder="邮编"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="性别" prop="sex">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.sex"
              placeholder="性别"
              :disabled="!footer"
            >
              <el-option label="男" value="1" />
              <el-option label="女" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="民族" prop="nation">
            <el-input
              v-model="formData.nation"
              clearable
              :disabled="!footer"
              placeholder="民族"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="政治面貌" prop="politicsStatus">
            <el-input
              v-model="formData.politicsStatus"
              clearable
              :disabled="!footer"
              placeholder="政治面貌"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="住所地址" prop="homeAddress">
            <el-input
              v-model="formData.homeAddress"
              clearable
              :disabled="!footer"
              placeholder="住所地址"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="最高学历" prop="highestEducation">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.highestEducation"
              placeholder="最高学历"
              :disabled="!footer"
            >
              <el-option label="博士研究生" value="1" />
              <el-option label="硕士研究生" value="2" />
              <el-option label="大学本科" value="3" />
              <el-option label="大学专科" value="4" />
              <el-option label="中专" value="5" />
              <el-option label="大学及以下" value="6" />
            </el-select>
          </el-form-item>

          <el-form-item label="现工作单位" prop="nowWorkUnit">
            <el-input
              v-model="formData.nowWorkUnit"
              clearable
              :disabled="!footer"
              placeholder="现工作单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="办公电话" prop="officePhone">
            <el-input
              v-model="formData.officePhone"
              clearable
              :disabled="!footer"
              placeholder="办公电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <!-- <el-form-item label="照片" prop="img">
            <el-upload
              class="avatar-uploader"
              :action="baseApi + api"
              :show-file-list="false"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="formData.photo"
            >
              <img
                v-if="formData.photo"
                :src="baseApi + formData.photo"
                class="avatar"
              />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item> -->
          <el-form-item label="电话" prop="phone">
            <el-input
              v-model="formData.phone"
              clearable
              :disabled="!footer"
              placeholder="电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="技术职务" prop="technicalPosition">
            <el-input
              v-model="formData.technicalPosition"
              clearable
              :disabled="!footer"
              placeholder="取得何种技术职务"
              :style="{ width: '100%' }"
            />
          </el-form-item>

          <el-form-item label="外语水平" prop="foreignLanguageLevel">
            <el-input
              v-model="formData.foreignLanguageLevel"
              clearable
              :disabled="!footer"
              placeholder="掌握何种外语水平"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="受过何种奖励" prop="award">
            <el-input
              v-model="formData.award"
              clearable
              type="textarea"
              rows="2"
              :disabled="!footer"
              placeholder="受过何种奖励"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="受过何种处分" prop="punishment">
            <el-input
              v-model="formData.punishment"
              clearable
              type="textarea"
              rows="2"
              :disabled="!footer"
              placeholder="受过何种处分"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>简历</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button type="success" @click="handleAdd">新建</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData">
            <el-table-column align="center" label="开始时间" prop="stareTime" />
            <el-table-column align="center" label="结束时间" prop="endTime" />
            <el-table-column
              align="center"
              label="在何地何部门(学习)工作"
              prop="oldWorkUnit"
            />
            <el-table-column align="center" label="职务" prop="position" />
            <el-table-column align="center" label="备注" prop="remark" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row)">编辑</el-button>
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
          <el-divider>申请人居民身份证复印件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
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
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>
            律师资格证书或法律职业资格证书（正、副本）复印件
          </el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
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
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>在合同有效期内的劳动合同复印件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
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
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>
            申请人符合《客户C运营公司律师管理办法》第七条第四项规定条件的工作经历、执业经历证明
          </el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
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
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <resumeView ref="resumeView" @addList="addList" />
  </el-dialog>
</template>

<script>
  import {
    addGSLS,
    deleteFile,
    deleteJLList,
    getGSLSDefaultInfo,
    uploadApi,
  } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import store from '@/store'
  import resumeView from './resumeView.vue'
  const token = store.getters['user/token']
  export default {
    components: { resumeView },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          age: '',
          award: '',
          certificationNumber: '',
          foreignLanguageLevel: '',
          highestEducation: '',
          homeAddress: '',
          identityCard: '',
          lawyerName: '',
          nation: '',
          nowWorkUnit: '',
          officePhone: '',
          phone: '',
          photo: '',
          politicsStatus: '',
          postalCode: '',
          punishment: '',
          schooloFgraduation: '',
          sex: '',
          specialty: '',
          technicalPosition: '',
          workDepartment: '',
          fileIds: [],
        },
        dialogFormVisible: false,
        title: '详情',
        footer: true,
        tableData: [],
        fileList: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.fileList = []
        this.tableData = []
        if (row) {
          getGSLSDefaultInfo({
            id: row.practiceApplyId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.lawyer)
            if (res.data.files) {
              this.fileList = res.data.files
            }
            if (res.data.lawyerExt) {
              this.tableData = res.data.lawyerExt
            }
            this.formData.sex = res.data.lawyer.sex.toString()
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData
        }
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['resumeView'].showEdit('add', null)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['resumeView'].showEdit('edit', row)
      },
      addList(value) {
        const aaa = this.tableData.findIndex(
          (x) => x.lawyerExtId == value.lawyerExtId
        )
        if (aaa !== -1) {
          this.tableData.splice(aaa, 1, value)
        } else {
          this.tableData.push(value)
        }
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        if (this.fileList.length !== 0) {
          const bb = []
          this.fileList.forEach((e) => {
            bb.push(e.fileId)
          })
          this.formData.fileIds = bb.toString()
        }
        if (this.tableData.length !== 0) {
          const aa = []
          this.tableData.forEach((e) => {
            aa.push(e.lawyerExtId)
          })
          this.formData.lawyerExtId = aa.toString()
        }
        this.$emit('add', this.formData)
        addGSLS(this.formData).then((res) => {
          if (res.msg == '成功') {
            this.dialogFormVisible = false
            this.$emit('fetchData')
          }
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.footer = true
        this.dialogFormVisible = false
      },
      // 照片上传
      handlePreview() {},
      handleSuccess(res) {
        if (res.code == 200) {
          this.formData.photo = res.data.fileIds[0].filePath
        }
      },
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteJLList({ id: row.lawyerExtId })
          if (code == 200) {
            this.tableData.splice(
              this.tableData.findIndex((x) => x.lawyerExtId == row.lawyerExtId),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      //文件上传
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },

      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
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
  .avatar-uploader .el-upload {
    width: 178px;
    height: 178px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    border: 1px dashed #d9d9d9;
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
