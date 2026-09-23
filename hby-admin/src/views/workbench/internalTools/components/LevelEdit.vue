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
            <el-form-item label="级别名称" prop="levelname">
              <el-input
                v-model="formData.levelname"
                clearable
                placeholder="请输入级别名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="级别上限" prop="levelupper">
              <el-input
                v-model="formData.levelupper"
                clearable
                placeholder="请输入级别上限"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="级别下限" prop="levellower">
              <el-input
                v-model="formData.levellower"
                placeholder="请输入级别下限"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="级别说明" prop="leveldes">
              <el-input
                v-model="formData.leveldes"
                placeholder="请输入级别说明"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="editTime">
              {{ formData.modifieddate }}
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save" v-if="!formDisabled">
          确 定
        </el-button>
      </template>
      <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    </el-dialog>
  </div>
</template>
<script>
  import { doDelete } from '@/api/table'
  import { levelAdd, levelEdit } from '@/api/internal/levelMaintenance'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  export default {
    name: 'LevelEdit',
    components: { ZXPerson },
    inheritAttrs: false,
    props: [],
    data() {
      var levelupperJudge = (rule, value, callback) => {
        if (value < 0 || value > 100) {
          return callback(new Error('上限的范围是0-100之间'))
        } else if (!(!Number.isInteger(value) && isFinite(value))) {
          return callback(new Error('上限必须是数字'))
        } else if (parseInt(value) <= parseInt(this.formData.levellower)) {
          return callback(new Error('上限必须大于下限'))
        } else {
          callback()
        }
      }
      var levellowerJudge = (rule, value, callback) => {
        if (value < 0 || value > 100) {
          return callback(new Error('下限的范围是0-100之间'))
        } else if (!(!Number.isInteger(value) && isFinite(value))) {
          return callback(new Error('下限必须是数字'))
        } else if (parseInt(value) >= parseInt(this.formData.levelupper)) {
          return callback(new Error('下限必须小于上限'))
        } else {
          callback()
        }
      }
      return {
        editTime: '',
        title: '',
        dialogFormVisible: false,
        formDisabled: false,
        formData: {
          levelname: undefined,
          levelupper: undefined,
          levellower: undefined,
          leveldes: undefined,
          modifieddate: undefined,
          secrectLevelId: undefined,
          staffScopeIds: undefined,
          staffScopeNames: undefined,
        },
        rules: {
          levelname: [
            {
              required: true,
              message: '请输入级别名称',
              trigger: 'blur',
            },
          ],
          levelupper: [
            {
              required: true,
              message: '请输入级别上限',
              trigger: 'blur',
            },
            { validator: levelupperJudge, trigger: 'blur' },
          ],
          levellower: [
            {
              required: true,
              message: '请输入级别下限',
              trigger: 'blur',
            },
            { validator: levellowerJudge, trigger: 'blur' },
          ],
          leveldes: [
            {
              required: true,
              message: '请输入级别说明',
              trigger: 'blur',
            },
          ],
        },
        MJoption: [],
        menuId: 0,
        showMJ: false,
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
          const res = await hasMJ('InternalLevelMaintenance')
          this.menuId = res[0].menuid
          const res2 = await getMJ({ rightId: res[0].menuid })
          this.MJoption = res2.data
        }
      },
      save() {
        this.$refs['elForm'].validate((valid) => {
          if (valid) {
            if (this.title == '添加') {
              const { modifieddate, ...other } = this.formData
              levelAdd(other).then((res) => {
                if (res.code == 1) {
                  this.$baseMessage(
                    res.msg,
                    'success',
                    'vab-hey-message-success'
                  )
                }
                this.close()
                this.$emit('fetch-data')
              })
            } else {
              const { modifieddate, ...other } = this.formData
              levelEdit(other).then((res) => {
                if (res.code == 1) {
                  this.$baseMessage(
                    res.msg,
                    'success',
                    'vab-hey-message-success'
                  )
                }
                this.close()
                this.$emit('fetch-data')
              })
            }
          }
        })
      },
      async showEdit(row, type) {
        console.log('🚀 ~ showEdit ~ type:', type)
        console.log('🚀 ~ showEdit ~ row:', row)
        
        // 每次打开弹窗时获取密级数据
        await this.fetchMJData()
        
        this.dialogFormVisible = true
        const date = new Date()
        if (!row) {
          this.title = '添加'
          this.editTime = '新建时间'
          this.formData.modifieddate = `${date.getFullYear()}-${
            date.getMonth() + 1
          }-${date.getDate()}`
        } else {
          this.title = '编辑'
          this.editTime = '修改时间'
          this.formData = JSON.parse(JSON.stringify(row))
          this.formData.levellower = row.levellower.toString()
          this.formData.levelupper = row.levelupper.toString()
          this.formData.modifieddate = `${date.getFullYear()}-${
            date.getMonth() + 1
          }-${date.getDate()}`
        }
        if (type) {
          this.title = '详情'
          this.formDisabled = true
        }
      },
      close() {
        this.formData = {
          levelname: undefined,
          levelupper: undefined,
          levellower: undefined,
          leveldes: undefined,
          modifieddate: undefined,
          secrectLevelId: undefined,
          staffScopeIds: undefined,
          staffScopeNames: undefined,
        }
        this.formDisabled = false
        this.dialogFormVisible = false
        this.$refs.elForm.resetFields()
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
