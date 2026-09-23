<template>
  <el-dialog
    title="档案借阅"
    @close="close"
    :visible.sync="dialogFormVisible"
    width="1000px"
    :close-on-click-modal="false"
  >
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="基本信息" name="first">
        <h3>档案借阅</h3>
        <el-row :gutter="15">
          <el-form
            ref="ruleForm"
            label-width="125px"
            :model="formData"
            :rules="rules"
            size="mini"
          >
            <el-col :span="12">
              <el-form-item label="档案名称" prop="prjoectName">
                <el-input
                  v-model="formData.prjoectName"
                  clearable
                  :disabled="disabled"
                  placeholder="请输入档案编号"
                  style="width: 346px"
                />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="档案编号" prop="projectCode">
                <el-input
                  v-model="formData.projectCode"
                  clearable
                  :disabled="disabled"
                  placeholder="请输入档案编号"
                  style="width: 346px"
                />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item prop="createDate" label="借阅日期">
                <el-date-picker
                  type="date"
                  placeholder="选择日期"
                  :disabled="disabled"
                  v-model="formData.createDate"
                  style="width: 346px"
                ></el-date-picker>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item prop="returnDate" label="归还日期">
                <el-date-picker
                  type="date"
                  placeholder="选择日期"
                  :disabled="disabled"
                  v-model="formData.returnDate"
                  style="width: 346px"
                ></el-date-picker>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="借阅人" prop="staffname">
                <el-input
                  v-model="formData.staffname"
                  clearable
                  :disabled="disabled"
                  style="width: 346px"
                />
              </el-form-item>
            </el-col>

            <el-col :span="24">
              <el-form-item label="借阅事由" prop="memo">
                <el-input
                  type="textarea"
                  v-model="formData.memo"
                  :style="{ width: '100%' }"
                  :disabled="disabled"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>审批意见</el-divider>
            </el-col>
            <el-col :span="24" style="margin-bottom: 16px">
              <el-table :data="aoptionList">
                <el-table-column
                  align="center"
                  label="审批人"
                  width="200px"
                  prop="staffidName"
                />
                <el-table-column align="center" label="意见" prop="optDesc" />
                <el-table-column
                  align="center"
                  label="时间"
                  prop="createDate"
                />
                <el-table-column align="center" label="结果" prop="optState" />
              </el-table>
            </el-col>

            <el-col :span="24" v-if="this.title !== '传阅'">
              <el-form-item label="审批意见" prop="optDesc">
                <el-input
                  type="textarea"
                  v-model="formData.optDesc"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="审批查看" name="second">
        <img :src="imgSrc" alt="" />
      </el-tab-pane>
    </el-tabs>

    <template #footer v-if="this.title !== '传阅'">
      <div v-if="btnList.length > 0">
        <el-button
          v-for="(item, index) in btnList"
          :key="index"
          type="primary"
          @click="onSubmit(item)"
        >
          {{ item }}
        </el-button>
      </div>
      <div v-else>
        <el-button
          type="primary"
          :disabled="disabled"
          @click="onSubmit('提交')"
        >
          提交
        </el-button>
        <el-button type="primary" :disabled="disabled" @click="save()">
          保存
        </el-button>
      </div>
      <!-- <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button> -->
    </template>
  </el-dialog>
</template>

<script>
  import {
    DealRecordApporvalInfo,
    GetRecordApprovalInfo,
    SubmitRecordApproval,
  } from '@/api/audit/archives'

  import { baseURL } from '@/config'
  export default {
    name: 'ReferenceDetail',
    components: {},

    props: {},
    data() {
      return {
        dialogFormVisible: false,
        activeName: 'first',
        disabled: false,
        title: '',
        formData: {
          projectCode: '',
          memo: '',
          returnDate: '',
          prjoectName: '',
          createDate: '',
          optDesc: '',
          staffname: '',
        },
        aoptionList: [],
        rules: {
          prjoectName: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'blur',
            },
          ],
          projectCode: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'blur',
            },
          ],
          createDate: [
            {
              required: true,
              message: '请选择借阅日期',
              trigger: 'blur',
            },
          ],
          returnDate: [
            {
              required: true,
              message: '请选择归还日期',
              trigger: 'blur',
            },
          ],
          optDesc: [
            {
              required: true,
              message: '请输入审批意见',
              trigger: 'blur',
            },
          ],
          staffname: [
            {
              required: true,
              // message: '请输入审批意见',
              trigger: 'blur',
            },
          ],
        },
        btnList: [],
        borrowid: '',
        taskId: '',
        imgSrc: '',
        baseURL: baseURL,
      }
    },
    watch: {},
    created() {},
    methods: {
      async showEdit(title, row) {
        this.title = title

        this.dialogFormVisible = true

        let obj = {
          cyId: row.cyid,
          borrowid: row.cyurl.split('=')[1],
          taskId: row.taskid,
        }

        const {
          data: {
            borrowrecord,
            btnList,
            cy,
            project,
            cyId,
            taskId,
            aoptionList,
          },
        } = await GetRecordApprovalInfo(obj)

        // 这里是有数据的  borrowrecord  project
        if (btnList && btnList[0] != null) {
          this.btnList = btnList
        }
        this.formData = {
          createDate: borrowrecord.createDate,
          returnDate: borrowrecord.returnDate,
          memo: borrowrecord.memo,
          prjoectName: project.prjoectName,
          projectCode: project.projectCode,
          staffname: borrowrecord.staffname,
        }
        if (cy.cystate == '审批中') {
          this.disabled = true
        } else if (cy.cystate == '需调整') {
          this.disabled = true
        }
        this.borrowid = borrowrecord.borrowid
        this.cyId = cyId
        this.taskId = taskId

        // aoptionList.createDate = aoptionList.createDate.split('T')[0]
        aoptionList.forEach((v) => {
          v.createDate = v.createDate.split('T')[0]
        })

        this.aoptionList = aoptionList
        this.getImg(taskId)
      },
      close() {
        this.dialogFormVisible = false
      },
      onSubmit(item) {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let obj = {
              optDesc: this.formData.optDesc,
              cyId: this.cyId,
              taskId: this.taskId,
              borrowid: this.borrowid,
              transition: item,
            }

            const { msg } = await DealRecordApporvalInfo(obj)
            if (msg) {
              this.$message.success(msg)
              this.dialogFormVisible = false
            }
          }
        })
      },
      async getImg(taskId) {
        this.imgSrc =
          await `${this.baseURL}/audit/nbsjapproval/picture?taskId=${taskId}`
      },
      async save() {
        const { msg } = await SubmitRecordApproval({ borrowid: this.borrowid })
        if (msg == '成功') {
          this.dialogFormVisible = false
        }
      },
    },
  }
</script>

<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .el-table {
    margin-top: 10px;
    margin-bottom: 18px;
  }
</style>
