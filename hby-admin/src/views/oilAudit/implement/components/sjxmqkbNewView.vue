<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <!-- <el-col :span="24">
        <div
          style="text-align: right; margin-bottom: 5px"
          v-if="title === '新增'"
        >
          <el-button type="success" @click="handleAdd">选择项目</el-button>
        </div>
        <el-table :data="list">
          <el-table-column
            align="center"
            label="项目编号"
            prop="qdcode"
            width="100"
          ></el-table-column>
          <el-table-column align="center" label="项目名称" prop="projectName" />
          <el-table-column align="center" label="周报时间" width="300">
            <template #default="{ row }">
              <el-date-picker
                v-model="row.weekDate"
                type="date"
                placeholder="选择日期"
              ></el-date-picker>
            </template>
          </el-table-column>
          <el-table-column align="center" label="现场结束时间" width="300">
            <template #default="{ row }">
              <el-date-picker
                v-model="row.endTimeSite"
                type="date"
                placeholder="选择日期"
              ></el-date-picker>
            </template>
          </el-table-column>
          <el-table-column align="center" label="本周周报内容" width="300">
            <template #default="{ row }">
              <el-input
                v-model="row.weekWork"
                type="textarea"
                :rows="6"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="下周周报内容" width="300">
            <template #default="{ row }">
              <el-input
                v-model="row.nextWeekWork"
                type="textarea"
                :rows="6"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" v-if="title === '新增'">
            <template #default="{ row }">
              <el-button type="text" @click="remove(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col> -->

      <el-form
        ref="ruleForm"
        label-width="160px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="项目编号" prop="qdcode">
            <el-input
              v-model="formData.qdcode"
              disabled
              placeholder="请输入项目编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              disabled
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="周报时间" prop="weekDate">
            <el-date-picker
              v-model="formData.weekDate"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择时间"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现场结束时间" prop="endTimeSite">
            <el-date-picker
              v-model="formData.endTimeSite"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择时间"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="本周周报内容" prop="weekWork">
            <el-input
              type="textarea"
              :rows="6"
              v-model="formData.weekWork"
              clearable
              placeholder="请输入本周周报内容"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="下周周报内容" prop="nextWeekWork">
            <el-input
              type="textarea"
              :rows="6"
              v-model="formData.nextWeekWork"
              clearable
              placeholder="请输入下周周报内容"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">
        确定
      </el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
    <implementPlanSuccess ref="implement" @save="saveImplement" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import { weerklySaveOrUpdate } from '@/oapi/audit/implement'
  import implementPlanSuccess from '@/views/oilAudit/implement/components/implementPlanSuccess.vue'
  const token = store.getters['user/token']
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { getCurrSsProject } from '@/oapi/audit/project'

  export default {
    components: { implementPlanSuccess, ProcessList },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/oiaudit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        footer: true,
        depType: '',
        manType: '',
        dialogFormVisible: false,
        title: '新增',
        list: [],
        isDel: false,
        editId: '',
        formData: {
          qdcode: '',
          projectName: '',
          weekDate: '',
          endTimeSite: '',
          weekWork: '',
          nextWeekWork: '',
        },
        rules: {
          qdcode: [
            {
              required: true,
              message: '请输入项目编号',
              trigger: 'blur',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'blur',
            },
          ],
          weekDate: [
            {
              required: true,
              message: '请输入周报时间',
              trigger: 'blur',
            },
          ],
          endTimeSite: [
            {
              required: true,
              message: '请输入现场结束时间',
              trigger: 'blur',
            },
          ],
          weekWork: [
            {
              required: true,
              message: '请输入本周周报内容',
              trigger: 'blur',
            },
          ],
          nextWeekWork: [
            {
              required: true,
              message: '请输入下周周报内容',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    methods: {
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      // 获取当前实施项目
      async getCurrentProject() {
        let obj = {}
        await getCurrSsProject().then((res) => {
          if (res.code === 1) {
            obj = res.data.pj
          } else {
            obj = undefined
          }
        })
        return obj
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true

        if (row) {
          this.editId = row.id
          this.formData = { ...row.implementPlanEntities, ...row }
          // this.list = [{ ...row.implementPlanEntities, ...row }]
          this.isDel = false
          // const res = await projectAuditProjectById({ projectId: row.id })
          // this.formData = res.data
        } else {
          this.isDel = true
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          const currentProject = await this.getCurrentProject()

          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            creater: resL,
            createTime: this.getCurrentDate(),
            ...currentProject,
          }
        }
      },
      close() {
        this.list = []
        this.formData = {
          qdcode: '',
          projectName: '',
          weekDate: '',
          endTimeSite: '',
          weekWork: '',
          nextWeekWork: '',
        }
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.editId = ''
        this.$emit('fetchData')
      },
      remove(row) {
        const index = this.list.indexOf(row)
        if (index > -1) {
          this.list.splice(index, 1)
        }
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let info = { ...this.formData, status: 0 }
            if (this.isDel) {
              info.implementId = info.id
              delete info.id
            }
            this.loading = true
            const data = await weerklySaveOrUpdate([info])
            console.log(data)
            if (data.code == 1) {
              this.editId = data.data.data.id
              this.formData.id = data.data.data.id
              this.$baseMessage(data.msg, 'success')
              this.$emit('fetchData')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.loading = false
          }
        })
      },
      // async save() {
      //   let params = this.list
      //   if (this.isDel) {
      //     params.forEach((v) => {
      //       v.implementId = v.id
      //       delete v.id
      //     })
      //   }
      //   for (let project of params) {
      //     if (
      //       !project.weekDate ||
      //       !project.weekWork ||
      //       !project.nextWeekWork ||
      //       !project.endTimeSite
      //     ) {
      //       this.$message.error('请填写全部信息')
      //       return // 直接返回
      //     }
      //   }
      //   const info = params.map((res) => {
      //     return { ...res, status: 0 }
      //   })
      //   this.loading = true
      //   const data = await weerklySaveOrUpdate(info)
      //   console.log(data)
      //   if (data.code == 1) {
      //     this.editId = data.data.id
      //     this.$baseMessage(data.msg, 'success')
      //   } else {
      //     this.$baseMessage(data.msg, 'error')
      //   }
      //   this.loading = false
      // },
      saveImplement(data) {
        this.list = data
      },
      // 添加点击按钮
      handleAdd() {
        this.$refs.implement.show()
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(156, this.editId)
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
</style>
