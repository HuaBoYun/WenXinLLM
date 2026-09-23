<template>
  <!-- 评议管理 > 评分管理 > 编辑 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <!-- 普通表单区 -->
      <el-form
        ref="postForm"
        :rules="rules"
        :model="postForm"
        label-width="150px"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="评分表类型" prop="examineType">
            <el-select v-model="postForm.type" :style="{ width: '100%' }">
              <el-option
                v-for="item in examineTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分值" prop="totalScore">
            <el-input
              v-model="postForm.score"
              type="number"
              class="filter-item"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              style="width: 100%"
              v-model="postForm.createUser"
              disabled
              placeholder="请输入创建人"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              style="width: 100%"
              v-model="postForm.createTime"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
      <!-- 动态表单区 -->
      <div style="margin-top: 25px">
        <el-button
          type="primary"
          icon="el-icon-plus"
          style="margin-button: 20px"
          size="small"
          @click="handleAdd"
          v-if="footer"
        >
          添加
        </el-button>

        <el-table
          :data="postForm.scoreItems"
          :border="true"
          style="width: 100%"
        >
          <!-- 外部监管考核 -->
          <el-table-column label="评分标准">
            <template slot-scope="scope">
              <el-input
                :disabled="!footer"
                v-model="scope.row.scoreContent"
                type="textarea"
              />
            </template>
          </el-table-column>

          <el-table-column label="分值">
            <template slot-scope="scope">
              <el-input
                :disabled="!footer"
                v-model="scope.row.score"
                type="text"
              />
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center" width="100px">
            <template slot-scope="scope" v-if="footer">
              <el-button type="text" @click="handleMove(scope.$index, 'up')">
                上移
              </el-button>
              <el-button type="text" @click="handleMove(scope.$index, 'down')">
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
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button :loading="loading" type="primary" @click="submitForm">
        保存
      </el-button>
    </template>
  </el-dialog>
</template>
<script>
import { qualityScoreList } from '@/oapi/audit/qualityScore' // 找归属重点列表
import { addScoreUpdate, addScoreDetail } from '@/oapi/audit/scoreManage' // 静态表单详情

export default {
  name: 'pfxglAdd',
  data() {
    return {
      title: '添加评分项',
      loading: false,
      dialogFormVisible: false,

      firstList: [],
      examineTypes: [
        // 固定评分类型下拉选项
        {
          value: 1,
          label: '审计实施方案制订及执行',
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
      postForm: {
        // 编辑表单
        type: 1, // 评分类型
        score: null,
        scoreItems: [],
      },
      sonList: [],
      rules: {
        type: [{ required: true, message: '请选择评分表类型！' }],
        score: [{ required: true, message: '请输入分值！' }],
        // secondContent: [{ required: true, message: '请输入评分项内容！' }],
      },
      footer: true,
    }
  },
  methods: {
    getCurrentDate() {
      return new Date(+new Date() + 8 * 3600 * 1000)
        .toJSON()
        .substr(0, 19)
        .replace('T', ' ')
    },
    getImportentContentList(type) {
      // 获取重点内容数据列表（质量评议列表）
      // qualityScoreList({type: type}).then(res => {
      //   console.log('获取重点内容数据列表', res)
      // })
    },
    async showModal(row, title) {
      // 打开编辑
      this.dialogFormVisible = true
      if (row) {
        // 编辑
        this.postForm.id = row.id
        this.postForm.type = row.type // 编辑中类型回显
        const detailData = await addScoreDetail({ id: row.id })
        this.postForm.score = detailData.data.data.score
        this.postForm.status = detailData.data.data.status
        this.postForm.createTime = detailData.data.data.createTime
        this.postForm.createUser = detailData.data.data.createUser.realname

        this.getImportentContentList(row.type)
        this.postForm.scoreItems = row?.scoreItems || []
      }
      if (title == 'edit') {
        this.title = '编辑'
      } else if (title == 'detail') {
        this.title = '详细'
        this.footer = false
      } else if (title == 'add') {
        this.title = '新增'
        let createUser = JSON.parse(localStorage.getItem('userInfo')).realname
        this.postForm = {
          ...this.postForm,
          createUser: createUser,
          createTime: this.getCurrentDate(),
        }
        this.addPro()
      }
    },
    // 添加子项
    handleAdd() {
      this.postForm.scoreItems.push({
        score: null, // 行分值范围
        scoreContent: '', // 工作内容
      })
    },

    // async refreshFirstList() {
    //   // 归属重点列表
    //   this.postForm.firstId = ''
    //   if (this.postForm.examineType && this.postForm.examineType !== 0) {
    //     await getList({ examineType: this.postForm.examineType }).then(
    //       (res) => {
    //         if (res && res.data && res.data.tlist) {
    //           this.firstList = res.data.tlist
    //         }
    //       }
    //     )
    //   }
    // },

    // 上下移动
    handleMove(index, dir) {
      const curOptionData = this.postForm.scoreItems.splice(index, 1)[0]
      const listData = JSON.parse(JSON.stringify(this.postForm.scoreItems))
      const len = listData.length
      let _index = 0
      if (dir === 'up') {
        _index = index <= 0 ? 0 : index - 1
      } else if (dir === 'down') {
        _index = index >= len ? len : index + 1
      }

      listData.splice(_index, 0, curOptionData)

      this.postForm.scoreItems = listData
    },

    // 删除归属重点项
    removeItem(index) {
      this.postForm.scoreItems.splice(index, 1)
    },
    // 提交表单
    submitForm() {
      this.$refs.postForm.validate((valid) => {
        if (!valid) {
          return
        }

        if (!this.postForm.scoreItems.length) {
          // 判断子项，是不是空
          this.$notify({
            title: '提示',
            message: '请添加子项！',
            type: 'error',
            duration: 2000,
          })
          return
        }
        const p = []
        let totalNum = 0
        this.postForm.scoreItems.forEach((x, i) => {
          // 遍历子项所有，为其添加排序重组
          p.push({
            ...x,
            sort: i,
          })
          if (x.score && x.score !== '') {
            totalNum += Number(x.score)
          }
        })
        if (totalNum > Number(this.postForm.score)) {
          return this.$message.error('评分项分值相加不能大于总分值')
        }
        this.postForm.scoreItems = p
        let params = {
          ...this.postForm,
        }
        delete params['createUser']
        delete params['createTime']

        addScoreUpdate(params).then((res) => {
          // 保存接口
          if (res.code === 1) {
            this.$notify({
              title: '成功',
              message: '保存成功！',
              type: 'success',
              duration: 2000,
            })
            this.close()
            this.$emit('get')
          } else {
            // this.$baseMessage('失败', 'error', 'vab-hey-message-success')
          }
        })
      })
    },
    close() {
      this.dialogFormVisible = false
      this.postForm = {
        examineType: 1,
        score: null,
        scoreItems: [],
      }
      this.sonList = []
      this.footer = true
    },
  },
}
</script>
