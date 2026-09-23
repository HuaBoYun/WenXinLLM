<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="140px" :model="form" :rules="rules">
      <el-form-item
        label="密级"
        prop="secrectLevelId"
        v-if="showMJ"
        :rules="[{ required: true, trigger: 'change', message: '请选择密级' }]"
      >
        <el-select
          v-model="form.secrectLevelId"
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
          v-model="form.staffScopeNames"
          disabled
          placeholder="请选择知悉范围"
          :style="{ width: '76%' }"
        />
        <el-button
          :style="{ marginLeft: '10px' }"
          type="primary"
          @click="$refs.ZXPerson.showEdit(form.secrectLevelId)"
          :disabled="!form.secrectLevelId"
        >
          选择
        </el-button>
      </el-form-item>

      <el-divider></el-divider>
      <el-form-item label="审计问题类型" prop="auditType">
        <el-input
          placeholder="请输入审计问题类型"
          v-model.trim="form.auditType"
        />
      </el-form-item>
      <el-form-item label="审计问题类型状态" prop="status">
        <el-select
          v-model="form.status"
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
      <!-- <el-form-item label="审计问题类型所属" props="typeBelong">
        <el-select
          v-model="form.typeBelong"
          placeholder="请选择"
          :style="{ width: '100%' }"
        >
          <el-option label="国资委" value="国资委" />
          <el-option label="审计署" value="审计署" />
        </el-select>
      </el-form-item> -->
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { sjwttype_save } from '@/api/workbench/auditTools'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'

  export default {
    name: 'LcdyEdit',
    components: { ZXPerson },
    data() {
      return {
        form: {
          auditType: '',
          status: '',
          typeBelong: '',
          secrectLevelId: '',
          staffScopeNames: '',
        },
        rules: {
          auditType: [
            { required: true, trigger: 'blur', message: '请输入审计问题类型' },
          ],
          status: [
            { required: true, trigger: 'change', message: '请选择状态' },
          ],
          typeBelong: [
            { required: true, trigger: 'change', message: '请选择类型' },
          ],
          secrectLevelId: [
            { required: true, trigger: 'change', message: '请选择密级' },
          ],
          staffScopeNames: [
            { required: true, trigger: 'change', message: '请选择知悉范围' },
          ],
        },
        title: '',
        dialogFormVisible: false,
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
      }
    },
    async created() {
      // this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('BaseConfigStatistics')
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
            this.form.staffScopeNames = '全部人员'
            this.form.staffScopeIds = ''
          } else {
            this.form.staffScopeIds = ''
            this.form.staffScopeNames = ''
          }
        }
      },
      showEdit(row) {
        if (!row) {
          this.form = {}
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const res = await sjwttype_save(this.form)
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleZXPersonSelected(val) {
        console.log('🚀 ~ handleZXPersonSelected ~ val:', val)
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.form, 'staffScopeIds', ids)
        this.$set(this.form, 'staffScopeNames', names)
        this.$forceUpdate()
      },
    },
  }
</script>
