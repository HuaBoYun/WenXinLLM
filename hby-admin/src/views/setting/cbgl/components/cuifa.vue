<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="130px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="24">
          <el-divider>催办内容</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="催办类型" prop="reminderId">
            <el-select
              v-model="formData.reminderId"
              placeholder="催办类型"
              style="width: 100%"
            >
              <el-option label="自动催办" value="1" />
              <el-option label="手动催办" value="2" />
              <el-option label="定制催办" value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="催办周期类型"
            prop="reminderType"
            v-if="formData.reminderId == '1'"
          >
            <el-select
              v-model="formData.reminderType"
              placeholder="催办周期类型"
              style="width: 100%"
            >
              <el-option label="月" value="1" />
              <el-option label="周" value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col
          :span="12"
          v-if="formData.reminderType == '1' && formData.reminderId == '1'"
        >
          <el-form-item label="日期" prop="reminderMonth">
            <el-input
              placeholder="请输入内容"
              v-model="formData.reminderMonth"
              style="width: 100%"
            >
              <template slot="prepend">每月</template>
              <template slot="append">号</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="周"
            prop="reminderWeek"
            v-if="formData.reminderType == '2' && formData.reminderId == '1'"
          >
            <el-input
              placeholder="请输入内容"
              v-model="formData.reminderWeek"
              style="width: 100%"
            >
              <template slot="prepend">每周</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="催办人"
            prop="reminderStaffIdsNames"
            v-if="formData.reminderId != '3'"
          >
            <el-input
              v-model="formData.reminderStaffIdsNames"
              clearable
              placeholder="请选择催办人"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              type="primary"
              size="mini"
              style="margin-left: 3px"
              @click="showTeamMembers"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.reminderId == '3'">
          <el-form-item label="催办业务表名" prop="reminderBusinessTable">
            <el-input
              v-model="formData.reminderBusinessTable"
              clearable
              placeholder="请输入催办业务表名"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.reminderId == '3'">
          <el-form-item label="催办业务表主键名" prop="reminderBusinessId">
            <el-input
              v-model="formData.reminderBusinessId"
              clearable
              placeholder="请输入催办业务表主键名"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.reminderId == '3'">
          <el-form-item label="催办业务人员" prop="reminderBusinessStaff">
            <el-input
              v-model="formData.reminderBusinessStaff"
              clearable
              placeholder="请输入催办业务人员"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.reminderId == '3'">
          <el-form-item label="催办业务时间" prop="reminderBusinessTime">
            <el-input
              v-model="formData.reminderBusinessTime"
              clearable
              placeholder="请输入催办业务时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.reminderId == '3'">
          <el-form-item label="类型名称" prop="typeName">
            <el-input
              v-model="formData.typeName"
              clearable
              placeholder="类型名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.reminderId == '3'">
          <el-form-item label="提前催办日" prop="dateTime">
            <el-input-number
              v-model="formData.dateTime"
              clearable
              placeholder="提前催办日"
              :style="{ width: '100%' }"
              precision="0"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.reminderId == '3'">
          <el-form-item label="催办业务内容" prop="reminderBusinessContent">
            <el-input
              v-model="formData.reminderBusinessContent"
              clearable
              placeholder="请输入催办业务内容"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="催办内容" prop="reminderContent">
            <el-input
              v-model="formData.reminderContent"
              clearable
              placeholder="请输入催办内容"
              type="textarea"
              rows="4"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <select-person
      ref="selectP"
      @projectManage="selectTeamListP"
    ></select-person>
  </el-dialog>
</template>

<script>
  import selectPerson from '@/components/selectPerson.vue'
  import { getCBDetail, saveCBInfo } from '@/api/setting/report'
  export default {
    name: 'FlawInfo',
    components: { selectPerson },
    props: [],
    data() {
      return {
        formData: {
          reminderContent: undefined,
          reminderId: undefined,
          reminderType: undefined,
          reminderMonth: undefined,
          reminderWeek: undefined,
          refopmId: undefined,
          id: undefined,
          reminderStaffIdsNames: '',
          reminderStaffIds: '',
          reminderStaffIdsArr: [],
          moduleRoute: '', //模块id，用于区分是哪个模块的数据
          reminderBusinessTable: '',
          reminderBusinessStaff: '',
          reminderBusinessTime: '',
          reminderBusinessContent: '',
          reminderBusinessId: '',
          dateTime: '',
          typeName: '',
        },
        footer: true,
        tableData: [],
        rules: {
          reformcarryout: [
            {
              required: false,
              message: '请输入整改落实情况',
              trigger: 'blur',
            },
          ],
          reminderContent: [
            {
              validator: (rule, value, callback) => {
                if (!value && !this.formData.reminderBusinessContent) {
                  callback(new Error('催办内容和催办业务内容至少填写一个'))
                } else {
                  callback()
                }
              },
              trigger: 'blur',
            },
          ],
          reminderBusinessContent: [
            {
              validator: (rule, value, callback) => {
                if (!value && !this.formData.reminderContent) {
                  callback(new Error('催办内容和催办业务内容至少填写一个'))
                } else {
                  callback()
                }
              },
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        footer: true,
      }
    },
    created() {},
    methods: {
      showEdit(row, type, mode) {
        //模块id，用于区分是哪个模块的数据
        if (mode) {
          this.formData.moduleRoute = mode
        }

        this.dialogFormVisible = true
        this.formData.refopmId = 12
        if (row) {
          getCBDetail({ id: row.id }).then((res) => {
            if (res.code == 200) {
              this.formData = {
                ...res.data,
                refopmId: 123,
                reminderId: res.data.reminderId
                  ? res.data.reminderId.toString()
                  : '',
                reminderType: res.data.reminderType
                  ? res.data.reminderType.toString()
                  : '',
              }
              let arrStr = ''
              let arr = []
              arr = res.data.reminderStaffIdsNames.map((item) => item.realName)
              arrStr = arr.join(',')
              this.formData.reminderStaffIdsNames = arrStr
              this.formData.reminderStaffIdsArr = res.data.reminderStaffIdsNames
              //拿到组员id字符串
              let arrStrZy = ''
              let arrZy = []
              arrZy = res.data.reminderStaffIdsNames.map((item) => item.staffId)
              arrStrZy = arrZy.join(',')
              this.formData.reminderStaffIds = arrStrZy
            }
          })
        }
        if (type == 'detail') {
          this.footer = false
        }
      },

      close() {
        this.formData = {
          reminderContent: undefined,
          reminderId: undefined,
          reminderType: undefined,
          reminderMonth: undefined,
          reminderWeek: undefined,
          refopmId: undefined,
          id: undefined,
          reminderStaffIdsNames: '',
          reminderStaffIds: '',
          reminderStaffIdsArr: [],
          moduleRoute: '', //模块id，用于区分是哪个模块的数据
          reminderBusinessTable: '',
          reminderBusinessStaff: '',
          reminderBusinessTime: '',
          reminderBusinessContent: '',
          reminderBusinessId: '',
          dateTime: '',
          typeName: '',
        }
        this.dialogFormVisible = false
        this.footer = true
        this.disabled = false
      },
      add() {
        this.$refs.elForm.validate(async (valid) => {
          if (valid) {
            const data = await saveCBInfo({
              ...this.formData,
              reminderStaffIdsNames: this.reminderStaffIdsArr,
            })
            if (data.code == 200) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
              this.close()
            }
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },

      showTeamMembers() {
        this.$refs['selectP'].showEdit()
      },
      selectTeamListP(val) {
        console.log(val, 'val')
        let arrStr = ''
        let arr = []
        arr = val.map((item) => item.realname)
        arrStr = arr.join(',')
        this.formData.reminderStaffIdsNames = arrStr
        this.formData.reminderStaffIdsArr = val

        //拿到组员id字符串
        let arrStrZy = ''
        let arrZy = []
        arrZy = val.map((item) => item.staffid)
        arrStrZy = arrZy.join(',')
        this.formData.reminderStaffIds = arrStrZy
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
