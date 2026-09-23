<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="24">
          <el-form-item label="来访事由" prop="visitorsReason">
            <el-input
              v-model="formData.visitorsReason"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入来访事由"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="接待类别" prop="receiverType">
            <el-radio-group v-model="formData.receiverType">
              <el-radio label="商务接待">商务接待</el-radio>
              <el-radio label="外事接待">外事接待</el-radio>
              <el-radio label="其他公务接待">其他公务接待</el-radio>
              <el-radio label="集团公司内部接待">集团公司内部接待</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="来访起止时间" prop="visitorsTime">
            <el-date-picker
              v-model="formData.visitorsTime"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="来宾人数" prop="visitorsNum">
            <el-input
              v-model="formData.visitorsNum"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入来宾人数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="党委办公室协助安排事宜" prop="arrangeMatters">
            <el-input
              v-model="formData.arrangeMatters"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入党委办公室协助安排事宜"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="陪同人员" prop="accompanyName">
            <el-input
              v-model="formData.accompanyName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请选择陪同人员"
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 20px"
              @click="selectDept('accompanyName')"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="申请单位负责人"
            prop="applyBelongGroupCreatorName"
          >
            <el-input
              v-model="formData.applyBelongGroupCreatorName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择申请单位负责人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="selectDept('applyBelongGroupCreatorName')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="党委办公室负责人" prop="officeManagerName">
            <el-input
              v-model="formData.officeManagerName"
              :style="{ width: '75%' }"
              clearable
              placeholder="请选择党委办公室负责人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="selectDept('officeManagerName')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="申请单位经办人"
            prop="applyBelongGroupOperatorName"
          >
            <el-input
              v-model="formData.applyBelongGroupOperatorName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择申请单位经办人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="selectDept('applyBelongGroupOperatorName')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input
              v-model="formData.contactNumber"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入联系电话"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>主要来访人员情况</el-divider>
        </el-col>
        <el-col :span="24">
          <div v-if="footer" style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="handleAdd">增加一行</el-button>
          </div>
          <el-table
            border
            :data="tableData"
            fit
            highlight-current-row
            style="width: 100%; margin-bottom: 25px"
          >
            <el-table-column align="center" label="姓名" prop="n'a'm'e">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.name"
                  size="mini"
                  :disabled="disabled"
                  style="width: 90%"
                />
              </template>
            </el-table-column>

            <el-table-column align="center" label="单位及职务" prop="dname">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.dname"
                  size="mini"
                  style="width: 90%"
                />
              </template>
            </el-table-column>

            <el-table-column align="center" label="级别" prop="level">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.level"
                  size="mini"
                  style="width: 90%"
                />
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="操作"
              min-width="40"
              v-if="footer"
            >
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleDelete(scope.row, scope.$index)"
                  :disabled="disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <!-- 申请人 -->
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <project-manage @projectManage="getChildlistPro" ref="department" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/views/oilAudit/report/components/options/executor.vue'
  import { edit, getInfoDetail } from '@/oapi/ypns_zhgl/gwjd.js'
  import projectManage from '@/components/danxuanPerson.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const token = store.getters['user/token']

  export default {
    components: {
      ExecutorOptions,
      DepartmentOptions,
      projectManage,
      Resubmit,
    },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          id: '',
          visitorsReason: '',
          receiverType: '',
          visitorsEndTime: '',
          visitorsStartTime: '',
          visitorsNum: '',
          arrangeMatters: '',
          accompanyName: '',
          accompanyId: '',
          applyBelongGroupCreatorName: '',
          applyBelongGroupCreator: '',
          contactNumber: '',
          applyBelongGroupOperator: '',
          applyBelongGroupOperatorName: '',
          officeManagerId: '',
          officeManagerName: '',
          visitorsJson: [],
          visitorsTime: [],
        },
        radio: '',
        footer: true,
        rules: {
          applyPeopleName: [
            {
              required: true,
              message: '请选择申请人',
              trigger: ['blur', 'change'],
            },
          ],
          applyBelongGroupName: [
            {
              required: true,
              message: '请选择部门/单位',
              trigger: ['blur', 'change'],
            },
          ],
          applyWorkUnitName: [
            {
              required: true,
              message: '请选择科室',
              trigger: ['blur', 'change'],
            },
          ],
          networkInterface: [
            {
              required: true,
              message: '请输入网络接口(T)',
              trigger: 'blur',
            },
          ],
          officeArea: [
            {
              required: true,
              message: '请输入办公区',
              trigger: 'blur',
            },
          ],
          roomNumber: [
            {
              required: true,
              message: '请输入房间号',
              trigger: 'blur',
            },
          ],
          equipmentType: [
            {
              required: true,
              message: '请输入设备类型',
              trigger: 'blur',
            },
          ],
          purpose: [
            {
              required: true,
              message: '请输入用途',
              trigger: 'blur',
            },
          ],
          applyPeriodTimeStart: [
            {
              required: true,
              message: '请选择使用期限',
              trigger: ['blur', 'change'],
            },
          ],
          externalNetworkPermissions: [
            {
              required: true,
              message: '请输入外网权限',
              trigger: 'blur',
            },
          ],
          applyTime: [
            {
              required: true,
              message: '选择申请时间',
              trigger: ['blur', 'change'],
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        deptType: '',
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      selectDept(type) {
        this.deptType = type
        this.$refs.department.showEdit()
      },
      getChildlistPro(val) {
        if (this.deptType == 'accompanyName') {
          this.formData.accompanyId = val[0].staffid
          this.formData.accompanyName = val[0].realname
        } else if (this.deptType == 'applyBelongGroupCreatorName') {
          this.formData.applyBelongGroupCreator = val[0].staffid
          this.formData.applyBelongGroupCreatorName = val[0].realname
        } else if (this.deptType == 'applyBelongGroupOperatorName') {
          this.formData.applyBelongGroupOperator = val[0].staffid
          this.formData.applyBelongGroupOperatorName = val[0].realname
        } else if (this.deptType == 'officeManagerName') {
          this.formData.officeManagerId = val[0].staffid
          this.formData.officeManagerName = val[0].realname
        }
      },
      changeApplyPeriod(val) {
        if (val && val.length) {
          this.$set(this.formData, 'applyPeriodTimeStart', val[0])
          this.$set(this.formData, 'applyPeriodTimeEnd', val[1])
        } else {
          this.$set(this.formData, 'applyPeriodTimeStart', '')
          this.$set(this.formData, 'applyPeriodTimeEnd', '')
        }
      },
      handleExecutorSelected(node) {
        this.$set(this.formData, 'applyPeopleName', node.realname)
        this.$set(this.formData, 'applyPeople', node.staffid)
        this.$forceUpdate()
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(
        title,
        row,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.dialogFormVisible = true
        this.footer = isWfqdedit
        this.fromId = row
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        if (row) {
          const res = await getInfoDetail({ id: row })
          Object.keys(this.formData).forEach(
            (key) => (this.formData[key] = res.data.data[key])
          )
          this.tableData = JSON.parse(res.data.data.visitorsJson)
          if (
            res.data.data.visitorsStartTime &&
            res.data.data.visitorsEndTime
          ) {
            this.formData.visitorsTime = [
              res.data.data.visitorsStartTime,
              res.data.data.visitorsEndTime,
            ]
          }
          this.formData.id = res.data.data.id
          // this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
          this.footer = true
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          visitorsReason: '',
          receiverType: '',
          visitorsEndTime: '',
          visitorsStartTime: '',
          visitorsNum: '',
          arrangeMatters: '',
          accompanyName: '',
          accompanyId: '',
          applyBelongGroupCreatorName: '',
          applyBelongGroupCreator: '',
          contactNumber: '',
          applyBelongGroupOperator: '',
          applyBelongGroupOperatorName: '',
          officeManagerId: '',
          officeManagerName: '',
          visitorsJson: [],
          visitorsTime: [],
          id: '',
        }
        this.$bus.$emit('updateMsg', 0)
        this.dialogFormVisible = false
        this.footer = true
        this.tableData = []
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(
              JSON.stringify({
                ...this.formData,
                visitorsStartTime: this.formData.visitorsTime[0],
                visitorsEndTime: this.formData.visitorsTime[1],
                visitorsJson: JSON.stringify(this.tableData),
              })
            )
            const res = await edit(params)
            if (res && res.code == 200) {
              this.$emit('fetchData')
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
          }
        })
      },

      handleAdd() {
        this.tableData.push({
          dname: '',
          level: '',
          name: '',
        })
      },
      handleDelete(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            if (row.teamId) {
              proPjteamDel({ teamId: row.teamId }).then((res) => {
                if (res.code) {
                  this.tableData.splice(index, 1)
                  this.$message({
                    type: 'success',
                    message: '删除成功!',
                  })
                }
              })
            } else {
              this.tableData.splice(index, 1)
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      // 流程相关-提交
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
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
