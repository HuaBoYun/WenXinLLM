<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-col :span="24">
        <el-divider>基本信息</el-divider>
      </el-col>
      <el-form
        ref="ruleForm"
        label-width="130px"
        :rules="rules"
        :model="formData"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="人员" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              :placeholder="footer ? '请选择人员' : ''"
              style="width: 85%"
              disabled
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄" prop="age">
            <el-input
              v-model="formData.age"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '年龄' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="identityCard">
            <el-input
              v-model="formData.identityCard"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '身份证号' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资格证书号" prop="certificationNumber">
            <el-input
              v-model="formData.certificationNumber"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '律师资格或法律职业资格证书号' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="公司律师执业证书编号" prop="certificateNumber">
            <el-input
              v-model="formData.certificateNumber"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '律师执业证书编号' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="毕业学校" prop="schoolOfGraduation">
            <el-input
              v-model="formData.schoolOfGraduation"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '毕业学校' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="专业" prop="specialty">
            <el-input
              v-model="formData.specialty"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '专业' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="具体工作部门" prop="workDepartment">
            <el-input
              v-model="formData.workDepartment"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '具体工作部门' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮编" prop="postalCode">
            <el-input
              v-model="formData.postalCode"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '邮编' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <!-- <el-form-item label="姓名" prop="practiceApplyName">
            <el-input
              v-model="formData.practiceApplyName"
              clearable
              :disabled="!footer"
              :placeholder="footer?姓名"
              :style="{ width: '100%' }"
            />
          </el-form-item> -->
        <el-col :span="12">
          <el-form-item label="性别" prop="sex">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.sex"
              :placeholder="footer ? '性别' : ''"
              :disabled="!footer"
            >
              <el-option label="男" value="1" />
              <el-option label="女" value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="民族" prop="nation">
            <el-input
              v-model="formData.nation"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '民族' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="政治面貌" prop="politicsStatus">
            <!-- <el-input
              v-model="formData.politicsStatus"
              clearable
              :disabled="!footer"
              :placeholder="footer?政治面貌"
              :style="{ width: '100%' }"
            /> -->
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.politicsStatus"
              :placeholder="footer ? '政治面貌' : ''"
              :disabled="!footer"
            >
              <el-option label="中共党员" value="中共党员" />
              <el-option label="中共预备党员" value="中共预备党员" />
              <el-option label="共青团员" value="共青团员" />
              <el-option label="无党派人士" value="其他党派" />
              <el-option label="群众" value="群众" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="住所地址" prop="homeAddress">
            <el-input
              v-model="formData.homeAddress"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '住所地址' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最高学历" prop="highestEducation">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.highestEducation"
              :placeholder="footer ? '最高学历' : ''"
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
        </el-col>
        <el-col :span="12">
          <el-form-item label="现工作单位" prop="nowWorkUnit">
            <el-input
              v-model="formData.nowWorkUnit"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '现工作单位' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="办公电话" prop="officePhone">
            <el-input
              v-model="formData.officePhone"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '办公电话' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

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
        <el-col :span="12">
          <el-form-item label="电话" prop="phone">
            <el-input
              v-model="formData.phone"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '电话' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="技术职务" prop="technicalPosition">
            <el-input
              v-model="formData.technicalPosition"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '取得何种技术职务' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="外语水平" prop="foreignLanguageLevel">
            <el-input
              v-model="formData.foreignLanguageLevel"
              clearable
              :disabled="!footer"
              :placeholder="footer ? '掌握何种外语水平' : ''"
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
              :placeholder="footer ? '受过何种奖励' : ''"
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
              :placeholder="footer ? '受过何种处分' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="所在单位意见" prop="opinions">
            <el-input
              v-model="formData.opinions"
              clearable
              type="textarea"
              rows="2"
              :disabled="!footer"
              :placeholder="footer ? '所在单位意见' : ''"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="申请书" prop="applicationBook">
            <el-input
              v-model="formData.applicationBook"
              clearable
              type="textarea"
              rows="2"
              :disabled="!footer"
              :placeholder="footer ? '申请书' : ''"
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
            <el-table-column
              align="center"
              label="开始时间"
              prop="stareTime"
              :formatter="formatDate"
            />
            <el-table-column
              align="center"
              label="结束时间"
              prop="endTime"
              :formatter="formatDate"
            />
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

        <el-col :span="24" style="margin-top: 20px">
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
        </el-col>
        <el-col :span="24">
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
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="fjxiaz(row)">下载</el-button>
                <el-button type="text" @click="fjDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" style="margin-top: 20px">
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
              :on-success="handleAvatarSuccess1"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="fileList1">
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
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="fjxiaz(row)">下载</el-button>
                <el-button type="text" @click="fjDelete1(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" style="margin-top: 20px">
          <el-divider>公司律师执业证书（正、副本）复印件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess4"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="fileList4">
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
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="fjxiaz(row)">下载</el-button>
                <el-button type="text" @click="fjDelete4(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" style="margin-top: 20px">
          <el-divider>在合同有效期内的劳动合同复印件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess2"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="fileList2">
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
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="fjxiaz(row)">下载</el-button>
                <el-button type="text" @click="fjDelete2(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" style="margin-top: 20px">
          <el-divider>
            申请人符合《客户C运营律师管理办法》第七条第四项规定条件的工作经历、执业经历证明；
          </el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess3"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
        </el-col>
        <el-col :span="24">
          <el-table :data="fileList3">
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
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="fjxiaz(row)">下载</el-button>
                <el-button type="text" @click="fjDelete3(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess5"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="practiSingFiles">
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
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="fjxiaz(row)">下载</el-button>
                <el-button type="text" @click="fjDelete5(row)" v-if="footer">
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
      <el-button
        v-if="
          (formData.state == 2 ||
            formData.state == 3 ||
            formData.status == 2 ||
            formData.status == 3) &&
          jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </template>
    <resumeView ref="resumeView" @addList="addList" />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
    <fljdView ref="fljdView" />
    <flpxView ref="flpxView" />
    <wzfbView ref="wzfbView" />
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
  import store from '@/store'
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import fljdView from './fljdView.vue'
  import flpxView from './flpxView.vue'
  import resumeView from './resumeView.vue'
  import wzfbView from './wzfbView.vue'
  import { formatDay } from '@/utils/index'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  import {
    addGSLS,
    deleteFile,
    deleteJLList,
    getGSLSDefaultInfo,
    uploadApi,
    download,
  } from '@/api/fwgl/zzxx'
  import { getZYSQDefaultInfo, addZYSQ } from '@/api/fwgl/gsls'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'

  import { GetusrDetail } from '@/api/setting/personnel'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  export default {
    components: {
      resumeView,
      projectManage,
      fljdView,
      flpxView,
      wzfbView,
      CandidateUserSelect,
      Resubmit,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          practiceApplyName: '',
          age: '',
          identityCard: '',
          certificationNumber: '',
          schooloFgraduation: '',
          specialty: '',
          workDepartment: '',
          postalCode: '',
          sex: '',
          nation: '',
          politicsStatus: '',
          homeAddress: '',
          highestEducation: '',
          nowWorkUnit: '',
          officePhone: '',
          phone: '',
          technicalPosition: '',
          foreignLanguageLevel: '',
          award: '',
          punishment: '',
          fileIds: '',
          lawyerExtId: '',
          identityCardFileIds: '',
          certificateFileIds: '',
          proveFileIds: '',
          contractFileIds: '',
          certificateNumberFileIds: '',
          practisingFileIds: '',
          practiceApplyExtId: '',
          certificateNumber: '',
          state: '',
          status: '',
        },
        rules: {
          realname: [
            { required: true, message: '请选择人员', trigger: 'blur' },
          ],
          practiceApplyName: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
          ],
          age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
          identityCard: [
            { required: true, message: '请输入身份证号', trigger: 'blur' },
          ],
          certificationNumber: [
            { required: true, message: '请输入执业证号', trigger: 'blur' },
          ],
          schoolOfGraduation: [
            { required: true, message: '请输入毕业院校', trigger: 'blur' },
          ],
          specialty: [
            { required: true, message: '请输入专业', trigger: 'blur' },
          ],
          workDepartment: [
            { required: true, message: '请输入工作单位', trigger: 'blur' },
          ],
          postalCode: [
            { required: true, message: '请输入邮政编码', trigger: 'blur' },
          ],
          sex: [{ required: true, message: '请选择性别', trigger: 'blur' }],
          nation: [{ required: true, message: '请选择民族', trigger: 'blur' }],
          politicsStatus: [
            { required: true, message: '请选择政治面貌', trigger: 'blur' },
          ],
          homeAddress: [
            { required: true, message: '请输入家庭住址', trigger: 'blur' },
          ],
          highestEducation: [
            { required: true, message: '请选择最高学历', trigger: 'blur' },
          ],
          nowWorkUnit: [
            { required: true, message: '请输入现工作单位', trigger: 'blur' },
          ],
          officePhone: [
            { required: true, message: '请输入办公电话', trigger: 'blur' },
          ],
          phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
          technicalPosition: [
            { required: true, message: '请输入技术职务', trigger: 'blur' },
          ],
          foreignLanguageLevel: [
            { required: true, message: '请输入外语语种', trigger: 'blur' },
          ],
          award: [
            { required: true, message: '请输入获奖情况', trigger: 'blur' },
          ],
          punishment: [
            { required: true, message: '请输入处分情况', trigger: 'blur' },
          ],
        },
        dialogFormVisible: false,
        title: '详情',
        footer: true,
        tableData: [],
        tableData1: [],
        tableData2: [],
        tableData3: [],
        tableData4: [],
        tableData5: [],
        tableData6: [],
        fileList: [],
        fileList1: [],
        fileList2: [],
        fileList3: [],
        fileList4: [],
        practiSingFiles: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
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
      /**
       * @description: 选择组件回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      async getChildlistPro(val) {
        let res = await GetusrDetail({
          staffid: val[0].staffid,
        })
        const data = res.data.usrInfo
        this.formData = {
          realname: data.realname,
          practiceApplyName: data.realname,
          // orgname: data.orgName,
          phone: data.miblephone,
          education: data.education,
          jobexperiences: data.jobexperiences,
          major: data.major,
          officephone: data.officephone,
          politicaloutlook: data.politicaloutlook,
          qualification: data.qualification,
          school: data.school,
          situation: data.situation,
          title: data.title,
          jobname: data.linkDetp.orgname,
          orgname: data.linkOrg.orgname,
          staffId: val[0].staffid,
        }
        // this.disabledEdit = true

        this.staffid = val[0].staffid
        this.$set(this.formData, 'realname', val[0].realname)
      },
      /**
       * @description: 打开选择人员组件
       * @return {*}
       */      
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.formData = {}
        this.dialogFormVisible = true
        this.fileList = []
        this.tableData = []
        if (row) {
          getZYSQDefaultInfo({
            id: row.practiceApplyId,
          }).then(async (res) => {
            this.formData = Object.assign({}, res.data.practiceApply)
            this.formData.realname = res.data.practiceApply.practiceApplyName
            this.formData.sex = res.data.practiceApply.sex.toString()

            //执业申请
            if (
              res.data.practiceApply.state == 2 ||
              res.data.practiceApply.state == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 23,
                formId: row.practiceApplyId,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo == 1) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.practiceApplyId + ''
                this.fromIdcopy = row.practiceApplyId + ''
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

            // 执业申请注销
            if (
              res.data.practiceApply.status == 2 ||
              res.data.practiceApply.status == 3
            ) {
              const res2 = await getFlowTaskInfo({
                tableId: 33,
                formId: row.practiceApplyId,
              })
              this.jurisdictionCode = res2.data.isFlowInfo
              if (res2.data.isFlowInfo) {
                this.flowtaskinfoflowid = res2.data.flowId + ''
                this.fromId = row.practiceApplyId + ''
                this.fromIdcopy = row.practiceApplyId + ''
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
            if (res.data.identityCardFiles) {
              this.fileList = res.data.identityCardFiles || []
            }
            if (res.data.certificateFiles) {
              this.fileList1 = res.data.certificateFiles || []
            }
            if (res.data.contractFiles) {
              this.fileList2 = res.data.contractFiles || []
            }
            if (res.data.proveFiles) {
              this.fileList3 = res.data.proveFiles || []
            }
            if (res.data.certificateNumberFiles) {
              this.fileList4 = res.data.certificateNumberFiles || []
            }
            if (res.data.practiceApplyExt) {
              this.tableData = res.data.practiceApplyExt || []
            }
            if (res.data.practiSingFiles) {
              this.practiSingFiles = res.data.practiSingFiles || []
            }
            // this.formData.sex = res.data.practiceApply.sex.toString()
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
       * @description: 打开新增表单弹框-简历
       * @return {*}
       */      
      handleAdd() {
        this.$refs['resumeView'].showEdit('add', null)
      },
      /**
       * @description: 打开编辑表单弹框-简历
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['resumeView'].showEdit('edit', row)
      },
      /**
       * @description: 子表回调-简历
       * @param {*} value 回调数据
       * @return {*}
       */      
      addList(value) {
        const aaa = this.tableData.findIndex(
          (x) => x.practiceApplyExtId == value.practiceApplyExtId
        )
        if (aaa !== -1) {
          this.tableData.splice(aaa, 1, value)
        } else {
          this.tableData.push(value)
        }
      },
      /**
       * @description: 删除子表行数据-简历
       * @param {*} row 行数据
       * @return {*}
       */      
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteJLList({
            id: row.practiceApplyExtId,
          })
          if (code == 200) {
            this.tableData.splice(
              this.tableData.findIndex(
                (x) => x.practiceApplyExtId == row.practiceApplyExtId
              ),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      // 删除复印件
      fjDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId)
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      fjDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList1.splice(
              this.fileList1.findIndex((x) => x.fileId == row.fileId)
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      fjDelete2(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList2.splice(
              this.fileList2.findIndex((x) => x.fileId == row.fileId)
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      fjDelete3(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList3.splice(
              this.fileList3.findIndex((x) => x.fileId == row.fileId)
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      fjDelete4(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList4.splice(
              this.fileList4.findIndex((x) => x.fileId == row.fileId)
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      fjDelete5(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.practiSingFiles.splice(
              this.practiSingFiles.findIndex((x) => x.fileId == row.fileId)
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const aa = []
            const bb = []
            const cc = []
            const dd = []
            const ee = []
            const ff = []
            const gg = []

            this.tableData.forEach((item) => {
              aa.push(item.practiceApplyExtId)
            })
            this.fileList.forEach((e) => {
              bb.push(e.fileId)
            })
            this.fileList1.forEach((e) => {
              cc.push(e.fileId)
            })
            this.fileList2.forEach((e) => {
              dd.push(e.fileId)
            })
            this.fileList3.forEach((e) => {
              ee.push(e.fileId)
            })
            this.fileList4.forEach((e) => {
              ff.push(e.fileId)
            })
            this.practiSingFiles.forEach((e) => {
              gg.push(e.fileId)
            })
            this.formData.identityCardFileIds = bb.toString()
            this.formData.certificateFileIds = cc.toString()
            this.formData.contractFileIds = dd.toString()
            this.formData.proveFileIds = ee.toString()
            this.formData.certificateNumberFileIds = ff.toString()
            this.formData.practisingFileIds = gg.toString()
            this.formData.practiceApplyExtId = aa.toString()
            // this.formData.STAFFID = this.staffid

            this.$emit('add', this.formData)
            addZYSQ(this.formData).then((res) => {
              if (res.msg == '成功') {
                this.dialogFormVisible = false

                this.$baseMessage('成功', 'success', 'vab-hey-message-success')
                this.close()
              }
            })
          }
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.footer = true
        this.tableData = []
        this.fileList = []
        this.fileList1 = []
        this.fileList2 = []
        this.fileList3 = []
        this.fileList4 = []
        this.practiSingFiles = []
        this.clearType = true
        this.$emit('fetchData')
        this.dialogFormVisible = false
      },
      // 照片上传
      handlePreview() {},
      handleSuccess(res) {
        if (res.code == 200) {
          this.formData.photo = res.data.fileIds[0].filePath
        }
      },
      //文件上传
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },
      handleAvatarSuccess1(res) {
        if (res.code == 200) {
          this.fileList1.push(res.data.fileIds[0])
        }
      },
      handleAvatarSuccess2(res) {
        if (res.code == 200) {
          this.fileList2.push(res.data.fileIds[0])
        }
      },
      handleAvatarSuccess3(res) {
        if (res.code == 200) {
          this.fileList3.push(res.data.fileIds[0])
        }
      },
      handleAvatarSuccess4(res) {
        if (res.code == 200) {
          this.fileList4.push(res.data.fileIds[0])
        }
      },
      handleAvatarSuccess5(res) {
        if (res.code == 200) {
          this.practiSingFiles.push(res.data.fileIds[0])
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
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
      handleAdd1() {
        this.$refs['wzfbView'].showEdit('add', null)
      },
      async handleEdit1(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['wzfbView'].showEdit('edit', row)
      },
      handleAdd2() {
        this.$refs['flpxView'].showEdit('add', null)
      },
      async handleEdit2(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['flpxView'].showEdit('edit', row)
      },
      handleAdd3() {
        this.$refs['fljdView'].showEdit('add', null)
      },
      async handleEdit3(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['fljdView'].showEdit('edit', row)
      },
      async fjxiaz(row) {
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
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
