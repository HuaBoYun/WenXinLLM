<!--
 * @Date: 2022-04-20 14:34:13
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 16:45:22
 * @FilePath: /hb-admin/src/views/contract/opposite/components/BlacklistForm.vue
-->
<template>
  <div>
    <el-row :gutter="15">
      <el-form ref="form" label-width="120px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="黑名单类型" prop="blacktype">
            <el-radio-group
              v-model="form.blacktype"
              size="medium"
              @input="$forceUpdate()"
            >
              <el-radio :label="2">长期黑名单</el-radio>
              <el-radio :label="1">短期黑名单</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            v-if="form.blacktype == 1"
            label="黑名单有效期"
            prop="datetext"
          >
            <el-date-picker
              v-model="form.pageEffectDate"
              clearable
              format="yyyy-MM-dd"
              placeholder="黑名单有效期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <MaintainDetailContent :current="current" :form-data="formData" />
    <div style="text-align: right; margin-top: 10px">
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
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
  </div>
</template>
<script>
import MaintainDetailContent from './MaintainDetailContent.vue'
import { addToBlackList } from '@/api/contract/opposite'
import AttachList from './AttachList.vue'
import Resubmit from '@/views/msg/components/options/Resubmit.vue'
import Bank from './Bank.vue'
export default {
  components: {
    Bank,
    AttachList,
    Resubmit,
    MaintainDetailContent,
  },
  props: {
    current: {
      type: Object,
      default: null,
    },
    formData: {
      type: Object,
      default: () => {},
    },
    localList: {
      type: Array,
      default: () => [],
    },
    fromId: {
      type: String,
      default: '',
    },
    flowtaskinfoflowid: {
      type: String,
      default: '',
    },
    ymFromId: {
      type: String,
      default: '',
    },
    fromIdcopy: {
      type: String,
      default: '',
    },
    status: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      curretnRow: {},
      rules: {
        blackType: [
          { required: true, trigger: 'blur', message: '请输入黑名单有效期' },
        ],
        effectdate: [
          { required: true, trigger: 'blur', message: '请输入黑名单有效期' },
        ],
      },
      title: '',
      node: {},
    }
  },
  methods: {
    save() {
      //
      this.$refs['form'].validate(async (valid) => {
        const { blacktype, effectdate, staffid, budgetid, ...other } =
          this.formData
        if (valid) {
          const { code, msg } = await addToBlackList({
            datetext: effectdate,
            staffId: staffid,
            budgetId: budgetid,
            blackType: blacktype,
          })
          if (code == 1) {
            this.$message.success(msg)
            // this.close()
          }
        }
      })
    },
    //引迈提交
    async ymsubmit() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.$refs.resubmit.ymsubmit()
        }
      })
    },
    close() {
      this.dialogFormVisible = false

      this.$bus.$emit('updateMsg', 0)
    },
  },
}
</script>
