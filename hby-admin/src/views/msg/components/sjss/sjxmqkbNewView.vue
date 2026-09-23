<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-col :span="24">
        <!-- <div style="text-align: right; margin-bottom: 5px" v-if="footer">
          <el-button type="success" @click="handleAdd">选择项目</el-button>
        </div> -->
        <el-table :data="list">
          <el-table-column align="center" label="项目编号" width="100">
            <template #default="{ row }">
              {{ row.implementPlanEntities.qdcode }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="项目名称">
            <template #default="{ row }">
              {{ row.implementPlanEntities.projectName }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="时间" width="300">
            <template #default="{ row }">
              <el-date-picker
                v-model="row.weekDate"
                type="date"
                placeholder="选择日期"
                :disabled="!footer"
              ></el-date-picker>
            </template>
          </el-table-column>
          <el-table-column align="center" label="现场实际结束时间" width="300">
            <template #default="{ row }">
              <el-date-picker
                v-model="row.endTimeSite"
                type="date"
                placeholder="选择日期"
                :disabled="!footer"
              ></el-date-picker>
            </template>
          </el-table-column>
          <el-table-column align="center" label="本周工作 " width="300">
            <template #default="{ row }">
              <el-input
                v-model="row.weekWork"
                type="textarea"
                :rows="6"
                :disabled="!footer"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="下周工作 " width="300">
            <template #default="{ row }">
              <el-input
                v-model="row.nextWeekWork"
                type="textarea"
                :disabled="!footer"
                :rows="6"
              ></el-input>
            </template>
          </el-table-column>
          <!-- <el-table-column align="center" label="操作" v-if="footer">
            <template #default="{ row }">
              <el-button type="text" @click="remove(row)">删除</el-button>
            </template>
          </el-table-column> -->
        </el-table>
      </el-col>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button type="primary" @click="ymsubmit" :disabled="btnLoading">
        提 交
      </el-button>
    </div>
    <implementPlanSuccess ref="implement" @save="saveImplement" />
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
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import {
    weerklySaveOrUpdate,
    projectAuditProjectById,
  } from '@/oapi/audit/implement'
  import implementPlanSuccess from '@/views/oilAudit/implement/components/implementPlanSuccess.vue'
  const token = store.getters['user/token']

  export default {
    components: { implementPlanSuccess, Resubmit },
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
        // 流程相关
        fromId: '',
        fromIdcopy: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        isDel: false,
        btnLoading: false,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async ymsubmit() {
        let params = this.list
        for (let project of params) {
          if (
            !project.weekDate ||
            !project.weekWork ||
            !project.nextWeekWork ||
            !project.endTimeSite
          ) {
            this.$message.error('请填写全部信息')
            return // 直接返回
          }
        }
        try {
          this.btnLoading = true
          this.$refs.resubmit.ymsubmit()
        } catch (error) {
          this.btnLoading = false
        }
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        this.dialogFormVisible = true
        // 流程相关
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (formId) {
          const res = await projectAuditProjectById({ id: formId })
          this.list = [res.data]
        }
        if (title == 'edit') {
          this.title = '编辑'
          this.isDel = false
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          this.isDel = true
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            creater: resL,
            createTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.$bus.$emit('updateMsg', 0)
        this.list = []
        this.dialogFormVisible = false
        this.footer = true
      },
      remove(row) {
        const index = this.list.indexOf(row)
        if (index > -1) {
          this.list.splice(index, 1)
        }
      },
      async save() {
        let params = this.list
        if (this.isDel) {
          params.forEach((v) => {
            v.implementId = v.id
            delete v.id
          })
        }
        for (let project of params) {
          if (!project.weekDate || !project.weekWork || !project.nextWeekWork) {
            this.$message.error('请填写全部信息')
            return // 直接返回
          }
        }
        const { data, code, msg } = await weerklySaveOrUpdate(params)
        if (code == 1) {
          this.$emit('fetchData')
          this.$baseMessage(msg, 'success')
        } else {
          this.$baseMessage(msg, 'error')
        }
      },
      saveImplement(data) {
        console.log(data, 'data')
        this.list = [
          {
            ...data[0],
            nextWeekWork: '',
            weekWork: '',
            implementPlanEntities: {
              qdcode: data[0].qdcode,
              projectName: data[0].projectName,
            },
          },
        ]
      },
      // 添加点击按钮
      handleAdd() {
        this.$refs.implement.show()
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
