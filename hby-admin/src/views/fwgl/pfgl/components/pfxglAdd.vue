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
          <el-form
            ref="postForm"
            :rules="rules"
            :model="postForm"
            label-width="100px"
          >
            <el-form-item label="评分表类型" prop="examineType">
              <el-select
                v-model="postForm.examineType"
                class="filter-item"
                @change="handleTypeChange"
              >
                <el-option
                  v-for="item in examineTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="归属重点" prop="firstId">
              <el-select
                v-model="postForm.firstId"
                class="filter-item"
                style="width: 100%"
              >
                <el-option
                  v-for="item in firstList"
                  :key="item.id"
                  :label="item.examineEmphasis"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="分值" prop="totalScore">
              <el-input
                v-model="postForm.totalScore"
                type="number"
                class="filter-item"
                :style="{ width: '200px' }"
              />
            </el-form-item>

            <el-form-item label="评分项内容" prop="secondContent">
              <el-input v-model="postForm.secondContent" type="textarea" />
            </el-form-item>
          </el-form>
        </el-card>

        <div class="filter-container" style="margin-top: 25px">
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

          <el-table :data="sonList" :border="true" style="width: 100%">
            <!-- 外部监管考核 -->
            <el-table-column label="评分标准" v-if="postForm.examineType === 1">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.sonGradeCriterion"
                  type="textarea"
                />
              </template>
            </el-table-column>

            <!-- 子单位考核 -->
            <el-table-column label="工作内容" v-if="postForm.examineType === 2">
              <template slot-scope="scope">
                <el-input v-model="scope.row.sonContent" type="textarea" />
              </template>
            </el-table-column>
            <el-table-column label="执行主体" v-if="postForm.examineType === 2">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.sonExecutiveBody"
                  type="textarea"
                />
              </template>
            </el-table-column>
            <el-table-column label="时限" v-if="postForm.examineType === 2">
              <template slot-scope="scope">
                <el-input v-model="scope.row.sonTimeLimit" type="textarea" />
              </template>
            </el-table-column>

            <el-table-column label="分值">
              <template slot-scope="scope">
                <el-input v-model="scope.row.sonScore" type="text" />
              </template>
            </el-table-column>

            <el-table-column label="操作" align="center" width="100px">
              <template slot-scope="scope">
                <el-button type="text" @click="handleMove(scope.$index, 'up')">
                  上移
                </el-button>
                <el-button
                  type="text"
                  @click="handleMove(scope.$index, 'down')"
                >
                  下移
                </el-button>
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
  import { saveOrUpdate, getDetail } from '@/api/fwgl/pfgl/pfxgl'
  import { getList } from '@/api/fwgl/pfgl/pfzdgl'

  export default {
    name: 'pfxglAdd',
    data() {
      return {
        title: '添加评分项',
        loading: false,
        dialogFormVisible: false,

        firstList: [],
        examineTypes: [
          {
            value: 1,
            label: '外部监管考核',
          },
          {
            value: 2,
            label: '子单位考核',
          },
        ],

        postForm: {
          examineType: 1,
          firstId: '',
          totalScore: '',
          secondContent: '',
        },
        sonList: [],
        rules: {
          examineType: [{ required: true, message: '请选择评分表类型！' }],
          firstId: [{ required: true, message: '请选择归属重点！' }],
          totalScore: [{ required: true, message: '请输入分值！' }],
          secondContent: [{ required: true, message: '请输入评分项内容！' }],
        },
      }
    },
    methods: {
      handleTypeChange(v) {
        this.sonList = []

        this.sonList.push({
          sonSort: 0, // 排序

          // 外部考核表
          sonGradeCriterion: '', // 子-评分标准
          // 子评分表
          sonContent: '', // 子-工作内容
          sonExecutiveBody: '', // 子-执行主体
          sonTimeLimit: '', // 子-时限

          sonScore: '', // 子项分值
        })

        this.refreshFirstList()
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.sonList.push({
          sonSort: 0, // 排序

          // 外部考核表
          sonGradeCriterion: '', // 子-评分标准
          // 子评分表
          sonContent: '', // 子-工作内容
          sonExecutiveBody: '', // 子-执行主体
          sonTimeLimit: '', // 子-时限

          sonScore: '', // 子项分值
        })
      },

      async refreshFirstList() {
        // 归属重点列表
        this.postForm.firstId = ''
        if (this.postForm.examineType && this.postForm.examineType !== 0) {
          await getList({ examineType: this.postForm.examineType }).then(
            (res) => {
              if (res && res.data && res.data.tlist) {
                this.firstList = res.data.tlist
              }
            }
          )
        }
      },

      handleMove(index, dir) {
        const curOptionData = this.sonList.splice(index, 1)[0]
        const listData = JSON.parse(JSON.stringify(this.sonList))
        const len = listData.length
        let _index = 0
        if (dir === 'up') {
          _index = index <= 0 ? 0 : index - 1
        } else if (dir === 'down') {
          _index = index >= len ? len : index + 1
        }

        listData.splice(_index, 0, curOptionData)

        this.sonList = listData
      },

      removeItem(index) {
        this.sonList.splice(index, 1)
      },

      fetchData(id) {
        this.loading = true
        getDetail({ id: id }).then((res) => {
          this.loading = false
          if (res && res.data && res.data[0]) {
            this.$set(this, 'postForm', {
              examineType: res.data[0].examineType,
              firstId: res.data[0].firstId,
              totalScore: res.data[0].totalScore,
              secondContent: res.data[0].secondContent,
            })

            this.$set(this, 'sonList', res.data)
          }
        })
      },
      submitForm() {
        this.$refs.postForm.validate((valid) => {
          if (!valid) {
            return
          }

          const p = []
          if (!this.sonList.length) {
            this.$notify({
              title: '提示',
              message: '请添加子项！',
              type: 'error',
              duration: 2000,
            })
            return
          }

          this.sonList.forEach((x, i) => {
            p.push({
              ...x,
              ...this.postForm,
              sonSort: i,
            })
          })

          saveOrUpdate(p).then((res) => {
            this.$notify({
              title: '成功',
              message: '保存成功！',
              type: 'success',
              duration: 2000,
            })
            this.close()
            this.$emit('get')
          })
        })
      },
      async showModal(data) {
        await this.refreshFirstList()
        if (data && data.transactionId) {
          this.fetchData(data.transactionId)
        }
        this.dialogFormVisible = true
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.postForm = {
          examineType: 1,
          firstId: '',
          totalScore: '',
          secondContent: '',
        }
        this.sonList = []
      },
    },
  }
</script>
