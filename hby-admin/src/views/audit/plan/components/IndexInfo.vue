<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="计划编号">
            <el-input
              v-model="formData.plancode"
              clearable
              placeholder="请输入计划编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称" prop="planname">
            <el-input
              v-model.trim="formData.planname"
              clearable
              placeholder="请输入计划名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划年度" prop="palnyear">
            <el-date-picker
              v-model="formData.palnyear"
              type="year"
              :style="{ width: '100%' }"
              placeholder="请选择计划年度"
              value-format="yyyy"
              :disabled="!footer"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划类别" prop="plantype">
            <el-input
              v-model="formData.plantype"
              clearable
              placeholder="请输入计划类别"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划费用估算">
            <el-input
              v-model="formData.palncost"
              clearable
              placeholder="请输入计划费用估算"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <template slot="append">(元)</template>
            </el-input>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="计划时间" prop="spanDate">
            <el-date-picker
              v-model="formData.spanDate"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="计划负责人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请选择计划负责人"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              @click="showGroupLeader"
              style="margin-left: 10px"
              type="primary"
              v-if="footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="编制人" prop="organizationName">
            <el-input
              v-model="formData.organizationName"
              clearable
              placeholder="请输入编制人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制日期" prop="createtime">
            <el-date-picker
              v-model="formData.createtime"
              :style="{ width: '100%' }"
              placeholder="请输入编制日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计单位" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入审计单位"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否外审" prop="isauditor">
            <el-radio-group v-model="formData.isauditor" size="medium">
              <el-radio
                v-for="(item, index) in fieldOptions"
                :key="index"
                :disabled="!footer || item.disabled"
                :label="item.value"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>计划项目</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              @click="handleAdd"
              v-if="this.formData.planid"
            >
              增加一行
            </el-button>
            <!-- <el-button type="success" @click="handleDelCheck">删除</el-button> -->
          </div>
          <!-- 新增可编辑表格 -->
          <el-table
            border
            :data="tableDataProject"
            style="width: 100%; margin-bottom: 25px"
            :rules="rulesProject"
          >
            <el-table-column
              align="center"
              label="项目名称"
              prop="projectname"
              :render-header="addRedStar"
            >
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.projectname"
                  size="mini"
                  style="width: 90%"
                />
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="工作目标"
              prop="targetname"
              :render-header="addRedStar"
            >
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.targetname"
                  size="mini"
                  style="width: 90%"
                />
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="计划完成时间"
              prop="platformName"
              min-width="180px"
            >
              <template slot-scope="scope">
                <el-date-picker
                  class="project"
                  v-model="scope.row.projectFinishTime"
                  type="date"
                  placeholder="选择日期"
                ></el-date-picker>
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="被审计单位"
              prop="platformName"
              min-width="180px"
            >
              <template slot-scope="scope">
                <el-input
                  disabled
                  v-model="scope.row.orgidnames"
                  size="mini"
                  style="width: 70%"
                />
                <el-button
                  type="primary"
                  size="mini"
                  style="margin-left: 3px"
                  @click="showObj(scope.$index)"
                >
                  选择
                </el-button>
              </template>
            </el-table-column>

            <el-table-column align="center" label="是否外委" min-width="130">
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.externalassig"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in fieldOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>

            <el-table-column align="center" label="操作" min-width="110">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleAddTable(scope.$index, scope.row)"
                >
                  保存
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteTable(scope.$index, scope.row)"
                >
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
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
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
    <selectTeam ref="select" @selectTeamList="selectTeamList" />
    <Auditee ref="auditee" @auditee="setAuditee"></Auditee>
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import {
    getAuditPlanAttInfo,
    getPlanProjectListByPlanId,
    mergeAuditPlanInfo,
    removePlanProjectInfo,
  } from '@/api/audit/plan'
  import store from '@/store'
  import { formatDate } from '@/utils/index'
  import Auditee from './childCom/Auditee.vue'
  import selectTeam from './selectTeam.vue'
  const { baseURL } = require('@/config')
  export default {
    name: 'IndexEdit',
    inheritAttrs: false,
    components: {
      selectTeam,
      Auditee,
    },
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          plancode: '',
          planname: undefined,
          palnyear: undefined,
          plantype: '',
          palncost: undefined,
          spanDate: [],
          realname: '',
          isauditor: '',
          organizationName: '',
          createtime: '',
          orgname: '',
          remarks: '',
          principalid: '',
          orgid: '',
          createstaffid: '',
        },
        sIndex: 0,
        templates: [],
        footer: true,
        tableData: [],
        tableDataProject: [],
        rules: {
          plancode: [
            { required: true, message: '请输入计划编号', trigger: 'blur' },
          ],
          palnyear: [
            { required: true, message: '请输入计划年度', trigger: 'blur' },
          ],
          plantype: [
            { required: true, message: '请输入计划类别', trigger: 'blur' },
          ],
          // spanDate: [
          //   {
          //     required: true,
          //     message: '请选择计划时间',
          //     trigger: 'change',
          //   },
          // ],
          realname: [
            { required: true, message: '请选择计划负责人', trigger: 'change' },
          ],
          isauditor: [
            { required: true, message: '请选择是否外审', trigger: 'change' },
          ],
          orgname: [
            { required: true, message: '请输入审计单位', trigger: 'blur' },
          ],
          remarks: [
            { required: false, message: '请输入活动名称', trigger: 'blur' },
          ],
        },
        rulesProject: {
          projectname: [
            { required: true, message: '请输入项目名称', trigger: 'blur' },
          ],
          targetname: [
            { required: true, message: '请输入工作目标', trigger: 'blur' },
          ],
        },
        fieldOptions: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 0,
          },
        ],
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},

    created() {},
    mounted() {},
    methods: { 
      /**
       * @description: 文件上传前校验
       * @param {*}  
       * @return {*}
       */
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      /**
       * @description: 删除一条 计划项目 数
       * @param {*}  
       * @return {*}
       */
      async handleAddTable(index, row) {},
      // 删除一条 计划项目 数据 
      async handleDeleteTable(index, row) {
        this.tableDataProject.splice(index, 1)
        if (row.planprojectid) {
          const data = await removePlanProjectInfo({
            planprojectid: row.planprojectid,
          })
        }
      },
        /**
       * @description: el-table一列颜色
       * @param {*}  
       * @return {*}
       */ 
      addRedStar(h, { column }) {
        return [
          h('span', { style: 'color: red' }, '*'),
          h('span', ' ' + column.label),
        ]
      },
        /**
       * @description: 选择人员组件，回调，把返回数据保存在tableDataProject
       * @param {*}  
       * @return {*}
       */  
      setAuditee(val, flag) {
        if (flag == 'right') {
          this.tableDataProject[this.sIndex].orgidnames = val[0].realname
        } else {
          this.tableDataProject[this.sIndex].orgidnames = val.name
        }
      },
        /**
       * @description:  唤起渲染组件
       * @param {*}  
       * @return {*}
       */   
      showObj(sIndex) {
        this.sIndex = sIndex
        this.$refs['auditee'].showEdit()
      },
         /**
       * @description:  添加点击按钮
       * @param {*}  
       * @return {*}
       */   
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.tableDataProject.push({
          projectname: '',
          targetname: '',
          projectFinishTime: '',
          externalassig: '',
          orgidnames: '',
        })
      },
          /**
       * @description:  择组员 组件 ，回调，把返回的数据存入formData
       * @param {*}  
       * @return {*}
       */   
      selectTeamList(val) {
        this.formData.staffid = val[0].staffid
        this.formData.realname = val[0].realname
      },
       /**
       * @description: 组件初始化，第一个参数 判断 编辑、新建、详情，第二个 页面form数据，第三个 表单数据，编号
       * @param {*}  
       * @return {*}
       */
      showEdit(title, row, plancode) {
        this.dialogFormVisible = true
        if (row) {
          this.formData = row.auditPlan
          const {
            starttime,
            endtime,
            createStaff,
            auditOrgInfo,
            principalStaff,
          } = row.auditPlan
          this.formData.spanDate = [starttime, endtime]
          this.formData.realname = principalStaff.realname
          this.formData.principalid = principalStaff.staffid
          this.formData.orgname = auditOrgInfo.orgname
          this.formData.orgid = auditOrgInfo.orgid
          this.formData.organizationName = createStaff.realname
          this.formData.createstaffid = createStaff.staffid
          this.getFileList(row.auditPlan)
          this.getTable(row.auditPlan)
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新建'
          this.footer = true
          this.formData.plancode =
            plancode.split('-')[0] +
            '-' +
            (parseInt(plancode.split('-')[1]) + 1)

          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.organizationName = userInfo.realname
          this.formData.createstaffid = userInfo.staffid
          this.formData.orgname = userInfo.linkDetp.orgname
          this.formData.orgid = userInfo.linkDetp.orgid
          this.formData.createtime = formatDate(new Date())
        }
      }, 
          /**
       * @description: 获取计划项目 列表接口
       * @param {*}  
       * @return {*}
       */  
      async getTable(row) {
        const data = await getPlanProjectListByPlanId({ planId: row.planid })
        this.tableDataProject = data.data.planList || []
      },
          /**
       * @description: 取附件列表接口
       * @param {*}  
       * @return {*}
       */  
      async getFileList(row) {
        const data = await getAuditPlanAttInfo({
          planId: row.planid,
        })
        this.tableData = data.data.data || []
      },
          /**
       * @description 唤起  选择组长组件
       * @param {*}  
       * @return {*}
       */   
      showGroupLeader(sIndex) {
        this.$refs['select'].showEdit('leader')
      },
       /**
       * @description: 关闭组件，清空form
       * @param {*} 
       * @return {*}
       */
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      }, 
         /**
       * @description 保存
       * @param {*}  
       * @return {*}
       */  
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })

            const {
              spanDate,
              auditOrgInfo,
              createStaff,
              principalStaff,
              createtime,
              ...other
            } = this.formData
            attids = attids.substring(0, attids.length - 1)
            let planStartTime = spanDate[0]
            let planEndTime = spanDate[1]
            const data = await mergeAuditPlanInfo({
              ...other,
              planStartTime,
              planEndTime,
              attids,
            })

            if (data.code == 1) {
              this.formData.planid = data.data.auditPlan.planid
            }

            this.$emit('fetch-data')
            // this.close()
          } else {
            return false
          }
        })
      },
         /**
       * @description 附件下载
       * @param {*}  
       * @return {*}
       */   
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
         /**
       * @description 附件删除
       * @param {*}  
       * @return {*}
       */    
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
      },
      handlePreview(file) {}, 
        /**
       * @description 附件上传成功回调
       * @param {*}  
       * @return {*}
       */ 
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData || []
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
