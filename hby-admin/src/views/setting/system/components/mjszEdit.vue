<template>
  <!-- 理论研究上报 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-form-item label="密级名称" prop="levelName">
            <el-input
              v-model="formData.levelName"
              placeholder="请输入密级名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="密级类型" prop="levelType">
            <el-select
              v-model="formData.levelType"
              clearable
              placeholder="密级类型"
              style="width: 100%"
              @change="handleData"
            >
              <el-option label="功能模块" :value="1" />
              <el-option label="业务单据" :value="2" />
              <el-option label="人员" :value="3" />
              <el-option label="附件" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="密级功能范围"
            prop="secrectMenuScope"
            v-if="formData.levelType != 4 && formData.levelType"
          >
            <el-select
              v-model="formData.secrectMenuScope"
              clearable
              placeholder="密级功能范围"
              style="width: 100%"
              multiple
            >
              <el-option
                v-for="item in Option"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="密级人员范围"
            prop="secrectStaffScope"
            v-if="formData.levelType == 2"
          >
            <el-select
              v-model="formData.secrectStaffScope"
              clearable
              placeholder="密级人员范围"
              style="width: 100%"
              multiple
            >
              <el-option
                v-for="item in Option1"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import {
    getSelectList,
    addData,
    editData,
    detailData,
  } from '@/api/setting/mjsz'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'mjszEdit',
    components: { ProcessList },
    data() {
      return {
        formData: {
          levelName: '',
          levelType: '',
          levelId: '',
          secrectMenuScope: [],
          secrectStaffScope: [],
        },
        rules: {
          tbrgname: [
            {
              required: true,
              message: '请选择填报单位',
              trigger: 'change',
            },
          ],
        },
        dialogJdVisible: false,
        disabled: false,
        title: '',
        Option: [],
        Option1: [],
        requireValuedata: false, // 是否需要流程校验
      }
    },
    created() {
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      async showEdit(title, row) {
        this.dialogJdVisible = true
        this.disabled = title == 'detail'
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.disabled = true
        } else {
          this.title = '新增'
          this.disabled = false
          this.formData = { ...this.formData }
        }
        if (row) {
          const { data } = await detailData({ levelId: row.levelId })
          console.log(data, 'data')
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = data[key]
            this.formData.levelType = +data.levelType
            this.handleData()
            this.formData.secrectMenuScope = data.secrectMenuScope
              .split(',')
              .map((res) => +res)
            this.formData.secrectStaffScope = data.secrectStaffScope
              .split(',')
              .map((res) => +res)
          })
        }
      },
      close() {
        this.formData = {
          levelName: '',
          levelType: '',
          levelId: '',
          secrectMenuScope: [],
          secrectStaffScope: [],
        }
        this.Option = []
        this.Option1 = []
        this.dialogJdVisible = false
        this.$emit('fetch-data')
      },

      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const fun = this.formData.levelId ? editData : addData
            const { msg, data, code } = await fun({
              ...this.formData,
              secrectMenuScope: this.formData.secrectMenuScope.toString(),
              secrectStaffScope: this.formData.secrectStaffScope.toString(),
            })
            // 流程校验
            if (this.requireValuedata && this.title != '新增') {
              //  查询当前是否有流程
              getFlowList({
                targetId: data.recordId,
                targetType: 'secrect',
                operationType: this.title == '新增' ? 1 : 2,
              }).then((res) => {
                if (res.data == 0) {
                  // 可以提交流程
                  this.$refs['process'].save(220, data.recordId)
                  this.$baseMessage(
                    '审批流程提交成功,请等待审批',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.close()
                } else {
                  // 不可以提交流程
                  this.$baseMessage(
                    '当前用户流程已存在,请先走审批流程',
                    'error',
                    'vab-hey-message-error'
                  )
                  return
                }
              })
            } else if (this.title == '新增' && this.requireValuedata) {
              // 可以提交流程
              this.$refs['process'].save(220, data.recordId)
              this.$baseMessage(
                '审批流程提交成功,请等待审批',
                'success',
                'vab-hey-message-success'
              )
              this.close()
            } else {
              if (code === 1) {
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                this.close()
              } else {
                this.$baseMessage(msg, 'error', 'vab-hey-message-error')
              }
            }
          }
        })
      },
      handleData() {
        this.formData.secrectMenuScope = []
        this.formData.secrectStaffScope = []
        if (this.formData.levelType == 4) return
        getSelectList({
          levelType: this.formData.levelType,
        }).then((res) => {
          if (this.formData.levelType == 1 || this.formData.levelType == 3) {
            this.Option = res.data.menuList
          }
          if (this.formData.levelType == 2) {
            this.Option = res.data.menuList
            this.Option1 = res.data.employeeList
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
