<template>
  <!-- 风险监测指标编辑 -->
  <div>
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="120px"
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
              :disabled="!formData.secrectLevelId || disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报年度" prop="riskyear">
            <el-date-picker
              v-model="formData.riskyear"
              type="year"
              value-format="yyyy"
              placeholder="选择年"
              :style="{ width: '100%' }"
              :disabled="disabled"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报季度" prop="quartername">
            <el-select
              v-model="formData.quartername"
              placeholder="请选择季度"
              clearable
              :style="{ width: '100%' }"
              :disabled="disabled"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="notes">
            <el-input
              type="textarea"
              v-model="formData.notes"
              :rows="4"
              placeholder="请输入备注信息"
              :disabled="disabled"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
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
  import {
    getMajorRiskCreateDetails,
    saveOrUpdateMajorRiskCreate,
  } from '@/api/risk/monitoring'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { getSPMJ } from '@/api/setting/mjsz'
  export default {
    components: {
      ZXPerson,
      Resubmit,
    },
    data() {
      return {
        dialogVisible: false,
        title: 'add',
        disabled: false,
        formData: {
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          assId: undefined,
          assname: undefined,
          quartername: '', // 季度名称
          riskyear: '', // 年度
          notes: '', // 备注
          id: '',
        },
        rules: {
          quartername: [
            {
              required: true,
              message: '请选择季度',
              trigger: 'change',
            },
          ],
          riskyear: [
            {
              required: true,
              message: '请选择年度',
              trigger: 'change',
            },
          ],
        },
        options: [
          {
            value: '一季度',
            label: '一季度',
          },
          {
            value: '二季度',
            label: '二季度',
          },
          {
            value: '三季度',
            label: '三季度',
          },
          {
            value: '四季度',
            label: '四季度',
          },
        ],
        showMJ: false,
        MJoption: [],
        alldisabled: false,

        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        MJoption: [],
        menuId: 0,
        showMJ: false,
        btnLoading: false,
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
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
            this.formData.assId = undefined
            this.formData.assname = undefined
          }
        }
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        console.log('🚀 ~ title:', title)
        // 重置表单数据
        this.formData = {
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          assId: undefined,
          assname: undefined,
          quartername: '', // 季度名称
          riskyear: '', // 年度
          notes: '', // 备注
          id: '',
        }
        if (flowType) {
          this.getMJData(flowType)
        }
        // 流程相关
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        this.disabled = title === 'detail'
        if (formId) {
          try {
            const { data } = await getMajorRiskCreateDetails({ id: formId })
            // 设置表单数据
            Object.keys(this.formData).forEach((key) => {
              if (data[key] !== undefined) {
                this.$set(this.formData, key, data[key])
              }
            })

            // 处理年度格式
            if (data.riskyear) {
              this.$set(this.formData, 'riskyear', String(data.riskyear))
            }
          } catch (error) {
            console.error('获取详情失败:', error)
            this.$message.error('获取详情失败')
          }
        }
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      close() {
        this.disabled = true
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            try {
              const res = await saveOrUpdateMajorRiskCreate(this.formData)
              if (res && res.code === 1) {
                this.$message({
                  message: '保存成功！',
                  type: 'success',
                })
                this.$emit('fetchData')
              } else {
                this.$message({
                  message: res.msg || '保存失败',
                  type: 'error',
                })
              }
            } catch (error) {
              console.error('保存失败:', error)
              this.$message.error('保存失败')
            }
          }
        })
      },
      //提交
      async ymsubmit() {
        console.log('🚀 ~ ymsubmit ~ this.formData:', this.formData)
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
