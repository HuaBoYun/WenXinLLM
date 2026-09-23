<template>
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
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="编号" label-width="140px" prop="elementcode">
            <el-input
              v-model="formData.elementcode"
              clearable
              placeholder="请输入编号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="控制方法"
            label-width="140px"
            prop="controlmethod"
          >
            <el-input
              v-model="formData.controlmethod"
              clearable
              placeholder="请输入控制方法"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制类型" label-width="140px" prop="controltype">
            <el-input
              v-model="formData.controltype"
              clearable
              placeholder="请输入控制类型"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制频率" label-width="140px" prop="controlreq">
            <el-input
              v-model="formData.controlreq"
              clearable
              placeholder="请输入控制频率"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险描述" label-width="140px" prop="risktype">
            <el-input
              v-model="formData.risktype"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="业务描述"
            label-width="140px"
            prop="controltarget"
          >
            <el-input
              v-model="formData.controltarget"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入业务描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="控制目标"
            label-width="140px"
            prop="businessdesc"
          >
            <el-input
              v-model="formData.businessdesc"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入控制目标"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="控制措施"
            label-width="140px"
            prop="controlmeasures"
          >
            <el-input
              v-model="formData.controlmeasures"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入控制措施"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="检查方法" label-width="140px" prop="checkmethod">
            <el-input
              v-model="formData.checkmethod"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入检查方法"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="检查程序" label-width="140px" prop="procedures">
            <el-input
              v-model="formData.procedures"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入检查程序"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="检查结果" label-width="140px" prop="testresult">
            <el-input
              v-model="formData.testresult"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入检查结果"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="检查有效性"
            label-width="140px"
            prop="testpointvalidity"
          >
            <el-select
              v-model="formData.testpointvalidity"
              placeholder="请选择检查有效性"
              :style="{ width: '100%' }"
              :disabled="!footer"
            >
              <el-option
                v-for="item in testpointvalidityList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" label-width="140px" prop="memo">
            <el-input
              v-model="formData.memo"
              clearable
              type="textarea"
              rows="4"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api + '?projectid=' + task.testtaskid"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div> -->
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    addtask,
    delFile,
    controlTestImplSave,
  } from '@/api/internal/new/tack'
  import { download } from '@/api/internal/score'
  import store from '@/store'
  const { baseURL } = require('@/config')
  export default {
    name: 'TaskForm',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/nkhg/nbkz/csrw/control_test_impl_upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          elementcode: undefined,
          controlmethod: undefined,
        },
        templates: [],
        footer: true,
        tableData: [],
        task: '',
        planid: '',
        rules: {
          elementcode: [
            {
              required: true,
              message: '请输入编号',
              trigger: 'blur',
            },
          ],
          procedures : [
            {
              required: true,
              message: '请输入检查程序',
              trigger: 'blur',
            },
          ],
          testresult : [
            {
              required: true,
              message: '请输入检查结果',
              trigger: 'blur',
            },
          ],
          testpointvalidity : [
            {
              required: true,
              message: '请选择检查有效性',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        testpointvalidityList: [
          {
            value: '1',
            label: '有效',
          },
          {
            value: '2',
            label: '无效',
          },
          {
            value: '3',
            label: '不适用',
          },
        ],
      }
    },
    computed: {},

    created() {},
    mounted() {},
    methods: {
      showEdit(row, type, planid, templId, node) {
        console.log(row)
        this.dialogFormVisible = true
        this.getInfo(row, planid, templId, node)
        this.planid = planid
        if (type == 'test') {
          this.title = '检查'
        } else if (type == 'view') {
          this.title = '查看'
          this.footer = false
        } else {
          this.title = '编辑'
        }
      },
      async getInfo(row, planid, templId, node) {
        console.log(row, 'aaaa')
        console.log(planid, 'bbb')
        console.log(templId, 'ccc')
        console.log(node, 'ddd')
        const data = await addtask({
          ementid: row.ELEMENTID,
          node,
          planid,
          templId,
        })
        console.log('sad', data)
        this.formData = data.data.element
        this.$set(this.formData, 'testresult', data.data.task.testresult)
        this.$set(this.formData, 'procedures', data.data.task.procedures)
        this.$set(
          this.formData,
          'testpointvalidity',
          data.data.task.testpointvalidity
        )
        this.$set(this.formData, 'memo', data.data.task.memo)
        // this.formData.testresult = data.data.task.testresult
        // this.formData.procedures = data.data.task.procedures
        // this.formData.testpointvalidity = data.data.task.testpointvalidity
        // this.formData.memo = data.data.task.memo
        this.tableData = data.data.atts
        this.task = data.data.task
      },
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const {
              procedures,
              testpointvalidity,
              testresult,
              memo,
              ...other
            } = this.formData
            // console.log(this.formData, 'this.formData')
            // attids = attids.substring(0, attids.length - 1)
            const data = await controlTestImplSave({
              procedures,
              testpointvalidity,
              testresult,
              memo,
              planid: this.planid,
              testtaskid: this.task.testtaskid,
            })
            if (data.code == 200) {
              this.$baseMessage('保存成功', 'success')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            console.log('error submit!!')
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
        await delFile({ attid: row.attid })
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.code == '200') {
          let list = this.tableData || []
          list.push(file.data.att)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
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
