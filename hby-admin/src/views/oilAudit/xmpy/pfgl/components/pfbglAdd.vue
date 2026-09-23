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
        <h3>评分表配置</h3>
        <el-card style="margin-top: 20px">
          <el-form
            ref="postForm"
            :model="postForm"
            :rules="rules"
            label-position="left"
            label-width="120px"
          >
            <el-form-item label="评分表名称" prop="topicName">
              <el-input
                v-model="postForm.topicName"
                style="width: 250px"
                placeholder="请输入评分表名称"
              />
            </el-form-item>

            <!-- <el-form-item label="评分表类型" prop="examineType">
              <el-select
                v-model="postForm.examineType"
                class="filter-item"
                placeholder="请选择评分表类型"
                @change="handleTypeChange"
              >
                <el-option
                  v-for="item in examineTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item> -->
          </el-form>
        </el-card>

        <el-card>
          <div>
            <el-button
              class="filter-item"
              size="small"
              type="primary"
              icon="el-icon-plus"
              @click="handleAdd"
            >
              添加重点内容
            </el-button>

            <el-table
              :data="sonList"
              :border="false"
              empty-text="请点击上面的`添加重点内容`进行设置"
              style="width: 100%; margin-top: 15px"
            >
              <el-table-column
                label="重点内容"
                prop="firstId"
                #default="{ row }"
              >
                <el-select
                  v-model="row.firstId"
                  class="filter-item"
                  style="width: 100%"
                  @change="handleFirstChange(row.firstId, row)"
                >
                  <el-option
                    v-for="item in firstList"
                    :key="item.id"
                    :label="item.examineEmphasis"
                    :value="item.id"
                  />
                </el-select>
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
        </el-card>
      </el-row>

      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button
          :loading="loading"
          type="primary"
          icon="el-icon-caret-right"
          @click="handleSave"
        >
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { saveOrUpdate, getDetail, getInfoList } from '@/oapi/fwgl/pfgl/pfbgl'
  import { getList } from '@/api/fwgl/pfgl/pfzdgl'

  export default {
    name: 'pfbglAdd',
    data() {
      return {
        title: '添加评分表',
        loading: false,
        dialogFormVisible: false,
        scoreTransaction: null,
        firstList: [],
        sonList: [],
        postForm: {
          topicName: '',
          examineType: 1,
        },
        rules: {
          topicName: [{ required: true, message: '请输入评分表名称！' }],
          examineType: [{ required: true, message: '请选择评分表类型！' }],
        },
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
      }
    },
    created() {},
    methods: {
      handleSave() {
        this.$refs.postForm.validate((valid) => {
          if (!valid) {
            return
          } else if (!this.sonList || !this.sonList.length)
            return this.$notify({
              title: '提示',
              message: '请添加重点内容！',
              type: 'error',
              duration: 1500,
            })
          else if (this.sonList.some((s) => !s.firstId))
            return this.$notify({
              title: '提示',
              message: '请选择重点内容！',
              type: 'error',
              duration: 1500,
            })

          const p = []

          this.sonList.forEach((son, i) => {
            son.sonList.forEach((item) => {
              p.push({
                sort: i,
                topicName: this.postForm.topicName,
                ...item,
                id: undefined,
              })
            })
          })

          this.$confirm('确实要提交保存吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }).then(() => {
            saveOrUpdate(p, this.scoreTransaction)
              .then((res) => {
                this.$emit('fetch')
                this.close()
              })
              .catch((err) => {})
          })
        })
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        if (!this.postForm.examineType) {
          this.$notify({
            title: '提示',
            message: '请先选择评分表类型！',
            type: 'error',
            duration: 2000,
          })
          return
        }
        this.sonList.push({
          firstId: '',
        })
      },
      removeItem(index) {
        this.sonList.splice(index, 1)
      },
      handleTypeChange(v) {
        this.sonList = []
        this.refreshFirstList()
      },
      async refreshFirstList() {
        // 归属重点列表
        if (this.postForm.examineType && this.postForm.examineType !== 0) {
          this.loading = true
          await getList({ examineType: this.postForm.examineType }).then(
            (res) => {
              if (res && res.data && res.data.tlist) {
                this.firstList = res.data.tlist
              }
            }
          )
          this.loading = false
        }
      },
      showModal(data) {
        const that = this
        this.dialogFormVisible = true
        if (data) {
          this.title = '编辑评分表'
          this.scoreTransaction = data.scoreTransaction
          this.loading = true
          getDetail({ id: data.scoreTransaction }).then(async (res) => {
            // this.postForm = response.data
            this.loading = false
            if (res && res.data && res.data.length) {
              this.postForm.topicName = res.data[0].topicName
              this.postForm.examineType = res.data[0].examineType

              await this.refreshFirstList()

              const tempList = []
              res.data.forEach((x) => {
                const findObj = tempList.find((y) => y.firstId === x.firstId)
                if (findObj) {
                  if (findObj.sonList) findObj.sonList.push(x)
                  else findObj.sonList = [x]
                } else {
                  tempList.push({
                    firstId: x.firstId,
                    sonList: [x],
                  })
                }
              })

              this.sonList = tempList
            }
          })
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.postForm = {
          topicName: '',
          examineType: 1,
        }
        this.sonList = []
        this.scoreTransaction = null
        this.dialogFormVisible = false
      },
      handleFirstChange(v, row) {
        getInfoList({ id: v }).then((res) => {
          if (res && res.code === 200) {
            if (!res.data || !res.data.length) {
              row.firstId = ''
              return this.$notify({
                title: '提示',
                message: '该重点没有评分项，请先去评分项管理添加数据！',
                type: 'error',
                duration: 1500,
              })
            }
            row.sonList = res.data
          }
        })
      },
    },
  }
</script>
