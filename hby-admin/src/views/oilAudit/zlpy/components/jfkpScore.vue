<template>
  <!-- 1审计实施方案 edit -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item
            label="审计项目名称"
            placeholder="请输入审计项目名称"
            prop="projectId"
          >
            <el-select v-model="formData.projectId" style="width: 100%">
              <el-option
                v-for="item in auditProjectSelectList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="评分时间" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建部门" prop="org.orgname">
            <el-input
              v-model="formData.org.orgname"
              clearable
              placeholder="请选择创建部门"
              disabled
              style="width: 78%"
            />
            <el-button
              style="margin-left: 15px"
              type="primary"
              @click="$refs.department.show()"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="reviewerId">
            <div style="display: flex; align-items: center">
              <el-input
                v-model="formData.createUser"
                clearable
                disabled
                placeholder="请输入创建人"
              />
              <!-- <el-button
                type="primary"
                style="margin-left: 20px"
                @click="chooseRe"
              >
                选择
              </el-button> -->
            </div>
          </el-form-item>
        </el-col>
      </el-form>
      <el-col :span="24" class="flex">
        <el-button
          class="mb20"
          type="primary"
          :disabled="!footer"
          @click="addPro"
        >
          新增
        </el-button>
      </el-col>
      <el-table :data="formData.qualityItems" border style="width: 100%">
        <el-table-column align="center" label="项目组人员名称" width="350">
          <template #default="{ row, $index }">
            <el-input
              style="width: 70%"
              v-model="row.userName"
              clearable
              disabled
              placeholder="请选择创建人"
            />
            <el-button
              type="primary"
              style="margin-left: 15px"
              @click="chooseRe($index)"
            >
              选择
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="项目角色" width="150">
          <template #default="{ row }">
            <el-input
              v-model="row.projectRole"
              size="mini"
              :disabled="!footer"
              style="width: 90%"
            />
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          prop="baseScore"
          label="基础分"
          width="100"
        >
          <template #default="{ row }">
            <el-input
              :disabled="!footer"
              v-model="row.baseScore"
              size="mini"
              style="width: 90%"
            />
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="increaseScore"
          label="增加分"
          width="100"
        >
          <template #default="{ row }">
            <el-input
              :disabled="!footer"
              v-model="row.increaseScore"
              size="mini"
              style="width: 90%"
            />
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          prop="increaseScoreReason"
          label="增加评分原因"
          width="300"
        >
          <template slot-scope="scope">
            <el-input
              :disabled="!footer"
              v-model="scope.row.increaseScoreReason"
              size="mini"
              style="width: 90%"
              type="textarea"
            />
          </template>
        </el-table-column>
        <el-table-column align="center" prop="increaseScore" label="合计分">
          <template #default="{ row }">
            {{ Number(row.baseScore) + Number(row.increaseScore) }}
          </template>
        </el-table-column>
        <el-table-column align="center" prop="increaseScore" label="操作">
          <template #default="{ row, $index }">
            <el-button
              type="text"
              @click="delRow($index, row)"
              :disabled="!footer"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <!-- 人员 -->
    <Executor ref="person" @selected="selected" />

    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { implementPlanList } from '@/oapi/audit/project'
  import {
    qualityScoreDetail,
    projectEvalScoreDetail,
    projectEvalScoreUpdate,
  } from '@/oapi/audit/qualityScore'
  import DepartmentOptions from '@/views/audit/report/components/options/department.vue'
  import { baseURL } from '@/config'
  import store from '@/store'
  import Executor from '@/views/audit/analyse/components/executor'
  // import {
  //   editDataSource,
  //   getDataSourceDefaultInfo,
  //   LinkTest,
  // } from '@/api/setting/org'

  const token = store.getters['user/token']

  export default {
    components: { DepartmentOptions, Executor },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        auditProjectSelectList: [], // 审计项目名称的列表
        detailScoreData: [], // 临时详情数据变量
        formData: {
          // 表单入参
          id: '',
          projectId: null, // 当前选中的审计项目名称ID
          org: {
            // 复制科室
            orgid: null,
            orgname: '',
          },
          orgId: null,
          qualityItems: [], // 考核内容
          baseScore: 0,
          increaseScore: 0,
          increaseScoreReason: '',
        },
        chooseType: 1,
        chooseIndex: 0,
        footer: true,
        rules: {
          projectId: [
            {
              required: true,
              message: '请选择审计项目名称',
              trigger: 'change',
            },
          ],
          startTime: [
            {
              required: true,
              message: '请输入实施时间',
              trigger: 'blur',
            },
          ],
          'org.orgname': [
            {
              required: true,
              message: '请选择负责科室',
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
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      handleDepartmentSelected(node) {
        // 选择科室回调
        //保存名称
        this.$set(this.formData.org, `orgname`, node.name)
        //保存名称对应的ID
        this.$set(this.formData.org, `orgid`, node.id)
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        const {
          data: { tlist },
        } = await implementPlanList({ pageNumber: 1, pageSize: 9999 })
        if (tlist.length) {
          this.auditProjectSelectList = tlist.map((item) => {
            return {
              value: item.id,
              label: item.projectName,
            }
          })
        }

        if (row) {
          const detailData = await projectEvalScoreDetail({ id: row.id })
          this.formData = detailData.data.data
          this.detailScoreData = detailData.data.data.qualityItems
          this.$set(
            this.formData,
            'projectId',
            Number(detailData.data.data.projectId)
          )
          console.log('🚀 ~ showEdit ~ this.formData:', this.formData)
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createUser: resL,
            createdTime: this.getCurrentDate(),
          }
          this.addPro()
        }
        // this.formData.qualityItems = []
        // this.getAvailableScoreItems(1)
      },
      addPro() {
        this.formData.qualityItems.push({
          baseScore: '', //基础分
          increaseScore: '', // 增加分
          increaseScoreReason: '', // 增加分原因
          projectRole: '', // 项目角色
          projectEvaluationId: '', // 项目角色
          userId: '', // 项目组人员名称
          userName: '', // 项目组人员名称
        })
      },
      // getAvailableScoreItems(type) {
      //   // 根据id获取评分标准（子table表单）
      //   qualityScoreDetail({ type: type }).then((res) => {
      //     console.log('根据id获取评分标准', res)
      //     // 处理数据，满足表单结构
      //     if (res) {
      //       const data = res.data.data
      //       data.forEach((item, index) => {
      //         this.formData.qualityItems.push({
      //           score: this.detailScoreData[index]?.score || null, // 评议得分
      //           deductReason: this.detailScoreData[index]?.deductReason || '', // 扣分项
      //           smItem: {
      //             id: item.id,
      //             score: item.score,
      //             scoreContent: item.scoreContent,
      //             smId: item.smId,
      //             sort: item.sort,
      //           },
      //         })
      //       })
      //     }
      //   })
      // },
      close() {
        this.detailScoreData = []
        this.formData = {
          id: '',
          type: 1,
          project: {
            prjoectId: null,
            prjoectName: '',
          },
          org: {
            orgid: null,
            orgname: '',
          },
          orgId: null,
          qualityItems: [], // 考核内容
          score: 0,
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        // 保存提交
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            params.orgId = this.formData.org.orgid
            delete this.formData['createUser']
            const res = await projectEvalScoreUpdate(params)
            if (res && res.code === 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },
      async checkLink() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
      },
      delRow(index, row) {
        this.formData.qualityItems.splice(index, 1)
      },
      chooseRe(index) {
        this.$refs.person.show()
        this.chooseType = 1
        this.chooseIndex = index
      },
      selected(val) {
        if (this.chooseType == 1) {
          this.formData.qualityItems[this.chooseIndex].userId = val.staffid
          this.formData.qualityItems[this.chooseIndex].userName = val.realname
        }
        console.log('🚀 ~ selected ~ val:', val)
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      async getChildlistPro(val) {
        this.staffid = val[0].staffid
        this.$set(this.formData, 'orgName', val[0].realname)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
  .mb20 {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    flex-direction: row-reverse;
  }
</style>
