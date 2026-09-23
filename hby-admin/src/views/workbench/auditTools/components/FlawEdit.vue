<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        label-width="125px"
        :model="form"
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
              v-model="form.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              @change="changeMJ"
              :disabled="isRead"
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
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
              :disabled="!form.secrectLevelId || isRead"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷级别" prop="bugcrilevel">
            <el-input
              :disabled="isRead"
              v-model="form.bugcrilevel"
              clearable
              placeholder="请输入缺陷级别"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷状态" prop="status">
            <el-select
              :disabled="isRead"
              v-model="form.status"
              placeholder="请选择"
              :style="{ width: '100%' }"
            >
              <el-option label="正常" :value="2" />
              <el-option label="禁用" :value="1" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷类别" prop="bugtype" style="height: 28px">
            <el-select
              v-model="form.bugtype"
              style="width: 100%"
              :disabled="isRead"
            >
              <el-option label="财报缺陷" value="财报缺陷"></el-option>
              <el-option label="非财报缺陷" value="非财报缺陷"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="定义" prop="bugcridefine">
            <el-input
              :disabled="isRead"
              v-model="form.bugcridefine"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入定义"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="定量标准" prop="bugcriration">
            <el-input
              :disabled="isRead"
              v-model="form.bugcriration"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入定量标准"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="定性标准" prop="bugcristability">
            <el-input
              :disabled="isRead"
              v-model="form.bugcristability"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入定性标准"
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
</template>

<script>
  import { quexianSave } from '@/api/workbench/auditTools'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  export default {
    components: { ZXPerson },
    name: 'YwcjEdit',
    data() {
      return {
        form: {
          bugcrilevel: '',
          status: 2,
          bugcridefine: '',
          bugcriration: '',
          bugcristability: '',
          staffScopeIds: '',
          staffScopeNames: '',
          secrectLevelId: '',
        },
        rules: {
          bugcrilevel: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          status: [
            {
              required: true,
              message: '请选择',
              trigger: 'blur',
            },
          ],
          bugcridefine: [
            {
              required: true,
              message: '请输入',
              trigger: 'change',
            },
          ],
          bugcriration: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          bugcristability: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        isRead: false,
        showMJ: false,
        menuId: '',
        MJoption: [],
      }
    },
    created() {
      this.showMJ = couldMJ()
    },
    methods: {
      async showEdit(row, isRead) {
        // 每次打开弹窗时获取密级数据
        if (this.showMJ) {
          // 获取密级,菜单id
          const res = await hasMJ('BaseConfigFlaw')
          this.menuId = res[0].menuid
          // 请求密级下拉数据
          const res2 = await getMJ({ rightId: res[0].menuid })
          this.MJoption = res2.data
        }
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
          this.form.org = '长江集团有限公司'
          this.form.code = 'XXXXXXXXXX'
          this.form.name = 'XXXXXXXXXX'
        }
        if (isRead) {
          this.isRead = isRead
        } else {
          this.isRead = false
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['elForm'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        if (this.isRead) {
          this.close()
          return
        }
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const { msg } = await quexianSave(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.form, 'staffScopeIds', ids)
        this.$set(this.form, 'staffScopeNames', names)
      },
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.form.staffScopeNames = '全部人员'
            this.form.staffScopeIds = ''
          } else {
            this.form.staffScopeIds = ''
            this.form.staffScopeNames = ''
          }
        }
      },
    },
  }
</script>
<style scoped>
  .model-show {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
  }
  .model-show > div {
    background: red;
    margin: 5px;
    padding: 10px;
    color: white;
    font-size: 16px;
    font-weight: 500;
  }
</style>
