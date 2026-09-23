<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-form ref="form" label-width="120px" :model="formData" :rules="rules">
        <el-form-item
          label="密级"
          prop="secrectLevelId"
          v-if="showMJ"
          :rules="[
            { required: true, trigger: 'change', message: '请选择密级' },
          ]"
        >
          <el-select
            v-model="formData.secrectLevelId"
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

        <el-form-item label="知悉范围" prop="staffScopeNames" v-if="showMJ">
          <el-input
            v-model="formData.staffScopeNames"
            disabled
            placeholder="请选择知悉范围"
            :style="{ width: '76%' }"
          />
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
            :disabled="!formData.secrectLevelId"
          >
            选择
          </el-button>
        </el-form-item>

        <el-divider></el-divider>

        <el-form-item label="审计类型说明" prop="auditType">
          <el-input v-model="formData.auditType" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="审计类型状态" prop="status">
          <el-select
            v-model="formData.status"
            placeholder="请选择"
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="item in options"
              :label="item.label"
              :value="item.value"
              :key="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer v-if="!disabled">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import { saveNbsjTypeOf } from '@/api/workbench/auditToolsForOld'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  export default {
    name: 'LcdyEdit',
    components: { ZXPerson },
    data() {
      return {
        formData: {
          auditType: '',
          status: '',
          staffScopeIds: '',
          staffScopeNames: '',
          secrectLevelId: '',
        },
        rules: {
          auditType: [{ required: true, message: '请输入审计类型说明' }],
          status: [
            {
              required: true,
              trigger: 'change',
              message: '请选择审计类型状态',
            },
          ],
          secrectLevelId: [
            {
              required: true,
              trigger: 'change',
              message: '请选择密级',
            },
          ],
          staffScopeNames: [
            {
              required: true,
              trigger: 'change',
              message: '请选择知悉范围',
            },
          ],
        },
        title: '',
        type: '',
        dialogFormVisible: false,
        disabled: false,
        options: [
          {
            value: 2,
            label: '正常',
          },
          {
            value: 1,
            label: '禁用',
          },
        ],
        showMJ: false,
        menuId: '',
        MJoption: [],
      }
    },

    async created() {
      // this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('BaseConfigExperience')
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
      showEdit(row, type) {
        if (type == 'add') {
          this.title = '新增'
        } else if (type == 'edit') {
          this.title = '编辑'
          this.formData = JSON.parse(JSON.stringify(row))
        } else {
          this.title = '详情'
          this.disabled = true
        }
        this.dialogFormVisible = true
      },
      close() {
        this.formData = {
          auditType: '',
          status: '',
        }
        this.$refs['form'].resetFields()
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let getUrl = saveNbsjTypeOf
            const { code } = await getUrl(this.formData)
            if (code == 0) return
            this.close()
            this.$emit('fetch-data')
          }
        })
      },
      handleZXPersonSelected(val) {
        console.log('🚀 ~ handleZXPersonSelected ~ val:', val)
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
        this.$forceUpdate()
      },
    },
  }
</script>
