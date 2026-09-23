<template>
  <!-- 质量评议表 编辑 -->
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
            <el-form-item label="评分表名称" prop="name">
              <el-input
                v-model="postForm.name"
                style="width: 250px"
                placeholder="请输入评分表名称"
              />
            </el-form-item>

            <!--            <el-form-item label="创建人" prop="create">-->
            <!--              <el-input-->
            <!--                v-model="postForm.create"-->
            <!--                class="filter-item"-->
            <!--                :style="{ width: '200px' }"-->
            <!--              />-->
            <!--            </el-form-item>-->
            <!--            <el-form-item label="创建时间" prop="createTime">-->
            <!--              <el-date-picker-->
            <!--                v-model="postForm.createTime"-->
            <!--                type="date"-->
            <!--                placeholder="选择日期"-->
            <!--              ></el-date-picker>-->
            <!--            </el-form-item>-->
          </el-form>
        </el-card>
        <!-- 动态表单区 -->
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
              <el-table-column label="重点内容" #default="{ row, $index }">
                <!-- 动态表单区 -->
                <el-select
                  v-model="row.type"
                  class="filter-item"
                  style="width: 100%"
                  @change="changeSelect(row, $index)"
                >
                  <el-option
                    v-for="item in scoreManageList"
                    :key="item.id"
                    :label="scoreTypeMap[item.type]"
                    :value="item.type"
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
  import {
    addScoreList,
    qualityScoreMeterDetail,
  } from '@/oapi/audit/scoreManage'
  import { qualityScoreMeterUpdate } from '@/oapi/audit/scoreManage'
  import { getList } from '@/api/fwgl/pfgl/pfzdgl'
  import { wwtjyjlrDetail } from '@/oapi/audit/plan'

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
          // 表单
          name: '',
          scoreIds: '',
        },
        scoreManageList: [], // 评分管理列表
        scoreTypeMap: [
          '',
          '审计实施方案制定及执行',
          '审计底稿',
          '审计报告',
          '审计管理系统上线',
          '奖惩事项',
        ], // 评分类型字典表
        rules: {
          topicName: [{ required: true, message: '请输入评分表名称！' }],
          examineType: [{ required: true, message: '请选择评分表类型！' }],
        },
        examineTypes: [
          // 搜索区评分类型列表
          {
            value: 1,
            label: '审计实施方案制定及执行',
          },
          {
            value: 2,
            label: '审计底稿',
          },
          {
            value: 3,
            label: '审计报告',
          },
          {
            value: 4,
            label: '审计管理系统上线',
          },
          {
            value: 5,
            label: '奖惩事项',
          },
        ],
      }
    },
    created() {},
    methods: {
      async showModal(row) {
        // 打开编辑
        this.dialogFormVisible = true
        this.getScoreManageList()
        if (row) {
          this.title = '编辑评分表'
          const res = await qualityScoreMeterDetail({ id: row.id }) // 请求详情接口 回显
          if (res.code === 1) {
            // 详情数据序列化
            const detailDataArrays = Object.entries(res.data.data)
            // this.postForm.name =
            // 然后，有多少数据，回填复制formData中多少项
            detailDataArrays.forEach((item) => {
              this.postForm[item[0]] = item[1]
            })
            this.postForm.name = res.data.data.name ? res.data.data.name : ''
            this.sonList = res.data.data.scores
          }

          // this.scoreTransaction = data.scoreTransaction
          // this.loading = true
          // getDetail({ id: data.scoreTransaction }).then(async (res) => {
          //   // this.postForm = response.data
          //   this.loading = false
          //   if (res && res.data && res.data.length) {
          //     this.postForm.topicName = res.data[0].topicName
          //     this.postForm.examineType = res.data[0].examineType
          //
          //     await this.refreshFirstList()
          //
          //     const tempList = []
          //     res.data.forEach((x) => {
          //       const findObj = tempList.find((y) => y.firstId === x.firstId)
          //       if (findObj) {
          //         if (findObj.sonList) findObj.sonList.push(x)
          //         else findObj.sonList = [x]
          //       } else {
          //         tempList.push({
          //           firstId: x.firstId,
          //           sonList: [x],
          //         })
          //       }
          //     })
          //
          //     this.sonList = tempList
          //   }
          // })
        }
      },
      changeSelect(data, index) {
        console.log('🚀 ~ changeSelect ~ data:', data)
        console.log('🚀 ~ changeSelect ~ index:', index)
        this.scoreManageList.map((v) => {
          if (v.type == data.type) {
            this.sonList[index].id = v.id
          }
          return v
        })
        console.log(
          '🚀 ~ this.sonList[index].id=this.scoreManageList.map ~ this.sonList:',
          this.sonList
        )
      },
      async getScoreManageList() {
        // 获取评分管理列表
        this.loading = true
        const {
          data: { tlist },
          msg,
        } = await addScoreList({})
        if (msg === '成功') {
          this.loading = false
          this.scoreManageList = tlist
        }
        console.log('getScoreManageList')
      },
      handleSave() {
        // 保存
        this.$refs.postForm.validate((valid) => {
          if (!valid) {
            return
          } else if (!this.sonList.length)
            return this.$notify({
              title: '提示',
              message: '请添加重点内容！',
              type: 'error',
              duration: 1500,
            })
          console.log(
            '🚀 ~ this.$refs.postForm.validate ~ this.sonList:',
            this.sonList
          )
          const importentDatas = [] // [{id: 809140, type: 2}, {id: 809098, type: 3}]
          const totalNum = 0
          // for (let i = 0; i < this.scoreManageList.length; i++) {
          //   // 全部评分管理列表
          //   for (let j = 0; j < this.sonList.length; j++) {
          //     if (this.scoreManageList[i].type === this.sonList[j].type) {
          //       importentDatas.push({
          //         id: this.scoreManageList[i].id,
          //         type: this.sonList[j].type,
          //       })
          //     }
          //   }
          // }
          let scoreIds = JSON.parse(JSON.stringify(this.sonList))
            .map((v) => {
              return v.id
            })
            .filter((item) => item !== null)
            .toString()

          qualityScoreMeterUpdate({ ...this.postForm, scoreIds }).then(
            (res) => {
              if (res.code === 1) {
                this.close()
                this.$emit('fetch')
                this.$message({
                  message: '提交成功！',
                  type: 'success',
                })
              }
            }
          )
        })
      },
      // 添加子项
      handleAdd() {
        this.sonList.push({
          id: null,
          type: '',
        })
      },
      removeItem(index) {
        this.sonList.splice(index, 1)
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
      close() {
        this.postForm = {
          topicName: '',
          examineType: '',
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
