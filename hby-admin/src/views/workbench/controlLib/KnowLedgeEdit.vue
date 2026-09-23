<!--
 * @Date: 2022-01-21 09:06:33
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
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="文章标题" prop="articletitle">
            <el-input v-model.trim="form.articletitle" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文章作者" prop="aruticleauther">
            <el-input v-model.trim="form.aruticleauther" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发布时间" prop="publishtime">
            <!-- <el-input v-model.trim="form.pulishDate" /> -->
            <el-date-picker
              v-model.trim="form.publishtime"
              placeholder="发布时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="articlestatusText">
            <el-select
              v-model="form.articlestatusText"
              placeholder="请选择"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in statusList"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input v-model.trim="form.memo" type="textarea" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="">
            <UEditor
              ref="ueditor"
              v-model="form.articlebody"
              :height="300"
              :templates="templates"
              style="margin-left: -80px"
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
            :before-upload="handleBeforeUpload"
            :show-file-list="false"
            multiple
            :file-list="fileList"
          >
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
            <el-button type="success" slot="tip" @click="handleDelFile">
              删除
            </el-button>
          </el-upload>
          <el-table
            :data="form.attIds"
            @selection-change="handleSelectionChangeFile"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column align="center" label="附件名称" prop="name" />
            <el-table-column align="center" label="文件大小(KB)" prop="size" />
            <el-table-column
              align="center"
              label="创建人"
              prop="createPerson"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleEdit2(row)"
                >
                  下载
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleEdit2(scope.$index)"
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
    <!-- <Department ref="department" @selected="handleDepartmentSelected" /> -->
  </el-dialog>
</template>

<script>
  import {
    mergeOtherarticle,
    selectOtherarticleInfo,
  } from '@/api/workbench/auditTools'
  import UEditor from '@/components/UEditor'

  export default {
    components: {
      UEditor,
    },
    name: 'LcdyEdit',

    data() {
      return {
        baseApi:
          process.env.NODE_ENV === 'development'
            ? '/vab-mock-server/audit'
            : process.env.VUE_APP_BASE_API,
        api: '/fileManage/upload',
        form: {
          articlebody: '',
          articlestatus: '',
          articlestatusText: '',
          articletitle: '',
          aruticleauther: '',
          memo: '',
          publishtime: '',
          attIds: [],
        },
        statusList: [
          { label: '草稿', value: '0' },
          { label: '已发布', value: '1' },
          { label: '已废止', value: '2' },
        ],
        disabled: false,
        templates: [],
        fileList: [],
        // tableData: [],
        rules: {
          articletitle: [
            { required: true, trigger: 'blur', message: '请输入文章标题' },
          ],
          aruticleauther: [
            { required: true, trigger: 'blur', message: '请输入文章作者' },
          ],
          articlestatusText: [
            { required: true, trigger: 'blur', message: '请选择状态' },
          ],
          publishtime: [
            { required: true, trigger: 'blur', message: '请输入发布时间' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
      }
    },
    created() {},
    methods: {
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      async showEdit(row, flag) {
        if (!row) {
          this.disabled = false
          this.title = '添加'
        } else if (row && flag) {
          let res = await selectOtherarticleInfo(row.othartid)
          this.form = {
            articlebody: res.data.tblOtherarticle.articlebody,
            articlestatus: res.data.tblOtherarticle.articlestatus,
            articletitle: res.data.tblOtherarticle.articletitle,
            aruticleauther: res.data.tblOtherarticle.aruticleauther,
            memo: res.data.tblOtherarticle.memo,
            publishtime: res.data.tblOtherarticle.publishtime,
            attIds: [],
          }

          this.disabled = true
        } else {
          let res = await selectOtherarticleInfo(row.othartid)
          this.title = '编辑'
          this.form = {
            articlebody: res.data.tblOtherarticle.articlebody,
            articlestatus: res.data.tblOtherarticle.articlestatus,
            articletitle: res.data.tblOtherarticle.articletitle,
            aruticleauther: res.data.tblOtherarticle.aruticleauther,
            memo: res.data.tblOtherarticle.memo,
            publishtime: res.data.tblOtherarticle.publishtime,
            attIds: [],
          }
          this.disabled = false
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      handleDepartmentSelected(node) {
        const data = node.id ? node : node.checked
        this.form.orgid = data.id
        this.form.orgname = data.text
      },
      save() {
        this.form.articlestatus =
          this.statusList[this.form.articlestatusText].label
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.form.publishtime = this.form.publishtime.substring(0, 11)
            const { msg } = await mergeOtherarticle(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleSuccess(response, file, fileList) {
        file.createPerson = this.createPerson
        file.id = Math.random()
        this.form.attIds.push(file)
      },
      //删除选中的已上传的文件
      handleDelFile() {
        let newArr = this.multipleSelectionFile.map((item) => {
          return item.id
        })
        this.form.attIds = this.form.attIds.filter((item) => {
          return !newArr.includes(item.id)
        })
      },
      handleEdit2(index) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.form.attIds.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleSelectionChangeFile(val) {
        this.multipleSelectionFile = val
      },
    },
  }
</script>
