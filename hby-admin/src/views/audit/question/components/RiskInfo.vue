<template>
  <el-dialog
    v-if="dialogFormVisible"
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
          <el-form-item label="风险编号" prop="risknumber">
            <el-input
              v-model="formData.risknumber"
              clearable
              placeholder="请输入风险编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险名称" prop="riskname">
            <el-input
              v-model="formData.riskname"
              clearable
              placeholder="请输入风险名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发现日期" prop="discovereddate">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.discovereddate"
              placeholder="请输入发现日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发生日期" prop="occureddate">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.occureddate"
              placeholder="请输入发生日期"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任部门" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入责任部门"
              :style="{ width: '305px' }"
              :disabled="true"
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
          <el-form-item label="发现人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请输入发现人"
              :style="{ width: '305px' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.showEdit()"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="相关部门" prop="sysOrgName">
            <el-input
              v-model="formData.sysOrgName"
              clearable
              placeholder="请输入相关部门"
              :style="{ width: '92%' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.department.show(true)"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险描述" prop="riskdes">
            <el-input
              v-model="formData.riskdes"
              clearable
              type="textarea"
              :rows="2"
              maxlength="300"
              show-word-limit
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>风险容忍度信息</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="newLine" v-if="footer">
              新建
            </el-button>
          </div>
          <el-table :data="rrdTableList">
            <el-table-column
              align="center"
              width="120"
              label="序号"
              prop="rtcode"
            >
              <template slot-scope="scope">
                <el-input
                  :disabled="!footer"
                  v-model="scope.row.rtcode"
                  placeholder="序号"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="基准线下边界"
              prop="lowerborder"
              width="160"
            >
              <template slot-scope="scope">
                <el-input-number
                  :disabled="!footer"
                  v-model="scope.row.lowerborder"
                  label="基准线下边界"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="基准线上边界"
              prop="upperborder"
              width="160"
            >
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.upperborder"
                  label="基准线下边界"
                  :disabled="!footer"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="颜色块编码"
              prop="colorstring"
              width="200"
            >
              <template slot-scope="scope">
                <div style="display: flex; align-items: center">
                  <div
                    :style="
                      scope.row.colorstring === '绿'
                        ? 'width:10px;height:10px;border: 50%;background-color: green;border-radius: 50%;'
                        : scope.row.colorstring === '黄'
                        ? 'width:10px;height:10px;border: 50%;background-color: yellow;border-radius: 50%;'
                        : 'width:10px;height:10px;border: 50%;background-color: red;border-radius: 50%;'
                    "
                  ></div>
                  <el-select
                    v-model="scope.row.colorstring"
                    placeholder="请选择"
                    :disabled="!footer"
                    style="margin-left: 10px"
                  >
                    <el-option
                      v-for="item in colorOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="描述" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-input
                  :disabled="!footer"
                  v-model="scope.row.description"
                  placeholder="描述"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="100"
            >
              <template slot-scope="scope">
                <!-- <el-button
                  type="text"
                  @click="handleAddRrd(scope.row)"
                  v-if="footer"
                >
                  保存
                </el-button> -->
                <el-button
                  type="text"
                  @click="handleRrd(scope.row, scope.$index)"
                  v-if="footer"
                >
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
              :before-upload="handleBeforeUpload"
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
                <el-button
                  type="text"
                  @click="handlePreview(row)"
                  :disabled="false"
                >
                  预览
                </el-button>
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
      <!-- <el-button @click="close">取消</el-button> -->
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <!-- <el-button @click="close">关 闭</el-button> -->
      <el-button @click="add" type="primary">确定</el-button>
    </template>

    <department-options ref="department" @selected="handleDepartmentSelected" />
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
  </el-dialog>
</template>

<script>
  import { deleteFile, download } from '@/api/audit/implement'
  import {
    addRrd,
    createRiskCode,
    delRrd,
    getFileList,
    getRrdList,
    riskAdd,
  } from '@/api/audit/question'
  import store from '@/store'
  import DepartmentOptions from './options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  const { baseURL } = require('@/config')
  export default {
    name: 'FlawInfo',
    components: {
      DepartmentOptions,
      ExecutorOptions,
    },
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
          risknumber: undefined,
          riskname: undefined,
          discovereddate: undefined,
          occureddate: undefined,
          riskeventdescription: undefined,
          realname: undefined,
          createorid: undefined,
          orgname: undefined,
          orgid: undefined,
          sysOrgName: undefined,
          sysOrgid: undefined,
        },
        rrdTableList: [],
        footer: true,
        tableData: [],
        riskid: '',
        colorOptions: [
          {
            value: '绿',
            label: '绿',
          },
          {
            value: '黄',
            label: '黄',
          },
          {
            value: '红',
            label: '红',
          },
        ],
        tableData: [],
        rules: {
          risknumber: [
            {
              required: true,
              message: '请输入风险编号',
              trigger: 'blur',
            },
          ],
          riskname: [
            {
              required: true,
              message: '请输入风险名称',
              trigger: 'blur',
            },
          ],
          discovereddate: [
            {
              required: true,
              message: '请输入发生日期',
              trigger: 'blur',
            },
          ],
          occureddate: [
            {
              required: true,
              message: '请输入发现日期',
              trigger: 'blur',
            },
          ],
          riskdes: [
            {
              required: true,
              message: '请输入风险描述',
              trigger: 'blur',
            },
          ],
          realname: [
            {
              required: true,
              message: '请输入责任部门',
              trigger: 'blur',
            },
          ],
          sysOrgName: [
            {
              required: true,
              message: '请输入相关部门',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      handleRrd(row, index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.rrdTableList.splice(index, 1)
            delRrd({
              toleid: row.toleid,
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async handleAddRrd(row) {
        row.riskid = this.riskid
        const res = await addRrd(row)
        if (res.code === 1) {
          this.$baseMessage(res.msg, 'success')
        }
      },

      async fetchRrdList(riskId) {
        const res = await getRrdList({
          riskid: riskId,
        })
        this.rrdTableList = res.data.pageInfo.tlist
        for (let i = 0; i < this.rrdTableList.length; i++) {
          this.rrdTableList[i].tableId = i
        }
      },
      async fetchFile(riskId) {
        const res = await getFileList({
          riskId: riskId,
        })
        this.tableData = res.data.data
      },
      newLine() {
        let size = this.rrdTableList.length
        this.rrdTableList.push({
          rtcode: '',
          lowerborder: 0,
          upperborder: 0,
          colorstring: '绿',
          description: '',
          tableId: size,
        })
      },
      handleExecutorSelected(node) {
        this.formData.realname = node[0].realname
        this.formData.createorid = node[0].staffid
      },
      handleDepartmentSelected(node, isCheckBox) {
        if (isCheckBox) {
          let names = ''
          let ids = ''
          node.forEach((item) => {
            names += item.name + ','
            ids += item.id + ','
          })
          names = names.substring(0, names.length - 1)
          ids = ids.substring(0, ids.length - 1)
          this.formData.sysOrgName = names
          this.formData.sysOrgid = ids
        } else {
          this.formData.reportdepartment = node.id
          // this.$set(this.formData, 'orgname', node.name)
          this.formData.orgname = node.name
          this.formData.orgid = node.id
        }
        this.$refs['ruleForm'].clearValidate()
        this.$forceUpdate()
      },
      showEdit(title, row, data) {
        this.dialogFormVisible = true
        // 当从疑点管理发送过来时，data有值
        if (data) {
          this.tableData = [data.attachment]
        }
        if (row) {
          this.riskid = row.risk.riskid
          const { tblOrganiDem, tblStaff, ...other } = row.risk
          this.formData = {
            orgname: tblOrganiDem.orgname,
            realname: tblStaff.realname,
            ...other,
          }
          this.fetchFile(row.risk.riskid)
          this.fetchRrdList(row.risk.riskid)
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          console.dir(userInfo)
          // let orgName = userInfo.linkDetp.orgname
          this.formData.orgname = userInfo.linkDetp.orgname
          this.formData.orgid = userInfo.linkDetp.orgid
          this.formData.realname = userInfo.realname
          this.formData.createorid = userInfo.staffid
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新建'
          this.rrdTableList = []
          createRiskCode().then((res) => {
            this.$set(this.formData, 'risknumber', res.data.autoCode.toString())
          })
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.formData = {
          risknumber: undefined,
          riskname: undefined,
          discovereddate: undefined,
          occureddate: undefined,
          riskeventdescription: undefined,
          realname: undefined,
          createorid: undefined,
          orgname: undefined,
          orgid: undefined,
          sysOrgName: undefined,
          sysOrgid: undefined,
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
        this.rrdTableList = []
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
            delete this.formData.createDate
            riskAdd({
              ...this.formData,
              attids,
              rrds: JSON.stringify(this.rrdTableList),
            }).then((res) => {
              if (res.code == 1) {
                this.$baseMessage('保存成功', 'success')
                // this.formData = data.data.WorkReport
                this.riskid = res.data.risk.riskid
                this.dialogFormVisible = false
              } else {
                this.$baseMessage(data.msg, 'error')
              }

              this.$emit('fetch-data')
              // this.close()
            })
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
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
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
