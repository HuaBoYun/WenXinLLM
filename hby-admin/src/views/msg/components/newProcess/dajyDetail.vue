<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="ruleForm"
        label-width="125px"
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
              style="width: 100%"
              :disabled="disabled"
              @change="changeMJ"
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
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目编号" prop="projectCode">
            <el-input
              v-model="formData.projectCode"
              clearable
              disabled
              placeholder="请输入项目编号"
              style="width: 346px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="prjoectName">
            <el-input
              v-model="formData.prjoectName"
              clearable
              disabled
              placeholder="请输入项目名称"
              style="width: 346px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="borrowDate" label="借阅日期">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              :disabled="disabled"
              v-model="formData.borrowDate"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 346px"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="backDate" label="归还日期">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              :disabled="disabled"
              v-model="formData.backDate"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 346px"
            ></el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="借阅人" prop="staffname">
            <el-input
              v-model="formData.staffname"
              clearable
              disabled
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
      </el-form>
    </el-row>

    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>

      <el-button type="primary" @click="ymsubmit" :disabled="btnLoading">
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
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import { getTjspBorrowDetail } from '@/api/audit/archives'

  import { baseURL } from '@/config'

  import { formatDay } from '@/utils'
  import { TjspBorrow } from '@/api/audit/archives'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  export default {
    name: 'ReferenceDetail',
    components: { Resubmit, ZXPerson },

    props: {},
    data() {
      return {
        dialogFormVisible: false,
        activeName: 'first',
        disabled: false,
        formData: {
          projectCode: '',
          memo: '',
          returnDate: '',
          prjoectName: '',
          createDate: '',
          optDesc: '',
          staffname: '',
          borrowDate: '',
          backDate: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
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
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        btnLoading: false,
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
      }
    },
    watch: {},
    async created() {
      // this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('AuditRecordRead')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
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
      async showEdit(title, row, fromId, flowtaskinfoflowid, ymFromId, status) {
        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
        this.status = status
        this.title = title
        // this.dialogFormVisible = true

        if (title == '编辑') {
          this.disabled = false
        } else if (title == '详细') {
          this.disabled = true
        }

        const res = await getTjspBorrowDetail({ borrowId: row.id })
        if (res.code == 1) {
          const {
            prjoectname,
            projectcode,
            borrowDate,
            backDate,
            createDate,
            returnDate,
            ...other
          } = res.data
          this.formData = {
            prjoectName: prjoectname,
            projectCode: projectcode,
            borrowDate: formatDay(createDate),
            backDate: formatDay(returnDate),
            ...other,
          }
        }
      },
      close() {
        this.dialogFormVisible = false

        this.$bus.$emit('updateMsg', 0)
      },
      async add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (!valid) {
            return false
          } else {
            const { borrowDate, backDate, ...other } = this.formData
            let obj = {
              ...other,
              borrowDate: formatDay(borrowDate),
              backDate: formatDay(backDate),
            }
            delete obj.prjoectName
            delete obj.projectCode
            delete obj.createDate
            delete obj.returnDate
            delete obj.tblproject
            delete obj.tblstaff

            // TjspBorrow(obj).then((res) => {
            //   // this.$baseMessage(res, 'success', 'vab-hey-message-success')
            //   this.$emit('fetch-data')
            //   this.close()
            //
            // })
            const res = await TjspBorrow(obj)
            if (res.code == 1) {
              this.$message.success('保存成功')
            } else {
              this.$message.error('提交失败')
            }
          }
        })
      },
      //提交
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
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
