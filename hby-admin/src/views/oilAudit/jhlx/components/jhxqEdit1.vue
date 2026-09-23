<template>
  <!-- 计划需求 edit1 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="230px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="计划需求名称" prop="jhxqmc">
            <el-input
              v-model="formData.jhxqmc"
              clearable
              placeholder="请输入计划需求名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目类型" prop="sjxmlx">
            <el-input
              v-model="formData.sjxmlx"
              disabled
              placeholder="请输入审计项目类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目投资渠道" prop="xmtzqd">
            <el-input
              v-model="formData.xmtzqd"
              clearable
              placeholder="请输入项目投资渠道"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目联系人" prop="xmlxr">
            <el-input
              v-model="formData.xmlxr"
              clearable
              placeholder="请输入项目联系人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="lxdh">
            <el-input
              v-model="formData.lxdh"
              clearable
              placeholder="请输入联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织方式" prop="zzfs">
            <el-input
              v-model="formData.zzfs"
              clearable
              placeholder="请输入组织方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位">
            <el-input
              v-model="formData.tbdw"
              clearable
              placeholder="请输入填报单位"
              :style="{ width: '70%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click.native="handleObject"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报时间" prop="tbsj">
            <el-date-picker
              v-model="formData.tbsj"
              placeholder="请输入填报时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建议审计实施时间(年/月)" prop="jysjsssj">
            <el-date-picker
              v-model="formData.jysjsssj"
              placeholder="请输入建议审计实施时间"
              type="month"
              format="yyyy-MM"
              value-format="yyyy-MM"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="概算批复投资额(万元)" prop="gspftzje">
            <el-input
              v-model="formData.gspftzje"
              clearable
              placeholder="请输入概算批复投资额(万元)"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'gspftzje')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现已完成投资额(万元)" prop="xywctzje">
            <el-input
              v-model="formData.xywctzje"
              clearable
              placeholder="请输入现已完成投资额(万元)"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'xywctzje')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计总投资额(万元)" prop="yjztzje">
            <el-input
              v-model="formData.yjztzje"
              clearable
              placeholder="请输入预计总投资额(万元)"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'yjztzje')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其中:系统外单位承建额(万元)" prop="qzxtwdwcje">
            <el-input
              v-model="formData.qzxtwdwcje"
              clearable
              placeholder="请输入其中:系统外单位承建额(万元)"
              :style="{ width: '100%' }"
              @input="inputMoney($event, 'qzxtwdwcje')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="拟竣工验收时间" prop="njgyssj">
            <el-date-picker
              v-model="formData.njgyssj"
              placeholder="请输入拟竣工验收时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="试运行合格时间" prop="syxhgsj">
            <el-date-picker
              v-model="formData.syxhgsj"
              placeholder="请输入试运行合格时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结算完成时间" prop="jswcsj">
            <el-date-picker
              v-model="formData.jswcsj"
              placeholder="请输入结算完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="决算完成时间" prop="jueswcsj">
            <el-date-picker
              v-model="formData.jueswcsj"
              placeholder="请输入决算完成时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="其他情况说明" prop="qtqksm">
            <el-input
              v-model="formData.qtqksm"
              clearable
              placeholder="请输入其他情况说明"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建人" prop="creator">
            <el-input
              v-model="formData.creator"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
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
      <el-button @click="add" type="primary" :loading="loading">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
    
    <ProcessList ref="process" @fetchData="close" />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </el-dialog>
</template>

<script>
  // import {
  //   download,
  //   myDraftSave,
  //   myDraftFileList,
  //   deleteFile,
  // } from '@/api/audit/implement'

  import { jhxqUpdate } from '@/oapi/audit/plan'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  export default {
    name: 'jhxqEdit1',
    inheritAttrs: false,
    components: { SelectDepartment, ProcessList },
    props: [],
    data() {
      return {
        loading: false,
        formData: {
          jhxqmc: '',
          sjxmlx: '工程项目审计',
          xmtzqd: '',
          xmlxr: '',
          lxdh: '',
          zzfs: '',
          tbdw: '',
          tbsj: '',
          jysjsssj: '',
          xywctzje: '',
          gspftzje: '',
          yjztzje: '',
          qzxtwdwcje: '',
          njgyssj: '',
          syxhgsj: '',
          jswcsj: '',
          jueswcsj: '',
          qtqksm: '',
          creator: '',
          createTime: '',
        },
        formDisabled: false,
        tableData: [],
        rules: {
          // field1: [
          //   {
          //     required: true,
          //     message: '请输入底稿编号',
          //     trigger: 'blur',
          //   },
          // ],
          // field2: [
          //   {
          //     required: true,
          //     message: '请输入建设单位',
          //     trigger: 'blur',
          //   },
          // ],
          // field3: [
          //   {
          //     required: true,
          //     message: '请输入项目类别',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
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
        this.formData[key] = sanitizedValue
      },
      showEdit(title, row) {
        this.dialogFormVisible = true

        if (row) {
          this.editId = row.id
          Object.assign(this.formData, row)
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.creator = userInfo.realname
          this.formData.createTime = new Date()
        }

        if (title === 'edit') {
          this.title = '编辑'
        } else if (title === 'detail') {
          this.formDisabled = true
        }
      },
      close() {
        this.formData = {
          jhxqmc: '',
          sjxmlx: '工程项目审计',
          xmtzqd: '',
          xmlxr: '',
          lxdh: '',
          zzfs: '',
          tbdw: '',
          tbsj: '',
          jysjsssj: '',
          xywctzje: '',
          gspftzje: '',
          yjztzje: '',
          qzxtwdwcje: '',
          njgyssj: '',
          syxhgsj: '',
          jswcsj: '',
          jueswcsj: '',
          qtqksm: '',
          creator: '',
          createTime: '',
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.formDisabled = false
        this.editId = ''
        this.$emit('fetchData')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const { data, code, msg } = await jhxqUpdate({
              ...this.formData,
              attids,
            })
            if (code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.editId = data.jhxqid
            } else {
              this.$baseMessage(msg, 'error')
            }
            this.loading = false
          } else {
            return false
          }
        })
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.tbdw = val.label
        this.formData.tbdwid = val.id
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(122, this.editId)
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
