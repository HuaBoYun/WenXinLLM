<template>
  <el-dialog
    :title="title"
    :visible="true"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
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
          <el-form-item label="缺陷编号" prop="bugnumber">
            <el-input
              v-model="formData.bugnumber"
              clearable
              placeholder="请输入缺陷编号"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷级别" prop="bugcriid">
            <el-select
              v-model="formData.bugcriid"
              placeholder="请输入缺陷级别"
              :disabled="!footer"
            >
              <el-option label="重大缺陷" value="重大缺陷" />
              <el-option label="重要缺陷" value="重要缺陷" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现日期" prop="discovertime">
            <el-date-picker
              v-model="formData.discovertime"
              placeholder="请输入发现日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现人" prop="discoverperson">
            <el-input
              v-model="formData.discoverperson"
              clearable
              placeholder="请输入发现人"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否财务相关" prop="needreform">
            <el-radio-group v-model="formData.needreform" :disabled="!footer">
              <el-radio label="是" value="0" />
              <el-radio label="否" value="1" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷性质" prop="bugproperty">
            <el-select
              v-model="formData.bugproperty"
              placeholder="请输入缺陷性质"
              :disabled="!footer"
            >
              <el-option label="执行缺陷" value="执行缺陷" />
              <el-option label="设计缺陷" value="设计缺陷" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷部门" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入缺陷部门"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否需要整改" prop="check">
            <el-radio-group v-model="formData.check" :disabled="!footer">
              <el-radio label="是" value="0" />
              <el-radio label="否" value="1" />
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务描述" prop="field108">
            <el-input
              v-model="formData.field108"
              clearable
              placeholder="请输入业务描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="缺陷描述" prop="field132">
            <el-input
              v-model="formData.field132"
              clearable
              placeholder="请输入缺陷描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>违反内规</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success">新建</el-button>
          </div>
          <el-table>
            <el-table-column align="center" label="制度名称" prop="name" />
            <el-table-column align="center" label="发文文号" prop="name" />
            <el-table-column align="center" label="发文机构" prop="name" />
            <el-table-column align="center" label="发文日期" prop="name" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit2(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>违反外规</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success">新建</el-button>
          </div>
          <el-table>
            <el-table-column align="center" label="制度名称" prop="name" />
            <el-table-column align="center" label="发文文号" prop="name" />
            <el-table-column align="center" label="发文机构" prop="name" />
            <el-table-column align="center" label="发文日期" prop="name" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit2(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>关联缺陷</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success">新建</el-button>
          </div>
          <el-table>
            <el-table-column align="center" label="缺陷描述" prop="name" />
            <el-table-column align="center" label="缺陷编号" prop="name" />
            <el-table-column align="center" label="发现人" prop="name" />
            <el-table-column align="center" label="发文日期" prop="name" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit2(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
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
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>

    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
  </el-dialog>
</template>

<script>
  import { download } from '@/oapi/audit/implement'
  import { defectAdd } from '@/oapi/audit/question'
  import store from '@/store'
  import DepartmentOptions from '../options/department.vue'
  import ExecutorOptions from '../options/executor.vue'
  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: { DepartmentOptions, ExecutorOptions },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          bugnumber: undefined,
          discovertime: undefined,
          bugcriid: '重大缺陷',
          discoverperson: undefined,
          needreform: 0,
          check: 0,
          bugproperty: undefined,
        },
        footer: true,
        tableData: [],
        rules: {
          bugnumber: [
            {
              required: true,
              message: '请输入缺陷编号',
              trigger: 'blur',
            },
          ],
          discovertime: [
            {
              required: true,
              message: '请输入发现日期',
              trigger: 'blur',
            },
          ],
          discoverperson: [
            {
              required: true,
              message: '请输入发现人',
              trigger: 'blur',
            },
          ],
          needreform: [],
          bugproperty: [],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleDepartmentSelected(node) {
        this.formData.reportdepartment = node.id
        this.formData.orgname = node.name
      },
      handleExecutorSelected(node) {
        this.formData.discoverperson = node.realname
        this.formData.reporter = node.staffid
      },
      showEdit(row) {
        if (row) {
          this.formData = row.project
          let list = []
          list.push(row.attachment)
          this.tableData = list
        }
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      close() {
        this.dialogFormVisible = false
        this.formData = {}
        this.tableData = []
        this.footer = true
        this.$emit('close')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const data = await defectAdd({
              ...this.formData,
              attids,
            })
            this.$emit('fetch-data')
            this.close()
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
      handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.table
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
