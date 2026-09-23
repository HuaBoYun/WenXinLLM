<template>
  <!-- 建设项目投资完成情况 新增/修改 -->
  <div>
    <el-row>
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="编号" prop="hzname">
            <el-input
              v-model="formData.hzname"
              clearable
              placeholder="请输入编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="orgname">
            <el-input
              v-model="formData.orgname"
              placeholder="请选择填报单位"
              :style="{ width: '78%' }"
              readonly
              disabled
            />
            <el-button
              type="primary"
              size="mini"
              style="margin-left: 15px"
              @click="$refs.audiTree.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类别" prop="fl">
            <el-select style="width: 100%" v-model="formData.fl" disabled>
              <el-option label="三类" value="三类"></el-option>
              <el-option label="四类" value="四类"></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="hzbz">
            <el-input
              v-model="formData.hzbz"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="6"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" class="flex-right" v-if="!formDisabled">
          <el-button type="success" @click.native="handleAdd()">新增</el-button>
        </el-col>
        <el-table :data="list">
          <el-table-column align="center" label="序号" type="index" />
          <el-table-column align="center" label="合同编号" prop="htbh">
            <template #default="{ row }">
              <el-button
                :disabled="false"
                type="text"
                @click="handleDetail(row)"
              >
                {{ row.htbh }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="工程或费用名称"
            prop="gchfymc"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="填报单位"
            prop="tbdwName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="实施单位"
            prop="ssdw"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="批复概算投资"
            prop="pfgstzje"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="合同金额"
            prop="htje"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="结算金额"
            prop="jsje"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="投资节超（概算-实际完成）"
            prop="tzjc"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="投资节超情况说明"
            prop="tzjcqksm"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="项目类别"
            prop="fl"
            show-overflow-tooltip
          />
          <el-table-column
            label="操作"
            fixed="right"
            align="center"
            width="160"
            v-if="!formDisabled"
          >
            <template #default="{ row, $index }">
              <el-button
                type="text"
                @click="handleEdit(row)"
                :disabled="formDisabled"
              >
                修改
              </el-button>
              <el-button
                type="text"
                :disabled="formDisabled"
                @click.native="handleDelete(row, $index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div style="text-align: right; margin-top: 20px" v-if="!formDisabled">
        <el-button @click="close">取消</el-button>
        <el-button @click="submit" type="primary">确定</el-button>
        <el-button type="primary" @click="ymsubmit" :disabled="btnLoading">
          提交
        </el-button>
      </div>
      <jsxmtzwcqkEdit ref="edit" @fetchData="fetchData" />
    </el-row>

    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
    <SelectDepartment ref="audiTree" @submit="handleUnitSelected" />
  </div>
</template>

<script>
  import {
    jsxmtzwcqkList,
    jsxmtzwcqkDel,
    flVerify,
    saveOrUpdate,
    jsxmtzwcqkhzHzDetail,
  } from '@/oapi/audit/plan'
  import jsxmtzwcqkEdit from '@/views/oilAudit/gcgl/components/jsxmtzwcqkEdit.vue'
  import store from '@/store'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  const token = store.getters['user/token']
  const { baseURL } = require('@/config')
  export default {
    name: 'jsxmtzwcqk',
    components: {
      jsxmtzwcqkEdit,
      Resubmit,
      SelectDepartment,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/fileManage/upload',
        headers: { token: token },
        title: '新增',
        list: [], // 列表数据,
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        localKey: 'oilAudit-gcgl-jsxmtzwcqk-search',
        tableKey: 'oilAudit-gcgl-jsxmtzwcqk-list',
        searchMore: true,
        xmtzDialog: false,
        formDisabled: true,
        rules: {},
        formData: {
          cjr: '',
          cjsj: '',
          fl: '',
          hzbz: '',
          hzname: '',
          hzid: '',
          orgname: '',
          orgid: '',
        },
        tableDataUpload: [],
        // 流程相关
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
        jsxmtzwcqkid: '',
        orgid: '',
        btnLoading: false,
      }
    },
    created() {},
    methods: {
      async ymsubmit() {
        try {
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      async showEdit(
        title,
        row,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        console.log('🚀 ~ row:', row)
        // 打开编辑
        this.xmtzDialog = true
        // 流程相关
        this.fromId = row
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (title == 'edit') {
          this.title = '编辑'
          this.formDisabled = false
        } else if (title == 'detail') {
          this.title = '详细'
          this.formDisabled = true
        }
        this.getDetail(row)
      },
      async getDetail(row) {
        // 获取数据
        this.listLoading = true
        const { data } = await jsxmtzwcqkhzHzDetail({
          hzid: row,
        })
        this.listLoading = false
        Object.assign(this.formData, data)
        this.list = data.listJsxmTzwcqk || []
      },
      async handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      async handleAdd() {
        this.$refs['edit'].showEdit(null, 'add', this.formData.fl)
      },
      fetchData(row, type) {
        // row 新建 1 提交后返回值
        if (type == 'add') {
          this.list.push(row)
        } else {
          this.list.map((v, i) => {
            if (v.jsxmtzwcqkid == row.jsxmtzwcqkid) {
              this.$set(this.list, i, row)
            }
            return v
          })
        }
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, 'detail')
      },
      async handleDelete(row, index) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.$message.success('操作成功！')
          this.list.splice(index, 1)
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDeleteFile(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableDataUpload
            list = list.filter((item) => item.attid != row.attid)
            this.tableDataUpload = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },

      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
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
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },

      handleSuccess(file) {
        // 父编辑上传回显
        if (file.result == '200') {
          let list = this.tableDataUpload
          list.push(file.data)
          this.tableDataUpload = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      close() {
        this.formData = this.formData = {
          orgid: '',
          cjr: '',
          cjsj: '',
          fl: '',
          hzbz: '',
          hzname: '',
          hzid: '',
          orgname: '',
          orgid: '',
        }
        this.list = []
        this.xmtzDialog = false
        this.formDisabled = true
        this.$bus.$emit('updateMsg', 0)
      },
      async submit() {
        let ids = this.list
          .map((v) => {
            return v.jsxmtzwcqkid
          })
          .toString()
        let params = { ...this.formData, jsxmtzwcqkids: ids }
        delete params.cjr
        delete params.cjsj
        delete params.listJsxmTzwcqk
        const { data, msg, result } = await saveOrUpdate(params)
        if (result == 500) return this.$message.error(msg)
        this.$baseMessage('保存成功', 'success')
        this.$emit('fetchData')
      },
      handleUnitSelected(node) {
        this.formData.orgname = node.label
        this.formData.orgid = node.id
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
  .mb30 {
    margin-bottom: 30px;
  }
  .flex-right {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 10px;
  }
</style>
