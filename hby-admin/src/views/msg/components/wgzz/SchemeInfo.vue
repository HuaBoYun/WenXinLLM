<template>
  <div>
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="110px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              :disabled="!footer"
              @change="handleMJChange"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '78%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || !footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="通知编号" prop="planCode">
            <el-input
              v-model="formData.planCode"
              clearable
              placeholder="请输入通知编号"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="(title = '新增')">
          <el-form-item label="通知类别" prop="planType">
            <el-select
              style="width: 100%"
              v-model="formData.planType"
              placeholder="选择通知类别"
              @change="typeChange"
              :disabled="!footer"
            >
              <el-option label="审计" :value="1" />
              <el-option label="内控" :value="2" />
              <el-option label="非系统实施" :value="3" />
              <el-option label="外部审计" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="通知名称" prop="planName">
            <el-input
              v-model="formData.planName"
              clearable
              placeholder="请输入通知名称"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="关联项目" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请选择关联项目"
              disabled
              style="width: 78%"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.list.show(formData.planType)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="整改经办人" prop="handlerName">
            <el-input
              v-model="formData.handlerName"
              clearable
              placeholder="整改经办人"
              disabled
              style="width: 78%"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="handleName"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="整改责任人" prop="zrrRealName">
            <el-input
              v-model="formData.zrrRealName"
              clearable
              placeholder="整改责任人"

              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show('zrrRealName')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="创建人" prop="createStaffName">
            <el-input
              v-model="formData.createStaffName"
              clearable
              placeholder="请输入创建人"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截止时间" prop="deadlineTime">
            <el-date-picker
              v-model="formData.deadlineTime"
              value-format="yyyy-MM-dd"
              type="date"
              :disabled="!footer"
              style="width: 100%"
              placeholder="请选择截止时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="planMemo">
            <el-input
              v-model="formData.planMemo"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              rows="4"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>整改清单</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              v-if="footer"
              :disabled="!formData.projectName"
              @click="openSelectContentList"
            >
              新建
            </el-button>
          </div>
          <el-table :data="issuesList" :key="tableFlag">
            <el-table-column
              align="center"
              label="问题编号"
              prop="issuesCode"
              width="100"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetailContent(row)">
                  {{ row.issuesCode }}
                </el-button>
              </template>
            </el-table-column>
            <!-- <el-table-column
              align="center"
              label="问题名称"
              prop="issuesName"
            /> -->
            <!-- <el-table-column align="center" label="问题来源" prop="issueType" #default="{ row }">{{ ['审计', '内控', '外部'][Number(row.issuesType) - 1] }}</el-table-column>
            <el-table-column align="center" label="被审计对象" prop="auditObjectName" /> -->
            <el-table-column
              align="center"
              label="问题标题"
              prop="issuesTitle"
              #default="{ row }"
            >
              <el-tooltip placement="top">
                <div
                  slot="content"
                  style="max-width: 600px; white-space: pre-wrap"
                >
                  {{ row.issuesTitle }}
                </div>
                <div class="showOverFlow">
                  {{ row.issuesTitle }}
                </div>
              </el-tooltip>
            </el-table-column>
            <el-table-column
              align="center"
              label="整改通知"
              prop="rectificationPlan"
              #default="{ row }"
            >
              <el-tooltip placement="top">
                <div
                  slot="content"
                  style="max-width: 600px; white-space: pre-wrap"
                >
                  {{ row.rectificationPlan }}
                </div>
                <div class="showOverFlow">
                  {{ row.rectificationPlan }}
                </div>
              </el-tooltip>
            </el-table-column>
            <el-table-column
              align="center"
              label="拟稿人"
              prop="createStaffName"
            />
            <el-table-column
              align="center"
              label="拟稿日期"
              prop="createTime"
              :formatter="formatDate"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
              v-if="footer"
              #default="{ row }"
            >
              <template>
                <el-button type="text" @click="handleEditContent(row)">
                  编辑
                </el-button>
              </template>
              <template>
                <el-button type="text" @click="handleDelte2(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24" style="margin: 20px 0">
          <UEditor
            ref="ueditor"
            v-model="formData.zgcont"
            :height="300"
            :disabled="!footer && !isShow"
            :templates="templates"
          />
        </el-col>
        <el-col :span="24" style="margin-top: 10px">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
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
          <el-table :data="attList">
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
                <el-button
                  type="text"
                  @click="handleDowns(row)"
                  :disabled="false"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handlePreviewFile(row)"
                  :disabled="false"
                >
                  预览
                </el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>

    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <!-- <el-button type="primary" @click="save" v-if="isShow">确 定</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button type="primary" @click="ymsubmit" :disable="btnLoading">
        提 交
      </el-button>
    </div>

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />

    <projectList ref="list" @selected="handleProjectSelected" />
    <contentTable ref="content" @selected="handleContentSelected" />
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <zgqdEdit v-if="showZgqdEdit" ref="edit" @closeDialog="closeDialog" />
    <contentEdit
      v-if="showContentEdit"
      ref="contentEdit"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
      @edit-data="handleEditData"
      @update-data="handleUpdateData"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import {
    saveRectificationPlan,
    delRectificationFile,
    delRectificationIssues,
    getIssuesAllDetailInfo,
    getIssuesDetail,
    getRectificationPlanDetail,
  } from '@/api/zgzz/index.js'
  import store from '@/store'
  import projectList from '@/views/audit/rectify/components/options/projectList'
  import contentEdit from '@/views/audit/rectify/components/options/contentEdit'
  import contentTable from '@/views/audit/rectify/components/table/contentTable'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  const { baseURL } = require('@/config')
  import * as dayjs from 'dayjs'
  import zgqdEdit from '@/views/audit/collect/components/edit'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import UEditor from '@/components/UEditor'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'

  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'FlawInfo',
    components: {
      projectList,
      contentTable,
      ExecutorOptions,
      zgqdEdit,
      contentEdit,
      Resubmit,
      UEditor,
      ZXPerson,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        templates: [],
        formData: {
          attIds: '', // 当前页面新保存的附件主键数组
          attList: [], // 附件列表
          issuesList: [],
          createStaffName: '', //创建人姓名
          // createStaff: '', //创建人主键
          // createTime: '', //创建时间
          deadlineTime: '', //截止时间
          handlerName: '', //整改经办人姓名
          handlerId: '', //整改经办人主键
          issuesIds: '', //当前页面新选择的整改清单主键数组
          linkDeptId: '', //数据所属部门主键
          linkOrgId: '', //通知所属公司主键
          planCode: '', //整改通知编号
          planId: '', //整改通知主键
          planMemo: '', //备注
          planName: '', //整改通知名称
          planType: '', //通知类别
          projectName: '', //关联项目名称
          projectId: '', //关联项目主键
          zrrRealName: '', //整改责任人姓名
          response: '', //整改责任人主键
          status: '', //通知
          updateStaff: '', //修改人主键
          updateTime: '', //修改时间
          zgcont: '', //富文本
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        type: '',
        footer: true,
        tableFlag: false,
        rules: {
          planCode: [
            {
              required: true,
              message: '请输入通知编号',
              trigger: 'blur',
            },
          ],
          planName: [
            {
              required: true,
              message: '请输入通知名称',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请选择关联项目',
              trigger: 'blur',
            },
          ],
          createStaffName: [
            {
              required: true,
              message: '请输入创建人',
              trigger: 'blur',
            },
          ],
          deadlineTime: [
            {
              required: true,
              message: '请输入截止时间',
              trigger: 'blur',
            },
          ],
          planMemo: [
            {
              required: false,
              message: '请输入备注',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        staffid: '',
        arr: [],
        attList: [],
        issuesList: [],
        showZgqdEdit: false,
        showContentEdit: false,

        isShow: false,
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        showModels: {},
        handler: true,
        btnLoading: false,
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
        editedIssuesList: [], // 添加编辑过的问题清单数据
      }
    },
    computed: {
      getFormLevel() {
        if (!this.formData.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.formData.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    watch: {},
    methods: {
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      handleName() {
        // if (this.formData.secrectLevelId) {
        this.$refs.executor.showEdit(null, this.formData.secrectLevelId)
        // } else {
        //   this.$message.error('请选择密级')
        // }
      },
      handleZgqdDetail(row) {
        this.showZgqdEdit = true
        this.$nextTick(async () => {
          const res = await getIssuesDetail({ issuesId: row.issuesId })
          this.$refs['edit'].showEdit('detail', res.data)
        })
      },
      closeDialog() {
        this.showZgqdEdit = false
        this.showContentEdit = false
      },
      typeChange(e) {
        this.formData.planType = e
        this.formData.projectId = ''
        this.formData.projectName = ''
        this.issuesList = []
      },

      handleContentSelected(node) {
        // if (this.issuesList) {
        //   this.issuesList = [...this.issuesList, ...node]
        // } else {
        //   this.issuesList = node
        // }
        const combinedList = [...this.issuesList, ...node]
        const uniqueList = Array.from(
          new Set(combinedList.map((item) => item.issuesId))
        ).map((issuesId) =>
          combinedList.find((item) => item.issuesId === issuesId)
        )
        this.issuesList = uniqueList
        this.tableFlag = true
      },
      openSelectContentList() {
        if (!this.formData.planType)
          return this.$message.error('请选择通知类别')
        if (!this.formData.projectId)
          return this.$message.error('请选择关联项目')
        this.$refs.content.show(this.formData)
      },
      handleProjectSelected(node) {
        this.formData.projectName = node.planName
        this.formData.projectId = node.planId
      },
      async fetchData() {
        // 清单编辑后刷新清单列表
        if (!this.formData.planId) return
        const res = await getRectificationPlanDetail({
          planId: this.formData.planId,
        })
        console.log('res', res)
        if (!res || !res.data) return
        const issuesList = res.data.issuesList.map((x) => {
          const { issues, ...other } = x
          return {
            ...issues,
            ...other,
            createTime: issues.createTime,
            rectificationPlan: x.rectificationPlan,
          }
        })
        this.issuesList = JSON.parse(JSON.stringify(issuesList))
        this.formData.issuesList = issuesList
      },
      showEdit(
        title,
        row,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        status,
        isWdcy,
        flowType
      ) {
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        console.log('isWdcy: ', isWdcy)
        this.dialogFormVisible = true
        if (row) {
          // 保存审批用的密级id
          if (row.secrectLevelId) {
            localStorage.setItem('SPsecrectLevelId', row.secrectLevelId)
          }
          Object.assign(this.formData, row)
          const issuesList = row.issuesList.map((x) => {
            const { issues, ...other } = x
            return {
              ...issues,
              ...other,
              rectificationPlan: x.rectificationPlan,
              createTime: issues.createTime,
            }
          })
          console.log('issuesList', issuesList)
          this.attList = JSON.parse(JSON.stringify(row.attList))
          this.issuesList = JSON.parse(JSON.stringify(issuesList))
          this.formData.issuesList = issuesList
          console.log('this.formData', this.formData)
          this.formData.deadlineTime = dayjs(this.formData.deadlineTime).format(
            'YYYY-MM-DD'
          )

          this.showModels = row.showModels || {}
          // const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          // if(row.handlerId == userInfo.staffid){
          //   this.handler = true
          // }else{
          //   this.handler = false
          // }
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.isShow = false
          this.footer = true
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
          if (status != 6) {
            const userInfo = JSON.parse(localStorage.getItem('userInfo'))
            if (row.handlerId == userInfo.staffid && !isWdcy) {
              this.isShow = true
            }
          }
        } else {
          this.title = '新增'
          this.formData.planType = 1
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.createStaffName = userInfo.realname
        }

        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      close() {
        // this.$emit('closeDialog')
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            // 验证整改清单是否有数据
            if (!this.issuesList || this.issuesList.length === 0) {
              this.$message.error('整改清单不能为空，请至少添加一条数据')
              return false
            }
            const attListArr = []
            this.attList.map((item) => {
              if (this.formData.attList.some((x) => x.attid === item.attid)) {
              } else attListArr.push(item.attid)
            })
            // const attIds = attListArr.join(',')

            const issuesArr = []
            this.issuesList.map((item) => {
              if (
                this.formData.issuesList.some(
                  (x) => x.issuesId === item.issuesId
                )
              ) {
              } else issuesArr.push(item.issuesId)
            })
            const issuesIds = issuesArr.join(',')

            const reqData = { ...this.formData }
            reqData.attIds = attListArr
            reqData.issuesIds = issuesIds
            delete reqData.updateTime
            delete reqData.createTime
            delete reqData.attList
            delete reqData.issuesList

            // 添加整改清单数据处理逻辑
            reqData.relaList = this.issuesList.map((item) => {
              // 处理附件ID为数组格式
              let itemAttIds = item.attIds
              if (typeof itemAttIds === 'string' && itemAttIds) {
                itemAttIds = itemAttIds.split(',')
              } else if (!Array.isArray(itemAttIds)) {
                itemAttIds = []
              }

              // 如果有附件列表，从附件列表中提取attid
              if (item.attList && item.attList.length > 0) {
                const attIdsFromList = item.attList.map((att) => att.attid)
                // 合并已有的attIds和从attList中提取的attIds
                if (Array.isArray(itemAttIds) && itemAttIds.length > 0) {
                  itemAttIds = [...new Set([...itemAttIds, ...attIdsFromList])]
                } else {
                  itemAttIds = attIdsFromList
                }
              }

              // 创建基本对象
              const relaItem = {
                issuesId: item.issuesId,
                rectificationPlan: item.rectificationPlan || '',
                resultMemo: item.resultMemo || '',
                deadline: item.deadline || '',
                attIds: itemAttIds,
              }

              // 如果有relaId则添加
              if (item.relaId) {
                relaItem.relaId = item.relaId
              }

              return relaItem
            })

            // 合并被编辑过的数据
            if (this.editedIssuesList.length > 0) {
              this.editedIssuesList.forEach((editedItem) => {
                const index = reqData.relaList.findIndex(
                  (r) => r.issuesId === editedItem.issuesId
                )
                if (index !== -1) {
                  let itemAttIds = editedItem.attIds
                  if (typeof itemAttIds === 'string' && itemAttIds) {
                    itemAttIds = itemAttIds.split(',')
                  } else if (!Array.isArray(itemAttIds)) {
                    itemAttIds = []
                  }

                  // 如果有附件列表，从附件列表中提取attid
                  if (editedItem.attList && editedItem.attList.length > 0) {
                    const attIdsFromList = editedItem.attList.map(
                      (att) => att.attid
                    )
                    // 合并已有的attIds和从attList中提取的attIds
                    if (Array.isArray(itemAttIds) && itemAttIds.length > 0) {
                      itemAttIds = [
                        ...new Set([...itemAttIds, ...attIdsFromList]),
                      ]
                    } else {
                      itemAttIds = attIdsFromList
                    }
                  }

                  // 更新已存在的项
                  reqData.relaList[index] = {
                    ...reqData.relaList[index],
                    rectificationPlan:
                      editedItem.rectificationPlan ||
                      reqData.relaList[index].rectificationPlan,
                    resultMemo:
                      editedItem.resultMemo ||
                      reqData.relaList[index].resultMemo,
                    deadline:
                      editedItem.deadline || reqData.relaList[index].deadline,
                    attIds: itemAttIds,
                  }
                }
              })
            }

            const res = await saveRectificationPlan(reqData)

            if (res.code == 1) {
              this.$baseMessage('保存成功', 'success')
              // 保存成功后清空编辑列表
              this.editedIssuesList = []
            }
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      async handleDelete(row) {
        let list = this.attList || []
        list = list.filter((item) => item.attid != row.attid)
        await delRectificationFile({
          attId: String(row.attid),
          planId: this.formData.planId,
        })
        this.$message.success('删除成功')
        this.attList = list
      },
      async handleDelte2(row) {
        let list = this.issuesList || []
        list = list.filter((item) => item.issuesId != row.issuesId)
        this.tableFlag = false
        console.log('this.formData.planId', this.formData.planId)
        if (this.formData.planId) {
          await delRectificationIssues({
            issuesId: row.issuesId,
            planId: this.formData.planId,
          })
        }
        this.issuesList = list
      },
      handleEditContent(row) {
        this.showContentEdit = true
        this.$nextTick(async () => {
          // 查找是否有已编辑但未保存的数据
          const editedItem = this.editedIssuesList.find(
            (item) => item.issuesId === row.issuesId
          )

          if (row.relaId) {
            const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
            if (res && res.data) {
              // 如果有已编辑的数据，合并到res.data中
              if (editedItem) {
                res.data.rectificationPlan =
                  editedItem.rectificationPlan || res.data.rectificationPlan
                res.data.resultMemo =
                  editedItem.resultMemo || res.data.resultMemo
                res.data.deadline = editedItem.deadline || res.data.deadline
                // 如果有编辑过的附件，使用编辑过的附件
                if (editedItem.attList) {
                  res.data.attList = editedItem.attList
                }
              }

              res.data.showModels = this.showModels || {}
              this.$refs.contentEdit.showEdit('edit', res.data)
            }
          } else {
            const res = await getIssuesDetail({ issuesId: row.issuesId })
            if (res && res.data) {
              // 如果有已编辑的数据，合并到res.data中
              if (editedItem) {
                res.data.rectificationPlan =
                  editedItem.rectificationPlan || res.data.rectificationPlan
                res.data.resultMemo =
                  editedItem.resultMemo || res.data.resultMemo
                res.data.deadline = editedItem.deadline || res.data.deadline
                // 如果有编辑过的附件，使用编辑过的附件
                if (editedItem.attList) {
                  res.data.attList = editedItem.attList
                }
              } else {
                // 如果没有已编辑的数据，使用当前行的数据
                res.data.rectificationPlan =
                  row.rectificationPlan || res.data.rectificationPlan
                res.data.resultMemo = row.resultMemo || res.data.resultMemo
                res.data.deadline = row.deadline || res.data.deadline
                // 使用当前行的附件数据
                if (row.attList) {
                  res.data.attList = row.attList
                }
              }

              res.data.showModels = this.showModels || {}
              this.$refs.contentEdit.showEdit('edit', {
                issues: res.data,
                currentEditState: editedItem || {
                  rectificationPlan: row.rectificationPlan,
                  resultMemo: row.resultMemo,
                  deadline: row.deadline,
                  attList: row.attList || [],
                },
              })
            }
          }
        })
      },
      handleDetailContent(row) {
        this.showContentEdit = true
        this.$nextTick(async () => {
          // 查找是否有已编辑但未保存的数据
          const editedItem = this.editedIssuesList.find(
            (item) => item.issuesId === row.issuesId
          )

          if (row.relaId) {
            const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
            if (res && res.data) {
              // 如果有已编辑的数据，合并到res.data中
              if (editedItem) {
                res.data.rectificationPlan =
                  editedItem.rectificationPlan || res.data.rectificationPlan
                res.data.resultMemo =
                  editedItem.resultMemo || res.data.resultMemo
                res.data.deadline = editedItem.deadline || res.data.deadline
                // 如果有编辑过的附件，使用编辑过的附件
                if (editedItem.attList) {
                  res.data.attList = editedItem.attList
                }
              }

              res.data.showModels = this.showModels || {}
              this.$refs.contentEdit.showEdit('detail', res.data)
            }
          } else {
            const res = await getIssuesDetail({ issuesId: row.issuesId })
            if (res && res.data) {
              // 如果有已编辑的数据，合并到res.data中
              if (editedItem) {
                res.data.rectificationPlan =
                  editedItem.rectificationPlan || res.data.rectificationPlan
                res.data.resultMemo =
                  editedItem.resultMemo || res.data.resultMemo
                res.data.deadline = editedItem.deadline || res.data.deadline
                // 如果有编辑过的附件，使用编辑过的附件
                if (editedItem.attList) {
                  res.data.attList = editedItem.attList
                }
              } else {
                // 如果没有已编辑的数据，使用当前行的数据
                res.data.rectificationPlan =
                  row.rectificationPlan || res.data.rectificationPlan
                res.data.resultMemo = row.resultMemo || res.data.resultMemo
                res.data.deadline = row.deadline || res.data.deadline
                // 使用当前行的附件数据
                if (row.attList) {
                  res.data.attList = row.attList
                }
              }

              res.data.showModels = this.showModels || {}
              this.$refs.contentEdit.showEdit('detail', {
                issues: res.data,
                currentEditState: editedItem || {
                  rectificationPlan: row.rectificationPlan,
                  resultMemo: row.resultMemo,
                  deadline: row.deadline,
                  attList: row.attList || [],
                },
              })
            }
          }
        })
      },

      handleExecutorSelected(node) {
        this.formData.handlerName = node[0].realname
        this.formData.handlerId = node[0].staffid
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return dayjs(data).format('YYYY-MM-DD')
      },

      //提交
      async ymsubmit() {
        try {
          this.$refs['elForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              // 清空编辑列表
              this.editedIssuesList = []
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
          console.error('Error fetching data:', error)
          // 你可以在这里处理错误，比如显示错误提示
        }
      },
      handleMJChange(selectedValue) {
        if (selectedValue) {
          // 保存密级id
          localStorage.setItem('SPsecrectLevelId', selectedValue)
        }
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      // 添加处理编辑数据的方法
      handleEditData(data) {
        const existingIndex = this.editedIssuesList.findIndex(
          (item) => item.issuesId === data.issuesId
        )
        if (existingIndex > -1) {
          this.editedIssuesList[existingIndex] = data
        } else {
          this.editedIssuesList.push(data)
        }

        // Update issuesList to show changes in the table
        const issuesIndex = this.issuesList.findIndex(
          (item) => item.issuesId === data.issuesId
        )
        if (issuesIndex > -1) {
          this.issuesList[issuesIndex] = {
            ...this.issuesList[issuesIndex],
            rectificationPlan: data.rectificationPlan,
            resultMemo: data.resultMemo,
            deadline: data.deadline,
            attList: data.attList || this.issuesList[issuesIndex].attList, // 保留附件信息
          }
          // Force table to update
          this.tableFlag = !this.tableFlag
        }
      },
      // 添加更新数据的方法
      handleUpdateData(data) {
        let issuesIndex = this.issuesList.findIndex(
          (item) => item.issuesId === data.issuesId
        )

        if (issuesIndex === -1 && data.relaId) {
          issuesIndex = this.issuesList.findIndex(
            (item) => item.relaId === data.relaId
          )
        }

        if (issuesIndex > -1) {
          this.issuesList[issuesIndex] = {
            ...this.issuesList[issuesIndex],
            rectificationPlan: data.rectificationPlan,
            resultMemo: data.resultMemo,
            deadline: data.deadline,
            attList: data.attList || this.issuesList[issuesIndex].attList, // 保留附件信息
          }
          this.tableFlag = !this.tableFlag
        }
      },
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
          return
        }
        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
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
      },
      handleSuccess(file) {
        if (file.code == 200) {
          // 更新文件列表
          this.fileList = [...this.fileList, ...file.data]
          this.attList = [...this.attList, ...file.data]
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
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
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
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
  .showOverFlow {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.5em;
    max-height: 3em;
    white-space: pre-wrap;
  }
</style>
