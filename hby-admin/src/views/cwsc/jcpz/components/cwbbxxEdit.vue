<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="24">
          <el-form-item label="父级分类" prop="pid">
            <el-select
              v-model="formData.pid"
              placeholder="请选择父级分类"
              style="width: 100%"
            >
              <el-option
                v-for="item in fatherTypeList"
                :key="item.fid"
                :label="item.handtext"
                :value="item.fid"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="子级分类" prop="handtext">
            <el-input v-model="formData.handtext" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24" style="height: 29px">
          <el-form-item label="分布式" prop="dataoriginflag">
            <el-select
              v-model="formData.dataoriginflag"
              placeholder="分布式"
              style="width: 100%"
            >
              <el-option
                v-for="item in typeList"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { getCwbbxxType, saveCwbbxx, getCwbbxxDetail } from '@/api/cwsc'

  export default {
    components: {},
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,

        tableData: [],
        formData: {
          // dataoriginflag: '',
          handtext: '',
          fid: '',
          pid: '',
        },
        footer: true,
        rules: {
          pid: [
            {
              required: true,
              message: '请选择父级分类',
              trigger: 'blur',
            },
          ],
          // dataoriginflag: [
          //   {
          //     required: true,
          //     message: '请选择分布式',
          //     trigger: 'blur',
          //   },
          // ],
          handtext: [
            {
              required: true,
              message: '请选择子级分类',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
        typeList: [
          {
            key: 0,
            value: '本级产生',
          },
          {
            key: 1,
            value: '上级下发',
          },
          {
            key: 2,
            value: '下级上报',
          },
          {
            key: 3,
            value: '本级产生已上报下发',
          },
        ],
        fatherTypeList: [],
      }
    },

    methods: {
      // 附件列表 删除附件
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        await deleteFileInfo({ id: row.attid })
        this.tableData = list
      },
      handlePreview(file) {},
      // 附件上传成功
      handleSuccess(file) {
        if (file.result == 200) {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      // 获取当前修改数据的详情
      async showEdit(row, title) {
        this.dialogFormVisible = true
        // 获取父级分类
        const res = await getCwbbxxType()
        this.fatherTypeList = res.data
        if (row) {
          const res = await getCwbbxxDetail({ fid: row.fid.toString() })
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data[key]
          })
          //   res.data.data.supervisionWorkUnitName
          // this.formData.transactor = res.data.data.transactor
          // this.formData.transactorName = res.data.data.transactorName
          // this.formData.remark = res.data.data.remark
          // this.formData.fileIds = res.data.data.fileIds
          // this.tableData =
          //   res.data.file && res.data.file.length ? res.data.file : []
          // this.formData.id = res.data.data.id
          // this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.transactorName = resL.realname
          this.formData.transactor = resL.staffid
          this.formData = {
            ...this.formData,
            // createdUser: resL,
            // createdTime: this.getCurrentDate(),
          }
        }
      },
      // 关闭清除form表单内容
      close() {
        this.formData = {
          // dataoriginflag: '',
          handtext: '',
          fid: '',
          pid: '',
        }
        this.tableData = []
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await saveCwbbxx({
              ...params,
            })
            if (res && res.code == 1) {
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
            this.close()
            this.$emit('fetchData')
            this.loading = false
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
