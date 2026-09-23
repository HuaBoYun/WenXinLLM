<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        :disabled="allDisabled"
        label-width="125px"
        :model="formData"
        :rules="rules"
      >
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编号" prop="code">
            <el-input
              v-model="formData.code"
              clearable
              placeholder="请填写编号"
              :style="{ width: '348px', height: '30px' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="标题" prop="tatle">
            <el-input
              v-model="formData.tatle"
              clearable
              placeholder="请填写标题"
              :style="{ width: '348px', height: '30px' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建人" prop="staffid" v-show="false">
            <el-input
              v-model="formData.staffid"
              clearable
              disabled
              :style="{ width: '348px', height: '30px' }"
            />
          </el-form-item>
          <el-form-item label="创建人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              disabled
              :style="{ width: '348px', height: '30px' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经验类型" prop="experiencetype">
            <el-select
              v-model="formData.experiencetype"
              placeholder="请选择经验类型"
              :style="{ width: '348px', height: '30px' }"
            >
              <el-option label="审计案例" value="审计案例" />
              <el-option label="审计方法" value="审计方法" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="经验标签" prop="experiencetatle">
            <el-checkbox-group v-model="formData.experiencetatle">
              <el-checkbox v-for="item in lists" :key="item" :label="item">
                {{ item }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="概述" prop="overview">
            <el-input
              v-model="formData.overview"
              clearable
              placeholder="请输入概述"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容" prop="jykcontent">
            <el-input
              v-model="formData.jykcontent"
              clearable
              placeholder="请输入内容"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
              :file-list="fileList"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableDataFile">
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
                  @click="handleDownload(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <!-- <div slot="footer" v-if="!allDisabled">
        <el-button @click="close">取消</el-button>
        <el-button @click="add" type="primary">确定</el-button>
      </div> -->
    <div style="text-align: right; margin-top: 10px" v-if="!allDisabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add()">确 定</el-button>

      <el-button type="primary" @click="ymsubmit" :disabled="btnLoading">
        提 交
      </el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import { download } from '@/api/audit/implement'
  import {
    createSJJYKCode,
    deleteSjjykFile,
    getSjjykInfo,
    sjjykAdd,
    sjjykFileList,
  } from '@/api/workbench/auditTools'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  import store from '@/store'
  const { baseURL } = require('@/config')
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  export default {
    name: 'xxxx',
    components: { Resubmit, ZXPerson },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          realname: '',
          staffid: '',
          code: '',
          experiencetype: '',
          jykcontent: '',
          jykid: '',
          overview: '',
          tatle: '',
          experiencetatle: [],
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
        },
        lists: [
          '财务收支审计',
          '固定资产投资审计',
          '内部控制和风险管理审计',
          '经济责任审计',
          '信息系统审计',
          '境外审计及其他',
          '战略执行',
          '公司治理',
          '投资合作',
          '八项规定',
          '资金信用',
          '核算纳税',
          '资产管理',
          '招标采购',
          '工程项目',
          '薪酬福利',
        ],
        // LIST: [
        //   { id: 1, name: '1' },
        //   { id: 2, name: '2' },
        //   { id: 3, name: '3' },
        // ],
        allDisabled: false,
        dialogFormVisible: false,
        title: '',
        rules: {
          audittype: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          mbcode: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          mbname: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
        },
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        //提交
        ymFromId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: '',
        btnLoading: false,
      }
    },
    // async created() {
    //   this.showMJ = couldMJ()
    //   if (this.showMJ) {
    //     // 获取密级,菜单id
    //     const res = await hasMJ('BaseConfig')
    //     this.menuId = res[0].menuid
    //     // 请求密级下拉数据
    //     const res2 = await getMJ({ rightId: res[0].menuid })
    //     this.MJoption = res2.data
    //   }
    // },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      close() {
        this.formData = {
          realname: '',
          staffid: '',
          code: '',
          experiencetype: '',
          jykcontent: '',
          jykid: '',
          overview: '',
          tatle: '',
          secrectLevelId: '',
          staffScopeIds: '',
          staffScopeNames: '',
          experiencetatle: [],
        }

        this.$bus.$emit('updateMsg', 0)
        this.dialogFormVisible = false
        this.allDisabled = false
        this.tableDataFile = []
        this.fileIds = []
        this.fileList = []
      },
      showEdit(
        title,
        row,
        fromId,
        flowtaskinfoflowid,
        ymFromId,
        status,
        flowType
      ) {
        if (flowType) {
          this.getMJData(flowType)
        }
        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
        this.status = status

        this.dialogFormVisible = true
        const info = JSON.parse(localStorage.getItem('userInfo'))
        this.formData.staffid = info.staffid
        this.formData.realname = info.realname
        if (title === '新建') {
          this.title = '添加'
          this.allDisabled = false
          createSJJYKCode().then((res) => {
            this.$set(this.formData, 'code', res.data.autoCode.toString())
          })
        } else if (title === '修改') {
          this.title = '修改'
          this.getDefaultInfo(row)
          this.getFileList(row.jykid)
          this.allDisabled = false
        } else {
          this.title = '详情'
          this.getDefaultInfo(row)
          this.getFileList(row.jykid)
          this.allDisabled = true
        }
      },
      async getDefaultInfo(row) {
        const info = await getSjjykInfo({ jykid: row.jykid })
        // 保存审批用的密级id
        if (info.data.jyk.secrectLevelId) {
          localStorage.setItem('SPsecrectLevelId', info.data.jyk.secrectLevelId)
        }
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = info.data.jyk[key]
        })
        this.formData.staffid = info.data.jyk.createStaff.staffid
        this.formData.realname = info.data.jyk.createStaff.realname
        this.formData.experiencetatle = info.data.jyk.experiencetatle
          ? info.data.jyk.experiencetatle.split(',')
          : []
      },
      async getFileList(jykid) {
        const res = await sjjykFileList({ jykid })
        //回填上传文件表格
        const arr = res.data.attList
        const arr1 = arr.map((item) => {
          return {
            ...item,
            name: item.attname,
            size: item.attsize,
            createPerson: item.uploader,
          }
        })
        const arr2 = arr.map((res) => {
          return res.attid
        })
        //收集id
        this.fileIds = arr2
        this.tableDataFile = arr1
      },
      add() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const data = await sjjykAdd({
              ...this.formData,
              attids: this.fileIds.toString() || '',
              experiencetatle: this.formData.experiencetatle.toString(),
              experiencetype: this.formData.experiencetype.toString(),
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('fetch-data')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            return false
          }
        })
      },
      async handleDownload(row) {
        const res = await download({ attId: row.attid })
        this.downloadFileByBlob(res, row.name)
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
      },

      handleSuccess(response, file, fileList) {
        if (file.response.result == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = file.response.data
          const arr1 = {
            name: arr.attname,
            size: arr.attsize,
            createPerson: arr.uploader,
            attid: arr.attid,
          }
          this.tableDataFile.push(arr1)

          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.attid)
          })
          this.fileIds = [...this.fileIds, ...fileArr]
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteSjjykFile({ attid: +row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.fileIds.splice(index, 1)
            this.tableDataFile.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      //提交
      async ymsubmit() {
        try {
          this.$refs['elForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {}
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
      },
    },
  }
</script>
