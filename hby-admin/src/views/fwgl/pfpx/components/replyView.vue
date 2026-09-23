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
        :rules="rules"
        :model="formData"
        size="mini"
      >
        <el-col :span="8">
          <el-form-item label="创建人" prop="nickNamess">
            <el-input
              v-model="formData.nickNamess"
              clearable
              placeholder="请选择创建人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.createTime"
              placeholder="创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="知识类型" prop="knowledgeType">
            <el-select
              v-model="formData.knowledgeType"
              clearable
              disabled
              placeholder="知识类型"
              style="width: 100%"
            >
              <el-option :value="1" label="劳动用工"></el-option>
              <el-option :value="2" label="知识产权"></el-option>
              <el-option :value="3" label="投融资"></el-option>
              <el-option :value="4" label="法律尽调"></el-option>
              <el-option :value="5" label="法律纠纷"></el-option>
              <el-option :value="6" label="其他"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容" prop="content">
            <el-input
              v-model="formData.content"
              clearable
              type="textarea"
              :rows="3"
              placeholder="请输入内容"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="回复记录" prop="">
            <el-table ref="multipleTable" :data="list" style="width: 100%">
              <el-table-column
                label="回复人"
                prop="replyNames"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="replyTime"
                label="回复时间"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="replyContent"
                label="回复内容"
                align="center"
              >
                <template slot-scope="scope">
                  {{ setWenZi(scope.row.replyContent) }}
                  <el-button
                    v-if="scope.row.replyContent.length > 11"
                    @click="getDeatail(scope.row.replyContent)"
                    type="mini"
                  >
                    查看详细
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="footer">
          <el-form-item label="回复内容" prop="replyContent">
            <el-input
              v-model="formData.replyContent"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入回复内容"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="footer">
          <el-form-item label="回复人" prop="replyNames">
            <el-input
              v-model="formData.replyNames"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="footer">
          <el-form-item label="回复时间" prop="replyTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.replyTime"
              placeholder="回复时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div> -->
    <template #footer v-if="footer">
      <el-button @click="close">关闭</el-button>
      <el-button @click="addReply" type="primary">确定</el-button>
    </template>

    <el-dialog
      title="详细内容"
      :visible.sync="dialogVisible"
      width="50%"
      :modal="false"
      @close="closeReplyContent"
    >
      <span>{{ replyContent }}</span>
    </el-dialog>
  </el-dialog>
</template>

<script>
  import { getMsgById, replyMsg, getBaseData } from '@/api/fwgl/pfpx'
  import { deleteFile, uploadApi } from '@/api/fwgl/zzxx'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { formatDate } from '@/utils'
  const token = store.getters['user/token']
  export default {
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          replyNames: '',
          replyName: '',
        },
        rules: {
          nickName: [
            { required: true, message: '请输入昵称', trigger: 'blur' },
          ],
          createTime: [
            { required: true, message: '请输入创建时间', trigger: 'blur' },
          ],
          content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
          replyContent: [
            { required: true, message: '请输入回复内容', trigger: 'blur' },
          ],
        },
        footer: true,
        dialogFormVisible: false,
        title: '新增',
        tableData: [],
        fileList: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
        list: [],
        ID: '',
        replyContent: '',
        dialogVisible: false,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      closeReplyContent() {
        this.replyContent = ''
        this.dialogVisible = false
      },
      getDeatail(text) {
        this.replyContent = text
        this.dialogVisible = true
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        // this.formData = this.$options.data().formData
        this.dialogFormVisible = true
        this.fileList = []
        const info = JSON.parse(localStorage.getItem('userInfo'))

        if (row) {
          getBaseData({
            id: row.id,
          }).then((res) => {
            this.$set(this.formData, 'nickName', res.data[0].nickName)
            this.$set(this.formData, 'nickNamess', res.data[0].nickNames)
            this.$set(this.formData, 'questionId', res.data[0].id)
            this.$set(this.formData, 'replyNames', info.realname)
            this.$set(this.formData, 'replyName', info.staffid)
            this.$set(this.formData, 'createTime', res.data[0].createTime)
            this.$set(this.formData, 'knowledgeType', res.data[0].knowledgeType)
            this.$set(this.formData, 'content', res.data[0].content)
            if (res.data.length > 1) {
              res.data.splice(0, 1)

              this.list = res.data
            }
          })
          getMsgById({ id: row.id }).then((res) => {
            if (!res.data) {
              this.ID = ''
              this.formData.id = ''
            } else {
              this.$set(this.formData, 'replyContent', res.data.replyContent)
              this.$set(this.formData, 'id', res.data.id)
              // this.formData.replyContent = res.data.replyContent
              // this.formData.id = res.data.id
            }
          })
        }
        if (title == 'edit') {
          this.title = '回复'
          this.formData.replyTime = formatDate(new Date())
          this.footer = true
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          this.formData = this.$options.data().formData
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.footer = true
      },
      addReply() {
        this.$refs.ruleForm.validate(async (valid) => {
          if (valid) {
            this.formData.createTime = formatDate(this.formData.createTime)
            const res = await replyMsg({
              ...this.formData,
              parentId: this.formData.questionId,
            })

            this.close()
            this.$emit('fentch-data')
          } else {
            return false
          }
        })
      },
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      setWenZi(text) {
        let end = ''
        if (text.length > 11) {
          end = '...'
        }
        let texts = text.substring(0, 10)

        return texts + end
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
