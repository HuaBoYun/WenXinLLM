<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-28 10:48:58
 * @FilePath: /hb-admin/src/views/setting/system/components/LcdyEdit.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      label-width="80px"
      :model="form"
      :rules="rules"
      :disabled="disabled"
    >
      <el-row :gutter="14">
        <el-col :span="12">
          <el-form-item label="文件编号" prop="rulecode">
            <el-input v-model.trim="form.rulecode" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文件名称" prop="rulename">
            <el-input v-model.trim="form.rulename" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文文号" prop="rulenumber">
            <el-input v-model.trim="form.rulenumber" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文部门" prop="publishorg">
            <el-input v-model.trim="form.publishorg" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="效力级别" prop="effectivelevel">
            <el-select
              style="width: 100%"
              v-model="form.effectivelevel"
              placeholder="请选择"
            >
              <el-option label="法律" value="法律"></el-option>
              <el-option label="行政法规" value="行政法规"></el-option>
              <el-option label="司法解释" value="司法解释"></el-option>
              <el-option
                label="部门章规及其他规范性文件"
                value="部门章规及其他规范性文件"
              ></el-option>
              <el-option label="地方法规规章" value="地方法规规章"></el-option>
              <el-option label="其他" value="其他"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时效性" prop="timeliness">
            <el-select
              style="width: 100%"
              v-model="form.timeliness"
              placeholder="请选择"
            >
              <el-option label="现行有效" value="现行有效"></el-option>
              <el-option label="征求意见稿" value="征求意见稿"></el-option>
              <el-option label="已失效" value="已失效"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文日期" prop="publishdate">
            <el-date-picker
              style="width: 100%"
              v-model="form.publishdate"
              type="date"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效日期" prop="takeeffecttime">
            <el-date-picker
              style="width: 100%"
              v-model="form.takeeffecttime"
              type="date"
              value-format="yyyy-MM-dd"
              format="yyyy-MM-dd"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入人" prop="enteringperson">
            <el-input :disabled="true" v-model="form.enteringperson" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入时间" prop="enteringtime">
            <el-input :disabled="true" v-model="form.enteringtime" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="摘要" prop="summaryinfo">
            <el-input
              type="textarea"
              :autosize="{ minRows: 4 }"
              placeholder="请输入摘要"
              v-model="form.summaryinfo"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="bodyinfo">
            <UEditor
              ref="ueditor"
              v-model="form.bodyinfo"
              :height="300"
              :templates="templates"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :action="baseApi + api"
            :on-success="handleSuccess"
            :show-file-list="false"
            multiple
            :headers="headers"
            :file-list="fileList"
            :before-upload="handleBeforeUpload"
          >
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
            <!-- <el-button type="success" slot="tip" @click="handleDelFile">
              删除
            </el-button> -->
          </el-upload>
          <el-table
            :data="fileTableList"
            @selection-change="handleSelectionChangeFile"
          >
            <!-- <el-table-column type="selection" width="55"></el-table-column> -->
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
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDown(row)"
                >
                  下载
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleEdit2(row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" v-if="!disabled" @click="save">确 定</el-button>
    </template>
    <Department ref="department" @selected="handleDepartmentSelected" />
  </el-dialog>
</template>

<script>
  import UEditor from '@/components/UEditor'

  import { deleteLawFile, download } from '@/api/audit/implement'
  import { addOutList, getOutDetails } from '@/api/workbench/auditTools'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { parseTime } from '@/utils/index'
  import Department from '@/views/setting/system/components/Department.vue'

  export default {
    name: 'LcdyEdit',
    components: { Department, UEditor },
    data() {
      return {
        fileTableList: [],
        headers: {
          token: store.getters['user/token'],
        },
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        form: {
          rulecode: '',
          rulename: '',
          innruletype: '',
          rulenumber: '',
          status: '',
          publishorg: '',
          publishdate: '',
          bodyinfo: '',
          attIds: [],
        },
        disabled: false,
        templates: [],
        fileList: [],
        // tableData: [],
        rules: {
          rulecode: [
            { required: true, trigger: 'blur', message: '请输入文件编号' },
          ],
          rulename: [
            { required: true, trigger: 'blur', message: '请输入文件名称' },
          ],
          rulenumber: [
            { required: true, trigger: 'blur', message: '请输入发文文号' },
          ],
          publishorg: [
            { required: true, trigger: 'blur', message: '请输入发文部门' },
          ],
          effectivelevel: [
            { required: true, trigger: 'blur', message: '请选择效力级别' },
          ],
          timeliness: [
            { required: true, trigger: 'blur', message: '请选择时效性' },
          ],
          publishdate: [
            { required: true, trigger: 'blur', message: '请选择发文日期' },
          ],
          takeeffecttime: [
            { required: true, trigger: 'blur', message: '请选择生效日期' },
          ],
          enteringperson: [
            { required: true, trigger: 'blur', message: '请输入录入人' },
          ],
          enteringtime: [
            { required: true, trigger: 'blur', message: '请输入录入时间' },
          ],
          summaryinfo: [
            { required: true, trigger: 'blur', message: '请输入摘要' },
          ],
          bodyinfo: [
            { required: true, trigger: 'blur', message: '请输入内容' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
      }
    },
    created() {},
    methods: {
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row, flag) {
        if (!row) {
          this.disabled = false
          this.title = '添加'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.enteringpersonName = userInfo.realname
          this.form.enteringperson = userInfo.username
          this.form.enteringtime = parseTime(new Date(), '{y}-{m}-{d}')
          this.fileTableList = []
        } else if (row && flag) {
          console.dir(row)
          let res = await getOutDetails({
            outrulid: row.outrulid,
          })
          this.form = {
            ...res.data.data,
          }
          this.disabled = true
          this.fileTableList = res.data.attList
          this.form.takeeffecttime = parseTime(
            this.form.takeeffecttime,
            '{y}-{m}-{d}'
          )
          this.form.enteringtime = parseTime(
            this.form.enteringtime,
            '{y}-{m}-{d}'
          )
          this.form.publishdate = parseTime(
            this.form.publishdate,
            '{y}-{m}-{d}'
          )
        } else {
          console.dir(row)
          let res = await getOutDetails({
            outrulid: row.outrulid,
          })
          // let res = await selectInnerRuleInfo(row.innrulid)

          console.log('编辑')
          this.title = '编辑'
          this.form = {
            ...res.data.data,
          }
          this.form.outerId = this.form.outrulid
          this.form.takeeffecttime = parseTime(
            this.form.takeeffecttime,
            '{y}-{m}-{d}'
          )
          this.form.enteringtime = parseTime(
            this.form.enteringtime,
            '{y}-{m}-{d}'
          )
          this.form.publishdate = parseTime(
            this.form.publishdate,
            '{y}-{m}-{d}'
          )
          this.disabled = false
          this.fileTableList = res.data.attList
        }
        this.dialogFormVisible = true
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      handleDepartmentSelected(node) {
        console.log('handleDepartmentSelected', node)
        const data = node.id ? node : node.checked
        this.form.orgid = data.id
        this.form.orgname = data.text
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      async save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let attids = ''
            this.fileTableList.forEach((item) => {
              attids += item.attid + ','
            })
            attids = attids.substring(0, attids.length - 1)
            this.form.attIds = attids
            delete this.form.tblOrganization
            let res = await addOutList(this.form)
            this.form.outerId = res.data.data.outrulid
            this.dialogFormVisible = false
            this.$emit('fetch-data')
            this.$message.success('操作成功')
            // const { msg } = await mergeInnerRule(this.form)
            // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            // this.close()
          }
        })
      },
      handleSuccess(response, file, fileList) {
        this.fileTableList.push(response.data)
        console.dir(this.fileTableList)
      },
      //删除选中的已上传的文件
      // handleDelFile() {
      //   let newArr = this.multipleSelectionFile.map((item) => {
      //     return item.id
      //   })
      //   this.form.attIds = this.form.attIds.filter((item) => {
      //     return !newArr.includes(item.id)
      //   })
      // },
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
      async handleEdit2(row) {
        console.dir(row)
        let list = this.fileTableList
        list = list.filter((item) => item.attid != row.attid)
        this.fileTableList = list
        await deleteLawFile({ attId: row.attid })
      },
      handleSelectionChangeFile(val) {
        this.multipleSelectionFile = val
      },
    },
  }
</script>
