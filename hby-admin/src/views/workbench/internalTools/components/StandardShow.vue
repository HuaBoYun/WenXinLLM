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
                disabled
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
                disabled
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缺陷级别" prop="bugcrilevel">
              <el-input
                v-model="formData.bugcrilevel"
                clearable
                placeholder="请输入缺陷级别"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缺陷状态" prop="status">
              <el-select
                v-model="formData.status"
                clearable
                placeholder="请选择缺陷状态"
                :style="{ width: '100%' }"
                disabled
              >
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="定义" prop="bugcridefine">
              <el-input
                v-model="formData.bugcridefine"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入定义"
                :style="{ width: '100%' }"
                type="textarea"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="定量标准" prop="bugcriration">
              <el-input
                v-model="formData.bugcriration"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入定量标准"
                :style="{ width: '100%' }"
                type="textarea"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="定性标准" prop="bugcristability">
              <el-input
                v-model="formData.bugcristability"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入定性标准"
                :style="{ width: '100%' }"
                type="textarea"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <!-- <el-button type="primary" @click="save">确 定</el-button> -->
      </template>
      <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    </el-dialog>
  </div>
</template>
<script>
  import {
    defQuexianDetail,
    defQuexianSave,
  } from '@/api/internal/defectStandard'
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
        title: '',
        dialogFormVisible: false,
        formData: {
          bugcrilevel: undefined,
          status: undefined,
          bugcridefine: undefined,
          bugcriration: undefined,
          bugcristability: undefined,
          orgid: undefined,
          secrectLevelId: undefined,
          staffScopeNames: undefined,
          staffScopeIds: undefined,
        },
        rules: {
          bugcrilevel: [
            {
              required: true,
              message: '请输入缺陷级别',
              trigger: 'blur',
            },
          ],
          status: [
            {
              required: true,
              message: '请选择缺陷状态',
              trigger: 'blur',
            },
          ],
          bugcridefine: [
            {
              required: true,
              message: '请输入缺陷定义',
              trigger: 'change',
            },
          ],
          bugcriration: [
            {
              required: true,
              message: '请输入定量标准',
              trigger: 'change',
            },
          ],
          bugcristability: [
            {
              required: true,
              message: '请输入定性标准',
              trigger: 'change',
            },
          ],
        },
        statusOptions: [
          {
            label: '正常',
            value: 1,
          },
          {
            label: '禁用',
            value: 2,
          },
        ],
        MJoption: [],
        menuId: 0,
        showMJ: false,
      }
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('InternalDefectStandard')
        this.menuId = res[0].menuid
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
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      save() {
        this.$refs['elForm'].validate((valid) => {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.orgid = userInfo.linkOrg.orgid
          if (valid) {
            defQuexianSave({
              ...this.formData,
              version: '1',
              // bugcriid: '1',
            }).then((res) => {
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
      },
      showEdit(row, title) {
        if (title == 'add') {
          this.title = '添加'
        } else if (title == 'edit') {
          this.title = '编辑'
          this.formData = row
        } else {
          this.title = '查看'
          this.formData = row
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
        this.formData = {}
      },
    },
  }
</script>
<style></style>
