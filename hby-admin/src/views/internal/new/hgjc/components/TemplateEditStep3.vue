<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :modal="false"
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
          <el-col :span="12">
            <el-form-item label="模板编号" prop="elementcode">
              <el-input
                disabled
                v-model="formData.elementcode"
                placeholder="请输入模板编号"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制方法" prop="controlmethod">
              <el-select
                :disabled="!isShow"
                placeholder="请选择控制方法"
                v-model="formData.controlmethod"
                :style="{ width: '100%' }"
              >
                <el-option label="自动" value="自动" />
                <el-option label="手动" value="手动" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制类型" prop="controltype">
              <el-select
                :disabled="!isShow"
                placeholder="请选择控制类型"
                v-model="formData.controltype"
                :style="{ width: '100%' }"
              >
                <el-option label="预防性" value="预防性" />
                <el-option label="发现性" value="发现性" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控制频率" prop="controlreq">
              <el-select
                :disabled="!isShow"
                placeholder="请选择控制频率"
                v-model="formData.controlreq"
                :style="{ width: '100%' }"
              >
                <el-option label="随时" value="随时" />
                <el-option label="日" value="日" />
                <el-option label="周" value="周" />
                <el-option label="月度" value="月度" />
                <el-option label="季度" value="季度" />
                <el-option label="年度" value="年度" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="业务描述" prop="businessdesc">
              <el-input
                :disabled="!isShow"
                v-model="formData.businessdesc"
                :autosize="{ minRows: 3, maxRows: 8 }"
                placeholder="请输入业务描述"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="风险描述" prop="risktype">
              <el-input
                :disabled="!isShow"
                v-model="formData.risktype"
                :autosize="{ minRows: 3, maxRows: 8 }"
                placeholder="请输入风险描述"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="控制目标" prop="controltarget">
              <el-input
                :disabled="!isShow"
                v-model="formData.controltarget"
                :autosize="{ minRows: 3, maxRows: 8 }"
                placeholder="请输入控制目标"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="控制措施" prop="controlmeasures">
              <el-input
                :disabled="!isShow"
                v-model="formData.controlmeasures"
                :autosize="{ minRows: 3, maxRows: 8 }"
                placeholder="请输入控制措施"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="检查方法" prop="checkmethod">
              <el-input
                :disabled="!isShow"
                v-model="formData.checkmethod"
                :autosize="{ minRows: 3, maxRows: 8 }"
                placeholder="请输入检查方法"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="所需资料" prop="material">
              <el-input
                :disabled="!isShow"
                v-model="formData.material"
                :autosize="{ minRows: 3, maxRows: 8 }"
                placeholder="请输入所需资料"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save" v-if="isShow">保 存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { doDelete } from '@/api/table'
  import {
    elementssave,
    elementmodify,
    findRootNumberByParentIdLevel,
  } from '@/api/internal/new/testTemplate'
  export default {
    name: 'TemplateEditStep3',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        isShow: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '新建',
        dialogFormVisible: false,
        queryForm: {},
        formData: {
          elementcode: undefined,
          controlmethod: '自动',
          controltype: '预防性',
          controlreq: '随时',
          businessdesc: undefined,
          risktype: undefined,
          controltarget: undefined,
          controlmeasures: undefined,
          checkmethod: undefined,
          material: undefined,
        },
        rules: {
          elementcode: [
            {
              required: true,
              message: '请输入模板编号',
              trigger: 'blur',
            },
          ],
          controlmethod: [
            {
              required: true,
              message: '请选择控制方法',
              trigger: 'blur',
            },
          ],
          controltype: [
            {
              required: true,
              message: '请选择控制类型',
              trigger: 'blur',
            },
          ],
          controlreq: [
            {
              required: true,
              message: '请选择控制频率',
              trigger: 'change',
            },
          ],
          businessdesc: [
            {
              required: true,
              message: '请输入业务描述',
              trigger: 'blur',
            },
          ],
          risktype: [
            {
              required: true,
              message: '请输入风险描述',
              trigger: 'blur',
            },
          ],
          controltarget: [
            {
              required: true,
              message: '请输入控制目标',
              trigger: 'blur',
            },
          ],
          controlmeasures: [
            {
              required: true,
              message: '请输入控制措施',
              trigger: 'blur',
            },
          ],
          checkmethod: [
            {
              required: true,
              message: '请输入检查方法',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async getNumber(typeid) {
        const { code, data } = await findRootNumberByParentIdLevel({
          parentId: typeid,
          chilNumberCol: 'ELEMENTCODE',
          chilParentCol: 'TYPEID',
          chilTblName: 'TBL_COM_EXT_TESTELEMENT',
          middleChilCol: 'TYPEID',
          middleIdCol: 'TYPEID',
          middleNumberCol: 'TYPECODE',
          middleParentCol: 'TESTTEMPLETAID',
          middleTblname: 'TBL_COM_EXT_TESTTEMPL_TYPE',
          noId: '317',
          parentIdCol: 'TESTTEMID',
          parentNumberCol: '',
          parentTblName: 'TBL_COM_EXT_TESTTEMPLE',
          parnetOrgCol: 'TBLCOMANY',
        })
        if (code == 200) {
          this.formData.elementcode = data
        }
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let fu
            if (this.formData.elementid) {
              fu = elementmodify
            } else {
              fu = elementssave
            }
            const params = {
              templid: this.queryForm.templid,
              typeid: this.queryForm.typeid,
              ...this.formData,
            }
            const { code, data } = await fu(params)
            if (code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch')
              this.close()
            }
          } else {
            return false
          }
        })
      },
      show(row, queryForm, type) {
        this.dialogFormVisible = true
        this.queryForm = queryForm
        this.isShow = true
        if (row) {
          if (type) {
            this.title = '查看'
            this.formData = row
            this.isShow = false
          } else {
            this.title = '编辑'
            console.log('row', row)
            this.formData = row
          }
        } else {
          this.title = '新建'
          this.getNumber(queryForm.typeid)
        }
      },
      close() {
        this.dialogFormVisible = false
        this.formData = {
          elementcode: undefined,
          controlmethod: '自动',
          controltype: '预防性',
          controlreq: '随时',
          businessdesc: undefined,
          risktype: undefined,
          controltarget: undefined,
          controlmeasures: undefined,
          checkmethod: undefined,
          material: undefined,
        }
      },
    },
  }
</script>
<style></style>
