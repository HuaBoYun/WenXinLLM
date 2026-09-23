<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="800px"
      @close="close"
    >
      <el-row :gutter="24" v-loading="loading">
        <el-card>
          <el-form ref="postForm" :rules="rules" :model="postForm">
            <el-form-item label="题目类型 " prop="quType">
              <el-select
                v-model="postForm.quType"
                :disabled="quTypeDisabled"
                class="filter-item"
                @change="handleTypeChange"
              >
                <el-option
                  v-for="item in quTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="难度等级 " prop="levela">
              <el-select v-model="postForm.levela" class="filter-item">
                <el-option
                  v-for="item in levels"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="归属题库" prop="repoIds">
              <repo-select v-model="postForm.repoIds" :multi="true" />
            </el-form-item>

            <el-form-item label="题目内容" prop="content">
              <el-input v-model="postForm.content" type="textarea" />
            </el-form-item>

            <el-form-item label="试题图片" prop="image">
              <file-upload v-model="postForm.image" />
            </el-form-item>

            <el-form-item label="整题解析" prop="analysis">
              <el-input
                v-model="postForm.analysis"
                :precision="1"
                :max="999999"
                type="textarea"
              />
            </el-form-item>
          </el-form>
        </el-card>

        <div
          v-if="postForm.quType !== 4"
          class="filter-container"
          style="margin-top: 25px"
        >
          <el-button
            class="filter-item"
            type="primary"
            icon="el-icon-plus"
            size="small"
            plain
            @click="handleAdd"
          >
            添加
          </el-button>

          <el-table
            :data="postForm.answerList"
            :border="true"
            style="width: 100%"
          >
            <el-table-column label="是否答案" width="120" align="center">
              <template slot-scope="scope">
                <el-checkbox v-model="scope.row.isRight">答案</el-checkbox>
              </template>
            </el-table-column>

            <el-table-column
              v-if="itemImage"
              label="选项图片"
              width="120px"
              align="center"
            >
              <template slot-scope="scope">
                <file-upload v-model="scope.row.image" />
              </template>
            </el-table-column>

            <el-table-column label="答案内容">
              <template slot-scope="scope">
                <el-input v-model="scope.row.content" type="textarea" />
              </template>
            </el-table-column>

            <el-table-column label="答案解析">
              <template slot-scope="scope">
                <el-input v-model="scope.row.analysis" type="textarea" />
              </template>
            </el-table-column>

            <el-table-column label="操作" align="center" width="100px">
              <template slot-scope="scope">
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="removeItem(scope.$index)"
                />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button :loading="loading" type="primary" @click="submitForm">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  // import { fetchDetail, saveData } from '@/api/qu/qu'
  import { detailTopic, saveTopic } from '@/api/fwgl/xfks/stgl'
  import FileUpload from './../FileUpload'
  import RepoSelect from './../RepoSelect'

  export default {
    name: 'stgl',
    components: { RepoSelect, FileUpload },
    data() {
      return {
        title: '添加试题',
        loading: false,
        dialogFormVisible: false,
        quTypeDisabled: false,
        itemImage: true,

        levels: [
          { value: 1, label: '普通' },
          { value: 2, label: '较难' },
        ],

        quTypes: [
          {
            value: 1,
            label: '单选题',
          },
          {
            value: 2,
            label: '多选题',
          },
          {
            value: 3,
            label: '判断题',
          },
        ],

        postForm: {
          repoIds: [],
          tagList: [],
          answerList: [],
        },
        rules: {
          content: [{ required: true, message: '题目内容不能为空！' }],

          quType: [{ required: true, message: '题目类型不能为空！' }],

          levela: [{ required: true, message: '必须选择难度等级！' }],

          repoIds: [{ required: true, message: '至少要选择一个题库！' }],
        },
      }
    },
    methods: {
      handleTypeChange(v) {
        this.postForm.answerList = []
        if (v === 3) {
          this.postForm.answerList.push({
            isRight: true,
            content: '正确',
            analysis: '',
          })
          this.postForm.answerList.push({
            isRight: false,
            content: '错误',
            analysis: '',
          })
        }

        if (v === 1 || v === 2) {
          this.postForm.answerList.push({
            isRight: false,
            content: '',
            analysis: '',
          })
          this.postForm.answerList.push({
            isRight: false,
            content: '',
            analysis: '',
          })
          this.postForm.answerList.push({
            isRight: false,
            content: '',
            analysis: '',
          })
          this.postForm.answerList.push({
            isRight: false,
            content: '',
            analysis: '',
          })
        }
      },

      // 添加子项
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.postForm.answerList.push({
          isRight: false,
          content: '',
          analysis: '',
        })
      },
      removeItem(index) {
        this.postForm.answerList.splice(index, 1)
      },
      fetchData(id) {
        detailTopic({ id: id }).then((response) => {
          this.postForm = response.data
        })
      },
      submitForm() {
        let rightCount = 0

        this.postForm.answerList.forEach(function (item) {
          if (item.isRight) {
            rightCount += 1
          }
        })

        if (this.postForm.quType === 1) {
          if (rightCount !== 1) {
            this.$message({
              message: '单选题答案只能有一个',
              type: 'warning',
            })

            return
          }
        }

        if (this.postForm.quType === 2) {
          if (rightCount < 2) {
            this.$message({
              message: '多选题至少要有两个正确答案！',
              type: 'warning',
            })

            return
          }
        }

        if (this.postForm.quType === 3) {
          if (rightCount !== 1) {
            this.$message({
              message: '判断题只能有一个正确项！',
              type: 'warning',
            })

            return
          }
        }

        this.$refs.postForm.validate((valid) => {
          if (!valid) {
            return
          }

          saveTopic(this.postForm).then((response) => {
            // this.postForm = response.data
            this.$notify({
              title: '成功',
              message: '试题保存成功！',
              type: 'success',
              duration: 2000,
            })
            this.close()
            this.$emit('get')
          })
        })
      },
      showModal(data) {
        if (data) {
          if (typeof data.id !== 'undefined') {
            this.quTypeDisabled = true
            this.fetchData(data.id)
          }
        }
        this.dialogFormVisible = true
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.quTypeDisabled = false
        this.postForm = {}
        // this.postForm.content = ''
        // this.postForm.oriPrice = ''
        // this.postForm.image = null
        // this.postForm.answerList = []
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      save() {},
    },
  }
</script>
