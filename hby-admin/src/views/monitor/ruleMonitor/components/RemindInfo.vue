<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :destroy-on-close="true"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="125px"
          size="medium"
          :model="formData"
          :disabled="disabledForm"
          :rules="rules"
        >
          <el-col :span="12">
            <el-form-item label="方案编号" prop="solutioncode">
              <el-input
                v-model="formData.solutioncode"
                placeholder="请输入方案编号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方案名称" prop="solutionname">
              <el-input
                v-model="formData.solutionname"
                placeholder="请输入方案名称"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="方案状态">
              <el-radio-group v-model="formData.solutionstatus">
                <el-radio label="启用" value="1" />
                <el-radio label="停用" value="2" />
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人" prop="creater">
              <el-input
                v-model="creater"
                placeholder="创建人"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.createdate">
            <el-form-item label="创建日期">
              <el-date-picker
                v-model="formData.createdate"
                type="date"
                placeholder="选择日期"
                format="yyyy - MM - dd "
                value-format="yyyy-MM-dd"
                style="width: 344px"
                :disabled="true"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="formData.memo"
                type="textarea"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <div
              v-if="!disabledForm"
              style="text-align: right; margin-bottom: 5px"
            >
              <el-button type="success" @click="handleAdd">添加指标</el-button>
            </div>
            <el-table :data="tableData">
              <el-table-column align="center" label="规则ID" prop="ruleid" />
              <el-table-column
                align="center"
                label="规则名称"
                prop="rulename"
              />
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer v-if="!disabledForm">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
    <InfoEdit ref="edit" @showRuleTable="showRuleTable" />
  </div>
</template>

<script>
  import {
    ToSolutionModify,
    ToSolutionAdd,
    SolutionAdd,
  } from '@/api/monitor/rule/index'
  import InfoEdit from './InfoEdit.vue'
  export default {
    name: 'RemindInfo',
    components: { InfoEdit },
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        tableData: [],
        formData: {
          solutionname: '',
          solutioncode: '',
          solutionstatus: 1,
          memo: '无',
        },
        creater: '',
        disabledForm: false,
        rules: {
          solutioncode: [
            { required: true, trigger: 'blur', message: '请输入方案编号' },
          ],
          solutionstatus: [
            { required: true, trigger: 'blur', message: '请选择方案状态' },
          ],
          solutionname: [
            { required: true, trigger: 'blur', message: '请输入方案名称' },
          ],
          // creater: [
          //   { required: true, trigger: 'blur', message: '请输入创建人' },
          // ],
        },
        orgid: '',
      }
    },
    created() {},
    methods: {
      showEdit(row, flag, orgid) {
        this.orgid = orgid
        this.dialogFormVisible = true

        if (flag == 'add') {
          this.title = '新增'
          this.formData = {}
          this.formData.solutionstatus = '启用'
          // this.formData.createdate = new Date()
          ToSolutionAdd({ orgId: this.orgid }).then((res) => {})
          this.creater = JSON.parse(localStorage.getItem('userInfo')).realname

          this.disabledForm = false
        } else if (flag == 'look') {
          this.formData = row
          this.title = '详细信息'
          this.disabledForm = true
        } else if (flag == 'edit') {
          this.formData = row

          this.title = '修改'
          this.disabledForm = false
          const params = {
            href: '',
            selectedid: row.solutionid,
          }
          var that = this
          ToSolutionModify(params).then((res) => {
            this.tableData = res.data.solutionruleList
            if (res.data == '方案已执行，不能修改！') {
              that.disabledForm = true
              that.$baseMessage(res.msg, 'error', 'vab-hey-message-error')
            } else {
              this.$nextTick(function () {
                this.creater = JSON.parse(
                  localStorage.getItem('userInfo')
                ).realname
              })

              this.disabledForm = false
            }
          })
        }
      },
      close() {
        this.dialogFormVisible = false
        this.$emit('fetch-data')
      },
      showRuleTable(val) {
        this.tableData = val
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const form = {
              solutionstatus: this.formData.solutionstatus || '',
              solutionname: this.formData.solutionname,
              solutioncode: this.formData.solutioncode,
              memo: this.formData.memo || '',
            }
            const data = await SolutionAdd(form)
            if (data.code == 200) {
              this.$message.success(data.msg)
            } else {
              this.$message.error(data.msg)
            }
          }
        })
      },
      handleAdd() {
        if (this.formData && this.formData.solutionid) {
          this.$refs['edit'].showEdit(this.formData)
        } else {
          this.$baseMessage(
            '请先保存以上方案，再添加规则',
            'error',
            'vab-hey-message-error'
          )
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
