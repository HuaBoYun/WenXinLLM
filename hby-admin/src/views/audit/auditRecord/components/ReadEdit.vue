<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="900px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form :model="form" ref="form" label-width="80px" :rules="rules">
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="form.secrectLevelId"
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
              v-model="form.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
              :disabled="!form.secrectLevelId || disabled"
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
            <el-input v-model.trim="form.projectCode" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="prjoectName">
            <el-input v-model.trim="form.prjoectName" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="借阅日期" prop="borrowDate">
            <el-date-picker
              v-model="form.borrowDate"
              type="date"
              placeholder="借阅日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="归还日期" prop="backDate">
            <el-date-picker
              v-model="form.backDate"
              type="date"
              placeholder="归还日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="借阅人" prop="creator">
            <el-input v-model.trim="form.creator" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="借阅事由" prop="memo">
            <el-input v-model.trim="form.memo" type="textarea" />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save" :disabled="btnLoading">
        确 定
      </el-button>
    </template>
    <ProcessList ref="process" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { TjspBorrow, SubmitRecordApproval } from '@/api/audit/archives'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  export default {
    name: 'LcdyEdit',
    components: {
      ProcessList,
      ZXPerson,
    },
    data() {
      return {
        rules: {
          projectCode: [
            { required: true, message: '请输入活动名称', trigger: 'blur' },
          ],
          prjoectName: [
            {
              required: true,
              message: '请输入活动名称',
              trigger: 'blur',
            },
          ],
          borrowDate: [
            {
              required: true,
              message: '请选择借阅日期',
              trigger: 'blur',
            },
          ],
          backDate: [
            {
              required: true,
              message: '请选择归还日期',
              trigger: 'blur',
            },
          ],
          memo: [
            {
              required: true,
              message: '请输入借阅事由',
              trigger: 'blur',
            },
          ],
        },
        form: {
          projectCode: '',
          prjoectName: '',
          borrowDate: '',
          projectId: '',
          backDate: '',
          memo: '',
          creator: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        title: '',
        dialogFormVisible: false,
        certificateId: '',
        btnLoading: false,
        mjId: '', // 密级id
        MJoption: [],
        showMJ: false,
      }
    },
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
        // selectedValue 就是选中的 value（即 levelId）
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.form.staffScopeNames = '全部人员'
            this.form.staffScopeIds = ''
          } else {
            this.form.staffScopeNames = ''
            this.form.staffScopeIds = ''
          }
        }
      },
      /**
       * @description: 打开表单
       * @param {*} row 传入数据
       * @return {*}
       */
      showEdit(row) {
        this.certificateId = row.projectId
        this.dialogFormVisible = true
        this.$nextTick(() => {
          if (!row) {
            this.title = '添加'
          } else {
            this.title = '申请借阅'
            this.form.secrectLevelId = row.secrectLevelId
            this.form.staffScopeIds = row.staffScopeIds
            this.form.staffScopeNames = row.staffScopeNames
            this.form.projectCode = row.projectCode
            this.form.prjoectName = row.prjoectName
            this.form.projectid = row.projectId
            const userInfo = JSON.parse(localStorage.getItem('userInfo'))
            this.form.creator = userInfo.realname
          }

          this.$refs['form'].clearValidate()
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['form'].resetFields()
        this.form.projectCode = ''
        this.form.prjoectName = ''
        this.form.borrowDate = ''
        this.form.projectId = ''
        this.form.backDate = ''
        this.form.memo = ''
        this.form.creator = ''
        this.form.secrectLevelId = ''
        this.form.staffScopeIds = ''
        this.form.staffScopeNames = ''
        this.dialogFormVisible = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      async save() {
        try {
          this.btnLoading = true
          this.$refs['form'].validate(async (valid) => {
            if (!valid) {
              return false
            } else {
              let obj = { ...this.form }
              delete obj.prjoectName
              delete obj.projectCode

              const res = await TjspBorrow(obj)
              if (res.code === 1) {
                // SubmitRecordApproval({
                //   borrowid: res.data.BorrowRecord.borrowid,
                // }).then((res) => {
                //   this.$message.success('提交成功')
                // })
                const tableId = 31
                const fromId = res.data.BorrowRecord.borrowid
                this.$refs['process'].save(tableId, fromId)

                this.close()
              } else {
                this.btnLoading = false
                this.$message.error('提交失败')
              }
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.form, 'staffScopeIds', ids)
        this.$set(this.form, 'staffScopeNames', names)
      },
    },
  }
</script>
