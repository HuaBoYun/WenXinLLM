<template>
  <!-- 工程项目造价中间表 edit -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="序号" prop="gcxmzjZjbNo">
            <el-input
              v-model="formData.gcxmzjZjbNo"
              clearable
              disabled
              placeholder="请输入序号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="tblYqnsGcxmzj.htbh">
            <el-input
              v-model="formData.tblYqnsGcxmzj.htbh"
              clearable
              disabled
              placeholder="请输入合同编号"
              :style="{ width: '79%' }"
            />
            <el-button
              type="primary"
              v-if="!formDisabled"
              style="margin-left: 11px"
              @click="selectGczj"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工程名称" prop="gcmc">
            <el-input
              v-model="formData.tblYqnsGcxmzj.gcmc"
              clearable
              disabled
              placeholder="请输入工程名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建设单位" prop="jsdw">
            <el-input
              v-model="formData.tblYqnsGcxmzj.jsdw"
              clearable
              disabled
              placeholder="请输入建设单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="二审审查金额（万元）" prop="esscjewy">
            <el-input
              type="number"
              v-model="formData.tblYqnsGcxmzj.esscjewy"
              clearable
              disabled
              placeholder="请输入二审审查金额（万元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="额度（万元）" prop="edje">
            <el-input
              v-model="formData.edje"
              clearable
              placeholder="请输入额度"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'edje')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="内外部" prop="nwb">
            <el-input
              v-model="formData.nwb"
              clearable
              placeholder="请输入内外部"
              :style="{ width: '100%' }"
            />
            <!-- <el-select
              v-model="formData.nwb"
              placeholder="内外部"
              :style="{ width: '100%' }"
            >
              <el-option label="内部" value="内部" />
              <el-option label="外部" value="外部" />
            </el-select> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="施工单位" prop="sgdw">
            <el-input
              v-model="formData.tblYqnsGcxmzj.sgdw"
              clearable
              disabled
              placeholder="请输入施工单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="lxr">
            <el-input
              v-model="formData.tblYqnsGcxmzj.lxr"
              clearable
              disabled
              placeholder="请输入联系人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="lxdh">
            <el-input
              v-model="formData.tblYqnsGcxmzj.lxdh"
              clearable
              disabled
              placeholder="请输入联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="xmzttype">
            {{ formData.xmzttype }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目状态" prop="xmstatus">
            {{ formData.xmstatus == 1 ? '已做审计项目' : '未做审计项目' }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary" :loading="loading">
        确定
      </el-button>
    </div>

    <selectGczjModal ref="selectGczjModal" @selected="selected" />
  </el-dialog>
</template>

<script>
  import {
    engineeringCostCenterTableDetail,
    engineeringCostCenterTableUpdate,
    gcxmzjZjbNo,
  } from '@/oapi/audit/plan'
  import selectGczjModal from './selectGczjModal.vue'

  export default {
    name: 'gcxmzjzjbEdit',
    inheritAttrs: false,
    components: { selectGczjModal },
    data() {
      const validator = (_rule, value, callback) => {
        if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        formData: {
          gcxmzjid: '', //工程项目造价id
          edje: '', // 额度金额
          nwb: '', // 内外部
          cjr: '',
          cjsj: '',
          gcxmzjZjbNo: '',
          esscjewy: '',
          tblYqnsGcxmzj: {
            htbh: '', // 合同编号
            gcmc: '', // 工程名称
            jsdw: '', // 建设单位
            esscje: '', // 二审审查金额（元）
            esscjewy: '', // 二审审查金额（万元）
            sgdw: '', // 施工单位
            lxr: '', // 联系人
            lxdh: '', // 联系电话
          },
        },
        formDisabled: true,
        rules: {
          'tblYqnsGcxmzj.htbh': [
            {
              required: true,
              message: '请选择工程项目造价',
              trigger: 'blur',
            },
          ],
          edje: [
            {
              // validator,
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      async getNumber() {
        const { msg, code, data } = await gcxmzjZjbNo({})
        if (code == 1) {
          this.formData.gcxmzjZjbNo = data
          this.$forceUpdate()
        } else {
          this.$baseMessage(msg, 'error')
        }
      },
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = disabled
        if (!row) {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.cjr = userInfo.realname
          this.formData.cjsj = new Date().toJSON().split('T')[0]
          this.getNumber()
        } else {
          this.title = disabled ? '详细' : '编辑'
          const res = await engineeringCostCenterTableDetail({
            gcxmzjzjbid: row.gcxmzjzjbid,
          }) // 获取到详情回显
          if (res.code === 1) {
            this.formData = res.data
          }
        }
      },
      close() {
        this.formData = {
          gcxmzjid: '', //工程项目造价id
          edje: '', // 额度金额
          nwb: '', // 内外部
          cjr: '',
          cjsj: '',
          esscjewy: '',
          tblYqnsGcxmzj: {
            htbh: '', // 合同编号
            gcmc: '', // 工程名称
            jsdw: '', // 建设单位
            esscje: '', // 二审审查金额（元）
            esscjewy: '', // 二审审查金额（万元）
            sgdw: '', // 施工单位
            lxr: '', // 联系人
            lxdh: '', // 联系电话
          },
        }
        this.dialogFormVisible = false
        this.formDisabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            // const data = {
            //   edje: this.formData.edje,
            //   nwb: this.formData.nwb,
            //   gcxmzjid: this.formData.gcxmzjid,
            // }
            const obj = Object.assign({}, this.formData)
            delete obj['tblYqnsGcxmzj']
            console.log(obj)
            const res = await engineeringCostCenterTableUpdate(obj)
            this.loading = false
            if (res && res.code === 1) {
              this.$message.success('保存成功！')
              this.$emit('fetch-data')
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          }
        })
      },
      selectGczj() {
        this.$refs.selectGczjModal.showEdit()
      },
      selected(val) {
        if (val) {
          this.formData = Object.assign({}, this.formData, {
            cjr: val.cjr,
            cjsj: val.cjsj,
            gcxmzjid: val.gcxmzjid,
            esscjewy: val.esscjewy,
            tblYqnsGcxmzj: {
              htbh: val.htbh, // 合同编号
              gcmc: val.gcmc, // 工程名称
              jsdw: val.jsdw, // 建设单位
              esscje: val.esscje.toFixed(2), // 二审审查金额（元）
              esscjewy: val.esscjewy.toFixed(2), // 二审审查金额（万元）
              sgdw: val.sgdw, // 施工单位
              lxr: val.lxr, // 联系人
              lxdh: val.lxdh, // 联系电话
            },
          })
          console.log(this.formData)
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
