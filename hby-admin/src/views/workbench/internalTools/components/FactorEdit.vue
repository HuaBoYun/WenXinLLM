<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
          :disabled="formDisabled"
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
                :style="{ width: '75%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                :disabled="!formData.secrectLevelId || formDisabled"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="要素编号" prop="elementnumber">
              <el-input
                v-model="formData.elementnumber"
                clearable
                placeholder="请输入要素编号"
                :style="{ width: '100%' }"
                readonly
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="要素名称" prop="elementname">
              <el-input
                v-model="formData.elementname"
                clearable
                placeholder="请输入要素名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="业务类别" prop="businesstype">
              <el-input
                v-model="formData.businesstype"
                placeholder="请输入业务类别"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select
                v-model="formData.status"
                clearable
                placeholder="请选择状态"
                :style="{ width: '100%' }"
              >
                <el-option key="1" label="正常" value="1" />
                <el-option key="2" label="禁用" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评分规则" prop="assessrules">
              <el-input
                v-model="formData.assessrules"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入规则"
                :style="{ width: '100%' }"
                readonly
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="审查要点" prop="auditpoint">
              <el-input
                v-model="formData.auditpoint"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入审查要点"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
      <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    </el-dialog>
  </div>
</template>
<script>
  import { doDelete } from '@/api/table'
  import { createProjectCode } from '@/api/internal/project'
  import { findAutoNum, defBasicSave } from '@/api/internal/factorMaintenance'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  export default {
    name: 'PlanEdit',
    components: { ZXPerson },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        MJoption: [],
        showMJ: false,
        menuId: 0,
        title: '',
        dialogFormVisible: false,
        formData: {
          elementnumber: undefined,
          elementname: undefined,
          businesstype: undefined,
          status: undefined,
          assessrules: '5.0',
          auditpoint: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        },
        rules: {
          elementnumber: [
            {
              required: true,
              message: '请输入要素编号',
              trigger: 'blur',
            },
          ],
          elementname: [
            {
              required: true,
              message: '请输入要素名称',
              trigger: 'blur',
            },
          ],
          businesstype: [
            {
              required: true,
              message: '请输入业务类别',
              trigger: 'blur',
            },
          ],
          status: [
            {
              required: true,
              message: '请选择状态',
              trigger: 'change',
            },
          ],
          assessrules: [
            {
              required: true,
              message: '请输入评分规则',
              trigger: 'blur',
            },
          ],
          auditpoint: [
            {
              required: true,
              message: '请输入审查要点',
              trigger: 'blur',
            },
          ],
        },
        formDisabled: false,
      }
    },

    created() {
      this.showMJ = couldMJ()
    },
    methods: {
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      async fetchMJData() {
        // 获取密级数据
        if (this.showMJ) {
          const res = await hasMJ('InternalFactorMaintenance')
          this.menuId = res[0].menuid
          const res2 = await getMJ({ rightId: res[0].menuid })
          this.MJoption = res2.data
        }
      },
      save() {
        this.$refs['elForm'].validate((valid) => {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.tblcomany = userInfo.linkOrg.orgid
          const info = {
            ...this.formData,
            assessrules: 0,
            businessattribute: '',
            memo: '',
          }

          if (valid) {
            defBasicSave(this.formData).then((res) => {
              if (res.code == 1) {
                this.$message.success('保存成功')
                this.dialogFormVisible = false
                this.$emit('fetch-data')
              } else {
                this.$message.success('保存失败')
                this.dialogFormVisible = false
                this.$emit('fetch-data')
              }
            })
          }
        })
        //  defBasicSave(this.formData)
        //
      },
      async showEdit(row, type) {
        // 每次打开弹窗时获取密级数据
        await this.fetchMJData()
        
        if (!row) {
          this.title = '添加'
          const autoNew = {
            tblName: 'TBL_ASSESSELEMENT',
            column: 'ELEMENTNUMBER',
            orgCol: 'TBLCOMANY',
            noId: 316,
          }
          //老系统调用的接口
          const { data } = await createProjectCode(autoNew)
          //新的接口，跟老系统对不上
          // const res = await findAutoNum(autoNew)
          //
          this.formData.elementnumber = data
          //orgid
          // this.formData.tblcomany =
        } else {
          this.title = '编辑'
          // this.form = Object.assign({}, row)
          // this.form.org = '长江集团有限公司'
          // this.form.code = 'XXXXXXXXXX'
          // this.form.name = 'XXXXXXXXXX'
          this.formData = row
          this.formData.assessrules = '5.0'
        }
        if (type) {
          this.formDisabled = true
        }
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {
          elementnumber: undefined,
          elementname: undefined,
          businesstype: undefined,
          status: undefined,
          assessrules: '5.0',
          auditpoint: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        }
        this.formDisabled = false
        this.dialogFormVisible = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
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
    },
  }
</script>
<style></style>
