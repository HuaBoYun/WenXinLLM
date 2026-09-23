<template>
  <!-- 风险监测指标编辑 -->
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="
        title === 'add'
          ? '新增风险监测指标'
          : title === 'edit'
          ? '修改风险监测指标'
          : '风险监测指标详情'
      "
      :visible.sync="dialogVisible"
      width="800px"
      @close="close"
      v-if="dialogVisible"
    >
      <el-row :gutter="14">
        <el-form
          ref="ruleForm"
          label-width="100px"
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
      <div slot="footer" v-if="!disabled">
        <el-button @click="close">取消</el-button>
        <el-button @click="save" type="primary">确定</el-button>
      </div>
    </el-dialog>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import {
    getMajorRiskCreateDetails,
    saveOrUpdateMajorRiskCreate,
  } from '@/api/risk/monitoring'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  export default {
    components: {
      ZXPerson,
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
      }
    },
    async created() {
      this.showMJ = couldMJ()
      console.log('🚀 ~ created ~ this.showMJ:', this.showMJ)
      if (this.showMJ) {
        const res = await hasMJ('riskIndicatorCreation')
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
      async showEdit(row, title) {
        this.dialogVisible = true
        this.title = title
        this.disabled = title == 'detail'
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
        if (row && row.id) {
          try {
            const { data } = await getMajorRiskCreateDetails({ id: row.id })
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
      close() {
        this.dialogVisible = false
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
                this.close()
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
